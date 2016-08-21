package mabit.exchange;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import mabit.dispatcher.IEvent;
import mabit.exchange.PriceSize.PriceComparator;
import mabit.marketdata.Quote;
import mabit.oms.exchange.IExchangeInterface;
import mabit.oms.order.IInstrument;
import mabit.oms.order.Order;
import mabit.oms.order.OrderRequest;
import mabit.oms.order.OrderState;
import mabit.oms.order.Side;
import mabit.time.ITimeManager;

public class OneSideOrderBook  implements IExchangeInterface {

private LinkedHashMap<Double, PriceSize> lastBack;
private Quote lastQuote;
	private LinkedHashMap<Double, PriceSize> lastLay;
	
	private final ITimeManager timeManager;
	private final IInstrument instrument;
	
	
	private final SortedMap<Double, List<SimOrder>> orderMap;
	private final SortedMap<Double, PriceSize> priceMap;

	
	private LinkedHashMap<Double, PriceSize> postImpactMap;

	//private boolean seeOwnTrades;
	
	private final Map<Long, SimOrder> allSimOrders = new HashMap<Long,SimOrder>();
	
	// TODO setup log4j
//	private static final Logger Log = Logger.getLogger(OneSideOrderBook.class);
	
	public OneSideOrderBook(ITimeManager timeManager,
		IInstrument instrument,
		Quote last
		//boolean seeOwnTrades
		) {
		this.timeManager = timeManager;
		this.instrument = instrument;

		this.lastQuote = last;
		this.lastBack = TradingUtils.priceSizesByAmount(last.getPriceSizes(Side.BACK));
		this.lastLay = TradingUtils.priceSizesByAmount(last.getPriceSizes(Side.LAY));
		this.postImpactBack = this.lastBack;
		this.postImpactLay = this.lastLay;
		
		this.buyMap = new TreeMap<Double, List<SimOrder>>(Collections.reverseOrder());
		this.layMap = new TreeMap<Double, List<SimOrder>>();
		this.backImpactedAmount = new TreeMap<Double, MutablePriceSize>(Collections.reverseOrder());
		this.layImpactedAmount = new TreeMap<Double, MutablePriceSize>();
	}
	
	public void init() {
		String myBeanStr = null;
		try {
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			ObjectName beanName = null;
			
			 myBeanStr = "com.martin.oms.exchg.simulator:name=" + getClass().getSimpleName() + ",exchg=" + selection.getExchange() + ",selection=" +  selection.getPath().replace("?", "");
	         beanName = new ObjectName(myBeanStr);
	         mbs.registerMBean(this, beanName);
	         if(Log.isDebugEnabled())
	        	 Log.debug("Registered MBean " + myBeanStr);

		} catch(Exception ex) {
			Log.error("Failed to register MBean "+ myBeanStr, ex);
		}

	}
	
	public String[] getLiveSimOrders() {
		
		String[] simOrderValues = new String[allSimOrders.size()];
		int i=0;
		for(SimOrder order : allSimOrders.values()) {
			simOrderValues[i++] = order.toString();
		}
		return simOrderValues;
	}
	
	/**
	 * A way to force an execution via JMX
	 */
	public void exec(long id, double amount, double price) {
		SimOrder simOrder = allSimOrders.get(id);
		if(simOrder != null)
			simOrder.exec(amount, price);
	}
	
	@Override
	public ExchangeSource getExchangeSource() {
		return null;
	}
	
	public IExchangeSelection getSelection() {
		return selection;
	}

	@Override
	public void send(Order order) {
		
		if(Log.isTraceEnabled())
			Log.trace("Sending:" + order);
		
		if(suspended) {
			sendAck(order, RequestStatus.FAILURE, "Suspended");
		} else {
			sendAck(order,RequestStatus.SUCCESS, "On Market");
			create(order);	
		}
	}
	
	
		
	private void sendAck(Order order, RequestStatus status, String msg) {
		post(new ExchangeEventValue(timeManager.getTime(), 
				order, 
				null, 
				status, 
				(status == RequestStatus.SUCCESS)? OrderStatus.ON_MARKET : OrderStatus.REJECTED,
				null, 
				msg,
				"SIMULATOR" + order.getId()
			)
		);
	}
	
	private SimOrder create(Order ord) {
		SimOrder order = new SimOrder(ord, adjustedQueueAmount(ord.getPrice(),ord.getSide().opposite()));
		addSimOrderToOrderMap(order);
		
		order.executeAgainstLastQuote(false);
		addToImpactMap(order.getOrderData().getSide().opposite(), 
				order.getOrderData().getPrice(), 
				order.remainingAmount()
			);
		
		//if(order.getExecAmount() == 0)
		updatePostImpactQuote();
		
		return order;
	}
	
	
	public boolean hasImpact() {
		return !layMap.isEmpty() || 
			!buyMap.isEmpty() || 
			!layImpactedAmount.isEmpty() ||
			!backImpactedAmount.isEmpty();
	}
	
	public ISelectionPrice getImpactedSelectionPrice() {
		return (hasImpact())? 
			new SelectionPriceValue(selection, 
				new QuoteValues(
					Lists.newArrayList(postImpactBack.values()), 
					Lists.newArrayList(postImpactLay.values())
				),
				(last != null)? last.isSuspended() : false
			) :
			last;
	}
	
	private List<PriceSize> adjustedPriceSize(List<PriceSize> priceSizes, Side side) {
		
		Map<Double,MutablePriceSize> priceSizesMap = new HashMap<Double,MutablePriceSize>(10);
		
		List<PriceSize> allPriceSizes = new ArrayList<PriceSize>(20);
		allPriceSizes.addAll(priceSizes);
		//if(seeOwnTrades)
		//allPriceSizes.addAll(ordersToPricesSizes(side));
		
		allPriceSizes.addAll(impactMap(side).values());
		
		// Total up all the amounts at the diferrent price levels
		for(PriceSize priceSize : allPriceSizes) {
			MutablePriceSize totalPriceSize = priceSizesMap.get(priceSize.getPrice());
			if(totalPriceSize == null) {
				priceSizesMap.put(priceSize.getPrice(), new MutablePriceSize(priceSize));
			} else {
				totalPriceSize.addSize(priceSize.getSize());
			}
		}

		// remove levels with less than 0
		List<Double> removeLevels = Lists.newLinkedList();
		for(MutablePriceSize priceSize : priceSizesMap.values()) {
			if(priceSize.getSize() <= 0.0)
				removeLevels.add(priceSize.getPrice());
		}
		for(Double removeLevel : removeLevels) {
			priceSizesMap.remove(removeLevel);
		}
		// and then sort
		List<PriceSize> finalPriceSizes = new ArrayList<PriceSize>(priceSizesMap.values());
		Collections.<PriceSize>sort(finalPriceSizes, PriceComparator.priceComparator(side));
		
		if(Log.isTraceEnabled()) {
			Log.trace("Side->" + side);
			Log.trace("PriceSizes->" + priceSizes);
			//Log.trace("Orders->" + ordersToPricesSizes(side) );
			Log.trace("Impact->" + impactMap(side).values() );
			Log.trace("Final->" + finalPriceSizes);
		}
		
		
		return finalPriceSizes;
	}

	
	/*private List<IPriceSize> ordersToPricesSizes(Side side) {
		
		List<IPriceSize> priceSizes = Lists.newArrayList();
		SortedMap<Double, List<SimOrder>> orderMap = orderMap(side);
		for(Map.Entry<Double, List<SimOrder>> entry : orderMap.entrySet()) {
			double amount = 0.0;
			for(SimOrder simOrder : entry.getValue()) {
				amount += simOrder.getOrderData().getAmount();
				amount += simOrder.getExecAmount();
			}
			priceSizes.add(new PriceSizeValue(entry.getKey(), amount)  );
		}
		return priceSizes;
	}*/
	
	private SortedMap<Double, List<SimOrder>> orderMap(Side side) {
		return (side == Side.LAY)? layMap : buyMap;
	}
	
	private void addSimOrderToOrderMap(SimOrder simOrder) {
		Map<Double, List<SimOrder>> map = orderMap(simOrder.getOrderData().getSide());
		Double price = simOrder.getOrderData().getPrice();
		List<SimOrder> simOrders = map.get(price);
		if(simOrders == null) {
			simOrders = Lists.newArrayList();
			map.put(price, simOrders);
		}
		simOrders.add(simOrder);
	}

	private double adjustedQueueAmount(double price, Side side) {
		
		PriceSize priceSize = (side == Side.LAY)? 
			lastLay.get(price) : lastBack.get(price);
		
		double amount = (priceSize != null)?
			priceSize.getSize() : 0.0;
			
		//amount += outstandingOrderAmount(price,side);
		amount += impactAmount(price, side);
		
		return Math.max(amount,0);
	}
	
	private double impactAmount(double price, Side side) {
		PriceSize amount = (side == Side.LAY)? 
			layImpactedAmount.get(price) : backImpactedAmount.get(price);
			
		return (amount != null)? amount.getSize() : 0.0;
	}
	
	/*private double outstandingOrderAmount(double price, Side side) {
		List<SimOrder> orders =
			(side == Side.LAY)? layMap.get(price) : backMap.get(price);
		
		double amount = 0.0;
		if(orders != null) {
			for(SimOrder ord : orders) {
				amount += ord.remainingAmount();
			}
		}
		return amount;
	}*/
	


	@Override
	public void cancel(Order order) {
		SimOrder simOrder = allSimOrders.get(order.getId());
		if(simOrder != null) {
			simOrder.cancel();
		} else {
			cancelAck(order,RequestStatus.FAILURE);
		}

	}
	
	private void removeSimOrder(SimOrder simOrder) {
		//Log.info("Removing Order:" + simOrder);
		Map<Double,List<SimOrder>> orders = 
			(simOrder.getOrderData().getSide() == Side.LAY)? layMap : buyMap;
		
		Double price = simOrder.getOrderData().getPrice();
		List<SimOrder> ordersForPrice = orders.get(price);
		if(ordersForPrice != null) {
			ordersForPrice.remove(simOrder);
			if(ordersForPrice.isEmpty())
				orders.remove(price);
		}
		
		double unExecAmount = simOrder.getOrderData().getAmount() - simOrder.getExecAmount();
		addToImpactMap(simOrder.getOrderData().getSide().opposite(), price, -1*unExecAmount);
	}
	
	private void cancelAck(Order order, RequestStatus status) {
		post(new ExchangeEventValue(timeManager.getTime(), 
				order, 
				null, 
				status, 
				(status == RequestStatus.SUCCESS)? OrderStatus.CANCELLED : OrderStatus.ON_MARKET,
				null, 
				OrderStatus.CANCELLED.toString(),
				"SIMULATOR" + order.getId()
			)
		);
	}

	@Override
	public void onEvent(IEvent event) {
		
		if(Log.isTraceEnabled())
			Log.trace("Received Event:" + event);
		
		if(event instanceof IMarketQuoteEvent) {
			IMarketQuoteEvent mktQuoteEvt = (IMarketQuoteEvent) event;
			suspended = mktQuoteEvt.isSuspended();
			ISelectionPrice newPrice = mktQuoteEvt.getSelectionPrice(selection);
			lastBack = TradingUtils.priceSizesByAmount(newPrice.getPriceSizes(Side.BACK));
			lastLay = TradingUtils.priceSizesByAmount(newPrice.getPriceSizes(Side.LAY));
			last = newPrice;
			updatePostImpactQuote();
		}  
		
		for(SimOrder simOrder : allSimOrders.values()) {
			simOrder.onEvent(event);
		}

	}
	
	private void updatePostImpactQuote() {
		if(hasImpact()) {
			postImpactBack = TradingUtils.priceSizesByAmount(adjustedPriceSize(last.getPriceSizes(Side.BACK), Side.BACK));
			postImpactLay = TradingUtils.priceSizesByAmount(adjustedPriceSize(last.getPriceSizes(Side.LAY), Side.LAY));
		}
	}
	
	public LinkedHashMap<Double, PriceSize> getSideImpactedLevels(Side side) {
		return (side == Side.BACK)? postImpactBack : postImpactLay;
	}
	
	private SortedMap<Double,MutablePriceSize> impactMap(Side side) {
		return (side == Side.LAY)? layImpactedAmount : backImpactedAmount;
	}
	
	private void addToImpactMap(Side impactSide, double price, double amount) {
		SortedMap<Double,MutablePriceSize> impactMap = impactMap(impactSide);
		
		MutablePriceSize priceSize = impactMap.get(price);
		if(priceSize == null) {
			impactMap.put(amount, new MutablePriceSize(price, amount));
			
		} else {
			priceSize.addSize(amount);
			if(priceSize.getSize() <= 0) {
				impactMap.remove(price);
			} 
		}
			
	}
	
	public class SimOrder {

		private final Order order;
		private double queuePos;
		private double execAmount;
		private OrderState ordStatus;

		public SimOrder(Order order, double queuePos) {
			this(order,queuePos,0);
		}
		public SimOrder(Order order, double queuePos, int execAmount) {
			this.order = order;
			this.queuePos = queuePos;
			this.execAmount = execAmount;
			this.ordStatus = OrderState.ON_MARKET;
			allSimOrders.put(order.getOrderId(),this);
			//addToImpactMap(order.getSide().opposite(), order.getPrice(), order.getAmount());
		}
		
		public Order getOrderData() {
			return order;
		}
		
		public String toString() {
			StringBuilder buff = new StringBuilder(order.toString());
			buff.append(", queuePos=" + queuePos);
			return buff.toString();
		}
		
		public double getExecAmount() {
			return execAmount;
		}


		public boolean isFullyFilled() {
			return execAmount >= order.getQ();
		}

		public void cancel() {
			ordStatus = OrderStatus.CANCELLED;
			removeSimOrder(this);
			cancelAck(order,RequestStatus.SUCCESS);
			updatePostImpactQuote();
		}
		
		public OrderStatus getOrderStatus() {
			return ordStatus;
		}
		
		public double remainingAmount() {
			return (!ordStatus.isTerminal())? order.getAmount() - execAmount : 0.0;
		}
		
		public void onEvent(IEvent event) {
			if(event instanceof IExchangeTradeEvent) {
				handleTrade((IExchangeTradeEvent) event);	
				
			} else if(event instanceof IMarketQuoteEvent) {
				handleQuote((IMarketQuoteEvent) event);
			}
		}
		
		private void handleQuote(IMarketQuoteEvent quote) {
			executeAgainstLastQuote(true);
			updatePostImpactQuote();
		}
		
		
		public void executeAgainstLastQuote(boolean quoteChanged) {
			// if we are done with this trade then return with 0 taken from the
			// trade size
			if(execAmount >= order.getAmount() || ordStatus.isTerminal())
				return;
			
			//Side orderSide = order.getSide();
			Side execSide = order.getSide();//orderSide.opposite();
			for(PriceSize quoteEntry : getSideImpactedLevels(execSide).values()) {
				double quotePrice = quoteEntry.getPrice();
				double quoteSize = quoteEntry.getSize();
				
				double limitPrice = order.getPrice();
				if(execSide.isMoreOrSameAggressiveThan(limitPrice, quotePrice)) {
					double execQty = Math.min(order.getAmount() - execAmount,quoteSize);

					if(Log.isTraceEnabled())
						Log.trace("simOrdID=" + order.getId() +
								" simQty=" + order.getAmount() +
								" simExecQty=" + execAmount +
								" new ExecQty=" + execQty
						); 

					if(execQty > 0) {
						exec(execQty, quotePrice);
						addToImpactMap((quoteChanged)? execSide.opposite() : execSide, (quoteChanged)? limitPrice : quotePrice, -1*execQty);
						if(isFullyFilled()) {
							return;
						}
					}
				} else {				
					if(Log.isDebugEnabled())
						Log.debug("limitPrice " + limitPrice + " not more aggressive than " + quotePrice + " - exiting");
					return;
				}
			}
			
		}
		
		

		private void handleTrade(IExchangeTradeEvent exchangeTradeEvt) {

			IExchangeTrade exchangeTrade = exchangeTradeEvt.getTrade();
			
			if(Log.isDebugEnabled())
				Log.debug("Handling Trade:" + exchangeTrade);
			
			// if we are done with this trade then return with 0 taken from the
			// trade size
			if(execAmount >= order.getAmount() || ordStatus.isTerminal())
				return;
						
			double tradeAmount = exchangeTrade.getAmount();
			double price = exchangeTrade.getPrice();
			
			boolean tradeIsMoreAgressive = (order.getSide() == Side.BACK)?
				price > order.getPrice() : price < order.getPrice();	

			if (tradeIsMoreAgressive) {
				queuePos = 0;
			} else if(! NumberUtils.isEqual(price, order.getPrice())) {
				if(Log.isDebugEnabled())
					Log.debug("trade " + exchangeTrade + " not at right price to execute " + order);
				return;
			}
			

			double tradeSize = 0;
			double newQueuePos = queuePos - tradeAmount;
			if(Log.isDebugEnabled())
				Log.debug("OrdID:" + order.getId() + "-> queue pos=" + newQueuePos);

			if (newQueuePos < 0) { // we're getting done!

				tradeSize = Math.abs( 
					// The queue has gone negative so newQueuePos got executed
					Math.min(-newQueuePos, 
					//
					Math.min(order.getAmount() - execAmount, 
						tradeAmount)
					) 
				);

			}
			queuePos = newQueuePos;

			if(tradeSize > order.getAmount() - execAmount) {
				tradeSize = order.getAmount() - execAmount;
			}
			
			if(tradeSize > 0) {
				if(Log.isDebugEnabled()) 
					Log.debug("tradeAmount=" + tradeAmount + " execQty now=" + execAmount);
				
				exec(tradeSize, price);
				//addToImpactMap(order.getSide().opposite(), order.getPrice(), -1*tradeSize);
			}

			return;
		}
		
		private void exec(double amount, double price) {
			execAmount += amount;
			if(execAmount >= order.getAmount())
				ordStatus = OrderStatus.FILLED;
			
			if(ordStatus.isTerminal())
				removeSimOrder(this);
			
			/*Map<Double,MutablePriceSize> impactMap = impactMap(impactSide);
			
			MutablePriceSize priceSize = impactMap.get(price);
			if(priceSize == null) {
				impactMap.put(amount, new MutablePriceSize(price, -1*amount));
			} else {
				priceSize.addSize(-1*amount);
			}*/
			
			updatePostImpactQuote();
			
			
			post(new ExchangeEventValue(timeManager.getTime(), 
				order, 
				new ExecValue(System.nanoTime(), 
					timeManager.getTime(), 
					amount, price), 
				RequestStatus.SUCCESS, 
				ordStatus,
				"EXEC", 
				"EXEC " + amount + "@" + price, 
				"SIMULATOR" + order.getId())
			);
			
		}
	
	}
	
	public static class Factory implements ISelectionExchangeFactory {


		public Factory(){}
		
		@Override
		public ISelectionExchangeSimulator create(ITimeManager timeManager,
				IExchangeSelection selection,
				ISelectionPrice last) {
			
			OneSideOrderBook selExchgSim =  new OneSideOrderBook(timeManager,selection,last);
			selExchgSim.init();
			return selExchgSim;
		}
		
	}

	@Override
	public void send(OrderRequest order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancel(Order order) {
		// TODO Auto-generated method stub
		
	}
	

}

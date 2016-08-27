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

import org.apache.log4j.Logger;

import mabit.dispatcher.IEvent;
import mabit.marketdata.Quote;
import mabit.marketdata.QuoteLine;
import mabit.oms.exchange.IExchangeInterface;
import mabit.oms.order.IInstrument;
import mabit.oms.order.Order;
import mabit.oms.order.OrderState;
import mabit.oms.order.Side;
import mabit.time.ITimeManager;
import mabit.util.TradingUtils;

public class OneSideOrderBook  {
	
	private Quote lastQuote;
	private final ITimeManager timeManager;
	private final IInstrument instrument;
	private final SortedMap<Double, List<SimOrder>> orderMap;
	private final List<QuoteLine> oneSideQuote;
	
	//private LinkedHashMap<Double, QuoteLine> postImpactMap; //kesse

	//private boolean seeOwnTrades;
	
	private final Map<Long, SimOrder> allSimOrders = new HashMap<Long,SimOrder>();
	
	// TODO setup log4j
	private static final Logger Log = Logger.getLogger(OneSideOrderBook.class);
	
	public OneSideOrderBook(ITimeManager timeManager,
		IInstrument instrument,
		Quote lastQuote,
		Side side
		
		//boolean seeOwnTrades
		) {
		this.timeManager = timeManager;
		this.instrument = instrument;

		this.lastQuote = lastQuote;
		this.oneSideQuote = lastQuote.getOneSide(side);
		
		orderMap = (side.isBuy()) ? 
				new TreeMap<Double, List<SimOrder>>(Collections.reverseOrder())
				: new TreeMap<Double, List<SimOrder>>();
	}
	
	public void init() {
		String myBeanStr = null;
		try {
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			ObjectName beanName = null;
			
			 myBeanStr = "com.martin.oms.exchg.simulator:name=" + getClass().getSimpleName() + ",selection=" +  instrument.getName();
	         beanName = new ObjectName(myBeanStr);
	         mbs.registerMBean(this, beanName);
	         if(Log.isDebugEnabled())
	        	 Log.debug("Registered MBean " + myBeanStr);

		} catch(Exception ex) {
			Log.error("Failed to register MBean "+ myBeanStr, ex);
		}

	}
	
	private boolean isSuspended() { return false; }
	
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
	public void exec(long id, double qty, double price) {
		SimOrder simOrder = allSimOrders.get(id);
		if(simOrder != null){}
			//simOrder.exec(qty, price);
	}
	
//	@Override
//	public ExchangeSource getExchangeSource() {
//		return null;
//	}
	
	public IInstrument getInstrument() {
		return instrument;
	}
/*
	@Override
	public void send(Order order) {
		
		if(Log.isTraceEnabled())
			Log.trace("Sending:" + order);
		
		if(isSuspended()) {
			sendAck(order, AckNackEvent.RequestResult.FAILLURE, "Suspended", OrderState.REJECTED);
		} else {
			sendAck(order,AckNackEvent.RequestResult.SUCCESS, "On Market", OrderState.ON_MARKET);
			create(order);	
		}
	}
	
	
		
	private void sendAck(Order order, AckNackEvent.RequestResult status, String message, OrderState orderState) {
	
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
		
		PriceQty priceSize = (side == Side.SELL)? 
			lastLay.get(price) : priceMap.get(price);
		
		double amount = (priceSize != null)?
			priceSize.getQty() : 0.0;
			
		//amount += outstandingOrderAmount(price,side);
		amount += impactAmount(price, side);
		
		return Math.max(amount,0);
	}
	
	private double impactAmount(double price, Side side) {
		PriceQty amount = (side == Side.SELL)? 
			layImpactedAmount.get(price) : backImpactedAmount.get(price);
			
		return (amount != null)? amount.getQty() : 0.0;
	}
	
	/*private double outstandingOrderAmount(double price, Side side) {
		List<SimOrder> orders =
			(side == Side.SELL)? layMap.get(price) : backMap.get(price);
		
		double amount = 0.0;
		if(orders != null) {
			for(SimOrder ord : orders) {
				amount += ord.remainingAmount();
			}
		}
		return amount;
	}*/
	


}

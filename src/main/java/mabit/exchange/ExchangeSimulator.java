package mabit.exchange;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import mabit.data.instruments.IInstrument;
import mabit.data.marketdata.IMarketDataService;
import mabit.data.marketdata.Quote;
import mabit.data.marketdata.Trade;
import mabit.dispatcher.Event.QuoteEvent;
import mabit.dispatcher.Event.TradeEvent;
import mabit.dispatcher.*;
import mabit.exchange.ExchangeUpdate.Listener;
import mabit.exchange.ExchangeUpdate.RequestResult;
import mabit.oms.order.Exec;
import mabit.oms.order.Order;
import mabit.oms.order.OrderState;
import mabit.oms.order.Side;
import mabit.time.ITimeManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExchangeSimulator implements IExchangeInterface, IEventListener {
	private final Map<Long,SimOrder> orderMap = new HashMap<>();
	
	private final Multimap<IInstrument, SimOrder> instrumentMap = LinkedListMultimap.create();
	private final IMarketDataService mds;
	private final List<ExchangeUpdate.Listener> updateListeners = Lists.newArrayList();
	private final ITimeManager tm;

	public ExchangeSimulator() {
		this(
				ServiceProvider.INSTANCE.getService(IDispatcher.class),
				ServiceProvider.INSTANCE.getService(ITimeManager.class),
				ServiceProvider.INSTANCE.getService(IMarketDataService.class));
	}

	public ExchangeSimulator(IDispatcher dispatcher, ITimeManager tm, IMarketDataService mds) {
		dispatcher.register(EventType.QUOTE, this);
		this.tm = tm;
		this.mds = mds;
	}
	 
	@Override
	public void send(Order order) {
		Quote quote = mds.getLastQuote(order.getIntrument());
		if(quote != null && quote.isValid()) {
			SimOrder simo = new SimOrder(order, quote);
			sendOrderUpdate(simo, "New Order", simo.onQuote(quote));
			orderMap.put(order.getOrderId(), simo);
            instrumentMap.put(order.getIntrument(),simo);
			
		} else {
			sendMessageReject(
					order.getIntrument(),
					order.getSide(),
					order.getOrderId(), 
					OrderState.REJECTED,
					"No Valid Quote: " + ((quote!=null)?quote.toString():"null"));
		}			
	}
	
	private void sendOrderUpdate(SimOrder simo, String msg, Exec exec) {
		ExchangeUpdate update= new ExchangeUpdate(
						simo.getOrder().getIntrument(),
						simo.getSide(),
						simo.getOrder().getOrderId(),
						RequestResult.SUCCESS,
						simo.getOrdStatus(),
						(exec!=null)? Collections.singletonList(exec) :null,
						msg,
						tm.getTime());
		for(ExchangeUpdate.Listener l : this.updateListeners) {
			l.onExchangeUpdate(update);
		}

	}
		
	private void sendMessageReject(IInstrument instrument, Side side, Long orderId, OrderState orderState, String msg) {
		ExchangeUpdate update = new ExchangeUpdate(
				instrument,
				side,
				orderId,
				RequestResult.FAILLURE,
				orderState,
				null,
				msg,
				tm.getTime());
		for(ExchangeUpdate.Listener l : this.updateListeners) {
			l.onExchangeUpdate(update);
		}
				
	}

	@Override
	public void cancel(Order order) {
		SimOrder simo = orderMap.get(order.getOrderId());
	
		if(simo==null) {
			//TODO handle error
		} else if(simo.getOrdStatus().isTerminal()){
			sendMessageReject(order.getIntrument(),order.getSide(),order.getOrderId(),simo.getOrdStatus(),"Order already terminal");
			// TODO send error message
		} else {
			simo.setOrderStatus(OrderState.REJECTED);
			sendOrderUpdate(simo, "Order Cancelled", null);
			removeFromLiveOrder(simo);
		}
		// TODO Auto-generated method stub
	}
	

	@Override
	public void onEvent(IEvent event) {
		switch(event.getEventType()) {
			case QUOTE:	onQuote(((QuoteEvent)event).getQuote()); break;
			case TRADE:	onTrade(((TradeEvent)event).getTrade()); break;
		}
	}

	private void onQuote(Quote quote) {
        instrumentMap.get(quote.getInstrument()).forEach(e -> e.onQuote(quote));
//		for(SimOrder order : )
		
	}
	private void onTrade(Trade trade) {
		
	}
	
	private void removeFromLiveOrder(SimOrder simo) {
		if(simo.getOrdStatus().isTerminal()) {
			orderMap.remove(simo.getOrder().getOrderId());
			instrumentMap.remove(simo.getOrder().getIntrument(), simo);
		} else {
			//TODO: throw error
		}
	}
	@Override
	public Priority getPriority() {
		return Priority.HIGH;
	}


	@Override
	public void register(Listener listener) {
		this.updateListeners.add(listener);
		
	}

}

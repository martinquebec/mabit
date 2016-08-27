package mabit.exchange;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.eventbus.EventBus;

import mabit.dispatcher.Event.QuoteEvent;
import mabit.dispatcher.Event.TradeEvent;
import mabit.dispatcher.IEvent;
import mabit.dispatcher.IEventListener;
import mabit.exchange.OrderUpdate.RequestResult;
import mabit.marketdata.MarketDataService;
import mabit.marketdata.Quote;
import mabit.marketdata.Trade;
import mabit.oms.order.Exec;
import mabit.oms.order.IInstrument;
import mabit.oms.order.Oms;
import mabit.oms.order.Order;
import mabit.oms.order.OrderState;
import mabit.time.ITimeManager;

public class ExchangeSimulator implements IExchangeInterface, IEventListener {
	Map<Long,SimOrder> orderMap = new HashMap<>();
	
	Multimap<IInstrument, SimOrder> instrumentMap = LinkedListMultimap.create();
	MarketDataService mds;
	Oms oms;
	ITimeManager tm;

	
	public void ExchangeSimulator(EventBus bus) {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void send(Order order) {
		Quote quote = mds.getLastQuote(order.getIntrument());
		if(quote != null && quote.isValid()) {
			SimOrder simo = new SimOrder(order, quote);
			sendOrderUpdate(simo, "New Order", simo.onQuote(quote));
			orderMap.put(order.getOrderId(), simo);
			
		} else {
			sendMessageReject(
					order.getOrderId(), 
					OrderState.REJECTED,
					"No Valid Quote: " + ((quote!=null)?quote.toString():"null"));
		}			
	}
	
	private void sendOrderUpdate(SimOrder simo, String msg, Exec exec) {
		oms.onOrderUpdate(
				new OrderUpdate(
						simo.getOrder().getOrderId(),
						msg,
						RequestResult.SUCCESS,
						simo.getOrdStatus(),
						(exec!=null)?Arrays.asList(exec):null,
						tm.getTime()));
	}
		
	private void sendMessageReject(Long orderId, OrderState orderState, String msg) {
		oms.onOrderUpdate(
			new OrderUpdate(
				orderId,
				msg,
				RequestResult.FAILLURE,
				orderState,
				null,
				tm.getTime()));
				
	}

	@Override
	public void cancel(Order order) {
		SimOrder simo = orderMap.get(order.getOrderId());
	
		if(simo==null) {
			//TODO handle error
		} else if(simo.getOrdStatus().isTerminal()){
			sendMessageReject(order.getOrderId(),simo.getOrdStatus(),"Order already terminal");
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
			case QUOTE:	onQuote(((QuoteEvent)event).getPayLoad()); break;	
			case TRADE:	onTrade(((TradeEvent)event).getPayLoad()); break;	
		}
	}
	public void onQuote(Quote quote) {
//		for(SimOrder order : )
		
	}
	public void onTrade(Trade trade) {
		
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

}

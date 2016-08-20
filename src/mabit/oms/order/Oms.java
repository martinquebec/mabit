package mabit.oms.order;

import java.util.IdentityHashMap;
import java.util.Map;

import mabit.exchange.IExchangeInterface;

public class Oms {
	final Map<Integer,Order> liveOrder;
	final Map<Integer,Order> closeOrder;
	IExchangeInterface exchange;
	
	
	public Oms(IExchangeInterface exchange) {
		this.exchange = exchange;
		liveOrder = new IdentityHashMap<Integer, Order>();
		closeOrder = new IdentityHashMap<Integer, Order>();
	}
	
	public void sendOrder(OrderRequest order) {
		//exchange.send(order);
		
	}
	

}

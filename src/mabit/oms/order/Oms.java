package mabit.oms.order;

import java.util.IdentityHashMap;
import java.util.Map;

import mabit.oms.exchange.IExchangeInterface;

public class Oms {
	final Map<Integer,Order> liveOrder;
	final Map<Integer,Order> closeOrder;
	IExchangeInterface exchange;
	private static int id=0;
	
	public Oms(IExchangeInterface exchange) {
		this.exchange = exchange;
		liveOrder = new IdentityHashMap<Integer, Order>();
		closeOrder = new IdentityHashMap<Integer, Order>();
	}
	
	public void sendOrder(OrderRequest requestOrder) {
		exchange.send(requestOrder);
		Order order = new Order(requestOrder, id++, OrderState.PENDING);
		liveOrder.put(order.getOrderId(), order);
	}
	

}

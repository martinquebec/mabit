package mabit.oms.order;

import java.util.IdentityHashMap;
import java.util.Map;

import mabit.oms.exchange.IExchangeInterface;

public class Oms {
	final Map<Long,Order> liveOrder;
	final Map<Long,Order> closeOrder;
	IExchangeInterface exchange;
	private static Long id=0l;
	
	public Oms(IExchangeInterface exchange) {
		this.exchange = exchange;
		liveOrder = new IdentityHashMap<Long, Order>();
		closeOrder = new IdentityHashMap<Long, Order>();
	}
	
	public void sendOrder(OrderRequest requestOrder) {
		exchange.send(requestOrder);
		Order order = new Order(requestOrder, id++, OrderState.PENDING_NEW);
		liveOrder.put(order.getOrderId(), order);
	}
	

}

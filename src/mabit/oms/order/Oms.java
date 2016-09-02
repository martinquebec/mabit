package mabit.oms.order;

import java.util.HashMap;
import java.util.Map;

import mabit.exchange.ExchangeUpdate;
import mabit.exchange.IExchangeInterface ;

public class OmsOneName {
	final Map<Long,Order> liveOrder;
	final Map<Long,Order> closeOrder;
	IExchangeInterface exchange;
	private static Long id=0l;
	
	public OmsOneName(IExchangeInterface exchange) {
		this.exchange = exchange;
		liveOrder = new HashMap<Long, Order>();
		closeOrder = new HashMap<Long, Order>();
	}
	
	public void sendOrder(OrderRequest requestOrder) {
		Order order = new Order(requestOrder, id++, OrderState.PENDING_NEW);
		exchange.send(order);
		liveOrder.put(order.getOrderId(), order);
	}

	public void onExchangeUpdate(ExchangeUpdate update) {
		Order order = liveOrder.get(update.getOrderId());
		if(order == null) {
			order = liveOrder.get(update.getOrderId())44444444444444444444444444444444444
					;
		}
		// TODO Auto-generated method stub
		
	}
	

}

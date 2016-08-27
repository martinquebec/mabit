package mabit.oms.exchange;

import mabit.oms.order.Order;
import mabit.oms.order.OrderRequest;

public interface IExchangeInterface {
	public void send(Order order);
	
	public void cancel(Order order);
}

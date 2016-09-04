package mabit.exchange;

import mabit.oms.order.Order;

public interface IExchangeInterface {
	public void send(Order order);
	public void cancel(Order order);
	public void register(ExchangeUpdate.Listener listener);
	

}

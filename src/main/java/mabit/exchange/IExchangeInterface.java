package mabit.exchange;

import mabit.oms.order.Order;

public interface IExchangeInterface {
	void send(Order order);
	void cancel(Order order);
	void register(ExchangeUpdate.Listener listener);
	

}

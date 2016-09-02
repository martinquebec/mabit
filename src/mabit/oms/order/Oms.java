package mabit.oms.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import mabit.dispatcher.Dispatcher;
import mabit.dispatcher.Event.OrderEvent;
import mabit.exchange.ExchangeUpdate;
import mabit.exchange.ExchangeUpdate.RequestResult;
import mabit.exchange.IExchangeInterface ;
import mabit.time.ITimeManager;

public class Oms implements ExchangeUpdate.Listener{
	private static final Logger Log = Logger.getLogger(Oms.class);


	final Map<Long,Order> orders;
	final Dispatcher dispatcher;
	final ITimeManager tm;
	IExchangeInterface exchange;
	private static Long id=0l;

	public Oms(IExchangeInterface exchange, Dispatcher bus, ITimeManager tm) {
		this.exchange = exchange;
		this.dispatcher = bus;
		this.tm = tm;
		orders = new HashMap<Long, Order>();
		exchange.register(this);
	}

	public void sendOrder(OrderRequest requestOrder) {
		Order order = new Order(requestOrder, id++, OrderState.PENDING_NEW);
		exchange.send(order);
		orders.put(order.getOrderId(), order);
		postOrderEvent(order,"New Order",null);	
	}
	
	public void cancelOrder(Order order) {
		exchange.cancel(order);
		order.setState(OrderState.PENDING_CANCEL);
	}
	
	private void postOrderEvent(Order order, String message,List<Exec> execs) {
		dispatcher.post(
				new OrderEvent(
						tm.getTime(),
						new OrderUpdate(
								order.getOrderId(),
								message,
								OrderState.PENDING_NEW,
								execs,
								tm.getTime())));		
	}

	public void onExchangeUpdate(ExchangeUpdate update) {
		Order order = orders.get(update.getOrderId());
		if(order != null) {
			order.addExecs(update.getExecs());
			order.state = update.getOrderState();
			if(update.getLastRequestResult()== RequestResult.FAILLURE) {
				Log.error("Received faillure message [\"" + update.getMessage() + " for order "  + order.toShortString() + ", update " + update.toString() );
			} else {
				Log.info("Received update message [\"" + update.getMessage() + " for order "  + order.toShortString() + ", update " + update.toString() );
			}
			dispatcher.post(new OrderEvent(tm.getTime(),new OrderUpdate(update)));
		} else {
			Log.error("No order found for update " + update);
		}
	}
}

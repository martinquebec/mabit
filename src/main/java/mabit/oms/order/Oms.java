package mabit.oms.order;

import mabit.dispatcher.Event.OrderEvent;
import mabit.dispatcher.IDispatcher;
import mabit.dispatcher.ServiceProvider;
import mabit.exchange.ExchangeUpdate;
import mabit.exchange.ExchangeUpdate.RequestResult;
import mabit.exchange.IExchangeInterface;
import mabit.time.ITimeManager;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Oms implements ExchangeUpdate.Listener{
	private static final Logger Log = Logger.getLogger(Oms.class);


	private final Map<Long,Order> orders;
	private final IDispatcher dispatcher;
	private final ITimeManager tm;
	private final IExchangeInterface exchange;
	private static Long id= 0L;

	public Oms() {
		this(
				ServiceProvider.INSTANCE.getService(IExchangeInterface.class),
				ServiceProvider.INSTANCE.getService(IDispatcher.class),
				ServiceProvider.INSTANCE.getService(ITimeManager.class));
	}

	public Oms(IExchangeInterface exchange, IDispatcher dispatcher, ITimeManager tm) {
		this.exchange = exchange;
		this.dispatcher = dispatcher;
		this.tm = tm;
		orders = new ConcurrentHashMap<>();
		exchange.register(this);
	}

	public void sendOrder(OrderRequest requestOrder) {
		Order order = new Order(requestOrder, id++, OrderState.PENDING_NEW);
		orders.put(order.getOrderId(), order);
		exchange.send(order);
		postOrderEvent(order,"New Order");
	}
	
	public void cancelOrder(Order order) {
		exchange.cancel(order);
		order.setState(OrderState.PENDING_CANCEL);
	}
	
	private void postOrderEvent(Order order, String message) {
		dispatcher.put(
				new OrderEvent(
						tm.getTime(),
						new OrderUpdate(order.copy(),
								message,
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
			dispatcher.put(new OrderEvent(tm.getTime(),new OrderUpdate(order.copy(),update.getMessage(),tm.getTime())));
		} else {
			Log.error("No order found for update " + update);
		}
	}
}

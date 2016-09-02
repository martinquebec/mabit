package mabit.oms.order;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.common.eventbus.EventBus;

import mabit.dispatcher.Event.OrderEvent;
import mabit.exchange.ExchangeUpdate;
import mabit.exchange.ExchangeUpdate.RequestResult;
import mabit.exchange.IExchangeInterface ;
import mabit.time.ITimeManager;

public class Oms {
	private static final Logger Log = Logger.getLogger(Oms.class);


	final Map<Long,Order> orders;
	final EventBus bus;
	final ITimeManager tm;
	IExchangeInterface exchange;
	private static Long id=0l;

	public Oms(IExchangeInterface exchange, EventBus bus, ITimeManager tm) {
		this.exchange = exchange;
		this.bus = bus;
		this.tm = tm;
		orders = new HashMap<Long, Order>();
	}

	public void sendOrder(OrderRequest requestOrder) {
		Order order = new Order(requestOrder, id++, OrderState.PENDING_NEW);
		exchange.send(order);
		orders.put(order.getOrderId(), order);
		bus.post(
				new OrderEvent(
						tm.getTime(),
						new OrderUpdate(
								order.getOrderId(),
								"new order",
								OrderState.PENDING_NEW,
								null,
								tm.getTime())));
	}
	
	public void cancelOrder(Order order) {
		exchange.cancel(order);
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
			bus.post(new OrderEvent(tm.getTime(),new OrderUpdate(update)));
		} else {
			Log.error("No order found for update " + update);
		}
	}
}

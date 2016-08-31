package mabit.exchange;

import java.util.List;

import org.joda.time.DateTime;

import mabit.oms.order.Exec;
import mabit.oms.order.IInstrument;
import mabit.oms.order.OrderState;
import mabit.oms.order.OrderUpdate.RequestResult;

public class ExchangeUpdate {
	final IInstrument instrument;
	final Long orderId;
	final RequestResult lastRequestResult;
	final OrderState orderState;
	final List<Exec> execs;
	final String message;
	final DateTime timestamp;
	
	public ExchangeUpdate(IInstrument instrument, Long orderId, RequestResult lastRequestResult, OrderState orderState, List<Exec> execs,
			String message, DateTime timestamp) {
		this.instrument = instrument;
		this.orderId = orderId;
		this.lastRequestResult = lastRequestResult;
		this.orderState = orderState;
		this.execs = execs;
		this.message = message;
		this.timestamp = timestamp;
	}
	
	public Long getOrderId() {
		return orderId;
	}
	public OrderState getOrderState() {
		return orderState;
	}
	public List<Exec> getExecs() {
		return execs;
	}
	
	
	
	public IInstrument getInstrument() {
		return instrument;
	}

	public RequestResult getLastRequestResult() {
		return lastRequestResult;
	}

	public String getMessage() {
		return message;
	}

	public DateTime getTimestamp() {
		return timestamp;
	}



	public static interface Listener {
		public void onExchangeUpdate(ExchangeUpdate update);
		
	}
	
}

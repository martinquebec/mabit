package mabit.exchange;


import mabit.data.instruments.IInstrument;
import mabit.oms.order.Exec;
import mabit.oms.order.OrderState;
import mabit.oms.order.Side;
import org.joda.time.DateTime;

import java.util.List;

public class ExchangeUpdate {
	public enum RequestResult { SUCCESS, FAILLURE }

	private final IInstrument instrument;
	private final Side side;
	private final Long orderId;
	private final RequestResult lastRequestResult;
	private final OrderState orderState;
	private final List<Exec> execs;
	private final String message;
	private final DateTime timestamp;
	
	public ExchangeUpdate(IInstrument instrument, Side side, Long orderId, RequestResult lastRequestResult, OrderState orderState, List<Exec> execs,
			String message, DateTime timestamp) {
		this.instrument = instrument;
		this.side = side;
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

	public Side getSide() { return side; }


	public interface Listener {
		void onExchangeUpdate(ExchangeUpdate update);
		
	}
	
}

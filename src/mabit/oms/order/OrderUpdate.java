package mabit.oms.order;


import java.util.List;

import org.joda.time.DateTime;

public class OrderUpdate  {
	public enum RequestResult { SUCCESS, FAILLURE }
	Long orderId;
	String message;
	RequestResult requestResult;
	OrderState state;
	List<Exec> execs;
    DateTime timestamp;
	public OrderUpdate(Long orderId, String message, RequestResult requestResult, OrderState state, List<Exec> execs,
			DateTime timestamp) {
		super();
		this.orderId = orderId;
		this.message = message;
		this.requestResult = requestResult;
		this.state = state;
		this.execs = execs;
		this.timestamp = timestamp;
	}
	public Long getOrder() {
		return orderId;
	}
	public String getMessage() {
		return message;
	}
	public RequestResult getRequestResult() {
		return requestResult;
	}
	public OrderState getState() {
		return state;
	}
	public List<Exec> getExecs() {
		return execs;
	}
	public DateTime getTimestamp() {
		return timestamp;
	}
    
    
	
}
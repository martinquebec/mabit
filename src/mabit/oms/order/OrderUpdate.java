package mabit.oms.order;


import java.util.List;

import org.joda.time.DateTime;

import mabit.exchange.ExchangeUpdate;

public class OrderUpdate  {
	Long orderId;
	String message;
	OrderState state;
	List<Exec> execs;
    DateTime timestamp;
	public OrderUpdate(Long orderId, String message,  OrderState state, List<Exec> execs,
			DateTime timestamp) {
		super();
		this.orderId = orderId;
		this.message = message;
		this.state = state;
		this.execs = execs;
		this.timestamp = timestamp;
	}
	
	public OrderUpdate(ExchangeUpdate u) {
		this.orderId = u.getOrderId();
		this.message = u.getMessage();
		this.state = u.getOrderState();
		this.execs = u.getExecs();
		this.timestamp = u.getTimestamp();
	}
	
	public Long getOrder() {
		return orderId;
	}
	public String getMessage() {
		return message;
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
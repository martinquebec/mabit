package mabit.oms.order;


import mabit.util.GsonUtils;
import org.joda.time.DateTime;

public class OrderUpdate  {
	private final Order order;
	private final String message;
    private final DateTime timestamp;

	public OrderUpdate(Order order, String message, DateTime timestamp) {
		this.order = order;
		this.message = message;
		this.timestamp = timestamp;
	}

	/*
        public OrderUpdate(ExchangeUpdate u) {
            this.instrument = u.getInstrument();
            this.side = u.getSide();
            this.orderId = u.getOrderId();
            this.message = u.getMessage();
            this.state = u.getOrderState();
            this.execs = u.getExecs();
            this.timestamp = u.getTimestamp();
        }
        */
	public String getMessage() {
		return message;
	}
	public DateTime getTimestamp() {
		return timestamp;
	}

	public Order getOrder(){
		return order;
	}


	public String toString() {
		return GsonUtils.get().toJson(this);
	}
    
    
	
}
package mabit.dispatcher;

import org.joda.time.DateTime;

public class GenericEvent<T> implements IEvent {
	private final DateTime timestamp;
	private final EventType eventType;
	private final T payload;
	
	
	public GenericEvent(DateTime timestamp, EventType type, T payload) {
		this.timestamp = timestamp;
		this.eventType =type;
		this.payload = payload;
	}

	@Override
	public DateTime getDateTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventType getEventType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public T getPayLoad() {
		return payload;
	}
	

}

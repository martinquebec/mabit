package mabit.dispatcher;

import org.joda.time.DateTime;

public class GenericEvent<T> implements IEvent {
	DateTime timestamp;
	EventType eventType;
	T payload;
	
	
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

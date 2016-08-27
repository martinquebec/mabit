package mabit.dispatcher;

import org.joda.time.DateTime;

public interface IEvent {
	
	DateTime getDateTime();
	EventType getEventType();
	

}

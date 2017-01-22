package mabit.dispatcher;

import org.joda.time.DateTime;

import java.util.Comparator;

public interface IEvent {
	Comparator<IEvent> EVENT_COMPARATOR = new IEventComparator();
	
	DateTime getDateTime();
	EventType getEventType();
	
	class IEventComparator implements Comparator<IEvent> {
		@Override
		public int compare(IEvent o1, IEvent o2) {
			
			return o1.getDateTime().isBefore(o2.getDateTime())? -1 :1;
		}		
	}
	

}

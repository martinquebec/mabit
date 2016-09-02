package mabit.dispatcher;

import java.util.Comparator;

import org.joda.time.DateTime;

public interface IEvent {
	public static Comparator<IEvent> EVENT_COMPARATOR = new IEventComparator(); 
	
	DateTime getDateTime();
	EventType getEventType();
	
	public static class IEventComparator implements Comparator<IEvent> {
		@Override
		public int compare(IEvent o1, IEvent o2) {
			
			return o1.getDateTime().isBefore(o2.getDateTime())? -1 :1;
		}		
	}
	

}

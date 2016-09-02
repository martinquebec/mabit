package mabit.dispatcher;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.apache.log4j.Logger;

public class SimulationDispatcher extends Dispatcher {
	Logger Log = Logger.getLogger(SimulationDispatcher.class);
	
	Queue<IEvent> events = new PriorityQueue<IEvent>(1000000,IEvent.EVENT_COMPARATOR);
	final String name;
	
	public SimulationDispatcher(String name, boolean sameThread) {
		super(sameThread);
		this.name = name;
	}

	public IEvent getNextEvent() {
		return events.poll();				
	}
	
	public void addEvents(List<? extends IEvent> eventsToAdd) {
		events.addAll(eventsToAdd);
	}
	public void addEvent(IEvent eventToAdd) {
		events.add(eventToAdd);
	}
	
	public void consumeAllEvents() {
		while(events.size() > 0) {
			post(events.poll());
		}
		Log.info("Done with Simution " + name);
		
		
	}

}

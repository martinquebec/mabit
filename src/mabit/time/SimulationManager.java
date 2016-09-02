package mabit.time;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.apache.log4j.Logger;

import mabit.dispatcher.Dispatcher;
import mabit.dispatcher.IEvent;

public class SimulationManager extends Dispatcher {
	Logger Log = Logger.getLogger(SimulationManager.class);
	
	Queue<IEvent> events = new PriorityQueue<IEvent>(1000000,IEvent.EVENT_COMPARATOR);
	final Dispatcher dispatcher;
	final String name;
	
	public SimulationManager(Dispatcher dispatcher, String name) {
		this.dispatcher = dispatcher;
		this.name = name;
	}

	public IEvent getNextEvent() {
		return events.poll();				
	}
	
	public void addEvents(List<IEvent> eventsToAdd) {
		events.addAll(eventsToAdd);
	}
	public void addEvent(IEvent eventToAdd) {
		events.add(eventToAdd);
	}
	
	public void consumeAllEvents() {
		while(events.size() > 0) {
			dispatcher.post(events.poll());
		Log.info("Done with Simution " + name);}
		
		
	}

}

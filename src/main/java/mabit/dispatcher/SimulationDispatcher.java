package mabit.dispatcher;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import static mabit.dispatcher.IDispatcher.State.STOP;

public class SimulationDispatcher extends Dispatcher {


	public static class SingleThreaderSimulationDispatcher extends SimulationDispatcher {
		Logger Log = Logger.getLogger(SingleThreaderSimulationDispatcher.class);

		public SingleThreaderSimulationDispatcher() {
			super("SingleThreaderSimulationDispatcher", true);
		}
	}

	private State state = STOP;
	private double speed = 0;

	private final Logger Log = Logger.getLogger(SimulationDispatcher.class);
	
	private final Queue<IEvent> events = new PriorityQueue<>(1000000, IEvent.EVENT_COMPARATOR);
	private final String name;
	private DateTime currentTime = null;
	
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

	@Override
	synchronized public void put(IEvent event) {
			events.add(event);
	}

	private void postOne() {
		if(events.size()>0) super.postOne(events.poll());
	}

	public void consumeAllEvents() {
		DateTime lastEventTime = null;
		while (true) {
			switch (state) {
				case STOP:
					try {
						Thread.currentThread().sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					break;
				case ONEEVENT:
					postOne();
					changeState(STOP, "after one event");
					break;
				case RUN:
					IEvent event = events.peek();
					if (lastEventTime != null)
						super.postOne(events.poll());
					lastEventTime = event.getDateTime();

					break;
				case TERMINATED:
					Log.info("Done with Simution " + name);
					return;

			}
		}
	}
	@Override
	synchronized public void changeState(State newState, String message) {
		if(newState != state) {
			Log.info("Changing state from " + state + " to " + newState + " because " + message);
			state = newState;
		} else {
			Log.info("Dispatcher alreader in state " + state + " , message=" + message);
		}
	}
}

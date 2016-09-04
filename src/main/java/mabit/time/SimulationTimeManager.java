package mabit.time;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;

import mabit.dispatcher.Dispatcher;
import mabit.dispatcher.Event.TimeEvent;
import mabit.dispatcher.EventType;
import mabit.dispatcher.IEvent;
import mabit.dispatcher.IEventListener;

public class SimulationTimeManager implements IEventListener, ITimeManager {
	private DateTime now;
	private final Dispatcher dispatcher;

	public SimulationTimeManager(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		dispatcher.register(EventType.ORDER, this);
		dispatcher.register(EventType.QUOTE, this);
		dispatcher.register(EventType.TIME, this);
		dispatcher.register(EventType.TRADE, this);
	}
	
	@Override
	public void onEvent(IEvent event) {
		this.now = event.getDateTime();
	}

	@Override
	public Priority getPriority() {
		return Priority.HIGH;
	}

	@Override
	public DateTime getTime() {
		return now;
	}

	@Override
	public ScheduledFuture<?> schedule(Runnable runnable, long delay, TimeUnit unit) {
		//TODO: handle scheduled Future
		DateTime scheduleTime = now.plusMillis((int)unit.toMillis(delay));
		TimeEvent event = new TimeEvent(scheduleTime,runnable);
		dispatcher.post(event);
		return null;
	}

	@Override
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long initialDelay, long period, TimeUnit unit) {
		// TODO Auto-generated method stub
		return null;
	}

}
package mabit.time;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;

import mabit.dispatcher.Dispatcher;
import mabit.dispatcher.Event;
import mabit.dispatcher.Event.TimeEvent;
import mabit.dispatcher.IEvent;
import mabit.dispatcher.IEventListener;

public class SimulationTimeManager implements IEventListener, ITimeManager {
	DateTime now;
	final Dispatcher dispatcher;

	public SimulationTimeManager(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
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

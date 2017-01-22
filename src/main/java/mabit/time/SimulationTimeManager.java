package mabit.time;

import mabit.dispatcher.*;
import mabit.dispatcher.Event.TimeEvent;
import org.joda.time.DateTime;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SimulationTimeManager implements IEventListener, ITimeManager {
	private DateTime now;
	private final IDispatcher dispatcher;

	public SimulationTimeManager() {
		this(ServiceProvider.INSTANCE.getService(IDispatcher.class));
	}
	public SimulationTimeManager(IDispatcher dispatcher) {
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
		dispatcher.put(event);
		return null;
	}

	@Override
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long initialDelay, long period, TimeUnit unit) {
		// TODO Auto-generated method stub
		return null;
	}

}

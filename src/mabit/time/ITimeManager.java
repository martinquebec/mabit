package mabit.time;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public interface ITimeManager {

	public DateTime getTime();
	
	public ScheduledFuture<?> schedule(Runnable runnable, long delay, TimeUnit unit);
	
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, 
			long initialDelay,
            long period,
            TimeUnit unit);
}

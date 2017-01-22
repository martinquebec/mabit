package mabit.time;

import org.joda.time.DateTime;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public interface ITimeManager {

	DateTime getTime();
	
	ScheduledFuture<?> schedule(Runnable runnable, long delay, TimeUnit unit);
	
	ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable,
                                           long initialDelay,
                                           long period,
                                           TimeUnit unit);
}

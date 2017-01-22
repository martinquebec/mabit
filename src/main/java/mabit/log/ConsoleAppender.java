package mabit.log;

import mabit.dispatcher.Event;
import mabit.dispatcher.IDispatcher;
import mabit.dispatcher.ServiceProvider;
import mabit.time.ITimeManager;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.joda.time.DateTime;

/**
 * Created by martin on 12/9/2016.
 */
public class ConsoleAppender  extends AppenderSkeleton {
    private IDispatcher dispatcher= null;
    private ITimeManager tm = null;
    public ConsoleAppender() {
        super();
        System.out.println("Creating appender");
        dispatcher = ServiceProvider.INSTANCE.getService(IDispatcher.class);
        tm = ServiceProvider.INSTANCE.getService(ITimeManager.class);
    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        if(dispatcher==null) dispatcher = ServiceProvider.INSTANCE.getServiceIfCreated(IDispatcher.class);
        if(tm==null) tm = ServiceProvider.INSTANCE.getServiceIfCreated(ITimeManager.class);
        if(dispatcher !=null && tm !=null) {
            System.out.println("Sending log event");
            DateTime time = tm.getTime();
            if(time==null)
                time = new DateTime().minusYears(10);
            dispatcher.put(new Event.LogEvent(time, loggingEvent));
        }
    }

    @Override
    public void close() {

    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}

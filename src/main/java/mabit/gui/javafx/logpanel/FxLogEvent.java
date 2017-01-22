package mabit.gui.javafx.logpanel;

import javafx.event.Event;
import javafx.event.EventType;
import org.apache.log4j.spi.LoggingEvent;
import org.joda.time.DateTime;

/**
 * Created by martin on 9/9/2016.
 */
public class FxLogEvent extends Event {
    public static final EventType<FxLogEvent> LOG_EVENT =
            new EventType<>(Event.ANY,"LogEvent");
    private final DateTime dt;
    private final LoggingEvent log4jEvent;


    public FxLogEvent( DateTime dt, LoggingEvent log4jEvent) {
        super(LOG_EVENT);
        this.dt = dt;
        this.log4jEvent = log4jEvent;
    }

    public DateTime getDt() {
        return dt;
    }

    public LoggingEvent getLog4jEvent() {
        return log4jEvent;
    }
}

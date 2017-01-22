package mabit.gui.javafx.simulpanel;

import javafx.event.Event;
import javafx.event.EventType;
import org.joda.time.DateTime;

/**
 * Created by martin on 9/9/2016.
 */
public class SimulEvent extends Event {
    public static final EventType<SimulEvent> ANY =
            new EventType<>(Event.ANY,"SimulEvent");

    public SimulEvent(EventType<SimulEvent> eventType) {
        super(eventType);
    }

    public static class SimulNewTimeEvent extends Event {
        final public static EventType<SimulNewTimeEvent> TYPE =
                new EventType<>(ANY,"NEW_TIME");
        private final DateTime newTime;

        public SimulNewTimeEvent( DateTime newTime) {
            super(TYPE);
            this.newTime = newTime;
        }

        public DateTime getNewTime() {
            return newTime;
        }
    }



}

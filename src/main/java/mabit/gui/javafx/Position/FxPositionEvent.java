package mabit.gui.javafx.Position;

import javafx.event.Event;
import javafx.event.EventType;
import mabit.oms.position.IPosition;
import org.joda.time.DateTime;

import static mabit.gui.javafx.quotepanel.FxQuoteEvent.QUOTE_EVENT;

/**
 * Created by martin on 9/9/2016.
 */
public class FxPositionEvent extends Event {
    public static final EventType<FxPositionEvent> POSITION_EVENT =
            new EventType<>(Event.ANY, "Positionvent");

    private final IPosition position;
    private final DateTime timestamp;

    public FxPositionEvent(IPosition position, DateTime timestamp) {
        super(QUOTE_EVENT);
        this.position = position;
        this.timestamp = timestamp;
    }

    public IPosition getPosition() {
        return position;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }
}
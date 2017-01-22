package mabit.gui.javafx.quotepanel;

import javafx.event.Event;
import javafx.event.EventType;
import mabit.data.marketdata.Quote;
import org.joda.time.DateTime;

/**
 * Created by martin on 9/9/2016.
 */
public class FxQuoteEvent extends Event {
    public static final EventType<FxQuoteEvent> QUOTE_EVENT =
            new EventType<>(Event.ANY, "QuoteEvent");

    private final Quote quote;
    private final DateTime timestamp;

    public FxQuoteEvent(Quote quote, DateTime timestamp) {
        super(QUOTE_EVENT);
        this.quote = quote;
        this.timestamp = timestamp;
    }

    public Quote getQuote() {
        return quote;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }
}
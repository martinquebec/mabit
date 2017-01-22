package mabit.gui.javafx.orderpanel2;

import javafx.event.Event;
import javafx.event.EventType;
import mabit.oms.order.Order;
import org.joda.time.DateTime;

/**
 * Created by martin on 9/9/2016.
 */
public class FxOrderEvent extends Event {
    public static final EventType<FxOrderEvent> ORDER_EVENT =
            new EventType<>(Event.ANY, "OrderEvent");

    private final Order order;
    private final String message;
    private final DateTime timestamp;

    public FxOrderEvent(Order order, String message, DateTime timestamp) {
        super(ORDER_EVENT);
        this.order = order;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Order getOrder() {
        return order;
    }

    public String getMessage() {
        return message;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }
}

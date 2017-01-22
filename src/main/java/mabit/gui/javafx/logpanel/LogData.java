package mabit.gui.javafx.logpanel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.joda.time.DateTime;

/**
 * Created by martin on 5/9/2016.
 */
public class LogData {




    private final StringProperty type;
    private final StringProperty message;
    private final ObjectProperty<DateTime> time;

    public LogData(StringProperty type, StringProperty message, ObjectProperty<DateTime> time) {
        this.type = type;
        this.message = message;
        this.time = time;
    }

    public LogData(DateTime dt, String type, String message) {
        this.type = new SimpleStringProperty(type);
        this.message = new SimpleStringProperty(message);
        this.time = new SimpleObjectProperty<>(dt);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getMessage() {
        return message.get();
    }

    public StringProperty messageProperty() {
        return message;
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    public DateTime getTime() {
        return time.get();
    }

    public ObjectProperty<DateTime> timeProperty() {
        return time;
    }

    public void setTime(DateTime time) {
        this.time.set(time);
    }
}

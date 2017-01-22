package mabit.gui.javafx.table;

import javafx.util.StringConverter;
import mabit.time.TimeUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by martin on 25/9/2016.
 */
public class TableDateTimeStringConverter extends StringConverter<Object> {
    @Override
    public String toString(Object obj) {
        if (obj instanceof DateTime) {
            DateTime dt = (DateTime) obj;
            return (dt != null) ? dt.toString(TimeUtils.HMS) : "no time";
        }
        return "bad type";
    }

    @Override
    public Object fromString(String string) {
        try {
            return DateTime.parse(string, DateTimeFormat.forPattern(TimeUtils.HMS));
        } catch (Exception e) {
            return new DateTime();
        }
    }
}

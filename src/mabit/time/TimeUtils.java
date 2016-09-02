package mabit.time;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TimeUtils {
	final public static DateTimeFormatter YYYYMMDD_HMS = DateTimeFormat.forPattern("YYYYMMdd, HH:mm:ss");
	final public static DateTimeFormatter HMS = DateTimeFormat.forPattern("HH:mm:ss");

	public static String toFullDateTimeNoMillis(DateTime dt) {
		return YYYYMMDD_HMS.print(dt);
	}
	public static String toTimeNoMills(DateTime dt) { return HMS.print(dt); }
	
	
}

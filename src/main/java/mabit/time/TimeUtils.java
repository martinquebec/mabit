package mabit.time;

import org.joda.time.DateTime;

public class TimeUtils {
	public final static String YYYYMMDD_HMS = "YYYYMMdd, HH:mm:ss";
	public final static String HMS = "HH:mm:ss";

	public static String toFullDateTimeNoMillis(DateTime dt) {
		return dt.toString(YYYYMMDD_HMS);
	}
	public static String toHHmmss(DateTime dt) { return dt.toString(HMS); }

//	public static class JavafxTimeConverter implements StringConverter<DateTime> {
//		@Override
//		public String toString(DateTime date) {
//			return date.toString(HMS);
//		}
//
//		@Override
//		public DateTime fromString(String string) {
//			return DateTime.parse(string,HMS);
//		}
//	}

	
}

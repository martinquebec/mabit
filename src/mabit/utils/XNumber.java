package mabit.utils;

public class XNumber {
	public static double getDouble(String s) {
		return getDouble(s,0.0);
	}
		public static double getDouble(String s,double def) {
		try {
			return Double.parseDouble(s.replaceAll(",", ""));
		} catch (Exception e) {
			return def;
		}
	}
}

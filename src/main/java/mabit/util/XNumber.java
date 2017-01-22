package mabit.util;

public class XNumber {
	private static final double TOLERANCE = 10e-10;
	public static double getDouble(String s) {
		return getDouble(s,0.0);
	}

	private static double getDouble(String s, double def) {
		try {
			return Double.parseDouble(s.replaceAll(",", ""));
		} catch (Exception e) {
			return def;
		}
	}

	private static boolean isEqual(double d1, double d2, double tolerance) {
		return Math.abs(d1-d2) < tolerance;	
	}	

	public static boolean  isEqual(double d1, double d2) {
		return isEqual(d1, d2,TOLERANCE);	

	}	
	
	public static double round(double x, int precision) {
		return Math.round(x * Math.pow(10,precision)) / Math.pow(10, precision);
	}

	public static boolean isGreaterOrEqual(double d1, double d2, double tolerance) {
		return d1 +tolerance > d2;	
	}	
	public static boolean isGreaterOrEqual(double d1, double d2) {
		return d1 + TOLERANCE > d2;	
	}	
	public static boolean isLessOrEqual(double d1, double d2, double tolerance) {
		return d1 - tolerance < d2;	
	}	
	public static boolean isLessOrEqual(double d1, double d2) {
		return d1 - TOLERANCE < d2;	
	}	
	
	public static double minMax(double min, double max, double x) {
		return Math.min(Math.max(x, min), max);
	}

}

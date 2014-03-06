package com.yhealthy.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TypeFactory {
	private static final String PATTERN = "yyyy-MM-dd";

	public static Object factory(String type, String value) {
		if ("long".equals(type) || "Long".equals(type)) {
			return Long.parseLong(value);
		} else if ("int".equals(type) || "Integer".equals(type)) {
			return Integer.parseInt(value);
		} else if ("float".equals(type) || "Float".equals(type)) {
			return Float.parseFloat(value);
		} else if ("double".equals(type) || "Double".equals(type)) {
			return Double.parseDouble(value);
		} else if ("date".equals(type)|| "Date".equals(type)) {
			SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
			Date date = null;
			try {
				date = sdf.parse(value);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return date;
		} else {
			return value;
		}
	}
}

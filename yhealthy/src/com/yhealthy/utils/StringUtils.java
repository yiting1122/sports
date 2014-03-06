package com.yhealthy.utils;

public abstract class StringUtils {
	public static boolean isEmpty(String string) {
		if (string != null && string.trim().length() > 0) {
			return false;
		}
		return true;
	}
}

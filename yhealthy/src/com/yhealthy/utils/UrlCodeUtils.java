package com.yhealthy.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;

public abstract class UrlCodeUtils {
	private static final String CODING = "UTF-8";

	public static String encode(String string, String encoding)
			throws Exception {
		if (!StringUtils.isEmpty(encoding)) {
			encoding = CODING;
		}
		if (!StringUtils.isEmpty(string)) {
			string = URLEncoder.encode(string, encoding);
		}
		return string;
	}

	public static String decode(String string, String decoding)
			throws Exception {
		if (!StringUtils.isEmpty(decoding)) {
			decoding = CODING;
		}
		if (!StringUtils.isEmpty(string)) {
			string = URLDecoder.decode(string, decoding);
		}
		return string;
	}

}

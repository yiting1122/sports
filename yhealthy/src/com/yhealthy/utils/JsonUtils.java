package com.yhealthy.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.platform.comapi.map.v;

import android.util.Log;

public abstract class JsonUtils {
	private final static String ENCODING = "GBK";

	public static Object parserJson(byte[] data, String field) {
		Object obj = null;
		if (data != null && data.length > 0) {
			try {

				String temp = new String(data, ENCODING);
				Log.i("json111", temp);

				JSONObject jsonObject = new JSONObject(temp);
				if (jsonObject != null) {
					obj = jsonObject.getString(field);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return obj;
	}

	/*
	 * public static List<Object> parserJsonObject(byte[] data, String field,
	 * Class<?> cls) { List<Object> list = new ArrayList<Object>(); if (data !=
	 * null && data.length > 0) { try { String temp = new String(data,
	 * ENCODING); JSONObject jsonObject = new JSONObject(temp); if (jsonObject
	 * != null) { JSONArray jsonArray = jsonObject.getJSONArray(field);
	 * BeanUtils beanUtils = BeanUtils.getInstance(); String[] fields =
	 * beanUtils.getFields(cls); if (jsonArray != null && jsonArray.length() > 0
	 * && fields != null && fields.length > 0) { for (int i = 0; i <
	 * jsonArray.length(); i++) { Object object = cls.newInstance(); JSONObject
	 * json = jsonArray.getJSONObject(i); for (String property : fields) {
	 * Object value = json.get(property); beanUtils.setProperty(object,
	 * property, value); } list.add(object); } }
	 * 
	 * } } catch (Exception e) { // TODO: handle exception e.printStackTrace();
	 * } } return list; }
	 */
	public static <T> List<T> parserJsonObject(byte[] data, String field,
			Class<T> cls) {
		List<T> objects = new ArrayList<T>();
		if (data != null && data.length > 0) {
			try {
				String temp = new String(data, ENCODING);
				JSONObject jsonObject = new JSONObject(temp);
				if (jsonObject != null) {
					JSONArray jsonArray = jsonObject.getJSONArray(field);
					BeanUtils beanUtils = BeanUtils.getInstance();
					String[] fields = beanUtils.getFields(cls);
					if (jsonArray != null && jsonArray.length() > 0
							&& fields != null && fields.length > 0) {
						for (int i = 0; i < jsonArray.length(); i++) {
							T object = cls.newInstance();
							JSONObject json = jsonArray.getJSONObject(i);
							for (String property : fields) {
								Object value = json.get(property);
								if (value != null
										&& (!"null".equals(value.toString()))) {
								
									beanUtils.setProperty(object, property,
											value);

								}
							}
							objects.add(object);
						}
					}

				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
		return objects;
	}
}

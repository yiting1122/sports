package com.yhealthy.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class BeanUtils {
	private static BeanUtils instance = null;
	public final static String FIELDNAME = "fieldName";
	public final static String FIELDTYPE = "fieldType";
	public final static String FIELDVALUE = "fieldValue";

	public BeanUtils() {

	}

	public static synchronized BeanUtils getInstance() {
		if (instance == null) {
			instance = new BeanUtils();
		}
		return instance;
	}

	public Class<?> getGenericClass(Class<?> clazz, int index)
			throws IndexOutOfBoundsException {
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			throw new IndexOutOfBoundsException("Index: " + index
					+ ", Size of Parameterized Type: " + params.length);
		}
		return (Class<?>) params[index];
	}

	public Object getProperty(Object object, String property)
			throws IllegalAccessException, NoSuchFieldException {
		Object retVal = null;
		Class<?> cls = object.getClass();
		Field field = cls.getDeclaredField(property);
		if (field != null) {
			field.setAccessible(true);
			retVal = field.get(object);
		}
		return retVal;
	}

	public void setProperty(Object object, String property, Object value)
			throws IllegalAccessException, NoSuchFieldException {
		Class<?> cls = object.getClass();
		Field field = cls.getDeclaredField(property);	
		if (field != null) {
			String  typeName=field.getType().getSimpleName();
			Object fieldValue=TypeFactory.factory(typeName, value.toString());
			if(fieldValue!=null){
			field.setAccessible(true);
			field.set(object, fieldValue);}
		}
	}

	public String[] getFields(Class<?> entityClass) {
		Map<String, Object> map = getAllField(entityClass);
		if (map != null && !map.isEmpty()) {
			return (String[]) map.get(FIELDNAME);
		}
		return null;
	}

	public Map<String, Object> getAllField(Class<?> entityClass) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (entityClass != null) {
			Field[] fields = entityClass.getDeclaredFields();
			String[] fieldName = new String[fields.length];
			Class<?>[] fieldType = new Class<?>[fields.length];

			for (Integer i = 0; i < fields.length; i++) {
				fieldName[i] = fields[i].getName();
				fieldType[i] = fields[i].getType();
			}
			map.put(FIELDNAME, fieldName);
			map.put(FIELDTYPE, fieldType);
		}
		return map;
	}

	public Object[] getFieldValue(Object object) {
		Object[] obj = null;
		Map<String, Object> map = getAllField(object.getClass());
		if (map != null && !map.isEmpty()) {
			String[] fieldName = (String[]) map.get(FIELDNAME);
			if (fieldName != null && fieldName.length > 0) {
				obj = new Object[fieldName.length];
				for (Integer i = 0; i < fieldName.length; i++) {
					try {
						obj[i] = getProperty(object, fieldName[i]);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}
			}
		}
		return obj;
	}
}

package com.yhealthy.daoImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yhealthy.dao.BaseDao;
import com.yhealthy.exception.ApplicationException;
import com.yhealthy.pager.Pager;
import com.yhealthy.utils.BeanUtils;
import com.yhealthy.utils.DatabaseHelper;
import com.yhealthy.utils.TypeFactory;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class AbstractBaseDaoImp<T, PK extends Serializable> implements
		BaseDao<T, PK> {
	protected Class<T> cls;
	protected SQLiteOpenHelper sqLiteOpenHelper;
	private static final String ID = "id";

	@SuppressWarnings("unchecked")
	public AbstractBaseDaoImp(Context context) {
		this.cls = (Class<T>) BeanUtils.getInstance().getGenericClass(
				getClass(), 0);
		this.sqLiteOpenHelper = new DatabaseHelper(context);
	}

	public SQLiteOpenHelper getSqLiteOpenHelper() {
		return sqLiteOpenHelper;
	}

	public void setSqLiteOpenHelper(SQLiteOpenHelper sqLiteOpenHelper) {
		this.sqLiteOpenHelper = sqLiteOpenHelper;
	}

	@Override
	public long save(T entity) throws ApplicationException {
		// TODO Auto-generated method stub
		List<T> list = new ArrayList<T>();
		list.add(entity);
		return (this.save(list));
	}

	@Override
	public long saveWithIncrementId(final T entity) throws ApplicationException {
		// TODO Auto-generated method stub
		List<T> list = new ArrayList<T>();
		list.add(entity);
		return (this.saveWithIncrementId(list));
	} 
	@Override
	public void update(T entity) throws ApplicationException {
		// TODO Auto-generated method stub

		String tableName = entity.getClass().getSimpleName();
		BeanUtils beanUtils = BeanUtils.getInstance();
		Map<String, Object> map = beanUtils.getAllField(entity.getClass());
		if (map != null && !map.isEmpty()) {
			SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
			String[] field = (String[]) map.get(BeanUtils.FIELDNAME);
			Object[] value = beanUtils.getFieldValue(entity);
			if (field != null && value != null && field.length == value.length
					&& field.length > 0) {
				ContentValues contentValues = new ContentValues();
				int index = -1;
				for (int i = 0; i < field.length; i++) {
					if (ID.equals(field[i])) {
						index = i;
						continue;
					}
					contentValues.put(field[i], value[i] + "");
				}
				db.update(tableName, contentValues, ID + "=?",
						new String[] { value[index].toString() });
				closeDatabase(db);
			}
		}
	}

	@Override
	public void remove(PK... ids) throws ApplicationException {
		// TODO Auto-generated method stub
		SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
		String tableName = cls.getSimpleName();db.beginTransaction();
		try {
			
		
		for (PK pk : ids) {

			db.delete(tableName, ID + "=?", new String[] { pk.toString() });
		}
		db.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	@Override
	public T find(PK id) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
		String tableName = cls.getSimpleName();
		Cursor cursor = db.query(tableName, null, ID + "=?",
				new String[] { id.toString() }, null, null, null);
		T entity = null;
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			entity = getEntity(cursor);
			cursor.close();
		}
		closeDatabase(db);
		return entity;
	}

	@Override
	public long getCount() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
		String tableName = cls.getSimpleName();
		Long count = 0L;
		Cursor cursor = db.query(tableName, new String[] { "count(*)" }, null,
				null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			count = cursor.getLong(0);
		}
		closeDatabase(db);
		return count;
	}

	@Override
	public List<T> select(Pager pager) {
		// TODO Auto-generated method stub

		String where = null;
		String limit = null;
		if (pager != null) {
			if (pager.getKeyWord() != null && !pager.getKeyWord().equals("")) {
				String temp = "";
				for (String field : pager.getFields()) {
					temp += field + "like '%" + pager.getKeyWord() + "%'";
				}
				if (temp.trim().length() > 0) {
					where = temp;
				}
			}
			if (pager.getStart() >= 0 && pager.getEnd() > 0) {
				limit = pager.getStart() + "," + pager.getEnd();
			}
		}
		List<T> list = new ArrayList<T>();
		SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
		String tableName = cls.getSimpleName();
		Cursor cursor = db.query(tableName, null, where, null, null, null, ID
				+ " asc", limit);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				T entity = getEntity(cursor);
				if (entity != null) {
					list.add(entity);
				}
			}
		}
		closeDatabase(db);
		return list;
	}

	@Override
	public List<T> selectByField(Map<String, Object> map, Pager pager) {
		// TODO Auto-generated method stub

		if (map != null && !map.isEmpty()) {
			String where = null;
			String[] whereArgs = null;
			String limit = null;
			if (pager != null) {

				if (pager.getStart() >= 0 && pager.getEnd() > 0) {
					limit = pager.getStart() + "," + pager.getEnd();
				}
			}
			Set<String> keySet = map.keySet();
			whereArgs = new String[keySet.size()];
			where = "";
			int i = 0;
			for (String key : keySet) {
				where += key + "=? and ";
				whereArgs[i] = map.get(key).toString();
				i++;
			}
			where += "1=1";
			List<T> list = new ArrayList<T>();

			SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
			String tableName = cls.getSimpleName();
			Cursor cursor = db.query(tableName, null, where, whereArgs, null,
					null, ID + " asc", limit);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					T entity = getEntity(cursor);
					if (entity != null) {
						list.add(entity);
					}
				}
			}
			closeDatabase(db);
			return list;
		} else {
			return this.select(pager);
		}
	}

	public T getEntity(Cursor cursor) {
		BeanUtils beanUtils = BeanUtils.getInstance();
		T obj = null;
		try {
			obj = cls.newInstance();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if (obj != null) {
			Map<String, Object> map = beanUtils.getAllField(cls);
			if (map != null && !map.isEmpty()) {
				String[] fields = (String[]) map.get(BeanUtils.FIELDNAME);
				Class<?>[] fieldTypes = (Class<?>[]) map
						.get(BeanUtils.FIELDTYPE);
				int length = fields.length;
				for (int i = 0; i < length; i++) {
					int position = cursor.getColumnIndex(fields[i]);
					if (position != -1) {
						String fieldTypeName = fieldTypes[i].getSimpleName();
						String value = cursor.getString(position);
						if (value != null&&!"null".equals(value)) {
							Object data = TypeFactory.factory(fieldTypeName,
									value);
							try {

								beanUtils.setProperty(obj, fields[i], data);

							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						}

					}
				}
			}
		}
		return obj;
	}

	@Override
	public long save(List<T> entities) throws ApplicationException {
		// TODO Auto-generated method stub
		String tableName = cls.getSimpleName();
		long valueReturn = 0;
		BeanUtils beanUtils = BeanUtils.getInstance();
		Map<String, Object> map = beanUtils.getAllField(entities.get(0)
				.getClass());
		if (map != null && !map.isEmpty()) {
			SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
			String[] field = (String[]) map.get(BeanUtils.FIELDNAME);
			db.beginTransaction();
			try {
				for (T entity : entities) {
					Object[] value = beanUtils.getFieldValue(entity);
					if (field != null && value != null
							&& field.length == value.length && field.length > 0) {
						ContentValues contentValues = new ContentValues();
						for (int i = 0; i < field.length; i++) {
//							if (ID.equals(field[i])) {
//								continue;
//							}
							if (value[i] != null) {
								contentValues.put(field[i], value[i] + "");
							}
						}
						valueReturn = db.insert(tableName, null, contentValues);
					}
				}
				db.setTransactionSuccessful();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				db.endTransaction();
			}
			closeDatabase(db);
		}
		return valueReturn;
	}

	public long saveWithIncrementId(List<T> entities) throws ApplicationException
	{
		String tableName = cls.getSimpleName();
		long valueReturn = 0;
		BeanUtils beanUtils = BeanUtils.getInstance();
		Map<String, Object> map = beanUtils.getAllField(entities.get(0)
				.getClass());
		if (map != null && !map.isEmpty()) {
			SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
			String[] field = (String[]) map.get(BeanUtils.FIELDNAME);
			db.beginTransaction();
			try {
				for (T entity : entities) {
					Object[] value = beanUtils.getFieldValue(entity);
					if (field != null && value != null
							&& field.length == value.length && field.length > 0) {
						ContentValues contentValues = new ContentValues();
						for (int i = 0; i < field.length; i++) {
							if (ID.equals(field[i])) {
								continue;
							}
							if (value[i] != null) {
								contentValues.put(field[i], value[i] + "");
							}
						}
						valueReturn = db.insert(tableName, null, contentValues);
					}
				}
				db.setTransactionSuccessful();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				db.endTransaction();
			}
			closeDatabase(db);
		}
		return valueReturn;
	}
	
	@Override
	public void remove(List<PK> ids) throws ApplicationException {
		// TODO Auto-generated method stub
		SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();

		String tableName = cls.getSimpleName();
		db.beginTransaction();
		try {
			for (PK pk : ids) {
				db.delete(tableName, ID + "=?", new String[] { pk.toString() });
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
		closeDatabase(db);
	}

	protected void closeDatabase(SQLiteDatabase db) {
		if (db != null) {
			db.close();
		}
	}
}

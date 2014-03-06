package com.yhealthy.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.yhealthy.exception.ApplicationException;
import com.yhealthy.pager.Pager;



public interface BaseDao<T, PK extends Serializable> {

	public long save(final T entity) throws ApplicationException;
    public long saveWithIncrementId(final T entity) throws ApplicationException;
	public void update(final T entity) throws ApplicationException;

	public void remove(final PK... ids) throws ApplicationException;

	public T find(final PK id);

	public long getCount();

	public List<T> select(Pager pager);

	public List<T> selectByField(Map<String, Object> map, Pager pager);

	public long save(final List<T> entities) throws ApplicationException;

	public void remove(final List<PK> ids) throws ApplicationException;
}

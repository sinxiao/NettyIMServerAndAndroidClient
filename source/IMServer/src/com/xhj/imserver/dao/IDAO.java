package com.xhj.imserver.dao;

import java.util.List;

public interface IDAO<T> {
	public void save(T t);
	public void update(T t);
	public void del(String id);
	public void del(T t);
	public List<T> find(String where);
	public T findById(String id);
	
}

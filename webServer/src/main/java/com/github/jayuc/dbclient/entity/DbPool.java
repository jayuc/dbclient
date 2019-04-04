package com.github.jayuc.dbclient.entity;

import com.github.jayuc.dbclient.iter.IDbPool;

public class DbPool implements IDbPool {
	
	private DbType type;
	
	private Object pool;
	
	private String token;

	@Override
	public DbType getType() {
		return type;
	}

	@Override
	public Object getPool() {
		return pool;
	}

	@Override
	public String getToken() {
		return token;
	}

	public void setType(DbType type) {
		this.type = type;
	}

	public void setPool(Object pool) {
		this.pool = pool;
	}

	public void setToken(String token) {
		this.token = token;
	}

}

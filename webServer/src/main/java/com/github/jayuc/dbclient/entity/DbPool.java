package com.github.jayuc.dbclient.entity;

import com.github.jayuc.dbclient.iter.IDbPool;

import lombok.Data;

@Data
public class DbPool implements IDbPool {
	
	private DbType type;
	
	private Object pool;
	
	private String token;

}

package com.github.jayuc.dbclient.iter;

import com.github.jayuc.dbclient.entity.DbType;

public interface IDbPool {
	
	DbType getType();
	
	Object getPool();
	
	String getToken();

}

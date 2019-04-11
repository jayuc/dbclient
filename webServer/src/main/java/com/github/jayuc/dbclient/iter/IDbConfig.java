package com.github.jayuc.dbclient.iter;

import com.github.jayuc.dbclient.entity.DbType;

public interface IDbConfig extends IToken {
	
	DbType getType();
	
	String getHost();
	
	String getPort();
	
	String getName();
	
	String getUserName();
	
	String getPassword();

}

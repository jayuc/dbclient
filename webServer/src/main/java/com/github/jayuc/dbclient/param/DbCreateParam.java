package com.github.jayuc.dbclient.param;

import com.github.jayuc.dbclient.entity.DbType;
import com.github.jayuc.dbclient.iter.IDbConfig;

import lombok.Data;

@Data
public class DbCreateParam implements IDbConfig {
	
	private DbType type;
	
	private String host;
	
	private String port;
	
	private String name;
	
	private String userName;
	
	private String passWord;

}

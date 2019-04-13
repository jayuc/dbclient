package com.github.jayuc.dbclient.param;

import com.github.jayuc.dbclient.entity.DbType;
import com.github.jayuc.dbclient.iter.IDbConfig;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DbCreateParam implements IDbConfig {
	
	private DbType type;
	
	private String host;
	
	private int port;
	
	private String name;
	
	private String userName;
	
	private String password;
	
	private String token;

}

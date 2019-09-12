package com.github.jayuc.dbclient.act;

import com.github.jayuc.dbclient.iter.IDbConfig;

public class MySqlDataSource extends AbstractMyDataSources {
	
	public static final String driver = "com.mysql.cj.jdbc.Driver";
	
	public static final String extra = "?&useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=false&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true";

	@Override
	protected String getUrl(IDbConfig config) {
		return "jdbc:mysql://" + config.getHost() + ":" 
				+ config.getPort() + "/" + config.getName() + extra;
	}

	@Override
	protected String getDriver() {
		return driver;
	}

}

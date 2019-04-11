package com.github.jayuc.dbclient.act;

import com.github.jayuc.dbclient.iter.IDbConfig;

/**
 * oracle data sources
 * @author yujie
 * 2019年4月11日 下午3:18:18
 */
public class MyOracleDataSource extends AbstractMyDataSources {
	
	private static final String driver = "oracle.jdbc.driver.OracleDriver";

	@Override
	protected String getUrl(IDbConfig config) {
		return "jdbc:oracle:thin:@" + config.getHost() + ":" + 
					config.getPort() +":" + config.getName();
	}

	@Override
	protected String getDriver() {
		return driver;
	}

}

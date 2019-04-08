package com.github.jayuc.dbclient.config;

/**
 * druid数据源配置
 * @author jayu
 *
 */
public class DruidDataSourceConfig {

	//最大等待时间(毫秒)
	public final static int MAX_WAIT_TIME = 6000;
	
	//收回连接的等待时间(毫秒)
	public final static int  REMOVE_ABANDONED_TIMEOUT_MILLIS = 6000;
	
}

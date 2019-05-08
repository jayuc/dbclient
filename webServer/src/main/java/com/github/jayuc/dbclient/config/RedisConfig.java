package com.github.jayuc.dbclient.config;

/**
 * redis 配置
 * @author yujie
 * 2019年4月13日 下午2:36:49
 */
public class RedisConfig {
	
	//连接超时
	public static int TIME_OUT = 10000;
	
	//获取连接前是否验证
	public static boolean QUERY_VALIDATE = true;
	
	// 最多查询多少条记录
	public static int MAX_LINE = 500;

}

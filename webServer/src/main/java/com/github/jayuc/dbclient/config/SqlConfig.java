package com.github.jayuc.dbclient.config;

/**
 * sql语句中的一些默认参数
 * @author yujie
 * 2019年4月10日 下午3:41:37
 */
public class SqlConfig {

	//默认查询数据量
	public static int LIMIT = 20;
	
	// 最大查询数量
	public static int MAX_LIMIT = 100;
	
	//组装sql count语句时需要的别名
	public static String COUNT_SQL_ALIAS = "AA_AA";
	
}

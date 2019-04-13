package com.github.jayuc.dbclient.iter;

import java.util.Map;

import com.github.jayuc.dbclient.err.SqlHandlerException;

/**
 * sql处理
 * @author yujie
 * 2019年4月10日 上午9:29:52
 */
public interface ISqlHandler {

	/**
	 * sql语句的执行
	 */
	Map<String, Object> execute(Object pool, String sql, String token)
			throws SqlHandlerException;
	
}

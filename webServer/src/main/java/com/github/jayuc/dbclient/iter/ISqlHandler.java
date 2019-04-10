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
	 * 查询sql，一般select语句会用
	 * 返回结果中包含,rows,headers,total等
	 * 备注：往往查询语句需要查询count(*)
	 * 参数说明：token用户key,为了识别用户信息
	 */
	Map<String, Object> query(Object pool, String sql, String token) throws SqlHandlerException;
	
	/**
	 * 执行sql,一般时没有返回结果的，包括,insert,delete,update
	 * 返回结果中包括，effect影响数量，success成功数量，fail失败数量
	 * 参数说明：token用户key,为了识别用户信息
	 */
	Map<String, Object> execute(Object pool, String sql, String token) throws SqlHandlerException;
	
}

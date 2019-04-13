package com.github.jayuc.dbclient.act;

import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.jayuc.dbclient.err.SqlHandlerException;
import com.github.jayuc.dbclient.iter.ISqlHandler;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * redis 处理器
 * @author yujie
 * 2019年4月13日 下午3:38:34
 */
@Component("redisHandler")
public class RedisSqlHandler implements ISqlHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(RedisSqlHandler.class);

	@Override
	public Map<String, Object> execute(Object pool, String sql, String token) 
			throws SqlHandlerException {
		LOG.debug("redis handler");
		try {
			Jedis jedis = ((JedisPool) pool).getResource();
			SqlProperty sqlproperty = parseSql(sql);
			LOG.info("parse sql property: " + sqlproperty);
			//Method method = Jedis.class.getMethod(sqlproperty.methodName, );
		} catch (Exception e) {
			throw new SqlHandlerException(e.getMessage());
		}
		return null;
	}
	
	//解析sql
	private SqlProperty parseSql(String sql) {
		SqlProperty pro = new SqlProperty();
		return pro;
	}
	
	/**
	 * sql 属性描述 内部类
	 */
	class SqlProperty {
		String methodName;
		int paramNumber;
		@Override
		public String toString() {
			return "SqlProperty(methodName=" + methodName + ",paramNumber=" + paramNumber + ")";
		}
	}

}

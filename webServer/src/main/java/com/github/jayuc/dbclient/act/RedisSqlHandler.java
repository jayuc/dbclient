package com.github.jayuc.dbclient.act;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.jayuc.dbclient.data.UserCacheData;
import com.github.jayuc.dbclient.entity.UserData;
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
	
	@Autowired
	UserCacheData userCacheData;

	@Override
	public Map<String, Object> execute(Object pool, String sql, String token) 
			throws SqlHandlerException {
		LOG.debug("redis handler");
		if(null == pool) {
			throw new SqlHandlerException("pool: " + pool);
		}
		Jedis jedis = null;
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			jedis = ((JedisPool) pool).getResource();
			SqlProperty sqlproperty = parseSql(sql);
			LOG.info("parse sql property: " + sqlproperty);
			
			int paramNum = sqlproperty.paramNumber;
			String methodName = sqlproperty.methodName;
			List<String> paramList = sqlproperty.paramList;
			Class<?>[] methodParam = new Class[paramNum];
			Object[] params = new String[paramNum];
			
			for(int i=0; i<paramNum; i++) {
				methodParam[i] = String.class;
				params[i] = paramList.get(i);
			}
			
			Method method = null;
			try {
				LOG.debug("method name: " + methodName);
				method = Jedis.class.getMethod(methodName, methodParam);
				LOG.info("get method: " + method);
			} catch (Exception e) {
				//不做处理
				LOG.info(methodName + "未能直接匹配到");
			}
			
			LOG.debug("-- method: " + method);
			
			// 对方法名进行处理，处理后尝试匹配方法
			if(null == method) {
				String varMethodName = sqlproperty.handleMethodName();
				LOG.info("varMethodName: " + varMethodName);
				if(null != varMethodName) {
					try {
						method = Jedis.class.getMethod(varMethodName, methodParam);
					} catch (Exception e) {
						LOG.info("处理后的方法名： " + varMethodName + " 未能匹配到");
					}
				}
			}
			
			LOG.debug("--- method: " + method);
			
			//通过方法名尝试进行匹配
			if(null == method) {
				Method[] methods = Jedis.class.getMethods();
				for(Method md:methods) {
					if(methodName.equals(md.getName()) 
							&& md.getParameterCount() == paramNum) {
						method = md;
					}
				}
				// 如果方法任未匹配到，则处理方法名后重新匹配
				if(null == method) {
					String varMethodName = sqlproperty.handleMethodName();
					for(Method md:methods) {
						if(varMethodName.equals(md.getName()) 
								&& md.getParameterCount() == paramNum) {
							method = md;
						}
					}
				}
				if(null != method) {
					params = new Object[paramNum];
					Class<?>[] paramTypes = method.getParameterTypes();
					for(int i=0; i<paramTypes.length; i++) {
						LOG.debug("param type: " + paramTypes[i].getName());
						if(String.class == paramTypes[i]) {
							params[i] = paramList.get(i);
						}else {
							params[i] = Integer.valueOf(paramList.get(i));
						}
					}
				}
			}
			
			LOG.debug("---- method: " + method);
			
			if(null != method) {
				Class<?> methodReturnClass = method.getReturnType();
				LOG.info("return class type: " + methodReturnClass.getName());
				
				//用户 redis index
				UserData userData = userCacheData.getUserData(token);
				LOG.info("user cache data: " + userData);
				if(null != userData) {
					LOG.debug("执行第" + userData.getRedisIndex() + "号库");
					jedis.select(userData.getRedisIndex());
				}
				
				Object methodResult = method.invoke(jedis, params);
				LOG.info("methodResult: " + methodResult);
				result = parseResult(methodReturnClass, methodResult, params);
			}else {
				throw new SqlHandlerException("方法名或参数不正确");
			}
			
			//关闭连接
			jedis.close();
			
		} catch (Exception e) {
			throw new SqlHandlerException(e.getMessage());
		} finally {
			//关闭连接
			if(null != jedis) {
				jedis.close();
			}
		}
		return result;
	}
	
	//根据返回结果和返回结果类型解析数据
	@SuppressWarnings("unchecked")
	private Map<String, Object> parseResult(Class<?> clazz, Object obj, Object[] params) 
			throws SqlHandlerException {
		Map<String, Object> map = new HashMap<String, Object>();
		if(null != obj) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			if(String.class == clazz || Boolean.class == clazz) {  //
				if(!"OK".equals(obj)) {
					Map<String, Object> row = new HashMap<String, Object>();
					if(params.length > 1) {
						row.put(params[1].toString(), obj);
					}else {
						if(params.length > 0) {
							row.put(params[0].toString(), obj);
						}
					}
					list.add(row);
					map.put("rows", list);
					map.put("total", 1);
				}
			}else if(Map.class == clazz) {  //map类型
				Map<String, Object> row = (Map<String, Object>) obj;
				Set<String> keys = row.keySet();
				for(String key:keys) {
					Map<String, Object> r = new HashMap<String, Object>();
					r.put(key, row.get(key));
					list.add(r);
				}
				map.put("rows", list);
				map.put("total", keys.size());
			}else if(Set.class == clazz) {
				Set<String> row = (Set<String>) obj;
				map.put("rows", row);
				map.put("total", row.size());
			}else if(List.class == clazz) {
				List<String> row = (List<String>) obj;
				map.put("rows", row);
				map.put("total", row.size());
			}
			if(params.length > 0) {
				map.put("headers", params[0]);
			}
		}
		return map;
	}
	
	//解析sql
	private SqlProperty parseSql(String sql) {
		LOG.debug("开始解析sql");
		SqlProperty pro = new SqlProperty();
		String[] sqlArr = sql.trim().split(" ");
		pro.methodName = sqlArr[0].toLowerCase();
		List<String> paramList = new ArrayList<String>();
		for(int i=1; i<sqlArr.length; i++) {
			if(sqlArr[i].length() > 0) {
				paramList.add(sqlArr[i]);
			}
		}
		pro.paramNumber = paramList.size();
		pro.paramList = paramList;
		LOG.debug("结束解析sql");
		return pro;
	}
	
	/**
	 * sql 属性描述 内部类
	 */
	class SqlProperty {
		String methodName;
		int paramNumber;
		List<String> paramList;
		@Override
		public String toString() {
			return "SqlProperty(methodName=" + methodName + ",paramNumber=" + paramNumber + 
					",paramList=" + paramList + ")";
		}
		//处理方法名
		public String handleMethodName() {
			if(methodName.contains("a") && methodName.indexOf("a") > 0) {
				return methodName.replace("a", "A");
			}
			if(methodName.contains("b") && methodName.indexOf("b") > 0) {
				return methodName.replace("b", "B");
			}
			return null;
		}
	}

}
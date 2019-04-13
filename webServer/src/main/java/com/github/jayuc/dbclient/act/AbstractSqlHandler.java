package com.github.jayuc.dbclient.act;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONArray;
import com.github.jayuc.dbclient.config.SqlConfig;
import com.github.jayuc.dbclient.data.UserCacheData;
import com.github.jayuc.dbclient.entity.UserData;
import com.github.jayuc.dbclient.err.SqlHandlerException;
import com.github.jayuc.dbclient.iter.IMyDataSources;
import com.github.jayuc.dbclient.iter.ISqlHandler;
import com.github.jayuc.dbclient.utils.ApplicationContextUtils;
import com.github.jayuc.dbclient.utils.DbUtil;

/**
 * 关系型数据库对sql语句执行的抽象实现 例如：mysql oracle
 * 注意：像redis这种内存型数据库则不能继承此抽象类
 * @author yujie
 * 2019年4月10日 下午3:10:10
 */
public abstract class AbstractSqlHandler implements ISqlHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(AbstractSqlHandler.class);
	
	@Override
	public Map<String, Object> execute(Object pool, String sql, String token) 
			throws SqlHandlerException{
		String upperSql = sql.toUpperCase();
		/**
		 * sql语句分为有查询结果的和无查询结果的两种sql
		 */
		if(upperSql.startsWith("SELECT")) {
			LOG.debug("sql type: select");
			return query(pool, sql, token);
		}else {
			LOG.debug("sql type: 非select");
			return executesql(pool, sql, token);
		}
	}

	//sql单语句查询
	private Map<String, Object> query(Object pool, String sql, String token) 
			throws SqlHandlerException {
		//判断是否为多语句
		if(haveMultiSql(sql)) {
			throw new SqlHandlerException("select语句不支持多语句同时查询");
		}
		IMyDataSources dataSources = (IMyDataSources) pool;
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> list = null;
		int total = 0;
		try {
			String optimizeSql = optimizeSql(sql, token);
			LOG.debug("optimizeSql: " + optimizeSql);
			list = DbUtil.queryJSONMap(dataSources.getConnection(), optimizeSql);
			LOG.debug("list: " + list);
		} catch (SQLException e) {
			LOG.error(e.getMessage() + " optimizeSql: " + optimizeSql(sql, token) + 
					",sql: " + sql);
			throw new SqlHandlerException(e.getMessage());
		}
		try {
			String countSql = createCountSql(sql);
			LOG.debug("countSql: " + countSql);
			JSONArray countList = DbUtil.queryJSONData(dataSources.getConnection(), countSql);
			LOG.debug("total: " + countList);
			total = countList.getJSONObject(0).getInteger("TOTAL");
		} catch (SQLException e) {
			LOG.error(e.getMessage() + " countSql: " + createCountSql(sql) + 
					",sql: " + sql);
			throw new SqlHandlerException(e.getMessage());
		}
		if(null != list) {
			map.put("rows", list.get("rows"));
			map.put("headers", list.get("headers"));
			map.put("total", total);
		}else {
			LOG.error("list: " + list);
			throw new SqlHandlerException("未查询到结果");
		}
		return map;
	}
	
	/**
	 * 对sql语句进行优化
	 * token标识用户，不同的用户可能需要不同的优化
	 */
	protected abstract String optimizeSql(String sql, String token);
	
	/**
	 * 组装sql count语句
	 */
	private String createCountSql(String sql) {
		return "select count(*) TOTAL from (" + sql + ") " + SqlConfig.COUNT_SQL_ALIAS;
	}
	
	/**
	 * 获取用户数据
	 */
	protected UserData getUserData(String token) {
		ApplicationContext ac = ApplicationContextUtils.getAc();
		if(null != ac) {
			UserCacheData uc = ac.getBean(UserCacheData.class);
			if(null != uc) {
				return uc.getUserData(token);
			}else {
				LOG.debug("uc: " + uc);
			}
		}else {
			LOG.debug("ac: " + ac);
		}
		return null;
	}
	
	/**
	 * 获取limit
	 */
	protected int getLimit(String token) {
		UserData userData = getUserData(token);
		LOG.debug("userData: " + userData);
		return (null != userData) ? 
				(userData.getLimit() > 0) ? userData.getLimit() : SqlConfig.LIMIT 
						: SqlConfig.LIMIT;
	}

	//一般表示非select查询
	private Map<String, Object> executesql(Object pool, String sql, String token) 
			throws SqlHandlerException {
		IMyDataSources dataSources = (IMyDataSources) pool;
		Map<String, Object> map = new HashMap<String, Object>();
		
		//判断是不是多条语句
		if(haveMultiSql(sql)) {
			LOG.debug("type: 多语句查询");
			List<String> sqllist = Arrays.asList(sql.split(";"));
			try {
				int[] effectRows = DbUtil.executeBatchData(dataSources.getConnection(), sqllist);
				map.put("totalSql", sqllist.size());
				map.put("effectSql", effectRows.length);
				map.put("effectRows", effectRows);
				map.put("sqlType", "multi");
			} catch (SQLException e) {
				LOG.error("sqllist: " + sqllist);
				throw new SqlHandlerException(e.getMessage());
			}
		}else {
			LOG.debug("type: 单语句");
			Map<String, Object> smap = executeSingleSql(dataSources, sql);
			LOG.debug("single sql execute result: ");
			Object type = smap.get("type");
			if("set".equals(type)) {
				map.put("rows", smap.get("rows"));
				map.put("headers", smap.get("headers"));
			}
		}
		
		return map;
	}
	
	/**
	 * 执行单语句sql
	 * 返回结果：{
	 * 	type: 'set', //表示是否有返回值   'set' 有值  , 'noSet' 没有值
	 * }
	 */
	private Map<String, Object> executeSingleSql(IMyDataSources pool, String sql) throws SqlHandlerException{
		Map<String, Object> map = new HashMap<String, Object>();
		LOG.info("executeSingleSql: sql: " + sql);
		try {
			boolean b = DbUtil.execute(pool.getConnection(), sql);
			LOG.info("return result: " + b);
			if(b) {  //有返回结果，则继续查询
				Map<String, Object> arr = DbUtil.queryJSONMap(pool.getConnection(), sql);
				map.put("type", "set");
				LOG.debug("json result: " + arr);
				if(null != arr) {
					map.put("rows", arr.get("rows"));
					map.put("headers", arr.get("headers"));
				}
			}else {
				map.put("type", "noSet");
			}
		} catch (SQLException e) {
			throw new SqlHandlerException(e.getMessage());
		}
		return map;
	}
	
	/**
	 * 执行单sql语句的另一种方法     (过时的一种写法)
	 */
	@SuppressWarnings("unused")
	private Map<String, Object> executeSingeSqlOtherMethod(IMyDataSources pool, String sql) 
			throws SqlHandlerException{
		Map<String, Object> map = new HashMap<String, Object>();
		String upperSql = sql.toUpperCase();
		map.put("type", "noSet");
		if(upperSql.startsWith("INSERT")) {
			LOG.debug("----: insert");
			try {
				int effectInt = DbUtil.insert(pool.getConnection(), sql);
				LOG.info("effect: " + effectInt);
				if(1 == effectInt) {
					map.put("status", "success");
				}else if(0 == effectInt) {
					map.put("status", "fail");
				}
			} catch (SQLException e) {
				LOG.error("sql: " + sql);
				throw new SqlHandlerException(e.getMessage());
			}
		}else if(upperSql.startsWith("UPDATE")) {
			LOG.debug("----: update");
			try {
				int effectInt = DbUtil.update(pool.getConnection(), sql);
				LOG.info("effect: " + effectInt);
				if(1 == effectInt) {
					map.put("status", "success");
				}else if(0 == effectInt) {
					map.put("status", "fail");
				}
			} catch (SQLException e) {
				LOG.error("sql: " + sql);
				throw new SqlHandlerException(e.getMessage());
			}
			/**
			 * 对于没有返回结果的查询用此查询语句
			 */
		}else if(upperSql.startsWith("CREATE") 
				|| upperSql.startsWith("DROP") 
				|| upperSql.startsWith("USE") 
				|| upperSql.startsWith("DELETE")) {
			LOG.debug("----: create");
			try {
				DbUtil.execute(pool.getConnection(), sql);
				map.put("status", "success");
			} catch (SQLException e) {
				LOG.error("sql: " + sql);
				throw new SqlHandlerException(e.getMessage());
			}
		} else {
			LOG.debug("----: other");
			map.put("type", "set");
			try {
				Map<String, Object> arr = DbUtil.queryJSONMap(pool.getConnection(), sql);
				LOG.debug("json result: " + arr);
				if(null != arr) {
					map.put("rows", arr.get("rows"));
					map.put("headers", arr.get("headers"));
				}
			} catch (SQLException e) {
				LOG.error("sql: " + sql);
				throw new SqlHandlerException(e.getMessage());
			}
		}
		return map;
	}
	
	//判断是不是多条语句
	private boolean haveMultiSql(String sql) {
		boolean b = false;
		String[] sqls = sql.split(";");
		if(sqls.length > 1) {
			b = true;
		}
		return b;
	}

}

package com.github.jayuc.dbclient.act;

import java.sql.Connection;
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
	public Map<String, Object> query(Object pool, String sql, String token) 
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

	@Override
	public Map<String, Object> execute(Object pool, String sql, String token) 
			throws SqlHandlerException {
		IMyDataSources dataSources = (IMyDataSources) pool;
		Connection conn = null;
		try {
			conn = dataSources.getConnection();
		} catch (SQLException e) {
			LOG.error("conn: " + conn);
			throw new SqlHandlerException(e.getMessage());
		}
		Map<String, Object> map = new HashMap<String, Object>();
		
		//判断是不是多条语句
		if(haveMultiSql(sql)) {
			LOG.debug("type: 多语句查询");
			List<String> sqllist = Arrays.asList(sql.split(";"));
			try {
				int[] effectRows = DbUtil.executeBatchData(conn, sqllist);
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
			String upperSql = sql.toUpperCase();
			if(upperSql.startsWith("INSERT")) {
				LOG.debug("----: insert");
				try {
					int effectInt = DbUtil.insert(conn, sql);
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
			}else if(upperSql.startsWith("UPDATE") || upperSql.startsWith("DELETE")) {
				LOG.debug("----: update");
				try {
					int effectInt = DbUtil.update(conn, sql);
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
			}else {
				LOG.debug("----: other");
				try {
					Map<String, Object> arr = DbUtil.queryJSONMap(conn, sql);
					LOG.debug("json result: " + arr);
					if(null != arr) {
						map.put("rows", arr.get("rows"));
						map.put("headers", arr.get("headers"));
					}else {
						LOG.error("arr: " + arr);
						throw new SqlHandlerException("未查询到结果");
					}
				} catch (SQLException e) {
					e.printStackTrace();
					LOG.error("sql: " + sql);
					throw new SqlHandlerException(e.getMessage());
				}
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

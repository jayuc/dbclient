package com.github.jayuc.dbclient.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.github.jayuc.dbclient.entity.Result;
import com.github.jayuc.dbclient.err.PoolException;
import com.github.jayuc.dbclient.err.SqlHandlerException;
import com.github.jayuc.dbclient.iter.IDBPoolsManager;
import com.github.jayuc.dbclient.iter.IDbPool;
import com.github.jayuc.dbclient.iter.ISqlHandler;
import com.github.jayuc.dbclient.param.SqlDataParam;
import com.github.jayuc.dbclient.utils.ApplicationContextUtils;
import com.github.jayuc.dbclient.utils.ResultUtils;
import com.github.jayuc.dbclient.utils.StringUtil;

/**
 * 执行sql语句
 * @author yujie
 *
 */
@RestController
@RequestMapping("/sql")
public class SqlExecuteController {
	
	private static final Logger LOG = LoggerFactory.getLogger(SqlExecuteController.class);
	
	@Autowired
	IDBPoolsManager dbPoolManager;
	
	/**
	 * 查询
	 * @param sql
	 * @return
	 */
	@GetMapping("/execute")
	public Map<String, Object> execute(@ModelAttribute("param") SqlDataParam param){
		LOG.debug("开始执行,sql: " + param.getSql());
		//开始时间
		long executeStartTime = System.currentTimeMillis();
		Result result = ResultUtils.normalResult();
		String tike = getTikeByParam(param);
		IDbPool pool = null;
		try {
			pool = dbPoolManager.getDbPool(tike);
		} catch (PoolException e) {
			result.setError(e.getMessage());
			LOG.error(e.getMessage() + param);
			e.printStackTrace();
		}
		if(null != pool && !StringUtil.isBlank(param.getSql())) {
			//去掉sql空格
			final String finalSql = param.getSql().trim();
			ApplicationContext ac = ApplicationContextUtils.getAc();
			if(null != ac) {
				ISqlHandler handler = ac.getBean(pool.getType().getHandlerName(), ISqlHandler.class);
				if(null != handler) {
					try {
						String upperSql = finalSql.toUpperCase();
						/**
						 * sql语句分为有查询结果的和无查询结果的两种sql
						 */
						if(upperSql.startsWith("SELECT")) {  //sql语句分为有查询结果的比如：select语句；无查询结果的比如：delete,insert,update
							LOG.debug("sql type: select");
							Map<String, Object> map = handler.query(pool.getPool(), finalSql, param.getToken());
							int total = 0;
							if(null != map.get("total")) {
								total = (int)map.get("total");
							}
							result.setResult(total, map.get("rows"));
							result.setResultProperty("headers", map.get("headers"));
						}else {  //当sql语句为 delete,insert,update
							LOG.debug("sql type: 非select");
							Map<String, Object> map = handler.execute(pool.getPool(), finalSql, param.getToken());
							LOG.info("结果: " + map);
							Object otype = map.get("sqlType");
							if("multi".equals(otype)) {
								LOG.info("tt:multi");
								result.setProperty("totalSql", map.get("totalSql"));
								result.setProperty("effectSql", map.get("effectSql"));
								result.setProperty("effectRows", map.get("effectRows"));
							}else {
								LOG.info("tt:singel");
								Object rows = map.get("rows");
								if(null != rows) {
									JSONArray arrs = (JSONArray) rows;
									result.setResult(arrs.size(), map.get("rows"));
									result.setResultProperty("headers", map.get("headers"));
								}
							}
						}
					} catch (SqlHandlerException e) {
						result.setError(e.getMessage() + finalSql);
						LOG.error(e.getMessage() + ",sql: " + finalSql);
						e.printStackTrace();
					}
				}else {
					LOG.error("未找到对应的handler,type: " + pool.getType().getHandlerName());
				}
			}else {
				LOG.error("ac为空");
			}
		}else {
			LOG.error("pool或者sql为空,pool: " + pool + " ，sql: " + param.getSql());
		}
		//结束时间
		long executeEndTime = System.currentTimeMillis();
		result.setProperty("took", (executeEndTime - executeStartTime));
		LOG.debug("执行结束");
		return result.getResult();
	}
	
	//获取token和dbId的组合
	private String getTikeByParam(SqlDataParam param) {
		if(StringUtil.isBlank(param.getDbId()) || StringUtil.isBlank(param.getToken())) {
			return null;
		}
		return param.getToken() + param.getDbId();
	}

}

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
			ApplicationContext ac = ApplicationContextUtils.getAc();
			if(null != ac) {
				ISqlHandler handler = ac.getBean(pool.getType().getHandlerName(), ISqlHandler.class);
				if(null != handler) {
					try {
						String upperSql = param.getSql().toUpperCase();
						/**
						 * sql语句分为有查询结果的和无查询结果的两种sql
						 */
						if(upperSql.startsWith("SELECT")) {  //sql语句分为有查询结果的比如：select语句；无查询结果的比如：delete,insert,update
							Map<String, Object> map = handler.query(pool.getPool(), param.getSql(), param.getToken());
							int total = 0;
							if(null != map.get("total")) {
								total = (int)map.get("total");
							}
							result.setResult(total, map.get("rows"));
							result.setResultProperty("headers", map.get("headers"));
						}else {  //当sql语句为 delete,insert,update
							
						}
					} catch (SqlHandlerException e) {
						result.setError(e.getMessage());
						LOG.error(e.getMessage() + ",sql: " + param.getSql());
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

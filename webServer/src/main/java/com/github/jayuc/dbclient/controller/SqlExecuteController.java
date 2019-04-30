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
			//标记数据库类型
			result.setProperty("dbType", pool.getType().getName());
			//去掉sql空格
			final String finalSql = param.getSql().trim();
			ApplicationContext ac = ApplicationContextUtils.getAc();
			if(null != ac) {
				ISqlHandler handler = ac.getBean(pool.getType().getHandlerName(), ISqlHandler.class);
				if(null != handler) {
					try {
						Map<String, Object> map = handler.execute(pool.getPool(), finalSql, param.getToken());
						/**
						 * 返回结果说明
						 * total: 结果总数
						 * rows: 结果数组
						 * totalSql： 总的执行sql语句数（每个sql之间用;隔开）
						 * effectSql： 影响的sql语句总数
						 * effectRows：影响的sql
						 */
						LOG.info("结果： " + map);
						if(null != map) {
							Object otype = map.get("sqlType");
							if("multi".equals(otype)) {
								LOG.info("tt:multi");
								result.setProperty("totalSql", map.get("totalSql"));
								result.setProperty("effectSql", map.get("effectSql"));
								result.setProperty("effectRows", map.get("effectRows"));
							}else {
								LOG.info("tt:singel");
								if(null != map.get("rows")) {
									int total = 0;
									if(null != map.get("total")) {
										total = (int)map.get("total");
									}else {
										total = ((JSONArray) map.get("rows")).size();
									}
									result.setResult(total, map.get("rows"));
									result.setResultProperty("headers", map.get("headers"));
								}
							}
						}
					} catch (SqlHandlerException e) {
						result.setError(e.getMessage());
						LOG.error(e.getMessage() + " ,sql: " + finalSql);
						e.printStackTrace();
					}
				}else {
					LOG.error("未找到对应的handler,type: " + pool.getType().getHandlerName());
					result.setError("未找到合适的sql处理器");
				}
			}else {
				LOG.error("ac为空");
				result.setError("ac为空");
			}
		}else {
			if(null == pool) {
				LOG.error("pool为空");
				result.setError("尚未创建连接");
				result.setProperty("go_home_page", "yes");
			}else {
				LOG.error("sql为空");
				result.setError("sql为空");
			}
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

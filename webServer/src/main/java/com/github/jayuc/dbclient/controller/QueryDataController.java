package com.github.jayuc.dbclient.controller;

import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.github.jayuc.dbclient.entity.Result;
import com.github.jayuc.dbclient.err.PoolException;
import com.github.jayuc.dbclient.iter.IDBPoolsManager;
import com.github.jayuc.dbclient.iter.IDbPool;
import com.github.jayuc.dbclient.iter.IMyDataSources;
import com.github.jayuc.dbclient.param.QueryDataParam;
import com.github.jayuc.dbclient.utils.DbUtil;
import com.github.jayuc.dbclient.utils.ResultUtils;
import com.github.jayuc.dbclient.utils.StringUtil;

/**
 * 根据sql语句查询结果
 * @author yujie
 *
 */
@RestController
@RequestMapping("/data")
public class QueryDataController {
	
	private static final Logger LOG = LoggerFactory.getLogger(QueryDataController.class);
	
	@Autowired
	IDBPoolsManager dbPoolManager;
	
	/**
	 * 查询
	 * @param sql
	 * @return
	 */
	@GetMapping("/query")
	public Map<String, Object> query(@ModelAttribute("param") QueryDataParam param){
		LOG.debug("开始查询,sql: " + param.getSql());
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
			IMyDataSources dataSource = (IMyDataSources) pool.getPool();
			if(null == dataSource) {
				LOG.info("dataSource为空");
			}else {
				try {
					JSONArray list = DbUtil.queryJSONData(dataSource.getConnection(), optimizeSql(param.getSql()));
					result.setResult(list.size(), list);
				} catch (SQLException e) {
					result.setError("执行数据库查询语句时失败了");
					LOG.error("执行数据库查询语句失败了: " + param.getSql());
					e.printStackTrace();
				}
			}
		}
		LOG.debug("查询结束");
		return result.getResult();
	}
	
	//sql语句执行前检查与优化
	private String optimizeSql(String sql) {
		return sql;
	}
	
	//获取token和dbId的组合
	private String getTikeByParam(QueryDataParam param) {
		if(StringUtil.isBlank(param.getDbId()) || StringUtil.isBlank(param.getToken())) {
			return null;
		}
		return param.getToken() + param.getDbId();
	}

}

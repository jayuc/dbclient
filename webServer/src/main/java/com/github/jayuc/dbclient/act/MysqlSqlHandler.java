package com.github.jayuc.dbclient.act;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.github.jayuc.dbclient.err.SqlHandlerException;
import com.github.jayuc.dbclient.iter.IMyDataSources;
import com.github.jayuc.dbclient.iter.ISqlHandler;
import com.github.jayuc.dbclient.utils.DbUtil;

/**
 * mysql sql语句处理
 * @author yujie
 * 2019年4月10日 上午10:42:59
 */
@Component("mysqlHandler")
public class MysqlSqlHandler implements ISqlHandler {

	@Override
	public Map<String, Object> query(Object pool, String sql) throws SqlHandlerException {
		IMyDataSources dataSources = (IMyDataSources) pool;
		JSONArray list = null;
		try {
			list = DbUtil.queryJSONData(dataSources.getConnection(), sql);
		} catch (Exception e) {
			System.out.println("----------: " + e.getMessage());
			//e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		//map.put("total", 1);
		return map;
	}

	@Override
	public Map<String, Object> execute(Object pool, String sql) throws SqlHandlerException {
		return null;
	}

}

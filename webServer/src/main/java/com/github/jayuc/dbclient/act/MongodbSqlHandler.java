package com.github.jayuc.dbclient.act;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.github.jayuc.dbclient.err.SqlHandlerException;
import com.github.jayuc.dbclient.iter.ISqlHandler;

/**
 * mogodb 处理器
 * @author yujie
 *
 */
@Component("mongodbHandler")
public class MongodbSqlHandler implements ISqlHandler {

	@Override
	public Map<String, Object> execute(Object pool, String sql, String token) 
			throws SqlHandlerException {
		return null;
	}

}

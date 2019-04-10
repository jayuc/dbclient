package com.github.jayuc.dbclient.act;

import org.springframework.stereotype.Component;

/**
 * mysql 处理器
 * @author yujie
 * 2019年4月10日 下午4:03:55
 */
@Component("mysqlHandler")
public class MysqlSqlHandler extends AbstractSqlHandler {

	@Override
	protected String optimizeSql(String sql, String token) {
		String upperSql = sql.toUpperCase();
		if(upperSql.contains("LIMIT")) {
			return sql;
		}
		return "select * from (" + sql + ") AA_AA limit " + getLimit(token);
	}

}

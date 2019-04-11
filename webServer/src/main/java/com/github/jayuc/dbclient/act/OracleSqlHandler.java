package com.github.jayuc.dbclient.act;

import org.springframework.stereotype.Component;

/**
 * oracle 处理器
 * @author yujie
 * 2019年4月11日 下午3:46:48
 */
@Component("oracleHandler")
public class OracleSqlHandler extends AbstractSqlHandler {

	@Override
	protected String optimizeSql(String sql, String token) {
		String upperSql = sql.toUpperCase();
		if(upperSql.contains("ROWNUM")) {
			return sql;
		}
		return "select * from (" + sql + ") where rownum <= " + getLimit(token);
	}

}

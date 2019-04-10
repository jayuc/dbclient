package com.github.jayuc.dbclient.param;

import lombok.Data;
import lombok.ToString;

/**
 * 数据查询参数
 * @author yujie
 *
 */
@Data
@ToString
public class SqlDataParam {

	//查询sql语句
	private String sql;
	
	private String token;

	//查询数据库id
	private String dbId;
	
}

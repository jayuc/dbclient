package com.github.jayuc.dbclient.act;

import org.springframework.stereotype.Component;

import com.github.jayuc.dbclient.entity.DbPool;
import com.github.jayuc.dbclient.iter.IDbConfig;
import com.github.jayuc.dbclient.iter.IDbCreate;
import com.github.jayuc.dbclient.iter.IDbPool;

/**
 * mysql 数据库连接池创建
 * @author jayu
 *
 */
@Component("mysql")
public class MysqlCreater implements IDbCreate {

	@Override
	public IDbPool create(IDbConfig config) {
		return new DbPool();
	}

}

package com.github.jayuc.dbclient.act;

import org.springframework.stereotype.Component;

import com.github.jayuc.dbclient.entity.DbPool;
import com.github.jayuc.dbclient.iter.IDbConfig;
import com.github.jayuc.dbclient.iter.IDbCreate;
import com.github.jayuc.dbclient.iter.IDbPool;

/**
 * redis 数据库连接池创建者
 * @author jayu
 *
 */
@Component("redis")
public class RedisCreater implements IDbCreate {

	@Override
	public IDbPool create(IDbConfig config) {
		return new DbPool();
	}

}

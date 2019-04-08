package com.github.jayuc.dbclient.act;

import org.springframework.stereotype.Component;

import com.github.jayuc.dbclient.entity.DbPool;
import com.github.jayuc.dbclient.err.PoolException;
import com.github.jayuc.dbclient.iter.IDbConfig;
import com.github.jayuc.dbclient.iter.IDbCreate;
import com.github.jayuc.dbclient.iter.IDbPool;

/**
 * oracle 数据库连接创造器
 * @author jayu
 *
 */
@Component("oracle")
public class OracleCreater implements IDbCreate {

	@Override
	public IDbPool create(IDbConfig config) throws PoolException {
		return new DbPool();
	}

}

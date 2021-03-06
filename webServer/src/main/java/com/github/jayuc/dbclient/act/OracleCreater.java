package com.github.jayuc.dbclient.act;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.jayuc.dbclient.entity.DbPool;
import com.github.jayuc.dbclient.err.PoolException;
import com.github.jayuc.dbclient.iter.IDbConfig;
import com.github.jayuc.dbclient.iter.IDbCreate;
import com.github.jayuc.dbclient.iter.IDbPool;
import com.github.jayuc.dbclient.iter.IMyDataSources;

/**
 * oracle 数据库连接创造器
 * @author jayu
 *
 */
@Component("oracleCreater")
public class OracleCreater implements IDbCreate {
	
	private static final Logger LOG = LoggerFactory.getLogger(OracleCreater.class);

	@Override
	public IDbPool create(IDbConfig config) throws PoolException {
		LOG.debug("oracle create start");
		DbPool pool = new DbPool();
		pool.setType(config.getType());
		IMyDataSources ds = new MyOracleDataSource();
		ds.init(config);
		pool.setPool(ds);
		LOG.debug("oracle create end");
		return pool;
	}

}

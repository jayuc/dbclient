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

@Component("postgresqlCreater")
public class PostgresqlCreater implements IDbCreate {
	
	private static final Logger LOG = LoggerFactory.getLogger(PostgresqlCreater.class);

	@Override
	public IDbPool create(IDbConfig config) throws PoolException {
		LOG.debug("postgresql create start");
		DbPool pool = new DbPool();
		pool.setType(config.getType());
		IMyDataSources ds = new PostgresqlDataSource();
		ds.init(config);
		pool.setPool(ds);
		LOG.debug("postgresql create end");
		return pool;
	}

}

package com.github.jayuc.dbclient.act;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.github.jayuc.dbclient.err.PoolException;
import com.github.jayuc.dbclient.iter.IDBPoolsManager;
import com.github.jayuc.dbclient.iter.IDbConfig;
import com.github.jayuc.dbclient.iter.IDbPool;

/**
 * 抽象数据库连接池管理类
 * @author jayu
 *
 */
public abstract class AbstractDBPoolsManager implements IDBPoolsManager {
	
	private Map<String, IDbPool> reposity = new ConcurrentHashMap<String, IDbPool>();

	@Override
	public IDbPool getDbPool(String token) throws PoolException {
		return reposity.get(token);
	}

	@Override
	public boolean setDbPool(IDbConfig config) throws PoolException {
		return false;
	}

	@Override
	public boolean removeDbPool(String token) throws PoolException {
		reposity.remove(token);
		return true;
	}
	
	protected abstract IDbPool createPool(IDbConfig config) throws PoolException;

}

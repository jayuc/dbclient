package com.github.jayuc.dbclient.act;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.github.jayuc.dbclient.err.PoolException;
import com.github.jayuc.dbclient.iter.IDBPoolsManager;
import com.github.jayuc.dbclient.iter.IDbConfig;
import com.github.jayuc.dbclient.iter.IDbPool;
import com.github.jayuc.dbclient.utils.IdUtils;

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
		String id = IdUtils.generateId();
		IDbPool pool = createPool(config);
		if(null == pool) {
			throw new PoolException("pool为空");
		}else {
			reposity.put(id, pool);
			return true;
		}
	}

	@Override
	public boolean removeDbPool(String token) throws PoolException {
		reposity.remove(token);
		return true;
	}
	
	protected abstract IDbPool createPool(IDbConfig config) throws PoolException;

}

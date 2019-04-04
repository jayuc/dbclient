package com.github.jayuc.dbclient.act;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.github.jayuc.dbclient.err.PoolException;
import com.github.jayuc.dbclient.iter.IDBPoolsManager;
import com.github.jayuc.dbclient.iter.IDbPool;

/**
 * 数据库连接池管理对象
 * @author yujie
 *
 */
@Component
public class DefaultDBPoolsManager implements IDBPoolsManager {
	
	private Map<String, IDbPool> reposity = new ConcurrentHashMap<String, IDbPool>();

	@Override
	public IDbPool getDbPool(String token) throws PoolException {
		vilidate();
		return reposity.get(token);
	}

	@Override
	public boolean setDbPool(String token, IDbPool pool) throws PoolException {
		vilidate();
		reposity.put(token, pool);
		return true;
	}

	@Override
	public boolean removeDbPool(String token) throws PoolException {
		vilidate();
		reposity.remove(token);
		return true;
	}
	
	private void vilidate() throws PoolException {
		if(null == reposity) {
			throw new PoolException("仓库为空");
		}
	}

}

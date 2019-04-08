package com.github.jayuc.dbclient.act;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	private static final Logger log = LoggerFactory.getLogger(AbstractDBPoolsManager.class);

	@Override
	public IDbPool getDbPool(String token) throws PoolException {
		log.debug("获取pool,id为: " + token);
		return reposity.get(token);
	}

	@Override
	public String setDbPool(IDbConfig config) throws PoolException {
		if(null != config.getToken() && reposity.containsKey(config.getToken())) {
			throw new PoolException("token已经存在");
		}
		String id = IdUtils.generateId();
		IDbPool pool = createPool(config);
		log.debug(id + " ----- " + pool);
		if(null == pool) {
			throw new PoolException("pool为空");
		}else {
			if(!reposity.containsKey(id)) {
				log.debug("向reposity中添加id为 " + id);
				reposity.put(id, pool);
			}else {
				log.info("reposity中已经包括此id: " + id);
			}
			return id;
		}
	}

	@Override
	public boolean removeDbPool(String token) throws PoolException {
		log.debug("删除pool,id为: " + token);
		reposity.remove(token);
		return true;
	}
	
	protected abstract IDbPool createPool(IDbConfig config) throws PoolException;

}

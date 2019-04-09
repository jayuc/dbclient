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
		String token = config.getToken();
		String urlId = getUrlId(config);
		if(null != token && reposity.containsKey(token + urlId)) {
			log.debug("token已经存在: " + token);
			return "token已经存在";
		}
		String id = (null == token) ? IdUtils.generateId() : token;
		IDbPool pool = createPool(config);
		log.debug(id + " ----- " + pool);
		if(null == pool) {
			throw new PoolException("pool为空");
		}else {
			String key = id + urlId;
			if(!reposity.containsKey(key)) {
				log.debug("向reposity中添加id为 " + key);
				reposity.put(key, pool);
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

	/**
	 * 数据库地址等参数生成一个唯一标识
	 * @param config
	 * @return
	 */
	protected String getUrlId(IDbConfig config) {
		String host = config.getHost().replace(".", "_");
		return config.getType().getName() + "_" + host + "_" + config.getPort() + "_" +
				config.getName() + "_" + config.getUserName();
	}
	
	protected abstract IDbPool createPool(IDbConfig config) throws PoolException;

}

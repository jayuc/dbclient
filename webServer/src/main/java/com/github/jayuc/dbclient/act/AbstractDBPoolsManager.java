package com.github.jayuc.dbclient.act;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jayuc.dbclient.err.PoolException;
import com.github.jayuc.dbclient.iter.IDBPoolsManager;
import com.github.jayuc.dbclient.iter.IDbConfig;
import com.github.jayuc.dbclient.iter.IDbPool;
import com.github.jayuc.dbclient.utils.IdUtils;
import com.github.jayuc.dbclient.utils.StringUtil;

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
		if(StringUtil.isBlank(token)) {
			throw new PoolException("token或dbId为空");
		}
		return reposity.get(token);
	}

	@Override
	public Map<String, Object> setDbPool(IDbConfig config) throws PoolException {
		String token = config.getToken();
		String urlId = getUrlId(config);
		Map<String, Object> map = new HashMap<String, Object>();
		String id = token;
		map.put("dbId", urlId);
		if(null == token) {
			id = IdUtils.generateId();
			map.put("token", id);
		}else if(reposity.containsKey(token + urlId)) {
			log.debug("token已经存在: " + token);
			map.put("tip", "token已存在");
			return map;
		}
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
			return map;
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
		String url = config.getType().getName() + "_" + host + "_" + config.getPort();
		if("redis".equals(config.getType().getName())) {
			return url;
		}
		return  url + "_" + config.getName() + "_" + config.getUserName();
	}
	
	protected abstract IDbPool createPool(IDbConfig config) throws PoolException;

}

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
	
	//数据库连接池仓库
	private Map<String, IDbPool> reposity = new ConcurrentHashMap<String, IDbPool>();
	
	//密码仓库
	private Map<String, String> passwrodReposity = new ConcurrentHashMap<String, String>();
	
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
		String key = token + urlId;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dbId", urlId);
		if(StringUtil.isBlank(token)) {
			log.debug("token为空，需要生成新的token");
			token = IdUtils.generateId();
			map.put("token", token);
		}else if(reposity.containsKey(key)) {
			//验证密码是否正确
			checkPassword(config, urlId);
			log.debug("token已经存在: " + token);
			map.put("tip", "token已存在");
			return map;
		}
		IDbPool pool = createPool(config);
		log.debug(token + " ----- " + pool);
		if(null == pool) {
			throw new PoolException("pool为空");
		}else {
			//此时的keys 保证token有值
			String keys = token + urlId;
			log.info("reposity 是否包含 key(" + keys + "): " + reposity.containsKey(keys));
			if(!reposity.containsKey(keys)) {
				log.debug("向reposity中添加id为 " + keys);
				reposity.put(keys, pool);
				log.debug("向password reposity仓库中添加，key: " + urlId);
				passwrodReposity.put(urlId, config.getPassword());
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
	 * 判断密码是否正确，不正确则抛出异常
	 * @param config
	 * @param urlId
	 * @throws PoolException
	 */
	protected void checkPassword(IDbConfig config, String urlId) throws PoolException {
		log.debug("reposity password: " + passwrodReposity.get(urlId));
		log.debug("config password: " + config.getPassword());
		if(!passwrodReposity.get(urlId).equals(config.getPassword())) {
			throw new PoolException("密码不正确");
		}
	}

	/**
	 * 数据库地址等参数生成一个唯一标识
	 * @param config
	 * @return
	 */
	protected String getUrlId(IDbConfig config) {
		String host = config.getHost().replace(".", "_");
		String url = config.getType().getName() + "_" + host + "_" 
				+ config.getPort() + "_" + config.getName();
		if("redis".equals(config.getType().getName())) {
			return url;
		}
		return  url + "_" + config.getUserName();
	}
	
	protected abstract IDbPool createPool(IDbConfig config) throws PoolException;

}

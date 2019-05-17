package com.github.jayuc.dbclient.act;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.jayuc.dbclient.config.RedisConfig;
import com.github.jayuc.dbclient.entity.DbPool;
import com.github.jayuc.dbclient.entity.RedisObj;
import com.github.jayuc.dbclient.err.PoolException;
import com.github.jayuc.dbclient.iter.IDbConfig;
import com.github.jayuc.dbclient.iter.IDbCreate;
import com.github.jayuc.dbclient.iter.IDbPool;
import com.github.jayuc.dbclient.utils.StringUtil;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis 数据库连接池创建者
 * @author jayu
 *
 */
@Component("redisCreater")
public class RedisCreater implements IDbCreate {
	
	private final static Logger LOG = LoggerFactory.getLogger(RedisCreater.class);

	@SuppressWarnings({ "unused" })
	@Override
	public IDbPool create(IDbConfig config) throws PoolException {
		LOG.debug("redis create start");
		DbPool pool = new DbPool();
		pool.setType(config.getType());
		JedisPoolConfig redisConfig = new JedisPoolConfig();
		 //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
		redisConfig.setTestOnBorrow(RedisConfig.QUERY_VALIDATE);
		// 选择的库
		int dbNum = 0;
		try {
			dbNum = Integer.valueOf(config.getName());
		} catch (Exception e) {
			throw new PoolException("redis非法库");
		}
		RedisObj redisObj = new RedisObj();
		JedisPool redisPool = null;
		if(StringUtil.isBlank(config.getPassword())) {
			redisPool = new JedisPool(redisConfig, config.getHost(), config.getPort(), 
					RedisConfig.TIME_OUT);
		}else {
			redisPool = new JedisPool(redisConfig, config.getHost(), 
					config.getPort(), RedisConfig.TIME_OUT, config.getPassword());
		}
		
		if(null == redisPool) {
			throw new PoolException("redis连接池创建失败");
		}else {
			redisObj.setJedisPool(redisPool);
			redisObj.setDbNum(dbNum);
		}
		
		try {
			redisPool.getResource();
		} catch (Exception e) {
			throw new PoolException("redis连接池创建失败了");
		}
		pool.setPool(redisObj);
		LOG.debug("redis create end");
		return pool;
	}

}

package com.github.jayuc.dbclient.act;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.jayuc.dbclient.config.RedisConfig;
import com.github.jayuc.dbclient.entity.DbPool;
import com.github.jayuc.dbclient.err.PoolException;
import com.github.jayuc.dbclient.iter.IDbConfig;
import com.github.jayuc.dbclient.iter.IDbCreate;
import com.github.jayuc.dbclient.iter.IDbPool;

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

	@SuppressWarnings("resource")
	@Override
	public IDbPool create(IDbConfig config) throws PoolException {
		LOG.debug("redis create start");
		DbPool pool = new DbPool();
		pool.setType(config.getType());
		JedisPoolConfig redisConfig = new JedisPoolConfig();
		 //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
		redisConfig.setTestOnBorrow(RedisConfig.QUERY_VALIDATE);
		JedisPool redisPool = new JedisPool(redisConfig, config.getHost(), 
				config.getPort(), RedisConfig.TIME_OUT, config.getPassword());
		try {
			redisPool.getResource();
		} catch (Exception e) {
			throw new PoolException("redis连接池创建失败了");
		}
		pool.setPool(redisPool);
		LOG.debug("redis create end");
		return pool;
	}

}

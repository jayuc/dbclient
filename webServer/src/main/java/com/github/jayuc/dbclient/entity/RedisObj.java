package com.github.jayuc.dbclient.entity;

import lombok.Data;
import redis.clients.jedis.JedisPool;

/**
 * redis连接池对象
 * @author yujie
 *
 */
@Data
public class RedisObj {
	
	private JedisPool jedisPool;
	
	private int dbNum;

}

package com.github.jayuc.dbclient.act;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.jayuc.dbclient.entity.DbPool;
import com.github.jayuc.dbclient.entity.MongoObj;
import com.github.jayuc.dbclient.err.PoolException;
import com.github.jayuc.dbclient.iter.IDbConfig;
import com.github.jayuc.dbclient.iter.IDbCreate;
import com.github.jayuc.dbclient.iter.IDbPool;
import com.github.jayuc.dbclient.utils.StringUtil;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoIterable;

/**
 * mongodb 创造数据库
 * @author yujie
 *
 */
@Component("mongodbCreater")
public class MongodbCreater implements IDbCreate {
	
	private final static Logger LOG = LoggerFactory.getLogger(MongodbCreater.class);

	@SuppressWarnings("resource")
	@Override
	public IDbPool create(IDbConfig config) throws PoolException {
		LOG.debug("mongodb create start");
		DbPool pool = new DbPool();
		pool.setType(config.getType());
		
		MongoObj obj = null;
		try {
			Builder options = new MongoClientOptions.Builder();
			options.serverSelectionTimeout(1000);
			options.build();
			MongoClient client = new MongoClient(new MongoClientURI(getUrl(config), options));
			client.getConnectPoint();
			client.getReadConcern();
			MongoIterable<String> list = client.listDatabaseNames();
			String dbName = config.getName();
			boolean containName = false;
			for(String name:list) {
				if(dbName.equals(name)) {
					containName = true;
				}
			}
			if(!containName) {
				throw new PoolException("数据库名不正确");
			}
			obj = new MongoObj(client, config.getName());
			LOG.debug("mongodb connect successfully");
		} catch (Exception e) {
			throw new PoolException(e.getMessage());
		}
		pool.setPool(obj);
		
		LOG.debug("mongodb create end");
		return pool;
	}
	
	// 获取url
	private String getUrl(IDbConfig config) {
		StringBuilder sb = new StringBuilder();
		sb.append("mongodb://");
		if(!StringUtil.isBlank(config.getUserName()) 
				&& !StringUtil.isBlank(config.getPassword())) {
			sb.append(config.getUserName());
			sb.append(":");
			sb.append(config.getPassword());
			sb.append("@");
		}
		sb.append(config.getHost());
		sb.append(":");
		sb.append(config.getPort());
		return sb.toString();
	}

}

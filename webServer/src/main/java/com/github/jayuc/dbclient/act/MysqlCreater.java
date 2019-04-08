package com.github.jayuc.dbclient.act;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.jayuc.dbclient.entity.DbPool;
import com.github.jayuc.dbclient.err.PoolException;
import com.github.jayuc.dbclient.iter.IDbConfig;
import com.github.jayuc.dbclient.iter.IDbCreate;
import com.github.jayuc.dbclient.iter.IDbPool;

/**
 * mysql 数据库连接池创建
 * @author jayu
 *
 */
@Component("mysql")
public class MysqlCreater implements IDbCreate {
	
	private static final Logger log = LoggerFactory.getLogger(MysqlCreater.class);
	
	public static final String driver = "com.mysql.jdbc.Driver";
	
	public static final String extra = "?&useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=false&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true";

	@Override
	public IDbPool create(IDbConfig config) throws PoolException {
		log.debug("mysql create start");
		DbPool pool = new DbPool();
		pool.setType(config.getType());
		DruidDataSource ds = new DruidDataSource();
		ds.setMaxWait(6000);
		ds.setTestOnReturn(false);
		ds.setDriverClassName(driver);
		ds.setUrl(getUrl(config));
		ds.setUsername(config.getUserName());
		ds.setPassword(config.getPassword());
		try {
			trySql(ds);
		} catch (SQLException e) {
			throw new PoolException("数据路连接失败了");
		}
		pool.setPool(ds);
		log.debug("mysql create end");
		return pool;
	}
	
	/**
	 * 拼装url地址
	 * @param config
	 * @return
	 */
	private String getUrl(IDbConfig config) {
		return "jdbc:mysql://" + config.getHost() + ":" 
				+ config.getPort() + "/" + config.getName() + extra;
	}
	
	private void trySql(DruidDataSource ds) throws SQLException {
		ds.getConnection();
	}

}

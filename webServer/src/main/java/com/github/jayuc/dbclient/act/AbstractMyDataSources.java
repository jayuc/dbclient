package com.github.jayuc.dbclient.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.jayuc.dbclient.err.PoolException;
import com.github.jayuc.dbclient.iter.IDbConfig;
import com.github.jayuc.dbclient.iter.IMyDataSources;

/**
 * 抽象数据库连接池，使druid实现，以后需要换druid只需要修改此类
 * @author yujie
 *
 */
public abstract class AbstractMyDataSources implements IMyDataSources {
	
	private DruidDataSource dataSource = null;

	@Override
	public Connection getConnection() throws SQLException {
		if(null != dataSource) {
			return dataSource.getConnection();
		}
		return null;
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return null;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	@Override
	public void init(IDbConfig config) throws PoolException {
		dataSource = new DruidDataSource();
		dataSource.setDriverClassName(getDriver());
		dataSource.setUrl(getUrl(config));
		dataSource.setUsername(config.getUserName());
		dataSource.setPassword(config.getPassword());
		try {
			dataSource.init();
			dataSource.createPhysicalConnection();
		} catch (SQLException e) {
			throw new PoolException("创建数据库连接池时失败了");
		}
	}
	
	protected abstract String getUrl(IDbConfig config);
	
	protected abstract String getDriver();

}

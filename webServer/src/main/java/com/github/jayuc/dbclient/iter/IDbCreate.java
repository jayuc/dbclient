package com.github.jayuc.dbclient.iter;

import com.github.jayuc.dbclient.err.PoolException;

public interface IDbCreate {
	
	/**
	 * 根据配置创建数据库连接池
	 * @param config 配置参数
	 * @return
	 */
	IDbPool create(IDbConfig config) throws PoolException;

}

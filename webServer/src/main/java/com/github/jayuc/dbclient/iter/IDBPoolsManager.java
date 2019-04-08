package com.github.jayuc.dbclient.iter;

import com.github.jayuc.dbclient.err.PoolException;

/**
 * 数据库连接池管理对象
 * @author yujie
 *
 */
public interface IDBPoolsManager {

	IDbPool getDbPool(String token) throws PoolException;
	
	//返回token
	String setDbPool(IDbConfig config) throws PoolException;
	
	boolean removeDbPool(String token) throws PoolException;
	
}

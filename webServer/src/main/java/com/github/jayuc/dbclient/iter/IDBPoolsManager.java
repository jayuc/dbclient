package com.github.jayuc.dbclient.iter;

import com.github.jayuc.dbclient.err.PoolException;

/**
 * 数据库连接池管理对象
 * @author yujie
 *
 */
public interface IDBPoolsManager {

	IDbPool getDbPool(String token) throws PoolException;
	
	boolean setDbPool(String token, IDbPool pool) throws PoolException;
	
	boolean removeDbPool(String token) throws PoolException;
	
}

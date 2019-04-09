package com.github.jayuc.dbclient.iter;

import javax.sql.DataSource;

import com.github.jayuc.dbclient.err.PoolException;

/**
 * 规范数据库连接池接口，
 * druid数据库连接池中没有判断是否连接成功，并且在创建连接不成功后会不停的尝试创建连接，故是否应该使用druid是个问题？
 * 暂时先使用druid,但定义一个标准接口，方便以后替换druid
 * @author yujie
 *
 */
public interface IMyDataSources extends DataSource {

	/**
	 * 包含一个重要功能：判断连接池是否成功创建，不成功则抛出异常
	 * 注意：druid数据源在创建失败的时候不会抛异常，只会在getConnection才会抛出异常，
	 * 本应用中需要在初始化的时候就要知道是否异常
	 * @param config
	 * @throws PoolException
	 */
	void init(IDbConfig config) throws PoolException;
	
}

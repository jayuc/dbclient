package com.github.jayuc.dbclient.act;

import org.springframework.stereotype.Component;

import com.github.jayuc.dbclient.err.PoolException;
import com.github.jayuc.dbclient.iter.IDbConfig;
import com.github.jayuc.dbclient.iter.IDbPool;

/**
 * 默认数据库连接池管理工具
 * @author jayu
 *
 */

@Component
public class DefaultDBPoolsManager extends AbstractDBPoolsManager {

	@Override
	protected IDbPool createPool(IDbConfig config) throws PoolException {
		return null;
	}

}

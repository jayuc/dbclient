package com.github.jayuc.dbclient.act;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.github.jayuc.dbclient.err.PoolException;
import com.github.jayuc.dbclient.iter.IDbConfig;
import com.github.jayuc.dbclient.iter.IDbCreate;
import com.github.jayuc.dbclient.iter.IDbPool;
import com.github.jayuc.dbclient.utils.ApplicationContextUtils;
import com.github.jayuc.dbclient.utils.DbTypeUtils;

/**
 * 默认数据库连接池管理工具
 * @author jayu
 *
 */

@Component
public class DefaultDBPoolsManager extends AbstractDBPoolsManager {
	
	private Map<String, IDbPool> urlReposity = new ConcurrentHashMap<String, IDbPool>();

	@Override
	protected IDbPool createPool(IDbConfig config) throws PoolException {
		checkParam(config);
		String urlId = getUrlId(config);
		if(urlReposity.containsKey(urlId)) {
			return urlReposity.get(urlId);
		}
		ApplicationContext ac = ApplicationContextUtils.getAc();
		if(null == ac) {
			throw new PoolException("applicationContext为空");
		}
		IDbCreate creater = ac.getBean(config.getType().getName(), IDbCreate.class);
		if(null == creater) {
			throw new PoolException("没有找到对应的数据库连接池创建者");
		}
		IDbPool pool = creater.create(config);
		if(null != pool) {
			urlReposity.put(urlId, pool);
		}
		return pool;
	}
	
	private String getUrlId(IDbConfig config) {
		String host = config.getHost().replace(".", "_");
		return config.getType().getName() + "_" + host + "_" + config.getPort() + "_" +
				config.getName() + "_" + config.getUserName();
	}
	
	//检查参数
	private void checkParam(IDbConfig config) throws PoolException {
		StringBuilder sb = new StringBuilder();
		if(null == config.getType()) {
			sb.append("数据库类型dbType不能为空,");
		}else if(!DbTypeUtils.containValue(config.getType())){
			sb.append("数据库类型dbType值不存在,");
		}
		if(null == config.getHost()) {
			sb.append("数据库地址不能为空,");
		}
		if(null == config.getPort()) {
			sb.append("端口port不能为空,");
		}
		if(null == config.getName()) {
			sb.append("数据库名name不能为空,");
		}
		if(null == config.getUserName()) {
			sb.append("用户名不能为空,");
		}
		if(null == config.getPassWord()) {
			sb.append("密码不能为空");
		}
		if(sb.length() > 0) {
			throw new PoolException(sb.toString());
		}
	}

}

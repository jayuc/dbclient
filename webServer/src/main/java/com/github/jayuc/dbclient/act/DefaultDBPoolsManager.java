package com.github.jayuc.dbclient.act;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static final Logger log = LoggerFactory.getLogger(DefaultDBPoolsManager.class);

	@Override
	protected IDbPool createPool(IDbConfig config) throws PoolException {
		checkParam(config);
		String urlId = getUrlId(config);
		if(urlReposity.containsKey(urlId)) {
			log.debug("仓库中已经包括数据库连接池: id: " + urlId);
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
	
	//检查参数
	private void checkParam(IDbConfig config) throws PoolException {
		log.debug("开始检查参数");
		StringBuilder sb = new StringBuilder();
		if(null == config.getType()) {
			sb.append("数据库类型dbType不能为空，");
		}else if(!DbTypeUtils.containValue(config.getType())){
			sb.append("数据库类型dbType值不存在，");
		}
		if(null == config.getHost()) {
			sb.append("数据库地址不能为空，");
		}
		if(null == config.getPort()) {
			sb.append("端口port不能为空，");
		}
		if(null == config.getName()) {
			sb.append("数据库名name不能为空，");
		}
		if(null == config.getUserName()) {
			sb.append("用户名不能为空，");
		}
		if(null == config.getPassword()) {
			sb.append("密码不能为空");
		}
		if(sb.length() > 0) {
			throw new PoolException(sb.toString());
		}
	}

}

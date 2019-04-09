package com.github.jayuc.dbclient.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.jayuc.dbclient.entity.Result;
import com.github.jayuc.dbclient.err.PoolException;
import com.github.jayuc.dbclient.iter.IDBPoolsManager;
import com.github.jayuc.dbclient.param.DbCreateParam;
import com.github.jayuc.dbclient.utils.ResultUtils;

/**
 * 创建新的数据据连接
 * @author yujie
 *
 */

@RestController
@RequestMapping("/newcon")
public class NewConnectionController {
	
	private final static Logger log = LoggerFactory.getLogger(NewConnectionController.class);
	
	@Autowired
	IDBPoolsManager dbPoolManager;

	@PostMapping("/create")
	public Map<String, Object> create(@ModelAttribute("param") DbCreateParam param){
		
		log.debug("接到创建数据库连接池请求");
		
		Result map = ResultUtils.simpleResult();
		
		try {
			Map<String, Object> returnMap = dbPoolManager.setDbPool(param);
			trySetProperty(returnMap, map, "tip");
			trySetProperty(returnMap, map, "token");
			trySetProperty(returnMap, map, "dbId");
		} catch (PoolException e) {
			map.setError(e.getMessage());
			log.error(e.getMessage() + param);
			e.printStackTrace();
		}
		
		log.debug("请求结束并返回结果");
		
		return map.getResult();
	}
	
	/**
	 * 设置返回结果，
	 * 如果returnMap包含名为name的属性，则把名为name的值插入map
	 * @param returnMap
	 * @param map
	 * @param name
	 */
	private void trySetProperty(Map<String, Object> returnMap, Result map, String name) {
		if(returnMap.containsKey(name)) {
			map.setProperty(name, returnMap.get(name));
		}
	}
	
}

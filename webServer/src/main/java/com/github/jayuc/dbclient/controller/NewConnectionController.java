package com.github.jayuc.dbclient.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.jayuc.dbclient.err.PoolException;
import com.github.jayuc.dbclient.iter.IDBPoolsManager;
import com.github.jayuc.dbclient.param.DbCreateParam;

/**
 * 创建新的数据据连接
 * @author yujie
 *
 */

@RestController
@RequestMapping("/newcon")
public class NewConnectionController {
	
	@Autowired
	IDBPoolsManager dbPoolManager;

	@PostMapping("/create")
	public Map<String, Object> create(@ModelAttribute("param") DbCreateParam param){
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			dbPoolManager.setDbPool(param);
		} catch (PoolException e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
}

package com.github.jayuc.dbclient.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.jayuc.dbclient.iter.IDBPoolsManager;
import com.github.jayuc.dbclient.iter.IDbCreate;
import com.github.jayuc.dbclient.param.DbCreateParam;
import com.github.jayuc.dbclient.utils.ApplicationContextUtils;

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
		
		ApplicationContext ac = ApplicationContextUtils.getAc();
		IDbCreate creater = ac.getBean(param.getType().getName(), IDbCreate.class);
		System.out.println(creater);
		
		dbPoolManager.setDbPool("1", creater.create(param));
		
		System.out.println(dbPoolManager.getDbPool("1"));
		
		return map;
	}
	
}

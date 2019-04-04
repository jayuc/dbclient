package com.github.jayuc.dbclient.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 创建新的数据据连接
 * @author yujie
 *
 */

@RestController
@RequestMapping("/newcon")
public class NewConnectionController {

	//@RequestMapping(value="/create", method=RequestMethod.POST)
	@PostMapping("/create")
	public Map<String, Object> create(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		System.out.println("连接成功----0079F46478614336FB9F1F8B6031059B");
		HttpSession session = request.getSession();
		System.out.println(session);
		System.out.println(session.getId());
		boolean tu = false;
		if("0079F46478614336FB9F1F8B6031059B".equals(session.getId())) {
			tu = true;
		}
		System.out.println(tu);
		return map;
	}
	
}

package com.github.jayuc.dbclient.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回结果
 *  * 统一后台返回的数据结构 json格式
 * {
 * 		status: 'success',   //此次请求的状态 可选值 success,error
 * 		errorInfo: '',       //请求出错说明
 * 		result: {            //请求结果对象
 * 			rows: [],		 //请求结果
 * 			total: 1  		 //结果数量
 * 			...				 //可包括其他扩展属性
 * 		}  
 * 		...					 //可包括其他扩展属性
 * }
 * @author yujie
 *
 */
public final class Result {
	
	private Map<String, Object> map = new HashMap<String, Object>();
	
	private Map<String, Object> properties = new HashMap<String, Object>();
	
	private Map<String, Object> result = null;

	public Result(boolean normal) {
		if(normal) {
			result = new HashMap<String, Object>();
			result.put("total", 1);
			result.put("rows", new ArrayList<Object>());
			map.put("result", result);
		}
		map.put("status", ResultStatus.success);
		map.put("attribute", properties);
		map.put("errorInfo", "");
	}
	
	public void setProperty(String key, Object value) {
		properties.put(key, value);
	}
	
	public void setResult(int total, Object rows) {
		setTotal(total);
		setRows(rows);
	}
	
	public void setTotal(int total) {
		if(null != result) {
			result.put("total", total);
		}
	}
	
	public void setRows(Object rows) {
		if(null != result) {
			result.put("rows", rows);
		}
	}

	public void setStatus(ResultStatus status) {
		map.put("status", status);
	}
	
	public void setError(String errorInfo) {
		setStatus(ResultStatus.error);
		map.put("errorInfo", errorInfo);
	}
	
	public Map<String, Object> getResult(){
		return map;
	}

}

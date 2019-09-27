package com.github.jayuc.dbclient.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.jayuc.dbclient.config.RedisConfig;
import com.github.jayuc.dbclient.err.SqlHandlerException;

/**
 * 对通过反射机制，返回的结果集解析
 * @author yujie
 *
 */
public class MethodReturnUtil {

	@SuppressWarnings({ "unchecked" })
	public static Map<String, Object> parse(Class<?> clazz, final Object obj, final String sql) 
			throws SqlHandlerException {
		Map<String, Object> map = new HashMap<String, Object>();
		final String field = "field";
		final String value = "value";
		if(null != obj) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			List<String> headerList = new ArrayList<String>();
			int total = 0;
			if(String.class == clazz || Boolean.class == clazz) {  //
				if(!"OK".equals(obj)) {
					Map<String, Object> row = new HashMap<String, Object>();
					row.put(value, obj);
					list.add(row);
					headerList.add(value);
					total = 1;
				}
			}else if(Map.class == clazz) {  //map类型
				Map<String, Object> row = (Map<String, Object>) obj;
				Set<String> keys = row.keySet();
				int len = keys.size();
				if(shoudLimit(sql) && len > RedisConfig.MAX_LINE) {
					len = RedisConfig.MAX_LINE;
				}
				int i = 0;
				for(String key:keys) {
					if(i < len) {
						Map<String, Object> r = new HashMap<String, Object>();
						r.put(field, key);
						r.put(value, row.get(key));
						list.add(r);
					}else {
						break;
					}
					i ++;
				}
				headerList.add(field);
				headerList.add(value);
				total = keys.size();
			}else if(Set.class == clazz) {
				Set<String> row = (Set<String>) obj;
				int len = row.size();
//				System.out.println(shoudLimit(sql));
				if(shoudLimit(sql) && len > RedisConfig.MAX_LINE) {
					len = RedisConfig.MAX_LINE;
				}
				int i = 0;
				for(String key:row) {
					if(i < len) {
						Map<String, Object> r = new HashMap<String, Object>();
						r.put(value, key);
						list.add(r);
					}else {
						break;
					}
					i ++;
				}
				headerList.add(value);
				total = row.size();
			}else if(List.class == clazz) {
				List<String> row = (List<String>) obj;
				int len = row.size();
				if(shoudLimit(sql) && len > RedisConfig.MAX_LINE) {
					len = RedisConfig.MAX_LINE;
				}
				int i = 0;
				for(String key:row) {
					if(i < len) {
						Map<String, Object> r = new HashMap<String, Object>();
						r.put(value, key);
						list.add(r);
					}else {
						break;
					}
					i ++;
				}
				headerList.add(value);
				total = row.size();
			}else {
				return null;
			}
			map.put("headers", headerList);
			map.put("rows", list);
			map.put("total", total);
		}
		return map;
	}
	
	private static final boolean shoudLimit(String sql) {
		if("keys *".equals(sql) || "show collections".equals(sql)) {
			return false;
		}
		return true;
	}
	
}

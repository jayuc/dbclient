package com.github.jayuc.dbclient.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.github.jayuc.dbclient.entity.UserData;

/**
 * 缓存在服务端的用户数据
 * @author yujie
 * 2019年4月10日 下午5:28:03
 */
@Component
public class UserCacheData {

	private Map<String, UserData> reposity = new ConcurrentHashMap<String, UserData>();

	public UserData getUserData(String token) {
		return reposity.get(token);
	}
	
	public void setUserData(String token, UserData data) {
		reposity.put(token, data);
	}
	
}

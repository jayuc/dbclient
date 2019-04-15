package com.github.jayuc.dbclient.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.jayuc.dbclient.data.UserCacheData;
import com.github.jayuc.dbclient.entity.Result;
import com.github.jayuc.dbclient.entity.UserData;
import com.github.jayuc.dbclient.param.UserSettingParam;
import com.github.jayuc.dbclient.utils.ResultUtils;
import com.github.jayuc.dbclient.utils.StringUtil;

/**
 * 用户设置
 * @author yujie
 * 2019年4月11日 上午8:55:17
 */
@RestController
@RequestMapping("/user/setting")
public class UserSettingController {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserSettingController.class);

	@Autowired
	UserCacheData userData;
	
	@PostMapping("/doset")
	public Map<String, Object> doSet(@ModelAttribute("param") UserSettingParam param){
		LOG.debug("设置参数开始");
		Result result = ResultUtils.simpleResult();
		if(haveUserDataChange(param)) {
			String token = param.getToken();
			int limit = param.getLimit();
			int redisIndex = param.getRedisIndex();
			UserData data = userData.getUserData(token);
			LOG.debug("user data: " + data);
			if(null != data) {
				data.setLimit(limit);
				data.setRedisIndex(redisIndex);
			}else {
				UserData ud = new UserData();
				ud.setLimit(limit);
				ud.setRedisIndex(redisIndex);
				userData.setUserData(token, ud);
			}
		}else {
			LOG.warn("参数无效 " + param);
			result.setError("参数无效");
		}
		LOG.debug("设置参数结束");
		return result.getResult();
	}
	
	//判断是否有真实参数
	private boolean haveUserDataChange(UserSettingParam param) {
		if(null != param 
				&& !StringUtil.isBlank(param.getToken())
				&& param.getLimit() > 0) {
			return true;
		}
		return false;
	}
	
}

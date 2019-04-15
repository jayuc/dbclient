package com.github.jayuc.dbclient.entity;

import lombok.Data;
import lombok.ToString;

/**
 * 用户数据
 * @author yujie
 * 2019年4月10日 下午5:31:51
 */
@Data
@ToString
public class UserData {

	private int limit;
	
	//redis 的库
	private int redisIndex;
	
}

package com.github.jayuc.dbclient.param;

import com.github.jayuc.dbclient.iter.IToken;

import lombok.Data;
import lombok.ToString;

/**
 * 用户设置
 * @author yujie
 * 2019年4月11日 上午8:52:11
 */
@Data
@ToString
public class UserSettingParam implements IToken {

	private String token;
	
	private int limit;

}

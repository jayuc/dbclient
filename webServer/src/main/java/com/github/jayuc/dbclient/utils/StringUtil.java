package com.github.jayuc.dbclient.utils;

import cn.hutool.core.util.StrUtil;

/**
 * 字符串工具类
 * @author yujie
 *
 */
public class StringUtil {

	/**
	 * 字符串是否为空白 空白的定义如下： <br>
	 * 1、为null <br>
	 * 2、为不可见字符（如空格）<br>
	 * 3、""<br>
	 * 
	 * @param str 被检测的字符串
	 * @return 是否为空
	 */
	public static boolean isBlank(String str) {
		return StrUtil.isBlank(str);
	}
	
}

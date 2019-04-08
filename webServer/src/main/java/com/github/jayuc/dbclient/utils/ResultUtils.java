package com.github.jayuc.dbclient.utils;

import com.github.jayuc.dbclient.entity.Result;

/**
 * 统一后台返回的数据结构 json格式
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
public class ResultUtils {
	
	/**
	 * 简单请求返回数据结构，不带返回的数据数组
	 * @return
	 */
	public static Result simpleResult(){
		return new Result(false);
	}
	
	/**
	 * 返回通用数据结构
	 * @return
	 */
	public static Result normalResult(){
		return new Result(true);
	}

}

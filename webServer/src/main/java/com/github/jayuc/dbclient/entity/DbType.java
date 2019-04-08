package com.github.jayuc.dbclient.entity;

/**
 * 数据库类型 
 * 注意：name 和 IDbCreate 的实现bean 的名字对应
 * @author yujie
 *
 */
public enum DbType {
	Oracle("oracle"),
	Mysql("mysql"),
	Redis("redis");
	
	private final String name;
	
	private DbType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}

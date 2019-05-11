package com.github.jayuc.dbclient.entity;

/**
 * 数据库类型 
 * @author yujie
 *
 */
public enum DbType {
	Oracle("oracle"),
	Mysql("mysql"),
	Redis("redis"),
	Postgresql("postgresql");
	
	private final String name;
	
	private DbType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	// 和ISqlHandler对应的实现类相对应
	public String getHandlerName() {
		return name + "Handler";
	}
	
	// 和IDbCreater对应的实现类相对应
	public String getCreaterName() {
		return name + "Creater";
	}
	
}

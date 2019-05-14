package com.github.jayuc.dbclient.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MongoSql {
	private String level;  // 可选值  db, client
	private String sql;
	private Class<?>[] paramClass = new Class[] {};
	private Object[] param = new Object[] {};
	
	public MongoSql(String level, String sql) {
		this.level = level;
		this.sql = sql;
	}
}

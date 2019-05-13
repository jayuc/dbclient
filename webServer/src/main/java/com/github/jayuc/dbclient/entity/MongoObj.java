package com.github.jayuc.dbclient.entity;

import com.mongodb.MongoClient;

/**
 * mongodb 数据库客户端，数据库包装类
 * @author yujie
 *
 */
public class MongoObj {
	private MongoClient client;
	private String databaseName;
	public MongoObj(MongoClient client, String databaseName) {
		this.client = client;
		this.databaseName = databaseName;
	}
	public MongoClient getClient() {
		return client;
	}
	public String getDb() {
		return databaseName;
	}
}

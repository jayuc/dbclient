package com.github.jayuc.dbclient.config;

import java.util.HashMap;
import java.util.Map;

import com.github.jayuc.dbclient.entity.MongoSql;

/**
 * mongodb 命令集
 * @author yujie
 *
 */
public final class MongoCommands {

	public static final Map<String, MongoSql> commands = new HashMap<String, MongoSql>();
	
	static {
		commands.put("db", new MongoSql("db", "getName"));
		commands.put("showdbs", new MongoSql("client", "listDatabaseNames"));
		commands.put("showcollections", new MongoSql("db", "listCollectionNames"));
	}
	
}

package com.github.jayuc.dbclient.utils;

import com.github.jayuc.dbclient.entity.DbType;

public class DbTypeUtils {

	public static boolean containValue(DbType type) {
		boolean b = false;
		DbType[] dbs = DbType.values();
		for(DbType t:dbs) {
			if(t == type) {
				b = true;
				break;
			}
		}
		return b;
	}
}

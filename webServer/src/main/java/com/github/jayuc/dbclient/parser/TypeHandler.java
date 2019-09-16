package com.github.jayuc.dbclient.parser;

public interface TypeHandler {
	
	Object handle(Object obj) throws Exception;
	
	String toString(Object obj) throws Exception;

}

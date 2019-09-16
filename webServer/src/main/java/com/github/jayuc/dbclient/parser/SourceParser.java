package com.github.jayuc.dbclient.parser;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface SourceParser {
	
	List<Object[]> parse(InputStream inputStream) throws Exception;
	
	List<Object[]> parse(String sourcePath) throws Exception;
	
	SourceData parseAndCheck(InputStream inputStream, Map<Integer, TypeHandler> typeHandlers) throws Exception;
	
	SourceData parseAndCheck(String sourcePath, Map<Integer, TypeHandler> typeHandlers) throws Exception;
	
	List<String[]> parse2(InputStream inputStream) throws Exception;
	
	List<String[]> parse2(String sourcePath) throws Exception;

}

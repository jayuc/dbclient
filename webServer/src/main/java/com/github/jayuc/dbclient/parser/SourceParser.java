package com.github.jayuc.dbclient.parser;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface SourceParser {
	
	List<RowData> parse(InputStream inputStream, int ignore) throws Exception;
	
	List<RowData> parse(String sourcePath, int ignore) throws Exception;
	
	SourceData parseAndCheck(InputStream inputStream, Map<Integer, TypeHandler> typeHandlers, int ignore) throws Exception;
	
	SourceData parseAndCheck(String sourcePath, Map<Integer, TypeHandler> typeHandlers, int ignore) throws Exception;
	
	List<String[]> parse2(InputStream inputStream, int ignore) throws Exception;
	
	List<String[]> parse2(String sourcePath, int ignore) throws Exception;

}

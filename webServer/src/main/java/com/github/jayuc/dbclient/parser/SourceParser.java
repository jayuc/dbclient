package com.github.jayuc.dbclient.parser;

import java.io.InputStream;
import java.util.List;

public interface SourceParser {
	
	List<String[]> parse(InputStream inputStream) throws Exception;
	
	List<String[]> parse(String sourcePath) throws Exception;
	
	SourceData parseAndCheck(InputStream inputStream, List<TypeHandler> typeHandlers) throws Exception;
	
	SourceData parseAndCheck(String sourcePath, List<TypeHandler> typeHandlers) throws Exception;

}

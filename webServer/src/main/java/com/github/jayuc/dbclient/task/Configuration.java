package com.github.jayuc.dbclient.task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.github.jayuc.dbclient.parser.SourceParser;
import com.github.jayuc.dbclient.parser.TypeHandler;
import com.github.jayuc.dbclient.parser.excel.ExcelParser;
import com.github.jayuc.dbclient.parser.type.DateTypeHandler;
import com.github.jayuc.dbclient.parser.type.IntTypeHandler;
import com.github.jayuc.dbclient.parser.type.NumberTypeHandler;
import com.github.jayuc.dbclient.parser.type.StringTypeHandler;
import com.github.jayuc.dbclient.task.fork.DefaultTaskFork;
import com.github.jayuc.dbclient.task.fork.TaskFork;
import com.github.jayuc.dbclient.utils.StringUtil;

@Component
public class Configuration {
	
	@Value("${insert.batch.size}")
	private int batchSize = 400;
	@Value("${insert.thread.size}")
	private int threadSize = 4;
	
	private static Map<String, TypeHandler> typeHandlers = new HashMap<>();
	static {
		typeHandlers.put("string", new StringTypeHandler());
		typeHandlers.put("int", new IntTypeHandler());
		typeHandlers.put("date", new DateTypeHandler());
		typeHandlers.put("number", new NumberTypeHandler());
	}
	
	public TaskFork newTaskFork(DataSource dataSource, String sql) {
		return new DefaultTaskFork()
				.batchSize(batchSize)
				.dataSource(dataSource)
				.sql(sql);
	}
	
	public int batchSize() {
		return batchSize;
	}
	
	public ExecutorService newExecutor() {
		int size = 4;
		if(threadSize > 0) {
			size = threadSize;
		}
		size = size > 16 ? 16 : size;
		return Executors.newFixedThreadPool(size);
	}
	
	public SourceParser newParser(String sourceType) {
		SourceParser parser = null;
		if(!StringUtil.isBlank(sourceType)) {
			switch (sourceType) {
				case "excel":
					parser = new ExcelParser();
					break;
			}
		}
		return parser;
	}
	
	public Map<Integer, TypeHandler> typeHandlers(Map<Integer, String> type){
		Map<Integer, TypeHandler> map = new HashMap<>();
		if(type != null) {
			type.forEach((k, v) -> {
				String t = v.toLowerCase();
				if(typeHandlers.containsKey(t)) {
					map.put(k, typeHandlers.get(t));
				}
			});
		}
		return map;
	}
	
}

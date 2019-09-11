package com.github.jayuc.dbclient.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.github.jayuc.dbclient.parser.SourceParser;
import com.github.jayuc.dbclient.parser.TypeHandler;
import com.github.jayuc.dbclient.parser.excel.ExcelParser;
import com.github.jayuc.dbclient.parser.type.DateTypeHandler;
import com.github.jayuc.dbclient.parser.type.IntTypeHandler;
import com.github.jayuc.dbclient.parser.type.StringTypeHandler;
import com.github.jayuc.dbclient.task.fork.DefaultTaskFork;
import com.github.jayuc.dbclient.task.fork.TaskFork;
import com.github.jayuc.dbclient.utils.StringUtil;

@Component
public class Configuration {
	
	@Value("${insert.batch.size}")
	private int batchSize;
	@Value("${insert.thread.size}")
	private int threadSize;
	
	private static Map<String, TypeHandler> typeHandlers = new HashMap<>();
	static {
		typeHandlers.put("string", new StringTypeHandler());
		typeHandlers.put("int", new IntTypeHandler());
		typeHandlers.put("date", new DateTypeHandler());
	}
	
	public TaskFork newTaskFork() {
		DefaultTaskFork fork = new DefaultTaskFork();
		if(batchSize > 0) {
			fork.setBatchSize(batchSize);
		}
		return fork;
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
	
	public List<TypeHandler> typeHandlers(String[] type){
		List<TypeHandler> list = new ArrayList<TypeHandler>();
		for(String t:type) {
			String tl = t.toLowerCase();
			if(typeHandlers.containsKey(tl)) {
				list.add(typeHandlers.get(tl));
			}else {
				list.add(null);
			}
		}
		return list;
	}
	
}

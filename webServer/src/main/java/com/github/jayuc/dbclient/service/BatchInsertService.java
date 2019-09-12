package com.github.jayuc.dbclient.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.jayuc.dbclient.entity.Result;
import com.github.jayuc.dbclient.param.BatchInsertParam;
import com.github.jayuc.dbclient.parser.SourceData;
import com.github.jayuc.dbclient.parser.SourceParser;
import com.github.jayuc.dbclient.task.BatchInsertTask;
import com.github.jayuc.dbclient.task.Configuration;
import com.github.jayuc.dbclient.task.TaskResult;
import com.github.jayuc.dbclient.task.fork.TaskFork;
import com.github.jayuc.dbclient.utils.IdUtils;
import com.github.jayuc.dbclient.utils.ResultUtils;

@Service
public class BatchInsertService {
	
	@Autowired
	private Configuration configuration;
	private ExecutorService executor;
	private TaskFork fork;
	private SourceParser parser;
	private final Map<String, TaskResult> resultMap = new ConcurrentHashMap<>();
	
	public Map<String, Object> insert(BatchInsertParam param){
		Result result = ResultUtils.simpleResult();
		
		if(parser == null) {
			parser = configuration.newParser(param.getSourceType());
		}
		if(executor == null) {
			executor = configuration.newExecutor();
		}
		if(fork == null) {
			fork = configuration.newTaskFork();
		}
		
		if(parser != null) {
			try {
				
				SourceData data = parser.parseAndCheck(param.getSourcePath(), configuration.typeHandlers(param.getRules()));
				List<Object[]> list = data.getNormalList();
				if(list.size() > 0) {
					List<BatchInsertTask> tasks = fork.fork(list);
					if(tasks.size() > 0) {
						TaskResult tr = new TaskResult();
						resultMap.put(IdUtils.generateId(), tr);
//						new TaskThread(tasks, tr).start();
					}else {
						result.setError("无可导入数据");
					}
				}else {
					result.setError("无可导入数据");
				}
				
			} catch (Exception e) {
				result.setError("解析出错");
			}
		}else {
			result.setError("文件类型不正确");
		}
		
		return result.getResult();
	}
	
	private class TaskThread extends Thread{
		private List<BatchInsertTask> tasks;
		private TaskResult result;
		public TaskThread(List<BatchInsertTask> tasks, TaskResult result) {
			super();
			this.tasks = tasks;
			this.result = result;
		}
		@Override
		public void run(){
			try {
				List<Future<TaskResult>> futures = executor.invokeAll(tasks);
				for(Future<TaskResult> future:futures) {
					TaskResult t = future.get();
					result.add(t.getTotal(), t.getSuccess(), t.getFail());
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
	
}

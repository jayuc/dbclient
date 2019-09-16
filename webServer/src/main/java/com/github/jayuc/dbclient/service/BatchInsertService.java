package com.github.jayuc.dbclient.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private final Logger LOG = LoggerFactory.getLogger(BatchInsertService.class);
	
	@Autowired
	private Configuration configuration;
	private ExecutorService executor;
	private TaskFork fork;
	private SourceParser parser;
	private final Map<String, TaskResult> resultMap = new ConcurrentHashMap<>();
	
	public Map<String, Object> insert(BatchInsertParam param){
		Result result = ResultUtils.simpleResult();
		LOG.info("提交导入任务...");
		
		if(parser == null) {
			parser = configuration.newParser(param.getSourceType());
		}
		if(executor == null) {
			executor = configuration.newExecutor();
		}
		if(fork == null) {
			fork = configuration.newTaskFork(null, param.getSql());
		}
		
		if(parser != null) {
			try {
				
				SourceData data = parser.parseAndCheck(param.getSourcePath(), configuration.typeHandlers(param.getRules()));
				TaskResult tr = new TaskResult();
				tr.addError(data.getAbnormalStringList());
				resultMap.put(IdUtils.generateId(), tr);
				List<Object[]> list = data.getNormalList();
				if(list.size() > 0) {
					List<BatchInsertTask> tasks = fork.fork(list);
					if(tasks.size() > 0) {
//						new TaskThread(tasks, tr).start();
					}else {
						LOG.error("无可导入数据");
						result.setError("无可导入数据");
						if(data.getErrorInfoList().size() > 0) {
							result.setProperty("onlyError", "yes");
						}
					}
				}else {
					LOG.error("无可导入数据");
					result.setError("无可导入数据");
					if(data.getErrorInfoList().size() > 0) {
						result.setProperty("onlyError", "yes");
					}
				}
				
			} catch (Exception e) {
				LOG.error("解析出错", e);
				e.printStackTrace();
				result.setError("解析出错");
			}
		}else {
			LOG.error("文件类型不正确");
			result.setError("文件类型不正确");
		}
		
		LOG.info("提交任务完成， " + result.getResult().toString());
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

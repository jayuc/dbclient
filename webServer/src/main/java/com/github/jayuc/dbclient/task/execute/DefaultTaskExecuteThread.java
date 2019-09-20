package com.github.jayuc.dbclient.task.execute;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jayuc.dbclient.parser.RowData;
import com.github.jayuc.dbclient.task.BatchInsertTask;
import com.github.jayuc.dbclient.task.Configuration;
import com.github.jayuc.dbclient.task.TaskResult;
import com.github.jayuc.dbclient.task.fork.DefaultTaskFork;

/**
 * excel导入任务默认线程
 * @author jayu
 *
 */
public class DefaultTaskExecuteThread extends Thread {
	
	private Logger LOG = LoggerFactory.getLogger(DefaultTaskExecuteThread.class);

	private final TaskResult taskResult;
	private final ExecutorService executor;
	private final DefaultTaskFork taskFork;
	private List<RowData> data;
	private int batchSize;  // 任务分片大小
	private boolean firstExecute = true;  // 是否第一次执行
	
	public DefaultTaskExecuteThread(DataSource dataSource, String sql, TaskResult taskResult,
			Configuration configuration, List<RowData> data) {
		super();
		this.taskResult = taskResult;
		this.data = data;
		this.executor = configuration.newExecutor();
		this.taskFork = (DefaultTaskFork) configuration.newTaskFork(dataSource, sql);
		this.batchSize = configuration.batchSize();
	}

	@Override
	public void run() {
		LOG.info("开始执行导入任务...");
		doExecute();
		LOG.info("结束执行导入任务...");
	}
	
	private void doExecute() {
		List<BatchInsertTask> tasks = forkTask();
		LOG.info("first execute: " + firstExecute + " ,batch size: " + batchSize + " ,data size: " + data.size());
		if(firstExecute) {
			firstExecute = false;
		}
		try {
			List<Future<TaskResult>> futures = executor.invokeAll(tasks);
			List<RowData> failData = new ArrayList<RowData>();
			for(Future<TaskResult> future:futures) {
				TaskResult t = future.get();
				if(t.getFailData().size() > 0) {
					failData.addAll(t.getFailData());
				}
			}
			LOG.info("fail data: " + failData.size());
			if(failData.size() > 0) {
				data = failData;
			}
		} catch (InterruptedException | ExecutionException e) {
			LOG.error("执行任务出错", e);
		}
		LOG.info("total: " + taskResult.getTotal() + " ,success: " + taskResult.getSuccess() + " ,fail: " + taskResult.getFail());
		if((taskResult.getSuccess() + taskResult.getFail()) < taskResult.getTotal()) {
			doExecute();
		}
	}
	
	private List<BatchInsertTask> forkTask(){
		int size = computeBatchSize();
		if(firstExecute) {
			size = batchSize;
		}else {
			batchSize = size;
		}
		return taskFork.batchSize(size)
					.fork(data, taskResult);
	}
	
	/**
	 * 切片 核心算法
	 * @return
	 */
	private int computeBatchSize() {
		int dataSize = data.size();
		int currentSize = dataSize > batchSize ? batchSize : dataSize;
		int part = 4;
		int size = currentSize/part;
		int preSize = currentSize%part > 0 ? size + 1 : size;
		return preSize;
	}
	
}

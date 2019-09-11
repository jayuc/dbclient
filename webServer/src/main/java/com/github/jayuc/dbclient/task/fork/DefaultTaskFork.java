package com.github.jayuc.dbclient.task.fork;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import com.github.jayuc.dbclient.task.BatchInsertTask;
import com.github.jayuc.dbclient.utils.ApplicationContextUtils;

public class DefaultTaskFork implements TaskFork {
	
	// 默认 400
	private int batchSize = 400;

	@Override
	public List<BatchInsertTask> fork(List<Object[]> list) {
		System.out.println(batchSize);
		ApplicationContext ac = ApplicationContextUtils.getAc();
		Environment environment = ac.getBean(Environment.class);
		String size = environment.getProperty("insert.batch.size");
		System.out.println(size);
		return null;
	}
	
	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

}

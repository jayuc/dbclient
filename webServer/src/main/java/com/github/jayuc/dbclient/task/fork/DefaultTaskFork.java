package com.github.jayuc.dbclient.task.fork;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.github.jayuc.dbclient.parser.RowData;
import com.github.jayuc.dbclient.task.BatchInsertTask;

public class DefaultTaskFork implements TaskFork {
	
	// 默认 400
	private int batchSize = 400;
	private DataSource dataSource;
	private String sql;

	public DefaultTaskFork() {
		super();
	}
	public DefaultTaskFork dataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		return this;
	}
	public DefaultTaskFork sql(String sql) {
		this.sql= sql;
		return this;
	}
	public DefaultTaskFork batchSize(int batchSize) {
		this.batchSize = batchSize;
		return this;
	}

	@Override
	public List<BatchInsertTask> fork(List<RowData> list) {
		List<BatchInsertTask> result = new ArrayList<BatchInsertTask>();
		List<RowData> item = new ArrayList<RowData>();
		for(int i=0; i<list.size(); i++) {
			if(item.size() >= batchSize) {
				result.add(new BatchInsertTask(sql, dataSource, item));
				item = new ArrayList<RowData>();
			}
			item.add(list.get(i));
		}
		if(item.size() > 0) {
			result.add(new BatchInsertTask(sql, dataSource, item));
		}
		return result;
	}

}

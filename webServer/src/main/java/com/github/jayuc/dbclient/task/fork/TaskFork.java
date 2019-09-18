package com.github.jayuc.dbclient.task.fork;

import java.util.List;

import com.github.jayuc.dbclient.parser.RowData;
import com.github.jayuc.dbclient.task.BatchInsertTask;

public interface TaskFork {

	List<BatchInsertTask> fork(List<RowData> list);
	
}

package com.github.jayuc.dbclient.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.sql.DataSource;

public class BatchInsertTask implements Callable<TaskResult> {
	
	private final String sql;
	private final DataSource dataSource;
	private final List<Object[]> data;

	public BatchInsertTask(String sql, DataSource dataSource, List<Object[]> data) {
		super();
		this.sql = sql;
		this.dataSource = dataSource;
		this.data = data;
	}

	@Override
	public TaskResult call() throws Exception {
		TaskResult result = new TaskResult();
		if(data.size() > 0) {
			result.setTotal(data.size());
			Connection conn = dataSource.getConnection();
			long start = System.currentTimeMillis();
			PreparedStatement statement = conn.prepareStatement(sql);
			conn.setAutoCommit(false);
			for(Object[] item:data) {
				for(int i=0; i<item.length; i++) {
					statement.setObject(i+1, item[i]);
				}
				statement.addBatch();
			}
			List<Integer> failList = new ArrayList<>();
			try {
				int[] re = statement.executeBatch();
				conn.commit();
				for(int i=0; i<re.length; i++) {
					if(re[i]>0) {
						result.successAdd();
					}else {
						failList.add(i);
					}
				}
			} catch (Exception e) {
				
			} finally {
				try {
					statement.close();
				} catch (Exception e2) {
				}
				try {
					conn.close();
				} catch (Exception e2) {
				}
			}
			long end = System.currentTimeMillis();
			result.setTook(end - start);
			result.setFail(result.getTotal() - result.getSuccess());
			if(failList.size() > 0) {
				result.setFailRows(failList);
			}
		}
		return result;
	}

}

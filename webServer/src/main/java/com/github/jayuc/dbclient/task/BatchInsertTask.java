package com.github.jayuc.dbclient.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jayuc.dbclient.parser.RowData;

public class BatchInsertTask implements Callable<TaskResult> {
	
	private final static Logger LOG = LoggerFactory.getLogger(BatchInsertTask.class);
	
	private final String sql;
	private final DataSource dataSource;
	private final List<RowData> data;

	public BatchInsertTask(String sql, DataSource dataSource, List<RowData> data) {
		super();
		this.sql = sql;
		this.dataSource = dataSource;
		this.data = data;
	}

	@Override
	public TaskResult call() throws Exception {
		TaskResult result = new TaskResult();
		if(data != null && data.size() > 0) {
			result.setTotal(data.size());
			Connection conn = dataSource.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			conn.setAutoCommit(false);
			if(!(setParams(statement, data, result) > 0)) {
				LOG.debug("设置全部失败");
				return result;
			}
			try {
				statement.executeBatch();
				conn.commit();
				result.setSuccess(data.size());
				if(LOG.isDebugEnabled()) {
					LOG.debug("本次插入数据成功");
				}
			} catch (Exception e) {
				rollback(conn);
				if(this.data.size() == 1) {
					result.addError(e.getMessage(), data.get(0).getIndex());
				}else {
					result.addFailData(data);
				}
				if(LOG.isDebugEnabled()) {
					LOG.debug("本次插入数据失败");
				}
			} finally {
				close(statement, conn);
			}
		}
		return result;
	}
	
	private int setParams(PreparedStatement statement, List<RowData> data, final TaskResult result) {
		int total = data.size();
		int fail = 0;
		for(RowData item:data) {
			boolean b = true;
			for(int i=0; i<item.getData().length; i++) {
				try {
					Object d = item.getData()[i];
					if(d instanceof Date) {
						statement.setObject(i + 1, new java.sql.Timestamp(((Date) d).getTime()));
					}else {
						statement.setObject(i+1, d);
					}
				} catch (SQLException e) {
					b = false;
					fail++;
					result.addError((i+1) + "：" + e.getMessage(), item.getIndex());
					LOG.error("设置参数失败", e);
				}
			}
			if(b) {
				try {
					statement.addBatch();
				} catch (SQLException e) {
					LOG.error("add batch", e);
				}
			}
		}
		return total - fail;
	}
	
	private void close(PreparedStatement statement, Connection conn) {
		try {
			statement.close();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		try {
			conn.close();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	private void rollback(Connection conn) {
		if(conn != null) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

}

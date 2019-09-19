package com.github.jayuc.dbclient.task;

import java.util.ArrayList;
import java.util.List;

import com.github.jayuc.dbclient.parser.RowData;

import lombok.Data;

@Data
public class TaskResult {
	
	private int total = 0;
	private int success = 0;
	private int fail = 0;
	private long took;
	private List<Integer> failRows = new ArrayList<>();
	private List<String> errorList = new ArrayList<String>();
	private List<RowData> failData = new ArrayList<>();
	
	public void successAdd() {
		success ++;
	}
	
	public synchronized void add(int s, int f) {
		success += s;
		fail += f;
	}
	
	public synchronized void addError(List<String> list, List<Integer> rows) {
		errorList.addAll(list);
		failRows.addAll(rows);
	}
	
	public synchronized void addError(String error, int row) {
		errorList.add(error);
		failRows.add(row);
		fail ++;
	}
	
	public synchronized void addFailData(List<RowData> data) {
		failData.addAll(data);
	}
	
	public synchronized void addFailData(RowData data) {
		failData.add(data);
	}

}

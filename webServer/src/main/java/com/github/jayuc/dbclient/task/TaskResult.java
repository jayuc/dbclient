package com.github.jayuc.dbclient.task;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TaskResult {
	
	private int total = 0;
	private int success = 0;
	private int fail = 0;
	private long took;
	private List<Integer> failList;
	private List<String[]> errorList = new ArrayList<String[]>();;
	
	public void successAdd() {
		success ++;
	}
	
	public synchronized void add(int t, int s, int f) {
		total += t;
		success += s;
		fail += f;
	}
	
	public synchronized void addError(List<String[]> list) {
		errorList.addAll(list);
	}
	
	public synchronized void addError(String[] error) {
		errorList.add(error);
	}

}

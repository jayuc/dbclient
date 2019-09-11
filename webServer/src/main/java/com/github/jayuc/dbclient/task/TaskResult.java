package com.github.jayuc.dbclient.task;

import java.util.List;

import lombok.Data;

@Data
public class TaskResult {
	
	private int total = 0;
	private int success = 0;
	private int fail = 0;
	private long took;
	private List<Integer> failList;
	
	public void successAdd() {
		success ++;
	}
	
	public synchronized void add(int t, int s, int f) {
		total += t;
		success += s;
		fail += f;
	}

}

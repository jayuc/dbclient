package com.github.jayuc.dbclient.parser;

import java.util.ArrayList;
import java.util.List;

public class SourceData {
	
	List<RowData> allList = new ArrayList<>();
	List<String[]> allStringList = new ArrayList<>();
	List<RowData> normalList = new ArrayList<>();
	List<String[]> normalStringList = new ArrayList<>();
	List<Object[]> abnormalList = new ArrayList<>();
	List<String[]> abnormalStringList = new ArrayList<>();
	List<String> errorInfoList = new ArrayList<>();
	List<Integer> failRows = new ArrayList<>();
	
	public List<RowData> getAllList() {
		return allList;
	}
	public List<RowData> getNormalList() {
		return normalList;
	}
	public List<Object[]> getAbnormalList() {
		return abnormalList;
	}
	public List<String> getErrorInfoList() {
		return errorInfoList;
	}
	public List<String[]> getAllStringList() {
		return allStringList;
	}
	public List<String[]> getNormalStringList() {
		return normalStringList;
	}
	public List<String[]> getAbnormalStringList() {
		return abnormalStringList;
	}
	public List<Integer> getFailRows(){
		return failRows;
	}
	
	public void putAll(RowData normal, String[] slist) {
		allList.add(normal);
		allStringList.add(slist);
	}
	public void putNormal(RowData normal, String[] slist) {
		normalList.add(normal);
		normalStringList.add(slist);
	}
	
	public void putAbnormal(Object[] abnormal, String errorInfo, String[] slist, int row) {
		abnormalList.add(abnormal);
		errorInfoList.add(errorInfo);
		abnormalStringList.add(slist);
		failRows.add(row);
	}

}

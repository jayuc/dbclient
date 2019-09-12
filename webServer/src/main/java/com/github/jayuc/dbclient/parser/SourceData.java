package com.github.jayuc.dbclient.parser;

import java.util.ArrayList;
import java.util.List;

public class SourceData {
	
	List<Object[]> allList = new ArrayList<>();
	List<String[]> allStringList = new ArrayList<>();
	List<Object[]> normalList = new ArrayList<>();
	List<String[]> normalStringList = new ArrayList<>();
	List<Object[]> abnormalList = new ArrayList<>();
	List<String[]> abnormalStringList = new ArrayList<>();
	List<Object> errorInfoList = new ArrayList<>();
	
	public List<Object[]> getAllList() {
		return allList;
	}
	public List<Object[]> getNormalList() {
		return normalList;
	}
	public List<Object[]> getAbnormalList() {
		return abnormalList;
	}
	public List<Object> getErrorInfoList() {
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
	
	public void putAll(Object[] normal, String[] slist) {
		allList.add(normal);
		allStringList.add(slist);
	}
	public void putNormal(Object[] normal, String[] slist) {
		normalList.add(normal);
		normalStringList.add(slist);
	}
	
	public void putAbnormal(Object[] abnormal, String errorInfo, String[] slist) {
		abnormalList.add(abnormal);
		errorInfoList.add(errorInfo);
		abnormalStringList.add(slist);
	}

}

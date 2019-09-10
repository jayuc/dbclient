package com.github.jayuc.dbclient.parser;

import java.util.ArrayList;
import java.util.List;

public class SourceData {
	
	List<String[]> allList = new ArrayList<>();
	List<String[]> normalList = new ArrayList<>();
	List<String[]> abnormalList = new ArrayList<>();
	List<String> errorInfoList = new ArrayList<>();
	
	public List<String[]> getAllList() {
		return allList;
	}
	public List<String[]> getNormalList() {
		return normalList;
	}
	public List<String[]> getAbnormalList() {
		return abnormalList;
	}
	public List<String> getErrorInfoList() {
		return errorInfoList;
	}
	
	public void putAll(String[] normal) {
		allList.add(normal);
	}
	public void putNormal(String[] normal) {
		normalList.add(normal);
	}
	
	public void putAbnormal(String[] abnormal, String errorInfo) {
		abnormalList.add(abnormal);
		errorInfoList.add(errorInfo);
	}

}

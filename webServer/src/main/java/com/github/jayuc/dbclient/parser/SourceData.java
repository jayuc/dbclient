package com.github.jayuc.dbclient.parser;

import java.util.ArrayList;
import java.util.List;

public class SourceData {
	
	List<Object[]> allList = new ArrayList<>();
	List<Object[]> normalList = new ArrayList<>();
	List<Object[]> abnormalList = new ArrayList<>();
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
	
	public void putAll(Object[] normal) {
		allList.add(normal);
	}
	public void putNormal(Object[] normal) {
		normalList.add(normal);
	}
	
	public void putAbnormal(Object[] abnormal, String errorInfo) {
		abnormalList.add(abnormal);
		errorInfoList.add(errorInfo);
	}

}

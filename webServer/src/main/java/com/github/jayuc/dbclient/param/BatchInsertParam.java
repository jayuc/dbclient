package com.github.jayuc.dbclient.param;

import java.util.Map;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BatchInsertParam {
	
	private String sql;
	private String sourcePath;
	private String sourceType;
	private String[] sorts;
	private Map<Integer, String> rules;

}

package com.github.jayuc.dbclient.param;

import java.util.Map;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BatchInsertParam extends SqlDataParam {
	
	private String sql;
	private String sourcePath;
	private String sourceType;
	private int startRow;  // 
	private Map<Integer, String> rules;

}

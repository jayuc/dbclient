package com.github.jayuc.dbclient.param;

import lombok.Data;

@Data
public class BatchInsertParam {
	
	private String sql;
	private String sourcePath;
	private String sourceType;
	private String[] rules;

}

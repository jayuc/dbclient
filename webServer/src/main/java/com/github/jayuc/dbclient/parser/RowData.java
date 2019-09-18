package com.github.jayuc.dbclient.parser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * 行对象
 * @author jayu
 *
 */
@Data
@AllArgsConstructor
@ToString
public class RowData {

	private int index;  // 行号
	private Object[] data;  // 数据
	
}

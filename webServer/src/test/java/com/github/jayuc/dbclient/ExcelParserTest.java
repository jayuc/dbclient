package com.github.jayuc.dbclient;

import java.io.FileInputStream;
import java.util.List;

import org.junit.Test;

import com.github.jayuc.dbclient.parser.excel.ExcelParser;

public class ExcelParserTest {
	
	@Test
	public void test() {
		ExcelParser parser = new ExcelParser();
		final String path = "/home/jayu/log/upload/2019_09_05/a.xlsx";
		
		
		
		try {
//			List<List<String[]>> list = ExcelUtil.readExcel(new FileInputStream(path));
			List<Object[]> list = parser.parse(path, 1);
			System.out.println(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

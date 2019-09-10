package com.github.jayuc.dbclient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.github.jayuc.dbclient.utils.FileUtil;

public class FileTest {
	
	@Test
	public void getFileMagic() {
		
		System.out.println("测试文件魔数");
		
		final String path = "/home/jayu/log/upload/2019_09_05/";
		try {
			InputStream fs = new FileInputStream(path + "a.xlsx");
			System.out.println(fs.available());
			System.out.println(FileUtil.getMagicNumber(fs, 8));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}

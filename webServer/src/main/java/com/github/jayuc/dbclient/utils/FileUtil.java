package com.github.jayuc.dbclient.utils;

public class FileUtil {
	
	public static boolean appendUtf8String(String path, String content) {
		
		if(null == path) {
			return false;
		}
		
		cn.hutool.core.io.FileUtil.appendUtf8String(content, path);
		
		return true;
	}
	
	public static String readUtf8String(String path) {
		return cn.hutool.core.io.FileUtil.readUtf8String(path);
	}
	
	public static void mkdirs(String path) {
		cn.hutool.core.io.FileUtil.mkdir(path);
	}
	
	public static boolean exist(String path) {
		return cn.hutool.core.io.FileUtil.exist(path);
	}

}

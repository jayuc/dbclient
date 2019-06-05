package com.github.jayuc.dbclient.utils;

public class FileUtil {
	
	// 对文件追加内容
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
	
	// 覆盖文件
	public static boolean overflowUtf8String(String path, String content) {
		boolean b = cn.hutool.core.io.FileUtil.del(path);
		if(b) {
			return appendUtf8String(path, content);
		}
		return b;
	}

}

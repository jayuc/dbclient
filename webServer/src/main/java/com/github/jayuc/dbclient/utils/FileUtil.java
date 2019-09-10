package com.github.jayuc.dbclient.utils;

import java.io.IOException;
import java.io.InputStream;

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
	
	/**
	 * 获取文件的 魔数
	 * @param is
	 * @return
	 * @throws IOException 
	 */
	public static String getMagicNumber(InputStream fs, int number) throws IOException {
		if(fs != null) {
			byte[] bs = new byte[fs.available()];
			fs.read(bs);
			StringBuilder magicNumber = new StringBuilder();
	        //一个字节对应魔数的两位
	        int magicNumberByteLength = number/2;
	        for (int i = 0; i < magicNumberByteLength; i++) {
	            magicNumber.append(Integer.toHexString(bs[i] >> 4 & 0xF));
	            magicNumber.append(Integer.toHexString(bs[i] & 0xF));
	        }
	        fs.close();
	        return magicNumber.toString().toUpperCase();
		}
		return null;
	}

}

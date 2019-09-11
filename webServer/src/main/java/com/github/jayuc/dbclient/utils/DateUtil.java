package com.github.jayuc.dbclient.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static Date StringToDate(String str) throws ParseException {
		return format.parse(str);
	}
	
	public static Date StringToDate(String source, String format) throws ParseException {
		return new SimpleDateFormat(format).parse(source);
	}
	
	public static String DateToString(Date date) {
		return format.format(date);
	}

}

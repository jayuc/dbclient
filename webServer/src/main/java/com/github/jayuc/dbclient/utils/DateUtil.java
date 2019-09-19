package com.github.jayuc.dbclient.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static enum Type{
		YEAR(Calendar.YEAR),
		MONTH(Calendar.MONTH),
		DAY(Calendar.DAY_OF_MONTH),
		HOUR(Calendar.HOUR_OF_DAY),
		MINUTE(Calendar.MINUTE),
		SECOND(Calendar.SECOND);
		private final int value;
		private Type(int value) {
			this.value = value;
		}
	}
	
	public static Date StringToDate(String str) throws ParseException {
		return format.parse(str);
	}
	
	public static Date StringToDate(String source, String format) throws ParseException {
		return new SimpleDateFormat(format).parse(source);
	}
	
	public static String DateToString(Date date) {
		return format.format(date);
	}
	
	/**
	 * 获取日期的年/月/日/小时/分钟/秒
	 * @param date
	 * @param type
	 * @return
	 */
	public static int get(Date date, Type type) {
		Calendar calendar = calendar();
		calendar.setTime(date);
		return calendar.get(type.value);
	}
	
	private static Calendar calendar() {
		return Calendar.getInstance();
	}

}

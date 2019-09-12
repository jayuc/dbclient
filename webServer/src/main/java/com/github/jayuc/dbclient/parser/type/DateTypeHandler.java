package com.github.jayuc.dbclient.parser.type;

import java.util.Date;

import com.github.jayuc.dbclient.utils.DateUtil;

public class DateTypeHandler extends AbstractTypeHandler {

	@Override
	protected Class<?> classType() {
		return Date.class;
	}

	@Override
	protected Object doHandle(Object obj) throws Exception {
		
		if(String.class == obj.getClass()) {
			String source = (String) obj;
			int len = source.length();
			if(len == 19) { // 2019-10-10 10:10:10
				return DateUtil.StringToDate((String) obj);
			}else if(len == 10) { // 2019-10-10
				return DateUtil.StringToDate(source, "yyyy-MM-dd");
			}else if(len == 16) { // 2019-10-10 10:10
				return DateUtil.StringToDate(source, "yyyy-MM-dd HH:mm");
			}else if(len == 8) { // 10:10:01
				return DateUtil.StringToDate(source, "HH:mm:ss");
			}else if(len == 4) { // 2019
				return DateUtil.StringToDate(source, "yyyy");
			}
		}
		if(Long.class == obj.getClass()) {
			return new Date((long) obj);
		}
		
		return (Date)obj;
	}

	@Override
	protected String string(Object obj) throws Exception {
		return DateUtil.DateToString((Date)obj);
	}

}

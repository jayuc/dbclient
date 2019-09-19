package com.github.jayuc.dbclient.parser.type;

import java.util.Date;

import com.github.jayuc.dbclient.utils.DateUtil;

public class YearTypeHandler extends AbstractTypeHandler {
	
	private final DateTypeHandler dateHandler = new DateTypeHandler();

	@Override
	protected Class<?> classType() {
		return null;
	}

	@Override
	protected Object doHandle(Object obj) throws Exception {
		Date date = (Date) dateHandler.handle(obj);
		return DateUtil.get(date, DateUtil.Type.YEAR);
	}

	@Override
	protected String string(Object obj) throws Exception {
		return obj.toString();
	}

}

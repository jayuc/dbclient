package com.github.jayuc.dbclient.parser.type;

import java.math.BigDecimal;

public class NumberTypeHandler extends AbstractTypeHandler {

	@Override
	protected Class<?> classType() {
		return Double.class;
	}

	@Override
	protected Object doHandle(Object obj) throws Exception {
		
		if(String.class == obj.getClass()) {
			return Double.valueOf((String)obj);
		}
		
		if(Float.class == obj.getClass()) {
			BigDecimal bigDecimal = new BigDecimal((float)obj);
			return bigDecimal.doubleValue();
		}
		
		return (double)obj;
	}

	@Override
	protected String string(Object obj) throws Exception {
		return Double.toString((double)obj);
	}

}

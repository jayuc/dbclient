package com.github.jayuc.dbclient.parser.type;

public class IntTypeHandler extends AbstractTypeHandler {

	@Override
	protected Class<?> classType() {
		return Integer.class;
	}

	@Override
	protected Object doHandle(Object obj) throws Exception {
		
		if(Double.class == obj.getClass()) {
			return ((Double)obj).intValue();
		}
		
		if(String.class == obj.getClass()) {
			return Integer.valueOf((String) obj);
		}
		
		if(Float.class == obj.getClass()) {
			return ((Float)obj).intValue();
		}
		
		return (int)obj;
	}

	@Override
	protected String string(Object obj) throws Exception {
		return Integer.toString((int)obj);
	}

}

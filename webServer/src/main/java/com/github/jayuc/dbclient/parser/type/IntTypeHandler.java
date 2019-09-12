package com.github.jayuc.dbclient.parser.type;

public class IntTypeHandler extends AbstractTypeHandler {

	@Override
	protected Class<?> classType() {
		return Integer.class;
	}

	@Override
	protected Object doHandle(Object obj) throws Exception {
		
		if(String.class == obj.getClass()) {
			return Integer.valueOf((String) obj);
		}
		
		return (Integer)obj;
	}

	@Override
	protected String string(Object obj) throws Exception {
		return Integer.toString((int)obj);
	}

}

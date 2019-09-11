package com.github.jayuc.dbclient.parser.type;

import com.github.jayuc.dbclient.parser.TypeHandler;

public abstract class AbstractTypeHandler implements TypeHandler {

	@Override
	public Object handle(Object obj) throws Exception {
		if(obj == null) {
			return null;
		}
		if(classType() == obj.getClass()) {
			return obj;
		}
		return doHandle(obj);
	}
	
	protected abstract Class<?> classType();
	
	protected abstract Object doHandle(Object obj) throws Exception;

}

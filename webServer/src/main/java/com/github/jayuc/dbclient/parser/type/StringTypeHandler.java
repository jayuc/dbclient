package com.github.jayuc.dbclient.parser.type;

public class StringTypeHandler extends AbstractTypeHandler {

	@Override
	protected Class<?> classType() {
		return String.class;
	}

	@Override
	protected Object doHandle(Object obj) {
		return obj.toString();
	}

}

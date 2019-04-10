package com.github.jayuc.dbclient.err;

/**
 * 查询异常
 * @author yujie
 * 2019年4月10日 上午9:31:48
 */
public class SqlHandlerException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String message;

	public SqlHandlerException(String message) {
		super(message);
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

}

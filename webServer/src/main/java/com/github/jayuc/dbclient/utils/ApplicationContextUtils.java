package com.github.jayuc.dbclient.utils;

import org.springframework.context.ApplicationContext;

public class ApplicationContextUtils {
	
	private static ApplicationContext ac;

	public static ApplicationContext getAc() {
		return ac;
	}

	public static void setAc(ApplicationContext ac) {
		ApplicationContextUtils.ac = ac;
	}

}

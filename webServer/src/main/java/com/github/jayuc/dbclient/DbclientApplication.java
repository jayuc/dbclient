package com.github.jayuc.dbclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.github.jayuc.dbclient.utils.ApplicationContextUtils;

@SpringBootApplication
public class DbclientApplication {

	public static void main(String[] args) {
		ApplicationContext ac = SpringApplication.run(DbclientApplication.class, args);
		ApplicationContextUtils.setAc(ac);
	}

}

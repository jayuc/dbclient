package com.github.jayuc.dbclient;

import java.text.ParseException;

import org.junit.Test;

import com.github.jayuc.dbclient.parser.type.DateTypeHandler;
import com.github.jayuc.dbclient.parser.type.IntTypeHandler;
import com.github.jayuc.dbclient.parser.type.StringTypeHandler;
import com.github.jayuc.dbclient.utils.DateUtil;

public class TypeTest {
	
	@Test
	public void test() {
		
		String str = "2019-09-11 16:23";
		String i = "11";
		long now = System.currentTimeMillis();
		Object obj = new Object();
		
		try {
			System.out.println(DateUtil.StringToDate(str, "yyyy-MM-dd HH:mm"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		System.out.println();
		
		StringTypeHandler string = new StringTypeHandler();
		try {
			System.out.println(string.handle(str));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			System.out.println(string.handle(i));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			System.out.println(string.handle(now));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			System.out.println(string.handle(obj));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println();
		IntTypeHandler in = new IntTypeHandler();
//		try {
//			System.out.println(in.handle(str));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		try {
			System.out.println(in.handle(i));
		} catch (Exception e) {
			e.printStackTrace();
		}
//		try {
//			System.out.println(in.handle(now));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		try {
//			System.out.println(in.handle(obj));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		System.out.println();
		DateTypeHandler date = new DateTypeHandler();
		try {
			System.out.println(date.handle(str));
		} catch (Exception e) {
			e.printStackTrace();
		}
//		try {
//			System.out.println(date.handle(i));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		try {
			System.out.println(date.handle(now));
		} catch (Exception e) {
			e.printStackTrace();
		}
//		try {
//			System.out.println(date.handle(obj));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		try {
			System.out.println(date.handle("2019-01-10 12:45:01"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			System.out.println(date.handle("12:45:01"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			System.out.println(date.handle("2019-01-10"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			System.out.println(date.handle("2019"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}

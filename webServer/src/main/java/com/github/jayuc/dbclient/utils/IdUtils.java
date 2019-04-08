package com.github.jayuc.dbclient.utils;

import java.util.UUID;

import cn.hutool.core.util.IdUtil;

public class IdUtils {
	
	/**
	 * 生成一个唯一id
	 * @return
	 */
	public static String generateId() {
		return IdUtil.fastSimpleUUID();
	}
	
	public static void main(String[] args) {
		String id = IdUtils.generateId();
		System.out.println("faseid: " + id);
		System.out.println("simple: " + IdUtil.simpleUUID());
		System.out.println("uuid: " + IdUtil.fastUUID());
		System.out.println("random: " + IdUtil.randomUUID());
		System.out.println("object: " + IdUtil.objectId());
		System.out.println("string id: " + UUID.randomUUID().toString().replace("-", ""));
	}

}

package com.github.jayuc.dbclient.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.jayuc.dbclient.utils.IdUtils;
import com.github.jayuc.dbclient.utils.StringUtil;

@RestController
@RequestMapping("/upload")
public class UploadController {

	@Value("${feedback.location}")
	private String rootPath;
	
	/**
	 * 上传单个文件
	 * @param request
	 * @return 文件路径 0失败 1成功
	 */
	@PostMapping("/file/one")
	public Map<String, Object> file(@RequestParam("file") MultipartFile file){
		
		Map<String, Object> map = new HashMap<>();
		
		if(file == null) {
			map.put("status", 0);
			map.put("errorInfo", "文件为空");
			return map;
		}
		
		File dest = fileName(getFileSuffix(file.getOriginalFilename()));
		
		try {
			file.transferTo(dest);
			map.put("status", 1);
			map.put("path", dest.getPath());
		} catch (Exception e) {
			map.put("status", 0);
			map.put("errorInfo", "上传时出错");
			e.printStackTrace();
		}
		
		return map;
	}
	
	private File fileName(String suffix) {
		final String dir = rootPath + "upload/" + currentDay();
		File dis = new File(dir);
		if(!dis.exists()) {
			dis.mkdirs();
		}
		return new File(dir + IdUtils.generateId() + suffix);
	}
	private String getFileSuffix(String fileName) {
		if(StringUtil.isBlank(fileName)) {
			return "";
		}
		int index = fileName.lastIndexOf(".");
		return fileName.substring(index, fileName.length());
	}
	
	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd");
	private String currentDay() {
		return formatter.format(new Date()) + "/";
	}
	
}

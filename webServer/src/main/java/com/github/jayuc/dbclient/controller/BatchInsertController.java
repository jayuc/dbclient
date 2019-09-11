package com.github.jayuc.dbclient.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.jayuc.dbclient.entity.Result;
import com.github.jayuc.dbclient.param.BatchInsertParam;
import com.github.jayuc.dbclient.service.BatchInsertService;
import com.github.jayuc.dbclient.utils.ResultUtils;
import com.github.jayuc.dbclient.utils.StringUtil;

@RestController
@RequestMapping("/batch")
public class BatchInsertController {
	
	@Autowired
	BatchInsertService batchService;

	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public Map<String, Object> insert(@RequestParam("param") BatchInsertParam param){
		Result result = ResultUtils.simpleResult();
		if(checkParam(result, param.getSql(), "sql") 
				|| checkParam(result, param.getSourcePath(), "文件地址") 
				|| checkParam(result, param.getSourceType(), "文件类型")) {
			return result.getResult();
		}
		return batchService.insert(param);
	}
	
	private boolean checkParam(Result result, String field, String error) {
		if(StringUtil.isBlank(field)) {
			result.setError(error + "不能为空");
			return true;
		}
		return false;
	}
	
}

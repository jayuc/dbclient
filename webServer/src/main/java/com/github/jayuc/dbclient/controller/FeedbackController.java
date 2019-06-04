package com.github.jayuc.dbclient.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.jayuc.dbclient.entity.Result;
import com.github.jayuc.dbclient.param.IssueParam;
import com.github.jayuc.dbclient.service.FeedbackService;
import com.github.jayuc.dbclient.utils.ResultUtils;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
	
	private static final Logger LOG = LoggerFactory.getLogger(FeedbackController.class);
	
	@Autowired
	FeedbackService feedbackService;
	
	@PostMapping("/publish")
	public Map<String, Object> publish(IssueParam param){
		LOG.debug("请求feedback publish: " + param);
		Result result = ResultUtils.simpleResult();
		
		feedbackService.publish(param);
		
		return result.getResult();
	}

}

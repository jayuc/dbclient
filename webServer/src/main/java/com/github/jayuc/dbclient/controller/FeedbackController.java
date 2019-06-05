package com.github.jayuc.dbclient.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.jayuc.dbclient.entity.FeedbackIssue;
import com.github.jayuc.dbclient.entity.Result;
import com.github.jayuc.dbclient.iter.IFeedbackService;
import com.github.jayuc.dbclient.param.IssueParam;
import com.github.jayuc.dbclient.utils.ResultUtils;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
	
	private static final Logger LOG = LoggerFactory.getLogger(FeedbackController.class);
	
	@Autowired
	IFeedbackService feedbackService;
	
	@PostMapping("/publish")
	public Map<String, Object> publish(IssueParam param){
		LOG.debug("请求feedback publish: " + param);
		Result result = ResultUtils.simpleResult();
		
		Map<String, Object> map = feedbackService.publish(param);
		result.setProperty("id", map.get("id"));
		
		return result.getResult();
	}
	
	@GetMapping("/getList")
	public Map<String, Object> getList(){
		LOG.debug("获取所有问题反馈记录");
		Result result = ResultUtils.normalResult();
		List<FeedbackIssue> list = feedbackService.getList();
		result.setResult(list.size(), list);
		return result.getResult();
	}
	
	@PostMapping("/modifyStatus")
	public int modifyStatus(String id, String status) {
		LOG.debug("修改问题状态,id: " + id + " ,status: " + status);
		return feedbackService.modifyStatus(id, status);
	}
	
	@GetMapping("/getFeedbackById")
	public FeedbackIssue getFeedbackById(String id) {
		LOG.debug("获取问题反馈内容: " + id);
		return feedbackService.getFeedbackById(id);
	}

}

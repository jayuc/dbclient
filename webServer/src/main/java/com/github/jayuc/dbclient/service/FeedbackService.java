package com.github.jayuc.dbclient.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.jayuc.dbclient.config.FeedbackConfig;
import com.github.jayuc.dbclient.param.IssueParam;
import com.github.jayuc.dbclient.utils.FileUtil;
import com.github.jayuc.dbclient.utils.IdUtils;

@Service
public class FeedbackService {
	
	@Value("${feedback.location}")
	private String root;
	
	private static final Logger LOG = LoggerFactory.getLogger(FeedbackService.class);

	public void publish(IssueParam param) {
		
		LOG.debug("root: " + root);
		if(!FileUtil.exist(root)) {
			FileUtil.mkdirs(root);
		}
		
		String fn = IdUtils.generateId();
		
		FileUtil.appendUtf8String(root + FeedbackConfig.INDEX, 
				param.getTitle() + FeedbackConfig.SPACE + fn + FeedbackConfig.SEP);
		FileUtil.appendUtf8String(root + fn, param.getContent());
		
	}
	
}

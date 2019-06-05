package com.github.jayuc.dbclient.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.jayuc.dbclient.config.FeedbackConfig;
import com.github.jayuc.dbclient.entity.FeedbackIssue;
import com.github.jayuc.dbclient.iter.IFeedbackService;
import com.github.jayuc.dbclient.param.IssueParam;
import com.github.jayuc.dbclient.utils.FileUtil;
import com.github.jayuc.dbclient.utils.IdUtils;

@Service
public class FeedbackService implements IFeedbackService {
	
	@Value("${feedback.location}")
	private String root;
	
	private static final Logger LOG = LoggerFactory.getLogger(FeedbackService.class);

	public Map<String, Object> publish(IssueParam param) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		LOG.debug("root: " + root);
		if(!FileUtil.exist(root)) {
			FileUtil.mkdirs(root);
		}
		
		String fn = IdUtils.generateId();
		String titles = formatContent(param, fn);
		LOG.debug("titles: " + titles);
		
		/**
		 * 存储索引的文件（包括问题标题）
		 */
		String indexPath = getIndexPath();
		LOG.debug("indexPath: " + indexPath);
		FileUtil.appendUtf8String(indexPath, titles);
		/**
		 * 根据索引，存储问题内容
		 */
		String contentPath = getContentPath(fn);
		LOG.debug("contentPath: " + contentPath);
		FileUtil.appendUtf8String(contentPath, param.getContent());
		/**
		 * 存储状态   0:未解决       1：已解决
		 */
		String statusPath = getStatusPath(fn);
		LOG.debug("statusPath: " + statusPath);
		FileUtil.appendUtf8String(statusPath, "0");
		
		map.put("id", fn);
		return map;
		
	}

	@Override
	public List<FeedbackIssue> getList() {
		List<FeedbackIssue> list = new ArrayList<>();
		String indexPath = getIndexPath();
		LOG.debug("indexPath: " + indexPath);
		if(FileUtil.exist(indexPath)) {
			String data = FileUtil.readUtf8String(indexPath);
			LOG.debug("data: " + data);
			String[] dataArr = data.split(FeedbackConfig.SEP);
			for(int i=0; i<dataArr.length; i++) {
				FeedbackIssue issue = new FeedbackIssue();
				issue.setIndex(i + 1);
				LOG.debug("data arr index: " + dataArr[i]);
				String[] item = dataArr[i].split(FeedbackConfig.SPACE);
				issue.setTitle(item[0]);
				issue.setId(item[1]);
				issue.setToken(item[2]);
				String statusPath = getStatusPath(item[1]);
				LOG.debug("statusPath: " + statusPath);
				String status = FileUtil.readUtf8String(statusPath);
				issue.setStatus(status);
				list.add(issue);
			}
		}
		return list;
	}

	@Override
	public int modifyStatus(String id, String status) {
		if("1".equals(status)) {
			String statusPath = getStatusPath(id);
			LOG.debug("statusPath: " + statusPath);
			String st = FileUtil.readUtf8String(statusPath);
			if("1".equals(st)) {
				return 0;
			}
			boolean b = FileUtil.overflowUtf8String(statusPath, status);
			if(b) {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public FeedbackIssue getFeedbackById(String id) {
		String contentPath = getContentPath(id);
		LOG.debug("contentPath: " + contentPath);
		FeedbackIssue issue = new FeedbackIssue();
		String content = FileUtil.readUtf8String(contentPath);
		issue.setContent(content);
		issue.setId(id);
		return issue;
	}

	/**
	 * 对数据进行格式化的存储
	 * @param param
	 * @return
	 */
	private String formatContent(IssueParam param, String fn) {
		/**
		 * 问题内容存储格式,如下：
		 * 问题标题|||存储问题内容的文件名|||提问题用户的token||||
		 */
		return param.getTitle() + FeedbackConfig.SPACE + fn + FeedbackConfig.SPACE
				+ param.getToken() + FeedbackConfig.SEP;
	}
	
	// 获取索引文件路径
	private String getIndexPath() {
		return root + FeedbackConfig.INDEX;
	}
	// 获取内容文件路径
	private String getContentPath(String id) {
		return root + id;
	}
	// 获取状态文件路径
	private String getStatusPath(String id) {
		return root + FeedbackConfig.STATUS_PRE + id;
	}
	
}

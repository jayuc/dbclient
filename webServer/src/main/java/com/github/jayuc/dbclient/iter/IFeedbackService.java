package com.github.jayuc.dbclient.iter;

import java.util.List;
import java.util.Map;

import com.github.jayuc.dbclient.entity.FeedbackIssue;
import com.github.jayuc.dbclient.param.IssueParam;

public interface IFeedbackService {
	
	Map<String, Object> publish(IssueParam param);
	
	List<FeedbackIssue> getList();
	
	int modifyStatus(String id, String status);
	
	FeedbackIssue getFeedbackById(String id);

}

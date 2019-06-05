package com.github.jayuc.dbclient.entity;

import com.github.jayuc.dbclient.iter.IToken;

import lombok.Data;

@Data
public class FeedbackIssue implements IToken {

	private String token;
	
	private String title;
	
	private String content;
	
	private String status;
	
	private String id;
	
	private int index;
	
}

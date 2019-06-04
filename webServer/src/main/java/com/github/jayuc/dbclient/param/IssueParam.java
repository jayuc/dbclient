package com.github.jayuc.dbclient.param;

import com.github.jayuc.dbclient.iter.IToken;

import lombok.Data;

@Data
public class IssueParam implements IToken {

	private String token;
	
	private String title;
	
	private String content;
	
}

/**
 * Copyright (c) 2012, xushun. All rights reserved.
 * 
 * 任何人可以复制、传播和修改程序源代码，但必须在显著位置发布适当的著作权标示及无担保声明
 * 本程序无偿授权，所以不保证这个软件的实用性及其对用户造成的任何损失负责
 * 
 * @author xushun
 * 
 *  email : xushun007@163.com
 */

package com.xushun.model;

public class QueryResult {
	private String url;
	private String title;
	private String webCachePath;
	private String abstruct ;
	
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getAbstruct() {
		return abstruct;
	}
	
	public void setAbstruct(String abstruct) {
		this.abstruct = abstruct;
	}
	
	public String getWebCachePath() {
		return webCachePath;
	}
	
	public void setWebCachePath(String webCachePath) {
		this.webCachePath = webCachePath;
	}

	
}

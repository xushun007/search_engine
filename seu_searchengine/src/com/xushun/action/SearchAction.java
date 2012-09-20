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

package com.xushun.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xushun.model.QueryResult;
import com.xushun.query.QueryService;

public class SearchAction extends HttpServlet {
	
	private List<QueryResult> resultList;
	
	private QueryService queryService;
	
	private String searchContent; 

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		
		searchContent = req.getParameter("searchContent");
		
		System.out.println("search content: " + searchContent);
		
		if(searchContent.trim().equals("")) {
			req.getRequestDispatcher("/search.jsp").forward(req, resp);
		}
		
		//queryStr = "教育部重点实验室评估";
		
		queryService = new QueryService(searchContent);
		
		try {
			resultList = queryService.excuteQuery();
			req.setAttribute("resultList", resultList);
			req.setAttribute("searchContent", searchContent);
			req.getRequestDispatcher("/search.jsp").forward(req, resp);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	
	public void setQueryService(QueryService queryService) {
		this.queryService = queryService;
	}

	
	public List<QueryResult> getResultList() {
		return resultList;
	}

	public void setResultList(List<QueryResult> resultList) {
		this.resultList = resultList;
	}	
	
	public QueryService getQueryService() {
		return queryService;
	}
	
	public String getSearchContent() {
		return searchContent;
	}

	public void setSearchContent(String searchContent) {
		this.searchContent = searchContent;
	}

}

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

package com.xushun.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.xushun.dao.IndexDAO;
import com.xushun.dao.PageDAO;
import com.xushun.model.FIndexKey;
import com.xushun.model.ForwardIndex;
import com.xushun.model.InvertedIndex;
import com.xushun.model.PageInfo;
import com.xushun.model.QueryResult;
import com.xushun.util.WordSegmentation;


public class QueryService {
	
	private static final int MAX_RET_RESULT_NUM = 30;
	
	private static final int RET_STR_NUM  = 240;
	
	private String queryStr;
	private List<String> keywords;
	private List<QueryResult> result;
	
//	private static Map<String, ArrayList<Integer>> invertedIndex = InvertedIndexService.globalInvertedIndex;
	private static Map<String, ArrayList<Integer>> invertedIndex = null;
	private static Map<FIndexKey, Double > fIndexKeyMap = null;
	
//	private static Map<String, ArrayList<Integer>> queryInvertedIndex = null;
	private static HashSet<String> wordset = null;
	
	
	static {
		wordset = IndexDAO.buildGlobalKeywordSet();
//		System.out.println(wordset);
		invertedIndex = IndexDAO.buildGlobalInvertedIndex();
		System.out.println("words : " + invertedIndex.size());
		
//		fIndexKeyMap = IndexDAO.buildGlobalFIndexTermFreMap();
//		System.out.println("forward_index size: " + fIndexKeyMap.size());
	}
	
	
	public QueryService(String queryStr) {
		this.queryStr = queryStr;
	}
	
	public QueryService() {}
	
	public List<String> segmentation() throws Exception{
		
		List<String> tempKeywords = WordSegmentation.getKeyWord(queryStr, true);
		
		keywords = new ArrayList<String>();
//		keywords.add(queryStr);
		
		for(String key: tempKeywords) {
			if(!keywords.contains(key)) {
				keywords.add(key);
			}
		}

		System.out.println("keyword: " + keywords);
		return keywords;
	}
	
	public List<QueryResult> excuteQuery() throws Exception{
		segmentation();
		
		System.out.println("documents -- keyword: " + new Date());
		// each keyword -- ArrayList<Integer>
		List <ArrayList<Integer>> queryList = new ArrayList<ArrayList<Integer>>(); 
		
		for(String keyword: keywords) {
			ArrayList<Integer> docs = invertedIndex.get(keyword);
			if(docs == null) {
				return null;
			}
			queryList.add(docs);
		}
		System.out.println(queryList);
		
		List<Integer> resultDocIds = intersection(queryList);
		System.out.println("fit document: " + resultDocIds);
		
		System.out.println("get keyword tf/ idf: " + new Date());
		List<DocTfidf> tfIdfList = new ArrayList<DocTfidf>();
		Map<String, Double> wordIDFMap = IndexDAO.getWordIDFMap(keywords);
		Map<Integer, HashMap<String, Double>> docListTFMap = IndexDAO.getDocListTFMap(resultDocIds, keywords);
		
		System.out.println("compute keyword tf/idf & sort : " + new Date());
		for(Map.Entry<Integer, HashMap<String, Double>> docTFMap: docListTFMap.entrySet()) {
			Integer docId = docTFMap.getKey();
			HashMap<String, Double> tfMap = docTFMap.getValue();
			
			double sum = 0.0;
			for(Map.Entry<String, Double> entry: tfMap.entrySet()) {
				String word = entry.getKey();
				Double tf = entry.getValue();
				sum += tf * wordIDFMap.get(word);
			}
			
			tfIdfList.add(new DocTfidf(docId, sum));
		}
		Collections.sort(tfIdfList);
		

		System.out.println("top N : " + new Date());
		// accrodding to TF/IDF sorted docId list
		List<Integer> sortedDoc = new ArrayList<Integer>();
		if(tfIdfList.size() > MAX_RET_RESULT_NUM) {
			for(int i = 0; i < MAX_RET_RESULT_NUM; ++i) {
				sortedDoc.add(tfIdfList.get(i).getDocId());
			}
		}
		else {
			for(DocTfidf entry: tfIdfList) {
				sortedDoc.add(entry.getDocId());
			}
		}

		System.out.println("get pageList : " + new Date());
		List<PageInfo> pageInfoList = PageDAO.getPageList(sortedDoc);
		
		System.out.println("get result: " +new Date());
		result = generateQueryResult(pageInfoList, keywords.get(0));
		
		System.out.println("end: query results: " + result.size());

		return result;
	}
	
	
	// 数据库操作多，速度慢，废弃
	public List<QueryResult> excuteQuery3() throws Exception{
		segmentation();
		
		
		// each keyword -- ArrayList<Integer>
		List <ArrayList<Integer>> queryList = new ArrayList<ArrayList<Integer>>(); 
		
		for(String keyword: keywords) {
			ArrayList<Integer> docs = invertedIndex.get(keyword);
			if(docs == null) {
				return null;
			}
			queryList.add(docs);
		}
		
		System.out.println(queryList);
		
		List<Integer> resultDocIds = intersection(queryList);
		
		System.out.println("fit document: " + resultDocIds);
		if(resultDocIds == null || resultDocIds.size() == 0) 
			return null;
		
		
		System.out.println("keyword InvertedIndex: " + new Date());
		
		List<DocTfidf> tfIdfList = new ArrayList<DocTfidf>();
//		Map<Integer, List<ForwardIndex>> forwardMap = new HashMap<Integer, List<ForwardIndex>>();
		List<InvertedIndex> invetedIndexList = IndexDAO.getInvertedIndexList(keywords);
		
		for(Integer docId: resultDocIds) {
			List<ForwardIndex> forwardIndexList = IndexDAO.getForwardIndexList(docId, keywords);
			
			double sum = 0.0;
			for(int i = 0; i < invetedIndexList.size(); ++i) {
				sum += invetedIndexList.get(i).getIdf() * forwardIndexList.get(i).getTermfre();
			}
			
			tfIdfList.add(new DocTfidf(docId, sum));
			
		}
		
		Collections.sort(tfIdfList);
		

		System.out.println("top N : " + new Date());
		// accrodding to TF/IDF sorted docId list
		List<Integer> sortedDoc = new ArrayList<Integer>();
		if(tfIdfList.size() > MAX_RET_RESULT_NUM) {
			for(int i = 0; i < MAX_RET_RESULT_NUM; ++i) {
				sortedDoc.add(tfIdfList.get(i).getDocId());
			}
		}
		else {
			for(DocTfidf entry: tfIdfList) {
				sortedDoc.add(entry.getDocId());
			}
		}

		System.out.println("get pageList : " + new Date());
		List<PageInfo> pageInfoList = PageDAO.getPageList(sortedDoc);
		
		System.out.println("get result: " +new Date());
		result = generateQueryResult(pageInfoList, keywords.get(0));
		
		System.out.println("end: query results: " + result.size());

		return result;
	}
	

	public List<QueryResult> generateQueryResult(List<PageInfo> pageInfoList, String keyword) {
		
		List<QueryResult> resultList = new ArrayList<QueryResult>();

		for(PageInfo pageInfo : pageInfoList) {
			String text = pageInfo.getPlain();
			String abstruct = generateAbstruct(text, keyword);

			QueryResult qResult = new QueryResult();
			qResult.setUrl(pageInfo.getUrl());
			qResult.setTitle(pageInfo.getTitle());
			qResult.setWebCachePath(pageInfo.getSavedName());
			qResult.setAbstruct(abstruct);

			resultList.add(qResult);
		}

		return resultList;
	}
	
	class DocTfidf implements Comparable<DocTfidf>  {
		private int docId;
		private double tfidf;
		
		public DocTfidf(int docId, double tfidf) {
			this.docId = docId;
			this.tfidf = tfidf;
		}
		
		public int getDocId() {
			return docId;
		}
		public void setDocId(int docId) {
			this.docId = docId;
		}
		public double getTfidf() {
			return tfidf;
		}
		public void setTfidf(double tfidf) {
			this.tfidf = tfidf;
		}
		
		public int compareTo(DocTfidf o) {
			if(tfidf == o.tfidf)
				return 0;
			else if(tfidf > o.tfidf ) 
				return 1;
			else
				return -1;
		}
		
	}
	
	
	private List<Integer> intersection(List< ArrayList<Integer>> queryList) {
		
//		List<Integer> interResult = new ArrayList<Integer>();
		if(queryList == null || queryList.size() < 1)
			return null;
		
		List<Integer> interResult = queryList.get(0);
		
		List<Integer> commonDocs = new ArrayList<Integer>();
		for(int i =1; i < queryList.size(); ++i) {
			List<Integer> docs = queryList.get(i);
			for(Integer docId: docs) {
				if(interResult.contains(docId)) {
					commonDocs.add(docId);
				}
			}
			
			List<Integer> temp = interResult;
			interResult = commonDocs;
			commonDocs = temp;
			commonDocs.clear();
		}
		
		return interResult;
	}
	
	
	public String generateAbstruct(String text, String keyword) {
		StringBuffer buffer = new StringBuffer();
		int position = text.indexOf(keyword);
		int start = 0;
		
		int temp = (position - 150 < 0)?0:(position - 150);
		
//		System.out.println(text);
		start = text.indexOf("。", temp);
		if((start == -1) || (start >= position))
			start =0;
		
		if(start !=0)
			start+=1;
		else {
			if(position > 140) {
				start = position - 140;
			}
		}
		
		System.out.println("start: " + start + " postion : "  + position);
		buffer.append(text.substring(start, position-1)).append("<b style=\"color:red\">").append(keyword).append("</b>");
		
//		System.out.println(buffer.length());
		if(buffer.length() < RET_STR_NUM) {
			int startIndex = position + keyword.length();
			int endIndex = startIndex + RET_STR_NUM - buffer.length() - 1;
			if(endIndex > text.length())
				endIndex = text.length() -1;
			buffer.append(text.substring(startIndex, endIndex));
		}
		
		//System.out.println(buffer.toString());
		
		return buffer.toString();
	}
	
	public static void main(String[] args) {
		QueryService query = new QueryService();
		
		String s = "8月31日上午，  东南大学国家大学科技园以“税务•融资•市场推广”为主题，召开今年第三季度的公共服务平台专场报告会。       报告会共有主题报告和提问咨询两大环节。百度南京营销服务中心资深营销专家、玄武区国税局政策法规科科长、江苏省农业银行科技支行金融专家在会上分别作主题报告。        据悉，此次报告会是东大科技园2012年公共服务平台系列专场报告会的第三场主题活动，旨在指导园区企业顺利完成“营业税改征增值税”相关事宜、成功申请科技银行贷款，并共同探讨中小企业如何利用网络进行市场推广，充分发挥公共服务平台的综合服务功能。";
		String keyword = "报告会";
		
		String abstruct = query.generateAbstruct(s, keyword);
		
		System.out.println(abstruct);
	}

}

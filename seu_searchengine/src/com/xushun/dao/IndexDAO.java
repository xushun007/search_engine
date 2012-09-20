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

package com.xushun.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.xushun.model.FIndexKey;
import com.xushun.model.ForwardIndex;
import com.xushun.model.InvertedIndex;
import com.xushun.util.StringUtil;
import com.xushun.util.WordSegmentation;

/**
 * 正排索引，倒排索引操作类
 *
 */
public class IndexDAO {

	public static Connection conn = null;

	private static int NUM_FORWARD_INDEX = 2000000;
	
	public static Map<FIndexKey, Double > buildGlobalFIndexTermFreMap() {
		
		Map<FIndexKey, Double > fIndexTermFreMap = new HashMap<FIndexKey, Double >(NUM_FORWARD_INDEX);
		
		try{
			conn = DBConnection.getConnectionInstance();
			String sql = "select docid, word, termfre from forward_index";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			int cnt = 0;
			while(rs.next()) {
				
				FIndexKey fKey = new FIndexKey();
				fKey.setDocId(rs.getInt(1));
				fKey.setWord(rs.getString(2));
				
				if(++cnt%1000 == 0)
					System.out.println(cnt);
				
				fIndexTermFreMap.put(fKey, rs.getDouble(3));
			}
			
		}
		catch(Exception e) {
			
			System.out.println("get failure !");
			e.printStackTrace();
		}

		return fIndexTermFreMap;
	}
	
	public static HashSet<String> buildGlobalKeywordSet() {
		String sql = "select distinct word from inverted_index";
		HashSet<String> wordSet = new HashSet<String>();
		
		try{
			conn = DBConnection.getConnectionInstance();
			
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				wordSet.add(rs.getString(1));
			}
				
		}
		catch(Exception e) {
			System.out.println("read word failure!");
			e.printStackTrace();
		}
		
		if(wordSet.size() == 0)
			return null;
		
		return wordSet;
	}
	
	public static Map<String, ArrayList<Integer>> buildGlobalInvertedIndex() {
		Map<String, ArrayList<Integer>> globleInvertedIndex = new HashMap<String, ArrayList<Integer>>();

		
		try{
			conn = DBConnection.getConnectionInstance();
			String sql = "select word,doclist  from inverted_index";
			
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				String word = rs.getString(1);
				String docListStr = rs.getString(2);
				ArrayList<Integer> docList = (ArrayList<Integer>)StringUtil.strToList(docListStr);
				
				globleInvertedIndex.put(word, docList);
			}
			
		}
		catch(Exception e) {
			
			System.out.println("get global inverted index failure !");
			e.printStackTrace();
		}
		
		return globleInvertedIndex;
	}
	
	
	public static Map<String, ArrayList<Integer>> buildGlobalInvertedIndex(HashSet<String> wordList) {
		Map<String, ArrayList<Integer>> invertedIndex = new HashMap<String, ArrayList<Integer>>();

		for(String word : wordList) {
			InvertedIndex ii = getInvertedIndex(word);

			if(ii != null) {
				String urlListStr = ii.getDocListStr();

				ArrayList<Integer> urlList = (ArrayList<Integer>)StringUtil.strToList(urlListStr);

				invertedIndex.put(word, urlList);

			}
		}
		
		return invertedIndex;
	}
	
	public static void addForwardIndex(int docId, Map<String, ArrayList<Integer> > wordMap, int totalWord) {
		String sql = "insert into forward_index(docid, word, nhits, hitlist,termfre) values(?, ?, ?, ?, ?)";
		String s = null;
		
		try{
			conn = DBConnection.getConnectionInstance();
			
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			
			for(Map.Entry<String, ArrayList<Integer>> entry: wordMap.entrySet()) {
	    		String word = entry.getKey();
	    		ArrayList<Integer> offsetList = entry.getValue();
	    		
	    		double termfre = ((double)offsetList.size())/totalWord;
	    		
	    		preparedStatement.setInt(1, docId);
	    		preparedStatement.setString(2, word);
	    		preparedStatement.setInt(3, offsetList.size());
	    		preparedStatement.setString(4, WordSegmentation.list2Str(offsetList));
	    		
	    		s = WordSegmentation.list2Str(offsetList);
	    		preparedStatement.setDouble(5, termfre);
	    		
	    		preparedStatement.executeUpdate();
	    	}
		}
		catch(Exception e) {
			System.out.println("add forwardindex failure!");
			System.out.println(s);
			e.printStackTrace();
		}
		
	}
	
	public static void addInvertedIndex(Map<String, ArrayList<Integer>> invertedMap, int totalDoc) {
		String sql = "insert into inverted_index(word, ndocs, doclist, idf) values(?, ?, ?, ?)";
		
		try{
			conn = DBConnection.getConnectionInstance();
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			
			for(Map.Entry<String, ArrayList<Integer>> entry: invertedMap.entrySet()) {
				String word = entry.getKey();
				ArrayList<Integer> docs = entry.getValue();
				
				int size = docs.size();
				double idf = Math.log(((double)totalDoc)/size);
				
				preparedStatement.setString(1, word);
				preparedStatement.setInt(2, docs.size());
				preparedStatement.setString(3, WordSegmentation.list2Str(docs));
				preparedStatement.setDouble(4, idf);
				
				preparedStatement.executeUpdate();
			}
		}
		catch(Exception e) {
			System.out.println("add forwardindex failure!");
			e.printStackTrace();
		}
	}
	
	// test 
	public static void main(String[] args) {
		List<Integer> docList = new ArrayList<Integer>();
		docList.add(3); docList.add(7);
		List<String> wordList = new ArrayList<String>();
		wordList.add("东南大学"); wordList.add("实验室"); 
		
		Map<Integer, HashMap<String, Double>> docListMap = getDocListTFMap(docList, wordList);
		System.out.println(docListMap);
		
		Map<String, Double> wordIDFMap = getWordIDFMap(wordList);
		System.out.println(wordIDFMap);
	}
	
	/**
	 * 获取关键字所对应的IDF值
	 * @param wordList 关键字列表
	 * @return 
	 */
	public static Map<String, Double> getWordIDFMap(List<String> wordList) {
		
		Map<String, Double> wordIDFMap = new HashMap<String ,Double>();
		
		StringBuilder wordListBuf = new StringBuilder();
		wordListBuf.append("(");
		for(String word: wordList) {
			wordListBuf.append("'").append(word).append("'").append(",");
		}
		wordListBuf.deleteCharAt(wordListBuf.length() - 1);
		wordListBuf.append(")");
		
		String sql = "select word, idf from inverted_index where word in " + wordListBuf.toString();
		System.out.println(sql);
		
		try{
			conn = DBConnection.getConnectionInstance();
			
			
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				
				String word = rs.getString(1);
				Double idf = rs.getDouble(2);
			
				wordIDFMap.put(word, idf);
			}
		}
		catch(Exception e) {
			
			System.out.println("getWordIDFMap failure !");
			e.printStackTrace();
		}
		
		return wordIDFMap;
	}
	
	
	/**
	 * 获取文档所包含关键字的TF值
	 * @param docList 文档列表ID
	 * @param wordList 关键字列表
	 * @return
	 */
	public static Map<Integer, HashMap<String, Double>> getDocListTFMap(List<Integer> docList, List<String> wordList) {
		Map<Integer, HashMap<String, Double>> docListTFMap = new HashMap<Integer, HashMap<String, Double>>();
		
		StringBuilder docListBuf = new StringBuilder();
		docListBuf.append("(");
		for(Integer docId: docList) {
			docListBuf.append(docId).append(",");
		}
		docListBuf.deleteCharAt(docListBuf.length() - 1);
		docListBuf.append(")");
		
//		System.out.println(docListBuf.toString());
		
		StringBuilder wordListBuf = new StringBuilder();
		wordListBuf.append("(");
		for(String word: wordList) {
			wordListBuf.append("'").append(word).append("'").append(",");
		}
		wordListBuf.deleteCharAt(wordListBuf.length() - 1);
		wordListBuf.append(")");
//		System.out.println(wordListBuf.toString());
		
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append("select docId, word, termfre from forward_index where docId in ").append(docListBuf.toString()).append("and word in ").append(wordListBuf.toString());
		String sql = sqlBuf.toString();
		System.out.println(sql);
		
		try{
			conn = DBConnection.getConnectionInstance();
			
			
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				Integer docId = rs.getInt(1);
				String word = rs.getString(2);
				Double termFre = rs.getDouble(3);
				
				HashMap<String, Double> docIdWordMap = docListTFMap.get(docId);
				if(docIdWordMap == null) {
					HashMap<String, Double> wordTFMap = new HashMap<String, Double>();
					wordTFMap.put(word, termFre);
					docListTFMap.put(docId, wordTFMap);
				}
				else {
					docIdWordMap.put(word, termFre);
				}
				
			
			}
			
		}
		catch(Exception e) {
			
			System.out.println("getDocListTFMap failure !");
			e.printStackTrace();
		}
		
		return docListTFMap;
	}

	public static List<ForwardIndex> getForwardIndexList(int docId, List<String> words) {
		List<ForwardIndex> forwardIndexList = new ArrayList<ForwardIndex>();
		
		for(String word: words) {
			ForwardIndex fi = getForwardIndex(docId, word);
			if(fi != null) {
				forwardIndexList.add(fi);
			}
		}
		
		return forwardIndexList;
	}
	
	
	public static ForwardIndex getForwardIndex(int docId, String word) {
		ForwardIndex forwardIndex = null;
		
		try{
			conn = DBConnection.getConnectionInstance();
			String sql = "select * from forward_index where docId = ? and word = ?";
			
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, docId);
			preparedStatement.setString(2, word);
			
			int cnt = 0;
			ResultSet rs = preparedStatement.executeQuery();
			forwardIndex = new ForwardIndex();
			while(rs.next()) {
				++cnt;
				
				forwardIndex.setForwardId(rs.getInt(1));
				forwardIndex.setDocId(rs.getInt(2));
				forwardIndex.setWord(rs.getString(3));
				forwardIndex.setNhits(rs.getInt(4));
				forwardIndex.setHitlist(rs.getString(5));
				forwardIndex.setTermfre(rs.getDouble(6));
			}
			
			if(cnt == 0)
				return null;
			
		}
		catch(Exception e) {
			
			System.out.println("get failure !");
			e.printStackTrace();
		}
		
		return forwardIndex;
	}
	
	public static List<InvertedIndex> getInvertedIndexList(List<String> words) {
		List<InvertedIndex> invertedIndexList = new ArrayList<InvertedIndex>();
		
		for(String word: words) {
			InvertedIndex ii = getInvertedIndex(word);
			if(ii != null) {
				invertedIndexList.add(ii);
			}
		}
		
		return invertedIndexList;
	}
	
	
	public static InvertedIndex getInvertedIndex(String word) {
		InvertedIndex invertedIndex = null;
		
		try{
			conn = DBConnection.getConnectionInstance();
			String sql = "select * from inverted_index where word = ?";
			
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, word);
			
			int cnt = 0;
			ResultSet rs = preparedStatement.executeQuery();
			invertedIndex = new InvertedIndex();
			while(rs.next()) {
				++cnt;
				
				invertedIndex.setInvertedId(rs.getInt(1));
				invertedIndex.setWord(rs.getString(2));
				invertedIndex.setNdocs(rs.getInt(3));
				invertedIndex.setDocListStr(rs.getString(4));
				invertedIndex.setIdf(rs.getDouble(5));
			}
			
			if(cnt == 0)
				return null;
			
		}
		catch(Exception e) {
			
			System.out.println("get failure !");
			e.printStackTrace();
		}
		
		return invertedIndex;
	}
}

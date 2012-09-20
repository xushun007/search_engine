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

public class InvertedIndex {

	private int invertedId;
	private String word;
	private int ndocs;
	private String docListStr;
	private double idf;
	
	public int getInvertedId() {
		return invertedId;
	}
	public void setInvertedId(int invertedId) {
		this.invertedId = invertedId;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public int getNdocs() {
		return ndocs;
	}
	public void setNdocs(int ndocs) {
		this.ndocs = ndocs;
	}
	public String getDocListStr() {
		return docListStr;
	}
	public void setDocListStr(String docListStr) {
		this.docListStr = docListStr;
	}
	public double getIdf() {
		return idf;
	}
	public void setIdf(double idf) {
		this.idf = idf;
	}
	
	
}

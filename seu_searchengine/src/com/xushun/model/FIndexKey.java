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

public class FIndexKey {
	
	private int docId;
	private String word;
	
	public FIndexKey() {}
	
	public FIndexKey(int docId, String word) {
		this.docId= docId;
		this.word = word;
	}
	
	public boolean equals(FIndexKey other) {
		
		return (docId == other.docId) && word.equals(other.word);
	}
	
	public int hashCode() {
		return 137 * docId + word.hashCode();
	}
	
	public int getDocId() {
		return docId;
	}
	public void setDocId(int docId) {
		this.docId = docId;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	
	

}

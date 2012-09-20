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

public class ForwardIndex {

	private int forwardId;
	private int docId;
	private String word;
	private int nhits;
	private String hitlist;
	private double termfre;
	
	public int getForwardId() {
		return forwardId;
	}
	public void setForwardId(int forwardId) {
		this.forwardId = forwardId;
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
	public int getNhits() {
		return nhits;
	}
	public void setNhits(int nhits) {
		this.nhits = nhits;
	}
	public String getHitlist() {
		return hitlist;
	}
	public void setHitlist(String hitlist) {
		this.hitlist = hitlist;
	}
	public double getTermfre() {
		return termfre;
	}
	public void setTermfre(double termfre) {
		this.termfre = termfre;
	}
	
	
}

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

package com.xushun.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class WordUtil {

	//private static final String dictFile = "Dictionary\\wordlist.txt";
	
	private static final String STOP_WORD_FILE_PATH = "Dictionary\\stopwordlist.txt";
	private static final String WORD_FILE_PATH = "Dictionary\\wordlist.txt";
	
	private static HashSet<String> stopWordSet;
	private static HashSet<String> wordSet;
	
	public static HashSet<String> getStopWordSet() {
		return stopWordSet;
	}
	
	public static HashSet<String> getWordSet() {
		return wordSet;
	}
	
	static {
		stopWordSet = scanDict(STOP_WORD_FILE_PATH);
		wordSet = scanDict(WORD_FILE_PATH);
	}
	
	public WordUtil() {
//		stopWordSet = scanDict(STOP_WORD_FILE_PATH);
//		wordSet = scanDict(WORD_FILE_PATH);
	}
	
	public static HashSet<String> scanDict(String dictFile)
	{
		HashSet<String> dictionary = new HashSet<String>();
		try {
			FileReader fileReader = new FileReader(dictFile);
			BufferedReader bfReader = new BufferedReader(fileReader);
			
			String word;
			while((word = bfReader.readLine()) != null)
			{
				dictionary.add(word);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("the size of dictionary is: " + dictionary.size());
			
		return dictionary;
	}
}

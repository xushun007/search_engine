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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;
  

public class WordSegmentation {  
	
	 //  要分词的字符串  
    private static String text = "hello,中国,china,中国，古时通常泛指中原地区，与中华中夏 中土中州含义相同。古代华夏族、汉族建国于黄河流域一带，以为居天下之中，故称中国";  
    
    private static String longText = "中广网南京9月5日消息（记者杨守华 江苏台刘冬）长着五个手指的机械手外面套上肉色橡胶手套，截肢的人接上它可以自如地拿起鸡蛋，并且能感受到鸡蛋的力度。这是东南大学仪器科学与工程学院院长宋爱国团队正在研制的第三代假手，让假手有了感觉，目前技术已经成熟。　　这种假手里面的机械部分跟科幻电影里一样，手的各个关节可以活动，接在人的断臂上，受肌电信号控制，可以做大部分握、抓等动作，象写字、端纸杯等都能做到，尽管是 慢腾腾的。最近网上有一段视频很流行，一位外国人将全身的肌肉接上传感器，通过肌肉的运动演奏乐器。这与假手的原理是一样的。东大仪器科学与工程学院院长宋爱国研制的 第二代假手已经在丹阳实现了产业化，一个假手市场上卖一万多块钱。现在第三代假手的研制已经进入实验阶段，能将假手上接触的力度传到人体，假手不再是“麻木”的了：“ 第三代是什么呢，把这种感觉反馈给人，比方拿一把剪刀，如果剪刀上装上各种各样的传感器，它力的大小反馈到你神经里面，你就感觉这个剪刀是你身体的一部分，你碰它有痛 感和节奏感，就能做更加精密的动作。像这种能传导力度感的技术还可以用在智能手机上，比如玩手机游戏时能感觉到宠物的力度，另外盲人上网就有福了，光听声不算什么，还可以用手触碰。“以后的游戏，比 如如《愤怒的小鸟》，撞过来，你感到有力的。还有那个建筑，你推着它，把它推倒。刚刚立项。”";
    
    /** 
     * @param args 
     * @throws IOException  
     */  
    public static void main(String[] args) throws IOException {  
        Analyzer ikAnalyzer = new IKAnalyzer();  
        System.out.println("======中文=======IKAnalyzer======分词=======");   
//        showToken(ikAnalyzer, text);  
        Map<String, ArrayList<Integer> > wordMap = listWordOffset(ikAnalyzer, longText);
        printWordOffset(wordMap);
        
    }  
      
    
    public static Map<String, ArrayList<Integer> > segmentation(String text) throws Exception {
    	Analyzer ikAnalyzer = new IKAnalyzer();
    	
    	return listWordOffset(ikAnalyzer, text);
    }
    
    public static Map<String, ArrayList<Integer> > listWordOffset(Analyzer analyzer, String text) throws IOException {  
          
    	Map<String, ArrayList<Integer> > wordMap = new HashMap<String, ArrayList<Integer> >();
    	    	
        Reader reader = new StringReader(text);  
        TokenStream stream = (TokenStream)analyzer.tokenStream("", reader);  
        TermAttribute termAtt  = (TermAttribute)stream.addAttribute(TermAttribute.class);  
        OffsetAttribute offAtt  = (OffsetAttribute)stream.addAttribute(OffsetAttribute.class); 
        while(stream.incrementToken()){          	
        	String word = termAtt.term();
        	int offset = offAtt.startOffset();
        	if(!wordMap.containsKey(word)) {
        		ArrayList<Integer> offsetList = new ArrayList<Integer>();
        		offsetList.add(offset);
        		wordMap.put(word, offsetList);
        	}
        	else {
        		ArrayList<Integer> offsetList = wordMap.get(word);
        		offsetList.add(offset);
        	}     	
//            System.out.print(termAtt.term() + "|("+ offAtt.startOffset() + " " + offAtt.endOffset()+")");   
        }  
        
        return wordMap;
        
    }  
    
    
    public static List<String> getKeyWord(String text) throws IOException {  
        
    	Analyzer analyzer = new IKAnalyzer();
    	
    	List<String> keywords = new ArrayList<String>();    
    	
        Reader reader = new StringReader(text);  
        TokenStream stream = (TokenStream)analyzer.tokenStream("", reader);  
        TermAttribute termAtt  = (TermAttribute)stream.addAttribute(TermAttribute.class);  
        
        while(stream.incrementToken()){          	
        	String word = termAtt.term();
        	if(!keywords.contains(word)) {
        		keywords.add(word);
        	}  	
        }  
        
        return keywords;
    }  
    
    public static List<String> getKeyWord(String text, boolean flag) throws IOException {  
        
    	Analyzer analyzer = new IKAnalyzer(flag);
    	
    	List<String> keywords = new ArrayList<String>();    
    	
        Reader reader = new StringReader(text);  
        TokenStream stream = (TokenStream)analyzer.tokenStream("", reader);  
        TermAttribute termAtt  = (TermAttribute)stream.addAttribute(TermAttribute.class);  
        
        while(stream.incrementToken()){          	
        	String word = termAtt.term();
        	if(!keywords.contains(word)) {
        		keywords.add(word);
        	}  	
        }  
        
        return keywords;
    } 
    
    public static void printWordOffset(Map<String, ArrayList<Integer> > wordMap) {
    	for(Map.Entry<String, ArrayList<Integer>> entry: wordMap.entrySet()) {
    		String key = entry.getKey();
    		ArrayList<Integer> offsetList = entry.getValue();
    		
    		System.out.println(key + ": " + list2Str(offsetList));
    	}
    }
    
    public static String list2Str(List<Integer> offsetList) {
    	
    	StringBuffer buffer = new StringBuffer();
    	for(Integer offset: offsetList) {
    		buffer.append(offset.toString()).append(",");
    	}
    	
    	return buffer.substring(0, buffer.length()-1);
    }
  
}  
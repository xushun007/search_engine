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

package com.xushun.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.xushun.dao.IndexDAO;
import com.xushun.dao.PageDAO;
import com.xushun.model.PageInfo;
import com.xushun.util.WordUtil;

public class InvertedIndexService {
	
	private static HashSet<String> stopWordSet;
	private static HashSet<String> wordSet;
	private static int totalDoc = 0;
	
	public static Map<String, ArrayList<Integer>> globalInvertedIndex = new HashMap<String, ArrayList<Integer>>();
	
	static {
		stopWordSet = WordUtil.getStopWordSet();
		wordSet = WordUtil.getWordSet();
	}
	
	public static void createInvertedIndex(Map<Integer, ArrayList<String> > forwardIndex) {
		
		int cnt = 0;
		for(String word: wordSet) {
			
			System.out.println(++cnt);
//			System.out.println(word);
			for(Map.Entry<Integer, ArrayList<String> > entry: forwardIndex.entrySet()) {
				Integer docId = entry.getKey();
				ArrayList<String> words = entry.getValue();
				
//				System.out.println(words);
				if(words.contains(word)) {
					if(globalInvertedIndex.containsKey(word)) {
						ArrayList<Integer> docs = globalInvertedIndex.get(word);
						docs.add(docId);
					}
					else {
						ArrayList<Integer> docs = new ArrayList<Integer>();
						docs.add(docId);
						globalInvertedIndex.put(word, docs);
					}
				}
			}
			
		}

		System.out.println(globalInvertedIndex);
		totalDoc = forwardIndex.size();
		IndexDAO.addInvertedIndex(globalInvertedIndex, totalDoc);
	}

//	private static String longText1 = "中广网南京9月5日消息（记者杨守华 江苏台刘冬）长着五个手指的机械手外面套上肉色橡胶手套，截肢的人接上它可以自如地拿起鸡蛋，并且能感受到鸡蛋的力度。这是东南大学仪器科学与工程学院院长宋爱国团队正在研制的第三代假手，让假手有了感觉，目前技术已经成熟。　　这种假手里面的机械部分跟科幻电影里一样，手的各个关节可以活动，接在人的断臂上，受肌电信号控制，可以做大部分握、抓等动作，象写字、端纸杯等都能做到，尽管是 慢腾腾的。最近网上有一段视频很流行，一位外国人将全身的肌肉接上传感器，通过肌肉的运动演奏乐器。这与假手的原理是一样的。东大仪器科学与工程学院院长宋爱国研制的 第二代假手已经在丹阳实现了产业化，一个假手市场上卖一万多块钱。现在第三代假手的研制已经进入实验阶段，能将假手上接触的力度传到人体，假手不再是“麻木”的了：“ 第三代是什么呢，把这种感觉反馈给人，比方拿一把剪刀，如果剪刀上装上各种各样的传感器，它力的大小反馈到你神经里面，你就感觉这个剪刀是你身体的一部分，你碰它有痛 感和节奏感，就能做更加精密的动作。像这种能传导力度感的技术还可以用在智能手机上，比如玩手机游戏时能感觉到宠物的力度，另外盲人上网就有福了，光听声不算什么，还可以用手触碰。“以后的游戏，比 如如《愤怒的小鸟》，撞过来，你感到有力的。还有那个建筑，你推着它，把它推倒。刚刚立项。”";
//	private static String longText2 = "9月5日，教育部重点实验室评估组莅临我校，对微电子机械系统（MEMS）教育部重点实验室进行现场评估。常务副校长胡敏强、科技处处长李建清等参加评估会。        实验室主任黄庆安教授向评估组做工作报告，孙立涛教授、周再发研究员、杨决宽副教授、黄见秋老师分别做代表性成果汇报。评估组还参观了南高院实验室和纳皮米中心，并与教师、学生代表进行座谈。  评估组对实验室近年来长足的进步和取得的成绩给予充分肯定，并对实验室今后进一步的发展方向和发展模式进行了指导。 (谢骁)";
//	private static String longText3 = "教育部重点实验室评估组莅临我校，对微电子机械系统（MEMS）教育部重点实验室进行现场评估。";

	public static void main(String[] args) {
		 
		stopWordSet = WordUtil.getStopWordSet();
		wordSet = WordUtil.getWordSet();
		
		List<PageInfo> allPageList = PageDAO.getAllPage();
//		List<PageInfo> pageList = PageDAO.getPageListWithRange(0, 5);
		System.out.println("size : " + allPageList.size());
//		List<PageInfo> allPageList = PageDAO.getAllPage();
//		List<Integer> docIds = new ArrayList<Integer>();
//		docIds.add(51);
//		docIds.add(1);
//		docIds.add(251);
//		docIds.add(301);
//		docIds.add(201);
		
		for(PageInfo page: allPageList) {
			System.out.println(page.getDocID());
			ForwardIndexService.createForwardIndex(page.getDocID(),page.getPlain());
		}
		
		Map<Integer, ArrayList<String> > globalForwordIndex = ForwardIndexService.globalForwordIndex;
		
		System.out.println("size: " + globalForwordIndex.size());
//		for(Map.Entry<Integer, ArrayList<String>> entry: globalForwordIndex.entrySet() ) {
//			System.out.println(entry);
//		}
		
		InvertedIndexService.createInvertedIndex(globalForwordIndex);
		System.out.println("create invertedindex end");
//		for(Map.Entry<String, ArrayList<Integer>> entry: globalInvertedIndex.entrySet()) {
//			System.out.println(entry);
//		}
	}

}

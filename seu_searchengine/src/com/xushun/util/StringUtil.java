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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class StringUtil {

	public static String getHTMLFileName() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String currentTimeStr = sdf.format(new Date());
		Random random = new Random(System.currentTimeMillis());
		
		StringBuilder builder = new StringBuilder();
		builder.append(currentTimeStr).append(random.nextInt());
		
		//String imageName = currentTimeStr + random.nextInt(10000);
		
		return builder.toString();
	}
	
	public static String getTimeStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	public static List<Integer> strToList(String s) {
	
		List<Integer> list = new ArrayList<Integer>();
		String[] arr = s.split(",");

		for(int i =0; i < arr.length; ++i) {
			list.add(Integer.parseInt(arr[i]));
		}

		return list;
	}
}

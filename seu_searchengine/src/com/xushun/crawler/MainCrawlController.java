package com.xushun.crawler;

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


import java.util.Date;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class MainCrawlController {

        public static void main(String[] args) throws Exception {
                if (args.length != 2) {
                        System.out.println("Needed parameters: ");
                        System.out.println("\t rootFolder (it will contain intermediate crawl data)");
                        System.out.println("\t numberOfCralwers (number of concurrent threads)");
                        return;
                }

                /*
                 * crawlStorageFolder is a folder where intermediate crawl data is
                 * stored.
                 */
                String crawlStorageFolder = args[0];

                /*
                 * numberOfCrawlers shows the number of concurrent threads that should
                 * be initiated for crawling.
                 */
                int numberOfCrawlers = Integer.parseInt(args[1]);

                CrawlConfig config = new CrawlConfig();

                config.setCrawlStorageFolder(crawlStorageFolder);


                /*
                 * You can set the maximum crawl depth here. The default value is -1 for
                 * unlimited depth
                 */
                config.setMaxDepthOfCrawling(-1);

                /*
                 * You can set the maximum number of pages to crawl. The default value
                 * is -1 for unlimited number of pages
                 */
                config.setMaxPagesToFetch(-1);

                config.setResumableCrawling(false);

                /*
                 * Instantiate the controller for this crawl.
                 */
                PageFetcher pageFetcher = new PageFetcher(config);
                RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
                RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
                CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);


                controller.addSeed("http://news.seu.edu.cn/");
                
                String seuImportNewsList = "http://news.seu.edu.cn/s/146/t/1399/p/1/c/2100/i/";
                int seuImportNews = 157;
                for(int i = seuImportNews; i >=1; --i) {
                	String s = seuImportNewsList + i +"/list.htm";
                	controller.addSeed(seuImportNewsList + i +"/list.htm") ;
                	System.out.println(s);
                }
                
                String seuPreNewsList = "http://news.seu.edu.cn/s/146/t/1399/p/1/c/6767/i/";
                int seuPreNews = 15;
                for(int i = seuPreNews; i >=1; --i) {
                	controller.addSeed(seuPreNewsList + i +"/list.htm") ;
                }
                
                String seuMediaList = "http://news.seu.edu.cn/s/146/t/1399/p/1/c/2103/i/";
                int seuMedia = 100;
                for(int i = seuMedia; i >=1; --i) {
                	controller.addSeed(seuMediaList + i +"/list.htm") ;
                }
                
                String seuSchoolList = "http://news.seu.edu.cn/s/146/t/1399/p/1/c/2108/i/";
                int seuSchool = 10;
                for(int i = seuSchool; i >=1; --i) {
                	controller.addSeed(seuSchoolList + i +"/list.htm") ;
                }
                
                String seuPeopleList = "http://news.seu.edu.cn/s/146/t/1399/p/1/c/2145/i/";
                int seuPeople = 6;
                for(int i = seuPeople; i >=1; --i) {
                	controller.addSeed(seuPeopleList + i +"/list.htm") ;
                }
                
                String seuTeachList = "http://news.seu.edu.cn/s/146/t/1399/p/2/c/6553/i/";
                int seuTeach = 3;
                for(int i = seuTeach; i >=1; --i) {
                	controller.addSeed(seuTeachList + i +"/list.htm") ;
                }
                
                String seuHistoryList = "http://news.seu.edu.cn/s/146/t/1399/p/2/c/6551/i/";
                int seuHistory = 3;
                for(int i = seuHistory; i >=1; --i) {
                	controller.addSeed(seuHistoryList + i +"/list.htm") ;
                }
                
                String seuHiList = "http://news.seu.edu.cn/s/146/t/1399/p/2/c/6548/i/";
                int seuHi = 4;
                for(int i = seuHi; i >=1; --i) {
                	controller.addSeed(seuHiList + i +"/list.htm") ;
                }
                
                String seuTechList = "http://news.seu.edu.cn/s/146/t/1399/p/2/c/6552/i/";
                int seuTech = 4;
                for(int i = seuTech; i >=1; --i) {
                	controller.addSeed(seuTechList + i +"/list.htm") ;
                }
                
                System.out.println(new Date());
                
                /*
                 * Start the crawl. This is a blocking operation, meaning that your code
                 * will reach the line after this only when crawling is finished.
                 */
                controller.start(BasicCrawler.class, numberOfCrawlers);
                
                System.out.println(new Date());
        }
}
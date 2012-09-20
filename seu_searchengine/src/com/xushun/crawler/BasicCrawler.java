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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.regex.Pattern;

import com.xushun.dao.PageDAO;
import com.xushun.model.PageInfo;
import com.xushun.util.StringUtil;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import edu.uci.ics.crawler4j.util.IO;

public class BasicCrawler extends WebCrawler {
	
	private static PrintWriter timeWriter ;
	private final static String TIME_RECORD = "time.txt";
		private final static String DOWNLOAD_PATH = "D:\\xushun\\java_project\\crawler_data\\seu_news\\";

        private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
                        + "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

        private static long cntDownload = 0;
        
        static {
            try {
            	timeWriter = new PrintWriter(new File(DOWNLOAD_PATH, TIME_RECORD));
            }
            catch(Exception e) {
            	e.printStackTrace();
            	System.exit(1);
            }
        }

        
        /**
         * You should implement this function to specify whether the given url
         * should be crawled or not (based on your crawling logic).
         */
        @Override
        public boolean shouldVisit(WebURL url) {
                String href = url.getURL().toLowerCase();
                return !FILTERS.matcher(href).matches() && (href.indexOf("news.seu.edu.cn") != -1) &&(href.length() < 120);
        }

        /**
         * This function is called when a page is fetched and ready to be processed
         * by your program.
         */
        @Override
        public void visit(Page page) {
        	
	            if(cntDownload % 100 == 0) {
	           	 timeWriter.println(cntDownload + ": " + StringUtil.getTimeStr());
	           	 timeWriter.flush();
	           }
	            cntDownload++;
        	
                int docid = page.getWebURL().getDocid();
                String url = page.getWebURL().getURL();
                String domain = page.getWebURL().getDomain();
                String path = page.getWebURL().getPath();
                String subDomain = page.getWebURL().getSubDomain();
                String parentUrl = page.getWebURL().getParentUrl();

                PageInfo pi = new PageInfo();
                
                System.out.println("Docid: " + docid);
                System.out.println("URL: " + url);
                String contentEncoding = page.getContentEncoding();
                String contentType = page.getContentType();
                String charset = page.getContentCharset();
                byte[] cd = page.getContentData();
                
                System.out.println("charset: " + charset);
                
                String fileName = StringUtil.getHTMLFileName();
                IO.writeBytesToFile(cd, DOWNLOAD_PATH + "html\\"+ fileName + ".html");
                
                List<WebURL> links;
                if (page.getParseData() instanceof HtmlParseData) {
                        HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                        String text = htmlParseData.getText();
                        String html = htmlParseData.getHtml();
                        links = htmlParseData.getOutgoingUrls();
                        String title = htmlParseData.getTitle();
                   
                        String newText = null;
                        if(!charset.equalsIgnoreCase("utf-8") && !charset.equalsIgnoreCase("utf8")) {
                        	try{
                        		newText = new String(text.getBytes(charset), "UTF-8");
                        	}
                        	catch(Exception e) {
                        		e.printStackTrace();
                        	}
                        	
                        }
                        
                        System.out.println("Html length: " + html.length());
                        System.out.println("Number of outgoing links: " + links.size());
                        
                        pi.setDocID(docid);
                        pi.setUrl(url);
                        pi.setDomain(domain);
                        pi.setPath(path);
                        pi.setSubDomain(subDomain);
                        pi.setParentUrl(parentUrl);
                        pi.setContentType(contentType);
                        pi.setContentEncoding(contentEncoding);
                        pi.setCharset(charset);
                        pi.setSavedName(fileName);
                        pi.setPlain(text);
                        pi.setTitle(title);
                        
                        PageDAO.addPage(pi);
                        PageDAO.addLinks(docid, url, links);
                        

           
                                 					
            			File file = new File(DOWNLOAD_PATH + "text", fileName + ".txt");           //璁惧畾杈撳嚭鐨勬枃浠跺悕         			          			 
            			//outputStream.write(content);            			
            			BufferedWriter bfWriter = null;
            			try {
            				OutputStreamWriter outputStream = new OutputStreamWriter(new FileOutputStream(file),charset); 
            				//file.createNewFile();
            				bfWriter = new BufferedWriter(outputStream);
                			bfWriter.append(text);
                			bfWriter.flush();	

            			} catch (Exception e3) {
            				e3.printStackTrace();
            			}
  
                }

                System.out.println("=============\n");
        }
}

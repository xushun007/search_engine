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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;


public class DBConnection 
{
	private static final int CONNECTION_SIZE = 10;
    private static Connection conn = null;
    private static Connection connections[] = null;
    private Statement stmt = null;
    private PreparedStatement prepstmt = null;
    private static final String url = "jdbc:mysql://localhost/seu_news"; // URL指向要访问的数据库名search_engine
    private static final String user = "root"; // MySQL配置时的用户名
    private static final String password = "xushun"; // MySQL配置时的密码
    
    static {
//    	connections = new Connection[CONNECTION_SIZE];
    	
//    	for(int i = 0; i < CONNECTION_SIZE; ++i) {
    		
	    	try{
	            Class.forName("com.mysql.jdbc.Driver");
	            conn = DriverManager.getConnection(url, user, password);  
	    	}
	    	catch(Exception e) {
	    		System.out.println("connect da failure!");
	    		e.printStackTrace();
	    	}   	
//    	}
    }
    
    public static Connection getConnectionInstance() throws Exception {
    	
//    	int rand = (new Random()).nextInt() % CONNECTION_SIZE;
    	
    	return conn;
    	
    }
    
    public DBConnection()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.createStatement();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    

    
    public Connection getConnection() 
    {
        return conn;
    }
    
 
    public void close()     
    {
        try
        {
            if (stmt != null) 
            {
                stmt.close();
                stmt = null;
            }
            if (prepstmt != null) 
            {
                prepstmt.close();
                prepstmt = null;
            }
            conn.close();
            conn = null;
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
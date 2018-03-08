package com.xhj.imserver.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLDBHelper extends DBHelper {
	public static final String url = "jdbc:mysql://127.0.0.1:3306/imdb?characterEncoding=utf8&useSSL=true";  
    public static final String name = "com.mysql.jdbc.Driver";  
    public static final String user = "root";  
    public static final String password = "20103885";  
  
    public Connection conn = null;  
    public PreparedStatement pst = null;  
  
    public void executeSql(String sql) {  
        try {  
            Class.forName(name);//指定连接类型  
            conn = DriverManager.getConnection(url, user, password);//获取连接  
            if(sql.contains("--")) {
            	pst = null ;
            } else {
            	pst = conn.prepareStatement(sql);//准备执行语句	
            }
            
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
    public boolean execute() {
    	try {
			return pst.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return false ;
    }
    
    public ResultSet executeQuery() {
    	try {
			return pst.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null ;
    }
  
    public void close() {  
        try {  
            this.conn.close();  
            this.pst.close();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    } 
}

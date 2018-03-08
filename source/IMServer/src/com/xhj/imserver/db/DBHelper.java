package com.xhj.imserver.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DBHelper {
//	public static final String url = "";  
//    public static final String name = "";  
//    public static final String user = "";  
//    public static final String password = "";  
  
    public Connection conn = null;  
    public PreparedStatement pst = null;  
    
    public void executeSql(String sql) {
    	
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
    	
    }
}

package com.xhj.imserver.db;

public class DBManager {
	
	private static String dbtype = "mysql";
	
	public static void executeSql(String sql) {
		DBHelper dbHelper = null;
		if(dbtype.equals("mysql")){
			dbHelper = new MySQLDBHelper();	
		}
		System.out.println("sql is >>> "+sql);
		dbHelper.executeSql(sql);
		dbHelper.execute();
		dbHelper.close();
	}
	
	public static DBHelper executeForStatement(String sql) {
		DBHelper dbHelper = null;
		if(dbtype.equals("mysql")){
			dbHelper = new MySQLDBHelper();	
		}
		System.out.println("sql is >>> "+sql);
		dbHelper.executeSql(sql);
		return dbHelper ;
	}
	
}

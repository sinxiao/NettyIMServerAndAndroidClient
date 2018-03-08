package com.xhj.imserver.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.xhj.imserver.bean.User;
import com.xhj.imserver.db.DBHelper;
import com.xhj.imserver.db.DBManager;

public class UserDAO implements IDAO<User> {

	@Override
	public void save(User t) {
		String sql = "INSERT INTO user(`uname`,`nickname`,`pwd`) VALUES ( '" + t.getUname() + "','" + t.getNickname() + "','"+t.getPwd()+"');";
		DBManager.executeSql(sql);
	}

	@Override
	public void update(User t) {
		String sql = "UPDATE user SET `uname` = '" + t.getUname() + "' , `nickname` = '" + t.getNickname()
				+ "' 'pwd' = '"+t.getPwd()+"' where uid = " + t.getUid();
		DBManager.executeSql(sql);
	}

	@Override
	public void del(String id) {
		String sql = "delete from user where uid = " + id;
		DBManager.executeSql(sql);
	}

	@Override
	public void del(User t) {
		String sql = "delete from user where uid = " + t.getUid();
		DBManager.executeSql(sql);
	}

	public List<User> find(String where) {
		String sql = null;
		if (where == null || where.trim().length() == 0) {
			sql = "SELECT `uid`, `uname`, `nickname`, `pwd` FROM `user`;";
		} else {
			sql = "SELECT `uid`, `uname`, `nickname` , `pwd` FROM `user` where " + where + ";";
		}
		DBHelper dbHelper = DBManager.executeForStatement(sql);
		ResultSet resultSet = dbHelper.executeQuery();
		List<User> users = new ArrayList<User>();
		try {
			while (resultSet.next()) {
				int uid = resultSet.getInt("uid");
				String uname = resultSet.getString("uname");
				String nickname = resultSet.getString("nickname");
				String pwd = resultSet.getString("pwd");
				
				User user = new User();
				user.setNickname(nickname);
				user.setUid(uid + "");
				user.setUname(uname);
				user.setPwd(pwd);
				users.add(user);
			}
			;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.close();
		}
		return users;
	}

	@Override
	public User findById(String id) {
		String sql = "SELECT `uid`, `uname`, `nickname`,'pwd' FROM `user` where uid = " + id + ";";
		DBHelper dbHelper = DBManager.executeForStatement(sql);
		ResultSet resultSet = dbHelper.executeQuery();
		try {
			while (resultSet.next()) {
				int uid = resultSet.getInt("uid");
				String uname = resultSet.getString("uname");
				String nickname = resultSet.getString("nickname");
				String pwd = resultSet.getString("pwd");
				
				User user = new User();
				user.setNickname(nickname);
				user.setUid(uid + "");
				user.setPwd(pwd);
				user.setUname(uname);
				// .add(user);
				return user;
			}
			;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.close();
		}
		return null;
	}

}

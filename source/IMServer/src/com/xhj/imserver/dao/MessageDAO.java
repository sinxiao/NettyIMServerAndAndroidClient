package com.xhj.imserver.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xhj.imserver.bean.IMMessage;
import com.xhj.imserver.db.DBHelper;
import com.xhj.imserver.db.DBManager;

public class MessageDAO implements IDAO<IMMessage> {

	@Override
	public void save(IMMessage t) {
		String sql = "INSERT INTO immessage(`from`,`to`,`body`,`type`,`sendTime`,`readTiem`) VALUES ( '" + t.getFrom()
				+ "', '" + t.getTo() + "', '" + t.getBody() + "', '" + t.getType() + "', '" + t.getSendTime() + "', '"
				+ t.getReadTiem() + "');";
		DBManager.executeSql(sql);
	}

	@Override
	public void update(IMMessage t) {
		String sql = "UPDATE immessage SET from = '" + t.getFrom() + "',to = '" + t.getTo() + "',body = '"
				+ t.getBody() + "',type = '" + t.getType() + "',sendTime = '" + t.getSendTime() + "',readTiem = '"
				+ t.getReadTiem() + "' WHERE msgId = " + t.getMsgId() + ";";
		DBManager.executeSql(sql);
	}

	@Override
	public void del(String id) {
		String sql = "delete from immessage where `msgId` = " + id;
		DBManager.executeSql(sql);
	}

	@Override
	public void del(IMMessage t) {
		String sql = "delete from immessage where `msgId` = " + t.getMsgId();
		DBManager.executeSql(sql);
	}

	@Override
	public List<IMMessage> find(String where) {
		String sql = null;
		if (where == null || where.trim().length() == 0) {
			sql = "SELECT `msgId`, `from`, `to`,`body`,`type`,`sendTime`,`readTiem` FROM `immessage`;";
		} else {
			sql = "SELECT `msgId`, `from`, `to`,`body`,`type`,`sendTime`,`readTiem` FROM `immessage` where " + where
					+ ";";
		}
		DBHelper dbHelper = DBManager.executeForStatement(sql);
		ResultSet resultSet = dbHelper.executeQuery();
		List<IMMessage> msgs = new ArrayList<IMMessage>();
		try {
			while (resultSet.next()) {
				int msgId = resultSet.getInt("msgId");
				String from = resultSet.getString("from");
				String to = resultSet.getString("to");
				String body = resultSet.getString("body");
				String type = resultSet.getString("type");
				String sendTime = resultSet.getString("sendTime");
				String readTiem = resultSet.getString("readTiem");
				IMMessage msg = new IMMessage();
				msg.setMsgId("" + msgId);
				msg.setBody(body);
				msg.setFrom(from);
				msg.setTo(to);
				msg.setReadTiem(Long.parseLong(readTiem));
				msg.setSendTime(Long.parseLong(sendTime));
				msg.setType(type);
				msgs.add(msg);
			}
			;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.close();
		}
		return msgs;
	}

	@Override
	public IMMessage findById(String id) {
		String sql = "SELECT `msgId`, `from`, `to`,`body`,`type`,`sendTime`,`readTiem` FROM `immessage` where msgId = "
				+ id + ";";
		DBHelper dbHelper = DBManager.executeForStatement(sql);
		ResultSet resultSet = dbHelper.executeQuery();
		try {
			while (resultSet.next()) {
				int msgId = resultSet.getInt("msgId");
				String from = resultSet.getString("from");
				String to = resultSet.getString("to");
				String body = resultSet.getString("body");
				String type = resultSet.getString("type");
				String sendTime = resultSet.getString("sendTime");
				String readTiem = resultSet.getString("readTiem");
				IMMessage msg = new IMMessage();
				msg.setMsgId("" + msgId);
				msg.setBody(body);
				msg.setFrom(from);
				msg.setTo(to);
				msg.setReadTiem(Long.parseLong(readTiem));
				msg.setSendTime(Long.parseLong(sendTime));
				msg.setType(type);
				// msgs.add(msg);
				return msg;
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

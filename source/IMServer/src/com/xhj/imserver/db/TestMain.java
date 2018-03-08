package com.xhj.imserver.db;

import java.util.List;

import com.xhj.imserver.bean.IMMessage;
import com.xhj.imserver.dao.MessageDAO;

public class TestMain {

	public static void main(String[] args) {
		IMMessage msg = new IMMessage();
		msg.setBody("ccc");
		msg.setFrom("aaa");
		msg.setTo("bbb");
//		msg.setMsgId("1");
		msg.setSendTime(System.currentTimeMillis());
		msg.setType("1");
		MessageDAO msgDAO = new MessageDAO();
		msgDAO.save(msg);
		List<IMMessage> msgs = msgDAO.find(null);
		System.out.println("msgs size >> "+msgs.size());
	}

}

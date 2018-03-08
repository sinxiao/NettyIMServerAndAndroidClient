package com.xhj.imserver.biz.protocol;

import com.alibaba.fastjson.JSON;
import com.xhj.imserver.bean.IMMessage;
import com.xhj.imserver.bean.User;
import com.xhj.imserver.utils.Utils;

public class IMProtocol {
	
	public static final int LOGIN = 0;
	public static final int SEND = 1;
	public static final int REV = 2 ;
	public static final int QUIT = 3;
	public static final int READIT = 4;
	public static final int REG = 5;
	public static final int LOGINNOW = 6;
	public static final int LOGINOUT = 7;

	public static String getLoginJson(User user) {
		Request request = new Request();
		request.setMethod(LOGIN);
		request.setSendTime(System.currentTimeMillis());
		request.setFrom(user.getUname());
		user.setPwd(Utils.getMD5(user.getPwd()));
		String userjson = JSON.toJSONString(user);
		request.setBody(userjson);
		String json = JSON.toJSONString(request);
		return json ;
	}
	
	public static String getSendJson(IMMessage msg) {
		Request request = new Request();
		request.setSendTime(System.currentTimeMillis());
		request.setFrom(msg.getFrom());
		//request.setTo(msg.getTo());
		request.setBody(JSON.toJSONString(msg));
		request.setMethod(SEND);
		String json = JSON.toJSONString(request);
		return json;
	}
	
	public static String getRevJson(IMMessage msg) {
		Request request = new Request();
		request.setBody(JSON.toJSONString(msg));
		request.setFrom(msg.getFrom());
		//request.setTo(msg.getTo());
		request.setMethod(REV);
		
		String json = JSON.toJSONString(request);
		return json;
	}
	
	public static String getQuitJson(String uname) {
		Request request = new Request();
		//request.setBody(JSON.toJSONString(msg));
		request.setFrom(uname);
		//request.setTo(msg.getTo());
		request.setMethod(QUIT);
		
		String json = JSON.toJSONString(request);
		return json;
	}

	public static String getUserRegJson(User user) {
		Request request = new Request();
		request.setMethod(REG);
		request.setSendTime(System.currentTimeMillis());
		request.setFrom(user.getUname());
		user.setPwd(Utils.getMD5(user.getPwd()));
		String userjson = JSON.toJSONString(user);
		request.setBody(userjson);
		String json = JSON.toJSONString(request);
		return json ;
	}
	
}

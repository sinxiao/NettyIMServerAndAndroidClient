package com.xhj.imserver.biz;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.xhj.imserver.bean.IMMessage;
import com.xhj.imserver.biz.protocol.IMProtocol;
import com.xhj.imserver.biz.protocol.Response;
import com.xhj.imserver.dao.MessageDAO;
import com.xhj.imserver.imserver.ChannelGroups;

import io.netty.channel.Channel;

public class IMMessageBiz {
	
	private MessageDAO messagedao = new MessageDAO();
	
	public void saveMsg(IMMessage msg) {
		messagedao.save(msg);
	}
	
	public List<IMMessage> getUnReadMsg(String uname) {
		List<IMMessage> msgs = messagedao.find(" `to` = '"+uname+"' and readTiem = -1 ");
		return msgs ;
	}

	public void makeRead(String[] ids) {
		for (String id : ids) {
			makeRead(id);
		}
	}
	
	public void makeRead(String mid) {
		IMMessage msg = messagedao.findById(mid);
		if(msg != null) {
			msg.setReadTiem(System.currentTimeMillis());
			messagedao.update(msg);
		}
	}
	
	public void transformMessage(IMMessage message) {
		
		Channel channel = ChannelGroups.getChannel(ChannelGroups.getChannelId(message.getTo()));
		if(channel != null && channel.isActive()) {
			Response response = new Response();
			response.setBody(JSON.toJSONString(message));
			response.setStatus(0);
			response.setMethod(IMProtocol.REV);
			response.setSendTime(System.currentTimeMillis());
			channel.writeAndFlush(JSON.toJSON(response)+"\r\n");
		}
		
	}
	
}

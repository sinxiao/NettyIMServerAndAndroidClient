package com.xhj.imserver.bean;

import java.io.Serializable;

public class IMMessage implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3625626018036155106L;
	private String msgId;
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getSendTime() {
		return sendTime;
	}
	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}
	public long getReadTiem() {
		return readTiem;
	}
	public void setReadTiem(long readTiem) {
		this.readTiem = readTiem;
	}
	private String from ;
	private String to ;
	private String body;
	private String type ;
	private long sendTime=-1;
	private long readTiem=-1;
	
}

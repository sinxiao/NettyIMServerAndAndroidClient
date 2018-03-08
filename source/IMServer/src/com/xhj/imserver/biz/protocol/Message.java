package com.xhj.imserver.biz.protocol;

public abstract class Message {
	//合法性
	private String key;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	private long sendTime ;
	public long getSendTime() {
		return sendTime;
	}
	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
//	public String getTo() {
//		return to;
//	}
//	public void setTo(String to) {
//		this.to = to;
//	}
	public int getMethod() {
		return method;
	}
	public void setMethod(int method) {
		this.method = method;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	private String from ;
//	private String to ;
	private int method ;
	private String body ;
	private byte[] data ;
}

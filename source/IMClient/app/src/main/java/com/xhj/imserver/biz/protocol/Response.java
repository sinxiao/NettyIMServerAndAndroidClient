package com.xhj.imserver.biz.protocol;

public class Response extends Message {
	
	private int errorCode ;
	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorStr() {
		return errorStr;
	}

	public void setErrorStr(String errorStr) {
		this.errorStr = errorStr;
	}

	private String errorStr ;
	
	private int status;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}

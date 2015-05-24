package com.hdos.bean;

import java.util.List;

public class Quegroup {
	private String respCode;
	private List data;
	private String currentAnswerIndex;
	private String respMsg;
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public List getData() {
		return data;
	}
	public void setData(List data) {
		this.data = data;
	}
	public String getCurrentAnswerIndex() {
		return currentAnswerIndex;
	}
	public void setCurrentAnswerIndex(String currentAnswerIndex) {
		this.currentAnswerIndex = currentAnswerIndex;
	}
	public String getRespMsg() {
		return respMsg;
	}
	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}
	
	
}

package com.sige.rest.request;

import java.io.Serializable;


public class ErrorReq implements Serializable{

	private String code;
	private String[] errors[];
	private String message;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String[][] getErrors() {
		return errors;
	}
	public void setErrors(String[][] errors) {
		this.errors = errors;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}


}

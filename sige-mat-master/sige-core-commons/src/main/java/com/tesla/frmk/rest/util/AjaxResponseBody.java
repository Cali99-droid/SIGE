package com.tesla.frmk.rest.util;


 import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import com.fasterxml.jackson.annotation.JsonView;
import com.sige.common.enums.EnumTipoException;
import com.tesla.frmk.common.exceptions.ControllerException;
import com.tesla.frmk.common.exceptions.DAOException;
import com.tesla.frmk.common.exceptions.ServiceException;


public class AjaxResponseBody {
	 

	@JsonView(Views.Public.class)
	String msg;
	@JsonView(Views.Public.class)
	String code;
	//@JsonView(Views.Public.class)
	Object result;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public void setException(Exception e){ 
		
		this.setCode("500");
		if ( e instanceof ControllerException){
			this.setMsg(e.getMessage());
			
			if(((ControllerException)e).getTipoException().equals(EnumTipoException.WARNING)){
				this.setCode("399");//ES WARNING
			}
			
		}else if ( e instanceof ServiceException){
				this.setMsg(e.getMessage());
				
				if(((ServiceException)e).getTipoException().equals(EnumTipoException.WARNING)){
					this.setCode("399");//ES WARNING
				}
				
		}else if( e instanceof DAOException){
			this.setMsg(e.getMessage());
		}else if (e instanceof DuplicateKeyException) {
			String errorSQL = e.getCause().getMessage();
			if (errorSQL.indexOf("Duplicate entry")>-1){
				errorSQL = errorSQL.replaceAll("Duplicate entry", "Campo");
				errorSQL = errorSQL.replaceAll("for key", "ya está registrado en el sistema.");
				if(errorSQL.indexOf("-")>-1)
					errorSQL = "Ya está registrado en el sistema.";
				
				errorSQL = errorSQL.substring(0,errorSQL.indexOf("sistema") + 8);
				this.setMsg(errorSQL);
			}
			else
				this.setMsg("Registro duplicado");
		}else if (e instanceof DataIntegrityViolationException) {
			String errorSQL = e.getCause().getMessage();
			if (errorSQL.indexOf("cannot be null")>-1){
				errorSQL = errorSQL.replaceAll("Column", "Columna");
				errorSQL = errorSQL.replaceAll("cannot be null", "es un dato obligatorio");
				this.setMsg(errorSQL);
			}
			else
				this.setMsg(errorSQL);
		}else{
			e.printStackTrace();
			this.setMsg("Error desconocido, consulte con el administrador del sistema");
		}
	}
	
	@Override
	public String toString() {
		return "AjaxResponseResult [msg=" + msg + ", code=" + code + ", result=" + result + "]";
	}

}

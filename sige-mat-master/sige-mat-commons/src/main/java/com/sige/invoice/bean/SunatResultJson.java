package com.sige.invoice.bean;

public class SunatResultJson {
	@Override
	public String toString() {
		return "SunatResultJson [respuesta_sunat=" + respuesta_sunat + ", archivo=" + archivo + ", id_eiv=" + id_eiv
				+ ", code=" + code + ", ticket=" + ticket + ", res_id=" + res_id + "]";
	}
	String respuesta_sunat;
	String archivo;
	String id_eiv;
	String code;
	String ticket;
	String res_id;
	
	public String getRespuesta_sunat() {
		return respuesta_sunat;
	}
	public void setRespuesta_sunat(String respuesta_sunat) {
		this.respuesta_sunat = respuesta_sunat;
	}
	public String getArchivo() {
		return archivo;
	}
	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}
	public String getId_eiv() {
		return id_eiv;
	}
	public void setId_eiv(String id_eiv) {
		this.id_eiv = id_eiv;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getRes_id() {
		return res_id;
	}
	public void setRes_id(String res_id) {
		this.res_id = res_id;
	}

	
	
}

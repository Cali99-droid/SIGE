package com.sige.rest.request;

import java.io.Serializable;
import java.util.Date; 

public class NotaDesempenioReq implements Serializable{
 
	private static final long serialVersionUID = -4470656952360885399L;
	private Integer id_not_des;
	private Integer id_desau;
	private Integer id_tra;
	private Integer id_alu;
	private Date fec;
	private Integer nota;
	public Integer getId_desau() {
		return id_desau;
	}
	public void setId_desau(Integer id_desau) {
		this.id_desau = id_desau;
	}
	public Integer getId_tra() {
		return id_tra;
	}
	public void setId_tra(Integer id_tra) {
		this.id_tra = id_tra;
	}
	public Integer getId_alu() {
		return id_alu;
	}
	public void setId_alu(Integer id_alu) {
		this.id_alu = id_alu;
	}
	public Date getFec() {
		return fec;
	}
	public void setFec(Date fec) {
		this.fec = fec;
	}
	public Integer getNota() {
		return nota;
	}
	public void setNota(Integer nota) {
		this.nota = nota;
	}
	public Integer getId_not_des() {
		return id_not_des;
	}
	public void setId_not_des(Integer id_not_des) {
		this.id_not_des = id_not_des;
	}
	
}

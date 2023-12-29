package com.sige.rest.request;

import java.io.Serializable;

public class PagoMatriculaReq implements Serializable{
	
	private static final long serialVersionUID = -7090031095757893226L;
	
	private Integer id;
	private Integer id_mat;
	private Integer id_suc;
	private String tip;
	private java.math.BigDecimal monto;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getId_mat() {
		return id_mat;
	}
	public void setId_mat(Integer id_mat) {
		this.id_mat = id_mat;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public java.math.BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(java.math.BigDecimal monto) {
		this.monto = monto;
	}
	public Integer getId_suc() {
		return id_suc;
	}
	public void setId_suc(Integer id_suc) {
		this.id_suc = id_suc;
	}
	
}

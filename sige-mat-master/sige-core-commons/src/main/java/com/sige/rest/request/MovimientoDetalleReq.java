package com.sige.rest.request;

import java.io.Serializable;
import java.math.BigDecimal;

public class MovimientoDetalleReq implements Serializable{

	private Integer id;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	private Integer id_fco;
	private String obs;
	private BigDecimal monto;
	private String concepto;
	
	
	public Integer getId_fco() {
		return id_fco;
	}
	public void setId_fco(Integer id_fco) {
		this.id_fco = id_fco;
	}
	public String getObs() {
		return obs;
	}
	public void setObs(String obs) {
		this.obs = obs;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	
	
}

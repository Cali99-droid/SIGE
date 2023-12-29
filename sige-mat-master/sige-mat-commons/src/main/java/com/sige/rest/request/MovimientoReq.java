package com.sige.rest.request;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MovimientoReq implements Serializable{

	private Integer id_suc;
	private Integer id_mat;
	private String tipo;
	private String fec;
	private String obs;
	
	private MovimientoDetalleReq[] detalle;
	
	public Integer getId_suc() {
		return id_suc;
	}
	public void setId_suc(Integer id_suc) {
		this.id_suc = id_suc;
	}
	public Integer getId_mat() {
		return id_mat;
	}
	public void setId_mat(Integer id_mat) {
		this.id_mat = id_mat;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public MovimientoDetalleReq[] getDetalle() {
		return detalle;
	}
	public void setDetalle(MovimientoDetalleReq[] detalle) {
		this.detalle = detalle;
	}
	public String getFec() {
		return fec;
	}
	public void setFec(String fec) {
		this.fec = fec;
	}
	public String getObs() {
		return obs;
	}
	public void setObs(String obs) {
		this.obs = obs;
	}

	
}

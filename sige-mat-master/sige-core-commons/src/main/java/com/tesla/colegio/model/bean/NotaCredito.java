package com.tesla.colegio.model.bean;

import java.math.BigDecimal;

public class NotaCredito {
	
	int id;
	int id_fmo;
	BigDecimal monto;
	String motivo;
	
	public int getId_fmo() {
		return id_fmo;
	}
	public void setId_fmo(int id_fmo) {
		this.id_fmo = id_fmo;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
}

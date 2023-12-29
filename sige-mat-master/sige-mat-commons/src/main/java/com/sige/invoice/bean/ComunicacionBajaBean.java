package com.sige.invoice.bean;

import java.util.Arrays;

public class ComunicacionBajaBean {
	String fechaCreacion;
	String fechaEnvio;
	String razonSocial;
	String ruc;

	ComunicacionBajaItemBean[] items;
	
	
	public String getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public String getFechaEnvio() {
		return fechaEnvio;
	}
	public void setFechaEnvio(String fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getRuc() {
		return ruc;
	}
	public void setRuc(String ruc) {
		this.ruc = ruc;
	}
	 
	public ComunicacionBajaItemBean[] getItems() {
		return items;
	}
	public void setItems(ComunicacionBajaItemBean[] items) {
		this.items = items;
	}
	@Override
	public String toString() {
		return "ComunicacionBajaBean [fechaCreacion=" + fechaCreacion + ", fechaEnvio=" + fechaEnvio + ", razonSocial="
				+ razonSocial + ", ruc=" + ruc + ", items=" + Arrays.toString(items) + "]";
	}
		
}
package com.sige.invoice.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class ImpresionDcto implements Serializable{
 
	private static final long serialVersionUID = -2361482791715551299L;
	private String descripcion;
	private BigDecimal descuento;
	
	public ImpresionDcto(String descripcion, BigDecimal descuento){
		this.descripcion = descripcion;
		this.descuento = descuento;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public BigDecimal getDescuento() {
		return descuento;
	}
	public void setDescuento(BigDecimal descuento) {
		this.descuento = descuento;
	}
	
	
	
}

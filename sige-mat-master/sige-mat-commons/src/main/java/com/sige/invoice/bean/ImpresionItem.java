package com.sige.invoice.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ImpresionItem {
	int nro;
	String descripcion;
	int cantidad;
	BigDecimal precioUnit;
	BigDecimal descuento;
	BigDecimal precio;
	String tipoOperacion;
	
	private List<ImpresionDcto> descuentos = new ArrayList<ImpresionDcto>();
	
	public int getNro() {
		return nro;
	}
	public void setNro(int nro) {
		this.nro = nro;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public BigDecimal getPrecioUnit() {
		return precioUnit;
	}
	public void setPrecioUnit(BigDecimal precioUnit) {
		this.precioUnit = precioUnit;
	}
	public BigDecimal getDescuento() {
		return descuento;
	}
	public void setDescuento(BigDecimal descuento) {
		this.descuento = descuento;
	}
	public BigDecimal getPrecio() {
		return precio;
	}
	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}
	public String getTipoOperacion() {
		return tipoOperacion;
	}
	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}
	public List<ImpresionDcto> getDescuentos() {
		return descuentos;
	}
	public void setDescuentos(List<ImpresionDcto> descuentos) {
		this.descuentos = descuentos;
	}

	
	@Override
	public String toString() {
		return "{\"nro\":" + nro + ", \"descripcion\":\"" + descripcion + "\", \"cantidad\":" + cantidad + ", \"precioUnit\":"
				+ precioUnit + ", \"descuento\":" + descuento + ", \"precio\":" + precio + ", \"tipoOperacion\":\"" + tipoOperacion
				+ "\", \"descuentos\":[]}";
	}

	
}

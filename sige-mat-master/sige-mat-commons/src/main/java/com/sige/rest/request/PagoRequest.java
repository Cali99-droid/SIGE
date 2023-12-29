package com.sige.rest.request;

import java.io.Serializable;

public class PagoRequest implements Serializable{
	
	private static final long serialVersionUID = -7090031095757893226L;
	
	private String id;
	private String monto;
	private String descuento;
	private String descuento_secretaria;
	private String descuento_personalizado;
	private String descuento_pago_ade;
	private String descuento_beca;
	private String tipo_pago;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}



	public String getMonto() {
		return monto;
	}
	public void setMonto(String monto) {
		this.monto = monto;
	}
	public String getDescuento() {
		return descuento;
	}
	public void setDescuento(String descuento) {
		this.descuento = descuento;
	}
	public String getDescuento_secretaria() {
		return descuento_secretaria;
	}
	public void setDescuento_secretaria(String descuento_secretaria) {
		this.descuento_secretaria = descuento_secretaria;
	}
	public String getDescuento_personalizado() {
		return descuento_personalizado;
	}
	public void setDescuento_personalizado(String descuento_personalizado) {
		this.descuento_personalizado = descuento_personalizado;
	}
	public String getDescuento_pago_ade() {
		return descuento_pago_ade;
	}
	public void setDescuento_pago_ade(String descuento_pago_ade) {
		this.descuento_pago_ade = descuento_pago_ade;
	}
	public String getTipo_pago() {
		return tipo_pago;
	}
	public void setTipo_pago(String tipo_pago) {
		this.tipo_pago = tipo_pago;
	}
	public String getDescuento_beca() {
		return descuento_beca;
	}
	public void setDescuento_beca(String descuento_beca) {
		this.descuento_beca = descuento_beca;
	}	
	
}

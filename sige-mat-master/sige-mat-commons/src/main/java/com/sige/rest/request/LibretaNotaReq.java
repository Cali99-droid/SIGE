package com.sige.rest.request;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tesla.colegio.model.CompetenciaDc; 

public class LibretaNotaReq implements Serializable{
 
	private static final long serialVersionUID = -4470656952360885399L;
	private CabeceraNotaReq cabecera;
	private DetalleNotaReq detalle;
	public CabeceraNotaReq getCabecera() {
		return cabecera;
	}
	public void setCabecera(CabeceraNotaReq cabecera) {
		this.cabecera = cabecera;
	}
	public DetalleNotaReq getDetalle() {
		return detalle;
	}
	public void setDetalle(DetalleNotaReq detalle) {
		this.detalle = detalle;
	}
		
}

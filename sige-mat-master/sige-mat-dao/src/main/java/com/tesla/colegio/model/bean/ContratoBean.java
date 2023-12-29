package com.tesla.colegio.model.bean;

import java.util.ArrayList;
import java.util.List;

public class ContratoBean {

	private String numero;
	private String nombre;
	private String nro_doc;
	private String domicilio;
	private String distrito;
	private String provincia;

	private String poder_nombre;
	private String poder_nro_doc;

	private List<ContratoHijoBean> hijos = new ArrayList<ContratoHijoBean>();

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNro_doc() {
		return nro_doc;
	}

	public void setNro_doc(String nro_doc) {
		this.nro_doc = nro_doc;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getDistrito() {
		return distrito;
	}

	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getPoder_nombre() {
		return poder_nombre;
	}

	public void setPoder_nombre(String poder_nombre) {
		this.poder_nombre = poder_nombre;
	}

	public String getPoder_nro_doc() {
		return poder_nro_doc;
	}

	public void setPoder_nro_doc(String poder_nro_doc) {
		this.poder_nro_doc = poder_nro_doc;
	}

	public List<ContratoHijoBean> getHijos() {
		return hijos;
	}

	public void setHijos(List<ContratoHijoBean> hijos) {
		this.hijos = hijos;
	}
	
	
}

package com.tesla.colegio.model.bean;

public class CondicionBean {

	private Integer id_ctc; //tipo de condicion
	private String obs;
	private String tipo; //bloqueo, condicionada. pìerde vacante
 
	public String getObs() {
		return obs;
	}
	public void setObs(String obs) {
		this.obs = obs;
	}
	public Integer getId_ctc() {
		return id_ctc;
	}
	public void setId_ctc(Integer id_ctc) {
		this.id_ctc = id_ctc;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	 
	
	
}

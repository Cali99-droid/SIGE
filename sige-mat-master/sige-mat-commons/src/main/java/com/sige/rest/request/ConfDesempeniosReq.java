package com.sige.rest.request;

import java.io.Serializable;
import java.util.Date;
import java.util.List; 

public class ConfDesempeniosReq implements Serializable{
 
	private static final long serialVersionUID = -4470656952360885399L;
	private Integer id_aula;
	private Integer id_areadc;
	private Integer id_curdc;
	private Integer id_periodo;
	private String id_des[];
	public Integer getId_aula() {
		return id_aula;
	}
	public void setId_aula(Integer id_aula) {
		this.id_aula = id_aula;
	}
	public Integer getId_areadc() {
		return id_areadc;
	}
	public void setId_areadc(Integer id_areadc) {
		this.id_areadc = id_areadc;
	}
	public Integer getId_curdc() {
		return id_curdc;
	}
	public void setId_curdc(Integer id_curdc) {
		this.id_curdc = id_curdc;
	}
	public Integer getId_periodo() {
		return id_periodo;
	}
	public void setId_periodo(Integer id_periodo) {
		this.id_periodo = id_periodo;
	}
	public String[] getId_des() {
		return id_des;
	}
	public void setId_des(String[] id_des) {
		this.id_des = id_des;
	}
	
	
}

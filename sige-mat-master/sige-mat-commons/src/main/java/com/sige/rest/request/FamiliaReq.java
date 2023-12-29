package com.sige.rest.request;

import java.io.Serializable;

import org.springframework.format.annotation.DateTimeFormat;


public class FamiliaReq implements Serializable{

	private static final long serialVersionUID = -1182003383085892015L;
	private Integer id;
	private Integer id_dep;
	private Integer id_pro;
	private Integer id_dist;
	private String direccion;
	private String referencia;
	private Integer id_seg;
	private String cod_aseg;
	private Integer id_csal;
	private String dir_csalud;
	private String est;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getId_dep() {
		return id_dep;
	}
	public void setId_dep(Integer id_dep) {
		this.id_dep = id_dep;
	}
	public Integer getId_pro() {
		return id_pro;
	}
	public void setId_pro(Integer id_pro) {
		this.id_pro = id_pro;
	}
	public Integer getId_dist() {
		return id_dist;
	}
	public void setId_dist(Integer id_dist) {
		this.id_dist = id_dist;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public Integer getId_seg() {
		return id_seg;
	}
	public void setId_seg(Integer id_seg) {
		this.id_seg = id_seg;
	}
	public String getCod_aseg() {
		return cod_aseg;
	}
	public void setCod_aseg(String cod_aseg) {
		this.cod_aseg = cod_aseg;
	}
	public Integer getId_csal() {
		return id_csal;
	}
	public void setId_csal(Integer id_csal) {
		this.id_csal = id_csal;
	}
	public String getDir_csalud() {
		return dir_csalud;
	}
	public void setDir_csalud(String dir_csalud) {
		this.dir_csalud = dir_csalud;
	}
	public String getEst() {
		return est;
	}
	public void setEst(String est) {
		this.est = est;
	}
	
}

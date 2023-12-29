package com.tesla.colegio.model.bean;

import com.tesla.colegio.model.Nota;
import com.tesla.frmk.model.EntidadBase;

import java.math.BigDecimal;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_grad
 * @author MV
 *
 */
public class PreguntaAlternativaBean{

	private String alt;
	private Integer id_enc_pre;	
	private Integer id_enc_alt;	
	private Integer id_alu ;
	private Integer id_mat ;
	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	public Integer getId_enc_pre() {
		return id_enc_pre;
	}
	public void setId_enc_pre(Integer id_enc_pre) {
		this.id_enc_pre = id_enc_pre;
	}
	public Integer getId_enc_alt() {
		return id_enc_alt;
	}
	public void setId_enc_alt(Integer id_enc_alt) {
		this.id_enc_alt = id_enc_alt;
	}
	public Integer getId_alu() {
		return id_alu;
	}
	public void setId_alu(Integer id_alu) {
		this.id_alu = id_alu;
	}
	public Integer getId_mat() {
		return id_mat;
	}
	public void setId_mat(Integer id_mat) {
		this.id_mat = id_mat;
	}
	@Override
	public String toString() {
		return "PreguntaAlternativaBean [alt=" + alt + ", id_enc_pre=" + id_enc_pre + ", id_enc_alt=" + id_enc_alt
				+ ", id_alu=" + id_alu + ", id_mat=" + id_mat + "]";
	}

	
}
package com.sige.rest.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date; 

public class NotaPromCompetenciaReq implements Serializable{
 
	private static final long serialVersionUID = -4470656952360885399L;
	private Integer id_not_com;
	private Integer id_com;
	private Integer id_au;
	private Integer id_tra;
	private Integer id_alu;
	private Integer id_cpu;
	private Integer id_cua;
	private Integer id;
	private Date fec;
	private BigDecimal prom;
	public Integer getId_not_com() {
		return id_not_com;
	}
	public void setId_not_com(Integer id_not_com) {
		this.id_not_com = id_not_com;
	}
	public Integer getId_com() {
		return id_com;
	}
	public void setId_com(Integer id_com) {
		this.id_com = id_com;
	}
	public Integer getId_tra() {
		return id_tra;
	}
	public void setId_tra(Integer id_tra) {
		this.id_tra = id_tra;
	}
	public Integer getId_alu() {
		return id_alu;
	}
	public void setId_alu(Integer id_alu) {
		this.id_alu = id_alu;
	}
	public Integer getId_cpu() {
		return id_cpu;
	}
	public void setId_cpu(Integer id_cpu) {
		this.id_cpu = id_cpu;
	}
	public Date getFec() {
		return fec;
	}
	public void setFec(Date fec) {
		this.fec = fec;
	}
	public BigDecimal getProm() {
		return prom;
	}
	public void setProm(BigDecimal prom) {
		this.prom = prom;
	}
	public Integer getId_au() {
		return id_au;
	}
	public void setId_au(Integer id_au) {
		this.id_au = id_au;
	}
	public Integer getId_cua() {
		return id_cua;
	}
	public void setId_cua(Integer id_cua) {
		this.id_cua = id_cua;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
}

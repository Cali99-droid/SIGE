package com.sige.rest.request;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tesla.colegio.model.CompetenciaDc; 

public class TardanzaReq implements Serializable{
 
	private static final long serialVersionUID = -4470656952360885399L;
	private Integer tot_tar_injustificada;
	private Integer tot_tar_justificada;
	private Integer tot_falta_injustificada;
	private Integer tot_falta_justificada;
	private List<TardanzaPeriodoReq> list_tardanzas_periodo;
	
	public List<TardanzaPeriodoReq> getList_tardanzas_periodo() {
		return list_tardanzas_periodo;
	}
	public void setList_tardanzas_periodo(List<TardanzaPeriodoReq> list_tardanzas_periodo) {
		this.list_tardanzas_periodo = list_tardanzas_periodo;
	}
	public Integer getTot_tar_injustificada() {
		return tot_tar_injustificada;
	}
	public void setTot_tar_injustificada(Integer tot_tar_injustificada) {
		this.tot_tar_injustificada = tot_tar_injustificada;
	}
	public Integer getTot_tar_justificada() {
		return tot_tar_justificada;
	}
	public void setTot_tar_justificada(Integer tot_tar_justificada) {
		this.tot_tar_justificada = tot_tar_justificada;
	}
	public Integer getTot_falta_injustificada() {
		return tot_falta_injustificada;
	}
	public void setTot_falta_injustificada(Integer tot_falta_injustificada) {
		this.tot_falta_injustificada = tot_falta_injustificada;
	}
	public Integer getTot_falta_justificada() {
		return tot_falta_justificada;
	}
	public void setTot_falta_justificada(Integer tot_falta_justificada) {
		this.tot_falta_justificada = tot_falta_justificada;
	}
	@Override
	public String toString() {
		return " [{\"tot_tar_injustificada\":\"" + tot_tar_injustificada + "\", \"tot_tar_justificada\":\""
				+ tot_tar_justificada + "\", \"tot_falta_injustificada\":\"" + tot_falta_injustificada
				+ "\", \"tot_falta_justificada\":\"" + tot_falta_justificada + "\", \"list_tardanzas_periodo\":"
				+ list_tardanzas_periodo + "}]";
	}	
	
	/*@Override
	public String toString() {
		return "[{\"total_tardanzas\":\"" + total_tardanzas + "\", \"list_tardanzas_periodo\":\"" + list_tardanzas_periodo
				+ "\"}]";
	}*/
}

package com.sige.rest.request;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tesla.colegio.model.CompetenciaDc;
import com.tesla.colegio.model.PerUni;
import com.tesla.frmk.sql.Row; 

public class DetalleNotaReq implements Serializable{
 
	private static final long serialVersionUID = -4470656952360885399L;
	private Integer id_mat;
	private String tipo_periodo;
	private Integer cant_areas;
	private List<AreaNotaReq> list_areas;
	private List<PeriodosReq> list_periodos;
	private List<AreaCompetenciaReq> list_areas_com;
	private String comentario_tutor;
	private String nom_tutor;
	private Integer puesto;
	public Integer getId_mat() {
		return id_mat;
	}
	public void setId_mat(Integer id_mat) {
		this.id_mat = id_mat;
	}
	public String getTipo_periodo() {
		return tipo_periodo;
	}
	public void setTipo_periodo(String tipo_periodo) {
		this.tipo_periodo = tipo_periodo;
	}
	public Integer getCant_areas() {
		return cant_areas;
	}
	public void setCant_areas(Integer cant_areas) {
		this.cant_areas = cant_areas;
	}
	public List<AreaNotaReq> getList_areas() {
		return list_areas;
	}
	public void setList_areas(List<AreaNotaReq> list_areas) {
		this.list_areas = list_areas;
	}
	public List<PeriodosReq> getList_periodos() {
		return list_periodos;
	}
	public void setList_periodos(List<PeriodosReq> list_periodos) {
		this.list_periodos = list_periodos;
	}
	public String getComentario_tutor() {
		return comentario_tutor;
	}
	public void setComentario_tutor(String comentario_tutor) {
		this.comentario_tutor = comentario_tutor;
	}
	public String getNom_tutor() {
		return nom_tutor;
	}
	public void setNom_tutor(String nom_tutor) {
		this.nom_tutor = nom_tutor;
	}
	public Integer getPuesto() {
		return puesto;
	}
	public void setPuesto(Integer puesto) {
		this.puesto = puesto;
	}
	public List<AreaCompetenciaReq> getList_areas_com() {
		return list_areas_com;
	}
	public void setList_areas_com(List<AreaCompetenciaReq> list_areas_com) {
		this.list_areas_com = list_areas_com;
	}
		
}

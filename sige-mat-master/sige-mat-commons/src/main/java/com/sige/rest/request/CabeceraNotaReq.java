package com.sige.rest.request;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tesla.colegio.model.CompetenciaDc; 

public class CabeceraNotaReq implements Serializable{
 
	private static final long serialVersionUID = -4470656952360885399L;
	private Integer id_mat;
	private String titulo;
	private String anio;
	private String nivel;
	private String periodo;
	private String cod_alumno;
	private String alumno;
	private String salon;
	private Integer nro_orden;
	private String tipo_periodo;
	private Integer cant_areas;
	private List<AreaNotaReq> list_areas_notas;
	private List<PeriodosReq> list_periodos;
	private List<AreaCompetenciaReq> list_areas_com;
	private TardanzaReq list_tardanzas;
	private NotaConductalReq list_notas_cond;
	private String comentario_tutor;
	private String nom_tutor;
	private Integer puesto;
	public Integer getId_mat() {
		return id_mat;
	}
	public void setId_mat(Integer id_mat) {
		this.id_mat = id_mat;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	public String getNivel() {
		return nivel;
	}
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public String getCod_alumno() {
		return cod_alumno;
	}
	public void setCod_alumno(String cod_alumno) {
		this.cod_alumno = cod_alumno;
	}
	public String getAlumno() {
		return alumno;
	}
	public void setAlumno(String alumno) {
		this.alumno = alumno;
	}
	public String getSalon() {
		return salon;
	}
	public void setSalon(String salon) {
		this.salon = salon;
	}
	public Integer getNro_orden() {
		return nro_orden;
	}
	public void setNro_orden(Integer nro_orden) {
		this.nro_orden = nro_orden;
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
	public List<AreaNotaReq> getList_areas_notas() {
		return list_areas_notas;
	}
	public void setList_areas_notas(List<AreaNotaReq> list_areas_notas) {
		this.list_areas_notas = list_areas_notas;
	}
	public List<PeriodosReq> getList_periodos() {
		return list_periodos;
	}
	public void setList_periodos(List<PeriodosReq> list_periodos) {
		this.list_periodos = list_periodos;
	}
	public List<AreaCompetenciaReq> getList_areas_com() {
		return list_areas_com;
	}
	public void setList_areas_com(List<AreaCompetenciaReq> list_areas_com) {
		this.list_areas_com = list_areas_com;
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
	@Override
	public String toString() {
		return "[{\"id_mat\":\"" + id_mat + "\", \"titulo\":\"" + titulo + "\", \"anio\":\"" + anio + "\", \"nivel\":\"" + nivel
				+ "\", \"periodo\":\"" + periodo + "\", \"cod_alumno\":\"" + cod_alumno + "\", \"alumno\":\"" + alumno + "\", \"salon\":\"" + salon
				+ "\", \"nro_orden\":\"" + nro_orden + "\", \"tipo_periodo\":\"" + tipo_periodo + "\", \"cant_areas\":\"" + cant_areas
				+ "\", \"list_areas_com\":" + list_areas_com + ", \"list_periodos\":" + list_periodos + ",\"list_tardanzas\":"+ list_tardanzas+ ",\"list_notas_cond\":"+ list_notas_cond+ ", \"list_areas_notas\":"
				+ list_areas_notas + ", \"comentario_tutor\":" + comentario_tutor + ", \"nom_tutor\":\"" + nom_tutor + "\", \"puesto\":"
				+ puesto + "}]";
		
		/*return "[{\"id_mat\":\"" + id_mat + "\", \"titulo\":\"" + titulo + "\", \"anio\":\"" + anio + "\", \"nivel\":\"" + nivel
				+ "\", \"periodo\":\"" + periodo + "\", \"cod_alumno\":\"" + cod_alumno + "\", \"alumno\":\"" + alumno + "\", \"salon\":\"" + salon
				+ "\", \"nro_orden\":\"" + nro_orden + "\", \"tipo_periodo\":\"" + tipo_periodo + "\", \"cant_areas\":\"" + cant_areas+ "\"}]";*/
		
		//return "[{idClassroom:" + idClassroom + ", listUsuarios:[" + listUsuarios + "]}]";
	}
	public TardanzaReq getList_tardanzas() {
		return list_tardanzas;
	}
	public void setList_tardanzas(TardanzaReq list_tardanzas) {
		this.list_tardanzas = list_tardanzas;
	}
	public NotaConductalReq getList_notas_cond() {
		return list_notas_cond;
	}
	public void setList_notas_cond(NotaConductalReq list_notas_cond) {
		this.list_notas_cond = list_notas_cond;
	}
		
}

package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla col_per_uni
 * @author MV
 *
 */
public class PerUni extends EntidadBase{

	public final static String TABLA = "col_per_uni";
	private Integer id;
	private Integer id_cpa;
	private Integer nump;
	private Integer numu_ini;
	private Integer numu_fin;
	private Integer id_anio;
	private Integer id_gir;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_ini;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_fin;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_ini_ing;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_fin_ing;
	private PerAcaNivel peracanivel;
	private Nivel nivel;
	private PeriodoAca periodoAca;
	private GiroNegocio giroNegocio;
	private List<CursoUnidad> cursounidads;

	public PerUni(){
	}

	/**
	* Obtiene Llave principal 
	* @return id
	*/
	public Integer getId(){
		return id;
	}	

	/**
	* Llave principal 
	* @param id
	*/
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	* Obtiene Periodo Acadmico Nivel 
	* @return id_cpa
	*/
	public Integer getId_cpa(){
		return id_cpa;
	}	

	/**
	* Periodo Acadmico Nivel 
	* @param id_cpa
	*/
	public void setId_cpa(Integer id_cpa) {
		this.id_cpa = id_cpa;
	}

	/**
	* Obtiene Nmero de Periodo 
	* @return nump
	*/
	public Integer getNump(){
		return nump;
	}	

	/**
	* Nmero de Periodo 
	* @param nump
	*/
	public void setNump(Integer nump) {
		this.nump = nump;
	}

	public PerAcaNivel getPerAcaNivel(){
		return peracanivel;
	}	

	public void setPerAcaNivel(PerAcaNivel peracanivel) {
		this.peracanivel = peracanivel;
	}
	/**
	* Obtiene lista de Unidad Didctica 
	*/
	public List<CursoUnidad> getCursoUnidads() {
		return cursounidads;
	}

	/**
	* Seta Lista de Unidad Didctica 
	* @param cursounidads
	*/	
	public void setCursoUnidad(List<CursoUnidad> cursounidads) {
		this.cursounidads = cursounidads;
	}

	public Nivel getNivel() {
		return nivel;
	}

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}

	public PeriodoAca getPeriodoAca() {
		return periodoAca;
	}

	public void setPeriodoAca(PeriodoAca periodoAca) {
		this.periodoAca = periodoAca;
	}

	public Integer getNumu_ini() {
		return numu_ini;
	}

	public void setNumu_ini(Integer numu_ini) {
		this.numu_ini = numu_ini;
	}

	public Integer getNumu_fin() {
		return numu_fin;
	}

	public void setNumu_fin(Integer numu_fin) {
		this.numu_fin = numu_fin;
	}

	public Integer getId_anio() {
		return id_anio;
	}

	public void setId_anio(Integer id_anio) {
		this.id_anio = id_anio;
	}

	public java.util.Date getFec_ini() {
		return fec_ini;
	}

	public void setFec_ini(java.util.Date fec_ini) {
		this.fec_ini = fec_ini;
	}

	public java.util.Date getFec_fin() {
		return fec_fin;
	}

	public void setFec_fin(java.util.Date fec_fin) {
		this.fec_fin = fec_fin;
	}

	public java.util.Date getFec_ini_ing() {
		return fec_ini_ing;
	}

	public void setFec_ini_ing(java.util.Date fec_ini_ing) {
		this.fec_ini_ing = fec_ini_ing;
	}

	public java.util.Date getFec_fin_ing() {
		return fec_fin_ing;
	}

	public void setFec_fin_ing(java.util.Date fec_fin_ing) {
		this.fec_fin_ing = fec_fin_ing;
	}

	public Integer getId_gir() {
		return id_gir;
	}

	public void setId_gir(Integer id_gir) {
		this.id_gir = id_gir;
	}

	public GiroNegocio getGiroNegocio() {
		return giroNegocio;
	}

	public void setGiroNegocio(GiroNegocio giroNegocio) {
		this.giroNegocio = giroNegocio;
	}
}
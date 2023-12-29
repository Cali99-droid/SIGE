package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase entidad que representa a la tabla not_evaluacion
 * @author MV
 *
 */
public class Evaluacion extends EntidadBase{

	public final static String TABLA = "not_evaluacion";
	private Integer id;
	private Integer id_nep;
	private Integer id_cca;
	private Integer id_nte;
	private Integer id_ses;
	private String ins;
	private String evi;
	private Integer nump;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_ini;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_fin;
	private CursoAula cursoaula;
	private Curso curso;
	private Aula aula;
	private Grad grad;
	private TipEva tipeva;
	private Nivel nivel;
	private Sucursal sucursal;
	private List<IndEva> indevas = new ArrayList<>();

	public Evaluacion(){
	}

	/**
	* Obtiene $field.description 
	* @return id
	*/
	public Integer getId(){
		return id;
	}	

	/**
	* $field.description 
	* @param id
	*/
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	* Obtiene Curso Aula 
	* @return id_cca
	*/
	public Integer getId_cca(){
		return id_cca;
	}	

	/**
	* Curso Aula 
	* @param id_cca
	*/
	public void setId_cca(Integer id_cca) {
		this.id_cca = id_cca;
	}

	/**
	* Obtiene Tipo Evaluacion 
	* @return id_nte
	*/
	public Integer getId_nte(){
		return id_nte;
	}	

	/**
	* Tipo Evaluacion 
	* @param id_nte
	*/
	public void setId_nte(Integer id_nte) {
		this.id_nte = id_nte;
	}

	/**
	* Obtiene Instrumento 
	* @return ins
	*/
	public String getIns(){
		return ins;
	}	

	/**
	* Instrumento 
	* @param ins
	*/
	public void setIns(String ins) {
		this.ins = ins;
	}

	/**
	* Obtiene Evidencia 
	* @return evi
	*/
	public String getEvi(){
		return evi;
	}	

	/**
	* Evidencia 
	* @param evi
	*/
	public void setEvi(String evi) {
		this.evi = evi;
	}

	/**
	* Obtiene Fecha Inicio de Evaluacion 
	* @return fec_ini
	*/
	public java.util.Date getFec_ini(){
		return fec_ini;
	}	

	/**
	* Fecha Inicio de Evaluacion 
	* @param fec_ini
	*/
	public void setFec_ini(java.util.Date fec_ini) {
		this.fec_ini = fec_ini;
	}

	/**
	* Obtiene Fecha Fin de Evaluacion 
	* @return fec_fin
	*/
	public java.util.Date getFec_fin(){
		return fec_fin;
	}	

	/**
	* Fecha Fin de Evaluacion 
	* @param fec_fin
	*/
	public void setFec_fin(java.util.Date fec_fin) {
		this.fec_fin = fec_fin;
	}

	public CursoAula getCursoAula(){
		return cursoaula;
	}	

	public void setCursoAula(CursoAula cursoaula) {
		this.cursoaula = cursoaula;
	}
	public TipEva getTipEva(){
		return tipeva;
	}	

	public void setTipEva(TipEva tipeva) {
		this.tipeva = tipeva;
	}
	/**
	* Obtiene lista de Indicador Evalaucion 
	*/
	public List<IndEva> getIndEvas() {
		return indevas;
	}

	/**
	* Seta Lista de Indicador Evalaucion 
	* @param indevas
	*/	
	public void setIndEva(List<IndEva> indevas) {
		this.indevas = indevas;
	}

	public Integer getId_nep() {
		return id_nep;
	}

	public void setId_nep(Integer id_nep) {
		this.id_nep = id_nep;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Aula getAula() {
		return aula;
	}

	public void setAula(Aula aula) {
		this.aula = aula;
	}

	public Grad getGrad() {
		return grad;
	}

	public void setGrad(Grad grad) {
		this.grad = grad;
	}

	public Integer getNump() {
		return nump;
	}

	public void setNump(Integer nump) {
		this.nump = nump;
	}

	public Nivel getNivel() {
		return nivel;
	}

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}

	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	public Integer getId_ses() {
		return id_ses;
	}

	public void setId_ses(Integer id_ses) {
		this.id_ses = id_ses;
	}
}
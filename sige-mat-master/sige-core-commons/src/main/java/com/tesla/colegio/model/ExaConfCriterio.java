package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;

/**
 * Clase entidad que representa a la tabla eva_exa_conf_criterio
 * @author MV
 *
 */
public class ExaConfCriterio extends EntidadBase{

	public final static String TABLA = "eva_exa_conf_criterio";
	private Integer id;
	private Integer id_eva_ex;
	private Integer dur;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_ini_psi;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_fin_psi;
	private EvaluacionVacExamen evaluacionvacexamen;	
	private List<InsExaCri> insexacris;
	private List<CriterioNota> criterionotas;


	public ExaConfCriterio(){
	}

	/**
	* Obtiene Llave Principal 
	* @return id
	*/
	public Integer getId(){
		return id;
	}	

	/**
	* Llave Principal 
	* @param id
	*/
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	* Obtiene Evaluacin Examen 
	* @return id_eva_ex
	*/
	public Integer getId_eva_ex(){
		return id_eva_ex;
	}	

	/**
	* Evaluacin Examen 
	* @param id_eva_ex
	*/
	public void setId_eva_ex(Integer id_eva_ex) {
		this.id_eva_ex = id_eva_ex;
	}

	/**
	* Obtiene Duracin 
	* @return dur
	*/
	public Integer getDur(){
		return dur;
	}	

	/**
	* Duracin 
	* @param dur
	*/
	public void setDur(Integer dur) {
		this.dur = dur;
	}

	/**
	* Obtiene Fecha de inicio 
	* @return fec_ini_psi
	*/
	public java.util.Date getFec_ini_psi(){
		return fec_ini_psi;
	}	

	/**
	* Fecha de inicio 
	* @param fec_ini_psi
	*/
	public void setFec_ini_psi(java.util.Date fec_ini_psi) {
		this.fec_ini_psi = fec_ini_psi;
	}

	/**
	* Obtiene Fecha fin 
	* @return fec_fin_psi
	*/
	public java.util.Date getFec_fin_psi(){
		return fec_fin_psi;
	}	

	/**
	* Fecha fin 
	* @param fec_fin_psi
	*/
	public void setFec_fin_psi(java.util.Date fec_fin_psi) {
		this.fec_fin_psi = fec_fin_psi;
	}

	public EvaluacionVacExamen getEvaluacionVacExamen(){
		return evaluacionvacexamen;
	}	

	public void setEvaluacionVacExamen(EvaluacionVacExamen evaluacionvacexamen) {
		this.evaluacionvacexamen = evaluacionvacexamen;
	}

	/**
	* Obtiene lista de Evaluacin a criterio 
	*/
	public List<CriterioNota> getCriterioNotas() {
		return criterionotas;
	}

	/**
	* Seta Lista de Evaluacin a criterio 
	* @param criterionotas
	*/	
	public void setCriterioNota(List<CriterioNota> criterionotas) {
		this.criterionotas = criterionotas;
	}
	/**
	* Obtiene lista de Examen Criterio Instrumentos 
	*/
	public List<InsExaCri> getInsExaCris() {
		return insexacris;
	}

	/**
	* Seta Lista de Examen Criterio Instrumentos 
	* @param insexacris
	*/	
	public void setInsExaCri(List<InsExaCri> insexacris) {
		this.insexacris = insexacris;
	}
}
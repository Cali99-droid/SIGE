package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;

/**
 * Clase entidad que representa a la tabla eva_evaluacion_vac
 * @author MV
 *
 */
public class EvaluacionVac extends EntidadBase{

	public final static String TABLA = "eva_evaluacion_vac";
	private Integer id;
	private Integer id_per;
	private String des;
	private java.math.BigDecimal precio;
	private java.math.BigDecimal ptje_apro;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_ini;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_fin;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_vig_vac;
	private Periodo periodo;	
	private List<EvaluacionVacExamen> evaluacionvacexamens;
	private List<MatrVacante> matrvacantes;

	public EvaluacionVac(){
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
	* Obtiene Periodo Acadmico 
	* @return id_per
	*/
	public Integer getId_per(){
		return id_per;
	}	

	/**
	* Periodo Acadmico 
	* @param id_per
	*/
	public void setId_per(Integer id_per) {
		this.id_per = id_per;
	}

	/**
	* Obtiene Descripcin 
	* @return des
	*/
	public String getDes(){
		return des;
	}	

	/**
	* Descripcin 
	* @param des
	*/
	public void setDes(String des) {
		this.des = des;
	}

	/**
	* Obtiene Precio 
	* @return precio
	*/
	public java.math.BigDecimal getPrecio(){
		return precio;
	}	

	/**
	* Precio 
	* @param precio
	*/
	public void setPrecio(java.math.BigDecimal precio) {
		this.precio = precio;
	}

	/**
	* Obtiene Puntaje mnimo aprobatorio 
	* @return ptje_apro
	*/
	public java.math.BigDecimal getPtje_apro(){
		return ptje_apro;
	}	

	/**
	* Puntaje mnimo aprobatorio 
	* @param ptje_apro
	*/
	public void setPtje_apro(java.math.BigDecimal ptje_apro) {
		this.ptje_apro = ptje_apro;
	}

	/**
	* Obtiene Fecha de inicio 
	* @return fec_ini
	*/
	public java.util.Date getFec_ini(){
		return fec_ini;
	}	

	/**
	* Fecha de inicio 
	* @param fec_ini
	*/
	public void setFec_ini(java.util.Date fec_ini) {
		this.fec_ini = fec_ini;
	}

	/**
	* Obtiene Fecha fin 
	* @return fec_fin
	*/
	public java.util.Date getFec_fin(){
		return fec_fin;
	}	

	/**
	* Fecha fin 
	* @param fec_fin
	*/
	public void setFec_fin(java.util.Date fec_fin) {
		this.fec_fin = fec_fin;
	}

	public Periodo getPeriodo(){
		return periodo;
	}	

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
	/**
	* Obtiene lista de Examen por Marcacin 
	*/
	public List<EvaluacionVacExamen> getEvaluacionVacExamens() {
		return evaluacionvacexamens;
	}

	/**
	* Seta Lista de Examen por Marcacin 
	* @param evaluacionvacexamens
	*/	
	public void setEvaluacionVacExamen(List<EvaluacionVacExamen> evaluacionvacexamens) {
		this.evaluacionvacexamens = evaluacionvacexamens;
	}
	/**
	* Obtiene lista de Examen Vacante 
	*/
	public List<MatrVacante> getMatrVacantes() {
		return matrvacantes;
	}

	/**
	* Seta Lista de Examen Vacante 
	* @param matrvacantes
	*/	
	public void setMatrVacante(List<MatrVacante> matrvacantes) {
		this.matrvacantes = matrvacantes;
	}

	public java.util.Date getFec_vig_vac() {
		return fec_vig_vac;
	}

	public void setFec_vig_vac(java.util.Date fec_vig_vac) {
		this.fec_vig_vac = fec_vig_vac;
	}
}
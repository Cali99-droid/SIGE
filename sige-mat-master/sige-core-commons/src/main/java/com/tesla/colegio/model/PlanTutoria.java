package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla con_plan_tutoria
 * @author MV
 *
 */
public class PlanTutoria extends EntidadBase{

	public final static String TABLA = "con_plan_tutoria";
	private Integer id;
	private Integer id_cfi;
	private Integer id_inc;
	private String fun;
	private String obj;
	private String eva;
	private String archivo;
	private FormatoIncidencia formatoincidencia;	
	private Incidencia incidencia;	
	private List<PlanActividades> planactividadess;

	public PlanTutoria(){
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
	* Obtiene Formato Incidencia 
	* @return id_cfi
	*/
	public Integer getId_cfi(){
		return id_cfi;
	}	

	/**
	* Formato Incidencia 
	* @param id_cfi
	*/
	public void setId_cfi(Integer id_cfi) {
		this.id_cfi = id_cfi;
	}

	/**
	* Obtiene Incidencia 
	* @return id_inc
	*/
	public Integer getId_inc(){
		return id_inc;
	}	

	/**
	* Incidencia 
	* @param id_inc
	*/
	public void setId_inc(Integer id_inc) {
		this.id_inc = id_inc;
	}

	/**
	* Obtiene Fundamento 
	* @return fun
	*/
	public String getFun(){
		return fun;
	}	

	/**
	* Fundamento 
	* @param fun
	*/
	public void setFun(String fun) {
		this.fun = fun;
	}

	/**
	* Obtiene Objetivo 
	* @return obj
	*/
	public String getObj(){
		return obj;
	}	

	/**
	* Objetivo 
	* @param obj
	*/
	public void setObj(String obj) {
		this.obj = obj;
	}

	/**
	* Obtiene Evaluacion 
	* @return eva
	*/
	public String getEva(){
		return eva;
	}	

	/**
	* Evaluacion 
	* @param eva
	*/
	public void setEva(String eva) {
		this.eva = eva;
	}

	/**
	* Obtiene Documento Escaneado 
	* @return archivo
	*/
	public String getArchivo(){
		return archivo;
	}	

	/**
	* Documento Escaneado 
	* @param archivo
	*/
	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}

	public FormatoIncidencia getFormatoIncidencia(){
		return formatoincidencia;
	}	

	public void setFormatoIncidencia(FormatoIncidencia formatoincidencia) {
		this.formatoincidencia = formatoincidencia;
	}
	public Incidencia getIncidencia(){
		return incidencia;
	}	

	public void setIncidencia(Incidencia incidencia) {
		this.incidencia = incidencia;
	}
	/**
	* Obtiene lista de Plan Actividades 
	*/
	public List<PlanActividades> getPlanActividadess() {
		return planactividadess;
	}

	/**
	* Seta Lista de Plan Actividades 
	* @param planactividadess
	*/	
	public void setPlanActividades(List<PlanActividades> planactividadess) {
		this.planactividadess = planactividadess;
	}
}
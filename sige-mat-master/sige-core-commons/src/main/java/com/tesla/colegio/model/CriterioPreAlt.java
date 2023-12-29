package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla eva_criterio_pre_alt
 * @author MV
 *
 */
public class CriterioPreAlt extends EntidadBase{

	public final static String TABLA = "eva_criterio_pre_alt";
	private Integer id;
	private Integer id_cri_not;
	private Integer id_pre;
	private Integer id_alt;
	private CriterioNota criterionota;	
	private CriterioPregunta criteriopregunta;	
	private CriterioAlternativa criterioalternativa;	

	public CriterioPreAlt(){
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
	* Obtiene Examen Criterio Nota 
	* @return id_cri_not
	*/
	public Integer getId_cri_not(){
		return id_cri_not;
	}	

	/**
	* Examen Criterio Nota 
	* @param id_cri_not
	*/
	public void setId_cri_not(Integer id_cri_not) {
		this.id_cri_not = id_cri_not;
	}

	/**
	* Obtiene Pregunta 
	* @return id_pre
	*/
	public Integer getId_pre(){
		return id_pre;
	}	

	/**
	* Pregunta 
	* @param id_pre
	*/
	public void setId_pre(Integer id_pre) {
		this.id_pre = id_pre;
	}

	/**
	* Obtiene Alternativa 
	* @return id_alt
	*/
	public Integer getId_alt(){
		return id_alt;
	}	

	/**
	* Alternativa 
	* @param id_alt
	*/
	public void setId_alt(Integer id_alt) {
		this.id_alt = id_alt;
	}

	public CriterioNota getCriterioNota(){
		return criterionota;
	}	

	public void setCriterioNota(CriterioNota criterionota) {
		this.criterionota = criterionota;
	}
	public CriterioPregunta getCriterioPregunta(){
		return criteriopregunta;
	}	

	public void setCriterioPregunta(CriterioPregunta criteriopregunta) {
		this.criteriopregunta = criteriopregunta;
	}
	public CriterioAlternativa getCriterioAlternativa(){
		return criterioalternativa;
	}	

	public void setCriterioAlternativa(CriterioAlternativa criterioalternativa) {
		this.criterioalternativa = criterioalternativa;
	}
}
package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla eva_marcacion_nota
 * @author MV
 *
 */
public class MarcacionNota extends EntidadBase{

	public final static String TABLA = "eva_marcacion_nota";
	private Integer id;
	private Integer id_mat_vac;
	private Integer id_exa_mar;
	private Integer preg_favor;
	private Integer preg_contra;
	private java.math.BigDecimal ptje;
	private MatrVacante matrvacante;	
	private ExaConfMarcacion exaconfmarcacion;	

	public MarcacionNota(){
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
	* Obtiene Matricula Vacante 
	* @return id_mat_vac
	*/
	public Integer getId_mat_vac(){
		return id_mat_vac;
	}	

	/**
	* Matricula Vacante 
	* @param id_mat_vac
	*/
	public void setId_mat_vac(Integer id_mat_vac) {
		this.id_mat_vac = id_mat_vac;
	}

	/**
	* Obtiene Examen Marcacion 
	* @return id_exa_mar
	*/
	public Integer getId_exa_mar(){
		return id_exa_mar;
	}	

	/**
	* Examen Marcacion 
	* @param id_exa_mar
	*/
	public void setId_exa_mar(Integer id_exa_mar) {
		this.id_exa_mar = id_exa_mar;
	}

	/**
	* Obtiene Pregunta a favor del area 
	* @return preg_favor
	*/
	public Integer getPreg_favor(){
		return preg_favor;
	}	

	/**
	* Pregunta a favor del area 
	* @param preg_favor
	*/
	public void setPreg_favor(Integer preg_favor) {
		this.preg_favor = preg_favor;
	}

	/**
	* Obtiene Pregunta en contra del area 
	* @return preg_contra
	*/
	public Integer getPreg_contra(){
		return preg_contra;
	}	

	/**
	* Pregunta en contra del area 
	* @param preg_contra
	*/
	public void setPreg_contra(Integer preg_contra) {
		this.preg_contra = preg_contra;
	}

	/**
	* Obtiene Puntaje del examen 
	* @return ptje
	*/
	public java.math.BigDecimal getPtje(){
		return ptje;
	}	

	/**
	* Puntaje del examen 
	* @param ptje
	*/
	public void setPtje(java.math.BigDecimal ptje) {
		this.ptje = ptje;
	}

	public MatrVacante getMatrVacante(){
		return matrvacante;
	}	

	public void setMatrVacante(MatrVacante matrvacante) {
		this.matrvacante = matrvacante;
	}
	public ExaConfMarcacion getExaConfMarcacion(){
		return exaconfmarcacion;
	}	

	public void setExaConfMarcacion(ExaConfMarcacion exaconfmarcacion) {
		this.exaconfmarcacion = exaconfmarcacion;
	}
}
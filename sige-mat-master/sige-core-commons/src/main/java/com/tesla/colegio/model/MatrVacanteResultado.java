package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla eva_matr_vacante_resultado
 * @author MV
 *
 */
public class MatrVacanteResultado extends EntidadBase{

	public final static String TABLA = "eva_matr_vacante_resultado";
	private Integer id;
	private Integer id_mat_vac;
	private java.math.BigDecimal notafinal;
	private String res;
	private MatrVacante matrvacante;	

	public MatrVacanteResultado(){
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
	* Obtiene Nota final 
	* @return notafinal
	*/
	public java.math.BigDecimal getNotafinal(){
		return notafinal;
	}	

	/**
	* Nota final 
	* @param notafinal
	*/
	public void setNotafinal(java.math.BigDecimal notafinal) {
		this.notafinal = notafinal;
	}

	/**
	* Obtiene REsultado A: aprobado, D:Desaprobado 
	* @return res
	*/
	public String getRes(){
		return res;
	}	

	/**
	* REsultado A: aprobado, D:Desaprobado 
	* @param res
	*/
	public void setRes(String res) {
		this.res = res;
	}

	public MatrVacante getMatrVacante(){
		return matrvacante;
	}	

	public void setMatrVacante(MatrVacante matrvacante) {
		this.matrvacante = matrvacante;
	}
}
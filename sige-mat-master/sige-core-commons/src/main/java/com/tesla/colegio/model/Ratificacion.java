package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla mat_ratificacion
 * @author MV
 *
 */
public class Ratificacion extends EntidadBase{

	public final static String TABLA = "mat_ratificacion";
	private Integer id;
	private Integer id_mat;
	private Integer id_anio_rat;
	private String res;
	private Matricula matricula;	
	private Anio anio;	

	public Ratificacion(){
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
	* Obtiene Matricula 
	* @return id_mat
	*/
	public Integer getId_mat(){
		return id_mat;
	}	

	/**
	* Matricula 
	* @param id_mat
	*/
	public void setId_mat(Integer id_mat) {
		this.id_mat = id_mat;
	}

	/**
	* Obtiene Anio Ratificado 
	* @return id_anio_rat
	*/
	public Integer getId_anio_rat(){
		return id_anio_rat;
	}	

	/**
	* Anio Ratificado 
	* @param id_anio_rat
	*/
	public void setId_anio_rat(Integer id_anio_rat) {
		this.id_anio_rat = id_anio_rat;
	}

	/**
	* Obtiene Respuesta 
	* @return res
	*/
	public String getRes(){
		return res;
	}	

	/**
	* Respuesta 
	* @param res
	*/
	public void setRes(String res) {
		this.res = res;
	}

	public Matricula getMatricula(){
		return matricula;
	}	

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}
	public Anio getAnio(){
		return anio;
	}	

	public void setAnio(Anio anio) {
		this.anio = anio;
	}
}
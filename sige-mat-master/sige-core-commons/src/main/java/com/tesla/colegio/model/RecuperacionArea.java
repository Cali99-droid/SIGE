package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla mat_recuperacion_area
 * @author MV
 *
 */
public class RecuperacionArea extends EntidadBase{

	public final static String TABLA = "mat_recuperacion_area";
	private Integer id;
	private Integer id_mat;
	private Integer id_area;
	private Integer prom;
	private Matricula matricula;	
	private Area area;	

	public RecuperacionArea(){
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
	* Obtiene Matcula Alumno 
	* @return id_mat
	*/
	public Integer getId_mat(){
		return id_mat;
	}	

	/**
	* Matcula Alumno 
	* @param id_mat
	*/
	public void setId_mat(Integer id_mat) {
		this.id_mat = id_mat;
	}

	/**
	* Obtiene Area 
	* @return id_area
	*/
	public Integer getId_area(){
		return id_area;
	}	

	/**
	* Area 
	* @param id_area
	*/
	public void setId_area(Integer id_area) {
		this.id_area = id_area;
	}

	/**
	* Obtiene Nota 
	* @return prom
	*/
	public Integer getProm(){
		return prom;
	}	

	/**
	* Nota 
	* @param prom
	*/
	public void setProm(Integer prom) {
		this.prom = prom;
	}

	public Matricula getMatricula(){
		return matricula;
	}	

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}
	public Area getArea(){
		return area;
	}	

	public void setArea(Area area) {
		this.area = area;
	}
}
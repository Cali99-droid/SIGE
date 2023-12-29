package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla not_nota_area
 * @author MV
 *
 */
public class NotaArea extends EntidadBase{

	public final static String TABLA = "not_nota_area";
	private Integer id;
	private Integer id_mat;
	private Integer id_au;
	private Integer id_area;
	private Integer prom;
	private Matricula matricula;	
	private Aula aula;	
	private Area area;	

	public NotaArea(){
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
	* Obtiene Aula 
	* @return id_au
	*/
	public Integer getId_au(){
		return id_au;
	}	

	/**
	* Aula 
	* @param id_au
	*/
	public void setId_au(Integer id_au) {
		this.id_au = id_au;
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
	* Obtiene Promedio final del area 
	* @return prom
	*/
	public Integer getProm(){
		return prom;
	}	

	/**
	* Promedio final del area 
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
	public Aula getAula(){
		return aula;
	}	

	public void setAula(Aula aula) {
		this.aula = aula;
	}
	public Area getArea(){
		return area;
	}	

	public void setArea(Area area) {
		this.area = area;
	}
}
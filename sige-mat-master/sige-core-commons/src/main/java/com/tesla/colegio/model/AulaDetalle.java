package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_aula_detalle
 * @author MV
 *
 */
public class AulaDetalle extends EntidadBase{

	public final static String TABLA = "col_aula_detalle";
	private Integer id;
	private Integer id_au;
	private Integer id_tut;
	private Integer id_aux;
	private Integer id_cord;
	private String nom;
	private String salon;
	private Aula aula;	
	private Trabajador trabajador;	

	public AulaDetalle(){
	}

	/**
	* Obtiene Llave principal 
	* @return id
	*/
	public Integer getId(){
		return id;
	}	

	/**
	* Llave principal 
	* @param id
	*/
	public void setId(Integer id) {
		this.id = id;
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
	* Obtiene Tutor 
	* @return id_tut
	*/
	public Integer getId_tut(){
		return id_tut;
	}	

	/**
	* Tutor 
	* @param id_tut
	*/
	public void setId_tut(Integer id_tut) {
		this.id_tut = id_tut;
	}

	/**
	* Obtiene Auxiliar 
	* @return id_aux
	*/
	public Integer getId_aux(){
		return id_aux;
	}	

	/**
	* Auxiliar 
	* @param id_aux
	*/
	public void setId_aux(Integer id_aux) {
		this.id_aux = id_aux;
	}

	/**
	* Obtiene Coordinador 
	* @return id_cord
	*/
	public Integer getId_cord(){
		return id_cord;
	}	

	/**
	* Coordinador 
	* @param id_cord
	*/
	public void setId_cord(Integer id_cord) {
		this.id_cord = id_cord;
	}

	/**
	* Obtiene Nombre 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Salon 
	* @return salon
	*/
	public String getSalon(){
		return salon;
	}	

	/**
	* Salon 
	* @param salon
	*/
	public void setSalon(String salon) {
		this.salon = salon;
	}

	public Aula getAula(){
		return aula;
	}	

	public void setAula(Aula aula) {
		this.aula = aula;
	}
	public Trabajador getTrabajador(){
		return trabajador;
	}	

	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
	}
	
}
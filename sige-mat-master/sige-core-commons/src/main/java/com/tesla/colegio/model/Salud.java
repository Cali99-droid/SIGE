package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla alu_salud
 * @author MV
 *
 */
public class Salud extends EntidadBase{

	public final static String TABLA = "alu_salud";
	private Integer id;
	private Integer id_alu;
	private String peso_nac;
	private String talla_nac;
	private String nu_edad_cabe;
	private String nu_edad_paro;
	private String nu_edad_cami;
	private Alumno alumno;	

	public Salud(){
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
	* Obtiene Alumno 
	* @return id_alu
	*/
	public Integer getId_alu(){
		return id_alu;
	}	

	/**
	* Alumno 
	* @param id_alu
	*/
	public void setId_alu(Integer id_alu) {
		this.id_alu = id_alu;
	}

	/**
	* Obtiene Peso de nacimiento del alumno 
	* @return peso_nac
	*/
	public String getPeso_nac(){
		return peso_nac;
	}	

	/**
	* Peso de nacimiento del alumno 
	* @param peso_nac
	*/
	public void setPeso_nac(String peso_nac) {
		this.peso_nac = peso_nac;
	}

	/**
	* Obtiene Talla de nacimiento del alumno 
	* @return talla_nac
	*/
	public String getTalla_nac(){
		return talla_nac;
	}	

	/**
	* Talla de nacimiento del alumno 
	* @param talla_nac
	*/
	public void setTalla_nac(String talla_nac) {
		this.talla_nac = talla_nac;
	}

	/**
	* Obtiene Edad a la que levanto su cabeza 
	* @return nu_edad_cabe
	*/
	public String getNu_edad_cabe(){
		return nu_edad_cabe;
	}	

	/**
	* Edad a la que levanto su cabeza 
	* @param nu_edad_cabe
	*/
	public void setNu_edad_cabe(String nu_edad_cabe) {
		this.nu_edad_cabe = nu_edad_cabe;
	}

	/**
	* Obtiene Edad a la edad que se paro 
	* @return nu_edad_paro
	*/
	public String getNu_edad_paro(){
		return nu_edad_paro;
	}	

	/**
	* Edad a la edad que se paro 
	* @param nu_edad_paro
	*/
	public void setNu_edad_paro(String nu_edad_paro) {
		this.nu_edad_paro = nu_edad_paro;
	}

	/**
	* Obtiene Edad a la que camino 
	* @return nu_edad_cami
	*/
	public String getNu_edad_cami(){
		return nu_edad_cami;
	}	

	/**
	* Edad a la que camino 
	* @param nu_edad_cami
	*/
	public void setNu_edad_cami(String nu_edad_cami) {
		this.nu_edad_cami = nu_edad_cami;
	}

	public Alumno getAlumno(){
		return alumno;
	}	

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}
}
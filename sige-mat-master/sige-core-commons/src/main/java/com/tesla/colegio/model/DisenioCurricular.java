package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_disenio_curricular
 * @author MV
 *
 */
public class DisenioCurricular extends EntidadBase{

	public final static String TABLA = "col_disenio_curricular";
	private Integer id;
	private Integer id_anio;
	private String nom;
	private Anio anio;	
	private List<DcnNivel> dcnnivels;

	public DisenioCurricular(){
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
	* Obtiene Anio 
	* @return id_anio
	*/
	public Integer getId_anio(){
		return id_anio;
	}	

	/**
	* Anio 
	* @param id_anio
	*/
	public void setId_anio(Integer id_anio) {
		this.id_anio = id_anio;
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

	public Anio getAnio(){
		return anio;
	}	

	public void setAnio(Anio anio) {
		this.anio = anio;
	}
	/**
	* Obtiene lista de Disenio Curricular Nivel 
	*/
	public List<DcnNivel> getDcnNivels() {
		return dcnnivels;
	}

	/**
	* Seta Lista de Disenio Curricular Nivel 
	* @param dcnnivels
	*/	
	public void setDcnNivel(List<DcnNivel> dcnnivels) {
		this.dcnnivels = dcnnivels;
	}
}
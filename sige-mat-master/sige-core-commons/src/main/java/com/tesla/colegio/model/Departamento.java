package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_departamento
 * @author MV
 *
 */
public class Departamento extends EntidadBase{

	public final static String TABLA = "cat_departamento";
	private Integer id;
	private String nom;
	private List<Provincia> provincias;

	public Departamento(){
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
	* Obtiene Departamento 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Departamento 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene lista de Provincia (Huaraz, Aija, Antonio Raimondy, etc) 
	*/
	public List<Provincia> getProvincias() {
		return provincias;
	}

	/**
	* Seta Lista de Provincia (Huaraz, Aija, Antonio Raimondy, etc) 
	* @param provincias
	*/	
	public void setProvincia(List<Provincia> provincias) {
		this.provincias = provincias;
	}
}
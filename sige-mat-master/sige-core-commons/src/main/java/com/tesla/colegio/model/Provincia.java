package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_provincia
 * @author MV
 *
 */
public class Provincia extends EntidadBase{

	public final static String TABLA = "cat_provincia";
	private Integer id;
	private String nom;
	private Integer id_dep;
	private Departamento departamento;	
	private List<Distrito> distritos;

	public Provincia(){
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
	* Obtiene Provincia 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Provincia 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Departamento 
	* @return id_dep
	*/
	public Integer getId_dep(){
		return id_dep;
	}	

	/**
	* Departamento 
	* @param id_dep
	*/
	public void setId_dep(Integer id_dep) {
		this.id_dep = id_dep;
	}

	public Departamento getDepartamento(){
		return departamento;
	}	

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}
	/**
	* Obtiene lista de Distrito (Huaraz, Independencia,etc) 
	*/
	public List<Distrito> getDistritos() {
		return distritos;
	}

	/**
	* Seta Lista de Distrito (Huaraz, Independencia,etc) 
	* @param distritos
	*/	
	public void setDistrito(List<Distrito> distritos) {
		this.distritos = distritos;
	}
}
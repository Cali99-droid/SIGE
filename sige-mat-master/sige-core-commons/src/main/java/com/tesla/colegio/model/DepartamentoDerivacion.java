package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla con_departamento_derivacion
 * @author MV
 *
 */
public class DepartamentoDerivacion extends EntidadBase{

	public final static String TABLA = "con_departamento_derivacion";
	private Integer id;
	private Integer id_ctd;
	private String nom;
	private List<FichaDerivacion> fichaderivacions;

	public DepartamentoDerivacion(){
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
	* Obtiene Tipo de Derivacin 
	* @return id_ctd
	*/
	public Integer getId_ctd(){
		return id_ctd;
	}	

	/**
	* Tipo de Derivacin 
	* @param id_ctd
	*/
	public void setId_ctd(Integer id_ctd) {
		this.id_ctd = id_ctd;
	}

	/**
	* Obtiene Nombre de Depatrtamento 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre de Depatrtamento 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene lista de Ficha Derivacion 
	*/
	public List<FichaDerivacion> getFichaDerivacions() {
		return fichaderivacions;
	}

	/**
	* Seta Lista de Ficha Derivacion 
	* @param fichaderivacions
	*/	
	public void setFichaDerivacion(List<FichaDerivacion> fichaderivacions) {
		this.fichaderivacions = fichaderivacions;
	}
}
package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_concepto_pago
 * @author MV
 *
 */
public class ConceptoPago extends EntidadBase{

	public final static String TABLA = "cat_concepto_pago";
	private Integer id;
	private String nom;
	private List<PagoProgramacion> pagoprogramacions;

	public ConceptoPago(){
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
	* Obtiene Concepto de pago 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Concepto de pago 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene lista de Programacin de Pagos por Matricula del alumno 
	*/
	public List<PagoProgramacion> getPagoProgramacions() {
		return pagoprogramacions;
	}

	/**
	* Seta Lista de Programacin de Pagos por Matricula del alumno 
	* @param pagoprogramacions
	*/	
	public void setPagoProgramacion(List<PagoProgramacion> pagoprogramacions) {
		this.pagoprogramacions = pagoprogramacions;
	}
}
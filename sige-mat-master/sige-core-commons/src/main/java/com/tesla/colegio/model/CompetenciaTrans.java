package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_competencia_trans
 * @author MV
 *
 */
public class CompetenciaTrans extends EntidadBase{

	public final static String TABLA = "cat_competencia_trans";
	private Integer id;
	private String nom;
	private List<DcnCompTrans> dcncomptranss;

	public CompetenciaTrans(){
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
	* Obtiene Competencia Transversal 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Competencia Transversal 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene lista de Competencia Transversal 
	*/
	public List<DcnCompTrans> getDcnCompTranss() {
		return dcncomptranss;
	}

	/**
	* Seta Lista de Competencia Transversal 
	* @param dcncomptranss
	*/	
	public void setDcnCompTrans(List<DcnCompTrans> dcncomptranss) {
		this.dcncomptranss = dcncomptranss;
	}
}
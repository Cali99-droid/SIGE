package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_cond_alumno
 * @author MV
 *
 */
public class CondAlumno extends EntidadBase{

	public final static String TABLA = "cat_cond_alumno";
	private Integer id;
	private Integer id_ctc;
	private String nom;
	private String des;
	private TipCond tipcond;	
	private List<Condicion> condicions;

	public CondAlumno(){
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
	* Obtiene Tipo de Condicion 
	* @return id_ctc
	*/
	public Integer getId_ctc(){
		return id_ctc;
	}	

	/**
	* Tipo de Condicion 
	* @param id_ctc
	*/
	public void setId_ctc(Integer id_ctc) {
		this.id_ctc = id_ctc;
	}

	/**
	* Obtiene Condicion 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Condicion 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Descripcin 
	* @return des
	*/
	public String getDes(){
		return des;
	}	

	/**
	* Descripcin 
	* @param des
	*/
	public void setDes(String des) {
		this.des = des;
	}

	public TipCond getTipCond(){
		return tipcond;
	}	

	public void setTipCond(TipCond tipcond) {
		this.tipcond = tipcond;
	}
	/**
	* Obtiene lista de Matrcula Condicin Alumno 
	*/
	public List<Condicion> getCondicions() {
		return condicions;
	}

	/**
	* Seta Lista de Matrcula Condicin Alumno 
	* @param condicions
	*/	
	public void setCondicion(List<Condicion> condicions) {
		this.condicions = condicions;
	}
}
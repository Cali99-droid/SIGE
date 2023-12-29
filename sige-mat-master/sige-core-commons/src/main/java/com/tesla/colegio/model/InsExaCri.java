package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla eva_ins_exa_cri
 * @author MV
 *
 */
public class InsExaCri extends EntidadBase{

	public final static String TABLA = "eva_ins_exa_cri";
	private Integer id;
	private Integer id_excri;
	private Integer id_ins;
	private ExaConfCriterio exaconfcriterio;	
	private Instrumento instrumento;	

	public InsExaCri(){
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
	* Obtiene Examen Criterio 
	* @return id_excri
	*/
	public Integer getId_excri(){
		return id_excri;
	}	

	/**
	* Examen Criterio 
	* @param id_excri
	*/
	public void setId_excri(Integer id_excri) {
		this.id_excri = id_excri;
	}

	/**
	* Obtiene Instrumento 
	* @return id_ins
	*/
	public Integer getId_ins(){
		return id_ins;
	}	

	/**
	* Instrumento 
	* @param id_ins
	*/
	public void setId_ins(Integer id_ins) {
		this.id_ins = id_ins;
	}

	public ExaConfCriterio getExaConfCriterio(){
		return exaconfcriterio;
	}	

	public void setExaConfCriterio(ExaConfCriterio exaconfcriterio) {
		this.exaconfcriterio = exaconfcriterio;
	}
	public Instrumento getInstrumento(){
		return instrumento;
	}	

	public void setInstrumento(Instrumento instrumento) {
		this.instrumento = instrumento;
	}
}
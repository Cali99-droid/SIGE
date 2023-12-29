package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_preg_dependencia
 * @author MV
 *
 */
public class PregDependencia extends EntidadBase{

	public final static String TABLA = "col_preg_dependencia";
	private Integer id;
	private Integer id_enc_pre;
	private Integer id_pre_dep;
	private Integer id_alt;
	private EncuestaPreg encuestapreg;	

	public PregDependencia(){
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
	* Obtiene Pregunta 
	* @return id_enc_pre
	*/
	public Integer getId_enc_pre(){
		return id_enc_pre;
	}	

	/**
	* Pregunta 
	* @param id_enc_pre
	*/
	public void setId_enc_pre(Integer id_enc_pre) {
		this.id_enc_pre = id_enc_pre;
	}

	/**
	* Obtiene Pregunta Dependencia 
	* @return id_pre_dep
	*/
	public Integer getId_pre_dep(){
		return id_pre_dep;
	}	

	/**
	* Pregunta Dependencia 
	* @param id_pre_dep
	*/
	public void setId_pre_dep(Integer id_pre_dep) {
		this.id_pre_dep = id_pre_dep;
	}

	public EncuestaPreg getEncuestaPreg(){
		return encuestapreg;
	}	

	public void setEncuestaPreg(EncuestaPreg encuestapreg) {
		this.encuestapreg = encuestapreg;
	}

	public Integer getId_alt() {
		return id_alt;
	}

	public void setId_alt(Integer id_alt) {
		this.id_alt = id_alt;
	}
}
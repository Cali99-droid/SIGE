package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla eva_exa_conf_marcacion
 * @author MV
 *
 */
public class ExaConfMarcacion extends EntidadBase{

	public final static String TABLA = "eva_exa_conf_marcacion";
	private Integer id;
	private Integer id_eva_ex;
	private Integer num_pre;
	private java.math.BigDecimal pje_pre_cor;
	private java.math.BigDecimal pje_pre_inc;

	public ExaConfMarcacion(){
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
	* Obtiene Evaluacin Examen 
	* @return id_eva_ex
	*/
	public Integer getId_eva_ex(){
		return id_eva_ex;
	}	

	/**
	* Evaluacin Examen 
	* @param id_eva_ex
	*/
	public void setId_eva_ex(Integer id_eva_ex) {
		this.id_eva_ex = id_eva_ex;
	}

	/**
	* Obtiene Nmero de Preguntas 
	* @return num_pre
	*/
	public Integer getNum_pre(){
		return num_pre;
	}	

	/**
	* Nmero de Preguntas 
	* @param num_pre
	*/
	public void setNum_pre(Integer num_pre) {
		this.num_pre = num_pre;
	}

	/**
	* Obtiene Puntaje de la pregunta correcta 
	* @return pje_pre_cor
	*/
	public java.math.BigDecimal getPje_pre_cor(){
		return pje_pre_cor;
	}	

	/**
	* Puntaje de la pregunta correcta 
	* @param pje_pre_cor
	*/
	public void setPje_pre_cor(java.math.BigDecimal pje_pre_cor) {
		this.pje_pre_cor = pje_pre_cor;
	}

	/**
	* Obtiene Puntaje de la pregunta incorrecta 
	* @return pje_pre_inc
	*/
	public java.math.BigDecimal getPje_pre_inc(){
		return pje_pre_inc;
	}	

	/**
	* Puntaje de la pregunta incorrecta 
	* @param pje_pre_inc
	*/
	public void setPje_pre_inc(java.math.BigDecimal pje_pre_inc) {
		this.pje_pre_inc = pje_pre_inc;
	}

}
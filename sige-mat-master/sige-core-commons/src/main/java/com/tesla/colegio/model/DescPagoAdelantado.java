package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla mat_desc_pago_adelantado
 * @author MV
 *
 */
public class DescPagoAdelantado extends EntidadBase{

	public final static String TABLA = "mat_desc_pago_adelantado";
	private Integer id;
	private Integer id_per;
	private java.math.BigDecimal desc_dic;
	private Periodo periodo;	

	public DescPagoAdelantado(){
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
	* Obtiene Periodo Acadmico 
	* @return id_per
	*/
	public Integer getId_per(){
		return id_per;
	}	

	/**
	* Periodo Acadmico 
	* @param id_per
	*/
	public void setId_per(Integer id_per) {
		this.id_per = id_per;
	}

	/**
	* Obtiene Monto 
	* @return desc_dic
	*/
	public java.math.BigDecimal getDesc_dic(){
		return desc_dic;
	}	

	/**
	* Monto 
	* @param desc_dic
	*/
	public void setDesc_dic(java.math.BigDecimal desc_dic) {
		this.desc_dic = desc_dic;
	}

	public Periodo getPeriodo(){
		return periodo;
	}	

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
}
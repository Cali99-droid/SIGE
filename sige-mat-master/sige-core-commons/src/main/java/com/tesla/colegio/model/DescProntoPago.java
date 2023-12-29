package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla mat_desc_pronto_pago
 * @author MV
 *
 */
public class DescProntoPago extends EntidadBase{

	public final static String TABLA = "mat_desc_pronto_pago";
	private Integer id;
	private Integer id_per;
	private java.math.BigDecimal banco;
	private java.math.BigDecimal secretaria;
	private Periodo periodo;	

	public DescProntoPago(){
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
	* @return banco
	*/
	public java.math.BigDecimal getBanco(){
		return banco;
	}	

	/**
	* Monto 
	* @param banco
	*/
	public void setBanco(java.math.BigDecimal banco) {
		this.banco = banco;
	}

	/**
	* Obtiene Monto 
	* @return secretaria
	*/
	public java.math.BigDecimal getSecretaria(){
		return secretaria;
	}	

	/**
	* Monto 
	* @param secretaria
	*/
	public void setSecretaria(java.math.BigDecimal secretaria) {
		this.secretaria = secretaria;
	}

	public Periodo getPeriodo(){
		return periodo;
	}	

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
}
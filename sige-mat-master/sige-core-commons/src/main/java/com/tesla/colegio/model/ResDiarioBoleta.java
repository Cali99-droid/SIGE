package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla fac_res_diario_boleta
 * @author MV
 *
 */
public class ResDiarioBoleta extends EntidadBase{

	public final static String TABLA = "fac_res_diario_boleta";
	private Integer id;
	private String numero;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_emi;
	private String cant_reg;
	private String estado;

	public ResDiarioBoleta(){
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
	* Obtiene Numero 
	* @return numero
	*/
	public String getNumero(){
		return numero;
	}	

	/**
	* Numero 
	* @param numero
	*/
	public void setNumero(String numero) {
		this.numero = numero;
	}

	/**
	* Obtiene Fecha Emision 
	* @return fec_emi
	*/
	public java.util.Date getFec_emi(){
		return fec_emi;
	}	

	/**
	* Fecha Emision 
	* @param fec_emi
	*/
	public void setFec_emi(java.util.Date fec_emi) {
		this.fec_emi = fec_emi;
	}

	/**
	* Obtiene Cantidad de Registros Procesados 
	* @return cant_reg
	*/
	public String getCant_reg(){
		return cant_reg;
	}	

	/**
	* Cantidad de Registros Procesados 
	* @param cant_reg
	*/
	public void setCant_reg(String cant_reg) {
		this.cant_reg = cant_reg;
	}

	/**
	* Obtiene Estado 
	* @return estado
	*/
	public String getEstado(){
		return estado;
	}	

	/**
	* Estado 
	* @param estado
	*/
	public void setEstado(String estado) {
		this.estado = estado;
	}

}
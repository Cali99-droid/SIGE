package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla fac_conf_pagos_ciclo_cuota
 * @author MV
 *
 */
public class ConfPagosCicloCuota extends EntidadBase{

	public final static String TABLA = "fac_conf_pagos_ciclo_cuota";
	private Integer id;
	private Integer id_cfpav;
	private Integer nro_cuota;
	private java.math.BigDecimal costo;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_venc;
	private ConfPagosCiclo confpagosciclo;	

	public ConfPagosCicloCuota(){
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
	* Obtiene Conficguracin de Pagos Ciclo 
	* @return id_cfpav
	*/
	public Integer getId_cfpav(){
		return id_cfpav;
	}	

	/**
	* Conficguracin de Pagos Ciclo 
	* @param id_cfpav
	*/
	public void setId_cfpav(Integer id_cfpav) {
		this.id_cfpav = id_cfpav;
	}

	/**
	* Obtiene Nmero de Cuota 
	* @return nro_cuota
	*/
	public Integer getNro_cuota(){
		return nro_cuota;
	}	

	/**
	* Nmero de Cuota 
	* @param nro_cuota
	*/
	public void setNro_cuota(Integer nro_cuota) {
		this.nro_cuota = nro_cuota;
	}

	/**
	* Obtiene Costo 
	* @return costo
	*/
	public java.math.BigDecimal getCosto(){
		return costo;
	}	

	/**
	* Costo 
	* @param costo
	*/
	public void setCosto(java.math.BigDecimal costo) {
		this.costo = costo;
	}

	/**
	* Obtiene Fecha de Vencimiento 
	* @return fec_venc
	*/
	public java.util.Date getFec_venc(){
		return fec_venc;
	}	

	/**
	* Fecha de Vencimiento 
	* @param fec_venc
	*/
	public void setFec_venc(java.util.Date fec_venc) {
		this.fec_venc = fec_venc;
	}

	public ConfPagosCiclo getConfPagosCiclo(){
		return confpagosciclo;
	}	

	public void setConfPagosCiclo(ConfPagosCiclo confpagosciclo) {
		this.confpagosciclo = confpagosciclo;
	}
}
package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla fac_descuento_cuota
 * @author MV
 *
 */
public class DescuentoCuota extends EntidadBase{

	public final static String TABLA = "fac_descuento_cuota";
	private Integer id;
	private Integer id_fdes;
	private Integer id_fcuo;
	private Integer nro_cuota;
	private DescuentoConf descuentoconf;	

	public DescuentoCuota(){
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
	* Obtiene Descuento 
	* @return id_fdes
	*/
	public Integer getId_fdes(){
		return id_fdes;
	}	

	/**
	* Descuento 
	* @param id_fdes
	*/
	public void setId_fdes(Integer id_fdes) {
		this.id_fdes = id_fdes;
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

	public DescuentoConf getDescuentoConf(){
		return descuentoconf;
	}	

	public void setDescuentoConf(DescuentoConf descuentoconf) {
		this.descuentoconf = descuentoconf;
	}

	public Integer getId_fcuo() {
		return id_fcuo;
	}

	public void setId_fcuo(Integer id_fcuo) {
		this.id_fcuo = id_fcuo;
	}
}
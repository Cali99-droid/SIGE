package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla fac_conf_recibo
 * @author MV
 *
 */
public class ConfRecibo extends EntidadBase{

	public final static String TABLA = "fac_conf_recibo";
	private Integer id;
	private Integer id_suc;
	private String tipo;
	private String serie;
	private Integer numero;
	private Integer hasta;
	private Sucursal sucursal;	
	private Integer numero_nc;

	public ConfRecibo(){
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
	* Obtiene Local 
	* @return id_suc
	*/
	public Integer getId_suc(){
		return id_suc;
	}	

	/**
	* Local 
	* @param id_suc
	*/
	public void setId_suc(Integer id_suc) {
		this.id_suc = id_suc;
	}

	/**
	* Obtiene Fisico o electronico 
	* @return tipo
	*/
	public String getTipo(){
		return tipo;
	}	

	/**
	* Fisico o electronico 
	* @param tipo
	*/
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	* Obtiene Serie 
	* @return serie
	*/
	public String getSerie(){
		return serie;
	}	

	/**
	* Serie 
	* @param serie
	*/
	public void setSerie(String serie) {
		this.serie = serie;
	}

	/**
	* Obtiene Nro de recibo 
	* @return numero
	*/
	public Integer getNumero(){
		return numero;
	}	

	/**
	* Nro de recibo 
	* @param numero
	*/
	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	/**
	* Obtiene Nro maximo de recibo 
	* @return hasta
	*/
	public Integer getHasta(){
		return hasta;
	}	

	/**
	* Nro maximo de recibo 
	* @param hasta
	*/
	public void setHasta(Integer hasta) {
		this.hasta = hasta;
	}

	public Sucursal getSucursal(){
		return sucursal;
	}	

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	
	public String getTipoDesc(){
		if ("E".equals(tipo))
			return "Electrnico";
		else
			return "Fsico";
	}

	public Integer getNumero_nc() {
		return numero_nc;
	}

	public void setNumero_nc(Integer numero_nc) {
		this.numero_nc = numero_nc;
	}	
	
}
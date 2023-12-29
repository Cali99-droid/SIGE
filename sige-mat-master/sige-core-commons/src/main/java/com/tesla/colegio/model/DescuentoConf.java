package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla fac_descuento_conf
 * @author MV
 *
 */
public class DescuentoConf extends EntidadBase{

	public final static String TABLA = "fac_descuento_conf";
	private Integer id;
	private Integer id_cct;
	private Integer id_des;
	private Integer nro_cuota_max;
	private String nom;
	private String monto;
	private String venc;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_venc;
	private String acu;
	private Ciclo ciclo;	
	
	private List<DescuentoCuota> descuentoCuotas;

	public DescuentoConf(){
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
	* Obtiene Ciclo 
	* @return id_cct
	*/
	public Integer getId_cct(){
		return id_cct;
	}	

	/**
	* Ciclo 
	* @param id_cct
	*/
	public void setId_cct(Integer id_cct) {
		this.id_cct = id_cct;
	}

	/**
	* Obtiene Vencimiento 
	* @return venc
	*/
	public String getVenc(){
		return venc;
	}	

	/**
	* Vencimiento 
	* @param venc
	*/
	public void setVenc(String venc) {
		this.venc = venc;
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

	/**
	* Obtiene Acumulable 
	* @return acu
	*/
	public String getAcu(){
		return acu;
	}	

	/**
	* Acumulable 
	* @param acu
	*/
	public void setAcu(String acu) {
		this.acu = acu;
	}

	public Ciclo getCiclo(){
		return ciclo;
	}	

	public void setCiclo(Ciclo ciclo) {
		this.ciclo = ciclo;
	}

	public List<DescuentoCuota> getDescuentoCuotas() {
		return descuentoCuotas;
	}

	public void setDescuentoCuotas(List<DescuentoCuota> descuentoCuotas) {
		this.descuentoCuotas = descuentoCuotas;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getMonto() {
		return monto;
	}

	public void setMonto(String monto) {
		this.monto = monto;
	}

	public Integer getNro_cuota_max() {
		return nro_cuota_max;
	}

	public void setNro_cuota_max(Integer nro_cuota_max) {
		this.nro_cuota_max = nro_cuota_max;
	}

	public Integer getId_des() {
		return id_des;
	}

	public void setId_des(Integer id_des) {
		this.id_des = id_des;
	}
}
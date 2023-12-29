package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla fac_comunicacion_baja
 * @author MV
 *
 */
public class ComunicacionBaja extends EntidadBase{

	public final static String TABLA = "fac_comunicacion_baja";
	private Integer id;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_emi;
	private Integer id_fmo;
	private String motivo;
	private Movimiento movimiento;	

	public ComunicacionBaja(){
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
	* Obtiene Documento de baja 
	* @return id_fmo
	*/
	public Integer getId_fmo(){
		return id_fmo;
	}	

	/**
	* Documento de baja 
	* @param id_fmo
	*/
	public void setId_fmo(Integer id_fmo) {
		this.id_fmo = id_fmo;
	}

	/**
	* Obtiene  
	* @return motivo
	*/
	public String getMotivo(){
		return motivo;
	}	

	/**
	*  
	* @param motivo
	*/
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Movimiento getMovimiento(){
		return movimiento;
	}	

	public void setMovimiento(Movimiento movimiento) {
		this.movimiento = movimiento;
	}
}
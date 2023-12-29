package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla fac_nota_credito
 * @author MV
 *
 */
public class NotaCredito extends EntidadBase{

	public final static String TABLA = "fac_nota_credito";
	private Integer id;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_emi;
	private Integer id_fmo;
	private Integer id_fmo_nc;
	private String motivo;
	private java.math.BigDecimal monto;
	private String ticket;
	private String id_eiv;
	private String code;
	private Movimiento movimiento;	

	public NotaCredito(){
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
	* Obtiene Afecta boleta 
	* @return id_fmo
	*/
	public Integer getId_fmo(){
		return id_fmo;
	}	

	/**
	* Afecta boleta 
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

	/**
	* Obtiene Monto 
	* @return monto
	*/
	public java.math.BigDecimal getMonto(){
		return monto;
	}	

	/**
	* Monto 
	* @param monto
	*/
	public void setMonto(java.math.BigDecimal monto) {
		this.monto = monto;
	}

	/**
	* Obtiene Ticket 
	* @return ticket
	*/
	public String getTicket(){
		return ticket;
	}	

	/**
	* Ticket 
	* @param ticket
	*/
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	/**
	* Obtiene id_eiv 
	* @return id_eiv
	*/
	public String getId_eiv(){
		return id_eiv;
	}	

	/**
	* id_eiv 
	* @param id_eiv
	*/
	public void setId_eiv(String id_eiv) {
		this.id_eiv = id_eiv;
	}

	/**
	* Obtiene code 
	* @return code
	*/
	public String getCode(){
		return code;
	}	

	/**
	* code 
	* @param code
	*/
	public void setCode(String code) {
		this.code = code;
	}

	public Movimiento getMovimiento(){
		return movimiento;
	}	

	public void setMovimiento(Movimiento movimiento) {
		this.movimiento = movimiento;
	}

	public Integer getId_fmo_nc() {
		return id_fmo_nc;
	}

	public void setId_fmo_nc(Integer id_fmo_nc) {
		this.id_fmo_nc = id_fmo_nc;
	}
}
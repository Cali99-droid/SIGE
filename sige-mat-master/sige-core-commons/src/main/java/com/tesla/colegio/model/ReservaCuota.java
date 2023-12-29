package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla fac_reserva_cuota
 * @author MV
 *
 */
public class ReservaCuota extends EntidadBase{

	public final static String TABLA = "fac_reserva_cuota";
	private Integer id;
	private Integer id_res;
	private java.math.BigDecimal monto;
	private Reserva reserva;	
	private Integer id_fmo;
	
	//YA NO SE USARA
	private String nro_recibo;
	
	public ReservaCuota(){
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
	* Obtiene Reserva 
	* @return id_res
	*/
	public Integer getId_res(){
		return id_res;
	}	

	/**
	* Reserva 
	* @param id_res
	*/
	public void setId_res(Integer id_res) {
		this.id_res = id_res;
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
	* Obtiene Nro de recibo 
	* @return nro_recibo
	*/
	public String getNro_recibo(){
		return nro_recibo;
	}	

	/**
	* Nro de recibo 
	* @param nro_recibo
	*/
	public void setNro_recibo(String nro_recibo) {
		this.nro_recibo = nro_recibo;
	}

	public Reserva getReserva(){
		return reserva;
	}	

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}

	public Integer getId_fmo() {
		return id_fmo;
	}

	public void setId_fmo(Integer id_fmo) {
		this.id_fmo = id_fmo;
	}
	
}
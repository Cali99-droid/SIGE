package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla pag_rec
 * @author MV
 *
 */
public class Rec extends EntidadBase{

	public final static String TABLA = "pag_rec";
	private Integer id;
	private Integer id_pag;
	private String num_rec;
	private String loc;
	private String tipo;
	private Pago pago;

	public Rec(){
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
	* Obtiene Pago Realizado 
	* @return id_pag
	*/
	public Integer getId_pag(){
		return id_pag;
	}	

	/**
	* Pago Realizado 
	* @param id_pag
	*/
	public void setId_pag(Integer id_pag) {
		this.id_pag = id_pag;
	}

	/**
	* Obtiene Nmero de Recibo 
	* @return num_rec
	*/
	public String getNum_rec(){
		return num_rec;
	}	

	/**
	* Nmero de Recibo 
	* @param num_rec
	*/
	public void setNum_rec(String num_rec) {
		this.num_rec = num_rec;
	}

	/**
	* Obtiene Local 
	* @return loc
	*/
	public String getLoc(){
		return loc;
	}	

	/**
	* Local 
	* @param loc
	*/
	public void setLoc(String loc) {
		this.loc = loc;
	}

	/**
	* Obtiene Tipo(C,V,A) 
	* @return tipo
	*/
	public String getTipo(){
		return tipo;
	}	

	/**
	* Tipo(C,V,A) 
	* @param tipo
	*/
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	public Pago getPago(){
		return pago;
	}	

	public void setPago(Pago pago) {
		this.pago = pago;
	}
}
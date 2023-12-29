package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_capacidad_setup
 * @author MV
 *
 */
public class CapacidadSetup extends EntidadBase{

	public final static String TABLA = "col_capacidad_setup";
	private Integer id;
	private Integer id_per;
	private Integer id_grad;
	private Integer cant;
	private Periodo periodo;	
	private Grad grad;	

	public CapacidadSetup(){
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
	* Obtiene Grado 
	* @return id_grad
	*/
	public Integer getId_grad(){
		return id_grad;
	}	

	/**
	* Grado 
	* @param id_grad
	*/
	public void setId_grad(Integer id_grad) {
		this.id_grad = id_grad;
	}

	/**
	* Obtiene Cantidad de vacantes 
	* @return cant
	*/
	public Integer getCant(){
		return cant;
	}	

	/**
	* Cantidad de vacantes 
	* @param cant
	*/
	public void setCant(Integer cant) {
		this.cant = cant;
	}

	public Periodo getPeriodo(){
		return periodo;
	}	

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
	public Grad getGrad(){
		return grad;
	}	

	public void setGrad(Grad grad) {
		this.grad = grad;
	}
}
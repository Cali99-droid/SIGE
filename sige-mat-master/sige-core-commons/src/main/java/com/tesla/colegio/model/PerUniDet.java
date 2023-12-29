package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_per_uni_det
 * @author MV
 *
 */
public class PerUniDet extends EntidadBase{

	public final static String TABLA = "col_per_uni_det";
	private Integer id;
	private Integer id_cpu;
	private Integer ord;
	private Integer nro_sem;
	private PerUni peruni;	

	public PerUniDet(){
	}

	/**
	* Obtiene Llave principal 
	* @return id
	*/
	public Integer getId(){
		return id;
	}	

	/**
	* Llave principal 
	* @param id
	*/
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	* Obtiene Periodo Unidad 
	* @return id_cpu
	*/
	public Integer getId_cpu(){
		return id_cpu;
	}	

	/**
	* Periodo Unidad 
	* @param id_cpu
	*/
	public void setId_cpu(Integer id_cpu) {
		this.id_cpu = id_cpu;
	}

	/**
	* Obtiene Orden 
	* @return ord
	*/
	public Integer getOrd(){
		return ord;
	}	

	/**
	* Orden 
	* @param ord
	*/
	public void setOrd(Integer ord) {
		this.ord = ord;
	}

	/**
	* Obtiene Numero de Semana 
	* @return nro_sem
	*/
	public Integer getNro_sem(){
		return nro_sem;
	}	

	/**
	* Numero de Semana 
	* @param nro_sem
	*/
	public void setNro_sem(Integer nro_sem) {
		this.nro_sem = nro_sem;
	}

	public PerUni getPerUni(){
		return peruni;
	}	

	public void setPerUni(PerUni peruni) {
		this.peruni = peruni;
	}
}
package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla con_unidad_trabajador
 * @author MV
 *
 */
public class UnidadTrabajador extends EntidadBase{

	public final static String TABLA = "con_unidad_trabajador";
	private Integer id;
	private Integer id_tra;
	private Integer id_uni;
	private String flg_descarga;
	private Trabajador trabajador;	
	private CursoUnidad cursounidad;	

	public UnidadTrabajador(){
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
	* Obtiene Trabajador 
	* @return id_tra
	*/
	public Integer getId_tra(){
		return id_tra;
	}	

	/**
	* Trabajador 
	* @param id_tra
	*/
	public void setId_tra(Integer id_tra) {
		this.id_tra = id_tra;
	}

	/**
	* Obtiene Curso Unidad 
	* @return id_uni
	*/
	public Integer getId_uni(){
		return id_uni;
	}	

	/**
	* Curso Unidad 
	* @param id_uni
	*/
	public void setId_uni(Integer id_uni) {
		this.id_uni = id_uni;
	}

	/**
	* Obtiene Flag Descarga Curso Unidad 
	* @return flg_descarga
	*/
	public String getFlg_descarga(){
		return flg_descarga;
	}	

	/**
	* Flag Descarga Curso Unidad 
	* @param flg_descarga
	*/
	public void setFlg_descarga(String flg_descarga) {
		this.flg_descarga = flg_descarga;
	}

	public Trabajador getTrabajador(){
		return trabajador;
	}	

	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
	}
	public CursoUnidad getCursoUnidad(){
		return cursounidad;
	}	

	public void setCursoUnidad(CursoUnidad cursounidad) {
		this.cursounidad = cursounidad;
	}
}
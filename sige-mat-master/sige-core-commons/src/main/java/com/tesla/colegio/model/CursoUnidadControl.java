package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_curso_unidad_control
 * @author MV
 *
 */
public class CursoUnidadControl extends EntidadBase{

	public final static String TABLA = "col_curso_unidad_control";
	private Integer id;
	private byte[] pdf;
	private Integer id_uni;
	private Integer id_tra;
	private CursoUnidad cursounidad;	
	private Trabajador trabajador;	

	public CursoUnidadControl(){
	}

	/**
	* Obtiene $field.description 
	* @return id
	*/
	public Integer getId(){
		return id;
	}	

	/**
	* $field.description 
	* @param id
	*/
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	* Obtiene PDF generado 
	* @return pdf
	*/
	public byte[] getPdf(){
		return pdf;
	}	

	/**
	* PDF generado 
	* @param pdf
	*/
	public void setPdf(byte[] pdf) {
		this.pdf = pdf;
	}

	/**
	* Obtiene Unidad 
	* @return id_uni
	*/
	public Integer getId_uni(){
		return id_uni;
	}	

	/**
	* Unidad 
	* @param id_uni
	*/
	public void setId_uni(Integer id_uni) {
		this.id_uni = id_uni;
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

	public CursoUnidad getCursoUnidad(){
		return cursounidad;
	}	

	public void setCursoUnidad(CursoUnidad cursounidad) {
		this.cursounidad = cursounidad;
	}
	public Trabajador getTrabajador(){
		return trabajador;
	}	

	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
	}
	
}
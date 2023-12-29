package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla ges_his_grado_ins
 * @author MV
 *
 */
public class HisGradoIns extends EntidadBase{

	public final static String TABLA = "ges_his_grado_ins";
	private Integer id;
	private Integer id_tra;
	private Integer id_gin;
	private String carrera;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_egre;
	private Trabajador trabajador;	
	private GradoInstruccion gradoinstruccion;	

	public HisGradoIns(){
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
	* Obtiene Grado de instruccin 
	* @return id_gin
	*/
	public Integer getId_gin(){
		return id_gin;
	}	

	/**
	* Grado de instruccin 
	* @param id_gin
	*/
	public void setId_gin(Integer id_gin) {
		this.id_gin = id_gin;
	}

	/**
	* Obtiene Carrera 
	* @return carrera
	*/
	public String getCarrera(){
		return carrera;
	}	

	/**
	* Carrera 
	* @param carrera
	*/
	public void setCarrera(String carrera) {
		this.carrera = carrera;
	}

	/**
	* Obtiene Fecha Egreso 
	* @return fec_egre
	*/
	public java.util.Date getFec_egre(){
		return fec_egre;
	}	

	/**
	* Fecha Egreso 
	* @param fec_egre
	*/
	public void setFec_egre(java.util.Date fec_egre) {
		this.fec_egre = fec_egre;
	}

	public Trabajador getTrabajador(){
		return trabajador;
	}	

	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
	}
	public GradoInstruccion getGradoInstruccion(){
		return gradoinstruccion;
	}	

	public void setGradoInstruccion(GradoInstruccion gradoinstruccion) {
		this.gradoinstruccion = gradoinstruccion;
	}
}
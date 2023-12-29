package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla col_situacion_mat
 * @author MV
 *
 */
public class SituacionMat extends EntidadBase{

	public final static String TABLA = "col_situacion_mat";
	private Integer id;
	private Integer id_mat;
	private Integer id_sit;
	private String mot;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec;
	private Matricula matricula;	
	private ColSituacion colsituacion;	

	public SituacionMat(){
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
	* Obtiene Matricula 
	* @return id_mat
	*/
	public Integer getId_mat(){
		return id_mat;
	}	

	/**
	* Matricula 
	* @param id_mat
	*/
	public void setId_mat(Integer id_mat) {
		this.id_mat = id_mat;
	}

	/**
	* Obtiene Situacin 
	* @return id_sit
	*/
	public Integer getId_sit(){
		return id_sit;
	}	

	/**
	* Situacin 
	* @param id_sit
	*/
	public void setId_sit(Integer id_sit) {
		this.id_sit = id_sit;
	}

	/**
	* Obtiene Motivo 
	* @return mot
	*/
	public String getMot(){
		return mot;
	}	

	/**
	* Motivo 
	* @param mot
	*/
	public void setMot(String mot) {
		this.mot = mot;
	}

	/**
	* Obtiene Fecha 
	* @return fec
	*/
	public java.util.Date getFec(){
		return fec;
	}	

	/**
	* Fecha 
	* @param fec
	*/
	public void setFec(java.util.Date fec) {
		this.fec = fec;
	}

	public Matricula getMatricula(){
		return matricula;
	}	

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}
	public ColSituacion getColSituacion(){
		return colsituacion;
	}	

	public void setColSituacion(ColSituacion colsituacion) {
		this.colsituacion = colsituacion;
	}
}
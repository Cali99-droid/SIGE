package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla mat_traslado_detalle
 * @author MV
 *
 */
public class TrasladoDetalle extends EntidadBase{

	public final static String TABLA = "mat_traslado_detalle";
	private Integer id;
	private Integer id_mat;
	private Integer id_sit;
	private Integer id_col;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec;
	private String mot;
	private Matricula matricula;	
	private ColSituacion colsituacion;	
	private Colegio colegio;
	private Condicion condicion;

	public TrasladoDetalle(){
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
	* Obtiene Colegio Destino 
	* @return id_col
	*/
	public Integer getId_col(){
		return id_col;
	}	

	/**
	* Colegio Destino 
	* @param id_col
	*/
	public void setId_col(Integer id_col) {
		this.id_col = id_col;
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

	/**
	* Obtiene Motivo del traslado 
	* @return mot
	*/
	public String getMot(){
		return mot;
	}	

	/**
	* Motivo del traslado 
	* @param mot
	*/
	public void setMot(String mot) {
		this.mot = mot;
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
	public Colegio getColegio(){
		return colegio;
	}	

	public void setColegio(Colegio colegio) {
		this.colegio = colegio;
	}

	public Condicion getCondicion() {
		return condicion;
	}

	public void setCondicion(Condicion condicion) {
		this.condicion = condicion;
	}
}
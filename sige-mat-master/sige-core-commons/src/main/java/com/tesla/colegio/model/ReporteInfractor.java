package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_reporte_infractor
 * @author MV
 *
 */
public class ReporteInfractor extends EntidadBase{

	public final static String TABLA = "col_reporte_infractor";
	private Integer id;
	private Integer id_cp;
	private Integer id_mat;
	private ReporteConductual reporteconductual;	
	private Matricula matricula;	

	public ReporteInfractor(){
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
	* Obtiene Reporte Conductual 
	* @return id_cp
	*/
	public Integer getId_cp(){
		return id_cp;
	}	

	/**
	* Reporte Conductual 
	* @param id_cp
	*/
	public void setId_cp(Integer id_cp) {
		this.id_cp = id_cp;
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

	public ReporteConductual getReporteConductual(){
		return reporteconductual;
	}	

	public void setReporteConductual(ReporteConductual reporteconductual) {
		this.reporteconductual = reporteconductual;
	}
	public Matricula getMatricula(){
		return matricula;
	}	

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}
}
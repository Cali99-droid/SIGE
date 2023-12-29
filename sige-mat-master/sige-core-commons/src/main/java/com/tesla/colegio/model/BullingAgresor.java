package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_bulling_agresor
 * @author MV
 *
 */
public class BullingAgresor extends EntidadBase{

	public final static String TABLA = "col_bulling_agresor";
	private Integer id;
	private Integer id_crb;
	private Integer id_mat;
	private Integer id_tra;
	private ReporteBulling reportebulling;	
	private TipoAgresor tipoagresor;	
	private Matricula matricula;	
	private Trabajador trabajador;	

	public BullingAgresor(){
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
	* Obtiene Reporte Bulling 
	* @return id_crb
	*/
	public Integer getId_crb(){
		return id_crb;
	}	

	/**
	* Reporte Bulling 
	* @param id_crb
	*/
	public void setId_crb(Integer id_crb) {
		this.id_crb = id_crb;
	}

	/**
	* Obtiene Agresor Alumno 
	* @return id_mat
	*/
	public Integer getId_mat(){
		return id_mat;
	}	

	/**
	* Agresor Alumno 
	* @param id_mat
	*/
	public void setId_mat(Integer id_mat) {
		this.id_mat = id_mat;
	}

	/**
	* Obtiene Agresor Trabajador 
	* @return id_tra
	*/
	public Integer getId_tra(){
		return id_tra;
	}	

	/**
	* Agresor Trabajador 
	* @param id_tra
	*/
	public void setId_tra(Integer id_tra) {
		this.id_tra = id_tra;
	}

	public ReporteBulling getReporteBulling(){
		return reportebulling;
	}	

	public void setReporteBulling(ReporteBulling reportebulling) {
		this.reportebulling = reportebulling;
	}
	public TipoAgresor getTipoAgresor(){
		return tipoagresor;
	}	

	public void setTipoAgresor(TipoAgresor tipoagresor) {
		this.tipoagresor = tipoagresor;
	}
	public Matricula getMatricula(){
		return matricula;
	}	

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}
	public Trabajador getTrabajador(){
		return trabajador;
	}	

	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
	}
}
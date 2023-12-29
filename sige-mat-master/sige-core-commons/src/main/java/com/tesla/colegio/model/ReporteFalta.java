package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_reporte_falta
 * @author MV
 *
 */
public class ReporteFalta extends EntidadBase{

	public final static String TABLA = "col_reporte_falta";
	private Integer id;
	private Integer id_cp;
	private Integer id_cf;
	private ReporteConductual reporteconductual;	
	private Falta falta;	

	public ReporteFalta(){
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
	* Obtiene true 
	* @return id_cf
	*/
	public Integer getId_cf(){
		return id_cf;
	}	

	/**
	* true 
	* @param id_cf
	*/
	public void setId_cf(Integer id_cf) {
		this.id_cf = id_cf;
	}

	public ReporteConductual getReporteConductual(){
		return reporteconductual;
	}	

	public void setReporteConductual(ReporteConductual reporteconductual) {
		this.reporteconductual = reporteconductual;
	}
	public Falta getFalta(){
		return falta;
	}	

	public void setFalta(Falta falta) {
		this.falta = falta;
	}
}
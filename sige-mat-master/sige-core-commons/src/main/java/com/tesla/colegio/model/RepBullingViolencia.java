package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla con_rep_bulling_violencia
 * @author MV
 *
 */
public class RepBullingViolencia extends EntidadBase{

	public final static String TABLA = "con_rep_bulling_violencia";
	private Integer id;
	private Integer id_crb;
	private Integer id_ctv;
	private Integer id_ctf;
	private ReporteBulling reportebulling;	
	private TipoViolencia tipoviolencia;	

	public RepBullingViolencia(){
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
	* Obtiene Tipo de Violencia 
	* @return id_ctv
	*/
	public Integer getId_ctv(){
		return id_ctv;
	}	

	/**
	* Tipo de Violencia 
	* @param id_ctv
	*/
	public void setId_ctv(Integer id_ctv) {
		this.id_ctv = id_ctv;
	}

	public ReporteBulling getReporteBulling(){
		return reportebulling;
	}	

	public void setReporteBulling(ReporteBulling reportebulling) {
		this.reportebulling = reportebulling;
	}
	public TipoViolencia getTipoViolencia(){
		return tipoviolencia;
	}	

	public void setTipoViolencia(TipoViolencia tipoviolencia) {
		this.tipoviolencia = tipoviolencia;
	}

	public Integer getId_ctf() {
		return id_ctf;
	}

	public void setId_ctf(Integer id_ctf) {
		this.id_ctf = id_ctf;
	}
}
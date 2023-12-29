package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla con_rep_bulling_motivo
 * @author MV
 *
 */
public class RepBullingMotivo extends EntidadBase{

	public final static String TABLA = "con_rep_bulling_motivo";
	private Integer id;
	private Integer id_crb;
	private Integer id_cmb;
	private String otro_motivo;
	private ReporteBulling reportebulling;	
	private MotivoBulling motivobulling;	

	public RepBullingMotivo(){
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
	* Obtiene Motivo Violencia 
	* @return id_cmb
	*/
	public Integer getId_cmb(){
		return id_cmb;
	}	

	/**
	* Motivo Violencia 
	* @param id_cmb
	*/
	public void setId_cmb(Integer id_cmb) {
		this.id_cmb = id_cmb;
	}

	public ReporteBulling getReporteBulling(){
		return reportebulling;
	}	

	public void setReporteBulling(ReporteBulling reportebulling) {
		this.reportebulling = reportebulling;
	}
	public MotivoBulling getMotivoBulling(){
		return motivobulling;
	}	

	public void setMotivoBulling(MotivoBulling motivobulling) {
		this.motivobulling = motivobulling;
	}

	public String getOtro_motivo() {
		return otro_motivo;
	}

	public void setOtro_motivo(String otro_motivo) {
		this.otro_motivo = otro_motivo;
	}
}
package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_falta
 * @author MV
 *
 */
public class Falta extends EntidadBase{

	public final static String TABLA = "cat_falta";
	private Integer id;
	private Integer id_ctf;
	private String nom;
	private TipFalta tipfalta;	
	private List<ReporteFalta> reportefaltas;

	public Falta(){
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
	* Obtiene Tipo de Falta 
	* @return id_ctf
	*/
	public Integer getId_ctf(){
		return id_ctf;
	}	

	/**
	* Tipo de Falta 
	* @param id_ctf
	*/
	public void setId_ctf(Integer id_ctf) {
		this.id_ctf = id_ctf;
	}

	/**
	* Obtiene Nombre de la Falta 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre de la Falta 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	public TipFalta getTipFalta(){
		return tipfalta;
	}	

	public void setTipFalta(TipFalta tipfalta) {
		this.tipfalta = tipfalta;
	}
	/**
	* Obtiene lista de Reporte Falta 
	*/
	public List<ReporteFalta> getReporteFaltas() {
		return reportefaltas;
	}

	/**
	* Seta Lista de Reporte Falta 
	* @param reportefaltas
	*/	
	public void setReporteFalta(List<ReporteFalta> reportefaltas) {
		this.reportefaltas = reportefaltas;
	}
}
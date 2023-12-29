package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_tipo_violencia
 * @author MV
 *
 */
public class TipoViolencia extends EntidadBase{

	public final static String TABLA = "cat_tipo_violencia";
	private Integer id;
	private String nom;
	private List<ReporteBulling> reportebullings;

	public TipoViolencia(){
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
	* Obtiene Nombre 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene lista de Reporte Bulling 
	*/
	public List<ReporteBulling> getReporteBullings() {
		return reportebullings;
	}

	/**
	* Seta Lista de Reporte Bulling 
	* @param reportebullings
	*/	
	public void setReporteBulling(List<ReporteBulling> reportebullings) {
		this.reportebullings = reportebullings;
	}
}
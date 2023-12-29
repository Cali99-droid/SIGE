package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla con_formato_incidencia
 * @author MV
 *
 */
public class FormatoIncidencia extends EntidadBase{

	public final static String TABLA = "con_formato_incidencia";
	private Integer id;
	private Integer nro;
	private String nom;
	private List<ReporteConductual> reporteconductuals;
	private List<ReporteBulling> reportebullings;
	private List<FormEntrevista> formentrevistas;
	private List<FormResultEntrevista> formresultentrevistas;
	private List<Compromiso> compromisos;
	private List<FichaDerivacion> fichaderivacions;
	private List<FichaSeguimiento> fichaseguimientos;

	public FormatoIncidencia(){
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
	* Obtiene Nmero de Formato 
	* @return nro
	*/
	public Integer getNro(){
		return nro;
	}	

	/**
	* Nmero de Formato 
	* @param nro
	*/
	public void setNro(Integer nro) {
		this.nro = nro;
	}

	/**
	* Obtiene Nombre del Formato 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre del Formato 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene lista de Reporte Conductual 
	*/
	public List<ReporteConductual> getReporteConductuals() {
		return reporteconductuals;
	}

	/**
	* Seta Lista de Reporte Conductual 
	* @param reporteconductuals
	*/	
	public void setReporteConductual(List<ReporteConductual> reporteconductuals) {
		this.reporteconductuals = reporteconductuals;
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
	/**
	* Obtiene lista de Formato Entrevista 
	*/
	public List<FormEntrevista> getFormEntrevistas() {
		return formentrevistas;
	}

	/**
	* Seta Lista de Formato Entrevista 
	* @param formentrevistas
	*/	
	public void setFormEntrevista(List<FormEntrevista> formentrevistas) {
		this.formentrevistas = formentrevistas;
	}
	/**
	* Obtiene lista de Formato Resultado Entrevista 
	*/
	public List<FormResultEntrevista> getFormResultEntrevistas() {
		return formresultentrevistas;
	}

	/**
	* Seta Lista de Formato Resultado Entrevista 
	* @param formresultentrevistas
	*/	
	public void setFormResultEntrevista(List<FormResultEntrevista> formresultentrevistas) {
		this.formresultentrevistas = formresultentrevistas;
	}
	/**
	* Obtiene lista de Compromiso 
	*/
	public List<Compromiso> getCompromisos() {
		return compromisos;
	}

	/**
	* Seta Lista de Compromiso 
	* @param compromisos
	*/	
	public void setCompromiso(List<Compromiso> compromisos) {
		this.compromisos = compromisos;
	}
	/**
	* Obtiene lista de Ficha Derivacion 
	*/
	public List<FichaDerivacion> getFichaDerivacions() {
		return fichaderivacions;
	}

	/**
	* Seta Lista de Ficha Derivacion 
	* @param fichaderivacions
	*/	
	public void setFichaDerivacion(List<FichaDerivacion> fichaderivacions) {
		this.fichaderivacions = fichaderivacions;
	}
	/**
	* Obtiene lista de Ficha Seguimiento 
	*/
	public List<FichaSeguimiento> getFichaSeguimientos() {
		return fichaseguimientos;
	}

	/**
	* Seta Lista de Ficha Seguimiento 
	* @param fichaseguimientos
	*/	
	public void setFichaSeguimiento(List<FichaSeguimiento> fichaseguimientos) {
		this.fichaseguimientos = fichaseguimientos;
	}
}
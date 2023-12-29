package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_reporte_conductual
 * @author MV
 *
 */
public class ReporteConductual extends EntidadBase{

	public final static String TABLA = "col_reporte_conductual";
	private Integer id;
	private Integer id_cfi;
	private Integer id_inc;
	private String obs;
	private Integer id_prof;
	private String des_doc;
	private String des_inf;
	private byte[] archivo;
	private Periodo periodo;	
	private Usuario usuario;	
	private CursoAula cursoaula;	
	private List<ReporteInfractor> reporteinfractors;
	private List<ReporteFalta> reportefaltas;

	public ReporteConductual(){
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
	* Obtiene Observacin 
	* @return obs
	*/
	public String getObs(){
		return obs;
	}	

	/**
	* Observacin 
	* @param obs
	*/
	public void setObs(String obs) {
		this.obs = obs;
	}

	/**
	* Obtiene Curso 
	* @return id_cur
	*/
	public Integer getId_prof(){
		return id_prof;
	}	

	/**
	* Curso 
	* @param id_prof
	*/
	public void setId_prof(Integer id_prof) {
		this.id_prof = id_prof;
	}

	/**
	* Obtiene Descripcin del Docente 
	* @return des_doc
	*/
	public String getDes_doc(){
		return des_doc;
	}	

	/**
	* Descripcin del Docente 
	* @param des_doc
	*/
	public void setDes_doc(String des_doc) {
		this.des_doc = des_doc;
	}

	/**
	* Obtiene Descripcin del Informante 
	* @return des_inf
	*/
	public String getDes_inf(){
		return des_inf;
	}	

	/**
	* Descripcin del Informante 
	* @param des_inf
	*/
	public void setDes_inf(String des_inf) {
		this.des_inf = des_inf;
	}

	public Periodo getPeriodo(){
		return periodo;
	}	

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
	public Usuario getUsuario(){
		return usuario;
	}	

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public CursoAula getCursoAula(){
		return cursoaula;
	}	

	public void setCursoAula(CursoAula cursoaula) {
		this.cursoaula = cursoaula;
	}
	/**
	* Obtiene lista de Infractor del Reporte Conductual 
	*/
	public List<ReporteInfractor> getReporteInfractors() {
		return reporteinfractors;
	}

	/**
	* Seta Lista de Infractor del Reporte Conductual 
	* @param reporteinfractors
	*/	
	public void setReporteInfractor(List<ReporteInfractor> reporteinfractors) {
		this.reporteinfractors = reporteinfractors;
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

	public Integer getId_cfi() {
		return id_cfi;
	}

	public void setId_cfi(Integer id_cfi) {
		this.id_cfi = id_cfi;
	}

	public Integer getId_inc() {
		return id_inc;
	}

	public void setId_inc(Integer id_inc) {
		this.id_inc = id_inc;
	}

	public byte[] getArchivo() {
		return archivo;
	}

	public void setArchivo(byte[] archivo) {
		this.archivo = archivo;
	}
}
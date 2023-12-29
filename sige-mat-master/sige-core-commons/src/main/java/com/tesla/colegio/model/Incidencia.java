package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla con_incidencia
 * @author MV
 *
 */
public class Incidencia extends EntidadBase{

	public final static String TABLA = "con_incidencia";
	private Integer id;
	private Integer id_per;
	private Integer id_cti;
	private Integer id_inf;
	private String tip_inf;
	private String cod;
	private String nro_reg;
	private String drea;
	private String ugel;
	private Integer id_cec;
	private Periodo periodo;	
	private TipInc tipinc;	
	private EstadoConductual estadoconductual;	
	private List<IncidenciaTraza> incidenciatrazas;
	private List<ReporteBulling> reportebullings;
	private List<FormEntrevista> formentrevistas;
	private List<FormResultEntrevista> formresultentrevistas;
	private List<Compromiso> compromisos;
	private List<FichaDerivacion> fichaderivacions;
	private List<FichaSeguimiento> fichaseguimientos;

	public Incidencia(){
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
	* Obtiene Periodo 
	* @return id_per
	*/
	public Integer getId_per(){
		return id_per;
	}	

	/**
	* Periodo 
	* @param id_per
	*/
	public void setId_per(Integer id_per) {
		this.id_per = id_per;
	}

	/**
	* Obtiene Tipo de Incidencia 
	* @return id_cti
	*/
	public Integer getId_cti(){
		return id_cti;
	}	

	/**
	* Tipo de Incidencia 
	* @param id_cti
	*/
	public void setId_cti(Integer id_cti) {
		this.id_cti = id_cti;
	}

	/**
	* Obtiene Cdigo del Reporte 
	* @return cod
	*/
	public String getCod(){
		return cod;
	}	

	/**
	* Cdigo del Reporte 
	* @param cod
	*/
	public void setCod(String cod) {
		this.cod = cod;
	}

	/**
	* Obtiene Nmero de Registro Siseve 
	* @return nro_reg
	*/
	public String getNro_reg(){
		return nro_reg;
	}	

	/**
	* Nmero de Registro Siseve 
	* @param nro_reg
	*/
	public void setNro_reg(String nro_reg) {
		this.nro_reg = nro_reg;
	}

	/**
	* Obtiene Drea 
	* @return drea
	*/
	public String getDrea(){
		return drea;
	}	

	/**
	* Drea 
	* @param drea
	*/
	public void setDrea(String drea) {
		this.drea = drea;
	}

	/**
	* Obtiene Ugel 
	* @return ugel
	*/
	public String getUgel(){
		return ugel;
	}	

	/**
	* Ugel 
	* @param ugel
	*/
	public void setUgel(String ugel) {
		this.ugel = ugel;
	}

	/**
	* Obtiene Estado Conductual 
	* @return id_cec
	*/
	public Integer getId_cec(){
		return id_cec;
	}	

	/**
	* Estado Conductual 
	* @param id_cec
	*/
	public void setId_cec(Integer id_cec) {
		this.id_cec = id_cec;
	}

	public Periodo getPeriodo(){
		return periodo;
	}	

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
	public TipInc getTipInc(){
		return tipinc;
	}	

	public void setTipInc(TipInc tipinc) {
		this.tipinc = tipinc;
	}
	public EstadoConductual getEstadoConductual(){
		return estadoconductual;
	}	

	public void setEstadoConductual(EstadoConductual estadoconductual) {
		this.estadoconductual = estadoconductual;
	}
	/**
	* Obtiene lista de Incidencia Conductual Traza 
	*/
	public List<IncidenciaTraza> getIncidenciaTrazas() {
		return incidenciatrazas;
	}

	/**
	* Seta Lista de Incidencia Conductual Traza 
	* @param incidenciatrazas
	*/	
	public void setIncidenciaTraza(List<IncidenciaTraza> incidenciatrazas) {
		this.incidenciatrazas = incidenciatrazas;
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

	public Integer getId_inf() {
		return id_inf;
	}

	public void setId_inf(Integer id_inf) {
		this.id_inf = id_inf;
	}

	public String getTip_inf() {
		return tip_inf;
	}

	public void setTip_inf(String tip_inf) {
		this.tip_inf = tip_inf;
	}
}
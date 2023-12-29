package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla con_reporte_bulling
 * @author MV
 *
 */
public class ReporteBulling extends EntidadBase{

	public final static String TABLA = "con_reporte_bulling";
	private Integer id;
	private Integer id_cfi;
	private Integer id_inc;
	private Integer id_vic;
	private String des;
	private String tram;
	private String med;
	private String archivo;
	private FormatoIncidencia formatoincidencia;	
	private Incidencia incidencia;	
	private Matricula matricula;	
	private TipoViolencia tipoviolencia;	
	private MotivoBulling motivobulling;	
	private List<BullingAgresor> bullingagresors;

	public ReporteBulling(){
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
	* Obtiene Formato Incidencia 
	* @return id_cfi
	*/
	public Integer getId_cfi(){
		return id_cfi;
	}	

	/**
	* Formato Incidencia 
	* @param id_cfi
	*/
	public void setId_cfi(Integer id_cfi) {
		this.id_cfi = id_cfi;
	}

	/**
	* Obtiene Incidencia 
	* @return id_inc
	*/
	public Integer getId_inc(){
		return id_inc;
	}	

	/**
	* Incidencia 
	* @param id_inc
	*/
	public void setId_inc(Integer id_inc) {
		this.id_inc = id_inc;
	}

	/**
	* Obtiene Vctima 
	* @return id_vic
	*/
	public Integer getId_vic(){
		return id_vic;
	}	

	/**
	* Vctima 
	* @param id_vic
	*/
	public void setId_vic(Integer id_vic) {
		this.id_vic = id_vic;
	}

	/**
	* Obtiene Descripcin del hecho 
	* @return des
	*/
	public String getDes(){
		return des;
	}	

	/**
	* Descripcin del hecho 
	* @param des
	*/
	public void setDes(String des) {
		this.des = des;
	}

	/**
	* Obtiene Trmites del hecho 
	* @return tram
	*/
	public String getTram(){
		return tram;
	}	

	/**
	* Trmites del hecho 
	* @param tram
	*/
	public void setTram(String tram) {
		this.tram = tram;
	}

	/**
	* Obtiene Medidas Correctivas 
	* @return med
	*/
	public String getMed(){
		return med;
	}	

	/**
	* Medidas Correctivas 
	* @param med
	*/
	public void setMed(String med) {
		this.med = med;
	}

	/**
	* Obtiene Documento Escaneado 
	* @return archivo
	*/
	public String getArchivo(){
		return archivo;
	}	

	/**
	* Documento Escaneado 
	* @param archivo
	*/
	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}

	public FormatoIncidencia getFormatoIncidencia(){
		return formatoincidencia;
	}	

	public void setFormatoIncidencia(FormatoIncidencia formatoincidencia) {
		this.formatoincidencia = formatoincidencia;
	}
	public Incidencia getIncidencia(){
		return incidencia;
	}	

	public void setIncidencia(Incidencia incidencia) {
		this.incidencia = incidencia;
	}
	public Matricula getMatricula(){
		return matricula;
	}	

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}
	public TipoViolencia getTipoViolencia(){
		return tipoviolencia;
	}	

	public void setTipoViolencia(TipoViolencia tipoviolencia) {
		this.tipoviolencia = tipoviolencia;
	}
	public MotivoBulling getMotivoBulling(){
		return motivobulling;
	}	

	public void setMotivoBulling(MotivoBulling motivobulling) {
		this.motivobulling = motivobulling;
	}
	/**
	* Obtiene lista de Agresor de bulling 
	*/
	public List<BullingAgresor> getBullingAgresors() {
		return bullingagresors;
	}

	/**
	* Seta Lista de Agresor de bulling 
	* @param bullingagresors
	*/	
	public void setBullingAgresor(List<BullingAgresor> bullingagresors) {
		this.bullingagresors = bullingagresors;
	}
}
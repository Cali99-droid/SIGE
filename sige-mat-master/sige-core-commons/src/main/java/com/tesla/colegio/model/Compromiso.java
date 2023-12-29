package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla con_compromiso
 * @author MV
 *
 */
public class Compromiso extends EntidadBase{

	public final static String TABLA = "con_compromiso";
	private Integer id;
	private Integer id_inc;
	private Integer id_cfi;
	private Integer id_dir;
	private Integer id_ctc;
	private String des;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec;
	private String comp;
	private String archivo;
	private Incidencia incidencia;	
	private FormatoIncidencia formatoincidencia;	
	private Trabajador trabajador;	
	private TipoCompromiso tipocompromiso;	

	public Compromiso(){
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
	* Obtiene Directivo 
	* @return id_dir
	*/
	public Integer getId_dir(){
		return id_dir;
	}	

	/**
	* Directivo 
	* @param id_dir
	*/
	public void setId_dir(Integer id_dir) {
		this.id_dir = id_dir;
	}

	/**
	* Obtiene Tipo Compromiso 
	* @return id_ctc
	*/
	public Integer getId_ctc(){
		return id_ctc;
	}	

	/**
	* Tipo Compromiso 
	* @param id_ctc
	*/
	public void setId_ctc(Integer id_ctc) {
		this.id_ctc = id_ctc;
	}

	/**
	* Obtiene Descripcin del Caso 
	* @return des
	*/
	public String getDes(){
		return des;
	}	

	/**
	* Descripcin del Caso 
	* @param des
	*/
	public void setDes(String des) {
		this.des = des;
	}

	/**
	* Obtiene Fecha 
	* @return fec
	*/
	public java.util.Date getFec(){
		return fec;
	}	

	/**
	* Fecha 
	* @param fec
	*/
	public void setFec(java.util.Date fec) {
		this.fec = fec;
	}

	/**
	* Obtiene Compromiso de los Alumnos 
	* @return comp
	*/
	public String getComp(){
		return comp;
	}	

	/**
	* Compromiso de los Alumnos 
	* @param comp
	*/
	public void setComp(String comp) {
		this.comp = comp;
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

	public Incidencia getIncidencia(){
		return incidencia;
	}	

	public void setIncidencia(Incidencia incidencia) {
		this.incidencia = incidencia;
	}
	public FormatoIncidencia getFormatoIncidencia(){
		return formatoincidencia;
	}	

	public void setFormatoIncidencia(FormatoIncidencia formatoincidencia) {
		this.formatoincidencia = formatoincidencia;
	}
	public Trabajador getTrabajador(){
		return trabajador;
	}	

	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
	}
	public TipoCompromiso getTipoCompromiso(){
		return tipocompromiso;
	}	

	public void setTipoCompromiso(TipoCompromiso tipocompromiso) {
		this.tipocompromiso = tipocompromiso;
	}
}
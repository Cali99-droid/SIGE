package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla con_ficha_derivacion
 * @author MV
 *
 */
public class FichaDerivacion extends EntidadBase{

	public final static String TABLA = "con_ficha_derivacion";
	private Integer id;
	private Integer id_cfi;
	private Integer id_inc;
	private Integer id_dir;
	private Integer id_cdd;
	private String des;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec;
	private String archivo;
	private FormatoIncidencia formatoincidencia;	
	private Incidencia incidencia;	
	private Trabajador trabajador;	
	private DepartamentoDerivacion departamentoderivacion;	

	public FichaDerivacion(){
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
	* Obtiene Departamento derivacin 
	* @return id_cdd
	*/
	public Integer getId_cdd(){
		return id_cdd;
	}	

	/**
	* Departamento derivacin 
	* @param id_cdd
	*/
	public void setId_cdd(Integer id_cdd) {
		this.id_cdd = id_cdd;
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
	public Trabajador getTrabajador(){
		return trabajador;
	}	

	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
	}
	public DepartamentoDerivacion getDepartamentoDerivacion(){
		return departamentoderivacion;
	}	

	public void setDepartamentoDerivacion(DepartamentoDerivacion departamentoderivacion) {
		this.departamentoderivacion = departamentoderivacion;
	}
}
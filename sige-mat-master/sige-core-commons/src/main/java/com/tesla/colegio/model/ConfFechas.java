package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla mat_conf_fechas
 * @author MV
 *
 */
public class ConfFechas extends EntidadBase{

	public final static String TABLA = "mat_conf_fechas";
	private Integer id;
	private Integer id_anio;
	private String tipo;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date del;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date al;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date del_cs;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date al_cs;
	private Anio anio;	

	public ConfFechas(){
	}

	/**
	* Obtiene Llave Principal 
	* @return id
	*/
	public Integer getId(){
		return id;
	}	

	/**
	* Llave Principal 
	* @param id
	*/
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	* Obtiene Ao academico 
	* @return id_anio
	*/
	public Integer getId_anio(){
		return id_anio;
	}	

	/**
	* Ao academico 
	* @param id_anio
	*/
	public void setId_anio(Integer id_anio) {
		this.id_anio = id_anio;
	}

	/**
	* Obtiene Tipo de matricula 
	* @return tipo
	*/
	public String getTipo(){
		return tipo;
	}	

	/**
	* Tipo de matricula 
	* @param tipo
	*/
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	* Obtiene Del 
	* @return del
	*/
	public java.util.Date getDel(){
		return del;
	}	

	/**
	* Del 
	* @param del
	*/
	public void setDel(java.util.Date del) {
		this.del = del;
	}

	/**
	* Obtiene Al 
	* @return al
	*/
	public java.util.Date getAl(){
		return al;
	}	

	/**
	* Al 
	* @param al
	*/
	public void setAl(java.util.Date al) {
		this.al = al;
	}

	public Anio getAnio(){
		return anio;
	}	

	public void setAnio(Anio anio) {
		this.anio = anio;
	}

	public java.util.Date getDel_cs() {
		return del_cs;
	}

	public void setDel_cs(java.util.Date del_cs) {
		this.del_cs = del_cs;
	}

	public java.util.Date getAl_cs() {
		return al_cs;
	}

	public void setAl_cs(java.util.Date al_cs) {
		this.al_cs = al_cs;
	}
}
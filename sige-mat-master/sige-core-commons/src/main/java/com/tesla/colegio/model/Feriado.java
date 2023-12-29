package com.tesla.colegio.model;


import com.tesla.frmk.model.EntidadBase;

//import org.codehaus.jackson.annotate.JsonAutoDetect;
//import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Clase entidad que representa a la tabla aca_feriado
 * @author MV
 *
 */
//@JsonAutoDetect
public class Feriado extends EntidadBase{

	public final static String TABLA = "aca_feriado";
	private Integer id;
	private String nom;
	//@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date dia;
	private String motivo;

	public Feriado(){
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
	* Obtiene Rgimen laboral 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Rgimen laboral 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Da 
	* @return dia
	*/
	//@JsonSerialize(using=JsonDateSerializer.class)
	public java.util.Date getDia(){
		return dia;
	}	

	/**
	* Da 
	* @param dia
	*/
	public void setDia(java.util.Date dia) {
		this.dia = dia;
	}

	/**
	* Obtiene Descripcin del rgimen 
	* @return motivo
	*/
	public String getMotivo(){
		return motivo;
	}	

	/**
	* Descripcin del rgimen 
	* @param motivo
	*/
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

}
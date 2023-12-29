package com.tesla.colegio.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla mat_cronograma
 * @author MV
 *
 */
public class Cronograma extends EntidadBase{

	public final static String TABLA = "mat_cronograma";
	private Integer id;
	private Integer id_anio;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private java.util.Date fec_mat;
	private String del;
	private String al;
	private Anio anio;	
	private String tipo;//Normal,Extemporaneo

	public Cronograma(){
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
	* Obtiene Fecha para matricula 
	* @return fec_mat
	*/
	public java.util.Date getFec_mat(){
		return fec_mat;
	}	

	/**
	* Fecha para matricula 
	* @param fec_mat
	*/
	public void setFec_mat(java.util.Date fec_mat) {
		this.fec_mat = fec_mat;
	}

	/**
	* Obtiene Del 
	* @return del
	*/
	public String getDel(){
		return del;
	}	

	/**
	* Del 
	* @param del
	*/
	public void setDel(String del) {
		this.del = del;
	}

	/**
	* Obtiene Al 
	* @return al
	*/
	public String getAl(){
		return al;
	}	

	/**
	* Al 
	* @param al
	*/
	public void setAl(String al) {
		this.al = al;
	}

	public Anio getAnio(){
		return anio;
	}	

	public void setAnio(Anio anio) {
		this.anio = anio;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	

}
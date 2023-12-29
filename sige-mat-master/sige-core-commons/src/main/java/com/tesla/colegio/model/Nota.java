package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.List;

/**
 * Clase entidad que representa a la tabla not_nota
 * @author MV
 *
 */
public class Nota extends EntidadBase{

	public final static String TABLA = "not_nota";
	private Integer id;
	private Integer id_ne;
	private Integer id_tra;
	private Integer id_alu;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec;
	private BigDecimal prom;
	private Evaluacion evaluacion;	
	private Trabajador trabajador;	
	private Alumno alumno;	
	private List<NotaIndicador> notaindicadors;

	public Nota(){
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
	* Obtiene Evaluacion 
	* @return id_ne
	*/
	public Integer getId_ne(){
		return id_ne;
	}	

	/**
	* Evaluacion 
	* @param id_ne
	*/
	public void setId_ne(Integer id_ne) {
		this.id_ne = id_ne;
	}

	/**
	* Obtiene Docente 
	* @return id_tra
	*/
	public Integer getId_tra(){
		return id_tra;
	}	

	/**
	* Docente 
	* @param id_tra
	*/
	public void setId_tra(Integer id_tra) {
		this.id_tra = id_tra;
	}

	/**
	* Obtiene Alumno 
	* @return id_alu
	*/
	public Integer getId_alu(){
		return id_alu;
	}	

	/**
	* Alumno 
	* @param id_alu
	*/
	public void setId_alu(Integer id_alu) {
		this.id_alu = id_alu;
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
	* Obtiene Promedio 
	* @return prom
	*/
	public BigDecimal getProm(){
		return prom;
	}	

	/**
	* Promedio 
	* @param prom
	*/
	public void setProm(BigDecimal prom) {
		this.prom = prom;
	}

	public Evaluacion getEvaluacion(){
		return evaluacion;
	}	

	public void setEvaluacion(Evaluacion evaluacion) {
		this.evaluacion = evaluacion;
	}
	public Trabajador getTrabajador(){
		return trabajador;
	}	

	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
	}
	public Alumno getAlumno(){
		return alumno;
	}	

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}
	/**
	* Obtiene lista de Nota Indicador 
	*/
	public List<NotaIndicador> getNotaIndicadors() {
		return notaindicadors;
	}

	/**
	* Seta Lista de Nota Indicador 
	* @param notaindicadors
	*/	
	public void setNotaIndicador(List<NotaIndicador> notaindicadors) {
		this.notaindicadors = notaindicadors;
	}
}
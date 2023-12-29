package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla not_nota_des
 * @author MV
 *
 */
public class NotaDes extends EntidadBase{

	public final static String TABLA = "not_nota_des";
	private Integer id;
	private Integer id_desau;
	private Integer id_tra;
	private Integer id_alu;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec;
	private Integer nota;
	private java.math.BigDecimal prom;
	private DesempenioAula desempenioaula;	
	private Trabajador trabajador;	
	private Alumno alumno;	

	public NotaDes(){
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
	* Obtiene Desempenio Aula 
	* @return id_desau
	*/
	public Integer getId_desau(){
		return id_desau;
	}	

	/**
	* Desempenio Aula 
	* @param id_desau
	*/
	public void setId_desau(Integer id_desau) {
		this.id_desau = id_desau;
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
	* Obtiene Nota 
	* @return nota
	*/
	public Integer getNota(){
		return nota;
	}	

	/**
	* Nota 
	* @param nota
	*/
	public void setNota(Integer nota) {
		this.nota = nota;
	}

	/**
	* Obtiene Promedio 
	* @return prom
	*/
	public java.math.BigDecimal getProm(){
		return prom;
	}	

	/**
	* Promedio 
	* @param prom
	*/
	public void setProm(java.math.BigDecimal prom) {
		this.prom = prom;
	}

	public DesempenioAula getDesempenioAula(){
		return desempenioaula;
	}	

	public void setDesempenioAula(DesempenioAula desempenioaula) {
		this.desempenioaula = desempenioaula;
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
}
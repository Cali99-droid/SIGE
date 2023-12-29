package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla not_promedio_com
 * @author MV
 *
 */
public class PromedioCom extends EntidadBase{

	public final static String TABLA = "not_promedio_com";
	private Integer id;
	private Integer id_com;
	private Integer id_au;
	private Integer id_tra;
	private Integer id_alu;
	private Integer id_cpu;
	private Integer id_cua;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec;
	private java.math.BigDecimal prom;
	private CompetenciaDc competenciadc;	
	private Trabajador trabajador;	
	private Alumno alumno;	
	private PerUni peruni;	

	public PromedioCom(){
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
	* Obtiene Competencia 
	* @return id_com
	*/
	public Integer getId_com(){
		return id_com;
	}	

	/**
	* Competencia 
	* @param id_com
	*/
	public void setId_com(Integer id_com) {
		this.id_com = id_com;
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
	* Obtiene Periodo Unidad 
	* @return id_cpu
	*/
	public Integer getId_cpu(){
		return id_cpu;
	}	

	/**
	* Periodo Unidad 
	* @param id_cpu
	*/
	public void setId_cpu(Integer id_cpu) {
		this.id_cpu = id_cpu;
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

	public CompetenciaDc getCompetenciaDc(){
		return competenciadc;
	}	

	public void setCompetenciaDc(CompetenciaDc competenciadc) {
		this.competenciadc = competenciadc;
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
	public PerUni getPerUni(){
		return peruni;
	}	

	public void setPerUni(PerUni peruni) {
		this.peruni = peruni;
	}

	public Integer getId_au() {
		return id_au;
	}

	public void setId_au(Integer id_au) {
		this.id_au = id_au;
	}

	public Integer getId_cua() {
		return id_cua;
	}

	public void setId_cua(Integer id_cua) {
		this.id_cua = id_cua;
	}
}
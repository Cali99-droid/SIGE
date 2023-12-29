package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_curso_horario_pad
 * @author MV
 *
 */
public class CursoHorarioPad extends EntidadBase{

	public final static String TABLA = "col_curso_horario_pad";
	private Integer id;
	private Integer id_anio;
	private Integer id_au;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_ini_vig;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_fin_vig;
	private Anio anio;	
	private Aula aula;	
	private List<CursoHorario> cursohorarios;

	public CursoHorarioPad(){
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
	* Obtiene Aula 
	* @return id_au
	*/
	public Integer getId_au(){
		return id_au;
	}	

	/**
	* Aula 
	* @param id_au
	*/
	public void setId_au(Integer id_au) {
		this.id_au = id_au;
	}

	/**
	* Obtiene Fecha Inicio Vigencia 
	* @return fec_ini_vig
	*/
	public java.util.Date getFec_ini_vig(){
		return fec_ini_vig;
	}	

	/**
	* Fecha Inicio Vigencia 
	* @param fec_ini_vig
	*/
	public void setFec_ini_vig(java.util.Date fec_ini_vig) {
		this.fec_ini_vig = fec_ini_vig;
	}

	/**
	* Obtiene Fecha Fin Vigencua 
	* @return fec_fin_vig
	*/
	public java.util.Date getFec_fin_vig(){
		return fec_fin_vig;
	}	

	/**
	* Fecha Fin Vigencua 
	* @param fec_fin_vig
	*/
	public void setFec_fin_vig(java.util.Date fec_fin_vig) {
		this.fec_fin_vig = fec_fin_vig;
	}

	public Anio getAnio(){
		return anio;
	}	

	public void setAnio(Anio anio) {
		this.anio = anio;
	}
	public Aula getAula(){
		return aula;
	}	

	public void setAula(Aula aula) {
		this.aula = aula;
	}
	/**
	* Obtiene lista de Curso Horario 
	*/
	public List<CursoHorario> getCursoHorarios() {
		return cursohorarios;
	}

	/**
	* Seta Lista de Curso Horario 
	* @param cursohorarios
	*/	
	public void setCursoHorario(List<CursoHorario> cursohorarios) {
		this.cursohorarios = cursohorarios;
	}
}
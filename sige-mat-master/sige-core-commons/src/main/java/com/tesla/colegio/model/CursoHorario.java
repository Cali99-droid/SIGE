package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_curso_horario
 * @author MV
 *
 */
public class CursoHorario extends EntidadBase{

	public final static String TABLA = "col_curso_horario";
	private Integer id;
	private Integer id_cchp;//@TODO col_curso_horario_pad
	private Integer id_anio;
	private Integer id_cca;
	private Integer dia;
	private String hora_ini;
	private String hora_fin;
	private Anio anio;	
	private CursoAula cursoaula;	

	public CursoHorario(){
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
	* Obtiene Curso Aula 
	* @return id_cca
	*/
	public Integer getId_cca(){
		return id_cca;
	}	

	/**
	* Curso Aula 
	* @param id_cca
	*/
	public void setId_cca(Integer id_cca) {
		this.id_cca = id_cca;
	}

	/**
	* Obtiene Dia 
	* @return dia
	*/
	public Integer getDia(){
		return dia;
	}	

	/**
	* Dia 
	* @param dia
	*/
	public void setDia(Integer dia) {
		this.dia = dia;
	}

	/**
	* Obtiene Hora Inicio 
	* @return hora_ini
	*/
	public String getHora_ini(){
		return hora_ini;
	}	

	/**
	* Hora Inicio 
	* @param hora_ini
	*/
	public void setHora_ini(String hora_ini) {
		this.hora_ini = hora_ini;
	}

	/**
	* Obtiene Hora Fin 
	* @return hora_fin
	*/
	public String getHora_fin(){
		return hora_fin;
	}	

	/**
	* Hora Fin 
	* @param hora_fin
	*/
	public void setHora_fin(String hora_fin) {
		this.hora_fin = hora_fin;
	}

	public Anio getAnio(){
		return anio;
	}	

	public void setAnio(Anio anio) {
		this.anio = anio;
	}
	public CursoAula getCursoAula(){
		return cursoaula;
	}	

	public void setCursoAula(CursoAula cursoaula) {
		this.cursoaula = cursoaula;
	}

	public Integer getId_cchp() {
		return id_cchp;
	}

	public void setId_cchp(Integer id_cchp) {
		this.id_cchp = id_cchp;
	}
	
	
}
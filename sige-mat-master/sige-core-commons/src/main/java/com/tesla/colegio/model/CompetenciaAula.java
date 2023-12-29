package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla aca_competencia_aula
 * @author MV
 *
 */
public class CompetenciaAula extends EntidadBase{

	public final static String TABLA = "aca_competencia_aula";
	private Integer id;
	private Integer id_com;
	private Integer id_cpu;
	private Integer id_au;
	private Integer id_cua;
	private String conf_curso;
	private CompetenciaDc competenciadc;	
	private PerUni peruni;	
	private Aula aula;	
	private CursoAnio cursoanio;	
	private List<PromedioCom> promediocoms;

	public CompetenciaAula(){
	}

	/**
	* Obtiene Llave principal 
	* @return id
	*/
	public Integer getId(){
		return id;
	}	

	/**
	* Llave principal 
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
	* Obtiene Curso Anio 
	* @return id_cua
	*/
	public Integer getId_cua(){
		return id_cua;
	}	

	/**
	* Curso Anio 
	* @param id_cua
	*/
	public void setId_cua(Integer id_cua) {
		this.id_cua = id_cua;
	}

	/**
	* Obtiene Configuracion por curso 
	* @return conf_curso
	*/
	public String getConf_curso(){
		return conf_curso;
	}	

	/**
	* Configuracion por curso 
	* @param conf_curso
	*/
	public void setConf_curso(String conf_curso) {
		this.conf_curso = conf_curso;
	}

	public CompetenciaDc getCompetenciaDc(){
		return competenciadc;
	}	

	public void setCompetenciaDc(CompetenciaDc competenciadc) {
		this.competenciadc = competenciadc;
	}
	public PerUni getPerUni(){
		return peruni;
	}	

	public void setPerUni(PerUni peruni) {
		this.peruni = peruni;
	}
	public Aula getAula(){
		return aula;
	}	

	public void setAula(Aula aula) {
		this.aula = aula;
	}
	public CursoAnio getCursoAnio(){
		return cursoanio;
	}	

	public void setCursoAnio(CursoAnio cursoanio) {
		this.cursoanio = cursoanio;
	}
	/**
	* Obtiene lista de Promedio Competencia 
	*/
	public List<PromedioCom> getPromedioComs() {
		return promediocoms;
	}

	/**
	* Seta Lista de Promedio Competencia 
	* @param promediocoms
	*/	
	public void setPromedioCom(List<PromedioCom> promediocoms) {
		this.promediocoms = promediocoms;
	}
}
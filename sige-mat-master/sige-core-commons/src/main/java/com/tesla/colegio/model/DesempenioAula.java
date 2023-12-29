package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla aca_desempenio_aula
 * @author MV
 *
 */
public class DesempenioAula extends EntidadBase{

	public final static String TABLA = "aca_desempenio_aula";
	private Integer id;
	private Integer id_desdc;
	private Integer id_cpu;
	private Integer id_au;
	private Integer id_cap;
	private Integer id_cua;
	private String conf_curso;
	private DesempenioDc desempeniodc;	
	private PerUni peruni;	
	private Aula aula;	
	private List<NotaDes> notadess;

	public DesempenioAula(){
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
	* Obtiene Desempenio 
	* @return id_desdc
	*/
	public Integer getId_desdc(){
		return id_desdc;
	}	

	/**
	* Desempenio 
	* @param id_desdc
	*/
	public void setId_desdc(Integer id_desdc) {
		this.id_desdc = id_desdc;
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

	public DesempenioDc getDesempenioDc(){
		return desempeniodc;
	}	

	public void setDesempenioDc(DesempenioDc desempeniodc) {
		this.desempeniodc = desempeniodc;
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
	/**
	* Obtiene lista de Nota Desempenio 
	*/
	public List<NotaDes> getNotaDess() {
		return notadess;
	}

	/**
	* Seta Lista de Nota Desempenio 
	* @param notadess
	*/	
	public void setNotaDes(List<NotaDes> notadess) {
		this.notadess = notadess;
	}

	public Integer getId_cap() {
		return id_cap;
	}

	public void setId_cap(Integer id_cap) {
		this.id_cap = id_cap;
	}

	public Integer getId_cua() {
		return id_cua;
	}

	public void setId_cua(Integer id_cua) {
		this.id_cua = id_cua;
	}

	public String getConf_curso() {
		return conf_curso;
	}

	public void setConf_curso(String conf_curso) {
		this.conf_curso = conf_curso;
	}
}
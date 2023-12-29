package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla eva_criterio_pregunta
 * @author MV
 *
 */
public class CriterioPregunta extends EntidadBase{

	public final static String TABLA = "eva_criterio_pregunta";
	private Integer id;
	private Integer id_per;
	private String pre;
	private Integer ord;
	private Periodo periodo;	
	private List<CriterioAlternativa> criterioalternativas;
	
	//vamos a seetear la respuesta
	private Integer id_alt;

	public Integer getId_alt() {
		return id_alt;
	}

	public void setId_alt(Integer id_alt) {
		this.id_alt = id_alt;
	}

	public CriterioPregunta(){
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
	* Obtiene Periodo Acadmico 
	* @return id_per
	*/
	public Integer getId_per(){
		return id_per;
	}	

	/**
	* Periodo Acadmico 
	* @param id_per
	*/
	public void setId_per(Integer id_per) {
		this.id_per = id_per;
	}

	/**
	* Obtiene Pregunta 
	* @return pre
	*/
	public String getPre(){
		return pre;
	}	

	/**
	* Pregunta 
	* @param pre
	*/
	public void setPre(String pre) {
		this.pre = pre;
	}

	/**
	* Obtiene Orden 
	* @return ord
	*/
	public Integer getOrd(){
		return ord;
	}	

	/**
	* Orden 
	* @param ord
	*/
	public void setOrd(Integer ord) {
		this.ord = ord;
	}

	public Periodo getPeriodo(){
		return periodo;
	}	

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
	/**
	* Obtiene lista de Alternativas del examen criterio 
	*/
	public List<CriterioAlternativa> getCriterioAlternativas() {
		return criterioalternativas;
	}

	/**
	* Seta Lista de Alternativas del examen criterio 
	* @param criterioalternativas
	*/	
	public void setCriterioAlternativa(List<CriterioAlternativa> criterioalternativas) {
		this.criterioalternativas = criterioalternativas;
	}
}
package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla eva_criterio_nota
 * @author MV
 *
 */
public class CriterioNota extends EntidadBase{

	public final static String TABLA = "eva_criterio_nota";
	private Integer id;
	private Integer id_ex_cri;
	private Integer id_mat_vac;
	private Integer num;
	private Integer puntaje;
	private String resultado;
	private String apto;
	private ExaConfCriterio exaconfcriterio;	
	private MatrVacante matrvacante;	
	private List<CriterioPreAlt> criterioprealts;

	public CriterioNota(){
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
	* Obtiene Examen Criterio 
	* @return id_ex_cri
	*/
	public Integer getId_ex_cri(){
		return id_ex_cri;
	}	

	/**
	* Examen Criterio 
	* @param id_ex_cri
	*/
	public void setId_ex_cri(Integer id_ex_cri) {
		this.id_ex_cri = id_ex_cri;
	}

	/**
	* Obtiene Matricula Vacante 
	* @return id_mat_vac
	*/
	public Integer getId_mat_vac(){
		return id_mat_vac;
	}	

	/**
	* Matricula Vacante 
	* @param id_mat_vac
	*/
	public void setId_mat_vac(Integer id_mat_vac) {
		this.id_mat_vac = id_mat_vac;
	}

	/**
	* Obtiene Nmero de la evluacin 
	* @return num
	*/
	public Integer getNum(){
		return num;
	}	

	/**
	* Nmero de la evluacin 
	* @param num
	*/
	public void setNum(Integer num) {
		this.num = num;
	}

	/**
	* Obtiene Puntaje 
	* @return puntaje
	*/
	public Integer getPuntaje(){
		return puntaje;
	}	

	/**
	* Puntaje 
	* @param puntaje
	*/
	public void setPuntaje(Integer puntaje) {
		this.puntaje = puntaje;
	}

	/**
	* Obtiene Resultado 
	* @return resultado
	*/
	public String getResultado(){
		return resultado;
	}	

	/**
	* Resultado 
	* @param resultado
	*/
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	/**
	* Obtiene Apto 
	* @return apto
	*/
	public String getApto(){
		return apto;
	}	

	/**
	* Apto 
	* @param apto
	*/
	public void setApto(String apto) {
		this.apto = apto;
	}

	public ExaConfCriterio getExaConfCriterio(){
		return exaconfcriterio;
	}	

	public void setExaConfCriterio(ExaConfCriterio exaconfcriterio) {
		this.exaconfcriterio = exaconfcriterio;
	}
	public MatrVacante getMatrVacante(){
		return matrvacante;
	}	

	public void setMatrVacante(MatrVacante matrvacante) {
		this.matrvacante = matrvacante;
	}
	/**
	* Obtiene lista de Evaluacin Criterio Preguntas Alternativas 
	*/
	public List<CriterioPreAlt> getCriterioPreAlts() {
		return criterioprealts;
	}

	/**
	* Seta Lista de Evaluacin Criterio Preguntas Alternativas 
	* @param criterioprealts
	*/	
	public void setCriterioPreAlt(List<CriterioPreAlt> criterioprealts) {
		this.criterioprealts = criterioprealts;
	}
}
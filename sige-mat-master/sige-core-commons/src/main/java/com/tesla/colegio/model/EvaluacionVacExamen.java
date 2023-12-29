package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla eva_evaluacion_vac_examen
 * @author MV
 *
 */
public class EvaluacionVacExamen extends EntidadBase{

	public final static String TABLA = "eva_evaluacion_vac_examen";
	private Integer id;
	private Integer id_eva;
	private Integer id_eae;
	private Integer id_tae;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_exa;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_not;
	private EvaluacionVac evaluacionvac;	
	private AreaEva areaeva;	
	private TipEva tipeva;	
	private List<ExaConfMarcacion> exaconfmarcacions;
	private List<ExaConfEscrito> exaconfescritos;
	private List<ExaConfCriterio> exaconfcriterios;

	public EvaluacionVacExamen(){
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
	* Obtiene Evaluacin Vacante 
	* @return id_eva
	*/
	public Integer getId_eva(){
		return id_eva;
	}	

	/**
	* Evaluacin Vacante 
	* @param id_eva
	*/
	public void setId_eva(Integer id_eva) {
		this.id_eva = id_eva;
	}

	/**
	* Obtiene Area de evaluacin 
	* @return id_eae
	*/
	public Integer getId_eae(){
		return id_eae;
	}	

	/**
	* Area de evaluacin 
	* @param id_eae
	*/
	public void setId_eae(Integer id_eae) {
		this.id_eae = id_eae;
	}

	/**
	* Obtiene Tipo de evaluacin 
	* @return id_tae
	*/
	public Integer getId_tae(){
		return id_tae;
	}	

	/**
	* Tipo de evaluacin 
	* @param id_tae
	*/
	public void setId_tae(Integer id_tae) {
		this.id_tae = id_tae;
	}

	/**
	* Obtiene Fecha del Examen Vacante 
	* @return fec_exa
	*/
	public java.util.Date getFec_exa(){
		return fec_exa;
	}	

	/**
	* Fecha del Examen Vacante 
	* @param fec_exa
	*/
	public void setFec_exa(java.util.Date fec_exa) {
		this.fec_exa = fec_exa;
	}

	/**
	* Obtiene Hasta que Fecha ingreso nota 
	* @return fec_not
	*/
	public java.util.Date getFec_not(){
		return fec_not;
	}	

	/**
	* Hasta que Fecha ingreso nota 
	* @param fec_not
	*/
	public void setFec_not(java.util.Date fec_not) {
		this.fec_not = fec_not;
	}

	public EvaluacionVac getEvaluacionVac(){
		return evaluacionvac;
	}	

	public void setEvaluacionVac(EvaluacionVac evaluacionvac) {
		this.evaluacionvac = evaluacionvac;
	}
	public AreaEva getAreaEva(){
		return areaeva;
	}	

	public void setAreaEva(AreaEva areaeva) {
		this.areaeva = areaeva;
	}
	public TipEva getTipEva(){
		return tipeva;
	}	

	public void setTipEva(TipEva tipeva) {
		this.tipeva = tipeva;
	}
	/**
	* Obtiene lista de Examen por Marcacin 
	*/
	public List<ExaConfMarcacion> getExaConfMarcacions() {
		return exaconfmarcacions;
	}

	/**
	* Seta Lista de Examen por Marcacin 
	* @param exaconfmarcacions
	*/	
	public void setExaConfMarcacion(List<ExaConfMarcacion> exaconfmarcacions) {
		this.exaconfmarcacions = exaconfmarcacions;
	}
	/**
	* Obtiene lista de Examen Escrito 
	*/
	public List<ExaConfEscrito> getExaConfEscritos() {
		return exaconfescritos;
	}

	/**
	* Seta Lista de Examen Escrito 
	* @param exaconfescritos
	*/	
	public void setExaConfEscrito(List<ExaConfEscrito> exaconfescritos) {
		this.exaconfescritos = exaconfescritos;
	}
	/**
	* Obtiene lista de Examen Criterio 
	*/
	public List<ExaConfCriterio> getExaConfCriterios() {
		return exaconfcriterios;
	}

	/**
	* Seta Lista de Examen Criterio 
	* @param exaconfcriterios
	*/	
	public void setExaConfCriterio(List<ExaConfCriterio> exaconfcriterios) {
		this.exaconfcriterios = exaconfcriterios;
	}
}
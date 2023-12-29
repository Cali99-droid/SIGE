package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla eva_examen
 * @author MV
 *
 */
public class Examen extends EntidadBase{

	public final static String TABLA = "eva_examen";
	private Integer id;
	private Integer id_eva;
	private Integer id_eae;
	private Integer id_tae;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_exa;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_not;
	private java.math.BigDecimal precio;
	private java.math.BigDecimal pje_pre_cor;
	private java.math.BigDecimal pje_pre_inc;
	private EvaluacionVac evaluacionvac;	
	private AreaEva areaeva;	
	private TipEva tipeva;	


	public Examen(){
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

	/**
	* Obtiene Precio 
	* @return precio
	*/
	public java.math.BigDecimal getPrecio(){
		return precio;
	}	

	/**
	* Precio 
	* @param precio
	*/
	public void setPrecio(java.math.BigDecimal precio) {
		this.precio = precio;
	}

	/**
	* Obtiene Puntaje de la pregunta correcta 
	* @return pje_pre_cor
	*/
	public java.math.BigDecimal getPje_pre_cor(){
		return pje_pre_cor;
	}	

	/**
	* Puntaje de la pregunta correcta 
	* @param pje_pre_cor
	*/
	public void setPje_pre_cor(java.math.BigDecimal pje_pre_cor) {
		this.pje_pre_cor = pje_pre_cor;
	}

	/**
	* Obtiene Puntaje de la pregunta incorrecta 
	* @return pje_pre_inc
	*/
	public java.math.BigDecimal getPje_pre_inc(){
		return pje_pre_inc;
	}	

	/**
	* Puntaje de la pregunta incorrecta 
	* @param pje_pre_inc
	*/
	public void setPje_pre_inc(java.math.BigDecimal pje_pre_inc) {
		this.pje_pre_inc = pje_pre_inc;
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
	* Obtiene lista de Evaluacin a criterio 
	*/
	
}
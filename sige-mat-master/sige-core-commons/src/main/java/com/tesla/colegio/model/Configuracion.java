package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla eco_configuracion
 * @author MV
 *
 */
public class Configuracion extends EntidadBase{

	public final static String TABLA = "eco_configuracion";
	private Integer id;
	private Integer id_per;
	private String condicion;
	private String tip_resp;
	private Integer id_ect;
	private java.math.BigDecimal valor1;
	private java.math.BigDecimal valor2;
	private java.math.BigDecimal ptj;
	private Periodo periodo;	
	private ConfTipo conftipo;	
	private List<EvaResultado> evaresultados;

	public Configuracion(){
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
	* Obtiene Condicin 
	* @return condicion
	*/
	public String getCondicion(){
		return condicion;
	}	

	/**
	* Condicin 
	* @param condicion
	*/
	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}

	/**
	* Obtiene Tipo de respuesta: S:Si/NO, R:Riesgo , N: Numerico,  
	* @return tip_resp
	*/
	public String getTip_resp(){
		return tip_resp;
	}	

	/**
	* Tipo de respuesta: S:Si/NO, R:Riesgo , N: Numerico,  
	* @param tip_resp
	*/
	public void setTip_resp(String tip_resp) {
		this.tip_resp = tip_resp;
	}

	/**
	* Obtiene Valor de la respuesta 
	* @return id_ect
	*/
	public Integer getId_ect(){
		return id_ect;
	}	

	/**
	* Valor de la respuesta 
	* @param id_ect
	*/
	public void setId_ect(Integer id_ect) {
		this.id_ect = id_ect;
	}

	/**
	* Obtiene Desde 
	* @return valor1
	*/
	public java.math.BigDecimal getValor1(){
		return valor1;
	}	

	/**
	* Desde 
	* @param valor1
	*/
	public void setValor1(java.math.BigDecimal valor1) {
		this.valor1 = valor1;
	}

	/**
	* Obtiene Hasta 
	* @return valor2
	*/
	public java.math.BigDecimal getValor2(){
		return valor2;
	}	

	/**
	* Hasta 
	* @param valor2
	*/
	public void setValor2(java.math.BigDecimal valor2) {
		this.valor2 = valor2;
	}

	/**
	* Obtiene Puntuacin 
	* @return ptj
	*/
	public java.math.BigDecimal getPtj(){
		return ptj;
	}	

	/**
	* Puntuacin 
	* @param ptj
	*/
	public void setPtj(java.math.BigDecimal ptj) {
		this.ptj = ptj;
	}

	public Periodo getPeriodo(){
		return periodo;
	}	

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
	public ConfTipo getConfTipo(){
		return conftipo;
	}	

	public void setConfTipo(ConfTipo conftipo) {
		this.conftipo = conftipo;
	}
	/**
	* Obtiene lista de Evaluacin Econmica 
	*/
	public List<EvaResultado> getEvaResultados() {
		return evaresultados;
	}

	/**
	* Seta Lista de Evaluacin Econmica 
	* @param evaresultados
	*/	
	public void setEvaResultado(List<EvaResultado> evaresultados) {
		this.evaresultados = evaresultados;
	}
}
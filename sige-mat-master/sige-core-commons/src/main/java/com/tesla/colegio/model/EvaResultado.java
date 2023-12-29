package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla eco_eva_resultado
 * @author MV
 *
 */
public class EvaResultado extends EntidadBase{

	public final static String TABLA = "eco_eva_resultado";
	private Integer id;
	private Integer id_eve;
	private Integer id_ecc;
	private String val_resp;
	private EvaEconomica evaeconomica;	
	private Configuracion configuracion;	

	public EvaResultado(){
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
	* Obtiene Evaluacion 
	* @return id_eve
	*/
	public Integer getId_eve(){
		return id_eve;
	}	

	/**
	* Evaluacion 
	* @param id_eve
	*/
	public void setId_eve(Integer id_eve) {
		this.id_eve = id_eve;
	}

	/**
	* Obtiene Evaluacion 
	* @return id_ecc
	*/
	public Integer getId_ecc(){
		return id_ecc;
	}	

	/**
	* Evaluacion 
	* @param id_ecc
	*/
	public void setId_ecc(Integer id_ecc) {
		this.id_ecc = id_ecc;
	}

	/**
	* Obtiene Valor de la Respuesta ingresada manualemente 
	* @return val_resp
	*/
	public String getVal_resp(){
		return val_resp;
	}	

	/**
	* Valor de la Respuesta ingresada manualemente 
	* @param val_resp
	*/
	public void setVal_resp(String val_resp) {
		this.val_resp = val_resp;
	}

	public EvaEconomica getEvaEconomica(){
		return evaeconomica;
	}	

	public void setEvaEconomica(EvaEconomica evaeconomica) {
		this.evaeconomica = evaeconomica;
	}
	public Configuracion getConfiguracion(){
		return configuracion;
	}	

	public void setConfiguracion(Configuracion configuracion) {
		this.configuracion = configuracion;
	}
}
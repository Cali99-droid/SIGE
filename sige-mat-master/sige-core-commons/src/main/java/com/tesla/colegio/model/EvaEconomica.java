package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla eco_eva_economica
 * @author MV
 *
 */
public class EvaEconomica extends EntidadBase{

	public final static String TABLA = "eco_eva_economica";
	private Integer id;
	private Integer id_fam;
	private java.math.BigDecimal ptj;
	private Familiar familiar;	
	private List<EvaResultado> evaresultados;

	public EvaEconomica(){
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
	* Obtiene Familiar 
	* @return id_fam
	*/
	public Integer getId_fam(){
		return id_fam;
	}	

	/**
	* Familiar 
	* @param id_fam
	*/
	public void setId_fam(Integer id_fam) {
		this.id_fam = id_fam;
	}

	/**
	* Obtiene Puntaje 
	* @return ptj
	*/
	public java.math.BigDecimal getPtj(){
		return ptj;
	}	

	/**
	* Puntaje 
	* @param ptj
	*/
	public void setPtj(java.math.BigDecimal ptj) {
		this.ptj = ptj;
	}

	public Familiar getFamiliar(){
		return familiar;
	}	

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
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
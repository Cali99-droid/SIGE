package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla aca_competencia_dc
 * @author MV
 *
 */
public class CompetenciaDc extends EntidadBase{

	public final static String TABLA = "aca_competencia_dc";
	private Integer id;
	private Integer id_dcare;
	private String nom;
	private java.math.BigDecimal peso;
	private Integer orden;
	private DcnArea dcnarea;	
	private List<CapacidadDc> capacidaddcs;
	private List<DesempenioDc> desempeniodcs;

	public CompetenciaDc(){
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
	* Obtiene Area 
	* @return id_dcare
	*/
	public Integer getId_dcare(){
		return id_dcare;
	}	

	/**
	* Area 
	* @param id_dcare
	*/
	public void setId_dcare(Integer id_dcare) {
		this.id_dcare = id_dcare;
	}

	/**
	* Obtiene Nombre 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Peso 
	* @return peso
	*/
	public java.math.BigDecimal getPeso(){
		return peso;
	}	

	/**
	* Peso 
	* @param peso
	*/
	public void setPeso(java.math.BigDecimal peso) {
		this.peso = peso;
	}

	/**
	* Obtiene Orden 
	* @return orden
	*/
	public Integer getOrden(){
		return orden;
	}	

	/**
	* Orden 
	* @param orden
	*/
	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public DcnArea getDcnArea(){
		return dcnarea;
	}	

	public void setDcnArea(DcnArea dcnarea) {
		this.dcnarea = dcnarea;
	}
	/**
	* Obtiene lista de Capacidad 
	*/
	public List<CapacidadDc> getCapacidadDcs() {
		return capacidaddcs;
	}

	/**
	* Seta Lista de Capacidad 
	* @param capacidaddcs
	*/	
	public void setCapacidadDc(List<CapacidadDc> capacidaddcs) {
		this.capacidaddcs = capacidaddcs;
	}
	/**
	* Obtiene lista de Desempenio 
	*/
	public List<DesempenioDc> getDesempenioDcs() {
		return desempeniodcs;
	}

	/**
	* Seta Lista de Desempenio 
	* @param desempeniodcs
	*/	
	public void setDesempenioDc(List<DesempenioDc> desempeniodcs) {
		this.desempeniodcs = desempeniodcs;
	}

	@Override
	public String toString() {
		return "{id:" + id + ", id_dcare:" + id_dcare + ", nom:" + nom + ", peso:" + peso + ", orden:"
				+ orden + ", dcnarea:" + dcnarea + "}";
	}
}
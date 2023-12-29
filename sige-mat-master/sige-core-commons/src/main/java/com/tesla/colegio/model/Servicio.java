package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla ges_servicio
 * @author MV
 *
 */
public class Servicio extends EntidadBase{

	public final static String TABLA = "ges_servicio";
	private Integer id;
	private Integer id_suc;
	private Integer id_niv;
	private Integer id_gir;
	private String nom;
	private String des;
	private Sucursal sucursal;	
	private List<Periodo> periodos;

	public Servicio(){
	}

	/**
	* Obtiene $field.description 
	* @return id
	*/
	public Integer getId(){
		return id;
	}	

	/**
	* $field.description 
	* @param id
	*/
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	* Obtiene Local 
	* @return id_suc
	*/
	public Integer getId_suc(){
		return id_suc;
	}	

	/**
	* Local 
	* @param id_suc
	*/
	public void setId_suc(Integer id_suc) {
		this.id_suc = id_suc;
	}

	/**
	* Obtiene Nombre del servicio 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre del servicio 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public Sucursal getSucursal(){
		return sucursal;
	}	

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	/**
	* Obtiene lista de Periodo de estudio 
	*/
	public List<Periodo> getPeriodos() {
		return periodos;
	}

	/**
	* Seta Lista de Periodo de estudio 
	* @param periodos
	*/	
	public void setPeriodo(List<Periodo> periodos) {
		this.periodos = periodos;
	}

	public Integer getId_niv() {
		return id_niv;
	}

	public void setId_niv(Integer id_niv) {
		this.id_niv = id_niv;
	}

	public Integer getId_gir() {
		return id_gir;
	}

	public void setId_gir(Integer id_gir) {
		this.id_gir = id_gir;
	}

}
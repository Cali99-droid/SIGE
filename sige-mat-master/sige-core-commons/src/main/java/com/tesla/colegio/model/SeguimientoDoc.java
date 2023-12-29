package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_seguimiento_doc
 * @author MV
 *
 */
public class SeguimientoDoc extends EntidadBase{

	public final static String TABLA = "col_seguimiento_doc";
	private Integer id;
	private Integer id_fam;
	private Integer id_cpu;
	private Integer id_mat;
	private String tip;
	private Familiar familiar;	
	private PerUni peruni;	
	private byte[] archivo;

	public SeguimientoDoc(){
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
	* Obtiene Periodo Acadmico 
	* @return id_cpu
	*/
	public Integer getId_cpu(){
		return id_cpu;
	}	

	/**
	* Periodo Acadmico 
	* @param id_cpu
	*/
	public void setId_cpu(Integer id_cpu) {
		this.id_cpu = id_cpu;
	}

	/**
	* Obtiene Tipo 
	* @return tip
	*/
	public String getTip(){
		return tip;
	}	

	/**
	* Tipo 
	* @param tip
	*/
	public void setTip(String tip) {
		this.tip = tip;
	}

	public Familiar getFamiliar(){
		return familiar;
	}	

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}
	public PerUni getPerUni(){
		return peruni;
	}	

	public void setPerUni(PerUni peruni) {
		this.peruni = peruni;
	}

	public Integer getId_mat() {
		return id_mat;
	}

	public void setId_mat(Integer id_mat) {
		this.id_mat = id_mat;
	}

	public byte[] getArchivo() {
		return archivo;
	}

	public void setArchivo(byte[] archivo) {
		this.archivo = archivo;
	}
	
}
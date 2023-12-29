package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla seg_familiar_opcion
 * @author MV
 *
 */
public class FamiliarOpcion extends EntidadBase{

	public final static String TABLA = "seg_familiar_opcion";
	private Integer id;
	private Integer id_fam;
	private Integer id_opc;
	private Familiar familiar;	
	private Opcion opcion;	

	public FamiliarOpcion(){
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
	* Obtiene Familiar Opcion 
	* @return id_fam
	*/
	public Integer getId_fam(){
		return id_fam;
	}	

	/**
	* Familiar Opcion 
	* @param id_fam
	*/
	public void setId_fam(Integer id_fam) {
		this.id_fam = id_fam;
	}

	/**
	* Obtiene Opcin 
	* @return id_opc
	*/
	public Integer getId_opc(){
		return id_opc;
	}	

	/**
	* Opcin 
	* @param id_opc
	*/
	public void setId_opc(Integer id_opc) {
		this.id_opc = id_opc;
	}

	public Familiar getFamiliar(){
		return familiar;
	}	

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}
	public Opcion getOpcion(){
		return opcion;
	}	

	public void setOpcion(Opcion opcion) {
		this.opcion = opcion;
	}
}
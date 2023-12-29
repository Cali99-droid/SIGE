package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla alu_permisos
 * @author MV
 *
 */
public class Permisos extends EntidadBase{

	public final static String TABLA = "alu_permisos";
	private Integer id;
	private Integer id_alu;
	private Integer id_fam;
	private String rec_lib;
	private String ped_inf;
	private Familiar familiar;	

	public Permisos(){
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
	* Obtiene Recogera o no libretas 
	* @return rec_lib
	*/
	public String getRec_lib(){
		return rec_lib;
	}	

	/**
	* Recogera o no libretas 
	* @param rec_lib
	*/
	public void setRec_lib(String rec_lib) {
		this.rec_lib = rec_lib;
	}

	/**
	* Obtiene Pedira informacin o no 
	* @return ped_inf
	*/
	public String getPed_inf(){
		return ped_inf;
	}	

	/**
	* Pedira informacin o no 
	* @param ped_inf
	*/
	public void setPed_inf(String ped_inf) {
		this.ped_inf = ped_inf;
	}

	public Familiar getFamiliar(){
		return familiar;
	}	

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}
	
	public Integer getId_alu() {
		return id_alu;
	}

	public void setId_alu(Integer id_alu) {
		this.id_alu = id_alu;
	}	
}
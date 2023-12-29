package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_informante_externo
 * @author MV
 *
 */
public class InformanteExterno extends EntidadBase{

	public final static String TABLA = "col_informante_externo";
	private Integer id;
	private String nom;
	private String ape_pat;
	private String ape_mat;
	private Integer nro_doc;
	private Integer tip_doc;
	private Integer cel;
	private String dir;
	private Integer id_par;

	public InformanteExterno(){
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
	* Obtiene Apellido Paterno 
	* @return ape_pat
	*/
	public String getApe_pat(){
		return ape_pat;
	}	

	/**
	* Apellido Paterno 
	* @param ape_pat
	*/
	public void setApe_pat(String ape_pat) {
		this.ape_pat = ape_pat;
	}

	/**
	* Obtiene Apellido Materno 
	* @return ape_mat
	*/
	public String getApe_mat(){
		return ape_mat;
	}	

	/**
	* Apellido Materno 
	* @param ape_mat
	*/
	public void setApe_mat(String ape_mat) {
		this.ape_mat = ape_mat;
	}

	/**
	* Obtiene Nmero de Documento 
	* @return nro_doc
	*/
	public Integer getNro_doc(){
		return nro_doc;
	}	

	/**
	* Nmero de Documento 
	* @param nro_doc
	*/
	public void setNro_doc(Integer nro_doc) {
		this.nro_doc = nro_doc;
	}

	/**
	* Obtiene Tipo de documento 
	* @return tip_doc
	*/
	public Integer getTip_doc(){
		return tip_doc;
	}	

	/**
	* Tipo de documento 
	* @param tip_doc
	*/
	public void setTip_doc(Integer tip_doc) {
		this.tip_doc = tip_doc;
	}

	/**
	* Obtiene Celular 
	* @return cel
	*/
	public Integer getCel(){
		return cel;
	}	

	/**
	* Celular 
	* @param cel
	*/
	public void setCel(Integer cel) {
		this.cel = cel;
	}

	/**
	* Obtiene Direccin 
	* @return dir
	*/
	public String getDir(){
		return dir;
	}	

	/**
	* Direccin 
	* @param dir
	*/
	public void setDir(String dir) {
		this.dir = dir;
	}

	public Integer getId_par() {
		return id_par;
	}

	public void setId_par(Integer id_par) {
		this.id_par = id_par;
	}

}
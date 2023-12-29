package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla alu_gru_fam_familiar
 * @author MV
 *
 */
public class GruFamFamiliar extends EntidadBase{

	public final static String TABLA = "alu_gru_fam_familiar";
	private Integer id;
	private Integer id_gpf;
	private Integer id_fam;
	private Integer id_par;
	private String flag_permisos;
	private GruFam gruFam;	
	private Familiar familiar;	

	public GruFamFamiliar(){
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
	* Obtiene Grupo familiar 
	* @return id_gpf
	*/
	public Integer getId_gpf(){
		return id_gpf;
	}	

	/**
	* Grupo familiar 
	* @param id_gpf
	*/
	public void setId_gpf(Integer id_gpf) {
		this.id_gpf = id_gpf;
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

	public GruFam getGruFam(){
		return gruFam;
	}	

	public void setGruFam(GruFam gruFam) {
		this.gruFam = gruFam;
	}
	public Familiar getFamiliar(){
		return familiar;
	}	

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}


	
	/**
	 * Flag que es un familiar agregado por el apoderado,
	 * @return
	 */
	public String getFlag_permisos() {
		return flag_permisos;
	}

	public void setFlag_permisos(String flag_permisos) {
		this.flag_permisos = flag_permisos;
	}

	public Integer getId_par() {
		return id_par;
	}

	public void setId_par(Integer id_par) {
		this.id_par = id_par;
	}
	
}
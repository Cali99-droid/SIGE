package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla aca_dcn_comp_trans
 * @author MV
 *
 */
public class DcnCompTrans extends EntidadBase{

	public final static String TABLA = "aca_dcn_comp_trans";
	private Integer id;
	private Integer id_dcare;
	private Integer id_dcniv;
	private Integer id_ctran;
	private DcnArea dcnarea;	
	private DcnNivel dcnivel;	
	private CompetenciaTrans competenciatrans;	

	public DcnCompTrans(){
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
	* Obtiene Competencia Transversal 
	* @return id_ctran
	*/
	public Integer getId_ctran(){
		return id_ctran;
	}	

	/**
	* Competencia Transversal 
	* @param id_ctran
	*/
	public void setId_ctran(Integer id_ctran) {
		this.id_ctran = id_ctran;
	}

	public DcnArea getDcnArea(){
		return dcnarea;
	}	

	public void setDcnArea(DcnArea dcnarea) {
		this.dcnarea = dcnarea;
	}
	public CompetenciaTrans getCompetenciaTrans(){
		return competenciatrans;
	}	

	public void setCompetenciaTrans(CompetenciaTrans competenciatrans) {
		this.competenciatrans = competenciatrans;
	}

	public Integer getId_dcniv() {
		return id_dcniv;
	}

	public void setId_dcniv(Integer id_dcniv) {
		this.id_dcniv = id_dcniv;
	}

	public DcnNivel getDcnivel() {
		return dcnivel;
	}

	public void setDcnivel(DcnNivel dcnivel) {
		this.dcnivel = dcnivel;
	}
}
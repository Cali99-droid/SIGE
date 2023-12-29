package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla aca_dcn_area
 * @author MV
 *
 */
public class DcnArea extends EntidadBase{

	public final static String TABLA = "aca_dcn_area";
	private Integer id;
	private Integer id_dcniv;
	private Integer id_are;
	private Integer ord;
	private DcnNivel dcnnivel;	
	private Area area;	
	private List<DcnCompTrans> dcncomptranss;
	private List<CompetenciaDc> competenciadcs;

	public DcnArea(){
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
	* Obtiene Nivel 
	* @return id_dcniv
	*/
	public Integer getId_dcniv(){
		return id_dcniv;
	}	

	/**
	* Nivel 
	* @param id_dcniv
	*/
	public void setId_dcniv(Integer id_dcniv) {
		this.id_dcniv = id_dcniv;
	}

	/**
	* Obtiene Area 
	* @return id_are
	*/
	public Integer getId_are(){
		return id_are;
	}	

	/**
	* Area 
	* @param id_are
	*/
	public void setId_are(Integer id_are) {
		this.id_are = id_are;
	}

	public DcnNivel getDcnNivel(){
		return dcnnivel;
	}	

	public void setDcnNivel(DcnNivel dcnnivel) {
		this.dcnnivel = dcnnivel;
	}
	public Area getArea(){
		return area;
	}	

	public void setArea(Area area) {
		this.area = area;
	}
	/**
	* Obtiene lista de Competencia Transversal 
	*/
	public List<DcnCompTrans> getDcnCompTranss() {
		return dcncomptranss;
	}

	/**
	* Seta Lista de Competencia Transversal 
	* @param dcncomptranss
	*/	
	public void setDcnCompTrans(List<DcnCompTrans> dcncomptranss) {
		this.dcncomptranss = dcncomptranss;
	}
	/**
	* Obtiene lista de Competencia 
	*/
	public List<CompetenciaDc> getCompetenciaDcs() {
		return competenciadcs;
	}

	/**
	* Seta Lista de Competencia 
	* @param competenciadcs
	*/	
	public void setCompetenciaDc(List<CompetenciaDc> competenciadcs) {
		this.competenciadcs = competenciadcs;
	}

	public Integer getOrd() {
		return ord;
	}

	public void setOrd(Integer ord) {
		this.ord = ord;
	}
}
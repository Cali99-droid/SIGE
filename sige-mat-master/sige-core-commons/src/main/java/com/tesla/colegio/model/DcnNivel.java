package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla aca_dcn_nivel
 * @author MV
 *
 */
public class DcnNivel extends EntidadBase{

	public final static String TABLA = "aca_dcn_nivel";
	private Integer id;
	private Integer id_niv;
	private Integer id_dcn;
	private Nivel nivel;	
	private DisenioCurricular diseniocurricular;	
	private List<DcnArea> dcnareas;

	public DcnNivel(){
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
	* @return id_niv
	*/
	public Integer getId_niv(){
		return id_niv;
	}	

	/**
	* Nivel 
	* @param id_niv
	*/
	public void setId_niv(Integer id_niv) {
		this.id_niv = id_niv;
	}

	/**
	* Obtiene Nivel 
	* @return id_dcn
	*/
	public Integer getId_dcn(){
		return id_dcn;
	}	

	/**
	* Nivel 
	* @param id_dcn
	*/
	public void setId_dcn(Integer id_dcn) {
		this.id_dcn = id_dcn;
	}

	public Nivel getNivel(){
		return nivel;
	}	

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}
	public DisenioCurricular getDisenioCurricular(){
		return diseniocurricular;
	}	

	public void setDisenioCurricular(DisenioCurricular diseniocurricular) {
		this.diseniocurricular = diseniocurricular;
	}
	/**
	* Obtiene lista de Disenio Curricular Area 
	*/
	public List<DcnArea> getDcnAreas() {
		return dcnareas;
	}

	/**
	* Seta Lista de Disenio Curricular Area 
	* @param dcnareas
	*/	
	public void setDcnArea(List<DcnArea> dcnareas) {
		this.dcnareas = dcnareas;
	}
}
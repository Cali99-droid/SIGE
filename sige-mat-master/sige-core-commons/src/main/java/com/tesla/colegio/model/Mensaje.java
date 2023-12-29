package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;

/**
 * Clase entidad que representa a la tabla msj_mensaje
 * @author MV
 *
 */
public class Mensaje extends EntidadBase{

	public final static String TABLA = "msj_mensaje";
	private Integer id;
	private Integer id_emi;
	private Integer id_adj;
	private String asu;
	private String des;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_envio;
	private Emisor emisor;	
	private Adjunto adjunto;	
	private List<Receptor> receptors;

	public Mensaje(){
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
	* Obtiene Emisor 
	* @return id_emi
	*/
	public Integer getId_emi(){
		return id_emi;
	}	

	/**
	* Emisor 
	* @param id_emi
	*/
	public void setId_emi(Integer id_emi) {
		this.id_emi = id_emi;
	}

	/**
	* Obtiene Adjunto 
	* @return id_adj
	*/
	public Integer getId_adj(){
		return id_adj;
	}	

	/**
	* Adjunto 
	* @param id_adj
	*/
	public void setId_adj(Integer id_adj) {
		this.id_adj = id_adj;
	}

	/**
	* Obtiene Asunto 
	* @return asu
	*/
	public String getAsu(){
		return asu;
	}	

	/**
	* Asunto 
	* @param asu
	*/
	public void setAsu(String asu) {
		this.asu = asu;
	}

	/**
	* Obtiene Descripcion 
	* @return des
	*/
	public String getDes(){
		return des;
	}	

	/**
	* Descripcion 
	* @param des
	*/
	public void setDes(String des) {
		this.des = des;
	}

	/**
	* Obtiene Fecha Envio 
	* @return fec_envio
	*/
	public java.util.Date getFec_envio(){
		return fec_envio;
	}	

	/**
	* Fecha Envio 
	* @param fec_envio
	*/
	public void setFec_envio(java.util.Date fec_envio) {
		this.fec_envio = fec_envio;
	}

	public Emisor getEmisor(){
		return emisor;
	}	

	public void setEmisor(Emisor emisor) {
		this.emisor = emisor;
	}
	public Adjunto getAdjunto(){
		return adjunto;
	}	

	public void setAdjunto(Adjunto adjunto) {
		this.adjunto = adjunto;
	}
	/**
	* Obtiene lista de Receptor 
	*/
	public List<Receptor> getReceptors() {
		return receptors;
	}

	/**
	* Seta Lista de Receptor 
	* @param receptors
	*/	
	public void setReceptor(List<Receptor> receptors) {
		this.receptors = receptors;
	}
}
package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla ges_empresa
 * @author MV
 *
 */
public class Empresa extends EntidadBase{

	public final static String TABLA = "ges_empresa";
	private Integer id;
	private Integer id_dist;
	private Integer id_rep_leg;
	private String nom;
	private String raz_soc;
	private String rep_leg;
	private String ruc;
	private String abrv;
	private String dir;
	private String tel;
	private String corr;
	private String dominio;
	private String pag_web;
	private List<ConfContenido> confcontenidos;
	private List<ReglasNegocio> reglasnegocios;
	private List<GiroNegocio> gironegocios;
	private Provincia provincia;
	private Departamento departamento;
	private Pais pais;
	
	public Empresa(){
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
	* Obtiene Nombre comercial 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre comercial 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Razon social 
	* @return raz_soc
	*/
	public String getRaz_soc(){
		return raz_soc;
	}	

	/**
	* Razon social 
	* @param raz_soc
	*/
	public void setRaz_soc(String raz_soc) {
		this.raz_soc = raz_soc;
	}

	/**
	* Obtiene Representante legal 
	* @return rep_leg
	*/
	public String getRep_leg(){
		return rep_leg;
	}	

	/**
	* Representante legal 
	* @param rep_leg
	*/
	public void setRep_leg(String rep_leg) {
		this.rep_leg = rep_leg;
	}

	/**
	* Obtiene Registro unico del contribuyente 
	* @return ruc
	*/
	public String getRuc(){
		return ruc;
	}	

	/**
	* Registro unico del contribuyente 
	* @param ruc
	*/
	public void setRuc(String ruc) {
		this.ruc = ruc;
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

	/**
	* Obtiene Correo 
	* @return tel
	*/
	public String getTel(){
		return tel;
	}	

	/**
	* Correo 
	* @param tel
	*/
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	* Obtiene Telfono de la empresa 
	* @return corr
	*/
	public String getCorr(){
		return corr;
	}	

	/**
	* Telfono de la empresa 
	* @param corr
	*/
	public void setCorr(String corr) {
		this.corr = corr;
	}

	/**
	* Obtiene Dominio 
	* @return dominio
	*/
	public String getDominio(){
		return dominio;
	}	

	/**
	* Dominio 
	* @param dominio
	*/
	public void setDominio(String dominio) {
		this.dominio = dominio;
	}

	/**
	* Obtiene pagina_web 
	* @return pagina_web
	*/
	public String getPag_web(){
		return pag_web;
	}	

	/**
	* pagina_web 
	* @param pagina_web
	*/
	public void setPag_web(String pag_web) {
		this.pag_web = pag_web;
	}

	/**
	* Obtiene lista de Configuracion del Contenido 
	*/
	public List<ConfContenido> getConfContenidos() {
		return confcontenidos;
	}

	/**
	* Seta Lista de Configuracion del Contenido 
	* @param confcontenidos
	*/	
	public void setConfContenido(List<ConfContenido> confcontenidos) {
		this.confcontenidos = confcontenidos;
	}
	/**
	* Obtiene lista de Reglas de Negocio 
	*/
	public List<ReglasNegocio> getReglasNegocios() {
		return reglasnegocios;
	}

	/**
	* Seta Lista de Reglas de Negocio 
	* @param reglasnegocios
	*/	
	public void setReglasNegocio(List<ReglasNegocio> reglasnegocios) {
		this.reglasnegocios = reglasnegocios;
	}
	/**
	* Obtiene lista de Giro de negocio de la Empresa 
	*/
	public List<GiroNegocio> getGiroNegocios() {
		return gironegocios;
	}

	/**
	* Seta Lista de Giro de negocio de la Empresa 
	* @param gironegocios
	*/	
	public void setGiroNegocio(List<GiroNegocio> gironegocios) {
		this.gironegocios = gironegocios;
	}

	public String getAbrv() {
		return abrv;
	}

	public void setAbrv(String abrv) {
		this.abrv = abrv;
	}

	public Integer getId_dist() {
		return id_dist;
	}

	public void setId_dist(Integer id_dist) {
		this.id_dist = id_dist;
	}

	public Integer getId_rep_leg() {
		return id_rep_leg;
	}

	public void setId_rep_leg(Integer id_rep_leg) {
		this.id_rep_leg = id_rep_leg;
	}

	public Provincia getProvincia() {
		return provincia;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}
}
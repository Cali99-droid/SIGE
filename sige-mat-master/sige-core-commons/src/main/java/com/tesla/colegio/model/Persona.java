package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_persona
 * @author MV
 *
 */
public class Persona extends EntidadBase{

	public final static String TABLA = "col_persona";
	private Integer id;
	private String id_tdc;
	private String id_tap;
	private String id_gen;
	private Integer id_eci;
	private Integer id_rel;
	private Integer id_dist_viv;
	private String nro_doc;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_emi;
	private String ubigeo;
	private String nom;
	private String ape_pat;
	private String ape_mat;
	private String foto;
	private String hue;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_nac;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_def;
	private Integer id_pais_nac;
	private Integer id_dist_nac;
	private Integer id_nac;
	private String tlf;
	private String corr;
	private String cel;
	private String viv;
	private String dir;
	private String trab;
	private String face;
	private String tik_tok;
	private String istrg;
	private String twitter;
	private Integer id_cond;
	private TipoDocumento tipodocumento;	
	private EstCivil estcivil;	
	private Religion religion;	
	private Distrito distrito;	
	private List<Familiar> familiars;
	private Provincia provincia_nac;
	private Departamento departamento;
	public Persona(){
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
	* Obtiene Tipo de Documento 
	* @return id_tdc
	*/
	public String getId_tdc(){
		return id_tdc;
	}	

	/**
	* Tipo de Documento 
	* @param id_tdc
	*/
	public void setId_tdc(String id_tdc) {
		this.id_tdc = id_tdc;
	}

	/**
	* Obtiene Tipo Apellido 
	* @return id_tap
	*/
	public String getId_tap(){
		return id_tap;
	}	

	/**
	* Tipo Apellido 
	* @param id_tap
	*/
	public void setId_tap(String id_tap) {
		this.id_tap = id_tap;
	}

	/**
	* Obtiene Gnero 
	* @return id_gen
	*/
	public String getId_gen(){
		return id_gen;
	}	

	/**
	* Gnero 
	* @param id_gen
	*/
	public void setId_gen(String id_gen) {
		this.id_gen = id_gen;
	}

	/**
	* Obtiene Estado Civil del familiar 
	* @return id_eci
	*/
	public Integer getId_eci(){
		return id_eci;
	}	

	/**
	* Estado Civil del familiar 
	* @param id_eci
	*/
	public void setId_eci(Integer id_eci) {
		this.id_eci = id_eci;
	}

	/**
	* Obtiene Religin 
	* @return id_rel
	*/
	public Integer getId_rel(){
		return id_rel;
	}	

	/**
	* Religin 
	* @param id_rel
	*/
	public void setId_rel(Integer id_rel) {
		this.id_rel = id_rel;
	}

	/**
	* Obtiene Distrito Vive 
	* @return id_dist_viv
	*/
	public Integer getId_dist_viv(){
		return id_dist_viv;
	}	

	/**
	* Distrito Vive 
	* @param id_dist_viv
	*/
	public void setId_dist_viv(Integer id_dist_viv) {
		this.id_dist_viv = id_dist_viv;
	}

	/**
	* Obtiene Nmero de documento 
	* @return nro_doc
	*/
	public String getNro_doc(){
		return nro_doc;
	}	

	/**
	* Nmero de documento 
	* @param nro_doc
	*/
	public void setNro_doc(String nro_doc) {
		this.nro_doc = nro_doc;
	}

	/**
	* Obtiene Fecha de Emisin del DNI 
	* @return fec_emi
	*/
	public java.util.Date getFec_emi(){
		return fec_emi;
	}	

	/**
	* Fecha de Emisin del DNI 
	* @param fec_emi
	*/
	public void setFec_emi(java.util.Date fec_emi) {
		this.fec_emi = fec_emi;
	}

	/**
	* Obtiene Ubigeo 
	* @return ubigeo
	*/
	public String getUbigeo(){
		return ubigeo;
	}	

	/**
	* Ubigeo 
	* @param ubigeo
	*/
	public void setUbigeo(String ubigeo) {
		this.ubigeo = ubigeo;
	}

	/**
	* Obtiene Nombres del familiar 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombres del familiar 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Apellido paterno del familiar 
	* @return ape_pat
	*/
	public String getApe_pat(){
		return ape_pat;
	}	

	/**
	* Apellido paterno del familiar 
	* @param ape_pat
	*/
	public void setApe_pat(String ape_pat) {
		this.ape_pat = ape_pat;
	}

	/**
	* Obtiene Apellido materno del familiar 
	* @return ape_mat
	*/
	public String getApe_mat(){
		return ape_mat;
	}	

	/**
	* Apellido materno del familiar 
	* @param ape_mat
	*/
	public void setApe_mat(String ape_mat) {
		this.ape_mat = ape_mat;
	}

	/**
	* Obtiene Foto 
	* @return foto
	*/
	public String getFoto(){
		return foto;
	}	

	/**
	* Foto 
	* @param foto
	*/
	public void setFoto(String foto) {
		this.foto = foto;
	}

	/**
	* Obtiene Huella 
	* @return hue
	*/
	public String getHue(){
		return hue;
	}	

	/**
	* Huella 
	* @param hue
	*/
	public void setHue(String hue) {
		this.hue = hue;
	}

	/**
	* Obtiene Fecha de nacimiento del familiar 
	* @return fec_nac
	*/
	public java.util.Date getFec_nac(){
		return fec_nac;
	}	

	/**
	* Fecha de nacimiento del familiar 
	* @param fec_nac
	*/
	public void setFec_nac(java.util.Date fec_nac) {
		this.fec_nac = fec_nac;
	}

	/**
	* Obtiene Fecha de defuncin 
	* @return fec_def
	*/
	public java.util.Date getFec_def(){
		return fec_def;
	}	

	/**
	* Fecha de defuncin 
	* @param fec_def
	*/
	public void setFec_def(java.util.Date fec_def) {
		this.fec_def = fec_def;
	}

	/**
	* Obtiene Pais de Nacimiento 
	* @return id_pais_nac
	*/
	public Integer getId_pais_nac(){
		return id_pais_nac;
	}	

	/**
	* Pais de Nacimiento 
	* @param id_pais_nac
	*/
	public void setId_pais_nac(Integer id_pais_nac) {
		this.id_pais_nac = id_pais_nac;
	}

	/**
	* Obtiene Distrito de Nacimiento 
	* @return id_dist_nac
	*/
	public Integer getId_dist_nac(){
		return id_dist_nac;
	}	

	/**
	* Distrito de Nacimiento 
	* @param id_dist_nac
	*/
	public void setId_dist_nac(Integer id_dist_nac) {
		this.id_dist_nac = id_dist_nac;
	}

	/**
	* Obtiene Nacionalidad 
	* @return id_nac
	*/
	public Integer getId_nac(){
		return id_nac;
	}	

	/**
	* Nacionalidad 
	* @param id_nac
	*/
	public void setId_nac(Integer id_nac) {
		this.id_nac = id_nac;
	}

	/**
	* Obtiene Telfono del familiar 
	* @return tlf
	*/
	public String getTlf(){
		return tlf;
	}	

	/**
	* Telfono del familiar 
	* @param tlf
	*/
	public void setTlf(String tlf) {
		this.tlf = tlf;
	}

	/**
	* Obtiene Correo del familiar 
	* @return corr
	*/
	public String getCorr(){
		return corr;
	}	

	/**
	* Correo del familiar 
	* @param corr
	*/
	public void setCorr(String corr) {
		this.corr = corr;
	}

	/**
	* Obtiene Celular del familiar 
	* @return cel
	*/
	public String getCel(){
		return cel;
	}	

	/**
	* Celular del familiar 
	* @param cel
	*/
	public void setCel(String cel) {
		this.cel = cel;
	}

	/**
	* Obtiene Vive o no 
	* @return viv
	*/
	public String getViv(){
		return viv;
	}	

	/**
	* Vive o no 
	* @param viv
	*/
	public void setViv(String viv) {
		this.viv = viv;
	}

	/**
	* Obtiene Direccin del padre de familia 
	* @return dir
	*/
	public String getDir(){
		return dir;
	}	

	/**
	* Direccin del padre de familia 
	* @param dir
	*/
	public void setDir(String dir) {
		this.dir = dir;
	}

	/**
	* Obtiene Trabaja? 
	* @return trab
	*/
	public String getTrab(){
		return trab;
	}	

	/**
	* Trabaja? 
	* @param trab
	*/
	public void setTrab(String trab) {
		this.trab = trab;
	}

	/**
	* Obtiene Condicion 
	* @return id_cond
	*/
	public Integer getId_cond(){
		return id_cond;
	}	

	/**
	* Condicion 
	* @param id_cond
	*/
	public void setId_cond(Integer id_cond) {
		this.id_cond = id_cond;
	}

	public TipoDocumento getTipoDocumento(){
		return tipodocumento;
	}	

	public void setTipoDocumento(TipoDocumento tipodocumento) {
		this.tipodocumento = tipodocumento;
	}
	public EstCivil getEstCivil(){
		return estcivil;
	}	

	public void setEstCivil(EstCivil estcivil) {
		this.estcivil = estcivil;
	}
	public Religion getReligion(){
		return religion;
	}	

	public void setReligion(Religion religion) {
		this.religion = religion;
	}
	public Distrito getDistrito(){
		return distrito;
	}	

	public void setDistrito(Distrito distrito) {
		this.distrito = distrito;
	}
	/**
	* Obtiene lista de Familiar del alumno 
	*/
	public List<Familiar> getFamiliars() {
		return familiars;
	}

	/**
	* Seta Lista de Familiar del alumno 
	* @param familiars
	*/	
	public void setFamiliar(List<Familiar> familiars) {
		this.familiars = familiars;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	public String getTik_tok() {
		return tik_tok;
	}

	public void setTik_tok(String tik_tok) {
		this.tik_tok = tik_tok;
	}

	public String getIstrg() {
		return istrg;
	}

	public void setIstrg(String istrg) {
		this.istrg = istrg;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public Provincia getProvincia_nac() {
		return provincia_nac;
	}

	public void setProvincia_nac(Provincia provincia_nac) {
		this.provincia_nac = provincia_nac;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}
}
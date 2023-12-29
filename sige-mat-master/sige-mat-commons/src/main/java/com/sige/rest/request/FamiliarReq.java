package com.sige.rest.request;

import java.io.Serializable;

import org.springframework.format.annotation.DateTimeFormat;


public class FamiliarReq implements Serializable{

	private static final long serialVersionUID = -1182003383085892015L;
	private Integer id_per;
	private Integer id_fam;
	private Integer id_tdc;
	private Integer id_gpf;
	private Integer id_par;
	private String id_tap;
	private String id_gen;
	private Integer id_eci;
	private Integer id_gin;
	private Integer id_rel;
	private Integer id_dist;
	private Integer id_pais;
	private Integer id_ocu;
	private Integer id_anio;
	private String nro_doc;
	private String ubigeo;
	private String nom;
	private String ape_pat;
	private String ape_mat;
	private String hue;
//	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private String fec_nac;
//	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private String fec_def;
	private String viv;
	private String viv_alu;
	private String dir;
	private String ref;
	private String tlf;
	private String corr;
	private String cel;
	private String cel_val;//cel validado
	private String pass;
	private String prof;
	private String ocu;
	private String cto_tra;
	private String dir_tra;
	private String tlf_tra;
	private String cel_tra;
	private String email_tra;
	private String flag_alu_ex;
	private String ini;
	private String corr_val; //1 SI EL CORREO ES VALIDO
	private String est;
	private Integer id_tra_rem;
	private String face;
	private String istrg;
	private String twitter;
	public Integer getId_per() {
		return id_per;
	}
	public void setId_per(Integer id_per) {
		this.id_per = id_per;
	}
	public Integer getId_tdc() {
		return id_tdc;
	}
	public void setId_tdc(Integer id_tdc) {
		this.id_tdc = id_tdc;
	}
	public Integer getId_par() {
		return id_par;
	}
	public void setId_par(Integer id_par) {
		this.id_par = id_par;
	}
	public String getId_tap() {
		return id_tap;
	}
	public void setId_tap(String id_tap) {
		this.id_tap = id_tap;
	}
	public String getId_gen() {
		return id_gen;
	}
	public void setId_gen(String id_gen) {
		this.id_gen = id_gen;
	}
	public Integer getId_eci() {
		return id_eci;
	}
	public void setId_eci(Integer id_eci) {
		this.id_eci = id_eci;
	}
	public Integer getId_gin() {
		return id_gin;
	}
	public void setId_gin(Integer id_gin) {
		this.id_gin = id_gin;
	}
	public Integer getId_rel() {
		return id_rel;
	}
	public void setId_rel(Integer id_rel) {
		this.id_rel = id_rel;
	}
	public Integer getId_dist() {
		return id_dist;
	}
	public void setId_dist(Integer id_dist) {
		this.id_dist = id_dist;
	}
	public Integer getId_pais() {
		return id_pais;
	}
	public void setId_pais(Integer id_pais) {
		this.id_pais = id_pais;
	}
	public Integer getId_ocu() {
		return id_ocu;
	}
	public void setId_ocu(Integer id_ocu) {
		this.id_ocu = id_ocu;
	}
	public String getNro_doc() {
		return nro_doc;
	}
	public void setNro_doc(String nro_doc) {
		this.nro_doc = nro_doc;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getApe_pat() {
		return ape_pat;
	}
	public void setApe_pat(String ape_pat) {
		this.ape_pat = ape_pat;
	}
	public String getApe_mat() {
		return ape_mat;
	}
	public void setApe_mat(String ape_mat) {
		this.ape_mat = ape_mat;
	}
	public String getHue() {
		return hue;
	}
	public void setHue(String hue) {
		this.hue = hue;
	}
	public String getFec_nac() {
		return fec_nac;
	}
	public void setFec_nac(String fec_nac) {
		this.fec_nac = fec_nac;
	}
	public String getFec_def() {
		return fec_def;
	}
	public void setFec_def(String fec_def) {
		this.fec_def = fec_def;
	}
	public String getViv() {
		return viv;
	}
	public void setViv(String viv) {
		this.viv = viv;
	}
	public String getViv_alu() {
		return viv_alu;
	}
	public void setViv_alu(String viv_alu) {
		this.viv_alu = viv_alu;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public String getTlf() {
		return tlf;
	}
	public void setTlf(String tlf) {
		this.tlf = tlf;
	}
	public String getCorr() {
		return corr;
	}
	public void setCorr(String corr) {
		this.corr = corr;
	}
	public String getCel() {
		return cel;
	}
	public void setCel(String cel) {
		this.cel = cel;
	}
	public String getCel_val() {
		return cel_val;
	}
	public void setCel_val(String cel_val) {
		this.cel_val = cel_val;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getProf() {
		return prof;
	}
	public void setProf(String prof) {
		this.prof = prof;
	}
	public String getOcu() {
		return ocu;
	}
	public void setOcu(String ocu) {
		this.ocu = ocu;
	}
	public String getCto_tra() {
		return cto_tra;
	}
	public void setCto_tra(String cto_tra) {
		this.cto_tra = cto_tra;
	}
	public String getDir_tra() {
		return dir_tra;
	}
	public void setDir_tra(String dir_tra) {
		this.dir_tra = dir_tra;
	}
	public String getTlf_tra() {
		return tlf_tra;
	}
	public void setTlf_tra(String tlf_tra) {
		this.tlf_tra = tlf_tra;
	}
	public String getCel_tra() {
		return cel_tra;
	}
	public void setCel_tra(String cel_tra) {
		this.cel_tra = cel_tra;
	}
	public String getEmail_tra() {
		return email_tra;
	}
	public void setEmail_tra(String email_tra) {
		this.email_tra = email_tra;
	}
	public String getFlag_alu_ex() {
		return flag_alu_ex;
	}
	public void setFlag_alu_ex(String flag_alu_ex) {
		this.flag_alu_ex = flag_alu_ex;
	}
	public String getIni() {
		return ini;
	}
	public void setIni(String ini) {
		this.ini = ini;
	}
	public String getCorr_val() {
		return corr_val;
	}
	public void setCorr_val(String corr_val) {
		this.corr_val = corr_val;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Integer getId_gpf() {
		return id_gpf;
	}
	public void setId_gpf(Integer id_gpf) {
		this.id_gpf = id_gpf;
	}
	public String getEst() {
		return est;
	}
	public void setEst(String est) {
		this.est = est;
	}
	public Integer getId_anio() {
		return id_anio;
	}
	public void setId_anio(Integer id_anio) {
		this.id_anio = id_anio;
	}
	public Integer getId_tra_rem() {
		return id_tra_rem;
	}
	public void setId_tra_rem(Integer id_tra_rem) {
		this.id_tra_rem = id_tra_rem;
	}
	public String getFace() {
		return face;
	}
	public void setFace(String face) {
		this.face = face;
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
	public Integer getId_fam() {
		return id_fam;
	}
	public void setId_fam(Integer id_fam) {
		this.id_fam = id_fam;
	}
	public String getUbigeo() {
		return ubigeo;
	}
	public void setUbigeo(String ubigeo) {
		this.ubigeo = ubigeo;
	}
}

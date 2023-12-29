package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla alu_alumno
 * @author MV
 *
 */
public class Alumno extends EntidadBase{

	public final static String TABLA = "alu_alumno";
	private Integer id;
	private Integer id_per;
	private Integer id_tdc;
	private Integer id_idio1;
	private Integer id_idio2;
	private Integer id_eci;
	private String id_tap;
	private String id_gen;
	private Integer id_anio_act;
	private String cod;
	private String nro_doc;
	private String nom;
	private String ape_pat;
	private String ape_mat;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fec_nac;//esto se nos paso.. debe ser Date en la clase
	private Integer id_pais_nac;
	private Integer id_dist_nac;
	private Integer id_nac;
	private Integer id_dist_viv;
	private Integer num_hij;
	private String direccion;
	private String ref;
	private String telf;
	private String celular;
	private String corr;
	private String usuario;
	private String pass_educando;
	private String pass_google;
	private String email_inst;
	private  byte[] foto;
	private TipoDocumento tipoDocumento;	
	private Idioma idioma1;
	private Idioma idioma2;
	private EstCivil estCivil;	
	private List<GruFamAlumno> gruFamAlumnos;
	private Departamento departamento_nac;
	private GruFamAlumno gruFamAlumno;
	private Provincia provincia_nac;
	private Departamento departamento_viv;
	private Provincia provincia_viv;
	private String id_classRoom;
	private Persona persona;
	
	//relacion 1 a 1
	private Matricula matricula;
	
	public Alumno(){
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
	* Obtiene Tipo de documento 
	* @return id_tdc
	*/
	public Integer getId_tdc(){
		return id_tdc;
	}	

	/**
	* Tipo de documento 
	* @param id_tdc
	*/
	public void setId_tdc(Integer id_tdc) {
		this.id_tdc = id_tdc;
	}

	/**
	* Obtiene Lengua primaria 
	* @return id_idio1
	*/
	public Integer getId_idio1(){
		return id_idio1;
	}	

	/**
	* Lengua primaria 
	* @param id_idio1
	*/
	public void setId_idio1(Integer id_idio1) {
		this.id_idio1 = id_idio1;
	}

	/**
	* Obtiene Segunda lengua 
	* @return id_idio2
	*/
	public Integer getId_idio2(){
		return id_idio2;
	}	

	/**
	* Segunda lengua 
	* @param id_idio2
	*/
	public void setId_idio2(Integer id_idio2) {
		this.id_idio2 = id_idio2;
	}

	/**
	* Obtiene Estado Civil del alumno 
	* @return id_eci
	*/
	public Integer getId_eci(){
		return id_eci;
	}	

	/**
	* Estado Civil del alumno 
	* @param id_eci
	*/
	public void setId_eci(Integer id_eci) {
		this.id_eci = id_eci;
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
	* Obtiene Cdigo del alumno 
	* @return cod
	*/
	public String getCod(){
		return cod;
	}	

	/**
	* Cdigo del alumno 
	* @param cod
	*/
	public void setCod(String cod) {
		this.cod = cod;
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
	* Obtiene Nombres del alumno 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombres del alumno 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Apellido paterno del alumno 
	* @return ape_pat
	*/
	public String getApe_pat(){
		return ape_pat;
	}	

	/**
	* Apellido paterno del alumno 
	* @param ape_pat
	*/
	public void setApe_pat(String ape_pat) {
		this.ape_pat = ape_pat;
	}

	/**
	* Obtiene Apellido materno del alumno 
	* @return ape_mat
	*/
	public String getApe_mat(){
		return ape_mat;
	}	

	/**
	* Apellido materno del alumno 
	* @param ape_mat
	*/
	public void setApe_mat(String ape_mat) {
		this.ape_mat = ape_mat;
	}

	/**
	* Obtiene Fecha de nacimiento del alumno 
	* @return fec_nac
	*/
	public Date getFec_nac(){
		return fec_nac;
	}	

	/**
	* Fecha de nacimiento del alumno 
	* @param fec_nac
	*/
	public void setFec_nac(Date fec_nac) {
		this.fec_nac = fec_nac;
	}

	/**
	* Obtiene Nmero de hijo 
	* @return num_hij
	*/
	public Integer getNum_hij(){
		return num_hij;
	}	

	/**
	* Nmero de hijo 
	* @param num_hij
	*/
	public void setNum_hij(Integer num_hij) {
		this.num_hij = num_hij;
	}

	/**
	* Obtiene Direccion del alumno 
	* @return direccion
	*/
	public String getDireccion(){
		return direccion;
	}	

	/**
	* Direccion del alumno 
	* @param direccion
	*/
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	* Obtiene Tel?fono del alumno 
	* @return telf
	*/
	public String getTelf(){
		return telf;
	}	

	/**
	* Tel?fono del alumno 
	* @param telf
	*/
	public void setTelf(String telf) {
		this.telf = telf;
	}

	/**
	* Obtiene Celular del alumno 
	* @return celular
	*/
	public String getCelular(){
		return celular;
	}	

	/**
	* Celular del alumno 
	* @param celular
	*/
	public void setCelular(String celular) {
		this.celular = celular;
	}

	/**
	* Obtiene Pasword del educando 
	* @return pass_educando
	*/
	public String getPass_educando(){
		return pass_educando;
	}	

	/**
	* Pasword del educando 
	* @param pass_educando
	*/
	public void setPass_educando(String pass_educando) {
		this.pass_educando = pass_educando;
	}

	/**
	* Obtiene Foto del alumno 
	* @return foto
	*/
	public byte[] getFoto(){
		return foto;
	}	

	/**
	* Foto del alumno 
	* @param foto
	*/
	public void setFoto( byte[] foto) {
		this.foto = foto;
	}

	public TipoDocumento getTipoDocumento(){
		return tipoDocumento;
	}	

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public Idioma getIdioma1(){
		return idioma1;
	}	

	public void setIdioma1(Idioma idioma1) {
		this.idioma1 = idioma1;
	}	
	public Idioma getIdioma2(){
		return idioma2;
	}	

	public void setIdioma2(Idioma idioma2) {
		this.idioma2 = idioma2;
	}	
	public EstCivil getEstCivil(){
		return estCivil;
	}	

	public void setEstCivil(EstCivil estCivil) {
		this.estCivil = estCivil;
	}
	/**
	* Obtiene lista de Grupo Familiar/alumno 
	*/
	public List<GruFamAlumno> getGruFamAlumnos() {
		return gruFamAlumnos;
	}

	/**
	* Seta Lista de Grupo Familiar/alumno 
	* @param gruFamAlumnos
	*/	
	public void setGruFamAlumno(List<GruFamAlumno> gruFamAlumnos) {
		this.gruFamAlumnos = gruFamAlumnos;
	}

	public Matricula getMatricula() {
		return matricula;
	}

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}

	public Integer getId_pais_nac() {
		return id_pais_nac;
	}

	public void setId_pais_nac(Integer id_pais_nac) {
		this.id_pais_nac = id_pais_nac;
	}

	public Integer getId_dist_nac() {
		return id_dist_nac;
	}

	public void setId_dist_nac(Integer id_dist_nac) {
		this.id_dist_nac = id_dist_nac;
	}

	public Integer getId_nac() {
		return id_nac;
	}

	public void setId_nac(Integer id_nac) {
		this.id_nac = id_nac;
	}

	public Integer getId_dist_viv() {
		return id_dist_viv;
	}

	public void setId_dist_viv(Integer id_dist_viv) {
		this.id_dist_viv = id_dist_viv;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getCorr() {
		return corr;
	}

	public void setCorr(String corr) {
		this.corr = corr;
	}

	public Integer getId_anio_act() {
		return id_anio_act;
	}

	public void setId_anio_act(Integer id_anio_act) {
		this.id_anio_act = id_anio_act;
	}

	public Departamento getDepartamento_nac() {
		return departamento_nac;
	}

	public void setDepartamento_nac(Departamento departamento) {
		this.departamento_nac = departamento;
	}

	public Provincia getProvincia_nac() {
		return provincia_nac;
	}

	public void setProvincia_nac(Provincia provincia) {
		this.provincia_nac = provincia;
	}

	public Departamento getDepartamento_viv() {
		return departamento_viv;
	}

	public void setDepartamento_viv(Departamento departamento_viv) {
		this.departamento_viv = departamento_viv;
	}

	public Provincia getProvincia_viv() {
		return provincia_viv;
	}

	public void setProvincia_viv(Provincia provincia_viv) {
		this.provincia_viv = provincia_viv;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getId_classRoom() {
		return id_classRoom;
	}

	public void setId_classRoom(String id_classRoom) {
		this.id_classRoom = id_classRoom;
	}

	public Integer getId_per() {
		return id_per;
	}

	public void setId_per(Integer id_per) {
		this.id_per = id_per;
	}

	public String getEmail_inst() {
		return email_inst;
	}

	public void setEmail_inst(String email_inst) {
		this.email_inst = email_inst;
	}

	public GruFamAlumno getGruFamAlumno() {
		return gruFamAlumno;
	}

	public void setGruFamAlumno(GruFamAlumno gruFamAlumno) {
		this.gruFamAlumno = gruFamAlumno;
	}

	public String getPass_google() {
		return pass_google;
	}

	public void setPass_google(String pass_google) {
		this.pass_google = pass_google;
	}

	@Override
	public String toString() {
		return "Alumno [id=" + id + ", id_per=" + id_per + ", id_tdc=" + id_tdc + ", id_idio1=" + id_idio1
				+ ", id_idio2=" + id_idio2 + ", id_eci=" + id_eci + ", id_tap=" + id_tap + ", id_gen=" + id_gen
				+ ", id_anio_act=" + id_anio_act + ", cod=" + cod + ", nro_doc=" + nro_doc + ", nom=" + nom
				+ ", ape_pat=" + ape_pat + ", ape_mat=" + ape_mat + ", fec_nac=" + fec_nac + ", id_pais_nac="
				+ id_pais_nac + ", id_dist_nac=" + id_dist_nac + ", id_nac=" + id_nac + ", id_dist_viv=" + id_dist_viv
				+ ", num_hij=" + num_hij + ", direccion=" + direccion + ", ref=" + ref + ", telf=" + telf + ", celular="
				+ celular + ", corr=" + corr + ", usuario=" + usuario + ", pass_educando=" + pass_educando
				+ ", pass_google=" + pass_google + ", email_inst=" + email_inst + ", foto=" + Arrays.toString(foto)
				+ ", tipoDocumento=" + tipoDocumento + ", idioma1=" + idioma1 + ", idioma2=" + idioma2 + ", estCivil="
				+ estCivil + ", gruFamAlumnos=" + gruFamAlumnos + ", departamento_nac=" + departamento_nac
				+ ", gruFamAlumno=" + gruFamAlumno + ", provincia_nac=" + provincia_nac + ", departamento_viv="
				+ departamento_viv + ", provincia_viv=" + provincia_viv + ", id_classRoom=" + id_classRoom
				+ ", matricula=" + matricula + "]";
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}
	
	
}
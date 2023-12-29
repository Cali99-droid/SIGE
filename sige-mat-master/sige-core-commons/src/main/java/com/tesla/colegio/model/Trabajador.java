package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;

/**
 * Clase entidad que representa a la tabla ges_trabajador
 * @author MV
 *
 */
public class Trabajador extends EntidadBase{

	public final static String TABLA = "ges_trabajador";
	private Integer id;
	private Integer id_per;
	private String cod;
	private Integer id_tdc;
	private String nro_doc;
	private String ape_pat;
	private String ape_mat;
	private String nom;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_nac;
	private String genero;
	private Integer id_eci;
	private String dir;
	private String tel;
	private String cel;
	private String corr;
	private String corr_val;
	private String email_inst;
	private Integer id_gin;
	private String carrera;
	private byte[] fot;
	private String hijos;
	private Integer num_hij;
	private Integer id_usr;
	private TipoDocumento tipoDocumento;	
	private EstCivil estCivil;	
	private GradoInstruccion gradoInstruccion;	
	private Usuario usuario;	
	private List<Curso> cursos;

	public Trabajador(){
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
	* Obtiene Nro de documento 
	* @return nro_doc
	*/
	public String getNro_doc(){
		return nro_doc;
	}	

	/**
	* Nro de documento 
	* @param nro_doc
	*/
	public void setNro_doc(String nro_doc) {
		this.nro_doc = nro_doc;
	}

	/**
	* Obtiene Apellido paterno 
	* @return ape_pat
	*/
	public String getApe_pat(){
		return ape_pat;
	}	

	/**
	* Apellido paterno 
	* @param ape_pat
	*/
	public void setApe_pat(String ape_pat) {
		this.ape_pat = ape_pat;
	}

	/**
	* Obtiene Apellido materno 
	* @return ape_mat
	*/
	public String getApe_mat(){
		return ape_mat;
	}	

	/**
	* Apellido materno 
	* @param ape_mat
	*/
	public void setApe_mat(String ape_mat) {
		this.ape_mat = ape_mat;
	}

	/**
	* Obtiene Nombres del trabajador 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombres del trabajador 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Fecha de nacimiento del trabajador 
	* @return fec_nac
	*/
	public java.util.Date getFec_nac(){
		return fec_nac;
	}	

	/**
	* Fecha de nacimiento del trabajador 
	* @param fec_nac
	*/
	public void setFec_nac(java.util.Date fec_nac) {
		this.fec_nac = fec_nac;
	}

	/**
	* Obtiene Gnero 
	* @return genero
	*/
	public String getGenero(){
		return genero;
	}	

	/**
	* Gnero 
	* @param genero
	*/
	public void setGenero(String genero) {
		this.genero = genero;
	}

	/**
	* Obtiene Estado civil 
	* @return id_eci
	*/
	public Integer getId_eci(){
		return id_eci;
	}	

	/**
	* Estado civil 
	* @param id_eci
	*/
	public void setId_eci(Integer id_eci) {
		this.id_eci = id_eci;
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
	* Obtiene Telfono del trabajador 
	* @return tel
	*/
	public String getTel(){
		return tel;
	}	

	/**
	* Telfono del trabajador 
	* @param tel
	*/
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	* Obtiene Celular del trabajador 
	* @return cel
	*/
	public String getCel(){
		return cel;
	}	

	/**
	* Celular del trabajador 
	* @param cel
	*/
	public void setCel(String cel) {
		this.cel = cel;
	}

	/**
	* Obtiene Correo del trabajador 
	* @return corr
	*/
	public String getCorr(){
		return corr;
	}	

	/**
	* Correo del trabajador 
	* @param corr
	*/
	public void setCorr(String corr) {
		this.corr = corr;
	}

	/**
	* Obtiene Grado de instruccin 
	* @return id_gin
	*/
	public Integer getId_gin(){
		return id_gin;
	}	

	/**
	* Grado de instruccin 
	* @param id_gin
	*/
	public void setId_gin(Integer id_gin) {
		this.id_gin = id_gin;
	}

	/**
	* Obtiene Carrera 
	* @return carrera
	*/
	public String getCarrera(){
		return carrera;
	}	

	/**
	* Carrera 
	* @param carrera
	*/
	public void setCarrera(String carrera) {
		this.carrera = carrera;
	}

	/**
	* Obtiene Foto del trabajador 
	* @return fot
	*/
	public byte[] getFot(){
		return fot;
	}	

	/**
	* Foto del trabajador 
	* @param fot
	*/
	public void setFot(byte[] fot) {
		this.fot = fot;
	}

	/**
	* Obtiene Cant. hijos 
	* @return num_hij
	*/
	public Integer getNum_hij(){
		return num_hij;
	}	

	/**
	* Cant. hijos 
	* @param num_hij
	*/
	public void setNum_hij(Integer num_hij) {
		this.num_hij = num_hij;
	}

	/**
	* Obtiene Usuario 
	* @return id_usr
	*/
	public Integer getId_usr(){
		return id_usr;
	}	

	/**
	* Usuario 
	* @param id_usr
	*/
	public void setId_usr(Integer id_usr) {
		this.id_usr = id_usr;
	}

	public TipoDocumento getTipoDocumento(){
		return tipoDocumento;
	}	

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public EstCivil getEstCivil(){
		return estCivil;
	}	

	public void setEstCivil(EstCivil estCivil) {
		this.estCivil = estCivil;
	}
	public GradoInstruccion getGradoInstruccion(){
		return gradoInstruccion;
	}	

	public void setGradoInstruccion(GradoInstruccion gradoInstruccion) {
		this.gradoInstruccion = gradoInstruccion;
	}
	public Usuario getUsuario(){
		return usuario;
	}	

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	/**
	* Obtiene lista de Curso 
	*/
	public List<Curso> getCursos() {
		return cursos;
	}

	/**
	* Seta Lista de Curso 
	* @param cursos
	*/	
	public void setCurso(List<Curso> cursos) {
		this.cursos = cursos;
	}

	public String getCorr_val() {
		return corr_val;
	}

	public void setCorr_val(String corr_val) {
		this.corr_val = corr_val;
	}

	public Integer getId_per() {
		return id_per;
	}

	public void setId_per(Integer id_per) {
		this.id_per = id_per;
	}

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public String getHijos() {
		return hijos;
	}

	public void setHijos(String hijos) {
		this.hijos = hijos;
	}

	public String getEmail_inst() {
		return email_inst;
	}

	public void setEmail_inst(String email_inst) {
		this.email_inst = email_inst;
	}
	
	
}
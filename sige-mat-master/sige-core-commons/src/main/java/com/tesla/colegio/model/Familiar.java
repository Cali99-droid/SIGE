package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

import java.util.Arrays;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla alu_familiar
 * @author MV
 *
 */
public class Familiar extends EntidadBase{

	public final static String TABLA = "alu_familiar";
	private Integer id;
	private Integer id_per;
	private Integer id_usr;
	private Integer id_tdc;
	private Integer id_par;
	private String id_tap;
	private String id_gen;
	private Integer id_eci;
	private Integer id_gin;
	private Integer id_rel;
	private Integer id_dist;
	private Integer id_pais;
	private Integer id_ocu;
	private String nro_doc;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_emi_dni;
	private String ubigeo;
	private String nom;
	private String ape_pat;
	private String ape_mat;
	private String hue;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_nac;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_def;
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
	private String email_inst;
	private String flag_alu_ex;
	private String ini;
	private String corr_val; //1 SI EL CORREO ES VALIDO
	private TipoDocumento tipodocumento;	
	private Parentesco parentesco;	
	private EstCivil estcivil;	
	private GradoInstruccion gradoinstruccion;	
	private Religion religion;	
	private Distrito distrito;	
	private Ocupacion ocupacion;	
	private List<GruFamFamiliar> grufamfamiliars;
	private List<Permisos> permisoss;//esto es 1 a 1 ? crlea ro q si uno a muchos
	private List<Reserva> reservas;
	private List<Matricula> matriculas;
	public Permisos getPermisos() {
		return permisos;
	}

	public void setPermisos(Permisos permisos) {
		this.permisos = permisos;
	}

	private Permisos permisos;
	private  byte[] foto;


	private  byte[] huella;
	
	public Familiar(){
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
	* Obtiene Parentesco 
	* @return id_par
	*/
	public Integer getId_par(){
		return id_par;
	}	

	/**
	* Parentesco 
	* @param id_par
	*/
	public void setId_par(Integer id_par) {
		this.id_par = id_par;
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
	* Obtiene Grado de instrucci?n del familiar 
	* @return id_gin
	*/
	public Integer getId_gin(){
		return id_gin;
	}	

	/**
	* Grado de instrucci?n del familiar 
	* @param id_gin
	*/
	public void setId_gin(Integer id_gin) {
		this.id_gin = id_gin;
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
	* Obtiene Distrito de donde proviene el familiar 
	* @return id_dist
	*/
	public Integer getId_dist(){
		return id_dist;
	}	

	/**
	* Distrito de donde proviene el familiar 
	* @param id_dist
	*/
	public void setId_dist(Integer id_dist) {
		this.id_dist = id_dist;
	}

	/**
	* Obtiene Ocupacin del familiar 
	* @return id_ocu
	*/
	public Integer getId_ocu(){
		return id_ocu;
	}	

	/**
	* Ocupacin del familiar 
	* @param id_ocu
	*/
	public void setId_ocu(Integer id_ocu) {
		this.id_ocu = id_ocu;
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
	* Obtiene Vive con el estudiante 
	* @return viv_alu
	*/
	public String getViv_alu(){
		return viv_alu;
	}	

	/**
	* Vive con el estudiante 
	* @param viv_alu
	*/
	public void setViv_alu(String viv_alu) {
		this.viv_alu = viv_alu;
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
	* Obtiene Pasword del familiar 
	* @return pass
	*/
	public String getPass(){
		return pass;
	}	

	/**
	* Pasword del familiar 
	* @param pass
	*/
	public void setPass(String pass) {
		this.pass = pass;
	}

	/**
	* Obtiene Ocupacin del familiar 
	* @return ocu
	*/
	public String getOcu(){
		return ocu;
	}	

	/**
	* Ocupacin del familiar 
	* @param ocu
	*/
	public void setOcu(String ocu) {
		this.ocu = ocu;
	}

	/**
	* Obtiene Centro de trabajo del familiar 
	* @return cto_tra
	*/
	public String getCto_tra(){
		return cto_tra;
	}	

	/**
	* Centro de trabajo del familiar 
	* @param cto_tra
	*/
	public void setCto_tra(String cto_tra) {
		this.cto_tra = cto_tra;
	}

	public TipoDocumento getTipoDocumento(){
		return tipodocumento;
	}	

	public void setTipoDocumento(TipoDocumento tipodocumento) {
		this.tipodocumento = tipodocumento;
	}
	public Parentesco getParentesco(){
		return parentesco;
	}	

	public void setParentesco(Parentesco parentesco) {
		this.parentesco = parentesco;
	}
	public EstCivil getEstCivil(){
		return estcivil;
	}	

	public void setEstCivil(EstCivil estcivil) {
		this.estcivil = estcivil;
	}
	public GradoInstruccion getGradoInstruccion(){
		return gradoinstruccion;
	}	

	public void setGradoInstruccion(GradoInstruccion gradoinstruccion) {
		this.gradoinstruccion = gradoinstruccion;
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
	public Ocupacion getOcupacion(){
		return ocupacion;
	}	

	public void setOcupacion(Ocupacion ocupacion) {
		this.ocupacion = ocupacion;
	}
	/**
	* Obtiene lista de Grupo Familiar/familia 
	*/
	public List<GruFamFamiliar> getGruFamFamiliars() {
		return grufamfamiliars;
	}
	
	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}
	/**
	* Seta Lista de Grupo Familiar/familia 
	* @param grufamfamiliars
	*/	
	public void setGruFamFamiliar(List<GruFamFamiliar> grufamfamiliars) {
		this.grufamfamiliars = grufamfamiliars;
	}
	/**
	* Obtiene lista de Permisos del alumno 
	*/
	public List<Permisos> getPermisoss() {
		return permisoss;
	}

	/**
	* Seta Lista de Permisos del alumno 
	* @param permisoss
	*/	
	public void setPermisos(List<Permisos> permisoss) {
		this.permisoss = permisoss;
	}
	/**
	* Obtiene lista de Reserva de matrcula 
	*/
	public List<Reserva> getReservas() {
		return reservas;
	}

	/**
	* Seta Lista de Reserva de matrcula 
	* @param reservas
	*/	
	public void setReserva(List<Reserva> reservas) {
		this.reservas = reservas;
	}
	/**
	* Obtiene lista de Matricula del alumno 
	*/
	public List<Matricula> getMatriculas() {
		return matriculas;
	}

	/**
	* Seta Lista de Matricula del alumno 
	* @param matriculas
	*/	
	public void setMatricula(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}
	

	public byte[] getHuella() {
		return huella;
	}

	public void setHuella(byte[] huella) {
		this.huella = huella;
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

	
	
	public String getCel_val() {
		return cel_val;
	}

	public void setCel_val(String cel_val) {
		this.cel_val = cel_val;
	}

	@Override
	public String toString() {
		return "Familiar [id=" + id + ", id_tdc=" + id_tdc + ", id_par=" + id_par + ", id_tap=" + id_tap + ", id_gen="
				+ id_gen + ", id_eci=" + id_eci + ", id_gin=" + id_gin + ", id_rel=" + id_rel + ", id_dist=" + id_dist
				+ ", id_ocu=" + id_ocu + ", nro_doc=" + nro_doc + ", nom=" + nom + ", ape_pat=" + ape_pat + ", ape_mat="
				+ ape_mat + ", hue=" + hue + ", fec_nac=" + fec_nac + ", viv=" + viv + ", viv_alu=" + viv_alu + ", dir="
				+ dir + ", tlf=" + tlf + ", corr=" + corr + ", cel=" + cel + ", pass=" + pass + ", ocu=" + ocu
				+ ", cto_tra=" + cto_tra + ", ini=" + ini + ", corr_val=" + corr_val + ", tipodocumento="
				+ tipodocumento + ", parentesco=" + parentesco + ", estcivil=" + estcivil + ", gradoinstruccion="
				+ gradoinstruccion + ", religion=" + religion + ", distrito=" + distrito + ", ocupacion=" + ocupacion
				+ ", grufamfamiliars=" + grufamfamiliars + ", permisoss=" + permisoss + ", reservas=" + reservas
				+ ", matriculas=" + matriculas + ", permisos=" + permisos + ", foto=" + Arrays.toString(foto)
				+ ", huella=" + Arrays.toString(huella) + "]";
	}

	public java.util.Date getFec_def() {
		return fec_def;
	}

	public void setFec_def(java.util.Date fec_def) {
		this.fec_def = fec_def;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
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

	public Integer getId_pais() {
		return id_pais;
	}

	public void setId_pais(Integer id_pais) {
		this.id_pais = id_pais;
	}

	public String getProf() {
		return prof;
	}

	public void setProf(String prof) {
		this.prof = prof;
	}

	public java.util.Date getFec_emi_dni() {
		return fec_emi_dni;
	}

	public void setFec_emi_dni(java.util.Date fec_emi_dni) {
		this.fec_emi_dni = fec_emi_dni;
	}

	public String getUbigeo() {
		return ubigeo;
	}

	public void setUbigeo(String ubigeo) {
		this.ubigeo = ubigeo;
	}

	public Integer getId_per() {
		return id_per;
	}

	public void setId_per(Integer id_per) {
		this.id_per = id_per;
	}

	public Integer getId_usr() {
		return id_usr;
	}

	public void setId_usr(Integer id_usr) {
		this.id_usr = id_usr;
	}

	public String getEmail_inst() {
		return email_inst;
	}

	public void setEmail_inst(String email_inst) {
		this.email_inst = email_inst;
	}

	
	
}
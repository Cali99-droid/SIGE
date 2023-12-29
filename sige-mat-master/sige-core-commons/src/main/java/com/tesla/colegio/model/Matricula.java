package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla mat_matricula
 * @author MV
 *
 */
public class Matricula extends EntidadBase{

	public final static String TABLA = "mat_matricula";
	private Integer id;
	private Integer id_alu;
	private Integer id_fam;
	private Integer id_fam_emer;
	private Integer id_enc;
	private Integer id_sit;
	private Integer id_per_res;
	private Integer id_fam_res_pag;
	private Integer id_fam_res_aca;
	private Integer id_con;
	private Integer id_cli;
	private Integer id_per;
	private Integer id_cic;
	private Integer id_cct;
	private Integer id_au;
	private Integer id_au_asi;
	private Integer id_gra;
	private Integer id_niv;
	private Integer id_suc_con;
	private Integer tip_mat;
	private Integer id_col;
	private String mat_val;
	private String actyc;
	private String srvint;
	private String camweb;
	private String con_val;
		
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fecha;
	private String car_pod;
	private String num_cont;
	private String num_adenda;
	private String obs;
	private String tipo;
	private Alumno alumno;	
	private Familiar familiar;	
	private CondMatricula condmatricula;	
	private Cliente cliente;	
	private Periodo periodo;	
	private Aula aula;	
	private Grad grad;	
	private Nivel nivel;	
	private ColSituacion colSituacion;	
	private List<PagoRealizado> pagorealizados;
	private List<AcademicoPago> academicoPagos = new ArrayList<AcademicoPago>(); 
	
	//campos auxiliares
	private Integer[] id_ado;//arreglo de documentos de la matricula
	
	private String nro_rec;
	private Integer id_au_nue;//seccion sugerida para esta matricula

	public Integer getId_au_nue() {
		return id_au_nue;
	}

	public void setId_au_nue(Integer id_au_nue) {
		this.id_au_nue = id_au_nue;
	}

	public Integer[] getId_ado() {
		return id_ado;
	}

	public void setId_ado(Integer[] id_ado) {
		this.id_ado = id_ado;
	}

	public Matricula(){
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
	* Obtiene Alumno 
	* @return id_alu
	*/
	public Integer getId_alu(){
		return id_alu;
	}	

	/**
	* Alumno 
	* @param id_alu
	*/
	public void setId_alu(Integer id_alu) {
		this.id_alu = id_alu;
	}

	/**
	* Obtiene Apoderado 
	* @return id_fam
	*/
	public Integer getId_fam(){
		return id_fam;
	}	

	/**
	* Apoderado 
	* @param id_fam
	*/
	public void setId_fam(Integer id_fam) {
		this.id_fam = id_fam;
	}

	/**
	* Obtiene Encargado 
	* @return id_enc
	*/
	public Integer getId_enc(){
		return id_enc;
	}	

	/**
	* Encargado 
	* @param id_enc
	*/
	public void setId_enc(Integer id_enc) {
		this.id_enc = id_enc;
	}

	/**
	* Obtiene Condicion Matricula 
	* @return id_con
	*/
	public Integer getId_con(){
		return id_con;
	}	

	/**
	* Condicion Matricula 
	* @param id_con
	*/
	public void setId_con(Integer id_con) {
		this.id_con = id_con;
	}

	/**
	* Obtiene Cliente 
	* @return id_cli
	*/
	public Integer getId_cli(){
		return id_cli;
	}	

	/**
	* Cliente 
	* @param id_cli
	*/
	public void setId_cli(Integer id_cli) {
		this.id_cli = id_cli;
	}

	/**
	* Obtiene Periodo Acadmico 
	* @return id_per
	*/
	public Integer getId_per(){
		return id_per;
	}	

	/**
	* Periodo Acadmico 
	* @param id_per
	*/
	public void setId_per(Integer id_per) {
		this.id_per = id_per;
	}

	/**
	* Obtiene Aula 
	* @return id_au
	*/
	public Integer getId_au(){
		return id_au;
	}	

	/**
	* Aula 
	* @param id_au
	*/
	public void setId_au(Integer id_au) {
		this.id_au = id_au;
	}

	/**
	* Obtiene Grado 
	* @return id_gra
	*/
	public Integer getId_gra(){
		return id_gra;
	}	

	/**
	* Grado 
	* @param id_gra
	*/
	public void setId_gra(Integer id_gra) {
		this.id_gra = id_gra;
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
	* Obtiene Fecha de matricula 
	* @return fecha
	*/
	public java.util.Date getFecha(){
		return fecha;
	}	

	/**
	* Fecha de matricula 
	* @param fecha
	*/
	public void setFecha(java.util.Date fecha) {
		this.fecha = fecha;
	}

	/**
	* Obtiene Presenta carta poder 
	* @return car_pod
	*/
	public String getCar_pod(){
		return car_pod;
	}	

	/**
	* Presenta carta poder 
	* @param car_pod
	*/
	public void setCar_pod(String car_pod) {
		this.car_pod = car_pod;
	}

	/**
	* Obtiene Nmero de Contrato 
	* @return num_cont
	*/
	public String getNum_cont(){
		return num_cont;
	}	

	/**
	* Nmero de Contrato 
	* @param num_cont
	*/
	public void setNum_cont(String num_cont) {
		this.num_cont = num_cont;
	}

	/**
	* Obtiene Observacin 
	* @return obs
	*/
	public String getObs(){
		return obs;
	}	

	/**
	* Observacin 
	* @param obs
	*/
	public void setObs(String obs) {
		this.obs = obs;
	}

	public Alumno getAlumno(){
		return alumno;
	}	

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}
	public Familiar getFamiliar(){
		return familiar;
	}	

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}
	
	public CondMatricula getCondMatricula(){
		return condmatricula;
	}	

	public void setCondMatricula(CondMatricula condmatricula) {
		this.condmatricula = condmatricula;
	}
	public Cliente getCliente(){
		return cliente;
	}	

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public Periodo getPeriodo(){
		return periodo;
	}	

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
	public Aula getAula(){
		return aula;
	}	

	public void setAula(Aula aula) {
		this.aula = aula;
	}
	public Grad getGrad(){
		return grad;
	}	

	public void setGrad(Grad grad) {
		this.grad = grad;
	}
	public Nivel getNivel(){
		return nivel;
	}	

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}
	/**
	* Obtiene lista de Pago Realizado 
	*/
	public List<PagoRealizado> getPagoRealizados() {
		return pagorealizados;
	}

	/**
	* Seta Lista de Pago Realizado 
	* @param pagorealizados
	*/	
	public void setPagoRealizado(List<PagoRealizado> pagorealizados) {
		this.pagorealizados = pagorealizados;
	}
	
	public List<AcademicoPago> getAcademicoPagos() {
		return academicoPagos;
	}

	public void setAcademicoPago(List<AcademicoPago> academicoPagos) {
		this.academicoPagos = academicoPagos;
	}

	public String getNro_rec() {
		return nro_rec;
	}

	public void setNro_rec(String nro_rec) {
		this.nro_rec = nro_rec;
	}
	
	public Integer getId_sit() {
		return id_sit;
	}

	public void setId_sit(Integer id_sit) {
		this.id_sit = id_sit;
	}

	public ColSituacion getColSituacion() {
		return colSituacion;
	}

	public void setColSituacion(ColSituacion colSituacion) {
		this.colSituacion = colSituacion;
	}

	public Integer getId_suc_con() {
		return id_suc_con;
	}

	public void setId_suc_con(Integer id_suc_con) {
		this.id_suc_con = id_suc_con;
	}

	public Integer getId_au_asi() {
		return id_au_asi;
	}

	public void setId_au_asi(Integer id_au_asi) {
		this.id_au_asi = id_au_asi;
	}

	@Override
	public String toString() {
		return "Matricula [id=" + id + ", id_alu=" + id_alu + ", id_fam=" + id_fam + ", id_enc=" + id_enc + ", id_sit="
				+ id_sit + ", id_con=" + id_con + ", id_cli=" + id_cli + ", id_per=" + id_per + ", id_au=" + id_au
				+ ", id_au_asi=" + id_au_asi + ", id_gra=" + id_gra + ", id_niv=" + id_niv + ", id_suc_con="
				+ id_suc_con + ", fecha=" + fecha + ", car_pod=" + car_pod + ", num_cont=" + num_cont + ", obs=" + obs
				+ ", alumno=" + alumno + ", familiar=" + familiar + ", condmatricula=" + condmatricula + ", cliente="
				+ cliente + ", periodo=" + periodo + ", aula=" + aula + ", grad=" + grad + ", nivel=" + nivel
				+ ", colSituacion=" + colSituacion + ", pagorealizados=" + pagorealizados + ", academicoPagos="
				+ academicoPagos + ", id_ado=" + Arrays.toString(id_ado) + ", nro_rec=" + nro_rec + ", id_au_nue="
				+ id_au_nue + "]";
	}

	public Integer getTip_mat() {
		return tip_mat;
	}

	public void setTip_mat(Integer tip_mat) {
		this.tip_mat = tip_mat;
	}

	public String getMat_val() {
		return mat_val;
	}

	public void setMat_val(String mat_val) {
		this.mat_val = mat_val;
	}

	public String getNum_adenda() {
		return num_adenda;
	}

	public void setNum_adenda(String num_adenda) {
		this.num_adenda = num_adenda;
	}

	public Integer getId_fam_emer() {
		return id_fam_emer;
	}

	public void setId_fam_emer(Integer id_fam_emer) {
		this.id_fam_emer = id_fam_emer;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getId_cic() {
		return id_cic;
	}

	public void setId_cic(Integer id_cic) {
		this.id_cic = id_cic;
	}

	public Integer getId_per_res() {
		return id_per_res;
	}

	public void setId_per_res(Integer id_per_res) {
		this.id_per_res = id_per_res;
	}

	public String getActyc() {
		return actyc;
	}

	public void setActyc(String actyc) {
		this.actyc = actyc;
	}

	public String getSrvint() {
		return srvint;
	}

	public void setSrvint(String srvint) {
		this.srvint = srvint;
	}

	public String getCamweb() {
		return camweb;
	}

	public void setCamweb(String camweb) {
		this.camweb = camweb;
	}

	public Integer getId_cct() {
		return id_cct;
	}

	public void setId_cct(Integer id_cct) {
		this.id_cct = id_cct;
	}

	public Integer getId_fam_res_pag() {
		return id_fam_res_pag;
	}

	public void setId_fam_res_pag(Integer id_fam_res_pag) {
		this.id_fam_res_pag = id_fam_res_pag;
	}

	public Integer getId_fam_res_aca() {
		return id_fam_res_aca;
	}

	public void setId_fam_res_aca(Integer id_fam_res_aca) {
		this.id_fam_res_aca = id_fam_res_aca;
	}

	public String getCon_val() {
		return con_val;
	}

	public void setCon_val(String con_val) {
		this.con_val = con_val;
	}

	public Integer getId_col() {
		return id_col;
	}

	public void setId_col(Integer id_col) {
		this.id_col = id_col;
	}


}
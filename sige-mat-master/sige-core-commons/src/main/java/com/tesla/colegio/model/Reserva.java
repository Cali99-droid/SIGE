package com.tesla.colegio.model;

import org.springframework.format.annotation.DateTimeFormat;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla mat_reserva
 * @author MV
 *
 */
public class Reserva extends EntidadBase{

	public final static String TABLA = "mat_reserva";
	private Integer id;
	private Integer id_alu;
	private Integer id_au;
	private Integer id_gra;
	private Integer id_niv;
	private Integer id_con;
	private Integer id_cli;
	private Integer id_per;
	private Integer id_fam;


	private ReservaCuota reservaCuota = new ReservaCuota();
	


	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_lim;
	private Alumno alumno;	
	private Aula aula;	
	private Grad grad;	
	private Nivel nivel;	
	private CondMatricula condmatricula;	
	private Cliente cliente;	
	private Periodo periodo;	
	private Familiar familiar;	
	private Persona persona_alu;
	private Persona persona_fam;

	public Reserva(){
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
	* Obtiene Fecha Reserva 
	* @return fec
	*/
	public java.util.Date getFec(){
		return fec;
	}	

	/**
	* Fecha Reserva 
	* @param fec
	*/
	public void setFec(java.util.Date fec) {
		this.fec = fec;
	}

	/**
	* Obtiene Fecha Lmite 
	* @return fec_lim
	*/
	public java.util.Date getFec_lim(){
		return fec_lim;
	}	

	/**
	* Fecha Lmite 
	* @param fec_lim
	*/
	public void setFec_lim(java.util.Date fec_lim) {
		this.fec_lim = fec_lim;
	}

	public Alumno getAlumno(){
		return alumno;
	}	

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
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
	public Familiar getFamiliar(){
		return familiar;
	}	

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}


	public ReservaCuota getReservaCuota() {
		return reservaCuota;
	}

	public void setReservaCuota(ReservaCuota reservaCuota) {
		this.reservaCuota = reservaCuota;
	}

	public Persona getPersona_alu() {
		return persona_alu;
	}

	public void setPersona_alu(Persona persona_alu) {
		this.persona_alu = persona_alu;
	}

	public Persona getPersona_fam() {
		return persona_fam;
	}

	public void setPersona_fam(Persona persona_fam) {
		this.persona_fam = persona_fam;
	}
}
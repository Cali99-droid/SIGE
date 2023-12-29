package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_sem_inscripcion
 * @author MV
 *
 */
public class SemInscripcion extends EntidadBase{

	public final static String TABLA = "col_sem_inscripcion";
	private Integer id;
	private Integer id_sem;
	private Integer id_gru;
	private Integer id_dist;
	private Integer nro_dni;
	private String ape_pat;
	private String ape_mat;
	private String nom;
	private String corr;
	private String col;
	private String flag_pago;
	private Seminario seminario;	
	private SemGrupo semgrupo;	
	private Distrito distrito;	
	private List<SemPago> sempagos;

	public SemInscripcion(){
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
	* Obtiene Seminario 
	* @return id_sem
	*/
	public Integer getId_sem(){
		return id_sem;
	}	

	/**
	* Seminario 
	* @param id_sem
	*/
	public void setId_sem(Integer id_sem) {
		this.id_sem = id_sem;
	}

	/**
	* Obtiene Grupo 
	* @return id_gru
	*/
	public Integer getId_gru(){
		return id_gru;
	}	

	/**
	* Grupo 
	* @param id_gru
	*/
	public void setId_gru(Integer id_gru) {
		this.id_gru = id_gru;
	}

	/**
	* Obtiene Distrito 
	* @return id_dist
	*/
	public Integer getId_dist(){
		return id_dist;
	}	

	/**
	* Distrito 
	* @param id_dist
	*/
	public void setId_dist(Integer id_dist) {
		this.id_dist = id_dist;
	}

	/**
	* Obtiene Nmero DNI 
	* @return nro_dni
	*/
	public Integer getNro_dni(){
		return nro_dni;
	}	

	/**
	* Nmero DNI 
	* @param nro_dni
	*/
	public void setNro_dni(Integer nro_dni) {
		this.nro_dni = nro_dni;
	}

	/**
	* Obtiene Apellido Paterno 
	* @return ape_pat
	*/
	public String getApe_pat(){
		return ape_pat;
	}	

	/**
	* Apellido Paterno 
	* @param ape_pat
	*/
	public void setApe_pat(String ape_pat) {
		this.ape_pat = ape_pat;
	}

	/**
	* Obtiene Apellido Materno 
	* @return ape_mat
	*/
	public String getApe_mat(){
		return ape_mat;
	}	

	/**
	* Apellido Materno 
	* @param ape_mat
	*/
	public void setApe_mat(String ape_mat) {
		this.ape_mat = ape_mat;
	}

	/**
	* Obtiene Nombre 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Correo 
	* @return corr
	*/
	public String getCorr(){
		return corr;
	}	

	/**
	* Correo 
	* @param corr
	*/
	public void setCorr(String corr) {
		this.corr = corr;
	}

	/**
	* Obtiene Colegio 
	* @return col
	*/
	public String getCol(){
		return col;
	}	

	/**
	* Colegio 
	* @param col
	*/
	public void setCol(String col) {
		this.col = col;
	}

	/**
	* Obtiene Flag Pago 
	* @return flag_pago
	*/
	public String getFlag_pago(){
		return flag_pago;
	}	

	/**
	* Flag Pago 
	* @param flag_pago
	*/
	public void setFlag_pago(String flag_pago) {
		this.flag_pago = flag_pago;
	}

	public Seminario getSeminario(){
		return seminario;
	}	

	public void setSeminario(Seminario seminario) {
		this.seminario = seminario;
	}
	public SemGrupo getSemGrupo(){
		return semgrupo;
	}	

	public void setSemGrupo(SemGrupo semgrupo) {
		this.semgrupo = semgrupo;
	}
	public Distrito getDistrito(){
		return distrito;
	}	

	public void setDistrito(Distrito distrito) {
		this.distrito = distrito;
	}
	/**
	* Obtiene lista de Seminario Pago 
	*/
	public List<SemPago> getSemPagos() {
		return sempagos;
	}

	/**
	* Seta Lista de Seminario Pago 
	* @param sempagos
	*/	
	public void setSemPago(List<SemPago> sempagos) {
		this.sempagos = sempagos;
	}
}
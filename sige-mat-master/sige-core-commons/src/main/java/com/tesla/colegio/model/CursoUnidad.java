package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_curso_unidad
 * @author MV
 *
 */
public class CursoUnidad extends EntidadBase{

	public final static String TABLA = "col_curso_unidad";
	private Integer id;
	private Integer id_niv;
	private Integer id_gra;
	private Integer id_cur;
	private Integer id_cpu;
	private Integer num;
	private String nom;
	private String des;
	private String producto;
	private Nivel nivel;	
	private Grad grad;	
	private Curso curso;	
	private PerUni peruni;	
	private PeriodoAca periodoAca;
	private Tema tema;
	private Subtema subtema;
	private List<UnidadTema> unidadtemas;
	private List<UnidadSesion> unidadsesions;
	private Integer nro_sem;
	
	public CursoUnidad(){
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
	* Obtiene Nivel al que pertenece el subtema 
	* @return id_niv
	*/
	public Integer getId_niv(){
		return id_niv;
	}	

	/**
	* Nivel al que pertenece el subtema 
	* @param id_niv
	*/
	public void setId_niv(Integer id_niv) {
		this.id_niv = id_niv;
	}

	/**
	* Obtiene Grado Acadmico al que pertenece el subtema 
	* @return id_gra
	*/
	public Integer getId_gra(){
		return id_gra;
	}	

	/**
	* Grado Acadmico al que pertenece el subtema 
	* @param id_gra
	*/
	public void setId_gra(Integer id_gra) {
		this.id_gra = id_gra;
	}

	/**
	* Obtiene Curso al que pertenece el subtema 
	* @return id_cur
	*/
	public Integer getId_cur(){
		return id_cur;
	}	

	/**
	* Curso al que pertenece el subtema 
	* @param id_cur
	*/
	public void setId_cur(Integer id_cur) {
		this.id_cur = id_cur;
	}

	/**
	* Obtiene Periodo Unidad 
	* @return id_cpu
	*/
	public Integer getId_cpu(){
		return id_cpu;
	}	

	/**
	* Periodo Unidad 
	* @param id_cpu
	*/
	public void setId_cpu(Integer id_cpu) {
		this.id_cpu = id_cpu;
	}

	/**
	* Obtiene Nmero de Unidad 
	* @return num
	*/
	public Integer getNum(){
		return num;
	}	

	/**
	* Nmero de Unidad 
	* @param num
	*/
	public void setNum(Integer num) {
		this.num = num;
	}

	/**
	* Obtiene Nombre de la unidad 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre de la unidad 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Descripcion de la Unidad 
	* @return des
	*/
	public String getDes(){
		return des;
	}	

	/**
	* Descripcion de la Unidad 
	* @param des
	*/
	public void setDes(String des) {
		this.des = des;
	}

	/**
	* Obtiene Producto 
	* @return producto
	*/
	public String getProducto(){
		return producto;
	}	

	/**
	* Producto 
	* @param producto
	*/
	public void setProducto(String producto) {
		this.producto = producto;
	}

	public Nivel getNivel(){
		return nivel;
	}	

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}
	public Grad getGrad(){
		return grad;
	}	

	public void setGrad(Grad grad) {
		this.grad = grad;
	}
	public Curso getCurso(){
		return curso;
	}	

	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	public PerUni getPerUni(){
		return peruni;
	}	

	public void setPerUni(PerUni peruni) {
		this.peruni = peruni;
	}
	/**
	* Obtiene lista de La unidad tiene campo temtico exclusivos 
	*/
	public List<UnidadTema> getUnidadTemas() {
		return unidadtemas;
	}

	/**
	* Seta Lista de La unidad tiene campo temtico exclusivos 
	* @param unidadtemas
	*/	
	public void setUnidadTema(List<UnidadTema> unidadtemas) {
		this.unidadtemas = unidadtemas;
	}
	/**
	* Obtiene lista de Sesiones por unidad 
	*/
	public List<UnidadSesion> getUnidadSesions() {
		return unidadsesions;
	}

	/**
	* Seta Lista de Sesiones por unidad 
	* @param unidadsesions
	*/	
	public void setUnidadSesion(List<UnidadSesion> unidadsesions) {
		this.unidadsesions = unidadsesions;
	}

	public PeriodoAca getPeriodoAca() {
		return periodoAca;
	}

	public void setPeriodoAca(PeriodoAca periodoAca) {
		this.periodoAca = periodoAca;
	}

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}

	public Subtema getSubtema() {
		return subtema;
	}

	public void setSubtema(Subtema subtema) {
		this.subtema = subtema;
	}

	
	
	public Integer getNro_sem() {
		return nro_sem;
	}

	public void setNro_sem(Integer nro_sem) {
		this.nro_sem = nro_sem;
	}

	@Override
	public String toString() {
		return "CursoUnidad [id=" + id + ", id_niv=" + id_niv + ", id_gra=" + id_gra + ", id_cur=" + id_cur
				+ ", id_cpu=" + id_cpu + ", num=" + num + ", nom=" + nom + ", des=" + des + ", producto=" + producto
				+ ", nivel=" + nivel + ", grad=" + grad + ", curso=" + curso + ", peruni=" + peruni + ", periodoAca="
				+ periodoAca + ", tema=" + tema + ", subtema=" + subtema + ", unidadtemas=" + unidadtemas
				+ ", unidadsesions=" + unidadsesions + "]";
	}
	
	
}
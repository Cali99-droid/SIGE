package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla asi_grado_horario
 * @author MV
 *
 */
public class GradoHorario extends EntidadBase{

	public final static String TABLA = "asi_grado_horario";
	private Integer id;
	private Integer id_anio;
	private Integer id_gir;
	private Integer id_au;
	private String hora_ini;
	private String hora_fin;
	private String hora_ini_aux;
	private String hora_fin_aux;
	private Anio anio;	
	private Aula aula;	
	
	//campo auxiliar
	private Integer id_gra;
	private Integer id_niv;
	private Integer id_cic;
	private Integer id_tpe;

	public GradoHorario(){
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
	* Obtiene Ao academico 
	* @return id_anio
	*/
	public Integer getId_anio(){
		return id_anio;
	}	

	/**
	* Ao academico 
	* @param id_anio
	*/
	public void setId_anio(Integer id_anio) {
		this.id_anio = id_anio;
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
	* Obtiene Hora de inicio 
	* @return hora_ini
	*/
	public String getHora_ini(){
		return hora_ini;
	}	

	/**
	* Hora de inicio 
	* @param hora_ini
	*/
	public void setHora_ini(String hora_ini) {
		this.hora_ini = hora_ini;
	}

	/**
	* Obtiene Hora fin 
	* @return hora_fin
	*/
	public String getHora_fin(){
		return hora_fin;
	}	

	/**
	* Hora fin 
	* @param hora_fin
	*/
	public void setHora_fin(String hora_fin) {
		this.hora_fin = hora_fin;
	}

	/**
	* Obtiene (Turno 2)Hora de inicio 
	* @return hora_ini_aux
	*/
	public String getHora_ini_aux(){
		return hora_ini_aux;
	}	

	/**
	* (Turno 2)Hora de inicio 
	* @param hora_ini_aux
	*/
	public void setHora_ini_aux(String hora_ini_aux) {
		this.hora_ini_aux = hora_ini_aux;
	}

	/**
	* Obtiene (Turno 2)Hora fin 
	* @return hora_fin_aux
	*/
	public String getHora_fin_aux(){
		return hora_fin_aux;
	}	

	/**
	* (Turno 2)Hora fin 
	* @param hora_fin_aux
	*/
	public void setHora_fin_aux(String hora_fin_aux) {
		this.hora_fin_aux = hora_fin_aux;
	}

	public Anio getAnio(){
		return anio;
	}	

	public void setAnio(Anio anio) {
		this.anio = anio;
	}
	public Aula getAula(){
		return aula;
	}	

	public void setAula(Aula aula) {
		this.aula = aula;
	}

	public Integer getId_gra() {
		return id_gra;
	}

	public void setId_gra(Integer id_gra) {
		this.id_gra = id_gra;
	}

	public Integer getId_niv() {
		return id_niv;
	}

	public void setId_niv(Integer id_niv) {
		this.id_niv = id_niv;
	}

	public Integer getId_gir() {
		return id_gir;
	}

	public void setId_gir(Integer id_gir) {
		this.id_gir = id_gir;
	}

	public Integer getId_cic() {
		return id_cic;
	}

	public void setId_cic(Integer id_cic) {
		this.id_cic = id_cic;
	}

	public Integer getId_tpe() {
		return id_tpe;
	}

	public void setId_tpe(Integer id_tpe) {
		this.id_tpe = id_tpe;
	}
}
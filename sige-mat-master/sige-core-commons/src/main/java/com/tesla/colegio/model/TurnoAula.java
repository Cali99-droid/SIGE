package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_turno_aula
 * @author MV
 *
 */
public class TurnoAula extends EntidadBase{

	public final static String TABLA = "col_turno_aula";
	private Integer id;
	private Integer id_au;
	private Integer id_cit;
	private Aula aula;	
	private CicloTurno cicloturno;	

	public TurnoAula(){
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
	* Obtiene Ciclo Turno 
	* @return id_cit
	*/
	public Integer getId_cit(){
		return id_cit;
	}	

	/**
	* Ciclo Turno 
	* @param id_cit
	*/
	public void setId_cit(Integer id_cit) {
		this.id_cit = id_cit;
	}

	public Aula getAula(){
		return aula;
	}	

	public void setAula(Aula aula) {
		this.aula = aula;
	}
	public CicloTurno getCicloTurno(){
		return cicloturno;
	}	

	public void setCicloTurno(CicloTurno cicloturno) {
		this.cicloturno = cicloturno;
	}
}
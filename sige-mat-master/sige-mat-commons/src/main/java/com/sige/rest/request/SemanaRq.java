package com.sige.rest.request;

import java.io.Serializable;

/**
 * Calendario
 * @author mvalle
 *
 */
public class SemanaRq implements Serializable{
	private static final long serialVersionUID = -9136355707667563679L;
	
	
	private Integer id_au;
	private Integer id_anio;
	private Integer id_cchp;//NUEVO, col_curso_horario_pad
	private HorarioReq horarios[];
	
	public Integer getId_au() {
		return id_au;
	}
	public void setId_au(Integer id_au) {
		this.id_au = id_au;
	}
	public HorarioReq[] getHorarios() {
		return horarios;
	}
	public void setHorarios(HorarioReq[] horarios) {
		this.horarios = horarios;
	}
	public Integer getId_anio() {
		return id_anio;
	}
	public void setId_anio(Integer id_anio) {
		this.id_anio = id_anio;
	}
	public Integer getId_cchp() {
		return id_cchp;
	}
	public void setId_cchp(Integer id_cchp) {
		this.id_cchp = id_cchp;
	}
	
	

}

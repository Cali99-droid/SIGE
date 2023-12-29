package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla col_per_uni
 * @author MV
 *
 */
public class PeriodoCalificacion extends EntidadBase{

	public final static String TABLA = "col_per_uni";
	private Integer id;
	private Integer id_gra;
	private Integer id_cpu;
	private Integer id_anio;
	private Integer id_tca;
	private String letra;
	private Integer nota_ini;
	private Integer nota_fin;
	private Integer nump;
	

	public PeriodoCalificacion(){
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getId_gra() {
		return id_gra;
	}


	public void setId_gra(Integer id_gra) {
		this.id_gra = id_gra;
	}


	public Integer getId_cpu() {
		return id_cpu;
	}


	public void setId_cpu(Integer id_cpu) {
		this.id_cpu = id_cpu;
	}


	public Integer getId_anio() {
		return id_anio;
	}


	public void setId_anio(Integer id_anio) {
		this.id_anio = id_anio;
	}


	public Integer getId_tca() {
		return id_tca;
	}


	public void setId_tca(Integer id_tca) {
		this.id_tca = id_tca;
	}


	public String getLetra() {
		return letra;
	}


	public void setLetra(String letra) {
		this.letra = letra;
	}


	public Integer getNota_ini() {
		return nota_ini;
	}


	public void setNota_ini(Integer nota_ini) {
		this.nota_ini = nota_ini;
	}


	public Integer getNota_fin() {
		return nota_fin;
	}


	public void setNota_fin(Integer nota_fin) {
		this.nota_fin = nota_fin;
	}


	public Integer getNump() {
		return nump;
	}


	public void setNump(Integer nump) {
		this.nump = nump;
	}

	
}
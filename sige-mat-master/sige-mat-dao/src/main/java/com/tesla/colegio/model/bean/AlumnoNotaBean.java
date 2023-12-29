package com.tesla.colegio.model.bean;

import com.tesla.colegio.model.Nota;
import com.tesla.frmk.model.EntidadBase;

import java.math.BigDecimal;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_grad
 * @author MV
 *
 */
public class AlumnoNotaBean{

	public BigDecimal getPeso() {
		return peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}

	public Integer getId_com() {
		return id_com;
	}

	public void setId_com(Integer id_com) {
		this.id_com = id_com;
	}

	private Integer id_alu;
	public Integer getId_alu() {
		return id_alu;
	}

	public void setId_alu(Integer id_alu) {
		this.id_alu = id_alu;
	}

	public Integer getId_not() {
		return id_not;
	}

	public void setId_not(Integer id_not) {
		this.id_not = id_not;
	}

	public Integer getNota() {
		return nota;
	}

	public void setNota(Integer nota) {
		this.nota = nota;
	}

	public Integer getId_cap() {
		return id_cap;
	}

	public void setId_cap(Integer id_cap) {
		this.id_cap = id_cap;
	}

	private Integer id_not;
	private Integer nota;	
	private Integer id_cap;	
	private Integer id_com;
	private BigDecimal peso;
	private BigDecimal prom_com;
	private BigDecimal prom_per;
	private Integer id_desau;
	private Integer id_cua;

	public AlumnoNotaBean(){
	}

	@Override
	public String toString() {
		return "AlumnoNotaBean [id_alu=" + id_alu + ", id_not=" + id_not + ", nota=" + nota + ", id_cap=" + id_cap
				+ ", id_com=" + id_com + ", peso=" + peso + "]";
	}

	public BigDecimal getProm_com() {
		return prom_com;
	}

	public void setProm_com(BigDecimal prom_com) {
		this.prom_com = prom_com;
	}

	public BigDecimal getProm_per() {
		return prom_per;
	}

	public void setProm_per(BigDecimal prom_per) {
		this.prom_per = prom_per;
	}

	public Integer getId_desau() {
		return id_desau;
	}

	public void setId_desau(Integer id_desau) {
		this.id_desau = id_desau;
	}

	public Integer getId_cua() {
		return id_cua;
	}

	public void setId_cua(Integer id_cua) {
		this.id_cua = id_cua;
	}
	
}
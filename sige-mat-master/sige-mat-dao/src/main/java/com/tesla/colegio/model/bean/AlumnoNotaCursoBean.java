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
public class AlumnoNotaCursoBean{

	private BigDecimal nota;	
	private Integer id_com;
	private Integer id_cua;
	public BigDecimal getNota() {
		return nota;
	}
	public void setNota(BigDecimal nota) {
		this.nota = nota;
	}
	public Integer getId_com() {
		return id_com;
	}
	public void setId_com(Integer id_com) {
		this.id_com = id_com;
	}
	public Integer getId_cua() {
		return id_cua;
	}
	public void setId_cua(Integer id_cua) {
		this.id_cua = id_cua;
	}

	
}
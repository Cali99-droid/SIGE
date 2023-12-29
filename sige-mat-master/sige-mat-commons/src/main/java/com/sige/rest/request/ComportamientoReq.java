package com.sige.rest.request;

import java.io.Serializable;


public class ComportamientoReq implements Serializable{

	private static final long serialVersionUID = -1182003383085892015L;
	private Integer id_au;
	private Integer id_cpu;
	private Integer id_tra;
	Integer id_cap[];
	private NotaComAlumnoReq[] notaComAlumno;
	NotaComAlumnoUpdateReq[] notaComUpdate;
	public Integer getId_au() {
		return id_au;
	}
	public void setId_au(Integer id_au) {
		this.id_au = id_au;
	}
	public Integer getId_cpu() {
		return id_cpu;
	}
	public void setId_cpu(Integer id_cpu) {
		this.id_cpu = id_cpu;
	}
	public Integer getId_tra() {
		return id_tra;
	}
	public void setId_tra(Integer id_tra) {
		this.id_tra = id_tra;
	}
	
	public NotaComAlumnoReq[] getNotaComAlumno() {
		return notaComAlumno;
	}
	public void setNotaComAlumno(NotaComAlumnoReq[] notaComAlumno) {
		this.notaComAlumno = notaComAlumno;
	}
	public NotaComAlumnoUpdateReq[] getNotaComUpdate() {
		return notaComUpdate;
	}
	public void setNotaComUpdate(NotaComAlumnoUpdateReq[] notaComUpdate) {
		this.notaComUpdate = notaComUpdate;
	}
	public Integer[] getId_cap() {
		return id_cap;
	}
	public void setId_cap(Integer[] id_cap) {
		this.id_cap = id_cap;
	}

}

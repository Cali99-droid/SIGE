package com.sige.rest.request;

import java.io.Serializable;
import java.util.Date; 

public class EvaluacionReq implements Serializable{
 
	private static final long serialVersionUID = -4470656952360885399L;
	private Integer id_eva;
	private Integer id_tra;
	private Date fec;
	Integer id_ind[];
	private NotaAlumnoReq[] notaAlumno;
	NotaAlumnoUpdateReq[] notasUpdate;
	
	public NotaAlumnoReq[] getNotaAlumno() {
		return notaAlumno;
	}
	public void setNotaAlumno(NotaAlumnoReq[] notaAlumno) {
		this.notaAlumno = notaAlumno;
	}
	public Integer getId_tra() {
		return id_tra;
	}
	public void setId_tra(Integer id_tra) {
		this.id_tra = id_tra;
	}
	public Integer getId_eva() {
		return id_eva;
	}
	public void setId_eva(Integer id_eva) {
		this.id_eva = id_eva;
	}
	
	public Date getFec() {
		return fec;
	}
	public void setFec(Date fec) {
		this.fec = fec;
	}
	public Integer[] getId_ind() {
		return id_ind;
	}
	public void setId_ind(Integer[] id_ind) {
		this.id_ind = id_ind;
	}
	
	public NotaAlumnoUpdateReq[] getNotasUpdate() {
		return notasUpdate;
	}
	public void setNotasUpdate(NotaAlumnoUpdateReq[] notasUpdate) {
		this.notasUpdate = notasUpdate;
	}
	
}

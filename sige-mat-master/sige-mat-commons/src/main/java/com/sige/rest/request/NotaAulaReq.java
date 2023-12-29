package com.sige.rest.request;

import java.io.Serializable;
import java.util.Date; 

public class NotaAulaReq implements Serializable{
 
	private static final long serialVersionUID = -4470656952360885399L;
	private NotaDesempenioReq[] notaDesempenioReq;
	private NotaPromCompetenciaReq[] notaPromCompetenciaReq;
	public NotaDesempenioReq[] getNotaDesempenioReq() {
		return notaDesempenioReq;
	}
	public void setNotaDesempenioReq(NotaDesempenioReq[] notaDesempenioReq) {
		this.notaDesempenioReq = notaDesempenioReq;
	}
	public NotaPromCompetenciaReq[] getNotaPromCompetenciaReq() {
		return notaPromCompetenciaReq;
	}
	public void setNotaPromCompetenciaReq(NotaPromCompetenciaReq[] notaPromCompetenciaReq) {
		this.notaPromCompetenciaReq = notaPromCompetenciaReq;
	}
	
}

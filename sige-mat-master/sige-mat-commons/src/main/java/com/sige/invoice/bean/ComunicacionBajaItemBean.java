package com.sige.invoice.bean;

public class ComunicacionBajaItemBean {
	int linea;
	String tipoDocumento;
	String serieDocumento;
	String numDocumento;
	String motivo;
	public int getLinea() {
		return linea;
	}
	public void setLinea(int linea) {
		this.linea = linea;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getSerieDocumento() {
		return serieDocumento;
	}
	public void setSerieDocumento(String serieDocumento) {
		this.serieDocumento = serieDocumento;
	}
	public String getNumDocumento() {
		return numDocumento;
	}
	public void setNumDocumento(String numDocumento) {
		this.numDocumento = numDocumento;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	@Override
	public String toString() {
		return "{\"linea\":" + linea 
				+ ", \"tipoDocumento\":\"" + tipoDocumento + "\""
				+ ", \"serieDocumento\":\"" + serieDocumento  + "\""
				+ ", \"numDocumento\":\"" + numDocumento + "\"" 
				+ ", \"motivo\":\"" + motivo + "\"}";
	}
		
}

package com.sige.invoice.bean;

public class DocumentoReferencia {
	String nro_rec;
	String tipoDocumento;
	
	public String getNro_rec() {
		return nro_rec;
	}
	public void setNro_rec(String nro_rec) {
		this.nro_rec = nro_rec;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	
	@Override
	public String toString() {
		return "{\"nro_rec\":\"" + nro_rec + "\", \"tipoDocumento\":\"" + tipoDocumento + "\"}";
	}
	
}

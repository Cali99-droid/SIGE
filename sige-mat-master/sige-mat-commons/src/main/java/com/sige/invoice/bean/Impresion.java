package com.sige.invoice.bean;

import java.util.ArrayList;
import java.util.List;

public class Impresion {

	private ImpresionCabecera cabecera;
	private ImpresionCliente cliente;
	private List<ImpresionItem> items = new ArrayList<ImpresionItem>();
	
	
	//para  la nota de credito
	private DocumentoReferencia documentoReferencia;
	
	public List<ImpresionItem> getItems() {
		return items;
	}
	public void setItems(List<ImpresionItem> items) {
		this.items = items;
	}
	public ImpresionCabecera getCabecera() {
		return cabecera;
	}
	public void setCabecera(ImpresionCabecera cabecera) {
		this.cabecera = cabecera;
	}
	public ImpresionCliente getCliente() {
		return cliente;
	}
	public void setCliente(ImpresionCliente cliente) {
		this.cliente = cliente;
	}
	public DocumentoReferencia getDocumentoReferencia() {
		return documentoReferencia;
	}
	public void setDocumentoReferencia(DocumentoReferencia documentoReferencia) {
		this.documentoReferencia = documentoReferencia;
	}
	

	
	
}

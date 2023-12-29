package com.sige.invoice.bean;
 

public class ImpresionCabecera {
	private String nro;
	private String dia;
	private String hora;
	private String comercio;
	private String usuario;
	private String telefono;
	private String codigoBarras;
	private String local;
	private String tipo_envio;//03, 07
	private String nombreBoleta;
	private String fecha_envio;
	
	public String getNro() {
		return nro;
	}
	public void setNro(String nro) {
		this.nro = nro;
	}

	public String getComercio() {
		return comercio;
	}
	public void setComercio(String comercio) {
		this.comercio = comercio;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getCodigoBarras() {
		return codigoBarras;
	}
	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}
	public String getDia() {
		return dia;
	}
	public void setDia(String dia) {
		this.dia = dia;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}


	public String getNombreBoleta() {
		return nombreBoleta;
	}
	public void setNombreBoleta(String nombreBoleta) {
		this.nombreBoleta = nombreBoleta;
	}
	public String getTipo_envio() {
		return tipo_envio;
	}
	public void setTipo_envio(String tipo_envio) {
		this.tipo_envio = tipo_envio;
	}
	public String getFecha_envio() {
		return fecha_envio;
	}
	public void setFecha_envio(String fecha_envio) {
		this.fecha_envio = fecha_envio;
	}
	@Override
	public String toString() {
		return "{\"nro\":\"" + nro + "\", \"dia\":\"" + dia + "\", \"hora\":\"" + hora + "\", \"comercio\":\"" + comercio
				+ "\", \"usuario\":\"" + usuario + "\", \"telefono\":\"" + telefono + "\", \"codigoBarras\":\"" + codigoBarras + "\", \"local\":\""
				+ local + "\", \"fecha_envio\":\"" + fecha_envio + "\", \"tipo_envio\":\"" + tipo_envio + "\",\"nombreBoleta\":\"" + nombreBoleta + "\"}";
	} 
}

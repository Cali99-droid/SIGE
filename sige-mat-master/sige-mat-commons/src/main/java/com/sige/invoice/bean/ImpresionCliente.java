package com.sige.invoice.bean;

public class ImpresionCliente {

	
	@Override
	public String toString() {
		return "{\"tip_doc\":\"" + tip_doc + "\", \"nro_doc\":\"" + nro_doc + "\", \"nombre\":\"" + nombre + "\", \"direccion\":\""
				+ direccion + "\",\"correo\":\"" +  correo + "\"}";
	}
	private String tip_doc;
	private String nro_doc;
	private String nombre;
	private String direccion;
	private String correo;
	public String getTip_doc() {
		return tip_doc;
	}
	public void setTip_doc(String tip_doc) {
		this.tip_doc = tip_doc;
	}
	public String getNro_doc() {
		return nro_doc;
	}
	public void setNro_doc(String nro_doc) {
		this.nro_doc = nro_doc;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	
	
}

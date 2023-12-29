package com.tesla.colegio.model.bean;

public class Adjunto {
	
	String nombre;
	byte[] archivo;

	
	public Adjunto(String nombre, byte[] archivo) {
		super();
		this.nombre = nombre;
		this.archivo = archivo;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public byte[] getArchivo() {
		return archivo;
	}
	public void setArchivo(byte[] archivo) {
		this.archivo = archivo;
	}
	
	
}

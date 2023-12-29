package com.sige.common.enums;

/*
 * constantes url
 */
public enum EnumRestMatricula {

	
	URL_LISTA_X_CONTRATO( "/listxContrato");

	private final String descripcion;

	EnumRestMatricula(final String newDescripcion) {

		descripcion = newDescripcion;
	}

	public String toString() {
		return descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	 

}

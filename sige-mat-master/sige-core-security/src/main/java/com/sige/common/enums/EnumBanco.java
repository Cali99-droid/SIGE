package com.sige.common.enums;

/*
 * constantes de la tabla cat_grad
 */
public enum EnumBanco {

	BCP(1, "BANCO DE CREDITO DEL PERU"), 
	CONTINENTAL(2, "BANCO CONTINENTAL"),
	FINANCIERO(3, "BANCO FINANCIERO"); 

	private final int value;
	private final String descripcion;

	EnumBanco(final int newValue, final String newDescripcion) {
		value = newValue;
		descripcion = newDescripcion;
	}

	public int getValue() {
		return value;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public static EnumBanco banco(int n) {
		switch (n) {
		case 1:
			return EnumBanco.FINANCIERO;
		case 2:
			return EnumBanco.CONTINENTAL;
		default:
			return null;
		}
	}

}

package com.sige.common.enums;

/*
 * constantes de la tabla cat_nivel
 */
public enum EnumTipoMovimiento {

	INGRESO("I"), SALIDA("S");

	private final String value;

	EnumTipoMovimiento(final String newValue) {
		value = newValue;

	}

	public String getValue() {
		return value;
	}

}

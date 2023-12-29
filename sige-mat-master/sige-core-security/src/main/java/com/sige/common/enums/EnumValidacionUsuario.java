package com.sige.common.enums;

/*
 * constantes de la tabla alu_familiar.ini
 */
public enum  EnumValidacionUsuario {
	
	DATOS_ACTUALIZADOS("1"),
	VALIDO_CELULAR("2");

    private final String value;

    EnumValidacionUsuario(final String newValue) {
        value = newValue;
    }

    public String getValue() { return value; }
    
    
    
}

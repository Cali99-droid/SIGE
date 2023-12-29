package com.sige.common.enums;

/*
 * constantes de la tabla cat_nivel
 */
public enum  EnumTipoCondicion {
	
	ECONOMICO(1),
	CONDUCTUAL(2);

    private final int value;

    EnumTipoCondicion(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
    
    
}

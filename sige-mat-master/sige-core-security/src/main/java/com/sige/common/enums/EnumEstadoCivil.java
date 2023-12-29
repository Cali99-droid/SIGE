package com.sige.common.enums;

/*
 * constantes de la tabla cat_est_civil
 */
public enum  EnumEstadoCivil {
	
	SOLTERO(1),
    CASADO(2);

    private final int value;

    EnumEstadoCivil(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
    
    
    
}

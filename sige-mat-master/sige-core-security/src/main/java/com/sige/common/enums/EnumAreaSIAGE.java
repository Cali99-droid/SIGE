package com.sige.common.enums;

/*
 * constantes de la tabla cat_grad
 */
public enum  EnumAreaSIAGE {
	
	MATEMATICA(14),
	COMUNICACION(7),
	CIENCIA_Y_AMBIENTE(3),
	CIENCIA_Y_TECNLOGIA(4),
	PERSONAL_SOCIAL(16);
	

    private final int value;

    EnumAreaSIAGE(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
    
    
    
}

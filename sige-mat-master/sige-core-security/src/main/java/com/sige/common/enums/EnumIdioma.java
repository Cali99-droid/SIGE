package com.sige.common.enums;

/*
 * constantes de la tabla cat_est_civil
 */
public enum  EnumIdioma {
	
	NINGUNO(1),
	CASTELLANO(2),
	QUECHUA(3);

    private final int value;

    EnumIdioma(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
    
    
    
}

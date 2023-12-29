package com.sige.common.enums;

/*
 * constantes de la tabla cat_grad
 */
public enum  EnumTipoSesion {
	
	CLASE(1),
	EXAMEN(2),
	REPASO(3);	

    private final int value;

    EnumTipoSesion(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
    
    
    
}

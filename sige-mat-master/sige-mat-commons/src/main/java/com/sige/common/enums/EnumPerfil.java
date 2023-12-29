package com.sige.common.enums;

/*
 * constantes de la tabla cat_grad
 */
public enum  EnumPerfil {
	
	PERFIL_ALUMNO(7),
	PERFIL_FAMILIAR(8),
	PERFIL_TRABAJADOR(9),
	PERFIL_EXTERNO(10);

    private final int value;

    EnumPerfil(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
    
    
    
}

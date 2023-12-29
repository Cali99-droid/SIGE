package com.sige.common.enums;

/*
 * constantes de la tabla cat_grad
 */
public enum  EnumGrado {
	
	INICIAL_3_ANIOS(1),
	INICIAL_4_ANIOS(2),
	INICIAL_5_ANIOS(3),
	PRIMARIA_PRIMERO(4),
	PRIMARIA_SEGUNDO(5),
	PRIMARIA_TERCERO(6),
	PRIMARIA_CUARTO(7),
	PRIMARIA_QUINTO(8),
	PRIMARIA_SEXTO(9),
	SECUNDARIA_PRIMERO(10),
	SECUNDARIA_SEGUNDO(11),
	SECUNDARIA_TERCERO(12),
	SECUNDARIA_CUARTO(13),
	SECUNDARIA_QUINTO(14);
	

    private final int value;

    EnumGrado(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
    
    
    
}

package com.sige.common.enums;

/*
 * constantes de la tabla cat_nivel
 */
public enum  EnumNivel {
	
	INICIAL(1,"INICIAL"),
    PRIMARIA(2,"PRIMARIA"),
    SECUNDARIA(3,"SECUNDARIA");

    private final int value;
    private final String descripcion;

    EnumNivel(final int newValue, final String newDescripcion) {
        value = newValue;
        descripcion = newDescripcion;
    }

    public int getValue() { return value; }
    public String getDescripcion() { return descripcion; }
    
    
    public static EnumNivel nivel(int n){
        switch (n) {
        case 1: return EnumNivel.INICIAL;
        case 2: return EnumNivel.PRIMARIA;
        case 3: return EnumNivel.SECUNDARIA;
        default: return null;
        }
    }
    
}

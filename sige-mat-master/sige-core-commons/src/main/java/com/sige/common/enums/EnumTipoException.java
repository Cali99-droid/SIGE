package com.sige.common.enums;

/*
 * constantes de la tabla cat_nivel
 */
public enum  EnumTipoException {
	
	WARNING(1,"WARNING"),
	ERROR(2,"ERROR");

    private final int value;
    private final String descripcion;

    EnumTipoException(final int newValue, final String newDescripcion) {
        value = newValue;
        descripcion = newDescripcion;
    }

    public int getValue() { return value; }
    public String getDescripcion() { return descripcion; }
    
    
    public static EnumTipoException nivel(int n){
        switch (n) {
        case 1: return EnumTipoException.WARNING;
        case 2: return EnumTipoException.ERROR;
        default: return null;
        }
    }
    
}

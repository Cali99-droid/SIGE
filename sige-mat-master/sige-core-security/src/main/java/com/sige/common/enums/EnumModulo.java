package com.sige.common.enums;

/*
 * constantes de la tabla cat_nivel
 */
public enum  EnumModulo {
	
	MODULO_MATRICULA(1,"MODULO_MATRICULA"),
	MODULO_ACADEMICO(2,"MODULO_ACADEMICO"),
	MODULO_TESORERIA(3,"MODULO_TESORERIA") ;
    private final int value;
    private final String descripcion;

    EnumModulo(final int newValue, final String newDescripcion) {
        value = newValue;
        descripcion = newDescripcion;
    }

    public int getValue() { return value; }
    public String getDescripcion() { return descripcion; }
        
}

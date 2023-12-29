package com.sige.common.enums;

/*
 * constantes de la tabla cat_nivel
 */
public enum  EnumTipoPeriodo {
	
	ESCOLAR(1,"ESCOLAR"),
	VACANTES(2,"VACANTES"),
	RECUPERACION(3,"RECUPERACION");

    private final int value;
    private final String descripcion;

    EnumTipoPeriodo(final int newValue, final String newDescripcion) {
        value = newValue;
        descripcion = newDescripcion;
    }

    public int getValue() { return value; }
    public String getDescripcion() { return descripcion; }
    
    
    public static EnumTipoPeriodo nivel(int n){
        switch (n) {
        case 1: return EnumTipoPeriodo.ESCOLAR;
        case 2: return EnumTipoPeriodo.VACANTES;
        case 3: return EnumTipoPeriodo.RECUPERACION;
        default: return null;
        }
    }
    
}

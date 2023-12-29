package com.sige.common.enums;

/*
 * constantes de la tabla cat_situacion
 */
public enum  EnumCondicionMatricula {
	
	INGRESANTE(1,"INGRESANTE"),
	PROMOVIDO(2,"PROMOVIDO"),
	REENETRANTE(4,"REENTRANTE"),
	REPITENTE(3,"REPITENTE");

    private final int value;
    private final String descripcion;

    EnumCondicionMatricula(final int newValue, final String newDescripcion) {
        value = newValue;
        descripcion = newDescripcion;
    }

    public int getValue() { return value; }
    public String getDescripcion() { return descripcion; }
    
    
    public static EnumCondicionMatricula nivel(int n){
        switch (n) {
        case 1: return EnumCondicionMatricula.INGRESANTE;
        case 2: return EnumCondicionMatricula.PROMOVIDO;
        case 3: return EnumCondicionMatricula.REPITENTE;
        case 4: return EnumCondicionMatricula.REENETRANTE; 
        default: return null;
        }
    }
    
}

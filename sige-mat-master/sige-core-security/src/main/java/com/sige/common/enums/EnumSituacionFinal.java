package com.sige.common.enums;

/*
 * constantes de la tabla cat_situacion
 */
public enum  EnumSituacionFinal {
	
	APROBADO(1,"A"),
	REQUIERE_RECUPERACION_PEDAGOGICA(2,"RR"),
	DESAPROBADO(3,"D"),
	RETIRADO(4,"R"),
	TRASLADADO(5,"T"),
	FALLECIDO(6,"F"),
	ADELANTO_EVALUACION(7,"AE"),
	POSTERGACION_EVALUACION(8,"PP");

    private final int value;
    private final String descripcion;

    EnumSituacionFinal(final int newValue, final String newDescripcion) {
        value = newValue;
        descripcion = newDescripcion;
    }

    public int getValue() { return value; }
    public String getDescripcion() { return descripcion; }
    
    
    public static EnumSituacionFinal nivel(int n){
        switch (n) {
        case 1: return EnumSituacionFinal.APROBADO;
        case 2: return EnumSituacionFinal.REQUIERE_RECUPERACION_PEDAGOGICA;
        case 3: return EnumSituacionFinal.DESAPROBADO;
        case 4: return EnumSituacionFinal.RETIRADO;
        case 5: return EnumSituacionFinal.TRASLADADO;
        case 6: return EnumSituacionFinal.FALLECIDO;
        case 7: return EnumSituacionFinal.ADELANTO_EVALUACION;
        case 8: return EnumSituacionFinal.POSTERGACION_EVALUACION;
        default: return null;
        }
    }
    
}

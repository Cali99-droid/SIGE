package com.sige.common.enums;

/*
 * constantes de la tabla cat_nivel
 */
public enum  EnumParentesco {
	
	PARENTESCO_MAMA(1,"MAMA"),
	PARENTESCO_PAPA(2,"PAPA") ;

    private final int value;
    private final String descripcion;

    EnumParentesco(final int newValue, final String newDescripcion) {
        value = newValue;
        descripcion = newDescripcion;
    }

    public int getValue() { return value; }
    public String getDescripcion() { return descripcion; }
    
    
    public static EnumParentesco nivel(int n){
        switch (n) {
        case 1: return EnumParentesco.PARENTESCO_MAMA;
        case 2: return EnumParentesco.PARENTESCO_PAPA; 
        default: return null;
        }
    }
    
}

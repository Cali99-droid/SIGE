package com.sige.common.enums;

/*
 * constantes de la tabla cat_grad
 */
public enum  EnumURL_API {
	
	MATRICULA("api/matricula");

    private final String value;

    EnumURL_API(final String newValue) {
        value = newValue;
    }

    public String toString() { return value; }
    
    
    
}

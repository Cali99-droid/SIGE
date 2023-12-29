package com.sige.common.enums;

/*
 * constantes de la tabla fac_concepto_pago
 */
public enum  EnumConceptoPago {
	
	CODIGO_BARRAS(1,"CODIGO DE BARRAS"),
    AGENDA_CONTROL(2,"AGENDA DE CONTROL"),
    MENSUALIDAD_COLEGIO(3,"MENSUALIDAD"),
    RESERVA_MATRICULA(4,"RESERVA DE MATRICULA"),
    MATRICULA(5,"MATRICULA"),
    CUOTA_DE_INGRESO(6,"CUOTA DE INGRESO"),
	CAMBIO_DE_LOCAL(25,"CAMBIO DE LOCAL");

    private final int value;
    private final String descripcion;
    
    EnumConceptoPago(final int newValue, final String newDescripcion) {
        value = newValue;
        descripcion = newDescripcion;
    }

    public int getValue() { return value; }
    public String getDescripcion() { return descripcion; }

    public static EnumConceptoPago comcepto(int n){
        switch (n) {
        case 1: return EnumConceptoPago.CODIGO_BARRAS;
        case 2: return EnumConceptoPago.AGENDA_CONTROL;
        case 3: return EnumConceptoPago.MENSUALIDAD_COLEGIO;
        case 4: return EnumConceptoPago.RESERVA_MATRICULA; 
        case 5: return EnumConceptoPago.MATRICULA; 
        case 6: return EnumConceptoPago.CUOTA_DE_INGRESO; 
        case 25: return EnumConceptoPago.CAMBIO_DE_LOCAL; 
        default: return null;
        }
    }
    
}

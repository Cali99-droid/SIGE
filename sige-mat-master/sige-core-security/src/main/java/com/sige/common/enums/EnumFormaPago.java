package com.sige.common.enums;

/*
 * constantes de la tabla fac_concepto_pago
 */
public enum  EnumFormaPago {
	
	EFECTIVO(1),
    DEPOSITO_BANCO(2);

    private final int value;

    EnumFormaPago(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}

package com.tesla.frmk.util;

import java.math.BigDecimal;

public class NumberUtil {

	/**
	 * Devuelve formato ###0.00
	 * 
	 * @param cantidad
	 * @return
	 */
	public static String toString(BigDecimal cantidad) {

		return cantidad.setScale(2, BigDecimal.ROUND_UP).toString();
	}

	public static String toTexto(int cantidad) {
		int num = cantidad;
		String res;

		N2t numero;
		numero = new N2t(num);
		res = numero.convertirLetras(num);
		return res;
	}

	public static String toTexto(BigDecimal cantidad) {
		int enteros = cantidad.intValue();
		String enteroTexto;
		String decimalesTexto;
		
		BigDecimal decimales = cantidad.remainder(BigDecimal.ONE);
		decimales = decimales.multiply(new BigDecimal(100));
		
		enteroTexto = toTexto(enteros);
		decimalesTexto = toTexto(decimales.intValue());
		
		
		return enteroTexto + "con " + decimales.intValue() + "/100";
	}
	
	public static void main(String args[]) {

	}

}

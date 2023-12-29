package com.tesla.frmk.util;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;

import com.tesla.colegio.util.Constante;

public class FechaUtil {

	public static String toString(Date date) {

		return (new SimpleDateFormat("dd/MM/yyyy")).format(date);
	}

	public static String toString(Date date, String format) {

		return (new SimpleDateFormat(format)).format(date);
	}

	public static Date toDate(String fecha, String format) throws Exception {

		SimpleDateFormat formatter = new SimpleDateFormat(format);

		try {
			return formatter.parse(fecha);
		} catch (Exception e) {
			throw new Exception("No se puede convertir la fecha:" + fecha + " al formato:" + format);
		}

	}

	/*public static Time toDatetime(String hora) throws Exception {

		if (hora == null)
			return null;

		SimpleTimeZone formatter = new SimpleTimeZone("HH:mm");

		try {
			return formatter.parse(hora);
		} catch (Exception e) {
			throw new Exception("No se puede convertir la hora:" + hora + " al formato:'HH:mm'");
		}

	}*/
	
	public static Date toDate(String fecha) throws Exception {

		if (fecha == null)
			return null;

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		try {
			return formatter.parse(fecha);
		} catch (Exception e) {
			throw new Exception("No se puede convertir la fecha:" + fecha + " al formato:'dd-MM-yyyy'");
		}

	}

	public static String toStringMYQL(Date date) {

		return (new SimpleDateFormat("yyyy-MM-dd")).format(date);
	}

	public static String toText(Date date) {
		int dia = Integer.valueOf((new SimpleDateFormat("dd")).format(date));
		int mes = Integer.valueOf((new SimpleDateFormat("MM")).format(date));
		String anio = (new SimpleDateFormat("yyyy")).format(date);
		String mesString = Constante.MES[mes - 1];

		return dia + " de " + mesString + " del " + anio;
	}

	/**
	 * Devuelve en booleano si es sabado o domingo
	 * 
	 * @param date
	 */
	public static boolean isSabOrDom(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day = c.get(Calendar.DAY_OF_WEEK);
		
		return (day == Calendar.SATURDAY || day == Calendar.SUNDAY);

	}
	
	/**
	 * Obtiene la diferencia en segundos entre dos fechas
	 * @param dateIni
	 * @param dateFin
	 * @return
	 */
	public static Integer diffSegundos(Date dateIni, Date dateFin) {
	    Long diffInMillies = Math.abs(dateFin.getTime() - dateIni.getTime())/1000;
	    return (diffInMillies).intValue();

	}
}

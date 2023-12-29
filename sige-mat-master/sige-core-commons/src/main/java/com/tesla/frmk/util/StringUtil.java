package com.tesla.frmk.util;

import java.security.SecureRandom;
import java.util.Random;

public class StringUtil {

	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static final String ABC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static final String NUMBER = "0123456789";
	static SecureRandom rnd = new SecureRandom();

	public static String randomString( int len ){
	   StringBuilder sb = new StringBuilder( len );
	   for( int i = 0; i < len; i++ ) 
	      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
	   return sb.toString();
	}

	public static String randomInt( int len ){
		   StringBuilder sb = new StringBuilder( len );
		   for( int i = 0; i < len; i++ ) 
		      sb.append( NUMBER.charAt( rnd.nextInt(NUMBER.length()) ) );
		   return sb.toString();
		}

	public static String randomStringLessComplex( int len ){
		   StringBuilder sb = new StringBuilder( len );
		   for( int i = 0; i < len; i++ ) 
		      sb.append( ABC.charAt( rnd.nextInt(ABC.length()) ) );
		   return sb.toString();
		}

	
	public static String replaceTilde(String cadena){
		return cadena.replace("Á", "A").replace("É", "E").replace("Í", "I").replace("Ó", "O").replace("Ú", "U").replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
	}
	
	public static String randomString2( int maxLength) {
		Random random = new Random(); // Or SecureRandom
		int startChar = (int) 'A';
		int endChar = (int) 'z';
		
		  final int length = random.nextInt(maxLength + 1);
		  String token = random.ints(length, startChar, endChar + 1)
		        .mapToObj((i) -> (char) i)
		        .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
		        .toString();
		  
		  return token.replaceAll("/", "").replaceAll("\\", "");
		}

	public static String espacioBlanco(int cantidad){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<cantidad;i++)
			sb.append(" ");
		
		return sb.toString();
	}

	public static String repiteCaracter(int cantidad,String caracter){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<cantidad;i++)
			sb.append(caracter);
		
		return sb.toString();
	}
	
	
	
	public static String rellenaVacio(String cadena, int cantidad){

		if(cadena.length()>cantidad)
			return cadena.substring(0, cantidad);
		
		return cadena + espacioBlanco(cantidad-cadena.length());
	}
	
	public static String rellenaVacioIzquierda(String cadena, int cantidad){

		if(cadena.length()>cantidad)
			return cadena.substring(0, cantidad);
		
		return espacioBlanco(cantidad-cadena.length()) + cadena;
	}
	
	public static void main(String args[]){

		
	}
	
	public static String rellenaCaracterIzq(String cadena, int cantidad,String relleno){

		if(cadena.length()>cantidad)
			return cadena.substring(0, cantidad);
		
		return repiteCaracter(cantidad- cadena.length(), relleno) + cadena;
	}
	
	public static String capitalize(String string){
		
		if(string!=null)
			string = string.trim().toLowerCase();

		return string.substring(0, 1).toUpperCase() + string.substring(1);  
	}

	public static String orden(int nro){
		if (nro==1)
			return "Primer";
		else if (nro==2)
			return "Segundo";
		else if (nro==3)
			return "Tercer";
		else if (nro==4)
			return "Cuarto";
		else if (nro==5)
			return "Quinto";
		else if (nro==6)
			return "Sexto";
		else if (nro==7)
			return "Septimo";
		else if (nro==8)
			return "Octavo";
		else if (nro==9)
			return "Noveno";
		else if (nro==10)
			return "Decimo";
		else
			return "";
		
	}
	
}
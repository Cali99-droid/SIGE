package com.sige.common.enums;

/*
 * constantes de la tabla cat_nivel
 */
public enum  EnumParametro {
	
	DIRECCION_LATITUD(1,"DIRECCION_LATITUD"),
	DIRECCION_LONGITUD(2,"DIRECCION_LONGITUD"),
	CODIGO_BARRAS_DNI(3,"CODIGO_BARRAS_DNI"),
	DIRECCION_DEPARTAMENTO(4,"DIRECCION_DEPARTAMENTO"),
	DIRECCION_PROVINCIA(5,"DIRECCION_PROVINCIA"),
	SERVICIO_HUELLA(6,"SERVICIO_HUELLA"),
	RUTA_PLANTILLA(7,"RUTA_PLANTILLA"),
	URL_SERVIDOR_EXTERNO(8,"URL_SERVIDOR_EXTERNO"),
	VACANTES(9,"VACANTES"),
	ANIO_SETUP(10,"ANIO_SETUP"),
	COORDINADOR_ACADEMICO(11,"COORDINADOR_ACADEMICO"),
	PAGO_MENSUALIDADES(12,"PAGO_MENSUALIDADES");

    private final int value;
    private final String descripcion;

    EnumParametro(final int newValue, final String newDescripcion) {
        value = newValue;
        descripcion = newDescripcion;
    }

    public int getValue() { return value; }
    public String getDescripcion() { return descripcion; }
    
    
  
}

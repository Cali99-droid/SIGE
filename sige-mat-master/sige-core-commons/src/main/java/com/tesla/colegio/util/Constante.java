package com.tesla.colegio.util;

public class Constante {
 	
	public static final Integer TIPO_DOCUMENTO_DNI= new Integer(1);
	public static final Integer TIPO_DOCUMENTO_PASAPORTE= new Integer(2);
	public static final Integer TIPO_DOCUMENTO_CARNET_EXTRANJERIA= new Integer(3);
	public static final String GENERO_MASCULINO= "1";
	public static final String GENERO_FEMENINO= "0";
	
	public static final String BUSQUEDA_FAMILIA_DNI= "DNI";
	public static final String BUSQUEDA_FAMILIA_DNI_AMBOS= "DNI_AMBOS";
	public static final String BUSQUEDA_FAMILIA_NOMBRE= "NOMBRE";
	public static final String BUSQUEDA_FAMILIA_HUELLA= "HUELLA";
	public static final String DESCUENTO_EINSTINO= "DSCTO EINSTINO";
	
	public static final Integer TIPO_EVALUACION_ESCRITO		= 3;
	public static final Integer TIPO_EVALUACION_CRITERIO	= 2;
	public static final Integer TIPO_EVALUACION_MARCACION	= 1;
	
	public static final Integer TIPO_PERIODO_ESCOLAR 		= 1;
	public static final Integer TIPO_PERIODO_VACANTES 		= 2;
	public static final Integer TIPO_PERIODO_RECUPERACION 	= 3;
	
	public static final Integer SITUACION_FINAL_APROBADO	= 1;
	public static final Integer SITUACION_FINAL_REQUIERE_RECUPERACION= 2;
	public static final Integer SITUACION_FINAL_DESAPROBADO	= 3;
	public static final Integer SITUACION_FINAL_RETIRADO	= 4;
	public static final Integer SITUACION_FINAL_TRASLADADO	= 5;
	public static final Integer SITUACION_FINAL_FALLECIDO 	= 6;
	public static final Integer SITUACION_FINAL_ADELANDO_EVALUCACION= 7;
	public static final Integer SITUACION_FINAL_POSTERFACION_EVALUACION= 8;

	//condicion matricula
	public static final Integer CONDICION_MATRICULA_INGRESANTE =1;
	//public static final Integer CONDICION_MATRICULA_INGRESANTE =1;

	public static final Integer NIVEL_INICIAL = 1;
	public static final Integer NIVEL_PRIMARIA= 2;
	public static final Integer NIVEL_SECUNDARIA = 3;
	
	//condicion matricula
	public static final Integer CLIENTE_NUEVO=1;
	public static final Integer CLIENTE_REENTRANTE=3;
	public static final Integer CLIENTE_ALUMNO=4;
	
	//tipo de periodo
	public static final Integer CONDICION_ESCOLAR =1;
	
	//tipos de pago
	public static final String PAGO_MENSUAL ="MEN";
	public static final String PAGO_CUOTA_INGRESO ="ING";
	public static final String PAGO_MATRICULA ="MAT";
	public static final String PAGO_RESERVA ="RES";
	public static final String PAGO_CAMBIO_LOCAL ="LOC";
	public static final String DEVOLUCION_CAMBIO_LOCAL ="DEV_LOC";
	public static final String PAGO_OTROS ="OTR";
	public static final String DESCUENTO ="DESC";

	public static final Integer CONCEPTO_MENSUALIDAD =3;// ESTA DATO ES INAMOVIVLE
	public static final Integer CONCEPTO_PAGO_ACADEMIA =22;// ESTA DATO ES INAMOVIVLE
	public static final Integer CONCEPTO_PAGO_VACACIONES =26;// ESTA DATO ES INAMOVIVLE

	//roles
	public static final Integer ROL_COORDINADOR_AREA=7;
	
	//perfiles
	
	//TIPO DE CONDICION MARTRICUL ALUMNO
	public static final String TIPO_ECONOMICO="ECONMICO";
	public static final String TIPO_CONDUCTUAL="CONDUCTUAL";
	
	public static final Integer TIPO_SESION_EXAMEN=2;
	
	//PARENETESCO
	public static final Integer PARENTESCO_MAMA = new Integer(1);
	public static final Integer PARENTESCO_PAPA = new Integer(2);
	
	//PERFIL
	public static final Integer PERFIL_TRABAJADOR = new Integer(9);
	public static final Integer PERFIL_FAMILIAR = new Integer(8);
	
	//TIPO DE FECHA VENCIMIENTO
	public static final String TIPO_FEC_VEN_DIA ="D";
	public static final String TIPO_FEC_VEN_FIN ="F";
	
	//TIPO DE ESTADOS DE LOS MENSAJES
	public static final String ESTADO_MENSAJE_ENVIADO ="E";
	public static final String ESTADO_MENSAJE_LEIDO ="L";
	public static final String ESTADO_MENSAJE_ARCHIVADO ="A";
	public static final String ESTADO_MENSAJE_ELIMINADO ="E";
	
	//TIPO DE ESTADOS DE LOS MENSAJES
	public static final Integer TIPO_CALIFICACION_CUALITATIVA =new Integer(1);
	public static final Integer TIPO_CALIFICACION_CUANTITATIVA =new Integer(2);
	
	//PARAMETROS VISA
	public static final String USER ="integraciones.visanet@necomplus.com";
	public static final String PASS ="d5e7nk$M";
	public static final String MERCHANTIDTEST ="522591303";
	public static final String URL_API_AUTHORIZATION_DESA="https://apitestenv.vnforapps.com/api.security/v1/security";
	public static final String URL_API_AUTHORIZATION_PROD="https://apiprod.vnforapps.com/api.security/v1/security";

	//SEGURIDAD
	public static final String[] URL_PUBLICOS = new String[]{
			//"/api/seguridad/usuario/logout",
			"/api/seguridad/login",
			//"/api/alumno/autocomplete",
			"/api/seguridad/validarCorreoToken",
			"/api/mensajeriaFamiliar/pendientes", 
			"/api/mensajeriaFamiliar/leidos", 
			"/api/seguridad/recover",
			"/api/seguridad/change",
			"/api/envioResumenDiarioJOB",
			//"/api/alumno/foto/",
			//"/api/familiar/foto",
			"/api/nota/libreta",
			"/api/seguimientoDoc/reimprimir/",
			"/api/codigoBarras/alumno",
			"/api/codigoBarras/seccion",
			"/api/movimiento/pdf/boleta",
			"/api/banco/xls",
			"/api/banco/genArchivoAnual",
			"/api/cursoUnidad/unidadAprendizajePDF",
			"/api/reporte/registro_auxiliares",
			"/api/tema/excel",
			"/api/cursoAula/programacionAnual",
			"/api/reporteCaja/excel",
			"/api/reporte/registro_asistencia_mensual",
			"/api/cursoUnidad/xls",
			"/api/cursoUnidad/pdf",
			"/api/familiar/exportarClaves",
			"/api/reserva/imprimir",
			"/api/facturaElectronica/reporte/xls",
			"/api/concurso",
			"/Foto/familiar",
			"/api/public/",
			//"/api/comboCache/",
			"/api/pagos/carta_cobranza1",
			"/api/pagos/excel",
			"/api/codigoBarras/accesoIntranet",
			"api/resultados/formato/",
			"/api/matricula/generarContrato",
			"/api/matricula/exportarDirectorioPPFF",
			"/api/insLote/xls/uploadInsInd",
			"/api/resultados/xls/upload",
			"/api/alumno/exportarFormatoUsuariosAlu",
			"/api/familiar/enviarAceptacinMatricula",
			"/api/pagoLinea",
			"/api/archivo/obtenerDatosPersonaxNroDoc/",
			"/api/archivo/existeAlumnoSeminario",
			"/api/archivo/imprimirCarnetSeminario",
			"/api/archivo/grabarInscripcionSeminario"
			//"/api/banco/xls/vistaPrevia"
			};
	

	public static final int MINUTOS_EXPIRACION = 30;
	public static final int MINUTOS_AVISO_PARA_EXPIRAR = 2;
	
	
	public static final String[] MES ={"Enero", "Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Setiembre","Octubre","Noviembre","Diciembre"};
	public static final Integer TIPO_CONCEPTO_MATRICULA=5;
	public static final Integer TIPO_CONCEPTO_CUOTA_ING=6;
	public static final Integer TIPO_CONCEPTO_MENSUALIDAD=3;
	public static final Integer TIPO_CONCEPTO_RESERVA=4;

	public static final Integer TIPO_PERIODOES=1;
	public static final Integer TIPO_PERIODOVAC=2;
	public static final String ESTADO="A";
	//TIPO DE CONDICION MARTRICUL ALUMNO
 
	//TIPO DE INCIDENCIA
	public static final Integer INCIDENCIA_TIPO_CONDUCTUAL=1;
	public static final Integer INCIDENCIA_TIPO_BULLING=2;
	
	//GIROS DE NEGOCIO DE LA EMPRESA
	public static final Integer GIRO_COLEGIO=1;
	public static final Integer GIRO_EMPRESA=2;
	public static final Integer GIRO_ACADEMIA=2;
	public static final Integer GIRO_VACACIONES=3;
	
	//MODULOS
	public static final Integer MODULO_MATRICULA=1;
	public static final Integer MODULO_ACADEMICO=2;
	public static final Integer MODULO_TESORERIA=3;
	
	//REGLAS DE NEGOCIO
	public static final String MATR_SEGUN_CRONOGRAMA="MATR_SEGUN_CRO";
	public static final String RES_MAT_NU_OBL="RES_MAT_NU_OBL";
	public static final String RESTR_MAT_DEUDA="RESTR_MAT_DEUDA";
	public static final String VAL_MATR_COND="VAL_MATR_COND";
	public static final String PAGO_OBL_MATR="PAGO_OBL_MATR";
	public static final String ADMI_OBL_NUEVOS="ADMI_OBL_NUEVOS";
	
}

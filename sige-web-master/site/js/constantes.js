
var _URL = null;
var _URL_CON = "/sige-con/";
var _URL_OLI = "/sige-oli/";
var _URL_SIGE = "localhost:8081/sige-mat/";
var _AMBIENTE = 'D';
var _CACHE = false;
var _TRIMESTRE = 2;//ID
var _BIMESTRE = 1;
var _ARRAY_BIMESTRE = [{id:"1", value:"1er Bimestre"},{id:"2", value:"2do Bimestre"},{id:"3", value:"3er Bimestre"},{id:"4", value:"4to Bimestre"}];
var _ARRAY_TRIMESTRE = [{id:"1", value:"1er Trimestre"},{id:"2", value:"2do Trimestre"},{id:"3", value:"3er Trimestre"}];

var _MES =['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Setiembre','Octubre','Noviembre','Diciembre'];
//ESTADOS DE VALIDACION DE USUARIO
var _ESTADO_ACTUALIZADO = '1';
var _ESTADO_CELULAR = '2';
var _ESTADO_CAMBIO_CLAVE = '3';
var _CORREO_VALIDADO = '1';

var _ROL_ADMINISTRADOR=1;
var _ROL_SECRETARIA=2;
var _ROL_PSICOLOGO=3;
var _ROL_OPERADOR=4;
var _ROL_AUXILIAR=5;
var _ROL_PROFESOR=6;
var _ROL_COORDINADOR_AREA=7;
var _ROL_ASISTENTE=9;
var _ROL_CONTADOR=10;
var _ROL_COORDINADOR_TUTOR=11;
var _ROL_COORDINADOR_NIVEL=12;
var _ROL_VIGILANTE=13;
var _ROL_SECRETARIA_OLI=14;
var _ROL_ADMIN_OLI=15;
var _ROL_RECURSOS_HUMANOS=16;
var _ROL_SOPORTE_TECNOLOGICO=18;
var _ROL_TUTOR=19;
var _ROL_ADMINISTRADOR_SEDE=20;
var _ROL_SOPORTE_OLI=21;
var _ROL_ASIST_ADMINISTRATIVO=22;

var _PERFIL_ALUMNO=7;
var _PERFIL_FAMILIAR=8;
var _PERFIL_TRABAJADOR=9;
var _PERFIL_EXTERNO=10;

var _PARENTESCO_MADRE=1;
var _PARENTESCO_PADRE=2;


var _CONCEPTO_CODIGO_BARRAS=1;
var _ACCESO_INTRANET=14;

var _MODALIDAD_COSTO_OLI=1;

function _init() {
	var origin = window.location.origin;
	//_URL = origin.substring(0,origin.lastIndexOf(':'))+ ":8081/sige-mat/";
	//alert("1"+window.location.origin);
	//alert("2"+origin.lastIndexOf(':'));
	//alert("3"+origin.substring(0,origin.lastIndexOf(':')));
	_URL = window.location.origin.replace(':8080','')+ ":8081/sige-mat/";
	//console.log(_URL);
    //juan_URL = "http://192.34.61.74:8081/sige-mat/";
	//URLVPS   _URL = window.location.origin+ ":8081/sige-mat/";
};
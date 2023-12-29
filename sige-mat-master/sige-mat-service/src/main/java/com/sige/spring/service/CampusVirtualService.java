package com.sige.spring.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sige.rest.request.ErrorReq;
import com.sige.rest.request.GrupoAulaVirtualReq;
import com.sige.rest.request.MatriculaAulaVirtualReq;
import com.sige.rest.request.UsuarioClassroomEnrollReq;
import com.sige.rest.request.UsuarioGoogleReq;
import com.sige.rest.request.UsuarioGrupoReq;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.Cliente;
import com.tesla.colegio.model.CondMatricula;
import com.tesla.colegio.model.Contador;
import com.tesla.colegio.model.CursoAnio;
import com.tesla.colegio.model.CursoAula;
import com.tesla.colegio.model.Error;
import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.GiroNegocio;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.GruFam;
import com.tesla.colegio.model.GrupoAlumno;
import com.tesla.colegio.model.GrupoAulaVirtual;
import com.tesla.colegio.model.GrupoConfig;
import com.tesla.colegio.model.InsExaCri;
import com.tesla.colegio.model.InscripcionCampus;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.MensajeriaFamiliar;
import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.Parametro;
import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.Persona;
import com.tesla.colegio.model.Reserva;
import com.tesla.colegio.model.SigeUsuarios;
import com.tesla.colegio.model.Usuario;
import com.tesla.colegio.model.UsuarioCampus;
import com.tesla.colegio.model.UsuarioNivel;
import com.tesla.colegio.model.bean.Adjunto;
import com.sige.mat.dao.AlumnoAulaDAO;
import com.sige.mat.dao.AlumnoDAO;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.AulaDAO;
import com.sige.mat.dao.CicloDAO;
import com.sige.mat.dao.ContadorDAO;
import com.sige.mat.dao.CursoAnioDAO;
import com.sige.mat.dao.CursoAulaDAO;
import com.sige.mat.dao.EmpresaDAO;
import com.sige.mat.dao.ErrorDAO;
import com.sige.mat.dao.FamiliarDAO;
import com.sige.mat.dao.GiroNegocioDAO;
import com.sige.mat.dao.GruFamDAO;
import com.sige.mat.dao.GrupoAlumnoDAO;
import com.sige.mat.dao.GrupoAulaVirtualDAO;
import com.sige.mat.dao.GrupoConfigDAO;
import com.sige.mat.dao.InscripcionCampusDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.MensajeriaFamiliarDAO;
import com.sige.mat.dao.ParametroDAO;
import com.sige.mat.dao.PersonaDAO;
import com.sige.mat.dao.SigeUsuariosDAO;
import com.sige.mat.dao.UsuarioCampusDAO;
import com.sige.mat.dao.UsuarioDAO;
import com.sige.mat.dao.UsuarioNivelDAO;
import com.tesla.colegio.util.Constante;
import com.tesla.colegio.util.FileUtil;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.CorreoUtil;

import com.tesla.frmk.util.ExcelXlsUtil;
import com.tesla.frmk.util.RestUtil;
import com.tesla.frmk.util.StringUtil;


@Service
public class CampusVirtualService {
	
	@Autowired
	private InscripcionCampusDAO inscripcionCampusDAO;
	
	@Autowired
	private UsuarioCampusDAO usuarioCampusDAO;
	
	@Autowired
	private AlumnoDAO alumnoDAO;
	
	@Autowired
	private FamiliarDAO familiarDAO;
	
	@Autowired
	private TokenSeguridad tokenSeguridad;
		
	@Autowired
	private ErrorDAO errorDAO;
	
	@Autowired
	private ParametroDAO parametroDAO;
	
	@Autowired
	private GruFamDAO gruFamDAO;
	
	@Autowired
	private GrupoConfigDAO grupoConfigDAO;
	
	@Autowired
	private GrupoAulaVirtualDAO grupoAulaVirtualDAO;
	
	@Autowired
	private GrupoAlumnoDAO grupoAlumnoDAO;
	
	@Autowired
	private EmpresaDAO empresaDAO;
	
	@Autowired
	private PagosService pagosService;
	
	@Autowired
	private MatriculaDAO matriculaDAO;
	
	@Autowired
	private SigeUsuariosDAO sigeUsuariosDAO;
	
	@Autowired
	private PersonaDAO personaDAO;
	
	@Autowired
	private GiroNegocioDAO giroNegocioDAO;
	
	@Autowired
	private AulaDAO aulaDAO;
	
	@Autowired
	private AnioDAO anioDAO;
	
	@Autowired
	private CursoAnioDAO cursoAnioDAO;
	
	@Autowired
	private CursoAulaDAO cursoAulaDAO;
	
	@Autowired
	private CicloDAO cicloDAO;
	
	@Autowired
	private ContadorDAO contadorDAO;
	
	@Autowired
	private MensajeriaFamiliarDAO mensajeriaFamiliarDAO;
	
	@Transactional
	public void grabarInscripcion(String id_alu[], InscripcionCampus inscripcionCampus,Integer id_anio){	
		
		try {
			//Obtenemos la lista de los alumnos por apoderado,
			List<Row> alumnos=alumnoDAO.listarAlumnosMatriculados(inscripcionCampus.getId_fam(), id_anio);
			Integer cant_alu=alumnos.size();
			final List<String> idAluForm = Arrays.asList(id_alu);
			for (Row row : alumnos) {
				if(idAluForm.contains(row.getInteger("id").toString())){
					//Buscamos si ya tiene una inscripci�n, puede ser que antes no acepto, y ahora reci�n acepte
					Param param = new Param();
					param.put("id_alu", row.getInteger("id"));
					param.put("id_anio", id_anio);
					InscripcionCampus inscripcionCampus2=inscripcionCampusDAO.getByParams(param);
					if(inscripcionCampus2!=null){
						inscripcionCampus.setId(inscripcionCampus2.getId());
					}
					inscripcionCampus.setId_alu(row.getInteger("id"));
					//Grabamos la inscripci�n al campus virtual al sige
					Integer id_ins=inscripcionCampusDAO.saveOrUpdate(inscripcionCampus);
					
					//Insertamos el usuario, solo de los que aceptaron los terminos
					if(inscripcionCampus.getTc_acept().equals("1")){
						
						Row matricula = matriculaDAO.getMatricula(row.getInteger("id"), id_anio);
						
						//Modificamos su pagos
						pagosService.procesarTarifaEmergenciaxAlumno(matricula.getInteger("id"));
						
						//Obtenemos el usuario del alumno
						Alumno alumno	=	alumnoDAO.getByParams(new Param("id",row.getInteger("id")));
						//System.out.println(alumno);
						String usuario = alumno.getUsuario();
						String nom_alu=StringUtil.replaceTilde(alumno.getNom()).toLowerCase().replace("�", "n");
						String ape_pat=StringUtil.replaceTilde(alumno.getApe_pat()).toLowerCase().replace("�", "n");
						String ape_mat=StringUtil.replaceTilde(alumno.getApe_mat()).toLowerCase().replace("�", "n");
						String clave = generarClave(nom_alu.substring(0, nom_alu.indexOf(" ") == -1 ? nom_alu.length() : nom_alu.indexOf(" ")), ape_pat.substring(0, ape_pat.indexOf(" ") == -1 ? ape_pat.length() : ape_pat.indexOf(" ")), 2);
						//Primero creamos el usuario en el google
						
						if(usuario==null){
							Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);
							usuario=StringUtil.replaceTilde(alumno.getNom().replace(" ","")).toLowerCase().replace("�", "n").replace(" ", "")+"."+StringUtil.replaceTilde(alumno.getApe_pat()).toLowerCase().replace("�", "n").replace(" ", "")+"@"+empresa.getString("dominio");
							Boolean existe_usuario=alumnoDAO.existeUsuario(usuario);
							if(existe_usuario){
								usuario=StringUtil.replaceTilde(alumno.getNom().replace(" ","")).toLowerCase().replace("�", "n").replace(" ", "")+"."+StringUtil.replaceTilde(alumno.getApe_pat()).toLowerCase().replace("�", "n").replace(" ", "")+"1@"+empresa.getString("dominio");
								existe_usuario=alumnoDAO.existeUsuario(usuario);
								if(existe_usuario){
									usuario=StringUtil.replaceTilde(alumno.getNom().replace(" ","")).toLowerCase().replace("�", "n").replace(" ", "")+"."+StringUtil.replaceTilde(alumno.getApe_pat()).toLowerCase().replace("�", "n").replace(" ", "")+"2@"+empresa.getString("dominio");
									existe_usuario=alumnoDAO.existeUsuario(usuario);
									if(existe_usuario){
										usuario=StringUtil.replaceTilde(alumno.getNom().replace(" ","")).toLowerCase().replace("�", "n").replace(" ", "")+"."+StringUtil.replaceTilde(alumno.getApe_pat()).toLowerCase().replace("�", "n").replace(" ", "")+"3@"+empresa.getString("dominio");
										existe_usuario=alumnoDAO.existeUsuario(usuario);
										if(existe_usuario){
											usuario=StringUtil.replaceTilde(alumno.getNom().replace(" ","")).toLowerCase().replace("�", "n").replace(" ", "")+"."+StringUtil.replaceTilde(alumno.getApe_pat()).toLowerCase().replace("�", "n").replace(" ", "")+"4@"+empresa.getString("dominio");	
										}
									}
								}
							} 
							
						}
						
						//Insertamos el usuario
						alumnoDAO.actualizarUsuarioAlumno(row.getInteger("id"), usuario,clave);
						Param param2 = new Param();
						param2.put("nom", "NOMBRE_COLEGIO");
						Parametro colegio=parametroDAO.getByParams(param2);
						String path=null;
						if(matricula.getString("nivel").equals("INICIAL")){
							path="/"+colegio.getVal()+"/"+matricula.getString("nivel")+"/"+matricula.getString("grado");	
						} else if(matricula.getString("nivel").equals("SECUNDARIA")){
							path="/"+colegio.getVal()+"/"+matricula.getString("nivel")+"/"+matricula.getString("orden")+". "+matricula.getString("grado");
						} /*else if( matricula.getString("nivel").equals("ACADEMIA") ) {
							//Academia/PRE/CIENCIAS/CIENCIAS1
							//Academia Encinas/CicloVerano2021/Ciencias/Ciencias1
							path="/"+colegio.getVal()+"/"+matricula.getString("nivel")+"/"+matricula.getString("orden")+". "+matricula.getString("grado");
						}*/
						

						String json="{"
								+"\"nombres\": \""+alumno.getNom()+"\","
								+"\"apellidos\": \""+alumno.getApe_pat()+" "+alumno.getApe_mat()+"\","
								+"\"email\": \""+usuario+"\","
								+"\"password\": \""+clave+"\","
								+"\"path\": \""+path+"\"}";
						
						RestUtil restUtil = new RestUtil();
						
						Object respuesta=restUtil.requestPOST("http://login.ae.edu.pe:8081/sige-google-api/user/createv2", "POST", json);
						
						Gson gson = new Gson();
						UsuarioClassroomEnrollReq usuarioClassroomEnrollReq = gson.fromJson(respuesta.toString(), UsuarioClassroomEnrollReq.class);
						if(usuarioClassroomEnrollReq.getErrors()==null){
							UsuarioCampus usuarioCampus= new UsuarioCampus();
							usuarioCampus.setId_cvic(id_ins);
							usuarioCampus.setEst("A");
							usuarioCampus.setUsr(usuario);
							usuarioCampus.setPsw(clave);
							
							Integer id_usr=usuarioCampusDAO.saveOrUpdate(usuarioCampus);	
							
							//Insertar en la tabla sige usuarios
							SigeUsuarios sigeUsuarios= new SigeUsuarios();
							sigeUsuarios.setId_alu(alumno.getId());
							sigeUsuarios.setNombres(alumno.getNom());
							sigeUsuarios.setApellidos(alumno.getApe_pat()+" "+alumno.getApe_mat());
							sigeUsuarios.setCorreo(usuario);
							sigeUsuarios.setClave(clave);
							sigeUsuarios.setEst("A");
							sigeUsuariosDAO.saveOrUpdate(sigeUsuarios);
							
							 String idUsuario= usuarioClassroomEnrollReq.getIdUsuario();
							//Actualizamos el idGoogle 
							 alumnoDAO.actualizarIdClasroomAlumno(idUsuario, usuario);
							 /**Comentado por ahora

							 //Insertar en el moodle
							 String json_moodle="{"
										+"\"nombres\": \""+alumno.getNom()+"\","
										+"\"apellidos\": \""+alumno.getApe_pat()+" "+alumno.getApe_mat()+"\","
										+"\"correo\": \""+usuario+"\","
										+"\"clave\": \""+clave+"\"}";
							 
							Object respuesta_moodle=restUtil.requestPOST("http://ae.edu.pe:8081/sige-moodle-api/users", "POST", json_moodle);
							Gson gson2 = new Gson();
							UsuarioClassroomEnrollReq matriculaMoodle = gson2.fromJson(respuesta_moodle.toString(), UsuarioClassroomEnrollReq.class);
							if(matriculaMoodle.getMessage()!=null){
								Gson gsonerror = new Gson();
								ErrorReq errorReq = gsonerror.fromJson(respuesta.toString(), ErrorReq.class);							
								Error error = new Error();
								error.setSql_code(errorReq.getCode());
								error.setError("Error en Inserci�n del Usuario en Moodle: "+alumno.getUsuario()+" "+errorReq.getMessage());
								error.setId_cvi(id_ins);
								//error.setError("Error en Creaci�n de Usuario: "+alumno.getUsuario()+" "+respuesta.toString());
								error.setEst("A");
								errorDAO.saveOrUpdate(error);
							}							  * 
							  */
						} else {
							Gson gson2 = new Gson();
							ErrorReq errorReq = gson2.fromJson(respuesta.toString(), ErrorReq.class);							
							Error error = new Error();
							error.setSql_code(errorReq.getCode());
							error.setError("Error en Creaci�n de Usuario: "+alumno.getUsuario()+" "+errorReq.getMessage());
							error.setId_cvi(id_ins);
							//error.setError("Error en Creaci�n de Usuario: "+alumno.getUsuario()+" "+respuesta.toString());
							error.setEst("A");
							errorDAO.saveOrUpdate(error);
						}
					}
					
				}
				
			}
			//Insertamos el estado "1" a ini de que ya tiene nueva inscripci�n
			//Buscamos la cantidad de hijos que ya aceptaron los tyc
			List<Row> alumnos_inscritos=alumnoDAO.listarAlumnosInsCV(inscripcionCampus.getId_fam(),id_anio);
			Integer cant_alu_ins=alumnos_inscritos.size();
			if(cant_alu.equals(cant_alu_ins)){
				familiarDAO.updateIni(inscripcionCampus.getId_fam(), "1", new Date(), tokenSeguridad.getId());
			}			
			
		} catch (Exception e) {
			
		}
		
	}
	public String generarClave(String nombre, String apellido, int intento) {
		//return nombre.substring(0, 1).toUpperCase().concat(nombre.substring(1, intento).toLowerCase().concat(apellido.toLowerCase()).concat(Integer.toString(Calendar.getInstance().get(Calendar.YEAR))).concat("$"));
		return StringUtil.replaceTilde(nombre.substring(0, 1).toUpperCase().concat(nombre.substring(1, intento).toLowerCase().concat(apellido.toLowerCase()).concat(StringUtil.randomInt(4)).concat("$")));
	}
	
	public static String cadenaAleatoria(int longitud) {
        // El banco de caracteres
        String banco = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        // La cadena en donde iremos agregando un carácter aleatorio
        String cadena = "";
        for (int x = 0; x < longitud; x++) {
            int indiceAleatorio = numeroAleatorioEnRango(0, banco.length() - 1);
            char caracterAleatorio = banco.charAt(indiceAleatorio);
            cadena += caracterAleatorio;
        }
        return cadena;
    }

    public static int numeroAleatorioEnRango(int minimo, int maximo) {
        // nextInt regresa en rango pero con límite superior exclusivo, por eso sumamos 1
        return ThreadLocalRandom.current().nextInt(minimo, maximo + 1);
    }
    
	@Transactional
	public void restablecerContrasenia(String usuario, String nuevo_pass){
		try {
			RestUtil restUtil = new RestUtil();
			//Alumno alumno = alumnoDAO.getByParams(new Param("usuario",usuario));
			String json="{"
					+"\"email\": \""+usuario+"\","
					+"\"password\": \""+nuevo_pass+"\"}";
				
				Object respuesta=restUtil.requestPOST("http://login.ae.edu.pe:8085/ae-google-api/user/update/password", "POST", json);
				if(respuesta.toString().equals("OK")){
					//Actualizamos las contrase�a en nuestra bd
					//usuarioCampusDAO.actualizarPswAlumno(usuario, nuevo_pass);
					alumnoDAO.actualizarPswAlumnoGoogle(usuario, nuevo_pass);
					
					/*//Actualizamos en el moodle
					String json_moodle="{"
							+"\"password\": \""+nuevo_pass+"\"}";
					
					Object respuesta_moodle=restUtil.requestPOST("http://ae.edu.pe:8081/sige-moodle-api/users/updateByEmail/"+usuario, "POST", json_moodle);
					if(!respuesta_moodle.toString().equals("OK")){
						Error error = new Error();
						error.setError("Restablecimiento del psw en moodle de "+usuario+" "+respuesta.toString());
						error.setEst("A");
						errorDAO.saveOrUpdate(error);
					}*/
					
				} else {					
					Error error = new Error();
					//error.setSql_code(errorReq.getCode());
					//error.setError("Alummno:"+alumno.getNom()+" "+alumno.getApe_pat()+" "+alumno.getApe_mat()+" "+alumno.getUsuario()+" "+errorReq.getMessage());
					//error.setId_cvi(id_ins);
					error.setError("Restablecimiento del psw de "+usuario+" "+respuesta.toString());
					error.setEst("A");
					errorDAO.saveOrUpdate(error);
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Transactional
	public void generarUsuarioGogle(Integer id_alu, Integer id_anio){
		try {
			//Obtenemos el usuario del alumno
			Alumno alumno	=	alumnoDAO.getByParams(new Param("id",id_alu));
			//Obtenemos a la persona
			Persona persona = personaDAO.get(alumno.getId_per());
			//Obtener Inscripcion
			Param param = new Param();
			param.put("id_alu", id_alu);
			param.put("id_anio", id_anio);
			InscripcionCampus inscripcionCampus= inscripcionCampusDAO.getByParams(param);
			//System.out.println(alumno);
			String usuario = alumno.getUsuario();
			String clave = alumno.getPass_google();
			if(usuario==null){
				Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);
				usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"@"+empresa.getString("dominio");
				Boolean existe_usuario=alumnoDAO.existeUsuario(usuario);
				if(existe_usuario){
					usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"1@"+empresa.getString("dominio");
					existe_usuario=alumnoDAO.existeUsuario(usuario);
					if(existe_usuario){
						usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"2@"+empresa.getString("dominio");
						existe_usuario=alumnoDAO.existeUsuario(usuario);
						if(existe_usuario){
							usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"3@"+empresa.getString("dominio");
							existe_usuario=alumnoDAO.existeUsuario(usuario);
							if(existe_usuario){
								usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"4@"+empresa.getString("dominio");	
							}
						}
					}
				} 
				
			}
			//StringUtil.replaceTilde(row.getString("nom").replace(" ",""))
			String nom_alu=StringUtil.replaceTilde(persona.getNom()).toLowerCase().replace("ñ", "n");
			String ape_pat=StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n");
			String ape_mat=StringUtil.replaceTilde(persona.getApe_mat()).toLowerCase().replace("ñ", "n");
			clave = generarClave(nom_alu.substring(0, nom_alu.indexOf(" ") == -1 ? nom_alu.length() : nom_alu.indexOf(" ")), ape_pat.substring(0, ape_pat.indexOf(" ") == -1 ? ape_pat.length() : ape_pat.indexOf(" ")), 2);
			//Insertamos el usuario
			alumnoDAO.actualizarUsuarioAlumno(alumno.getId(), usuario, clave);
			Param param2 = new Param();
			param2.put("nom", "NOMBRE_COLEGIO");
			Parametro colegio=parametroDAO.getByParams(param2);
			Row matricula = matriculaDAO.getMatricula(alumno.getId(), id_anio);
			String path=null;
			if(matricula.getString("nivel").equals("INICIAL")){
				path="/"+colegio.getVal()+"/"+matricula.getString("nivel")+"/"+matricula.getString("grado");	
			} else {
				path="/"+colegio.getVal()+"/"+matricula.getString("nivel")+"/"+matricula.getString("orden")+". "+matricula.getString("grado");
			}
			
			
			//Primero creamos el usuario en el google

			String json="{"
					+"\"nombres\": \""+alumno.getNom()+"\","
					+"\"apellidos\": \""+alumno.getApe_pat()+" "+alumno.getApe_mat()+"\","
					+"\"email\": \""+usuario+"\","
					+"\"password\": \""+clave+"\","
					+"\"path\": \""+path+"\"}";
			RestUtil restUtil = new RestUtil();
			
			Object respuesta=restUtil.requestPOST("http://login.ae.edu.pe:8081/sige-google-api/user/createv2", "POST", json);
			
			Gson gson = new Gson();
			UsuarioClassroomEnrollReq usuarioClassroomEnrollReq = gson.fromJson(respuesta.toString(), UsuarioClassroomEnrollReq.class);
			if(usuarioClassroomEnrollReq.getErrors()==null){
				UsuarioCampus usuarioCampus= new UsuarioCampus();
				usuarioCampus.setId_cvic(inscripcionCampus.getId());
				usuarioCampus.setEst("A");
				usuarioCampus.setUsr(usuario);
				usuarioCampus.setPsw(clave);
				
				Integer id_usr=usuarioCampusDAO.saveOrUpdate(usuarioCampus);	
				//Insertar en la tabla sige usuarios
				//Buscamos si existe en la tabla
				SigeUsuarios sigeUsuarios=sigeUsuariosDAO.getByParams(new Param("id_alu", alumno.getId()));
				sigeUsuarios.setId_alu(alumno.getId());
				sigeUsuarios.setNombres(alumno.getNom());
				sigeUsuarios.setApellidos(alumno.getApe_pat()+" "+alumno.getApe_mat());
				sigeUsuarios.setCorreo(usuario);
				sigeUsuarios.setClave(clave);
				sigeUsuarios.setEst("A");
				sigeUsuariosDAO.saveOrUpdate(sigeUsuarios);
				
				/* String idUsuario= usuarioClassroomEnrollReq.getIdUsuario();
				//Actualizamos el idGoogle 
				 alumnoDAO.actualizarIdClasroomAlumno(idUsuario, usuario);
				 
				 //Insertar en el moodle
				 String json_moodle="{"
							+"\"nombres\": \""+alumno.getNom()+"\","
							+"\"apellidos\": \""+alumno.getApe_pat()+" "+alumno.getApe_mat()+"\","
							+"\"correo\": \""+usuario+"\","
							+"\"clave\": \""+clave+"\"}";
				 
				Object respuesta_moodle=restUtil.requestPOST("http://login.ae.edu.pe:8081/sige-moodle-api/users", "POST", json_moodle);
				Gson gson2 = new Gson();
				UsuarioClassroomEnrollReq matriculaMoodle = gson2.fromJson(respuesta_moodle.toString(), UsuarioClassroomEnrollReq.class);
				if(matriculaMoodle.getMessage()!=null){
					Gson gsonerror = new Gson();
					ErrorReq errorReq = gsonerror.fromJson(respuesta.toString(), ErrorReq.class);							
					Error error = new Error();
					error.setSql_code(errorReq.getCode());
					error.setError("Error en Inserci�n del Usuario en Moodle: "+alumno.getUsuario()+" "+errorReq.getMessage());
					//error.setId_cvi(id_ins);
					//error.setError("Error en Creaci�n de Usuario: "+alumno.getUsuario()+" "+respuesta.toString());
					error.setEst("A");
					errorDAO.saveOrUpdate(error);
				}*/
				
			} else {
				Gson gson2 = new Gson();
				ErrorReq errorReq = gson2.fromJson(respuesta.toString(), ErrorReq.class);							
				Error error = new Error();
				error.setSql_code(errorReq.getCode());
				error.setError("Error en Creaci�n de Usuario: "+alumno.getUsuario()+" "+errorReq.getMessage());
				error.setId_cvi(inscripcionCampus.getId());
				//error.setError("Error en Creaci�n de Usuario: "+alumno.getUsuario()+" "+respuesta.toString());
				error.setEst("A");
				errorDAO.saveOrUpdate(error);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Transactional
	public void generarUsuarioGooglexMatricula(Integer id_alu, Integer id_mat, Integer id_anio){
		try {
			//Obtenemos el usuario del alumno
			Alumno alumno	=	alumnoDAO.getByParams(new Param("id",id_alu));
			//if(alumno.getPass_google()==null) {
				//Obtenemos a la persona
				Persona persona = personaDAO.get(alumno.getId_per());
				//Obtener Inscripcion
				Param param = new Param();
				param.put("id_alu", alumno.getId());
				param.put("id_anio", id_anio);
				//System.out.println(alumno);
				String usuario = alumno.getUsuario();
				String clave = alumno.getPass_google();
				if(usuario==null){
					Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);
					usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"@"+empresa.getString("dominio");
					Boolean existe_usuario=alumnoDAO.existeUsuario(usuario);
					if(existe_usuario){
						usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"1@"+empresa.getString("dominio");
						existe_usuario=alumnoDAO.existeUsuario(usuario);
						if(existe_usuario){
							usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"2@"+empresa.getString("dominio");
							existe_usuario=alumnoDAO.existeUsuario(usuario);
							if(existe_usuario){
								usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"3@"+empresa.getString("dominio");
								existe_usuario=alumnoDAO.existeUsuario(usuario);
								if(existe_usuario){
									usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"4@"+empresa.getString("dominio");	
								}
							}
						}
					} 
					
				}
				//StringUtil.replaceTilde(row.getString("nom").replace(" ",""))
				String nom_alu=StringUtil.replaceTilde(persona.getNom()).toLowerCase().replace("ñ", "n");
				String ape_pat=StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n");
				String ape_mat=StringUtil.replaceTilde(persona.getApe_mat()).toLowerCase().replace("ñ", "n");
				if(clave==null) {
					clave = generarClave(nom_alu.substring(0, nom_alu.indexOf(" ") == -1 ? nom_alu.length() : nom_alu.indexOf(" ")), ape_pat.substring(0, ape_pat.indexOf(" ") == -1 ? ape_pat.length() : ape_pat.indexOf(" ")), 2);
				}
				
				String path="/Estudiantes";		
				
				//Primero creamos el usuario en el google

				String json="{"
						+"\"nombres\": \""+persona.getNom()+"\","
						+"\"apellidos\": \""+persona.getApe_pat()+" "+persona.getApe_mat()+"\","
						+"\"email\": \""+usuario+"\","
						+"\"password\": \""+clave+"\","
						+"\"path\": \""+path+"\"}";
				
				System.out.println(json);
				RestUtil restUtil = new RestUtil();
				
				Object respuesta=restUtil.requestPOST("http://login.ae.edu.pe:8085/ae-google-api/user/createv2", "POST", json);
				
				Gson gson = new Gson();
				UsuarioClassroomEnrollReq usuarioClassroomEnrollReq = gson.fromJson(respuesta.toString(), UsuarioClassroomEnrollReq.class);
				if(usuarioClassroomEnrollReq.getErrors()==null){
					//Insertamos el usuario
					alumnoDAO.actualizarUsuarioAlumno(alumno.getId(), usuario,clave);
					 String idUsuario= usuarioClassroomEnrollReq.getIdUsuario();
					//Actualizamos el idGoogle 
					 alumnoDAO.actualizarIdClasroomAlumno(idUsuario, usuario);
					 //Enrolamos al alumno a sus cursos
					 Matricula matricula= matriculaDAO.get(id_mat);
					 //Nuevo alumno
					 Alumno alumno2 = alumnoDAO.get(matricula.getId_alu());
					 //Por ahgora ya no es x cursos
					// List<CursoAula> cursos_aulas=cursoAulaDAO.listByParams(new Param("id_au",matricula.getId_au_asi()),new String[]{"id"});
					 Aula aula = aulaDAO.get(matricula.getId_au_asi());
					 List<UsuarioGrupoReq> ListaGruposClass = new ArrayList<>();
						//Lista de cursos
					//	for (CursoAula cursoAula : cursos_aulas) {
							UsuarioGrupoReq GruposClass = new UsuarioGrupoReq();
							try {
									System.out.println("entro");
									//GruposClass.setIdClassroom(cursoAula.getCod_classroom());;
									GruposClass.setIdClassroom(aula.getId_classroom());;
										List<UsuarioClassroomEnrollReq> listaUsuarios = new ArrayList<>(); 
										UsuarioClassroomEnrollReq usuarioClassroomEnrollReq2;
											usuarioClassroomEnrollReq2 = new UsuarioClassroomEnrollReq();
											System.out.println("alumno>"+persona.getApe_pat()+" "+persona.getApe_mat()+" "+persona.getNom());
											System.out.println("matricula.getAlumno().getId()>"+alumno2.getId());
											System.out.println("matricula.getAlumno().getId_classRoom()>"+alumno2.getId_classRoom());
											usuarioClassroomEnrollReq2.setIdUsuario(alumno2.getId_classRoom());
											listaUsuarios.add(usuarioClassroomEnrollReq2);
										GruposClass.setListUsuarios(listaUsuarios);
								ListaGruposClass.add(GruposClass);
								
							} catch (Exception e) {
								System.out.println("Error0>"  + e.getMessage() + ">" + e );
							}
								
					//	}
					 
						RestUtil restUtil2 = new RestUtil();
						
						//Para recuperar listas de Servicio
						Gson gson2 = new Gson();
						String usuarioGrupoReqs = gson2.toJson(ListaGruposClass);
						System.out.println(usuarioGrupoReqs);
				
						Object respuesta2=restUtil2.requestPOST("http://login.ae.edu.pe:8085/ae-google-api/course/enrole_full", "POST", usuarioGrupoReqs);
						
						//if(respuesta.toString().equals("OK")){
							System.out.println(respuesta2.toString());
							
						/*} else {
							System.out.print("Error");
						}*/
				} else {
					Gson gson2 = new Gson();
					ErrorReq errorReq = gson2.fromJson(respuesta.toString(), ErrorReq.class);							
					Error error = new Error();
					error.setSql_code(errorReq.getCode());
					error.setError("Error en Creacion de Usuario: "+alumno.getUsuario()+" "+errorReq.getMessage());
					//error.setId_cvi(inscripcionCampus.getId());
					//error.setError("Error en Creaci�n de Usuario: "+alumno.getUsuario()+" "+respuesta.toString());
					error.setEst("A");
					errorDAO.saveOrUpdate(error);
				}
			//}
			
		} catch (Exception e) {
			System.out.print("ERROR>" + e.getMessage() + " > " + e);
		}
	}
	
	@Transactional
	public void generarUsuarioGooglexMatriculaColegio(Integer id_alu, Integer id_mat, Integer id_anio) throws Exception{
		try {
			//Obtenemos el usuario del alumno
			Alumno alumno	=	alumnoDAO.getByParams(new Param("id",id_alu));
			//if(alumno.getPass_google()==null) {
				//Obtenemos a la persona
				Persona persona = personaDAO.get(alumno.getId_per());
				//Obtener Inscripcion
				Param param = new Param();
				param.put("id_alu", alumno.getId());
				param.put("id_anio", id_anio);
				//System.out.println(alumno);
				String usuario = alumno.getUsuario();
				String clave = alumno.getPass_google();
				if(usuario==null){
					Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);
					usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"@"+empresa.getString("dominio");
					Boolean existe_usuario=alumnoDAO.existeUsuario(usuario);
					if(existe_usuario){
						usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"1@"+empresa.getString("dominio");
						existe_usuario=alumnoDAO.existeUsuario(usuario);
						if(existe_usuario){
							usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"2@"+empresa.getString("dominio");
							existe_usuario=alumnoDAO.existeUsuario(usuario);
							if(existe_usuario){
								usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"3@"+empresa.getString("dominio");
								existe_usuario=alumnoDAO.existeUsuario(usuario);
								if(existe_usuario){
									usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"4@"+empresa.getString("dominio");	
								}
							}
						}
					} 
					
				}
				//StringUtil.replaceTilde(row.getString("nom").replace(" ",""))
				String nom_alu=StringUtil.replaceTilde(persona.getNom()).toLowerCase().replace("ñ", "n");
				String ape_pat=StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n");
				String ape_mat=StringUtil.replaceTilde(persona.getApe_mat()).toLowerCase().replace("ñ", "n");
				if(clave==null) {
					int longitud = 8;
			        clave = cadenaAleatoria(longitud);
				}
				
				String path="/Estudiantes";		
				
				//Primero creamos el usuario en el google

				String json="{"
						+"\"nombres\": \""+persona.getNom()+"\","
						+"\"apellidos\": \""+persona.getApe_pat()+" "+persona.getApe_mat()+"\","
						+"\"email\": \""+usuario+"\","
						+"\"password\": \""+clave+"\","
						+"\"path\": \""+path+"\"}";
				
				System.out.println(json);
				RestUtil restUtil = new RestUtil();
				
				Object respuesta=restUtil.requestPOST("http://login.ae.edu.pe:8085/ae-google-api/user/createv2", "POST", json);
				
				Gson gson = new Gson();
				UsuarioClassroomEnrollReq usuarioClassroomEnrollReq = gson.fromJson(respuesta.toString(), UsuarioClassroomEnrollReq.class);
				if(usuarioClassroomEnrollReq.getErrors()==null){
					//Insertamos el usuario
					alumnoDAO.actualizarUsuarioAlumno(alumno.getId(), usuario,clave);
					 String idUsuario= usuarioClassroomEnrollReq.getIdUsuario();
					//Actualizamos el idGoogle 
					 alumnoDAO.actualizarIdClasroomAlumno(idUsuario, usuario);
					 //Enrolamos al alumno a sus cursos
					 Matricula matricula= matriculaDAO.get(id_mat);
					 //Persona Familia
					 Familiar familiar = familiarDAO.get(matricula.getId_fam());
					 Persona persona_fam = personaDAO.get(familiar.getId_per());
					 //Nuevo alumno
					 Alumno alumno2 = alumnoDAO.get(matricula.getId_alu());
					 Aula aula = aulaDAO.get(matricula.getId_au_asi());
					 //List<CursoAula> cursos_aulas=cursoAulaDAO.listByParams(new Param("id_au",matricula.getId_au_asi()),new String[]{"id"});
					 List<UsuarioGrupoReq> ListaGruposClass = new ArrayList<>();
						//Lista de cursos
						//for (CursoAula cursoAula : cursos_aulas) {
							UsuarioGrupoReq GruposClass = new UsuarioGrupoReq();
							try {
									System.out.println("entro");
									GruposClass.setIdClassroom(aula.getId_classroom());
										List<UsuarioClassroomEnrollReq> listaUsuarios = new ArrayList<>(); 
										UsuarioClassroomEnrollReq usuarioClassroomEnrollReq2;
											usuarioClassroomEnrollReq2 = new UsuarioClassroomEnrollReq();
											System.out.println("alumno>"+persona.getApe_pat()+" "+persona.getApe_mat()+" "+persona.getNom());
											System.out.println("matricula.getAlumno().getId()>"+alumno2.getId());
											System.out.println("matricula.getAlumno().getId_classRoom()>"+alumno2.getId_classRoom());
											usuarioClassroomEnrollReq2.setIdUsuario(alumno2.getId_classRoom());
											listaUsuarios.add(usuarioClassroomEnrollReq2);
										GruposClass.setListUsuarios(listaUsuarios);
								ListaGruposClass.add(GruposClass);
								
							} catch (Exception e) {
								System.out.println("Error0>"  + e.getMessage() + ">" + e );
							}
								
						//}
					 
						RestUtil restUtil2 = new RestUtil();
						
						//Para recuperar listas de Servicio
						Gson gson2 = new Gson();
						String usuarioGrupoReqs = gson2.toJson(ListaGruposClass);
						System.out.println(usuarioGrupoReqs);
				
						Object respuesta2=restUtil2.requestPOST("http://login.ae.edu.pe:8085/ae-google-api/course/enrole_full", "POST", usuarioGrupoReqs);
						
						if(respuesta2.toString().equals("OK ENROL>1/FAILED>0")){
							System.out.println(respuesta2.toString());
							//Mandar Correo
							String alumno_nom=persona.getApe_pat()+" "+persona.getApe_mat()+" "+persona.getNom();
							//String local=row.getString("sucursal");
							//String nivel=row.getString("nivel");
							//String grado=row.getString("grado");
							String correo_apod=persona_fam.getCorr();
							//String turno=row.getString("turno");
							CorreoUtil correoUtil = new CorreoUtil();
							//String pdfRuta =  "http://login.ae.edu.pe:8080/documentos/tycAcademiaVacaciones.pdf";
							//String pdfRuta =  "D:/AE/tycAcademiaVacaciones.pdf";
							String pdfRuta =  "/opt/tomcat/webapps/documentos/Bienvenida.pdf";
							//String pdfRuta =  "C:/plantillas/Bienvenida.pdf";
							byte[] pdfBytes = FileUtil.filePathToByte(pdfRuta);
							
							String pdfRuta2 =  "/opt/tomcat/webapps/documentos/Ingreso_a_las_clases_Online.pdf";
							//String pdfRuta2 =  "C:/plantillas/Ingreso_a_las_clases_Online.pdf";
							//String pdfRuta2 =  "C:/plantillas/pasos para ingresar al teams padres.pdf";
							byte[] pdfBytes2 = FileUtil.filePathToByte(pdfRuta2);
							
							//String pdfRuta3 =  "C:/plantillas/Horario_de_Clases.pdf";
							String pdfRuta3 =  "/opt/tomcat/webapps/documentos/Horario_de_Clases.pdf";
							byte[] pdfBytes3 = FileUtil.filePathToByte(pdfRuta3);
							
							List<Adjunto> adjuntos = new ArrayList<Adjunto>();
							adjuntos.add(new Adjunto("Bienvenida.pdf",pdfBytes));
							adjuntos.add(new Adjunto("Ingreso a las clases Online.pdf",pdfBytes2));
							adjuntos.add(new Adjunto("Horario de Clases.pdf",pdfBytes3));
							
							//adjuntos.add(new Adjunto("Instructivo.pdf",pdfBytes2));
							String html ="<html lang=\"en\">\n" + 
									"\n" + 
									"<head>\n" + 
									"  <meta charset=\"UTF-8\">\n" + 
									"  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" + 
									"  <title>Email classroom\n" + 
									"\n" + 
									"  </title>\n" + 
									"</head>\n" + 
									"\n" + 
									"<body>\n" + 
									"\n" + 
									"  <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\">\n" + 
									"    <tbody>\n" + 
									"      <tr>\n" + 
									"        <td align=\"center\" valign=\"top\">\n" + 
									"\n" + 
									"        </td>\n" + 
									"      </tr>\n" + 
									"      <tr>\n" + 
									"        <td align=\"center\">\n" + 
									"          <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"550\" class=\"m_-2564031024094939495container\"\n" + 
									"            align=\"center\">\n" + 
									"            <tbody>\n" + 
									"              <tr>\n" + 
									"                <td>\n" + 
									"                  <table style=\"background-color:#ffffff\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\"\n" + 
									"                    width=\"100%\">\n" + 
									"                    <tbody>\n" + 
									"                      <tr>\n" + 
									"                        <td align=\"center\" valign=\"top\">\n" + 
									"                          <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" + 
									"                            <tbody>\n" + 
									"                              <tr>\n" + 
									"\n" + 
									"                                <td>\n" + 
									"\n" + 
									"                                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" + 
									"                                    <tbody>\n" + 
									"                                      <tr>\n" + 
									"                                        <td align=\"center\" class=\"m_-2564031024094939495header\" valign=\"top\">\n" + 
									"                                          <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" + 
									"                                            <tbody>\n" + 
									"                                              <tr>\n" + 
									"                                                <td align=\"left\" valign=\"top\">\n" + 
									"                                                  <table cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">\n" + 
									"                                                    <tbody>\n" + 
									"                                                      <tr>\n" + 
									"                                                        <td class=\"m_-2564031024094939495responsive-td\" valign=\"top\"\n" + 
									"                                                          style=\"width:100%\">\n" + 
									"                                                          <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\n" + 
									"                                                            style=\"min-width:100%\">\n" + 
									"                                                            <tbody>\n" + 
									"                                                              <tr>\n" + 
									"                                                                <td>\n" + 
									"                                                                  <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" + 
									"                                                                    <tbody>\n" + 
									"                                                                      <tr>\n" + 
									"                                                                        <td aling=\"center\"><img\n" + 
									"                                                                            src=\"http://ae.edu.pe/email/matricula-2021.png\"\n" + 
									"                                                                            style=\"display:block;padding:0px;text-align:center;height:auto;width:100%;border:0px\"\n" + 
									"                                                                            width=\"600\" class=\"CToWUd\"></td>\n" + 
									"                                                                      </tr>\n" + 
									"                                                                    </tbody>\n" + 
									"                                                                  </table>\n" + 
									"                                                                </td>\n" + 
									"                                                              </tr>\n" + 
									"                                                            </tbody>\n" + 
									"                                                          </table>\n" + 
									"\n" + 
									"                                                        </td>\n" + 
									"                                                      </tr>\n" + 
									"                                                    </tbody>\n" + 
									"                                                  </table>\n" + 
									"                                                </td>\n" + 
									"                                              </tr>\n" + 
									"                                            </tbody>\n" + 
									"                                          </table>\n" + 
									"                                        </td>\n" + 
									"                                      </tr>\n" + 
									"                                      <!-- <tr>\n" + 
									"                                <td align=\"center\" class=\"m_-2564031024094939495header\" valign=\"top\">\n" + 
									"                                  <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" + 
									"                                    <tbody>\n" + 
									"                                    <tr>\n" + 
									"                                      <td align=\"left\" valign=\"top\">\n" + 
									"                                        <table cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">\n" + 
									"                                          <tbody>\n" + 
									"                                          <tr>\n" + 
									"                                            <td class=\"m_-2564031024094939495responsive-td\" valign=\"top\" style=\"width:100%\">\n" + 
									"                                              <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"min-width:100%\"><tbody><tr><td><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tbody><tr><td align=\"center\"><img src=\"https://ci3.googleusercontent.com/proxy/1yQGXaz1dcLtDOK4Bl5Tq3LKxU7XKIc-LcLRmhb2m4bqiaVKUkwiHl35EDAP1OM-Fp0RWgKlVxlV_wuk12dMP4jN1Wgd6_BvgY4D4bErRWO3YLRIQdD9nxa6oc19ulPq_3bn_RKYm4hmgCTT7wejC3DJnlLHjA=s0-d-e1-ft#https://image.mail.bbva.pe/lib/fe4315707564047f701771/m/12/766c20ce-5a00-4fae-8e1c-e2fbcf909fc1.png\" style=\"display:block;padding:0px;text-align:center;height:auto;width:100%\" width=\"600\" class=\"CToWUd a6T\" tabindex=\"0\"><div class=\"a6S\" dir=\"ltr\" style=\"opacity: 0.01; left: 801px; top: 335px;\"><div id=\":4xt\" class=\"T-I J-J5-Ji aQv T-I-ax7 L3 a5q\" role=\"button\" tabindex=\"0\" aria-label=\"Descargar el archivo adjunto \" data-tooltip-class=\"a1V\" data-tooltip=\"Descargar\"><div class=\"wkMEBb\"><div class=\"aSK J-J5-Ji aYr\"></div></div></div></div></td></tr></tbody></table></td></tr></tbody></table>\n" + 
									"                                              \n" + 
									"                                            </td>\n" + 
									"                                          </tr>\n" + 
									"                                          </tbody>\n" + 
									"                                        </table>\n" + 
									"                                      </td>\n" + 
									"                                    </tr>\n" + 
									"                                    </tbody>\n" + 
									"                                  </table>\n" + 
									"                                </td>\n" + 
									"                              </tr> -->\n" + 
									"                                      <tr>\n" + 
									"                                        <td align=\"center\" class=\"m_-2564031024094939495header\" valign=\"top\">\n" + 
									"                                          <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" + 
									"                                            <tbody>\n" + 
									"                                              <tr>\n" + 
									"                                                <td align=\"left\" valign=\"top\">\n" + 
									"                                                  <table cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">\n" + 
									"                                                    <tbody>\n" + 
									"                                                      <tr>\n" + 
									"                                                        <td class=\"m_-2564031024094939495responsive-td\" valign=\"top\"\n" + 
									"                                                          style=\"width:100%\">\n" + 
									"                                                          <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\n" + 
									"                                                            style=\"background-color:#f4f4f4;min-width:100%\">\n" + 
									"                                                            <tbody>\n" + 
									"                                                              <tr>\n" + 
									"                                                                <td style=\"padding:30px\"><b><span\n" + 
									"                                                                      style=\"font-size:17px\"><span\n" + 
									"                                                                        style=\"font-family:Arial,Helvetica,sans-serif\">ESTIMADO\n" + 
									"                                                                        PADRE DE FAMILIA EINSTINO</span></span></b>\n" + 
									"\n" + 
									"\n" + 
									"\n" + 
									"\n" + 
									"                                                                        <br>\n" + 
									"                                                                        &nbsp;<div\n" + 
									"                                                                          style=\"text-align:justify\">\n" + 
									"                                                                          <span\n" + 
									"                                                                            style=\"font-family:Arial,Helvetica,sans-serif\"><span\n" + 
									"                                                                              style=\"font-size:15px\">Reciba un cordial saludo, para hacerle presente el envío de los accesos al classroom: \n" + 
									"\n" + 
									"                                                                        </div>\n" + 
									"                                                                       \n" + 
									"                                                                        &nbsp;<div\n" + 
									"                                                                          style=\"text-align:left\">\n" + 
									"                                                                          <span\n" + 
									"                                                                            style=\"font-family:Arial,Helvetica,sans-serif\"><span\n" + 
									"                                                                              style=\"font-size:15px\"><b>Alumno: </b>"+alumno_nom+"<br>\n" + 
									"                                                                              <b>Email: </b>"+usuario+" <br>\n" + 
									"                                                                              <b>Contraseña: </b>"+clave+" <br>\n" + 
									"                                                                              \n" + 
									"                                                                             </span></span>\n" + 
									"\n" + 
									"                                                                        </div>\n" + 
									"\n" + 
									"                                                                        &nbsp;<div\n" + 
									"                                                                        style=\"text-align:justify\">\n" + 
									"                                                                        <span\n" + 
									"                                                                          style=\"font-family:Arial,Helvetica,sans-serif\"><span\n" + 
									"                                                                            style=\"font-size:15px\">Adjuntamos, además, información importante:\n" + 
									"                                                                           </span></span>\n" + 
									"\n" + 
									"                                                                      </div>\n" + 
									"                                                                    <div\n" + 
									"                                                                      style=\"text-align:justify\">\n" + 
									"                                                                      <span\n" + 
									"                                                                        style=\"font-family:Arial,Helvetica,sans-serif\"><span\n" + 
									"                                                                          style=\"font-size:15px\"><ol>\n" + 
									"                                                                            <li>Bienvenida.</li>\n" + 
									"                                                                            <li>Tutorial ingreso a clases online.</li>\n" + 
									"                                                                            <li>Horario de Clases.</li>\n" + 
									"\n" + 
									"                                                                          </ol>\n" + 
									"\n" + 
									"                                                                    </div>\n" + 
									 "<br>      <div\n" + 
										"                                                                                          style=\"text-align:right\">\n" + 
										"                                                                                          <span\n" + 
										"                                                                                            style=\"font-family:Arial,Helvetica,sans-serif\"><span\n" + 
										"                                                                                              style=\"font-size:15px\">\n" + 
										"                                                                                              <b>LA DIRECCIÓN</b></span></span>\n" + 
										"                                                                                             \n" + 
										"                                                                                              \n" + 
										"                                                                                          <br>\n" + 
										"                                                                                          <br>\n" + 
										"                                                                                        </div>  \n" + 
									"                                                      \n" + 
									"                                                                </td>\n" + 
									"                                                              </tr>\n" + 
									"                                                            </tbody>\n" + 
									"                                                          </table>\n" + 
									"\n" + 
									"                                                        </td>\n" + 
									"                                                      </tr>\n" + 
									"                                                    </tbody>\n" + 
									"                                                  </table>\n" + 
									"                                                </td>\n" + 
									"                                              </tr>\n" + 
									"                                            </tbody>\n" + 
									"                                          </table>\n" + 
									"                                        </td>\n" + 
									"                                      </tr>\n" + 
									"                                      <tr>\n" + 
									"\n" + 
									"                                      </tr>\n" + 
									"                                      <tr>\n" + 
									"                                        <td align=\"center\" class=\"m_-2564031024094939495header\" valign=\"top\">\n" + 
									"                                          <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" + 
									"                                            <tbody>\n" + 
									"                                              <tr>\n" + 
									"                                                <td align=\"left\" valign=\"top\">\n" + 
									"                                                  <table cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">\n" + 
									"                                                    <tbody>\n" + 
									"                                                      <tr>\n" + 
									"                                                        <td class=\"m_-2564031024094939495responsive-td\" valign=\"top\"\n" + 
									"                                                          style=\"width:100%\">\n" + 
									"                                                          <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\n" + 
									"                                                            style=\"background-color:#f4f4f4;min-width:100%\">\n" + 
									"                                                            <tbody>\n" + 
									"                                                              <tr>\n" + 
									"                                                                <td style=\"padding:0px 10px 10px\">\n" + 
									"                                                                  <table cellspacing=\"0\" cellpadding=\"0\"\n" + 
									"                                                                    style=\"width:100%\">\n" + 
									"                                                                    <tbody>\n" + 
									"                                                                      <tr>\n" + 
									"                                                                        <td>\n" + 
									"                                                                          <table cellspacing=\"0\" cellpadding=\"0\"\n" + 
									"                                                                            dir=\"rtl\" style=\"width:100%\">\n" + 
									"                                                                            <tbody>\n" + 
									"                                                                              <tr>\n" + 
									"                                                                                <td valign=\"top\"\n" + 
									"                                                                                  class=\"m_-2564031024094939495responsive-td\"\n" + 
									"                                                                                  dir=\"ltr\"\n" + 
									"                                                                                  style=\"width:70%;padding-left:0px\">\n" + 
									"                                                                                  <table cellpadding=\"0\" cellspacing=\"0\"\n" + 
									"                                                                                    width=\"100%\"\n" + 
									"                                                                                    style=\"background-color:transparent;min-width:100%\">\n" + 
									"                                                                                    <tbody>\n" + 
									"                                                                                      <tr>\n" + 
									"                                                                                        <td                                                                                   \n" + 
									"\n" + 
									"\n" + 
									"                                                                                           >\n" + 
									"                                                                                           \n" + 
									"                                                                                          </div>\n" + 
									"\n" + 
									"\n" + 
									"\n" + 
									"\n" + 
									"\n" + 
									"                                                                                         \n" + 
									"                                                                        \n" + 
									"                                                                                  \n" +                                                                                         
									"\n" + 
									"\n" + 
									"                                                                                      \n" + 
									"                                                                                        </td>\n" + 
									"\n" + 
									"\n" + 
									"\n" + 
									"                                                                                      </tr>\n" + 
									"                                                                                    </tbody>\n" + 
									"                                                                                  </table>\n" + 
									"                                                                                </td>\n" + 
									"\n" + 
									"                                                                              </tr>\n" + 
									"                                                                            </tbody>\n" + 
									"                                                                          </table>\n" + 
									"                                                                        </td>\n" + 
									"                                                                      </tr>\n" + 
									"                                                                    </tbody>\n" + 
									"                                                                  </table>\n" + 
									"                                                                </td>\n" + 
									"                                                              </tr>\n" + 
									"                                                            </tbody>\n" + 
									"                                                          </table>\n" + 
									"\n" + 
									"                                                        </td>\n" + 
									"                                                      </tr>\n" + 
									"                                                    </tbody>\n" + 
									"                                                  </table>\n" + 
									"                                                </td>\n" + 
									"                                              </tr>\n" + 
									"                                            </tbody>\n" + 
									"                                          </table>\n" + 
									"                                        </td>\n" + 
									"                                      </tr>\n" + 
									"                                      <tr>\n" + 
									"                                        <td align=\"center\" class=\"m_-2564031024094939495header\" valign=\"top\">\n" + 
									"\n" + 
									"                                        </td>\n" + 
									"                                      </tr>\n" + 
									"                                      <tr>\n" + 
									"                                        <td align=\"center\" class=\"m_-2564031024094939495header\" valign=\"top\">\n" + 
									"                                          <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" + 
									"                                            <tbody>\n" + 
									"                                              <tr>\n" + 
									"                                                <td align=\"left\" valign=\"top\">\n" + 
									"\n" + 
									"                                                </td>\n" + 
									"                                              </tr>\n" + 
									"                                            </tbody>\n" + 
									"                                          </table>\n" + 
									"                                        </td>\n" + 
									"                                      </tr>\n" + 
									"\n" + 
									"\n" + 
									"\n" + 
									"\n" + 
									"                                    </tbody>\n" + 
									"                                  </table>\n" + 
									"                                </td>\n" + 
									"                              </tr>\n" + 
									"                            </tbody>\n" + 
									"                          </table>\n" + 
									"                        </td>\n" + 
									"                      </tr>\n" + 
									"                    </tbody>\n" + 
									"                  </table>\n" + 
									"                </td>\n" + 
									"              </tr>\n" + 
									"            </tbody>\n" + 
									"          </table>\n" + 
									"        </td>\n" + 
									"      </tr>\n" + 
									"\n" + 
									"    </tbody>\n" + 
									"  </table>\n" + 
									"</body>\n" + 
									"\n" + 
									"</html>";		
							
							//correoUtil.enviarVarios("Registro de Pre-Matrícula - " + datos_alu.getString("nom") +" " +datos_alu.getString("ape_pat"), "",new String[]{row.getString("fam_corr"),"maricris1906@hotmail.com"}, html,adjuntos,"consultas@ae.edu.pe","ALBERT EINSTEIN");
							//Verfico la cantidad de mensajes enviados
							Contador contador = contadorDAO.get(1);
							Integer cant_msj_env=contador.getNro();
							SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
							String format = formatter.format(new Date());
							String format2 = formatter.format(contador.getFec());
							int fecActual = Integer.parseInt(format);
							int fecContador=Integer.parseInt(format2);
							if(correo_apod!=null){
							if(!correo_apod.equals("")) {
								if(cant_msj_env<=1000 && fecActual==fecContador) {
									correoUtil.enviar("Accesos para el Classroom - " + alumno_nom, "",correo_apod, html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
									MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
									mensajeriaFamiliar.setId_des(matricula.getId_fam());
									mensajeriaFamiliar.setId_per(8);
									mensajeriaFamiliar.setMsj("Accesos para el Classroom " + alumno_nom);
									mensajeriaFamiliar.setEst("A");
									mensajeriaFamiliar.setFlg_en("1");
									mensajeriaFamiliar.setId_alu(alumno.getId());
									mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
									mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
									//Actualizo el estado de la matricula a correo enviado
									matriculaDAO.actualizarestadoEnvioAccesos(matricula.getId());
									//actualizo el contador
									cant_msj_env = cant_msj_env + 1;
									//si es 500 cambio el usuario 
									if(cant_msj_env.equals(1000)) {
										//actualizo
										contadorDAO.actualizarUsuarioContador("noreply2@ae.edu.pe");
									}
									contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
								} else if((cant_msj_env>1000 && cant_msj_env<=2000) && fecActual==fecContador){ // matricula.getString("corr")
									correoUtil.enviar("Accesos para el Classroom - " + alumno_nom, "",correo_apod, html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
									MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
									mensajeriaFamiliar.setId_des(matricula.getId_fam());
									mensajeriaFamiliar.setId_per(8);
									mensajeriaFamiliar.setMsj("Accesos para el Classroom " + alumno_nom);
									mensajeriaFamiliar.setEst("A");
									mensajeriaFamiliar.setFlg_en("1");
									mensajeriaFamiliar.setId_alu(alumno.getId());
									mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
									mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
									//Actualizo el estado de la matricula a correo enviado
									matriculaDAO.actualizarestadoEnvioAccesos(matricula.getId());
									//actualizo el contador
									cant_msj_env = cant_msj_env + 1;
									//si es 500 cambio el usuario 
									if(cant_msj_env.equals(2000)) {
										//actualizo
										contadorDAO.actualizarUsuarioContador("noreply3@ae.edu.pe");
									}
									contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
								} else if((cant_msj_env>2000 && cant_msj_env<=3000) && fecActual==fecContador){ // matricula.getString("corr")
									correoUtil.enviar("Accesos para el Classroom - " + alumno_nom, "",correo_apod, html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
									MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
									mensajeriaFamiliar.setId_des(matricula.getId_fam());
									mensajeriaFamiliar.setId_per(8);
									mensajeriaFamiliar.setMsj("Accesos para el Classroom " + alumno_nom);
									mensajeriaFamiliar.setEst("A");
									mensajeriaFamiliar.setFlg_en("1");
									mensajeriaFamiliar.setId_alu(alumno.getId());
									mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
									mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
									//Actualizo el estado de la matricula a correo enviado
									matriculaDAO.actualizarestadoEnvioAccesos(matricula.getId());
									//actualizo el contador
									cant_msj_env = cant_msj_env + 1;
									//si es 500 cambio el usuario 
									if(cant_msj_env.equals(3000)){
										//actualizo
										contadorDAO.actualizarUsuarioContador("noreply4@ae.edu.pe");
									}
									contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
								} else if((cant_msj_env>3000 && cant_msj_env<=4000) && fecActual==fecContador){ // matricula.getString("corr")
									correoUtil.enviar("Accesos para el Classroom - " + alumno_nom, "",correo_apod, html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
									MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
									mensajeriaFamiliar.setId_des(matricula.getId_fam());
									mensajeriaFamiliar.setId_per(8);
									mensajeriaFamiliar.setMsj("Accesos para el Classroom " + alumno_nom);
									mensajeriaFamiliar.setEst("A");
									mensajeriaFamiliar.setFlg_en("1");
									mensajeriaFamiliar.setId_alu(alumno.getId());
									mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
									mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
									//Actualizo el estado de la matricula a correo enviado
									matriculaDAO.actualizarestadoEnvioAccesos(matricula.getId());
									//actualizo el contador
									cant_msj_env = cant_msj_env + 1;
									//si es 500 cambio el usuario 
									if(cant_msj_env.equals(4000)){
										//actualizo
										contadorDAO.actualizarUsuarioContador("noreply5@ae.edu.pe");
									}
									contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
								} else if((cant_msj_env>4000) && fecActual==fecContador){
									throw new Exception("Se ha excedido el envío de mensajes, por favor comuníquese con el administrador.");
								} else if(fecActual!=fecContador){
									//Actualizo la fecha del contador, Nuevo dia
									contadorDAO.actualizarFechaContador(new Date());
									contadorDAO.actualizarUsuarioContador("noreply@ae.edu.pe");
									Contador contador2= contadorDAO.get(1);
									correoUtil.enviar("Accesos para el Classroom - " + alumno_nom, "",correo_apod, html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador2.getUsr(), contador2.getPsw());
									MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
									mensajeriaFamiliar.setId_des(matricula.getId_fam());
									mensajeriaFamiliar.setId_per(8);
									mensajeriaFamiliar.setMsj("Accesos para el Classroom " + alumno_nom);
									mensajeriaFamiliar.setEst("A");
									mensajeriaFamiliar.setFlg_en("1");
									mensajeriaFamiliar.setId_alu(alumno.getId());
									mensajeriaFamiliar.setUsr_rmt(contador2.getUsr());
									mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
									//Actualizo el estado de la matricula a correo enviado
									matriculaDAO.actualizarestadoEnvioAccesos(matricula.getId());
									//actualizo el contador
									cant_msj_env=contador2.getNro();
									cant_msj_env = cant_msj_env + 1;
									//si es 500 cambio el usuario 
									contadorDAO.actualizarContador(cant_msj_env, contador2.getFec());
								}
							}
							}
							
						} else {
							System.out.print("Error");
						}
				} else {
					Gson gson2 = new Gson();
					ErrorReq errorReq = gson2.fromJson(respuesta.toString(), ErrorReq.class);							
					Error error = new Error();
					error.setSql_code(errorReq.getCode());
					error.setError("Error en Creacion de Usuario: "+alumno.getUsuario()+" "+errorReq.getMessage());
					//error.setId_cvi(inscripcionCampus.getId());
					//error.setError("Error en Creaci�n de Usuario: "+alumno.getUsuario()+" "+respuesta.toString());
					error.setEst("A");
					errorDAO.saveOrUpdate(error);
				}
			//}
			
		} catch (Exception e) {
			System.out.print("ERROR>" + e.getMessage() + " > " + e);
		}
	}
	
	/**Generar usuarios google por ciclo
	 * 
	 * @param id_alu
	 * @param id_anio
	 * @param id_mat
	 * @param id_cic
	 */
	
	@Transactional
	public void generarUsuarioGooglexCilo(Integer id_anio, Integer id_cic){
		try {
			Param param1 = new Param();
			param1.put("id_cic", id_cic);
			param1.put("mat_val", 1);
			//param1.put("id", 16441);
			List<Matricula> matriculas = matriculaDAO.listByParams(param1, null);
			for (Matricula matricula : matriculas) {
				//Obtenemos el usuario del alumno
				Alumno alumno	=	alumnoDAO.getByParams(new Param("id",matricula.getId_alu()));
				if(alumno.getPass_google()==null) {
					//Obtenemos a la persona
					Persona persona = personaDAO.get(alumno.getId_per());
					//Obtener Inscripcion
					Param param = new Param();
					param.put("id_alu", alumno.getId());
					param.put("id_anio", id_anio);
					InscripcionCampus inscripcionCampus= inscripcionCampusDAO.getByParams(param);
					//System.out.println(alumno);
					String usuario = alumno.getUsuario();
					String clave = alumno.getPass_google();
					if(usuario==null){
						Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);
						usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"@"+empresa.getString("dominio");
						Boolean existe_usuario=alumnoDAO.existeUsuario(usuario);
						if(existe_usuario){
							usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"1@"+empresa.getString("dominio");
							existe_usuario=alumnoDAO.existeUsuario(usuario);
							if(existe_usuario){
								usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"2@"+empresa.getString("dominio");
								existe_usuario=alumnoDAO.existeUsuario(usuario);
								if(existe_usuario){
									usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"3@"+empresa.getString("dominio");
									existe_usuario=alumnoDAO.existeUsuario(usuario);
									if(existe_usuario){
										usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"4@"+empresa.getString("dominio");	
									}
								}
							}
						} 
						
					}
					//StringUtil.replaceTilde(row.getString("nom").replace(" ",""))
					String nom_alu=StringUtil.replaceTilde(persona.getNom()).toLowerCase().replace("ñ", "n");
					String ape_pat=StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n");
					String ape_mat=StringUtil.replaceTilde(persona.getApe_mat()).toLowerCase().replace("ñ", "n");
					if(clave==null) {
						clave = generarClave(nom_alu.substring(0, nom_alu.indexOf(" ") == -1 ? nom_alu.length() : nom_alu.indexOf(" ")), ape_pat.substring(0, ape_pat.indexOf(" ") == -1 ? ape_pat.length() : ape_pat.indexOf(" ")), 2);
					}
					
					
					
					//String clave =StringUtil.randomInt(6)
					
					//Row matricula = matriculaDAO.getMatricula(alumno.getId(), id_anio);
					//Row matricula2 = matriculaDAO.getDatosMatriculaxId(matricula.getId());
					String path="/Estudiantes";
					/*if(matricula2.getString("tipo").equals("A")) {
						GiroNegocio giroNegocio=giroNegocioDAO.get(Constante.GIRO_ACADEMIA);
						path="/"+giroNegocio.getNom()+"/"+matricula2.getString("nivel")+"/ Ciclo "+matricula2.getString("ciclo");
					} else if(matricula2.getString("tipo").equals("V")) {
						GiroNegocio giroNegocio=giroNegocioDAO.get(Constante.GIRO_VACACIONES);
						if(matricula2.getString("nivel").equals("INICIAL")){
							path="/"+giroNegocio.getNom()+"/"+matricula2.getString("ciclo")+"/"+matricula2.getString("nivel")+"/"+matricula2.getString("grado");	
						} else {
							path="/"+giroNegocio.getNom()+"/"+matricula2.getString("ciclo")+"/"+matricula2.getString("nivel")+"/"+matricula2.getString("orden")+". "+matricula2.getString("grado");
						}
					} else {
						Param param2 = new Param();
						param2.put("nom", "NOMBRE_COLEGIO");
						Parametro colegio=parametroDAO.getByParams(param2);
						
						
						if(matricula2.getString("nivel").equals("INICIAL")){
							path="/"+colegio.getVal()+"/"+matricula2.getString("nivel")+"/"+matricula2.getString("grado");	
						} else {
							path="/"+colegio.getVal()+"/"+matricula2.getString("nivel")+"/"+matricula2.getString("orden")+". "+matricula2.getString("grado");
						}
					}*/
					
					
					
					//Primero creamos el usuario en el google

					String json="{"
							+"\"nombres\": \""+persona.getNom()+"\","
							+"\"apellidos\": \""+persona.getApe_pat()+" "+persona.getApe_mat()+"\","
							+"\"email\": \""+usuario+"\","
							+"\"password\": \""+clave+"\","
							+"\"path\": \""+path+"\"}";
					
					System.out.println(json);
					RestUtil restUtil = new RestUtil();
					
					Object respuesta=restUtil.requestPOST("http://localhost:8085/ae-google-api/user/createv2", "POST", json);
					
					Gson gson = new Gson();
					UsuarioClassroomEnrollReq usuarioClassroomEnrollReq = gson.fromJson(respuesta.toString(), UsuarioClassroomEnrollReq.class);
					if(usuarioClassroomEnrollReq.getErrors()==null){
						/*UsuarioCampus usuarioCampus= new UsuarioCampus();
						usuarioCampus.setId_cvic(inscripcionCampus.getId());
						usuarioCampus.setEst("A");
						usuarioCampus.setUsr(usuario);
						usuarioCampus.setPsw(clave);
						
						Integer id_usr=usuarioCampusDAO.saveOrUpdate(usuarioCampus);	*/
						//Insertar en la tabla sige usuarios
						//SigeUsuarios sigeUsuarios= new SigeUsuarios();
						/*SigeUsuarios sigeUsuarios=sigeUsuariosDAO.getByParams(new Param("id_alu", alumno.getId()));
						System.out.print(alumno.getId());
						sigeUsuarios.setId_alu(alumno.getId());
						System.out.print(persona.getNom());
						sigeUsuarios.setNombres(persona.getNom());
						sigeUsuarios.setApellidos(persona.getApe_pat()+" "+persona.getApe_mat());
						sigeUsuarios.setCorreo(usuario);
						sigeUsuarios.setClave(clave);
						sigeUsuarios.setEst("A");
						sigeUsuariosDAO.saveOrUpdate(sigeUsuarios);*/
						//Insertamos el usuario
						alumnoDAO.actualizarUsuarioAlumno(alumno.getId(), usuario,clave);
						 String idUsuario= usuarioClassroomEnrollReq.getIdUsuario();
						//Actualizamos el idGoogle 
						 alumnoDAO.actualizarIdClasroomAlumno(idUsuario, usuario);
						 
						 //Insertar en el moodle
						/* String json_moodle="{"
									+"\"nombres\": \""+alumno.getNom()+"\","
									+"\"apellidos\": \""+alumno.getApe_pat()+" "+alumno.getApe_mat()+"\","
									+"\"correo\": \""+usuario+"\","
									+"\"clave\": \""+clave+"\"}";
						 
						Object respuesta_moodle=restUtil.requestPOST("http://login.ae.edu.pe:8081/sige-moodle-api/users", "POST", json_moodle);
						Gson gson2 = new Gson();
						UsuarioClassroomEnrollReq matriculaMoodle = gson2.fromJson(respuesta_moodle.toString(), UsuarioClassroomEnrollReq.class);
						if(matriculaMoodle.getMessage()!=null){
							Gson gsonerror = new Gson();
							ErrorReq errorReq = gsonerror.fromJson(respuesta.toString(), ErrorReq.class);							
							Error error = new Error();
							error.setSql_code(errorReq.getCode());
							error.setError("Error en Inserci�n del Usuario en Moodle: "+alumno.getUsuario()+" "+errorReq.getMessage());
							//error.setId_cvi(id_ins);
							//error.setError("Error en Creaci�n de Usuario: "+alumno.getUsuario()+" "+respuesta.toString());
							error.setEst("A");
							errorDAO.saveOrUpdate(error);
						}*/
						
					} else {
						Gson gson2 = new Gson();
						ErrorReq errorReq = gson2.fromJson(respuesta.toString(), ErrorReq.class);							
						Error error = new Error();
						error.setSql_code(errorReq.getCode());
						error.setError("Error en Creaci�n de Usuario: "+alumno.getUsuario()+" "+errorReq.getMessage());
						error.setId_cvi(inscripcionCampus.getId());
						//error.setError("Error en Creaci�n de Usuario: "+alumno.getUsuario()+" "+respuesta.toString());
						error.setEst("A");
						errorDAO.saveOrUpdate(error);
					}
				}

			}
			
		} catch (Exception e) {
			System.out.print(e);
		}
	}
	
	/**Generar usuarios google por ciclo
	 * 
	 * @param id_alu
	 * @param id_anio
	 * @param id_mat
	 * @param id_cic
	 */
	
	@Transactional
	public void generarUsuarioGooglexCiCloColegioMatriculasValidadas(Integer id_anio, Integer id_cic){
		try {
			//Param param1 = new Param();
			//param1.put("id_cic", id_cic);
			//param1.put("mat_val", 1);
			//param1.put("id", 16441);
			List<Row> matriculas = matriculaDAO.listarMatriculasxAnioCiclo(id_cic);
			for (Row row : matriculas) {
				//Obtenemos el usuario del alumno
				Alumno alumno	=	alumnoDAO.getByParams(new Param("id",row.getInteger("id_alu")));
				if(alumno.getPass_google()==null) {
					//Obtenemos a la persona
					Persona persona = personaDAO.get(alumno.getId_per());
					//Obtener Inscripcion
					Param param = new Param();
					param.put("id_alu", alumno.getId());
					param.put("id_anio", id_anio);
					InscripcionCampus inscripcionCampus= inscripcionCampusDAO.getByParams(param);
					//System.out.println(alumno);
					String usuario = alumno.getUsuario();
					String clave = alumno.getPass_google();
					if(usuario==null){
						Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);
						usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"@"+empresa.getString("dominio");
						Boolean existe_usuario=alumnoDAO.existeUsuario(usuario);
						if(existe_usuario){
							usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"1@"+empresa.getString("dominio");
							existe_usuario=alumnoDAO.existeUsuario(usuario);
							if(existe_usuario){
								usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"2@"+empresa.getString("dominio");
								existe_usuario=alumnoDAO.existeUsuario(usuario);
								if(existe_usuario){
									usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"3@"+empresa.getString("dominio");
									existe_usuario=alumnoDAO.existeUsuario(usuario);
									if(existe_usuario){
										usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"4@"+empresa.getString("dominio");	
									}
								}
							}
						} 
						
					}
					//StringUtil.replaceTilde(row.getString("nom").replace(" ",""))
					String nom_alu=StringUtil.replaceTilde(persona.getNom()).toLowerCase().replace("ñ", "n");
					String ape_pat=StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n");
					String ape_mat=StringUtil.replaceTilde(persona.getApe_mat()).toLowerCase().replace("ñ", "n");
					if(clave==null) {
						//clave = generarClave(nom_alu.substring(0, nom_alu.indexOf(" ") == -1 ? nom_alu.length() : nom_alu.indexOf(" ")), ape_pat.substring(0, ape_pat.indexOf(" ") == -1 ? ape_pat.length() : ape_pat.indexOf(" ")), 2);
						int longitud = 8;
				        clave = cadenaAleatoria(longitud);				        
					}
					
					
					
					//String clave =StringUtil.randomInt(6)
					
					//Row matricula = matriculaDAO.getMatricula(alumno.getId(), id_anio);
					//Row matricula2 = matriculaDAO.getDatosMatriculaxId(matricula.getId());
					String path="/Estudiantes";
					/*if(matricula2.getString("tipo").equals("A")) {
						GiroNegocio giroNegocio=giroNegocioDAO.get(Constante.GIRO_ACADEMIA);
						path="/"+giroNegocio.getNom()+"/"+matricula2.getString("nivel")+"/ Ciclo "+matricula2.getString("ciclo");
					} else if(matricula2.getString("tipo").equals("V")) {
						GiroNegocio giroNegocio=giroNegocioDAO.get(Constante.GIRO_VACACIONES);
						if(matricula2.getString("nivel").equals("INICIAL")){
							path="/"+giroNegocio.getNom()+"/"+matricula2.getString("ciclo")+"/"+matricula2.getString("nivel")+"/"+matricula2.getString("grado");	
						} else {
							path="/"+giroNegocio.getNom()+"/"+matricula2.getString("ciclo")+"/"+matricula2.getString("nivel")+"/"+matricula2.getString("orden")+". "+matricula2.getString("grado");
						}
					} else {
						Param param2 = new Param();
						param2.put("nom", "NOMBRE_COLEGIO");
						Parametro colegio=parametroDAO.getByParams(param2);
						
						
						if(matricula2.getString("nivel").equals("INICIAL")){
							path="/"+colegio.getVal()+"/"+matricula2.getString("nivel")+"/"+matricula2.getString("grado");	
						} else {
							path="/"+colegio.getVal()+"/"+matricula2.getString("nivel")+"/"+matricula2.getString("orden")+". "+matricula2.getString("grado");
						}
					}*/
					
					
					
					//Primero creamos el usuario en el google

					String json="{"
							+"\"nombres\": \""+persona.getNom()+"\","
							+"\"apellidos\": \""+persona.getApe_pat()+" "+persona.getApe_mat()+"\","
							+"\"email\": \""+usuario+"\","
							+"\"password\": \""+clave+"\","
							+"\"path\": \""+path+"\"}";
					
					System.out.println(json);
					RestUtil restUtil = new RestUtil();
					
					Object respuesta=restUtil.requestPOST("http://localhost:8085/ae-google-api/user/createv2", "POST", json);
					
					Gson gson = new Gson();
					UsuarioClassroomEnrollReq usuarioClassroomEnrollReq = gson.fromJson(respuesta.toString(), UsuarioClassroomEnrollReq.class);
					if(usuarioClassroomEnrollReq.getErrors()==null){
						/*UsuarioCampus usuarioCampus= new UsuarioCampus();
						usuarioCampus.setId_cvic(inscripcionCampus.getId());
						usuarioCampus.setEst("A");
						usuarioCampus.setUsr(usuario);
						usuarioCampus.setPsw(clave);
						
						Integer id_usr=usuarioCampusDAO.saveOrUpdate(usuarioCampus);	*/
						//Insertar en la tabla sige usuarios
						//SigeUsuarios sigeUsuarios= new SigeUsuarios();
						/*SigeUsuarios sigeUsuarios=sigeUsuariosDAO.getByParams(new Param("id_alu", alumno.getId()));
						System.out.print(alumno.getId());
						sigeUsuarios.setId_alu(alumno.getId());
						System.out.print(persona.getNom());
						sigeUsuarios.setNombres(persona.getNom());
						sigeUsuarios.setApellidos(persona.getApe_pat()+" "+persona.getApe_mat());
						sigeUsuarios.setCorreo(usuario);
						sigeUsuarios.setClave(clave);
						sigeUsuarios.setEst("A");
						sigeUsuariosDAO.saveOrUpdate(sigeUsuarios);*/
						//Insertamos el usuario
						alumnoDAO.actualizarUsuarioAlumno(alumno.getId(), usuario,clave);
						 String idUsuario= usuarioClassroomEnrollReq.getIdUsuario();
						//Actualizamos el idGoogle 
						 alumnoDAO.actualizarIdClasroomAlumno(idUsuario, usuario);
						 
						 //Activo la cuenta
						 String json2 ="{"
									//+"\"idClassroom\": \""+row.getString("cod_classroom")+"\","
									+"\"email\": \""+usuario+"\","
									+"\"suspendido\": \""+false+"\","
									+"\"motivoSuspension\": \"Matricula 2021\"}";
							RestUtil restUtil2 = new RestUtil();
							Object respuesta2=restUtil2.requestPOST("http://localhost:8085/ae-google-api/user/desactive", "POST",json2);
							
						if(respuesta2.toString().equals("OK")){
								System.out.println(respuesta2.toString()+"Activo "+usuario);
						} else {
								System.out.println("NO ACTIVO");
						}
						 //Insertar en el moodle
						/* String json_moodle="{"
									+"\"nombres\": \""+alumno.getNom()+"\","
									+"\"apellidos\": \""+alumno.getApe_pat()+" "+alumno.getApe_mat()+"\","
									+"\"correo\": \""+usuario+"\","
									+"\"clave\": \""+clave+"\"}";
						 
						Object respuesta_moodle=restUtil.requestPOST("http://login.ae.edu.pe:8081/sige-moodle-api/users", "POST", json_moodle);
						Gson gson2 = new Gson();
						UsuarioClassroomEnrollReq matriculaMoodle = gson2.fromJson(respuesta_moodle.toString(), UsuarioClassroomEnrollReq.class);
						if(matriculaMoodle.getMessage()!=null){
							Gson gsonerror = new Gson();
							ErrorReq errorReq = gsonerror.fromJson(respuesta.toString(), ErrorReq.class);							
							Error error = new Error();
							error.setSql_code(errorReq.getCode());
							error.setError("Error en Inserci�n del Usuario en Moodle: "+alumno.getUsuario()+" "+errorReq.getMessage());
							//error.setId_cvi(id_ins);
							//error.setError("Error en Creaci�n de Usuario: "+alumno.getUsuario()+" "+respuesta.toString());
							error.setEst("A");
							errorDAO.saveOrUpdate(error);
						}*/
						
					} else {
						Gson gson2 = new Gson();
						ErrorReq errorReq = gson2.fromJson(respuesta.toString(), ErrorReq.class);							
						Error error = new Error();
						error.setSql_code(errorReq.getCode());
						error.setError("Error en Creacion de Usuario: "+alumno.getUsuario()+" "+errorReq.getMessage());
						error.setId_cvi(inscripcionCampus.getId());
						//error.setError("Error en Creaci�n de Usuario: "+alumno.getUsuario()+" "+respuesta.toString());
						error.setEst("A");
						errorDAO.saveOrUpdate(error);
					}
				}

			}
			
		} catch (Exception e) {
			System.out.print(e);
		}
	}
	
	@Transactional
	public void generarUsuarioGooglexCiCloColegioMatriculasValidadasVU(Integer id_anio, Integer id_cic){
		try {
			//Param param1 = new Param();
			//param1.put("id_cic", id_cic);
			//param1.put("mat_val", 1);
			//param1.put("id", 16441);
			List<Row> matriculas = matriculaDAO.listarMatriculasxAnioCicloVU(id_cic);
			for (Row row : matriculas) {
				//Obtenemos el usuario del alumno
				Alumno alumno	=	alumnoDAO.getByParams(new Param("id",row.getInteger("id_alu")));
				if(alumno.getPass_google()==null) {
					//Obtenemos a la persona
					Persona persona = personaDAO.get(alumno.getId_per());
					//Obtener Inscripcion
					Param param = new Param();
					param.put("id_alu", alumno.getId());
					param.put("id_anio", id_anio);
					InscripcionCampus inscripcionCampus= inscripcionCampusDAO.getByParams(param);
					//System.out.println(alumno);
					String usuario = alumno.getUsuario();
					String clave = alumno.getPass_google();
					if(usuario==null){
						Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);
						usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"@"+empresa.getString("dominio");
						Boolean existe_usuario=alumnoDAO.existeUsuario(usuario);
						if(existe_usuario){
							usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"1@"+empresa.getString("dominio");
							existe_usuario=alumnoDAO.existeUsuario(usuario);
							if(existe_usuario){
								usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"2@"+empresa.getString("dominio");
								existe_usuario=alumnoDAO.existeUsuario(usuario);
								if(existe_usuario){
									usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"3@"+empresa.getString("dominio");
									existe_usuario=alumnoDAO.existeUsuario(usuario);
									if(existe_usuario){
										usuario=StringUtil.replaceTilde(persona.getNom().replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n").replace(" ", "")+"4@"+empresa.getString("dominio");	
									}
								}
							}
						} 
						
					}
					//StringUtil.replaceTilde(row.getString("nom").replace(" ",""))
					String nom_alu=StringUtil.replaceTilde(persona.getNom()).toLowerCase().replace("ñ", "n");
					String ape_pat=StringUtil.replaceTilde(persona.getApe_pat()).toLowerCase().replace("ñ", "n");
					String ape_mat=StringUtil.replaceTilde(persona.getApe_mat()).toLowerCase().replace("ñ", "n");
					if(clave==null) {
						//clave = generarClave(nom_alu.substring(0, nom_alu.indexOf(" ") == -1 ? nom_alu.length() : nom_alu.indexOf(" ")), ape_pat.substring(0, ape_pat.indexOf(" ") == -1 ? ape_pat.length() : ape_pat.indexOf(" ")), 2);
						int longitud = 8;
				        clave = cadenaAleatoria(longitud);				        
					}
					
					
					
					//String clave =StringUtil.randomInt(6)
					
					//Row matricula = matriculaDAO.getMatricula(alumno.getId(), id_anio);
					//Row matricula2 = matriculaDAO.getDatosMatriculaxId(matricula.getId());
					String path="/Estudiantes";
					/*if(matricula2.getString("tipo").equals("A")) {
						GiroNegocio giroNegocio=giroNegocioDAO.get(Constante.GIRO_ACADEMIA);
						path="/"+giroNegocio.getNom()+"/"+matricula2.getString("nivel")+"/ Ciclo "+matricula2.getString("ciclo");
					} else if(matricula2.getString("tipo").equals("V")) {
						GiroNegocio giroNegocio=giroNegocioDAO.get(Constante.GIRO_VACACIONES);
						if(matricula2.getString("nivel").equals("INICIAL")){
							path="/"+giroNegocio.getNom()+"/"+matricula2.getString("ciclo")+"/"+matricula2.getString("nivel")+"/"+matricula2.getString("grado");	
						} else {
							path="/"+giroNegocio.getNom()+"/"+matricula2.getString("ciclo")+"/"+matricula2.getString("nivel")+"/"+matricula2.getString("orden")+". "+matricula2.getString("grado");
						}
					} else {
						Param param2 = new Param();
						param2.put("nom", "NOMBRE_COLEGIO");
						Parametro colegio=parametroDAO.getByParams(param2);
						
						
						if(matricula2.getString("nivel").equals("INICIAL")){
							path="/"+colegio.getVal()+"/"+matricula2.getString("nivel")+"/"+matricula2.getString("grado");	
						} else {
							path="/"+colegio.getVal()+"/"+matricula2.getString("nivel")+"/"+matricula2.getString("orden")+". "+matricula2.getString("grado");
						}
					}*/
					
					
					
					//Primero creamos el usuario en el google

					String json="{"
							+"\"nombres\": \""+persona.getNom()+"\","
							+"\"apellidos\": \""+persona.getApe_pat()+" "+persona.getApe_mat()+"\","
							+"\"email\": \""+usuario+"\","
							+"\"password\": \""+clave+"\","
							+"\"path\": \""+path+"\"}";
					
					System.out.println(json);
					RestUtil restUtil = new RestUtil();
					
					Object respuesta=restUtil.requestPOST("http://localhost:8085/ae-google-api/user/createv2", "POST", json);
					
					Gson gson = new Gson();
					UsuarioClassroomEnrollReq usuarioClassroomEnrollReq = gson.fromJson(respuesta.toString(), UsuarioClassroomEnrollReq.class);
					if(usuarioClassroomEnrollReq.getErrors()==null){
						/*UsuarioCampus usuarioCampus= new UsuarioCampus();
						usuarioCampus.setId_cvic(inscripcionCampus.getId());
						usuarioCampus.setEst("A");
						usuarioCampus.setUsr(usuario);
						usuarioCampus.setPsw(clave);
						
						Integer id_usr=usuarioCampusDAO.saveOrUpdate(usuarioCampus);	*/
						//Insertar en la tabla sige usuarios
						//SigeUsuarios sigeUsuarios= new SigeUsuarios();
						/*SigeUsuarios sigeUsuarios=sigeUsuariosDAO.getByParams(new Param("id_alu", alumno.getId()));
						System.out.print(alumno.getId());
						sigeUsuarios.setId_alu(alumno.getId());
						System.out.print(persona.getNom());
						sigeUsuarios.setNombres(persona.getNom());
						sigeUsuarios.setApellidos(persona.getApe_pat()+" "+persona.getApe_mat());
						sigeUsuarios.setCorreo(usuario);
						sigeUsuarios.setClave(clave);
						sigeUsuarios.setEst("A");
						sigeUsuariosDAO.saveOrUpdate(sigeUsuarios);*/
						//Insertamos el usuario
						alumnoDAO.actualizarUsuarioAlumno(alumno.getId(), usuario,clave);
						 String idUsuario= usuarioClassroomEnrollReq.getIdUsuario();
						//Actualizamos el idGoogle 
						 alumnoDAO.actualizarIdClasroomAlumno(idUsuario, usuario);
						 
						 //Activo la cuenta
						 String json2 ="{"
									//+"\"idClassroom\": \""+row.getString("cod_classroom")+"\","
									+"\"email\": \""+usuario+"\","
									+"\"suspendido\": \""+false+"\","
									+"\"motivoSuspension\": \"Matricula 2021\"}";
							RestUtil restUtil2 = new RestUtil();
							Object respuesta2=restUtil2.requestPOST("http://localhost:8085/ae-google-api/user/desactive", "POST",json2);
							
						if(respuesta2.toString().equals("OK")){
								System.out.println(respuesta2.toString()+"Activo "+usuario);
						} else {
								System.out.println("NO ACTIVO");
						}
						 //Insertar en el moodle
						/* String json_moodle="{"
									+"\"nombres\": \""+alumno.getNom()+"\","
									+"\"apellidos\": \""+alumno.getApe_pat()+" "+alumno.getApe_mat()+"\","
									+"\"correo\": \""+usuario+"\","
									+"\"clave\": \""+clave+"\"}";
						 
						Object respuesta_moodle=restUtil.requestPOST("http://login.ae.edu.pe:8081/sige-moodle-api/users", "POST", json_moodle);
						Gson gson2 = new Gson();
						UsuarioClassroomEnrollReq matriculaMoodle = gson2.fromJson(respuesta_moodle.toString(), UsuarioClassroomEnrollReq.class);
						if(matriculaMoodle.getMessage()!=null){
							Gson gsonerror = new Gson();
							ErrorReq errorReq = gsonerror.fromJson(respuesta.toString(), ErrorReq.class);							
							Error error = new Error();
							error.setSql_code(errorReq.getCode());
							error.setError("Error en Inserci�n del Usuario en Moodle: "+alumno.getUsuario()+" "+errorReq.getMessage());
							//error.setId_cvi(id_ins);
							//error.setError("Error en Creaci�n de Usuario: "+alumno.getUsuario()+" "+respuesta.toString());
							error.setEst("A");
							errorDAO.saveOrUpdate(error);
						}*/
						
					} else {
						Gson gson2 = new Gson();
						ErrorReq errorReq = gson2.fromJson(respuesta.toString(), ErrorReq.class);							
						Error error = new Error();
						error.setSql_code(errorReq.getCode());
						error.setError("Error en Creacion de Usuario: "+alumno.getUsuario()+" "+errorReq.getMessage());
						error.setId_cvi(inscripcionCampus.getId());
						//error.setError("Error en Creaci�n de Usuario: "+alumno.getUsuario()+" "+respuesta.toString());
						error.setEst("A");
						errorDAO.saveOrUpdate(error);
					}
				}

			}
			
		} catch (Exception e) {
			System.out.print(e);
		}
	}
	
	public void enviarCorreoElectronico(Integer id_fam, Integer id_alu, String usuario, String psw)throws Exception{
		
		//Obtener datos del apoderado
		/*
		if(inscripcion_ind.getCorr()==null || inscripcion_ind.getCorr().trim().equals(""))
			return;
		
		List<Row>inscripcion=consultarDatosInsInd(inscripcion_ind.getId(), "I");
		
		byte[]  pdf_bytes  = generaCarnet(inscripcion);

		Integer id_niv=inscripcion.get(0).getInt("id_niv");
		Config config = configDAO.get(inscripcion_ind.getId_oli());//DATOS DE LA OLIMPIADA
		String nom_colegio_organizador=colegioOrgDAO.obtenerDatosColegioxNivelyOli(id_niv, inscripcion_ind.getId_oli()).getString("colegio");
		CorreoUtil correoUtil = new CorreoUtil();
		//String host = request.getHeader("host");
		String html = "Estimad@:";
		html += "<br><br>" + inscripcion_ind.getApe_pat() + " " + inscripcion_ind.getApe_mat() +", " + inscripcion_ind.getNom();
		html += "<br><br>SU INSCRIPCI�N FUE SATISFACTORIA.";
		html += "<br><br><h2><u>FECHA DE CONCURSO</u></h2>";
		html += "<h1><font color='green'>" + config.getFec() + "</font></h1>";

		//html += "Este correo es informativo, favor no responder a esta direcci�n de correo, ya que no se encuentra habilitada para recibir mensajes.";
		//html += "<BR>Si requiere mayor informaci�n, contactar con secretaria de Lunes a Viernes de 8:00 a 12:45 y de 14:00 a 17:00 horas, tel�fono 043-422110 o al e-mail:<a href='mailto:consultas@ae.edu.pe'>consultas@ae.edu.pe</a>";
		html += "<br><br>Atentamente";
		html += "<br><b>La Direcci�n</b>";

		correoUtil.enviar("Inscripci�n " + inscripcion_ind.getNom() +" " + inscripcion_ind.getApe_pat() , "", inscripcion_ind.getCorr(), html,config.getNom()+".pdf",pdf_bytes,config.getCorr_envio(),nom_colegio_organizador);

		*/
	}
	
	/*@RequestMapping(value = "/generarCosto/{id_gpf}")
	@ResponseBody
	public void reservaImprimir(HttpServletResponse response, @PathVariable Integer id_gpf) throws Exception {

		Parametro parametro = parametroDAO.getByParams(new Param("nom", "RUTA_PLANTILLA"));

		GruFam grupo= gruFamDAO.getByParams(new Param("id",id_gpf));
		File file = null;
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;

		Map<String, String> map = new HashMap<String, String>();
		map.put("CODIGO_FAM", grupo.getCod());
		

		String nuevoArchivo = DocxUtil.generate(parametro.getVal(), "Costos.docx", map);

		String fileName = URLEncoder.encode("Costos.docx", "UTF-8");
		fileName = URLDecoder.decode(fileName, "ISO8859_1");
		response.setContentType("application/x-msdownload");
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);

		file = new File(nuevoArchivo);
		fis = new FileInputStream(file);
		bos = new ByteArrayOutputStream();
		int readNum;
		byte[] buf = new byte[1024];
		try {

			for (; (readNum = fis.read(buf)) != -1;) {
				bos.write(buf, 0, readNum);
			}
		} catch (IOException ex) {

		}
		ServletOutputStream out = response.getOutputStream();
		bos.writeTo(out);
	}*/
	
	@Transactional
	public void asignarGrupos(Integer id_anio, Integer id_gra)throws Exception{
		GrupoConfig grupoConfig=null;
		if(id_gra.equals(1)){
			 grupoConfig = grupoConfigDAO.getByParams(new Param("cap","20"));
		} else if(id_gra.equals(2)){
			 grupoConfig = grupoConfigDAO.getByParams(new Param("cap","25"));
		} else if(id_gra.equals(3)){
			 grupoConfig = grupoConfigDAO.getByParams(new Param("cap","25"));
		} else if(id_gra.equals(4)){
			 grupoConfig = grupoConfigDAO.getByParams(new Param("cap","30"));
		}  else if(id_gra.equals(5)){
			 grupoConfig = grupoConfigDAO.getByParams(new Param("cap","30"));
		}  else if(id_gra.equals(6)){
			 grupoConfig = grupoConfigDAO.getByParams(new Param("cap","30"));
		}  else if(id_gra.equals(7)){
			 grupoConfig = grupoConfigDAO.getByParams(new Param("cap","30"));
		}  else if(id_gra.equals(8)){
			 grupoConfig = grupoConfigDAO.getByParams(new Param("cap","30"));
		}  else if(id_gra.equals(9)){
			 grupoConfig = grupoConfigDAO.getByParams(new Param("cap","30"));
		}  else if(id_gra.equals(10)){
			 grupoConfig = grupoConfigDAO.getByParams(new Param("cap","35"));
		}  else if(id_gra.equals(11)){
			 grupoConfig = grupoConfigDAO.getByParams(new Param("cap","35"));
		}  else if(id_gra.equals(12)){
			 grupoConfig = grupoConfigDAO.getByParams(new Param("cap","35"));
		}  else if(id_gra.equals(13)){
			 grupoConfig = grupoConfigDAO.getByParams(new Param("cap","35"));
		}  else if(id_gra.equals(14)){
			 grupoConfig = grupoConfigDAO.getByParams(new Param("cap","35"));
		}
		
		//Obtener la lista de ese grado
		List<Row> inscritos= inscripcionCampusDAO.listarInscritosAgrupamiento(id_anio, id_gra);
		for (Row row : inscritos) {
			//Verficamos si existe grupo aula virtual
			List<Row> grupoAulaVirtual = grupoAulaVirtualDAO.listarGruposNoLlenos(id_gra, grupoConfig.getId(),id_anio);
			if(grupoAulaVirtual.size()==0){
				//Insertamos el grupo Aula Virtual
				GrupoAulaVirtual grupoAulaVirtualNuevo = new GrupoAulaVirtual();
				grupoAulaVirtualNuevo.setId_cgc(grupoConfig.getId());
				grupoAulaVirtualNuevo.setId_gra(id_gra);
				grupoAulaVirtualNuevo.setId_anio(id_anio);
				grupoAulaVirtualNuevo.setDes("Grupo");
				grupoAulaVirtualNuevo.setNro(1);
				grupoAulaVirtualNuevo.setLleno("0");
				grupoAulaVirtualNuevo.setEst("A");
				Integer id_cgr=grupoAulaVirtualDAO.saveOrUpdate(grupoAulaVirtualNuevo);
				//Creamos el grupo en el classroom
				//Obtenemos datos del Grupo
				Row grupo =grupoAulaVirtualDAO.obtenerDatosGrupo(id_cgr);
				String moderador="116180455041256041855";
				String json ="{"
						+"\"nombre\": \""+grupo.getString("nivel")+"\","
						+"\"seccion\": \""+grupo.getString("grado")+" - "+grupo.getString("grupo")+"\","
						+"\"descripcion\": \""+grupo.getString("anio")+"-"+grupo.getString("abrev")+"-"+grupo.getString("abrv_classroom")+"-G"+grupo.getInt("nro")+"\","
						+"\"idModerador\": \""+moderador+"\"}";
				RestUtil restUtil = new RestUtil();
				
				Object respuesta=restUtil.requestPOST("http://login.ae.edu.pe/:8081/sige-google-api/course/create", "POST", json);
				
				Gson gson = new Gson();
				GrupoAulaVirtualReq grupoAulaVirtualReq = gson.fromJson(respuesta.toString(), GrupoAulaVirtualReq.class);
				String id_grupo=null;
				if(grupoAulaVirtualReq.getErrors()==null){
					id_grupo=grupoAulaVirtualReq.getIdClassroom();
					//Actualizar el campo de id_Grupoclass
					grupoAulaVirtualDAO.actualizarIdGrupo(id_grupo, row.getInteger("id_cga"));
					System.out.println("update cvi_grupo_aula_virtual set id_grupoclass="+id_grupo+" where id="+row.getInteger("id_cga")+";");
					
				} else {
					Gson gson1 = new Gson();
					ErrorReq errorReq = gson1.fromJson(respuesta.toString(), ErrorReq.class);							
					Error error = new Error();
					error.setSql_code(errorReq.getCode());
					error.setError("Error en Actualiar Id grupo: "+grupo.getInteger("id_cga"));
					//error.setError("Error en Creaci�n de Usuario: "+alumno.getUsuario()+" "+respuesta.toString());
					error.setEst("A");
					errorDAO.saveOrUpdate(error);
				}
				//Insertamos al primer alumno en este grupo
				GrupoAlumno grupoAlumno = new GrupoAlumno();
				grupoAlumno.setId_alu(row.getInteger("id_alu"));
				grupoAlumno.setId_cgr(id_cgr);
				grupoAlumno.setEst("A");
				grupoAlumnoDAO.saveOrUpdate(grupoAlumno);
				//Hacemos el enrolamiento
				//Obtenemos datos del Alumno
				Alumno alumno = alumnoDAO.getByParams(new Param("id",row.getInteger("id_alu")));
				String json2="{"
						+"\"idClassroom\": \""+id_grupo+"\","
						+"\"idUsuario\": \""+alumno.getId_classRoom()+"\"}";
				
				Object respuesta2=restUtil.requestPOST("http://login.ae.edu.pe:8081/sige-google-api/course/enrole", "POST", json2);
				
				if(respuesta2.toString().equals("OK")){
					System.out.println(respuesta2.toString());
					
				} else {
					
				}
				
			} else{
				//Insertamos al alumno al grupo que no esta lleno
				GrupoAlumno grupoAlumno = new GrupoAlumno();
				grupoAlumno.setId_alu(row.getInteger("id_alu"));
				grupoAlumno.setId_cgr(grupoAulaVirtual.get(0).getInteger("id"));
				grupoAlumno.setEst("A");
				grupoAlumnoDAO.saveOrUpdate(grupoAlumno);
				//Hacemos el enrolamiento
				//Obtenemos datos del Alumno
				Alumno alumno = alumnoDAO.getByParams(new Param("id",row.getInteger("id_alu")));
				//Obtenemos datos del Grupo
				Row grupo =grupoAulaVirtualDAO.obtenerDatosGrupo(grupoAulaVirtual.get(0).getInteger("id"));
				String json2="{"
						+"\"idClassroom\": \""+grupo.getString("id_grupoclass")+"\","
						+"\"idUsuario\": \""+alumno.getId_classRoom()+"\"}";
				RestUtil restUtil = new RestUtil();
				Object respuesta2=restUtil.requestPOST("http://login.ae.edu.pe:8081/sige-google-api/course/enrole", "POST", json2);
				
				if(respuesta2.toString().equals("OK")){
					System.out.println(respuesta2.toString());
					
				} else {
					
				}
				//Verficamos si ya esta lleno
				Integer cant_inscritos=grupoAulaVirtualDAO.inscritosGrupo(grupoAulaVirtual.get(0).getInteger("id"));
				if(cant_inscritos==Integer.parseInt(grupoConfig.getCap())){
					//Ponemos a lleno el grupo
					grupoAulaVirtualDAO.actualizarEstadoGrupo(grupoAulaVirtual.get(0).getInteger("id"));
					//Creamos otro grupo
					GrupoAulaVirtual grupoAulaVirtual_nuevo=new GrupoAulaVirtual();
					grupoAulaVirtual_nuevo.setId_cgc(grupoConfig.getId());
					grupoAulaVirtual_nuevo.setId_gra(id_gra);
					grupoAulaVirtual_nuevo.setId_anio(id_anio);
					grupoAulaVirtual_nuevo.setDes("Grupo");
					grupoAulaVirtual_nuevo.setEst("A");
					//Cantidad de grupos
					List<Row> grupoAulaVirtual_llenos = grupoAulaVirtualDAO.listarGruposLlenos(id_gra, grupoConfig.getId(), id_anio);
					grupoAulaVirtual_nuevo.setNro(grupoAulaVirtual_llenos.size()+1);
					grupoAulaVirtual_nuevo.setEst("A");
					grupoAulaVirtual_nuevo.setLleno("0");
					Integer id_cga=grupoAulaVirtualDAO.saveOrUpdate(grupoAulaVirtual_nuevo);
					//Creamos el grupo en el classroom
					//Obtenemos datos del Grupo
					Row grupo_nuevo =grupoAulaVirtualDAO.obtenerDatosGrupo(id_cga);
					String moderador="116180455041256041855";
					String json ="{"
							+"\"nombre\": \""+grupo_nuevo.getString("nivel")+"\","
							+"\"seccion\": \""+grupo_nuevo.getString("grado")+" - "+grupo_nuevo.getString("grupo")+"\","
							+"\"descripcion\": \""+grupo_nuevo.getString("anio")+"-"+grupo_nuevo.getString("abrev")+"-"+grupo_nuevo.getString("abrv_classroom")+"-G"+grupo_nuevo.getInt("nro")+"\","
							+"\"idModerador\": \""+moderador+"\"}";
					
					Object respuesta=restUtil.requestPOST("http://login.ae.edu.pe:8081/sige-google-api/course/create", "POST", json);
					
					Gson gson = new Gson();
					GrupoAulaVirtualReq grupoAulaVirtualReq = gson.fromJson(respuesta.toString(), GrupoAulaVirtualReq.class);
					
					if(grupoAulaVirtualReq.getErrors()==null){
						String id_grupo=grupoAulaVirtualReq.getIdClassroom();
						//Actualizar el campo de id_Grupoclass
						grupoAulaVirtualDAO.actualizarIdGrupo(id_grupo, row.getInteger("id_cga"));
						System.out.println("update cvi_grupo_aula_virtual set id_grupoclass="+id_grupo+" where id="+row.getInteger("id_cga")+";");
						
					} else {
						Gson gson1 = new Gson();
						ErrorReq errorReq = gson1.fromJson(respuesta.toString(), ErrorReq.class);							
						Error error = new Error();
						error.setSql_code(errorReq.getCode());
						error.setError("Error en Actualiar Id grupo: "+grupo_nuevo.getInteger("id_cga"));
						//error.setError("Error en Creaci�n de Usuario: "+alumno.getUsuario()+" "+respuesta.toString());
						error.setEst("A");
						errorDAO.saveOrUpdate(error);
					}
				}
			}
		}
	}
	
	@Transactional
	public void crearGruposClassroom(Integer id_anio){	
		String moderador="116180455041256041855";
		//String json=null;
		/*{
	    "nombre": "INICIAL",
	    "seccion": "5 A�OS - GRUPO 1",
	    "descripcion" : "2020-IB-INI-5ANIOS-G1",
	    "idModerador": "102414161971166561521"
	},*/
		try {
			List<Row> grupos= grupoAulaVirtualDAO.listarGruposAulaVirtual(id_anio);
			
			//List<Row> lista_grupos = new ArrayList<Row>();

			for (Row row : grupos) {
				/*Row row1 = new Row();
				row1.put("nombre",row.getString("inicial"));
				row1.put("seccion",row.getString("grado")+" - "+row.getString("grupo"));
				row1.put("descripcion", row.getString("anio")+"-"+row.getString("abrev")+"-"+"abrv_classroom"+"-G"+row.getInt("nro"));
				row1.put("idModerador", moderador);
				lista_grupos.add(row);*/
				String json ="{"
						+"\"nombre\": \""+row.getString("nivel")+"\","
						+"\"seccion\": \""+row.getString("grado")+" - "+row.getString("grupo")+"\","
						+"\"descripcion\": \""+row.getString("anio")+"-"+row.getString("abrev")+"-"+row.getString("abrv_classroom")+"-G"+row.getInt("nro")+"\","
						+"\"idModerador\": \""+moderador+"\"}";
				RestUtil restUtil = new RestUtil();
				
				Object respuesta=restUtil.requestPOST("http://login.ae.edu.pe:8081/sige-google-api/course/create", "POST", json);
				
				Gson gson = new Gson();
				GrupoAulaVirtualReq grupoAulaVirtualReq = gson.fromJson(respuesta.toString(), GrupoAulaVirtualReq.class);
				
				if(grupoAulaVirtualReq.getErrors()==null){
					String id_grupo=grupoAulaVirtualReq.getIdClassroom();
					//Actualizar el campo de id_Grupoclass
					grupoAulaVirtualDAO.actualizarIdGrupo(id_grupo, row.getInteger("id_cga"));
					System.out.println("update cvi_grupo_aula_virtual set id_grupoclass="+id_grupo+" where id="+row.getInteger("id_cga")+";");
					
				} else {
					/*Gson gson = new Gson();
					ErrorReq errorReq = gson.fromJson(respuesta.toString(), ErrorReq.class);							
					Error error = new Error();
					error.setSql_code(errorReq.getCode());
					error.setError("Error en Creaci�n de Usuario: "+alumno.getUsuario()+" "+errorReq.getMessage());
					error.setId_cvi(inscripcionCampus.getId());
					//error.setError("Error en Creaci�n de Usuario: "+alumno.getUsuario()+" "+respuesta.toString());
					error.setEst("A");
					errorDAO.saveOrUpdate(error);*/
				}
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	@Transactional
	public void generarCursoAula(Integer id_cic){
		List<Row> aulas = aulaDAO.listAulasxCiclo(id_cic);
		for (Row row : aulas) {
			//Por cada aula crear los cursos aula
			Param param = new Param();
			param.put("id_cic", id_cic);
			param.put("id_gra", row.getInteger("id_grad"));
			//param.put("id",1280);
			List<CursoAnio> cursos_anios=cursoAnioDAO.listByParams(param, new String[]{"orden"});
			//Insertamos para cada aula cursos aula
			for (CursoAnio curso_anio : cursos_anios) {
				CursoAula cursoAula = new CursoAula();
				cursoAula.setId_au(row.getInteger("id"));
				cursoAula.setId_cua(curso_anio.getId());
				cursoAula.setId_tra(3);
				cursoAula.setEst("A");
				cursoAulaDAO.saveOrUpdate(cursoAula);
			}
		}	
	}
	
	/** Crear Ciclos x Giro y Ciclo
	 * 
	 * @param id_anio
	 * @param id_gir
	 * @param id_cic
	 */
	@Transactional
	public void crearGruposClassroomxGiro(Integer id_anio, Integer id_gir, Integer id_cic){	
		String moderador="109283927567092375308";
		Anio  anio =anioDAO.get(id_anio);
		//String json=null;
		/*{
	    "nombre": "INICIAL",
	    "seccion": "5 A�OS - GRUPO 1",
	    "descripcion" : "2020-IB-INI-5ANIOS-G1",
	    "idModerador": "102414161971166561521"
	},*/
		try {
			if(id_gir.equals(Constante.GIRO_ACADEMIA)) {
				List<Row> aulas = aulaDAO.listAulasxCiclo(id_cic);
				moderador="113822766055364156658";
				for (Row row : aulas) {
					//List<Row> cursos = cursoAnioDAO.listaCursosAula(id_anio, row.getInt("id"));
				//	for (Row row2 : cursos) { 2021REPASOAPREAC1
						String json ="{"
								+"\"nombre\": \""+anio.getNom()+row.getString("ciclo").replaceAll("\\s","")+"APRE"+row.getString("abrv")+row.getString("secc")+"\",\"seccion\": \"A-"+row.getString("ciclo").replaceAll("\\s","")+"-PRE-"+row.getString("grado")+row.getString("secc")+"\",\"descripcion\": \""+anio.getNom()+row.getString("ciclo").replaceAll("\\s","")+"APRE"+row.getString("abrv")+row.getString("secc")+"\",\"idModerador\": \""+moderador+"\"}";
						RestUtil restUtil = new RestUtil();
						
					
						
						Object respuesta=restUtil.requestPOST("http://localhost:8085/ae-google-api/course/create", "POST", json);
						
						Gson gson = new Gson();
						GrupoAulaVirtualReq grupoAulaVirtualReq = gson.fromJson(respuesta.toString(), GrupoAulaVirtualReq.class);
						
						if(grupoAulaVirtualReq.getErrors()==null){
							String id_grupo=grupoAulaVirtualReq.getIdClassroom();
							//Actualizar el campo de id_Grupoclass
							//grupoAulaVirtualDAO.actualizarIdGrupo(id_grupo, row.getInteger("id_cga"));
							//Usado cuando era curso aula
							/*cursoAulaDAO.actualizarIdClassroom(id_grupo, row2.getInteger("id_cca"));
							System.out.println("update col_curso_aula set cod_classroom="+id_grupo+" where id="+row2.getInteger("id_cca")+";");*/
							aulaDAO.actualizarIdClassroom(id_grupo, row.getInteger("id"));
							System.out.println("update col_aula set id_classroom="+id_grupo+" where id="+row.getInteger("id")+";");
						} else {
							/*Gson gson = new Gson();
							ErrorReq errorReq = gson.fromJson(respuesta.toString(), ErrorReq.class);							
							Error error = new Error();
							error.setSql_code(errorReq.getCode());
							error.setError("Error en Creaci�n de Usuario: "+alumno.getUsuario()+" "+errorReq.getMessage());
							error.setId_cvi(inscripcionCampus.getId());
							//error.setError("Error en Creaci�n de Usuario: "+alumno.getUsuario()+" "+respuesta.toString());
							error.setEst("A");
							errorDAO.saveOrUpdate(error);*/
						}
					//}
					
				}
			} else if(id_gir.equals(Constante.GIRO_VACACIONES)) {
				List<Row> aulas = aulaDAO.listAulasxCiclo(id_cic);
				for (Row row : aulas) {
					//List<Row> cursos = cursoAnioDAO.listaCursosAula(id_anio, row.getInt("id"));
					//for (Row row2 : cursos) {
					//	String json=null;
						String json ="{" //2021CPRIP6C
								//+"\"idClassroom\": \""+row.getString("cod_classroom")+"\","
								+"\"nombre\": \""+anio.getNom()+"VU"+row.getString("abrv_classroom")+row.getString("secc")+"\","
								+"\"seccion\": \""+row.getString("des_classroom")+"\","
								+"\"descripcion\": \""+anio.getNom()+"VU"+row.getString("abrv_classroom")+row.getString("secc")+"\","
								+"\"idModerador\": \""+moderador+"\"}";
						/*if(row.getString("nivel").equals("INICIAL")) {
							json ="{"
									//+"\"idClassroom\": \""+row.getString("cod_classroom")+"\","
									+"\"nombre\": \""+anio.getNom()+"VI"+row.getString("abrv")+row.getString("secc")+row2.getString("abrv_curso")+"\","
									//+"\"seccion\": \""+row.getString("grado")+" - "+row.getString("grupo")+"\","
									+"\"seccion\": \"VU-INI-"+row.getString("abrv")+row.getString("secc")+"-"+row2.getString("value")+"\","
									+"\"descripcion\": \""+anio.getNom()+"VI"+row.getString("abrv")+row.getString("secc")+row2.getString("abrv_curso")+"\"," 
									+"\"idModerador\": \""+moderador+"\"}";
						} else if(row.getString("nivel").equals("PRIMARIA")) {
							json ="{"
									//+"\"idClassroom\": \""+row.getString("cod_classroom")+"\","
									+"\"nombre\": \""+anio.getNom()+"VP"+row.getString("abrv")+row.getString("secc")+row2.getString("abrv_curso")+"\","
									//+"\"seccion\": \""+row.getString("grado")+" - "+row.getString("grupo")+"\","
									+"\"seccion\": \"VU-PRI-"+row.getString("abrv")+row.getString("secc")+"-"+row2.getString("value")+"\","
									+"\"descripcion\": \""+anio.getNom()+"VP"+row.getString("abrv")+row.getString("secc")+row2.getString("abrv_curso")+"\"," 
									+"\"idModerador\": \""+moderador+"\"}";
						} else if(row.getString("nivel").equals("SECUNDARIA")) {
							json ="{"
									//+"\"idClassroom\": \""+row.getString("cod_classroom")+"\","
									+"\"nombre\": \""+anio.getNom()+"VS"+row.getString("abrv")+row.getString("secc")+row2.getString("abrv_curso")+"\","
									//+"\"seccion\": \""+row.getString("grado")+" - "+row.getString("grupo")+"\","
									+"\"seccion\": \"VU-SEC-"+row.getString("abrv")+row.getString("secc")+"-"+row2.getString("value")+"\","
									+"\"descripcion\": \""+anio.getNom()+"VS"+row.getString("abrv")+row.getString("secc")+row2.getString("abrv_curso")+"\"," 
									+"\"idModerador\": \""+moderador+"\"}";
						}*/
						
						RestUtil restUtil = new RestUtil();
						System.out.println("json>"+json);
						Object respuesta=restUtil.requestPOST("http://localhost:8085/ae-google-api/course/create", "POST", json);
						
						Gson gson = new Gson();
						GrupoAulaVirtualReq grupoAulaVirtualReq = gson.fromJson(respuesta.toString(), GrupoAulaVirtualReq.class);
						
						if(grupoAulaVirtualReq.getErrors()==null){
							String id_classroom=grupoAulaVirtualReq.getIdClassroom();
							//Actualizar el campo de id_Grupoclass
							//grupoAulaVirtualDAO.actualizarIdGrupo(id_grupo, row.getInteger("id_cga"));
							aulaDAO.actualizarIdClassroom(id_classroom, row.getInteger("id"));
							System.out.println("update col_aula set id_classroom="+id_classroom+" where id="+row.getInteger("id_au")+";");
							
						} else {
							/*Gson gson = new Gson();
							ErrorReq errorReq = gson.fromJson(respuesta.toString(), ErrorReq.class);							
							Error error = new Error();
							error.setSql_code(errorReq.getCode());
							error.setError("Error en Creaci�n de Usuario: "+alumno.getUsuario()+" "+errorReq.getMessage());
							error.setId_cvi(inscripcionCampus.getId());
							//error.setError("Error en Creaci�n de Usuario: "+alumno.getUsuario()+" "+respuesta.toString());
							error.setEst("A");
							errorDAO.saveOrUpdate(error);*/
						}
					//}
					
				}
			}
			//Antes cuando era por curso
			/*else if(id_gir.equals(Constante.GIRO_VACACIONES)) {
				List<Row> aulas = aulaDAO.listAulasxCiclo(id_cic);
				for (Row row : aulas) {
					List<Row> cursos = cursoAnioDAO.listaCursosAula(id_anio, row.getInt("id"));
					for (Row row2 : cursos) {
						String json=null;
						if(row.getString("nivel").equals("INICIAL")) {
							json ="{"
									//+"\"idClassroom\": \""+row.getString("cod_classroom")+"\","
									+"\"nombre\": \""+anio.getNom()+"VI"+row.getString("abrv")+row.getString("secc")+row2.getString("abrv_curso")+"\","
									//+"\"seccion\": \""+row.getString("grado")+" - "+row.getString("grupo")+"\","
									+"\"seccion\": \"VU-INI-"+row.getString("abrv")+row.getString("secc")+"-"+row2.getString("value")+"\","
									+"\"descripcion\": \""+anio.getNom()+"VI"+row.getString("abrv")+row.getString("secc")+row2.getString("abrv_curso")+"\"," 
									+"\"idModerador\": \""+moderador+"\"}";
						} else if(row.getString("nivel").equals("PRIMARIA")) {
							json ="{"
									//+"\"idClassroom\": \""+row.getString("cod_classroom")+"\","
									+"\"nombre\": \""+anio.getNom()+"VP"+row.getString("abrv")+row.getString("secc")+row2.getString("abrv_curso")+"\","
									//+"\"seccion\": \""+row.getString("grado")+" - "+row.getString("grupo")+"\","
									+"\"seccion\": \"VU-PRI-"+row.getString("abrv")+row.getString("secc")+"-"+row2.getString("value")+"\","
									+"\"descripcion\": \""+anio.getNom()+"VP"+row.getString("abrv")+row.getString("secc")+row2.getString("abrv_curso")+"\"," 
									+"\"idModerador\": \""+moderador+"\"}";
						} else if(row.getString("nivel").equals("SECUNDARIA")) {
							json ="{"
									//+"\"idClassroom\": \""+row.getString("cod_classroom")+"\","
									+"\"nombre\": \""+anio.getNom()+"VS"+row.getString("abrv")+row.getString("secc")+row2.getString("abrv_curso")+"\","
									//+"\"seccion\": \""+row.getString("grado")+" - "+row.getString("grupo")+"\","
									+"\"seccion\": \"VU-SEC-"+row.getString("abrv")+row.getString("secc")+"-"+row2.getString("value")+"\","
									+"\"descripcion\": \""+anio.getNom()+"VS"+row.getString("abrv")+row.getString("secc")+row2.getString("abrv_curso")+"\"," 
									+"\"idModerador\": \""+moderador+"\"}";
						}
						
					/*	RestUtil restUtil = new RestUtil();
						System.out.println("json>"+json);
						Object respuesta=restUtil.requestPOST("http://localhost:8085/ae-google-api/course/create", "POST", json);
						
						Gson gson = new Gson();
						GrupoAulaVirtualReq grupoAulaVirtualReq = gson.fromJson(respuesta.toString(), GrupoAulaVirtualReq.class);
						
						if(grupoAulaVirtualReq.getErrors()==null){
							String id_grupo=grupoAulaVirtualReq.getIdClassroom();
							//Actualizar el campo de id_Grupoclass
							//grupoAulaVirtualDAO.actualizarIdGrupo(id_grupo, row.getInteger("id_cga"));
							cursoAulaDAO.actualizarIdClassroom(id_grupo, row2.getInteger("id_cca"));
							System.out.println("update col_curso_aula set cod_classroom="+id_grupo+" where id="+row2.getInteger("id_cca")+";");
							
						} else {
							/*Gson gson = new Gson();
							ErrorReq errorReq = gson.fromJson(respuesta.toString(), ErrorReq.class);							
							Error error = new Error();
							error.setSql_code(errorReq.getCode());
							error.setError("Error en Creaci�n de Usuario: "+alumno.getUsuario()+" "+errorReq.getMessage());
							error.setId_cvi(inscripcionCampus.getId());
							//error.setError("Error en Creaci�n de Usuario: "+alumno.getUsuario()+" "+respuesta.toString());
							error.setEst("A");
							errorDAO.saveOrUpdate(error);*/
				/*		}
					}
					
				}
			}*/ else {
				//List<Row> grupos= grupoAulaVirtualDAO.listarGruposAulaVirtual(id_anio); Esto funciono 2020
				List<Row> aulas = aulaDAO.listAulasxCiclo(id_cic);
				
				//List<Row> lista_grupos = new ArrayList<Row>();

				for (Row row3 : aulas) {
					
					String json ="{" //2021CPRIP6C
							//+"\"idClassroom\": \""+row.getString("cod_classroom")+"\","
							+"\"nombre\": \""+anio.getNom()+"C"+row3.getString("abrv_classroom")+row3.getString("secc")+"\","
							+"\"seccion\": \""+row3.getString("des_classroom")+"\","
							+"\"descripcion\": \""+anio.getNom()+"C"+row3.getString("abrv_classroom")+row3.getString("secc")+"\","
							+"\"idModerador\": \""+moderador+"\"}";
					RestUtil restUtil = new RestUtil();
					
					Object respuesta=restUtil.requestPOST("http://localhost:8085/ae-google-api/course/create", "POST", json);
					
					Gson gson = new Gson();
					GrupoAulaVirtualReq grupoAulaVirtualReq = gson.fromJson(respuesta.toString(), GrupoAulaVirtualReq.class);
					
					if(grupoAulaVirtualReq.getErrors()==null){
						String id_classroom=grupoAulaVirtualReq.getIdClassroom();
						//Actualizar el campo de id_Grupoclass
						aulaDAO.actualizarIdClassroom(id_classroom, row3.getInteger("id"));
						System.out.println("update col_aula set id_classroom="+id_classroom+" where id="+row3.getInteger("id_au")+";");
						
					} else {
						/*Gson gson = new Gson();
						ErrorReq errorReq = gson.fromJson(respuesta.toString(), ErrorReq.class);							
						Error error = new Error();
						error.setSql_code(errorReq.getCode());
						error.setError("Error en Creaci�n de Usuario: "+alumno.getUsuario()+" "+errorReq.getMessage());
						error.setId_cvi(inscripcionCampus.getId());
						//error.setError("Error en Creaci�n de Usuario: "+alumno.getUsuario()+" "+respuesta.toString());
						error.setEst("A");
						errorDAO.saveOrUpdate(error);*/
					}
				}
			}	
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	
	
	
	
	
	@Transactional
	public void asignacionClase(Integer id_anio){	
		
		try {
			
			List<GrupoAulaVirtual> lista_grupos=grupoAulaVirtualDAO.listByParams(new Param("id_anio",id_anio),null);
			
			//Response -> generar el JSON
			List<UsuarioGrupoReq> ListaGruposClass = new ArrayList<>();
			
			for (GrupoAulaVirtual grupoAulaVirtual : lista_grupos) {
				UsuarioGrupoReq GruposClass = new UsuarioGrupoReq();
				//if(grupoAulaVirtual.getId()!=78){
					GruposClass.setIdClassroom(grupoAulaVirtual.getId_grupoclass());
					
					List<Row> usuarios=grupoAulaVirtualDAO.listarUsuariosxGrupo(grupoAulaVirtual.getId());
					if(usuarios!=null){
						List<UsuarioClassroomEnrollReq> listaUsuarios = new ArrayList<>(); 
						UsuarioClassroomEnrollReq usuarioClassroomEnrollReq;
						for (Row row : usuarios) {
							usuarioClassroomEnrollReq = new UsuarioClassroomEnrollReq();
							usuarioClassroomEnrollReq.setIdUsuario(row.getString("idUsuario"));
							listaUsuarios.add(usuarioClassroomEnrollReq);
						}
						GruposClass.setListUsuarios(listaUsuarios);
					}
				//}
				ListaGruposClass.add(GruposClass);
			}
			
			
			RestUtil restUtil = new RestUtil();
			
			
			//Para recuperar listas de Servicio
			Gson gson = new Gson();
			String usuarioGrupoReqs = gson.toJson(ListaGruposClass);
			System.out.println(usuarioGrupoReqs);
	
			
			Object respuesta=restUtil.requestPOST("http://login.ae.edu.pe:8081/sige-google-api/course/enrole_full", "POST", usuarioGrupoReqs);
			
			if(respuesta.toString().equals("OK")){
				System.out.println(respuesta.toString());
				
			} else {
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			
		}
		
	}
	
	/**
	 * Enrolar x Ciclo
	 * @param id_anio
	 * @param id_cic
	 */
	@Transactional
	public void asignacionClasexCiclo(Integer id_anio, Integer id_cic){	
		
		try {
			Param param = new Param();
			//param.put("id_grad", 19);
			//param.put("id", 371);
			param.put("id_cic",id_cic);
			List<Aula> aulas=aulaDAO.listByParams(param,new String[]{"secc"});
			
			//Response -> generar el JSON
			List<UsuarioGrupoReq> ListaGruposClass = new ArrayList<>();
			
			//for (GrupoAulaVirtual grupoAulaVirtual : aulas) {
			for (Aula aula : aulas) {
				//List<CursoAula> cursos_aulas=cursoAulaDAO.listByParams(new Param("id_au",aula.getId()),new String[]{"id"});
				//Lista de matriculados
				//List<Matricula> matriculas = matriculaDAO.listFullByParams(new Param("mat.id_au_asi", aula.getId()), new String[]{"alu.id"});
				List<Row> matriculas = matriculaDAO.listarMatriculasValidadasxAulaAcademia(id_anio, aula.getId());
				//for (CursoAula cursoAula : cursos_aulas) {
					UsuarioGrupoReq GruposClass = new UsuarioGrupoReq();
					//if(grupoAulaVirtual.getId()!=78){
					try {
					
						//if( cursoAula.getCod_classroom().equals("220489280209")) {
							System.out.println("entro");
							//GruposClass.setIdClassroom(cursoAula.getCod_classroom());;
							GruposClass.setIdClassroom(aula.getId_classroom());
							
							//List<Row> usuarios=grupoAulaVirtualDAO.listarUsuariosxGrupo(grupoAulaVirtual.getId());
							if(matriculas!=null){
								List<UsuarioClassroomEnrollReq> listaUsuarios = new ArrayList<>(); 
								UsuarioClassroomEnrollReq usuarioClassroomEnrollReq;
								for (Row matricula : matriculas) {
									/*usuarioClassroomEnrollReq = new UsuarioClassroomEnrollReq();
									System.out.println("alumno>"+matricula.getAlumno().toString());
									System.out.println("matricula.getAlumno().getId()>"+matricula.getAlumno().getId());
									System.out.println("matricula.getAlumno().getId_classRoom()>"+matricula.getAlumno().getId_classRoom());
									usuarioClassroomEnrollReq.setIdUsuario(matricula.getAlumno().getId_classRoom());
									listaUsuarios.add(usuarioClassroomEnrollReq);*/
									usuarioClassroomEnrollReq = new UsuarioClassroomEnrollReq();
									System.out.println("alumno>"+matricula.getString("alumno"));
									System.out.println("matricula.getAlumno().getId()>"+matricula.getInteger("id_alu"));
									System.out.println("matricula.getAlumno().getId_classRoom()>"+matricula.getString("id_classRoom"));
									usuarioClassroomEnrollReq.setIdUsuario(matricula.getString("id_classRoom"));
									listaUsuarios.add(usuarioClassroomEnrollReq);
								}
								GruposClass.setListUsuarios(listaUsuarios);
							}
						//}
						ListaGruposClass.add(GruposClass);
					//	}
						
					} catch (Exception e) {
						System.out.println("Error0>"  + e.getMessage() + ">" + e );
					}
						
				//}
				
			}
			
			
			RestUtil restUtil = new RestUtil();
			
			
			//Para recuperar listas de Servicio
			Gson gson = new Gson();
			String usuarioGrupoReqs = gson.toJson(ListaGruposClass);
			System.out.println(usuarioGrupoReqs);
	
			
			Object respuesta=restUtil.requestPOST("http://localhost:8085/ae-google-api/course/enrole_full", "POST", usuarioGrupoReqs);
			
			//if(respuesta.toString().equals("OK")){
				System.out.println(respuesta.toString());
				
			/*} else {
				System.out.print("Error");
			}*/
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print("ERROR>" + e.getMessage() + " > " + e);
		}
		
	}
	
	/**
	 * Enrolar x Ciclo
	 * @param id_anio
	 * @param id_cic
	 */
	@Transactional
	public void asignacionClasexCicloColegio(Integer id_anio, Integer id_cic){	
		
		try {
			Param param = new Param();
			//param.put("id_grad", 19);
			//param.put("id", 416);
			param.put("id_cic",id_cic);
			List<Aula> aulas=aulaDAO.listByParams(param,new String[]{"secc"});
			
			//Response -> generar el JSON
			List<UsuarioGrupoReq> ListaGruposClass = new ArrayList<>();
			
			//for (GrupoAulaVirtual grupoAulaVirtual : aulas) {
			for (Aula aula : aulas) {
				//List<CursoAula> cursos_aulas=cursoAulaDAO.listByParams(new Param("id_au",aula.getId()),new String[]{"id"});
				//Lista de matriculados
				List<Row> matriculas = matriculaDAO.listarMatriculasValidadasxAula(id_anio, aula.getId());
					UsuarioGrupoReq GruposClass = new UsuarioGrupoReq();
					//if(grupoAulaVirtual.getId()!=78){
					try {
					
						//if( cursoAula.getCod_classroom().equals("220489280209")) {
							System.out.println("entro");
							GruposClass.setIdClassroom(aula.getId_classroom());
							
							//List<Row> usuarios=grupoAulaVirtualDAO.listarUsuariosxGrupo(grupoAulaVirtual.getId());
							if(matriculas!=null){
								List<UsuarioClassroomEnrollReq> listaUsuarios = new ArrayList<>(); 
								UsuarioClassroomEnrollReq usuarioClassroomEnrollReq;
								for (Row matricula : matriculas) {
									//Primero debemos de activar su cuenta
									usuarioClassroomEnrollReq = new UsuarioClassroomEnrollReq();
									System.out.println("alumno>"+matricula.getString("alumno"));
									System.out.println("matricula.getAlumno().getId()>"+matricula.getInteger("id_alu"));
									System.out.println("matricula.getAlumno().getId_classRoom()>"+matricula.getString("id_classRoom"));
									usuarioClassroomEnrollReq.setIdUsuario(matricula.getString("id_classRoom"));
									listaUsuarios.add(usuarioClassroomEnrollReq);
								}
								GruposClass.setListUsuarios(listaUsuarios);
							}
						//}
						ListaGruposClass.add(GruposClass);
					//	}
						
					} catch (Exception e) {
						System.out.println("Error0>"  + e.getMessage() + ">" + e );
					}
				
			}
			
			
			RestUtil restUtil = new RestUtil();
			
			
			//Para recuperar listas de Servicio
			Gson gson = new Gson();
			String usuarioGrupoReqs = gson.toJson(ListaGruposClass);
			System.out.println(usuarioGrupoReqs);
	
			
			Object respuesta=restUtil.requestPOST("http://localhost:8085/ae-google-api/course/enrole_full", "POST", usuarioGrupoReqs);
			
			//if(respuesta.toString().equals("OK")){
				System.out.println(respuesta.toString());
				
			/*} else {
				System.out.print("Error");
			}*/
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print("ERROR>" + e.getMessage() + " > " + e);
		}
		
	}
	
	@Transactional
	public void asignacionClasexCicloColegioVU(Integer id_anio, Integer id_cic){	
		
		try {
			Param param = new Param();
			//param.put("id_grad", 19);
			//param.put("id", 416);
			param.put("id_cic",id_cic);
			List<Aula> aulas=aulaDAO.listByParams(param,new String[]{"secc"});
			
			//Response -> generar el JSON
			List<UsuarioGrupoReq> ListaGruposClass = new ArrayList<>();
			
			//for (GrupoAulaVirtual grupoAulaVirtual : aulas) {
			for (Aula aula : aulas) {
				//List<CursoAula> cursos_aulas=cursoAulaDAO.listByParams(new Param("id_au",aula.getId()),new String[]{"id"});
				//Lista de matriculados
				List<Row> matriculas = matriculaDAO.listarMatriculasValidadasxAulaVU(id_anio, aula.getId());
					UsuarioGrupoReq GruposClass = new UsuarioGrupoReq();
					//if(grupoAulaVirtual.getId()!=78){
					try {
					
						//if( cursoAula.getCod_classroom().equals("220489280209")) {
							System.out.println("entro");
							GruposClass.setIdClassroom(aula.getId_classroom());
							
							//List<Row> usuarios=grupoAulaVirtualDAO.listarUsuariosxGrupo(grupoAulaVirtual.getId());
							if(matriculas!=null){
								List<UsuarioClassroomEnrollReq> listaUsuarios = new ArrayList<>(); 
								UsuarioClassroomEnrollReq usuarioClassroomEnrollReq;
								for (Row matricula : matriculas) {
									//Primero debemos de activar su cuenta
									usuarioClassroomEnrollReq = new UsuarioClassroomEnrollReq();
									System.out.println("alumno>"+matricula.getString("alumno"));
									System.out.println("matricula.getAlumno().getId()>"+matricula.getInteger("id_alu"));
									System.out.println("matricula.getAlumno().getId_classRoom()>"+matricula.getString("id_classRoom"));
									usuarioClassroomEnrollReq.setIdUsuario(matricula.getString("id_classRoom"));
									listaUsuarios.add(usuarioClassroomEnrollReq);
								}
								GruposClass.setListUsuarios(listaUsuarios);
							}
						//}
						ListaGruposClass.add(GruposClass);
					//	}
						
					} catch (Exception e) {
						System.out.println("Error0>"  + e.getMessage() + ">" + e );
					}
				
			}
			
			
			RestUtil restUtil = new RestUtil();
			
			
			//Para recuperar listas de Servicio
			Gson gson = new Gson();
			String usuarioGrupoReqs = gson.toJson(ListaGruposClass);
			System.out.println(usuarioGrupoReqs);
	
			
			Object respuesta=restUtil.requestPOST("http://localhost:8085/ae-google-api/course/enrole_full", "POST", usuarioGrupoReqs);
			
			//if(respuesta.toString().equals("OK")){
				System.out.println(respuesta.toString());
				
			/*} else {
				System.out.print("Error");
			}*/
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print("ERROR>" + e.getMessage() + " > " + e);
		}
		
	}
	
	@Transactional
	public void actualizarIdAlumno(){	
		
		try {
			
			RestUtil restUtil = new RestUtil();
			
			Object respuesta = restUtil.requestPOST("http://login.ae.edu.pe:8081/sige-google-api/user/list", "GET",null);
			
			//String cadena=""
			System.out.println(respuesta);
			//JSONArray arregloJson = new JSONArray(respuesta);
	
			//Para recuperar listas de Servicio
			Gson gson = new Gson();
			Type listType = new TypeToken<ArrayList<UsuarioGoogleReq>>(){}.getType();
			ArrayList<UsuarioGoogleReq> grupoAulaVirtualReq = gson.fromJson(respuesta.toString(), listType);
			
			
			for (UsuarioGoogleReq usuarioGoogleReq2 : grupoAulaVirtualReq) {
				 String usuario = usuarioGoogleReq2.getEmail();
				 String idUsuario= usuarioGoogleReq2.getIdUsuario();
				//Actualizamos el idGoogle 
				 alumnoDAO.actualizarIdClasroomAlumno(idUsuario, usuario);
			}			
			/*for (int indice = 0; indice < arregloJson.length(); indice++) {
				JSONObject alumno = arregloJson.getJSONObject(indice);
			    String usuario = alumno.getString("email");
			    String idUsuario= alumno.getString("idUsuario");
			   //Actualizamos el idGoogle 
			    alumnoDAO.actualizarIdClasroomAlumno(idUsuario, usuario);
				
			}*/
						
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	@Transactional
	public void sincronizarUsuarios(Integer id_anio){	
		
		try {
			
			List<Row> inscritos=inscripcionCampusDAO.listarInscritosxAnio(id_anio);
			Param param = new Param();
			param.put("nom", "NOMBRE_COLEGIO");
			String colegio=parametroDAO.getByParams(param).getVal();
			
			//Response -> generar el JSON
			List<UsuarioGoogleReq> listaGruposClass = new ArrayList<>();
			
			for (Row row : inscritos) {
				UsuarioGoogleReq usuarioGoogleReq = new UsuarioGoogleReq();
				usuarioGoogleReq.setEmail(row.getString("usuario"));
				usuarioGoogleReq.setPath("/"+colegio+"/"+row.getString("nivel")+"/"+row.getString("grado"));
				listaGruposClass.add(usuarioGoogleReq);
			}
			
			RestUtil restUtil = new RestUtil();
			
			//Para recuperar listas de Servicio
			Gson gson = new Gson();
			String usuarioGrupoReqs = gson.toJson(listaGruposClass);
			System.out.println(usuarioGrupoReqs);
			
		} catch (Exception e) {
			// TODO: handle exception
			
		}
		
	}
	
	@Transactional
	public void matriculacionFull(Integer id_anio, Integer nro_pe){	
		
		try {
			
			List<Row> inscritos=inscripcionCampusDAO.listarInscritosxAnioyBimestre(id_anio, nro_pe);
			
			List<MatriculaAulaVirtualReq> listaMatriculas = new ArrayList<>();
			for (Row row : inscritos) {
				MatriculaAulaVirtualReq matriculaAulaVirtualReq= new MatriculaAulaVirtualReq();
				matriculaAulaVirtualReq.setCorreo(row.getString("usr"));
				//"2020-IB-SEC-1ER-G1-EDF"
				matriculaAulaVirtualReq.setCodigoCurso(row.getString("anio")+"-"+row.getString("periodo_abrev")+"-"+row.getString("abrv_classroom")+"-G"+row.getString("nro_grupo")+"-"+row.getString("curso_abrev"));
				listaMatriculas.add(matriculaAulaVirtualReq);
			}
			
			//Para recuperar listas de Servicio
			Gson gson = new Gson();
			String matriculas = gson.toJson(listaMatriculas);

			RestUtil restUtil = new RestUtil();	
			
			Object respuesta=restUtil.requestPOST("http://login.ae.edu.pe:8081/sige-google-api/course/enrole_full", "POST", matriculas);
			
			if(respuesta.toString().equals("OK")){
				System.out.println(respuesta.toString());
				
			} else {
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			
		}
		
	}
	
	@Transactional
	public void desactivarCursos(Integer id_anio){	
		//String json=null;
		/*{
	    "nombre": "INICIAL",
	    "seccion": "5 A�OS - GRUPO 1",
	    "descripcion" : "2020-IB-INI-5ANIOS-G1",
	    "idModerador": "102414161971166561521"
	},*/
		try {
			List<Row> grupos= grupoAulaVirtualDAO.listarGruposAulaVirtualTodos(id_anio);
			for (Row row : grupos) {
				String cod_classroom=row.getString("id_grupoclass");
				RestUtil restUtil = new RestUtil();
				/*/String json ="{"
						//+"\"idClassroom\": \""+row.getString("cod_classroom")+"\","
						+"\"token\": \"$2a$10$f9B5cI5cSsM6HgSmImpQYudIQbkuZQt8lZobcBQI9FH2eJlPlacwW\","
						+"\"idCurso\": \""+cod_classroom+"\","
						+"\"stateCurso\": \"SUSPENDED\"}";*/
				//Object respuesta=restUtil.requestPOST("http://localhost:8085/ae-google-api/course/desactive", "POST",json);
				Object respuesta=restUtil.requestPOST("http://localhost:8085/ae-google-api/course/delete/"+cod_classroom, "DELETE",null);
				
				if(respuesta.toString().equals("OK")){
					System.out.println(respuesta.toString());
					
				} else {
					System.out.println("NO DESACTIVO");
				}
				
			}
			//Desactivamos el ciclo
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}
	
	@Transactional
	public void desactivarCursosxCiclo(Integer id_cic){	
		//String json=null;
		/*{
	    "nombre": "INICIAL",
	    "seccion": "5 A�OS - GRUPO 1",
	    "descripcion" : "2020-IB-INI-5ANIOS-G1",
	    "idModerador": "102414161971166561521"
	},*/
		try {
			List<CursoAula> cursoAula = cursoAulaDAO.listFullByParams(new Param("au.id_cic",id_cic), null);
			for (CursoAula cursoAula2 : cursoAula) {
				String cod_classroom=cursoAula2.getCod_classroom();
				RestUtil restUtil = new RestUtil();
				/*/String json ="{"
						//+"\"idClassroom\": \""+row.getString("cod_classroom")+"\","
						+"\"token\": \"$2a$10$f9B5cI5cSsM6HgSmImpQYudIQbkuZQt8lZobcBQI9FH2eJlPlacwW\","
						+"\"idCurso\": \""+cod_classroom+"\","
						+"\"stateCurso\": \"SUSPENDED\"}";*/
				//Object respuesta=restUtil.requestPOST("http://localhost:8085/ae-google-api/course/desactive", "POST",json);
				Object respuesta=restUtil.requestPOST("http://localhost:8085/ae-google-api/course/delete/"+cod_classroom, "DELETE",null);
				
				if(respuesta.toString().equals("OK")){
					System.out.println(respuesta.toString());
					//desactivamos el curso Aula
					cursoAulaDAO.desactivarCursoAula(cursoAula2.getId());
				} else {
					System.out.println("NO DESACTIVO");
				}
				
			}
			cicloDAO.desactivarCiclo(id_cic);
			//desactivar las cuentas de los del ciclo
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}
	
	
	@Transactional
	public void desactivarAulasxCiclo(Integer id_cic){	
		//String json=null;
		/*{
	    "nombre": "INICIAL",
	    "seccion": "5 A�OS - GRUPO 1",
	    "descripcion" : "2020-IB-INI-5ANIOS-G1",
	    "idModerador": "102414161971166561521"
	},*/
		try {
			List<Aula> aulas = aulaDAO.listFullByParams(new Param("aula.id_cic",id_cic), null);
			for (Aula aula : aulas) {				
				String cod_classroom=aula.getId_classroom();
				RestUtil restUtil = new RestUtil();
				/*/String json ="{"
						//+"\"idClassroom\": \""+row.getString("cod_classroom")+"\","
						+"\"token\": \"$2a$10$f9B5cI5cSsM6HgSmImpQYudIQbkuZQt8lZobcBQI9FH2eJlPlacwW\","
						+"\"idCurso\": \""+cod_classroom+"\","
						+"\"stateCurso\": \"SUSPENDED\"}";*/
				//Object respuesta=restUtil.requestPOST("http://localhost:8085/ae-google-api/course/desactive", "POST",json);
				Object respuesta=restUtil.requestPOST("http://localhost:8085/ae-google-api/course/delete/"+cod_classroom, "DELETE",null);
				
				if(respuesta.toString().equals("OK")){
					System.out.println(respuesta.toString());
					//desactivamos el curso Aula
					cursoAulaDAO.desactivarCursoAula(aula.getId());
				} else {
					System.out.println("NO DESACTIVO");
				}
				
			}
			cicloDAO.desactivarCiclo(id_cic);
			//desactivar las cuentas de los del ciclo
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}
	
	
	@Transactional
	public void desacticarCuentasxCiclo(Integer id_cic){	
		//String json=null;
		/*{
	    "nombre": "INICIAL",
	    "seccion": "5 A�OS - GRUPO 1",
	    "descripcion" : "2020-IB-INI-5ANIOS-G1",
	    "idModerador": "102414161971166561521"
	},*/
		try {
			//List<Matricula> matriculas = matriculaDAO.listFullByParams(new Param("aula.id_cic",id_cic), null);
			Param param = new Param();
			param.put("mat.id_gra", 13);
			param.put("pee.id_anio", 4);
			List<Matricula> matriculas = matriculaDAO.listFullByParams(param, null);
			for (Matricula matricula : matriculas) {
				String usuario_alu=matricula.getAlumno().getUsuario();
				String json ="{"
						//+"\"idClassroom\": \""+row.getString("cod_classroom")+"\","
						+"\"email\": \""+usuario_alu+"\","
						+"\"suspendido\": \""+true+"\","
						+"\"motivoSuspension\": \"Fin de ciclo académico\"}";
				RestUtil restUtil = new RestUtil();
				Object respuesta=restUtil.requestPOST("http://localhost:8085/ae-google-api/user/desactive", "POST",json);
				
				if(respuesta.toString().equals("OK")){
					System.out.println(respuesta.toString());
					//Ponemos en inactivo la cuenta google
					alumnoDAO.desactivarCuentaGoogle(matricula.getAlumno().getId());
				} else {
					System.out.println("NO DESACTIVO");
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}
	
	@Transactional
	public void activarCuentaGoogleUsr(String usuario){	
		//String json=null;
		/*{
	    "nombre": "INICIAL",
	    "seccion": "5 A�OS - GRUPO 1",
	    "descripcion" : "2020-IB-INI-5ANIOS-G1",
	    "idModerador": "102414161971166561521"
	},*/
		try {
			//List<Matricula> matriculas = matriculaDAO.listFullByParams(new Param("aula.id_cic",id_cic), null);
			/*Param param = new Param();
			param.put("mat.id_gra", 13);
			param.put("pee.id_anio", 4);
			List<Matricula> matriculas = matriculaDAO.listFullByParams(param, null);
			for (Matricula matricula : matriculas) {*/
				//String usuario_alu=matricula.getAlumno().getUsuario();
				String json ="{"
						//+"\"idClassroom\": \""+row.getString("cod_classroom")+"\","
						+"\"email\": \""+usuario+"\","
						+"\"suspendido\": \""+false+"\","
						+"\"motivoSuspension\": \"Matrícula\"}";
				RestUtil restUtil = new RestUtil();
				Object respuesta=restUtil.requestPOST("http://login.ae.edu.pe:8085/ae-google-api/user/desactive", "POST",json);
				
				if(respuesta.toString().equals("OK")){
					System.out.println(respuesta.toString());
					//Ponemos en inactivo la cuenta google
					Param param = new Param();
					param.put("usuario", usuario);
					Alumno alumo = alumnoDAO.getByParams(param);
					alumnoDAO.activarCuentaGoogle(alumo.getId());
				} else {
					System.out.println("NO DESACTIVO");
				}
			//}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}

}

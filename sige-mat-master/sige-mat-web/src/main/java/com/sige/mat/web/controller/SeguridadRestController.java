package com.sige.mat.web.controller;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.annotation.JsonView;
import com.sige.common.enums.EnumPerfil;
import com.sige.mat.dao.AlumnoDAO;
import com.sige.mat.dao.ContadorDAO;
import com.sige.mat.dao.EmpresaDAO;
import com.sige.mat.dao.FamiliarDAO;
import com.sige.mat.dao.GruFamDAO;
import com.sige.mat.dao.LogLoginDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.OpcionDAO;
import com.sige.mat.dao.PersonaDAO;
import com.sige.mat.dao.TrabajadorDAO;
import com.sige.mat.dao.UsuarioDAO;
import com.sige.mat.dao.UsuarioRolDAO;
import com.sige.mat.dao.UsuarioTokenDAO;
import com.sige.spring.service.SeguridadService;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.Contador;
import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.GruFam;
import com.tesla.colegio.model.LogLogin;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.MensajeriaFamiliar;
import com.tesla.colegio.model.Opcion;
import com.tesla.colegio.model.Persona;
import com.tesla.colegio.model.Trabajador;
import com.tesla.colegio.model.Usuario;
import com.tesla.colegio.model.UsuarioRol;
import com.tesla.colegio.model.UsuarioSeg;
import com.tesla.colegio.model.UsuarioToken;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.rest.util.Views;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.CorreoUtil;
import com.tesla.frmk.util.FechaUtil;
import com.tesla.frmk.util.StringUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping(value = "/api/seguridad")
//@PropertySource("classpath:config.properties")
public class SeguridadRestController {
	final static Logger logger = Logger.getLogger(SeguridadRestController.class);

	@Autowired
	private TokenSeguridad tokenStrategy;

	@Autowired
	private Environment env;

	@Autowired
	private HttpSession httpSession;

	@Autowired
	private UsuarioDAO usuarioDAO;

	@Autowired
	private UsuarioRolDAO usuarioRolDAO;

	@Autowired
	private UsuarioTokenDAO usuarioTokenDAO;

	@Autowired
	private TrabajadorDAO trabajadorDAO;

	@Autowired
	private OpcionDAO opcionDAO;

	@Autowired
	private FamiliarDAO familiarDAO;

	@Autowired
	private LogLoginDAO logLoginDAO;

	@Autowired
	private EmpresaDAO empresaDAO;
	
	@Autowired
	private GruFamDAO gruFamDAO;
	
	@Autowired
	private PersonaDAO personaDAO;

	@Autowired
	private MatriculaDAO matriculaDAO;
	
	@Autowired
	private AlumnoDAO alumnoDao;
	
	@Autowired
	private ContadorDAO contadorDAO;

	@Autowired
	private SeguridadService seguridadService;

	Properties prop = new Properties();
	InputStream input = null;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public AjaxResponseBody login(Usuario usuario, HttpServletRequest request) {
		String token = null;

		AjaxResponseBody result = new AjaxResponseBody();

		String ip = request.getRemoteAddr();

		logger.debug("ip:hora" + ip + "-" + new Date() + "-" + usuario.getLogin());
		/*
		 * if (usuario.getLogin() != "" && usuario.getPassword() != "") { if
		 * (usuario.getId_per() == EnumPerfil.PERFIL_TRABAJADOR.getValue() ||
		 * usuario.getId_per() == EnumPerfil.PERFIL_EXTERNO.getValue()) {
		 */

		if (usuario.getLogin() != "" && usuario.getPassword() != "") {

			Param param = new Param();
			param.put("usr.login", usuario.getLogin());
			param.put("usr.password", usuario.getPassword());
			// param.put("usr.est", "A");

			List<Usuario> usuarios = usuarioDAO.listFullByParams(param, null);

			if (usuarios.size() > 0 && usuarios.get(0).getEst().equals("A")) {

				// TOKEN
				List<Opcion> opciones = new ArrayList<Opcion>();

				int id_per = usuarios.get(0).getId_per();

				try {

					String nombres = null;
					if (id_per == EnumPerfil.PERFIL_TRABAJADOR.getValue() || id_per == EnumPerfil.PERFIL_EXTERNO.getValue()) {

						nombres = usuarios.get(0).getPersona().getApe_pat() + " "
								+ usuarios.get(0).getPersona().getApe_mat() + ", "
								+ usuarios.get(0).getPersona().getNom();
					} else if (id_per == EnumPerfil.PERFIL_FAMILIAR.getValue()) {
						// obtener query para el nombre del familiar
						GruFam gruFam=gruFamDAO.getByParams(new Param("id_usr",usuarios.get(0).getId()));
						//nombres = "PADRE DE FAMILIA";
						nombres = gruFam.getNom();
					}

					usuario.setId_per(id_per);
					UsuarioSeg usuarioSeg = new UsuarioSeg();
					usuarioSeg.setId_per(usuario.getId_per());
					usuarioSeg.setLogin(usuario.getLogin());
					usuarioSeg.setPassword(usuarios.get(0).getPassword());
					usuarioSeg.setId(usuarios.get(0).getId());
					usuarioSeg.setNombres(nombres.toUpperCase());
					usuarioSeg.setId_suc(usuarios.get(0).getId_suc());
					usuarioSeg.setId_tra(usuarios.get(0).getTrabajador().getId());
					usuarioSeg.setIni(usuarios.get(0).getIni());
					param = new Param();
					param.put("id_usr", usuarios.get(0).getId());
					param.put("est", "A");
					List<UsuarioRol> usuarioRoles = usuarioRolDAO.listByParams(param, null);

					Integer roles[] = new Integer[usuarioRoles.size()];

					opciones = opcionDAO.getListAplicacionesXusuario(usuarioSeg.getId());

					int i = 0;
					for (UsuarioRol usuarioRol : usuarioRoles) {
						roles[i++] = usuarioRol.getId_rol();
					}
					usuarioSeg.setRoles(roles);

					// GENERA EL TOKEN
					// token = seguridadService.authenticateAndSignToken(usuarioSeg);
					token = tokenStrategy.getJWTToken(usuarioSeg);

				} catch (Exception e) {
					result.setCode("401");
					result.setMsg("No se puede generar el token de autenticacion");
					e.printStackTrace();
				}

				// LOG DE EXITO
				LogLogin log_login = new LogLogin();
				log_login.setEst("A");
				log_login.setId_per(usuario.getId_per());
				log_login.setIp(ip);
				log_login.setId_usr(usuarios.get(0).getId());
				log_login.setExito("1");
				logLoginDAO.saveOrUpdate(log_login);

				result.setCode("200");
				Row row = new Row();
				row.put("token", token);
				row.put("usuario", usuarios.get(0));
				row.put("opciones", opciones);
				result.setResult(row);

				logLoginDAO.resetearIntentosFallidos(usuarios.get(0).getId(), usuario.getId_per());

			} else {

				// LOG DE ERROR
				param = new Param();
				param.put("usr.login", usuario.getLogin());
				// param.put("usr.est", "A");

				usuarios = usuarioDAO.listFullByParams(param, null);

				if (usuarios.size() > 0) {

					// cantidad de intentos
					int intentos = logLoginDAO.cantidadAccesosFallidos(usuarios.get(0).getId(), usuario.getId_per());

					if (usuarios.get(0).getEst().equals("A")) {
						if (intentos == 0)
							result.setMsg(
									"Revisa que los datos que ingresaste sean los correctos!, si olvidaste el password, haz click en <a href='#' onclick='abrirRecover()'>Olvido su clave?</a>.<br>"
											+ "Tiene 2 intentos m�s, antes que su cuenta sea bloqueada.");
						else if (intentos == 1)
							result.setMsg(
									"Revisa que los datos que ingresaste sean los correctos!, si olvidaste el password, haz click en <a href='#' onclick='abrirRecover()'>Olvido su clave?</a>.<br>"
											+ "Tiene 1 intento m�s, antes que su cuenta sea bloqueada.");
						else
							result.setMsg("Su cuenta está bloqueada!, por superar el l�mite de intentos de ingresar");
					} else {
						result.setMsg("Usuario bloqueado");
					}
					LogLogin log_login = new LogLogin();
					log_login.setEst("A");
					log_login.setId_per(usuario.getId_per());
					log_login.setIp(ip);
					log_login.setId_usr(usuarios.get(0).getId());
					log_login.setExito("0");
					logLoginDAO.saveOrUpdate(log_login);

					if (intentos > 1) {
						usuarioDAO.actualizarEstado(log_login.getId_usr(), "I");
					}

				} else
					result.setMsg("Usuario, Clave o Tipo de usuario equivocado");

				result.setCode("204");

			}

		}
		/*
		 * if (usuario.getId_per() == EnumPerfil.PERFIL_FAMILIAR.getValue()) { // if
		 * (usuario.getId_per() == 1000) {
		 * 
		 * Param param = new Param(); param.put("nro_doc", usuario.getLogin());
		 * param.put("pass", usuario.getPassword()); param.put("est", "A");
		 * 
		 * Familiar familiar = familiarDAO.getByParams(param);
		 * 
		 * if (familiar != null) {
		 * 
		 * // TOKEN // String token = null; try { UsuarioSeg usuarioSeg = new
		 * UsuarioSeg(); usuarioSeg.setId_per(usuario.getId_per());
		 * usuarioSeg.setLogin(usuario.getLogin());
		 * usuarioSeg.setPassword(familiar.getPass());
		 * usuarioSeg.setId(familiar.getId()); usuarioSeg.setIni(familiar.getIni());
		 * String nombres = familiar.getApe_mat() + " " + familiar.getApe_pat() + ", " +
		 * familiar.getNom(); usuarioSeg.setNombres(nombres.toUpperCase()); //
		 * usuarioSeg.setId_suc(usuarios.get(0).getId_suc());
		 * 
		 * 
		 * token = tokenStrategy.getJWTToken(usuarioSeg );
		 * 
		 * // Consulto si esta dentro del cronograma // Boolean existe =
		 * familiarDAO.existeCronograma(familiar.getId()); Date fecha_actual = new
		 * Date();
		 * 
		 * // // if(!existe){ result. //
		 * setMsg("Usted no se encuentra en el horario programado para su inscripci�n. <a href='http://ae.edu.pe:8080/documentos/Cronograma_Matricula2020.pdf' target='_blank'>Verifique cronograma</a>"
		 * // ); result.setCode("500"); return result; } // } catch (Exception e) {
		 * result.setCode("401");
		 * result.setMsg("No se puede generar el token de autenticacion");
		 * e.printStackTrace(); }
		 * 
		 * LogLogin log_login = new LogLogin(); log_login.setEst("A");
		 * log_login.setId_per(usuario.getId_per()); log_login.setIp(ip);
		 * log_login.setId_usr(familiar.getId()); log_login.setExito("1");
		 * logLoginDAO.saveOrUpdate(log_login);
		 * 
		 * result.setCode("200"); usuario.setPassword("");
		 * usuario.setId(familiar.getId());
		 * usuario.setId_per(EnumPerfil.PERFIL_FAMILIAR.getValue());
		 * usuario.setIni(familiar.getIni()); httpSession.setAttribute("_USUARIO",
		 * usuario); httpSession.setAttribute("_FAMILIAR", familiar);
		 * 
		 * logLoginDAO.resetearIntentosFallidos(familiar.getId(), usuario.getId_per());
		 * 
		 * Row row = new Row(); row.put("token", token); row.put("usuario", usuario);
		 * result.setResult(row);
		 * 
		 * // result.setResult(usuario); } else {
		 * 
		 * // DESACTIVAR USUARIO param = new Param(); param.put("nro_doc",
		 * usuario.getLogin()); param.put("est", "A");
		 * 
		 * familiar = familiarDAO.getByParams(param);
		 * 
		 * if (familiar != null) { LogLogin log_login = new LogLogin();
		 * log_login.setEst("A"); log_login.setId_per(usuario.getId_per());
		 * log_login.setIp(ip); log_login.setId_usr(familiar.getId());
		 * log_login.setExito("0"); logLoginDAO.saveOrUpdate(log_login);
		 * 
		 * // cantidad de intentos // cantidad de intentos int intentos =
		 * logLoginDAO.cantidadAccesosFallidos(familiar.getId(), usuario.getId_per());
		 * 
		 * if (familiar.getEst().equals("A")) { if (intentos == 1) result.setMsg(
		 * "�Revisa que los datos que ingresaste sean los correctos!, si olvidaste el password, haz click en <a href='#' onclick='abrirRecover()'>Olvido su clave?</a>.<br>"
		 * + "Tiene 2 intentos m�s, antes que su cuenta sea bloqueada."); else if
		 * (intentos == 2) result.setMsg(
		 * "�Revisa que los datos que ingresaste sean los correctos!, si olvidaste el password, haz click en <a href='#' onclick='abrirRecover()'>Olvido su clave?</a>.<br>"
		 * + "Tiene 1 intento m�s, antes que su cuenta sea bloqueada."); else
		 * result.setMsg(
		 * "�Su cuenta est� bloqueada!, por superar el l�mite de intentos de ingresar");
		 * } else { result.setMsg("Usuario, Clave o Tipo de usuario equivocado"); }
		 * 
		 * if (intentos > 2 && "A".equals(usuario.getEst())) {
		 * familiarDAO.actualizarEstado(log_login.getId_usr(), "I"); } } else
		 * result.setMsg("Usuario bloqueado");
		 * 
		 * result.setCode("204"); // result.setMsg("Usuario y/o clave no existe"); }
		 * 
		 * } } else { result.setMsg("Debe llenar los campos de usuario y contrase�a!!");
		 * result.setCode("204"); }
		 */
		return result;

	}

	@RequestMapping(value = "/login_old", method = RequestMethod.POST)
	public AjaxResponseBody getSearchResultViaAjax(Usuario usuario, HttpServletRequest request) {

		String ip = request.getRemoteAddr();

		logger.debug("ip:hora" + ip + "-" + new Date() + "-" + usuario.getLogin());

		AjaxResponseBody result = new AjaxResponseBody();

		if (usuario.getLogin() != "" && usuario.getPassword() != "") {
			if (usuario.getId_per() == EnumPerfil.PERFIL_TRABAJADOR.getValue()
					|| usuario.getId_per() == EnumPerfil.PERFIL_EXTERNO.getValue()) {

				Param param = new Param();
				param.put("usr.login", usuario.getLogin());
				param.put("usr.password", usuario.getPassword());
				// param.put("usr.est", "A");

				List<Usuario> usuarios = usuarioDAO.listFullByParams(param, null);

				if (usuarios.size() > 0 && usuarios.get(0).getEst().equals("A")) {

					// TOKEN
					String token = null;
					try {
						UsuarioSeg usuarioSeg = new UsuarioSeg();
						usuarioSeg.setId_per(usuario.getId_per());
						usuarioSeg.setLogin(usuario.getLogin());
						usuarioSeg.setPassword(usuarios.get(0).getPassword());
						usuarioSeg.setId(usuarios.get(0).getId());
						String nombres = usuarios.get(0).getTrabajador().getApe_pat() + " "
								+ usuarios.get(0).getTrabajador().getApe_mat() + ", "
								+ usuarios.get(0).getTrabajador().getNom();
						usuarioSeg.setNombres(nombres.toUpperCase());
						usuarioSeg.setId_suc(usuarios.get(0).getId_suc());
						usuarioSeg.setId_tra(usuarios.get(0).getTrabajador().getId());
						usuarioSeg.setIni(usuarios.get(0).getIni());
						param = new Param();
						param.put("id_usr", usuarios.get(0).getId());
						param.put("est", "A");
						List<UsuarioRol> usuarioRoles = usuarioRolDAO.listByParams(param, null);

						Integer roles[] = new Integer[usuarioRoles.size()];
						int i = 0;
						for (UsuarioRol usuarioRol : usuarioRoles) {
							roles[i++] = usuarioRol.getId_rol();
						}
						usuarioSeg.setRoles(roles);

						// GENERA EL TOKEN
						token = seguridadService.authenticateAndSignToken(usuarioSeg);

					} catch (Exception e) {
						result.setCode("401");
						result.setMsg("No se puede generar el token de autenticacion");
						e.printStackTrace();
					}

					// LOG DE EXITO
					LogLogin log_login = new LogLogin();
					log_login.setEst("A");
					log_login.setId_per(usuario.getId_per());
					log_login.setIp(ip);
					log_login.setId_usr(usuarios.get(0).getId());
					log_login.setExito("1");
					logLoginDAO.saveOrUpdate(log_login);

					result.setCode("200");
					Row row = new Row();
					row.put("token", token);
					row.put("usuario", usuarios.get(0));
					result.setResult(row);

					logLoginDAO.resetearIntentosFallidos(usuarios.get(0).getId(), usuario.getId_per());

				} else {

					// LOG DE ERROR
					param = new Param();
					param.put("usr.login", usuario.getLogin());
					// param.put("usr.est", "A");

					usuarios = usuarioDAO.listFullByParams(param, null);

					if (usuarios.size() > 0) {

						// cantidad de intentos
						int intentos = logLoginDAO.cantidadAccesosFallidos(usuarios.get(0).getId(),
								usuario.getId_per());

						if (usuarios.get(0).getEst().equals("A")) {
							if (intentos == 0)
								result.setMsg(
										"Revisa que los datos que ingresaste sean los correctos!, si olvidaste el password, haz click en <a href='#' onclick='abrirRecover()'>Olvido su clave?</a>.<br>"
												+ "Tiene 2 intentos m�s, antes que su cuenta sea bloqueada.");
							else if (intentos == 1)
								result.setMsg(
										"Revisa que los datos que ingresaste sean los correctos!, si olvidaste el password, haz click en <a href='#' onclick='abrirRecover()'>Olvido su clave?</a>.<br>"
												+ "Tiene 1 intento m�s, antes que su cuenta sea bloqueada.");
							else
								result.setMsg(
										"Su cuenta está bloqueada!, por superar el l�mite de intentos de ingresar");
						} else {
							result.setMsg("Usuario bloqueado");
						}
						LogLogin log_login = new LogLogin();
						log_login.setEst("A");
						log_login.setId_per(usuario.getId_per());
						log_login.setIp(ip);
						log_login.setId_usr(usuarios.get(0).getId());
						log_login.setExito("0");
						logLoginDAO.saveOrUpdate(log_login);

						if (intentos > 1) {
							usuarioDAO.actualizarEstado(log_login.getId_usr(), "I");
						}

					} else
						result.setMsg("Usuario, Clave o Tipo de usuario equivocado");

					result.setCode("204");

				}

			}

			if (usuario.getId_per() == EnumPerfil.PERFIL_FAMILIAR.getValue()) {
				// if (usuario.getId_per() == 1000) {

				Param param = new Param();
				param.put("nro_doc", usuario.getLogin());
				param.put("pass", usuario.getPassword());
				param.put("est", "A");

				Familiar familiar = familiarDAO.getByParams(param);

				if (familiar != null) {

					// TOKEN
					String token = null;
					try {
						UsuarioSeg usuarioSeg = new UsuarioSeg();
						usuarioSeg.setId_per(usuario.getId_per());
						usuarioSeg.setLogin(usuario.getLogin());
						usuarioSeg.setPassword(familiar.getPass());
						usuarioSeg.setId(familiar.getId());
						usuarioSeg.setIni(familiar.getIni());
						String nombres = familiar.getApe_mat() + " " + familiar.getApe_pat() + ", " + familiar.getNom();
						usuarioSeg.setNombres(nombres.toUpperCase());
						// usuarioSeg.setId_suc(usuarios.get(0).getId_suc());

						token = seguridadService.authenticateAndSignToken(usuarioSeg);

						// Consulto si esta dentro del cronograma
						// Boolean existe = familiarDAO.existeCronograma(familiar.getId());
						Date fecha_actual = new Date();

						/*
						 * if(!existe){ result.
						 * setMsg("Usted no se encuentra en el horario programado para su inscripci�n. <a href='http://ae.edu.pe:8080/documentos/Cronograma_Matricula2020.pdf' target='_blank'>Verifique cronograma</a>"
						 * ); result.setCode("500"); return result; }
						 */
					} catch (Exception e) {
						result.setCode("401");
						result.setMsg("No se puede generar el token de autenticacion");
						e.printStackTrace();
					}

					LogLogin log_login = new LogLogin();
					log_login.setEst("A");
					log_login.setId_per(usuario.getId_per());
					log_login.setIp(ip);
					log_login.setId_usr(familiar.getId());
					log_login.setExito("1");
					logLoginDAO.saveOrUpdate(log_login);

					result.setCode("200");
					usuario.setPassword("");
					usuario.setId(familiar.getId());
					usuario.setId_per(EnumPerfil.PERFIL_FAMILIAR.getValue());
					usuario.setIni(familiar.getIni());
					httpSession.setAttribute("_USUARIO", usuario);
					httpSession.setAttribute("_FAMILIAR", familiar);

					logLoginDAO.resetearIntentosFallidos(familiar.getId(), usuario.getId_per());

					Row row = new Row();
					row.put("token", token);
					row.put("usuario", usuario);
					result.setResult(row);

					// result.setResult(usuario);
				} else {

					// DESACTIVAR USUARIO
					param = new Param();
					param.put("nro_doc", usuario.getLogin());
					param.put("est", "A");

					familiar = familiarDAO.getByParams(param);

					if (familiar != null) {
						LogLogin log_login = new LogLogin();
						log_login.setEst("A");
						log_login.setId_per(usuario.getId_per());
						log_login.setIp(ip);
						log_login.setId_usr(familiar.getId());
						log_login.setExito("0");
						logLoginDAO.saveOrUpdate(log_login);

						// cantidad de intentos
						// cantidad de intentos
						int intentos = logLoginDAO.cantidadAccesosFallidos(familiar.getId(), usuario.getId_per());

						if (familiar.getEst().equals("A")) {
							if (intentos == 1)
								result.setMsg(
										"�Revisa que los datos que ingresaste sean los correctos!, si olvidaste el password, haz click en <a href='#' onclick='abrirRecover()'>Olvido su clave?</a>.<br>"
												+ "Tiene 2 intentos m�s, antes que su cuenta sea bloqueada.");
							else if (intentos == 2)
								result.setMsg(
										"�Revisa que los datos que ingresaste sean los correctos!, si olvidaste el password, haz click en <a href='#' onclick='abrirRecover()'>Olvido su clave?</a>.<br>"
												+ "Tiene 1 intento m�s, antes que su cuenta sea bloqueada.");
							else
								result.setMsg(
										"�Su cuenta est� bloqueada!, por superar el l�mite de intentos de ingresar");
						} else {
							result.setMsg("Usuario, Clave o Tipo de usuario equivocado");
						}

						if (intentos > 2 && "A".equals(usuario.getEst())) {
							familiarDAO.actualizarEstado(log_login.getId_usr(), "I");
						}
					} else
						result.setMsg("Usuario bloqueado");

					result.setCode("204");
					// result.setMsg("Usuario y/o clave no existe");
				}

			}
		} else {
			result.setMsg("Debe llenar los campos de usuario y contrase�a!!");
			result.setCode("204");
		}
		return result;

	}

	@RequestMapping(value = "/loginToken", method = RequestMethod.POST)
	public AjaxResponseBody getSearchResultViaAjax(String token, String password, String password2) {

		AjaxResponseBody result = new AjaxResponseBody();

		if (!password.equals(password2)) {
			result.setCode("500");
			result.setMsg("Las claves deben coincidir");
			return result;
		}

		UsuarioToken usuarioToken = usuarioTokenDAO.getByToken(token, new Date());// SOLO
																					// DE
																					// LA
																					// FECHA
																					// QUE
																					// FUE
																					// GENERADO
																					// EL
																					// TOKEN.

		if (usuarioToken == null) {
			result.setCode("500");
			result.setMsg("El codigo de seguridad expir� o no existe");
			return result;
		}

		// ACTUALIZAR NUEVO PASSWORD
		if (usuarioToken.getId_per().equals(EnumPerfil.PERFIL_TRABAJADOR.getValue())
				|| usuarioToken.getId_per().equals(EnumPerfil.PERFIL_EXTERNO.getValue())) {
			Usuario usuario = usuarioDAO.get(usuarioToken.getId_usr());
			//Usuario usuario = usuarioDAO.getByParams(new Param("login",login));

			if (usuario.getLogin().equals(password)) {
				result.setCode("500");
				result.setMsg("La clave debe ser diferente al codigo de usuario");
				return result;
			}

			usuario.setPassword(password);
			usuarioDAO.saveOrUpdate(usuario);

			result.setCode("200");
			result.setMsg("");
			result.setResult(usuario);
			Param param = new Param();
			param.put("id_usr", usuario.getId());
			param.put("est", "A");
			List<UsuarioRol> usuarioRol = usuarioRolDAO.listByParams(param, null);
			Usuario usuario_sesion = usuario;
			usuario_sesion.setUsuariorols(usuarioRol);

			param = new Param();
			param.put("id", usuario.getId_tra());
			//Usuario tiene al trabajador
			Trabajador trabajador = trabajadorDAO.getByParams(new Param("id",usuario.getId_tra()));
			//Trabajador trabajador = trabajadorDAO.getByParams(param);

			httpSession.setAttribute("_TRABAJADOR", trabajador);
			httpSession.setAttribute("_USUARIO", usuario_sesion);
			httpSession.setAttribute("_ID_SUC", usuario_sesion.getId_suc());
		}

		if (usuarioToken.getId_per().equals(EnumPerfil.PERFIL_FAMILIAR.getValue())) {
			//Usuario usuario = usuarioDAO.getByParams(new Param("login",login));
			Usuario usuario = usuarioDAO.get(usuarioToken.getId_usr());
			if (usuario.getLogin().equals(password)) {
				result.setCode("500");
				result.setMsg("La clave debe ser diferente al codigo de usuario");
				return result;
			}

			usuario.setPassword(password);
			usuario.setId_suc(null);
			usuarioDAO.saveOrUpdate(usuario);
			/*Familiar familiar = familiarDAO.get(usuarioToken.getId_fam());
			familiar.setPass(password);
			familiarDAO.saveOrUpdate(familiar);*/
			//String login = usuarioToken.get

			/*Usuario usuario = new Usuario();
			usuario.setId(familiar.getId());
			usuario.setId_per(EnumPerfil.PERFIL_FAMILIAR.getValue());
			usuario.setIni(familiar.getIni());*/
			GruFam gruFam = gruFamDAO.getByParams(new Param("id_usr",usuario.getId()));
			httpSession.setAttribute("_USUARIO", usuario);
			httpSession.setAttribute("_FAMILIAR", gruFam);

			result.setCode("200");
			result.setMsg("");
			result.setResult(usuario);
			Param param = new Param();
			param.put("id_usr", usuario.getId());
			param.put("est", "A");

		}

		return result;

	}
	
	@RequestMapping(value = "/loginToken_old", method = RequestMethod.POST)
	public AjaxResponseBody getSearchResultViaAjaxOld(String token, String password, String password2) {

		AjaxResponseBody result = new AjaxResponseBody();

		if (!password.equals(password2)) {
			result.setCode("500");
			result.setMsg("Las claves deben coincidir");
			return result;
		}

		UsuarioToken usuarioToken = usuarioTokenDAO.getByToken(token, new Date());// SOLO
																					// DE
																					// LA
																					// FECHA
																					// QUE
																					// FUE
																					// GENERADO
																					// EL
																					// TOKEN.

		if (usuarioToken == null) {
			result.setCode("500");
			result.setMsg("El codigo de seguridad expir� o no existe");
			return result;
		}

		// ACTUALIZAR NUEVO PASSWORD
		if (usuarioToken.getId_per().equals(EnumPerfil.PERFIL_TRABAJADOR.getValue())
				|| usuarioToken.getId_per().equals(EnumPerfil.PERFIL_EXTERNO.getValue())) {
			Usuario usuario = usuarioDAO.get(usuarioToken.getId_usr());

			if (usuario.getLogin().equals(password)) {
				result.setCode("500");
				result.setMsg("La clave debe ser diferente al codigo de usuario");
				return result;
			}

			usuario.setPassword(password);
			usuarioDAO.saveOrUpdate(usuario);

			result.setCode("200");
			result.setMsg("");
			result.setResult(usuario);
			Param param = new Param();
			param.put("id_usr", usuario.getId());
			param.put("est", "A");
			List<UsuarioRol> usuarioRol = usuarioRolDAO.listByParams(param, null);
			Usuario usuario_sesion = usuario;
			usuario_sesion.setUsuariorols(usuarioRol);

			param = new Param();
			param.put("id", usuario.getId_tra());
			Trabajador trabajador = trabajadorDAO.getByParams(param);

			httpSession.setAttribute("_TRABAJADOR", trabajador);
			httpSession.setAttribute("_USUARIO", usuario_sesion);
			httpSession.setAttribute("_ID_SUC", usuario_sesion.getId_suc());
		}

		if (usuarioToken.getId_per().equals(EnumPerfil.PERFIL_FAMILIAR.getValue())) {
			Familiar familiar = familiarDAO.get(usuarioToken.getId_fam());
			familiar.setPass(password);
			familiarDAO.saveOrUpdate(familiar);

			Usuario usuario = new Usuario();
			usuario.setId(familiar.getId());
			usuario.setId_per(EnumPerfil.PERFIL_FAMILIAR.getValue());
			usuario.setIni(familiar.getIni());
			httpSession.setAttribute("_USUARIO", usuario);
			httpSession.setAttribute("_FAMILIAR", familiar);

			result.setCode("200");
			result.setMsg("");
			result.setResult(usuario);
			Param param = new Param();
			param.put("id_usr", usuario.getId());
			param.put("est", "A");

		}

		return result;

	}

	// @JsonView(Views.Public.class)
	@RequestMapping(value = "/usuario/session")
	public AjaxResponseBody getUsuario(HttpServletRequest request) throws Exception {

		String usuarioProperties = env.getProperty("usuario");
		String ambienteProperties = env.getProperty("ambiente");
		// String perfil = env.getProperty("perfil");
		String path = request.getServletContext().getRealPath("/");
		AjaxResponseBody result = new AjaxResponseBody();

		UsuarioSeg usuarioOBJ = tokenStrategy.getUsuarioSeg();
		// if (usuarioOBJ != null && ((Usuario) usuarioOBJ).getId_per() !=null)
		// {
		if (usuarioOBJ != null) {
			Map<String, Object> map = new HashMap<String, Object>();

			if (usuarioOBJ.getId_per() == EnumPerfil.PERFIL_TRABAJADOR.getValue() || usuarioOBJ.getId_per() == EnumPerfil.PERFIL_EXTERNO.getValue()) {
				Param param = new Param();
				param.put("id", usuarioOBJ.getId_tra());

				Trabajador trabajador = trabajadorDAO.getByParams(param);
				map.put("usuario", usuarioOBJ);
				map.put("trabajador", trabajador);
			} else if (usuarioOBJ.getId_per() == EnumPerfil.PERFIL_FAMILIAR.getValue()) {

				Param param = new Param();
				param.put("id_usr", usuarioOBJ.getId());
				//Antes era Familiar , ahora es grupo familiar
				//Familiar familiar = familiarDAO.getByParams(param);
				GruFam gruFam = gruFamDAO.getByParams(new Param("id_usr",usuarioOBJ.getId()));
				//familiar.setFoto(null);// se demoraba mucho
				//familiar.setHuella(null);// se demoraba mucho

				map.put("usuario", usuarioOBJ);
				//map.put("familiar", familiar);
				map.put("familiar", gruFam);
			}

			Claims claim = tokenStrategy.validateToken(request);
			Date fecha_expiracion = claim.getExpiration();

			System.out.println(fecha_expiracion);

			Integer segundos = FechaUtil.diffSegundos(fecha_expiracion, new Date());
			map.put("segundos_to_expire", segundos);

			result.setResult(map);

		} else {

			if (path.substring(1, 2).equals(":") && // si es ruta windows
					ambienteProperties != null && "D".equals(ambienteProperties) && usuarioProperties != null
					&& !"".equals(usuarioProperties)) {
				Param param = new Param();
				param.put("login", usuarioProperties);
				Usuario usuario = usuarioDAO.getByParams(param);
				httpSession.setAttribute("_USUARIO", usuario);
				httpSession.setAttribute("_ID_SUC", usuario.getId_suc());

				param = new Param();
				param.put("id", usuario.getId_tra());
				Trabajador trabajador = trabajadorDAO.getByParams(param);
				Usuario usuarioSession = new Usuario();

				param = new Param();
				param.put("id_usr", usuario.getId());
				param.put("est", "A");
				List<UsuarioRol> usuariorols = usuarioRolDAO.listByParams(param, null);
				usuario.setUsuariorols(usuariorols);

				httpSession.setAttribute("_TRABAJADOR", trabajador);

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("trabajador", trabajador);
				result.setResult(map);

			} else {
				result.setCode("401");
				result.setMsg("Se perdi� sesi�n por inactividad");
			}
		}
		return result;

	}

	@JsonView(Views.Public.class)
	@RequestMapping(value = "/usuario/logout")
	public AjaxResponseBody logout() {
		AjaxResponseBody result = new AjaxResponseBody();

		httpSession.removeAttribute("_USUARIO");
		result.setCode("200");
		result.setMsg("");
		result.setResult("");
		return result;
	}

	@RequestMapping(value = "/change", method = RequestMethod.POST)
	public AjaxResponseBody cambioClave(Integer id_per, Integer id, String password1, String password2) {

		AjaxResponseBody result = new AjaxResponseBody();

		if (password1 == null || password1.trim().equals("")) {

			result.setCode("500");
			result.setMsg("Por favor ingresar la clave");
			return result;
		}

		if (!password1.equals(password2)) {

			result.setCode("500");
			result.setMsg("Las claves deben coincidir");
			return result;
		}

		if (id_per == EnumPerfil.PERFIL_TRABAJADOR.getValue()) {

			Usuario usuario = usuarioDAO.get(id);

			if (usuario.getPassword().equals(password1)) {
				result.setCode("500");
				result.setMsg("La nueva clave debe ser diferente a la anterior");
				return result;
			} else if (usuario.getLogin().equals(password1)) {
				result.setCode("500");
				result.setMsg("La nueva clave debe ser diferente al codigo de usuario");
				return result;
			} else {
				usuario.setPassword(password1);
				if ("2".equals(usuario.getIni()))
					usuario.setIni("3");// todo confirmado
				usuarioDAO.saveOrUpdate(usuario);
			}

			result.setCode("200");
		}

		if (id_per == EnumPerfil.PERFIL_FAMILIAR.getValue()) {

			Familiar familiar = familiarDAO.get(id);

			if (familiar.getPass().equals(password1)) {
				result.setCode("500");
				result.setMsg("La nueva clave debe ser diferente a la anterior");
			} else {
				familiar.setPass(password1);
				// int ini = Integer.parseInt(familiar.getIni());

				if ("2".equals(familiar.getIni()) || "4".equals(familiar.getIni()))
					familiarDAO.updatePasswordIni(id, password1, new Date(), -1);
				else
					familiarDAO.updatePassword(id, password1, new Date(), -1);

				result.setCode("200");
			}

		}

		return result;

	}

	@RequestMapping(value = "/recover_old", method = RequestMethod.POST)
	public AjaxResponseBody envioInstruccionesCorreo(String correo, Integer id_per, HttpServletRequest request) {

		AjaxResponseBody result = new AjaxResponseBody();

		Param param = new Param();

		Integer id = null;
		String ape_pat = null;
		String nom = null;

		// Datos de la empresa
		Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);

		if (EnumPerfil.PERFIL_TRABAJADOR.getValue() == id_per) {
			param.put("corr", correo);
			Trabajador trabajador = trabajadorDAO.getByParams(param);
			if (trabajador == null) {
				result.setCode("500");
				result.setMsg(
						"El usuario no tiene un correo asignado, por favor coordinar con el administrador del sistema");
				return result;
			}

			param = new Param();
			param.put("id_tra", trabajador.getId());
			Usuario usuario = usuarioDAO.getByParams(param);

			id = usuario.getId();
		}

		if (EnumPerfil.PERFIL_FAMILIAR.getValue() == id_per) {
			param.put("corr", correo);
			Familiar familiar = familiarDAO.getByParams(param);
			if (familiar == null) {
				result.setCode("500");
				result.setMsg(
						"El usuario no tiene un correo asignado, por favor coordinar con el administrador del sistema");
				return result;
			}

			id = familiar.getId();

			ape_pat = familiar.getApe_pat();
			nom = familiar.getNom();

		}

		try {

			String token = StringUtil.randomString(100);

			String contexto = request.getContextPath().replace("-api", "").replace("-", "_"); // /sige-api-desa

			UsuarioToken usuarioToken = new UsuarioToken();
			usuarioToken.setEst("A");
			usuarioToken.setFecha(new Date());
			usuarioToken.setToken(token);
			if (EnumPerfil.PERFIL_FAMILIAR.getValue() == id_per)
				usuarioToken.setId_fam(id);
			if (EnumPerfil.PERFIL_TRABAJADOR.getValue() == id_per)
				usuarioToken.setId_usr(id);

			usuarioToken.setId_per(id_per);
			usuarioTokenDAO.saveOrUpdate(usuarioToken);

			CorreoUtil correoUtil = new CorreoUtil();
			String host = request.getHeader("host");
			String html = "Sr(a)";
			html += "<br>" + nom + " " + ape_pat;
			html += "<br><br>Ingrese al siguiente link para resetear su clave:";
			html += "<br><a href='http://" + host + contexto + "/reset.html?token=" + token + "'>Resetar clave</a>";

			html += "<br><br>Att";
			html += "<br>Asociacion Educativa Luz y Ciencia";

			/*correoUtil.enviar(empresa.getString("giro_negocio") + " - Recuperaci�n de clave",
					"michael.valle77@gmail.com", correo, html, null, empresa.getString("corr"),
					empresa.getString("giro_negocio"));*/

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setCode("500");
			result.setMsg("No se pudo enviar el correo:" + e.getMessage());
		}

		result.setCode("200");

		return result;

	}
	
	@RequestMapping(value = "/recover", method = RequestMethod.POST)
	//public AjaxResponseBody envioInstruccionesCorreo2(String dni,String correo, Integer id_per, HttpServletRequest request) {
	public AjaxResponseBody envioInstruccionesCorreo2(String cuenta, String dni, HttpServletRequest request) {

		AjaxResponseBody result = new AjaxResponseBody();

		//Param param = new Param();

		Integer id = null;
		String ape_pat = null;
		String nom = null;
		String correo = null;

		// Datos de la empresa
		Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);
		Param param = new Param();
		param.put("login", cuenta);
		param.put("est", "A");

		Usuario usuario = usuarioDAO.getByParams(param);
		if(usuario==null) {
			result.setCode("500");
			result.setMsg(
					"No existe el usuario digitado, por favor revisar su cuenta");
			return result;
		}	
		//Persona persona = personaDAO.getByParams(new Param("nro_doc", dni));

		if (EnumPerfil.PERFIL_TRABAJADOR.getValue() == usuario.getId_per()) {
			Trabajador trabajador = trabajadorDAO.getByParams(new Param("idr",usuario.getId_tra()));
			//Usuario usuario_datos = usuarioDAO.getByParams(new Param("id_tra", trabajador.getId()));
			Integer id_fam=null;
			

			//id = usuario.getId();
		}

		if (EnumPerfil.PERFIL_FAMILIAR.getValue() == usuario.getId_per()) {
			//Usuario usuario_datos = usuarioDAO.getByParams(new Param("login", usuario));
			//Integer id_fam=null;
			GruFam gruFam = gruFamDAO.getByParams(new Param("id_usr",usuario.getId()));
			/*if(persona==null) {
				result.setCode("500");
				result.setMsg(
						"No existe el usuario digitado, por favor revisar su cuenta");
				return result;
			} else {
				/*GruFam gruFam = gruFamDAO.getByParams(new Param("id_usr",usuario_datos.getId()));
				List<Row> matriculas=alumnoDao.listarMatriculadosxFamilia(gruFam.getId(), 6);
				if(matriculas.size()>0) {
					id_fam = matriculas.get(0).getInteger("id_fam");
				}
			}*/
			//Obtener el correo del responsable de matricula
			//Familiar familiar = familiarDAO.getByParams(new Param("id_per",persona.getId()));
			Persona persona = personaDAO.getByParams(new Param("nro_doc", dni));
			if(persona==null) {
				result.setCode("500");
				result.setMsg(
						"El DNI digitado, no se encuentrado registrado en nuestro sistema.");
				return result;
			} else {
				correo = persona.getCorr();
				if(correo==null) {
					result.setCode("500");
					result.setMsg(
							"El DNI digitado,no cuenta con un correo registrado.");
					return result;
				} else {
					id = gruFam.getId();
					ape_pat = persona.getApe_pat();
					nom = persona.getNom();
				}	
				
			}	
		}

		try {

			String token = StringUtil.randomString(100);

			String contexto = request.getContextPath().replace("-api", "").replace("-", "_"); // /sige-api-desa
			//String contexto = request.getContextPath().replace("-api", "").replace("-", "_"); // /sige-api-desa

			UsuarioToken usuarioToken = new UsuarioToken();
			usuarioToken.setEst("A");
			usuarioToken.setFecha(new Date());
			usuarioToken.setToken(token);
			if (EnumPerfil.PERFIL_FAMILIAR.getValue() == usuario.getId_per())
				usuarioToken.setId_fam(id);
			if (EnumPerfil.PERFIL_TRABAJADOR.getValue() == usuario.getId_per())
				usuarioToken.setId_usr(id);

			usuarioToken.setId_per(usuario.getId_per());
			usuarioToken.setId_usr(usuario.getId());
			usuarioTokenDAO.saveOrUpdate(usuarioToken);

			CorreoUtil correoUtil = new CorreoUtil();
			String host = request.getHeader("host");
			String html = "Sr(a)";
			html += "<br>" + nom + " " + ape_pat;
			html += "<br><br>Ingrese al siguiente link para resetear su clave:";
			//html += "<br><a href='http://" + host + contexto + "/reset.html?token=" + token + "'>Resetar clave</a>";
			//html += "<br><a href='http://localhost:8080/sige-web/reset.html?token=" + token + "'>Resetar clave</a>";
			html += "<br><a href='http://login.ae.edu.pe:8080/sige/reset.html?token=" + token + "'>Cambiar clave</a>";

			html += "<br><br>Att";
			html += "<br>Asociacion Educativa Luz y Ciencia";
			
			//correoUtil.enviar(empresa.getString("giro_negocio") + " - Recuperación de clave", "	noreply@ae.edu.pe", correo, html, null, null, empresa.getString("corr"), empresa.getString("giro_negocio"));
			Contador contador = contadorDAO.get(1);
			Integer cant_msj_env=contador.getNro();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			String format = formatter.format(new Date());
			String format2 = formatter.format(contador.getFec());
			int fecActual = Integer.parseInt(format);
			int fecContador=Integer.parseInt(format2);
			if(cant_msj_env<=500 && fecActual==fecContador) {
				correoUtil.enviar(empresa.getString("giro_negocio") + " - Recuperación de clave", "",correo,html,null,"noreply@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				/*MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
				mensajeriaFamiliar.setId_des(familiar.getId());
				mensajeriaFamiliar.setId_per(8);
				mensajeriaFamiliar.setMsj("Confirmacion de Documentos correctos " + persona.getNom() +" " +persona.getApe_pat()+" "+persona.getApe_pat());
				mensajeriaFamiliar.setEst("A");
				mensajeriaFamiliar.setFlg_en("1");
				mensajeriaFamiliar.setId_alu(null);
				mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
				mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);*/
				//actualizo el contador
				cant_msj_env = cant_msj_env + 1;
				//si es 500 cambio el usuario 
				if(cant_msj_env.equals(500)) {
					//actualizo
					contadorDAO.actualizarUsuarioContadorSinTK("noreply@ae.edu.pe");
				}
				contadorDAO.actualizarContadorSinTK(cant_msj_env, contador.getFec());
			} else if((cant_msj_env>500 && cant_msj_env<=1000) && fecActual==fecContador){ // matricula.getString("corr")
				correoUtil.enviar(empresa.getString("giro_negocio") + " - Recuperación de clave", "",correo,html,null,"noreply@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				/*MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
				mensajeriaFamiliar.setId_des(familiar.getId());
				mensajeriaFamiliar.setId_per(8);
				mensajeriaFamiliar.setMsj("Confirmacion de Documentos correctos " + persona.getNom() +" " +persona.getApe_pat()+" "+persona.getApe_pat());
				mensajeriaFamiliar.setEst("A");
				mensajeriaFamiliar.setFlg_en("1");
				mensajeriaFamiliar.setId_alu(null);
				mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
				mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);*/
				//actualizo el contador
				cant_msj_env = cant_msj_env + 1;
				//si es 500 cambio el usuario 
				if(cant_msj_env.equals(1000)) {
					//actualizo
					contadorDAO.actualizarUsuarioContadorSinTK("noreply2@ae.edu.pe");
				}
				contadorDAO.actualizarContadorSinTK(cant_msj_env, contador.getFec());
			} else if((cant_msj_env>1000 && cant_msj_env<=1500) && fecActual==fecContador){ // matricula.getString("corr")
				correoUtil.enviar(empresa.getString("giro_negocio") + " - Recuperación de clave", "",correo,html,null,"noreply@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				/*MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
				mensajeriaFamiliar.setId_des(familiar.getId());
				mensajeriaFamiliar.setId_per(8);
				mensajeriaFamiliar.setMsj("Confirmacion de Documentos correctos " + persona.getNom() +" " +persona.getApe_pat()+" "+persona.getApe_pat());
				mensajeriaFamiliar.setEst("A");
				mensajeriaFamiliar.setFlg_en("1");
				mensajeriaFamiliar.setId_alu(null);
				mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
				mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);*/
				//actualizo el contador
				cant_msj_env = cant_msj_env + 1;
				//si es 500 cambio el usuario 
				if(cant_msj_env.equals(1500)){
					//actualizo
					contadorDAO.actualizarUsuarioContadorSinTK("noreply3@ae.edu.pe");
				}
				contadorDAO.actualizarContadorSinTK(cant_msj_env, contador.getFec());
			} else if((cant_msj_env>1500 && cant_msj_env<=2000) && fecActual==fecContador){ // matricula.getString("corr")
				correoUtil.enviar(empresa.getString("giro_negocio") + " - Recuperación de clave", "",correo,html,null,"noreply@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				/*MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
				mensajeriaFamiliar.setId_des(familiar.getId());
				mensajeriaFamiliar.setId_per(8);
				mensajeriaFamiliar.setMsj("Confirmacion de Documentos correctos " + persona.getNom() +" " +persona.getApe_pat()+" "+persona.getApe_pat());
				mensajeriaFamiliar.setEst("A");
				mensajeriaFamiliar.setFlg_en("1");
				mensajeriaFamiliar.setId_alu(null);
				mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
				mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);*/
				//actualizo el contador
				cant_msj_env = cant_msj_env + 1;
				//si es 500 cambio el usuario 
				if(cant_msj_env.equals(2000)){
					//actualizo
					contadorDAO.actualizarUsuarioContadorSinTK("noreply4@ae.edu.pe");
				}
				contadorDAO.actualizarContadorSinTK(cant_msj_env, contador.getFec());
			} else if((cant_msj_env>2000) && fecActual==fecContador){
				throw new Exception("Se ha excedido el envío de mensajes, por favor comuníquese con el administrador.");
			} else if(fecActual!=fecContador){
				//Actualizo la fecha del contador, Nuevo dia
				contadorDAO.actualizarFechaContadorSinTK(new Date());
				contadorDAO.actualizarUsuarioContadorSinTK("noreply@ae.edu.pe");
				Contador contador2= contadorDAO.get(1);
				correoUtil.enviar(empresa.getString("giro_negocio") + " - Recuperación de clave", "",correo,html,null,"noreply@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				/*MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
				mensajeriaFamiliar.setId_des(familiar.getId());
				mensajeriaFamiliar.setId_per(8);
				mensajeriaFamiliar.setMsj("Confirmacion de Documentos correctos " + persona.getNom() +" " +persona.getApe_pat()+" "+persona.getApe_pat());
				mensajeriaFamiliar.setEst("A");
				mensajeriaFamiliar.setFlg_en("1");
				mensajeriaFamiliar.setId_alu(null);
				mensajeriaFamiliar.setUsr_rmt(contador2.getUsr());
				mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);*/
				//actualizo el contador
				cant_msj_env = cant_msj_env + 1;
				//si es 500 cambio el usuario 
				contadorDAO.actualizarContadorSinTK(cant_msj_env, contador2.getFec());
			}
			
			result.setMsg("Se envió el link de recuperación al siguiente correo "+correo.substring(0,3)+"************ , el cual ha registrado en nuestro sistema");
		
			/*correoUtil.enviar(empresa.getString("giro_negocio") + " - Recuperación de clave",
					"michael.valle77@gmail.com", correo, html, null, empresa.getString("corr"),
					empresa.getString("giro_negocio"));*/

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setCode("500");
			result.setMsg("No se pudo enviar el correo:" + e.getMessage());
		}

		result.setCode("200");

		return result;

	}

	@RequestMapping(value = "/validarCorreo", method = RequestMethod.POST)
	public AjaxResponseBody validarCorreo(Integer id_per, Integer id, String corr, HttpServletRequest request) {

		AjaxResponseBody result = new AjaxResponseBody();

		try {
			Familiar familiar = familiarDAO.get(id);

			String host = request.getHeader("host");

			String contexto = request.getContextPath().replace("-api", "").replace("-", "_"); // /sige-api-desa

			familiar.setCorr(corr);
			familiarDAO.saveOrUpdate(familiar);

			// Datos de la empresa
			Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);

			// enviar mensaje de correo con un link de validacion
			CorreoUtil correoUtil = new CorreoUtil();

			String token = StringUtil.randomString(100);

			UsuarioToken usuarioToken = new UsuarioToken();
			usuarioToken.setEst("A");
			usuarioToken.setFecha(new Date());
			usuarioToken.setToken(token);
			if (EnumPerfil.PERFIL_FAMILIAR.getValue() == id_per)
				usuarioToken.setId_fam(id);
			if (EnumPerfil.PERFIL_TRABAJADOR.getValue() == id_per)
				usuarioToken.setId_usr(id);

			usuarioToken.setId_per(id_per);
			usuarioTokenDAO.saveOrUpdate(usuarioToken);

			String url = "<a href='http://" + host + contexto + "/valida_correo.html?token=" + token
					+ "'>���Click aqui!!!</a>";

			String html = "Estimad@:";
			html += "<br><br>" + familiar.getApe_pat() + " " + familiar.getApe_mat() + ", " + familiar.getNom();
			// html += "<br><br>Estimad@.<br>";
			html += "Para confirmar que esta direcci�n de correo es suya, haga click en el link azul:";
			// html += "<br><br><h2><u>LINK DE VERIFICACI�N</u></h2>";
			html += "<h1><font color='blue'>" + url + "</font></h1>";

			html += "Este correo es informativo, favor no responder a esta direcci�n de correo, esta no se encuentra habilitada para recibir emails.";
			html += "<BR>Si requiere mayor informaci�n, contactar con secretaria de Lunes a Viernes de 8:00 a 12:45 y de 14:00 a 17:00 horas, tel�fonos 043-422110 / 043-427705 / 043231421 o al e-mail:<a href='mailto:consultas@ae.edu.pe'>consultas@ae.edu.pe</a>";
			html += "<br><br>Atentamente";
			html += "<br><b>La Direcci�n</b>";

			/*x ahora comentadocorreoUtil.enviar(empresa.getString("giro_negocio") + " - Recuperaci�n de clave", "", familiar.getCorr(),
					html, null, empresa.getString("corr"), empresa.getString("giro_negocio"));*/
			// Map<String,Object> map = new HashMap<String,Object>();
			// map.put("id_msg", id_msg);
			// map.put("clave", clave);

			// result.setResult(id_msj);

		} catch (Exception e) {
			result.setException(e);
		}

		return result;

	}

	@RequestMapping(value = "/validarCorreoToken", method = RequestMethod.POST)
	public AjaxResponseBody validarCorreoToken(String token) {

		UsuarioToken usuarioToken = usuarioTokenDAO.getByToken(token, new Date());// SOLO
		AjaxResponseBody result = new AjaxResponseBody();

		if (usuarioToken == null) {
			result.setCode("500");
			result.setMsg("El c�digo de seguridad expir� o no existe");
			return result;
		}

		// ACTUALIZAR EL CORREO
		if (usuarioToken.getId_per() == EnumPerfil.PERFIL_TRABAJADOR.getValue()) {
			Usuario usuario = usuarioDAO.get(usuarioToken.getId_usr());
			Trabajador trabajador = trabajadorDAO.get(usuario.getId());
			trabajador.setCorr_val("1");
			trabajadorDAO.saveOrUpdate(trabajador);
			result.setResult(trabajador.getCorr());
		}

		if (usuarioToken.getId_per() == EnumPerfil.PERFIL_FAMILIAR.getValue()) {

			Familiar familiar = familiarDAO.get(usuarioToken.getId_fam());
			familiar.setCorr_val("1");
			familiarDAO.saveOrUpdate(familiar);

			result.setResult(familiar.getCorr());
		}

		return result;

	}

	@RequestMapping(value = "/esCorreoValidado", method = RequestMethod.GET)
	public AjaxResponseBody esCorreoValidado(Integer id_fam) {

		AjaxResponseBody result = new AjaxResponseBody();
		result.setResult(familiarDAO.esCorreoValidado(id_fam));
		return result;
	}

	/**
	 * Esta a punto de perder session y el usuario desea continar se creara un nuevo
	 * token
	 * 
	 * @param id_fam
	 * @return
	 */
	@RequestMapping(value = "/reconectar", method = RequestMethod.POST)
	public AjaxResponseBody reconectar(Integer id_fam) throws AuthenticationException {

		AjaxResponseBody result = new AjaxResponseBody();

		UsuarioSeg usuarioSeg = tokenStrategy.getUsuarioSeg();

		String token = tokenStrategy.getJWTToken(usuarioSeg);

		Date dateExpiration = tokenStrategy.getExpirationDateFromToken(token);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("token", token);
		map.put("segundos_to_expire", FechaUtil.diffSegundos(dateExpiration, new Date()));

		result.setResult(map);

		return result;
	}

	// llama cada minuto, preguntando si hay session, para preguntar si desea
	// mantener ( nueva session) o salir
	@RequestMapping(value = "/validarSessionToken", method = RequestMethod.GET)
	public AjaxResponseBody validarSessionToken(HttpServletRequest request) {

		AjaxResponseBody result = new AjaxResponseBody();

		String tokenStr = tokenStrategy.getTokenFromRequest(request);
		Claims claims = tokenStrategy.parseToken(tokenStr);

		Date fechaExpiracion = tokenStrategy.getExpirationDate(claims);
		Date fechaActual = new Date();

		long segundosDiferenciaActual = (fechaExpiracion.getTime() - fechaActual.getTime()) / 1000;
		int segundosDiferenciaConfigurada = Constante.MINUTOS_AVISO_PARA_EXPIRAR * 60 + 10;// 10SEGUNDOS
																							// DE
																							// HOGLURA

		Row row = new Row();
		row.put("segundos", segundosDiferenciaActual);
		if (segundosDiferenciaActual < segundosDiferenciaConfigurada) {
			row.put("estado", "P");
		} else
			row.put("estado", "A");

		row.put("fecha", FechaUtil.toString(fechaExpiracion, "yyyy-MM-dd HH:mm:ss"));

		result.setResult(row);
		return result;
	}

}

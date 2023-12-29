package com.sige.mat.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.sige.common.enums.EnumParentesco;
import com.sige.common.enums.EnumPerfil;
import com.sige.common.enums.EnumValidacionUsuario;
import com.sige.mat.dao.AcademicoPagoDAO;
import com.sige.mat.dao.AlumnoDAO;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.AulaDAO;
import com.sige.mat.dao.BancoDAO;
import com.sige.mat.dao.ContadorDAO;
import com.sige.mat.dao.EmpresaDAO;
import com.sige.mat.dao.EvaluacionVacDAO;
import com.sige.mat.dao.FamiliarDAO;
import com.sige.mat.dao.GruFamAlumnoDAO;
import com.sige.mat.dao.GruFamDAO;
import com.sige.mat.dao.GruFamFamiliarDAO;
import com.sige.mat.dao.HistorialCorreoDAO;
import com.sige.mat.dao.LogLoginDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.MensajeriaFamiliarDAO;
import com.sige.mat.dao.ParentescoDAO;
import com.sige.mat.dao.PerHistorialDAO;
import com.sige.mat.dao.PermisosDAO;
import com.sige.mat.dao.PersonaDAO;
import com.sige.mat.dao.RatificacionDAO;
import com.sige.rest.request.FamiliarReq;
import com.sige.spring.service.SeguridadService;
import com.tesla.colegio.model.AcademicoPago;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Banco;
import com.tesla.colegio.model.Contador;
import com.tesla.colegio.model.Departamento;
import com.tesla.colegio.model.Distrito;
import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.GruFam;
import com.tesla.colegio.model.GruFamAlumno;
import com.tesla.colegio.model.GruFamFamiliar;
import com.tesla.colegio.model.HistorialCorreo;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.MensajeriaFamiliar;
import com.tesla.colegio.model.Parentesco;
import com.tesla.colegio.model.PerHistorial;
import com.tesla.colegio.model.Permisos;
import com.tesla.colegio.model.Persona;
import com.tesla.colegio.model.Provincia;
import com.tesla.colegio.model.Ratificacion;
import com.tesla.colegio.model.Usuario;
import com.tesla.colegio.model.UsuarioSeg;
import com.tesla.colegio.model.bean.Adjunto;
import com.tesla.colegio.util.Constante;
import com.tesla.colegio.util.FileUtil;
import com.tesla.frmk.common.exceptions.ControllerException;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.CodigoBarrasUtil;
import com.tesla.frmk.util.CorreoUtil;
import com.tesla.frmk.util.FechaUtil;
import com.tesla.frmk.util.JsonUtil;
import com.tesla.frmk.util.StringUtil;

@RestController
@RequestMapping(value = "/api/familiar")
public class FamiliarRestController {
	final static Logger logger = Logger.getLogger(FamiliarRestController.class);
	@Autowired
	FamiliarDAO familiarDAO;

	@Autowired
	GruFamFamiliarDAO gruFamFamiliarDAO;

	@Autowired
	LogLoginDAO logLoginDAO;

	@Autowired
	AulaDAO aulaDAO;
	
	@Autowired
	EvaluacionVacDAO evaluacion_vacDAO;
	
	@Autowired
	GruFamAlumnoDAO gruFamAlumnoDAO;
	
	@Autowired
	EmpresaDAO empresaDAO;
	
	@Autowired
	MensajeriaFamiliarDAO mensajeriaFamiliarDAO;
	
	@Autowired
	SeguridadService seguridadService;
	
	@Autowired
	ParentescoDAO parentescoDAO;
	
	@Autowired
	GruFamDAO gru_famDAO;
	
	@Autowired
	PermisosDAO	permisosDAO;
	
	@Autowired
	MatriculaDAO matriculaDAO;
	
	@Autowired
	PersonaDAO personaDAO;
	
	@Autowired
	HistorialCorreoDAO historialCorreoDAO;
	
	@Autowired
	AcademicoPagoDAO academicoPagoDAO;
	
	@Autowired
	BancoDAO bancoDAO;
	
	@Autowired
	AlumnoDAO alumnoDAO;
	
	@Autowired
	ContadorDAO contadorDAO;
	
	@Autowired
	PerHistorialDAO perHistorialDAO;
	
	@Autowired
	RatificacionDAO ratificacionDAO;
	
	@Autowired
	AnioDAO anioDAO;
	
	List<Usuario> usuario;

	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			Familiar familiar = familiarDAO.getFull(id, new String[]{Departamento.TABLA, Provincia.TABLA, Distrito.TABLA}) ;
			map.put("familiar", familiar);
			if( EnumValidacionUsuario.DATOS_ACTUALIZADOS.getValue().equals(familiar.getIni()) || familiar.getIni()==null){
				MensajeriaFamiliar mensajeria = mensajeriaFamiliarDAO.ultimoMensajeEnviado(id) ;
				if(mensajeria!=null)
					map.put("mensajeria", mensajeria.getId());
				else
					map.put("mensajeria", null);
			}else
				map.put("mensajeria", null);

			
			result.setResult(map);
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@Transactional
	@RequestMapping(  value="/actualizarInformacion", method = RequestMethod.POST)
	public AjaxResponseBody grabar(Persona persona, Integer id_fam, Integer id_par,Integer id_gpf, String ocu, String prof, String email_inst, Boolean es_padre_madre, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
			//Validamos al familiar
			String error = validarFamiliarPersona( id_gpf, persona, es_padre_madre, id_par);
			Integer id_per=null;
			Integer id_fam_ins=null;
			if (error==null) {
				//Verifico si el email es diferente
				if(persona.getId()!=null) {
					Persona persona2= personaDAO.get(persona.getId());
					if(persona2!=null) {
						//Busco en el historial si existe del año en curso
						Param param = new Param();
						param.put("id_per", persona.getId());
						param.put("id_anio", id_anio);
						PerHistorial persona_historial= perHistorialDAO.getByParams(param);
						if(persona_historial!=null) {
							if(persona.getCorr()!=null && persona2.getCorr()!=null && persona.getCorr()!="" && persona2.getCorr()!="") {
								if(!persona2.getCorr().toUpperCase().equals(persona.getCorr().toUpperCase())){
									//Actualizamos el historial
									persona_historial.setCorr_antiguo(persona2.getCorr());
									persona_historial.setCorr_actual(persona.getCorr());
									perHistorialDAO.saveOrUpdate(persona_historial);
									
									/*//Insertamos en el historial
									HistorialCorreo historialCorreo= new HistorialCorreo();
									historialCorreo.setId_per(persona.getId());
									historialCorreo.setCorr_antiguo(persona2.getCorr());
									historialCorreo.setCorr_nuevo(persona.getCorr());
									historialCorreo.setEst("A");
									historialCorreoDAO.saveOrUpdate(historialCorreo);*/
								}	
							}
							if(persona.getCel()!=null && persona2.getCel()!=null && persona.getCel()!="" && persona2.getCel()!="") {
								if(!persona2.getCel().toUpperCase().equals(persona.getCel().toUpperCase())){
									//Actualizamos el historial
									persona_historial.setCel_antiguo(persona2.getCel());
									persona_historial.setCel_actual(persona.getCel());
									perHistorialDAO.saveOrUpdate(persona_historial);
								}	
							}
						} else {
							//Insertamos en el historial
							PerHistorial historial= new PerHistorial();
							historial.setId_per(persona.getId());
							historial.setId_anio(id_anio);
							historial.setId_eci(persona.getId_eci());
							historial.setCel_actual(persona.getCel());
							historial.setCorr_actual(persona.getCorr());
							historial.setEst("A");
							perHistorialDAO.saveOrUpdate(historial);
						}
						/*if(persona.getCorr()!=null && persona2.getCorr()!=null && persona.getCorr()!="" && persona2.getCorr()!="") {
							if(!persona2.getCorr().toUpperCase().equals(persona.getCorr().toUpperCase())){
								//Insertamos en el historial
								HistorialCorreo historialCorreo= new HistorialCorreo();
								historialCorreo.setId_per(persona.getId());
								historialCorreo.setCorr_antiguo(persona2.getCorr());
								historialCorreo.setCorr_nuevo(persona.getCorr());
								historialCorreo.setEst("A");
								historialCorreoDAO.saveOrUpdate(historialCorreo);
							}	
						}*/
					}
				}
				
				//Asignamos la direccion del grupo de familia
				GruFam gruFam= gru_famDAO.get(id_gpf);
				Integer id_dist=gruFam.getId_dist();
				String direccion=gruFam.getDireccion();
				persona.setId_dist_viv(id_dist);
				persona.setDir(direccion);
				//Actualizamos los datos de la Persona
				id_per=personaDAO.saveOrUpdate(persona);
				//Verficamos si fue un nuevo registro
				if(persona.getId()==null) {
					//Insertamos en el historial
					PerHistorial historial= new PerHistorial();
					historial.setId_per(id_per);
					historial.setId_anio(id_anio);
					historial.setId_eci(persona.getId_eci());
					historial.setCel_actual(persona.getCel());
					historial.setCorr_actual(persona.getCorr());
					historial.setEst("A");
					perHistorialDAO.saveOrUpdate(historial);
				}
				//Encontramos al familiar
				Familiar familiar= new Familiar();
				if(id_fam!=null) {
					familiar=familiarDAO.getByParams(new Param("id",id_fam));
					Integer id_par_existe=familiar.getId_par();
					if(id_par.equals(id_par_existe)) {
						familiar.setId_par(id_par);
					} else {
						//verifico si ya esta asignado como papa o mama
						if(id_par.equals(2) || id_par.equals(1)) {
							Row familiares = familiarDAO.datosFamiliarxParentesco(persona.getId(), 2);
							Row familiares1 = familiarDAO.datosFamiliarxParentesco(persona.getId(), 1);
							//Existe papa
							
							if(familiares!=null) {
								Integer id_fami=familiares.getInteger("id_fam");
								//Verifico si existe como integrante de la familia
								//Busco si el familiar ya existe como integrante del grupo familiar
								Param param = new Param();
								param.put("id_gpf", id_gpf);
								param.put("id_fam", id_fam);
								GruFamFamiliar existe_integrante = gruFamFamiliarDAO.getByParams(param); //Tiene ya el grupo familiar
								if(existe_integrante==null) {
									throw new ControllerException("Ya existe un padre o madre con el DNI "+persona.getNro_doc());
								} else {
									if(!id_par.equals(2)) {
										throw new ControllerException("Ya existe un padre o madre con el DNI "+persona.getNro_doc());
									}
								}
							}
							
							//Existe MAMA
							if(familiares1!=null) {
								//Verifico si existe como integrante de la familia
								//Busco si el familiar ya existe como integrante del grupo familiar
								Param param = new Param();
								param.put("id_gpf", id_gpf);
								param.put("id_fam", id_fam);
								GruFamFamiliar existe_integrante = gruFamFamiliarDAO.getByParams(param); //Tiene ya el grupo familiar
								if(existe_integrante==null) {
									throw new ControllerException("Ya existe un padre o madre con el DNI "+persona.getNro_doc());
								} else {
									if(!id_par.equals(2)) {
										throw new ControllerException("Ya existe un padre o madre con el DNI "+persona.getNro_doc());
									}
								}
							}
							
							//if(familiares!=null || familiares1!=null)
							//throw new ControllerException("Ya existe un padre o madre con el DNI "+persona.getNro_doc());
						}
						//Busco si el familiar ya existe como integrante del grupo familiar
						Param param = new Param();
						param.put("id_gpf", id_gpf);
						param.put("id_fam", id_fam);
						GruFamFamiliar existe_integrante = gruFamFamiliarDAO.getByParams(param); //Tiene ya el grupo familiar
						if(existe_integrante==null) {
							familiar.setId(null);
						} else {
							//valido si exoiste con el parentesco nuevo en la familia
							Row familiar_nuevo = familiarDAO.datosFamiliarxParentesco(persona.getId(), id_par);
							if(familiar_nuevo==null) {
								familiar.setId(null);
							}
						}
						
						familiar.setId_par(id_par);
						
					}
						
					familiar.setId_per(id_per);
					familiar.setOcu(ocu);
					familiar.setProf(prof);
					familiar.setEmail_inst(email_inst);
					familiar.setEst("A");
					
				} else {
					familiar.setId_per(id_per);
					familiar.setId_par(id_par);
					familiar.setOcu(ocu);
					familiar.setProf(prof);
					familiar.setEmail_inst(email_inst);
					familiar.setEst("A");
				}				
				id_fam_ins=familiarDAO.saveOrUpdate(familiar);
				
				GruFamFamiliar familiarNueva=null;
				if(id_fam!=null) {
					Param param = new Param();
					param.put("id_gpf", id_gpf);
					param.put("id_fam", id_fam);
					familiarNueva = gruFamFamiliarDAO.getByParams(param); //Tiene ya el grupo familiar
					//Verfico si ya tenia asignado el parentesco correcto
				}
				
				//Revisar si para el familiar existe el grupo familiar
				if (familiarNueva==null){
					//Cuando es una edicion de persona para el familiar, verificamos q exista solo un papa o mama
					if(id_par.equals(1) || id_par.equals(2)) {
						Row relacion_familiar = familiarDAO.datosFamiliaxParentescoGruFam(id_par, id_gpf);
						if(relacion_familiar!=null) {
							//Actualizamos al nuevo familiar como padre o madre
							GruFamFamiliar gruFamFamiliar = new GruFamFamiliar();
							gruFamFamiliar.setId(relacion_familiar.getInteger("id"));
							gruFamFamiliar.setId_fam(id_fam_ins);
							gruFamFamiliar.setId_gpf(id_gpf);
							gruFamFamiliar.setId_par(id_par);
							gruFamFamiliar.setEst("A");
							gruFamFamiliarDAO.saveOrUpdate(gruFamFamiliar);
						} else {
							//Insertamos el grupo familiar
							GruFamFamiliar gruFamFamiliar = new GruFamFamiliar();
							gruFamFamiliar.setId_fam(id_fam_ins);
							gruFamFamiliar.setId_gpf(id_gpf);
							gruFamFamiliar.setId_par(id_par);
							gruFamFamiliar.setEst("A");
							gruFamFamiliarDAO.saveOrUpdate(gruFamFamiliar);
						}
					} else {
						//Insertamos el grupo familiar
						GruFamFamiliar gruFamFamiliar = new GruFamFamiliar();
						gruFamFamiliar.setId_fam(id_fam_ins);
						gruFamFamiliar.setId_gpf(id_gpf);
						gruFamFamiliar.setId_par(id_par);
						gruFamFamiliar.setEst("A");
						gruFamFamiliarDAO.saveOrUpdate(gruFamFamiliar);
					}
					
					
					
				} else {
					//Actualizo con el nuevo parentesco
					GruFamFamiliar gruFamFamiliar = new GruFamFamiliar();
					gruFamFamiliar.setId_fam(id_fam_ins);
					gruFamFamiliar.setId_gpf(id_gpf);
					gruFamFamiliar.setId_par(id_par);
					gruFamFamiliar.setEst("A");
					gruFamFamiliar.setId(familiarNueva.getId());
					gruFamFamiliarDAO.saveOrUpdate(gruFamFamiliar);
				}
			} else{
				
				throw new ControllerException(error);	
			}
			
			
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("id_per", id_per);
			map.put("id_fam",id_fam_ins);
			map.put("id_gpf",id_gpf);
			result.setResult(map);
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/obtenerDatosFamiliar", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosFamiliar(Integer id_fam,String nro_doc) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( familiarDAO.obtenerDatosFamiliar(id_fam, nro_doc));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@Transactional
	@RequestMapping( value="eliminarOtroFamiliar/{id_gpf}/{id_fam}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id_gpf, @PathVariable Integer id_fam) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			familiarDAO.eliminarFamiliarGrupo(id_gpf, id_fam);
			familiarDAO.eliminarFamiliar(id_fam);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	

	@RequestMapping( value="/buscar/", method = RequestMethod.GET)
	public AjaxResponseBody buscar(Integer id_anio, String nom , String est ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( familiarDAO.buscarxNombre(id_anio, nom, est));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	

	
	@RequestMapping( value="/nro_doc/{nro_doc}", method = RequestMethod.GET)
	public AjaxResponseBody nro_doc(@PathVariable String nro_doc) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			Param param = new Param();
			param.put("nro_doc", nro_doc);
			result.setResult( familiarDAO.getByParams(param));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	

	/**
	 * Familiares que estan agregados por el mismo apoderados
	 * @param id
	 * @return
	 */
	@RequestMapping( value="/familias/{id_anio}/{id}", method = RequestMethod.GET)
	public AjaxResponseBody getFamilias(@PathVariable Integer id_anio,@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( familiarDAO.getListGrupoFamiliares(id,id_anio));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/usuario", method = RequestMethod.GET)
	public AjaxResponseBody detalle(Integer id_anio,Integer id_suc ,Integer id_au ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( familiarDAO.generarClaveFamiliarUsuario(id_anio, id_au) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/actualizarApoderado", method = RequestMethod.GET)
	public AjaxResponseBody actualizarApoderado(Integer id_anio  ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			List<Row> list=  familiarDAO.listaApoderado(id_anio);
			for (Row row : list) {
				Integer resultado = familiarDAO.actualizaApoderado(row.getInteger("id_fam"), row.getInteger("id_par"));
				logger.debug(resultado + ":" + row.getInteger("id_fam") + "-"+ row.getInteger("id_par"));
			}
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping( value="/pagosHijos", method = RequestMethod.GET)
	public AjaxResponseBody pagosHijos(Integer id_anio,Integer id_fam) {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
		List<Row> hijos = familiarDAO.listaHijos(id_anio,id_fam);
		List<Row> pagos = familiarDAO.listaPagosHijos(id_anio,id_fam,"MEN");
		for (Row row : pagos) {
			for (Row hijo : hijos) {
				if (hijo.getInteger("id").equals(row.getInteger("id") )){
					
					List<Row> pagos_hijos = (hijo.get("pagos")==null)? new ArrayList<Row>(): (List<Row>)hijo.get("pagos");
					pagos_hijos.add(row);
					hijo.put("pagos", pagos_hijos);
				}
			}
		}
		 
		
			result.setResult( hijos );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@SuppressWarnings("unchecked")
	@RequestMapping( value="/pagosPendientesHijos", method = RequestMethod.GET)
	public AjaxResponseBody pagosPendientesHijos(Integer id_anio,Integer id_fam) {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
		List<Row> hijos = familiarDAO.listaHijos(id_anio,id_fam);
		List<Row> pagos = familiarDAO.listaPagosPendientesHijos(id_anio,id_fam,"MEN");
		for (Row row : pagos) {
			for (Row hijo : hijos) {
				if (hijo.getInteger("id").equals(row.getInteger("id") )){
					
					List<Row> pagos_hijos = (hijo.get("pagos")==null)? new ArrayList<Row>(): (List<Row>)hijo.get("pagos");
					pagos_hijos.add(row);
					hijo.put("pagos", pagos_hijos);
				}
			}
		}
		 
		
			result.setResult( hijos );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	
	@RequestMapping( value="/hijos", method = RequestMethod.GET)
	public AjaxResponseBody hijos(Integer id_anio,Integer id_usr) {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
			List<Row> hijos = familiarDAO.listaHijosMatriculados(id_usr, id_anio);
			result.setResult( hijos );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarHijosFamilia", method = RequestMethod.GET)
	public AjaxResponseBody hijos(Integer id_usr) {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
			List<Row> hijos = familiarDAO.listaTodosHijosFamilia(id_usr);
			result.setResult( hijos );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarHijosMatriculadosNoTrasladados", method = RequestMethod.GET)
	public AjaxResponseBody listarHijosMatriculadosNoTrasladados(Integer id_usr, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
			List<Row> hijos = familiarDAO.listaTodosHijosFamiliaMatNoTras(id_usr, id_anio);
			result.setResult( hijos );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	/*
	@RequestMapping( value="/pruebasCorreo/{id_gra}/{id_anio}", method = RequestMethod.POST)
	public void enviarMensajes(@PathVariable Integer id_gra,@PathVariable Integer id_anio)throws Exception{

		//Obtener la lista de familiares 
		List<Row> familiares=matriculaDAO.listarAlumnosxGradoAnio(id_gra, id_anio);
		
		for (Row row : familiares) {
			//Verificar si ya existe en mensajeria familiar
			Row msj_existe=familiarDAO.mensajeExiste(row.getInteger("id_fam"), row.getInteger("id_alu"));
			if(msj_existe!=null){//si el mensaje exikste
				if(msj_existe.get("flg_en").equals("0")){//Pregunto si no esta enviado
					if(row.get("fam_corr")==null || row.get("fam_corr").equals("")){
						mensajeriaFamiliarDAO.actualizaEnviado(msj_existe.getInteger("id"),"0");//actualizo la fecha
						/*MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
						mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
						mensajeriaFamiliar.setId_per(8);
						mensajeriaFamiliar.setMsj("Datos de Acceso " + row.getString("nom") +" " +row.getString("ape_pat")+" "+row.getString("nom"));
						mensajeriaFamiliar.setEst("A");
						mensajeriaFamiliar.setFlg_en("0");
						mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);*/
						//return;
	/*				} else{
						CorreoUtil correoUtil = new CorreoUtil();
						String pdfRuta =  "/home/aeedupeh/public_html/plantillas/Comunicado Clases Virtuales.pdf";
						//String pdfRuta =  "C:/plantillas/Comunicado Clases Virtuales.pdf";
						byte[] pdfBytes = FileUtil.filePathToByte(pdfRuta);
						
						String pdfRuta2 =  "/home/aeedupeh/public_html/plantillas/pasos para ingresar al teams padres.pdf";
						//String pdfRuta2 =  "C:/plantillas/pasos para ingresar al teams padres.pdf";
						byte[] pdfBytes2 = FileUtil.filePathToByte(pdfRuta2);
						
						List<Adjunto> adjuntos = new ArrayList<Adjunto>();
						adjuntos.add(new Adjunto("Comunicado.pdf",pdfBytes));
						adjuntos.add(new Adjunto("Instructivo.pdf",pdfBytes2));
								
						  //FileSystemResource file = new FileSystemResource("http://ae.edu.pe/pdf/Comunicado Clases Virtuales.pdf");
						//creamos un adjunto con el stream de datos
						//ByteArrayDataSource attachment = new ByteArrayDataSource(myStream, "application/pdf");
						//lo añadimos al correo a enviar
						//helper.addAttachment("nombreFichero.pdf", attachment);
				
						String html="<html lang='es'>";
						html +=" <head>";
						    html +=" <meta charset='UTF-8'>";
						    html +=" <meta name='viewport' content='width=device-width, initial-scale=1.0'>";
						    html +=" <title>Document</title>";
						html +=" </head>";
						html +=" <body>";
						html +=" <table";
						    html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;border-spacing:0px;width:100%;max-width:600px;margin:0 auto 0 auto;background:#072146'";
						    html +=" width='100%'>";
						    html +=" <tbody>";
						        html +=" <tr style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif'>";
						            html +=" <td style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px'>";
						            html +=" </td>";
						            html +=" <td style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;display:block;max-width:600px;margin:0 auto;clear:both'>";
						                html +=" <div style='font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;max-width:600px;margin:0 auto;display:block;padding:0px;background-color: #004481;'>";
						                    html +=" <table style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;border-spacing:0px;width:100%;'";
						                        html +=" width='100%'>";
						                        html +=" <tbody>";
						                            html +=" <tr";
						                                html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;background:#004481'>";
						                                html +=" <td style='margin:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;padding:0px;text-align:center'";
						                                    html +=" align='center'><img src='http://ae.edu.pe/logos/logoaemail.png'";
						                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;height:75px;max-width:600px'";
						                                        html +=" data-image-whitelisted='' height='75'></td>"; 
						                            html +=" </tr>";
						                        html +=" </tbody>";
						                    html +=" </table>";
						                html +=" </div>";
						            html +=" </td>";
						            html +=" <td style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px'>";
						            html +=" </td>";
						        html +=" </tr>";
						    html +=" </tbody>";
						html +=" </table>";
						html +=" <table";
						    html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;border-spacing:0px;width:100%'";
						    html +=" width='100%'>";
						    html +=" <tbody>";
						        html +=" <tr style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif'>";
						            html +=" <td";
						                html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;display:block;max-width:600px;margin:0 auto;clear:both'>";
						                html +=" <div";
						                    html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;max-width:600px;margin:0 auto;display:block'>";
						                    html +=" <table";
						                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;border-spacing:0px;width:100%'";
						                        html +=" width='100%'>";
						                        html +=" <tbody>";
						                            html +=" <tr";
						                                html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif'>";
						                                html +=" <td";
						                                    html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px'>";
						                                    html +=" <h3";
						                                        html +=" style='margin:0;padding:0;font-family:Arial,HelveticaNeue-Light,Helvetica Neue Light,Helvetica Neue,Helvetica,Lucida Grande,sans-serif;margin-bottom:15px;color:#121212;text-align:center;margin-top:40px;font-size:25px;font-weight:bold;font-style:normal;font-stretch:normal;letter-spacing:normal;line-height:1;padding-left:50px;padding-right:50px'>";
						                                        html +=" Saludos,"+row.getString("fam_ape_pat")+" "+row.getString("fam_ape_mat")+" "+row.getString("fam_nom")+"</h3>";
						                                    html +=" <p";
						                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-weight:normal;font-size:15px;line-height:24px;color:#121212;padding-left:50px;padding-right:50px;margin-bottom:0px;text-align:center'>";
						                                        html +=" Se adjunta comunicado e instructivo de acceso al aula virtual,";
						                                        html +=" recomendamos leerlos previamente. <br> <br> El usuario y password brindado es de uso exclusivo para el aula virtual.";
						                                      html +=" </p>";                                
						                                        html +=" <center><a  href='https://www.office.com' target='_blank'><img src='http://ae.edu.pe/logos/aula.png'"; 
						                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;height:75px;max-width:600px'";
						                                        html +=" data-image-whitelisted='' height='75' ></a></center>";
						                                    html +=" <p";
						                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;color:#121212;padding-left:50px;padding-right:50px;margin-bottom:0px;text-align:center;font-weight:bold'>";
						                                        html +=" USUARIO: "+row.getString("usuario")+"</p>";
						                                    html +=" <p";
						                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;color:#121212;padding-left:50px;padding-right:50px;margin-bottom:0px;text-align:center;font-weight:bold'>";
						                                        html +=" CONTRASEÑA: "+row.getString("pass_educando")+"</p>";
						                                        html +=" <br>"; 
						                                    html +=" <p";
						                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;color:#121212;padding-left:50px;padding-right:50px;margin-bottom:0px;text-align:center;font-weight:bold'>";
						                                        html +=" <a  href='https://www.office.com' target='_blank'>AULA VIRTUAL</a></p>";
						                                        html +=" <br>"; 
						                                        html +=" <p";
						                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;color:#ed1c24;padding-left:50px;padding-right:50px;margin-bottom:0px;text-align:center'>";
						                                        html +=" Atención: Este password es temporal, obligatoriamente lo cambiara, guarde su nuevo password.";
						                                    html +=" <hr";
						                                        html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;padding-left:50px;padding-right:50px;max-width:300px;margin:0 auto 0 auto;background-color:#d3d3d3;height:1px;border:0'>";
						                                    html +=" <div";
						                                        html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;text-align:center;padding-left:50px;padding-right:50px;display:block;max-width:600px;clear:both;margin:24px 0 24px 0'>";
						                                        html +=" <p";
						                                            html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;margin-bottom:10px;font-weight:bold;font-size:15px;line-height:24px;color:#121212;text-align:right;margin:0 0 4px 0>";
						                                            html +=" La Direcci贸n</p>";
						                                    html +=" </div>";
						                                    html +=" <table";
						                                        html +=" style='font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;border-spacing:0px;max-width:320px;margin:0 auto 0 auto;padding-left:50px;padding-right:50px;padding:16px 0 24px 0;width:100%'";
						                                        html +=" width='100%'>";
						                                    html +=" </body>";
						                                    html +=" </html>";
						correoUtil.enviar("Datos de Acceso " + row.getString("nom") +" " +row.getString("ape_pat"), "",row.getString("fam_corr"), html,adjuntos,"consultas@ae.edu.pe","ALBERT EINSTEIN");
						//correoUtil.enviar("Usuario " + "LINA LEON" , "", inscripcion_ind.getCorr(), html,config.getNom()+".pdf",pdf_bytes,config.getCorr_envio(),nom_colegio_organizador);
						/*MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
						mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
						mensajeriaFamiliar.setId_per(8);
						mensajeriaFamiliar.setMsj("Datos de Acceso " + row.getString("nom") +" " +row.getString("ape_pat")+" "+row.getString("nom"));
						mensajeriaFamiliar.setEst("A");
						mensajeriaFamiliar.setFlg_en("1");
						mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);*/
	/*					mensajeriaFamiliarDAO.actualizaEnviado(msj_existe.getInteger("id"),"1");//actualizo la fecha
				}
				}
			} else{
				if(row.get("fam_corr")==null || row.get("fam_corr").equals("")){
					//mensajeriaFamiliarDAO.actualizaEnviado(msj_existe.getInteger("id"),"0");
					MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
					mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
					mensajeriaFamiliar.setId_per(8);
					mensajeriaFamiliar.setMsj("Datos de Acceso " + row.getString("nom") +" " +row.getString("ape_pat")+" "+row.getString("ape_mat"));
					mensajeriaFamiliar.setEst("A");
					mensajeriaFamiliar.setFlg_en("0");
					mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
					mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
					//return;
				} else{
					CorreoUtil correoUtil = new CorreoUtil();
					String pdfRuta =  "/home/aeedupeh/public_html/plantillas/Comunicado Clases Virtuales.pdf";
					//String pdfRuta =  "C:/plantillas/Comunicado Clases Virtuales.pdf";
					byte[] pdfBytes = FileUtil.filePathToByte(pdfRuta);
					
					String pdfRuta2 =  "/home/aeedupeh/public_html/plantillas/pasos para ingresar al teams padres.pdf";
					//String pdfRuta2 =  "C:/plantillas/pasos para ingresar al teams padres.pdf";
					byte[] pdfBytes2 = FileUtil.filePathToByte(pdfRuta2);
					
					List<Adjunto> adjuntos = new ArrayList<Adjunto>();
					adjuntos.add(new Adjunto("Comunicado.pdf",pdfBytes));
					adjuntos.add(new Adjunto("Instructivo.pdf",pdfBytes2));
							
					  //FileSystemResource file = new FileSystemResource("http://ae.edu.pe/pdf/Comunicado Clases Virtuales.pdf");
					//creamos un adjunto con el stream de datos
					//ByteArrayDataSource attachment = new ByteArrayDataSource(myStream, "application/pdf");
					//lo añadimos al correo a enviar
					//helper.addAttachment("nombreFichero.pdf", attachment);
			
					String html="<html lang='es'>";
					html +=" <head>";
					    html +=" <meta charset='UTF-8'>";
					    html +=" <meta name='viewport' content='width=device-width, initial-scale=1.0'>";
					    html +=" <title>Document</title>";
					html +=" </head>";
					html +=" <body>";
					html +=" <table";
					    html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;border-spacing:0px;width:100%;max-width:600px;margin:0 auto 0 auto;background:#072146'";
					    html +=" width='100%'>";
					    html +=" <tbody>";
					        html +=" <tr style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif'>";
					            html +=" <td style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px'>";
					            html +=" </td>";
					            html +=" <td style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;display:block;max-width:600px;margin:0 auto;clear:both'>";
					                html +=" <div style='font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;max-width:600px;margin:0 auto;display:block;padding:0px;background-color: #004481;'>";
					                    html +=" <table style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;border-spacing:0px;width:100%;'";
					                        html +=" width='100%'>";
					                        html +=" <tbody>";
					                            html +=" <tr";
					                                html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;background:#004481'>";
					                                html +=" <td style='margin:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;padding:0px;text-align:center'";
					                                    html +=" align='center'><img src='http://ae.edu.pe/logos/logoaemail.png'";
					                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;height:75px;max-width:600px'";
					                                        html +=" data-image-whitelisted='' height='75'></td>"; 
					                            html +=" </tr>";
					                        html +=" </tbody>";
					                    html +=" </table>";
					                html +=" </div>";
					            html +=" </td>";
					            html +=" <td style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px'>";
					            html +=" </td>";
					        html +=" </tr>";
					    html +=" </tbody>";
					html +=" </table>";
					html +=" <table";
					    html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;border-spacing:0px;width:100%'";
					    html +=" width='100%'>";
					    html +=" <tbody>";
					        html +=" <tr style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif'>";
					            html +=" <td";
					                html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;display:block;max-width:600px;margin:0 auto;clear:both'>";
					                html +=" <div";
					                    html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;max-width:600px;margin:0 auto;display:block'>";
					                    html +=" <table";
					                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;border-spacing:0px;width:100%'";
					                        html +=" width='100%'>";
					                        html +=" <tbody>";
					                            html +=" <tr";
					                                html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif'>";
					                                html +=" <td";
					                                    html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px'>";
					                                    html +=" <h3";
					                                        html +=" style='margin:0;padding:0;font-family:Arial,HelveticaNeue-Light,Helvetica Neue Light,Helvetica Neue,Helvetica,Lucida Grande,sans-serif;margin-bottom:15px;color:#121212;text-align:center;margin-top:40px;font-size:25px;font-weight:bold;font-style:normal;font-stretch:normal;letter-spacing:normal;line-height:1;padding-left:50px;padding-right:50px'>";
					                                        html +=" Saludos,"+row.getString("fam_ape_pat")+" "+row.getString("fam_ape_mat")+" "+row.getString("fam_nom")+"</h3>";
					                                    html +=" <p";
					                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-weight:normal;font-size:15px;line-height:24px;color:#121212;padding-left:50px;padding-right:50px;margin-bottom:0px;text-align:center'>";
					                                        html +=" Se adjunta comunicado e instructivo de acceso al aula virtual,";
					                                        html +=" recomendamos leerlos previamente. <br> <br> El usuario y password brindado es de uso exclusivo para el aula virtual.";
					                                      html +=" </p>";                                
					                                        html +=" <center><a  href='https://www.office.com' target='_blank'><img src='http://ae.edu.pe/logos/aula.png'"; 
					                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;height:75px;max-width:600px'";
					                                        html +=" data-image-whitelisted='' height='75' ></a></center>";
					                                    html +=" <p";
					                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;color:#121212;padding-left:50px;padding-right:50px;margin-bottom:0px;text-align:center;font-weight:bold'>";
					                                        html +=" USUARIO: "+row.getString("usuario")+"</p>";
					                                    html +=" <p";
					                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;color:#121212;padding-left:50px;padding-right:50px;margin-bottom:0px;text-align:center;font-weight:bold'>";
					                                        html +=" CONTRASEÑA: "+row.getString("pass_educando")+"</p>";
					                                        html +=" <br>"; 
					                                    html +=" <p";
					                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;color:#121212;padding-left:50px;padding-right:50px;margin-bottom:0px;text-align:center;font-weight:bold'>";
					                                        html +=" <a  href='https://www.office.com' target='_blank'>AULA VIRTUAL</a></p>";
					                                        html +=" <br>"; 
					                                        html +=" <p";
					                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;color:#ed1c24;padding-left:50px;padding-right:50px;margin-bottom:0px;text-align:center'>";
					                                        html +=" Atención: Este password es temporal, obligatoriamente lo cambiara, guarde su nuevo password.";
					                                    html +=" <hr";
					                                        html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;padding-left:50px;padding-right:50px;max-width:300px;margin:0 auto 0 auto;background-color:#d3d3d3;height:1px;border:0'>";
					                                    html +=" <div";
					                                        html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;text-align:center;padding-left:50px;padding-right:50px;display:block;max-width:600px;clear:both;margin:24px 0 24px 0'>";
					                                        html +=" <p";
					                                            html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;margin-bottom:10px;font-weight:bold;font-size:15px;line-height:24px;color:#121212;text-align:right;margin:0 0 4px 0>";
					                                            html +=" La Direcci贸n</p>";
					                                    html +=" </div>";
					                                    html +=" <table";
					                                        html +=" style='font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;border-spacing:0px;max-width:320px;margin:0 auto 0 auto;padding-left:50px;padding-right:50px;padding:16px 0 24px 0;width:100%'";
					                                        html +=" width='100%'>";
					                                    html +=" </body>";
					                                    html +=" </html>";
					correoUtil.enviar("Datos de Acceso " + row.getString("nom") +" " +row.getString("ape_pat"), "",row.getString("fam_corr"), html,adjuntos,"consultas@ae.edu.pe","ALBERT EINSTEIN");
					//correoUtil.enviar("Usuario " + "LINA LEON" , "", inscripcion_ind.getCorr(), html,config.getNom()+".pdf",pdf_bytes,config.getCorr_envio(),nom_colegio_organizador);
					MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
					mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
					mensajeriaFamiliar.setId_per(8);
					mensajeriaFamiliar.setMsj("Datos de Acceso " + row.getString("nom") +" " +row.getString("ape_pat")+" "+row.getString("ape_mat"));
					mensajeriaFamiliar.setEst("A");
					mensajeriaFamiliar.setFlg_en("1");
					mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
					mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
			}
			}
		}
	} */
	
	@RequestMapping( value="/pruebasCorreo/{id_gra}/{id_anio}", method = RequestMethod.POST)
	public void enviarMensajes(@PathVariable Integer id_gra,@PathVariable Integer id_anio)throws Exception{

		//Obtener la lista de familiares 
		List<Row> familiares=matriculaDAO.listarApoderadosxAnio(id_gra, id_anio);
		
		for (Row row : familiares) {
			if(row.get("fam_corr")!=null && !row.get("fam_corr").equals("")){
				CorreoUtil correoUtil = new CorreoUtil();
				//String host = request.getHeader("host");
				//System.out.println(row.get("fam_corr").toString());
				String html = "Estimad@:";
				html += "<br><br>" + row.getString("fam_ape_pat") + " " + row.getString("fam_ape_mat") +", " + row.getString("fam_nom");
				html += "<br><br>Le remitimos datos para que pueda realizar su matrícula segun cronograma previamente enviado.";
				html += "<br><br>Ingrese a: <a href='http://www.ae.edu.pe:8080/sige'>www.ae.edu.pe:8080/sige</a>";
				html += "<h2>Usuario: <font color='green'>" + row.getString("fam_nro_doc").toString()+ "</font></h2>";
				html += "<h2>Contraseña: <font color='green'>" + row.getString("fam_pass").toString()+ "</font></h2>";
				html += "<br><br>Atentamente";
				html += "<br><b>La Dirección</b>";

				correoUtil.enviar("Datos de Acceso al Sistema ", "", row.get("fam_corr").toString(), html,null,null,"consultas@ae.edu.pe","ALBERT EINSTEIN");
			}
		}
	}
	
	@RequestMapping( value="/pruebasCorreo", method = RequestMethod.POST)
	public void enviarMensajesPrueba()throws Exception{

		for (int i = 1; i < 60; i++) {
			CorreoUtil correoUtil = new CorreoUtil();
			//String pdfRuta =  "http://login.ae.edu.pe:8080/documentos/tycAcademiaVacaciones.pdf";
			//String pdfRuta =  "/opt/tomcat/webapps/documentos/Costos.pdf";
			String pdfRuta =  "C:/plantillas/Costos.pdf";
			//String pdfRuta =  null;
			byte[] pdfBytes = FileUtil.filePathToByte(pdfRuta);
			
			//String pdfRuta2 =  "/opt/tomcat/webapps/documentos/PlandeEstudios2021.pdf";
			String pdfRuta2 =  "C:/plantillas/PlandeEstudios2021.pdf";
			byte[] pdfBytes2 = FileUtil.filePathToByte(pdfRuta2);
			
			//String pdfRuta3 =  "/opt/tomcat/webapps/documentos/ReglamentoInterno2021.pdf";
			String pdfRuta3 =  "C:/plantillas/ReglamentoInterno2021.pdf";
			byte[] pdfBytes3 = FileUtil.filePathToByte(pdfRuta3);
			
			//String pdfRuta4 =  "/opt/tomcat/webapps/documentos/ContratoColegioAE2021.pdf";
			String pdfRuta4 =  "C:/plantillas/ContratoColegioAE2021.pdf";
			byte[] pdfBytes4 = FileUtil.filePathToByte(pdfRuta4);
			
			List<Adjunto> adjuntos = new ArrayList<Adjunto>();
			adjuntos.add(new Adjunto("Costos.pdf",pdfBytes));
			adjuntos.add(new Adjunto("Plan de Estudios 2021.pdf",pdfBytes2));
			adjuntos.add(new Adjunto("Reglamento Interno 2021.pdf",pdfBytes3));
			adjuntos.add(new Adjunto("Contrato Colegio AE 2021.pdf",pdfBytes4));
			//adjuntos.add(new Adjunto("Instructivo.pdf",pdfBytes2));
			String html ="Hola, esto es una prueba" ;
			Contador contador = contadorDAO.get(1);
			Integer cant_msj_env=contador.getNro();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			String format = formatter.format(new Date());
			String format2 = formatter.format(contador.getFec());
			int fecActual = Integer.parseInt(format);
			int fecContador=Integer.parseInt(format2);
			if(cant_msj_env<=500 && fecActual==fecContador) {
				correoUtil.enviar("Esto es una prueba AE " ,"", "elebola@outllok.com", html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				correoUtil.enviar("Esto es una prueba AE " ,"", "alberthuaraz@outlook.com", html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				correoUtil.enviar("Esto es una prueba AE " ,"", "rock-xoft@hotmail.com", html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				correoUtil.enviar("Esto es una prueba AE " ,"", "mespinozareyes@outlook.com", html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				correoUtil.enviar("Esto es una prueba AE " ,"", "maricris1906@hotmail.com", html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				//actualizo el contador
				cant_msj_env = cant_msj_env + 5;
				//si es 500 cambio el usuario 
				if(cant_msj_env.equals(500)) {
					//actualizo
					contadorDAO.actualizarUsuarioContador("noreply@ae.edu.pe");
				}
				contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
			} else if((cant_msj_env>500 && cant_msj_env<=1000) && fecActual==fecContador){ // matricula.getString("corr")
				correoUtil.enviar("Esto es una prueba AE " ,"", "elebola@outllok.com", html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				correoUtil.enviar("Esto es una prueba AE " ,"", "alberthuaraz@outlook.com", html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				correoUtil.enviar("Esto es una prueba AE " ,"", "rock-xoft@hotmail.com", html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				correoUtil.enviar("Esto es una prueba AE " ,"", "mespinozareyes@outlook.com", html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				correoUtil.enviar("Esto es una prueba AE " ,"", "maricris1906@hotmail.com", html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				//actualizo el contador
				cant_msj_env = cant_msj_env + 5;
				//si es 500 cambio el usuario 
				if(cant_msj_env.equals(1000)) {
					//actualizo
					contadorDAO.actualizarUsuarioContador("noreply2@ae.edu.pe");
				}
				contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
			} else if((cant_msj_env>1000 && cant_msj_env<=1500) && fecActual==fecContador){ // matricula.getString("corr")
				correoUtil.enviar("Esto es una prueba AE " ,"", "elebola@outllok.com", html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				correoUtil.enviar("Esto es una prueba AE " ,"", "alberthuaraz@outlook.com", html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				correoUtil.enviar("Esto es una prueba AE " ,"", "rock-xoft@hotmail.com", html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				correoUtil.enviar("Esto es una prueba AE " ,"", "mespinozareyes@outlook.com", html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				correoUtil.enviar("Esto es una prueba AE " ,"", "maricris1906@hotmail.com", html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				//actualizo el contador
				cant_msj_env = cant_msj_env + 5;
				//si es 500 cambio el usuario 
				if(cant_msj_env.equals(1500)){
					//actualizo
					contadorDAO.actualizarUsuarioContador("noreply3@ae.edu.pe");
				}
				contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
			} else if((cant_msj_env>1500 && cant_msj_env<=2000) && fecActual==fecContador){ // matricula.getString("corr")
				correoUtil.enviar("Esto es una prueba AE " ,"", "elebola@outllok.com", html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				correoUtil.enviar("Esto es una prueba AE " ,"", "alberthuaraz@outlook.com", html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				correoUtil.enviar("Esto es una prueba AE " ,"", "rock-xoft@hotmail.com", html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				correoUtil.enviar("Esto es una prueba AE " ,"", "mespinozareyes@outlook.com", html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				correoUtil.enviar("Esto es una prueba AE " ,"", "maricris1906@hotmail.com", html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				//actualizo el contador
				cant_msj_env = cant_msj_env + 5;
				//si es 500 cambio el usuario 
				if(cant_msj_env.equals(2000)){
					//actualizo
					contadorDAO.actualizarUsuarioContador("noreply4@ae.edu.pe");
				}
				contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
			} else if((cant_msj_env>2000) && fecActual==fecContador){
				throw new Exception("Se ha excedido el envío de mensajes, por favor comuníquese con el administrador.");
			} else if(fecActual!=fecContador){
				//Actualizo la fecha del contador, Nuevo dia
				contadorDAO.actualizarFechaContador(new Date());
				contadorDAO.actualizarUsuarioContador("noreply@ae.edu.pe");
				Contador contador2= contadorDAO.get(1);
				correoUtil.enviar("Esto es una prueba AE " ,"", "elebola@outllok.com", html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				correoUtil.enviar("Esto es una prueba AE " ,"", "alberthuaraz@outlook.com", html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				correoUtil.enviar("Esto es una prueba AE " ,"", "rock-xoft@hotmail.com", html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				correoUtil.enviar("Esto es una prueba AE " ,"", "mespinozareyes@outlook.com", html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				correoUtil.enviar("Esto es una prueba AE " ,"", "maricris1906@hotmail.com", html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				//actualizo el contador
				cant_msj_env = cant_msj_env + 5;
				//si es 500 cambio el usuario 
				contadorDAO.actualizarContador(cant_msj_env, contador2.getFec());
			}
		}
		
	}
	
	@RequestMapping( value="/enviarCorreoMasivoFamilias/{id_anio}", method = RequestMethod.POST)
	public void enviarMensajesMasivos(@PathVariable Integer id_anio)throws Exception{

		//Obtener la lista de familiares 
		List<Row> familias=matriculaDAO.listaFamiliasxAnio(id_anio);
		
		for (Row row : familias) {
			if(row.get("corr")!=null && !row.get("corr").equals("")){
				CorreoUtil correoUtil = new CorreoUtil();
				String html = "<html lang=\"en\">\r\n" + 
						"\r\n" + 
						"<head>\r\n" + 
						"  <meta charset=\"UTF-8\">\r\n" + 
						"  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" + 
						"  <title>Email Matrícula\r\n" + 
						"\r\n" + 
						"  </title>\r\n" + 
						"</head>\r\n" + 
						"\r\n" + 
						"<body>\r\n" + 
						"\r\n" + 
						"  <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\">\r\n" + 
						"    <tbody>\r\n" + 
						"      <tr>\r\n" + 
						"        <td align=\"center\" valign=\"top\">\r\n" + 
						"\r\n" + 
						"        </td>\r\n" + 
						"      </tr>\r\n" + 
						"      <tr>\r\n" + 
						"        <td align=\"center\">\r\n" + 
						"          <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"550\" class=\"m_-2564031024094939495container\"\r\n" + 
						"            align=\"center\">\r\n" + 
						"            <tbody>\r\n" + 
						"              <tr>\r\n" + 
						"                <td>\r\n" + 
						"                  <table style=\"background-color:#ffffff\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\"\r\n" + 
						"                    width=\"100%\">\r\n" + 
						"                    <tbody>\r\n" + 
						"                      <tr>\r\n" + 
						"                        <td align=\"center\" valign=\"top\">\r\n" + 
						"                          <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + 
						"                            <tbody>\r\n" + 
						"                              <tr>\r\n" + 
						"\r\n" + 
						"                                <td>\r\n" + 
						"\r\n" + 
						"                                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + 
						"                                    <tbody>\r\n" + 
						"                                      <tr>\r\n" + 
						"                                        <td align=\"center\" class=\"m_-2564031024094939495header\" valign=\"top\">\r\n" + 
						"                                          <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + 
						"                                            <tbody>\r\n" + 
						"                                              <tr>\r\n" + 
						"                                                <td align=\"left\" valign=\"top\">\r\n" + 
						"                                                  <table cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">\r\n" + 
						"                                                    <tbody>\r\n" + 
						"                                                      <tr>\r\n" + 
						"                                                        <td class=\"m_-2564031024094939495responsive-td\" valign=\"top\"\r\n" + 
						"                                                          style=\"width:100%\">\r\n" + 
						"                                                          <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\r\n" + 
						"                                                            style=\"min-width:100%\">\r\n" + 
						"                                                            <tbody>\r\n" + 
						"                                                              <tr>\r\n" + 
						"                                                                <td>\r\n" + 
						"                                                                  <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\r\n" + 
						"                                                                    <tbody>\r\n" + 
						"                                                                      <tr>\r\n" + 
						"                                                                        <td aling=\"center\"><img\r\n" + 
						"                                                                            src=\"http://ae.edu.pe/email/matricula-2021.png\"\r\n" + 
						"                                                                            style=\"display:block;padding:0px;text-align:center;height:auto;width:100%;border:0px\"\r\n" + 
						"                                                                            width=\"600\" class=\"CToWUd\"></td>\r\n" + 
						"                                                                      </tr>\r\n" + 
						"                                                                    </tbody>\r\n" + 
						"                                                                  </table>\r\n" + 
						"                                                                </td>\r\n" + 
						"                                                              </tr>\r\n" + 
						"                                                            </tbody>\r\n" + 
						"                                                          </table>\r\n" + 
						"\r\n" + 
						"                                                        </td>\r\n" + 
						"                                                      </tr>\r\n" + 
						"                                                    </tbody>\r\n" + 
						"                                                  </table>\r\n" + 
						"                                                </td>\r\n" + 
						"                                              </tr>\r\n" + 
						"                                            </tbody>\r\n" + 
						"                                          </table>\r\n" + 
						"                                        </td>\r\n" + 
						"                                      </tr>\r\n" + 
						"                                      <!-- <tr>\r\n" + 
						"                                <td align=\"center\" class=\"m_-2564031024094939495header\" valign=\"top\">\r\n" + 
						"                                  <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + 
						"                                    <tbody>\r\n" + 
						"                                    <tr>\r\n" + 
						"                                      <td align=\"left\" valign=\"top\">\r\n" + 
						"                                        <table cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">\r\n" + 
						"                                          <tbody>\r\n" + 
						"                                          <tr>\r\n" + 
						"                                            <td class=\"m_-2564031024094939495responsive-td\" valign=\"top\" style=\"width:100%\">\r\n" + 
						"                                              <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"min-width:100%\"><tbody><tr><td><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tbody><tr><td align=\"center\"><img src=\"https://ci3.googleusercontent.com/proxy/1yQGXaz1dcLtDOK4Bl5Tq3LKxU7XKIc-LcLRmhb2m4bqiaVKUkwiHl35EDAP1OM-Fp0RWgKlVxlV_wuk12dMP4jN1Wgd6_BvgY4D4bErRWO3YLRIQdD9nxa6oc19ulPq_3bn_RKYm4hmgCTT7wejC3DJnlLHjA=s0-d-e1-ft#https://image.mail.bbva.pe/lib/fe4315707564047f701771/m/12/766c20ce-5a00-4fae-8e1c-e2fbcf909fc1.png\" style=\"display:block;padding:0px;text-align:center;height:auto;width:100%\" width=\"600\" class=\"CToWUd a6T\" tabindex=\"0\"><div class=\"a6S\" dir=\"ltr\" style=\"opacity: 0.01; left: 801px; top: 335px;\"><div id=\":4xt\" class=\"T-I J-J5-Ji aQv T-I-ax7 L3 a5q\" role=\"button\" tabindex=\"0\" aria-label=\"Descargar el archivo adjunto \" data-tooltip-class=\"a1V\" data-tooltip=\"Descargar\"><div class=\"wkMEBb\"><div class=\"aSK J-J5-Ji aYr\"></div></div></div></div></td></tr></tbody></table></td></tr></tbody></table>\r\n" + 
						"                                              \r\n" + 
						"                                            </td>\r\n" + 
						"                                          </tr>\r\n" + 
						"                                          </tbody>\r\n" + 
						"                                        </table>\r\n" + 
						"                                      </td>\r\n" + 
						"                                    </tr>\r\n" + 
						"                                    </tbody>\r\n" + 
						"                                  </table>\r\n" + 
						"                                </td>\r\n" + 
						"                              </tr> -->\r\n" + 
						"                                      <tr>\r\n" + 
						"                                        <td align=\"center\" class=\"m_-2564031024094939495header\" valign=\"top\">\r\n" + 
						"                                          <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + 
						"                                            <tbody>\r\n" + 
						"                                              <tr>\r\n" + 
						"                                                <td align=\"left\" valign=\"top\">\r\n" + 
						"                                                  <table cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">\r\n" + 
						"                                                    <tbody>\r\n" + 
						"                                                      <tr>\r\n" + 
						"                                                        <td class=\"m_-2564031024094939495responsive-td\" valign=\"top\"\r\n" + 
						"                                                          style=\"width:100%\">\r\n" + 
						"                                                          <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\r\n" + 
						"                                                            style=\"background-color:#f4f4f4;min-width:100%\">\r\n" + 
						"                                                            <tbody>\r\n" + 
						"                                                              <tr>\r\n" + 
						"                                                                <td style=\"padding:30px\"><b><span\r\n" + 
						"                                                                      style=\"font-size:17px\"><span\r\n" + 
						"                                                                        style=\"font-family:Arial,Helvetica,sans-serif\">ESTIMADO\r\n" + 
						"                                                                        PADRE O MADRE DE FAMILIA</span></span></b>\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"                                                                        <br>\r\n" + 
						"                                                                        &nbsp;<div\r\n" + 
						"                                                                          style=\"text-align:justify\">\r\n" + 
						"                                                                          <span\r\n" + 
						"                                                                            style=\"font-family:Arial,Helvetica,sans-serif\"><span\r\n" + 
						"                                                                              style=\"font-size:15px\">Este\r\n" + 
						"                                                                              lunes 18 de enero\r\n" + 
						"                                                                              inician las matrículas\r\n" + 
						"                                                                              para el año académico\r\n" + 
						"                                                                              2021, recomendamos optar\r\n" + 
						"                                                                              por la matricula\r\n" + 
						"                                                                              virtual, se ha trabajado\r\n" + 
						"                                                                              en un proceso amigable y\r\n" + 
						"                                                                              fácil, si tuviera dudas\r\n" + 
						"                                                                              sobre el proceso nos\r\n" + 
						"                                                                              puede escribir a:\r\n" + 
						"                                                                              <a\r\n" + 
						"                                                                              href=\"mailto: soporte@colegioae.freshdesk.com\">soporte@colegioae.freshdesk.com</a></span></span>\r\n" + 
						"\r\n" + 
						"                                                                        </div>\r\n" + 
						"\r\n" + 
						"                                                                  <div style=\"line-height:150%;text-align:justify\">\r\n" + 
						"                                                                    <br>\r\n" + 
						"                                                                    <span style=\"font-size:15px\"><span\r\n" + 
						"                                                                        style=\"font-family:Arial,Helvetica,sans-serif\">Las matrículas se desarrollarán en:\r\n" + 
						"                                                                        <br>\r\n" + 
						"\r\n" + 
						"                                                                        <!-- Para conocer el detalle de los cambios puedes descargar la <a href=\"#\" style=\"color:#043263;text-decoration:none\" title=\"carta adjunta\" target=\"_blank\" >carta adjunta</a>.<br> -->\r\n" + 
						"\r\n" + 
						"                                                                  </div>\r\n" + 
						"                                                                </td>\r\n" + 
						"                                                              </tr>\r\n" + 
						"                                                            </tbody>\r\n" + 
						"                                                          </table>\r\n" + 
						"\r\n" + 
						"                                                        </td>\r\n" + 
						"                                                      </tr>\r\n" + 
						"                                                    </tbody>\r\n" + 
						"                                                  </table>\r\n" + 
						"                                                </td>\r\n" + 
						"                                              </tr>\r\n" + 
						"                                            </tbody>\r\n" + 
						"                                          </table>\r\n" + 
						"                                        </td>\r\n" + 
						"                                      </tr>\r\n" + 
						"                                      <tr>\r\n" + 
						"\r\n" + 
						"                                      </tr>\r\n" + 
						"                                      <tr>\r\n" + 
						"                                        <td align=\"center\" class=\"m_-2564031024094939495header\" valign=\"top\">\r\n" + 
						"                                          <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + 
						"                                            <tbody>\r\n" + 
						"                                              <tr>\r\n" + 
						"                                                <td align=\"left\" valign=\"top\">\r\n" + 
						"                                                  <table cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">\r\n" + 
						"                                                    <tbody>\r\n" + 
						"                                                      <tr>\r\n" + 
						"                                                        <td class=\"m_-2564031024094939495responsive-td\" valign=\"top\"\r\n" + 
						"                                                          style=\"width:100%\">\r\n" + 
						"                                                          <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\r\n" + 
						"                                                            style=\"background-color:#f4f4f4;min-width:100%\">\r\n" + 
						"                                                            <tbody>\r\n" + 
						"                                                              <tr>\r\n" + 
						"                                                                <td style=\"padding:0px 10px 10px\">\r\n" + 
						"                                                                  <table cellspacing=\"0\" cellpadding=\"0\"\r\n" + 
						"                                                                    style=\"width:100%\">\r\n" + 
						"                                                                    <tbody>\r\n" + 
						"                                                                      <tr>\r\n" + 
						"                                                                        <td>\r\n" + 
						"                                                                          <table cellspacing=\"0\" cellpadding=\"0\"\r\n" + 
						"                                                                            dir=\"rtl\" style=\"width:100%\">\r\n" + 
						"                                                                            <tbody>\r\n" + 
						"                                                                              <tr>\r\n" + 
						"                                                                                <td valign=\"top\"\r\n" + 
						"                                                                                  class=\"m_-2564031024094939495responsive-td\"\r\n" + 
						"                                                                                  dir=\"ltr\"\r\n" + 
						"                                                                                  style=\"width:70%;padding-left:0px\">\r\n" + 
						"                                                                                  <table cellpadding=\"0\" cellspacing=\"0\"\r\n" + 
						"                                                                                    width=\"100%\"\r\n" + 
						"                                                                                    style=\"background-color:transparent;min-width:100%\">\r\n" + 
						"                                                                                    <tbody>\r\n" + 
						"                                                                                      <tr>\r\n" + 
						"                                                                                        <td\r\n" + 
						"                                                                                          style=\"padding:0px 30px 15px 15px\">\r\n" + 
						"                                                                                          <span\r\n" + 
						"                                                                                            style=\"font-family:Arial,Helvetica,sans-serif\"><span\r\n" + 
						"                                                                                              style=\"font-size:17px\">\r\n" + 
						"                                                                                              <b>Link:</b> <a href=\"http://login.ae.edu.pe/\" target=\"_blank\">http://login.ae.edu.pe/ </a>\r\n" + 
						"                                                                                              <br>\r\n" + 
						"                                                                                              <b> Usuario:</b> "+row.getString("login")+"<br>\r\n" + 
						"                                                                                              <b> Password:</b> "+row.getString("password")+"\r\n" + 
						"                                                                                              <br>\r\n" + 
						"                                                                                        \r\n" + 
						"\r\n" + 
						"\r\n" + 
						"                                                                                            </span></span><br>\r\n" + 
						"                                                                                          &nbsp;<div\r\n" + 
						"                                                                                            style=\"text-align:justify\">\r\n" + 
						"                                                                                            <span\r\n" + 
						"                                                                                              style=\"font-family:Arial,Helvetica,sans-serif\"><span\r\n" + 
						"                                                                                                style=\"font-size:15px\">\r\n" + 
						"                                                                                                Adjuntamos 2 videos tutoriales</span></span>\r\n" + 
						"                                                                                                <ol>\r\n" + 
						"                                                                                                  <li><a href=\"https://youtu.be/C7ykY_Zct1Q\" target=\"_blank\">Proceso de Matricula Virtual</a>  </li>\r\n" + 
						"                                                                                                  <li><a href=\"https://youtu.be/n-r82rBNvlM\" target=\"_blank\">Llenado del Contrato  </a></li>\r\n" + 
						"                                                                                                  \r\n" + 
						"                                                                                                </ol>\r\n" + 
						"                                                                                                \r\n" + 
						"                                                                                            <br>\r\n" + 
						"                                                                                            <br>\r\n" + 
						"                                                                                          </div>\r\n" + 
						"                                                                                          <div\r\n" + 
						"                                                                                          style=\"text-align:right\">\r\n" + 
						"                                                                                          <span\r\n" + 
						"                                                                                            style=\"font-family:Arial,Helvetica,sans-serif\"><span\r\n" + 
						"                                                                                              style=\"font-size:15px\">\r\n" + 
						"                                                                                              <b>El Director</b></span></span>\r\n" + 
						"                                                                                             \r\n" + 
						"                                                                                              \r\n" + 
						"                                                                                          <br>\r\n" + 
						"                                                                                          <br>\r\n" + 
						"                                                                                        </div>                                                                                          <br>\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"                                                                                          <br>\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"                                                                                      \r\n" + 
						"                                                                                        </td>\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"                                                                                      </tr>\r\n" + 
						"                                                                                    </tbody>\r\n" + 
						"                                                                                  </table>\r\n" + 
						"                                                                                </td>\r\n" + 
						"\r\n" + 
						"                                                                              </tr>\r\n" + 
						"                                                                            </tbody>\r\n" + 
						"                                                                          </table>\r\n" + 
						"                                                                        </td>\r\n" + 
						"                                                                      </tr>\r\n" + 
						"                                                                    </tbody>\r\n" + 
						"                                                                  </table>\r\n" + 
						"                                                                </td>\r\n" + 
						"                                                              </tr>\r\n" + 
						"                                                            </tbody>\r\n" + 
						"                                                          </table>\r\n" + 
						"\r\n" + 
						"                                                        </td>\r\n" + 
						"                                                      </tr>\r\n" + 
						"                                                    </tbody>\r\n" + 
						"                                                  </table>\r\n" + 
						"                                                </td>\r\n" + 
						"                                              </tr>\r\n" + 
						"                                            </tbody>\r\n" + 
						"                                          </table>\r\n" + 
						"                                        </td>\r\n" + 
						"                                      </tr>\r\n" + 
						"                                      <tr>\r\n" + 
						"                                        <td align=\"center\" class=\"m_-2564031024094939495header\" valign=\"top\">\r\n" + 
						"\r\n" + 
						"                                        </td>\r\n" + 
						"                                      </tr>\r\n" + 
						"                                      <tr>\r\n" + 
						"                                        <td align=\"center\" class=\"m_-2564031024094939495header\" valign=\"top\">\r\n" + 
						"                                          <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + 
						"                                            <tbody>\r\n" + 
						"                                              <tr>\r\n" + 
						"                                                <td align=\"left\" valign=\"top\">\r\n" + 
						"\r\n" + 
						"                                                </td>\r\n" + 
						"                                              </tr>\r\n" + 
						"                                            </tbody>\r\n" + 
						"                                          </table>\r\n" + 
						"                                        </td>\r\n" + 
						"                                      </tr>\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"                                    </tbody>\r\n" + 
						"                                  </table>\r\n" + 
						"                                </td>\r\n" + 
						"                              </tr>\r\n" + 
						"                            </tbody>\r\n" + 
						"                          </table>\r\n" + 
						"                        </td>\r\n" + 
						"                      </tr>\r\n" + 
						"                    </tbody>\r\n" + 
						"                  </table>\r\n" + 
						"                </td>\r\n" + 
						"              </tr>\r\n" + 
						"            </tbody>\r\n" + 
						"          </table>\r\n" + 
						"        </td>\r\n" + 
						"      </tr>\r\n" + 
						"\r\n" + 
						"    </tbody>\r\n" + 
						"  </table>\r\n" + 
						"</body>\r\n" + 
						"\r\n" + 
						"</html>";

				correoUtil.enviar("Datos para la matrícula web 2021. ", "", row.get("corr").toString(), html,null,null,"consultas@ae.edu.pe","ALBERT EINSTEIN");
				//actualizo estado de enviado
				gru_famDAO.actualizoEstadoEnvio(row.getInteger("id"));
			}
		}
	}
	
	@RequestMapping( value="/enviarUsuarioPswFamilias/{id_anio}/{id_gir}", method = RequestMethod.POST)
	public void enviarUsuarioPswFamilias(@PathVariable Integer id_anio, @PathVariable Integer id_gir)throws Exception{

		//Obtener la lista de familiares 
		List<Row> familias=matriculaDAO.listaDatosFamiliasxAnio(id_anio, id_gir);
		
		for (Row row : familias) {
			if(row.get("corr")!=null && !row.get("corr").equals("")){
				CorreoUtil correoUtil = new CorreoUtil();
				String html = "<html lang=\"en\">\r\n" + 
						"\r\n" + 
						"<head>\r\n" + 
						"  <meta charset=\"UTF-8\">\r\n" + 
						"  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" + 
						"  <title>ENCUESTA DE INTENCIÓN DE MATRÍCULA Y MODALIDAD DE ESTUDIO 2022\r\n" + 
						"\r\n" + 
						"  </title>\r\n" + 
						"</head>\r\n" + 
						"\r\n" + 
						"<body>\r\n" + 
						"\r\n" + 
						"  <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\">\r\n" + 
						"    <tbody>\r\n" + 
						"      <tr>\r\n" + 
						"        <td align=\"center\" valign=\"top\">\r\n" + 
						"\r\n" + 
						"        </td>\r\n" + 
						"      </tr>\r\n" + 
						"      <tr>\r\n" + 
						"        <td align=\"center\">\r\n" + 
						"          <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"550\" class=\"m_-2564031024094939495container\"\r\n" + 
						"            align=\"center\">\r\n" + 
						"            <tbody>\r\n" + 
						"              <tr>\r\n" + 
						"                <td>\r\n" + 
						"                  <table style=\"background-color:#ffffff\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\"\r\n" + 
						"                    width=\"100%\">\r\n" + 
						"                    <tbody>\r\n" + 
						"                      <tr>\r\n" + 
						"                        <td align=\"center\" valign=\"top\">\r\n" + 
						"                          <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + 
						"                            <tbody>\r\n" + 
						"                              <tr>\r\n" + 
						"\r\n" + 
						"                                <td>\r\n" + 
						"\r\n" + 
						"                                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + 
						"                                    <tbody>\r\n" + 
						"                                      <tr>\r\n" + 
						"                                        <td align=\"center\" class=\"m_-2564031024094939495header\" valign=\"top\">\r\n" + 
						"                                          <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + 
						"                                            <tbody>\r\n" + 
						"                                              <tr>\r\n" + 
						"                                                <td align=\"left\" valign=\"top\">\r\n" + 
						"                                                  <table cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">\r\n" + 
						"                                                    <tbody>\r\n" + 
						"                                                      <tr>\r\n" + 
						"                                                        <td class=\"m_-2564031024094939495responsive-td\" valign=\"top\"\r\n" + 
						"                                                          style=\"width:100%\">\r\n" + 
						"                                                          <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\r\n" + 
						"                                                            style=\"min-width:100%\">\r\n" + 
						"                                                            <tbody>\r\n" + 
						"                                                              <tr>\r\n" + 
						"                                                                <td>\r\n" + 
						"                                                                  <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\r\n" + 
						"                                                                    <tbody>\r\n" + 
						"                                                                      <tr>\r\n" + 
						"                                                                        <td aling=\"center\"><img\r\n" + 
						"                                                                            src=\"http://ae.edu.pe/email/en1.png\"\r\n" + 
						"                                                                            style=\"display:block;padding:0px;text-align:center;height:auto;width:100%;border:0px\"\r\n" + 
						"                                                                            width=\"600\" class=\"CToWUd\"></td>\r\n" + 
						"                                                                      </tr>\r\n" + 
						"                                                                    </tbody>\r\n" + 
						"                                                                  </table>\r\n" + 
						"                                                                </td>\r\n" + 
						"                                                              </tr>\r\n" + 
						"                                                            </tbody>\r\n" + 
						"                                                          </table>\r\n" + 
						"\r\n" + 
						"                                                        </td>\r\n" + 
						"                                                      </tr>\r\n" + 
						"                                                    </tbody>\r\n" + 
						"                                                  </table>\r\n" + 
						"                                                </td>\r\n" + 
						"                                              </tr>\r\n" + 
						"                                            </tbody>\r\n" + 
						"                                          </table>\r\n" + 
						"                                        </td>\r\n" + 
						"                                      </tr>\r\n" + 
						"                                      <!-- <tr>\r\n" + 
						"                                <td align=\"center\" class=\"m_-2564031024094939495header\" valign=\"top\">\r\n" + 
						"                                  <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + 
						"                                    <tbody>\r\n" + 
						"                                    <tr>\r\n" + 
						"                                      <td align=\"left\" valign=\"top\">\r\n" + 
						"                                        <table cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">\r\n" + 
						"                                          <tbody>\r\n" + 
						"                                          <tr>\r\n" + 
						"                                            <td class=\"m_-2564031024094939495responsive-td\" valign=\"top\" style=\"width:100%\">\r\n" + 
						"                                              <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"min-width:100%\"><tbody><tr><td><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tbody><tr><td align=\"center\"><img src=\"https://ci3.googleusercontent.com/proxy/1yQGXaz1dcLtDOK4Bl5Tq3LKxU7XKIc-LcLRmhb2m4bqiaVKUkwiHl35EDAP1OM-Fp0RWgKlVxlV_wuk12dMP4jN1Wgd6_BvgY4D4bErRWO3YLRIQdD9nxa6oc19ulPq_3bn_RKYm4hmgCTT7wejC3DJnlLHjA=s0-d-e1-ft#https://image.mail.bbva.pe/lib/fe4315707564047f701771/m/12/766c20ce-5a00-4fae-8e1c-e2fbcf909fc1.png\" style=\"display:block;padding:0px;text-align:center;height:auto;width:100%\" width=\"600\" class=\"CToWUd a6T\" tabindex=\"0\"><div class=\"a6S\" dir=\"ltr\" style=\"opacity: 0.01; left: 801px; top: 335px;\"><div id=\":4xt\" class=\"T-I J-J5-Ji aQv T-I-ax7 L3 a5q\" role=\"button\" tabindex=\"0\" aria-label=\"Descargar el archivo adjunto \" data-tooltip-class=\"a1V\" data-tooltip=\"Descargar\"><div class=\"wkMEBb\"><div class=\"aSK J-J5-Ji aYr\"></div></div></div></div></td></tr></tbody></table></td></tr></tbody></table>\r\n" + 
						"                                              \r\n" + 
						"                                            </td>\r\n" + 
						"                                          </tr>\r\n" + 
						"                                          </tbody>\r\n" + 
						"                                        </table>\r\n" + 
						"                                      </td>\r\n" + 
						"                                    </tr>\r\n" + 
						"                                    </tbody>\r\n" + 
						"                                  </table>\r\n" + 
						"                                </td>\r\n" + 
						"                              </tr> -->\r\n" + 
						"                                      <tr>\r\n" + 
						"                                        <td align=\"center\" class=\"m_-2564031024094939495header\" valign=\"top\">\r\n" + 
						"                                          <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + 
						"                                            <tbody>\r\n" + 
						"                                              <tr>\r\n" + 
						"                                                <td align=\"left\" valign=\"top\">\r\n" + 
						"                                                  <table cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">\r\n" + 
						"                                                    <tbody>\r\n" + 
						"                                                      <tr>\r\n" + 
						"                                                        <td class=\"m_-2564031024094939495responsive-td\" valign=\"top\"\r\n" + 
						"                                                          style=\"width:100%\">\r\n" + 
						"                                                          <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\r\n" + 
						"                                                            style=\"background-color:#f4f4f4;min-width:100%\">\r\n" + 
						"                                                            <tbody>\r\n" + 
						"                                                              <tr>\r\n" + 
						"                                                                <td style=\"padding:30px\"><b><center><span\r\n" + 
						"                                                                      style=\"font-size:17px\"><span\r\n" + 
						"                                                                        style=\"font-family:Arial,Helvetica,sans-serif\">ESTIMADO\r\n" + 
						"                                                                        PADRE Y/O MADRE DE FAMILIA\r\n" + 
						"                                                                      </span></span></b></center>\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"                                                                        \r\n" + 
						"                                                                        &nbsp;<div\r\n" + 
						"                                                                          style=\"text-align:justify\">\r\n" + 
						"                                                                          <span\r\n" + 
						"                                                                            style=\"font-family:Arial,Helvetica,sans-serif\"><center><span\r\n" + 
						"                                                                              style=\"font-size:15px\">Reciba un cordial saludo, invitamos a Ud. a participar de la <b>\"ENCUESTA DE INTENCIÓN DE MATRÍCULA Y MODALIDAD DE ESTUDIO 2022\"</b>, la misma que nos brindará información importante para un exitoso año académico 2022.\r\n" + 
						"                                                                            </center>\r\n" + 
						"                                                                            <br>\r\n" + 
						"                                                                           \r\n" + 
						"\r\n" + 
						"                                                                        </div>\r\n" + 
						"\r\n" + 
						"                                                                  <div style=\"line-height:150%;text-align:justify\">\r\n" + 
						"                                                                    \r\n" + 
						"                                                                    <span style=\"font-size:15px\"><span\r\n" + 
						"                                                                        style=\"font-family:Arial,Helvetica,sans-serif\"><center>La encuesta la encontrara en la web: <a href=\"http://www.login.ae.edu.pe\"  target=\"_blank\"> www.login.ae.edu.pe</a>\r\n" + 
						"                                                                            <center><a href=\"http://www.login.ae.edu.pe\" target=\"_blank\"><img src=\"http://ae.edu.pe/email/btn.png\" alt=\"\" width=\"300px\"></a></center>\r\n" + 
						"                                                                            \r\n" + 
						"                                                                          Los datos de acceso son:\r\n" + 
						"                                                                        </center>\r\n" + 
						"                                                                        <!-- Para conocer el detalle de los cambios puedes descargar la <a href=\"#\" style=\"color:#043263;text-decoration:none\" title=\"carta adjunta\" target=\"_blank\" >carta adjunta</a>.<br> -->\r\n" + 
						"\r\n" + 
						"                                                                  </div>\r\n" + 
						"                                                                \r\n" + 
						"                                                                  &nbsp;<div\r\n" + 
						"                                                                  style=\"text-align:left\">\r\n" + 
						"                                                                  <span\r\n" + 
						"                                                                    style=\"font-family:Arial,Helvetica,sans-serif\"><span\r\n" + 
						"                                                                      style=\"font-size:15px\">\r\n" + 
						"                                                                      \r\n" + 
						"                                                                      <b>FAMILIA:</b>&nbsp;"+row.getString("nom")+" <br>\r\n" + 
						"                                                                      <b>Usuario:</b>&nbsp;"+row.getString("login")+"<br>\r\n" + 
						"                                                                      <b>Password:</b>&nbsp;"+row.getString("password")+"<br>\r\n" + 
						"                                                                      \r\n" + 
						"                                                                     </span></span>\r\n" + 
						"\r\n" + 
						"                                                                </div>\r\n" + 
						"                                                              \r\n" + 
						"                                                                &nbsp;<div\r\n" + 
						"                                                                style=\"text-align:center\">\r\n" + 
						"                                                                <span\r\n" + 
						"                                                                  style=\"font-family:Arial,Helvetica,sans-serif\"><span\r\n" + 
						"                                                                    style=\"font-size:15px\">Atentamente. <br>\r\n" + 
						"                                                                    Coordinación Académica.\r\n" + 
						"                                                                   </span></span>\r\n" + 
						"\r\n" + 
						"                                                              </div>\r\n" + 
						"\r\n" + 
						"                                                                </td>\r\n" + 
						"                                                              </tr>\r\n" + 
						"                                                            </tbody>\r\n" + 
						"                                                          </table>\r\n" + 
						"\r\n" + 
						"                                                        </td>\r\n" + 
						"                                                      </tr>\r\n" + 
						"                                                    </tbody>\r\n" + 
						"                                                  </table>\r\n" + 
						"                                                </td>\r\n" + 
						"                                              </tr>\r\n" + 
						"                                            </tbody>\r\n" + 
						"                                          </table>\r\n" + 
						"                                        </td>\r\n" + 
						"                                      </tr>\r\n" + 
						"                                      <tr>\r\n" + 
						"\r\n" + 
						"                                      </tr>\r\n" + 
						"                                      <tr>\r\n" + 
						"                                       \r\n" + 
						"                                      </tr>\r\n" + 
						"                                      <tr>\r\n" + 
						"                                \r\n" + 
						"\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"                                    </tbody>\r\n" + 
						"                                  </table>\r\n" + 
						"                                </td>\r\n" + 
						"                              </tr>\r\n" + 
						"                            </tbody>\r\n" + 
						"                          </table>\r\n" + 
						"                        </td>\r\n" + 
						"                      </tr>\r\n" + 
						"                    </tbody>\r\n" + 
						"                  </table>\r\n" + 
						"                </td>\r\n" + 
						"              </tr>\r\n" + 
						"            </tbody>\r\n" + 
						"          </table>\r\n" + 
						"        </td>\r\n" + 
						"      </tr>\r\n" + 
						"\r\n" + 
						"    </tbody>\r\n" + 
						"  </table>\r\n" + 
						"</body>\r\n" + 
						"\r\n" + 
						"</html>";

				//correoUtil.enviar("Datos para la matrícula web 2021. ", "", row.get("corr").toString(), html,null,null,"consultas@ae.edu.pe","ALBERT EINSTEIN");
				Contador contador = contadorDAO.get(1);
				Integer cant_msj_env=contador.getNro();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
				String format = formatter.format(new Date());
				String format2 = formatter.format(contador.getFec());
				int fecActual = Integer.parseInt(format);
				int fecContador=Integer.parseInt(format2);
				String correo_apod= row.getString("corr");
				if(!correo_apod.equals("")) {
					if(cant_msj_env<=500 && fecActual==fecContador) {
						correoUtil.enviar("Datos de acceso para la Encuesta de Intención de Matrícula y Modalidad de Estudio 2022 " , "",correo_apod, html,null,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
						MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
						mensajeriaFamiliar.setId_des(row.getInteger("id"));
						mensajeriaFamiliar.setId_per(8);
						mensajeriaFamiliar.setMsj("Datos de Acceso - Familia " + row.getString("nom"));
						mensajeriaFamiliar.setEst("A");
						mensajeriaFamiliar.setFlg_en("1");
						//mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
						mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
						mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
						//Actualizo el estado de la matricula a correo enviado
						matriculaDAO.actualizarestadoEnviadoCorreoFamilia(row.getInteger("id"));
						//actualizo el contador
						cant_msj_env = cant_msj_env + 1;
						//si es 500 cambio el usuario 
						if(cant_msj_env.equals(500)) {
							//actualizo
							contadorDAO.actualizarUsuarioContador("noreply@ae.edu.pe");
						}
						contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
					} else if((cant_msj_env>500 && cant_msj_env<=1000) && fecActual==fecContador){ // matricula.getString("corr")
						correoUtil.enviar("Datos de acceso para la Encuesta de Intención de Matrícula y Modalidad de Estudio 2022 ","",correo_apod, html,null,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
						MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
						mensajeriaFamiliar.setId_des(row.getInteger("id"));
						mensajeriaFamiliar.setId_per(8);
						mensajeriaFamiliar.setMsj("Datos de Acceso - Familia " + row.getString("nom"));
						mensajeriaFamiliar.setEst("A");
						mensajeriaFamiliar.setFlg_en("1");
						//mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
						mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
						mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
						//Actualizo el estado de la matricula a correo enviado
						matriculaDAO.actualizarestadoEnviadoCorreoFamilia(row.getInteger("id"));
						//actualizo el contador
						cant_msj_env = cant_msj_env + 1;
						//si es 500 cambio el usuario 
						if(cant_msj_env.equals(1000)) {
							//actualizo
							contadorDAO.actualizarUsuarioContador("noreply2@ae.edu.pe");
						}
						contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
					} else if((cant_msj_env>1000 && cant_msj_env<=1500) && fecActual==fecContador){ // matricula.getString("corr")
						correoUtil.enviar("Datos de acceso para la Encuesta de Intención de Matrícula y Modalidad de Estudio 2022 ", "",correo_apod, html,null,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
						MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
						mensajeriaFamiliar.setId_des(row.getInteger("id"));
						mensajeriaFamiliar.setId_per(8);
						mensajeriaFamiliar.setMsj("Datos de Acceso - Familia " + row.getString("nom"));
						mensajeriaFamiliar.setEst("A");
						mensajeriaFamiliar.setFlg_en("1");
						//mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
						mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
						mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
						//Actualizo el estado de la matricula a correo enviado
						matriculaDAO.actualizarestadoEnviadoCorreoFamilia(row.getInteger("id"));
						//actualizo el contador
						cant_msj_env = cant_msj_env + 1;
						//si es 500 cambio el usuario 
						if(cant_msj_env.equals(1500)){
							//actualizo
							contadorDAO.actualizarUsuarioContador("noreply3@ae.edu.pe");
						}
						contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
					} else if((cant_msj_env>1500 && cant_msj_env<=2000) && fecActual==fecContador){ // matricula.getString("corr")
						correoUtil.enviar("Datos de acceso para la Encuesta de Intención de Matrícula y Modalidad de Estudio 2022 ",correo_apod, html,null,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
						MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
						mensajeriaFamiliar.setId_des(row.getInteger("id"));
						mensajeriaFamiliar.setId_per(8);
						mensajeriaFamiliar.setMsj("Datos de Acceso - Familia " + row.getString("nom"));
						mensajeriaFamiliar.setEst("A");
						mensajeriaFamiliar.setFlg_en("1");
						//mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
						mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
						mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
						//Actualizo el estado de la matricula a correo enviado
						matriculaDAO.actualizarestadoEnviadoCorreoFamilia(row.getInteger("id"));
						//actualizo el contador
						cant_msj_env = cant_msj_env + 1;
						//si es 500 cambio el usuario 
						if(cant_msj_env.equals(2000)){
							//actualizo
							contadorDAO.actualizarUsuarioContador("noreply4@ae.edu.pe");
						}
						contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
					} else if((cant_msj_env>2000) && fecActual==fecContador){
						throw new Exception("Se ha excedido el envío de mensajes, por favor comuníquese con el administrador.");
					} else if(fecActual!=fecContador){
						//Actualizo la fecha del contador, Nuevo dia
						contadorDAO.actualizarFechaContador(new Date());
						contadorDAO.actualizarUsuarioContador("noreply@ae.edu.pe");
						Contador contador2= contadorDAO.get(1);
						correoUtil.enviar("Datos de acceso para la Encuesta de Intención de Matrícula y Modalidad de Estudio 2022 ", "",correo_apod, html,null,"informes@ae.edu.pe","ALBERT EINSTEIN",contador2.getUsr(), contador2.getPsw());
						MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
						mensajeriaFamiliar.setId_des(row.getInteger("id"));
						mensajeriaFamiliar.setId_per(8);
						mensajeriaFamiliar.setMsj("Datos de Acceso - Familia " + row.getString("nom"));
						mensajeriaFamiliar.setEst("A");
						mensajeriaFamiliar.setFlg_en("1");
						//mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
						mensajeriaFamiliar.setUsr_rmt(contador2.getUsr());
						mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
						//Actualizo el estado de la matricula a correo enviado
						matriculaDAO.actualizarestadoEnviadoCorreoFamilia(row.getInteger("id"));
						//actualizo el contador
						cant_msj_env = cant_msj_env + 1;
						//si es 500 cambio el usuario 
						contadorDAO.actualizarContador(cant_msj_env, contador2.getFec());
					}
				}
				//actualizo estado de enviado
				gru_famDAO.actualizoEstadoEnvio(row.getInteger("id"));
			}
		}
	}
	
	@RequestMapping( value="/enviarAceptacinMatricula/{id_fam}", method = RequestMethod.POST)
	public void enviarAceptacionMatricula(@PathVariable Integer id_fam)throws Exception{

		//Obtener la lista de familiares 
		//Row inscripcion= matriculaDAO.getInscripcionAulaVirtual(id_alu);
		Familiar familiar= familiarDAO.getByParams(new Param("id",id_fam));
		
			if(familiar.getCorr()!=null && !familiar.getCorr().equals("")){
				CorreoUtil correoUtil = new CorreoUtil();
				//String host = request.getHeader("host");
				////System.out.println(row.get("fam_corr").toString());
				String html = "Buen día,";
				html +="<p align='justify'>Es un gusto saludarle y darle la bienvenida al nuevo sistema de educación virtual del Colegio Albert Einstein, a partir de este momento le acompañaremos a disfrutar de esta gran experiencia, prometiéndole mejorar día a día a fin de brindarle las mejores herramientas para su aprendizaje.</p>";
				html +="<p align='justify'>Al haber completado la primera etapa de REGISTRO, se ha procedido al registro de sus datos y los de su padre, madre o apoderado en nuestra base de datos, así como se le ha creado la presente cuenta de correo para centralizar las comunicaciones de carácter académico.</p>";
				html +="<p align='justify'>El segundo paso a seguir será su ingreso a la plataforma virtual E-Learning, la cual podrá ser efectuada a partir del <b><u>11 de mayo del presente año</u></b>. Cabe recordar que el acceso a dicha plataforma debe ser realizado únicamente por el usuario registrado.</p>";
				html +="<p align='justify'>Una vez haya ingresado a la plataforma, usted podrá visualizar la programación de los cursos a dictarse, los horarios sugeridos, los docentes asignados, y el grupo al que usted pertenece. Asimismo, se incluirá la información de actividades adicionales, así como de herramientas interactivas, las cuales serán incrementadas durante el presente proceso educativo.</p>";
				html +="<p align='justify'>Este sistema de educación virtual permite lograr resultados bastante satisfactorios y traspasar las barreras geográficas para conectarnos a un mundo globalizado y exigente; sin embargo, todo ello requiere del compromiso y responsabilidad del participante para su activo desenvolvimiento en las interacciones virtuales, así como en el cumplimiento de tareas y evaluaciones a rendirse, es por ello que invitamos a la participación conjunta con los padres, madres y tutores, quienes además de verificar la calidad educativa, son pieza clave en la formación integral de nuestros participantes.</p>";
				html +="<p align='justify'>Tenga en cuenta que toda consulta sobre el uso del sistema y reporte de problemas deberá ser canalizada a través del correo: plataforma@ae.edu.pe</p>";
				html +="<p align='justify'>Llegado a este punto, le queremos reiterar la bienvenida a nuestra gran comunidad educativa, garantizando el brindarle las mejores herramientas para el cumplimiento de sus metas académicas.</p>";
				html +="<p align='justify'>¡Un Einstino: Un Triunfador!";
				html += "<br><br>Atentamente";
				html += "<br><b>La Dirección</b>";
				/*String pdfRuta =  "/home/aeedupeh/public_html/plantillas/Comunicado Clases Virtuales.pdf";
				//String pdfRuta =  "C:/plantillas/Comunicado Clases Virtuales.pdf";
				byte[] pdfBytes = FileUtil.filePathToByte(pdfRuta);
				
				String pdfRuta2 =  "/home/aeedupeh/public_html/plantillas/pasos para ingresar al teams padres.pdf";
				//String pdfRuta2 =  "C:/plantillas/pasos para ingresar al teams padres.pdf";
				byte[] pdfBytes2 = FileUtil.filePathToByte(pdfRuta2);
				
				List<Adjunto> adjuntos = new ArrayList<Adjunto>();
				adjuntos.add(new Adjunto("Comunicado.pdf",pdfBytes));
				adjuntos.add(new Adjunto("Instructivo.pdf",pdfBytes2));*/
			//comentado x la apuranza x ahora	correoUtil.enviar("BIENVENIDO(A) AL SISTEMA EDUCATIVO E-LEARNING DEL COLEGIO ALBERT EINSTEIN HUARAZ", "", familiar.getCorr(), html,null,"consultas@ae.edu.pe","ALBERT EINSTEIN");
				//correoUtil.enviar("Datos de Acceso " + row.getString("nom") +" " +row.getString("ape_pat"), "",row.getString("fam_corr"), html,adjuntos,"consultas@ae.edu.pe","ALBERT EINSTEIN");
			}
	}
	
	
	@RequestMapping( value="/enviarMensajexFamiliar/{id_fam}/{id_gpf}/{id_anio}", method = RequestMethod.POST)
	public void enviarMensajexFamiliar(@PathVariable Integer id_fam, @PathVariable Integer id_gpf,@PathVariable Integer id_anio)throws Exception{

		//Obtener la lista de familiares 
		List<Row> familiares=familiarDAO.listarHijosFamiliar(id_fam, id_gpf, id_anio);
		
		for (Row row : familiares) {
			//Verificar si ya existe en mensajeria familiar
			Row msj_existe=familiarDAO.mensajeExiste(row.getInteger("id_fam"), row.getInteger("id_alu"));
			if(msj_existe!=null){//si el mensaje exikste
				if(msj_existe.get("flg_en").equals("0")){//Pregunto si no esta enviado
					if(row.get("fam_corr")==null || row.get("fam_corr").equals("")){
						mensajeriaFamiliarDAO.actualizaEnviado(msj_existe.getInteger("id"),"0");//actualizo la fecha
						/*MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
						mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
						mensajeriaFamiliar.setId_per(8);
						mensajeriaFamiliar.setMsj("Datos de Acceso " + row.getString("nom") +" " +row.getString("ape_pat")+" "+row.getString("nom"));
						mensajeriaFamiliar.setEst("A");
						mensajeriaFamiliar.setFlg_en("0");
						mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);*/
						//return;
					} else{
						CorreoUtil correoUtil = new CorreoUtil();
						String pdfRuta =  "/home/aeedupeh/public_html/plantillas/Comunicado Clases Virtuales.pdf";
						//String pdfRuta =  "C:/plantillas/Comunicado Clases Virtuales.pdf";
						byte[] pdfBytes = FileUtil.filePathToByte(pdfRuta);
						
						String pdfRuta2 =  "/home/aeedupeh/public_html/plantillas/pasos para ingresar al teams padres.pdf";
						//String pdfRuta2 =  "C:/plantillas/pasos para ingresar al teams padres.pdf";
						byte[] pdfBytes2 = FileUtil.filePathToByte(pdfRuta2);
						
						List<Adjunto> adjuntos = new ArrayList<Adjunto>();
						adjuntos.add(new Adjunto("Comunicado.pdf",pdfBytes));
						adjuntos.add(new Adjunto("Instructivo.pdf",pdfBytes2));
								
						  //FileSystemResource file = new FileSystemResource("http://ae.edu.pe/pdf/Comunicado Clases Virtuales.pdf");
						//creamos un adjunto con el stream de datos
						//ByteArrayDataSource attachment = new ByteArrayDataSource(myStream, "application/pdf");
						//lo añadimos al correo a enviar
						//helper.addAttachment("nombreFichero.pdf", attachment);
				
						String html="<html lang='es'>";
						html +=" <head>";
						    html +=" <meta charset='UTF-8'>";
						    html +=" <meta name='viewport' content='width=device-width, initial-scale=1.0'>";
						    html +=" <title>Document</title>";
						html +=" </head>";
						html +=" <body>";
						html +=" <table";
						    html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;border-spacing:0px;width:100%;max-width:600px;margin:0 auto 0 auto;background:#072146'";
						    html +=" width='100%'>";
						    html +=" <tbody>";
						        html +=" <tr style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif'>";
						            html +=" <td style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px'>";
						            html +=" </td>";
						            html +=" <td style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;display:block;max-width:600px;margin:0 auto;clear:both'>";
						                html +=" <div style='font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;max-width:600px;margin:0 auto;display:block;padding:0px;background-color: #004481;'>";
						                    html +=" <table style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;border-spacing:0px;width:100%;'";
						                        html +=" width='100%'>";
						                        html +=" <tbody>";
						                            html +=" <tr";
						                                html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;background:#004481'>";
						                                html +=" <td style='margin:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;padding:0px;text-align:center'";
						                                    html +=" align='center'><img src='http://ae.edu.pe/logos/logoaemail.png'";
						                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;height:75px;max-width:600px'";
						                                        html +=" data-image-whitelisted='' height='75'></td>"; 
						                            html +=" </tr>";
						                        html +=" </tbody>";
						                    html +=" </table>";
						                html +=" </div>";
						            html +=" </td>";
						            html +=" <td style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px'>";
						            html +=" </td>";
						        html +=" </tr>";
						    html +=" </tbody>";
						html +=" </table>";
						html +=" <table";
						    html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;border-spacing:0px;width:100%'";
						    html +=" width='100%'>";
						    html +=" <tbody>";
						        html +=" <tr style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif'>";
						            html +=" <td";
						                html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;display:block;max-width:600px;margin:0 auto;clear:both'>";
						                html +=" <div";
						                    html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;max-width:600px;margin:0 auto;display:block'>";
						                    html +=" <table";
						                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;border-spacing:0px;width:100%'";
						                        html +=" width='100%'>";
						                        html +=" <tbody>";
						                            html +=" <tr";
						                                html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif'>";
						                                html +=" <td";
						                                    html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px'>";
						                                    html +=" <h3";
						                                        html +=" style='margin:0;padding:0;font-family:Arial,HelveticaNeue-Light,Helvetica Neue Light,Helvetica Neue,Helvetica,Lucida Grande,sans-serif;margin-bottom:15px;color:#121212;text-align:center;margin-top:40px;font-size:25px;font-weight:bold;font-style:normal;font-stretch:normal;letter-spacing:normal;line-height:1;padding-left:50px;padding-right:50px'>";
						                                        html +=" Saludos,"+row.getString("fam_ape_pat")+" "+row.getString("fam_ape_mat")+" "+row.getString("fam_nom")+"</h3>";
						                                    html +=" <p";
						                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-weight:normal;font-size:15px;line-height:24px;color:#121212;padding-left:50px;padding-right:50px;margin-bottom:0px;text-align:center'>";
						                                        html +=" Se adjunta comunicado e instructivo de acceso al aula virtual,";
						                                        html +=" recomendamos leerlos previamente. <br> <br> El usuario y password brindado es de uso exclusivo para el aula virtual.";
						                                      html +=" </p>";                                
						                                        html +=" <center><a  href='https://www.office.com' target='_blank'><img src='http://ae.edu.pe/logos/aula.png'"; 
						                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;height:75px;max-width:600px'";
						                                        html +=" data-image-whitelisted='' height='75' ></a></center>";
						                                    html +=" <p";
						                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;color:#121212;padding-left:50px;padding-right:50px;margin-bottom:0px;text-align:center;font-weight:bold'>";
						                                        html +=" USUARIO: "+row.getString("usuario")+"</p>";
						                                    html +=" <p";
						                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;color:#121212;padding-left:50px;padding-right:50px;margin-bottom:0px;text-align:center;font-weight:bold'>";
						                                        html +=" CONTRASEÑA: "+row.getString("pass_educando")+"</p>";
						                                        html +=" <br>"; 
						                                    html +=" <p";
						                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;color:#121212;padding-left:50px;padding-right:50px;margin-bottom:0px;text-align:center;font-weight:bold'>";
						                                        html +=" <a  href='https://www.office.com' target='_blank'>AULA VIRTUAL</a></p>";
						                                        html +=" <br>"; 
						                                        html +=" <p";
						                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;color:#ed1c24;padding-left:50px;padding-right:50px;margin-bottom:0px;text-align:center'>";
						                                        html +=" Atención: Este password es temporal, obligatoriamente lo cambiara, guarde su nuevo password.";
						                                    html +=" <hr";
						                                        html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;padding-left:50px;padding-right:50px;max-width:300px;margin:0 auto 0 auto;background-color:#d3d3d3;height:1px;border:0'>";
						                                    html +=" <div";
						                                        html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;text-align:center;padding-left:50px;padding-right:50px;display:block;max-width:600px;clear:both;margin:24px 0 24px 0'>";
						                                        html +=" <p";
						                                            html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;margin-bottom:10px;font-weight:bold;font-size:15px;line-height:24px;color:#121212;text-align:right;margin:0 0 4px 0>";
						                                            html +=" La Direcci贸n</p>";
						                                    html +=" </div>";
						                                    html +=" <table";
						                                        html +=" style='font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;border-spacing:0px;max-width:320px;margin:0 auto 0 auto;padding-left:50px;padding-right:50px;padding:16px 0 24px 0;width:100%'";
						                                        html +=" width='100%'>";
						                                    html +=" </body>";
						                                    html +=" </html>";
						//comentado x la apuranza x ahora correoUtil.enviar("Datos de Acceso " + row.getString("nom") +" " +row.getString("ape_pat"), "",row.getString("fam_corr"), html,adjuntos,"consultas@ae.edu.pe","ALBERT EINSTEIN");
						//correoUtil.enviar("Usuario " + "LINA LEON" , "", inscripcion_ind.getCorr(), html,config.getNom()+".pdf",pdf_bytes,config.getCorr_envio(),nom_colegio_organizador);
						/*MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
						mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
						mensajeriaFamiliar.setId_per(8);
						mensajeriaFamiliar.setMsj("Datos de Acceso " + row.getString("nom") +" " +row.getString("ape_pat")+" "+row.getString("nom"));
						mensajeriaFamiliar.setEst("A");
						mensajeriaFamiliar.setFlg_en("1");
						mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);*/
						mensajeriaFamiliarDAO.actualizaEnviado(msj_existe.getInteger("id"),"1");//actualizo la fecha
				}
				} else if(msj_existe.get("flg_en").equals("1")){//Pregunto si esta enviado
					CorreoUtil correoUtil = new CorreoUtil();
					String pdfRuta =  "/home/aeedupeh/public_html/plantillas/Comunicado Clases Virtuales.pdf";
					//String pdfRuta =  "C:/plantillas/Comunicado Clases Virtuales.pdf";
					byte[] pdfBytes = FileUtil.filePathToByte(pdfRuta);
					
					String pdfRuta2 =  "/home/aeedupeh/public_html/plantillas/pasos para ingresar al teams padres.pdf";
					//String pdfRuta2 =  "C:/plantillas/pasos para ingresar al teams padres.pdf";
					byte[] pdfBytes2 = FileUtil.filePathToByte(pdfRuta2);
					
					List<Adjunto> adjuntos = new ArrayList<Adjunto>();
					adjuntos.add(new Adjunto("Comunicado.pdf",pdfBytes));
					adjuntos.add(new Adjunto("Instructivo.pdf",pdfBytes2));
							
					  //FileSystemResource file = new FileSystemResource("http://ae.edu.pe/pdf/Comunicado Clases Virtuales.pdf");
					//creamos un adjunto con el stream de datos
					//ByteArrayDataSource attachment = new ByteArrayDataSource(myStream, "application/pdf");
					//lo añadimos al correo a enviar
					//helper.addAttachment("nombreFichero.pdf", attachment);
			
					String html="<html lang='es'>";
					html +=" <head>";
					    html +=" <meta charset='UTF-8'>";
					    html +=" <meta name='viewport' content='width=device-width, initial-scale=1.0'>";
					    html +=" <title>Document</title>";
					html +=" </head>";
					html +=" <body>";
					html +=" <table";
					    html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;border-spacing:0px;width:100%;max-width:600px;margin:0 auto 0 auto;background:#072146'";
					    html +=" width='100%'>";
					    html +=" <tbody>";
					        html +=" <tr style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif'>";
					            html +=" <td style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px'>";
					            html +=" </td>";
					            html +=" <td style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;display:block;max-width:600px;margin:0 auto;clear:both'>";
					                html +=" <div style='font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;max-width:600px;margin:0 auto;display:block;padding:0px;background-color: #004481;'>";
					                    html +=" <table style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;border-spacing:0px;width:100%;'";
					                        html +=" width='100%'>";
					                        html +=" <tbody>";
					                            html +=" <tr";
					                                html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;background:#004481'>";
					                                html +=" <td style='margin:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;padding:0px;text-align:center'";
					                                    html +=" align='center'><img src='http://ae.edu.pe/logos/logoaemail.png'";
					                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;height:75px;max-width:600px'";
					                                        html +=" data-image-whitelisted='' height='75'></td>"; 
					                            html +=" </tr>";
					                        html +=" </tbody>";
					                    html +=" </table>";
					                html +=" </div>";
					            html +=" </td>";
					            html +=" <td style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px'>";
					            html +=" </td>";
					        html +=" </tr>";
					    html +=" </tbody>";
					html +=" </table>";
					html +=" <table";
					    html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;border-spacing:0px;width:100%'";
					    html +=" width='100%'>";
					    html +=" <tbody>";
					        html +=" <tr style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif'>";
					            html +=" <td";
					                html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;display:block;max-width:600px;margin:0 auto;clear:both'>";
					                html +=" <div";
					                    html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;max-width:600px;margin:0 auto;display:block'>";
					                    html +=" <table";
					                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;border-spacing:0px;width:100%'";
					                        html +=" width='100%'>";
					                        html +=" <tbody>";
					                            html +=" <tr";
					                                html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif'>";
					                                html +=" <td";
					                                    html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px'>";
					                                    html +=" <h3";
					                                        html +=" style='margin:0;padding:0;font-family:Arial,HelveticaNeue-Light,Helvetica Neue Light,Helvetica Neue,Helvetica,Lucida Grande,sans-serif;margin-bottom:15px;color:#121212;text-align:center;margin-top:40px;font-size:25px;font-weight:bold;font-style:normal;font-stretch:normal;letter-spacing:normal;line-height:1;padding-left:50px;padding-right:50px'>";
					                                        html +=" Saludos,"+row.getString("fam_ape_pat")+" "+row.getString("fam_ape_mat")+" "+row.getString("fam_nom")+"</h3>";
					                                    html +=" <p";
					                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-weight:normal;font-size:15px;line-height:24px;color:#121212;padding-left:50px;padding-right:50px;margin-bottom:0px;text-align:center'>";
					                                        html +=" Se adjunta comunicado e instructivo de acceso al aula virtual,";
					                                        html +=" recomendamos leerlos previamente. <br> <br> El usuario y password brindado es de uso exclusivo para el aula virtual.";
					                                      html +=" </p>";                                
					                                        html +=" <center><a  href='https://www.office.com' target='_blank'><img src='http://ae.edu.pe/logos/aula.png'"; 
					                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;height:75px;max-width:600px'";
					                                        html +=" data-image-whitelisted='' height='75' ></a></center>";
					                                    html +=" <p";
					                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;color:#121212;padding-left:50px;padding-right:50px;margin-bottom:0px;text-align:center;font-weight:bold'>";
					                                        html +=" USUARIO: "+row.getString("usuario")+"</p>";
					                                    html +=" <p";
					                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;color:#121212;padding-left:50px;padding-right:50px;margin-bottom:0px;text-align:center;font-weight:bold'>";
					                                        html +=" CONTRASEÑA: "+row.getString("pass_educando")+"</p>";
					                                        html +=" <br>"; 
					                                    html +=" <p";
					                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;color:#121212;padding-left:50px;padding-right:50px;margin-bottom:0px;text-align:center;font-weight:bold'>";
					                                        html +=" <a  href='https://www.office.com' target='_blank'>AULA VIRTUAL</a></p>";
					                                        html +=" <br>"; 
					                                        html +=" <p";
					                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;color:#ed1c24;padding-left:50px;padding-right:50px;margin-bottom:0px;text-align:center'>";
					                                        html +=" Atención: Este password es temporal, obligatoriamente lo cambiara, guarde su nuevo password.";
					                                    html +=" <hr";
					                                        html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;padding-left:50px;padding-right:50px;max-width:300px;margin:0 auto 0 auto;background-color:#d3d3d3;height:1px;border:0'>";
					                                    html +=" <div";
					                                        html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;text-align:center;padding-left:50px;padding-right:50px;display:block;max-width:600px;clear:both;margin:24px 0 24px 0'>";
					                                        html +=" <p";
					                                            html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;margin-bottom:10px;font-weight:bold;font-size:15px;line-height:24px;color:#121212;text-align:right;margin:0 0 4px 0>";
					                                            html +=" La Direcci贸n</p>";
					                                    html +=" </div>";
					                                    html +=" <table";
					                                        html +=" style='font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;border-spacing:0px;max-width:320px;margin:0 auto 0 auto;padding-left:50px;padding-right:50px;padding:16px 0 24px 0;width:100%'";
					                                        html +=" width='100%'>";
					                                    html +=" </body>";
					                                    html +=" </html>";
					////comentado x la apuranza x ahora correoUtil.enviar("Datos de Acceso " + row.getString("nom") +" " +row.getString("ape_pat"), "",row.getString("fam_corr"), html,adjuntos,"consultas@ae.edu.pe","ALBERT EINSTEIN");
					mensajeriaFamiliarDAO.actualizaEnviado(msj_existe.getInteger("id"),"1");//actualizo la fecha
				}
			} else{
				if(row.get("fam_corr")==null || row.get("fam_corr").equals("")){
					//mensajeriaFamiliarDAO.actualizaEnviado(msj_existe.getInteger("id"),"0");
					MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
					mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
					mensajeriaFamiliar.setId_per(8);
					mensajeriaFamiliar.setMsj("Datos de Acceso " + row.getString("nom") +" " +row.getString("ape_pat")+" "+row.getString("ape_mat"));
					mensajeriaFamiliar.setEst("A");
					mensajeriaFamiliar.setFlg_en("0");
					mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
					mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
					//return;
				} else{
					CorreoUtil correoUtil = new CorreoUtil();
					String pdfRuta =  "/home/aeedupeh/public_html/plantillas/Comunicado Clases Virtuales.pdf";
					//String pdfRuta =  "C:/plantillas/Comunicado Clases Virtuales.pdf";
					byte[] pdfBytes = FileUtil.filePathToByte(pdfRuta);
					
					String pdfRuta2 =  "/home/aeedupeh/public_html/plantillas/pasos para ingresar al teams padres.pdf";
					//String pdfRuta2 =  "C:/plantillas/pasos para ingresar al teams padres.pdf";
					byte[] pdfBytes2 = FileUtil.filePathToByte(pdfRuta2);
					
					List<Adjunto> adjuntos = new ArrayList<Adjunto>();
					adjuntos.add(new Adjunto("Comunicado.pdf",pdfBytes));
					adjuntos.add(new Adjunto("Instructivo.pdf",pdfBytes2));
							
					  //FileSystemResource file = new FileSystemResource("http://ae.edu.pe/pdf/Comunicado Clases Virtuales.pdf");
					//creamos un adjunto con el stream de datos
					//ByteArrayDataSource attachment = new ByteArrayDataSource(myStream, "application/pdf");
					//lo añadimos al correo a enviar
					//helper.addAttachment("nombreFichero.pdf", attachment);
			
					String html="<html lang='es'>";
					html +=" <head>";
					    html +=" <meta charset='UTF-8'>";
					    html +=" <meta name='viewport' content='width=device-width, initial-scale=1.0'>";
					    html +=" <title>Document</title>";
					html +=" </head>";
					html +=" <body>";
					html +=" <table";
					    html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;border-spacing:0px;width:100%;max-width:600px;margin:0 auto 0 auto;background:#072146'";
					    html +=" width='100%'>";
					    html +=" <tbody>";
					        html +=" <tr style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif'>";
					            html +=" <td style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px'>";
					            html +=" </td>";
					            html +=" <td style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;display:block;max-width:600px;margin:0 auto;clear:both'>";
					                html +=" <div style='font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;max-width:600px;margin:0 auto;display:block;padding:0px;background-color: #004481;'>";
					                    html +=" <table style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;border-spacing:0px;width:100%;'";
					                        html +=" width='100%'>";
					                        html +=" <tbody>";
					                            html +=" <tr";
					                                html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;background:#004481'>";
					                                html +=" <td style='margin:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;padding:0px;text-align:center'";
					                                    html +=" align='center'><img src='http://ae.edu.pe/logos/logoaemail.png'";
					                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;height:75px;max-width:600px'";
					                                        html +=" data-image-whitelisted='' height='75'></td>"; 
					                            html +=" </tr>";
					                        html +=" </tbody>";
					                    html +=" </table>";
					                html +=" </div>";
					            html +=" </td>";
					            html +=" <td style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px'>";
					            html +=" </td>";
					        html +=" </tr>";
					    html +=" </tbody>";
					html +=" </table>";
					html +=" <table";
					    html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;border-spacing:0px;width:100%'";
					    html +=" width='100%'>";
					    html +=" <tbody>";
					        html +=" <tr style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif'>";
					            html +=" <td";
					                html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;display:block;max-width:600px;margin:0 auto;clear:both'>";
					                html +=" <div";
					                    html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;max-width:600px;margin:0 auto;display:block'>";
					                    html +=" <table";
					                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;border-spacing:0px;width:100%'";
					                        html +=" width='100%'>";
					                        html +=" <tbody>";
					                            html +=" <tr";
					                                html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif'>";
					                                html +=" <td";
					                                    html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px'>";
					                                    html +=" <h3";
					                                        html +=" style='margin:0;padding:0;font-family:Arial,HelveticaNeue-Light,Helvetica Neue Light,Helvetica Neue,Helvetica,Lucida Grande,sans-serif;margin-bottom:15px;color:#121212;text-align:center;margin-top:40px;font-size:25px;font-weight:bold;font-style:normal;font-stretch:normal;letter-spacing:normal;line-height:1;padding-left:50px;padding-right:50px'>";
					                                        html +=" Saludos,"+row.getString("fam_ape_pat")+" "+row.getString("fam_ape_mat")+" "+row.getString("fam_nom")+"</h3>";
					                                    html +=" <p";
					                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-weight:normal;font-size:15px;line-height:24px;color:#121212;padding-left:50px;padding-right:50px;margin-bottom:0px;text-align:center'>";
					                                        html +=" Se adjunta comunicado e instructivo de acceso al aula virtual,";
					                                        html +=" recomendamos leerlos previamente. <br> <br> El usuario y password brindado es de uso exclusivo para el aula virtual.";
					                                      html +=" </p>";                                
					                                        html +=" <center><a  href='https://www.office.com' target='_blank'><img src='http://ae.edu.pe/logos/aula.png'"; 
					                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;height:75px;max-width:600px'";
					                                        html +=" data-image-whitelisted='' height='75' ></a></center>";
					                                    html +=" <p";
					                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;color:#121212;padding-left:50px;padding-right:50px;margin-bottom:0px;text-align:center;font-weight:bold'>";
					                                        html +=" USUARIO: "+row.getString("usuario")+"</p>";
					                                    html +=" <p";
					                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;color:#121212;padding-left:50px;padding-right:50px;margin-bottom:0px;text-align:center;font-weight:bold'>";
					                                        html +=" CONTRASEÑA: "+row.getString("pass_educando")+"</p>";
					                                        html +=" <br>"; 
					                                    html +=" <p";
					                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;color:#121212;padding-left:50px;padding-right:50px;margin-bottom:0px;text-align:center;font-weight:bold'>";
					                                        html +=" <a  href='https://www.office.com' target='_blank'>AULA VIRTUAL</a></p>";
					                                        html +=" <br>"; 
					                                        html +=" <p";
					                                        html +=" style='margin:0;padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;font-size:15px;line-height:24px;color:#ed1c24;padding-left:50px;padding-right:50px;margin-bottom:0px;text-align:center'>";
					                                        html +=" Atención: Este password es temporal, obligatoriamente lo cambiara, guarde su nuevo password.";
					                                    html +=" <hr";
					                                        html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;padding-left:50px;padding-right:50px;max-width:300px;margin:0 auto 0 auto;background-color:#d3d3d3;height:1px;border:0'>";
					                                    html +=" <div";
					                                        html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;text-align:center;padding-left:50px;padding-right:50px;display:block;max-width:600px;clear:both;margin:24px 0 24px 0'>";
					                                        html +=" <p";
					                                            html +=" style='padding:0;font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;margin-bottom:10px;font-weight:bold;font-size:15px;line-height:24px;color:#121212;text-align:right;margin:0 0 4px 0>";
					                                            html +=" La Direcci贸n</p>";
					                                    html +=" </div>";
					                                    html +=" <table";
					                                        html +=" style='font-family:Arial,Helvetica Neue,Helvetica,Helvetica,sans-serif;border-spacing:0px;max-width:320px;margin:0 auto 0 auto;padding-left:50px;padding-right:50px;padding:16px 0 24px 0;width:100%'";
					                                        html +=" width='100%'>";
					                                    html +=" </body>";
					                                    html +=" </html>";
					//comentado x la apuranza x ahora correoUtil.enviar("Datos de Acceso " + row.getString("nom") +" " +row.getString("ape_pat"), "",row.getString("fam_corr"), html,adjuntos,"consultas@ae.edu.pe","ALBERT EINSTEIN");
					//correoUtil.enviar("Usuario " + "LINA LEON" , "", inscripcion_ind.getCorr(), html,config.getNom()+".pdf",pdf_bytes,config.getCorr_envio(),nom_colegio_organizador);
					MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
					mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
					mensajeriaFamiliar.setId_per(8);
					mensajeriaFamiliar.setMsj("Datos de Acceso " + row.getString("nom") +" " +row.getString("ape_pat")+" "+row.getString("ape_mat"));
					mensajeriaFamiliar.setEst("A");
					mensajeriaFamiliar.setFlg_en("1");
					mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
					mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
			}
			}
		}
	}
	
	@RequestMapping( value="/enviarMensajexMatriculaColegio/{num_cont}", method = RequestMethod.POST)
	public void enviarMensajeMatriculaxAlumnoColegio(@PathVariable String num_cont)throws Exception{
		//Obtener a los hijos pre matriculados
		List<Row> hijos = matriculaDAO.matriculadosxContratoparaWeb(num_cont);
		for (Row row : hijos) {
			//Datos de la Matrícula
			Row matricula = matriculaDAO.obtenerDatosMatriculaColegio(row.getInteger("id_mat"));			
			String grado = matricula.getString("grado");
			String local=matricula.getString("sucursal");
			String nivel=matricula.getString("nivel");
			String alumno = matricula.getString("ape_pat")+" "+matricula.getString("ape_mat")+" "+matricula.getString("nom");
			
						CorreoUtil correoUtil = new CorreoUtil();
						//String pdfRuta =  "http://login.ae.edu.pe:8080/documentos/tycAcademiaVacaciones.pdf";
						String pdfRuta =  "/opt/tomcat/webapps/documentos/Costos.pdf";
						//String pdfRuta =  "C:/plantillas/Costos.pdf";
						//String pdfRuta =  null;
						byte[] pdfBytes = FileUtil.filePathToByte(pdfRuta);
						
						String pdfRuta2 =  "/opt/tomcat/webapps/documentos/PlandeEstudios2021.pdf";
						//String pdfRuta2 =  "C:/plantillas/PlandeEstudios2021.pdf";
						byte[] pdfBytes2 = FileUtil.filePathToByte(pdfRuta2);
						
						String pdfRuta3 =  "/opt/tomcat/webapps/documentos/ReglamentoInterno2021.pdf";
						//String pdfRuta3 =  "C:/plantillas/ReglamentoInterno2021.pdf";
						byte[] pdfBytes3 = FileUtil.filePathToByte(pdfRuta3);
						
						String pdfRuta4 =  "/opt/tomcat/webapps/documentos/ContratoColegioAE2021.pdf";
						//String pdfRuta4 =  "C:/plantillas/ContratoColegioAE2021.pdf";
						byte[] pdfBytes4 = FileUtil.filePathToByte(pdfRuta4);
						
						List<Adjunto> adjuntos = new ArrayList<Adjunto>();
						adjuntos.add(new Adjunto("Costos.pdf",pdfBytes));
						adjuntos.add(new Adjunto("Plan de Estudios 2021.pdf",pdfBytes2));
						adjuntos.add(new Adjunto("Reglamento Interno 2021.pdf",pdfBytes3));
						adjuntos.add(new Adjunto("Contrato Colegio AE 2021.pdf",pdfBytes4));
						//adjuntos.add(new Adjunto("Instructivo.pdf",pdfBytes2));
						String html ="<head>\r\n" + 
								"  <meta charset=\"UTF-8\">\r\n" + 
								"  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" + 
								"  <title>Email Matrícula\r\n" + 
								"\r\n" + 
								"  </title>\r\n" + 
								"</head>\r\n" + 
								"\r\n" + 
								"<body>\r\n" + 
								"\r\n" + 
								"  <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\">\r\n" + 
								"    <tbody>\r\n" + 
								"      <tr>\r\n" + 
								"        <td align=\"center\" valign=\"top\">\r\n" + 
								"\r\n" + 
								"        </td>\r\n" + 
								"      </tr>\r\n" + 
								"      <tr>\r\n" + 
								"        <td align=\"center\">\r\n" + 
								"          <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"550\" class=\"m_-2564031024094939495container\"\r\n" + 
								"            align=\"center\">\r\n" + 
								"            <tbody>\r\n" + 
								"              <tr>\r\n" + 
								"                <td>\r\n" + 
								"                  <table style=\"background-color:#ffffff\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\"\r\n" + 
								"                    width=\"100%\">\r\n" + 
								"                    <tbody>\r\n" + 
								"                      <tr>\r\n" + 
								"                        <td align=\"center\" valign=\"top\">\r\n" + 
								"                          <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + 
								"                            <tbody>\r\n" + 
								"                              <tr>\r\n" + 
								"\r\n" + 
								"                                <td>\r\n" + 
								"\r\n" + 
								"                                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + 
								"                                    <tbody>\r\n" + 
								"                                      <tr>\r\n" + 
								"                                        <td align=\"center\" class=\"m_-2564031024094939495header\" valign=\"top\">\r\n" + 
								"                                          <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + 
								"                                            <tbody>\r\n" + 
								"                                              <tr>\r\n" + 
								"                                                <td align=\"left\" valign=\"top\">\r\n" + 
								"                                                  <table cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">\r\n" + 
								"                                                    <tbody>\r\n" + 
								"                                                      <tr>\r\n" + 
								"                                                        <td class=\"m_-2564031024094939495responsive-td\" valign=\"top\"\r\n" + 
								"                                                          style=\"width:100%\">\r\n" + 
								"                                                          <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\r\n" + 
								"                                                            style=\"min-width:100%\">\r\n" + 
								"                                                            <tbody>\r\n" + 
								"                                                              <tr>\r\n" + 
								"                                                                <td>\r\n" + 
								"                                                                  <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\r\n" + 
								"                                                                    <tbody>\r\n" + 
								"                                                                      <tr>\r\n" + 
								"                                                                        <td aling=\"center\"><img\r\n" + 
								"                                                                            src=\"http://ae.edu.pe/email/matricula-2021.png\"\r\n" + 
								"                                                                            style=\"display:block;padding:0px;text-align:center;height:auto;width:100%;border:0px\"\r\n" + 
								"                                                                            width=\"600\" class=\"CToWUd\"></td>\r\n" + 
								"                                                                      </tr>\r\n" + 
								"                                                                    </tbody>\r\n" + 
								"                                                                  </table>\r\n" + 
								"                                                                </td>\r\n" + 
								"                                                              </tr>\r\n" + 
								"                                                            </tbody>\r\n" + 
								"                                                          </table>\r\n" + 
								"\r\n" + 
								"                                                        </td>\r\n" + 
								"                                                      </tr>\r\n" + 
								"                                                    </tbody>\r\n" + 
								"                                                  </table>\r\n" + 
								"                                                </td>\r\n" + 
								"                                              </tr>\r\n" + 
								"                                            </tbody>\r\n" + 
								"                                          </table>\r\n" + 
								"                                        </td>\r\n" + 
								"                                      </tr>\r\n" + 
								"                                      <!-- <tr>\r\n" + 
								"                                <td align=\"center\" class=\"m_-2564031024094939495header\" valign=\"top\">\r\n" + 
								"                                  <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + 
								"                                    <tbody>\r\n" + 
								"                                    <tr>\r\n" + 
								"                                      <td align=\"left\" valign=\"top\">\r\n" + 
								"                                        <table cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">\r\n" + 
								"                                          <tbody>\r\n" + 
								"                                          <tr>\r\n" + 
								"                                            <td class=\"m_-2564031024094939495responsive-td\" valign=\"top\" style=\"width:100%\">\r\n" + 
								"                                              <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"min-width:100%\"><tbody><tr><td><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tbody><tr><td align=\"center\"><img src=\"https://ci3.googleusercontent.com/proxy/1yQGXaz1dcLtDOK4Bl5Tq3LKxU7XKIc-LcLRmhb2m4bqiaVKUkwiHl35EDAP1OM-Fp0RWgKlVxlV_wuk12dMP4jN1Wgd6_BvgY4D4bErRWO3YLRIQdD9nxa6oc19ulPq_3bn_RKYm4hmgCTT7wejC3DJnlLHjA=s0-d-e1-ft#https://image.mail.bbva.pe/lib/fe4315707564047f701771/m/12/766c20ce-5a00-4fae-8e1c-e2fbcf909fc1.png\" style=\"display:block;padding:0px;text-align:center;height:auto;width:100%\" width=\"600\" class=\"CToWUd a6T\" tabindex=\"0\"><div class=\"a6S\" dir=\"ltr\" style=\"opacity: 0.01; left: 801px; top: 335px;\"><div id=\":4xt\" class=\"T-I J-J5-Ji aQv T-I-ax7 L3 a5q\" role=\"button\" tabindex=\"0\" aria-label=\"Descargar el archivo adjunto \" data-tooltip-class=\"a1V\" data-tooltip=\"Descargar\"><div class=\"wkMEBb\"><div class=\"aSK J-J5-Ji aYr\"></div></div></div></div></td></tr></tbody></table></td></tr></tbody></table>\r\n" + 
								"                                              \r\n" + 
								"                                            </td>\r\n" + 
								"                                          </tr>\r\n" + 
								"                                          </tbody>\r\n" + 
								"                                        </table>\r\n" + 
								"                                      </td>\r\n" + 
								"                                    </tr>\r\n" + 
								"                                    </tbody>\r\n" + 
								"                                  </table>\r\n" + 
								"                                </td>\r\n" + 
								"                              </tr> -->\r\n" + 
								"                                      <tr>\r\n" + 
								"                                        <td align=\"center\" class=\"m_-2564031024094939495header\" valign=\"top\">\r\n" + 
								"                                          <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + 
								"                                            <tbody>\r\n" + 
								"                                              <tr>\r\n" + 
								"                                                <td align=\"left\" valign=\"top\">\r\n" + 
								"                                                  <table cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">\r\n" + 
								"                                                    <tbody>\r\n" + 
								"                                                      <tr>\r\n" + 
								"                                                        <td class=\"m_-2564031024094939495responsive-td\" valign=\"top\"\r\n" + 
								"                                                          style=\"width:100%\">\r\n" + 
								"                                                          <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\r\n" + 
								"                                                            style=\"background-color:#f4f4f4;min-width:100%\">\r\n" + 
								"                                                            <tbody>\r\n" + 
								"                                                              <tr>\r\n" + 
								"                                                                <td style=\"padding:30px\"><b><span\r\n" + 
								"                                                                      style=\"font-size:17px\"><span\r\n" + 
								"                                                                        style=\"font-family:Arial,Helvetica,sans-serif\">ESTIMADO\r\n" + 
								"                                                                        PADRE O MADRE DE FAMILIA</span></span></b>\r\n" + 
								"\r\n" + 
								"\r\n" + 
								"\r\n" + 
								"\r\n" + 
								"                                                                        <br>\r\n" + 
								"                                                                        &nbsp;<div\r\n" + 
								"                                                                          style=\"text-align:left\">\r\n" + 
								"                                                                          <span\r\n" + 
								"                                                                            style=\"font-family:Arial,Helvetica,sans-serif\"><span\r\n" + 
								"                                                                              style=\"font-size:15px\">Reciba un cordial saludo a nombre del Colegio Albert Einstein, ha realizado satisfactoriamente la prematricula 2021.\r\n" + 
								"                                                                             </span></span>\r\n" + 
								"\r\n" + 
								"                                                                        </div>\r\n" + 
								"\r\n" + 
								"                                                      \r\n" + 
								"                                                                </td>\r\n" + 
								"                                                              </tr>\r\n" + 
								"                                                            </tbody>\r\n" + 
								"                                                          </table>\r\n" + 
								"\r\n" + 
								"                                                        </td>\r\n" + 
								"                                                      </tr>\r\n" + 
								"                                                    </tbody>\r\n" + 
								"                                                  </table>\r\n" + 
								"                                                </td>\r\n" + 
								"                                              </tr>\r\n" + 
								"                                            </tbody>\r\n" + 
								"                                          </table>\r\n" + 
								"                                        </td>\r\n" + 
								"                                      </tr>\r\n" + 
								"                                      <tr>\r\n" + 
								"\r\n" + 
								"                                      </tr>\r\n" + 
								"                                      <tr>\r\n" + 
								"                                        <td align=\"center\" class=\"m_-2564031024094939495header\" valign=\"top\">\r\n" + 
								"                                          <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + 
								"                                            <tbody>\r\n" + 
								"                                              <tr>\r\n" + 
								"                                                <td align=\"left\" valign=\"top\">\r\n" + 
								"                                                  <table cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">\r\n" + 
								"                                                    <tbody>\r\n" + 
								"                                                      <tr>\r\n" + 
								"                                                        <td class=\"m_-2564031024094939495responsive-td\" valign=\"top\"\r\n" + 
								"                                                          style=\"width:100%\">\r\n" + 
								"                                                          <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\r\n" + 
								"                                                            style=\"background-color:#f4f4f4;min-width:100%\">\r\n" + 
								"                                                            <tbody>\r\n" + 
								"                                                              <tr>\r\n" + 
								"                                                                <td style=\"padding:0px 10px 10px\">\r\n" + 
								"                                                                  <table cellspacing=\"0\" cellpadding=\"0\"\r\n" + 
								"                                                                    style=\"width:100%\">\r\n" + 
								"                                                                    <tbody>\r\n" + 
								"                                                                      <tr>\r\n" + 
								"                                                                        <td>\r\n" + 
								"                                                                          <table cellspacing=\"0\" cellpadding=\"0\"\r\n" + 
								"                                                                            dir=\"rtl\" style=\"width:100%\">\r\n" + 
								"                                                                            <tbody>\r\n" + 
								"                                                                              <tr>\r\n" + 
								"                                                                                <td valign=\"top\"\r\n" + 
								"                                                                                  class=\"m_-2564031024094939495responsive-td\"\r\n" + 
								"                                                                                  dir=\"ltr\"\r\n" + 
								"                                                                                  style=\"width:70%;padding-left:0px\">\r\n" + 
								"                                                                                  <table cellpadding=\"0\" cellspacing=\"0\"\r\n" + 
								"                                                                                    width=\"100%\"\r\n" + 
								"                                                                                    style=\"background-color:transparent;min-width:100%\">\r\n" + 
								"                                                                                    <tbody>\r\n" + 
								"                                                                                      <tr>\r\n" + 
								"                                                                                        <td\r\n" + 
								"                                                                                          style=\"padding:0px 30px 15px 15px\">\r\n" + 
								"                                                                                          <span\r\n" + 
								"                                                                                            style=\"font-family:Arial,Helvetica,sans-serif\"><span\r\n" + 
								"                                                                                              style=\"font-size:17px\">\r\n" + 
								"                                                                                              <b>Nombre del Alumno:</b> "+alumno+"\r\n" + 
								"                                                                                              <br>\r\n" + 
								"                                                                                              <b> Local:</b>"+local+"<br>\r\n" + 
								"                                                                                              <b> Nivel:</b>\r\n" + 
								"                                                                                              "+nivel+"<br>\r\n" + 
								"                                                                                              <b> Grado:</b>\r\n" + 
								"                                                                                              "+grado+"<br>\r\n" + 
								"                                                                                        \r\n" + 
								"\r\n" + 
								"\r\n" + 
								"                                                                                            </span></span><br>\r\n" + 
								"                                                                                          &nbsp;<div\r\n" + 
								"                                                                                            style=\"text-align:justify\">\r\n" + 
								"                                                                                            <span\r\n" + 
								"                                                                                              style=\"font-family:Arial,Helvetica,sans-serif\"><span\r\n" + 
								"                                                                                                style=\"font-size:15px\">\r\n" + 
								"                                                                                                El siguiente e importante paso en el proceso de matrícula, es enviar por email adjuntando el Contrato de servicios correctamente llenado y una copia del DNI del apoderado.\r\n" + 
								"                                                                                                Estos documentos deben ser enviados a <a href=\"mailto: soporte@colegioae.freshdesk.com\">soporte@colegioae.freshdesk.com</a> \r\n" + 
								"                                                                                                \r\n" + 
								"                                                                                                \r\n" + 
								"                                                                                                \r\n" + 
								"                                                                                               \r\n" + 
								"                                                                                                </span></span>\r\n" + 
								"                                                                                            <br>\r\n" + 
								"                                                                                            <br>\r\n" + 
								"                                                                                          </div>\r\n" + 
								"\r\n" + 
								"\r\n" + 
								"\r\n" + 
								"\r\n" + 
								"\r\n" + 
								"                                                                                          <div\r\n" + 
								"                                                                          style=\"text-align:left\">\r\n" + 
								"                                                                          <span\r\n" + 
								"                                                                            style=\"font-family:Arial,Helvetica,sans-serif\"><span\r\n" + 
								"                                                                              style=\"font-size:15px\">Luego de la validación, nos comunicaremos con Ud. vía email informándole la fecha en la que se podrá acercar al Banco Continental para realizar el pago de derecho de matricula mediante el servicio de recaudo.\r\n" + 
								"                                                                             </span></span>\r\n" + 
								"\r\n" + 
								"                                                                        </div>\r\n" + 
								"\r\n" + 
								"<br>\r\n" + 
								"                                                                        <div\r\n" + 
								"                                                                          style=\"text-align:left\">\r\n" + 
								"                                                                          <span\r\n" + 
								"                                                                            style=\"font-family:Arial,Helvetica,sans-serif\"><span\r\n" + 
								"                                                                              style=\"font-size:15px\"> <b>PD: La prematricula no puede ser considerada como una matrícula, el proceso de matricula finaliza cuando el padre de familia hace el abono por el derecho de matrícula 2021.</b>\r\n" + 
								"                                                                             </span></span>\r\n" + 
								"\r\n" + 
								"                                                                        </div>\r\n" + 
								"                                                                        \r\n" + 
								"                                                                                          <br>\r\n" + 
								"                                                                                          <br>      <div\r\n" + 
								"                                                                                          style=\"text-align:right\">\r\n" + 
								"                                                                                          <span\r\n" + 
								"                                                                                            style=\"font-family:Arial,Helvetica,sans-serif\"><span\r\n" + 
								"                                                                                              style=\"font-size:15px\">\r\n" + 
								"                                                                                              <b>LA DIRECCIÓN</b></span></span>\r\n" + 
								"                                                                                             \r\n" + 
								"                                                                                              \r\n" + 
								"                                                                                          <br>\r\n" + 
								"                                                                                          <br>\r\n" + 
								"                                                                                        </div>  \r\n" + 
								"\r\n" + 
								"\r\n" + 
								"                                                                                      \r\n" + 
								"                                                                                        </td>\r\n" + 
								"\r\n" + 
								"\r\n" + 
								"\r\n" + 
								"                                                                                      </tr>\r\n" + 
								"                                                                                    </tbody>\r\n" + 
								"                                                                                  </table>\r\n" + 
								"                                                                                </td>\r\n" + 
								"\r\n" + 
								"                                                                              </tr>\r\n" + 
								"                                                                            </tbody>\r\n" + 
								"                                                                          </table>\r\n" + 
								"                                                                        </td>\r\n" + 
								"                                                                      </tr>\r\n" + 
								"                                                                    </tbody>\r\n" + 
								"                                                                  </table>\r\n" + 
								"                                                                </td>\r\n" + 
								"                                                              </tr>\r\n" + 
								"                                                            </tbody>\r\n" + 
								"                                                          </table>\r\n" + 
								"\r\n" + 
								"                                                        </td>\r\n" + 
								"                                                      </tr>\r\n" + 
								"                                                    </tbody>\r\n" + 
								"                                                  </table>\r\n" + 
								"                                                </td>\r\n" + 
								"                                              </tr>\r\n" + 
								"                                            </tbody>\r\n" + 
								"                                          </table>\r\n" + 
								"                                        </td>\r\n" + 
								"                                      </tr>\r\n" + 
								"                                      <tr>\r\n" + 
								"                                        <td align=\"center\" class=\"m_-2564031024094939495header\" valign=\"top\">\r\n" + 
								"\r\n" + 
								"                                        </td>\r\n" + 
								"                                      </tr>\r\n" + 
								"                                      <tr>\r\n" + 
								"                                        <td align=\"center\" class=\"m_-2564031024094939495header\" valign=\"top\">\r\n" + 
								"                                          <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + 
								"                                            <tbody>\r\n" + 
								"                                              <tr>\r\n" + 
								"                                                <td align=\"left\" valign=\"top\">\r\n" + 
								"\r\n" + 
								"                                                </td>\r\n" + 
								"                                              </tr>\r\n" + 
								"                                            </tbody>\r\n" + 
								"                                          </table>\r\n" + 
								"                                        </td>\r\n" + 
								"                                      </tr>\r\n" + 
								"\r\n" + 
								"\r\n" + 
								"\r\n" + 
								"\r\n" + 
								"                                    </tbody>\r\n" + 
								"                                  </table>\r\n" + 
								"                                </td>\r\n" + 
								"                              </tr>\r\n" + 
								"                            </tbody>\r\n" + 
								"                          </table>\r\n" + 
								"                        </td>\r\n" + 
								"                      </tr>\r\n" + 
								"                    </tbody>\r\n" + 
								"                  </table>\r\n" + 
								"                </td>\r\n" + 
								"              </tr>\r\n" + 
								"            </tbody>\r\n" + 
								"          </table>\r\n" + 
								"        </td>\r\n" + 
								"      </tr>\r\n" + 
								"\r\n" + 
								"    </tbody>\r\n" + 
								"  </table>\r\n" + 
								"</body>\r\n" + 
								"\r\n" + 
								"</html>"	;		
						//matricula.getString("corr")
						//correoUtil.enviarVarios("Registro de Pre-Matrícula - " + datos_alu.getString("nom") +" " +datos_alu.getString("ape_pat"), "",new String[]{row.getString("fam_corr"),"maricris1906@hotmail.com"}, html,adjuntos,"consultas@ae.edu.pe","ALBERT EINSTEIN");
						//Verfico la cantidad de mensajes enviados
						Contador contador = contadorDAO.get(1);
						Integer cant_msj_env=contador.getNro();
						SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
						String format = formatter.format(new Date());
						String format2 = formatter.format(contador.getFec());
						int fecActual = Integer.parseInt(format);
						int fecContador=Integer.parseInt(format2);
						if(cant_msj_env<=500 && fecActual==fecContador) {
							correoUtil.enviar("Registro de Pre-Matrícula - " + matricula.getString("nom") +" " +matricula.getString("ape_pat"), "", matricula.getString("corr"), html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
							MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
							mensajeriaFamiliar.setId_des(matricula.getInteger("id_fam"));
							mensajeriaFamiliar.setId_per(8);
							mensajeriaFamiliar.setMsj("Registro de Pre-Matrícula " + matricula.getString("nom") +" " +matricula.getString("ape_pat")+" "+matricula.getString("ape_mat"));
							mensajeriaFamiliar.setEst("A");
							mensajeriaFamiliar.setFlg_en("1");
							mensajeriaFamiliar.setId_alu(matricula.getInteger("id_alu"));
							mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
							mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
							//actualizo el contador
							cant_msj_env = cant_msj_env + 1;
							//si es 500 cambio el usuario 
							if(cant_msj_env.equals(500)) {
								//actualizo
								contadorDAO.actualizarUsuarioContador("noreply@ae.edu.pe");
							}
							contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
						} else if((cant_msj_env>500 && cant_msj_env<=1000) && fecActual==fecContador){ // matricula.getString("corr")
							correoUtil.enviar("Registro de Pre-Matrícula - " + matricula.getString("nom") +" " +matricula.getString("ape_pat"), "", matricula.getString("corr"), html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
							MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
							mensajeriaFamiliar.setId_des(matricula.getInteger("id_fam"));
							mensajeriaFamiliar.setId_per(8);
							mensajeriaFamiliar.setMsj("Registro de Pre-Matrícula " + matricula.getString("nom") +" " +matricula.getString("ape_pat")+" "+matricula.getString("ape_mat"));
							mensajeriaFamiliar.setEst("A");
							mensajeriaFamiliar.setFlg_en("1");
							mensajeriaFamiliar.setId_alu(matricula.getInteger("id_alu"));
							mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
							mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
							//actualizo el contador
							cant_msj_env = cant_msj_env + 1;
							//si es 500 cambio el usuario 
							if(cant_msj_env.equals(1000)) {
								//actualizo
								contadorDAO.actualizarUsuarioContador("noreply2@ae.edu.pe");
							}
							contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
						} else if((cant_msj_env>1000 && cant_msj_env<=1500) && fecActual==fecContador){ // matricula.getString("corr")
							correoUtil.enviar("Registro de Pre-Matrícula - " + matricula.getString("nom") +" " +matricula.getString("ape_pat"), "", matricula.getString("corr"), html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
							MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
							mensajeriaFamiliar.setId_des(matricula.getInteger("id_fam"));
							mensajeriaFamiliar.setId_per(8);
							mensajeriaFamiliar.setMsj("Registro de Pre-Matrícula " + matricula.getString("nom") +" " +matricula.getString("ape_pat")+" "+matricula.getString("ape_mat"));
							mensajeriaFamiliar.setEst("A");
							mensajeriaFamiliar.setFlg_en("1");
							mensajeriaFamiliar.setId_alu(matricula.getInteger("id_alu"));
							mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
							mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
							//actualizo el contador
							cant_msj_env = cant_msj_env + 1;
							//si es 500 cambio el usuario 
							if(cant_msj_env.equals(1500)){
								//actualizo
								contadorDAO.actualizarUsuarioContador("noreply3@ae.edu.pe");
							}
							contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
						} else if((cant_msj_env>1500 && cant_msj_env<=2000) && fecActual==fecContador){ // matricula.getString("corr")
							correoUtil.enviar("Registro de Pre-Matrícula - " + matricula.getString("nom") +" " +matricula.getString("ape_pat"), "", matricula.getString("corr"), html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
							MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
							mensajeriaFamiliar.setId_des(matricula.getInteger("id_fam"));
							mensajeriaFamiliar.setId_per(8);
							mensajeriaFamiliar.setMsj("Registro de Pre-Matrícula " + matricula.getString("nom") +" " +matricula.getString("ape_pat")+" "+matricula.getString("ape_mat"));
							mensajeriaFamiliar.setEst("A");
							mensajeriaFamiliar.setFlg_en("1");
							mensajeriaFamiliar.setId_alu(matricula.getInteger("id_alu"));
							mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
							mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
							//actualizo el contador
							cant_msj_env = cant_msj_env + 1;
							//si es 500 cambio el usuario 
							if(cant_msj_env.equals(2000)){
								//actualizo
								contadorDAO.actualizarUsuarioContador("noreply4@ae.edu.pe");
							}
							contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
						} else if((cant_msj_env>2000) && fecActual==fecContador){
							throw new Exception("Se ha excedido el envío de mensajes, por favor comuníquese con el administrador.");
						} else if(fecActual!=fecContador){
							//Actualizo la fecha del contador, Nuevo dia
							contadorDAO.actualizarFechaContador(new Date());
							contadorDAO.actualizarUsuarioContador("noreply@ae.edu.pe");
							Contador contador2= contadorDAO.get(1);
							correoUtil.enviar("Registro de Pre-Matrícula - " + matricula.getString("nom") +" " +matricula.getString("ape_pat"), "", matricula.getString("corr"), html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador2.getUsr(), contador2.getPsw());
							MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
							mensajeriaFamiliar.setId_des(matricula.getInteger("id_fam"));
							mensajeriaFamiliar.setId_per(8);
							mensajeriaFamiliar.setMsj("Registro de Pre-Matrícula " + matricula.getString("nom") +" " +matricula.getString("ape_pat")+" "+matricula.getString("ape_mat"));
							mensajeriaFamiliar.setEst("A");
							mensajeriaFamiliar.setFlg_en("1");
							mensajeriaFamiliar.setId_alu(matricula.getInteger("id_alu"));
							mensajeriaFamiliar.setUsr_rmt(contador2.getUsr());
							mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
							//actualizo el contador
							cant_msj_env = cant_msj_env + 1;
							//si es 500 cambio el usuario 
							contadorDAO.actualizarContador(cant_msj_env, contador2.getFec());
						}
						
						
		}
		
				
	}
	
	@RequestMapping( value="/enviarMensajexMatricula/{id_mat}/{id_gpf}/{id_anio}", method = RequestMethod.POST)
	public void enviarMensajeMatriculaxAlumno(@PathVariable Integer id_mat, @PathVariable Integer id_gpf,@PathVariable Integer id_anio)throws Exception{

		//Obtener la lista de familiares 
		List<Row> familiares=familiarDAO.listarFamiliares(id_gpf);
		//Datos de la Matrícula
		Row matricula = matriculaDAO.obtenerDatosMatriculaAcadVac(id_mat);
		String giro="";
		if(matricula.getString("tipo").equals("A")) {
			giro = "Academia Encinas";
		} else if(matricula.getString("tipo").equals("V")) {
			giro = "Vacaciones Útiles";
		}	
		Alumno alumno = alumnoDAO.get(matricula.getInt("id_alu"));
		Row datos_alu=alumnoDAO.datosAlumnoxCodigo(alumno.getCod(), id_anio);
		List<AcademicoPago> academicoPagos=academicoPagoDAO.listByParams(new Param("id_mat",id_mat),new String[] { "nro_cuota" });
		
		Integer nro_cuota=academicoPagos.size();
		
		//Pago de la primera cuota
		BigDecimal monto_cuota1=new BigDecimal(0);
		Date fec_ven=new Date();
		String dia ="";
		String mes ="";
		String anio = "";
		String banco_nombre="";
		String cuenta="";
		
		for (AcademicoPago academicoPago : academicoPagos) {
			if(academicoPago.getNro_cuota()==1) {
				monto_cuota1=academicoPago.getMontoTotal();
				fec_ven=academicoPago.getFec_venc();
				Date fechaConcreta = new SimpleDateFormat("dd/MM/yyyy").parse(FechaUtil.toString(fec_ven));
			    dia = new SimpleDateFormat("dd").format(fechaConcreta);
			    mes = new SimpleDateFormat("MMMM").format(fechaConcreta);
			    anio = new SimpleDateFormat("yyyy").format(fechaConcreta);
			    Banco banco = bancoDAO.get(academicoPago.getId_bco_pag());
			    banco_nombre = banco.getNom();
			    cuenta = banco.getNro_cta();
			}	
			
		}
		
		String nivel=matricula.getString("nivel");
		String grado = matricula.getString("grado");
		
		for (Row row : familiares) {
			
					CorreoUtil correoUtil = new CorreoUtil();
					//String pdfRuta =  "http://login.ae.edu.pe:8080/documentos/tycAcademiaVacaciones.pdf";
					//String pdfRuta =  "D:/AE/tycAcademiaVacaciones.pdf";
					String pdfRuta =  "C:/Documentos/AN_Contrato_Servicios_Virtuales.pdf";
					byte[] pdfBytes = FileUtil.filePathToByte(pdfRuta);
					
					/*String pdfRuta2 =  "/home/aeedupeh/public_html/plantillas/pasos para ingresar al teams padres.pdf";
					//String pdfRuta2 =  "C:/plantillas/pasos para ingresar al teams padres.pdf";
					byte[] pdfBytes2 = FileUtil.filePathToByte(pdfRuta2);*/
					
					List<Adjunto> adjuntos = new ArrayList<Adjunto>();
					adjuntos.add(new Adjunto("Comunicado.pdf",pdfBytes));
					
					//adjuntos.add(new Adjunto("Instructivo.pdf",pdfBytes2));
					String html ="<html lang=\"en\">\n" + 
							"\n" + 
							"<head>\n" + 
							"  <meta charset=\"UTF-8\">\n" + 
							"  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" + 
							"  <title>Document</title>\n" + 
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
							"                                                                        <td aling=\"center\"><img src=\"http://ae.edu.pe/email/cabecera.png\"\n" + 
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
							"                                                                        style=\"font-family:Arial,Helvetica,sans-serif\">Estimado@\n" + row.getString("ape_pat")+" "+row.getString("ape_mat")+" "+row.getString("nom")+
							"                                                                        </span></span></b>\n" + 
							"                                                                  <div style=\"line-height:150%;text-align:justify\">\n" + 
							"                                                                    <br>\n" + 
							"                                                                    <span style=\"font-size:15px\"><span\n" + 
							"                                                                        style=\"font-family:Arial,Helvetica,sans-serif\">Ud\n" + 
							"                                                                        ha realizado una prematricula a: "+giro+"\n" + 
							"                                                                        Útiles 2021\n" + 
							"                                                                        <br>\n" + 
							"                                                                  </div>\n" + 
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
							"                                       \n" + 
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
							"                                                                                        <td\n" + 
							"                                                                                          style=\"padding:0px 30px 15px 15px\">\n" + 
							"                                                                                          <span\n" + 
							"                                                                                            style=\"font-family:Arial,Helvetica,sans-serif\"><span\n" + 
							"                                                                                              style=\"font-size:17px\">\n" + 
							"                                                                                                <b>Servicio:</b> "+giro+"<br>\n" + 
							"                                                                                               <b> Nivel:</b> "+nivel+"<br>\n" + 
							"                                                                                               <b> Aula/Grado:</b> "+grado+"<br>\n" + 
							"                                                                                               <b> Modalidad de Pago:</b> "+nro_cuota+" Cuota<br>\n" + 
							"                                                                                               <b> bMonto a Pagar:</b> S/ "+monto_cuota1+"\n" + 
							"                                                                                                \n" + 
							"                                                                                              \n" + 
							"                                                                                              </span></span><br>\n" + 
							"                                                                                          &nbsp;<div\n" + 
							"                                                                                            style=\"text-align:justify\">\n" + 
							"                                                                                            <span\n" + 
							"                                                                                              style=\"font-family:Arial,Helvetica,sans-serif\"><span\n" + 
							"                                                                                                style=\"font-size:15px\">EL plazo para formalizar la matrícula vence el "+fec_ven+", puede realizarlo en el <b>BANCO</b> "+banco_nombre+" al número de cuenta: "+cuenta+" CCI: XXXXXXXXX a nombre de <b>ASOCIACIÓN EDUCATIVA LUZ Y CIENCIA</b> o en nuestra secretaría Jr. Huaylas 245 en el Horario de 8:00am a 3:00pm.</span></span>\n" + 
							"                                                                                                <br>\n" + 
							"                                                                                                <br>\n" + 
							"                                                                                              </div>\n" + 
							"                                                                                               \n" + 
							"                                                                                                  <br>\n" + 
							"                                                                                                  <br>\n" + 
							"                                                                                                 \n" + 
							"                                                                                         \n" + 
							"                                                                                                          <div style=\"text-align:center\">\n" + 
							"                                                                                                  <span\n" + 
							"                                                                                                  style=\"font-family:Arial,Helvetica,sans-serif\"><span\n" + 
							"                                                                                                    style=\"font-size:15px\"><b><i>Si tuviera alguna consulta nos puede escribir a:</b></i> \n" + 
							"                                                                                                    <a href=\"mailto: soporte@colegioae.freshdesk.com\">soporte@colegioae.freshdesk.com</a> o a los teléfonos:<br> 943-861219, 954-101793.</span></span>\n" + 
							"                                                                                                  </div>\n" + 
							"                                                                                        </td>\n" + 
							"\n" + 
							"\n" + 
							"                                                                                        \n" + 
							"                                                                                      </tr>\n" + 
							"                                                                                    </tbody>\n" + 
							"                                                                                  </table>\n" + 
							"                                                                                </td>\n" + 
							"                                                                              \n" + 
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
							"</html>"	;		
					
					//correoUtil.enviarVarios("Registro de Pre-Matrícula - " + datos_alu.getString("nom") +" " +datos_alu.getString("ape_pat"), "",new String[]{row.getString("fam_corr"),"maricris1906@hotmail.com"}, html,adjuntos,"consultas@ae.edu.pe","ALBERT EINSTEIN");
					//comentado x la apuranza x ahora correoUtil.enviar("Registro de Pre-Matrícula - " + datos_alu.getString("nom") +" " +datos_alu.getString("ape_pat"), "","linarosario1994@gmail.com", "hola",adjuntos,"consultas@ae.edu.pe","ALBERT EINSTEIN");
					MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
					mensajeriaFamiliar.setId_des(row.getInteger("id"));
					mensajeriaFamiliar.setId_per(8);
					mensajeriaFamiliar.setMsj("Registro de Pre-Matrícula " + datos_alu.getString("nom") +" " +datos_alu.getString("ape_pat")+" "+datos_alu.getString("ape_mat"));
					mensajeriaFamiliar.setEst("A");
					mensajeriaFamiliar.setFlg_en("1");
					mensajeriaFamiliar.setId_alu(alumno.getId());
					mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
				
		}
	}
	
	@RequestMapping( value="/enviarMensajeMatriculasPagadas/{id_anio}", method = RequestMethod.POST) // email_4
	public void enviarMensajeMatriculasPagadas(@PathVariable Integer id_anio)throws Exception{

		//Obtener la lista de matriculas 
		List<Row> matriculas=matriculaDAO.listarMatriculasxAnio(id_anio);
		
		for (Row row : matriculas) {
					String alumno=row.getString("alumno");
					String local=row.getString("sucursal");
					String nivel=row.getString("nivel");
					String grado=row.getString("grado");
					String correo_apod=row.getString("corr");
					String turno=row.getString("turno");
					CorreoUtil correoUtil = new CorreoUtil();
					//String pdfRuta =  "http://login.ae.edu.pe:8080/documentos/tycAcademiaVacaciones.pdf";
					//String pdfRuta =  "D:/AE/tycAcademiaVacaciones.pdf";
					//String pdfRuta =  "C:/Documentos/AN_Contrato_Servicios_Virtuales.pdf";
					//byte[] pdfBytes = FileUtil.filePathToByte(pdfRuta);
					
					/*String pdfRuta2 =  "/home/aeedupeh/public_html/plantillas/pasos para ingresar al teams padres.pdf";
					//String pdfRuta2 =  "C:/plantillas/pasos para ingresar al teams padres.pdf";
					byte[] pdfBytes2 = FileUtil.filePathToByte(pdfRuta2);*/
					
					List<Adjunto> adjuntos = new ArrayList<Adjunto>();
				//	adjuntos.add(new Adjunto("Comunicado.pdf",pdfBytes));
					
					//adjuntos.add(new Adjunto("Instructivo.pdf",pdfBytes2));
					String html ="<html lang=\"en\">\n" + 
							"\n" + 
							"<head>\n" + 
							"  <meta charset=\"UTF-8\">\n" + 
							"  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" + 
							"  <title>Email Matrícula\n" + 
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
							"                                                                        PADRE O MADRE DE FAMILIA</span></span></b>\n" + 
							"\n" + 
							"\n" + 
							"\n" + 
							"\n" + 
							"                                                                        <br>\n" + 
							"                                                                        &nbsp;<div\n" + 
							"                                                                          style=\"text-align:justify\">\n" + 
							"                                                                          <span\n" + 
							"                                                                            style=\"font-family:Arial,Helvetica,sans-serif\"><span\n" + 
							"                                                                              style=\"font-size:15px\">Reciba un cordial saludo a nombre del Colegio Albert Einstein. Le confirmamos por el presente que ha realizado satisfactoriamente matrícula para el año escolar 2021 a favor de:\n" + 
							"                                                                             </span></span>\n" + 
							"\n" + 
							"                                                                        </div>\n" + 
							"                                                                       \n" + 
							"                                                                        &nbsp;<div\n" + 
							"                                                                          style=\"text-align:left\">\n" + 
							"                                                                          <span\n" + 
							"                                                                            style=\"font-family:Arial,Helvetica,sans-serif\"><span\n" + 
							"                                                                              style=\"font-size:15px\"><b>Nombre del Alumno: </b>"+alumno+"<br>\n" + 
							"                                                                              <b>Local: </b>"+local+"<br>\n" + 
							"                                                                              <b>Nivel: </b>"+nivel+"<br>\n" + 
							"                                                                              <b>Grado: </b>"+grado+"<br>\n" + 
							"																			   <b>Turno:</b>"+turno+"<br>\n" +
							"                                                                              \n" + 
							"                                                                             </span></span>\n" + 
							"\n" + 
							"                                                                        </div>\n" + 
							"\n" + 
							"                                                                        &nbsp;<div\n" + 
							"                                                                        style=\"text-align:justify\">\n" + 
							"                                                                        <span\n" + 
							"                                                                          style=\"font-family:Arial,Helvetica,sans-serif\"><span\n" + 
							"                                                                            style=\"font-size:15px\">Toda información sobre el año académico será enviada a su <b>email: "+correo_apod+"</b>, registrado como medio de contacto con su familia.\n" + 
							"                                                                           </span></span><BR>\n" + 
							"                                                                                          <br><div\n" + 
							"                                                                                          style=\"text-align:right\">\n" + 
							"                                                                                          <span\n" + 
							"                                                                                            style=\"font-family:Arial,Helvetica,sans-serif\"><span\n" + 
							"                                                                                              style=\"font-size:15px\">\n" + 
							"                                                                                              <b>LA DIRECCIÓN</b></span></span>\n" +
							"</div>"+			
							"\n" + 
							"                                                                      </div>\n" + 
							"\n" + 
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
							"</html>"	;		
					
					//correoUtil.enviarVarios("Registro de Pre-Matrícula - " + datos_alu.getString("nom") +" " +datos_alu.getString("ape_pat"), "",new String[]{row.getString("fam_corr"),"maricris1906@hotmail.com"}, html,adjuntos,"consultas@ae.edu.pe","ALBERT EINSTEIN");
					//Verfico la cantidad de mensajes enviados
					Contador contador = contadorDAO.get(1);
					Integer cant_msj_env=contador.getNro();
					SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
					String format = formatter.format(new Date());
					String format2 = formatter.format(contador.getFec());
					int fecActual = Integer.parseInt(format);
					int fecContador=Integer.parseInt(format2);
					if(!correo_apod.equals("")) {
						if(cant_msj_env<=500 && fecActual==fecContador) {
							correoUtil.enviar("Constancia de Matrícula 2021 - " + alumno, "",correo_apod, html,null,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
							MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
							mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
							mensajeriaFamiliar.setId_per(8);
							mensajeriaFamiliar.setMsj("Confirmación de Matrícula 2021 " + alumno);
							mensajeriaFamiliar.setEst("A");
							mensajeriaFamiliar.setFlg_en("1");
							mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
							mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
							mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
							//Actualizo el estado de la matricula a correo enviado
							matriculaDAO.actualizarestadoEnviadoCorreo(row.getInteger("id_mat"));
							//actualizo el contador
							cant_msj_env = cant_msj_env + 1;
							//si es 500 cambio el usuario 
							if(cant_msj_env.equals(500)) {
								//actualizo
								contadorDAO.actualizarUsuarioContador("noreply@ae.edu.pe");
							}
							contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
						} else if((cant_msj_env>500 && cant_msj_env<=1000) && fecActual==fecContador){ // matricula.getString("corr")
							correoUtil.enviar("Constancia de Matrícula 2021 - " + alumno, "",correo_apod, html,null,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
							MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
							mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
							mensajeriaFamiliar.setId_per(8);
							mensajeriaFamiliar.setMsj("Confirmación de Matrícula 2021 " + alumno);
							mensajeriaFamiliar.setEst("A");
							mensajeriaFamiliar.setFlg_en("1");
							mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
							mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
							mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
							//Actualizo el estado de la matricula a correo enviado
							matriculaDAO.actualizarestadoEnviadoCorreo(row.getInteger("id_mat"));
							//actualizo el contador
							cant_msj_env = cant_msj_env + 1;
							//si es 500 cambio el usuario 
							if(cant_msj_env.equals(1000)) {
								//actualizo
								contadorDAO.actualizarUsuarioContador("noreply2@ae.edu.pe");
							}
							contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
						} else if((cant_msj_env>1000 && cant_msj_env<=1500) && fecActual==fecContador){ // matricula.getString("corr")
							correoUtil.enviar("Constancia de Matrícula 2021 - " + alumno, "",correo_apod, html,null,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
							MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
							mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
							mensajeriaFamiliar.setId_per(8);
							mensajeriaFamiliar.setMsj("Confirmación de Matrícula 2021 " + alumno);
							mensajeriaFamiliar.setEst("A");
							mensajeriaFamiliar.setFlg_en("1");
							mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
							mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
							mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
							//Actualizo el estado de la matricula a correo enviado
							matriculaDAO.actualizarestadoEnviadoCorreo(row.getInteger("id_mat"));
							//actualizo el contador
							cant_msj_env = cant_msj_env + 1;
							//si es 500 cambio el usuario 
							if(cant_msj_env.equals(1500)){
								//actualizo
								contadorDAO.actualizarUsuarioContador("noreply3@ae.edu.pe");
							}
							contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
						} else if((cant_msj_env>1500 && cant_msj_env<=2000) && fecActual==fecContador){ // matricula.getString("corr")
							correoUtil.enviar("Constancia de Matrícula 2021 - " + alumno, "",correo_apod, html,null,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
							MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
							mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
							mensajeriaFamiliar.setId_per(8);
							mensajeriaFamiliar.setMsj("Confirmación de Matrícula 2021 " + alumno);
							mensajeriaFamiliar.setEst("A");
							mensajeriaFamiliar.setFlg_en("1");
							mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
							mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
							mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
							//Actualizo el estado de la matricula a correo enviado
							matriculaDAO.actualizarestadoEnviadoCorreo(row.getInteger("id_mat"));
							//actualizo el contador
							cant_msj_env = cant_msj_env + 1;
							//si es 500 cambio el usuario 
							if(cant_msj_env.equals(2000)){
								//actualizo
								contadorDAO.actualizarUsuarioContador("noreply4@ae.edu.pe");
							}
							contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
						} else if((cant_msj_env>2000) && fecActual==fecContador){
							throw new Exception("Se ha excedido el envío de mensajes, por favor comuníquese con el administrador.");
						} else if(fecActual!=fecContador){
							//Actualizo la fecha del contador, Nuevo dia
							contadorDAO.actualizarFechaContador(new Date());
							contadorDAO.actualizarUsuarioContador("noreply@ae.edu.pe");
							Contador contador2= contadorDAO.get(1);
							correoUtil.enviar("Constancia de Matrícula 2021 - " + alumno, "",correo_apod, html,null,"informes@ae.edu.pe","ALBERT EINSTEIN",contador2.getUsr(), contador2.getPsw());
							MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
							mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
							mensajeriaFamiliar.setId_per(8);
							mensajeriaFamiliar.setMsj("Confirmación de Matrícula 2021 " + alumno);
							mensajeriaFamiliar.setEst("A");
							mensajeriaFamiliar.setFlg_en("1");
							mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
							mensajeriaFamiliar.setUsr_rmt(contador2.getUsr());
							mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
							//Actualizo el estado de la matricula a correo enviado
							matriculaDAO.actualizarestadoEnviadoCorreo(row.getInteger("id_mat"));
							//actualizo el contador
							cant_msj_env = cant_msj_env + 1;
							//si es 500 cambio el usuario 
							contadorDAO.actualizarContador(cant_msj_env, contador2.getFec());
						}
					}
					
				
		}
	}
	
	@RequestMapping( value="/enviarTutorialClassroom/{id_anio}", method = RequestMethod.POST) // email_4
	public void enviarMensajeMatriculasPagadasInfoGoogle(@PathVariable Integer id_anio)throws Exception{

		//Obtener la lista de matriculas 
		List<Row> matriculas=matriculaDAO.listarMatriculasxAnioPendientesEnvioTutorial(id_anio);
		
		for (Row row : matriculas) {
					String alumno=row.getString("alumno");
					//String local=row.getString("sucursal");
					//String nivel=row.getString("nivel");
					//String grado=row.getString("grado");
					String correo_apod=row.getString("corr");
					//String turno=row.getString("turno");
					CorreoUtil correoUtil = new CorreoUtil();
					//String pdfRuta =  "http://login.ae.edu.pe:8080/documentos/tycAcademiaVacaciones.pdf";
					//String pdfRuta =  "D:/AE/tycAcademiaVacaciones.pdf";
					String pdfRuta =  "C:/plantillas/IngresoalClassroom.pdf";
					byte[] pdfBytes = FileUtil.filePathToByte(pdfRuta);
					
					/*String pdfRuta2 =  "/home/aeedupeh/public_html/plantillas/pasos para ingresar al teams padres.pdf";
					//String pdfRuta2 =  "C:/plantillas/pasos para ingresar al teams padres.pdf";
					byte[] pdfBytes2 = FileUtil.filePathToByte(pdfRuta2);*/
					
					List<Adjunto> adjuntos = new ArrayList<Adjunto>();
					adjuntos.add(new Adjunto("Tutorial - Ingreso al classroom.pdf",pdfBytes));
					
					//adjuntos.add(new Adjunto("Instructivo.pdf",pdfBytes2));
					String html ="<html lang=\"en\">\n" + 
							"\n" + 
							"<head>\n" + 
							"  <meta charset=\"UTF-8\">\n" + 
							"  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" + 
							"  <title>Email\n" + 
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
							"                                                                        PADRE O MADRE DE FAMILIA</span></span></b>\n" + 
							"\n" + 
							"\n" + 
							"\n" + 
							"\n" + 
							"                                                                        <br>\n" + 
							"                                                                        &nbsp;<div\n" + 
							"                                                                          style=\"text-align:justify\">\n" + 
							"                                                                          <span\n" + 
							"                                                                            style=\"font-family:Arial,Helvetica,sans-serif\"><span\n" + 
							"                                                                              style=\"font-size:15px\">Se envía el tutorial de Ingresar a Classroom.\n" + 
							"                                                                             </span></span>\n" + 
							"\n" + 
							"                                                                        </div>\n" + 
							"                                                                        &nbsp;\n" + 
							"\n" + 
							"                                                                        <div\n" + 
							"                                                                          style=\"text-align:justify\">\n" + 
							"                                                                          <span\n" + 
							"                                                                            style=\"font-family:Arial,Helvetica,sans-serif\"><span\n" + 
							"                                                                              style=\"font-size:15px\">El email institucional y password de cada alumno se enviará próximamente.\n" + 
							"                                                                             </span></span>\n" + 
							"\n" + 
							"                                                                        </div>\n" + 
							"                                                                                          <div\n" + 
							"                                                                                          style=\"text-align:center\">\n" + 
							"                                                                                          <span\n" + 
							"                                                                                            style=\"font-family:Arial,Helvetica,sans-serif\"><span\n" + 
							"                                                                                              style=\"font-size:15px\">\n" + 
							"                                                                                              Atentamente</span></span>\n" + 
							"                                                                                             \n" + 
							"                                                                                              \n" + 
							"                                                                                         \n" + 
							"                                                                                          \n" + 
							"                                                                                        </div>  \n" + 
							"                                                                                         \n" + 
							"                                                                        \n" + 
							"                                                                                  \n" + 
							"                                                                                          <br>      <div\n" + 
							"                                                                                          style=\"text-align:right\">\n" + 
							"                                                                                          <span\n" + 
							"                                                                                            style=\"font-family:Arial,Helvetica,sans-serif\"><span\n" + 
							"                                                                                              style=\"font-size:15px\">\n" + 
							"                                                                                              <b>DIRECCIÓN ACADÉMICA</b></span></span>\n" + 
							"                                                                                             \n" + 
							"                                                                                              \n" + 
							"                                                                                          <br>\n" + 
							"                                                                                          <br>\n" + 
							"                                                                                        </div>  \n" + 
							"\n" + 
							"\n" + 
							"                                                                  \n" + 
							"\n" + 
							"                                                                      \n" + 
							"\n" + 
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
							"</html>"	;		
					
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
							correoUtil.enviar("Tutorial para el ingreso al Classroom - " + alumno, "",correo_apod, html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
							MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
							mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
							mensajeriaFamiliar.setId_per(8);
							mensajeriaFamiliar.setMsj("Tutorial para el ingreso al Classroom " + alumno);
							mensajeriaFamiliar.setEst("A");
							mensajeriaFamiliar.setFlg_en("1");
							mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
							mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
							mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
							//Actualizo el estado de la matricula a correo enviado
							matriculaDAO.actualizarestadoEnviadoTutorial(row.getInteger("id_mat"));
							//actualizo el contador
							cant_msj_env = cant_msj_env + 1;
							//si es 500 cambio el usuario 
							if(cant_msj_env.equals(1000)) {
								//actualizo
								contadorDAO.actualizarUsuarioContador("noreply2@ae.edu.pe");
							}
							contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
						} else if((cant_msj_env>1000 && cant_msj_env<=2000) && fecActual==fecContador){ // matricula.getString("corr")
							correoUtil.enviar("Tutorial para el ingreso al Classroom - " + alumno, "",correo_apod, html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
							MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
							mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
							mensajeriaFamiliar.setId_per(8);
							mensajeriaFamiliar.setMsj("Tutorial para el ingreso al Classroom " + alumno);
							mensajeriaFamiliar.setEst("A");
							mensajeriaFamiliar.setFlg_en("1");
							mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
							mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
							mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
							//Actualizo el estado de la matricula a correo enviado
							matriculaDAO.actualizarestadoEnviadoTutorial(row.getInteger("id_mat"));
							//actualizo el contador
							cant_msj_env = cant_msj_env + 1;
							//si es 500 cambio el usuario 
							if(cant_msj_env.equals(2000)) {
								//actualizo
								contadorDAO.actualizarUsuarioContador("noreply3@ae.edu.pe");
							}
							contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
						} else if((cant_msj_env>2000 && cant_msj_env<=3000) && fecActual==fecContador){ // matricula.getString("corr")
							correoUtil.enviar("Tutorial para el ingreso al Classroom - " + alumno, "",correo_apod, html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
							MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
							mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
							mensajeriaFamiliar.setId_per(8);
							mensajeriaFamiliar.setMsj("Tutorial para el ingreso al Classroom " + alumno);
							mensajeriaFamiliar.setEst("A");
							mensajeriaFamiliar.setFlg_en("1");
							mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
							mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
							mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
							//Actualizo el estado de la matricula a correo enviado
							matriculaDAO.actualizarestadoEnviadoTutorial(row.getInteger("id_mat"));
							//actualizo el contador
							cant_msj_env = cant_msj_env + 1;
							//si es 500 cambio el usuario 
							if(cant_msj_env.equals(3000)){
								//actualizo
								contadorDAO.actualizarUsuarioContador("noreply4@ae.edu.pe");
							}
							contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
						} else if((cant_msj_env>3000 && cant_msj_env<=4000) && fecActual==fecContador){ // matricula.getString("corr")
							correoUtil.enviar("Tutorial para el ingreso al Classroom - " + alumno, "",correo_apod, html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
							MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
							mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
							mensajeriaFamiliar.setId_per(8);
							mensajeriaFamiliar.setMsj("Tutorial para el ingreso al Classroom " + alumno);
							mensajeriaFamiliar.setEst("A");
							mensajeriaFamiliar.setFlg_en("1");
							mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
							mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
							mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
							//Actualizo el estado de la matricula a correo enviado
							matriculaDAO.actualizarestadoEnviadoTutorial(row.getInteger("id_mat"));
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
							correoUtil.enviar("Tutorial para el ingreso al Classroom - " + alumno, "",correo_apod, html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador2.getUsr(), contador2.getPsw());
							MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
							mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
							mensajeriaFamiliar.setId_per(8);
							mensajeriaFamiliar.setMsj("Tutorial para el ingreso al Classroom " + alumno);
							mensajeriaFamiliar.setEst("A");
							mensajeriaFamiliar.setFlg_en("1");
							mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
							mensajeriaFamiliar.setUsr_rmt(contador2.getUsr());
							mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
							//Actualizo el estado de la matricula a correo enviado
							matriculaDAO.actualizarestadoEnviadoTutorial(row.getInteger("id_mat"));
							//actualizo el contador
							cant_msj_env=contador2.getNro();
							cant_msj_env = cant_msj_env + 1;
							//si es 500 cambio el usuario 
							contadorDAO.actualizarContador(cant_msj_env, contador2.getFec());
						}
					}
					}
				
		}
	}
	
	@RequestMapping( value="/enviarUsrPswClassroom/{id_anio}", method = RequestMethod.POST) // email_4
	public void enviarUsuarioPswClassroom(@PathVariable Integer id_anio)throws Exception{

		//Obtener la lista de matriculas 
		List<Row> matriculas=matriculaDAO.listarMatriculasxAnioPendientesEnvioUsrPsw(id_anio);
		
		for (Row row : matriculas) {
					String alumno=row.getString("alumno");
					String usuario=row.getString("usuario");
					String psw=row.getString("pass_google");
					//String local=row.getString("sucursal");
					//String nivel=row.getString("nivel");
					//String grado=row.getString("grado");
					String correo_apod=row.getString("corr");
					//String turno=row.getString("turno");
					CorreoUtil correoUtil = new CorreoUtil();
					//String pdfRuta =  "http://login.ae.edu.pe:8080/documentos/tycAcademiaVacaciones.pdf";
					//String pdfRuta =  "D:/AE/tycAcademiaVacaciones.pdf";
					String pdfRuta =  "C:/plantillas/Bienvenida.pdf";
					byte[] pdfBytes = FileUtil.filePathToByte(pdfRuta);
					
					String pdfRuta2 =  "C:/plantillas/Ingreso_a_las_clases_Online.pdf";
					//String pdfRuta2 =  "C:/plantillas/pasos para ingresar al teams padres.pdf";
					byte[] pdfBytes2 = FileUtil.filePathToByte(pdfRuta2);
					
					String pdfRuta3 =  "C:/plantillas/Horario_de_Clases.pdf";
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
							"                                                                              style=\"font-size:15px\"><b>Alumno: </b>"+alumno+"<br>\n" + 
							"                                                                              <b>Email: </b>"+usuario+" <br>\n" + 
							"                                                                              <b>Contraseña: </b>"+psw+" <br>\n" + 
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
							correoUtil.enviar("Accesos para el Classroom - " + alumno, "",correo_apod, html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
							MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
							mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
							mensajeriaFamiliar.setId_per(8);
							mensajeriaFamiliar.setMsj("Accesos para el Classroom " + alumno);
							mensajeriaFamiliar.setEst("A");
							mensajeriaFamiliar.setFlg_en("1");
							mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
							mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
							mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
							//Actualizo el estado de la matricula a correo enviado
							matriculaDAO.actualizarestadoEnvioAccesos(row.getInteger("id_mat"));
							//actualizo el contador
							cant_msj_env = cant_msj_env + 1;
							//si es 500 cambio el usuario 
							if(cant_msj_env.equals(1000)) {
								//actualizo
								contadorDAO.actualizarUsuarioContador("noreply2@ae.edu.pe");
							}
							contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
						} else if((cant_msj_env>1000 && cant_msj_env<=2000) && fecActual==fecContador){ // matricula.getString("corr")
							correoUtil.enviar("Accesos para el Classroom - " + alumno, "",correo_apod, html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
							MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
							mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
							mensajeriaFamiliar.setId_per(8);
							mensajeriaFamiliar.setMsj("Accesos para el Classroom " + alumno);
							mensajeriaFamiliar.setEst("A");
							mensajeriaFamiliar.setFlg_en("1");
							mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
							mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
							mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
							//Actualizo el estado de la matricula a correo enviado
							matriculaDAO.actualizarestadoEnvioAccesos(row.getInteger("id_mat"));
							//actualizo el contador
							cant_msj_env = cant_msj_env + 1;
							//si es 500 cambio el usuario 
							if(cant_msj_env.equals(2000)) {
								//actualizo
								contadorDAO.actualizarUsuarioContador("noreply3@ae.edu.pe");
							}
							contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
						} else if((cant_msj_env>2000 && cant_msj_env<=3000) && fecActual==fecContador){ // matricula.getString("corr")
							correoUtil.enviar("Accesos para el Classroom - " + alumno, "",correo_apod, html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
							MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
							mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
							mensajeriaFamiliar.setId_per(8);
							mensajeriaFamiliar.setMsj("Accesos para el Classroom " + alumno);
							mensajeriaFamiliar.setEst("A");
							mensajeriaFamiliar.setFlg_en("1");
							mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
							mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
							mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
							//Actualizo el estado de la matricula a correo enviado
							matriculaDAO.actualizarestadoEnvioAccesos(row.getInteger("id_mat"));
							//actualizo el contador
							cant_msj_env = cant_msj_env + 1;
							//si es 500 cambio el usuario 
							if(cant_msj_env.equals(3000)){
								//actualizo
								contadorDAO.actualizarUsuarioContador("noreply4@ae.edu.pe");
							}
							contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
						} else if((cant_msj_env>3000 && cant_msj_env<=4000) && fecActual==fecContador){ // matricula.getString("corr")
							correoUtil.enviar("Accesos para el Classroom - " + alumno, "",correo_apod, html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
							MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
							mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
							mensajeriaFamiliar.setId_per(8);
							mensajeriaFamiliar.setMsj("Accesos para el Classroom " + alumno);
							mensajeriaFamiliar.setEst("A");
							mensajeriaFamiliar.setFlg_en("1");
							mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
							mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
							mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
							//Actualizo el estado de la matricula a correo enviado
							matriculaDAO.actualizarestadoEnvioAccesos(row.getInteger("id_mat"));
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
							correoUtil.enviar("Accesos para el Classroom - " + alumno, "",correo_apod, html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador2.getUsr(), contador2.getPsw());
							MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
							mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
							mensajeriaFamiliar.setId_per(8);
							mensajeriaFamiliar.setMsj("Accesos para el Classroom " + alumno);
							mensajeriaFamiliar.setEst("A");
							mensajeriaFamiliar.setFlg_en("1");
							mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
							mensajeriaFamiliar.setUsr_rmt(contador2.getUsr());
							mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
							//Actualizo el estado de la matricula a correo enviado
							matriculaDAO.actualizarestadoEnvioAccesos(row.getInteger("id_mat"));
							//actualizo el contador
							cant_msj_env=contador2.getNro();
							cant_msj_env = cant_msj_env + 1;
							//si es 500 cambio el usuario 
							contadorDAO.actualizarContador(cant_msj_env, contador2.getFec());
						}
					}
					}
				
		}
	}
	
	@RequestMapping( value="/enviarMensajeCanalPago/{id_anio}", method = RequestMethod.POST) // email_4
	public void enviarMensajeCanalPago(@PathVariable Integer id_anio)throws Exception{

		//Obtener la lista de matriculas 
		List<Row> matriculas=matriculaDAO.listarMatriculasxAnioPendientesEnvioTutorial(id_anio);
		
		for (Row row : matriculas) {
			if(row.getInteger("id_bco_pag")==1) {
				String alumno=row.getString("alumno");
				String correo_apod=row.getString("corr");
				//String turno=row.getString("turno");
				CorreoUtil correoUtil = new CorreoUtil();
				//String pdfRuta =  "http://login.ae.edu.pe:8080/documentos/tycAcademiaVacaciones.pdf";
				//String pdfRuta =  "D:/AE/tycAcademiaVacaciones.pdf";
				String pdfRuta =  "C:/plantillas/BancoDeCredito.pdf";
				byte[] pdfBytes = FileUtil.filePathToByte(pdfRuta);
				
				List<Adjunto> adjuntos = new ArrayList<Adjunto>();
				adjuntos.add(new Adjunto("Banco de Credito.pdf",pdfBytes));
				
				//adjuntos.add(new Adjunto("Instructivo.pdf",pdfBytes2));
				String html ="<html lang=\"en\">\n" + 
						"\n" + 
						"<head>\n" + 
						"  <meta charset=\"UTF-8\">\n" + 
						"  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" + 
						"  <title>Email Matrícula\n" + 
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
						"                                                                            src=\"http://ae.edu.pe/email/ac2021.png\"\n" + 
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
						"                                                                        PADRE Y/O MADRE DE FAMILIA</span></span></b>\n" + 
						"\n" + 
						"\n" + 
						"\n" + 
						"\n" + 
						"                                                                        <br>\n" + 
						"                                                                        &nbsp;<div\n" + 
						"                                                                          style=\"text-align:justify\">\n" + 
						"                                                                          <span\n" + 
						"                                                                            style=\"font-family:Arial,Helvetica,sans-serif\"><span\n" + 
						"                                                                              style=\"font-size:15px\">Ud. eligió al <B>BANCO DE CRÉDITO</B> como entidad bancaria para el pago de pensiones 2021, le recordamos los canales disponibles:\n" + 
						"                                                                              <ul>\n" + 
						"                                                                                  <li>Banca móvil.</li>\n" + 
						"                                                                                   <li> Banca por internet.</li>\n" + 
						"                                                                                    <li> Agentes BCP.</li>\n" + 
						"                                                                                    \n" + 
						"                                                                              </ul>\n" + 
						"\n" + 
						"                                                                        </div>\n" + 
						"\n" + 
						"                                                                  <div style=\"line-height:150%;text-align:justify\">\n" + 
						"                                                                    \n" + 
						"                                                                    <span style=\"font-size:15px\"><span\n" + 
						"                                                                        style=\"font-family:Arial,Helvetica,sans-serif\">El servicio de recaudo en ventanilla del banco no está habilitado.\n" + 
						"                                                                        <br>\n" + 
						"\n" + 
						"                                                                        <!-- Para conocer el detalle de los cambios puedes descargar la <a href=\"#\" style=\"color:#043263;text-decoration:none\" title=\"carta adjunta\" target=\"_blank\" >carta adjunta</a>.<br> -->\n" + 
						"\n" + 
						"                                                                  </div>\n" + 
						"                                                                  <br>\n" + 
						"\n" + 
						"                                                                  <div style=\"line-height:150%;text-align:justify\">\n" + 
						"                                                                    \n" + 
						"                                                                    <span style=\"font-size:15px\"><span\n" + 
						"                                                                        style=\"font-family:Arial,Helvetica,sans-serif\">Se adjunta instructivo.\n" + 
						"                                                                        <br>\n" + 
						"\n" + 
						"                                                                        <!-- Para conocer el detalle de los cambios puedes descargar la <a href=\"#\" style=\"color:#043263;text-decoration:none\" title=\"carta adjunta\" target=\"_blank\" >carta adjunta</a>.<br> -->\n" + 
						"\n" + 
						"                                                                  </div>\n" + 
						"                                                                        <br>\n" + 
						"                                                                        <br>\n" + 
						"\n" + 
						"                                                                  <div\n" + 
						"                                                                                          style=\"text-align:center\">\n" + 
						"                                                                                          <span\n" + 
						"                                                                                            style=\"font-family:Arial,Helvetica,sans-serif\"><span\n" + 
						"                                                                                              style=\"font-size:15px\">\n" + 
						"                                                                                              Atentamente<br>\n" + 
						"                                                                                              <b>LA DIRECCIÓN</b></span></span>\n" + 
						"                                                                                             \n" + 
						"                                                                                              \n" + 
						"                                                                                          <br>\n" + 
						"                                                                                          <br>\n" + 
						"                                                                                        </div>  \n" + 
						"\n" + 
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
						"                                       \n" + 
						"                                      </tr>\n" + 
						"                                      <tr>\n" + 
						"                                \n" + 
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
						correoUtil.enviar("Instructivo para pago de pensiones - " + alumno, "",correo_apod, html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
						MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
						mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
						mensajeriaFamiliar.setId_per(8);
						mensajeriaFamiliar.setMsj("Instructivo para pago de pensiones " + alumno);
						mensajeriaFamiliar.setEst("A");
						mensajeriaFamiliar.setFlg_en("1");
						mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
						mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
						mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
						//Actualizo el estado de la matricula a correo enviado
						matriculaDAO.actualizarestadoEnviadoTutorial(row.getInteger("id_mat"));
						//actualizo el contador
						cant_msj_env = cant_msj_env + 1;
						//si es 500 cambio el usuario 
						if(cant_msj_env.equals(1000)) {
							//actualizo
							contadorDAO.actualizarUsuarioContador("noreply2@ae.edu.pe");
						}
						contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
					} else if((cant_msj_env>1000 && cant_msj_env<=2000) && fecActual==fecContador){ // matricula.getString("corr")
						correoUtil.enviar("Instructivo para pago de pensiones - " + alumno, "",correo_apod, html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
						MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
						mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
						mensajeriaFamiliar.setId_per(8);
						mensajeriaFamiliar.setMsj("Instructivo para pago de pensiones " + alumno);
						mensajeriaFamiliar.setEst("A");
						mensajeriaFamiliar.setFlg_en("1");
						mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
						mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
						mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
						//Actualizo el estado de la matricula a correo enviado
						matriculaDAO.actualizarestadoEnviadoTutorial(row.getInteger("id_mat"));
						//actualizo el contador
						cant_msj_env = cant_msj_env + 1;
						//si es 500 cambio el usuario 
						if(cant_msj_env.equals(2000)) {
							//actualizo
							contadorDAO.actualizarUsuarioContador("noreply3@ae.edu.pe");
						}
						contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
					} else if((cant_msj_env>2000 && cant_msj_env<=3000) && fecActual==fecContador){ // matricula.getString("corr")
						correoUtil.enviar("Instructivo para pago de pensiones - " + alumno, "",correo_apod, html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
						MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
						mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
						mensajeriaFamiliar.setId_per(8);
						mensajeriaFamiliar.setMsj("Instructivo para pago de pensiones " + alumno);
						mensajeriaFamiliar.setEst("A");
						mensajeriaFamiliar.setFlg_en("1");
						mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
						mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
						mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
						//Actualizo el estado de la matricula a correo enviado
						matriculaDAO.actualizarestadoEnviadoTutorial(row.getInteger("id_mat"));
						//actualizo el contador
						cant_msj_env = cant_msj_env + 1;
						//si es 500 cambio el usuario 
						if(cant_msj_env.equals(3000)){
							//actualizo
							contadorDAO.actualizarUsuarioContador("noreply4@ae.edu.pe");
						}
						contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
					} else if((cant_msj_env>3000 && cant_msj_env<=4000) && fecActual==fecContador){ // matricula.getString("corr")
						correoUtil.enviar("Instructivo para pago de pensiones - " + alumno, "",correo_apod, html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
						MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
						mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
						mensajeriaFamiliar.setId_per(8);
						mensajeriaFamiliar.setMsj("Instructivo para pago de pensiones " + alumno);
						mensajeriaFamiliar.setEst("A");
						mensajeriaFamiliar.setFlg_en("1");
						mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
						mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
						mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
						//Actualizo el estado de la matricula a correo enviado
						matriculaDAO.actualizarestadoEnviadoTutorial(row.getInteger("id_mat"));
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
						correoUtil.enviar("Instructivo para pago de pensiones - " + alumno, "",correo_apod, html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador2.getUsr(), contador2.getPsw());
						MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
						mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
						mensajeriaFamiliar.setId_per(8);
						mensajeriaFamiliar.setMsj("Instructivo para pago de pensiones " + alumno);
						mensajeriaFamiliar.setEst("A");
						mensajeriaFamiliar.setFlg_en("1");
						mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
						mensajeriaFamiliar.setUsr_rmt(contador2.getUsr());
						mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
						//Actualizo el estado de la matricula a correo enviado
						matriculaDAO.actualizarestadoEnviadoTutorial(row.getInteger("id_mat"));
						//actualizo el contador
						cant_msj_env=contador2.getNro();
						cant_msj_env = cant_msj_env + 1;
						//si es 500 cambio el usuario 
						contadorDAO.actualizarContador(cant_msj_env, contador2.getFec());
					}
				}
				}
			} else if(row.getInteger("id_bco_pag")==2) {
				String alumno=row.getString("alumno");
				String correo_apod=row.getString("corr");
				//String turno=row.getString("turno");
				CorreoUtil correoUtil = new CorreoUtil();
				//String pdfRuta =  "http://login.ae.edu.pe:8080/documentos/tycAcademiaVacaciones.pdf";
				//String pdfRuta =  "D:/AE/tycAcademiaVacaciones.pdf";
				String pdfRuta =  "C:/plantillas/BancoContinental.pdf";
				byte[] pdfBytes = FileUtil.filePathToByte(pdfRuta);
				
				List<Adjunto> adjuntos = new ArrayList<Adjunto>();
				adjuntos.add(new Adjunto("Banco Continental.pdf",pdfBytes));
				
				//adjuntos.add(new Adjunto("Instructivo.pdf",pdfBytes2));
				String html ="<html lang=\"en\">\n" + 
						"\n" + 
						"<head>\n" + 
						"  <meta charset=\"UTF-8\">\n" + 
						"  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" + 
						"  <title>Email Matrícula\n" + 
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
						"                                                                            src=\"http://ae.edu.pe/email/ac2021.png\"\n" + 
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
						"                                                                        PADRE Y/O MADRE DE FAMILIA</span></span></b>\n" + 
						"\n" + 
						"\n" + 
						"\n" + 
						"\n" + 
						"                                                                        <br>\n" + 
						"                                                                        &nbsp;<div\n" + 
						"                                                                          style=\"text-align:justify\">\n" + 
						"                                                                          <span\n" + 
						"                                                                            style=\"font-family:Arial,Helvetica,sans-serif\"><span\n" + 
						"                                                                              style=\"font-size:15px\">Ud. eligió al <B>BANCO CONTINENTAL</B> como entidad bancaria para el pago de pensiones 2021, le recordamos los canales disponibles:\n" + 
						"                                                                              <ul>\n" + 
						"                                                                                  <li>Banca móvil.</li>\n" + 
						"                                                                                   <li> Banca por internet.</li>\n" + 
						"                                                                                    <li> Agentes BBVA.</li>\n" + 
						"                                                                                    \n" + 
						"                                                                              </ul>\n" + 
						"\n" + 
						"                                                                        </div>\n" + 
						"\n" + 
						"                                                                  <div style=\"line-height:150%;text-align:justify\">\n" + 
						"                                                                    \n" + 
						"                                                                    <span style=\"font-size:15px\"><span\n" + 
						"                                                                        style=\"font-family:Arial,Helvetica,sans-serif\">El servicio de recaudo en ventanilla del banco no está habilitado.\n" + 
						"                                                                        <br>\n" + 
						"\n" + 
						"                                                                        <!-- Para conocer el detalle de los cambios puedes descargar la <a href=\"#\" style=\"color:#043263;text-decoration:none\" title=\"carta adjunta\" target=\"_blank\" >carta adjunta</a>.<br> -->\n" + 
						"\n" + 
						"                                                                  </div>\n" + 
						"                                                                  <br>\n" + 
						"\n" + 
						"                                                                  <div style=\"line-height:150%;text-align:justify\">\n" + 
						"                                                                    \n" + 
						"                                                                    <span style=\"font-size:15px\"><span\n" + 
						"                                                                        style=\"font-family:Arial,Helvetica,sans-serif\">Se adjunta instructivo.\n" + 
						"                                                                        <br>\n" + 
						"\n" + 
						"                                                                        <!-- Para conocer el detalle de los cambios puedes descargar la <a href=\"#\" style=\"color:#043263;text-decoration:none\" title=\"carta adjunta\" target=\"_blank\" >carta adjunta</a>.<br> -->\n" + 
						"\n" + 
						"                                                                  </div>\n" + 
						"                                                                        <br>\n" + 
						"                                                                        <br>\n" + 
						"\n" + 
						"                                                                  <div\n" + 
						"                                                                                          style=\"text-align:center\">\n" + 
						"                                                                                          <span\n" + 
						"                                                                                            style=\"font-family:Arial,Helvetica,sans-serif\"><span\n" + 
						"                                                                                              style=\"font-size:15px\">\n" + 
						"                                                                                              Atentamente<br>\n" + 
						"                                                                                              <b>LA DIRECCIÓN</b></span></span>\n" + 
						"                                                                                             \n" + 
						"                                                                                              \n" + 
						"                                                                                          <br>\n" + 
						"                                                                                          <br>\n" + 
						"                                                                                        </div>  \n" + 
						"\n" + 
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
						"                                       \n" + 
						"                                      </tr>\n" + 
						"                                      <tr>\n" + 
						"                                \n" + 
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
						correoUtil.enviar("Instructivo para pago de pensiones - " + alumno, "",correo_apod, html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
						MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
						mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
						mensajeriaFamiliar.setId_per(8);
						mensajeriaFamiliar.setMsj("Instructivo para pago de pensiones " + alumno);
						mensajeriaFamiliar.setEst("A");
						mensajeriaFamiliar.setFlg_en("1");
						mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
						mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
						mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
						//Actualizo el estado de la matricula a correo enviado
						matriculaDAO.actualizarestadoEnviadoTutorial(row.getInteger("id_mat"));
						//actualizo el contador
						cant_msj_env = cant_msj_env + 1;
						//si es 500 cambio el usuario 
						if(cant_msj_env.equals(1000)) {
							//actualizo
							contadorDAO.actualizarUsuarioContador("noreply2@ae.edu.pe");
						}
						contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
					} else if((cant_msj_env>1000 && cant_msj_env<=2000) && fecActual==fecContador){ // matricula.getString("corr")
						correoUtil.enviar("Instructivo para pago de pensiones - " + alumno, "",correo_apod, html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
						MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
						mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
						mensajeriaFamiliar.setId_per(8);
						mensajeriaFamiliar.setMsj("Instructivo para pago de pensiones " + alumno);
						mensajeriaFamiliar.setEst("A");
						mensajeriaFamiliar.setFlg_en("1");
						mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
						mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
						mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
						//Actualizo el estado de la matricula a correo enviado
						matriculaDAO.actualizarestadoEnviadoTutorial(row.getInteger("id_mat"));
						//actualizo el contador
						cant_msj_env = cant_msj_env + 1;
						//si es 500 cambio el usuario 
						if(cant_msj_env.equals(2000)) {
							//actualizo
							contadorDAO.actualizarUsuarioContador("noreply3@ae.edu.pe");
						}
						contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
					} else if((cant_msj_env>2000 && cant_msj_env<=3000) && fecActual==fecContador){ // matricula.getString("corr")
						correoUtil.enviar("Instructivo para pago de pensiones - " + alumno, "",correo_apod, html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
						MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
						mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
						mensajeriaFamiliar.setId_per(8);
						mensajeriaFamiliar.setMsj("Instructivo para pago de pensiones " + alumno);
						mensajeriaFamiliar.setEst("A");
						mensajeriaFamiliar.setFlg_en("1");
						mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
						mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
						mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
						//Actualizo el estado de la matricula a correo enviado
						matriculaDAO.actualizarestadoEnviadoTutorial(row.getInteger("id_mat"));
						//actualizo el contador
						cant_msj_env = cant_msj_env + 1;
						//si es 500 cambio el usuario 
						if(cant_msj_env.equals(3000)){
							//actualizo
							contadorDAO.actualizarUsuarioContador("noreply4@ae.edu.pe");
						}
						contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
					} else if((cant_msj_env>3000 && cant_msj_env<=4000) && fecActual==fecContador){ // matricula.getString("corr")
						correoUtil.enviar("Instructivo para pago de pensiones - " + alumno, "",correo_apod, html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
						MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
						mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
						mensajeriaFamiliar.setId_per(8);
						mensajeriaFamiliar.setMsj("Instructivo para pago de pensiones " + alumno);
						mensajeriaFamiliar.setEst("A");
						mensajeriaFamiliar.setFlg_en("1");
						mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
						mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
						mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
						//Actualizo el estado de la matricula a correo enviado
						matriculaDAO.actualizarestadoEnviadoTutorial(row.getInteger("id_mat"));
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
						correoUtil.enviar("Instructivo para pago de pensiones - " + alumno, "",correo_apod, html,adjuntos,"informes@ae.edu.pe","ALBERT EINSTEIN",contador2.getUsr(), contador2.getPsw());
						MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
						mensajeriaFamiliar.setId_des(row.getInteger("id_fam"));
						mensajeriaFamiliar.setId_per(8);
						mensajeriaFamiliar.setMsj("Instructivo para pago de pensiones " + alumno);
						mensajeriaFamiliar.setEst("A");
						mensajeriaFamiliar.setFlg_en("1");
						mensajeriaFamiliar.setId_alu(row.getInteger("id_alu"));
						mensajeriaFamiliar.setUsr_rmt(contador2.getUsr());
						mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
						//Actualizo el estado de la matricula a correo enviado
						matriculaDAO.actualizarestadoEnviadoTutorial(row.getInteger("id_mat"));
						//actualizo el contador
						cant_msj_env=contador2.getNro();
						cant_msj_env = cant_msj_env + 1;
						//si es 500 cambio el usuario 
						contadorDAO.actualizarContador(cant_msj_env, contador2.getFec());
					}
				}
				}
			}
					
				
		}
	}
	
	@RequestMapping( value="/enviarCorreoRatificacion/{id_mats}", method = RequestMethod.POST)
	public void enviarCorreoRatificacion(@PathVariable Integer[] id_mats)throws Exception{

		for (int i = 0; i < id_mats.length; i++) {
			//Enviar Correo por cada matrícula
			//Buscar Ratificacion
			Ratificacion ratificacion =ratificacionDAO.getByParams(new Param("id_mat",id_mats[i]));
			if(ratificacion!=null) {
				Matricula matricula = matriculaDAO.get(id_mats[i]);
				Integer id_fam=matricula.getId_fam();
				Familiar familiar= familiarDAO.get(id_fam);
				Persona persona = personaDAO.getByParams(new Param("id",familiar.getId_per()));
				String correo=persona.getCorr();
				Anio anio = anioDAO.getByParams(new Param("id",ratificacion.getId_anio_rat()));
				Alumno alumno = alumnoDAO.get(matricula.getId_alu());
				Persona persona2= personaDAO.get(alumno.getId_per());
				if(correo!=null) {
					if(ratificacion.getRes().equals("1")) {
						CorreoUtil correoUtil = new CorreoUtil();
						if(persona.getCorr()!=null) {
							String html ="<html lang=\"en\">\n" + 
									"<head>\n" + 
									"  <meta charset=\"UTF-8\">\n" + 
									"  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" + 
									"  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" + 
									"  <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\n" + 
									"  <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\n" + 
									"  <link href=\"https://fonts.googleapis.com/css2?family=Oswald:wght@300;500;700&display=swap\" rel=\"stylesheet\"> \n" + 
									"  <link href=\"https://fonts.googleapis.com/css2?family=Lato:wght@300;500;700;900&display=swap\" rel=\"stylesheet\"> \n" + 
									"  <link href=\"https://fonts.googleapis.com/css2?family=Satisfy&display=swap\" rel=\"stylesheet\"> \n" + 
									"  <link rel=\"stylesheet\" href=\"http://login.ae.edu.pe:8080//estilos_ratificacion//normalize.css\">"+
									"  <link rel=\"stylesheet\" href=\"http://login.ae.edu.pe:8080//estilos_ratificacion//style.css\">"+
									"  <title>Responsive Desgn</title>\n" + 
									"</head>\n" + 
									"<body>\n" + 
									"  <header class=\"header\">\n" + 
									"    \n" + 
									"      <section class=\"info\">\n" + 
									"        <div class=\"container\">\n" + 
									"          <div class=\"comunication\">\n" + 
									"              <a href=\"https://api.whatsapp.com/send?phone=51943861219&text=%F0%9F%98%80%F0%9F%98%80%F0%9F%98%80%20Saludos%2C%20deseo%20informaci%C3%B3n%20%F0%9F%98%80%F0%9F%98%80%F0%9F%98%80\" target=\"_blank\">\n" + 
									"\n" + 
									"                <i class=\"fa-brands fa-whatsapp\"></i>\n" + 
									"                <span>943861219</span>\n" + 
									"              </a>\n" + 
									"              <a href=\"https://api.whatsapp.com/send?phone=51941886903&text=%F0%9F%98%80%F0%9F%98%80%F0%9F%98%80%20Saludos%2C%20deseo%20informaci%C3%B3n%20%F0%9F%98%80%F0%9F%98%80%F0%9F%98%80\" target=\"_blank\">\n" + 
									"                <i class=\"fa-brands fa-whatsapp\"></i>\n" + 
									"                <span>941886903</span>\n" + 
									"              </a>\n" + 
									"              <a href=\"https://api.whatsapp.com/send?phone=51954101793&text=%F0%9F%98%80%F0%9F%98%80%F0%9F%98%80%20Saludos%2C%20deseo%20informaci%C3%B3n%20%F0%9F%98%80%F0%9F%98%80%F0%9F%98%80\" target=\"_blank\">\n" + 
									"                <i class=\"fa-brands fa-whatsapp\"></i>\n" + 
									"                <span>954101793</span>\n" + 
									"              </a>\n" + 
									"              <a href=\"mailto:soporte@colegioae.freshdesk.com\">\n" + 
									"                <i class=\"fa-regular fa-envelope\"></i>\n" + 
									"                <span>soporte@colegioae.freshdesk.com</span>\n" + 
									"              </a>\n" + 
									"          </div>\n" + 
									"\n" + 
									"          <div class=\"social-media\">\n" + 
									"            <div>\n" + 
									"              <a href=\"https://www.facebook.com/colegioalberteinsteinhuaraz\" target=\"_blank\">\n" + 
									"                <i class=\"fa-brands fa-facebook-f\" ></i>\n" + 
									"              </a>\n" + 
									"              <a href=\"https://www.instagram.com/colegioalberteinsteinhz/\" target=\"_blank\">\n" + 
									"                <i class=\"fa-brands fa-instagram\" ></i>\n" + 
									"              </a>\n" + 
									"              <a href=\"https://www.youtube.com/channel/UCB6v3T9k4lV3igyVYMdihzg\" target=\"_blank\">\n" + 
									"                <i class=\"fa-brands fa-youtube\"></i>\n" + 
									"              </a>\n" + 
									"              <a href=\"https://www.tiktok.com/@colegioalberteinsteinhz\" target=\"_blank\">\n" + 
									"                <i class=\"fa-brands fa-tiktok\" ></i>\n" + 
									"              </a>\n" + 
									"            </div>\n" + 
									"          </div>\n" + 
									"        </div>\n" + 
									"        \n" + 
									"\n" + 
									" \n" + 
									"\n" + 
									"      </section>\n" + 
									"      <section class=\"logo container\">\n" + 
									"        \n" + 
									"        <picture>\n" + 
									"          <!-- <source media=\"(min-width: 480px)\" srcset=\"http://login.ae.edu.pe:8080/assets/logo-ae-text.png\"> -->\n" + 
									"          <img src=\"http://login.ae.edu.pe:8080/assets/logo-ae-text.png\" alt=\"\">\n" + 
									"        </picture>\n" + 
									"        <h2>¡Un einstino, un triundador!\n" + 
									"        </h2>\n" + 
									"      </section>\n" + 
									"\n" + 
									"      \n" + 
									"    \n" + 
									"  </header>\n" + 
									"  <main class=\"main-actividades\">\n" + 
									"    <div class=\"container\">\n" + 
									"      <section >\n" + 
									"        <h1 class=\"title\">ratificación de matricula</h1>\n" + 
									"        <!-- <h2 class=\"subtitle\">30 aniversario</h2> -->\n" + 
									"        <h2 class=\"subtitle\">Estimad@ "+persona.getNom()+" "+persona.getApe_pat()+" "+persona.getApe_mat()+"</h2>\n" + 
									"        <p  class=\"paragraph\">El Colegio Albert Einstein le saluda cordialmente e informa, a la consulta de sus planes académicos "+anio.getNom()+" , Ud. nos comunicó:</p>\n" + 
									"\n" + 
									"        <p class=\"paragraph\">Su menor hijo "+persona2.getNom()+" "+persona2.getApe_pat()+" "+persona2.getApe_mat()+" continuará sus estudios en nuestra institución educativa.</p>\n" + 
									"\n" + 
									"        <p class=\"paragraph\">Nos alegra su decisión, posteriormente le informaremos sobre el proceso de matrícula.</p>\n" + 
									"\n" + 
									"        <p class=\"paragraph\">Si tuviera consultas adicionales, no dude en escribirnos a <a href=\"mailto:soporte@colegioae.freshdesk.com\">soporte@colegioae.freshdesk.com</a> si tienes interés o necesitas mayor información.\n" + 
									"        </p>\n" + 
									"       \n" + 
									"      </section>\n" + 
									"    </div>\n" + 
									"  </main>\n" + 
									"\n" + 
									"  <footer class=\"footer\">\n" + 
									"    <div class=\"container\">\n" + 
									"      <div class=\"logo\">\n" + 
									"        <picture>\n" + 
									"          <!-- <source media=\"(min-width: 480px)\" srcset=\"http://login.ae.edu.pe:8080/assets/logo-ae-text.png\"> -->\n" + 
									"          <img src=\"http://login.ae.edu.pe:8080/assets/logo-ae-text.png\" alt=\"\">\n" + 
									"        </picture>\n" + 
									"      </div>\n" + 
									"  \n" + 
									"      <div class=\"social-media footer-media\">\n" + 
									"        <div>\n" + 
									"          <a href=\"https://www.facebook.com/colegioalberteinsteinhuaraz\" target=\"_blank\">\n" + 
									"            <i class=\"fa-brands fa-facebook-f\" ></i>\n" + 
									"          </a>\n" + 
									"          <a href=\"https://www.instagram.com/colegioalberteinsteinhz/\" target=\"_blank\">\n" + 
									"            <i class=\"fa-brands fa-instagram\" ></i>\n" + 
									"          </a>\n" + 
									"          <a href=\"https://www.youtube.com/channel/UCB6v3T9k4lV3igyVYMdihzg\" target=\"_blank\">\n" + 
									"            <i class=\"fa-brands fa-youtube\"></i>\n" + 
									"          </a>\n" + 
									"          <a href=\"https://www.tiktok.com/@colegioalberteinsteinhz\" target=\"_blank\">\n" + 
									"            <i class=\"fa-brands fa-tiktok\" ></i>\n" + 
									"          </a>\n" + 
									"        </div>\n" + 
									"      </div>\n" + 
									"  \n" + 
									"      <section></section>\n" + 
									"\n" + 
									"    </div>\n" + 
									"    \n" + 
									"  </footer>\n" + 
									"\n" + 
									"  <script src=\"https://kit.fontawesome.com/526c55f52d.js\" crossorigin=\"anonymous\"></script>\n" + 
									"</body>\n" + 
									"</html>"	;		
							
							//correoUtil.enviarVarios("Registro de Pre-Matrícula - " + datos_alu.getString("nom") +" " +datos_alu.getString("ape_pat"), "",new String[]{row.getString("fam_corr"),"maricris1906@hotmail.com"}, html,adjuntos,"consultas@ae.edu.pe","ALBERT EINSTEIN");
							//correoUtil.enviar("Confirmación de Documentos recepcionados correctamente ", "",persona.getCorr(),html,null,"noreply2@ae.edu.pe","ALBERT EINSTEIN"); //corregir aurita
							//Verfico la cantidad de mensajes enviados
							Contador contador = contadorDAO.get(1);
							Integer cant_msj_env=contador.getNro();
							SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
							String format = formatter.format(new Date());
							String format2 = formatter.format(contador.getFec());
							int fecActual = Integer.parseInt(format);
							int fecContador=Integer.parseInt(format2);
							if(cant_msj_env<=500 && fecActual==fecContador) {
								correoUtil.enviar("Confirmación de Ratificación de Matrícula ", "",persona.getCorr(),html,null,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
								MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
								mensajeriaFamiliar.setId_des(familiar.getId());
								mensajeriaFamiliar.setId_per(8);
								mensajeriaFamiliar.setMsj("Confirmación de Ratificación de Matrícula " + persona.getNom() +" " +persona.getApe_pat()+" "+persona.getApe_pat());
								mensajeriaFamiliar.setEst("A");
								mensajeriaFamiliar.setFlg_en("1");
								mensajeriaFamiliar.setId_alu(null);
								mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
								mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
								//actualizo el contador
								cant_msj_env = cant_msj_env + 1;
								//si es 500 cambio el usuario 
								if(cant_msj_env.equals(500)) {
									//actualizo
									contadorDAO.actualizarUsuarioContador("noreply@ae.edu.pe");
								}
								contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
							} else if((cant_msj_env>500 && cant_msj_env<=1000) && fecActual==fecContador){ // matricula.getString("corr")
								correoUtil.enviar("Confirmación de Ratificación de Matrícula ", "",persona.getCorr(),html,null,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
								MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
								mensajeriaFamiliar.setId_des(familiar.getId());
								mensajeriaFamiliar.setId_per(8);
								mensajeriaFamiliar.setMsj("Confirmación de Ratificación de Matrícula " + persona.getNom() +" " +persona.getApe_pat()+" "+persona.getApe_pat());
								mensajeriaFamiliar.setEst("A");
								mensajeriaFamiliar.setFlg_en("1");
								mensajeriaFamiliar.setId_alu(null);
								mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
								mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
								//actualizo el contador
								cant_msj_env = cant_msj_env + 1;
								//si es 500 cambio el usuario 
								if(cant_msj_env.equals(1000)) {
									//actualizo
									contadorDAO.actualizarUsuarioContador("noreply2@ae.edu.pe");
								}
								contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
							} else if((cant_msj_env>1000 && cant_msj_env<=1500) && fecActual==fecContador){ // matricula.getString("corr")
								correoUtil.enviar("Confirmación de Ratificación de Matrícula ", "",persona.getCorr(),html,null,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
								MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
								mensajeriaFamiliar.setId_des(familiar.getId());
								mensajeriaFamiliar.setId_per(8);
								mensajeriaFamiliar.setMsj("Confirmación de Ratificación de Matrícula " + persona.getNom() +" " +persona.getApe_pat()+" "+persona.getApe_pat());
								mensajeriaFamiliar.setEst("A");
								mensajeriaFamiliar.setFlg_en("1");
								mensajeriaFamiliar.setId_alu(null);
								mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
								mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
								//actualizo el contador
								cant_msj_env = cant_msj_env + 1;
								//si es 500 cambio el usuario 
								if(cant_msj_env.equals(1500)){
									//actualizo
									contadorDAO.actualizarUsuarioContador("noreply3@ae.edu.pe");
								}
								contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
							} else if((cant_msj_env>1500 && cant_msj_env<=2000) && fecActual==fecContador){ // matricula.getString("corr")
								correoUtil.enviar("Confirmación de Ratificación de Matrícula ", "",persona.getCorr(),html,null,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
								MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
								mensajeriaFamiliar.setId_des(familiar.getId());
								mensajeriaFamiliar.setId_per(8);
								mensajeriaFamiliar.setMsj("Confirmación de Ratificación de Matrícula " + persona.getNom() +" " +persona.getApe_pat()+" "+persona.getApe_pat());
								mensajeriaFamiliar.setEst("A");
								mensajeriaFamiliar.setFlg_en("1");
								mensajeriaFamiliar.setId_alu(null);
								mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
								mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
								//actualizo el contador
								cant_msj_env = cant_msj_env + 1;
								//si es 500 cambio el usuario 
								if(cant_msj_env.equals(2000)){
									//actualizo
									contadorDAO.actualizarUsuarioContador("noreply4@ae.edu.pe");
								}
								contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
							} else if((cant_msj_env>2000) && fecActual==fecContador){
								throw new Exception("Se ha excedido el envío de mensajes, por favor comuníquese con el administrador.");
							} else if(fecActual!=fecContador){
								//Actualizo la fecha del contador, Nuevo dia
								contadorDAO.actualizarFechaContador(new Date());
								contadorDAO.actualizarUsuarioContador("noreply@ae.edu.pe");
								Contador contador2= contadorDAO.get(1);
								correoUtil.enviar("Confirmación de Ratificación de Matrícula ", "",persona.getCorr(),html,null,"informes@ae.edu.pe","ALBERT EINSTEIN",contador2.getUsr(), contador2.getPsw());
								MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
								mensajeriaFamiliar.setId_des(familiar.getId());
								mensajeriaFamiliar.setId_per(8);
								mensajeriaFamiliar.setMsj("Confirmación de Ratificación de Matrícula " + persona.getNom() +" " +persona.getApe_pat()+" "+persona.getApe_pat());
								mensajeriaFamiliar.setEst("A");
								mensajeriaFamiliar.setFlg_en("1");
								mensajeriaFamiliar.setId_alu(null);
								mensajeriaFamiliar.setUsr_rmt(contador2.getUsr());
								mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
								//actualizo el contador
								cant_msj_env = cant_msj_env + 1;
								//si es 500 cambio el usuario 
								contadorDAO.actualizarContador(cant_msj_env, contador2.getFec());
							}
						}
					}
				}
			}
		}
		
	}
	
	
	@RequestMapping( value="/enviarMensajeMatriculaValidada/{id_fam}/{id_anio}", method = RequestMethod.POST)
	public void enviarMensajeMatriculaValidada(@PathVariable Integer id_fam,@PathVariable Integer id_anio)throws Exception{

		//Datos del familiar
		Familiar familiar= familiarDAO.get(id_fam);
		Persona persona = personaDAO.getByParams(new Param("id",familiar.getId_per()));
		Param param = new Param();
		param.put("mat.id_fam", id_fam); 
		param.put("pee.id_anio", id_anio);
		param.put("pee.id_tpe", "1");
		List<Matricula> matriculas = matriculaDAO.listFullByParams(param, null);
		
		Param param2 = new Param();
		param2.put("id_mat", matriculas.get(0).getId());
		param2.put("tip", "MAT");
		
		AcademicoPago academicoPago = academicoPagoDAO.getByParams(param2);
		Integer id_bco= academicoPago.getId_bco_pag();
		
		Banco banco = bancoDAO.get(id_bco);
		//Fecha de Pago
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		String MES[] = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
		//Date fechaActual=new Date();
		//String cadenaFecha=FechaUtil.toString(calendar.getTime());
		//Date fechaConcreta = new SimpleDateFormat("dd/MM/yyyy").parse(cadenaFecha);
		
		 Integer   annio = calendar.get(Calendar.YEAR);
		 String   mes = MES[calendar.get(Calendar.MONTH)];
		 Integer dia=calendar.get(Calendar.DAY_OF_MONTH);

	   /* String dia = new SimpleDateFormat("dd").format(fechaConcreta);
	    String mes = new SimpleDateFormat("MMMM").format(fechaConcreta);
	    String anio = new SimpleDateFormat("yyyy").format(fechaConcreta);*/
					
		CorreoUtil correoUtil = new CorreoUtil();
		if(persona.getCorr()!=null) {
			String html ="<html lang=\"en\">\r\n" + 
					"\r\n" + 
					"<head>\r\n" + 
					"  <meta charset=\"UTF-8\">\r\n" + 
					"  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" + 
					"  <title>Email Matrícula\r\n" + 
					"\r\n" + 
					"  </title>\r\n" + 
					"</head>\r\n" + 
					"\r\n" + 
					"<body>\r\n" + 
					"\r\n" + 
					"  <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\">\r\n" + 
					"    <tbody>\r\n" + 
					"      <tr>\r\n" + 
					"        <td align=\"center\" valign=\"top\">\r\n" + 
					"\r\n" + 
					"        </td>\r\n" + 
					"      </tr>\r\n" + 
					"      <tr>\r\n" + 
					"        <td align=\"center\">\r\n" + 
					"          <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"550\" class=\"m_-2564031024094939495container\"\r\n" + 
					"            align=\"center\">\r\n" + 
					"            <tbody>\r\n" + 
					"              <tr>\r\n" + 
					"                <td>\r\n" + 
					"                  <table style=\"background-color:#ffffff\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\"\r\n" + 
					"                    width=\"100%\">\r\n" + 
					"                    <tbody>\r\n" + 
					"                      <tr>\r\n" + 
					"                        <td align=\"center\" valign=\"top\">\r\n" + 
					"                          <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + 
					"                            <tbody>\r\n" + 
					"                              <tr>\r\n" + 
					"\r\n" + 
					"                                <td>\r\n" + 
					"\r\n" + 
					"                                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + 
					"                                    <tbody>\r\n" + 
					"                                      <tr>\r\n" + 
					"                                        <td align=\"center\" class=\"m_-2564031024094939495header\" valign=\"top\">\r\n" + 
					"                                          <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + 
					"                                            <tbody>\r\n" + 
					"                                              <tr>\r\n" + 
					"                                                <td align=\"left\" valign=\"top\">\r\n" + 
					"                                                  <table cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">\r\n" + 
					"                                                    <tbody>\r\n" + 
					"                                                      <tr>\r\n" + 
					"                                                        <td class=\"m_-2564031024094939495responsive-td\" valign=\"top\"\r\n" + 
					"                                                          style=\"width:100%\">\r\n" + 
					"                                                          <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\r\n" + 
					"                                                            style=\"min-width:100%\">\r\n" + 
					"                                                            <tbody>\r\n" + 
					"                                                              <tr>\r\n" + 
					"                                                                <td>\r\n" + 
					"                                                                  <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\r\n" + 
					"                                                                    <tbody>\r\n" + 
					"                                                                      <tr>\r\n" + 
					"                                                                        <td aling=\"center\"><img\r\n" + 
					"                                                                            src=\"http://ae.edu.pe/email/matricula-2021.png\"\r\n" + 
					"                                                                            style=\"display:block;padding:0px;text-align:center;height:auto;width:100%;border:0px\"\r\n" + 
					"                                                                            width=\"600\" class=\"CToWUd\"></td>\r\n" + 
					"                                                                      </tr>\r\n" + 
					"                                                                    </tbody>\r\n" + 
					"                                                                  </table>\r\n" + 
					"                                                                </td>\r\n" + 
					"                                                              </tr>\r\n" + 
					"                                                            </tbody>\r\n" + 
					"                                                          </table>\r\n" + 
					"\r\n" + 
					"                                                        </td>\r\n" + 
					"                                                      </tr>\r\n" + 
					"                                                    </tbody>\r\n" + 
					"                                                  </table>\r\n" + 
					"                                                </td>\r\n" + 
					"                                              </tr>\r\n" + 
					"                                            </tbody>\r\n" + 
					"                                          </table>\r\n" + 
					"                                        </td>\r\n" + 
					"                                      </tr>\r\n" + 
					"                                      <!-- <tr>\r\n" + 
					"                                <td align=\"center\" class=\"m_-2564031024094939495header\" valign=\"top\">\r\n" + 
					"                                  <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + 
					"                                    <tbody>\r\n" + 
					"                                    <tr>\r\n" + 
					"                                      <td align=\"left\" valign=\"top\">\r\n" + 
					"                                        <table cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">\r\n" + 
					"                                          <tbody>\r\n" + 
					"                                          <tr>\r\n" + 
					"                                            <td class=\"m_-2564031024094939495responsive-td\" valign=\"top\" style=\"width:100%\">\r\n" + 
					"                                              <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"min-width:100%\"><tbody><tr><td><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tbody><tr><td align=\"center\"><img src=\"https://ci3.googleusercontent.com/proxy/1yQGXaz1dcLtDOK4Bl5Tq3LKxU7XKIc-LcLRmhb2m4bqiaVKUkwiHl35EDAP1OM-Fp0RWgKlVxlV_wuk12dMP4jN1Wgd6_BvgY4D4bErRWO3YLRIQdD9nxa6oc19ulPq_3bn_RKYm4hmgCTT7wejC3DJnlLHjA=s0-d-e1-ft#https://image.mail.bbva.pe/lib/fe4315707564047f701771/m/12/766c20ce-5a00-4fae-8e1c-e2fbcf909fc1.png\" style=\"display:block;padding:0px;text-align:center;height:auto;width:100%\" width=\"600\" class=\"CToWUd a6T\" tabindex=\"0\"><div class=\"a6S\" dir=\"ltr\" style=\"opacity: 0.01; left: 801px; top: 335px;\"><div id=\":4xt\" class=\"T-I J-J5-Ji aQv T-I-ax7 L3 a5q\" role=\"button\" tabindex=\"0\" aria-label=\"Descargar el archivo adjunto \" data-tooltip-class=\"a1V\" data-tooltip=\"Descargar\"><div class=\"wkMEBb\"><div class=\"aSK J-J5-Ji aYr\"></div></div></div></div></td></tr></tbody></table></td></tr></tbody></table>\r\n" + 
					"                                              \r\n" + 
					"                                            </td>\r\n" + 
					"                                          </tr>\r\n" + 
					"                                          </tbody>\r\n" + 
					"                                        </table>\r\n" + 
					"                                      </td>\r\n" + 
					"                                    </tr>\r\n" + 
					"                                    </tbody>\r\n" + 
					"                                  </table>\r\n" + 
					"                                </td>\r\n" + 
					"                              </tr> -->\r\n" + 
					"                                      <tr>\r\n" + 
					"                                        <td align=\"center\" class=\"m_-2564031024094939495header\" valign=\"top\">\r\n" + 
					"                                          <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + 
					"                                            <tbody>\r\n" + 
					"                                              <tr>\r\n" + 
					"                                                <td align=\"left\" valign=\"top\">\r\n" + 
					"                                                  <table cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">\r\n" + 
					"                                                    <tbody>\r\n" + 
					"                                                      <tr>\r\n" + 
					"                                                        <td class=\"m_-2564031024094939495responsive-td\" valign=\"top\"\r\n" + 
					"                                                          style=\"width:100%\">\r\n" + 
					"                                                          <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\r\n" + 
					"                                                            style=\"background-color:#f4f4f4;min-width:100%\">\r\n" + 
					"                                                            <tbody>\r\n" + 
					"                                                              <tr>\r\n" + 
					"                                                                <td style=\"padding:30px\"><b><span\r\n" + 
					"                                                                      style=\"font-size:17px\"><span\r\n" + 
					"                                                                        style=\"font-family:Arial,Helvetica,sans-serif\">ESTIMADO\r\n" + 
					"                                                                        PADRE O MADRE DE FAMILIA</span></span></b>\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"                                                                        <br>\r\n" + 
					"                                                                        &nbsp;<div\r\n" + 
					"                                                                          style=\"text-align:justify\">\r\n" + 
					"                                                                          <span\r\n" + 
					"                                                                            style=\"font-family:Arial,Helvetica,sans-serif\"><span\r\n" + 
					"                                                                              style=\"font-size:15px\">Reciba un cordial saludo a nombre del Colegio Albert Einstein. Le confirmamos por el presente que los documentos enviados están correctamente llenados.\r\n" + 
					"                                                                             </span></span>\r\n" + 
					"\r\n" + 
					"                                                                        </div>\r\n" + 
					"                                                                       \r\n" + 
					"                                                                        &nbsp;<div\r\n" + 
					"                                                                          style=\"text-align:left\">\r\n" + 
					"                                                                          <span\r\n" + 
					"                                                                            style=\"font-family:Arial,Helvetica,sans-serif\"><span\r\n" + 
					"                                                                              style=\"font-size:15px\">El pago por derecho de matrícula lo puede realizar en el "+banco.getNom()+" a partir del día <b>"+dia+" de "+mes+" del "+annio+".</b>\r\n" + 
					"                                                                             </span></span>\r\n" + 
					"\r\n" + 
					"                                                                        </div>\r\n" + 
					"\r\n" + 
					"                                                                        &nbsp;<div\r\n" + 
					"                                                                        style=\"text-align:justify\">\r\n" + 
					"                                                                        <span\r\n" + 
					"                                                                          style=\"font-family:Arial,Helvetica,sans-serif\"><span\r\n" + 
					"                                                                            style=\"font-size:15px\"><b>Importante:</b> Servicio de Recaudo a Favor de: <b>Colegio Albert Einstein Huaraz</b>, el nro de Dni de su menor hijo es el código de pago.</b>\r\n" + 
					"                                                                           </span></span>\r\n" + 
					"\r\n" + 
					"                                                                      </div>\r\n" + 
					"\r\n" + 
					"                                                      \r\n" + 
					"                                                                </td>\r\n" + 
					"                                                              </tr>\r\n" + 
					"                                                            </tbody>\r\n" + 
					"                                                          </table>\r\n" + 
					"\r\n" + 
					"                                                        </td>\r\n" + 
					"                                                      </tr>\r\n" + 
					"                                                    </tbody>\r\n" + 
					"                                                  </table>\r\n" + 
					"                                                </td>\r\n" + 
					"                                              </tr>\r\n" + 
					"                                            </tbody>\r\n" + 
					"                                          </table>\r\n" + 
					"                                        </td>\r\n" + 
					"                                      </tr>\r\n" + 
					"                                      <tr>\r\n" + 
					"\r\n" + 
					"                                      </tr>\r\n" + 
					"                                      <tr>\r\n" + 
					"                                        <td align=\"center\" class=\"m_-2564031024094939495header\" valign=\"top\">\r\n" + 
					"                                          <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + 
					"                                            <tbody>\r\n" + 
					"                                              <tr>\r\n" + 
					"                                                <td align=\"left\" valign=\"top\">\r\n" + 
					"                                                  <table cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">\r\n" + 
					"                                                    <tbody>\r\n" + 
					"                                                      <tr>\r\n" + 
					"                                                        <td class=\"m_-2564031024094939495responsive-td\" valign=\"top\"\r\n" + 
					"                                                          style=\"width:100%\">\r\n" + 
					"                                                          <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\r\n" + 
					"                                                            style=\"background-color:#f4f4f4;min-width:100%\">\r\n" + 
					"                                                            <tbody>\r\n" + 
					"                                                              <tr>\r\n" + 
					"                                                                <td style=\"padding:0px 10px 10px\">\r\n" + 
					"                                                                  <table cellspacing=\"0\" cellpadding=\"0\"\r\n" + 
					"                                                                    style=\"width:100%\">\r\n" + 
					"                                                                    <tbody>\r\n" + 
					"                                                                      <tr>\r\n" + 
					"                                                                        <td>\r\n" + 
					"                                                                          <table cellspacing=\"0\" cellpadding=\"0\"\r\n" + 
					"                                                                            dir=\"rtl\" style=\"width:100%\">\r\n" + 
					"                                                                            <tbody>\r\n" + 
					"                                                                              <tr>\r\n" + 
					"                                                                                <td valign=\"top\"\r\n" + 
					"                                                                                  class=\"m_-2564031024094939495responsive-td\"\r\n" + 
					"                                                                                  dir=\"ltr\"\r\n" + 
					"                                                                                  style=\"width:70%;padding-left:0px\">\r\n" + 
					"                                                                                  <table cellpadding=\"0\" cellspacing=\"0\"\r\n" + 
					"                                                                                    width=\"100%\"\r\n" + 
					"                                                                                    style=\"background-color:transparent;min-width:100%\">\r\n" + 
					"                                                                                    <tbody>\r\n" + 
					"                                                                                      <tr>\r\n" + 
					"                                                                                        <td                                                                                   \r\n" + 
					"\r\n" + 
					"\r\n" + 
					"                                                                                           >\r\n" + 
					"                                                                                           \r\n" + 
					"                                                                                          </div>\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"                                                                                         \r\n" + 
					"                                                                        \r\n" + 
					"                                                                                          <br>\r\n" + 
					"                                                                                          <br>      <div\r\n" + 
					"                                                                                          style=\"text-align:right\">\r\n" + 
					"                                                                                          <span\r\n" + 
					"                                                                                            style=\"font-family:Arial,Helvetica,sans-serif\"><span\r\n" + 
					"                                                                                              style=\"font-size:15px\">\r\n" + 
					"                                                                                              <b>LA DIRECCIÓN</b></span></span>\r\n" + 
					"                                                                                             \r\n" + 
					"                                                                                              \r\n" + 
					"                                                                                          <br>\r\n" + 
					"                                                                                          <br>\r\n" + 
					"                                                                                        </div>  \r\n" + 
					"\r\n" + 
					"\r\n" + 
					"                                                                                      \r\n" + 
					"                                                                                        </td>\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"                                                                                      </tr>\r\n" + 
					"                                                                                    </tbody>\r\n" + 
					"                                                                                  </table>\r\n" + 
					"                                                                                </td>\r\n" + 
					"\r\n" + 
					"                                                                              </tr>\r\n" + 
					"                                                                            </tbody>\r\n" + 
					"                                                                          </table>\r\n" + 
					"                                                                        </td>\r\n" + 
					"                                                                      </tr>\r\n" + 
					"                                                                    </tbody>\r\n" + 
					"                                                                  </table>\r\n" + 
					"                                                                </td>\r\n" + 
					"                                                              </tr>\r\n" + 
					"                                                            </tbody>\r\n" + 
					"                                                          </table>\r\n" + 
					"\r\n" + 
					"                                                        </td>\r\n" + 
					"                                                      </tr>\r\n" + 
					"                                                    </tbody>\r\n" + 
					"                                                  </table>\r\n" + 
					"                                                </td>\r\n" + 
					"                                              </tr>\r\n" + 
					"                                            </tbody>\r\n" + 
					"                                          </table>\r\n" + 
					"                                        </td>\r\n" + 
					"                                      </tr>\r\n" + 
					"                                      <tr>\r\n" + 
					"                                        <td align=\"center\" class=\"m_-2564031024094939495header\" valign=\"top\">\r\n" + 
					"\r\n" + 
					"                                        </td>\r\n" + 
					"                                      </tr>\r\n" + 
					"                                      <tr>\r\n" + 
					"                                        <td align=\"center\" class=\"m_-2564031024094939495header\" valign=\"top\">\r\n" + 
					"                                          <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n" + 
					"                                            <tbody>\r\n" + 
					"                                              <tr>\r\n" + 
					"                                                <td align=\"left\" valign=\"top\">\r\n" + 
					"\r\n" + 
					"                                                </td>\r\n" + 
					"                                              </tr>\r\n" + 
					"                                            </tbody>\r\n" + 
					"                                          </table>\r\n" + 
					"                                        </td>\r\n" + 
					"                                      </tr>\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"                                    </tbody>\r\n" + 
					"                                  </table>\r\n" + 
					"                                </td>\r\n" + 
					"                              </tr>\r\n" + 
					"                            </tbody>\r\n" + 
					"                          </table>\r\n" + 
					"                        </td>\r\n" + 
					"                      </tr>\r\n" + 
					"                    </tbody>\r\n" + 
					"                  </table>\r\n" + 
					"                </td>\r\n" + 
					"              </tr>\r\n" + 
					"            </tbody>\r\n" + 
					"          </table>\r\n" + 
					"        </td>\r\n" + 
					"      </tr>\r\n" + 
					"\r\n" + 
					"    </tbody>\r\n" + 
					"  </table>\r\n" + 
					"</body>\r\n" + 
					"\r\n" + 
					"</html>"	;		
			
			//correoUtil.enviarVarios("Registro de Pre-Matrícula - " + datos_alu.getString("nom") +" " +datos_alu.getString("ape_pat"), "",new String[]{row.getString("fam_corr"),"maricris1906@hotmail.com"}, html,adjuntos,"consultas@ae.edu.pe","ALBERT EINSTEIN");
			//correoUtil.enviar("Confirmación de Documentos recepcionados correctamente ", "",persona.getCorr(),html,null,"noreply2@ae.edu.pe","ALBERT EINSTEIN"); //corregir aurita
			//Verfico la cantidad de mensajes enviados
			Contador contador = contadorDAO.get(1);
			Integer cant_msj_env=contador.getNro();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			String format = formatter.format(new Date());
			String format2 = formatter.format(contador.getFec());
			int fecActual = Integer.parseInt(format);
			int fecContador=Integer.parseInt(format2);
			if(cant_msj_env<=500 && fecActual==fecContador) {
				correoUtil.enviar("Confirmación de Documentos recepcionados correctamente ", "",persona.getCorr(),html,null,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
				mensajeriaFamiliar.setId_des(familiar.getId());
				mensajeriaFamiliar.setId_per(8);
				mensajeriaFamiliar.setMsj("Confirmacion de Documentos correctos " + persona.getNom() +" " +persona.getApe_pat()+" "+persona.getApe_pat());
				mensajeriaFamiliar.setEst("A");
				mensajeriaFamiliar.setFlg_en("1");
				mensajeriaFamiliar.setId_alu(null);
				mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
				mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
				//actualizo el contador
				cant_msj_env = cant_msj_env + 1;
				//si es 500 cambio el usuario 
				if(cant_msj_env.equals(500)) {
					//actualizo
					contadorDAO.actualizarUsuarioContador("noreply@ae.edu.pe");
				}
				contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
			} else if((cant_msj_env>500 && cant_msj_env<=1000) && fecActual==fecContador){ // matricula.getString("corr")
				correoUtil.enviar("Confirmación de Documentos recepcionados correctamente ", "",persona.getCorr(),html,null,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
				mensajeriaFamiliar.setId_des(familiar.getId());
				mensajeriaFamiliar.setId_per(8);
				mensajeriaFamiliar.setMsj("Confirmacion de Documentos correctos " + persona.getNom() +" " +persona.getApe_pat()+" "+persona.getApe_pat());
				mensajeriaFamiliar.setEst("A");
				mensajeriaFamiliar.setFlg_en("1");
				mensajeriaFamiliar.setId_alu(null);
				mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
				mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
				//actualizo el contador
				cant_msj_env = cant_msj_env + 1;
				//si es 500 cambio el usuario 
				if(cant_msj_env.equals(1000)) {
					//actualizo
					contadorDAO.actualizarUsuarioContador("noreply2@ae.edu.pe");
				}
				contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
			} else if((cant_msj_env>1000 && cant_msj_env<=1500) && fecActual==fecContador){ // matricula.getString("corr")
				correoUtil.enviar("Confirmación de Documentos recepcionados correctamente ", "",persona.getCorr(),html,null,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
				mensajeriaFamiliar.setId_des(familiar.getId());
				mensajeriaFamiliar.setId_per(8);
				mensajeriaFamiliar.setMsj("Confirmacion de Documentos correctos " + persona.getNom() +" " +persona.getApe_pat()+" "+persona.getApe_pat());
				mensajeriaFamiliar.setEst("A");
				mensajeriaFamiliar.setFlg_en("1");
				mensajeriaFamiliar.setId_alu(null);
				mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
				mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
				//actualizo el contador
				cant_msj_env = cant_msj_env + 1;
				//si es 500 cambio el usuario 
				if(cant_msj_env.equals(1500)){
					//actualizo
					contadorDAO.actualizarUsuarioContador("noreply3@ae.edu.pe");
				}
				contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
			} else if((cant_msj_env>1500 && cant_msj_env<=2000) && fecActual==fecContador){ // matricula.getString("corr")
				correoUtil.enviar("Confirmación de Documentos recepcionados correctamente ", "",persona.getCorr(),html,null,"informes@ae.edu.pe","ALBERT EINSTEIN",contador.getUsr(), contador.getPsw());
				MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
				mensajeriaFamiliar.setId_des(familiar.getId());
				mensajeriaFamiliar.setId_per(8);
				mensajeriaFamiliar.setMsj("Confirmacion de Documentos correctos " + persona.getNom() +" " +persona.getApe_pat()+" "+persona.getApe_pat());
				mensajeriaFamiliar.setEst("A");
				mensajeriaFamiliar.setFlg_en("1");
				mensajeriaFamiliar.setId_alu(null);
				mensajeriaFamiliar.setUsr_rmt(contador.getUsr());
				mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
				//actualizo el contador
				cant_msj_env = cant_msj_env + 1;
				//si es 500 cambio el usuario 
				if(cant_msj_env.equals(2000)){
					//actualizo
					contadorDAO.actualizarUsuarioContador("noreply4@ae.edu.pe");
				}
				contadorDAO.actualizarContador(cant_msj_env, contador.getFec());
			} else if((cant_msj_env>2000) && fecActual==fecContador){
				throw new Exception("Se ha excedido el envío de mensajes, por favor comuníquese con el administrador.");
			} else if(fecActual!=fecContador){
				//Actualizo la fecha del contador, Nuevo dia
				contadorDAO.actualizarFechaContador(new Date());
				contadorDAO.actualizarUsuarioContador("noreply@ae.edu.pe");
				Contador contador2= contadorDAO.get(1);
				correoUtil.enviar("Confirmación de Documentos recepcionados correctamente ", "",persona.getCorr(),html,null,"informes@ae.edu.pe","ALBERT EINSTEIN",contador2.getUsr(), contador2.getPsw());
				MensajeriaFamiliar mensajeriaFamiliar = new MensajeriaFamiliar();
				mensajeriaFamiliar.setId_des(familiar.getId());
				mensajeriaFamiliar.setId_per(8);
				mensajeriaFamiliar.setMsj("Confirmacion de Documentos correctos " + persona.getNom() +" " +persona.getApe_pat()+" "+persona.getApe_pat());
				mensajeriaFamiliar.setEst("A");
				mensajeriaFamiliar.setFlg_en("1");
				mensajeriaFamiliar.setId_alu(null);
				mensajeriaFamiliar.setUsr_rmt(contador2.getUsr());
				mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
				//actualizo el contador
				cant_msj_env = cant_msj_env + 1;
				//si es 500 cambio el usuario 
				contadorDAO.actualizarContador(cant_msj_env, contador2.getFec());
			}
		}
		
				
	}
	
	@RequestMapping( value="/llenarOpcionesMamayPapa", method = RequestMethod.GET)
	public AjaxResponseBody llenarOpcionesMamaYPapa() {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
			List<Parentesco> parentescoList = parentescoDAO.list();
			List<Parentesco> parentescoList1 = new ArrayList<Parentesco>();
			for (Parentesco parentesco : parentescoList) {
				if ((parentesco.getId().equals(Constante.PARENTESCO_MAMA) || parentesco.getId().equals(Constante.PARENTESCO_PAPA)))
					parentescoList1.add(parentesco);
			}
			
			result.setResult(parentescoList1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/llenarOpcionesOtros", method = RequestMethod.GET)
	public AjaxResponseBody llenarOpcionesOtros() {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
			List<Parentesco> parentescoList = parentescoDAO.list();
			List<Parentesco> parentescoList1 = new ArrayList<Parentesco>();
			for (Parentesco parentesco : parentescoList) {
				if ((!parentesco.getId().equals(Constante.PARENTESCO_MAMA) && !parentesco.getId().equals(Constante.PARENTESCO_PAPA)))
					parentescoList1.add(parentesco);
			}
			
			result.setResult(parentescoList1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Familiar familiar) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( familiarDAO.update(familiar) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	/**
	 * @param _id_fam id del familiar principal
	 * @param familiar Familiar nuevo/editar
	 * @param id_gpf grupo familiar
	 * @return
	 */
	@RequestMapping( value="/grabar",method = RequestMethod.POST)
	public AjaxResponseBody grabarFamiliar(Integer _id_fam,Integer _id_par,Familiar familiar,Integer id_gpf,String flag_permisos) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
			/*inicio - validaciones*/
			Param param = new Param();
			param.put("id_gpf", id_gpf);
			param.put("id_fam", _id_fam);
			GruFamFamiliar familiarPrincipal = gruFamFamiliarDAO.getByParams(param); //es el familiar apoderado q esta editando o agregando familiares
			
			if (familiarPrincipal.getId_par()==null){
				familiarPrincipal.setId_par(_id_par);
				gruFamFamiliarDAO.saveOrUpdate(familiarPrincipal);
			}
			
			//que no tenga el mismo parentesco q el principal
			
			
			//parentesco en la familia
			
			param = new Param();
			param.put("id_gpf", id_gpf);
			
			List<GruFamFamiliar> familiares =  gruFamFamiliarDAO.listFullByParams(param, null);
			
			for (GruFamFamiliar gruFamFamiliar : familiares) {
				if (gruFamFamiliar.getId_par()!=null && gruFamFamiliar.getFamiliar().getNro_doc().equals(familiar.getNro_doc())){

				//if (familiar.getId_par().equals(familiarPrincipal.getId_par())){
					result.setCode("500");
					result.setMsg("Ya existe el familiar agregado.");
					return result;
				}
			}
			
			boolean familiaTienePadre = false;
			boolean familiaTieneMadre = false;
			//ver si tiene padre y/o madre
			for (GruFamFamiliar gruFamFamiliar : familiares) {
				if (gruFamFamiliar.getId_par()!=null && gruFamFamiliar.getId_par().intValue() == EnumParentesco.PARENTESCO_MAMA.getValue() ){
					familiaTieneMadre = true;
				}
				if (gruFamFamiliar.getId_par()!=null && gruFamFamiliar.getId_par().intValue() == EnumParentesco.PARENTESCO_PAPA.getValue()){
					familiaTienePadre = true;
				}
			}
			
			if (familiaTieneMadre && familiar.getId_par().intValue() == EnumParentesco.PARENTESCO_MAMA.getValue()){
				result.setCode("500");
				result.setMsg("La familia ya tiene una madre asignada");
				return result;
			}
			
			if (familiaTienePadre && familiar.getId_par().intValue() == EnumParentesco.PARENTESCO_PAPA.getValue()){
				result.setCode("500");
				result.setMsg("La familia ya tiene un padre asignado");
				return result;
			}
			
			
			/*fin    - validaciones*/
			Integer id_fam = null;
			if (familiar.getId()==null)
				id_fam = familiarDAO.saveOrUpdate(familiar);// se crea uno nuevo
			else
				id_fam  = familiar.getId();
			
			param = new Param();
			param.put("id_gpf", id_gpf);
			param.put("id_fam", id_fam);
			GruFamFamiliar familiarNueva = gruFamFamiliarDAO.getByParams(param); //es el familiar apoderado q esta editando o agregando familiares
			
			
			//Revisar si la familia ya tiene el familiar relacionado, para poder actualizar
			if (familiarNueva==null){
				GruFamFamiliar gruFamFamiliar = new GruFamFamiliar();
				gruFamFamiliar.setEst("A");
				gruFamFamiliar.setFlag_permisos(flag_permisos);
				gruFamFamiliar.setId_fam(id_fam);
				gruFamFamiliar.setId_gpf(id_gpf);
				gruFamFamiliar.setId_par(familiar.getId_par());

				gruFamFamiliarDAO.saveOrUpdate(gruFamFamiliar);
			}else{
				familiarNueva.setFlag_permisos(flag_permisos);;
				familiarNueva.setId_par(familiar.getId_par());
				gruFamFamiliarDAO.saveOrUpdate(familiarNueva);

			}
			result.setResult( id_fam );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	

	@RequestMapping(value = "/FamiliarGrabar", method = RequestMethod.POST)
	public void grabar(Familiar familiar, Integer id_gpf, String rec_lib, Integer id_alu_per,HttpServletResponse response) throws IOException {
		
		Map<String, Object> result= new HashMap<String,Object>();
		
		try{
			String error = validarFamiliar( id_gpf, familiar, true);

			if (error==null) {
				 result=grabarFamiliar(familiar, id_gpf,rec_lib,id_alu_per);
			} else{
				
				throw new ControllerException(error);	
			}
			
			Map<String, Object> map = new HashMap<String,Object>();
			//map.put("error", error);
			map.put("id_gpf", result.get("id_gpf"));
			map.put("id_per", result.get("id_per"));
			map.put("id_par", familiar.getId_par());
			map.put("id", familiar.getId());
			
			//System.out.println(map);
			response.setContentType("application/json");
	        response.getWriter().write(JsonUtil.toJson(map));
	        
		}catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e.getMessage());
		}
	}	
	
	@Transactional
	@RequestMapping(value = "/FamiliarGrabarReq", method = RequestMethod.POST)
	public void grabarFamiliarReq(@RequestBody  FamiliarReq familiarReq, HttpServletResponse response) throws IOException {
		
		Map<String, Object> result= new HashMap<String,Object>();
		
		try{
			//Llenamos datos de la persona
			Persona persona = personaDAO.get(familiarReq.getId_per());
			//familiar.setId(familiarReq.getId());
			persona.setId_tdc(familiarReq.getId_tdc().toString());
			persona.setNro_doc(familiarReq.getNro_doc());
			persona.setUbigeo(familiarReq.getUbigeo());
			persona.setId_gen(familiarReq.getId_gen()); 
			persona.setId_eci(familiarReq.getId_eci()); 
			persona.setNom(familiarReq.getNom());
			persona.setApe_pat(familiarReq.getApe_pat());
			persona.setApe_mat(familiarReq.getApe_mat()); 
			//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			persona.setFec_nac(formatter.parse(familiarReq.getFec_nac()));
			if(familiarReq.getFec_def()!=null && familiarReq.getFec_def()!="")
			persona.setFec_def(formatter.parse(familiarReq.getFec_def()));
			persona.setViv(familiarReq.getViv());
			persona.setTlf(familiarReq.getTlf());
			persona.setCorr(familiarReq.getCorr());
			persona.setCel(familiarReq.getCel());
			persona.setFace(familiarReq.getFace());
			persona.setIstrg(familiarReq.getIstrg());
			persona.setTwitter(familiarReq.getTwitter());
			persona.setId_cond(1);
			//Actualizo a la persona
			Integer id_per=personaDAO.saveOrUpdate(persona);
			//familiar.setId_tap(familiarReq.getId_tap()); 
			
			//familiar.setId_gin(familiarReq.getId_gin()); 
			//familiar.setId_rel(familiarReq.getId_rel()); 
			//familiar.setId_dist(familiarReq.getId_dist()); 
			//familiar.setId_pais(familiarReq.getId_pais()); 
			//familiar.setId_ocu(familiarReq.getId_ocu()); 

			
			//familiar.setViv_alu(familiarReq.getViv_alu());
			//familiar.setDir(familiarReq.getDir());
			//familiar.setRef(familiarReq.getRef());
			Familiar familiar = familiarDAO.get(familiarReq.getId_fam());
			familiar.setId_par(familiarReq.getId_par());
			familiar.setProf(familiarReq.getProf());
			familiar.setOcu(familiarReq.getOcu());
			//Actualizo al familiar
			Integer id_fam=familiarDAO.saveOrUpdate(familiar);
			//familiar.setCto_tra(familiarReq.getCto_tra());
			//familiar.setDir_tra(familiarReq.getDir_tra());
			//familiar.setTlf_tra(familiarReq.getTlf_tra());
			//familiar.setCel_tra(familiarReq.getCel_tra());
			//familiar.setEmail_tra(familiarReq.getEmail_tra());
		//	familiar.setFlag_alu_ex(familiarReq.getFlag_alu_ex());
		//	familiar.setEst(familiarReq.getEst());
			//Por ahora no vamos a validar porque no estamos creando
			
			/*String error = validarFamiliar( familiarReq.getId_gpf(), familiar, true);

			if (error==null) {
				 result=grabarFamiliar(familiar, familiarReq.getId_gpf(),rec_lib,id_alu_per);
			} else{
				throw new ControllerException(error);	
			}*/
			
			//Actualizamos el año que se actualizan los datos
			//familiarDAO.actualizarAnioFamiliarActualizacion(familiarReq.getId_anio(),Integer.parseInt(result.get("id_fam").toString()));
			familiarDAO.actualizarAnioFamiliarActualizacion(familiarReq.getId_anio(),id_fam);
			
			Map<String, Object> map = new HashMap<String,Object>();
			//map.put("error", error);
			//map.put("id_gpf", result.get("id_gpf"));
			//map.put("id_gpf", result.get("id_gpf"));
			map.put("id_per", id_per);
			map.put("id_fam", id_fam);
			//map.put("id_par", familiar.getId_par());
			//map.put("id", familiar.getId());
			
			//System.out.println(map);
			response.setContentType("application/json");
	        response.getWriter().write(JsonUtil.toJson(map));
	        
		}catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e.getMessage());
		}
	}	
		
	@RequestMapping(value = "/OtroFamiliarEditar", method = RequestMethod.GET)
	public AjaxResponseBody otroFamiliarEditar(Integer id_gpf, Integer id_fam, HttpServletResponse response) throws IOException  {

		AjaxResponseBody result = new AjaxResponseBody();
		
		/** Mostrar el primer familiar **/
		String[] joins = { Parentesco.TABLA, Distrito.TABLA, Provincia.TABLA, Departamento.TABLA, Permisos.TABLA };
		Familiar familiar = new Familiar();
		
		if (id_fam != null){
			familiar = familiarDAO.getFull(id_fam, joins);//aca se setea el permiso
			Permisos permisos = permisosDAO.getByParams(new Param("id_fam",familiar.getId()));
			familiar.setPermisos(permisos);
		}else{
			//familiar.setId_tdc(Constante.TIPO_DOCUMENTO_DNI);
			familiar.setId_tap("A");
			Permisos permisos = new Permisos();
			permisos.setRec_lib("0");//NO
			permisos.setPed_inf("0");//NO
			familiar.setPermisos(permisos);
		}
			
		List<GruFamFamiliar> gruFamFamiliarList = gruFamFamiliarDAO.listFullByParams(new Param("gpf.id", id_gpf), new String[] { "gpf.id" });
		List<Familiar> familiarList = new ArrayList<Familiar>();
		
		for (GruFamFamiliar gruFamFamiliar : gruFamFamiliarList) {
			Familiar familiar1 = familiarDAO.getFull(gruFamFamiliar.getFamiliar().getId(), new String[] { Parentesco.TABLA, Permisos.TABLA });
			if (! gruFamFamiliar.getFamiliar().getId_par().equals(Constante.PARENTESCO_MAMA) && !gruFamFamiliar.getFamiliar().getId_par().equals(Constante.PARENTESCO_PAPA)) {// papa o mama
				familiarList.add(familiar1);
			} 
		}
		//cargarListas(model);
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("otroFamiliar", familiar);// es el familiar seleccionado
		map.put("familiarList",familiarList);
		map.put("id_gpf",id_gpf);

		result.setResult(map);
		return result;
		/*//System.out.println(map);
		response.setContentType("application/json");
        response.getWriter().write(JsonUtil.toJson(map));*/
        
	}	
	
	
	@Transactional
	private HashMap<String, Object> grabarFamiliar(Familiar familiar, Integer id_gpf, String rec_lib, Integer id_alu_per) {

		HashMap<String,Object> result = new HashMap<String,Object>();
		if (familiar.getId() == null) {

			
			/*** si el familiar es nuevo se debe crear grupo familiar ***/
			if (id_gpf == null) {
				
				GruFam gruFam = new GruFam();
				// obtener codigo de famila nueva
				String codigo = getCodigoFamilia();
				gruFam.setCod(codigo);
				gruFam.setUsr_ins(familiar.getUsr_ins());
				id_gpf = gru_famDAO.saveOrUpdate(gruFam);
			}

			//Generar un psw aleatorio
			String pass=StringUtil.randomInt(4);
			familiar.setPass(pass);
			Integer id_fam = familiarDAO.saveOrUpdate(familiar);
			familiar.setId(id_fam);
			result.put("id_fam", id_fam);
			/*if(!familiar.getId_par().equals(Constante.PARENTESCO_PAPA) && !familiar.getId_par().equals(Constante.PARENTESCO_MAMA)){
				
				Permisos permisos = new Permisos();
				permisos.setId(id_alu_per);
				permisos.setId_fam(familiar.getId());
				permisos.setPed_inf(ped_inf);
				permisos.setRec_lib(rec_lib);
				permisos.setEst("A");
				permisosDAO.saveOrUpdate(permisos);
				
				/*if(familiar.getPermisos()!=null){
					familiar.getPermisos().setId_fam(id_fam);
					permisosDAO.saveOrUpdate(familiar.getPermisos());
					
					
				}else{
					
						permisosDAO.saveOrUpdate(familiar.getPermisos());
					}*/
					
				
		//	}
		
			// 3-grabar NUEVO Grupo familiar/Familia
			GruFamFamiliar gru_fam_familiar = new GruFamFamiliar();
			gru_fam_familiar.setEst("A");
			gru_fam_familiar.setId_fam(id_fam);
			gru_fam_familiar.setId_gpf(id_gpf);
			if(!familiar.getId_par().equals(Constante.PARENTESCO_PAPA) && !familiar.getId_par().equals(Constante.PARENTESCO_MAMA)){
				if(rec_lib.equals("1"))
					gru_fam_familiar.setFlag_permisos("A");
			}
			gruFamFamiliarDAO.saveOrUpdate(gru_fam_familiar);

			//System.out.println("id_gpf nuevo::" + id_gpf);

			//System.out.println("id_fam nuevo::" + id_fam);
			result.put("id_gpf", id_gpf);
			if(familiar.getPermisos()!=null)
				result.put("id_per", familiar.getPermisos().getId());
			
			return result;

		} else {
			
			//Generar un psw aleatorio
			String pass=StringUtil.randomInt(4);
			familiar.setPass(pass);
			int grabo = familiarDAO.saveOrUpdate(familiar);
			
			if(!familiar.getId_par().equals(Constante.PARENTESCO_PAPA) && !familiar.getId_par().equals(Constante.PARENTESCO_MAMA)){
				if(familiar.getPermisos()!=null){
					familiar.getPermisos().setId_fam(familiar.getId());
					permisosDAO.saveOrUpdate(familiar.getPermisos());
				}
			}
			//System.out.println("actualiza familiar:" + grabo);

			/*** si el familiar es nuevo se debe crear grupo familiar ***/
			if (id_gpf == null) {
				GruFam gruFam = new GruFam();
				// obtener codigo de famila nueva
				String codigo = getCodigoFamilia();
				gruFam.setCod(codigo);
				id_gpf = gru_famDAO.saveOrUpdate(gruFam);
				
				
				GruFamFamiliar gru_fam_familiar = new GruFamFamiliar();
				gru_fam_familiar.setEst("A");
				gru_fam_familiar.setId_fam(familiar.getId());
				gru_fam_familiar.setId_gpf(id_gpf);
				if(!familiar.getId_par().equals(Constante.PARENTESCO_PAPA) && !familiar.getId_par().equals(Constante.PARENTESCO_MAMA)){
					if(rec_lib.equals("1"))
						gru_fam_familiar.setFlag_permisos("A");
				}
				gruFamFamiliarDAO.saveOrUpdate(gru_fam_familiar);
			}else{
				
				//validar si existe en el grupo familiar
				Param param = new Param();
				param.put("id_gpf",id_gpf);
				param.put("id_fam",familiar.getId());
				
				List<GruFamFamiliar> lista = gru_famDAO.getListGruFamFamiliar(param, null);
				
				//anexar al grupo familiar
				if(lista.size()==0){
					GruFamFamiliar gru_fam_familiar = new GruFamFamiliar();
					gru_fam_familiar.setEst("A");
					gru_fam_familiar.setId_fam(familiar.getId());
					gru_fam_familiar.setId_gpf(id_gpf);
					gruFamFamiliarDAO.saveOrUpdate(gru_fam_familiar);
				}
			}

			//System.out.println("id_gpf update::" + id_gpf);
			result.put("id_fam",familiar.getId());
			result.put("id_gpf", id_gpf);
			if(!familiar.getId_par().equals(Constante.PARENTESCO_PAPA) && !familiar.getId_par().equals(Constante.PARENTESCO_MAMA)){
				if(familiar.getPermisos()!=null)
					result.put("id_per", familiar.getPermisos().getId());
			}
			return result;
		}

	}
	
	/**
	 * Validar para grabar nuevo familiar
	 * 
	 * @param model
	 * @param id_gpf
	 * @param familiar
	 * @return
	 */
	private String validarFamiliar(Integer id_gpf, Familiar familiar, boolean esPadreOMadre) {

		//VALIDA QUE DNI NO EXISTE CUANDO ES UN NUEVO PADRE O MADRE 
		if (id_gpf != null  && familiar.getId() == null && esPadreOMadre) {
				
			/*validar si dni existe*/
			Param param = new Param("nro_doc",familiar.getNro_doc());
			param.put("id_par","2");
			List<Familiar> familiares = familiarDAO.listByParams(param, null);
			Param param1 = new Param("nro_doc",familiar.getNro_doc());
			param1.put("id_par","1");
			List<Familiar> familiares1 = familiarDAO.listByParams(param1, null);
			//List<Familiar> familiares2 = familiarDAO.listByParams(param1, null);
			if(familiares.size()>=1 || familiares1.size()>=1)
				return "Ya existe un padre o madre con el DNI "+familiar.getNro_doc();
		}
		
		if ((id_gpf != null && !"".equals(id_gpf) ) && familiar.getId() == null && esPadreOMadre) {
			Integer id_par = familiar.getId_par();

			// nuevo familiar en un grupo existente
			if (id_par.equals(Constante.PARENTESCO_MAMA)) {

				List<GruFamFamiliar> gruFamFamiliarList = gruFamFamiliarDAO
						.listFullByParams(new Param("id_gpf", id_gpf), null);
				for (GruFamFamiliar gruFamFamiliar : gruFamFamiliarList) {
					Integer bd_id_par = gruFamFamiliar.getFamiliar().getId_par();
					if (bd_id_par.equals(Constante.PARENTESCO_MAMA)) {
						return "Ya existe una mama como familiar del alumno";
					}
				}
			}

			if (id_par.equals(Constante.PARENTESCO_PAPA)) {

				List<GruFamFamiliar> gruFamFamiliarList = gruFamFamiliarDAO
						.listFullByParams(new Param("id_gpf", id_gpf), null);
				for (GruFamFamiliar gruFamFamiliar : gruFamFamiliarList) {
					Integer bd_id_par = gruFamFamiliar.getFamiliar().getId_par();
					if (bd_id_par.equals(Constante.PARENTESCO_PAPA)) {
						return "error:Ya existe un papa como familiar del alumno";
					}
				}
			}

		}

		if ((id_gpf != null && !"".equals(id_gpf)) && !esPadreOMadre) {	// if ((id_gpf != null && !"".equals(id_gpf))  && familiar.getId() == null && !esPadreOMadre) {
			Integer id_par = familiar.getId_par();

			// nuevo familiar en un grupo existente
			if (id_par.equals(Constante.PARENTESCO_MAMA)) {

				List<GruFamFamiliar> gruFamFamiliarList = gruFamFamiliarDAO
						.listFullByParams(new Param("id_gpf", id_gpf), null);
				for (GruFamFamiliar gruFamFamiliar : gruFamFamiliarList) {
					Integer bd_id_par = gruFamFamiliar.getFamiliar().getId_par();
					if (bd_id_par.equals(Constante.PARENTESCO_MAMA)) {
						return "error:Ya existe una mama como familiar del alumno";
					}
				}
			}

		}
		
		if ((id_gpf != null && !"".equals(id_gpf)) && familiar.getId() == null && esPadreOMadre) {	// if ((id_gpf != null && !"".equals(id_gpf))  && familiar.getId() == null && !esPadreOMadre) {
			Integer id_par = familiar.getId_par();

			// nuevo familiar en un grupo existente
			if (id_par.equals(Constante.PARENTESCO_MAMA)) {

				List<GruFamFamiliar> gruFamFamiliarList = gruFamFamiliarDAO
						.listFullByParams(new Param("id_gpf", id_gpf), null);
				for (GruFamFamiliar gruFamFamiliar : gruFamFamiliarList) {
					Integer bd_id_par = gruFamFamiliar.getFamiliar().getId_par();
					if (bd_id_par.equals(Constante.PARENTESCO_MAMA)) {
						return "error:Ya existe una mama como familiar del alumno";
					}
				}
			}

		}
		return null;
	}
	
	private String validarFamiliarPersona(Integer id_gpf, Persona persona, boolean esPadreOMadre, Integer id_par) {
		//Busco si sexiste familiar
		//Familiar familiar = familiarDAO.getByParams(new Param("id_per",persona.getId()));
		//VALIDA QUE DNI NO EXISTE CUANDO ES UN NUEVO PADRE O MADRE 
		if (id_gpf != null  && persona.getId() == null && esPadreOMadre) {
				
			/*validar si dni existe*/
			//Param param = new Param("nro_doc",persona.getNro_doc());
			//param.put("id_par","2");
			Row familiares = familiarDAO.datosFamiliarxParentesco(persona.getId(), 2);
			//Param param1 = new Param("nro_doc",familiar.getNro_doc());
			//param1.put("id_par","1");
			Row familiares1 = familiarDAO.datosFamiliarxParentesco(persona.getId(), 1);
			//List<Familiar> familiares1 = familiarDAO.listByParams(param1, null);
			//List<Familiar> familiares2 = familiarDAO.listByParams(param1, null);
			if(familiares!=null || familiares1!=null)
				return "Ya existe un padre o madre con el DNI "+persona.getNro_doc();
		}
		
		if ((id_gpf != null && !"".equals(id_gpf) ) && persona.getId() == null && esPadreOMadre) {
			//Buscamos al familiar
			//Familiar familiar=familiarDAO.getByParams(new Param("id_per",persona.getId()));
			//Integer id_par = familiarDAO.datosFamiliar(familiar.getId(),null).getInteger("id_par");

			// nuevo familiar en un grupo existente
			if (id_par.equals(Constante.PARENTESCO_MAMA)) {

				List<GruFamFamiliar> gruFamFamiliarList = gruFamFamiliarDAO
						.listFullByParams(new Param("id_gpf", id_gpf), null);
				for (GruFamFamiliar gruFamFamiliar : gruFamFamiliarList) {
					Integer bd_id_par = gruFamFamiliar.getFamiliar().getId_par();
					if (bd_id_par.equals(Constante.PARENTESCO_MAMA)) {
						return "Ya existe una mama como familiar del alumno";
					}
				}
			}

			if (id_par.equals(Constante.PARENTESCO_PAPA)) {

				List<GruFamFamiliar> gruFamFamiliarList = gruFamFamiliarDAO
						.listFullByParams(new Param("id_gpf", id_gpf), null);
				for (GruFamFamiliar gruFamFamiliar : gruFamFamiliarList) {
					Integer bd_id_par = gruFamFamiliar.getFamiliar().getId_par();
					if (bd_id_par.equals(Constante.PARENTESCO_PAPA)) {
						return "error:Ya existe un papa como familiar del alumno";
					}
				}
			}

		}

		/*if ((id_gpf != null && !"".equals(id_gpf)) && !esPadreOMadre) {	// if ((id_gpf != null && !"".equals(id_gpf))  && familiar.getId() == null && !esPadreOMadre) {
			Familiar familiar=familiarDAO.getByParams(new Param("id_per",persona.getId()));
			//Integer id_par = familiar.getId_par();

			// nuevo familiar en un grupo existente
			if (id_par.equals(Constante.PARENTESCO_MAMA)) {

				List<GruFamFamiliar> gruFamFamiliarList = gruFamFamiliarDAO
						.listFullByParams(new Param("id_gpf", id_gpf), null);
				for (GruFamFamiliar gruFamFamiliar : gruFamFamiliarList) {
					Integer bd_id_par = gruFamFamiliar.getFamiliar().getId_par();
					if (bd_id_par.equals(Constante.PARENTESCO_MAMA)) {
						return "error:Ya existe una mama como familiar del alumno";
					}
				}
			}

		}*/
		
		if ((id_gpf != null && !"".equals(id_gpf)) && persona.getId() != null && esPadreOMadre) {	// if ((id_gpf != null && !"".equals(id_gpf))  && familiar.getId() == null && !esPadreOMadre) {
			Familiar familiar=familiarDAO.getByParams(new Param("id_per",persona.getId()));
			//Integer id_par = familiar.getId_par();

			// nuevo familiar en un grupo existente
			if (id_par.equals(Constante.PARENTESCO_MAMA)) {

				List<GruFamFamiliar> gruFamFamiliarList = gruFamFamiliarDAO
						.listFullByParams(new Param("id_gpf", id_gpf), null);
				for (GruFamFamiliar gruFamFamiliar : gruFamFamiliarList) {
					Integer bd_id_par = gruFamFamiliar.getFamiliar().getId_par();
					if (bd_id_par.equals(Constante.PARENTESCO_MAMA)) {
						return "error:Ya existe una mama como familiar del alumno";
					}
				}
			}
			
			if (id_par.equals(Constante.PARENTESCO_PAPA)) {

				List<GruFamFamiliar> gruFamFamiliarList = gruFamFamiliarDAO
						.listFullByParams(new Param("id_gpf", id_gpf), null);
				for (GruFamFamiliar gruFamFamiliar : gruFamFamiliarList) {
					Integer bd_id_par = gruFamFamiliar.getFamiliar().getId_par();
					if (bd_id_par.equals(Constante.PARENTESCO_PAPA)) {
						return "error:Ya existe un papa como familiar del alumno";
					}
				}
			}

		}
		return null;
	}
	
	private String getCodigoFamilia() {
		List<Row> Cantidad = familiarDAO.Cantidad_Fam();
		int correlativo = Integer.parseInt(Cantidad.get(0).get("cantidad").toString()) + 1;

		Date date = new Date(); // your date
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String year =Integer.toString(cal.get(Calendar.YEAR)).substring(2);

		DecimalFormat myFormatter = new DecimalFormat("00000");
		String correlativoString = myFormatter.format(correlativo);

		String codigo = year  + correlativoString;

		return codigo;
	}

	
	@RequestMapping( value="/actualizarDatos",method = RequestMethod.POST)
	public AjaxResponseBody actualizarDatos(Familiar familiar,  HttpServletRequest request) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);
			Familiar familiar_val1=familiarDAO.getByParams(new Param("nro_doc",familiar.getNro_doc()));
			Familiar familiar_val2=familiarDAO.getByParams(new Param("corr",familiar.getCorr()));

			String dni=null;
			String corr=null;
			Integer id_val1=null;
			Integer id_val2=null;
			
			/*if ("".equals(familiar.getIni()))
				familiar.setIni("1");//SIGNIFICA QUE LE FALTA CONFIRMUAR SU CORREO*/ //comentado 2020
			
			if(familiar_val1!=null){
				dni=familiar_val1.getNro_doc();
				id_val1=familiar_val1.getId();
			}
			if(familiar_val2!=null){
				corr=familiar_val1.getCorr();
				id_val2=familiar_val1.getId();
			}
			if(dni!=null && !(id_val1.equals(familiar.getId()))){
				//result.setResult(familiarDAO.actualizarDatos(familiar))
				result.setCode("500");
				result.setMsg("El dni ingresado ya pertenece a otro Padre de Familia");;
			}else if (corr!=null  && !(id_val2.equals(familiar.getId()))){
				result.setCode("500");
				result.setMsg("El email esta vinculado a otro Padre de Familia, ingrese un email diferente.");
			} else {
				result.setResult(familiarDAO.actualizarDatos(familiar));
			}
			
			/*if("1".equals(familiar.getIni())){
				
				String conf  = StringUtil.randomString(5);
				familiarDAO.updateCampoConfirmacion(familiar.getId(), conf, -1);
				
				CorreoUtil correoUtil = new CorreoUtil();
				//String host = request.getHeader("host");
				String html = "Estimad@:";
				html += "<br><br>" + familiar.getApe_pat() + " " + familiar.getApe_mat() +", " + familiar.getNom();
				html += "<br><br>Para completar el cambio de información personal ingresa el código de verificación:";
				html += "<br><br><h2><u>CODIGO DE VERIFICACIÓN</u></h2>";
				html += "<h1><font color='red'>" + conf + "</font></h1>";

				html += "Este correo es informativo, favor no responder a esta dirección de correo, ya que no se encuentra habilitada para recibir mensajes.";
				html += "<BR>Si requiere mayor información, contactar con secretaria de Lunes a Viernes de 8:00 a 12:45 y de 14:00 a 17:00 horas, teléfono "+empresa.getString("tel")+" o al e-mail:<a href='mailto:"+empresa.getString("giro_negocio")+"'>"+empresa.getString("giro_negocio")+"</a>";
				html += "<br><br>Atentamente";
				html += "<br><b>La Dirección</b>";

				correoUtil.enviar(empresa.getString("giro_negocio")+" - Recuperación de clave", "", familiar.getCorr(), html, null,empresa.getString("corr"),empresa.getString("giro_negocio"));
				
			}*/
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	
	@RequestMapping( value="/actualizarDatosLight",method = RequestMethod.POST)
	public AjaxResponseBody actualizarDatosLight(Familiar familiar) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			Familiar familiar_val1=familiarDAO.getByParams(new Param("nro_doc",familiar.getNro_doc()));
			Familiar familiar_val2=familiarDAO.getByParams(new Param("corr",familiar.getCorr()));

			String dni=null;
			String corr=null;
			Integer id_val1=null;
			Integer id_val2=null;
			
			if ("".equals(familiar.getIni()))
				familiar.setIni("1");//SIGNIFICA QUE LE FALTA CONFIRMUAR SU CORREO
			
			if(familiar_val1!=null){
				dni=familiar_val1.getNro_doc();
				id_val1=familiar_val1.getId();
			}
			
			if(familiar_val2!=null){
				corr=familiar_val1.getCorr();
				id_val2=familiar_val1.getId();
			}
			
			if(dni!=null && !(id_val1.equals(familiar.getId()))){

				result.setCode("500");
				result.setMsg("El dni ingresado ya pertenece a otro Padre de Familia");;
			}else if (corr!=null  && !(id_val2.equals(familiar.getId()))){
				result.setCode("500");
				result.setMsg("El email esta vinculado a otro Padre de Familia, ingrese un email diferente.");
			} else {
				result.setResult(familiarDAO.actualizarDatosLight(familiar));
			}
	 		
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/confirmarCodigo",method = RequestMethod.POST)
	public AjaxResponseBody confirmarCodigo(Integer id, Integer id_per, String codigo) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
			if(EnumPerfil.PERFIL_FAMILIAR.getValue() == id_per){
				
				Param param = new Param();
				param.put("id", id);
				param.put("conf", codigo);
				//param.put("est", "A");
				
				Familiar familiar = familiarDAO.getByParams(param) ;
				
				if(familiar==null){
					result.setMsg("ERROR");
				}
				else
					familiarDAO.updateIni(id, "2", new Date(), id) ;
			}
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	

	/**
	 * Exportar a excel la seccion con su codigo de barras
	 * @param response
	 * @param id_au
	 * @throws Exception
	 */
	@RequestMapping(value = "/exportarClaves/{id_au}")
	@ResponseBody
	public void getLista(HttpServletResponse response, @PathVariable Integer id_au) throws Exception {

		List<Row> listaFamiliares = familiarDAO.listFamiliarUsuario(null, id_au);
		
		for (Row map : listaFamiliares) {
			String nombre = map.get("nom").toString();
			String apellidos = map.get("ape_pat") + " " + map.get("ape_mat");
			if (apellidos.length()>24)
				apellidos = apellidos.substring(0, 24);
			if (nombre.length()>24)
				nombre = nombre.substring(0, 24);
			map.put("familiar",apellidos);
			map.put("nombre", nombre);
			map.put("nro_doc",map.get("nro_doc"));
			map.put("grado", map.get("nivel") + "-" + map.getString("grado") + "-" + map.getString("secc"));
		}


		CodigoBarrasUtil codigoBarras = new CodigoBarrasUtil();
        float[] _margenes = new float[]{0,5,0,10};
        float _width = 134;
        float _fontSize = 7;

		InputStream is = codigoBarras.createPdfApoderado(_width, _margenes, _fontSize, listaFamiliares);

		response.setContentType("application/pdf");
		response.addHeader("Content-Disposition", "attachment; filename=familiar.pdf");

		IOUtils.copy(is, response.getOutputStream());

	}
	
	@RequestMapping( value="/familiarParentesco", method = RequestMethod.GET)
	public AjaxResponseBody listarDesempenios( Integer id_fam) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			if(id_fam!=null)
				result.setResult(familiarDAO.familiarParentesco(id_fam));
			else
				result.setResult(new ArrayList<>());
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/actualizarEstado", method = RequestMethod.POST)
	public AjaxResponseBody actualizarEstado(Integer id, String est) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			familiarDAO.actualizarEstado(id, est);
			
			if(est.equals("A")){
				//limpiar los bloqueos
				logLoginDAO.resetearIntentosFallidos(id, EnumPerfil.PERFIL_FAMILIAR.getValue());
			}
			
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	

	@RequestMapping(value = "/FamiliarBuscarResultados", method = RequestMethod.GET)
	public AjaxResponseBody listar( 
			String apellidosNombres,
			Integer id_tdc, 
			String nro_doc, 
			String type) throws IOException {

		List<GruFam> gruFamList = new ArrayList<GruFam>();
		List<GruFamFamiliar> gruFamFamiliarList = new ArrayList<GruFamFamiliar>();
		
		AjaxResponseBody result = new AjaxResponseBody();

		Map<String,Object> mapResult = new HashMap<String, Object>();
		
		List<Map<String,Object>> evaluacion_vacList = evaluacion_vacDAO.listaEvaluacionesVigentes();
		mapResult.put("evaluacion_vacList",evaluacion_vacList);
		
		/** Lista de familiares del alumno **/
		if ("2".equals(type)) {

			Param param = new Param();
			if (apellidosNombres != "")
				param.put("concat(fam.ape_pat,' ',fam.ape_pat, ' ', fam.nom  )", "%" + apellidosNombres + "%");
			gruFamFamiliarList = gruFamFamiliarDAO.listFullByParams(param, new String[] { "fam.ape_pat asc" });

		} else if ("1".equals(type)) {

			Param param = new Param();
			//param.put("fam.id_tdc", id_tdc);
			param.put("fam.nro_doc", nro_doc);
			gruFamFamiliarList = gruFamFamiliarDAO.listFullByParams(param, new String[] { "gpf.id" });
			
			for (GruFamFamiliar gruFamFamiliar : gruFamFamiliarList) {
				if(gruFamFamiliar.getFamiliar().getNro_doc().equals(nro_doc))
					mapResult.put("id_fam", gruFamFamiliar.getFamiliar().getId());
			}
		} 
		for (GruFamFamiliar gruFamFamiliar : gruFamFamiliarList) {
			GruFam gruFam = gruFamFamiliar.getGruFam();
			String[] order = { " alu.nom ", " alu.ape_pat", " alu.ape_mat " };
			List<GruFamAlumno> gruFamAlumnos = gruFamAlumnoDAO
					.listFullByParams(new Param("gpf.id", gruFamFamiliar.getId_gpf()), order);
			gruFam.setGruFamAlumno(gruFamAlumnos);
			gruFam.setId_fam(gruFamFamiliar.getId_fam());
			gruFamList.add(gruFam);
		}
		mapResult.put("gruFamList",gruFamList);
		result.setResult(mapResult);
		return result;
	}

	@RequestMapping( value="/validarCelular", method = RequestMethod.POST)
	public AjaxResponseBody validarCelular(Integer id, String cel) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
			Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);
			//validar si celular ya esta validado por otro familiar
			
			
			Familiar familiar = familiarDAO.get(id);
			
			familiar.setCel(cel);
			familiarDAO.saveOrUpdate(familiar);
			
			//enviar mensaje de texto
			MensajeriaFamiliar mensajeriaFamiliar =  new MensajeriaFamiliar();
			mensajeriaFamiliar.setId_des(id);
			mensajeriaFamiliar.setId_per(EnumPerfil.PERFIL_FAMILIAR.getValue());
			mensajeriaFamiliar.setFlg_en("0");
			mensajeriaFamiliar.setEst("A");
			Usuario usuario = new Usuario();
			usuario.setId(id);
			mensajeriaFamiliar.setUsuario(usuario);
			String clave = StringUtil.randomInt(4);
			mensajeriaFamiliar.setClave(clave);
			mensajeriaFamiliar.setMsj("SIGE, Usar: " + clave + " como codigo de validacion en el Colegio "+empresa.getString("giro_negocio")+".");
			int id_msj = mensajeriaFamiliarDAO.saveOrUpdate(mensajeriaFamiliar);
			
			//Map<String,Object> map = new HashMap<String,Object>();
			//map.put("id_msg", id_msg);
			//map.put("clave", clave);
			
			result.setResult(id_msj);
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/generarPswAleatorios", method = RequestMethod.GET)
	public AjaxResponseBody generarPaswAleatorios() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			Integer id_anio=4;
			List<Row> apoderados=familiarDAO.listaApoderado(id_anio);
			for (Row row : apoderados) {
				//Resetear el psw
				String pass=StringUtil.randomInt(4);
				//Integer pass =(int)(Math.random() * 4) + 1;
				familiarDAO.actualizarPswFamiliar(pass, row.getInteger("id_fam"));
			}
			
			result.setResult(1);
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	

	
	@RequestMapping( value="/validarClaveCelular", method = RequestMethod.POST)
	public AjaxResponseBody validarClaveCelular(Integer id_msj,String clave_celular) {
		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//limpiando caracteres extraños
			clave_celular = clave_celular.replaceAll("\\(", "").trim();
			clave_celular = clave_celular.replaceAll("\\)", "");
			clave_celular = clave_celular.replaceAll("-", "");
			clave_celular = clave_celular.replaceAll(" ", "");
			
			
		    MensajeriaFamiliar mensajeria =	mensajeriaFamiliarDAO.get(id_msj);
			
		    if (mensajeria.getClave()!=null && mensajeria.getClave().equals(clave_celular) ){
		    	
		    	int id_fam = mensajeria.getId_des();
		    	//actualizar estado  a "2"
		    	
		    	Familiar familiar = familiarDAO.get(id_fam);
		    	familiar.setIni("2");//ETAPA VALIDACION DE CELULAR
		    	familiar.setCel("1");//CELULAR VALIDADO
		    	familiarDAO.saveOrUpdate(familiar);
		    	
		    	//actualizar token del usuario validado
		    	UsuarioSeg usuarioSeg = new UsuarioSeg();
		    	usuarioSeg.setId_per(EnumPerfil.PERFIL_FAMILIAR.getValue());
				usuarioSeg.setLogin(familiar.getNro_doc());
				usuarioSeg.setPassword(familiar.getPass());
				usuarioSeg.setId(familiar.getId());
				usuarioSeg.setIni(familiar.getIni());
				String nombres = familiar.getApe_mat() + " " + familiar.getApe_pat() + ", " + familiar.getNom();
				usuarioSeg.setNombres(nombres.toUpperCase());
				String token = seguridadService.authenticateAndSignToken(usuarioSeg); 
				result.setResult(token);
		    	
		    }else{
		    	result.setCode("412");
		    	result.setMsg("Lo sentimos, clave invalida, verifique por favor que la clave concuerde con lo enviado a su celular.");
		    }
			

		} catch (Exception e) {
			result.setException(e);
		}

	    return result;
	}

	@RequestMapping( value="/listarPadres", method = RequestMethod.GET)
	public AjaxResponseBody listarPadres(Integer id_gpf) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( familiarDAO.listarFamiliares(id_gpf));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarOtrosFamiliares", method = RequestMethod.GET)
	public AjaxResponseBody listarOtrosPadres(Integer id_gpf) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( familiarDAO.listarOtrosFamiliares(id_gpf));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/obtenerDatosDomiciliariosFamiliar", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosDomiciliariosFamiliar(Integer id_gpf, Integer id_par) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( familiarDAO.obtenerDatosDomiciliariosFamiliar(id_gpf, id_par));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	/** 
	 * Actualizar datos verificados
	 * @param id
	 * @param est
	 * @return
	 */
	@RequestMapping( value="/actualizarDatosVerificados", method = RequestMethod.POST)
	public AjaxResponseBody actualizarDatosVerificados(String cel, String corr, Date fec_emi_dni, String ubigeo, Integer id_fam) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			familiarDAO.actualizarDatosVerficadosFamiliar(cel, corr, fec_emi_dni, ubigeo, id_fam);		
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/busquedaAlumnos1", method = RequestMethod.GET)
	public AjaxResponseBody listarAlumnosTipo(String tipBusqueda, Integer id_anio, Integer id_gir, Integer id_suc, Integer id_niv) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(familiarDAO.listarFamiliasSegunTipo(tipBusqueda, id_anio, id_gir, id_suc, id_niv));
		} catch (Exception e) {
			result.setException(e);
		}
		return result;
	}
	
	@RequestMapping( value="/obtenerDatosFamilia/{cod}/{id_anio}", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosAlumnoxCod(@PathVariable String cod, @PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
				Object respuesta = familiarDAO.datosFamiliaxCodigo(cod, id_anio);
				
				logger.info(respuesta);
				result.setResult(respuesta );
				
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/obtenerDatosFamiliaxUusario/{id_usr}", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosFamiliaxUsuario(@PathVariable Integer id_usr) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
				Object respuesta = familiarDAO.datosFamiliaxUsuario(id_usr);
				
				logger.info(respuesta);
				result.setResult(respuesta );
				
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/obtenerDatosFamiliaxGrupoFamiliar/{id_gpf}", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosFamiliaxGrupoFamiliar(@PathVariable Integer id_gpf) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
				Object respuesta = familiarDAO.datosFamiliaxGrupoFamiliar(id_gpf);
				
				logger.info(respuesta);
				result.setResult(respuesta );
				
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/obtenerDatosFamiliarPersona/{id_fam}/{nro_doc}", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosFamiliarPersona(@PathVariable Integer id_fam, @PathVariable String nro_doc) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
				Object respuesta = familiarDAO.datosFamiliar(id_fam,nro_doc);
				
				logger.info(respuesta);
				result.setResult(respuesta );
				
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/obtenerDatosPersona/{id_per}", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosPersona(@PathVariable Integer id_per) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
				Object respuesta = personaDAO.datosPersona(id_per);
				
				logger.info(respuesta);
				result.setResult(respuesta );
				
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/obtenerDatosPersonaxNroDoc/{nro_doc}", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosPersonaxNroDoc(@PathVariable String nro_doc) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
				Object respuesta = personaDAO.datosPersonaxNro_doc(nro_doc);
				
				logger.info(respuesta);
				result.setResult(respuesta );
				
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/validarApoderado/{id_fam}/{id_anio}", method = RequestMethod.GET)
	public AjaxResponseBody validarApoderadoxAnio(@PathVariable Integer id_fam, @PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
				Object respuesta = familiarDAO.validarApoderadoxAnio(id_anio, id_fam);
				
				logger.info(respuesta);
				result.setResult(respuesta );
				
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
}

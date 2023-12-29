package com.sige.mat.web.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;
import com.sige.common.enums.EnumEstadoCivil;
import com.sige.common.enums.EnumIdioma;
import com.sige.common.enums.EnumParentesco;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.AlumnoDAO;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.ConfFechasDAO;
import com.sige.mat.dao.CriterioNotaDAO;
import com.sige.mat.dao.CronogramaDAO;
import com.sige.mat.dao.EmpresaDAO;
import com.sige.mat.dao.GradDAO;
import com.sige.mat.dao.GruFamAlumnoDAO;
import com.sige.mat.dao.GruFamDAO;
import com.sige.mat.dao.GruFamFamiliarDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.NivelDAO;
import com.sige.mat.dao.PeriodoDAO;
import com.sige.mat.dao.PersonaDAO;
import com.sige.mat.dao.ReglasNegocioDAO;
import com.sige.rest.request.AlumnoReq;

import java.util.List;
import java.util.Map;

import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.GruFam;
import com.tesla.colegio.model.GruFamAlumno;
import com.tesla.colegio.model.GruFamFamiliar;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.Persona;
import com.tesla.colegio.model.ReglasNegocio;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.common.exceptions.ControllerException;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.ExcelXlsUtil;
import com.tesla.frmk.util.JsonUtil;
import com.tesla.frmk.util.StringUtil; 

@RestController
@RequestMapping(value = "/api/alumno")
public class AlumnoRestController {
	
	final static Logger logger = Logger.getLogger(AlumnoRestController.class);

	
	@Autowired
	private MatriculaDAO matriculaDAO;

	@Autowired
	private AlumnoDAO alumnoDAO;
		
	@Autowired
	private PersonaDAO personaDAO;

	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private GruFamAlumnoDAO gruFamAlumnoDAO;

	@Autowired
	private GruFamFamiliarDAO gruFamFamiliarDAO;
	
	@Autowired
	private GruFamDAO gruFamDAO;
	
	@Autowired
	private AnioDAO anioDAO;
	
	@Autowired
	private CronogramaDAO cronogramaDAO; 

	@Autowired
	private ConfFechasDAO confFechasDAO; 
	
	@Autowired
	private CriterioNotaDAO criterioNotaDAO; 
	
	@Autowired
	private EmpresaDAO empresaDAO; 
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private PeriodoDAO periodoDAO;
	
	@Autowired
	private GradDAO gradDAO;
	
	@Autowired
	private NivelDAO nivelDAO;
	
	@Autowired
	private ReglasNegocioDAO reglasNegocioDAO;
	
	@Transactional
	@RequestMapping(value = "/alumnoGrabar", method = RequestMethod.POST)
	public void grabar(Alumno alumno, Persona persona, Integer id_per, Integer id_gpf, HttpSession session, HttpServletResponse response) throws IOException {

		alumno.setUsuario(httpSession.getAttribute("usuario"));

		/*Integer id_gpf = null;
		String id_gpf_request= request.getParameter("id_gpf");
		if (id_gpf_request!=null)
			id_gpf = Integer.parseInt(id_gpf_request);*/

		String error = null;
		
		//validacion de duplicados (tanto para nuevo como para actualizar)
		if (alumnoDAO.existsAlumno(persona.getId(), persona.getNro_doc())) {
			
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("error", "Dni esta siendo usado en otro alumno");
			map.put("id", alumno.getId());
			response.setContentType("application/json");
	        response.getWriter().write(JsonUtil.toJson(map));
	        return;		
		}
		
		
		/*alumno.setApe_mat(alumno.getApe_mat().trim() );
		alumno.setApe_pat(alumno.getApe_pat().trim() );
		alumno.setNom(alumno.getNom().trim() );*/
		persona.setApe_mat(persona.getApe_mat().trim() );
		persona.setApe_pat(persona.getApe_pat().trim() );
		persona.setNom(persona.getNom().trim() );
		persona.setId(id_per);
		
		if(alumno.getId()==null ){
			/*** si el familiar es nuevo se debe crear grupo familiar ***/
			if(id_gpf==null) {
				error ="Debe grabarse el grupo familiar";
			}else {
				
				GruFam gruFam = gruFamDAO.get(id_gpf);
				//2- grabar alumno
				List<GruFamAlumno> list = gruFamAlumnoDAO.listByParams(new Param("id_gpf",id_gpf), null);
				int nroHijo = list.size()+1;
				alumno.setNum_hij(nroHijo);
				alumno.setCod(gruFam.getCod() + nroHijo);
				alumno.setPass_educando(StringUtil.randomString(4));
				alumno.setEst("A");
				Integer id_alu = alumnoDAO.saveOrUpdate(alumno);
				alumno.setId(id_alu);
				
				//3-grabar NUEVO Grupo familiar/Familia
				GruFamAlumno gru_fam_alumno = new GruFamAlumno();
				gru_fam_alumno.setEst("A");
				gru_fam_alumno.setId_alu(id_alu);
				gru_fam_alumno.setId_gpf(id_gpf);
				gruFamAlumnoDAO.saveOrUpdate(gru_fam_alumno);				
			}

		}else{
			//Actualizamos a la persona
			personaDAO.saveOrUpdate(persona);
			alumnoDAO.saveOrUpdate(alumno);
		}

		if(alumno.getId_anio_act()!=null)
			alumnoDAO.actualizarAnioAlumnoActualizacion(alumno.getId_anio_act(), persona.getId());

		Map<String, Object> map = new HashMap<String,Object>();
		map.put("error", error);
		map.put("id", alumno.getId());
		map.put("id_gpf", id_gpf);
		map.put("cod", alumno.getCod());
		
		response.setContentType("application/json");
        response.getWriter().write(JsonUtil.toJson(map));

	}
	
	@Transactional
	@RequestMapping(value = "/alumnoActualizacion", method = RequestMethod.POST)
	public AjaxResponseBody grabar(Persona persona,Integer id_gpf, String email_inst,String cod,Integer id_anio,Integer id_fam_emer,Integer id_idio1, Integer id_idio2, HttpSession session, HttpServletResponse response) throws IOException {
		
		AjaxResponseBody result = new AjaxResponseBody();
		Integer id_per=null;
		Alumno alumno=new Alumno();
		Integer id_gpf_ant=null;
		String error = null;
		//Verificar si existe la persona con el numero de DNI
		Persona persona_existe = personaDAO.getByParams(new Param("nro_doc", persona.getNro_doc()));
		if(persona_existe!=null) {
			persona.setId(persona_existe.getId());
			//Verifico si ya existe como alumno
			Alumno alumno_existe = alumnoDAO.getByParams(new Param("id_per",persona_existe.getId()));
			if(alumno_existe!=null) {
				cod=alumno_existe.getCod();
			}
		}
		if(!cod.equals("")) {
			List<Alumno> alumno_datos=alumnoDAO.listFullByParams(new Param("cod",cod),null);
			if(alumno_datos.size()>0) {
				alumno=alumno_datos.get(0);
				id_per= alumno.getId_per();
				id_gpf_ant=alumno.getGruFamAlumno().getId_gpf();
				//Actualizamos datos de la persona
				persona.setId(id_per);
				personaDAO.saveOrUpdate(persona);
				//Actualizamos datos del alumno
				alumno.setId_idio1(id_idio1);
				alumno.setId_idio2(id_idio2);
				alumno.setEmail_inst(email_inst);
				alumnoDAO.saveOrUpdate(alumno);
				//Vemos si tiene creado un usuario
				String usuario = alumno.getUsuario();
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
					//Insertamos el usuario
					alumnoDAO.actualizarUsuarioAlumno(alumno.getId(), usuario,null);
				}
				//Actualizar el grupo familiar si es otro
				if(id_gpf!=id_gpf_ant)
					gruFamAlumnoDAO.actualizarGrupoFamiliar(id_gpf, alumno.getId());
				//Actualizar los privilegios de la matricula
				Param param = new Param();
				param.put("alu.id", alumno.getId());
				param.put("pee.id_anio", id_anio);
				List<Matricula> matricula= matriculaDAO.listFullByParams(param, null);
				if(matricula.size()>0)
					matriculaDAO.actualizarFamiliarEmergencia(matricula.get(0).getId(), id_fam_emer);
			} 
	 } else {
		 if(persona.getId()==null && cod.equals("")) {
				/*** si el alumno es nuevo se debe crear grupo familiar ***/
				if(id_gpf==null) {
					error ="Debe grabarse el grupo familiar";
				}else {
					GruFam gruFam = gruFamDAO.get(id_gpf);
					//1- Grabar Persona
					id_per=personaDAO.saveOrUpdate(persona);
					
					//2- grabar alumno
					List<GruFamAlumno> list = gruFamAlumnoDAO.listByParams(new Param("id_gpf",id_gpf), null);
					int nroHijo = list.size()+1;
					alumno.setNum_hij(nroHijo);
					alumno.setId_per(id_per);
					cod=gruFam.getCod() + nroHijo;
					alumno.setCod(cod);
					alumno.setPass_educando(StringUtil.randomString(4));
					alumno.setEst("A");
					Integer id_alu = alumnoDAO.saveOrUpdate(alumno);
					alumno.setId(id_alu);
					
					//Vemos si tiene creado un usuario
					String usuario = alumno.getUsuario();
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
						//Insertamos el usuario
						alumnoDAO.actualizarUsuarioAlumno(id_alu, usuario,null);
					}
					
					//3-grabar NUEVO Grupo familiar/Familia
					GruFamAlumno gru_fam_alumno = new GruFamAlumno();
					gru_fam_alumno.setEst("A");
					gru_fam_alumno.setId_alu(id_alu);
					gru_fam_alumno.setId_gpf(id_gpf);
					gruFamAlumnoDAO.saveOrUpdate(gru_fam_alumno);	
				
			}
		} else if(persona.getId()!=null && cod.equals("")) {
			if(id_gpf==null) {
				error ="Debe grabarse el grupo familiar";
			}else {
				GruFam gruFam = gruFamDAO.get(id_gpf);
				//1- Grabar Persona
				id_per=personaDAO.saveOrUpdate(persona);
				
				//2- grabar alumno
				List<GruFamAlumno> list = gruFamAlumnoDAO.listByParams(new Param("id_gpf",id_gpf), null);
				int nroHijo = list.size()+1;
				alumno.setNum_hij(nroHijo);
				alumno.setId_per(id_per);
				cod=gruFam.getCod() + nroHijo;
				alumno.setCod(cod);
				alumno.setPass_educando(StringUtil.randomString(4));
				alumno.setEst("A");
				Integer id_alu = alumnoDAO.saveOrUpdate(alumno);
				alumno.setId(id_alu);
				
				//Vemos si tiene creado un usuario
				String usuario = alumno.getUsuario();
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
					//Insertamos el usuario
					alumnoDAO.actualizarUsuarioAlumno(id_alu, usuario,null);
				}
				
				//3-grabar NUEVO Grupo familiar/Familia
				GruFamAlumno gru_fam_alumno = new GruFamAlumno();
				gru_fam_alumno.setEst("A");
				gru_fam_alumno.setId_alu(id_alu);
				gru_fam_alumno.setId_gpf(id_gpf);
				gruFamAlumnoDAO.saveOrUpdate(gru_fam_alumno);	
			
		}
		}
	 }
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("cod", cod);
		map.put("ape_pat", persona.getApe_pat());
		map.put("ape_mat", persona.getApe_mat());
		map.put("nom", persona.getNom());
		result.setResult(map);
		return result;
	}
	
	@Transactional
	@RequestMapping(value = "/alumnoActualiarDatosV2", method = RequestMethod.POST)
	public AjaxResponseBody actualizarDatos(@RequestBody AlumnoReq alumnoReq, HttpSession session, HttpServletResponse response) throws IOException {
		
		AjaxResponseBody result = new AjaxResponseBody();
		try{
		//Llenamos datos de la persona
		Persona persona = personaDAO.get(alumnoReq.getId_per());
		//familiar.setId(familiarReq.getId());
		persona.setId_tdc(alumnoReq.getId_tdc().toString());
		persona.setNro_doc(alumnoReq.getNro_doc());
		persona.setUbigeo(alumnoReq.getUbigeo());
		persona.setId_gen(alumnoReq.getId_gen().toString()); 
		persona.setId_eci(alumnoReq.getId_eci()); 
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		persona.setFec_nac(formatter.parse(alumnoReq.getFec_nac()));
		if(alumnoReq.getFec_def()!=null && alumnoReq.getFec_def()!="")
		persona.setFec_def(formatter.parse(alumnoReq.getFec_def()));
		persona.setViv(alumnoReq.getViv());
		persona.setTlf(alumnoReq.getTlf());
		persona.setCorr(alumnoReq.getCorr());
		persona.setCel(alumnoReq.getCel());
		persona.setId_pais_nac(alumnoReq.getId_pais_nac());
		persona.setId_dist_nac(alumnoReq.getId_dist_nac());
		//persona.setFace(familiarReq.getFace());
		//persona.setIstrg(familiarReq.getIstrg());
		//persona.setTwitter(familiarReq.getTwitter());
		persona.setId_cond(alumnoReq.getId_cond());
		persona.setId_rel(alumnoReq.getId_rel());
		persona.setTrab(alumnoReq.getTrab());
		Integer id_per=personaDAO.saveOrUpdate(persona);
		//Actualizo al alumno
		Alumno alumno = alumnoDAO.get(alumnoReq.getId_alu());
		alumno.setId_idio1(alumnoReq.getId_idio1());
		alumno.setId_idio2(alumnoReq.getId_idio2());
		alumno.setId_anio_act(alumnoReq.getId_anio());
		Integer id_alu=alumnoDAO.saveOrUpdate(alumno);
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("id_alu", id_alu);
		result.setResult(map);
		return result;
		
		}catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e.getMessage());
		}
	
	}	
	
	@Transactional
	@RequestMapping(value = "/alumnoGenerarUsuarios", method = RequestMethod.POST)
	public void generarUsuariosAlu(Integer[] id , Integer id_anio, HttpSession session, HttpServletResponse response) throws IOException {

		/*Integer id_gpf = null;
		String id_gpf_request= request.getParameter("id_gpf");
		if (id_gpf_request!=null)
			id_gpf = Integer.parseInt(id_gpf_request);*/
		Map<String, Object> map = new HashMap<String,Object>();
		Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);
		for (int i = 0; i < id.length; i++) {
			Integer id_gra = id[i];
			List<Row> alumnos_periodo=matriculaDAO.listarAlumnosxGradoAnio(id_gra, id_anio);
			for (Row row : alumnos_periodo) {
				String usuario=null;
				if(row.get("usuario")==null){
					usuario=StringUtil.replaceTilde(row.getString("nom").replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(row.getString("ape_pat")).toLowerCase().replace("ñ", "n").replace(" ", "")+"@"+empresa.getString("dominio");
					Boolean existe_usuario=alumnoDAO.existeUsuario(usuario);
					if(existe_usuario){
						usuario=StringUtil.replaceTilde(row.getString("nom").replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(row.getString("ape_pat")).toLowerCase().replace("ñ", "n").replace(" ", "")+"1@"+empresa.getString("dominio");
						existe_usuario=alumnoDAO.existeUsuario(usuario);
						if(existe_usuario){
							usuario=StringUtil.replaceTilde(row.getString("nom").replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(row.getString("ape_pat")).toLowerCase().replace("ñ", "n").replace(" ", "")+"2@"+empresa.getString("dominio");
							existe_usuario=alumnoDAO.existeUsuario(usuario);
							if(existe_usuario){
								usuario=StringUtil.replaceTilde(row.getString("nom").replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(row.getString("ape_pat")).toLowerCase().replace("ñ", "n").replace(" ", "")+"3@"+empresa.getString("dominio");
								existe_usuario=alumnoDAO.existeUsuario(usuario);
								if(existe_usuario){
									usuario=StringUtil.replaceTilde(row.getString("nom").replace(" ","")).toLowerCase().replace("ñ", "n").replace(" ", "")+"."+StringUtil.replaceTilde(row.getString("ape_pat")).toLowerCase().replace("ñ", "n").replace(" ", "")+"4@"+empresa.getString("dominio");	
								}
							}
						}
					} 
					//Insertamos el usuario
					alumnoDAO.actualizarUsuarioAlumno(row.getInteger("id"), usuario, null);
				}
			}
			//actualizamos el estado del periodo
			gradDAO.actualizarGeneracionUsuario(id_anio, id_gra);
			
			map.put("id_gra", id_gra);
		}
		
		response.setContentType("application/json");
        response.getWriter().write(JsonUtil.toJson(map));	

	}
	
	@RequestMapping(value = "/exportarFormatoUsuariosAlu")
	@ResponseBody
	public void descargarExcel(HttpServletRequest request,HttpServletResponse response,Integer id_gra, Integer id_anio)  throws Exception {
	  
		//List<Row> list_alumnos_procesados = new ArrayList<>();
		//Generamos el excexl
		//for (int i = 0; i < id.length; i++) {
		//	Integer id_per = id[i];
			List<Row> list_alumnos_procesados=matriculaDAO.listarAlumnosxGradoAnio(id_gra, id_anio);
		/*	Row alumnos_periodo = new Row();
			alumnos_periodo.put("nom", alumnos);
			list_alumnos_procesados.add(alumnos_periodo);
		}*/
		
		Grad grad=gradDAO.getByParams(new Param("id",id_gra));
		Nivel nivel=nivelDAO.getByParams(new Param("id",grad.getId_nvl()));
		response.setContentType("application/vnd.ms-excel");
		ExcelXlsUtil xls = new ExcelXlsUtil();
		String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();
		//String  rutacARPETA =  "C:/plantillas/";
		
		String archivo = xls.generaExcelFormatoUsuarioAlu(rutacARPETA, "formato_generacion_usr_alu.xls", list_alumnos_procesados);
        DateFormat dateFormat = new SimpleDateFormat("mm-dd hh-mm-ss");  

		response.setHeader("Content-Disposition","attachment;filename=Formato_Usuarios_"+grad.getNom()+"_"+nivel.getNom()+ dateFormat.format(new Date())  + ".xls");

		File initialFile = new File(archivo);
	    InputStream is = FileUtils.openInputStream(initialFile);
	    	
		IOUtils.copy(is, response.getOutputStream());
		
	}	
	
	@RequestMapping(method = RequestMethod.POST, value = "/xls/uploadPsw")
	public AjaxResponseBody uploadFile(@RequestParam("file") MultipartFile uploadfile,Integer id_oli) {

		AjaxResponseBody result = new AjaxResponseBody();

		if (uploadfile.isEmpty()) {
			result.setCode("ARCHIVO");
			result.setMsg("archivo es vacio");
			return result;
		}

		try {

			InputStream is = uploadfile.getInputStream();
			//resultadosDAO.calcularResultados(id_oli);
			 procesarPsw(is);
			result.setResult("procesado");
			return result;
			
		} catch (Exception e) {
			result.setCode("ARCHIVO");
			result.setMsg("archivo con errores:" + e.getMessage());
			//logger.error("metodo:uploadFile",e);
			return result;
		}

	}
	
	@Transactional
	public void procesarPsw(InputStream inputStream) throws ControllerException{
		
		int columnaIncial=0;
		
		int linea = 0;

		try {
		
			//FileInputStream inputStream = new FileInputStream(new File(archivo));
			Workbook workbook = WorkbookFactory.create(inputStream);

			Sheet sheet = workbook.getSheetAt(0);

			String datos= sheet.getRow(linea).getCell(0).getStringCellValue();
			String datos_alu[]=null;
			String usuario=null;
			String psw=null;
			while(datos!=null && !"".equals(datos) ){
				linea++;
				//System.out.println(("linea"+linea));
				if(sheet.getRow(linea)==null || sheet.getRow(linea).getCell(0)==null){
					break;	
				}
				
				if(sheet.getRow(linea).getCell(columnaIncial)!=null){
					datos_alu= sheet.getRow(linea).getCell(columnaIncial).getStringCellValue().split(",");
					usuario=datos_alu[1];
					psw=datos_alu[2];
				}

				//Actualizar el psw
				alumnoDAO.actualizarPswAlumno(usuario, psw);
			}
					
			inputStream.close();

			workbook.close();

		} catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException("Registro nro:" + linea + ", error: " +e.getMessage());
		}
}
		
	
	@RequestMapping(value = "/consultar")
	public AjaxResponseBody getSearchResultViaAjax(@RequestParam String alumno,@RequestParam Integer id_anio,@RequestParam Integer id_suc) {

		AjaxResponseBody result = new AjaxResponseBody();		 
		result.setResult( matriculaDAO.matriculadosLista(alumno, id_anio,id_suc));
		
		return result;

	}
	

	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( alumnoDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	
	@RequestMapping(value = "/matriculadosCombo")
	public Object autocompleteAjax(@RequestParam Integer id_au) {
		AjaxResponseBody result = new AjaxResponseBody();
		result.setResult( matriculaDAO.matriculadosCombo(id_au));
		return result;
	}

	//alumnos matriculados
	@RequestMapping(value = "/autocomplete")
	public Object autocompleteAjax(@RequestParam String term, @RequestParam Integer id_anio) {
		return matriculaDAO.alumnosCombo(term,  id_anio);
	}
	
	@RequestMapping(value = "/autocompleteCondicionConductual")
	@JsonView
	public Object autocompleteAjax(@RequestParam String term, @RequestParam Integer id_anio, Integer id_rol, Integer id_usr) {
		return matriculaDAO.matriculadosxUsuario(term, id_anio, id_rol, id_usr);

	}
	
	@RequestMapping(value = "/actualizarContrasenia", method = RequestMethod.POST)
	public AjaxResponseBody obtenerDatosDomiciliariosFamiliar(String pass_educando,Integer id_alu) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			alumnoDAO.actualizarPswAlumnoxId(id_alu, pass_educando);
			result.setResult("1");
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}

	
	@RequestMapping(value = "/autocompleteParaDescuento")
	@JsonView
	public Object autocompleteParaDescuentoAjax(@RequestParam String term,@RequestParam Integer id_anio,@RequestParam Integer id_suc) {
		 
		return matriculaDAO.matriculadosParaDescuentoCombo(term, id_anio,id_suc);
		
	}

	@RequestMapping(value = "/foto/{id}")
	@ResponseBody
	public void viewFile(HttpServletResponse request,HttpServletResponse response,@PathVariable Integer id)  throws IOException {
	  
		Alumno alumno = alumnoDAO.getPhoto(id);
		
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		
		if(alumno.getFoto()!=null) {
			ByteArrayInputStream in = new ByteArrayInputStream(alumno.getFoto());
			IOUtils.copy(in, response.getOutputStream());
		}else {
			String genero = alumno.getId_gen(); 
			InputStream is = null ;
			if (Constante.GENERO_FEMENINO.equals(genero))
				is = httpSession.getServletContext().getResourceAsStream("/WEB-INF/views/img/ninia.png");
			else
				is = httpSession.getServletContext().getResourceAsStream("/WEB-INF/views/img/ninio.png");
			
			IOUtils.copy(is, response.getOutputStream());
		}
	}	
	
	@RequestMapping( value="/listarFamiliares", method = RequestMethod.GET)
	public AjaxResponseBody listarFamiliares( Integer id_alu) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			if(id_alu!=null)
				result.setResult( alumnoDAO.listApoderados(id_alu));
			else
				result.setResult(new ArrayList<>());
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarFamiliaresPMxGruFam", method = RequestMethod.GET)
	public AjaxResponseBody listarFamiliaresPMxGruFam( Integer id_gpf) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			if(id_gpf!=null)
				result.setResult( alumnoDAO.listApoderadosxGrupoFamiliar(id_gpf));
			else
				result.setResult(new ArrayList<>());
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarTodosFamiliares", method = RequestMethod.GET)
	public AjaxResponseBody listarTodosFamiliares(Integer id_gpf) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//if(id_alu!=null)
				result.setResult( alumnoDAO.listTodosFamiliares(id_gpf));
			/*else
				result.setResult(new ArrayList<>());*/
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarTodosFamiliaresxGruFam", method = RequestMethod.GET)
	public AjaxResponseBody listarTodosFamiliaresxGruFam(Integer id_gpf) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//if(id_alu!=null)
				result.setResult( alumnoDAO.listTodosFamiliaresxGrupoFam(id_gpf));
			/*else
				result.setResult(new ArrayList<>());*/
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarTodosIntegrantesFamilia", method = RequestMethod.GET)
	public AjaxResponseBody listarTodosIntegrantesFamilia(Integer id_gpf, Integer id_alu) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			if(id_alu!=null)
				result.setResult( alumnoDAO.listTodosIntegrantesFamilia(id_gpf, id_alu));
			else
				result.setResult(new ArrayList<>());
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarPadres", method = RequestMethod.GET)
	public AjaxResponseBody listarApoderadores( Integer id_alu) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			if(id_alu!=null)
				result.setResult( alumnoDAO.listPadres(id_alu));
			else
				result.setResult(new ArrayList<>());
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarResponsablesMat", method = RequestMethod.GET)
	public AjaxResponseBody listarResponsablesMat( Integer id_mat) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			if(id_mat!=null)
				result.setResult( alumnoDAO.listarResponsablesMat(id_mat));
			else
				result.setResult(new ArrayList<>());
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarOtrosFamiliares", method = RequestMethod.GET)
	public AjaxResponseBody listarOtrosFamiliares( Integer id_alu) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			if(id_alu!=null)
				result.setResult( alumnoDAO.listOtrosFamiliares(id_alu));
			else
				result.setResult(new ArrayList<>());
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/obtenerDatosAlumno/{id_mat}", method = RequestMethod.GET)
	public AjaxResponseBody datosAlumno(@PathVariable Integer id_mat) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
				Object respuesta = alumnoDAO.datosAlumno(id_mat).get(0);
				
				logger.info(respuesta);
				result.setResult(respuesta );
				
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/obtenerDatosAlumnoxId/{id_alu}", method = RequestMethod.GET)
	public AjaxResponseBody datosAlumnoxId(@PathVariable Integer id_alu) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
				Object respuesta = alumnoDAO.datosAlumnoxId(id_alu);
				
				logger.info(respuesta);
				result.setResult(respuesta );
				
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}

	@RequestMapping(value = "/grupoFamiliar/alumnos", method = RequestMethod.GET)
	public AjaxResponseBody grupoFamiliarAlumnos(Integer id_gpf, Integer id_alu) {
		
		logger.debug("id_alu:" + id_alu);
		
		AjaxResponseBody result = new AjaxResponseBody();
		Map<String,Object> map = listarAlumnos( id_gpf, id_alu);
		result.setResult(map);

		return result;
	}
	
	
	private Map<String,Object> listarAlumnos(Integer id_gpf, Integer id_alu) {

		Map<String,Object> result = new HashMap<String,Object>();
		
		/** Listamos todos los alumnos del grupo familiar ***/
		List<GruFamAlumno> alumnoList = new ArrayList<GruFamAlumno>();
		List<GruFamFamiliar> familiarList = new ArrayList<GruFamFamiliar>();
		Familiar papa = null;
		Familiar mama = null;
		
		
		if (id_gpf != null) {
			alumnoList = gruFamAlumnoDAO.listFullByParams(new Param("id_gpf", id_gpf), null);
			familiarList = gruFamFamiliarDAO.listFullByParams(new Param("id_gpf", id_gpf), null);
		}
		
		Alumno alumnoModel = new Alumno();
		Persona personaModel = new Persona();

		/*alumnoModel.setId_tdc(Constante.TIPO_DOCUMENTO_DNI);
		alumnoModel.setId_eci(EnumEstadoCivil.SOLTERO.getValue());*///Soltero TODO agregarlo a constante
		personaModel.setId_tdc(Constante.TIPO_DOCUMENTO_DNI.toString());
		personaModel.setId_eci(EnumEstadoCivil.SOLTERO.getValue());//Soltero TODO agregarlo a constante
		alumnoModel.setId_idio1(EnumIdioma.CASTELLANO.getValue());//castellano
		
		for (GruFamFamiliar gruFamFamiliar : familiarList) {
			if(gruFamFamiliar.getFamiliar().getId_par().intValue() == EnumParentesco.PARENTESCO_PAPA.getValue())
				papa =gruFamFamiliar.getFamiliar();
			if(gruFamFamiliar.getFamiliar().getId_par().intValue() == EnumParentesco.PARENTESCO_MAMA.getValue())
				mama =gruFamFamiliar.getFamiliar();
		}

		
		for (GruFamAlumno gruFamAlumno : alumnoList) {
			logger.debug("gruFamAlumno.getAlumno().getId():" + gruFamAlumno.getAlumno().getId());
			if (id_alu != null && gruFamAlumno.getAlumno().getId().equals(id_alu)) {
				alumnoModel = gruFamAlumno.getAlumno();
				personaModel = gruFamAlumno.getPersona();
				logger.debug("alumnoModel:" + alumnoModel);
				
			}
		}

		if(id_alu==null){
			if (alumnoList.size() > 0)
				alumnoModel = alumnoList.get(0).getAlumno();
			else {
				//todavia no se ingresa alumno
				if(papa!=null && mama==null){
					alumnoModel.setApe_pat(papa.getApe_pat());
					alumnoModel.setApe_mat(papa.getApe_mat());
				} else if(mama!=null && papa==null){
					alumnoModel.setApe_pat(mama.getApe_pat());
					alumnoModel.setApe_mat(mama.getApe_mat());
				} else if(papa!=null && mama!=null){
					alumnoModel.setApe_pat(papa.getApe_pat());
					alumnoModel.setApe_mat(mama.getApe_pat());
				}
					
			}
		}

		if (alumnoModel.getId_tap()==null)
			alumnoModel.setId_tap("A");//ambos
		
		logger.debug("alumnoModel:" + alumnoModel);
		
		//devolver padre y madre
		result.put("padre", papa);
		result.put("madre", mama);
		result.put("id_gpf", id_gpf);
		// primer alumno
		result.put("alumno", alumnoModel);
		result.put("persona", personaModel);

		// Listar mama y papa
		result.put("alumnoList", alumnoList);
		
		return result;
	}

	@RequestMapping( value="/listarAlumnos", method = RequestMethod.GET)
	public AjaxResponseBody listarPadres(Integer id_gpf, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(alumnoDAO.listarAlumnos(id_gpf, id_anio));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarAlumnosAntNuevosRecparaMatriculaAct", method = RequestMethod.GET)
	public AjaxResponseBody listarAlumnosAntNuevosRecparaMatriculaAct(Integer id_gpf, Integer id_anio_ant, Integer id_anio_act) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(alumnoDAO.listarAlumnosMatriculaAnterioresAprobadosyRecuperacionNuevos(id_gpf, id_anio_ant, id_anio_act));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarHijosxUsuario", method = RequestMethod.GET)
	public AjaxResponseBody listarHijosxUsuario(Integer id_usr, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(alumnoDAO.listarHijosxUsuario(id_usr, id_anio));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarHijosxUsuarioRatificacion", method = RequestMethod.GET)
	public AjaxResponseBody listarHijosxUsuarioRatificacion(Integer id_usr, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(alumnoDAO.listarHijosxUsuarioRatificacion(id_usr, id_anio));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	/**
	 * lista a los hermanos segun cronograma y local
	 * @param id_gpf
	 * @param id_suc
	 * @param id_anio
	 * @return
	 */
	@RequestMapping( value="/listarHermanosxLocal", method = RequestMethod.GET)
	public AjaxResponseBody listarPadres(Integer id_gpf, Integer id_suc, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//result.setResult(alumnoDAO.listarHermanosxLocal(id_gpf, id_suc));
			Anio anio = anioDAO.get(id_anio);
			int anio_anterior = Integer.parseInt(anio.getNom()) - 1;
			Param param = new Param();
			param.put("nom", anio_anterior);
			Anio anioAnterior = anioDAO.getByParams(param);
			ReglasNegocio matr = reglasNegocioDAO.getByParams(new Param("cod",Constante.MATR_SEGUN_CRONOGRAMA));
			String matricula_segun_cronograma=matr.getVal();

			//Buscar la regla de negocio 
			if(matricula_segun_cronograma.equals("0")){
				result.setResult(alumnoDAO.listarHermanosxLocal(null, id_gpf, id_anio, anioAnterior.getId(), id_suc));
			} else if(matricula_segun_cronograma.equals("1")){
				//ALUMNOS ANTIGUOS - CON CRONOGRAMA
				boolean antiguo_con_cronograma = cronogramaDAO.alumnosAntiguosTienenVigente(id_anio);

				//ALUMNOS ANTIGUOS - SIN CRONOGRAMA
				boolean antiguo_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "AS");

						
				//ALUMNOS NUEVOS SIN CRONOGRAMA
				boolean nuevos_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "NS");
				
				//ALUMNOS NUEVOS CON CRONOGRAMA 
				boolean nuevos_cronograma = confFechasDAO.cronogramaVigente(id_anio, "NC");
				
				Map<String,Object> map = new HashMap<String,Object>();
				
				if (antiguo_con_cronograma){
					logger.debug("antiguo_con_cronograma");
					
					result.setResult(alumnoDAO.listarHermanosxLocal("AC", id_gpf, id_anio, anioAnterior.getId(), id_suc));
					
				}else if (antiguo_sin_cronograma && nuevos_sin_cronograma){
					logger.debug("antiguo_sin_cronograma Y nuevos_cronograma");
					result.setResult(alumnoDAO.listarHermanosxLocal("ASNS",id_gpf, id_anio, anioAnterior.getId(), id_suc));				
						
				}else if (antiguo_sin_cronograma && !nuevos_cronograma){

					logger.debug("antiguo_SIN_cronograma");
					
					result.setResult(alumnoDAO.listarHermanosxLocal("AS",id_gpf, id_anio, anioAnterior.getId(), id_suc));
					
				}else{
					if(nuevos_cronograma || nuevos_sin_cronograma){
						logger.debug("nuevos_cronograma");
						result.setResult(alumnoDAO.listarHermanosxLocal("NC",id_gpf, id_anio, anioAnterior.getId(), id_suc));					
					}
					
				}
				
			}

		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarHermanosxGruFam", method = RequestMethod.GET)
	public AjaxResponseBody listarHermanosxGruFam(Integer id_gpf, Integer id_suc, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//result.setResult(alumnoDAO.listarHermanosxLocal(id_gpf, id_suc));
			Anio anio = anioDAO.get(id_anio);
			int anio_anterior = Integer.parseInt(anio.getNom()) - 1;
			Param param = new Param();
			param.put("nom", anio_anterior);
			Anio anioAnterior = anioDAO.getByParams(param);
			ReglasNegocio matr = reglasNegocioDAO.getByParams(new Param("cod",Constante.MATR_SEGUN_CRONOGRAMA));
			String matricula_segun_cronograma=matr.getVal();

			//Buscar la regla de negocio 
			if(matricula_segun_cronograma.equals("0")){
				result.setResult(alumnoDAO.listarHermanosxLocal(null, id_gpf, id_anio, anioAnterior.getId(), id_suc));
			} else if(matricula_segun_cronograma.equals("1")){
				//ALUMNOS ANTIGUOS - CON CRONOGRAMA
				boolean antiguo_con_cronograma = cronogramaDAO.alumnosAntiguosTienenVigente(id_anio);

				//ALUMNOS ANTIGUOS - SIN CRONOGRAMA
				boolean antiguo_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "AS");

						
				//ALUMNOS NUEVOS SIN CRONOGRAMA
				boolean nuevos_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "NS");
				
				//ALUMNOS NUEVOS CON CRONOGRAMA 
				boolean nuevos_cronograma = confFechasDAO.cronogramaVigente(id_anio, "NC");
				
				Map<String,Object> map = new HashMap<String,Object>();
				
				if (antiguo_con_cronograma){
					logger.debug("antiguo_con_cronograma");
					
					result.setResult(alumnoDAO.listarTodosHermanosxGruFamparaWeb("AC", id_gpf, id_anio, anioAnterior.getId()));
					
				}else if (antiguo_sin_cronograma && nuevos_sin_cronograma){
					logger.debug("antiguo_sin_cronograma Y nuevos_cronograma");
					result.setResult(alumnoDAO.listarTodosHermanosxGruFamparaWeb("ASNS", id_gpf, id_anio, anioAnterior.getId()));
					//result.setResult(alumnoDAO.listarHermanosxLocal("ASNS",id_gpf, id_anio, anioAnterior.getId(), id_suc));				
						
				}else if (antiguo_sin_cronograma && !nuevos_cronograma){

					logger.debug("antiguo_SIN_cronograma");
					result.setResult(alumnoDAO.listarTodosHermanosxGruFamparaWeb("AS",id_gpf, id_anio, anioAnterior.getId()));
					//result.setResult(alumnoDAO.listarHermanosxLocal("AS",id_gpf, id_anio, anioAnterior.getId(), id_suc));
					
				}else{
					if(nuevos_cronograma || nuevos_sin_cronograma){
						logger.debug("nuevos_cronograma");
						result.setResult(alumnoDAO.listarTodosHermanosxGruFamparaWeb("NC",id_gpf, id_anio, anioAnterior.getId()));
						//result.setResult(alumnoDAO.listarHermanosxLocal("NC",id_gpf, id_anio, anioAnterior.getId(), id_suc));					
					}
					
				}
				
			}

		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarTodosAlumnos", method = RequestMethod.GET)
	public AjaxResponseBody listarTodosAlumnos(String apellidosNombres) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(alumnoDAO.listarTodosAlumnos(apellidosNombres));
		} catch (Exception e) {
			result.setException(e);
		}
		return result;
	}
	
	@RequestMapping( value="/listarTodosHijosMatriculadosxFamilia", method = RequestMethod.GET)
	public AjaxResponseBody listarTodosHijosMatriculadosxFamilia(Integer id_gpf, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(alumnoDAO.listarMatriculadosxFamilia(id_gpf, id_anio));
		} catch (Exception e) {
			result.setException(e);
		}
		return result;
	}
	
	@RequestMapping( value="/datosAlumnoEvaPsico", method = RequestMethod.GET)
	public AjaxResponseBody datosAlumnoEvaPsico(Integer id_alu) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(criterioNotaDAO.datos_Alumnos(id_alu));
		} catch (Exception e) {
			result.setException(e);
		}
		return result;
	}
	
	@RequestMapping( value="/busquedaAlumnos", method = RequestMethod.GET)
	public AjaxResponseBody listarAlumnosTipo(String tipBusqueda, Integer id_anio, Integer id_gir, Integer id_suc, Integer id_niv) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(alumnoDAO.listarAlumnosSegunTipo(tipBusqueda, id_anio, id_gir, id_suc, id_niv));
		} catch (Exception e) {
			result.setException(e);
		}
		return result;
	}
	
	@RequestMapping( value="/obtenerDatosAlumnoxCod/{cod}/{id_anio}", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosAlumnoxCod(@PathVariable String cod, @PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
				Object respuesta = alumnoDAO.datosAlumnoxCodigo(cod, id_anio);
				
				logger.info(respuesta);
				result.setResult(respuesta );
				
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarFamilias", method = RequestMethod.GET)
	public AjaxResponseBody listarFamilias() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
				Object respuesta = alumnoDAO.listarFamilias();
				
				logger.info(respuesta);
				result.setResult(respuesta );
				
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarMatriculas", method = RequestMethod.GET)
	public AjaxResponseBody listarMatriculas (Integer id_alu) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
				Object respuesta = alumnoDAO.listarMatriculas(id_alu);
				
				logger.info(respuesta);
				result.setResult(respuesta );
				
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarPagosxAlumno", method = RequestMethod.GET)
	public AjaxResponseBody listarPagosxAlumno (Integer id_anio_des, Integer id_per) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
				Object respuesta = alumnoDAO.listaPagosxAlumno(id_anio_des, id_per);
				
				logger.info(respuesta);
				result.setResult(respuesta );
				
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
}


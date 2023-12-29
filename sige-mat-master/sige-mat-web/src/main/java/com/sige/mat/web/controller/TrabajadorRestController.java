package com.sige.mat.web.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.StringUtil;
import com.sige.mat.dao.HisGradoInsDAO;
import com.sige.mat.dao.NivelCoordinadorDAO;
import com.sige.mat.dao.PersonaDAO;
import com.sige.mat.dao.TrabajadorDAO;
import com.sige.mat.dao.UsuarioDAO;
import com.sige.mat.dao.UsuarioRolDAO;
import com.tesla.colegio.model.HisGradoIns;
import com.tesla.colegio.model.NivelCoordinador;
import com.tesla.colegio.model.Persona;
import com.tesla.colegio.model.Trabajador;
import com.tesla.colegio.model.Usuario;
import com.tesla.colegio.model.UsuarioRol;

@RestController
@RequestMapping(value = "/api/trabajador")
public class TrabajadorRestController {
	
	@Autowired
	private TrabajadorDAO trabajadorDAO;
	
	@Autowired
	private UsuarioDAO usuarioDAO;
	
	@Autowired
	private UsuarioRolDAO usuarioRolDAO;
	
	@Autowired
	private PersonaDAO personaDAO;
	
	@Autowired
	private HisGradoInsDAO hisGradoInsDAO;
	
	@Autowired
	private NivelCoordinadorDAO nivelCoordinadorDAO;

	@Autowired
	private HttpSession httpSession;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Trabajador trabajador) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(trabajadorDAO.listFullByParams( trabajador, new String[]{"tra.id"}) );
		
		return result;
	}
	
	@RequestMapping(value = "/listarTodosTrabajadores")
	public AjaxResponseBody listarTodosTrabajadores() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(trabajadorDAO.listarTodosTrabajadores());
		
		return result;
	}
	
	@RequestMapping(value = "/listarDocentesContratoVig")
	public AjaxResponseBody listarDocentesContratoVig() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(trabajadorDAO.listarTodosTrabajadoresDocTut());
		
		return result;
	}
	
	@RequestMapping(value = "/listarAuxiliaresContratoVigente/{id_niv}")
	public AjaxResponseBody listarAuxiliaresConVigente(@PathVariable Integer id_niv) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(trabajadorDAO.listarAuxiliaresConVigente(id_niv));
		
		return result;
	}
	
	@RequestMapping(value = "/listarGradosAcademicos")
	public AjaxResponseBody listarGradosAcademicos(Integer id_tra) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(hisGradoInsDAO.listFullByParams(new Param("id_tra",id_tra),new String[]{"gins.fec_ins"}));
		
		return result;
	}
	
	@RequestMapping(value = "/listarGirosNegocioxTrabajador/{id_tra}/{id_anio}")
	public AjaxResponseBody listarGirosNegocioxTrabajador(@PathVariable Integer id_tra, @PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(trabajadorDAO.listarGirosNegocioxTrabajadorCoordinador(id_tra, id_anio));
		
		return result;
	}
	
	@RequestMapping(value = "/listarGirosNegocioxAuxiliar/{id_tra}/{id_anio}")
	public AjaxResponseBody listarGirosNegocioxAuxiliar(@PathVariable Integer id_tra, @PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(trabajadorDAO.listarGirosNegocioxAuxiliar(id_tra, id_anio));
		
		return result;
	}
	
	@RequestMapping(value = "/listarGirosNegocioxTutor/{id_tut}/{id_anio}")
	public AjaxResponseBody listarGirosNegocioxTutor(@PathVariable Integer id_tut, @PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(trabajadorDAO.listarGirosNegocioxTutor(id_tut, id_anio));
		
		return result;
	}
	
	@RequestMapping(value = "/listarGirosNegocioxDocente/{id_tra}/{id_anio}")
	public AjaxResponseBody listarGirosNegocioxDocente(@PathVariable Integer id_tra, @PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(trabajadorDAO.listarGirosNegocioxTrabajadorDocente(id_tra, id_anio));
		
		return result;
	}
	
	@RequestMapping(value = "/listarGirosNegocioxCoordinadorArea/{id_tra}/{id_anio}")
	public AjaxResponseBody listarGirosNegocioxCoordinadorArea(@PathVariable Integer id_tra, @PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(trabajadorDAO.listarGirosNegocioxCoordinadorArea(id_tra, id_anio));
		
		return result;
	}
	

	@RequestMapping(value = "/listarGirosNivelesxCoordinador/{id_tra}/{id_anio}/{id_gir}")
	public AjaxResponseBody listarGirosNivelesxCoordinador(@PathVariable Integer id_tra, @PathVariable Integer id_anio, @PathVariable Integer id_gir) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(trabajadorDAO.listarNivelesxTrabajadorCoordinador(id_tra, id_anio, id_gir));
		
		return result;
	}
	
	
	/*@RequestMapping(value = "/listarGirosxCoordinador/{id_tra}/{id_anio}")
	public AjaxResponseBody listarGirosxCoordinador(@PathVariable Integer id_tra, @PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(trabajadorDAO.listarGirosxTrabajadorCoordinador(id_tra, id_anio));
		
		return result;
	}*/
	
	@RequestMapping(value = "/listarGirosNivelesAuxiliar/{id_aux}")
	public AjaxResponseBody listarGirosAuxiliar(@PathVariable Integer id_aux) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(trabajadorDAO.listarNivelesxAuxiliar(id_aux));
		
		return result;
	}
	
	@RequestMapping(value = "/listarGirosNivelesxDocente/{id_tra}/{id_anio}/{id_gir}")
	public AjaxResponseBody listarGirosNivelesxDocente(@PathVariable Integer id_tra, @PathVariable Integer id_anio, @PathVariable Integer id_gir) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(trabajadorDAO.listarNivelesxTrabajadorDocente(id_tra, id_anio, id_gir));
		
		return result;
	}
	
	@RequestMapping(value = "/listarCoordinadoresNivelxAnio/{id_anio}")
	public AjaxResponseBody listarCoordinadoresNivelxAnio(@PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(trabajadorDAO.listarCoordinadorNivelxAnio(id_anio));
		
		return result;
	}
	
	@RequestMapping(value = "/listarCoordinadoresNivelUsuario/{id_anio}")
	public AjaxResponseBody listarCoordinadoresNivelUsuario(@PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(trabajadorDAO.listarCoordinadorNivelUsuarioxAnio(id_anio));
		
		return result;
	}
	
	@RequestMapping(value = "/listarAdministradorSedeUsuario/{id_anio}")
	public AjaxResponseBody listarAdministradorSedeUsuario(@PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(trabajadorDAO.listarAdministradorSedeUsuarioxAnio(id_anio));
		
		return result;
	}	
	
	@RequestMapping(value = "/listarHijosTrabajadores")
	public AjaxResponseBody listarHijosTrabajadores(Integer id_anio, Integer id_gir, String tip_con, Integer id_suc) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(trabajadorDAO.listarHijosTrabajadores(id_anio, id_gir, tip_con, id_suc));
		
		return result;
	}
	
	@RequestMapping(value = "/listarContratosxAnio")
	public AjaxResponseBody listarContratosxAnio(Integer id_anio, String tip_con, Integer id_gir) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(trabajadorDAO.listarContratosxAnio(id_anio, tip_con, id_gir));
		
		return result;
	}
	
	

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Trabajador trabajador, Integer id_usr,String password) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		Usuario usuario = usuarioDAO.get(id_usr);

		usuario.setIni("2");//Actualizo sus datos por primera vez
		usuarioDAO.saveOrUpdate(usuario);
		
		Param param= new Param();
		param.put("id_usr",usuario.getId());
		param.put("est", "A");
		List<UsuarioRol> usuariorols= usuarioRolDAO.listByParams(param, null);
		usuario.setUsuariorols(usuariorols);
		
		httpSession.setAttribute("_USUARIO", usuario);

		
		try {
			result.setResult( trabajadorDAO.saveOrUpdate(trabajador) );
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}
		
		return result;

	}	
	
	@Transactional
	@RequestMapping(  value="/grabarTrabajador", method = RequestMethod.POST)
	public AjaxResponseBody grabarTrabajador(Persona persona,Trabajador trabajador,Integer id_per, Integer id) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		try {
			//Grabamos los datos de la Persona
			//Buscamos a la persona si existe
			Persona persona_ex= personaDAO.getByParams(new Param("nro_doc",persona.getNro_doc()));
			if(persona_ex!=null) {
				persona.setId(persona_ex.getId());
			}
			//persona.setId(id_per);
			id_per=personaDAO.saveOrUpdate(persona);
			
			//Grabamos los datos de trabajador
			trabajador.setId(id);
			trabajador.setId_per(id_per);
			trabajador.setEst("A");
			//Generamos un codigo para el trabajador
			if(id==null && trabajador.getCod()==null) {
				String codigo = generarCodigoTrabajador();
				trabajador.setCod(codigo);
			} else if(id!=null) {
				Trabajador trabajador2=trabajadorDAO.get(id);	
				trabajador.setCod(trabajador2.getCod());
			}
			
			String login= null;
			String pass=null;
			//Aun voy a consultarlo
			/*
			if(trabajador.getId_usr()==null) {
				///Generamos el usuario 
				login=persona.getNom().substring(0)+""+persona.getApe_pat();
				//Validamos si existe el login o no
				Usuario usr = usuarioDAO.getByParams(new Param("login",login));
				if(usr!=null) {
					login=persona.getNom().substring(0)+""+persona.getApe_mat().substring(0)+""+persona.getApe_pat();
					//Validamos si existe el login o no
					Usuario usr2 = usuarioDAO.getByParams(new Param("login",login));
					if(usr2!=null) {
						login=persona.getNom().substring(0)+""+persona.getApe_pat()+"1";
						//Validamos si existe el login o no
						Usuario usr3 = usuarioDAO.getByParams(new Param("login",login));
						if(usr3!=null) {
							login=persona.getNom().substring(0)+""+persona.getApe_pat()+"2";
						}
					}
				}
				pass=StringUtil.randomInt(6);
				Usuario usuario = new Usuario();
				usuario.setId_per(Constante.PERFIL_TRABAJADOR);
				usuario.setLogin(login);
				usuario.setPassword(pass);
				usuario.setEst("A");
				usuario.setTipo("I");
				Integer id_usr=usuarioDAO.saveOrUpdate(usuario);
				trabajador.setId_usr(id_usr);
			} */
				
				
			Integer id_tra=trabajadorDAO.saveOrUpdate(trabajador);
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("id", id_tra);
			map.put("id_per",id_per);
			map.put("trabajador",persona.getApe_pat()+" "+persona.getApe_mat()+" "+persona.getNom());
			result.setResult(map);
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}
		
		return result;

	}	
	
	@Transactional
	@RequestMapping(  value="/grabarHistorialGradoIns", method = RequestMethod.POST)
	public AjaxResponseBody grabarHistorialGradoIns(HisGradoIns hisGradoIns) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		try {
			result.setResult(hisGradoInsDAO.saveOrUpdate(hisGradoIns));
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}
		
		return result;

	}	
	
	@Transactional
	@RequestMapping(  value="/grabarCoordinadorNivel", method = RequestMethod.POST)
	public AjaxResponseBody grabarCoordinadorNivel(NivelCoordinador nivelCoordinador) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		try {
			result.setResult(nivelCoordinadorDAO.saveOrUpdate(nivelCoordinador));
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/calcularFechaPrueba/{fec_fin}/{nro_meses}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Date fec_fin , Integer nro_meses) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			Calendar cal2 = Calendar.getInstance();
			cal2.add(Calendar.MONTH, nro_meses);
			result.setResult( cal2.getTime());
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			trabajadorDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/eliminarGrado/{id_gra}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminarGrado(@PathVariable Integer id_gra) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			hisGradoInsDAO.delete(id_gra);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( trabajadorDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/profesoresxMat/{id_mat}", method = RequestMethod.GET)
	public AjaxResponseBody profesoresxMat(@PathVariable Integer id_mat ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( trabajadorDAO.listarProfesoresPorMatricula(id_mat) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/profesoresxNivel",method = RequestMethod.GET)
	public AjaxResponseBody profesoresxNivel(Integer id_anio, Integer id_niv ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( trabajadorDAO.listarprofesoresxNivel(id_anio, id_niv) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/coordinadoresxNivel/{id}", method = RequestMethod.GET)
	public AjaxResponseBody getcoordinadoresxNivel(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( nivelCoordinadorDAO.get(id));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/coordinadoresxNivelDelete/{id}", method = RequestMethod.DELETE)//aqui
	public AjaxResponseBody deletecoordinadoresxNivel(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			nivelCoordinadorDAO.delete(id);
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarTrabajadores",method = RequestMethod.GET)
	public AjaxResponseBody listarTrabajadores(@RequestParam String term) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( trabajadorDAO.listartrabajadores(term));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarPersonas",method = RequestMethod.GET)
	public AjaxResponseBody listarPersonas() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(personaDAO.listarPersonas());
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarProfesoresTutores",method = RequestMethod.GET)
	public AjaxResponseBody listarProfesoresTutores(Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( trabajadorDAO.listarProfesoresTutores(id_anio));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping(value = "/foto/{id}")
	@ResponseBody
	public void getFoto(HttpServletResponse request,HttpServletResponse response,@PathVariable Integer id)  throws IOException {
	  
		Trabajador trabajador = trabajadorDAO.getPhoto(id);
		
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		
		if(trabajador.getFot()!=null) {
			ByteArrayInputStream in = new ByteArrayInputStream(trabajador.getFot());
			IOUtils.copy(in, response.getOutputStream());
		}else {
			String genero = trabajador.getGenero(); 
			InputStream is = null ;
			if ("F".equals(genero))
				is = httpSession.getServletContext().getResourceAsStream("/WEB-INF/views/img/mujer.png");
			else
				is = httpSession.getServletContext().getResourceAsStream("/WEB-INF/views/img/hombre.png");
			
			IOUtils.copy(is, response.getOutputStream());
		}
	}		
	
	private String generarCodigoTrabajador() {
		//List<Row> Cantidad = familiarDAO.Cantidad_Fam();
		Row ultimo_codigo=trabajadorDAO.obtenerUltimoCodigodeTrabajador();
		int correlativo = 0;
		if(ultimo_codigo.getString("codigo")!=null) {
			if(!ultimo_codigo.getString("codigo").equals("")){
				String num_ultimo=ultimo_codigo.getString("codigo").toString().replace("T","");
				 correlativo = Integer.parseInt(num_ultimo) + 1;
			} else {
				correlativo = 1;
			}
		} else {
			 correlativo = 1;
		}
		

		/*Date date = new Date(); // your date
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String year =Integer.toString(cal.get(Calendar.YEAR)).substring(2);*/

		DecimalFormat myFormatter = new DecimalFormat("00000");
		String correlativoString = myFormatter.format(correlativo);

		String codigo ="T" + correlativoString;

		return codigo;
	}
	
	@RequestMapping(value = "/obtenerDatosTrabajador/{cod}")
	@ResponseBody
	public AjaxResponseBody obtenerDatosTrabajador(HttpServletResponse request,HttpServletResponse response,@PathVariable String cod)  throws IOException {
	  
		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(trabajadorDAO.obtenerDatosTrabajador(cod));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;
	}	
	
	@RequestMapping(value = "/obtenerDatosPersonaxDNI/{dni}")
	@ResponseBody
	public AjaxResponseBody obtenerDatosTrabajadorxDNI(HttpServletResponse request,HttpServletResponse response,@PathVariable String dni)  throws IOException {
	  
		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(trabajadorDAO.obtenerDatosTrabajadorxDNI(dni));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;
	}	
	
	@RequestMapping( value="/listarRegimenLaboral",method = RequestMethod.GET)
	public AjaxResponseBody listarRegimenLaboral() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( trabajadorDAO.listarRegimenLaboral());
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarModalidadTrabajo",method = RequestMethod.GET)
	public AjaxResponseBody listarModalidadTrabajo() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( trabajadorDAO.listarModalidadTrabajo());
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarCategoriaOcupacional",method = RequestMethod.GET)
	public AjaxResponseBody listarCategoriaOcupacional() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( trabajadorDAO.listarCategoriaOcupacional());
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarPuestoTrabajador",method = RequestMethod.GET)
	public AjaxResponseBody listarPuestoTrabajador() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( trabajadorDAO.listarPuesto());
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarPeriodoPrueba",method = RequestMethod.GET)
	public AjaxResponseBody listarPeriodoPrueba() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( trabajadorDAO.listarPeriodoPrueba());
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarLineaCarrera",method = RequestMethod.GET)
	public AjaxResponseBody listarLineaCarrera() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( trabajadorDAO.listarLineaCarrera());
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarDenominacionTrabajador",method = RequestMethod.GET)
	public AjaxResponseBody listarDenominacion(Integer id_anio, Integer id_lcarr) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( trabajadorDAO.listarDenominacion(id_anio, id_lcarr));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarRemuneracion",method = RequestMethod.GET)
	public AjaxResponseBody listarRemuneracion(Integer id_lcarr, Integer id_cden, Integer id_cocu, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( trabajadorDAO.listarRemuneracion(id_lcarr, id_cden, id_cocu, id_anio));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarFrecuenciaPago",method = RequestMethod.GET)
	public AjaxResponseBody listarFrecuenciaPago() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( trabajadorDAO.listarFrecuenciaPago());
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarAnio",method = RequestMethod.GET)
	public AjaxResponseBody listarAnio() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( trabajadorDAO.listarAnio());
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarGirosNegocio/{id_emp}",method = RequestMethod.GET)
	public AjaxResponseBody listarGirosNegocio(@PathVariable Integer id_emp) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( trabajadorDAO.listarGiroNegocio(id_emp));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}

}

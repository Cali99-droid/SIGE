package com.sige.mat.web.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
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
import com.sige.mat.dao.ContratoTrabajadorDAO;
import com.sige.mat.dao.PersonaDAO;
import com.sige.mat.dao.TrabajadorDAO;
import com.sige.mat.dao.UsuarioDAO;
import com.sige.mat.dao.UsuarioRolDAO;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.ContratoTrabajador;
import com.tesla.colegio.model.Denominacion;
import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.Persona;
import com.tesla.colegio.model.Remuneracion;
import com.tesla.colegio.model.Sucursal;
import com.tesla.colegio.model.Trabajador;
import com.tesla.colegio.model.Usuario;
import com.tesla.colegio.model.UsuarioRol;


@RestController
@RequestMapping(value = "/api/contratoTrabajador")
public class ContratoTrabajadorRestController {
	
	@Autowired
	private ContratoTrabajadorDAO contratoTrabajadorDAO;
	
	@Autowired
	private TrabajadorDAO trabajadorDAO;
	
	@Autowired
	private UsuarioDAO usuarioDAO;
	
	@Autowired
	private UsuarioRolDAO usuarioRolDAO;
	
	@Autowired
	private PersonaDAO personaDAO;

	@Autowired
	private HttpSession httpSession;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Trabajador trabajador) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(trabajadorDAO.listFullByParams( trabajador, new String[]{"tra.id"}) );
		
		return result;
	}
	
	@RequestMapping(value = "/listarContratosTrabajador")
	public AjaxResponseBody listarTodosTrabajadores(Integer id_tra) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(contratoTrabajadorDAO.listarContratoTrabajador(id_tra));
		
		return result;
	}

	@Transactional
	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(ContratoTrabajador contratoTrabajador) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		try {
			result.setResult( contratoTrabajadorDAO.saveOrUpdate(contratoTrabajador) );
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}
		
		return result;

	}	
	
	/*@Transactional
	@RequestMapping(  value="/grabarTrabajador", method = RequestMethod.POST)
	public AjaxResponseBody grabarTrabajador(Persona persona,Trabajador trabajador,Integer id_per, Integer id) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		try {
			//Grabamos los datos de la Persona
			persona.setId(id_per);
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
			Integer id_tra=trabajadorDAO.saveOrUpdate(trabajador);
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("id", id_tra);
			map.put("id_per",id_per);
			result.setResult(map);
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}
		
		return result;

	}	*/
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			contratoTrabajadorDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( contratoTrabajadorDAO.getFull(id, new String[]{Denominacion.TABLA,Remuneracion.TABLA}));
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
	
	@RequestMapping( value="/listarGiroNegocio",method = RequestMethod.GET)
	public AjaxResponseBody listarGiroNegocio() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( trabajadorDAO.listarAnio());
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}

}

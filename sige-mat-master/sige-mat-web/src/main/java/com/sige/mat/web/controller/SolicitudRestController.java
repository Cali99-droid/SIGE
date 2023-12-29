package com.sige.mat.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.colegio.model.Solicitud;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Reserva;
import com.sige.mat.dao.AlumnoDAO;
import com.sige.mat.dao.AulaDAO;
import com.sige.mat.dao.ConfFechasDAO;
import com.sige.mat.dao.GradDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.ReservaDAO;
import com.sige.mat.dao.SolicitudDAO;
import com.sige.mat.dao.SucursalDAO;
import com.sige.spring.service.SolicitudService;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.Sucursal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Este controllador es llamado desde los jsp
 * 
 * @author Institiucion educativa y ciencias
 *
 */

@RestController
@RequestMapping(value = "/api/solicitud")
public class SolicitudRestController {

	@Autowired
	private SolicitudService solicitudService;

	@Autowired
	private SolicitudDAO solicitudDAO;

	@Autowired
	private ReservaDAO reservaDAO;

	@Autowired
	private MatriculaDAO matriculaDAO;

	@Autowired
	private AlumnoDAO alumnoDAO;

	@Autowired
	private SucursalDAO sucursalDAO;

	@Autowired
	private ConfFechasDAO confFechasDAO;
	
	@Autowired
	private AulaDAO aulaDAO;
	
	@Autowired
	private GradDAO gradDAO;
	
	
	@Autowired
	private HttpSession httpSession;

	
	@RequestMapping(value="/alumnoBuscar")
	public AjaxResponseBody solicitudAlumnoBuscar( String alumno, Integer id_suc_ori, Integer id_suc_des,Integer id_anio) throws IOException{
		
		AjaxResponseBody result = new AjaxResponseBody();
		
		//revisar si tiene cronograma 
		boolean antiguo_sin_cronograma = confFechasDAO.cambioSeccionVigente(id_anio, "AS");

		boolean nuevo_con_cronograma = confFechasDAO.cambioSeccionVigente(id_anio, "NC");
		
		boolean nuevo_sin_cronograma = confFechasDAO.cambioSeccionVigente(id_anio, "NS");

		if (antiguo_sin_cronograma && !nuevo_sin_cronograma)
			result.setResult(solicitudDAO.consultarAlumnos(alumno, id_anio, id_suc_ori,id_suc_des,"AS"));
		if (nuevo_con_cronograma || nuevo_sin_cronograma && !antiguo_sin_cronograma)
			result.setResult(solicitudDAO.consultarAlumnos(alumno, id_anio, id_suc_ori,id_suc_des,"NC"));
		if (antiguo_sin_cronograma && nuevo_sin_cronograma)
			result.setResult(solicitudDAO.consultarAlumnos(alumno, id_anio, id_suc_ori,id_suc_des,"NSAS"));
		return result;
		
	}
	
	@RequestMapping(value = "/otrosLocales", method = RequestMethod.GET)
	private AjaxResponseBody cargarListas(Integer id_suc) {
		AjaxResponseBody result = new AjaxResponseBody();

		Param param = new Param();
		param.put("est", "A");
		param.put("id", "!='" + id_suc + "'");
		List<Sucursal> sucursalList = sucursalDAO.listByParams(param, new String[] { "nom asc" });
		
		result.setResult(sucursalList);
		return result;

	}

	@RequestMapping(value = "/datos", method = RequestMethod.GET)
	private AjaxResponseBody datos(Integer id_mat, Integer id_alu, Integer id_res,String tipo, Integer id_anio) {
		AjaxResponseBody result = new AjaxResponseBody();
		Row alumno = null;
		if (tipo.equals("A") || tipo.equals("M") ){
			alumno = alumnoDAO.datosAlumno(id_mat).get(0);	
			if (tipo.equals("A"))
				alumno.put("id_fam",null);
		}else if (tipo.equals("R") ){
			alumno = reservaDAO.datosAlumno(id_res).get(0);
		}else//SOLO TIENE SOLICITUD SIN HACER EFECTIVO SU CAMBIO DE  LOCAL
			alumno = solicitudDAO.datosAlumno(id_alu, id_anio).get(0);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("alumno", alumno);
 
		result.setResult(map);
		return result;

	}
	
	/**
	 * Agrega la nueva solicitud y desactiva el resto
	 * @param solicitud
	 * @return
	 */
	@RequestMapping(value = "/grabar", method = RequestMethod.POST)
	private AjaxResponseBody agregarSolicitud(Solicitud solicitud) {
		AjaxResponseBody result = new AjaxResponseBody();
		solicitud.setEst("A");
 	 
	
		result.setResult(solicitudService.agregarSolicitud(solicitud));
		return result;

	}
	
	@RequestMapping(value = "/validarLocal", method = RequestMethod.GET)
	private AjaxResponseBody validarLocal(Integer id_anio,Integer id_suc,Integer id_mat,Integer id_alu,String tipo) {
		AjaxResponseBody result = new AjaxResponseBody();
		
		Matricula matricula;
		if (tipo.equals("M")){//matriculados
			matricula = matriculaDAO.get(id_mat);
			
			List<Row> aulas = aulaDAO.listAulasXLocal(id_anio, id_suc, matricula.getId_gra());
			
			if (aulas.size()==0){
				result.setCode("201");
				result.setMsg("El local destino no tiene las aulas para la solicitud");
				return result;
			}
				
		}

		if (tipo.equals("R")){//matriculados
			Param param = new Param();
			param.put("id_alu", id_alu);
			param.put("id_anio", id_anio);
			Reserva reserva = reservaDAO.getPeriodoByParams(param);
			
			List<Row> aulas = aulaDAO.listAulasXLocal(id_anio, id_suc, reserva.getId_gra());
			
			if (aulas.size()==0){
				result.setCode("201");
				result.setMsg("El local destino no tiene las aulas para la solicitud.");
				return result;
			}
				
		}
		if (tipo.equals("A")){//ANTIGUOS NO matriculados
			matricula = matriculaDAO.get(id_mat);
			
			
			Param param = new Param();
			param.put("id_gra_ant", matricula.getId_gra());
			Grad grado = gradDAO.getByParams(param);
			List<Row> aulas = aulaDAO.listAulasXLocal(id_anio, id_suc, grado.getId());
			
			if (aulas.size()==0){
				result.setCode("201");
				result.setMsg("El local destino no tiene las aulas para la solicitud");
				return result;
			}
				
		}

		
		return result;

	}

	@RequestMapping(value = "/resumen", method = RequestMethod.POST)
	private AjaxResponseBody resumen(Integer id_anio) {
		AjaxResponseBody result = new AjaxResponseBody();
		
		
		result.setResult(solicitudDAO.resumenSolicitudes(id_anio));
		return result;

	}

}
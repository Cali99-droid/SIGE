package com.sige.mat.web.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sige.mat.dao.AulaDAO;
import com.sige.mat.dao.CapComDAO;
import com.sige.mat.dao.ComportamientoDAO;
import com.sige.mat.dao.GradDAO;
import com.sige.rest.request.ComportamientoReq;
import com.sige.rest.request.EvaluacionReq;
import com.sige.rest.request.NotaAlumnoReq;
import com.sige.rest.request.NotaAlumnoUpdateReq;
import com.sige.rest.request.NotaComAlumnoReq;
import com.sige.rest.request.NotaComAlumnoUpdateReq;
import com.sige.spring.service.ComportamientoService;
import com.tesla.frmk.common.exceptions.ServiceException;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.colegio.model.CapCom;
import com.tesla.colegio.model.Comportamiento;
import com.tesla.colegio.model.Nota;
import com.tesla.colegio.model.NotaIndicador;


@RestController
@RequestMapping(value = "/api/comportamiento")
public class ComportamientoRestController {
	final static Logger logger = Logger.getLogger(ComportamientoRestController.class);

	@Autowired
	private ComportamientoDAO comportamientoDAO;
	
	@Autowired
	private CapComDAO capComDAO;
	
	@Autowired
	private AulaDAO aulaDAO;
	
	@Autowired
	private GradDAO gradDAO;
	
	@Autowired
	private ComportamientoService comportamientoService;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Comportamiento comportamiento) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(comportamientoDAO.listFullByParams( comportamiento, new String[]{"nc.id"}) );
		
		return result;
	}
	
	@RequestMapping( value="/reporteComportamiento", method = RequestMethod.GET)
	public AjaxResponseBody alumnopromedioxCurso( Integer id_cpu, Integer id_anio, Integer id_niv, String id_suc, Integer id_tra) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//String fec_inicial= fec_ini;
			//String fec_final= fec_fin;
			result.setResult( comportamientoDAO.listarNotasComportamiento(id_cpu, id_anio, id_niv, id_suc, id_tra));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}

	@RequestMapping( value="/grabar",method = RequestMethod.POST)
	public AjaxResponseBody grabarNota(@RequestBody ComportamientoReq comportamientoReq) throws ServiceException{

		AjaxResponseBody result = new AjaxResponseBody();
		comportamientoService.grabar(comportamientoReq);
		return result;

	}
	
	@RequestMapping( value="/actualizar",method = RequestMethod.POST)
	public AjaxResponseBody actualizarComportamiento(@RequestBody  NotaComAlumnoUpdateReq[] notaComAlumnoUpdateReq) {

	
		
		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
			for (NotaComAlumnoUpdateReq notaAlumno : notaComAlumnoUpdateReq) {
				Integer id_ncc = notaAlumno.getId();
				Integer nota = notaAlumno.getNota();
				Integer id_tra = notaAlumno.getId_usr();
				
				comportamientoDAO.actualizaNotaComportamiento(id_ncc, nota, id_tra);
				
			}
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id_au}/{id_cpu}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id_au, @PathVariable Integer id_cpu ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			capComDAO.delete(id_au, id_cpu);
			comportamientoDAO.delete(id_au, id_cpu);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( comportamientoDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarNivelesTutor", method = RequestMethod.GET)
	public AjaxResponseBody listarNivelTutor( Integer id_tra, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( comportamientoDAO.listaNivelTutor(id_tra, id_anio));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarNivelesAuxiliar", method = RequestMethod.GET)
	public AjaxResponseBody listarNivelAuxiliar( Integer id_tra, Integer id_anio, Integer id_gir) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( comportamientoDAO.listaNivelAuxiliar(id_tra, id_anio,id_gir));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarSucursalTutor", method = RequestMethod.GET)
	public AjaxResponseBody listarSucursalTutor( Integer id_tra, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( comportamientoDAO.listarSucursalTutor(id_tra, id_anio));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarSucursalAuxiliar", method = RequestMethod.GET)
	public AjaxResponseBody listarSucursalAuxiliar( Integer id_tra, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( comportamientoDAO.listarSucursalAuxiliar(id_tra, id_anio));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarGrados", method = RequestMethod.GET)
	public AjaxResponseBody listarGradosTutor( Integer id_tra, Integer id_anio, Integer id_niv) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( comportamientoDAO.listaGradosTutor(id_tra, id_anio, id_niv));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarGradosAuxiliar", method = RequestMethod.GET)
	public AjaxResponseBody listarGradosAuxiliar( Integer id_tra, Integer id_anio, Integer id_niv) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( comportamientoDAO.listaGradosAuxiliar(id_tra, id_anio, id_niv));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarAulaTutor", method = RequestMethod.GET)
	public AjaxResponseBody listarAula( Integer id_tra, Integer id_grad, Integer id_suc, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( comportamientoDAO.listarAulaTutor(id_tra, id_grad, id_suc, id_anio));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	
	@RequestMapping( value="/listarAulaAuxiliar", method = RequestMethod.GET)
	public AjaxResponseBody listarAulaAuxiliar( Integer id_tra, Integer id_grad, Integer id_cic) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( comportamientoDAO.listarAulaAuxiliar(id_tra, id_grad, id_cic));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarCursosTutor", method = RequestMethod.GET)
	public AjaxResponseBody listarCursosProfesor( ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( comportamientoDAO.listarCursosTutor());
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarAlumnosNotasCom", method = RequestMethod.GET)
	public AjaxResponseBody listarAlumnosNotasCom(Integer id_cur, Integer id_niv, Integer id_cpu, Integer id_au, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			Integer id_gra=aulaDAO.getByParams(new Param("id",id_au)).getId_grad();
			String tip_gra=gradDAO.getByParams(new Param("id",id_gra)).getTipo();
			result.setResult( comportamientoDAO.listarAlumnoCapacidades(id_cur, id_niv, id_cpu, id_au, id_anio, tip_gra));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
}

package com.sige.mat.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.common.exceptions.ServiceException;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.CursoHorarioSesDAO;
import com.sige.spring.service.CursoHorarioService;
import com.sige.spring.service.CursoHorarioSesionService;
import com.sige.spring.service.UnidadSesionService;
import com.tesla.colegio.model.CursoHorarioSes;


@RestController
@RequestMapping(value = "/api/cursoHorarioSes")
public class CursoHorarioSesRestController {
	


	final static Logger logger = Logger.getLogger(CursoHorarioSesRestController.class);

	
	@Autowired
	private CursoHorarioSesDAO curso_horario_sesDAO;

	@Autowired
	private CursoHorarioService cursoHorarioService;
	
	@Autowired
	private CursoHorarioSesionService cursoHorarioSesService;

	@Autowired
	private UnidadSesionService unidadSesionService;
	
	@Autowired
	private AnioDAO anioDAO;

	
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(CursoHorarioSes curso_horario_ses) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(curso_horario_sesDAO.listFullByParams( curso_horario_ses, new String[]{"cchs.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(CursoHorarioSes curso_horario_ses) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( cursoHorarioService.grabar(curso_horario_ses) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}/{id_cca}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id, @PathVariable Integer id_cca ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {cursoHorarioService.eliminar(id,id_cca);
			//curso_horario_sesDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( curso_horario_sesDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}

	/**
	 * Agenda para el padre de familia
	 * @param id_alu
	 * @param id_anio
	 * @param id_mes
	 * @return
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/listarAgenda", method = RequestMethod.GET)
	public AjaxResponseBody listarAgenda(Integer id_alu,  Integer id_mes)
			throws ServiceException {

		AjaxResponseBody result = new AjaxResponseBody();
		//Integer anio= anioDAO.getByParams(new Param("id", id_anio)).getId();

		List<Row> agenda = curso_horario_sesDAO.listaAgenda(id_alu,  id_mes);		
		result.setResult(agenda);

		return result;
	}


	@RequestMapping(value = "/validarHorarioSesionxSemana", method = RequestMethod.GET)
	public AjaxResponseBody validarHorarioSesionxSemana(Integer id_ccu, Integer id_cca, int nro_sem )
			throws ServiceException {

		AjaxResponseBody result = new AjaxResponseBody(); 

		//VALIDA QUE LA SEMANA ANTERIOR ESTE COMPLETA
		int semana_valida = cursoHorarioSesService.validarHorarioSesionxSemana(id_cca, nro_sem);
		
		if(semana_valida!=nro_sem){
			result.setCode("422");
			result.setMsg("Por favor, ingrese el horario para la semana:" + semana_valida);
		}else{
			
			//VALIDA QUE TENGA CONFIGURADO LA UNIDAD
			
			try {
				unidadSesionService.listarSesionesCompletasxUnidad(id_ccu);
			} catch (Exception e) {
				logger.error("validacion de sesiones completas:" + id_ccu,e);
				result.setCode("422");
				result.setMsg("Por favor, complete la configuraci�n de las sesiones para la unidad" );

			}
			
			result.setResult(true);
		}
		

		return result;
	}

	/**
	 * Valida que la sesion no tenga notas
	 * Se utiliza en la vinculacion de sesiones por horario
	 * Pantalla: vinculacion de sesionex x horario
	 * 
	 * @param id_cchs
	 * @return
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/validarNotasxSesion", method = RequestMethod.GET)
	public AjaxResponseBody validarNotasxSesion(Integer id_cchs, Integer id_sem)
			throws ServiceException {

		AjaxResponseBody result = new AjaxResponseBody(); 

		//VALIDA QUE LA SESION NO TENGA NOTAS
		int notas = cursoHorarioSesService.validarNotasxHorarioSesion(id_cchs, id_sem);
		result.setResult(notas);
		return result;
	}
	
	@RequestMapping(value = "/listarDetalle", method = RequestMethod.GET)
	public AjaxResponseBody listarSubtemas(Integer id_cchs) throws ServiceException {
		AjaxResponseBody result = new AjaxResponseBody();
		result.setResult(cursoHorarioSesService.listarDetalle(id_cchs));
		return result;
	}
	
}

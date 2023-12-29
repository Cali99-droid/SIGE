package com.sige.mat.web.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.common.exceptions.ServiceException;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.FechaUtil;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.CursoHorarioDAO;
import com.sige.mat.dao.CursoHorarioPadDAO;
import com.sige.rest.request.SemanaRq;
import com.sige.spring.service.CursoHorarioService;
import com.sige.spring.service.CursoHorarioSesionService;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.CursoHorario;
import com.tesla.colegio.model.CursoHorarioPad;

@RestController
@RequestMapping(value = "/api/cursoHorario")
public class CursoHorarioRestController {

	@Autowired
	private CursoHorarioDAO curso_horarioDAO;

	@Autowired
	private AnioDAO anioDAO;
	
	@Autowired
	private CursoHorarioPadDAO cursoHorarioPadDAO;

	@Autowired
	private CursoHorarioService cursoHorarioService;

	@Autowired
	private  CursoHorarioSesionService cursoHorarioSesService;
	
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(CursoHorario curso_horario) {

		AjaxResponseBody result = new AjaxResponseBody();

		result.setResult(curso_horarioDAO.listFullByParams(curso_horario, new String[] { "cch.id" }));

		return result;
	}

	@RequestMapping(value = "/grabar", method = RequestMethod.POST)
	public AjaxResponseBody grabar(@RequestBody SemanaRq semanaRq) {

		AjaxResponseBody result = new AjaxResponseBody();

		try {
			
			result.setResult(cursoHorarioService.saveOrUpdate(semanaRq));
		} catch (Exception e) {
			result.setException(e);
		}

		return result;

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id) {

		AjaxResponseBody result = new AjaxResponseBody();

		try {
			curso_horarioDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}

		return result;

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id) {

		AjaxResponseBody result = new AjaxResponseBody();

		try {
			result.setResult(curso_horarioDAO.get(id));
		} catch (Exception e) {
			result.setException(e);
		}

		return result;

	}

	@RequestMapping(value = "/listarCursos", method = RequestMethod.GET)
	public AjaxResponseBody getListaCursos(Integer id_au, Integer id_anio) throws ServiceException {

		AjaxResponseBody result = new AjaxResponseBody();
		//A�o actual
		Integer anio_act=Integer.parseInt(anioDAO.getByParams(new Param("id", id_anio)).getNom());
		Row row = new Row();
		// PRIMER LUNES DEL A�O, ES UN VALOR REFERENCIAL PARA EL CALENDARIO
		// SEMANAL
		Calendar cacheCalendar = Calendar.getInstance();
		cacheCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cacheCalendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
		cacheCalendar.set(Calendar.MONTH, 0);
		cacheCalendar.set(Calendar.YEAR, anio_act);
		Date date = cacheCalendar.getTime();

		// listar cursos disponibles
		List<Row> cursos_aula = curso_horarioDAO.listaCursos(id_au);
		List<Row> cursos_disponibles = new ArrayList<>();
		for (Row row2 : cursos_aula) {
			// Verificamos si se ha insertado en el horario
				Param param = new Param();
				param.put("id_cca", row2.get("id"));
				param.put("est", "A");
			List<CursoHorario> curso_horario = curso_horarioDAO.listByParams(param, null);
			Integer cant_ses_inser = curso_horario.size(); // Cantidad de
															// sesiones
															// insertadas en el
															// horario
			Integer cant_ses_curso = row2.getInt("nro_ses");// Cantidad de
															// sesiones q
															// deberia tener el
															// curso
			Integer dif = cant_ses_curso - cant_ses_inser;
			if (dif > 0) {
				for (int i = 0; i < dif; i++) {
					Row curso = new Row();
					curso.put("id", row2.get("id"));
					curso.put("curso", row2.get("curso"));
					curso.put("docente", row2.get("docente"));
					cursos_disponibles.add(curso);
				}

			}

		}

		// listar horarios registrados por el aula
		List<Row> horarios = cursoHorarioService.listarHorariosxAula(id_au, anio_act);

		row.put("defaultDate", FechaUtil.toString(date, "yyyy-MM-dd"));// necesario
																		// para
																		// configurar
																		// el
																		// horario
																		// de la
																		// semana
		row.put("cursos", cursos_disponibles);
		row.put("horarios", horarios);

		result.setResult(row);

		return result;
	}

	
	@RequestMapping(value = "/horariolistarCursos", method = RequestMethod.GET)
	public AjaxResponseBody getHorariolistarCursos(Integer id_au,Integer id_cchp, Integer id_anio) throws ServiceException {

		AjaxResponseBody result = new AjaxResponseBody();
		//A�o actual
		Integer anio_act=Integer.parseInt(anioDAO.getByParams(new Param("id", id_anio)).getNom());
		
		Row row = new Row();
		// PRIMER LUNES DEL A�O, ES UN VALOR REFERENCIAL PARA EL CALENDARIO
		// SEMANAL
		Calendar cacheCalendar = Calendar.getInstance();
		cacheCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cacheCalendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
		cacheCalendar.set(Calendar.MONTH, 0);
		cacheCalendar.set(Calendar.YEAR, anio_act);
		Date date = cacheCalendar.getTime();

		// listar cursos disponibles
		List<Row> cursos_aula = curso_horarioDAO.listaCursos(id_au);
		
		List<Row> cursos_disponibles = new ArrayList<>();
		for (Row row2 : cursos_aula) {
			// Verificamos si se ha insertado en el horario
				Param param = new Param();
				param.put("id_cca", row2.get("id"));
				param.put("id_cchp", id_cchp);//TODO col_curso_horario_pad
				param.put("est", "A");
			List<CursoHorario> curso_horario = curso_horarioDAO.listByParams(param, null);
			Integer cant_ses_inser = curso_horario.size(); // Cantidad de
															// sesiones
															// insertadas en el
															// horario
			Integer cant_ses_curso = row2.getInt("nro_ses");// Cantidad de
															// sesiones q
															// deberia tener el
															// curso
			Integer dif = cant_ses_curso - cant_ses_inser;
			if (dif > 0) {
				for (int i = 0; i < dif; i++) {
					Row curso = new Row();
					curso.put("id", row2.get("id"));
					curso.put("curso", row2.get("curso"));
					curso.put("docente", row2.get("docente"));
					cursos_disponibles.add(curso);
				}

			}

		}

		// listar horarios registrados por el aula
		//List<Row> horarios = cursoHorarioService.listarHorariosxAula(id_au); //TODO col_curso_horario_pad
		List<Row> horarios = cursoHorarioService.listarHorariosxAula(id_au, id_cchp,anio_act);

		row.put("defaultDate", FechaUtil.toString(date, "yyyy-MM-dd"));// necesario para configurar el horario de la semana
		row.put("cursos", cursos_disponibles);
		row.put("horarios", horarios);

		result.setResult(row);

		return result;
	}
	
	@RequestMapping(value = "/listarPorSemana", method = RequestMethod.GET)
	public AjaxResponseBody getListarPorSemana(Integer id_suc, Integer id_tra, Integer id_niv, Integer id_sem)
			throws ServiceException {

		AjaxResponseBody result = new AjaxResponseBody();

		Row horarios = cursoHorarioService.listarHorariosxSemana(id_tra, id_suc, id_niv, id_sem);
		
		result.setResult(horarios);

		return result;
	}

	
	@RequestMapping(value = "/listarSessiones", method = RequestMethod.GET)
	public AjaxResponseBody listarSessiones(Integer id_cch,Integer id_sem)  throws ServiceException {

		AjaxResponseBody result = new AjaxResponseBody();
		
		Map<String,Object> map = new HashMap<String,Object>();
		List<Row> sesiones = cursoHorarioService.listarSessiones(id_cch, id_sem);
		map.put("sesiones", sesiones);
		
		//notas del curso_horario 
		Integer notas = cursoHorarioSesService.validarNotasxHorarioSesion(id_cch, id_sem);
		map.put("notas", notas);
		
		result.setResult(map);
		
		return result;
	}
	
	@RequestMapping(value = "/listaAgenda", method = RequestMethod.GET)
	public AjaxResponseBody listaAgenda(Integer id_mat,Integer id_anio,Integer mes)  throws ServiceException {

		Anio anio = anioDAO.get(id_anio);
		if (mes==null)
			mes = 3;//,MARZO
		
		AjaxResponseBody result = new AjaxResponseBody();
		
		Row row  = new Row();
		List<Row> horarios = cursoHorarioSesService.listaAgenda(id_mat, mes);
		row.put("horarios", horarios);
		row.put("fecha", anio.getNom() + "-" + ((mes<10)?"0" + mes:mes) + "-01");
		result.setResult(row);
		
		return result;
	}
	
	@RequestMapping(value = "/listarHorariosPadre", method = RequestMethod.GET)
	public AjaxResponseBody listarHorariosPadre(Integer id_au)  throws ServiceException {

		AjaxResponseBody result = new AjaxResponseBody();

		try {
			result.setResult(cursoHorarioPadDAO.listByParams(new Param("id_au",id_au), null));
		} catch (Exception e) {
			result.setException(e);
		}

		return result;
	}

	@RequestMapping(value = "/_actualizarPadres", method = RequestMethod.POST)
	public AjaxResponseBody actualizarPadres() {
		AjaxResponseBody result = new AjaxResponseBody();
		
		try {
			curso_horarioDAO.actualizarPadres();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	@RequestMapping(value = "/cursoHorarioPad", method = RequestMethod.POST)
	public AjaxResponseBody cursoHorarioPad(CursoHorarioPad cursoHorarioPad) {

		AjaxResponseBody result = new AjaxResponseBody();

		try {

			result.setResult(cursoHorarioService.nuevoHorarioPadre(cursoHorarioPad));
			
		} catch (Exception e) {
			
			result.setException(e);
			
		}

		return result;

	}


}

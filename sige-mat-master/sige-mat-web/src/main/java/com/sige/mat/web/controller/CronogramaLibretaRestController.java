package com.sige.mat.web.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sige.mat.dao.AlumnoDAO;
import com.sige.mat.dao.CronogramaLibretaDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.CronogramaLibreta;
import com.tesla.colegio.model.Matricula;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;


@RestController
@RequestMapping(value = "/api/cronogramaLibreta")
public class CronogramaLibretaRestController {
	
	@Autowired
	private CronogramaLibretaDAO cronograma_libretaDAO;
	
	@Autowired
	private MatriculaDAO matriculaDAO;
	
	@Autowired
	private AlumnoDAO alumnoDAO;

	@RequestMapping(value = "/listar/{id_anio}")
	public AjaxResponseBody getLista(CronogramaLibreta cronograma_libreta, @PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		cronograma_libreta.setId_anio(id_anio);
		result.setResult(cronograma_libretaDAO.listFullByParams( cronograma_libreta, new String[]{" niv.id, cpu.id_cpa"}) );
		
		return result;
	}

	@RequestMapping(value = "/listarNiveles")
	public AjaxResponseBody listarNiveles() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(cronograma_libretaDAO.listarNiveles() );
		
		return result;
	}
	
	@RequestMapping( value="/listarPeriodos", method = RequestMethod.GET)
	public AjaxResponseBody listarCursos( Integer id_niv, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( cronograma_libretaDAO.listaPeriodos(id_niv, id_anio));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}

	@RequestMapping( value="/listarPeriodosX_Nivel", method = RequestMethod.GET)
	public AjaxResponseBody listarPeriodosX_Nivel( Integer id_niv, Integer id_anio, Integer id_gir) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			if(id_niv!=null) {
				result.setResult( cronograma_libretaDAO.listarPeriodosX_Nivel(id_niv, id_anio, id_gir));
			} else {
				result.setResult(null);
			}
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	

	@RequestMapping( value="/listarPeriodosX_Alumno", method = RequestMethod.GET)
	public AjaxResponseBody listarPeriodosX_Alumno( Integer id_per, Integer id_anio, Integer id_gir) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			Alumno alumno = alumnoDAO.getByParams(new Param("id_per", id_per));
			Param param = new Param();
			param.put("mat.id_alu", alumno.getId());
			param.put("pee.id_anio", id_anio);
			param.put("ser.id_gir", id_gir);
			List<Matricula> matricula = matriculaDAO.listFullByParams(param, null);
			if(matricula.size()>0) {
				Integer id_niv=matricula.get(0).getId_niv();
				if(id_niv!=null) {
					result.setResult( cronograma_libretaDAO.listarPeriodosX_Nivel(id_niv, id_anio, id_gir));
				} else {
					result.setResult(null);
				}
			} else {
				result.setResult(null);
			}
			
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarPeriodosVigentes", method = RequestMethod.GET)
	public AjaxResponseBody listarPeriodosVigentes( Integer id_niv, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( cronograma_libretaDAO.listarPeriodosVigentes(id_niv, id_anio));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(CronogramaLibreta cronograma_libreta) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( cronograma_libretaDAO.saveOrUpdate(cronograma_libreta) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			cronograma_libretaDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( cronograma_libretaDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}

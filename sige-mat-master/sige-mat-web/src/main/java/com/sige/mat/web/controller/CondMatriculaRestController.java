package com.sige.mat.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.CondMatriculaDAO;
import com.tesla.colegio.model.CondMatricula;

@RestController
@RequestMapping(value = "/api/condMatricula")
public class CondMatriculaRestController {
	
	@Autowired
	private CondMatriculaDAO cond_matriculaDAO;

	@Autowired
	private CacheManager cacheManager;
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(CondMatricula cond_matricula) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(cond_matriculaDAO.listFullByParams( cond_matricula, new String[]{"cma.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(CondMatricula cond_matricula) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( cond_matriculaDAO.saveOrUpdate(cond_matricula) );
			cacheManager.update(CondMatricula.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			cond_matriculaDAO.delete(id);
			cacheManager.update(CondMatricula.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( cond_matriculaDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarAlumnos", method = RequestMethod.GET)
	public AjaxResponseBody listarAlumnos( Integer id_anio, String apelllidosNombres, Integer tip) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( cond_matriculaDAO.listarAlumnos(id_anio, apelllidosNombres,tip));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/obtenerDatosCond", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosCond( Integer id_mat) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			List<Row> datos=cond_matriculaDAO.obtenerDatosCond(id_mat);
			if(datos.size()>0)
				result.setResult(datos.get(0));
			else
				result.setResult(null);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
}

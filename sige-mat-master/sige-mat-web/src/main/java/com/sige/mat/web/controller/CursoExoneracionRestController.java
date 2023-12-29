package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.CursoExoneracionDAO;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.Curso;
import com.tesla.colegio.model.CursoExoneracion;
import com.tesla.colegio.model.Matricula;


@RestController
@RequestMapping(value = "/api/cursoExoneracion")
public class CursoExoneracionRestController {
	
	@Autowired
	private CursoExoneracionDAO curso_exoneracionDAO;
/*
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(CursoExoneracion curso_exoneracion) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(curso_exoneracionDAO.listFullByParams( curso_exoneracion, new String[]{"nce.id"}) );
		
		return result;
	}
*/

	@RequestMapping(value = "/listarAnio/{id_anio}")
	public AjaxResponseBody getListaAnio(@PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(curso_exoneracionDAO.listaPorAnio(id_anio) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(CursoExoneracion curso_exoneracion) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( curso_exoneracionDAO.saveOrUpdate(curso_exoneracion) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			curso_exoneracionDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( curso_exoneracionDAO.getFull(id, new String[]{Curso.TABLA, Matricula.TABLA, Alumno.TABLA}) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}

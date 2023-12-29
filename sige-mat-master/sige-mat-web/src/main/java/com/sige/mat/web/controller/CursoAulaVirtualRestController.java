package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.CursoAulaVirtualDAO;
import com.tesla.colegio.model.CursoAulaVirtual;


@RestController
@RequestMapping(value = "/api/cursoAulaVirtual")
public class CursoAulaVirtualRestController {
	
	@Autowired
	private CursoAulaVirtualDAO curso_aula_virtualDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(CursoAulaVirtual curso_aula_virtual) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(curso_aula_virtualDAO.listFullByParams( curso_aula_virtual, new String[]{"cau.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(CursoAulaVirtual curso_aula_virtual) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( curso_aula_virtualDAO.saveOrUpdate(curso_aula_virtual) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			curso_aula_virtualDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( curso_aula_virtualDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}

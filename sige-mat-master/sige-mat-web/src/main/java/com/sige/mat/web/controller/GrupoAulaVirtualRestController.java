package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.colegio.model.GrupoAulaVirtual;
import com.sige.mat.dao.GrupoAulaVirtualDAO;
import com.sige.spring.service.CampusVirtualService;


@RestController
@RequestMapping(value = "/api/grupoAulaVirtual")
public class GrupoAulaVirtualRestController {
	
	@Autowired
	private GrupoAulaVirtualDAO grupo_aula_virtualDAO;
	
	@Autowired
	private CampusVirtualService campusVirtualService;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(GrupoAulaVirtual grupo_aula_virtual) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(grupo_aula_virtualDAO.listFullByParams( grupo_aula_virtual, new String[]{"cgr.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(GrupoAulaVirtual grupo_aula_virtual) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( grupo_aula_virtualDAO.saveOrUpdate(grupo_aula_virtual) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			grupo_aula_virtualDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( grupo_aula_virtualDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/crearGruposClassroom/{id_anio}", method = RequestMethod.GET)
	public AjaxResponseBody crearGruposClasrrom(@PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			campusVirtualService.crearGruposClassroom(id_anio);
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/actualizarIdGoogle", method = RequestMethod.GET)
	public AjaxResponseBody actualizarIdGoogle() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			campusVirtualService.actualizarIdAlumno();
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/enrol/{id_anio}", method = RequestMethod.GET)
	public AjaxResponseBody enRol(@PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			campusVirtualService.asignacionClase(id_anio);
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping(value = "/listarGrupos")
	public AjaxResponseBody listarGrupos(Integer id_gra, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(grupo_aula_virtualDAO.listarGrupos(id_gra, id_anio));
		
		return result;
	}
}

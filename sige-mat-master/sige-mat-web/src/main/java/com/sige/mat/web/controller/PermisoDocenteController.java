package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.PermisoDocenteDAO;
import com.tesla.colegio.model.PermisoDocente;


@RestController
@RequestMapping(value = "/api/permisoDocente")
public class PermisoDocenteController {

	
	@Autowired
	private PermisoDocenteDAO permiso_docenteDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(PermisoDocente permiso_docente) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(permiso_docenteDAO.listFullByParams( permiso_docente, new String[]{"tra.ape_pat, tra.ape_mat, tra.nom, cp.nom, cpu.nump"}) );
		
		return result;
	}
	
	@RequestMapping(value = "/listarxAnio/{id_anio}")
	public AjaxResponseBody getListaxAnio(@PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(permiso_docenteDAO.listarPermisoDocente(id_anio) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(PermisoDocente permiso_docente) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( permiso_docenteDAO.saveOrUpdate(permiso_docente));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			permiso_docenteDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( permiso_docenteDAO.listFullByParams(new Param("cpd.id",id), null).get(0) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/obtenerVigencia", method = RequestMethod.GET)
	public AjaxResponseBody detalle(Integer id_tra, Integer id_au, Integer id_cpu) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( permiso_docenteDAO.obtenerVigencia(id_tra, id_au, id_cpu));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}

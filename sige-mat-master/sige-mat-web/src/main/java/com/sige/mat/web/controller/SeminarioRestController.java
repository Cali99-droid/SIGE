package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.CapacidadDAO;
import com.sige.mat.dao.SemGrupoDAO;
import com.sige.mat.dao.SemInscripcionDAO;
import com.sige.mat.dao.SeminarioDAO;
import com.tesla.colegio.model.Capacidad;
import com.tesla.colegio.model.SemGrupo;
import com.tesla.colegio.model.SemInscripcion;
import com.tesla.colegio.model.Seminario;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;

@RestController
@RequestMapping(value = "/api/seminario")
public class SeminarioRestController {
	
	@Autowired
	private SeminarioDAO seminarioDAO;
	
	@Autowired
	private SemGrupoDAO semGrupoDAO;
	
	@Autowired
	private SemInscripcionDAO semInscripcionDAO;

	@Autowired
	private CacheManager cacheManager;
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Seminario seminario, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		seminario.setId_anio(id_anio);
		result.setResult(seminarioDAO.listFullByParams( seminario, new String[]{"sem.id"}) );
		
		return result;
	}
	
	@RequestMapping(value = "/listarInscritosxSeminario")
	public AjaxResponseBody listarInscritosxSeminario(SemInscripcion semInscripcion, Integer id_sem) {

		AjaxResponseBody result = new AjaxResponseBody();
		semInscripcion.setId_sem(id_sem);
		result.setResult(semInscripcionDAO.listFullByParams(semInscripcion, new String[]{"sem.id"}) );
		
		return result;
	}
	
	@RequestMapping(value = "/listarGrupos")
	public AjaxResponseBody listarGrupos(SemGrupo semGrupo,Integer id_sem) {

		AjaxResponseBody result = new AjaxResponseBody();
		semGrupo.setId_sem(id_sem);
		result.setResult(semGrupoDAO.listFullByParams(semGrupo, new String[]{"gru.id"}) );
		
		return result;
	}


	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Seminario seminario) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( seminarioDAO.saveOrUpdate(seminario));
			cacheManager.update(Capacidad.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping(value = "/grabarSeminarioGrupo", method = RequestMethod.POST)
	public AjaxResponseBody grabarSeminarioGrupo(SemGrupo semGrupo) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(semGrupoDAO.saveOrUpdate(semGrupo));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			seminarioDAO.delete(id);
			cacheManager.update(Capacidad.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="eliminarGrupo/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminarGrupo(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			semGrupoDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( seminarioDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="editarGrupo/{id}", method = RequestMethod.GET)
	public AjaxResponseBody editarGrupo(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( semGrupoDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	

}

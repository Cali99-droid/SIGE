package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.GradDAO;
import com.sige.mat.dao.NivelDAO;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.Nivel;

@RestController
@RequestMapping(value = "/api/grad")
public class GradRestController {
	
	@Autowired
	private GradDAO gradDAO;
	
	@Autowired
	private NivelDAO nivelDAO;

	@Autowired
	private CacheManager cacheManager;
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Grad grad, Integer id_niv) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		grad.setId_nvl(id_niv);
		result.setResult(gradDAO.listFullByParams( grad, new String[]{"gra.id"}) );
		
		return result;
	}
	
	@RequestMapping(value = "/listarNiveles")
	public AjaxResponseBody listarNiveles(Nivel nivel) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(nivelDAO.listFullByParams( nivel, new String[]{"nvl.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Grad grad) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( gradDAO.saveOrUpdate(grad) );
			//cacheManager.update(Grad.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping(value = "/grabarNivel", method = RequestMethod.POST)
	public AjaxResponseBody grabarNivel(Nivel nivel) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( nivelDAO.saveOrUpdate(nivel) );
			//cacheManager.update(Grad.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			gradDAO.delete(id);
			//cacheManager.update(Grad.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="eliminarNivel/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminarNivel(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			nivelDAO.delete(id);
			//cacheManager.update(Grad.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( gradDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="obtenerDatosNivel/{id}", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosNivel(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( nivelDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping(value = "/listarTodosGrados")
	public AjaxResponseBody getLista( Integer id_niv) {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
			result.setResult(gradDAO.listaTodosGrados(id_niv));
			
		} catch (Exception e) {
			result.setException(e);
		}
		return result;

	}
	
	@RequestMapping(value = "/listarTodosGradosxGiro")
	public AjaxResponseBody getLista( Integer id_niv, Integer id_gir) {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
			result.setResult(gradDAO.listaTodosGradosxGiro(id_niv, id_gir));
			
		} catch (Exception e) {
			result.setException(e);
		}
		return result;

	}
	
	@RequestMapping(value = "/listarGradosColegio")
	public AjaxResponseBody listarGradosColegio() {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
			result.setResult(gradDAO.listaGrados());
			
		} catch (Exception e) {
			result.setException(e);
		}
		return result;

	}
	
	
}

package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.SubtemaDAO;
import com.tesla.colegio.model.Subtema;


@RestController
@RequestMapping(value = "/api/subtema")
public class SubtemaRestController {
	
	@Autowired
	private SubtemaDAO subtemaDAO;
	
	@Autowired
	private CacheManager cacheManager;
	
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Subtema subtema) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(subtemaDAO.listFullByParams( subtema, new String[]{"sub.ord"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Subtema subtema) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( subtemaDAO.saveOrUpdate(subtema) );
			cacheManager.update(Subtema.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			subtemaDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( subtemaDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping(value = "/listarSubtemas")
	public AjaxResponseBody getLista(Integer id_tem) {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
			result.setResult(subtemaDAO.listaSubtemaxTema(id_tem));
			
		} catch (Exception e) {
			result.setException(e);
		}
		return result;

	}

	@RequestMapping(value = "/listarCursoSubtemas")
	public AjaxResponseBody listarCursoSubtemas(Integer id_anio, Integer id_niv,Integer id_gra, Integer id_cur,Integer id_tem) {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
			result.setResult(subtemaDAO.listarCursoSubtemas(id_anio, id_niv, id_gra, id_cur, id_tem));
			
		} catch (Exception e) {
			result.setException(e);
		}
		return result;

	}

	
	@RequestMapping(value = "/listarSubtemasxAnioCurso")
	public AjaxResponseBody listarSubtemasxAnioCurso(Integer id_anio,Integer id_niv,Integer id_cur) {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
			result.setResult(subtemaDAO.listarSubtemasxAnioCurso(id_anio,id_niv,id_cur));
			
		} catch (Exception e) {
			result.setException(e);
		}
		return result;

	}
	
	
}

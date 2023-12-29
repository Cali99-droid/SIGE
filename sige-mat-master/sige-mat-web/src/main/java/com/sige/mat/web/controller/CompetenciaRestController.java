package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.CompetenciaDAO;
import com.tesla.colegio.model.Competencia;

@RestController
@RequestMapping(value = "/api/competencia")
public class CompetenciaRestController {
	
	@Autowired
	private CompetenciaDAO competenciaDAO;

	@Autowired
	private CacheManager cacheManager;
	
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Competencia competencia) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(competenciaDAO.listFullByParams( competencia, new String[]{"com.id"}) );
		
		return result;
	}
	

	@RequestMapping(value = "/listaCompetenciasCursoAnio")
	public AjaxResponseBody getlistaCompetenciasCursoAnio(Integer id_anio, Integer id_niv, Integer id_cur, Integer id_gra) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(competenciaDAO.listaCompetenciasCursoAnio(id_anio, id_niv, id_cur, id_gra));
		
		return result;
	}
	

	@RequestMapping(value = "/listaCompetenciasCursoAnioCatalogo")
	public AjaxResponseBody listaCompetenciasCursoAnioCatalogo(Integer id_anio, Integer id_niv, Integer id_cur, Integer id_gra) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(competenciaDAO.listaCompetenciasCursoAnioCatalogo(id_anio, id_niv, id_cur, id_gra));
		
		return result;
	}
	

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Competencia competencia) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( competenciaDAO.saveOrUpdate(competencia) );
			cacheManager.update(Competencia.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			competenciaDAO.delete(id);
			cacheManager.update(Competencia.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( competenciaDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping(value = "/consulta")
	public AjaxResponseBody getLista(Integer id_niv, Integer id_cur) {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
			result.setResult(competenciaDAO.listaCompetenciasCapacidad(id_niv, id_cur));
			
		} catch (Exception e) {
			result.setException(e);
		}
		return result;

	}
}

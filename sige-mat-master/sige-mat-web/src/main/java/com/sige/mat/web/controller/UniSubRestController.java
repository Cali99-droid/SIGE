package com.sige.mat.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.common.exceptions.ControllerException;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;		
		
import com.tesla.colegio.model.UniSub;
import com.tesla.colegio.model.UsuarioNivel;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.UniSubDAO;
import com.sige.spring.service.ProgramacionAnualService;

@RestController
@RequestMapping(value = "/api/uniSub")
public class UniSubRestController {
	
	@Autowired
	private UniSubDAO uni_subDAO;
	
	@Autowired
	private ProgramacionAnualService programacionAnualService;
	
	@Autowired
	private CacheManager cacheManager;
	
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(UniSub uni_sub, int id_uni) {

		AjaxResponseBody result = new AjaxResponseBody();
		Param param= new Param();
		param.put("uni.id", id_uni);
		param.put("cus.est", "A");
		result.setResult(uni_subDAO.listFullByParams( param, new String[]{"cus.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(UniSub uni_sub, Integer[] id_subtemas) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
		//	result.setResult(uni_subDAO.saveOrUpdate(uni_sub));
			result.setResult(programacionAnualService.grabarSubtemasporUnidad(uni_sub, id_subtemas));
			//cacheManager.update(UniSub.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			uni_subDAO.delete(id);
			//cacheManager.update(UniSub.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/eliminarxGrupo/{id_cgsp}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminarxGrupo(@PathVariable Integer id_cgsp ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			uni_subDAO.deletegrupo(id_cgsp);
			//cacheManager.update(UniSub.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( uni_subDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarSubtemas", method = RequestMethod.GET)
	public AjaxResponseBody listarSubtemas(Integer id_anio, Integer id_cur, Integer id_niv, Integer id_gra, String tip, Integer id_uni) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( uni_subDAO.listaSubtemas(id_anio,id_cur, id_niv, id_gra, tip, id_uni));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarSubtemasxUnidad", method = RequestMethod.GET)
	public AjaxResponseBody listarSubtemasxUnidad(Integer id_anio,Integer id_niv, Integer id_gra, Integer id_cur, Integer id_uni) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( uni_subDAO.listaSubtemasxUnidad(id_anio,id_niv, id_gra, id_cur,id_uni));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}

}


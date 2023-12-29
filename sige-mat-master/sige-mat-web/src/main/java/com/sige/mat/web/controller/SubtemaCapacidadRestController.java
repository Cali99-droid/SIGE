package com.sige.mat.web.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.colegio.model.CursoSubtema;
import com.tesla.colegio.model.Desempenio;
import com.tesla.colegio.model.SubtemaCapacidad;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.DesempenioDAO;
import com.sige.mat.dao.SubtemaCapacidadDAO;
import com.sige.spring.service.SubTemaCapacidadService;

@RestController
@RequestMapping(value = "/api/subtemaCapacidad")
public class SubtemaCapacidadRestController {
	
	@Autowired
	private SubtemaCapacidadDAO subtema_capacidadDAO;

	@Autowired
	private DesempenioDAO desempenioDAO;

	@Autowired
	private SubTemaCapacidadService subTemaCapacidadService;

	@Autowired
	private CacheManager cacheManager;
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(SubtemaCapacidad subtema_capacidad) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(subtema_capacidadDAO.listFullByParams( subtema_capacidad, new String[]{"cuc.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(SubtemaCapacidad subtema_capacidad) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( subtema_capacidadDAO.saveOrUpdate(subtema_capacidad) );
			cacheManager.update(SubtemaCapacidad.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			subtema_capacidadDAO.delete(id);
			cacheManager.update(SubtemaCapacidad.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( subtema_capacidadDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping(value = "/listarxCapacidadSubTema")
	public AjaxResponseBody listarxCapacidadSubTema(CursoSubtema curso_subtema) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(subtema_capacidadDAO.listCursoSubTema( curso_subtema) );
		
		return result;
	}
	
	@RequestMapping(value = "/listarTema")
	public AjaxResponseBody listarTema(Integer id_anio, Integer id_niv, Integer id_cur, Integer id_gra) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		if(id_cur!=null && id_gra != null)
			result.setResult(subtema_capacidadDAO.listarTemas(id_anio, id_niv, id_cur, id_gra));
		else
			result.setResult(new ArrayList<Row>());
		return result;
	}
	
	@RequestMapping(value = "/grabarSubtemasCapacidad", method = RequestMethod.POST)
	public AjaxResponseBody grabarSubtemasCapacidad(Integer[] id_sub,Integer id_cap,Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( subTemaCapacidadService.grabarSubtemasCapacidad(id_sub, id_cap, id_anio) );
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	
	@RequestMapping(value = "/listSubtemaCapacidades", method = RequestMethod.GET)
	public AjaxResponseBody listSubtemaCapacidades(Integer id_anio,Integer id_tem, Integer id_niv, Integer id_gra, Integer id_cur) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( subTemaCapacidadService.listSubtemaCapacidades(id_anio, id_tem, id_niv, id_gra, id_cur));
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	

	/**
	 * Lista de desemepe�os por grupo de subtemas/capacidad
	 * @param id_cgc
	 * @return
	 */
	@RequestMapping(value = "/listDesempenios", method = RequestMethod.GET)
	public AjaxResponseBody listDesempenios(Integer id_cgc) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			Param param = new Param();
			param.put("id_cgc", id_cgc);
			param.put("est", "A");
			
			result.setResult( desempenioDAO.listByParams(param, new String[]{"nom"}));
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	


	/**
	 * agrega  desemepe�os por grupo de subtemas/capacidad
	 * @param id_cgc
	 * @return
	 */
	@RequestMapping(value = "/agregarDesempenio", method = RequestMethod.POST)
	public AjaxResponseBody agregarDesempenio(Desempenio desempenio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		
		try {
			
			result.setResult(desempenioDAO.saveOrUpdate(desempenio));
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	/**
	 * eliminar desempenios
	 * @param id_cgc
	 * @return
	 */
	@RequestMapping(value = "/eliminarDesempenio/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminarDesempenio(@PathVariable Integer id) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		
		try {
			desempenioDAO.delete(id);
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	/**
	 * eliminar agrupacion
	 * @param id_cgc
	 * @return
	 */
	@RequestMapping(value = "/eliminarAgrupacion/{id_cgc}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminarAgrupacion(@PathVariable Integer id_cgc) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		
		try {
			subTemaCapacidadService.eliminarAgrupacion(id_cgc);
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	
}

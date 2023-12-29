package com.sige.mat.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Row;
import com.tesla.colegio.model.SesionDesempenio;
import com.tesla.colegio.model.SesionTipo;
import com.tesla.colegio.model.UnidadSesion;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.CursoHorarioSesDAO;
import com.sige.mat.dao.UnidadSesionDAO;
import com.sige.spring.service.UnidadSesionService;

@RestController
@RequestMapping(value = "/api/unidadSesion")
public class UnidadSesionRestController {
	
	@Autowired
	private UnidadSesionDAO unidad_sesionDAO;

	@Autowired
	private UnidadSesionService unidadSesionService;

	@Autowired
	private CursoHorarioSesDAO cursoHorarioSesDAO;
		
	@Autowired
	private CacheManager cacheManager;
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(UnidadSesion unidad_sesion) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(unidad_sesionDAO.listFullByParams( unidad_sesion, new String[]{"ses.id"}) );
		
		return result;
	}
	
	@Transactional
	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(UnidadSesion unidad_sesion, Integer id_uns, Integer id_cde[],SesionTipo sesionTipo, SesionDesempenio sesionDesempenio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//Grababamos la sesi�n
			result.setResult(unidadSesionService.agregarClase(unidad_sesion, id_uns, id_cde, sesionTipo, sesionDesempenio));
			cacheManager.update(UnidadSesion.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id_ses}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id_ses ) {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
			result.setResult(unidadSesionService.eliminarEvaluaciones(id_ses));
			//cacheManager.update(UnidadSesion.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="eliminarClaseoRepaso/{id_ses}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminarClaseoRepaso(@PathVariable Integer id_ses ) {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
			result.setResult(unidadSesionService.eliminarClase(id_ses));
			//cacheManager.update(UnidadSesion.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( unidad_sesionDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarSesionesxUnidad/{id_uni}/{id_gra}", method = RequestMethod.GET)
	public AjaxResponseBody listarSesionesxUnidad(@PathVariable Integer id_uni, @PathVariable Integer id_gra) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
			Map<String,Object> rpta = new HashMap<String,Object>();
			
			List<Row> sesiones = unidadSesionService.listarSesionesxUnidad(id_uni);
			rpta.put("sesiones", sesiones);
			
			//las sesiones vinculadas se bloquearan en el modulo curricula>sessiones
			List<Row> sesionesVinculadas = cursoHorarioSesDAO.listarSesionesVinculadas(id_uni, id_gra);
			rpta.put("sesionesVinculadas", sesionesVinculadas);

			result.setResult(rpta);
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@Transactional
	@RequestMapping(value="/agregarDesempenio/{id_ses}", method = RequestMethod.POST)
	public AjaxResponseBody grabar(@PathVariable Integer id_ses, Integer id_cde[], SesionDesempenio sesionDesempenio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//Grababamos la sesi�n
			result.setResult(unidadSesionService.agregarDesempenio(id_ses, id_cde, sesionDesempenio));
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	

	@RequestMapping( value="/tiposSesionDisponible/{id_uns}", method = RequestMethod.GET)
	public AjaxResponseBody tiposSesionDisponible(@PathVariable Integer id_uns ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(unidad_sesionDAO.tiposSesionDisponible(id_uns));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
 

}

package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.CapacidadDcDAO;
import com.sige.mat.dao.CompetenciaDAO;
import com.sige.mat.dao.CompetenciaDcDAO;
import com.sige.mat.dao.DesempenioDcDAO;
import com.sige.mat.dao.DisenioCurricularDAO;
import com.tesla.colegio.model.CapacidadDc;
import com.tesla.colegio.model.Competencia;
import com.tesla.colegio.model.CompetenciaDc;
import com.tesla.colegio.model.DesempenioDc;
import com.tesla.colegio.model.DisenioCurricular;

@RestController
@RequestMapping(value = "/api/competenciaDC")
public class CompetenciaDCRestController {
	
	@Autowired
	private CompetenciaDcDAO competenciaDcDAO;
	
	@Autowired
	private CapacidadDcDAO capacidadDcDAO;
	
	@Autowired
	private CompetenciaDAO competenciaDAO;
	
	@Autowired
	private DesempenioDcDAO desempenioDcDAO;

	@Autowired
	private CacheManager cacheManager;
	
	@RequestMapping(value = "/listarCompetencias/{id_dcare}")
	public AjaxResponseBody listarCompetencias(CompetenciaDc competenciaDc, @PathVariable Integer id_dcare) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		competenciaDc.setId_dcare(id_dcare);
		result.setResult(competenciaDcDAO.listarCompetencias(id_dcare));
		
		return result;
	}	
	
	@RequestMapping(value = "/listarCapacidades/{id_com}")
	public AjaxResponseBody listarCapacidades(@PathVariable Integer id_com) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		result.setResult(capacidadDcDAO.listarCapacidades(id_com));
		
		return result;
	}	
	
	@RequestMapping(value = "/listarDesempenios/{id_com}/{id_gra}")
	public AjaxResponseBody listarDesempenios(@PathVariable Integer id_com, @PathVariable Integer id_gra) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		result.setResult(desempenioDcDAO.listarDesempenios(id_com, id_gra));
		
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
	public AjaxResponseBody grabar(CompetenciaDc competenciaDc, Integer id_area) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			competenciaDc.setId_dcare(id_area);
			result.setResult( competenciaDcDAO.saveOrUpdate(competenciaDc) );
			cacheManager.update(Competencia.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value = "/grabarCapacidad", method = RequestMethod.POST)
	public AjaxResponseBody grabarCapacidad(CapacidadDc capacidadDc) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( capacidadDcDAO.saveOrUpdate(capacidadDc) );
			cacheManager.update(Competencia.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value = "/grabarDesempenio", method = RequestMethod.POST)
	public AjaxResponseBody grabarDesempenio(DesempenioDc desemepnioDc) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( desempenioDcDAO.saveOrUpdate(desemepnioDc) );
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
			competenciaDcDAO.delete(id);
			//cacheManager.update(Competencia.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="eliminarCapacidad/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminarCapacidad(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			capacidadDcDAO.delete(id);
			//cacheManager.update(Competencia.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="eliminarDesempenio/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminarDesempenio(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			desempenioDcDAO.delete(id);
			//cacheManager.update(Competencia.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( competenciaDcDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="obtenerDatosCapacidad/{id}", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosCapacidad(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( capacidadDcDAO.get(id));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="obtenerDatosDesempenio/{id}", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosDesempenio(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( desempenioDcDAO.get(id));
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

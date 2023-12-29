package com.sige.mat.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.AnioDAO;
import com.tesla.colegio.model.Anio;

@RestController
@RequestMapping(value = "/api/anio")
public class AnioRestController {
	
	@Autowired
	private AnioDAO anioDAO;

	@Autowired
	private CacheManager cacheManager;
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Anio anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(anioDAO.listFullByParams( anio, new String[]{"anio.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Anio anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			int anioing=Integer.parseInt(anio.getNom());
			Anio anioexis =anioDAO.getByParams(new Param("nom",anio.getNom()));//Verificar si el a�o existe
			int anioactual= Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));//A�o actual
			int dif= (anioing - anioactual);

			if(dif<0){
				result.setMsg("No se puede agregar a�os anteriores!");
				result.setCode("204");
			} else if(dif>1){
				result.setMsg("Solo se puede agregar un a�o mayor al a�o actual!");
				result.setCode("204");
			} else if (anioexis==null){
				result.setResult( anioDAO.saveOrUpdate(anio) );
				cacheManager.update(Anio.TABLA);
			} else {
				result.setMsg("El a�o ya esta registrado!");
				result.setCode("204");
			}	
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			anioDAO.delete(id);
			cacheManager.update(Anio.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( anioDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/listarAnios", method = RequestMethod.GET)
	public AjaxResponseBody listarAnios() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( anioDAO.listaAnios());
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
}

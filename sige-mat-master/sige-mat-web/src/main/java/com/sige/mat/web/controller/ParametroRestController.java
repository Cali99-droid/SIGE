package com.sige.mat.web.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.AlumnoAulaDAO;
import com.sige.mat.dao.ParametroDAO;
import com.sige.rest.request.ModuloReq;
import com.tesla.colegio.model.AlumnoAula;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.Modulo;
import com.tesla.colegio.model.Parametro;


@RestController
@RequestMapping(value = "/api/parametro")
public class ParametroRestController {
	
	@Autowired
	ParametroDAO parametroDAO;
	
	@Autowired
	CacheManager cacheManager;
	
	@RequestMapping(value = "/listarParametros")
	public AjaxResponseBody getListaModulos(Integer id_mod) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(parametroDAO.listByParams(new Param("id_mod",id_mod), null));
		
		return result;
	}

	
	/*@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Integer id, String value) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
			Parametro parametro = parametroDAO.get(id);
			parametro.setVal(value);
			
			result.setResult( parametroDAO.saveOrUpdate(parametro) );
			
			cacheManager.update(Parametro.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}*/	
	
	@Transactional
	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(@RequestBody  ModuloReq moduloReq) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
			for (int i = 0; i < moduloReq.getParametroReq().length; i++) {
				//Actualizar el valor
				/*if(tipo[i]=="flag" && val[i]==null)
					parametroDAO.upValorParametro(id_par[i], "0");
				else*/
					parametroDAO.upValorParametro(moduloReq.getParametroReq()[i].getId_par(), moduloReq.getParametroReq()[i].getVal());
			}
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
}

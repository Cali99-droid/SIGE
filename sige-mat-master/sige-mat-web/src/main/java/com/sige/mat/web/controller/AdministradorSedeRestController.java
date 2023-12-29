package com.sige.mat.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.AdministradorSedeDAO;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.CronogramaDAO;
import com.sige.mat.dao.NivelDAO;
import com.tesla.colegio.model.AdministradorSede;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Cronograma;
import com.tesla.colegio.model.Nivel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Este controllador es llamado desde los jsp 
 * @author Institiucion educativa y ciencias
 *
 */

@RestController
@RequestMapping(value = "/api/administradorSede")
public class AdministradorSedeRestController{

	@Autowired
	private AdministradorSedeDAO administradorSedeDAO;

	@Autowired
	private NivelDAO nivelDAO;
	
	@Autowired
	private AnioDAO anioDAO;
	
	@Autowired
	private HttpSession httpSession;

	@RequestMapping(value="/listar", method = RequestMethod.GET)
	public AjaxResponseBody listar(AdministradorSede administradorSede) throws IOException{
		
		AjaxResponseBody result = new AjaxResponseBody();
		
		Param param = new Param();
		param.put("id_anio", administradorSede.getId_anio());

		List<AdministradorSede> cronogramaList = administradorSedeDAO.listFullByParams(administradorSede, null);
		
		result.setResult(cronogramaList);
		return result;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public AjaxResponseBody grabar(AdministradorSede administradorSede ) throws IOException {
		
		AjaxResponseBody result = new AjaxResponseBody();

		administradorSedeDAO.saveOrUpdate(administradorSede);		
 	
		return result;

	}
	

	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			administradorSedeDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( administradorSedeDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
package com.sige.mat.web.controller;
import java.util.ArrayList;
import java.util.List;

//package com.sige.spring.rest.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sige.mat.dao.RolDAO;
import com.sige.mat.dao.RolOpcionDAO;
import com.sige.rest.request.RolOpcionRequest;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.Rol;
import com.tesla.colegio.model.RolOpcion;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;

@RestController
@RequestMapping(value = "/api/rol")
public class RolRestController {
	
	@Autowired
	private RolDAO rolDAO;

	@Autowired
	private TokenSeguridad tokenStrategy;
	 
	
	
@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(rolDAO.listFullByParams( new Param(), new String[]{"rol.id"}) );
		
		return result;

	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Rol rol) {

		AjaxResponseBody result = new AjaxResponseBody();
	
		Object id = tokenStrategy.getId();
		
		try {
			result.setResult( rolDAO.saveOrUpdate(rol) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			rolDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( rolDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}

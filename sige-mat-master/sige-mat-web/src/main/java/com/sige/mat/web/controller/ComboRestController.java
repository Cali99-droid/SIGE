package com.sige.mat.web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.sige.core.dao.cache.CacheManager;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.rest.util.Views;


@RestController
@RequestMapping(value = "/api/combo")
public class ComboRestController {

	@Autowired
	private CacheManager cacheManager;
	
	//@JsonView(Views.Public.class)
	@RequestMapping(value = "/{catalogo}/{args}")
	public AjaxResponseBody getCombo(@PathVariable String catalogo,@PathVariable String args[]) {

		AjaxResponseBody result = new AjaxResponseBody();
		result.setResult( cacheManager.getComboBox(catalogo,args) );
		
		return result;

	}

	
}

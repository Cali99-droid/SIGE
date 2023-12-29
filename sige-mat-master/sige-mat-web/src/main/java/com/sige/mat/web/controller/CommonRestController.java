package com.sige.mat.web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sige.core.dao.cache.CacheManager;
import com.sige.core.dao.cache.CacheManagerUtil;
import com.tesla.frmk.rest.util.AjaxResponseBody;



@RestController
@RequestMapping(value = "/api/common")
public class CommonRestController {

	@Autowired
	private CacheManagerUtil cacheManagerUtil;
	
	//@JsonView(Views.Public.class)
	@RequestMapping(value = "/cache/{catalogo}")
	public AjaxResponseBody getCombo(@PathVariable String catalogo,String args[]) {

		AjaxResponseBody result = new AjaxResponseBody();
		result.setResult( cacheManagerUtil.getComboBox(catalogo,args) );
		
		return result;

	}

	
}

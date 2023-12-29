package com.sige.mat.web.controller;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;


@RestController
@RequestMapping(value = "/api/common")
public class UtilRestController {

	
	@RequestMapping(value = "/fecha")
	public AjaxResponseBody getFecha() {
		AjaxResponseBody result = new AjaxResponseBody();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		String fecha = sdf.format(date);
		result.setResult( fecha );
		
		return result;

	}

	
}

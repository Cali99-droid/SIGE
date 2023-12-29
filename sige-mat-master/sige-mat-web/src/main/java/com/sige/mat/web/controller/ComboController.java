package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sige.core.dao.cache.CacheManagerUtil;
import com.tesla.frmk.rest.util.AjaxResponseBody;


@RestController
@RequestMapping(value = "/api/comboCache")
public class ComboController {

	@Autowired
	private CacheManagerUtil cacheManager;
	
	@RequestMapping(value = "/{catalogo}")
	public AjaxResponseBody getCombo(@PathVariable String catalogo) {

		AjaxResponseBody result = new AjaxResponseBody();
		result.setResult( cacheManager.getComboBox(catalogo,null) );
		
		return result;

	}
	
	/**
	 * Obtiene las provincias de un departamento desde la cach�
	 * @param id_dep
	 * @return
	 */
	@RequestMapping(value = "/departamentos/{id_pais}")
	public AjaxResponseBody getDepartementos(@PathVariable Integer id_pais) {

		AjaxResponseBody result = new AjaxResponseBody();
		result.setResult( cacheManager.getListDepartamentos(id_pais));
		
		return result;

	}

	/**
	 * Obtiene las provincias de un departamento desde la cach�
	 * @param id_dep
	 * @return
	 */
	@RequestMapping(value = "/provincias/{id_dep}")
	public AjaxResponseBody getProvincias(@PathVariable Integer id_dep) {

		AjaxResponseBody result = new AjaxResponseBody();
		result.setResult( cacheManager.getListProvincias(id_dep) );
		
		return result;

	}
	
	@RequestMapping(value = "/distritos/{id_pro}")
	public AjaxResponseBody getDistritos(@PathVariable Integer id_pro) {

		AjaxResponseBody result = new AjaxResponseBody();
		result.setResult( cacheManager.getListDistritos(id_pro) );
		
		return result;

	}

}

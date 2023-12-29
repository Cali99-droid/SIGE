package com.sige.spring.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.ParametroDAO;
import com.tesla.colegio.model.Parametro;
import com.tesla.colegio.model.bean.CondicionBean;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;

@Service
public class ModuloService {

	Logger logger;
	
	@Autowired
	private SQLUtil sqlUtil;
	
	@Autowired
	private ParametroDAO parametroDAO;

	public Parametro valorParametro(Integer id_mod, String nom){
		
		Param param = new Param();
		param.put("id_mod", id_mod);
		param.put("nom", nom);
		
		Parametro parametro = parametroDAO.getByParams(param);
		
		return parametro;
	}
}

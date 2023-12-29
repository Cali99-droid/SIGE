package com.sige.mat.web.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;
import com.sige.common.enums.EnumEstadoCivil;
import com.sige.common.enums.EnumIdioma;
import com.sige.common.enums.EnumParentesco;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.AlumnoDAO;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.ConfFechasDAO;
import com.sige.mat.dao.CriterioNotaDAO;
import com.sige.mat.dao.CronogramaDAO;
import com.sige.mat.dao.EmpresaDAO;
import com.sige.mat.dao.GradDAO;
import com.sige.mat.dao.GruFamAlumnoDAO;
import com.sige.mat.dao.GruFamDAO;
import com.sige.mat.dao.GruFamFamiliarDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.NivelDAO;
import com.sige.mat.dao.PeriodoDAO;
import com.sige.mat.dao.PersonaDAO;
import com.sige.mat.dao.ReglasNegocioDAO;
import com.sige.rest.request.AlumnoReq;

import java.util.List;
import java.util.Map;

import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.GruFam;
import com.tesla.colegio.model.GruFamAlumno;
import com.tesla.colegio.model.GruFamFamiliar;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.Persona;
import com.tesla.colegio.model.ReglasNegocio;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.common.exceptions.ControllerException;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.ExcelXlsUtil;
import com.tesla.frmk.util.JsonUtil;
import com.tesla.frmk.util.StringUtil; 

@RestController
@RequestMapping(value = "/api/persona")
public class PersonaRestController {
	
	final static Logger logger = Logger.getLogger(PersonaRestController.class);

	
	@Autowired
	private PersonaDAO personaDAO;
	
	
	
	@RequestMapping( value="/listarPersonas", method = RequestMethod.GET)
	public AjaxResponseBody listarPersonas() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
				result.setResult(personaDAO.listarPersonas());
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	
}


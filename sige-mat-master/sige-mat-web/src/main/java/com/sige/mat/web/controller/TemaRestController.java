package com.sige.mat.web.controller;

import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.ExcelXlsUtil;
import com.tesla.frmk.util.FechaUtil;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.CursoDAO;
import com.sige.mat.dao.GradDAO;
import com.sige.mat.dao.NivelDAO;
import com.sige.mat.dao.TemaDAO;
import com.tesla.colegio.model.Curso;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.Tema;


@RestController
@RequestMapping(value = "/api/tema")
public class TemaRestController {
	
	@Autowired
	private TemaDAO temaDAO;
	
	@Autowired
	private NivelDAO nivelDAO;
	
	@Autowired
	private CursoDAO cursoDAO;
	
	@Autowired
	private CacheManager cacheManager;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Tema tema, Integer id_anio, Integer id_cur) {

		AjaxResponseBody result = new AjaxResponseBody();
		//tema.setId_anio(id_anio);
		Param param = new Param();
		param.put("tem.id_anio", id_anio);
		param.put("tem.id_cur", id_cur);		
		result.setResult(temaDAO.listFullByParams( param, new String[]{"tem.ord"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Tema tema) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( temaDAO.saveOrUpdate(tema) );
			cacheManager.update(Tema.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			temaDAO.delete(id);
			cacheManager.update(Tema.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( temaDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping(value = "/consulta")
	public AjaxResponseBody getLista(Integer id_niv, Integer id_cur, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
			result.setResult(temaDAO.listaTemaSubtema(id_niv, id_cur, id_anio));
			
		} catch (Exception e) {
			result.setException(e);
		}
		return result;

	}
	
	@RequestMapping(value = "/excel")
	@ResponseBody
	public void descargarExcel(HttpServletRequest request,HttpServletResponse response, Integer id_niv, Integer id_cur, Integer id_anio)  throws Exception {
	  
		ExcelXlsUtil xls = new ExcelXlsUtil();
		List<Row> list = temaDAO.listaTemaSubtema(id_niv, id_cur, id_anio);
		
		response.setContentType("application/vnd.ms-excel");
		
		String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();
		//String  rutacARPETA =  "C:/plantillas/";

		Nivel nivel = nivelDAO.get(id_niv);
		Curso curso = cursoDAO.get(id_cur);
		
		String archivo = xls.generaExcelReporteTema(rutacARPETA, "plantilla_reporte_tema.xls", nivel.getNom(), curso.getNom(),  list);
        DateFormat dateFormat = new SimpleDateFormat("mm-dd hh-mm-ss");  

		response.setHeader("Content-Disposition","attachment;filename=Reporte_Tema" + dateFormat.format(new Date())  + ".xls");

		File initialFile = new File(archivo);
	    InputStream is = FileUtils.openInputStream(initialFile);
	    	
		IOUtils.copy(is, response.getOutputStream());
		
	}	
}

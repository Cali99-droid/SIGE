package com.sige.mat.web.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.sige.mat.dao.SeguimientoDocDAO;
import com.tesla.colegio.model.SeguimientoDoc;


@RestController
@RequestMapping(value = "/api/seguimientoDoc")
public class SeguimientoDocRestController {
	
	@Autowired
	private SeguimientoDocDAO seguimiento_docDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(SeguimientoDoc seguimiento_doc) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(seguimiento_docDAO.listFullByParams( seguimiento_doc, new String[]{"csd.id"}) );
		
		return result;
	}

	@RequestMapping(value = "/entregaLibretas")
	public AjaxResponseBody entregaLibretas(Integer id_mat) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(seguimiento_docDAO.logEntregaLibretas( id_mat) );
		
		return result;
	}

		
	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(SeguimientoDoc seguimiento_doc) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( seguimiento_docDAO.saveOrUpdate(seguimiento_doc) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			seguimiento_docDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( seguimiento_docDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping(value = "/reimprimir/{id}")
	@ResponseBody
	public void downloadLibreta(HttpServletResponse response,@PathVariable Integer id)  throws IOException {
	  
		byte[] image = seguimiento_docDAO.getArchivo(id);
		/*
		response.setContentType(MediaType.MULTIPART_FORM_DATA_VALUE);
		if(image!=null) {
		ByteArrayInputStream in = new ByteArrayInputStream(image);

		IOUtils.copy(in, response.getOutputStream());}
		else{
			logger.debug("PDF es vacio");
		
		}*/
		
		String nombrePDF = "libreta_" + id +".pdf";
		response.setHeader("Content-Disposition","inline; filename=\"" + nombrePDF +"\"");
		response.setContentType("application/pdf; name=\"" + nombrePDF + "\"");
		response.getOutputStream().write(image);


	}
	
	
	
	@RequestMapping(value = "/reporteEntregaLibretas")
	public AjaxResponseBody reporteEntregaLibretas(Integer id_niv,Integer id_cpu,Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(seguimiento_docDAO.reporteEntregaLibretas(id_niv, id_cpu, id_anio));
		
		return result;
	}
	
}

package com.sige.mat.web.controller;


import java.io.File;
import java.io.InputStream;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.PagoRealizadoDAO;
import com.tesla.colegio.model.Anio;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.util.ExcelXlsUtil;
import com.tesla.frmk.util.FechaUtil;

@RestController
@RequestMapping(value = "/api/reportePagos")
public class ReportePagosRestController {
	
	@Autowired
	private PagoRealizadoDAO pagorealizadoDAO;
	
	@Autowired
	private AnioDAO anioDAO;
	
	@Autowired
	private CacheManager cacheManager;
	
	@RequestMapping(value = "/reporte/{id_au}/{id_anio}")
	public AjaxResponseBody getLista(@PathVariable  Integer id_au, @PathVariable  Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		try {
			Anio anio = anioDAO.get(id_anio);
			int anio_siguiente= Integer.parseInt(anio.getNom()) + 1;
			Anio anio_sig=anioDAO.getByParams(new Param("nom",anio_siguiente));
			Integer id_anio_sig=null;
			if(anio_sig!=null)
				id_anio_sig=anio_sig.getId();
			result.setResult(pagorealizadoDAO.consultarReportePagos(id_au,id_anio_sig));
			
		} catch (Exception e) {
			result.setException(e);
		}
		return result;

	}
	
	@RequestMapping(value = "/excel")
	@ResponseBody
	public void descargarExcel(HttpServletRequest request,HttpServletResponse response, String nivel, String grado,String aula, String sucursal, Integer id_au, Integer id_anio)  throws Exception {
	  		ExcelXlsUtil xls = new ExcelXlsUtil();
	  		Anio anio = anioDAO.get(id_anio);
			int anio_siguiente= Integer.parseInt(anio.getNom()) + 1;
			Anio anio_sig=anioDAO.getByParams(new Param("nom",anio_siguiente));
			Integer id_anio_sig=null;
			if(anio_sig!=null)
				id_anio_sig=anio_sig.getId();
		List<Map<String,Object>> list = pagorealizadoDAO.consultarReportePagos(id_au,id_anio_sig); 
		Date fec=new Date();
		response.setContentType("application/vnd.ms-excel");
		
		String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();

		String archivo = xls.generaExcelReportePagos(rutacARPETA, "plantilla_reporte_pensiones.xls",nivel, grado, aula, sucursal, list);
		response.setHeader("Content-Disposition","attachment;filename=Reporte_Caja" + FechaUtil.toString(fec, "dd-MM-yyyy") + ".xls");

		File initialFile = new File(archivo);
	    InputStream is = FileUtils.openInputStream(initialFile);
	    	
		IOUtils.copy(is, response.getOutputStream());
		
	}	
	
}

package com.sige.mat.web.controller;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.MovimientoDAO;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.rest.util.Views;
import com.tesla.frmk.util.ExcelXlsUtil;
import com.tesla.frmk.util.FechaUtil;

@RestController
@RequestMapping(value = "/api/reporteCaja")
public class ReporteCajaRestController {
	
	@Autowired
	private MovimientoDAO movimientoDAO;

	@Autowired
	private CacheManager cacheManager;
	
	@RequestMapping(value = "/consulta")
	public AjaxResponseBody getLista(String fec_ini,String fec_fin, Integer id_suc, Integer id_usr) {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
			result.setResult(movimientoDAO.consultarReporteCaja(FechaUtil.toDate(fec_ini),FechaUtil.toDate(fec_fin), id_suc,id_usr) );
			
		} catch (Exception e) {
			result.setException(e);
		}
		return result;

	}


	@RequestMapping(value = "/excel")
	@ResponseBody
	public void descargarExcel(HttpServletRequest request,HttpServletResponse response, String fec_ini, String fec_fin,String usuario, String sucursal, Integer id_suc, Integer id_usr )  throws Exception {
	  
		ExcelXlsUtil xls = new ExcelXlsUtil();
		List<Map<String,Object>> list = movimientoDAO.consultarReporteCaja(FechaUtil.toDate(fec_ini),FechaUtil.toDate(fec_fin),id_suc,id_usr); 
		
		response.setContentType("application/vnd.ms-excel");
		
		String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();

		String archivo = xls.generaExcelReporteCaja(rutacARPETA, "plantilla_reporte_caja.xls", FechaUtil.toDate(fec_fin), usuario, sucursal, list);
		response.setHeader("Content-Disposition","attachment;filename=Reporte_Caja" + FechaUtil.toString(FechaUtil.toDate(fec_fin), "dd-MM-yyyy") + ".xls");

		File initialFile = new File(archivo);
	    InputStream is = FileUtils.openInputStream(initialFile);
	    	
		IOUtils.copy(is, response.getOutputStream());
		
	}	
}

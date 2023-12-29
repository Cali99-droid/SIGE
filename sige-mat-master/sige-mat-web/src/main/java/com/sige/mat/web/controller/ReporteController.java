package com.sige.mat.web.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.AulaDAO;
import com.sige.mat.dao.LecturaBarrasDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.SucursalDAO;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.Sucursal;
import com.tesla.colegio.model.Usuario;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.ArchivoUtil;
import com.tesla.frmk.util.ExcelXlsUtil;
import com.tesla.frmk.util.FechaUtil;


@RestController
@RequestMapping(value = "/api/reporte")
public class ReporteController {


	@Autowired
	private SucursalDAO sucursalDAO;

	@Autowired
	private MatriculaDAO matriculaDAO;
	
	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private AnioDAO anioDAO;

	@Autowired
	private AulaDAO aulaDAO;
	
	@Autowired
	private LecturaBarrasDAO lecturaBarrasDAO;
	
	List<Usuario> usuario;
	

	//@JsonView(Views.Public.class)

	@RequestMapping(value = "/listaCombo")
	public AjaxResponseBody getPagosPagados() {

		AjaxResponseBody result = new AjaxResponseBody();
				
		List<Sucursal> sucursalList = sucursalDAO.listByParams(new Param("est","A"),new String[]{"nom asc"});
		result.setResult(sucursalList);
		
		return result;

	}
	
	@RequestMapping(value = "/reporte")
	public AjaxResponseBody getSearchResultViaAjax(@RequestParam String apoderado,@RequestParam Integer id_anio,@RequestParam Integer local) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult( matriculaDAO.reporteContratos(apoderado, id_anio, local));
		
		return result;

	}
	

	@RequestMapping(value = "/registro_auxiliares/{id_gir}")
	@ResponseBody
	public void descargarExcel(HttpServletRequest request,HttpServletResponse response, @PathVariable Integer id_gir, @PathVariable Integer id_cic)  throws Exception {

		Date fec= new Date();
		ExcelXlsUtil xls = new ExcelXlsUtil();
		
		String str_anio = request.getParameter("id_anio");
		int id_anio = Integer.parseInt(str_anio);
		String str_suc=request.getParameter("id_suc");
		Integer id_suc =0 ;
		if(str_suc!=null)
		id_suc = Integer.parseInt(str_suc);
		
		Anio anio = anioDAO.get(id_anio);
		
		response.setContentType("application/vnd.ms-excel");
		
		//Inicio datos del archivo nuevo
		String  carpeta =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();
		//String  carpeta =  "D:/plantillas/";
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd_hhmmss");

		String archivo = "Registros_Auxiliares.xls";
		String nuevoArchivo = null;
		nuevoArchivo = dt1.format(new Date());
		
		nuevoArchivo = carpeta + "tmp/" + archivo.replace("Registros_Auxiliares", "Registros_Auxiliares_" +anio.getNom() + nuevoArchivo);
		//nuevoArchivo = carpeta  + archivo.replace("Registros_Auxiliares", "Registros_Auxiliares_" +anio.getNom() + nuevoArchivo);
		ArchivoUtil.copyFileUsingStream(new File(carpeta + archivo), new File(nuevoArchivo));
		

		FileInputStream inputStream = new FileInputStream(new File(nuevoArchivo));
		Workbook workbook = WorkbookFactory.create(inputStream);
		
		List<Aula> aulaList = matriculaDAO.listAulas(id_anio, id_suc, id_gir,id_cic);
		
        for (int i = 0; i < aulaList.size(); i++) {
        	
        	String nombre ="Aula" + aulaList.get(i).getId_grad() + "-" + aulaList.get(i).getSecc();
        	 workbook.cloneSheet(0);
             workbook.setSheetName(i, nombre);
        }
         
		//Fin - datos del archivo nuevo
        for (int i = 0; i < aulaList.size(); i++) {
    		List<Map<String,Object>> list = matriculaDAO.registroAuxiliares(id_anio, aulaList.get(i).getId(), id_suc);
    		if(list.size()>0){
    			xls.generaRegistrosAuxiliares( workbook, i,id_anio, list);	
    		}else
    			System.err.println("VACIO:" +  aulaList.get(i).getId());
    		
        }
		
		inputStream.close();

		FileOutputStream outputStream = new FileOutputStream(nuevoArchivo);
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
		
		response.setHeader("Content-Disposition","attachment;filename=Registros_Auxiliares_" + anio.getNom() + FechaUtil.toString(fec, "dd-MM-yyyy") + ".xls");

		File initialFile = new File(nuevoArchivo);
	    InputStream is = FileUtils.openInputStream(initialFile);
	    	
		IOUtils.copy(is, response.getOutputStream());
		
	}
	
	@RequestMapping(value = "/registro_asistencia_mensual")
	@ResponseBody
	public void descargarReporteMensual(HttpServletRequest request,HttpServletResponse response)  throws Exception {

		Date fec= new Date();
		ExcelXlsUtil xls = new ExcelXlsUtil();
		
		String str_anio = request.getParameter("id_anio");
		int id_anio = Integer.parseInt(str_anio);
		String str_tra=request.getParameter("id_tra");
		Integer id_tra = Integer.parseInt(str_tra);
		String usuario=request.getParameter("usuario");
		String str_id_rol=request.getParameter("id_rol");
		Integer id_rol = Integer.parseInt(str_id_rol);
		
		String str_mes = request.getParameter("id_mes");
		Integer mes=Integer.parseInt(str_mes);
		Anio anio = anioDAO.get(id_anio);
		
		response.setContentType("application/vnd.ms-excel");
		
		//Inicio datos del archivo nuevo
		String  carpeta =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();
		//String carpeta =  "D:/plantillas/";
		
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd_hhmmss");

		String archivo = "Reporte_Asistencia_Mensual.xls";
		String nuevoArchivo = null;
		nuevoArchivo = dt1.format(new Date());
		
		nuevoArchivo = carpeta + "tmp/" + archivo.replace("Reporte_Asistencia_Mensual", "Reporte_Asistencia_Mensual_" +anio.getNom() + nuevoArchivo);

		ArchivoUtil.copyFileUsingStream(new File(carpeta + archivo), new File(nuevoArchivo));
		

		FileInputStream inputStream = new FileInputStream(new File(nuevoArchivo));
		Workbook workbook = WorkbookFactory.create(inputStream);
		
		List<Aula> aulaList = aulaDAO.listAulasxTutor(id_anio, id_tra, id_rol);
		
        for (int i = 1; i < aulaList.size(); i++) {
             workbook.cloneSheet(0);
             workbook.setSheetName(i, "Aula" + aulaList.get(i).getId_grad() + "-" + aulaList.get(i).getSecc());
        }
        
        //Formar los dias del mes
        // Get the number of days in that month 
        YearMonth yearMonthObject = YearMonth.of(Integer.parseInt(anio.getNom()),mes); 
        int num_dias = yearMonthObject.lengthOfMonth(); //28  
        List<Map<String, Object>> list_dias_mes = new ArrayList<>();
        for (int i = 1; i <=num_dias; i++) {
			String dia = ((i<10)? "0"+ i:i) +"/"+(mes<10?"0" + mes:mes )+"/"+anio.getNom();
			Param param = new Param();
			param.put("dia", dia);
			list_dias_mes.add(param);
		}
		//Fin - datos del archivo nuevo
        for (int i = 0; i < aulaList.size(); i++) {
    		List<Map<String, Object>> listAlumnos = lecturaBarrasDAO.listAsistenciaAula(id_anio, aulaList.get(i).getId());
    		//Dias q falto 
    		
            String mes_format=String.format("%02d",mes); 
            for (int j = 0; j < listAlumnos.size(); j++) {
                Row dias=lecturaBarrasDAO.rowDiasAsistencia(id_anio, listAlumnos.get(j).get("codigo").toString(), anio.getNom(), mes_format);
                listAlumnos.get(j).put("dias_asistencia", dias);
            }
           
    		if(listAlumnos.size()>0){
    			xls.generaReporteAsistencia(workbook, i, id_anio, listAlumnos, list_dias_mes, usuario, mes,anio.getNom());
    		}else
    			System.err.println("VACIO:" +  aulaList.get(i).getId());
    		
        }
		
		inputStream.close();

		FileOutputStream outputStream = new FileOutputStream(nuevoArchivo);
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
		
		response.setHeader("Content-Disposition","attachment;filename=Reporte_Asistencia_Mensual_" + anio.getNom() + FechaUtil.toString(fec, "dd-MM-yyyy") + ".xls");

		File initialFile = new File(nuevoArchivo);
	    InputStream is = FileUtils.openInputStream(initialFile);
	    	
		IOUtils.copy(is, response.getOutputStream());
		
	}	
	
	
}

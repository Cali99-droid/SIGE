package com.sige.mat.web.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.ArchivoUtil;
import com.tesla.frmk.util.FechaUtil;
import com.tesla.frmk.util.WordToPDFUtil;
import com.tesla.colegio.model.CursoUnidad;
import com.tesla.colegio.model.CursoUnidadControl;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.PerUni;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.CursoUnidadControlDAO;
import com.sige.mat.dao.CursoUnidadDAO;
import com.sige.mat.dao.GradDAO;
import com.sige.mat.dao.NivelDAO;
import com.sige.mat.dao.PerUniDAO;
import com.sige.spring.service.CursoUnidadService;
import com.sige.spring.service.UnidadSesionService;

@RestController
@RequestMapping(value = "/api/cursoUnidad")
public class CursoUnidadRestController {
	
	final static Logger logger = Logger.getLogger(CursoUnidadRestController.class);
	
	@Autowired
	private CursoUnidadDAO curso_unidadDAO;

	@Autowired
	private CursoUnidadControlDAO cursoUnidadControlDAO;

	@Autowired
	private NivelDAO nivelDAO;

	@Autowired
	private GradDAO gradDAO;

	@Autowired
	private PerUniDAO perUniDAO;
	
	@Autowired
	private UnidadSesionService unidadSesionService;

	@Autowired
	private CursoUnidadService cursoUnidadService;

	@Autowired
	private CacheManager cacheManager;
	

	
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(CursoUnidad curso_unidad, Integer id_anio, Integer id_tra,Integer id_gra, Integer id_cur) {

		AjaxResponseBody result = new AjaxResponseBody();
		Param param=new Param();
		param.put("id_tra", id_tra);
		param.put("cpu.id_anio", id_anio);
		param.put("cur.id", id_cur);
		param.put("gra.id", id_gra);
		result.setResult(curso_unidadDAO.listFullByParams( param, new String[]{"niv.nom asc, gra.id asc, cur.nom asc, uni.num asc"}) );
		
		return result;
	}

	@RequestMapping(value = "/listaxNivel")
	public AjaxResponseBody getListaxNivel(Integer id_anio, Integer id_niv) {

		AjaxResponseBody result = new AjaxResponseBody();
		Param param=new Param();
		param.put("id_anio", id_anio);
		param.put("id_niv", id_niv);

		result.setResult(curso_unidadDAO.listaUnidades(id_anio, id_niv));
		
		return result;
	}
	
	@RequestMapping(value = "/consultar")
	public AjaxResponseBody consultar(CursoUnidad curso_unidad, Integer id_anio, Integer id_tra) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		Integer id_cur=null;
		if(curso_unidad.getId_cur()!=null)
			id_cur=curso_unidad.getId_cur();
		
		result.setResult(curso_unidadDAO.consultarUnidades(id_tra, id_anio,  curso_unidad.getId_niv(), curso_unidad.getId_gra(), id_cur));
		
		return result;
	}


	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(CursoUnidad curso_unidad, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			Integer id_niv=curso_unidad.getId_niv();
			Integer id_gra=curso_unidad.getId_gra();
			Integer id_cur=curso_unidad.getId_cur();
			Integer id_cpu=curso_unidad.getId_cpu();
			unidadSesionService.grabarSesionesxUnidad(curso_unidad,id_niv, id_gra, id_cur, id_cpu, id_anio);
			result.setResult(1);
			cacheManager.update(CursoUnidad.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			curso_unidadDAO.delete(id);
			cacheManager.update(CursoUnidad.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			List<CursoUnidad> cursoList=curso_unidadDAO.listFullByParams(new Param("uni.id",id), null);
			result.setResult(cursoList.get(0));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	

	
	@RequestMapping( value="/listarUnidades", method = RequestMethod.GET)
	public AjaxResponseBody listarUnidades( Integer id_niv, Integer id_gra, Integer id_cur, Integer nump) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( curso_unidadDAO.listaUnidades(id_niv, id_gra, id_cur, nump));
			
		} catch (Exception e) {
			logger.debug(id_niv + "-" + id_gra +"-" + id_cur +"-"+  nump);
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarUnidadesxGrado", method = RequestMethod.GET)
	public AjaxResponseBody listarUnidadesxGrado( Integer id_niv, Integer id_gra, Integer id_cpu) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try { 
			result.setResult( curso_unidadDAO.listarUnidadesxGrado(id_niv, id_gra,  id_cpu));
			
		} catch (Exception e) {
			logger.debug(id_niv + "-" + id_gra +"-"  +  id_cpu);
			result.setException(e);
		}
		
		return result;

	}


	
	@RequestMapping( value="/listarSubtemaUnidad", method = RequestMethod.GET)
	public AjaxResponseBody listarSubtemas( Integer id_uni) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			if(id_uni!=null)
				result.setResult( curso_unidadDAO.listaSubtemas(id_uni));
			else
				result.setResult(new ArrayList<>());
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarDesem", method = RequestMethod.GET)
	public AjaxResponseBody listarDesempenios(Integer id_cpu, Integer id_cur, Integer id_anio, Integer num,Integer id_gra,Integer id_nep, Integer id_uni) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			if(num==null){
				result.setMsg("No existe configuraci�n vigente para creaci�n de evaluaciones para este nivel, Seleccione otro!!");
				result.setCode("204");
			} else{
				if(id_cur!=null)
					result.setResult( curso_unidadDAO.listaDesempenios(id_cpu,id_cur, id_anio, num, id_gra, id_nep, id_uni));
				else
					result.setResult(new ArrayList<>());
			}
	
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	
	/**
	 * Genera el PDF unidad de aprendizaje para el profesor
	 * y a la vez graba una tabla de control
	 * @param response
	 * @param id_anio
	 * @param id_tra
	 * @param id id de la unidad
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping( value="/unidadAprendizajePDF/{id_anio}/{id_tra}/{id}", method = RequestMethod.GET)
	public void unidadAprendizajePDF(HttpServletResponse response,@PathVariable Integer id_anio,@PathVariable Integer id_tra,@PathVariable Integer id)throws Exception {
			
			//CursoAula cursoAula = curso_aulaDAO.getFull(id_cca,new String[]{CursoAnio.TABLA});

				Row cabecera = cursoUnidadService.obtenerCabecera(id_anio,id_tra, id);
				List<Row> competencias = cursoUnidadService.obtenerCompetencias(id_anio, id);
				List<Row> temas = cursoUnidadService.obtenerCamposTematicos(id_anio, id);
				//List<Row> sesiones = cursoUnidadService.obtenerSesiones(id_anio, id);
				List<Row> sesiones = unidadSesionService.listarSesionesxUnidadPDF(id);
				
				logger.debug(sesiones);				
				
				Row unidadCursoRow = new Row();
				unidadCursoRow.put("cabecera", cabecera);
				unidadCursoRow.put("competencias", competencias);
				unidadCursoRow.put("temas", temas);
				unidadCursoRow.put("sesiones", sesiones);
				
				WordToPDFUtil util = new WordToPDFUtil();
				String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();
				
				//String rutacARPETA =  "C:/plantillas/";
				
				String pdf = util.createPdfCursoUnidad(rutacARPETA, id_tra, unidadCursoRow );
				 
				byte[] pdf_bytes = Files.readAllBytes(new File(pdf).toPath());

				//grabar PDF
				CursoUnidadControl curso_unidad_control = new CursoUnidadControl();
				curso_unidad_control.setEst("A");
				curso_unidad_control.setId_tra(id_tra);
				curso_unidad_control.setId_uni(id);
				curso_unidad_control.setPdf(pdf_bytes);
				cursoUnidadControlDAO.saveOrUpdate(curso_unidad_control);
	 
				response.setHeader("Content-Disposition","inline; filename=\"" + "xxxx" +"\"");
				response.setContentType("application/pdf; name=\"" + cabecera.getString("trabajador") + "\"");
				response.getOutputStream().write(pdf_bytes);

	}
	
	@RequestMapping( value="/validarSesionesCompletas", method = RequestMethod.GET)
	public AjaxResponseBody validarSesionesCompletasxUnidad(Integer id_uni, Integer id_niv, Integer id_cur, Integer id_cpu, Integer id_gra) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			int num_uni_act=curso_unidadDAO.getByParams(new Param("id", id_uni)).getNum();
			int num_uni_ant=num_uni_act-1;
			Param param = new Param();
			param.put("id_niv", id_niv);
			param.put("id_cur", id_cur);
			param.put("id_cpu", id_cpu);
			param.put("id_gra", id_gra);
			param.put("num", num_uni_ant);
			Integer id_uni_ant=null;
			if(num_uni_act>1){
				CursoUnidad cursoUnidadAnterior =curso_unidadDAO.getByParams(param);
				if (cursoUnidadAnterior==null){
					//es un periodo anterior
					param.put("id_cpu", id_cpu-1);
					cursoUnidadAnterior =curso_unidadDAO.getByParams(param);
				}
				 id_uni_ant=cursoUnidadAnterior.getId();
				 result.setResult(unidadSesionService.listarSesionesCompletasxUnidad(id_uni_ant));
			}
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/validarSesionesCompletasxUnidad", method = RequestMethod.GET)
	public AjaxResponseBody validarSesionesCompletasxUnidad(Integer id_uni) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
				 result.setResult(unidadSesionService.listarSesionesCompletasxUnidad(id_uni));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/validarSesionesVinculadasxUnidad", method = RequestMethod.GET)
	public AjaxResponseBody validarSesionesVinculadasxUnidad(Integer id_uni, Integer id_tra, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
				 result.setResult(unidadSesionService.validarSesionesVinculadas(id_uni, id_tra, id_anio));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}


	@RequestMapping( value="/listarUnidadesTrabajador", method = RequestMethod.GET)
	public AjaxResponseBody detalle(Integer id_anio, Integer id_tra,Integer id_gra, Integer id_cur) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(curso_unidadDAO.listaUnidadesTrabajador(id_tra, id_gra, id_anio, id_cur));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	


	/**
	 * Lista los cursos segun el numero de unidad, para el reporte de unidades por docente 
	 * @param id_niv
	 * @param id_gra
	 * @param id_cpu
	 * @param num
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping( value="/unidadesDocente", method = RequestMethod.GET)
	public AjaxResponseBody unidadesDocente(Integer id_niv, Integer id_gra,Integer id_cpu,Integer num, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
			Row resultados =  listarUnidadesDocente(id_niv, id_gra,id_cpu,num, id_anio); 
			result.setResult(resultados);
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	

	private Row listarUnidadesDocente(Integer id_niv, Integer id_gra,Integer id_cpu,Integer num, Integer id_anio) {
		//List<Row> cursos = curso_unidadDAO.listarCursos(id_niv, id_gra, id_cpu, num);
		List<Row> cursos = curso_unidadDAO.listarCursosAnio(id_niv, id_gra, id_anio);
		List<Row> docentes_unidades =  curso_unidadDAO.listarDocentesUnidades(id_niv, id_gra, id_cpu, num, id_anio);
		
		Map<String, Row> docenteMap = new LinkedHashMap<>();
		
		for (Row docente_unidad : docentes_unidades) {
			String id_tra = docente_unidad.getString("id");
			Row docente = docenteMap.get(id_tra);
			
			if(docente==null){
				Row row = new Row();
				Row cursoUnidad = new Row();
				
				cursoUnidad.put("id", docente_unidad.get("id_ccu"));
				cursoUnidad.put("id_cur", docente_unidad.get("id_cur"));
				cursoUnidad.put("id_ccuc", docente_unidad.get("id_ccuc"));
				List<Row> cursoUnidadList =   new ArrayList<Row>();
				cursoUnidadList.add(cursoUnidad);
				row.put("curso_unidades", cursoUnidadList);
				row.put("docente", docente_unidad.getString("ape_pat") + " " + docente_unidad.getString("ape_mat") + ", " +docente_unidad.getString("nom"));
				row.put("id_tra",id_tra);
				docenteMap.put(id_tra, row);
			}else{ // agregar unidad
				Row cursoUnidad = new Row();
				cursoUnidad.put("id", docente_unidad.get("id_ccu"));
				cursoUnidad.put("id_cur", docente_unidad.get("id_cur"));
				cursoUnidad.put("id_ccuc", docente_unidad.get("id_ccuc"));
				List<Row> cursoUnidadList = (ArrayList<Row>)docente.get("curso_unidades");
				cursoUnidadList.add(cursoUnidad);
				docente.put("curso_unidades", cursoUnidadList);
				
			}
		}
		
		List<Row> docentes = new ArrayList<Row>();
		for (Map.Entry<String, Row> entry : docenteMap.entrySet()) {
		    //String id_tra = entry.getKey();
		    Row value = entry.getValue();
		    // now work with key and value...
		    docentes.add(value);
		}
		
		Row row = new Row();
		row.put("cursos", cursos);
		row.put("docentes", docentes);
		
		return  row;
	}
	
	@RequestMapping( value="/pdf/{id_ccuc}", method = RequestMethod.GET)
	@ResponseBody
	public void pdf( HttpServletResponse response,@PathVariable Integer id_ccuc) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {

			CursoUnidadControl cursoUnidadControl = cursoUnidadControlDAO.get(id_ccuc);
			
			response.setHeader("Content-Disposition","attachment;filename=pdf_" + id_ccuc + ".pdf");

			InputStream is = new ByteArrayInputStream(cursoUnidadControl.getPdf());
		    	
			IOUtils.copy(is, response.getOutputStream());
			
			
		} catch (Exception e) {
			result.setException(e);
		}
		
	}	

	
	@RequestMapping( value="/xls", method = RequestMethod.GET)
	@ResponseBody
	public void xls(HttpServletResponse response,HttpServletRequest request,Integer id_niv, Integer id_gra,Integer id_cpu,Integer num, Integer id_anio) {
 
		 
			Nivel nivel = nivelDAO.get(id_niv);
			Grad grado = gradDAO.get(id_gra);
			PerUni perUni = perUniDAO.get(id_cpu);
			String fecha = (new SimpleDateFormat("dd/MM/yyyy")).format(new Date());
			String hora = (new SimpleDateFormat("hh:mm a")).format(new Date());
			
			Row resultados=listarUnidadesDocente(id_niv, id_gra,id_cpu,num,id_anio); 
			
			String plantilla ="Reporte_Curso_unidad.xls";
			
			SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd_hhmmss");
 
			try {
				 
				//String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();
				
				String rutacARPETA =  "C:/plantillas/";
			
	
				String nuevoArchivo = rutacARPETA + "tmp/" + plantilla.replace("Reporte_Curso_unidad", "Reporte_Curso_unidad" + dt1.format(new Date()) + ".xls")  ;

				ArchivoUtil.copyFileUsingStream(new File(rutacARPETA + plantilla), new File(nuevoArchivo));

				FileInputStream inputStream = new FileInputStream(new File(nuevoArchivo));
				Workbook workbook = WorkbookFactory.create(inputStream);

				Sheet sheet = workbook.getSheetAt(0);

				// TITULO
				sheet.getRow(0).getCell(0).setCellValue("REPORTE DE UNIDADES " + grado.getNom().toUpperCase() + " DE " +nivel.getNom().toUpperCase());
				
				//FECHA
				sheet.getRow(2).getCell(13).setCellValue("Fecha: " + fecha);
				
				//HORA
				sheet.getRow(3).getCell(13).setCellValue("Hora: " + hora);
				
				//PERIODO
				sheet.getRow(1).getCell(0).setCellValue( " - UNIDAD NRO " + num);
				
				//cursos
				List<Row> cursos = (ArrayList<Row>)resultados.get("cursos");
				List<Row> docentes = (ArrayList<Row>)resultados.get("docentes");
				
				int i_celda=2;
				for (Row curso : cursos) {
					sheet.getRow(5).getCell(i_celda++).setCellValue(curso.getString("nom"));
				}
				
				Font font = workbook.createFont();
			    font.setColor(HSSFColor.RED.index);
			  
				CellStyle style = workbook.createCellStyle();
				style.setFont(font);
				style.setAlignment(HorizontalAlignment.CENTER);

				//docentes
				int i_fila=6;
				int orden=1;
				
				InputStream my_banner_image = request.getServletContext().getResourceAsStream("/WEB-INF/views/img/pdf_verde.png");
				byte[] bytes = IOUtils.toByteArray(my_banner_image);
				int my_picture_id = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
				my_banner_image.close();
  
				for (Row docente : docentes) {
					sheet.getRow(i_fila).getCell(0).setCellValue( orden++);
					sheet.getRow(i_fila).getCell(1).setCellValue( docente.getString("docente"));
						
					Object curso_unidades_o = docente.get("curso_unidades");
					if(curso_unidades_o!=null){
						
						List<Row> curso_unidades = (ArrayList<Row>)curso_unidades_o;
						
						//poner FALTA o excel si tiene unidad
						i_celda = 2;
						for (Row curso : cursos) {
							int id_cur = curso.getInt("id");
							Row rowCursoUnidad = null;
							for (Row row : curso_unidades) {
								int id_curso_unidad = row.getInt("id_cur");
								if (id_curso_unidad == id_cur)
									rowCursoUnidad = row;
							}
							
							if(rowCursoUnidad==null){
								sheet.getRow(i_fila).getCell(i_celda).setCellValue("");
							}else{
								if(rowCursoUnidad.getInteger("id_ccuc")==null){
									sheet.getRow(i_fila).getCell(i_celda).setCellStyle(style);
									sheet.getRow(i_fila).getCell(i_celda).setCellValue("F");	
								}else{
									//InputStream my_banner_image = new FileInputStream("/WEB-INF/views/img/pdf_verde.png");
					                
					                CreationHelper helper = workbook.getCreationHelper();

					                Drawing drawing = sheet.createDrawingPatriarch();

					                ClientAnchor anchor = helper.createClientAnchor();
					                anchor.setDx1(250);
					                anchor.setDy1(20);
					                anchor.setDx2(20);
					                anchor.setDy2(20);
					                
					                anchor.setCol1(i_celda);
					                anchor.setRow1(i_fila);
					                Picture pict = drawing.createPicture(anchor, my_picture_id);
					                pict.resize();
					                

									//sheet.getRow(i_fila).getCell(i_celda).
									//sheet.getRow(i_fila).getCell(i_celda).setCellValue("X");
									sheet.getRow(i_fila).getCell(i_celda).setCellStyle(style);
								}
							}
							
							i_celda++;
						}

					}
					
					i_fila++;
				}
				
				/*
				// USUARIO
				sheet.getRow(3).getCell(4).setCellValue(usuario);
				// LOCAL
				sheet.getRow(4).getCell(4).setCellValue(local);

				sheet.shiftRows(8, sheet.getLastRowNum(), 1);
				
				int fila =1;
				Double total = new Double(0);
				DataFormat format = workbook.createDataFormat();
				Font font = workbook.createFont();
			      font.setColor(HSSFColor.BLUE.index);

				for (Map<String,Object> map : list) {

					Row row = sheet.createRow(filaInicial++);

					Double monto = Double.parseDouble( map.get("monto_total").toString());
					String tipoDes = "";
				
					if ( map.get("tipo").toString().equals("I")){
						total = total + monto;
						tipoDes = "INGRESO";
					}else{
						total = total + monto;
						tipoDes = "SALIDA";
					}
					row.createCell(columnaInicial + 0).setCellValue(fila++);
					row.createCell(columnaInicial + 1).setCellValue(map.get("nro_rec").toString());
					row.createCell(columnaInicial + 2).setCellValue(map.get("fecha").toString());
					row.createCell(columnaInicial + 3).setCellValue(map.get("concepto").toString());
					row.createCell(columnaInicial + 4).setCellValue(map.get("obs").toString());
					row.createCell(columnaInicial + 5).setCellValue((map.get("nivel")==null)? "":map.get("nivel").toString());
					row.createCell(columnaInicial + 6).setCellValue((map.get("grado")==null)? "":map.get("grado").toString());
					row.createCell(columnaInicial + 7).setCellValue((map.get("secc")==null)? "":map.get("secc").toString());
					//row.createCell(columnaInicial + 7).setCellValue(tipoDes);
					CellStyle style = workbook.createCellStyle();
					style.setDataFormat(format.getFormat("#,##0.00"));
			        style.setFont(font);

					Cell cell = row.createCell(columnaInicial + 8);
					cell.setCellValue(monto);
					cell.setCellStyle(style);

				     
				}
				
				Row row = sheet.createRow(++filaInicial);

				
				row.createCell(columnaInicial + 6).setCellValue("TOTAL");
				Cell cell = row.createCell(columnaInicial + 7); 
				cell.setCellValue(total);
			    
				CellStyle style = workbook.createCellStyle();
				style.setDataFormat(format.getFormat("#,##0.00"));
				cell.setCellStyle(style);
				*/
				inputStream.close();

				FileOutputStream outputStream = new FileOutputStream(nuevoArchivo);
				workbook.write(outputStream);
				workbook.close();
				outputStream.close();
				
				
				response.setHeader("Content-Disposition","attachment;filename=Reporte_Unidad_docente_" + dt1.format(new Date()) + ".xls");

				File initialFile = new File(nuevoArchivo);
			    InputStream is = FileUtils.openInputStream(initialFile);
			    	
				IOUtils.copy(is, response.getOutputStream());
				

			} catch (Exception e) {
				e.printStackTrace();
				//return null;
			}

			
	 
	}	
	
	
 
	
}

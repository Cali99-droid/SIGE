package com.sige.mat.web.controller;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.common.enums.EnumNivel;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.AlumnoDAO;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.AreaAnioDAO;
import com.sige.mat.dao.AulaDAO;
import com.sige.mat.dao.AulaDetalleDAO;
import com.sige.mat.dao.AulaEspecialDAO;
import com.sige.mat.dao.CapacidadDcDAO;
import com.sige.mat.dao.CompetenciaAulaDAO;
import com.sige.mat.dao.CompetenciaDcDAO;
import com.sige.mat.dao.ConfAnioAcadDcnDAO;
import com.sige.mat.dao.CronogramaLibretaDAO;
import com.sige.mat.dao.DcnAreaDAO;
import com.sige.mat.dao.DcnNivelDAO;
import com.sige.mat.dao.DesempenioAulaDAO;
import com.sige.mat.dao.DesempenioDcDAO;
import com.sige.mat.dao.FamiliarDAO;
import com.sige.mat.dao.GradDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.NotaAreaDAO;
import com.sige.mat.dao.NotaDAO;
import com.sige.mat.dao.NotaDesDAO;
import com.sige.mat.dao.NotaIndicadorDAO;
import com.sige.mat.dao.PerAcaNivelDAO;
import com.sige.mat.dao.PerUniDAO;
import com.sige.mat.dao.PeriodoAcaDAO;
import com.sige.mat.dao.PersonaDAO;
import com.sige.mat.dao.PromedioComDAO;
import com.sige.mat.dao.ReglasNegocioDAO;
import com.sige.mat.dao.SeguimientoDocDAO;
import com.sige.mat.dao.TrabajadorDAO;
import com.sige.rest.request.AreaCompetenciaReq;
import com.sige.rest.request.AreaNotaReq;
import com.sige.rest.request.CabeceraNotaReq;
import com.sige.rest.request.CapacidadReq;
import com.sige.rest.request.CompetenciaDCReq;
import com.sige.rest.request.CompetenciaReq;
import com.sige.rest.request.ConfDesempeniosReq;
import com.sige.rest.request.DesemepenioReq;
import com.sige.rest.request.DetalleNotaReq;
import com.sige.rest.request.EstadoConfDesReq;
import com.sige.rest.request.EvaluacionReq;
import com.sige.rest.request.LibretaNotaReq;
import com.sige.rest.request.NotaAlumnoUpdateReq;
import com.sige.rest.request.NotaAulaReq;
import com.sige.rest.request.NotaConductalPeriodoReq;
import com.sige.rest.request.NotaConductalReq;
import com.sige.rest.request.PeriodosReq;
import com.sige.rest.request.TardanzaPeriodoReq;
import com.sige.rest.request.TardanzaReq;
import com.sige.spring.service.NotaService;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.AreaAnio;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.AulaDetalle;
import com.tesla.colegio.model.AulaEspecial;
import com.tesla.colegio.model.ColSituacion;
import com.tesla.colegio.model.CompetenciaAula;
import com.tesla.colegio.model.CompetenciaDc;
import com.tesla.colegio.model.ConfAnioAcadDcn;
import com.tesla.colegio.model.CronogramaLibreta;
import com.tesla.colegio.model.DcnNivel;
import com.tesla.colegio.model.DesempenioAula;
import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.Nota;
import com.tesla.colegio.model.NotaArea;
import com.tesla.colegio.model.PerAcaNivel;
import com.tesla.colegio.model.PerUni;
import com.tesla.colegio.model.PeriodoAca;
import com.tesla.colegio.model.Persona;
import com.tesla.colegio.model.ReglasNegocio;
import com.tesla.colegio.model.SeguimientoDoc;
import com.tesla.colegio.model.Trabajador;
import com.tesla.colegio.model.bean.AlumnoMatriculaPromBean;
import com.tesla.colegio.model.bean.PromedioBean;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.ArchivoUtil;
import com.tesla.frmk.util.FechaUtil;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.query.JsonQueryExecuterFactory;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import com.tesla.frmk.util.RestUtil;
import com.google.gson.Gson;

@RestController
@RequestMapping(value = "/api/nota")
public class NotaRestController {
	
	final static Logger logger = Logger.getLogger(NotaRestController.class);
 
	@Autowired
	private NotaDAO notaDAO; 
	
	@Autowired
	private NotaIndicadorDAO notaIndicadorDAO;

	@Autowired
	private FamiliarDAO familiarDAO;

	@Autowired
	private MatriculaDAO matriculaDAO;
	
	@Autowired
	private AnioDAO anioDAO;
	
	@Autowired
	private AreaAnioDAO areaAnioDAO;

	@Autowired
	private PerUniDAO perUniDAO;

	@Autowired
	private AulaDAO aulaDAO;

	@Autowired
	private GradDAO gradDAO;

	@Autowired
	private AulaEspecialDAO aulaEspecialDAO;
	
	@Autowired
	private  SeguimientoDocDAO seguimientoDocDAO;
	
	@Autowired
	private  CronogramaLibretaDAO cronogramaDAO;

	@Autowired
	private CacheManager  cacheManager ;
	
	@Autowired
	private NotaAreaDAO notaAreaDAO;
	
	@Autowired
	private CompetenciaDcDAO competenciaDcDAO;
	
	@Autowired
	private CapacidadDcDAO capacidadDcDAO;
	
	@Autowired
	private DesempenioDcDAO desemepenioDcDAO;
	
	@Autowired
	private DesempenioAulaDAO desempenioAulaDAO;
	
	@Autowired
	private ReglasNegocioDAO reglasNegocioDAO;
	
	@Autowired
	private CompetenciaAulaDAO competenciaAulaDAO;
	
	@Autowired
	private NotaDesDAO notaDesDAO;
	
	@Autowired
	private PromedioComDAO promedioComDAO;

	@Autowired
	private NotaService notaService;
	
	@Autowired
	private AlumnoDAO alumnoDAO;
	
	@Autowired
	private DcnNivelDAO dcNivelDAO;
	
	@Autowired
	private ConfAnioAcadDcnDAO confAnioAcadDcnDAO;
	
	@Autowired
	private TrabajadorDAO trabajadorDAO;
	
	@Autowired
	private AulaDetalleDAO aulaDetalleDAO;
	
	@Autowired
	private PersonaDAO personaDAO;
	
	@Autowired
	private PerAcaNivelDAO perAcaNivelDAO;
	
	@Autowired
	private PeriodoAcaDAO periodoAcaDAO;
	
	@Autowired
	private DcnAreaDAO dcnAreaDAO;
	

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Nota nota) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(notaDAO.listFullByParams( nota, new String[]{"nota.id"}) );
		
		return result;
	}

	@RequestMapping( value="/grabar",method = RequestMethod.POST)
	public AjaxResponseBody grabarNota(@RequestBody EvaluacionReq evaluacionReq) throws Exception{

		AjaxResponseBody result = new AjaxResponseBody(); 
		
		notaService.grabarNota(evaluacionReq);
		result.setResult("1");
		
		return result;

	}
	
	@RequestMapping( value="/grabarNotasDesCom",method = RequestMethod.POST)
	public AjaxResponseBody grabarNota(@RequestBody NotaAulaReq notaAulaReq) throws Exception{

		AjaxResponseBody result = new AjaxResponseBody(); 
		
		notaService.grabarNotaDesempeniosCom(notaAulaReq);
		result.setResult("1");
		
		return result;

	}
	
	@RequestMapping( value="/actualizar/{per}",method = RequestMethod.POST)
	public AjaxResponseBody actualizarNota(@RequestBody  NotaAlumnoUpdateReq[] notaAlumnoUpdateReq, @PathVariable Integer per) throws Exception{

		AjaxResponseBody result = new AjaxResponseBody();
		 
		notaService.actualizarNota(notaAlumnoUpdateReq, per);
		 
		return result;

	}	
	
	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Nota nota) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( notaDAO.saveOrUpdate(nota) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/grabar2", method = RequestMethod.POST)
	public AjaxResponseBody grabarPrueba(@RequestBody EvaluacionReq evaluacion) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		logger.debug(evaluacion);
		
		try {
			//result.setResult( notaDAO.saveOrUpdate(nota) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/{id_eva}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id_eva ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			notaIndicadorDAO.delete(id_eva);
			notaDAO.delete(id_eva);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="elimarNotasDesempenioCompetencia/{id_au}/{id_cpu}/{id_dcarea}/{id_cur}", method = RequestMethod.DELETE)
	public AjaxResponseBody elimarNotasDesempenioCompetencia(@PathVariable Integer id_au, @PathVariable Integer id_cpu, @PathVariable Integer id_dcarea, @PathVariable Integer id_cur) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			notaDesDAO.deleteNotasDesempenios(id_au, id_cpu, id_cur);
			promedioComDAO.deletePromedioCom(id_au, id_cpu, id_cur);
			//notaDAO.delete(id_eva);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( notaDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarCursosProfesor", method = RequestMethod.GET)
	public AjaxResponseBody listarCursosProfesor( Integer id_tra,Integer id_anio, Integer id_au) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( notaDAO.listarCursosProfesor(id_tra, id_anio, id_au));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarEvaluaciones", method = RequestMethod.GET)
	public AjaxResponseBody listarEvaluaciones( Integer id_au, Integer nump, Integer id_anio, Integer id_cur) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( notaDAO.listarEvaluaciones(id_au, nump, id_anio, id_cur));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	/**
	 * Mostrar la grilla de los alumnos con sus indicadores para ingresar sus notas.
	 * @param id_au
	 * @param nump
	 * @param id_anio
	 * @return
	 */
	@RequestMapping( value="/listarAlumnoIndicadores", method = RequestMethod.GET)
	public AjaxResponseBody listarAlumnoIndicadores( Integer id_au, Integer nump, Integer id_anio, Integer id_eva) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( notaDAO.listarAlumnoIndicadores(id_au, nump, id_anio, id_eva));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	
	@RequestMapping( value="/listarSucursal", method = RequestMethod.GET)
	public AjaxResponseBody listarSucursal( Integer id_tra, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( notaDAO.listarSucursal(id_tra, id_anio));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarAulaProfesor", method = RequestMethod.GET)
	public AjaxResponseBody listarAula( Integer id_tra, Integer id_grad, Integer id_anio, Integer id_gir) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( notaDAO.listarAulaProfesor(id_tra, id_grad , id_anio, id_gir));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	public AjaxResponseBody  calcularCapacidad(Integer id_ind){

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	//PROMEDIOS
	
	@RequestMapping( value="/listarPromedioCursos/{id_mat}/{id_gir}", method = RequestMethod.GET)
	public AjaxResponseBody listarPromedioCursos(@PathVariable  Integer id_mat, @PathVariable  Integer id_gir) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( notaDAO.listarPromedioCursos(id_mat,0, id_gir));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}

	@RequestMapping( value="/alumno/promedioxCurso", method = RequestMethod.GET)
	public AjaxResponseBody alumnopromedioxCurso( Integer id_au, Integer nump, Integer id_anio, Integer id_cur) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( notaDAO.listarPromedioxCurso(id_au, nump, id_anio, id_cur));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/reporteNota", method = RequestMethod.GET)
	public AjaxResponseBody alumnopromedioxCurso( Integer id_cpu, Integer id_anio, Integer id_niv, String id_suc, Integer id_tra, String est, Integer id_gir) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//String fec_inicial= fec_ini;
			//String fec_final= fec_fin;
			result.setResult( notaDAO.listarNotas(id_cpu, id_anio, id_niv, id_suc, id_tra, est, id_gir));
		} catch (Exception e) {
			result.setException(e);
		}
		return result;

	}
	
	//@SuppressWarnings("unused")
	@RequestMapping(value = "/tienePermiso")
	public AjaxResponseBody tienePermiso(Integer id_anio,Integer id_niv,Integer id_cpu,String nro_doc)  throws Exception {
		AjaxResponseBody result = new AjaxResponseBody();

		Param param = new Param();
		param.put("nro_doc", nro_doc);
		Familiar familiar = familiarDAO.getByParams(param);
		
		
		if (familiar==null){
			result.setCode("402");
			result.setMsg("El nro de documento de identidad no existe.");
			return result;
		}
		logger.debug("FAMILIAR:" + familiar.getId());

		 
		Param param2= new Param();
		param2.put("id_niv", id_niv);
		param2.put("id_anio", id_anio);
		param2.put("id_cpu", id_cpu);
		
		
		
		List<CronogramaLibreta> cronograma = null;
		cronograma = cronogramaDAO.listByParams(param2, null);

		if(cronograma.size()==0){
			result.setCode("402");
			result.setMsg("No existe cronograma configurado para el nivel y periodo seleccionado.");
			return result;	
		}
		
	//	if(fec.before(cronograma.getFec_ini()))
		
		List<Row> hijos = familiarDAO.listaHijosLibreta(familiar.getId(), id_anio);
		int cantHijos = 0;
		
		logger.debug("hijos:" + hijos.size());

		if(hijos.size()==0){
			result.setCode("4021");
			result.setMsg("No tiene autorizaci�n para recojer libreta(s).");
			return result;
		}
		boolean hayhijosnivelSel = false;
		for (Row row : hijos) {
			if (row.getInteger("id_niv").equals(id_niv)){
				hayhijosnivelSel = true;
				param = new Param();
				//param.put("id_fam", familiar.getId());
				param.put("id_cpu", id_cpu);
				param.put("id_mat", row.getInteger("id_mat"));
				SeguimientoDoc seguimiento = seguimientoDocDAO.getByParams(param);
				
				if (seguimiento==null){
					cantHijos++;
					//result.setCode("403");
					//result.setMsg("�Libreta de Notas fue emitida!");
					//return result;
				}
			}
		}

		logger.debug("hayhijosnivelSel:" +hayhijosnivelSel);

		
		if (!hayhijosnivelSel){
			result.setCode("403");
			result.setMsg("No tiene hijos en el nivel seleccionado");
			return result;
		}

		logger.debug("cantHijos:" +cantHijos);

		if (cantHijos==0){
			result.setCode("403");
			result.setMsg("�Libreta de Notas fue emitida!");
			return result;
		}
		
		logger.debug("result:" +result);

		return result;
	}
	@RequestMapping(value = "/libreta")
	@ResponseBody
	public void getLibreta(HttpServletResponse request,HttpServletResponse response,Integer id_anio,Integer id_niv,Integer id_cpu,String nro_doc, Integer id_gir)  throws Exception {
		
		//response.setContentType("application/pdf");
		//response.addHeader("Content-Disposition", "attachment; filename=libreta_" + nro_doc +".pdf");

		Param param = new Param();
		param.put("nro_doc", nro_doc);
		Familiar familiar = familiarDAO.getByParams(param);
		
		if (familiar==null)
			return;
		
		//List<Row> hijos = familiarDAO.listaHijos(familiar.getId());
		List<Row> hijos = familiarDAO.listaHijosLibreta(familiar.getId(), id_anio);
		List<Matricula> matriculas = new ArrayList<Matricula>();
		
		//Cronograma
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

		String format = formatter.format(new Date());
		int fecActual = Integer.parseInt(format); 
		
		for (Row row : hijos) {
			Integer id_mat = row.getInteger("id_mat");
			Matricula matricula = matriculaDAO.getFull(id_mat,new String[]{Alumno.TABLA,Nivel.TABLA,Grad.TABLA,Aula.TABLA,ColSituacion.TABLA});
			
			//listar solo los del nivel q corresponde
			if (matricula.getId_niv().equals(id_niv)){
				
				param = new Param();
				//param.put("id_fam", familiar.getId());
				param.put("id_cpu", id_cpu);
				param.put("id_mat", row.getInteger("id_mat"));
				SeguimientoDoc seguimiento = seguimientoDocDAO.getByParams(param);
				
				//Verificamos cronograma
				Param param2= new Param();
				param2.put("id_niv", id_niv);
				param2.put("id_anio", id_anio);
				param2.put("id_cpu", id_cpu);
				CronogramaLibreta cronograma = cronogramaDAO.getByParams(param2);
				if(cronograma!=null){
					if((fecActual>=Integer.parseInt(formatter.format(cronograma.getFec_ini())) && fecActual<=Integer.parseInt(formatter.format(cronograma.getFec_fin())))){
						if (seguimiento==null){ 
							matriculas.add(matricula);
						}
					}
				}
			}
			
		}
		
		if(matriculas.size()==0)
			return;
		
		String nombrePDF = "libreta_" + nro_doc +".pdf";
		response.setHeader("Content-Disposition","inline; filename=\"" + nombrePDF +"\"");
		response.setContentType("application/pdf; name=\"" + nombrePDF + "\"");
		response.getOutputStream().write(generaLibreta(familiar.getId(),id_anio,id_cpu,matriculas, id_gir));


	}
 
	
	@SuppressWarnings("unchecked")
	public byte[] generaLibreta(Integer id_fam, Integer id_anio, Integer id_cpu,List<Matricula> matriculas, Integer id_gir) throws Exception {

		logger.debug("generaLibreta");
		Anio anio = anioDAO.get(id_anio);
		String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();
		
		Document document = new Document();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfCopy copy = new PdfCopy(document, baos);

        document.open();
		
		for (Matricula matricula : matriculas) {
			
			List<Row> listaPeriodos = cronogramaDAO.listarPeriodosX_Nivel(matricula.getId_niv(), id_anio, id_gir);
			
			AulaEspecial col_aula_especial = aulaEspecialDAO.getByMatricula(matricula.getId());
			Integer id_au = matricula.getId_au_asi();
			String tipoAula = "N";
			if (col_aula_especial!=null){
				id_au = col_aula_especial.getId_au();
				tipoAula = "F";
			}
			HashMap<String,Object> parameters = new HashMap<String,Object>();

			logger.debug("antes del promedio aula" + id_au + "-"+ new Date());
			
			//String puesto1 = null;
			if(matricula.getColSituacion().getCod()!=null)
				parameters.put("P_SITUACION", "(" + matricula.getColSituacion().getCod() + ")" + matricula.getColSituacion().getNom());
			else
				parameters.put("P_SITUACION", "");
			
			int nro_periodo = 0;
			// PUESTOS POR PERIODO
			parameters.put("P_PUESTO2", "");
			parameters.put("P_PUESTO3", "");
			parameters.put("P_PUESTO4", "");
					
			for (Row row : listaPeriodos) {
				nro_periodo++;
				Integer id_cpu_periodo = row.getInteger("id");
				
				if ( id_cpu_periodo<=id_cpu ){
					List<AlumnoMatriculaPromBean> promediosAula = notaDAO.promediosPorAula(id_au, id_cpu_periodo, tipoAula, id_gir);
					
					logger.debug("promediosAula.size():" + promediosAula.size());
					logger.debug("alumnoMatriculaPromBean.getId_mat():" + promediosAula.size());

					for (AlumnoMatriculaPromBean alumnoMatriculaPromBean : promediosAula) {
						
						logger.debug("alumnoMatriculaPromBean:" + alumnoMatriculaPromBean);
						
						if (alumnoMatriculaPromBean.getId_mat().equals(matricula.getId())){
							String puesto = (alumnoMatriculaPromBean.getPuesto()!=null)?alumnoMatriculaPromBean.getPuesto().toString():"";
							parameters.put("P_PUESTO" + nro_periodo, puesto );
						}
					}
				}

				
			}
			// PUESTO ANUAL
			
			String puesto = null;
			List<AlumnoMatriculaPromBean> promediosAula = notaDAO.promediosAnualAula(id_au,tipoAula,id_cpu, id_gir);

			for (AlumnoMatriculaPromBean alumnoMatriculaPromBean : promediosAula) {
				if (alumnoMatriculaPromBean.getId_mat().equals(matricula.getId())){
					puesto = (alumnoMatriculaPromBean.getPuesto()!=null)?alumnoMatriculaPromBean.getPuesto().toString():"";
				}
			}

			parameters.put("P_VACIO", puesto);
		
			InputStream inputStream = null;
			String ruta=null;
			logger.debug("matricula.getid()>>>>" + matricula.getId());
			logger.debug("matricula.getGrad().getId>>>>" + matricula.getGrad().getId());
			logger.debug("matricula.getGrad().getId_nvl>>>>" + matricula.getGrad().getId_nvl());

			if (matricula.getGrad().getId_nvl().equals(Constante.NIVEL_SECUNDARIA)){
				ruta=rutacARPETA  + "libreta.jrxml";
			}else if (matricula.getGrad().getId_nvl().equals(Constante.NIVEL_INICIAL)){
				ruta=rutacARPETA  + "libretaInicial.jrxml";
			}else{
				ruta=rutacARPETA  + "libretaTrimestre.jrxml";
			}
			
			logger.debug("ruta>>>>>>>>>" + ruta);
			inputStream = new FileInputStream( ruta);

			
			// titulo
			parameters.put("P_TITULO", "BOLETA DE NOTAS " + anio.getNom());
			parameters.put("P_ALUMNO", matricula.getAlumno().getApe_pat() + " " + matricula.getAlumno().getApe_mat() + " " + matricula.getAlumno().getNom());
			parameters.put("P_NIVEL", matricula.getNivel().getNom());
			parameters.put("P_GRADO", matricula.getGrad().getNom());
			parameters.put("P_SECCION", matricula.getAula().getSecc());

			
			parameters.put("P_FECHA", FechaUtil.toText(new Date()));

			// grilla
			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

			List<PromedioBean> list = new ArrayList<PromedioBean>();
			
			Row promedios = notaDAO.listarPromedioCursos(matricula.getId(),id_cpu, id_gir);
			List<Row> cursos = (List<Row>)promedios.get("listaProfesoresCursos");
			
			int nro = 0;
			
			Map<Integer, Integer> sumaCursosPeriodo = new HashMap<Integer, Integer>();
			sumaCursosPeriodo.put(1, 0 );
			sumaCursosPeriodo.put(2, 0 );
			sumaCursosPeriodo.put(3, 0);
			sumaCursosPeriodo.put(4, 0 );
			
			Map<Integer, Integer> cantCursosPeriodo = new HashMap<Integer, Integer>();
			cantCursosPeriodo.put(1, 0 );
			cantCursosPeriodo.put(2, 0 );
			cantCursosPeriodo.put(3, 0);
			cantCursosPeriodo.put(4, 0 );

			for (Row row : cursos) {
				String curso = row.getString("curso");
				
				PromedioBean promedioBean = new PromedioBean();
				promedioBean.setNro(++nro);
				promedioBean.setCurso(curso);
				
				List<Row> periodos = (List<Row>)row.get("periodos");
				
				int i = 0;
				Integer sumaPromedio = 0;
				Integer cantPeriodos = 0;
				int cantCursos = 0;
				for (Row periodo : periodos) {
					i++;
					if(periodo.getInteger("id_cpu")<= id_cpu){

						Row curso1 = (Row)periodo.get("cursos");
						Integer promedioInt = null;
						if (curso1!=null && curso1.getString("competenciasPromedio")!=null && !curso1.getString("competenciasPromedio").equals("EXO")){
							BigDecimal promedio = curso1.getBigDecimal("competenciasPromedio");
							if(promedio!=null){
								
								cantCursosPeriodo.put(i, cantCursosPeriodo.get(i)+1 );
								
								promedioInt = promedio.setScale(0, RoundingMode.HALF_UP).intValue();//ENTERO SUMA DE PROMEDIOS
								sumaPromedio = sumaPromedio + promedioInt;
								cantPeriodos ++;
								
								sumaCursosPeriodo.put(i,sumaCursosPeriodo.get(i) + promedioInt );
	
							}
							
						}
				
						if (curso1!=null && curso1.getString("competenciasPromedio")!=null  && curso1.getString("competenciasPromedio").equals("EXO")){
							if(i==1)
								promedioBean.setPeriodo1( "EXO");
							else if(i==2)
								promedioBean.setPeriodo2( "EXO");
							else if(i==3)
								promedioBean.setPeriodo3( "EXO");
							else
								promedioBean.setPeriodo4( "EXO");
						}else{
							
							logger.debug("promedioInt:" + promedioInt);
							
							if(promedioInt!=null)
								cantCursos++;
								
							if(i==1 && promedioInt!=null){
								cantCursos++;
								if (matricula.getGrad().getId_nvl().equals(Constante.NIVEL_SECUNDARIA))
									promedioBean.setPeriodo1( promedioInt.toString());
								else
									promedioBean.setPeriodo1(convertNotaPrimaria(promedioInt,matricula.getGrad().getId_nvl() ) );
							}if(i==2 && promedioInt!=null){
								if (matricula.getGrad().getId_nvl().equals(Constante.NIVEL_SECUNDARIA))
									promedioBean.setPeriodo2( promedioInt.toString());
								else
									promedioBean.setPeriodo2(convertNotaPrimaria(promedioInt,matricula.getGrad().getId_nvl()) );
							}if(i==3 && promedioInt!=null){
								if (matricula.getGrad().getId_nvl().equals(Constante.NIVEL_SECUNDARIA))
									promedioBean.setPeriodo3( promedioInt.toString());
								else
									promedioBean.setPeriodo3(convertNotaPrimaria(promedioInt,matricula.getGrad().getId_nvl()) );
							}if(i==4 && promedioInt!=null){
								if (matricula.getGrad().getId_nvl().equals(Constante.NIVEL_SECUNDARIA))
									promedioBean.setPeriodo4( promedioInt.toString());
								else
									promedioBean.setPeriodo4(convertNotaPrimaria(promedioInt,matricula.getGrad().getId_nvl()) );
							}
						}
				
					}
				}
				if (cantPeriodos>0){
						//if (matricula.getGrad().getId_nvl().equals(Constante.NIVEL_SECUNDARIA))
						//Integer promerdioAnual = (int) Math.ceil((double)sumaPromedio / cantPeriodos);
						
						BigDecimal promerdioAnualBig =  new BigDecimal(sumaPromedio).divide(new BigDecimal(cantPeriodos),2, RoundingMode.HALF_UP);
						promedioBean.setPromedio(Integer.toString(promerdioAnualBig.setScale(0, RoundingMode.HALF_UP).intValue()));
						
					//else
					//	promedioBean.setPromedio(convertNotaPrimaria(sumaPromedio/cantPeriodos) );
					
					//promedioBean.setPromedio(sumaPromedio/cantPeriodos);
				}
				
				list.add(promedioBean);

			}
			
			//promedio de curso por periodo
			//secundaria
			BigDecimal totalSuma = new BigDecimal(0);
			BigDecimal totalCant = new BigDecimal(0);
			int index=0;
			for (Row periodo : listaPeriodos) {
				index++;
				if(periodo.getInteger("id")<= id_cpu){
			//for (int index = 1; index <= 4; index++) {
				//if(index<=2){//parametrizar
					
					//BigDecimal promedio = new BigDecimal(sumaCursosPeriodo.get(index)).divide(new BigDecimal(cantidadCursos),2,RoundingMode.HALF_UP);
					if(cantCursosPeriodo.get(index)!=0){
						BigDecimal promedio = new BigDecimal(sumaCursosPeriodo.get(index)).divide(new BigDecimal(cantCursosPeriodo.get(index)),2,RoundingMode.HALF_UP);
						String promedioPeriodo = "";
						if (promedio.intValue()>0){
							promedioPeriodo = promedio.toString();
							totalSuma = totalSuma.add(promedio);
							totalCant = totalCant.add(new BigDecimal(1));
						}
						if (matricula.getGrad().getId_nvl().equals(Constante.NIVEL_SECUNDARIA))
							parameters.put("P_PROMEDIO" + index, promedioPeriodo);
						else{
							
							if (matricula.getGrad().getId_nvl().equals(Constante.NIVEL_INICIAL))
								parameters.put("P_PROMEDIO" + index, convertNotaPrimaria(new BigDecimal(promedioPeriodo), Constante.NIVEL_INICIAL) );
							else
								parameters.put("P_PROMEDIO" + index, promedioPeriodo);
						}
						
					}

				}
				else
					parameters.put("P_PROMEDIO" + index, "");
			}
			
			
				if (matricula.getGrad().getId_nvl().equals(Constante.NIVEL_SECUNDARIA)){
					Integer promedioAnual = 0;
					int nro_promedios = 0;
					for (PromedioBean promedio : list) {
						if(promedio.getPromedio()!=null && !"".equals(promedio.getPromedio())){
							promedioAnual += Integer.parseInt( promedio.getPromedio());
							nro_promedios ++;
						}else
							promedio.setPromedio(null);
					}
					BigDecimal totalSumaanual = new BigDecimal(promedioAnual);
					parameters.put("P_PROMEDIO_FINAL" , totalSumaanual.divide(new BigDecimal(nro_promedios),2,RoundingMode.HALF_UP));
				}else
					parameters.put("P_PROMEDIO_FINAL" , convertNotaPrimaria(totalSuma.divide(totalCant,2,RoundingMode.HALF_UP),matricula.getGrad().getId_nvl()));

			//comportamiento
			
			Row tutor = (Row)promedios.get("tutor");
			logger.debug("tutor:" + tutor);

			if (tutor!=null  ){
				Object objPeriodos = tutor.get("periodos");
				List<Row> periodos = (List<Row>)objPeriodos;
				int i = 0;
				Integer comportamientoSuma = 0;
				Integer comportamientoCant= 0;
				
				parameters.put("P_COMPORTAMIENTO" + 1, "");
				parameters.put("P_COMPORTAMIENTO" + 2, "");
				parameters.put("P_COMPORTAMIENTO" + 3, "");
				parameters.put("P_COMPORTAMIENTO" + 4, "");
				parameters.put("P_COMPORTAMIENTO_FINAL","" );
				for (Row row : periodos) {
					i++;
					logger.debug("i:" + i);
					logger.debug("id_cpu:" + id_cpu);
					if(row.getInteger("id_cpu")<= id_cpu){
						
						BigDecimal comportamientoDecimal = row.getBigDecimal("promedioComportamiento");
						
						logger.debug("comportamientoDecimal:" + comportamientoDecimal);
						String comportamiento = "";
						
						//TODO falta parametrizar
						if (comportamientoDecimal!=null){
								int notaComportamiento = comportamientoDecimal.intValue();
								comportamientoSuma = comportamientoSuma + notaComportamiento;
								comportamientoCant ++;
								
								if (matricula.getGrad().getId_nvl().equals(Constante.NIVEL_SECUNDARIA)){
									/*
									if (notaComportamiento>=17)
										comportamiento = "AD";
									else if (notaComportamiento>=13)
										comportamiento = "A";
									else if (notaComportamiento>=11)
										comportamiento = "B";
									else
										comportamiento = "C";
									*/
									comportamiento = convertNotaPrimaria(notaComportamiento,  Constante.NIVEL_INICIAL);
								}else
									comportamiento = convertNotaPrimaria(notaComportamiento,  Constante.NIVEL_INICIAL);
						}
						
						parameters.put("P_COMPORTAMIENTO" + i, comportamiento);
						
					}

					
				}
				
				String comportamientoFinal = "";

				if (comportamientoCant!=0){
					
					if (matricula.getGrad().getId_nvl().equals(Constante.NIVEL_SECUNDARIA)){
						Integer comportamientoPromedio = comportamientoSuma/comportamientoCant;
						/*
						if (comportamientoPromedio>=17)
							comportamientoFinal = "AD";
						else if (comportamientoPromedio>=13)
							comportamientoFinal = "A";
						else if (comportamientoPromedio>=11)
							comportamientoFinal = "B";
						else
							comportamientoFinal = "C";
						*/
						comportamientoFinal = convertNotaPrimaria(comportamientoPromedio,  Constante.NIVEL_INICIAL);

					}else{
						Integer comportamientoPromedio = comportamientoSuma/comportamientoCant;
						//comportamientoFinal = convertNotaComPrimaria(comportamientoPromedio);
						comportamientoFinal = convertNotaPrimaria(comportamientoPromedio,  Constante.NIVEL_INICIAL);

					}

				}
				
				parameters.put("P_COMPORTAMIENTO_FINAL",comportamientoFinal );
			}else{
				parameters.put("P_COMPORTAMIENTO" + 1, "");
				parameters.put("P_COMPORTAMIENTO" + 2, "");
				parameters.put("P_COMPORTAMIENTO" + 3, "");
				parameters.put("P_COMPORTAMIENTO" + 4, "");
				parameters.put("P_COMPORTAMIENTO_FINAL","" );

			}
			

			logger.debug("parameters");
			logger.debug(parameters);
			logger.debug("list");
			logger.debug(list);
			
			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list);

			logger.debug("parameters");
			logger.debug(parameters);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);
			byte[] bytesPDF =  JasperExportManager.exportReportToPdf(jasperPrint);
			
			SeguimientoDoc seguimiento_doc = new SeguimientoDoc();
			seguimiento_doc.setId_fam(id_fam);
			seguimiento_doc.setId_cpu(id_cpu);
			seguimiento_doc.setId_mat(matricula.getId());
			seguimiento_doc.setEst("A");
			seguimiento_doc.setTip("L");
			seguimiento_doc.setArchivo(bytesPDF);
			seguimiento_doc.setFec_ins(new Date());
			seguimientoDocDAO.actualizarEntrega(seguimiento_doc);
			
			PdfReader reader = new PdfReader(bytesPDF);

			copy.addPage(copy.getImportedPage(reader, 1));
			
			
		}
		
		document.close();
		return  baos.toByteArray();

	}
	@RequestMapping(value = "/generarLibretaNuevo/{id_anio}/{id_cpu}/{id_gra}/{id_gir}")
	//@SuppressWarnings("unchecked") //aquiii
	//public AjaxResponseBody generaLibreta_Nuevo(Integer id_fam, Integer id_anio, Integer id_cpu,List<Matricula> matriculas) throws Exception {
	public void generaLibreta_Nuevo(@PathVariable Integer id_anio,@PathVariable Integer id_cpu,@PathVariable Integer id_gra,@PathVariable Integer id_gir, HttpServletResponse response) throws Exception {
		AjaxResponseBody result = new AjaxResponseBody();
        CabeceraNotaReq cabecera = new CabeceraNotaReq();
		try {
			
			List<Row> list_matriculas=matriculaDAO.listarAlumnosxGradoAnioSinPF(id_gra, id_anio, id_gir);
			logger.debug("generaLibreta");
			Anio anio = anioDAO.get(id_anio);
			//String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();
			for (Row row_mat : list_matriculas) {
				Document document = new Document();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				//PdfCopy copy = new PdfCopy(document, baos);

		        document.open();
		        Integer id_mat=row_mat.getInteger("id_mat");
		        String abrv=row_mat.getString("abrv_classroom")+" "+row_mat.getString("secc");
		        String dni_alumno=row_mat.getString("nro_doc");
		        Matricula matricula = matriculaDAO.get(id_mat);
		        Row alumno = alumnoDAO.datosAlumno(id_mat).get(0);
		        Row periodo_academico=perUniDAO.datosPeriodoxNivel(id_anio, matricula.getId_niv()).get(0);
		        String titulo = "INFORME DE LOS PROCESOS DEL EDUCANDO - "+anio.getNom().toUpperCase();
		        
		        LibretaNotaReq libreta = new LibretaNotaReq();

		        cabecera.setAlumno(alumno.getString("alumno"));
		        cabecera.setAnio(anio.getNom());
		        cabecera.setCod_alumno(alumno.getString("cod"));
		        cabecera.setId_mat(id_mat);
		        cabecera.setNivel(alumno.getString("nivel"));
		        cabecera.setPeriodo(periodo_academico.getString("nump")+" "+periodo_academico.getString("periodo"));
		        cabecera.setSalon(alumno.getString("aula"));
		        cabecera.setTitulo(titulo);
		        ConfAnioAcadDcn confAnioAcadDcn = confAnioAcadDcnDAO.getByParams(new Param("id_anio",id_anio));
		      //  DetalleNotaReq detalle_nota = new DetalleNotaReq();
		        Param param = new Param();
		        param.put("id_niv", matricula.getId_niv());
		        param.put("id_dcn", confAnioAcadDcn.getId_dcn());
		        DcnNivel dcnNivel= dcNivelDAO.getByParams(param);
		        List<AreaCompetenciaReq> areas_competencia= new ArrayList<AreaCompetenciaReq>();
		      //Obtengo la lista de Areas
		       List<Row> areas= dcnAreaDAO.listarAreasComboAnio(dcnNivel.getId(),id_anio,id_gra,id_gir);
		       for (Row row : areas) {
					List<CompetenciaDc> competenciasDc= competenciaDcDAO.listByParams(new Param("id_dcare",row.getInteger("id")),null);
					List<CompetenciaDCReq> competenciaDCReqs = new ArrayList<CompetenciaDCReq>();
					for (CompetenciaDc competenciaDC : competenciasDc) {
						CompetenciaDCReq competenciaDCReq = new CompetenciaDCReq();
						competenciaDCReq.setId(competenciaDC.getId());
						competenciaDCReq.setNom(competenciaDC.getNom());
						competenciaDCReq.setId_area(competenciaDC.getId_dcare());
						//poner la nota final por competencia
						competenciaDCReqs.add(competenciaDCReq);
					}
					AreaCompetenciaReq area_com= new AreaCompetenciaReq();
					area_com.setId(row.getInteger("id"));
					area_com.setNom_area(row.getString("value"));
					area_com.setCompetencias(competenciaDCReqs);
					areas_competencia.add(area_com);
		       }	
		        cabecera.setList_areas_com(areas_competencia);
		        cabecera.setCant_areas(areas.size());
		        cabecera.setId_mat(id_mat);
		      
		        List<PeriodosReq> periodos_acad = new ArrayList<PeriodosReq>();
		        List<Row> listaPeriodos = cronogramaDAO.listarPeriodosX_Nivel(matricula.getId_niv(), id_anio, id_gir);
		        for (Row row : listaPeriodos) {
					PeriodosReq periodosReq = new PeriodosReq();
					periodosReq.setId(row.getInteger("id"));
					periodosReq.setNom_periodo(row.getString("nro_per_rom"));
					periodos_acad.add(periodosReq);
					
				}
		        
	 	        PerUni perUni=perUniDAO.get(id_cpu);
	 	        Integer nro_per=perUni.getNump();
	 	        TardanzaReq tardanzas_totales = new TardanzaReq();
	 	        NotaConductalReq nota_final = new NotaConductalReq();
	 	        List<TardanzaPeriodoReq> tardanzas = new ArrayList<TardanzaPeriodoReq>();
	 	        List<NotaConductalPeriodoReq> nota_conductual = new ArrayList<NotaConductalPeriodoReq>();
	 	        for (Row row : listaPeriodos) {
	 	        	if(row.getInteger("aux1")<= nro_per) {
	 	        		TardanzaPeriodoReq tardanza = new TardanzaPeriodoReq();
	 	        		tardanza.setPeriodo(row.getString("nro_per_rom"));
	 	        		tardanzas.add(tardanza);
	 	        		NotaConductalPeriodoReq notaConductaReq = new NotaConductalPeriodoReq();
	 	        		notaConductaReq.setPeriodo(row.getString("nro_per_rom"));
	 	        		nota_conductual.add(notaConductaReq);
	 	        	}
	 	        	
	 	        } 
	 	        tardanzas_totales.setList_tardanzas_periodo(tardanzas);
	 	        nota_final.setList_notas_comportamiento(nota_conductual);
	 	        cabecera.setList_tardanzas(tardanzas_totales);
	 	        cabecera.setList_notas_cond(nota_final);
		       // if(periodo.getInteger("id")<= id_cpu){
		       // PerUni perUni=perUniDAO.get(id_cpu);
		    //    Integer nro_per=perUni.getNump();
		     //   List<AreaNotaReq> areas_nota_total= new ArrayList<AreaNotaReq>();
		      //  for (Row row : listaPeriodos) {
		        	//if(row.getInteger("aux1")<= nro_per) {
		        List<AreaNotaReq> areas_nota=notaDAO.promediosAreasMatricula(id_mat, matricula.getId_alu(), matricula.getId_gra(), matricula.getId_au_asi(),id_cpu, id_anio, dcnNivel.getId(),id_gir);
		        		// areas_nota_total.add(areas_nota);
		        	//}
		      //  }
		        cabecera.setList_areas_notas(areas_nota);
		        cabecera.setList_periodos(periodos_acad);
		        AulaDetalle aulaDetalle = aulaDetalleDAO.getByParams(new Param("id_au",matricula.getId_au_asi()));
		        Trabajador tutor = trabajadorDAO.get(aulaDetalle.getId_tut());
		        Persona persona = personaDAO.get(tutor.getId_per());
		        cabecera.setNom_tutor(persona.getApe_pat()+" "+persona.getApe_mat()+" "+persona.getNom());
		        Param param3 = new Param();
		        param3.put("id_niv", matricula.getId_niv());
		        param3.put("id_anio", id_anio);
		        PerAcaNivel perAcaNivel = perAcaNivelDAO.getByParams(param3);
		        PeriodoAca periodoAca = periodoAcaDAO.getByParams(new Param("id",perAcaNivel.getId_cpa()));
		        cabecera.setTipo_periodo(periodoAca.getNom());
		        
				//libreta.setCabecera(cabecera);
				//libreta.setDetalle(detalle_nota);
				//Gson gson = new Gson();
				System.out.println(cabecera);
				String nombrePDF = "libreta_" + dni_alumno +"_"+abrv+".pdf";
				//response.setHeader("Content-Disposition","inline; filename=\"" + nombrePDF +"\"");
				//response.setContentType("application/pdf; name=\"" + nombrePDF + "\"");
				String pdf=generaLibretaNuevo(cabecera);
				//response.getOutputStream().write();
				File initialFile = new File(pdf);
			    InputStream is = FileUtils.openInputStream(initialFile);
				IOUtils.copy(is, response.getOutputStream());
			}
			
			
			
			//String libreta_conf = gson.toJson(cabecera);
			//System.out.println(libreta_conf);
			//document.close();
			//return  baos.toByteArray();
			//result.setResult(cabecera);
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print((e.getMessage().toString()));
		}
		

	}
	
	@SuppressWarnings("unchecked")
	public String generaLibretaNuevo(CabeceraNotaReq cabecera) throws Exception {

		logger.debug("generaLibreta");
		String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();
		//Genero el json
		String nombreJSON = cabecera.getPeriodo().replace(" ", "") + "-" + cabecera.getCod_alumno();
		String rutaJson = "C:\\plantillas\\tmp\\" + nombreJSON + ".json";
		//String rutaJson = rutacARPETA + nombreJSON + ".json";

	    Path path = Paths.get(rutaJson);
	    byte[] strToBytes = cabecera.toString().getBytes();

	    Files.write(path, strToBytes);
	    
		//Anio anio = anioDAO.get(id_anio);
		
		Document document = new Document();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfCopy copy = new PdfCopy(document, baos);

        document.open();
		
    	InputStream inputStream = null;
		String ruta=null;
		
			ruta=rutacARPETA  + "final_ireport_java/report.jrxml";
		
		//inputStream = new FileInputStream(ruta);
		
		//String JASPER_FILE_LOCATION=rutacARPETA+"final_ireport_java/";
	   // String IMAGE_LOCATION = rutacARPETA+"final_ireport_java/";
	    String JASPER_FILE_LOCATION="C:\\plantillas\\final_ireport_java\\";
	    String IMAGE_LOCATION = "C:\\plantillas\\final_ireport_java\\";
		//Parametros
		Map<String, Object> parameters = new HashMap<>();
        parameters.put(JsonQueryExecuterFactory.JSON_DATE_PATTERN, "yyyy-MM-dd");
        parameters.put(JsonQueryExecuterFactory.JSON_NUMBER_PATTERN, "#,##0.##");
        parameters.put(JsonQueryExecuterFactory.JSON_LOCALE, Locale.ENGLISH);
        parameters.put(JRParameter.REPORT_LOCALE, Locale.US);
        parameters.put(JsonQueryExecuterFactory.JSON_SOURCE,rutaJson);
        parameters.put("SUBREPORT_DIR", JASPER_FILE_LOCATION);
        parameters.put("IMAGE_LOCATION",IMAGE_LOCATION );
        
        System.out.println( "generating jasper report..." );
        String compiledFile = JASPER_FILE_LOCATION+"report.jasper"; 
       // Map<String, Object> parameters = getParameters();
        JasperPrint jasperPrint = JasperFillManager.fillReport(compiledFile, parameters);
        String pdfGenerado =rutacARPETA + "/tmp/libreta" + nombreJSON +".pdf";
		JasperExportManager.exportReportToPdfFile(jasperPrint, pdfGenerado);
		
		logger.debug("despues de JasperExportManager.exportReportToPdfFile");
		
		document.close();
		//return  baos.toByteArray();
		
		return pdfGenerado;
		/*File initialFile = new File(pdfGenerado);
	    InputStream is = FileUtils.openInputStream(initialFile);
	    	
		
		response.setContentType("application/pdf");
		response.addHeader("Content-Disposition", "attachment; filename=boleta" + nombreJSON + ".pdf");

		IOUtils.copy(is, response.getOutputStream());*/
		
        /*byte[] bytesPDF =  JasperExportManager.exportReportToPdf(jasperPrint);
        PdfReader reader = new PdfReader(bytesPDF);

		copy.addPage(copy.getImportedPage(reader, 1));

		// grilla
		//JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
		//JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
		//HashMap<String,Object> parameters = new HashMap<String,Object>();
		//JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(null);
		
		// JsonDataSource datasource = new JsonDataSource(new ByteArrayInputStream(cabecera.toString().getBytes("UTF-8")));
       //  JasperPrint objJasperPrint = JasperFillManager.fillReport( new FileInputStream(new File(reportPath + "General.jasper")), params, datasource );  
		//JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);
		//byte[] bytesPDF =  JasperExportManager.exportReportToPdf(jasperPrint);
			
			
			//PdfReader reader = new PdfReader(bytesPDF);

			//copy.addPage(copy.getImportedPage(reader, 1));
			
			
		//}
		
		document.close();*/
		
		//return  baos.toByteArray();

	}
	
	/**
	 * Transforma las notas 1
	 * @param nota
	 * @param id_niv
	 * @return
	 */
	private String convertNotaPrimaria(Integer nota, Integer id_niv){
		
		if (id_niv.equals(Constante.NIVEL_PRIMARIA)){
			if (nota==null )
				return "";
			else if (nota<=10)
				return "C";
			else if (nota<=12)
				return "B";
			else if (nota<=16)
				return "A";
			else 
				return "AD";
			
		}else{
			if (nota==null )
				return "";
			else if (nota==4)
				return "AD";
			else if (nota>=3)
				return "A";
			else if (nota>=2)
				return "B";
			else
				return "C";
		}
		
	}

	private String convertNotaPrimaria(BigDecimal nota, Integer id_niv){
		
		if (id_niv.equals(Constante.NIVEL_PRIMARIA)){
			if (nota==null )
				return "";
			else if (nota.compareTo(new BigDecimal(10))<=0)
				return "C";
			else if (nota.compareTo(new BigDecimal(12))<=0)
				return "B";
			else if (nota.compareTo(new BigDecimal(16))<=0)
				return "A";
			else 
				return "AD";
			
		}else{
			
			int not_dec = nota.setScale(0,RoundingMode.HALF_UP).intValue();

			if (not_dec==4)
				return "AD";
			else if (not_dec>=3)
				return "A";
			else if (not_dec>=2)
				return "B";
			else
				return "C";
		}
		
		
	}

	
	private String convertNotaComPrimaria(Integer nota){
		/*
		0-10: C
		11-12: B
		13-16: A
		17-20: AD
		*/
		if (nota==null )
			return "";
		else if (nota<=10)
			return "C";
		else if (nota<=12)
			return "B";
		else if (nota<=16)
			return "A";
		else 
			return "AD";
	}
	
	@RequestMapping(value = "/listarNotasAula")
	public AjaxResponseBody listarNotasAula(Integer id_anio,Integer id_cur, Integer id_niv, Integer id_au, Integer nump) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(notaDAO.listNotaAula(id_anio,id_cur, id_niv, id_au, nump) );
		
		return result;
	}
	
	@RequestMapping(value = "/listarNotasAulaAreaoCurso")
	public AjaxResponseBody listarNotasAulaAreaoCurso(Integer id_anio,String id_cur, Integer id_niv, Integer id_au, Integer id_dcare, Integer id_gra, Integer id_cpu) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(notaDAO.listNotasAulaAreaoCurso(id_anio, id_cur, id_niv, id_au, id_dcare, id_gra, id_cpu) );
		
		return result;
	}
	
	@RequestMapping(value = "/listarNotasAlumnoPeriodo")
	public AjaxResponseBody listarNotasAlumnoPeriodo(Integer id_gir,Integer id_anio, Integer id_per, Integer id_cpu) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(notaDAO.listNotasAlumnoPeriodo(id_gir, id_anio, id_per, id_cpu));
		
		return result;
	}
	
	@RequestMapping(value = "/listarNotasxCompetencia")
	public AjaxResponseBody listarNotasxCompetencia(Integer id_com,Integer id_cua, Integer id_au, Integer id_cpu) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(notaDesDAO.listarNotasCompetencias(id_com, id_au, id_cpu, id_cua) );
		
		return result;
	}
	
	@RequestMapping(value = "/listarNotasxCapacidad")
	public AjaxResponseBody listarNotasxCapacidad(Integer id_cap,Integer id_cua, Integer id_au, Integer id_cpu) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(notaDesDAO.listarNotasCapacidades(id_cap, id_au, id_cpu, id_cua));
		
		return result;
	}
	
	@Transactional
	@RequestMapping(value = "/listarConfCompCapDesem")
	public AjaxResponseBody  generarConfDesempenios(Integer id_au, Integer id_dcare, Integer id_gra, Integer id_cpu, Integer id_cua){	
		AjaxResponseBody result = new AjaxResponseBody();
		try {
			
			List<Row> lista_competencias=competenciaDcDAO.listarCompetenciasxAula(id_au, id_dcare, id_gra);
			List<Row> lista_capacidades=capacidadDcDAO.listarCapacidadesxAula(id_au, id_dcare, id_gra);
			List<Row> lista_desempenios=desemepenioDcDAO.listarDesempeniosxAula(id_au, id_dcare, id_gra, id_cpu, id_cua);
			
			List<CompetenciaReq> ListaCompetencias = new ArrayList<>();
			
			for (Row row : lista_competencias) {
				CompetenciaReq competencia =new CompetenciaReq();
				competencia.setId("COM"+row.getInteger("com_id").toString());
				competencia.setText(row.getString("nom"));
				//Busco si se esta usando la competencia
				List<Row> desmpeniosAula=desempenioAulaDAO.listarDesmepenioAulaxCompetencia(row.getInteger("com_id"), id_au, id_cpu,id_cua);
				
				int cant_notas=0;
				for (Row row2 : desmpeniosAula) {
					List<Row> notas=notaDesDAO.listarNotasCompetencias(row2.getInteger("id_com"), id_au, id_cpu, id_cua);
					if(notas.size()>0) {
						cant_notas = cant_notas+1;
					}
					
				}
				//Row competenciaExiste2 = competenciaDcDAO.existeCompetencia(id_au, id_cpu, id_dcare, row.getInteger("com_id"), id_cua);
				EstadoConfDesReq estado1 = new EstadoConfDesReq();
				if(desmpeniosAula.size()>0) {
					estado1.setChecked(true);
				} else {
					estado1.setChecked(false);
				}
				
				if(cant_notas>0) {
					//estado1.setDisabled(true);
					estado1.setExpanded(true);
				//	estado1.setDisabled(true);
				} else {
					//estado1.setDisabled(false);
					estado1.setExpanded(false);
					estado1.setDisabled(false);
				}
				
				/*if(competenciaExiste2.size()>0) {
					estado1.setChecked(true);
				} else {
					estado1.setChecked(false);
				}*/
				/*if(row.getInteger("id_desau")!=null)
					estado1.setChecked(true);
				else
					estado1.setChecked(false);	*/
				//estado1.setExpanded(false);
				competencia.setState(estado1);
				competencia.setColor("blue");
				competencia.setBackColor("#AED6F1");
				//Recorremos la lista de capacidades
				List<CapacidadReq> ListaCapacidades = new ArrayList<>();
				for (Row row2 : lista_capacidades) {
					CapacidadReq capacidad =new CapacidadReq();
					if(row.getInteger("com_id").equals(row2.getInteger("com_id"))) {
						capacidad.setId("CAP"+row2.getInteger("id").toString());
						//Busco si se esta usando la competencia
						List<Row> desmpeniosAula2=desempenioAulaDAO.listarDesmepenioAulaxCapacidad(row2.getInteger("id"), id_au, id_cpu,id_cua);
						
						int cant_notas2=0;
						for (Row row3 : desmpeniosAula2) {
							List<Row> notas=notaDesDAO.listarNotasCapacidades(row3.getInteger("id_cap"), id_au, id_cpu, id_cua);
							if(notas.size()>0) {
								cant_notas2 = cant_notas2+1;
							}
							
						}
						EstadoConfDesReq estado = new EstadoConfDesReq();
						if(desmpeniosAula2.size()>0) {
							estado.setChecked(true);
						} else {
							estado.setChecked(false);	
						}
						
						if(cant_notas2>0) {
							//
							estado.setExpanded(true);
							//estado.setDisabled(true);
						} else {
							estado.setDisabled(false);
							estado.setExpanded(false);
						}
						
						/*if(row2.getInteger("id_desau")!=null)
							estado.setChecked(true);
						else
							estado.setChecked(false);	*/
						//estado.setExpanded(false);
						capacidad.setText(row2.getString("nom"));
						capacidad.setState(estado);
						capacidad.setBackColor("#D5F5E3");
						List<DesemepenioReq> ListaDesemepenios = new ArrayList<>();
						for (Row row3 : lista_desempenios) {
							if(row2.getInteger("id").equals(row3.getInteger("id_cap"))) {
								DesemepenioReq desempenio = new DesemepenioReq();
								desempenio.setId("DES"+row3.getInteger("id_des").toString()+"-CAP"+row3.getInteger("id_cap").toString()+"-COM"+row3.getInteger("id_com").toString());
								desempenio.setText(row3.getString("des_nom"));
								//Busco si se esta usando la competencia
								List<Row> desmpeniosAulaexiste=desempenioAulaDAO.listarDesmepenioAula(row3.getInteger("id_des"), id_au, id_cpu,id_cua,row3.getInteger("id_cap"));
								int cant_notas3=0;
								for (Row row4 : desmpeniosAulaexiste) {
									List<Row> notas=notaDesDAO.listarNotasDesempenio(row4.getInteger("id"));
									if(notas.size()>0) {
										cant_notas3 = cant_notas3+1;
									}
									
								}
								EstadoConfDesReq estado4 = new EstadoConfDesReq();
								if(desmpeniosAulaexiste.size()>0) {
									estado4.setChecked(true);
								}else {
									estado4.setChecked(false);	
								}	
								
								if(cant_notas3>0) {
									estado4.setDisabled(true);
									estado4.setExpanded(true);
								} else {
									estado4.setDisabled(false);
									estado4.setExpanded(false);
								}
								//estado4.setExpanded(false);
								desempenio.setState(estado4);
								desempenio.setBackColor("#FCF3CF");
								ListaDesemepenios.add(desempenio);
							}
						}
						capacidad.setNodes(ListaDesemepenios);
						ListaCapacidades.add(capacidad);
					}
					
				}
				competencia.setNodes(ListaCapacidades);
				ListaCompetencias.add(competencia);
			}
			
			//Response -> generar el JSON
			/*List<UsuarioGrupoReq> ListaGruposClass = new ArrayList<>();
			
			for (GrupoAulaVirtual grupoAulaVirtual : lista_grupos) {
				UsuarioGrupoReq GruposClass = new UsuarioGrupoReq();
				//if(grupoAulaVirtual.getId()!=78){
					GruposClass.setIdClassroom(grupoAulaVirtual.getId_grupoclass());
					
					List<Row> usuarios=grupoAulaVirtualDAO.listarUsuariosxGrupo(grupoAulaVirtual.getId());
					if(usuarios!=null){
						List<UsuarioClassroomEnrollReq> listaUsuarios = new ArrayList<>(); 
						UsuarioClassroomEnrollReq usuarioClassroomEnrollReq;
						for (Row row : usuarios) {
							usuarioClassroomEnrollReq = new UsuarioClassroomEnrollReq();
							usuarioClassroomEnrollReq.setIdUsuario(row.getString("idUsuario"));
							listaUsuarios.add(usuarioClassroomEnrollReq);
						}
						GruposClass.setListUsuarios(listaUsuarios);
					}
				//}
				ListaGruposClass.add(GruposClass);
			}*/
			
			
			RestUtil restUtil = new RestUtil();
			
			
			//Para recuperar listas de Servicio
			Gson gson = new Gson();
			String usuarioGrupoReqs = gson.toJson(ListaCompetencias);
			System.out.println(usuarioGrupoReqs);
			result.setResult(usuarioGrupoReqs);
			//return usuarioGrupoReqs;
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			
		}
		return result;
		
	}
	
	@RequestMapping(value = "/obtenerConfiguracionArea")
	public AjaxResponseBody obtenerConfiguracionArea(Integer id_anio,Integer id_gra, Integer id_adc) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(areaAnioDAO.obtenerConfiguracionxArea(id_adc, id_anio, id_gra));
		
		return result;
	}
	
	@Transactional
	@RequestMapping( value="/grabarConfiguracion",method = RequestMethod.POST)
	public AjaxResponseBody grabarConfiguracion(@RequestBody ConfDesempeniosReq confDesempeniosReq) throws Exception{

		AjaxResponseBody result = new AjaxResponseBody(); 
		
		Integer id_au=confDesempeniosReq.getId_aula();
		Integer id_areadc=confDesempeniosReq.getId_areadc();
		Integer id_curdc=confDesempeniosReq.getId_curdc();
		Integer id_cpu=confDesempeniosReq.getId_periodo();
		//Modificar a todos los periodos
		PerUni perUni = perUniDAO.getByParams(new Param("id",id_cpu));
		Integer id_cpa=perUni.getId_cpa();
		//Busco si existe la regla de replicar la configuracion de desempeños a todos los periodos del aula
		Param param = new Param();
		param.put("cod", "REP_CONF_DES_TOD_PERI");
		ReglasNegocio rep_conf=reglasNegocioDAO.getByParams(param);
		/*List<PerUni> periodos = new ArrayList<>();
		if(rep_conf.getVal().equals("1")) {
			Param param2 = new Param();
			param2.put("id_cpa", id_cpa);
			periodos=perUniDAO.listByParams(param2, null);
		}*/
		//if(periodos)
		//Lista de desempenio para el curso
		List<Row> desempenios = new ArrayList<>();
		//Lista de competencias para el curso
		//List<Row> competencias = new ArrayList<>();
		if(id_curdc!=null) {
			desempenios = desempenioAulaDAO.listarDesmepenioAulaCurso(id_au, id_cpu, id_curdc);
			//competencias = desempenioAulaDAO.listarCompetenciasAulaCurso(id_au, id_cpu, id_curdc);
		} else {
			desempenios = desempenioAulaDAO.listarDesmepenioAulaArea(id_au, id_cpu, id_areadc);
			//competencias = desempenioAulaDAO.listarCompetenciasAulaArea(id_au, id_cpu, id_areadc);
		}
		
		final List<String> desempeniosForm = Arrays.asList(confDesempeniosReq.getId_des());	
		if (desempenios != null){//si hay desempenios
			String listaIds[] = new String[ desempenios.size() ];
			String listaCapacidad[] = new String[ desempenios.size() ];
		int i=0;	
		for (Row row : desempenios) {
			listaIds[i]=row.getInteger("id").toString();
			listaCapacidad[i]=row.getInteger("id_cap").toString();
			i++;
		}
		
		//if (competencias != null){//si hay desempenios
		/*String listaIdsCom[] = new String[competencias.size() ];
		int j=0;	
		for (Row row : competencias) {
			listaIdsCom[j]=row.getInteger("id_com").toString();
			j++;
		}*/
		final List<String> desempeniosExiste = Arrays.asList(listaIds);	
		final List<String> capacidadesExiste = Arrays.asList(listaCapacidad);	
	//	final List<String> competenciasExiste = Arrays.asList(listaIdsCom);	
		if(desempeniosForm.size()>0) {
			for (String desempenioForm : desempeniosForm) {
				//String competencia=desempenioForm.split("-")[0].substring(0,3);
				if(desempenioForm.contains("DES")) {
					String id_des=desempenioForm.split("-")[0].replace("DES", "");
					String id_cap=desempenioForm.split("-")[1].replace("CAP", "");
					String id_com=desempenioForm.split("-")[1].replace("COM", "");
					int y=0;
					for (String string : desempeniosExiste) {
						if(!id_des.contains(string)) {
							if(id_curdc!=null) {
								desempenioAulaDAO.desactivarDesemepnioAulaxCurso(id_au, Integer.parseInt(string), Integer.parseInt(capacidadesExiste.get(y)), id_curdc, id_cpu);
							} else {
								desempenioAulaDAO.desactivarDesemepnioAulaxArea(id_au, Integer.parseInt(string), Integer.parseInt(capacidadesExiste.get(y)), id_cpu);
							}
						} else if(id_des.contains(string) && id_cap.equals(capacidadesExiste.get(y))) {
							if(id_curdc!=null) {
								desempenioAulaDAO.activarDesemepnioAulaxCurso(id_au, Integer.parseInt(string), Integer.parseInt(capacidadesExiste.get(y)), id_curdc, id_cpu);
							} else {
								desempenioAulaDAO.activarDesemepnioAulaxArea(id_au, Integer.parseInt(string), Integer.parseInt(capacidadesExiste.get(y)), id_cpu);
							}
						} else if(!id_cap.equals(capacidadesExiste.get(y))  && id_des.contains(string)) {
							if(id_curdc!=null) {
								desempenioAulaDAO.desactivarDesemepnioAulaxCurso(id_au, Integer.parseInt(string), Integer.parseInt(capacidadesExiste.get(y)), id_curdc, id_cpu);
							} else {
								desempenioAulaDAO.desactivarDesemepnioAulaxArea(id_au, Integer.parseInt(string), Integer.parseInt(capacidadesExiste.get(y)), id_cpu);
							}
						}
						y++;
					}	
					/*for (String string : competenciasExiste) {
						if(!id_com.contains(string)) {
							if(id_curdc!=null) {
								competenciaDcDAO.desactivarCompetenciaAulaxCurso(id_au, Integer.parseInt(string), id_curdc, id_cpu);
							} else {
								competenciaDcDAO.desactivarCompetenciaAulaxArea(id_au, Integer.parseInt(string), id_cpu);
							}
						} else {
							if(id_curdc!=null) {
								competenciaDcDAO.activarCompetenciaAulaxCurso(id_au, Integer.parseInt(string), id_curdc, id_cpu);
							} else {
								competenciaDcDAO.activarCompetenciaAulaxArea(id_au, Integer.parseInt(string), id_cpu);
							}
						}
					}	*/
					
				} /*else if(competencia.equals("COM")){
					String id_com=desempenioForm.split("-")[0].replace("COM", "");
					for (String string : competenciasExiste) {
						if(!id_com.contains(string)) {
							if(id_curdc!=null) {
								competenciaDcDAO.desactivarCompetenciaAulaxCurso(id_au, Integer.parseInt(string), id_curdc, id_cpu);
							} else {
								competenciaDcDAO.desactivarCompetenciaAulaxArea(id_au, Integer.parseInt(string), id_cpu);
							}
						} else {
							if(id_curdc!=null) {
								competenciaDcDAO.activarCompetenciaAulaxCurso(id_au, Integer.parseInt(string), id_curdc, id_cpu);
							} else {
								competenciaDcDAO.activarCompetenciaAulaxArea(id_au, Integer.parseInt(string), id_cpu);
							}
						}
					}	
				}*/
			}
			} else {
				//Elimino todos los q existen
				for (Row row : desempenios) {
					desempenioAulaDAO.delete(row.getInteger("id_desau"));
				}
			}
		} 		
		
		/*if (desempenios == null && competencias!=null){//si hay competencias solo eso
			String listaIds[] = new String[ desempenios.size() ];
		
		//if (competencias != null){//si hay desempenios
		String listaIdsCom[] = new String[competencias.size() ];
		int j=0;	
		for (Row row : competencias) {
			listaIdsCom[j]=row.getInteger("id_com").toString();
			j++;
		}
		final List<String> competenciasExiste = Arrays.asList(listaIdsCom);	
		for (String desempenioForm : desempeniosForm) {
			String competencia=desempenioForm.split("-")[0].substring(0,3);
			if(competencia.equals("COM")){
				String id_com=desempenioForm.split("-")[0].replace("COM", "");
				for (String string : competenciasExiste) {
					if(!id_com.contains(string)) {
						if(id_curdc!=null) {
							competenciaDcDAO.desactivarCompetenciaAulaxCurso(id_au, Integer.parseInt(string), id_curdc, id_cpu);
						} else {
							competenciaDcDAO.desactivarCompetenciaAulaxArea(id_au, Integer.parseInt(string), id_cpu);
						}
					} else {
						if(id_curdc!=null) {
							competenciaDcDAO.activarCompetenciaAulaxCurso(id_au, Integer.parseInt(string), id_curdc, id_cpu);
						} else {
							competenciaDcDAO.activarCompetenciaAulaxArea(id_au, Integer.parseInt(string), id_cpu);
						}
					}
				}	
			}
		}
		}*/
		
		for (int y = 0; y < confDesempeniosReq.getId_des().length; y++) {
			//String competencia=confDesempeniosReq.getId_des()[y].split("-")[0].substring(0,3);
			if(confDesempeniosReq.getId_des()[y].contains("DES")) {
				String id_des=confDesempeniosReq.getId_des()[y].split("-")[0].replace("DES", "");
				String id_cap=confDesempeniosReq.getId_des()[y].split("-")[1].replace("CAP", "");
				String id_com=confDesempeniosReq.getId_des()[y].split("-")[2].replace("COM", "");
				System.out.println(id_des);
				System.out.println(id_cap);
				System.out.println(id_com);
				//Busco la competencia
			//	Row competenciaExiste = competenciaDcDAO.existeCompetencia(id_au, id_cpu, id_areadc, Integer.parseInt(id_com), id_curdc);
				//Busco si ya existe
				Row desempenioExiste = desempenioAulaDAO.existeDesempenio(id_au, id_cpu, id_areadc, Integer.parseInt(id_cap), Integer.parseInt(id_des), id_curdc);
				if(desempenioExiste!=null) {
					if(desempenioExiste.getString("est").equals("I")) {
						DesempenioAula desempenioAula = new DesempenioAula();
						desempenioAula.setId(desempenioExiste.getInteger("id"));
						desempenioAula.setId_desdc(Integer.parseInt(id_des));
						desempenioAula.setId_cap(Integer.parseInt(id_cap));
						desempenioAula.setId_cpu(id_cpu);
						desempenioAula.setId_au(id_au);
						desempenioAula.setId_cua(id_curdc);
						if(id_curdc!=null) {
							desempenioAula.setConf_curso("1");
						} else {
							desempenioAula.setConf_curso("0");
						}
						desempenioAula.setEst("A");
						desempenioAulaDAO.saveOrUpdate(desempenioAula);
					}
					
					/*if(competenciaExiste!=null) {
						if(competenciaExiste.getString("est").equals("I")) {
							CompetenciaAula competenciaAula = new CompetenciaAula();
							competenciaAula.setId(competenciaExiste.getInteger("id"));
							competenciaAula.setId_com(Integer.parseInt(id_com));
							competenciaAula.setId_cpu(id_cpu);
							competenciaAula.setId_au(id_au);
							competenciaAula.setId_cua(id_curdc);
							if(id_curdc!=null) {
								competenciaAula.setConf_curso("1");
							} else {
								competenciaAula.setConf_curso("0");
							}
							competenciaAula.setEst("A");
							competenciaAulaDAO.saveOrUpdate(competenciaAula);
						}
					} else {
						CompetenciaAula competenciaAula = new CompetenciaAula();
						competenciaAula.setId_com(Integer.parseInt(id_com));
						competenciaAula.setId_cpu(id_cpu);
						competenciaAula.setId_au(id_au);
						competenciaAula.setId_cua(id_curdc);
						if(id_curdc!=null) {
							competenciaAula.setConf_curso("1");
						} else {
							competenciaAula.setConf_curso("0");
						}
						competenciaAula.setEst("A");
						competenciaAulaDAO.saveOrUpdate(competenciaAula);
					}*/
				} else {
					DesempenioAula desempenioAula = new DesempenioAula();
					desempenioAula.setId_desdc(Integer.parseInt(id_des));
					desempenioAula.setId_cap(Integer.parseInt(id_cap));
					desempenioAula.setId_cpu(id_cpu);
					desempenioAula.setId_au(id_au);
					desempenioAula.setId_cua(id_curdc);
					if(id_curdc!=null) {
						desempenioAula.setConf_curso("1");
					} else {
						desempenioAula.setConf_curso("0");
					}
					desempenioAula.setEst("A");
					desempenioAulaDAO.saveOrUpdate(desempenioAula);
					
					//Busco si ya existe
				/*	Row competenciaExiste2 = competenciaDcDAO.existeCompetencia(id_au, id_cpu, id_areadc, Integer.parseInt(id_com), id_curdc);
					
					CompetenciaAula competenciaAula = new CompetenciaAula();
					if(competenciaExiste2!=null) {
						competenciaAula.setId(competenciaExiste2.getInteger("id"));
					}
					competenciaAula.setId_cpu(id_cpu);
					competenciaAula.setId_au(id_au);
					competenciaAula.setId_com(Integer.parseInt(id_com));
					competenciaAula.setId_cua(id_curdc);
					if(id_curdc!=null) {
						competenciaAula.setConf_curso("1");
					} else {
						competenciaAula.setConf_curso("0");
					}
					competenciaAula.setEst("A");
					competenciaAulaDAO.saveOrUpdate(competenciaAula);*/
				}
				
			} /*else if(competencia.equals("COM")) {
				String id_com=confDesempeniosReq.getId_des()[y].split("-")[0].replace("COM", "");
				//Busco si ya existe
				Row competenciaExiste2 = competenciaDcDAO.existeCompetencia(id_au, id_cpu, id_areadc, Integer.parseInt(id_com), id_curdc);
				if(competenciaExiste2!=null) {
					if(competenciaExiste2.getString("est").equals("I")) {
						CompetenciaAula competenciaAula = new CompetenciaAula();
						competenciaAula.setId(competenciaExiste2.getInteger("id"));
						competenciaAula.setId_com(Integer.parseInt(id_com));
						competenciaAula.setId_cpu(id_cpu);
						competenciaAula.setId_au(id_au);
						competenciaAula.setId_cua(id_curdc);
						if(id_curdc!=null) {
							competenciaAula.setConf_curso("1");
						} else {
							competenciaAula.setConf_curso("0");
						}
						competenciaAula.setEst("A");
						competenciaAulaDAO.saveOrUpdate(competenciaAula);
					}
				} else {
					CompetenciaAula competenciaAula = new CompetenciaAula();
					competenciaAula.setId_com(Integer.parseInt(id_com));
					competenciaAula.setId_cpu(id_cpu);
					competenciaAula.setId_au(id_au);
					competenciaAula.setId_cua(id_curdc);
					if(id_curdc!=null) {
						competenciaAula.setConf_curso("1");
					} else {
						competenciaAula.setConf_curso("0");
					}
					competenciaAula.setEst("A");
					competenciaAulaDAO.saveOrUpdate(competenciaAula);
				}
			}*/
			
		}
		
		result.setResult("1");
		
		return result;

	}	
	
	

	@RequestMapping(value = "/listarNotasCurso")
	public AjaxResponseBody listarNotasCurso(Integer id_anio,Integer id_gra,Integer id_niv, Integer id_au, Integer nump) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(notaDAO.listarNotasCurso(id_anio, id_gra,id_niv, id_au, nump) );
		
		return result;
	}
	

	@RequestMapping(value = "/listarNotasArea")
	public AjaxResponseBody listarNotasArea(Integer id_anio,Integer id_gra,Integer id_niv, Integer id_au, Integer nump) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(notaDAO.listarNotasArea(id_anio, id_gra,id_niv, id_au, nump) );
		
		return result;
	}
	
	@RequestMapping(value = "/listarNotasAreaCompetencias")
	public AjaxResponseBody listarNotasAreaCompetencia(Integer id_anio,Integer id_gra,Integer id_niv, Integer id_au, Integer nump, Integer id_gir, Integer rep_com) throws Exception{

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(notaDAO.listarNotasAreaCompetencias(id_anio, id_gra,id_niv, id_au, nump, id_gir, rep_com) );
		
		return result;
	}

	

	@RequestMapping(value = "/notas_areas_finales")
	@ResponseBody
	public void descargarExcel(HttpServletRequest request,HttpServletResponse response)  throws Exception {

		Integer id_anio = Integer.parseInt(request.getParameter("id_anio"));
		Integer id_gra= Integer.parseInt(request.getParameter("id_gra"));
		Integer id_niv= Integer.parseInt(request.getParameter("id_niv"));
		Integer id_au= Integer.parseInt(request.getParameter("id_au"));
		Integer nump= Integer.parseInt(request.getParameter("nump"));
		
		Map<String, Object> notas = notaDAO.listarNotasArea(id_anio, id_gra,id_niv, id_au, nump);

		Anio anio = anioDAO.get( id_anio );

	
		response.setContentType("application/vnd.ms-excel");
		
		//Inicio datos del archivo nuevo
		String  carpeta =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();
		////String carpeta = "D:/plantillas/";
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd_hhmmss");

		String archivo = "Areas_Notas_Finales.xls";
		String nuevoArchivo = null;
		nuevoArchivo = dt1.format(new Date());
		
		nuevoArchivo = carpeta + "tmp/" + archivo.replace("Areas_Notas_Finales", "Areas_Notas_Finales_" + anio.getNom() + "_" + nuevoArchivo);

		ArchivoUtil.copyFileUsingStream(new File(carpeta + archivo), new File(nuevoArchivo));
		

		FileInputStream inputStream = new FileInputStream(new File(nuevoArchivo));
		Workbook workbook = WorkbookFactory.create(inputStream);
		   Sheet sheet = workbook.getSheetAt(0);

		//inicio - Obtener descripciones periodo, nivel, grado, seccion
		String strDescripcion ="";
		String strPeriodo ="";
		String strNivel = EnumNivel.nivel(id_niv).getDescripcion();
		String strGrado  = "";
		String strSeccion = "";
		
		if (nump!=0){
			PerUni perUni = perUniDAO.get(nump);
			if (id_niv == EnumNivel.SECUNDARIA.getValue()){
				strPeriodo = "BIMESTRE " + perUni.getNump();
			}else{
				strPeriodo = "TRIMESTRE " + perUni.getNump();
			}
		}else
			strPeriodo = "PROMEDIO FINAL";
		
		Grad grado = gradDAO.get(id_gra);
		strGrado =  grado.getNom();
		
		if (id_au!=-1){
			Aula aula = aulaDAO.get(id_au);
			strSeccion  = aula.getSecc();
		}
		strDescripcion =  strPeriodo + ": " + strNivel + " " + strGrado + " " + strSeccion; 
		Cell cell = sheet.getRow(1).createCell(2);
		cell.setCellValue(strDescripcion);
     
		//fin - Obtener descripciones periodo, nivel, grado, seccion
		
		@SuppressWarnings("unchecked")
		List<Row> alumnos = (ArrayList<Row>)notas.get("alumnos");
		AreaAnio areaAnionParam = new AreaAnio();
		areaAnionParam.setId_anio(id_anio);
		areaAnionParam.setId_niv(id_niv);
		List<AreaAnio> areas = areaAnioDAO.listFullByParams(areaAnionParam, new String[]{"ORD"});
        int filaInicio=3;
        
        int columna=3;
        
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GOLD.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        
        CellStyle styleCell = workbook.createCellStyle();
        styleCell.setBorderBottom(BorderStyle.THIN);
        styleCell.setBorderTop(BorderStyle.THIN);
        styleCell.setBorderRight(BorderStyle.THIN);
        
        
        if (id_au==-1){
        	Cell cellAula = sheet.getRow(filaInicio-1).createCell(columna++);
        	cellAula.setCellValue("Grado");
        	cellAula.setCellStyle(style);
        }
        
        if(nump.intValue()==0){//EN PROMEDIO FINAL.. HAY COMPORTAMIENTO
			Cell cell4 = sheet.getRow(filaInicio-1).createCell(columna++);
			cell4.setCellValue("Comportamiento");
			cell4.setCellStyle(styleCell);
		}
        
		for (AreaAnio areaAnio : areas) {
			cell = sheet.getRow(filaInicio-1).createCell(columna++);
			cell.setCellValue(areaAnio.getArea().getNom());
			cell.setCellStyle(style);
        	
		}

        
        for (int i = 0; i < alumnos.size(); i++) {

        	Cell cell1 = sheet.getRow(filaInicio+i).createCell(0);
        	cell1.setCellValue(i+1);
			cell1.setCellStyle(styleCell);

			Cell cell2 = sheet.getRow(filaInicio+i).createCell(1);
        	cell2.setCellValue(alumnos.get(i).getString("nro_doc"));
			cell2.setCellStyle(styleCell);

			Cell cell3 = sheet.getRow(filaInicio+i).createCell(2);
        	cell3.setCellValue(alumnos.get(i).getString("alumno"));
			cell3.setCellStyle(styleCell);

			int j=2;
			
			

			if (id_au==-1){
				j++;
	        	Cell cellAula = sheet.getRow(filaInicio+i).createCell(j);
	        	cellAula.setCellValue(alumnos.get(i).getString("grado") + alumnos.get(i).getString("secc"));
	        	cellAula.setCellStyle(style);
	        }
			
			if(nump.intValue()==0){//EN PROMEDIO FINAL.. HAY COMPORTAMIENTO
				j++;
				Cell cell4 = sheet.getRow(filaInicio+i).createCell(j);
				cell4.setCellValue(alumnos.get(i).getString("comportamiento"));
				cell4.setCellStyle(styleCell);
			}
			
			Map<Integer,Object> areasMap = (Map<Integer,Object>)alumnos.get(i).get("areas");
			
		
			

			for (AreaAnio areaAnio : areas) {
				j++;
				Object valor = areasMap.get(areaAnio.getArea().getId());
				Cell cellArea = sheet.getRow(filaInicio+i).createCell(j);
				if (valor!=null){
					if (id_niv==EnumNivel.SECUNDARIA.getValue())
						cellArea.setCellValue(convertSIAGE_Secundaria(valor));
					else
						cellArea.setCellValue(convertNotaPrimaria(Integer.parseInt(valor.toString()),id_niv));
				}else
					cellArea.setCellValue("");
				cellArea.setCellStyle(styleCell);
	        	
			}
        }
         
	
		inputStream.close();

		FileOutputStream outputStream = new FileOutputStream(nuevoArchivo);
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
		
		response.setHeader("Content-Disposition","attachment;filename=Areas_Notas_Finales_" + anio.getNom() + ".xls");

		File initialFile = new File(nuevoArchivo);
	    InputStream is = FileUtils.openInputStream(initialFile);
	    	
		IOUtils.copy(is, response.getOutputStream());
		
	}	
	
  
	private Integer convertSIAGE_Secundaria(Object nota){

		if (nota==null)
			return 0;
		else
			//comportamiento =SI(D6="","",SI(D6>16,"AD",SI(D6>12,"A",SI(D6>10,"B","C"))))
			return Integer.parseInt(nota.toString());
			
	}

	@RequestMapping( value="/grabarNotaAreaSiage",method = RequestMethod.POST)
	public AjaxResponseBody grabarAreaSiage(Integer id_anio,Integer id_gra,Integer id_niv, Integer id_au, Integer nump)  {
		
		AjaxResponseBody result = new AjaxResponseBody();
		int puntosAdicionales=0;
		
		if (id_au.intValue()!=132 && id_au.intValue()!=136)
			return result;
		
		if (id_au.intValue()==132 )
			puntosAdicionales =2;
		if (id_au.intValue()==136 )
			puntosAdicionales =1;
		
		if (nump.intValue()==0){
			
			 
			
			Map<String, Object> mapResult = notaDAO.listarNotasArea(id_anio, id_gra,id_niv, id_au, nump);
			
			   List<Row> alumnos = (List<Row>)mapResult.get("alumnos");
			   for (Row row : alumnos) {
				   Integer id_mat = row.getInteger("id_mat");
				   Map<Integer, Object> areaAlumno = (Map<Integer, Object>)row.get("areas");  
				   
				   java.util.Iterator<Map.Entry<Integer, Object>> it = areaAlumno.entrySet().iterator();
				   while (it.hasNext()) {
				       Map.Entry<Integer, Object> pair = it.next();
				       Integer id_area =pair.getKey();
				       
				       Integer promedio = (Integer)pair.getValue();
 
				       promedio = promedio + puntosAdicionales;
				       if (promedio.intValue()>20)
				    	   promedio=20;
				       
				       NotaArea notaArea = new NotaArea();
				       notaArea.setId_mat(id_mat);
				       notaArea.setId_area(id_area);
				       notaArea.setId_au(id_au);
				       notaArea.setProm(promedio);
				       notaArea.setUsr_ins(1);
				       notaArea.setEst("A");
				       notaAreaDAO.saveOrUpdate(notaArea);
				       
				   }
				    
			}
		}
		
		return result;
	}

		
}

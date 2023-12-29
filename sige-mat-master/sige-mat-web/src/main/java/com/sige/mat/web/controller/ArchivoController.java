package com.sige.mat.web.controller;
 
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JFileChooser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.ArchivoUtil;
import com.tesla.frmk.util.CodigoBarrasUtil;
import com.tesla.frmk.util.CorreoUtil;
import com.tesla.frmk.util.DocxUtil;
import com.tesla.frmk.util.ExcelXlsUtil;
import com.tesla.frmk.util.FechaUtil;
import com.tesla.frmk.util.NumberUtil;
import com.fasterxml.jackson.annotation.JsonView;
import com.ibm.icu.text.SimpleDateFormat;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode39;
import com.itextpdf.text.pdf.BarcodeEAN;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.sige.common.enums.EnumBanco;
import com.sige.core.dao.cache.CacheManager;
import com.sige.core.dao.cache.CacheManagerUtil;
import com.sige.invoice.bean.Impresion;
import com.sige.invoice.bean.ImpresionDcto;
import com.sige.invoice.bean.ImpresionItem;
import com.sige.mat.dao.AcademicoPagoDAO;
import com.sige.mat.dao.AlumnoDAO;
import com.sige.mat.dao.AlumnoDescuentoDAO;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.ArchivoDAO;
import com.sige.mat.dao.AreaAnioDAO;
import com.sige.mat.dao.AulaDAO;
import com.sige.mat.dao.AulaDetalleDAO;
import com.sige.mat.dao.AulaEspecialDAO;
import com.sige.mat.dao.AulaModalidadDetDAO;
import com.sige.mat.dao.BancoDAO;
import com.sige.mat.dao.BecaDAO;
import com.sige.mat.dao.CapacidadDcDAO;
import com.sige.mat.dao.CicloDAO;
import com.sige.mat.dao.CicloTurnoDAO;
import com.sige.mat.dao.CompetenciaAulaDAO;
import com.sige.mat.dao.CompetenciaDcDAO;
import com.sige.mat.dao.ConfAnioAcadDcnDAO;
import com.sige.mat.dao.ConfMensualidadDAO;
import com.sige.mat.dao.ContadorDAO;
import com.sige.mat.dao.CronogramaLibretaDAO;
import com.sige.mat.dao.DcnAreaDAO;
import com.sige.mat.dao.DcnNivelDAO;
import com.sige.mat.dao.DesempenioAulaDAO;
import com.sige.mat.dao.DesempenioDcDAO;
import com.sige.mat.dao.FamiliarDAO;
import com.sige.mat.dao.GiroNegocioDAO;
import com.sige.mat.dao.GradDAO;
import com.sige.mat.dao.GruFamDAO;
import com.sige.mat.dao.LecturaBarrasDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.MovimientoDAO;
import com.sige.mat.dao.NivelDAO;
import com.sige.mat.dao.NotaAreaDAO;
import com.sige.mat.dao.NotaCreditoDAO;
import com.sige.mat.dao.NotaDAO;
import com.sige.mat.dao.NotaDesDAO;
import com.sige.mat.dao.NotaIndicadorDAO;
import com.sige.mat.dao.PagoRealizadoDAO;
import com.sige.mat.dao.ParametroDAO;
import com.sige.mat.dao.PerAcaNivelDAO;
import com.sige.mat.dao.PerUniDAO;
import com.sige.mat.dao.PeriodoAcaDAO;
import com.sige.mat.dao.PeriodoDAO;
import com.sige.mat.dao.PersonaDAO;
import com.sige.mat.dao.PromedioComDAO;
import com.sige.mat.dao.ReglasNegocioDAO;
import com.sige.mat.dao.ReservaDAO;
import com.sige.mat.dao.SeguimientoDocDAO;
import com.sige.mat.dao.SemGrupoDAO;
import com.sige.mat.dao.SemInscripcionDAO;
import com.sige.mat.dao.SeminarioDAO;
import com.sige.mat.dao.SucursalDAO;
import com.sige.mat.dao.TarifasEmergenciaDAO;
import com.sige.mat.dao.TrabajadorDAO;
import com.sige.mat.dao.TurnoAulaDAO;
import com.sige.rest.request.AreaCompetenciaReq;
import com.sige.rest.request.AreaNotaReq;
import com.sige.rest.request.CabeceraNotaReq;
import com.sige.rest.request.CompetenciaDCReq;
import com.sige.rest.request.LibretaNotaReq;
import com.sige.rest.request.NotaConductalPeriodoReq;
import com.sige.rest.request.NotaConductalReq;
import com.sige.rest.request.PeriodosReq;
import com.sige.rest.request.TardanzaPeriodoReq;
import com.sige.rest.request.TardanzaReq;
import com.sige.spring.service.FacturacionService;
import com.sige.spring.service.NotaService;
import com.sige.spring.service.PagosService;
import com.sige.spring.service.VacanteService;
import com.tesla.colegio.model.AcademicoPago;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Archivo;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.AulaDetalle;
import com.tesla.colegio.model.AulaModalidadDet;
import com.tesla.colegio.model.Ciclo;
import com.tesla.colegio.model.CicloTurno;
import com.tesla.colegio.model.Cliente;
import com.tesla.colegio.model.CompetenciaDc;
import com.tesla.colegio.model.CondMatricula;
import com.tesla.colegio.model.ConfAnioAcadDcn;
import com.tesla.colegio.model.ConfMensualidad;
import com.tesla.colegio.model.Contador;
import com.tesla.colegio.model.DcnNivel;
import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.GiroNegocio;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.GruFam;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Movimiento;
import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.NotaCredito;
import com.tesla.colegio.model.Parametro;
import com.tesla.colegio.model.PerAcaNivel;
import com.tesla.colegio.model.PerUni;
import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.PeriodoAca;
import com.tesla.colegio.model.Persona;
import com.tesla.colegio.model.Reserva;
import com.tesla.colegio.model.SemGrupo;
import com.tesla.colegio.model.SemInscripcion;
import com.tesla.colegio.model.Seminario;
import com.tesla.colegio.model.Sucursal;
import com.tesla.colegio.model.Trabajador;
import com.tesla.colegio.model.TurnoAula;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.util.StringUtil;
import com.tesla.frmk.util.WordToPDFUtil;

import net.sf.jasperreports.engine.JRException;
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
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;



@RestController
@RequestMapping(value = "/api/archivo")
public class ArchivoController {

	@Autowired
	private ArchivoDAO archivoDAO;
	
	@Autowired
	private MatriculaDAO matriculaDAO;
	
	@Autowired
	private MovimientoDAO movimientoDAO;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private CacheManagerUtil cacheManager2;
	
	@Autowired
	private AcademicoPagoDAO academicoPagoDAO;

	@Autowired
	private FacturacionService facturacionService;
	
	@Autowired
	private AlumnoDescuentoDAO alumnoDescuentoDAO;
	
	@Autowired
	private PagoRealizadoDAO pagorealizadoDAO;
	
	@Autowired
	private BancoDAO bancoDAO;
	
	@Autowired
	private AnioDAO anioDAO;
	
	@Autowired
	private TarifasEmergenciaDAO tarifasEmergenciaDAO;
	
	@Autowired
	private ConfMensualidadDAO confMensualidadDAO;
	
	@Autowired
	private PagosService pagosService;
	
	@Autowired
	private FamiliarDAO familiarDAO;
	
	@Autowired
	private ParametroDAO parametroDAO;
	
	@Autowired
	private VacanteService vacanteService;
	
	@Autowired
	private ReservaDAO reservaDAO;
	
	@Autowired
	private GradDAO gradDAO;
	
	@Autowired
	private AulaDAO aulaDAO;
	
	@Autowired
	private NotaDAO notaDAO; 
	
	@Autowired
	private NotaIndicadorDAO notaIndicadorDAO;

	
	@Autowired
	private AreaAnioDAO areaAnioDAO;

	@Autowired
	private PerUniDAO perUniDAO;


	@Autowired
	private AulaEspecialDAO aulaEspecialDAO;
	
	@Autowired
	private  SeguimientoDocDAO seguimientoDocDAO;
	
	@Autowired
	private  CronogramaLibretaDAO cronogramaDAO;

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
	
	@Autowired
	private GruFamDAO gruFamDAO;
	
	@Autowired
	private AulaModalidadDetDAO aulaModalidadDetDAO;
	
	@Autowired
	private SucursalDAO sucursalDAO;
	
	@Autowired
	private TurnoAulaDAO turnoAulaDAO;
	
	@Autowired
	private PeriodoDAO periodoDAO;
	
	@Autowired
	private NotaCreditoDAO notaCreditoDAO;
	
	@Autowired
	private BecaDAO becaDAO;
	
	@Autowired
	private NivelDAO nivelDAO;
	
	@Autowired
	private CicloDAO cicloDAO;
	
	@Autowired
	private CicloTurnoDAO cicloTurnoDAO;
	
	@Autowired
	private GiroNegocioDAO giroNegocioDAO;
	
	@Autowired
	private SeminarioDAO seminarioDAO;
	
	@Autowired
	private SemGrupoDAO semGrupoDAO;
	
	@Autowired
	private SemInscripcionDAO semInscripcionDAO;	
	
	@Autowired
	private ContadorDAO contadorDAO;
	
	@Autowired
	private LecturaBarrasDAO lecturaBarrasDAO;
	
	@Autowired
	private CacheManagerUtil cacheManagerUtil;
	
	private static String destFileName = "C:\\plantillas\\final_ireport_java\\report1.pdf";
    private static String JSON_DATA_FILE="C:\\plantillas\\tmp\\json1.json";
    private static String JASPER_FILE_LOCATION="C:\\plantillas\\final_ireport_java\\";
    private static String IMAGE_LOCATION = "C:\\plantillas\\final_ireport_java\\";
	
	@RequestMapping(method = RequestMethod.POST, value = "/codigo/{codigo}")
	public AjaxResponseBody uploadFile(@RequestParam("file") MultipartFile uploadfile,@PathVariable String codigo) {

		AjaxResponseBody result = new AjaxResponseBody();

		if (uploadfile.isEmpty()) {
			result.setCode("ARCHIVO");
			result.setMsg("archivo es vacio");
			return result;
		}

		try {
			String nombreArchivo = uploadfile.getOriginalFilename();

			InputStream inputStream = uploadfile.getInputStream();

			Archivo archivo = new Archivo();
			archivo.setCodigo(codigo);

			archivoDAO.saveOrUpadte(archivo, inputStream);
			
			result.setResult("1");
			return result;
			
		} catch (Exception e) {
			result.setCode("ARCHIVO");
			result.setMsg("archivo con errores:" + e.getMessage());
			e.printStackTrace();
			return result;
		}

	}
	

	/**
	 * Obtiene las provincias de un departamento desde la cach�
	 * @param id_dep
	 * @return
	 */
	@RequestMapping(value = "/departamentos/{id_pais}")
	public AjaxResponseBody getDepartementos(@PathVariable Integer id_pais) {

		AjaxResponseBody result = new AjaxResponseBody();
		result.setResult( cacheManager2.getListDepartamentos(id_pais));
		
		return result;

	}
	
	@RequestMapping(value = "/listarSeminarios")
	public AjaxResponseBody getLista(Seminario seminario) {

		AjaxResponseBody result = new AjaxResponseBody();
		  Calendar cal = Calendar.getInstance();
		  Anio anio=anioDAO.getByParams(new Param("nom",cal.get(Calendar.YEAR)));
		Integer id_anio=anio.getId();
		//seminario.setId_anio(id_anio);
		result.setResult(seminarioDAO.listarSeminarios(id_anio));
		
		return result;
	}
	
	@RequestMapping( value="/obtenerDatosPersonaxNroDoc/{nro_doc}", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosPersonaxNroDoc(@PathVariable String nro_doc) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
				Object respuesta = personaDAO.datosPersonaxNro_doc(nro_doc);

				result.setResult(respuesta );
				
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/existeAlumnoSeminario", method = RequestMethod.GET)
	public AjaxResponseBody existeAlumnoSeminario(String dni,Integer id_sem) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
				Boolean postulante= seminarioDAO.existsInscripcion(dni, id_sem);
 			
				result.setResult(postulante);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@Transactional
	@RequestMapping( value="/grabarInscripcionSeminario", method = RequestMethod.POST)
	public AjaxResponseBody grabar(SemInscripcion semInscripcion) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			semInscripcion.setApe_mat(semInscripcion.getApe_mat().toUpperCase());
			semInscripcion.setApe_pat(semInscripcion.getApe_pat().toUpperCase());
			semInscripcion.setNom(semInscripcion.getNom().toUpperCase());
			semInscripcion.setCorr(semInscripcion.getCorr().toUpperCase());
			//Busco Grupo al cual asignar
			Param param3= new Param();
			param3.put("id_sem",semInscripcion.getId_sem());
			List<SemGrupo> grupos = semGrupoDAO.listByParams(param3, new String[]{"nro"});
			for (SemGrupo semGrupo : grupos) {
				//Busco la capacidad del grupo
				Integer cap2=semGrupo.getCap();
				//Busco los matriculados en ese aula
				Param param4 = new Param();
				param4.put("id_gru", semGrupo.getId());
				param4.put("est", "A");
				List<SemInscripcion> matriculas2=semInscripcionDAO.listByParams(param4, null);
				Integer nro_vac2=cap2-matriculas2.size();
				if(nro_vac2>0) {
					semInscripcion.setId_gru(semGrupo.getId());
					break;
				}
			}
			
			int id_ind = semInscripcionDAO.saveOrUpdate(semInscripcion) ;
			result.setResult(id_ind );
			
			if (semInscripcion.getCorr()!=null){
				
				semInscripcion.setId(id_ind);
				//olimpiadaConfigService.enviarCorreoElectronico(inscripcion_ind);
				//enviar correo electronico
			}
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping(value = "/imprimirCarnetSeminario") //lo  llamas desde js
	@ResponseBody
	public void imprimirCarnetSeminario(HttpServletResponse request,HttpServletResponse response,Integer id_ins)  throws Exception {
		
		List<Row> inscritosForm = new ArrayList<Row>();
		List<Row>inscritos=seminarioDAO.obtenerDatosInscripcion(id_ins);
		

		inscritosForm = inscritos;
		
		byte[]  pdf_bytes  = generaCarnetSeminario(inscritosForm);
		
		//envia correo solo si es delegacion y tiene correo
		
		String correoInscrito = inscritosForm.get(0).getString("corr");
		Contador contador = contadorDAO.get(1);
		//if("1".equals(correo) && "D".equals(tipo) && correoDelegacion!=null && !"".equals(correoDelegacion)){
		
			

			Seminario seminario = seminarioDAO.get(inscritosForm.get(0).getInt("id_sem"));
			

			CorreoUtil correoUtil = new CorreoUtil();
			//String host = request.getHeader("host");
			String html = "Estimad@:";
			html += "<br><br>" + inscritosForm.get(0).getString("ape_pat")+ " " + inscritosForm.get(0).getString("ape_mat") +", " + inscritosForm.get(0).getString("nom");
			html += "<br><br>SU INSCRIPCIÓN AL SEMINARIO FUE SATISFACTORIA.";
			html += "<br><br><h2><u>FECHA DEL SEMINARIO</u></h2>";
			html += "<h1><font color='green'>" + seminario.getFec() + "</font></h1>";

			//html += "Este correo es informativo, favor no responder a esta dirección de correo, ya que no se encuentra habilitada para recibir mensajes.";
			//html += "<BR>Si requiere mayor información, contactar con secretaria de Lunes a Viernes de 8:00 a 12:45 y de 14:00 a 17:00 horas, teléfono 043-422110 o al e-mail:<a href='mailto:consultas@ae.edu.pe'>consultas@ae.edu.pe</a>";
			html += "<br><br>Atentamente";
			html += "<br><b>La Dirección</b>";

			correoUtil.enviar("Inscripción " + inscritosForm.get(0).getString("ape_pat")+" "+inscritosForm.get(0).getString("ape_mat")+" "+inscritosForm.get(0).getString("nom") , "", correoInscrito, html,inscritosForm.get(0).getString("nro_dni")+".pdf",pdf_bytes, contador.getUsr(),"ENCINAS",contador.getUsr(), contador.getPsw());

 
		String nombrePDF="Carnet";
		response.setHeader("Content-Disposition","inline; filename=\"" + nombrePDF +"\"");
		response.setContentType("application/pdf; name=\"" + nombrePDF + "\"");
		response.getOutputStream().write(pdf_bytes);
	}
	
	public byte[] generaCarnetSeminario(List<Row> inscritos) throws Exception {

		String sistemaOperativo = System.getProperty("os.name");
		String rutacARPETA = cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();

		
		if (sistemaOperativo.toUpperCase().indexOf("WINDOWS")>-1)
			rutacARPETA = "C:/plantillas/";

		rutacARPETA += "seminario/";
		
		Document document = new Document();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfCopy copy = new PdfCopy(document, baos);

		document.open();

		/*InputStream inputStream = null;
		System.out.println(rutacARPETA);
		String ruta = rutacARPETA + "oli-carnet-seminario.jasper";*/
		
		InputStream inputStream = null;
		System.out.println(rutacARPETA);
		String ruta = rutacARPETA + "oli-carnet-seminario.jrxml";

		inputStream = new FileInputStream(ruta);

		// grilla
		JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
		JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(inscritos);
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);
		byte[] bytesPDF = JasperExportManager.exportReportToPdf(jasperPrint);

		//PdfReader reader = new PdfReader(bytesPDF);

		//copy.addPage(copy.getImportedPage(reader, 1));

		//document.close();
		
		return bytesPDF; //baos.toByteArray();
		/*JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(inscritos);

		InputStream resource = new FileInputStream(rutacARPETA + "oli-carnet-seminario.jasper");
		//logger.debug("llamo al jasper");
		
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(resource);
		
		//logger.debug("despues del loadObject");
		
		resource.close();
		
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);
		 //  JasperPrint objJasperPrint = JasperFillManager.fillReport( new FileInputStream(new File(reportPath + "General.jasper")), params, datasource );  
		
		//byte[] bytesPDF = JasperExportManager.exportReportToPdf(jasperPrint);
		 String pdfGenerado =rutacARPETA + "/tmp/prueba.pdf";
			JasperExportManager.exportReportToPdfFile(jasperPrint, pdfGenerado);
			
			//logger.debug("despues de JasperExportManager.exportReportToPdfFile");
			
			//document.close();
			//return  baos.toByteArray();
			
		return pdfGenerado;*/
		//String pdfGenerado =rutacARPETA + "/tmp/boleta" + (new Date()).toString().replaceAll(" ","").replaceAll(":", "") +".pdf";
		//JasperExportManager.exportReportToPdfFile(jasperPrint, pdfGenerado);
		
		//logger.debug("despues de JasperExportManager.exportReportToPdfFile");
		
		//return pdfGenerado;
		/*inputStream = new FileInputStream(ruta);

		// grilla
		//JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
		//JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);

		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(inscritos);
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);
		byte[] bytesPDF = JasperExportManager.exportReportToPdf(jasperPrint);
		
		

		//InputStream resource = new FileInputStream(rutacARPETA + "/boleta.jasper");
		//logger.debug("llamo al jasper");
		
		
		
		//logger.debug("despues del loadObject");
		
		//resource.close();
		
		//JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);
		
		

		//PdfReader reader = new PdfReader(bytesPDF);

		//copy.addPage(copy.getImportedPage(reader, 1));

		//document.close();*/
		
		//return bytesPDF; //baos.toByteArray();

	}
	
	@RequestMapping(value = "/codigo/{codigo}", method = RequestMethod.GET)
	  public void showImage(HttpServletResponse response,@PathVariable String codigo) throws Exception {
		
		
		byte[] imgBD= null;
		String mimeType = "image/png";
		
		try {
			Archivo archivoBD = archivoDAO.getBycodigo(codigo);
			if(archivoBD!=null && archivoBD.getArchivo()!=null) {
				imgBD = archivoBD.getArchivo();
				mimeType = archivoBD.getMimetype();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(imgBD==null) {
			// SI NO EXISTE IMAGEN, LO LLENAMOS CON UNO X DEFECTO
			InputStream is = null;
			if(codigo.equals("insignia")) {
				is = getFileResources("insignia.png");
			}else
				is = getFileResources("cloud.png");
			imgBD = new byte[is.available()];
			is.read(imgBD);
		}

	    response.setHeader("Cache-Control", "no-store");
	    response.setHeader("Pragma", "no-cache");
	    response.setDateHeader("Expires", 0);
	    response.setContentType(mimeType);
	    ServletOutputStream responseOutputStream = response.getOutputStream();
	    responseOutputStream.write(imgBD);
	    responseOutputStream.flush();
	    responseOutputStream.close();
	  }
	
	 public InputStream getFileResources(String fileName){

		 	try {
	            File file = ResourceUtils.getFile("classpath:" + fileName);
	            InputStream in = new FileInputStream(file);
	            return in;
	        } catch (IOException e) {
	        	return null;
	        }

	    }
	 
	//alumnos matriculados
		@RequestMapping(value = "/autocomplete",method = RequestMethod.GET)
		public Object autocompleteAjax(@RequestParam String term, @RequestParam Integer id_anio) {
			return matriculaDAO.alumnosCombo(term,  id_anio);
		}
		
	@RequestMapping(value = "/autocompleteParaDescuento",method = RequestMethod.GET)
	public Object autocompleteParaDescuentoAjax(@RequestParam String term,@RequestParam Integer id_anio,@RequestParam Integer id_suc) {
		 
		return matriculaDAO.matriculadosParaDescuentoCombo(term, id_anio,id_suc);
		
	}
	
	@RequestMapping(value = "/autocompleteCondicionConductual")
	public Object autocompleteAjax(@RequestParam String term, @RequestParam Integer id_anio, Integer id_rol, Integer id_usr) {
		return matriculaDAO.matriculadosxUsuario(term, id_anio, id_rol, id_usr);

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
	
	/**
	 * Procesar el archivo excel del banco FINANCIERO TODO FALTA PARAMETRIAR POR BANCO
	 * @param uploadfile
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/xls/vistaPrevia")
	public AjaxResponseBody uploadFileParaVistaPrevia(@RequestParam("file") MultipartFile uploadfile,Integer id_banco) throws IOException{

		AjaxResponseBody result = new AjaxResponseBody();

		if (uploadfile.isEmpty()) {
			result.setCode("ARCHIVO");
			result.setMsg("archivo es vacio");
			return result;
		}
		
		 /*ZipInputStream zis = new ZipInputStream(uploadfile.getInputStream());
		 ZipEntry ze;*/
		// while ((ze = zis.getNextEntry()) != null) {
		 // process entry
			 try {

					if (id_banco.intValue() == EnumBanco.FINANCIERO.getValue()){
						InputStream is = uploadfile.getInputStream();
						//List<Map<String,Object>> listaCargada= procesaExcelBancoFinanciero(is,false);
						//result.setResult(listaCargada);
					}
					
					if (id_banco.intValue() == EnumBanco.BCP.getValue()){
						InputStream is = uploadfile.getInputStream();
						List<Map<String,Object>>  listaCargada= procesaTXTBanco(is,false,id_banco);
						result.setResult(listaCargada);
					}
					
					if (id_banco.intValue() == EnumBanco.CONTINENTAL.getValue()){
						InputStream is = uploadfile.getInputStream();
						List<Map<String,Object>>  listaCargada= procesaTXTBanco(is,false,id_banco);
						//List<Map<String,Object>>  listaCargada= procesaTXTBancoContinental(is,false);
						result.setResult(listaCargada);
					}
					
					return result;
					
				} catch (Exception e) {
					result.setCode("ARCHIVO");
					result.setMsg("archivo con errores:" + e.getMessage());
					e.printStackTrace();
					return result;
				}
		// }

		

	}
	
	/**
	 * Procesar el archivo excel del banco FINANCIERO TODO FALTA PARAMETRIAR POR BANCO
	 * @param uploadfile
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/xls/upload")
	public AjaxResponseBody uploadFile(@RequestParam("file") MultipartFile uploadfile,Integer id_banco,Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();

		if (uploadfile.isEmpty()) {
			result.setCode("ARCHIVO");
			result.setMsg("archivo es vacio");
			return result;
		}

		try {

			InputStream is = uploadfile.getInputStream();

			List<Map<String,Object>> listaCargada = null;
			if (id_banco.intValue() == EnumBanco.FINANCIERO.getValue()) {
				//listaCargada= procesaTXTBanco(is,true,id_banco);
			} else if (id_banco.intValue() == EnumBanco.CONTINENTAL.getValue()){
				listaCargada= procesaTXTBanco(is,true,id_banco);
			} else if (id_banco.intValue() == EnumBanco.BCP.getValue()){
				listaCargada= procesaTXTBanco(is,true,id_banco);
			}
				
			
				
			
			result.setResult(listaCargada);
			return result;
			
		} catch (Exception e) {
			result.setCode("ARCHIVO");
			result.setMsg("archivo con errores:" + e.getMessage());
			e.printStackTrace();
			return result;
		}

	}
	
	/**
	 * Graba los pagos del banco continental al sige, en caso, el registro ya ha sido procesado anteriormente, simplemente se ignora
	 * 
	 * @param inputStream
	 * @param debeGrabar
	 * @return
	 * @throws Exception
	 */
	/*	@SuppressWarnings("unused")
		@Transactional
		public List<Map<String,Object>>  procesaTXTBancoContinental(InputStream inputStream, boolean debeGrabar ) throws Exception{


	        BufferedReader br = null;
	         
	        Reader r = new InputStreamReader(inputStream);
	        String line;

	        StringBuilder sb = new StringBuilder();
	        char[] chars = new char[4*1024];
	        int len;
	        while((len = r.read(chars))>=0) {
	            sb.append(chars, 0, len);
	        }
	       
	        String  nroSerieLocal = null;
			if (!debeGrabar){
				nroSerieLocal =  facturacionService.getNroReciboBanco(2);
				nroSerieLocal = nroSerieLocal.split("-")[0] + "-" + String.format("%06d", Integer.parseInt(nroSerieLocal.split("-")[1]) - 1);
			}
			
	        String[] lines = sb.toString().split(System.getProperty("line.separator"));
	       
	        //leyendo linea 1

	        String line1 = lines[0];
	        //if (line1.length()<152)
	       	if (line1.trim().length()<45)
	       		throw new Exception("Archivo no tiene tama�o de cabecera correcto");
	        
	        String tipoRegistro 	= line1.substring(0,2); 
	        String ruc 				= line1.substring(2,13);
	        String clase 			= line1.substring(13,16);
	        String moneda 			= line1.substring(16,19);
	        String fecha 			= line1.substring(19,27);
	        String cuentaRecaudadora= line1.substring(27,45);
	        
	        if (!tipoRegistro.equals("01")){
	        	throw new Exception("Archivo no tiene el indicador de cabecera correcto");
	        }
	        
	        if (!ruc.equals("20531084587")){
	        	throw new Exception("Archivo no tiene el RUC correcto");
	        }
	        
	        List<Map<String,Object>> listBanco = new ArrayList<Map<String,Object>>();
	        
	        //String fechaPago		= linea.substring(135,143);
	        
	        Date fechaProcesoExcel 		= FechaUtil.toDate(fecha, "yyyyMMdd");
	        
	        //leer lineas detalle
	        List<String> listLineas = new ArrayList<String>();
	        
	        for (int i = 1; i < lines.length; i++) {
	        	
				String linea = lines[i].replaceAll("\\r\\n|\\r|\\n", "");//ELIMINA LOS 'ENTER'
				tipoRegistro = linea.substring(0,2);
				if  (tipoRegistro.equals("02")){
			
					if (linea.trim().length()!=151)
						throw new Exception("La linea " + (++i) + " no tiene el tama�o correcto:" + linea + ":");
					
					String fechaPago		= linea.substring(135,143);
					
					listLineas.add(linea) ;
					
				}
				
				if  (tipoRegistro.equals("03")){
					//TOTALES				
					break;
				}
			}
	        
	        Collections.sort(listLineas, new Comparator<String>() {
	            @Override
	            public int compare(String s1, String s2) {
	                // return p1.age+"".compareTo(p2.age+""); //sort by age
	                return s1.substring(71,80).compareTo(s2.substring(71,80)); // if you want to short by name
	                //return s1.substring(32,80).compareTo(s2.substring(135,143)); // if you want to short by name
	            }
	        });
	        
	        
	        
	        
	        //procesar lineas detalle
	        
	        for (String linea : listLineas) {
				
			
	        //for (int i = 1; i < lines.length; i++) {
	        	
				//String linea = lines[i].replaceAll("\\r\\n|\\r|\\n", "");//ELIMINA LOS 'ENTER'
				tipoRegistro = linea.substring(0,2);
				

					String nombreCliente	= linea.substring(2,32);
					String referencias		= linea.substring(32,80);
					String importeOrigen	= linea.substring(80,95);
					String strImporteDepositado= linea.substring(95,110);
					String importeMora		= linea.substring(110,125);
					String oficina			= linea.substring(125,129);
					String nroMovimiento	= linea.substring(129,135);
					String fechaPago		= linea.substring(135,143);
					Date fechaPagoDate 		= FechaUtil.toDate(fechaPago, "yyyyMMdd");
					String tipoValor		= linea.substring(143,145);
					String canal			= linea.substring(145,147);
					String vacio			= linea.substring(147);

					//fechaProcesoExcel 		= FechaUtil.toDate(fechaPago, "yyyyMMdd");
					String descripcion		= referencias.substring(11,35);
					String strIdPago		= referencias.substring(39);
					Integer id_pago 		= Integer.parseInt(strIdPago); 
					
					AcademicoPago pago = academicoPagoDAO.get(id_pago);
					
					//Ver si esta pagado el mes anterior
					
					
					/*if(!fechaPago.equals(fecha)){
						//NO PROCESAR
						continue;
					}*/
					
			/*	 	int dia_mora = 10;
				 	boolean existioPrimerVencimiento = false;
		/*		 	
				 	int primerVencimiento =  getFecVencimiento(id_anio, 3,dia_mora );
					if(Integer.parseInt(fechaPago)>primerVencimiento)
						existioPrimerVencimiento = true;
	*/

		/*			Map<String,Object> map = new HashMap<String,Object>();

					//transformar importe a BigDecimal
					BigDecimal montoVentanilla = new BigDecimal(strImporteDepositado);
					montoVentanilla = montoVentanilla.divide(new BigDecimal(100),2);
					
					map.put("idPagoBanco", id_pago);
					map.put("montoVentanilla", montoVentanilla);
					//map.put("fechaProceso", fechaPago);
					map.put("fechaProceso", fechaPago);
					
					
					////System.out.println(id_pago);
					AcademicoPago academicoPago = academicoPagoDAO.get(id_pago);
					System.out.println(academicoPago.getId_mat());
					Map<String,Object> matricula = matriculaDAO.getMatriculaDatosPrincipales(academicoPago.getId_mat());

					map.put("alumno", matricula.get("alumno"));
					map.put("local", matricula.get("local"));
					map.put("nivel", matricula.get("nivel"));
					map.put("montoMes", academicoPago.getMonto());
					
					
					if (academicoPago==null ){
						map.put("procesado", "ERROR: Alumno no encontrado");
					}else{
						if ("1".equals(academicoPago.getCanc())){
							//NO SE HACE NADA, POR QUE YA EST� CANCELADO
							map.put("procesado", "NO");
							map.put("nro_rec", academicoPago.getNro_rec());
							map.put("montoVentanilla", academicoPago.getMontoTotal());
							map.put("fechaProceso",FechaUtil.toString(academicoPago.getFec_pago()));
							if(academicoPago.getMens().equals(0) && academicoPago.getTip().equals("MAT"))
								map.put("mes","MATRICULA");
							else if(academicoPago.getMens().equals(0) && academicoPago.getTip().equals("ING"))
								map.put("mes","CUOTA DE INGRESO");
							else
								map.put("mes",academicoPago.getMes());							
						}else{
							String nuevo_nro_recivo = "";
							if (debeGrabar)
								nuevo_nro_recivo = facturacionService.getNroReciboBanco(2);//1== codigo de banco
							else{
								//incrementarlo manualmente
								//if(linea==1)
									nuevo_nro_recivo = nroSerieLocal;
								//else
									nuevo_nro_recivo = nuevo_nro_recivo.split("-")[0] + "-" + String.format("%06d", Integer.parseInt(nuevo_nro_recivo.split("-")[1]) + 1);
								nroSerieLocal = nuevo_nro_recivo;
							}
							
							
							//Obtener descuento personalizado si es que lo tiene
							Map<String, Object> alumnoDescuento = alumnoDescuentoDAO.getAlumnoPorMatricula(academicoPago.getId_mat());
							//Buscar si ya tiene Beca
							
							BigDecimal descuentoPersonalizado = null;
							BigDecimal descuentoHermano = null;
							String id_descuento_personalizado = null;
							
							if (alumnoDescuento!=null){
								descuentoPersonalizado = new BigDecimal(alumnoDescuento.get("descuento").toString());
							}
							
							academicoPago.setBanco("CONTINENTAL");//debe parametrizarse por archivo
							academicoPago.setCanc("1");
							

							/*
							int fecVencimiento =  getFecVencimiento(id_anio, Integer.parseInt(academicoPago.getMes()),dia_mora );
							
							if(Integer.parseInt(fechaPago)>primerVencimiento)
								
							if (descuentoPersonalizado!=null && Integer.parseInt(fechaPago)<=fecVencimiento ){
	 							descuentoHermano = new BigDecimal(0);
							}else{
								descuentoPersonalizado = new BigDecimal(0);
								
								if (Integer.parseInt(fechaPago)<=fecVencimiento){
								 	
									if(cant_hermanos>1 && Integer.parseInt(matricula.get("id_niv").toString())!=Constante.NIVEL_INICIAL ){
										descuentoHermano = desc_hermano;
									}
									else
										descuentoHermano = new BigDecimal(0);
			 

								}else{
									descuentoHermano = new BigDecimal(0);
									
								}
								

							}			
							*/
							
				/*			BigDecimal descuentoTotal = academicoPago.getMonto().subtract(montoVentanilla);
							academicoPago.setDesc_pronto_pago(descuentoTotal);
							academicoPago.setMontoTotal(montoVentanilla);
							academicoPago.setFec_pago(fechaPagoDate);
							academicoPago.setNro_rec(nuevo_nro_recivo);
							academicoPago.setNro_pe(canal + vacio);
							academicoPago.setUsr_ins(1);
							map.put("procesado", "SI");
							map.put("nro_rec", nuevo_nro_recivo);
							if(!academicoPago.getMens().equals(0))
							map.put("mes",academicoPago.getMes());
							else if(academicoPago.getMens().equals(0) && academicoPago.getTip().equals("MAT"))
								map.put("mes","MATRICULA");
							else if(academicoPago.getMens().equals(0) && academicoPago.getTip().equals("ING"))
								map.put("mes","CUOTA DE INGRESO");
							
							if (debeGrabar){
								academicoPagoDAO.saveOrUpdate(academicoPago);
								//Si es matricula cambio a validado
								if(academicoPago.getMens().equals(0))
								matriculaDAO.validarContratoxUsuario(Integer.parseInt(matricula.get("id").toString()));
								facturacionService.updateNroReciboBanco(2, nuevo_nro_recivo);
							}

						}
					}
					
					
					listBanco.add(map);
					
				}
				
			        
			if (debeGrabar)
				facturacionService.cargarMovimientosBanco(fechaProcesoExcel );

	        
	        
	        return listBanco;
		}*/
		
		
		/**
		 * Graba los pagos del BCP al sige, en caso, el registro ya ha sido procesado anteriormente, simplemente se ignora
		 * 
		 * @param inputStream
		 * @param debeGrabar
		 * @return
		 * @throws Exception
		 */
			@SuppressWarnings("unused")
			@Transactional
			public List<Map<String,Object>>  procesaTXTBanco(InputStream inputStream, boolean debeGrabar, Integer id_bco ) throws Exception{


		        BufferedReader br = null;
		         
		        Reader r = new InputStreamReader(inputStream);
		        String line;

		        StringBuilder sb = new StringBuilder();
		        char[] chars = new char[4*1024];
		        int len;
		        while((len = r.read(chars))>=0) {
		            sb.append(chars, 0, len);
		        }
		       
		        String  nroSerieLocal = null;
				if (!debeGrabar){
					nroSerieLocal =  facturacionService.getNroReciboBanco(id_bco);
					nroSerieLocal = nroSerieLocal.split("-")[0] + "-" + String.format("%06d", Integer.parseInt(nroSerieLocal.split("-")[1]) - 1);
				}
				
		        String[] lines = sb.toString().split(System.getProperty("line.separator"));
		       
		        //leyendo linea 1

		        String line1 = lines[0];
		        //if (line1.length()<152)
		        if(id_bco==1) {
		        	if (line1.trim().length()<68)
			       		throw new Exception("Archivo no tiene tama�o de cabecera correcto");
		        } if (id_bco==2) {
		        	if (line1.trim().length()<45)
			       		throw new Exception("Archivo no tiene tama�o de cabecera correcto");
		        }
		       	
		        String tipoRegistro 	= ""; 
		        String ruc 				= "";
		        String clase 			= "";
		        String moneda 			= "";
		        String fecha 			= "";
		        String cuentaRecaudadora= "";
		        String cuentaEmpresa	= "";
		        //BCP
		        String cod_cta			= "";
		        String cantidad_reg		= "";
		        String monto_total		= "";
		        if(id_bco==1) {   
			        tipoRegistro 	= line1.substring(0,2); 
		        	cod_cta			= line1.substring(2,5);
		        	moneda 			= line1.substring(5,6);
		        	cuentaEmpresa	= line1.substring(6,13);
		        	fecha 			= line1.substring(14,22);
		        	cantidad_reg 	= line1.substring(22,31);
		        	monto_total 	= line1.substring(31,46);
		        	
		        	if (!tipoRegistro.equals("CC")){
			        	throw new Exception("Archivo no tiene el indicador de cabecera correcto");
			        }
			        
			        if (!cuentaEmpresa.equals("8739262")){
			        	throw new Exception("Archivo no tiene el Número de Cuenta de la Empresa correcta");
			        }
		        } else if(id_bco==2) {
		        	tipoRegistro 	= line1.substring(0,2); 
			        ruc 			= line1.substring(2,13);
			        clase 			= line1.substring(13,16);
			        moneda 			= line1.substring(16,19);
			        fecha 			= line1.substring(19,27);
			        cuentaRecaudadora= line1.substring(27,45);
			        
			        if (!tipoRegistro.equals("01")){
			        	throw new Exception("Archivo no tiene el indicador de cabecera correcto");
			        }
			        
			        if (!ruc.equals("20531084587")){
			        	throw new Exception("Archivo no tiene el RUC correcto");
			        }
		        }
		       
		        List<Map<String,Object>> listBanco = new ArrayList<Map<String,Object>>();
		        
		        //String fechaPago		= linea.substring(135,143);
		        
		        Date fechaProcesoExcel 		= FechaUtil.toDate(fecha, "yyyyMMdd");
		        
		        //leer lineas detalle
		        List<String> listLineas = new ArrayList<String>();
		        
		        if(id_bco==1) {
		        	for (int i = 1; i < lines.length; i++) {
			        	
						String linea = lines[i].replaceAll("\\r\\n|\\r|\\n", "");//ELIMINA LOS 'ENTER'
						tipoRegistro = linea.substring(0,2);
						if  (tipoRegistro.equals("DD")){
					
							if (linea.trim().length()!=233)
								throw new Exception("La linea " + (++i) + " no tiene el tamaño correcto:" + linea + ":");
							
							String fechaPago		= linea.substring(57,65);
							
							listLineas.add(linea) ;
							
						}
						
						/*if  (tipoRegistro.equals("03")){
							//TOTALES				
							break;
						}*/
					}
			        
			        Collections.sort(listLineas, new Comparator<String>() {
			            @Override
			            public int compare(String s1, String s2) {
			                // return p1.age+"".compareTo(p2.age+""); //sort by age
			                return s1.substring(71,80).compareTo(s2.substring(71,80)); // if you want to short by name
			                //return s1.substring(32,80).compareTo(s2.substring(135,143)); // if you want to short by name
			            }
			        });
			        
			        
			        
			        
			        //procesar lineas detalle
			        
			        for (String linea : listLineas) {
						
					
			        //for (int i = 1; i < lines.length; i++) {
			        	
						//String linea = lines[i].replaceAll("\\r\\n|\\r|\\n", "");//ELIMINA LOS 'ENTER'
						tipoRegistro = linea.substring(0,2);
						
							String dni_cliente = linea.substring(14,27);
							String referencias		= linea.substring(27,57);
							String fechaPago		= linea.substring(57,65);
							String fechaVencimiento		= linea.substring(65,73);
							String strImporteDepositado= linea.substring(73,88);
							String importeMora		= linea.substring(88,103);
							String strImporteTotal= linea.substring(103,118);
							String sucursal = linea.substring(118,124);
							String nro_ope = linea.substring(124,130);
							String descripcion		= linea.substring(197,217);
							String canal			= linea.substring(225,233);
							String vacio			= linea.substring(234);
							/*String nombreCliente	= linea.substring(2,32);
							//String referencias		= linea.substring(32,80);
							String importeOrigen	= linea.substring(80,95);
							String strImporteDepositado= linea.substring(95,110);
							String importeMora		= linea.substring(110,125);
							String oficina			= linea.substring(125,129);
							String nroMovimiento	= linea.substring(129,135);
							String fechaPago		= linea.substring(135,143);
							Date fechaPagoDate 		= FechaUtil.toDate(fechaPago, "yyyyMMdd");
							String tipoValor		= linea.substring(143,145);
							String canal			= linea.substring(145,147);
							String vacio			= linea.substring(147);*/

							//fechaProcesoExcel 		= FechaUtil.toDate(fechaPago, "yyyyMMdd");
							Date fechaPagoDate 		= FechaUtil.toDate(fechaPago, "yyyyMMdd");
							String strIdPago		= referencias.trim();
							Integer id_pago 		= Integer.parseInt(strIdPago); 
							
							AcademicoPago pago = academicoPagoDAO.get(id_pago);
							
							//Ver si esta pagado el mes anterior
							
							
							/*if(!fechaPago.equals(fecha)){
								//NO PROCESAR
								continue;
							}*/
							
						 	//int dia_mora = 10;
						 	boolean existioPrimerVencimiento = false;
				/*		 	
						 	int primerVencimiento =  getFecVencimiento(id_anio, 3,dia_mora );
							if(Integer.parseInt(fechaPago)>primerVencimiento)
								existioPrimerVencimiento = true;
			*/

							Map<String,Object> map = new HashMap<String,Object>();

							//transformar importe a BigDecimal
							BigDecimal montoVentanilla = new BigDecimal(strImporteDepositado);
							montoVentanilla = montoVentanilla.divide(new BigDecimal(100),2);
							
							map.put("idPagoBanco", id_pago);
							map.put("montoVentanilla", montoVentanilla);
							//map.put("fechaProceso", fechaPago);
							map.put("fechaProceso", fechaPago);
							
							
							////System.out.println(id_pago);
							AcademicoPago academicoPago = academicoPagoDAO.get(id_pago);
							System.out.println(academicoPago.getId_mat());
							Map<String,Object> matricula = matriculaDAO.getMatriculaDatosPrincipales(academicoPago.getId_mat());

							map.put("alumno", matricula.get("alumno"));
							map.put("local", matricula.get("local"));
							map.put("nivel", matricula.get("nivel"));
							map.put("montoMes", academicoPago.getMonto());
							
							
							if (academicoPago==null ){
								map.put("procesado", "ERROR: Alumno no encontrado");
							}else{
								if ("1".equals(academicoPago.getCanc())){
									//NO SE HACE NADA, POR QUE YA EST� CANCELADO
									map.put("procesado", "NO");
									map.put("nro_rec", academicoPago.getNro_rec());
									map.put("montoVentanilla", academicoPago.getMontoTotal());
									map.put("fechaProceso",FechaUtil.toString(academicoPago.getFec_pago()));
									if(academicoPago.getMens().equals(0) && academicoPago.getTip().equals("MAT"))
										map.put("mes","MATRICULA");
									else if(academicoPago.getMens().equals(0) && academicoPago.getTip().equals("ING"))
										map.put("mes","CUOTA DE INGRESO");
									else
										map.put("mes",academicoPago.getMes());							
								}else{
									String nuevo_nro_recivo = "";
									if (debeGrabar)
										nuevo_nro_recivo = facturacionService.getNroReciboBanco(id_bco);//1== codigo de banco
									else{
										//incrementarlo manualmente
										//if(linea==1)
											nuevo_nro_recivo = nroSerieLocal;
										//else
											nuevo_nro_recivo = nuevo_nro_recivo.split("-")[0] + "-" + String.format("%06d", Integer.parseInt(nuevo_nro_recivo.split("-")[1]) + 1);
										nroSerieLocal = nuevo_nro_recivo;
									}
									
									
									//Obtener descuento personalizado si es que lo tiene
									Map<String, Object> alumnoDescuento = alumnoDescuentoDAO.getAlumnoPorMatricula(academicoPago.getId_mat());
									BigDecimal descuentoPersonalizado = null;
									BigDecimal descuentoHermano = null;
									BigDecimal descuentoBeca = academicoPago.getDesc_beca();
									String id_descuento_personalizado = null;
									
									if (alumnoDescuento!=null){
										descuentoPersonalizado = new BigDecimal(alumnoDescuento.get("descuento").toString());
									}
									
									academicoPago.setBanco("BCP");//debe parametrizarse por archivo
									academicoPago.setCanc("1");
									

									/*
									int fecVencimiento =  getFecVencimiento(id_anio, Integer.parseInt(academicoPago.getMes()),dia_mora );
									
									if(Integer.parseInt(fechaPago)>primerVencimiento)
										
									if (descuentoPersonalizado!=null && Integer.parseInt(fechaPago)<=fecVencimiento ){
			 							descuentoHermano = new BigDecimal(0);
									}else{
										descuentoPersonalizado = new BigDecimal(0);
										
										if (Integer.parseInt(fechaPago)<=fecVencimiento){
										 	
											if(cant_hermanos>1 && Integer.parseInt(matricula.get("id_niv").toString())!=Constante.NIVEL_INICIAL ){
												descuentoHermano = desc_hermano;
											}
											else
												descuentoHermano = new BigDecimal(0);
					 

										}else{
											descuentoHermano = new BigDecimal(0);
											
										}
										

									}			
									*/
									
									BigDecimal descuentoTotal = null;
									if(descuentoBeca!=null) {
										if(!descuentoBeca.equals(new BigDecimal(0))) {
											descuentoTotal = academicoPago.getMonto().subtract(descuentoBeca).subtract(montoVentanilla);
										} else {
											descuentoTotal = academicoPago.getMonto().subtract(montoVentanilla);
										}
									} else {
										descuentoTotal = academicoPago.getMonto().subtract(montoVentanilla);
									}
									academicoPago.setDesc_personalizado(descuentoTotal);
									academicoPago.setMontoTotal(montoVentanilla);
									academicoPago.setFec_pago(fechaPagoDate);
									academicoPago.setNro_rec(nuevo_nro_recivo);
									academicoPago.setNro_pe(canal + vacio);
									academicoPago.setUsr_ins(1);
									map.put("procesado", "SI");
									map.put("nro_rec", nuevo_nro_recivo);
									if(!academicoPago.getMens().equals(0))
									map.put("mes",academicoPago.getMes());
									else if(academicoPago.getMens().equals(0) && academicoPago.getTip().equals("MAT"))
										map.put("mes","MATRICULA");
									else if(academicoPago.getMens().equals(0) && academicoPago.getTip().equals("ING"))
										map.put("mes","CUOTA DE INGRESO");
									
									if (debeGrabar){
										academicoPagoDAO.saveOrUpdate(academicoPago);
										//Si es matricula cambio a validado
										if(academicoPago.getMens().equals(0)) {
											System.out.println(academicoPago.getId());
											System.out.println(matricula.get("id"));
											matriculaDAO.validarContratoxUsuarioCarga(Integer.parseInt(matricula.get("id").toString()));
										}
										
										facturacionService.updateNroReciboBanco(id_bco, nuevo_nro_recivo);
									}

								}
							}
							
							
							listBanco.add(map);
							
						}
						
					        
					if (debeGrabar)
						facturacionService.cargarMovimientosBanco(fechaProcesoExcel );
		        } else if(id_bco==2) {
		        	for (int i = 1; i < lines.length; i++) {
			        	
						String linea = lines[i].replaceAll("\\r\\n|\\r|\\n", "");//ELIMINA LOS 'ENTER'
						tipoRegistro = linea.substring(0,2);
						if  (tipoRegistro.equals("02")){
					
							if (linea.trim().length()!=151)
								throw new Exception("La linea " + (++i) + " no tiene el tama�o correcto:" + linea + ":");
							
							String fechaPago		= linea.substring(135,143);
							
							listLineas.add(linea) ;
							
						}
						
						if  (tipoRegistro.equals("03")){
							//TOTALES				
							break;
						}
					}
			        
			        Collections.sort(listLineas, new Comparator<String>() {
			            @Override
			            public int compare(String s1, String s2) {
			                // return p1.age+"".compareTo(p2.age+""); //sort by age
			                return s1.substring(71,80).compareTo(s2.substring(71,80)); // if you want to short by name
			                //return s1.substring(32,80).compareTo(s2.substring(135,143)); // if you want to short by name
			            }
			        });
			        
			        
			        
			        
			        //procesar lineas detalle
			        
			        for (String linea : listLineas) {
						
					
			        //for (int i = 1; i < lines.length; i++) {
			        	
						//String linea = lines[i].replaceAll("\\r\\n|\\r|\\n", "");//ELIMINA LOS 'ENTER'
						tipoRegistro = linea.substring(0,2);
						

							String nombreCliente	= linea.substring(2,32);
							String referencias		= linea.substring(32,80);
							String importeOrigen	= linea.substring(80,95);
							String strImporteDepositado= linea.substring(95,110);
							String importeMora		= linea.substring(110,125);
							String oficina			= linea.substring(125,129);
							String nroMovimiento	= linea.substring(129,135);
							String fechaPago		= linea.substring(135,143);
							Date fechaPagoDate 		= FechaUtil.toDate(fechaPago, "yyyyMMdd");
							String tipoValor		= linea.substring(143,145);
							String canal			= linea.substring(145,147);
							String vacio			= linea.substring(147);

							//fechaProcesoExcel 		= FechaUtil.toDate(fechaPago, "yyyyMMdd");
							String descripcion		= referencias.substring(11,35);
							String strIdPago		= referencias.substring(39);
							Integer id_pago 		= Integer.parseInt(strIdPago); 
							
							AcademicoPago pago = academicoPagoDAO.get(id_pago);
							
							//Ver si esta pagado el mes anterior
							
							
							/*if(!fechaPago.equals(fecha)){
								//NO PROCESAR
								continue;
							}*/
							
						 	int dia_mora = 10;
						 	boolean existioPrimerVencimiento = false;
				/*		 	
						 	int primerVencimiento =  getFecVencimiento(id_anio, 3,dia_mora );
							if(Integer.parseInt(fechaPago)>primerVencimiento)
								existioPrimerVencimiento = true;
			*/

							Map<String,Object> map = new HashMap<String,Object>();

							//transformar importe a BigDecimal
							BigDecimal montoVentanilla = new BigDecimal(strImporteDepositado);
							montoVentanilla = montoVentanilla.divide(new BigDecimal(100),2);
							
							map.put("idPagoBanco", id_pago);
							map.put("montoVentanilla", montoVentanilla);
							//map.put("fechaProceso", fechaPago);
							map.put("fechaProceso", fechaPago);
							
							
							////System.out.println(id_pago);
							AcademicoPago academicoPago = academicoPagoDAO.get(id_pago);
							System.out.println(academicoPago.getId_mat());
							Map<String,Object> matricula = matriculaDAO.getMatriculaDatosPrincipales(academicoPago.getId_mat());

							map.put("alumno", matricula.get("alumno"));
							map.put("local", matricula.get("local"));
							map.put("nivel", matricula.get("nivel"));
							map.put("montoMes", academicoPago.getMonto());
							
							
							if (academicoPago==null ){
								map.put("procesado", "ERROR: Alumno no encontrado");
							}else{
								if ("1".equals(academicoPago.getCanc())){
									//NO SE HACE NADA, POR QUE YA EST� CANCELADO
									map.put("procesado", "NO");
									map.put("nro_rec", academicoPago.getNro_rec());
									map.put("montoVentanilla", academicoPago.getMontoTotal());
									map.put("fechaProceso",FechaUtil.toString(academicoPago.getFec_pago()));
									if(academicoPago.getMens().equals(0) && academicoPago.getTip().equals("MAT"))
										map.put("mes","MATRICULA");
									else if(academicoPago.getMens().equals(0) && academicoPago.getTip().equals("ING"))
										map.put("mes","CUOTA DE INGRESO");
									else
										map.put("mes",academicoPago.getMes());							
								}else{
									String nuevo_nro_recivo = "";
									if (debeGrabar)
										nuevo_nro_recivo = facturacionService.getNroReciboBanco(2);//1== codigo de banco
									else{
										//incrementarlo manualmente
										//if(linea==1)
											nuevo_nro_recivo = nroSerieLocal;
										//else
											nuevo_nro_recivo = nuevo_nro_recivo.split("-")[0] + "-" + String.format("%06d", Integer.parseInt(nuevo_nro_recivo.split("-")[1]) + 1);
										nroSerieLocal = nuevo_nro_recivo;
									}
									
									
									//Obtener descuento personalizado si es que lo tiene
									Map<String, Object> alumnoDescuento = alumnoDescuentoDAO.getAlumnoPorMatricula(academicoPago.getId_mat());
									BigDecimal descuentoPersonalizado = null;
									BigDecimal descuentoHermano = null;
									String id_descuento_personalizado = null;
									
									if (alumnoDescuento!=null){
										descuentoPersonalizado = new BigDecimal(alumnoDescuento.get("descuento").toString());
									}
									
									academicoPago.setBanco("CONTINENTAL");//debe parametrizarse por archivo
									academicoPago.setCanc("1");
									

									
									/*
									int fecVencimiento =  getFecVencimiento(id_anio, Integer.parseInt(academicoPago.getMes()),dia_mora );
									
									if(Integer.parseInt(fechaPago)>primerVencimiento)
										
									if (descuentoPersonalizado!=null && Integer.parseInt(fechaPago)<=fecVencimiento ){
			 							descuentoHermano = new BigDecimal(0);
									}else{
										descuentoPersonalizado = new BigDecimal(0);
										
										if (Integer.parseInt(fechaPago)<=fecVencimiento){
										 	
											if(cant_hermanos>1 && Integer.parseInt(matricula.get("id_niv").toString())!=Constante.NIVEL_INICIAL ){
												descuentoHermano = desc_hermano;
											}
											else
												descuentoHermano = new BigDecimal(0);
					 

										}else{
											descuentoHermano = new BigDecimal(0);
											
										}
										

									}			
									*/
									BigDecimal descuentoBeca = academicoPago.getDesc_beca();
									BigDecimal descuentoTotal = null;
									if(descuentoBeca!=null) {
										if(!descuentoBeca.equals(new BigDecimal(0))) {
											descuentoTotal = academicoPago.getMonto().subtract(descuentoBeca).subtract(montoVentanilla);
										} else {
											descuentoTotal = academicoPago.getMonto().subtract(montoVentanilla);
										}
									} else {
										descuentoTotal = academicoPago.getMonto().subtract(montoVentanilla);
									}
									//BigDecimal descuentoTotal = academicoPago.getMonto().subtract(montoVentanilla);
									academicoPago.setDesc_personalizado(descuentoTotal);
									academicoPago.setMontoTotal(montoVentanilla);
									academicoPago.setFec_pago(fechaPagoDate);
									academicoPago.setNro_rec(nuevo_nro_recivo);
									academicoPago.setNro_pe(canal + vacio);
									academicoPago.setUsr_ins(1);
									map.put("procesado", "SI");
									map.put("nro_rec", nuevo_nro_recivo);
									if(!academicoPago.getMens().equals(0))
									map.put("mes",academicoPago.getMes());
									else if(academicoPago.getMens().equals(0) && academicoPago.getTip().equals("MAT"))
										map.put("mes","MATRICULA");
									else if(academicoPago.getMens().equals(0) && academicoPago.getTip().equals("ING"))
										map.put("mes","CUOTA DE INGRESO");
									
									if (debeGrabar){
										academicoPagoDAO.saveOrUpdate(academicoPago);
										//Si es matricula cambio a validado
										if(academicoPago.getMens().equals(0))
										matriculaDAO.validarContratoxUsuarioCarga(Integer.parseInt(matricula.get("id").toString()));
										facturacionService.updateNroReciboBanco(2, nuevo_nro_recivo);
									}

								}
							}
							
							
							listBanco.add(map);
							
						}
						
					        
					if (debeGrabar)
						facturacionService.cargarMovimientosBanco(fechaProcesoExcel );
		        }
		        

		        
		        
		        return listBanco;
			}
		
		@RequestMapping(value = "/genArchivoAnual/{id_banco}/{id_anio}/{meses}", produces = "text/plain;charset=windows-1252")
		@ResponseBody
		public void genArchivoPagos(HttpServletRequest request,HttpServletResponse response,@PathVariable Integer id_banco,@PathVariable Integer id_anio, @PathVariable Integer[] meses)  throws Exception {
		  
			response.setContentType("text/plain");

			String fechaActual = FechaUtil.toStringMYQL(new Date());
			
			String archivoDownload="";
			
			Integer anio = Integer.parseInt(anioDAO.get(id_anio).getNom());
			
			if(id_banco==1) {
				archivoDownload= "CREP"+anio+".txt";
			} else if(id_banco==2) {
				archivoDownload= fechaActual + ".txt";
			}		
			
			response.setHeader("Content-Disposition","attachment;filename=" + archivoDownload);
	 
			Integer total=0;
			
			try {

				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

				String format = formatter.format(new Date());
				int fecActual = Integer.parseInt(format); 
				//Si le pasamos de un a�o especifico, generar� solo de eso, sino de todos
				//List<Map<String, Object>> pagosList = bancoDAO.pagosMes(id_anio);
				List<Map<String, Object>> pagosList = bancoDAO.pagosMes(id_anio,meses,id_anio-1,id_banco);
				
				StringBuilder sb = new StringBuilder("");
				
				boolean existioPrimerVencimiento = false;
			
				BigDecimal pagoTotal = BigDecimal.ZERO;
				for (int i = 0; i < pagosList.size(); i++) {

					Object id = pagosList.get(i).get("id");
					System.out.println("id->" + id);

					System.out.println("pagosList.get(0)->" + pagosList.get(i));

					//int dia_mora = Integer.parseInt(pagosList.get(i).get("dia_mora").toString());
					//Busco si existe una tarifa de emergencia
					//Row tarifa= tarifasEmergenciaDAO.listarTarifasPeriodo(Integer.parseInt(pagosList.get(i).get("id_per").toString()), Integer.parseInt(pagosList.get(i).get("id_mes").toString()));
					Row tarifa=null;
					if(!pagosList.get(i).get("id_mes").equals(0)) {
						tarifa= tarifasEmergenciaDAO.listarTarifasMEs(Integer.parseInt(pagosList.get(i).get("id_mes").toString()),Integer.parseInt(pagosList.get(i).get("id_anio_ult_mat").toString()));
					}
					
					System.out.println("tarifa->" + tarifa);
					//System.out.println(pagosList.get(i).get("));
					BigDecimal desc_hermano = new BigDecimal(0);
					if(tarifa!=null){
						desc_hermano=tarifa.getBigDecimal("des_hermano");
						System.out.println("desc_hermano->" + desc_hermano);
					} else{
						if(!pagosList.get(i).get("id_mes").toString().equals("0"))
							desc_hermano = new BigDecimal(10);
						else
							desc_hermano = new BigDecimal(0);
					}
					Integer dia_mora= pagosList.get(i).get("dia_mora")!=null ? Integer.parseInt(pagosList.get(i).get("dia_mora").toString()) : null;
				 	boolean hizo_cambio_local = (pagosList.get(i).get("solicitud")!=null);
					
				 	Object mensualidadBcoDescObj = pagosList.get(i).get("mensualidad_bco");
					////System.out.print(""+pagosList.get(i).get("hermanos"));
					//	String cant_hermanos= pagosList.get(i).get("hermanos").toString();
					Integer cant_hermanos=  Integer.parseInt(pagosList.get(i).get("hermanos").toString());
					Integer id_niv =   Integer.parseInt(pagosList.get(i).get("id_niv").toString());
					Integer mes =0;
					if(!pagosList.get(i).get("id_mes").equals(0))
					mes =   Integer.parseInt(pagosList.get(i).get("id_mes").toString());
					
					//logger.debug("ape_pat:" + pagosList.get(i).get("ape_pat") + "|mes:" + mes);
					
					BigDecimal pago_mes = new BigDecimal(pagosList.get(i).get("monto").toString());
					
					//Obtener el tipo de fecha de vencimiento
					String tipo_fec_venc = confMensualidadDAO.getByParams(new Param("id_per",pagosList.get(i).get("id_per"))).getTipo_fec_ven();
					
					Integer id_anio_ult_mat=Integer.parseInt(pagosList.get(i).get("id_anio_ult_mat").toString());
					Anio anio_ult_mat = anioDAO.getByParams(new Param("id",id_anio_ult_mat));

					int primerVencimiento=0;
					if(tipo_fec_venc!=null){
						if(tipo_fec_venc.equals(Constante.TIPO_FEC_VEN_FIN)){
							System.out.println("tipo_fec_venc->" + tipo_fec_venc);
							if(!mes.equals(0))
								primerVencimiento = pagosService.getFecVencimientoFin(Integer.parseInt(pagosList.get(i).get("id_mat").toString()), 3,"MEN");
							//else
							//	primerVencimiento = pagosService.getFecVencimientoFin(Integer.parseInt(pagosList.get(i).get("id_mat").toString()), 3,"MEN");
									
							//primerVencimiento = pagosService.getFecVencimientoFin(Integer.parseInt(pagosList.get(i).get("id_mat").toString()), 3);
						} else if(tipo_fec_venc.equals(Constante.TIPO_FEC_VEN_DIA)){
							primerVencimiento = getFecVencimiento(Integer.parseInt(anio_ult_mat.getNom()),3,dia_mora);
						}
					} else{
						if(dia_mora!=null){
							 primerVencimiento =  getFecVencimiento(Integer.parseInt(anio_ult_mat.getNom()), 3,dia_mora);
						} 
					}
					
					if(fecActual>primerVencimiento)
						existioPrimerVencimiento = true;

					
					if(hizo_cambio_local)
						pago_mes = new BigDecimal(pagosList.get(i).get("monto_conf").toString());
					
					BigDecimal desc_banco= new BigDecimal(0);
					Integer id_au_asi= Integer.parseInt(pagosList.get(i).get("id_au_asi").toString());
					//Buscamos si existe una modalidad configurada por el mes en generación
					Param param = new Param();
					param.put("id_au", id_au_asi);
					param.put("mes", Integer.parseInt(pagosList.get(i).get("id_mes").toString()));
					AulaModalidadDet aulaModalidadDet = aulaModalidadDetDAO.getByParams(param);
					//Buscar el costo de la modalidad
					BigDecimal mensualidad_modalidad_mes = null;
					if(aulaModalidadDet!=null) {
						Aula aula = aulaDAO.get(Integer.parseInt(pagosList.get(i).get("id_au_asi").toString()));
						TurnoAula turno_aula = turnoAulaDAO.getByParams(new Param("id_au", id_au_asi));
						Param param_mod = new Param();
						param_mod.put("id_per", aula.getId_per());
						param_mod.put("id_cct", turno_aula.getId_cit());
						param_mod.put("id_cme", aulaModalidadDet.getId_cme());
						ConfMensualidad confMensualidad = confMensualidadDAO.getByParams(param_mod);
						mensualidad_modalidad_mes = confMensualidad.getMonto();
						
					}
					
					
					if(tarifa!=null){
						//System.out.println(2);
						desc_banco = tarifa.getBigDecimal("des_banco");
						if(desc_banco==null)
							desc_banco=new BigDecimal(0);
						System.out.println("desc_banco-->" + desc_banco);
						// pago_mes=tarifa.getBigDecimal("monto");
						/*if(pagosList.get(i).get("monto_total")!=null)
							pago_mes = new BigDecimal(pagosList.get(i).get("monto_total").toString());

						else*/
							pago_mes=tarifa.getBigDecimal("monto");
					} else if (aulaModalidadDet!=null) {
						
						pago_mes=mensualidad_modalidad_mes;
					} else {	
						//System.out.println(3);
						if(pagosList.get(i).get("desc_banco")!=null)
							desc_banco = new BigDecimal(pagosList.get(i).get("desc_banco").toString());
					
					}
						
					BigDecimal pago_final= new BigDecimal(0);
					
					System.out.println("1");
					//Calculamos si tiene descuentos 0 definidos
					String mes_nom="0";
					if(!pagosList.get(i).get("id_mes").equals("0"))
						mes_nom=pagosList.get(i).get("id_mes").toString();
					Param param1 = new Param();
					param1.put("mens",mes_nom);
					param1.put("id_mat", pagosList.get(i).get("id_mat").toString());
					AcademicoPago descuento = academicoPagoDAO.getByParams(param1);
					
					System.out.println("2");

					
					//CALCULAR EL DESCUENTO SI PAGA PUNTUAL
					int fecVencimiento=0;
					if(tipo_fec_venc!=null){
						if(tipo_fec_venc.equals(Constante.TIPO_FEC_VEN_FIN)){
							System.out.println("3.1");
							System.out.println("3.1-->" + pagosList.get(i).get("id_mat"));
							System.out.println("3.1---->" + mes);
							if(!mes.equals(0)) {
								primerVencimiento = pagosService.getFecVencimientoFin(Integer.parseInt(pagosList.get(i).get("id_mat").toString()), 3,"MEN");
								fecVencimiento = pagosService.getFecVencimientoFin(Integer.parseInt(pagosList.get(i).get("id_mat").toString()), mes,"MEN");
							} else {
								fecVencimiento = pagosService.getFecVencimientoFin(Integer.parseInt(pagosList.get(i).get("id_mat").toString()), mes,"MAT");
							}
								
							//else
							//	primerVencimiento = pagosService.getFecVencimientoFin(Integer.parseInt(pagosList.get(i).get("id_mat").toString()), 3,"MEN");
								
							//fecVencimiento = pagosService.getFecVencimientoFin(Integer.parseInt(pagosList.get(i).get("id_mat").toString()), mes.toString());
						} else if(tipo_fec_venc.equals(Constante.TIPO_FEC_VEN_DIA)){
							

							fecVencimiento = getFecVencimiento(Integer.parseInt(anio_ult_mat.getNom()),mes,dia_mora);
						}
					} else{
						if(dia_mora!=null){
							System.out.println("3.2");

							fecVencimiento =  getFecVencimiento(Integer.parseInt(anio_ult_mat.getNom()),mes,dia_mora);
						} 
					}
					
					System.out.println("3");

					//int fecVencimiento =  getFecVencimiento(anio, mes, dia_mora);
					BigDecimal des_pag_adelantado= new BigDecimal(0);
					BigDecimal des_beca= new BigDecimal(0);
					System.out.println("desc_pago_ade->" + descuento.getDesc_pago_adelantado());
					if(descuento.getDesc_pago_adelantado()!=null && tarifa==null )//MV 10/05 - AGREGU�  && tarifa==null
						des_pag_adelantado=descuento.getDesc_pago_adelantado();
					if(descuento.getDesc_beca()!=null) {
						des_beca=descuento.getDesc_beca();
					}
					
					//if(descuento.getDesc_hermano()!=null && descuento.getDesc_pago_adelantado()!=null && descuento.getDesc_personalizado()!=null && descuento.getDesc_pronto_pago()!=null){//si el descuento es diferente de nulo y cero entonces se procede a los c�lculos de descuento, caso contrario no
					
					if((descuento.getDesc_hermano()!=null ) && //&& descuento.getDesc_hermano().intValue()==0
						 (descuento.getDesc_pago_adelantado()!=null ) && 
						 (descuento.getDesc_personalizado()!=null )  && 
						 (descuento.getDesc_pronto_pago()!=null )){//si el descuento es diferente de nulo y cero entonces se procede a los c�lculos de descuento, caso contrario no
					
						
						
						if(descuento.getDesc_hermano().compareTo(new BigDecimal(0))!=0 && descuento.getDesc_pago_adelantado().compareTo(new BigDecimal(0))!=0 && descuento.getDesc_personalizado().compareTo(new BigDecimal(0))!=0 && descuento.getDesc_pronto_pago().compareTo(new BigDecimal(0))!=0){
							if (mensualidadBcoDescObj!=null && fecActual<=fecVencimiento ){
								pago_final= new BigDecimal(mensualidadBcoDescObj.toString()).subtract(des_pag_adelantado).subtract(des_beca);
							}else{
								pago_final=pago_mes.subtract(des_pag_adelantado);;

								//se aplica descuentos si estan en fecha
								if(fecActual<=fecVencimiento){
									//Aplicamos el descuentopagosList.get(i).
									if(cant_hermanos>1 && id_niv != Constante.NIVEL_INICIAL) {
										if(!des_beca.equals(new BigDecimal(0))) {
											pago_final=(pago_mes.subtract(des_beca)).subtract(des_pag_adelantado);
										} else {
											pago_final=(pago_mes.subtract(desc_hermano)).subtract(des_pag_adelantado);
										}
										
									}else { 
										pago_final=pago_mes.subtract(des_pag_adelantado).subtract(des_beca);
									}	
									//Actualizamos el monto de pago con el descuento si lo hubo


									pago_final = pago_final.subtract(desc_banco).subtract(des_pag_adelantado).subtract(des_beca);


								} else {
									pago_final=pago_mes.subtract(des_beca);
								}

								//Para el a�o 2020 dejo de funcionar
								/*if(mes==12 && !existioPrimerVencimiento) //falta validar la fecha
									pago_final = pago_final.divide(new BigDecimal(2));*/
							}
						} else{
							if(tarifa!=null){
								if(mensualidadBcoDescObj!=null){
									BigDecimal monto_banco=new BigDecimal(mensualidadBcoDescObj.toString());
									if(tarifa.getBigDecimal("monto").compareTo(monto_banco)<=0){ //Si mi tarifa es menor al monto, pongo la tarifa del monto
										pago_final= pago_mes.subtract(des_pag_adelantado);
										//se aplica descuentos si estan en fecha
										if(fecActual<=fecVencimiento){
											//Aplicamos el descuentopagosList.get(i).
											if(cant_hermanos>1 && id_niv != Constante.NIVEL_INICIAL) {
												if(!des_beca.equals(new BigDecimal(0))) {
													pago_final=(pago_mes.subtract(des_beca)).subtract(des_pag_adelantado);
												} else {
													pago_final=(pago_mes.subtract(desc_hermano)).subtract(des_pag_adelantado);
												}
											} else {
												pago_final=pago_mes.subtract(des_beca);
											}
												
											//Actualizamos el monto de pago con el descuento si lo hubo

											pago_final = pago_final.subtract(desc_banco).subtract(des_pag_adelantado).subtract(des_beca);


										} else {
											pago_final=pago_mes.subtract(des_beca);
										}
									} else if(tarifa.getBigDecimal("monto").compareTo(monto_banco)>0){//Si mi tarifa es mayor, se mantiene el del banco
										if (mensualidadBcoDescObj!=null && fecActual<=fecVencimiento ){
											pago_final= new BigDecimal(mensualidadBcoDescObj.toString()).subtract(des_pag_adelantado).subtract(des_beca);
										} else{
											pago_final=pago_mes.subtract(des_pag_adelantado).subtract(des_beca);
										}
									}
								} else{
									//pago_final=pago_mes.subtract(des_pag_adelantado);
									pago_final=pago_mes;
									
									if(fecActual<=fecVencimiento){
										//Aplicamos el descuentopagosList.get(i).
										if(cant_hermanos>1 && id_niv != Constante.NIVEL_INICIAL) {
											if(!des_beca.equals(new BigDecimal(0))) {
												pago_final=(pago_mes.subtract(des_beca)).subtract(des_pag_adelantado);
											} else {
												pago_final=(pago_mes.subtract(desc_hermano)).subtract(des_pag_adelantado);
											}
										} else {
											pago_final = pago_final.subtract(desc_banco).subtract(des_pag_adelantado).subtract(des_beca);
										}
											//pago_final=pago_mes;
										//Actualizamos el monto de pago con el descuento si lo hubo
									} else {
										pago_final=pago_mes.subtract(des_beca).subtract(des_pag_adelantado);
									}
								}
								
							} else{
								if (mensualidadBcoDescObj!=null && fecActual<=fecVencimiento ){
									pago_final= new BigDecimal(mensualidadBcoDescObj.toString()).subtract(des_pag_adelantado).subtract(des_beca);
								} else{
									pago_final=pago_mes.subtract(des_pag_adelantado).subtract(des_beca);
								}
							}
						}
					} else{
						if(tarifa!=null){
							if(mensualidadBcoDescObj!=null){
								BigDecimal monto_banco=new BigDecimal(mensualidadBcoDescObj.toString());
								if(tarifa.getBigDecimal("monto").compareTo(monto_banco)<=0){ //Si mi tarifa es menor al monto, pongo la tarifa del monto
									pago_final=pago_mes.subtract(des_pag_adelantado);
									//se aplica descuentos si estan en fecha
									if(fecActual<=fecVencimiento){
										//Aplicamos el descuentopagosList.get(i).
										if(cant_hermanos>1 && id_niv != Constante.NIVEL_INICIAL) {
											if(!des_beca.equals(new BigDecimal(0))) { 
												pago_final=(pago_mes.subtract(des_beca)).subtract(des_pag_adelantado);
											} else {
												pago_final=(pago_mes.subtract(desc_hermano)).subtract(des_pag_adelantado);
											}
										} else {
											pago_final=pago_mes;
										}		
										//Actualizamos el monto de pago con el descuento si lo hubo


										pago_final = pago_final.subtract(desc_banco).subtract(des_pag_adelantado).subtract(des_beca);


									} else {
										pago_final=pago_mes.subtract(des_beca);
									}
								} else if(tarifa.getBigDecimal("monto").compareTo(monto_banco)>0){//Si mi tarifa es mayor, se mantiene el del banco
									if (mensualidadBcoDescObj!=null && fecActual<=fecVencimiento ){
										pago_final= new BigDecimal(mensualidadBcoDescObj.toString()).subtract(des_pag_adelantado).subtract(des_beca);
									} else{
										pago_final=pago_mes.subtract(des_pag_adelantado).subtract(des_beca);
									}
								}
							} else{
								pago_final=pago_mes;
								
								if(fecActual<=fecVencimiento){
									//Aplicamos el descuentopagosList.get(i).
									if(cant_hermanos>1 && id_niv != Constante.NIVEL_INICIAL) {
										if(!des_beca.equals(new BigDecimal(0))) {
											pago_final=(pago_mes.subtract(des_beca)).subtract(des_pag_adelantado);
										} else {
											pago_final=(pago_mes.subtract(desc_hermano)).subtract(des_pag_adelantado);
										}
									} else {
										pago_final = pago_final.subtract(desc_banco).subtract(des_pag_adelantado).subtract(des_beca);
									}
										//pago_final=pago_mes;
									//Actualizamos el monto de pago con el descuento si lo hubo
								} else {
									pago_final=pago_mes.subtract(des_beca);
								}
								
							}
						} else if (aulaModalidadDet!=null){
							//if (mensualidad_modalidad_mes!=null && fecActual<=fecVencimiento ){
							if (mensualidad_modalidad_mes!=null ){
								//pago_final= pago_mes.subtract(des_pag_adelantado).subtract(des_beca);
							//}else{
								if(!des_beca.equals(new BigDecimal(0))) {
									//Buscamos el tipo de descuento
									Integer id_bec=Integer.parseInt(pagosList.get(i).get("id_bec").toString());
									String val= becaDAO.get(id_bec).getVal();
									BigDecimal descuento_beca=(new BigDecimal(val).divide(new BigDecimal(100))).multiply(pago_mes).setScale(2, RoundingMode.HALF_DOWN);;
									pago_final=pago_mes.subtract(des_pag_adelantado).subtract(descuento_beca);
								} else {
									if(fecActual<=fecVencimiento){
										//Aplicamos el descuentopagosList.get(i).
										if(cant_hermanos>1 && id_niv != Constante.NIVEL_INICIAL) {
											if(!des_beca.equals(new BigDecimal(0))) {
												pago_final=(pago_mes.subtract(des_beca)).subtract(des_pag_adelantado);
											} else {
												pago_final=(pago_mes.subtract(desc_hermano)).subtract(des_pag_adelantado);
											}	
										} else {
											//pago_final=pago_mes;
											//Actualizamos el monto de pago con el descuento si lo hubo
											pago_final = pago_mes.subtract(desc_banco).subtract(des_pag_adelantado);
										}
										
									} else {
										pago_final=pago_mes.subtract(des_beca);
									}
								}
								
								//se aplica descuentos si estan en fecha
								
							}	
							//}						
							/*if(mes==12 && !existioPrimerVencimiento) //falta validar la fecha
								pago_final = pago_final.divide(new BigDecimal(2));*/ //ya no usado para el decuento anual	
						} else{
							if (mensualidadBcoDescObj!=null && fecActual<=fecVencimiento ){
								pago_final= new BigDecimal(mensualidadBcoDescObj.toString()).subtract(des_pag_adelantado).subtract(des_beca);
							}else{
								pago_final=pago_mes.subtract(des_pag_adelantado).subtract(des_beca);

								//se aplica descuentos si estan en fecha
								if(fecActual<=fecVencimiento){
									//Aplicamos el descuentopagosList.get(i).
									if(cant_hermanos>1 && id_niv != Constante.NIVEL_INICIAL) {
										if(!des_beca.equals(new BigDecimal(0))) {
											pago_final=(pago_mes.subtract(des_beca)).subtract(des_pag_adelantado);
										} else {
											pago_final=(pago_mes.subtract(desc_hermano)).subtract(des_pag_adelantado);
										}	
									} else {
										//pago_final=pago_mes;
										//Actualizamos el monto de pago con el descuento si lo hubo
										pago_final = pago_final.subtract(desc_banco);
									}
									
								} else {
									if(!des_beca.equals(new BigDecimal(0))) {
										pago_final=pago_mes.subtract(des_beca);
									} else {
										pago_final=pago_mes;
									}
									
								}
							}						
							/*if(mes==12 && !existioPrimerVencimiento) //falta validar la fecha
								pago_final = pago_final.divide(new BigDecimal(2));*/ //ya no usado para el decuento anual	
						}
					}
					
					

					if (id_banco.intValue() == EnumBanco.FINANCIERO.getValue()){

						if (Integer.parseInt(pago_final.toString().replace(".","").toString())!=0 ){
							sb.append("CO\t549937730\t");
							sb.append(pagosList.get(i).get("nro_doc"));
							sb.append("\tPEN\t");
							sb.append(pago_final.toString().replace(".",""));
							sb.append("\tREC\t0035\tC\t");
							sb.append(pagosList.get(i).get("nro_doc"));
							sb.append("\t");
							sb.append(StringUtil.replaceTilde(pagosList.get(i).get("ape_pat").toString()));
							sb.append(" ");
							sb.append(StringUtil.replaceTilde(pagosList.get(i).get("ape_mat").toString()));
							sb.append(" ");
							sb.append(StringUtil.replaceTilde(pagosList.get(i).get("nom").toString()));
							if(pagosList.get(i).get("mes").equals("0"))
								sb.append("\tCONCEPTO DE ");
							else
							sb.append("\tMENSUALIDAD ");
							sb.append(pagosList.get(i).get("mes"));
							sb.append("\t");
							sb.append(pagosList.get(i).get("id"));
							sb.append("\t07" + String.valueOf(fecVencimiento).substring(4, 6) + String.valueOf(fecVencimiento).substring(0, 4));//TODO PARAMETRIZAR
							sb.append("\n");
						}

					}else if(id_banco.intValue() == EnumBanco.CONTINENTAL.getValue()){
						//if (Integer.parseInt(pago_final.toString().replace(".","").toString())!=0 ){
							total++;
							String cliente = StringUtil.replaceTilde(pagosList.get(i).get("ape_pat").toString()).replace("Ñ", "N") + " " + StringUtil.replaceTilde(pagosList.get(i).get("ape_mat").toString()).replace("Ñ", "N") + " " + StringUtil.replaceTilde(pagosList.get(i).get("nom").toString()).replace("Ñ", "N");
							String concepto=null;
							if(pagosList.get(i).get("mes").equals("MATRICULA"))
								concepto= "CONCEPTO DE "+pagosList.get(i).get("mes");
							else if(pagosList.get(i).get("mes").equals("CUOTA DE INGRESO"))
								concepto= ""+pagosList.get(i).get("mes");
							else
								concepto = "MENSUALIDAD " + pagosList.get(i).get("mes");
							//String concepto = "MENSUALIDAD " + pagosList.get(i).get("mes");
							sb.append("\n02");//Indicador de detalle 02 (*)
							sb.append(StringUtil.rellenaVacio(cliente,30)); //nombre del cliente
							sb.append(StringUtil.rellenaCaracterIzq(pagosList.get(i).get("nro_doc").toString(),11,"0"));
							sb.append(StringUtil.rellenaVacio(concepto, 24));
							sb.append("COD-" + StringUtil.rellenaCaracterIzq(pagosList.get(i).get("id").toString(), 9, "0"));
							sb.append(fecVencimiento);
							sb.append("20301231");
							sb.append(StringUtil.rellenaCaracterIzq(String.valueOf(mes),2,"0"));
							sb.append(StringUtil.rellenaCaracterIzq(pago_final.toString().replace(".",""),15,"0")); //Se colocara 13 enteros y 2 decimales sin punto ni comas
							sb.append(StringUtil.rellenaCaracterIzq(pago_final.toString().replace(".",""),15,"0")); //Se colocara 13 enteros y 2 decimales sin punto ni comas
							sb.append(StringUtil.repiteCaracter(32, "0"));
							sb.append(StringUtil.repiteCaracter(16*8, "0"));
							sb.append(StringUtil.repiteCaracter(72, " "));
							
							pagoTotal = pagoTotal.add(pago_final);
						//}
					} else if(id_banco.intValue() == EnumBanco.BCP.getValue()){
						//if (Integer.parseInt(pago_final.toString().replace(".","").toString())!=0 ){
						total++;
						String cliente = StringUtil.replaceTilde(pagosList.get(i).get("ape_pat").toString()).replace("Ñ", "N") + " " + StringUtil.replaceTilde(pagosList.get(i).get("ape_mat").toString()).replace("Ñ", "N") + " " + StringUtil.replaceTilde(pagosList.get(i).get("nom").toString()).replace("Ñ", "N");
						String concepto=null;
						if(pagosList.get(i).get("mes").equals("MATRICULA"))
							concepto= "CONCEPTODE"+pagosList.get(i).get("mes");
						else if(pagosList.get(i).get("mes").equals("CUOTA DE INGRESO"))
							//concepto= "CUOTADEINGRESO"+pagosList.get(i).get("mes");
							concepto= "CUOTADEINGRESO";
						else
							concepto = "PENSION" + pagosList.get(i).get("mes");
						//String concepto = "MENSUALIDAD " + pagosList.get(i).get("mes");
						sb.append("\nDD");//Tipo de Registro
						sb.append("375");//Codigo de la Cuenta
						sb.append("0");//Codigo de la Moneda
						sb.append("8739262");//Numero de Cuenta de la Empresa
						sb.append(StringUtil.rellenaCaracterIzq(pagosList.get(i).get("nro_doc").toString(),14,"0"));
						sb.append(StringUtil.rellenaVacio(cliente, 40));
						//sb.append(StringUtil.rellenaVacio(cliente,30)); //nombre del cliente
						sb.append(StringUtil.rellenaVacio(pagosList.get(i).get("id").toString(), 30));//Codigo de indentificacion del depositante
						sb.append(fecActual);
						sb.append(fecVencimiento);
						sb.append(StringUtil.rellenaCaracterIzq(pago_final.toString().replace(".",""),15,"0"));
						sb.append(StringUtil.repiteCaracter(15, "0"));
						sb.append(StringUtil.rellenaCaracterIzq(pago_final.toString().replace(".",""),9,"0"));
						sb.append(StringUtil.rellenaVacioIzquierda(concepto, 21));
						sb.append(StringUtil.rellenaCaracterIzq(pagosList.get(i).get("nro_doc").toString(),16,"0"));
						sb.append(StringUtil.repiteCaracter(61, " "));
						pagoTotal = pagoTotal.add(pago_final);
					//}
				}
					

				}
				
				StringBuilder sb2 = new StringBuilder("");
				//CABECERA DEL ARCHIVO
				String cabecera = obtenerCabecera(id_banco,total,pagoTotal);
				sb2.append(cabecera);
				sb2.append(sb);
				//inicio - pie de archivo
				if(id_banco.intValue() == EnumBanco.CONTINENTAL.getValue()){
					sb2.append("\n03");//Indicador de detalle�02 (*)
					//sb.append(StringUtil.rellenaCaracterIzq(Integer.toString(pagosList.size()),9,"0"));
					sb2.append(StringUtil.rellenaCaracterIzq(Integer.toString(total),9,"0"));
					sb2.append(StringUtil.rellenaCaracterIzq(pagoTotal.toString().replace(".",""),18,"0")); //18
					sb2.append(StringUtil.rellenaCaracterIzq(pagoTotal.toString().replace(".",""),18,"0")); //18
					sb2.append(StringUtil.repiteCaracter(18, "0"));
					sb2.append(StringUtil.repiteCaracter(295, " "));

				}
				
				
				
				

				/*
				InputStream is=IOUtils.toInputStream(sb.toString());
				response.setContentType("text/plain");
				response.setCharacterEncoding("windows-1252");
				response.setHeader("Content-Type", "text/xml; charset=Windows-1252");

				IOUtils.copy(is, response.getOutputStream());
				*/

				response.setContentType("text/plain");
				response.setCharacterEncoding("windows-1252");

				PrintWriter out = response.getWriter();
				  out.append(sb2.toString());
				  out.close();
				  
			} catch (IOException e) {

				e.printStackTrace();

			}

		}
		
		@Transactional
		@RequestMapping(value = "/generarContratoMatriculaWeb")
		@ResponseBody
		public void descargarContratoMatriculaWeb(HttpServletRequest request, HttpServletResponse response,Integer id_per, Integer id_anio) throws Exception {
			// AjaxResponseBody result = new AjaxResponseBody();
			// Integer id_anio=2;
			
			//List<Row> cabecera_contrato= matriculaDAO.cabeceraContrato(id_fam, id_anio);
			
			//buscar si contrato ya existe
			/*Param param = new Param();
			param.put("num_cont", cabecera_contrato.get(0).getString("num_cont"));
			List<Matricula> matriculasContrato = matriculaDAO.listByParams(param, new String[]{"id desc"});*/
			//List<Row> matriculasLocalApod= matriculaDAO.matriculadosxLocalyApoderadoValidadas(id_suc, id_fam,id_anio);
			Familiar familiar = familiarDAO.getByParams(new Param("id_per",id_per));
			List<Row> matriculasLocalApod= matriculaDAO.matriculadosWebxApoderado(familiar.getId(), id_anio);
			String num_adenda=null;
			for (Row row : matriculasLocalApod) {
				if(row.getString("num_adenda")!=null){
					num_adenda=row.getString("num_adenda");
					break;
				}
			}
			String alumnos_concat="";
			String alumnos_adenda_concat="";
			BigDecimal monto_total=new BigDecimal(0);
		
			Parametro parametro = parametroDAO.getByParams(new Param("nom", "RUTA_PLANTILLA"));
			Map<String, String> map = new HashMap<String, String>();
		
			XWPFDocument src1Document = new XWPFDocument();
			File file = null;
			FileInputStream fis = null;
			ByteArrayOutputStream bos = null;
			Integer annio;
			String mesactual = "";
			Calendar c1 = Calendar.getInstance();
			annio = c1.get(Calendar.YEAR);
			String MES[] = { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre",
					"Octubre", "Noviembre", "Diciembre" };
			mesactual = MES[c1.get(Calendar.MONTH)];
			Integer dia_mes =c1.get(Calendar.DAY_OF_MONTH);
			Row cabecera_contrato=matriculaDAO.cabeceraContrato(familiar.getId(),id_anio).get(0);
			String num_cont=cabecera_contrato.getString("num_cont");
			map.put("NUM_CONT", (cabecera_contrato.get("num_cont").toString()));
			map.put("datosfamiliar", (cabecera_contrato.get("familiar").toString()));
			map.put("NRO_DOC", (cabecera_contrato.get("nro_doc").toString()));
			//alumno.getNom()==null ? alumno.getNom() : alumno.getNom().toUpperCase()
			map.put("famdirec", cabecera_contrato.get("direccion")==null ? "" : cabecera_contrato.get("direccion").toString());
			map.put("DISTRITO_FAM", (cabecera_contrato.get("distrito")==null ? "" : cabecera_contrato.get("distrito").toString()));
			map.put("PROVINCIA_FAM", (cabecera_contrato.get("provincia")==null ? "" : cabecera_contrato.get("provincia").toString()));
			map.put("DEPARTAMENTO_FAM", (cabecera_contrato.get("departamento")==null ? "" : cabecera_contrato.get("departamento").toString()));
			map.put("CELULAR_FAM", (cabecera_contrato.get("cel")==null ? "" : cabecera_contrato.get("cel").toString()));
			map.put("EMAIL_FAM", (cabecera_contrato.get("corr")==null ? "" : cabecera_contrato.get("corr").toString()));
			map.put("NOMBREDIA", dia_mes.toString());
			map.put("NOMBRE_MES", mesactual);
			map.put("ANIO", annio.toString());
			List<Row> matriculados = matriculaDAO.matriculadosxContratoparaWeb(num_cont);
			Integer cant_hijos = matriculados.size();
			map.put("cant_hijos", cant_hijos.toString());
			for (Row row : matriculados) {
				if(row.getString("num_adenda")==null)
				//alumnos_concat = alumnos_concat + row.getString("alumno") + " al "+ row.getString("grado")+" "+ row.getString("seccion")+ " de Educaci�n "+row.getString("nivel")+", ";
				alumnos_concat = alumnos_concat + row.getString("alumno") + " al "+ row.getString("grado")+" de Educación "+row.getString("nivel")+", ";
				BigDecimal monto = confMensualidadDAO.getByParams(new Param("id_per", row.getInt("id_per"))).getMonto();
				monto_total = monto_total.add(monto);
			}
			map.put("monto_total", monto_total.toString());

			for (Row row : matriculasLocalApod) {
				if(row.getString("num_adenda")!=null){
					alumnos_adenda_concat= alumnos_adenda_concat + row.getString("alumno") + " al "+ row.getString("grado")+" "+ row.getString("seccion")+ " de Educaci�n "+row.getString("nivel")+", ";
				}
			}
			
			String nuevo = null;
			String fileName = null;
			if(num_adenda!=null){
				map.put("NUM_ADENDA", num_adenda);
				map.put("ALUCONTRATO", alumnos_adenda_concat);
				nuevo = DocxUtil.generate(parametro.getVal(), "Adenda.docx", map);
				fileName = URLEncoder.encode("Adenda.docx", "UTF-8");
			} else{
				map.put("ALUCONTRATO", alumnos_concat);
				/*if(id_suc==2){
					nuevo = DocxUtil.generate(parametro.getVal(), "Contrato_local1.docx", map);
				} else if(id_suc==3){
					nuevo = DocxUtil.generate(parametro.getVal(), "Contrato_local2.docx", map);
				} else if(id_suc==4){
					nuevo = DocxUtil.generate(parametro.getVal(), "Contrato_local3.docx", map);
				}*/
				fileName = URLEncoder.encode("Contrato.docx", "UTF-8");
				nuevo = DocxUtil.generate(parametro.getVal(), "Contrato_local1.docx", map);
			}
			
			//String fecha_actual = c1.get(Calendar.DAY_OF_MONTH) + " de " + mesactual + " del " + c1.get(Calendar.YEAR);
			// OutputStream out = new
			// FileOutputStream("D://plantillas//CartaFinal.docx");//prueba nro 2,
			// no se si funcione.. porsiacaso proibal
			///home/aeedupeh/public_html/plantillas/CartaFinal.docx
			//OutputStream out = new FileOutputStream("D://plantillas//Contrato_local1.docx");
			//src1Document.write(out);
			fileName = URLDecoder.decode(fileName, "ISO8859_1");
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-disposition", "attachment; filename=" + fileName);

			file = new File(nuevo);
			fis = new FileInputStream(file);
			bos = new ByteArrayOutputStream();
			int readNum;
			byte[] buf = new byte[1024];
			try {

				for (; (readNum = fis.read(buf)) != -1;) {
					bos.write(buf, 0, readNum);
				}
			} catch (IOException ex) {

			}
			ServletOutputStream out1 = response.getOutputStream();
			bos.writeTo(out1);
			// fin* aca se muestra el archivo fisico al naegador
		}
		
		/**
		 * Obtiene los datos de la cabecera del archivo segun el banco
		 * @param id_banco
		 * @return
		 */
		private String obtenerCabecera(int id_banco, int total, BigDecimal pagoTotal){
			String lineas = "";
			String linea1 = "";
			if (id_banco == EnumBanco.CONTINENTAL.getValue()){
				linea1 = "01";//indciador cabecera
				linea1 += "20531084587"; //ruc
				linea1 += "000"; //nro de la clase
				linea1 += "PEN"; //MONEDA
				linea1 += FechaUtil.toString(new Date(), "YYYYMMdd"); //FECHA DE FACTURACION
				linea1 += "000";//VERSION
				linea1 += StringUtil.espacioBlanco(7);//7 ESPACIOS EN BLANCO
				linea1 += "T";//T ( ACTUALIZACION TOTAL)
				linea1 += StringUtil.espacioBlanco(322);//322 ESPACIOS EN BLANCO
			} 
			
			if (id_banco == EnumBanco.BCP.getValue()){
				linea1 = "CC";//indciador cabecera
				linea1 += "375"; //Codigo de la Sucursal
				linea1 += "0"; //Codigo de la Moneda
				linea1 += "8739262"; //Numero de Cuenta de la empresa (Aqui dice 7)
				linea1 += "C"; //Tipo de Validacion
				linea1 += StringUtil.rellenaVacio("ASOCIACION EDUCATIVA LUZ Y CIENCIA", 40);
				linea1 += FechaUtil.toString(new Date(), "YYYYMMdd"); //FECHA DE FACTURACION
				linea1 += StringUtil.rellenaCaracterIzq(Integer.toString(total),9,"0");
				linea1 += StringUtil.rellenaCaracterIzq(pagoTotal.toString().replace(".",""),15,"0"); //Duda sobre dos decimales
				linea1 += "R"; //Codigo Interno BCP
				linea1 += StringUtil.espacioBlanco(6);
				linea1 += StringUtil.espacioBlanco(157);
				//linea1 += "";//Fille no se q es (libre)
			} 
			
			lineas += linea1;
			return lineas;
		}
		
		/**
		 * 
		 * @param mes
		 * @return
		 */
		private int getFecVencimiento(int anio, int mes, int dia) throws Exception{
			
			int anioSiguiente;
			int mesSiguiente;
			
			if (mes==12){
				anioSiguiente = anio + 1;
				mesSiguiente = 1;
			}else{
				anioSiguiente = anio;
				mesSiguiente = mes+1;
			}
			
			String fecVenc = anioSiguiente + String.format("%02d", mesSiguiente) + String.format("%02d", dia);//TODO PARAMETRIZAR FECHA
			
			
			return Integer.parseInt(fecVenc);
			
		}
		
		/**
		 * Genera pdf en el servidor
		 * @param response
		 * @param id_fmo
		 * @return
		 */
		@RequestMapping(value = "/pdf/boleta/{id_fmo}/{id_alu}", method = RequestMethod.GET)
		@ResponseBody
		public AjaxResponseBody imprimirBoleta(HttpServletResponse response, @PathVariable Integer id_fmo, @PathVariable Integer id_alu){
			AjaxResponseBody result = new AjaxResponseBody();
			
			try{
			
				NotaCredito nota_credito=notaCreditoDAO.getByParams(new Param("id_fmo_nc",id_fmo));
				Impresion impresion = facturacionService.getImpresion(id_fmo,id_alu);
				String pdf=null;
				if(nota_credito!=null) {
					 pdf =generatePDFNC(impresion); 
				} else {
					pdf =generatePDF(impresion); 
				}
				
				
				result.setResult(impresion);
				File initialFile = new File(pdf);
			    InputStream is = FileUtils.openInputStream(initialFile);
			    	
				
				response.setContentType("application/pdf");
				response.addHeader("Content-Disposition", "attachment; filename=boleta" + id_fmo + ".pdf");

				IOUtils.copy(is, response.getOutputStream());
				
			} catch (Exception e) {
				result.setException(e);
			}
			
			
			return result;

		}
		
		@RequestMapping(value = "/pdf/contrato/{num_cont}/{familia}", method = RequestMethod.GET)
		@ResponseBody
		public AjaxResponseBody descargarContratoEnviado(HttpServletResponse response, @PathVariable String num_cont, @PathVariable String familia){
			AjaxResponseBody result = new AjaxResponseBody();
			
			try{
				String fileName = "ContratoAE_"+num_cont+"_"+familia+".pdf";
				downloadLocal(response, fileName,num_cont, familia );
				
				
			} catch (Exception e) {
				result.setException(e);
			}
			
			
			return result;

		}
		
		@RequestMapping(value = "/pdf/declaracion/{num_cont}/{familia}", method = RequestMethod.GET)
		@ResponseBody
		public AjaxResponseBody descargarDeclaracionEnviado(HttpServletResponse response, @PathVariable String num_cont, @PathVariable String familia){
			AjaxResponseBody result = new AjaxResponseBody();
			
			try{
				String fileName = "DeclaracionAE_"+num_cont+"_"+familia+".pdf";
				downloadDeclaracion(response, fileName,num_cont, familia );
				
				
			} catch (Exception e) {
				result.setException(e);
			}
			
			
			return result;

		}
		
		@RequestMapping(value = "/pdf/protocolo/{num_cont}/{familia}", method = RequestMethod.GET)
		@ResponseBody
		public AjaxResponseBody descargarProtocoloEnviado(HttpServletResponse response, @PathVariable String num_cont, @PathVariable String familia){
			AjaxResponseBody result = new AjaxResponseBody();
			
			try{
				String fileName = "ProtocoloAE_"+num_cont+"_"+familia+".pdf";
				downloadProtocolo(response, fileName,num_cont, familia );
				
				
			} catch (Exception e) {
				result.setException(e);
			}
			
			
			return result;

		}
		
		public void downloadLocal(HttpServletResponse response, String fileName, String contrato, String familia) throws Exception {
		       // leer en la secuencia
			   //String rutaCarpeta= "c:\\contratos\\";
			   //Para el servidor
			   String rutaCarpeta= "//home//aeedupeh//public_html//plantillas//tmp//contratos//";
			   			   
		       InputStream inStream = new FileInputStream (rutaCarpeta+fileName); // Ruta de almacenamiento de archivos
		       // Establecer el formato de salida
		       
		       //String name = URLEncoder.encode("Contrato" + contrato + ".docx", "UTF-8");
				//name = URLDecoder.decode(name, "ISO8859_1");
		       
		       //InputStream is = FileUtils.openInputStream(initialFile);
		    	
				
				response.setContentType("application/pdf");
				response.addHeader("Content-Disposition", "attachment; filename=Contrato" + contrato + "_"+familia+".pdf");

				IOUtils.copy(inStream, response.getOutputStream());
		       /* response.reset();
		        response.setContentType("application/pdf");
		        response.addHeader("Content-Disposition", "attachment; filename=\"" + contrato + "\"");
		        // Recorre los datos en la secuencia
		        byte[] b = new byte[1024];
		        int len;
		        try {
		            while ((len = inStream.read(b)) > 0)
		                response.getOutputStream().write(b, 0, len);
		            inStream.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }*/
		}
		
		public void downloadDeclaracion(HttpServletResponse response, String fileName, String contrato, String familia) throws Exception {
		       // leer en la secuencia
			  // String rutaCarpeta= "c:\\declaraciones\\";
			   //Para el servidor
			   String rutaCarpeta= "//home//aeedupeh//public_html//plantillas//tmp//declaraciones//";
			   			   
		       InputStream inStream = new FileInputStream (rutaCarpeta+fileName); // Ruta de almacenamiento de archivos
		       // Establecer el formato de salida
		       
		       //String name = URLEncoder.encode("Contrato" + contrato + ".docx", "UTF-8");
				//name = URLDecoder.decode(name, "ISO8859_1");
		       
		       //InputStream is = FileUtils.openInputStream(initialFile);
		    	
				
				response.setContentType("application/pdf");
				response.addHeader("Content-Disposition", "attachment; filename=Declaracion" + contrato + "_"+familia+".pdf");

				IOUtils.copy(inStream, response.getOutputStream());
		       /* response.reset();
		        response.setContentType("application/pdf");
		        response.addHeader("Content-Disposition", "attachment; filename=\"" + contrato + "\"");
		        // Recorre los datos en la secuencia
		        byte[] b = new byte[1024];
		        int len;
		        try {
		            while ((len = inStream.read(b)) > 0)
		                response.getOutputStream().write(b, 0, len);
		            inStream.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }*/
		}
		
		public void downloadProtocolo(HttpServletResponse response, String fileName, String contrato, String familia) throws Exception {
		       // leer en la secuencia
			  // String rutaCarpeta= "c:\\protocolos\\";
			   //Para el servidor
			   String rutaCarpeta= "//home//aeedupeh//public_html//plantillas//tmp//protocolos//";
			   			   
		       InputStream inStream = new FileInputStream (rutaCarpeta+fileName); // Ruta de almacenamiento de archivos
		       // Establecer el formato de salida
		       
		       //String name = URLEncoder.encode("Contrato" + contrato + ".docx", "UTF-8");
				//name = URLDecoder.decode(name, "ISO8859_1");
		       
		       //InputStream is = FileUtils.openInputStream(initialFile);
		    	
				
				response.setContentType("application/pdf");
				response.addHeader("Content-Disposition", "attachment; filename=Protocolo" + contrato + "_"+familia+".pdf");

				IOUtils.copy(inStream, response.getOutputStream());
		       /* response.reset();
		        response.setContentType("application/pdf");
		        response.addHeader("Content-Disposition", "attachment; filename=\"" + contrato + "\"");
		        // Recorre los datos en la secuencia
		        byte[] b = new byte[1024];
		        int len;
		        try {
		            while ((len = inStream.read(b)) > 0)
		                response.getOutputStream().write(b, 0, len);
		            inStream.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }*/
		}
		
		public  void mayyyyin(String fileName) {
			try {
				// Url con la foto
				String rutaCarpeta= "C:\\contratos\\";
				URL url = new URL(rutaCarpeta+fileName);

				// establecemos conexion
				URLConnection urlCon = url.openConnection();

				// Sacamos por pantalla el tipo de fichero
				System.out.println(urlCon.getContentType());

				// Se obtiene el inputStream de la foto web y se abre el fichero
				// local.
				InputStream is = urlCon.getInputStream();
				FileOutputStream fos = new FileOutputStream("C:/"+fileName);

				// Lectura de la foto de la web y escritura en fichero local
				byte[] array = new byte[1000]; // buffer temporal de lectura.
				int leido = is.read(array);
				while (leido > 0) {
					fos.write(array, 0, leido);
					leido = is.read(array);
				}

				// cierre de conexion y fichero.
				is.close();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public String generatePDF(Impresion impresion)throws Exception{
			
			String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();

			//String rutacARPETA =  "D:/plantillas/";

			//InputStream inputStream = new FileInputStream("D:\\proyectos\\colegio\\boleta.jrxml");
			HashMap<String,Object> parameters = new HashMap<String,Object>();
			
			
			// boleta
			parameters.put("boleta_nombre", impresion.getCabecera().getNombreBoleta());
			parameters.put("boleta_nro", impresion.getCabecera().getNro());
			parameters.put("boleta_dia", impresion.getCabecera().getDia());
			parameters.put("boleta_hora", impresion.getCabecera().getHora());
			parameters.put("boleta_comercio", impresion.getCabecera().getComercio());
			parameters.put("boleta_local", impresion.getCabecera().getLocal());
			parameters.put("boleta_usuario", impresion.getCabecera().getUsuario());
			parameters.put("boleta_telefono",impresion.getCabecera().getTelefono());
			//parameters.put("boleta_codigo",1);

			//cliente
			parameters.put("cliente_nombre", impresion.getCliente().getNombre());
			parameters.put("cliente_direccion", impresion.getCliente().getDireccion());
			parameters.put("cliente_nro_doc", impresion.getCliente().getNro_doc());
			parameters.put("cliente_tip_doc", impresion.getCliente().getTip_doc());
			
			//LISTA DE ITEMS
			List<ImpresionItem> list = new ArrayList<ImpresionItem>();
			
			BigDecimal operacionGravada = new BigDecimal(0);
			BigDecimal operacionGratuita = new BigDecimal(0);
			BigDecimal operacionInafecta = new BigDecimal(0);
			BigDecimal descuento = new BigDecimal(0);
			BigDecimal igv = new BigDecimal(0);
			BigDecimal total = new BigDecimal(0);
			int nro = 0;
			for (ImpresionItem impresionItem : impresion.getItems()) {
				nro++;
				ImpresionItem itemBean = new ImpresionItem();
		 
				itemBean.setNro(nro);
				itemBean.setDescripcion(impresionItem.getDescripcion());
				itemBean.setCantidad(impresionItem.getCantidad());
				
				itemBean.setPrecioUnit(impresionItem.getPrecio());
				itemBean.setPrecio(impresionItem.getPrecio());
				itemBean.setTipoOperacion("OPE_INA");//OPERACION INAFECTA
				list.add(itemBean);
			
				if (!impresionItem.getDescuentos().isEmpty()){
					for (ImpresionDcto impDescuento : impresionItem.getDescuentos()) {
						//nro++;
						ImpresionItem itemBean1 = new ImpresionItem();
						
						itemBean1.setNro(0);
						itemBean1.setDescripcion(impDescuento.getDescripcion());
						itemBean1.setCantidad(1);
						
						itemBean1.setPrecioUnit(impDescuento.getDescuento());
						itemBean1.setPrecio(impDescuento.getDescuento().multiply(new BigDecimal(-1)));
						//itemBean.setTipoOperacion("OPE_INA");//OPERACION INAFECTA
						list.add(itemBean1);
					}
					
				}
				
				if (itemBean.getTipoOperacion().equals("OPE_INA")){
					operacionInafecta = operacionInafecta.add(itemBean.getPrecio());
				}else if (itemBean.getTipoOperacion().equals("OPE_GRAT"))
					operacionGratuita = operacionGratuita.add(itemBean.getPrecio());
				else if (itemBean.getTipoOperacion().equals("OPE_GRAV")){
					operacionGravada = operacionGravada.add(itemBean.getPrecio());
					//igv = igv.add(augend)
				}
				descuento = descuento.add(impresionItem.getDescuento());
				total = total.add(itemBean.getPrecio().subtract(impresionItem.getDescuento()));
				
			}
			
			//parameters.put("total_venta", "440.00");
				
			
			//resumen
			parameters.put("resumen_gravadas", NumberUtil.toString(operacionGravada) );
			parameters.put("resumen_gratuitas", NumberUtil.toString(operacionGratuita) );
			parameters.put("resumen_inafectas", NumberUtil.toString(operacionInafecta));
			parameters.put("resumen_descuento", NumberUtil.toString(descuento));
			parameters.put("resumen_igv", NumberUtil.toString(igv));
			parameters.put("resumen_monto_texto", NumberUtil.toTexto(total).toUpperCase() + " SOLES");
			parameters.put("resumen_total_venta", NumberUtil.toString(total));
			

			// RUC | TIPO DE DOCUMENTO | SERIE | NUMERO | MTO TOTAL IGV | MTO TOTAL DEL COMPROBANTE | FECHA DE EMISION | TIPO DE DOCUMENTO ADQUIRENTE | NUMERO DE DOCUMENTO ADQUIRENTE |
			String boleta_codigo = "20531084587|" + "03|" + impresion.getCabecera().getNro().replace("-", "|");
			boleta_codigo += igv+ "|";
			boleta_codigo += total + "|";
			boleta_codigo += impresion.getCabecera().getDia() + "|";
			boleta_codigo +=  "1|";
			boleta_codigo += impresion.getCliente().getNro_doc() + "|";
			parameters.put("boleta_codigo", boleta_codigo);

			//logger.debug("antes de llamar al jasper");
			 
			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list);

			InputStream resource = new FileInputStream(rutacARPETA + "/boleta.jasper");
			//logger.debug("llamo al jasper");
			
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(resource);
			
			//logger.debug("despues del loadObject");
			
			resource.close();
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);
			
			//logger.debug("despues de JasperFillManager.fillReport");
			
			String pdfGenerado =rutacARPETA + "/tmp/boleta" + (new Date()).toString().replaceAll(" ","").replaceAll(":", "") +".pdf";
			JasperExportManager.exportReportToPdfFile(jasperPrint, pdfGenerado);
			
			//logger.debug("despues de JasperExportManager.exportReportToPdfFile");
			
			return pdfGenerado;
		}
		
		public String generatePDFNC(Impresion impresion)throws Exception{
			
			String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();

			//String rutacARPETA =  "D:/plantillas/";

			//InputStream inputStream = new FileInputStream("D:\\proyectos\\colegio\\boleta.jrxml");
			HashMap<String,Object> parameters = new HashMap<String,Object>();
			
			
			// boleta
			parameters.put("boleta_nombre", impresion.getCabecera().getNombreBoleta());
			parameters.put("boleta_nro", impresion.getCabecera().getNro());
			parameters.put("boleta_dia", impresion.getCabecera().getDia());
			parameters.put("boleta_hora", impresion.getCabecera().getHora());
			parameters.put("boleta_comercio", impresion.getCabecera().getComercio());
			parameters.put("boleta_local", impresion.getCabecera().getLocal());
			parameters.put("boleta_usuario", impresion.getCabecera().getUsuario());
			parameters.put("boleta_telefono",impresion.getCabecera().getTelefono());
			parameters.put("boleta_ref_nro",impresion.getDocumentoReferencia().getNro_rec());
			//parameters.put("boleta_codigo",1);

			//cliente
			parameters.put("cliente_nombre", impresion.getCliente().getNombre());
			parameters.put("cliente_direccion", impresion.getCliente().getDireccion());
			parameters.put("cliente_nro_doc", impresion.getCliente().getNro_doc());
			parameters.put("cliente_tip_doc", impresion.getCliente().getTip_doc());
			
			//LISTA DE ITEMS
			List<ImpresionItem> list = new ArrayList<ImpresionItem>();
			
			BigDecimal operacionGravada = new BigDecimal(0);
			BigDecimal operacionGratuita = new BigDecimal(0);
			BigDecimal operacionInafecta = new BigDecimal(0);
			BigDecimal descuento = new BigDecimal(0);
			BigDecimal igv = new BigDecimal(0);
			BigDecimal total = new BigDecimal(0);
			int nro = 0;
			for (ImpresionItem impresionItem : impresion.getItems()) {
				nro++;
				ImpresionItem itemBean = new ImpresionItem();
		 
				itemBean.setNro(nro);
				itemBean.setDescripcion(impresionItem.getDescripcion());
				itemBean.setCantidad(impresionItem.getCantidad());
				
				itemBean.setPrecioUnit(impresionItem.getPrecio());
				itemBean.setPrecio(impresionItem.getPrecio());
				itemBean.setTipoOperacion("OPE_INA");//OPERACION INAFECTA
				list.add(itemBean);
			
				if (!impresionItem.getDescuentos().isEmpty()){
					for (ImpresionDcto impDescuento : impresionItem.getDescuentos()) {
						//nro++;
						ImpresionItem itemBean1 = new ImpresionItem();
						
						itemBean1.setNro(0);
						itemBean1.setDescripcion(impDescuento.getDescripcion());
						itemBean1.setCantidad(1);
						
						itemBean1.setPrecioUnit(impDescuento.getDescuento());
						itemBean1.setPrecio(impDescuento.getDescuento().multiply(new BigDecimal(-1)));
						//itemBean.setTipoOperacion("OPE_INA");//OPERACION INAFECTA
						list.add(itemBean1);
					}
					
				}
				
				if (itemBean.getTipoOperacion().equals("OPE_INA")){
					operacionInafecta = operacionInafecta.add(itemBean.getPrecio());
				}else if (itemBean.getTipoOperacion().equals("OPE_GRAT"))
					operacionGratuita = operacionGratuita.add(itemBean.getPrecio());
				else if (itemBean.getTipoOperacion().equals("OPE_GRAV")){
					operacionGravada = operacionGravada.add(itemBean.getPrecio());
					//igv = igv.add(augend)
				}
				descuento = descuento.add(impresionItem.getDescuento());
				total = total.add(itemBean.getPrecio().subtract(impresionItem.getDescuento()));
				
			}
			
			//parameters.put("total_venta", "440.00");
				
			
			//resumen
			parameters.put("resumen_gravadas", NumberUtil.toString(operacionGravada) );
			parameters.put("resumen_gratuitas", NumberUtil.toString(operacionGratuita) );
			parameters.put("resumen_inafectas", NumberUtil.toString(operacionInafecta));
			parameters.put("resumen_descuento", NumberUtil.toString(descuento));
			parameters.put("resumen_igv", NumberUtil.toString(igv));
			parameters.put("resumen_monto_texto", NumberUtil.toTexto(total).toUpperCase() + " SOLES");
			parameters.put("resumen_total_venta", NumberUtil.toString(total));
			

			// RUC | TIPO DE DOCUMENTO | SERIE | NUMERO | MTO TOTAL IGV | MTO TOTAL DEL COMPROBANTE | FECHA DE EMISION | TIPO DE DOCUMENTO ADQUIRENTE | NUMERO DE DOCUMENTO ADQUIRENTE |
			String boleta_codigo = "20531084587|" + "03|" + impresion.getCabecera().getNro().replace("-", "|");
			boleta_codigo += igv+ "|";
			boleta_codigo += total + "|";
			boleta_codigo += impresion.getCabecera().getDia() + "|";
			boleta_codigo +=  "1|";
			boleta_codigo += impresion.getCliente().getNro_doc() + "|";
			parameters.put("boleta_codigo", boleta_codigo);

			//logger.debug("antes de llamar al jasper");
			 
			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list);

			InputStream resource = new FileInputStream(rutacARPETA + "/nota_credito2.jasper");
			//logger.debug("llamo al jasper");
			
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(resource);
			
			//logger.debug("despues del loadObject");
			
			resource.close();
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);
			
			//logger.debug("despues de JasperFillManager.fillReport");
			
			String pdfGenerado =rutacARPETA + "/tmp/boleta" + (new Date()).toString().replaceAll(" ","").replaceAll(":", "") +".pdf";
			JasperExportManager.exportReportToPdfFile(jasperPrint, pdfGenerado);
			
			//logger.debug("despues de JasperExportManager.exportReportToPdfFile");
			
			return pdfGenerado;
		}
		
		@RequestMapping(value = "/imprimir/{id}")
		@ResponseBody
		public void reservaImprimir(HttpServletResponse response, @PathVariable Integer id) throws Exception {

			Parametro parametro = parametroDAO.getByParams(new Param("nom", "RUTA_PLANTILLA"));

			Reserva reserva = reservaDAO.getFull(id, new String[] { Alumno.TABLA, Aula.TABLA, Grad.TABLA, Nivel.TABLA,
					CondMatricula.TABLA, Cliente.TABLA, Periodo.TABLA, Familiar.TABLA , "col_persona_a","col_persona_f"});

			File file = null;
			FileInputStream fis = null;
			ByteArrayOutputStream bos = null;

			Map<String, String> map = new HashMap<String, String>();
			map.put("ANIO", reserva.getPeriodo().getAnio().getNom());
			String apoderado = reserva.getPersona_fam().getNom() + " " + reserva.getPersona_fam().getApe_pat() + " "
					+ reserva.getPersona_fam().getApe_mat();
			map.put("APODERADO", apoderado.toUpperCase());
			map.put("NIVEL", reserva.getNivel().getNom());
			String alumno = reserva.getPersona_alu().getNom() + " " + reserva.getPersona_alu().getApe_pat() + " "
					+ reserva.getPersona_alu().getApe_mat();
			map.put("ALUMNO", alumno.toUpperCase());

			map.put("CODIGO_MOD", reserva.getNivel().getCod_mod());
			map.put("GRADO", reserva.getGrad().getNom());
			map.put("SECCION", reserva.getAula().getSecc());
			SimpleDateFormat dtfECLim = new SimpleDateFormat("dd-MM-yyyy");
			map.put("VENCIMIENTO", dtfECLim.format(reserva.getFec_lim()));

			SimpleDateFormat dtDia = new SimpleDateFormat("dd");
			SimpleDateFormat dtMes = new SimpleDateFormat("MM");
			SimpleDateFormat dtAnio = new SimpleDateFormat("YYYY");

			map.put("DIA", dtDia.format(reserva.getFec()));
			// map.put("MES", dtMes.format(reserva.getFec()));
			String mes = dtMes.format(reserva.getFec());
			if (mes.equals("01")) {
				map.put("MES", "enero");
			} else if (mes.equals("02")) {
				map.put("MES", "febrero");
			} else if (mes.equals("03")) {
				map.put("MES", "marzo");
			} else if (mes.equals("04")) {
				map.put("MES", "abril");
			} else if (mes.equals("05")) {
				map.put("MES", "mayo");
			} else if (mes.equals("06")) {
				map.put("MES", "junio");
			} else if (mes.equals("07")) {
				map.put("MES", "julio");
			} else if (mes.equals("08")) {
				map.put("MES", "agosto");
			} else if (mes.equals("09")) {
				map.put("MES", "setiembre");
			} else if (mes.equals("10")) {
				map.put("MES", "octubre");
			} else if (mes.equals("11")) {
				map.put("MES", "noviembre");
			} else if (mes.equals("12")) {
				map.put("MES", "diciembre");
			}

			// map.put("MES", dtMes.format(reserva.getFec()));
			map.put("AAAA", dtAnio.format(reserva.getFec()));			

			String nuevoArchivo = DocxUtil.generate(parametro.getVal(), "CartaReserva.docx", map);
			//String nuevoArchivo = DocxUtil.generate("D:\\plantillas\\", "CartaReserva.docx", map);

			String fileName = URLEncoder.encode("CartaReserva" + reserva.getId_alu() + ".docx", "UTF-8");
			
			fileName = URLDecoder.decode(fileName, "ISO8859_1");
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-disposition", "attachment; filename=" + fileName);

			file = new File(nuevoArchivo);
			fis = new FileInputStream(file);
			bos = new ByteArrayOutputStream();
			int readNum;
			byte[] buf = new byte[1024];
			try {

				for (; (readNum = fis.read(buf)) != -1;) {
					bos.write(buf, 0, readNum);
				}
			} catch (IOException ex) {

			}
			ServletOutputStream out = response.getOutputStream();
			bos.writeTo(out);
		}
		
		@RequestMapping(value = "/registro_auxiliares")
		@ResponseBody
		public void descargarExcel(HttpServletRequest request,HttpServletResponse response, Integer id_anio, Integer id_gir, Integer id_cit)  throws Exception {

			Date fec= new Date();
			ExcelXlsUtil xls = new ExcelXlsUtil();
			
			/*String str_anio = request.getParameter("id_anio");
			int id_anio = Integer.parseInt(str_anio);
			String str_suc=request.getParameter("id_suc");
			Integer id_suc =0 ;
			if(str_suc!=null)
			id_suc = Integer.parseInt(str_suc);*/
			CicloTurno ciclo_turno = cicloTurnoDAO.get(id_cit);
			Ciclo ciclo = cicloDAO.get(ciclo_turno.getId_cic());
			Periodo periodo = periodoDAO.get(ciclo.getId_per());
			Integer id_suc= periodo.getId_suc();
			
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
			
			List<Aula> aulaList = matriculaDAO.listAulas(id_anio, id_suc,id_gir,id_cit);
			
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
		
		
		@RequestMapping(method = RequestMethod.POST, value = "/subirContrato/{id_gpf}/{id_anio}")
		public AjaxResponseBody uploadFileContrato(@RequestParam("file") MultipartFile uploadfile, @PathVariable Integer id_gpf, @PathVariable Integer id_anio) {

			AjaxResponseBody result = new AjaxResponseBody();

			if (uploadfile.isEmpty()) {
				result.setCode("ARCHIVO");
				result.setMsg("archivo es vacio");
				return result;
			}

			try {

				
				//Obtener nro_con por grupo fam
				
				String num_cont =matriculaDAO.getContratoxGruFam(id_gpf, id_anio).get("num_cont").toString();
			
				GruFam gruFam = gruFamDAO.get(id_gpf);
			
				InputStream is = uploadfile.getInputStream();
				
				//Ruta de la carpeta
				
				//String  carpeta =  cacheManager.getComboBox("mod_parametro", new String[]{"CARPETA_CONTRATOS"} ).get(0).getAux1();
				
				//Obtenemos datos de la familia
				
				String nombre_contrato = "ContratoAE_"+num_cont+"_"+gruFam.getNom();
				//File file = new File("c:\\contratos\\contrato_prueba.docx");
				//Para pruebas localles
				//File file = new File("c:\\contratos\\"+nombre_contrato+".pdf");
				
				//Para el servidor /home/aeedupeh/public_html/plantillas/tmp/contratos
				File file = new File("//home//aeedupeh//public_html//plantillas//tmp//contratos//"+nombre_contrato+".pdf");

	            copyInputStreamToFile(is, file);
	            
	          //Insertamos el atributo de documento enviado a las matriculas del contrato relacionado
	            List<Matricula> matricula = matriculaDAO.listByParams(new Param("num_cont",num_cont ), null);
	            
	            for (Matricula matricula2 : matricula) {
					//Ponemos el estado de enviado documento
	            	matriculaDAO.actualizarestadoEnvioDoc(matricula2.getId());
				}
				
				/*Workbook workbook = WorkbookFactory.create(is);
				
				Row datos_alu=matriculaDAO.getDatosAlumno(id_mat);
				
				String archivo = "Contrato.pdf";
				String nuevoArchivo = null;
				SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd_hhmmss");
				nuevoArchivo = dt1.format(new Date());
				String carpeta = "C:/contratos";
				//nuevoArchivo = carpeta + "tmp/" + archivo.replace("Registros_Auxiliares", "Registros_Auxiliares_" +anio.getNom() + nuevoArchivo);
				
				nuevoArchivo = carpeta + "/" + archivo.replace("Contrato", "Contrato" +datos_alu.getString("alumno") + nuevoArchivo);
				//nuevoArchivo = carpeta  + archivo.replace("Registros_Auxiliares", "Registros_Auxiliares_" +anio.getNom() + nuevoArchivo);
				ArchivoUtil.copyFileUsingStream(new File(carpeta + archivo), new File(nuevoArchivo));*/
				
				// File origen = new File("C:\\Users\\Montse\\Documents\\NetBeansProjects\\Copiar2\\hola.txt");
	           /*  File destino = new File("C:\\contratos\\");
	            * 
	            * 

	             try {
	                   //  InputStream in = new FileInputStream(origen);
	                     OutputStream out = new FileOutputStream(destino);

	                     byte[] buf = new byte[1024];
	                     int len;

	                     while ((len = is.read(buf)) > 0) {
	                             out.write(buf, 0, len);
	                     }

	                     is.close();
	                     out.close();
	             } catch (IOException ioe){
	                     ioe.printStackTrace();
	             }*/


				//resultadosDAO.calcularResultados(id_oli);
				//List<Row> listaCargada= procesaExcel(is,id_oli);
				
				//result.setResult(listaCargada);
				return result;
				
			} catch (Exception e) {
				result.setCode("ARCHIVO");
				result.setMsg("archivo con errores:" + e.getMessage());
				//logger.error("metodo:uploadFile",e);
				return result;
			}

		}
		
		@RequestMapping(method = RequestMethod.POST, value = "/subirDeclaracion/{id_gpf}/{id_anio}")
		public AjaxResponseBody uploadFileDeclaracion(@RequestParam("file") MultipartFile uploadfile, @PathVariable Integer id_gpf, @PathVariable Integer id_anio) {

			AjaxResponseBody result = new AjaxResponseBody();

			if (uploadfile.isEmpty()) {
				result.setCode("ARCHIVO");
				result.setMsg("archivo es vacio");
				return result;
			}

			try {

				
				//Obtener nro_con por grupo fam
				
				String num_cont =matriculaDAO.getContratoxGruFam(id_gpf, id_anio).get("num_cont").toString();
			
				GruFam gruFam = gruFamDAO.get(id_gpf);
			
				InputStream is = uploadfile.getInputStream();
				
				//Ruta de la carpeta
				
				//String  carpeta =  cacheManager.getComboBox("mod_parametro", new String[]{"CARPETA_CONTRATOS"} ).get(0).getAux1();
				
				//Obtenemos datos de la familia
				
				String nombre_contrato = "DeclaracionAE_"+num_cont+"_"+gruFam.getNom();
				//File file = new File("c:\\contratos\\contrato_prueba.docx");
				//Para pruebas
				//File file = new File("c:\\declaraciones\\"+nombre_contrato+".pdf");
				
				//Para el servidor /home/aeedupeh/public_html/plantillas/tmp/contratos
				File file = new File("//home//aeedupeh//public_html//plantillas//tmp//declaraciones//"+nombre_contrato+".pdf");
				//File file = new File("//home//aeedupeh//public_html//plantillas//tmp//contratos//"+nombre_contrato+".pdf");

	            copyInputStreamToFile(is, file);
	            
	          //Insertamos el atributo de documento enviado a las matriculas del contrato relacionado
	            List<Matricula> matricula = matriculaDAO.listByParams(new Param("num_cont",num_cont ), null);
	            
	            for (Matricula matricula2 : matricula) {
					//Ponemos el estado de enviado documento
	            	matriculaDAO.actualizarestadoEnvioDecla(matricula2.getId());
				}
				
				
				return result;
				
			} catch (Exception e) {
				result.setCode("ARCHIVO");
				result.setMsg("archivo con errores:" + e.getMessage());
				//logger.error("metodo:uploadFile",e);
				return result;
			}

		}
		
		
		@RequestMapping(method = RequestMethod.POST, value = "/subirProtocolo/{id_gpf}/{id_anio}")
		public AjaxResponseBody uploadFileProtocolo(@RequestParam("file") MultipartFile uploadfile, @PathVariable Integer id_gpf, @PathVariable Integer id_anio) {

			AjaxResponseBody result = new AjaxResponseBody();

			if (uploadfile.isEmpty()) {
				result.setCode("ARCHIVO");
				result.setMsg("archivo es vacio");
				return result;
			}

			try {

				
				//Obtener nro_con por grupo fam
				
				String num_cont =matriculaDAO.getContratoxGruFam(id_gpf, id_anio).get("num_cont").toString();
			
				GruFam gruFam = gruFamDAO.get(id_gpf);
			
				InputStream is = uploadfile.getInputStream();
				
				//Ruta de la carpeta
				
				//String  carpeta =  cacheManager.getComboBox("mod_parametro", new String[]{"CARPETA_CONTRATOS"} ).get(0).getAux1();
				
				//Obtenemos datos de la familia
				
				String nombre_contrato = "ProtocoloAE_"+num_cont+"_"+gruFam.getNom();
				//File file = new File("c:\\contratos\\contrato_prueba.docx");
				//Para pruebas
				//File file = new File("c:\\protocolos\\"+nombre_contrato+".pdf");
				
				//Para el servidor /home/aeedupeh/public_html/plantillas/tmp/contratos
				File file = new File("//home//aeedupeh//public_html//plantillas//tmp//protocolos//"+nombre_contrato+".pdf");

	            copyInputStreamToFile(is, file);
	            
	          //Insertamos el atributo de documento enviado a las matriculas del contrato relacionado
	            List<Matricula> matricula = matriculaDAO.listByParams(new Param("num_cont",num_cont ), null);
	            
	            for (Matricula matricula2 : matricula) {
					//Ponemos el estado de enviado documento
	            	matriculaDAO.actualizarestadoEnvioPro(matricula2.getId());
				}
				
				
				return result;
				
			} catch (Exception e) {
				result.setCode("ARCHIVO");
				result.setMsg("archivo con errores:" + e.getMessage());
				//logger.error("metodo:uploadFile",e);
				return result;
			}

		}
		
		
		
		private static void copyInputStreamToFile(InputStream inputStream, File file)
	            throws IOException {
			Integer DEFAULT_BUFFER_SIZE = 18192;
	        // append = false
	        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
	            int read;
	            byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
	            while ((read = inputStream.read(bytes)) != -1) {
	                outputStream.write(bytes, 0, read);
	            }
	        }

	    }
		
		
		@RequestMapping(value = "/reporte/xls")
		@ResponseBody
		public void descargarExcel(HttpServletRequest request,HttpServletResponse response,String fec_ini, String fec_fin,String fec_ini_env, String fec_fin_env,String tip_com, String nro_serie, String enviadoSunat)  throws Exception {
			 Date fec_ini_ins=null;
			 Date fec_fin_ins=null;
			 Date fec_ini_envio=null;
			 Date fec_fin_envio=null;
			 if(fec_ini!=null && fec_ini!="")
				 fec_ini_ins=FechaUtil.toDate(fec_ini);
			 if(fec_fin!=null && fec_fin!="")
				 fec_fin_ins=FechaUtil.toDate(fec_fin);
			 if(fec_ini_env!=null && fec_ini_env!="")
				 fec_ini_envio=FechaUtil.toDate(fec_ini_env);
			 if(fec_fin_env!=null && fec_fin_env!="")
				 fec_fin_envio=FechaUtil.toDate(fec_fin_env);

			List<Row> list = movimientoDAO.pagosFacturaElectronica(fec_ini_ins , fec_fin_ins, fec_ini_envio, fec_fin_envio,tip_com,nro_serie, enviadoSunat);

		
			response.setContentType("application/vnd.ms-excel");
			
			//Inicio datos del archivo nuevo
			String  carpeta =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();


			SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd_hhmmss");

			String archivo = "ReporteFacElectronica.xls";
			String nuevoArchivo = null;
			nuevoArchivo = dt1.format(new Date());
			
			nuevoArchivo = carpeta + "tmp/" + archivo.replace("ReporteFacElectronica", "ReporteFacElectronica" + nuevoArchivo);

			ArchivoUtil.copyFileUsingStream(new File(carpeta + archivo), new File(nuevoArchivo));
			

			FileInputStream inputStream = new FileInputStream(new File(nuevoArchivo));
			Workbook workbook = WorkbookFactory.create(inputStream);
			   Sheet sheet = workbook.getSheetAt(0);

			//inicio - Obtener descripciones periodo, nivel, grado, seccion
			//fec_ini
			Cell cell = sheet.getRow(3).getCell(3);
			cell.setCellValue(fec_ini);

			//fec_fin
			cell = sheet.getRow(3).getCell(6);
			cell.setCellValue(fec_fin);

			//fec_ini
			cell = sheet.getRow(3).getCell(12);
			cell.setCellValue(nro_serie);

			//fec_ini
			cell = sheet.getRow(3).getCell(16);
			cell.setCellValue(enviadoSunat);

			//fin - Obtener descripciones periodo, nivel, grado, seccion
			
		    int filaInicio=6;
	        
	        int columna=1;
	        
	        CellStyle styleCell = workbook.createCellStyle();
	        styleCell.setBorderBottom(BorderStyle.THIN);
	        styleCell.setBorderTop(BorderStyle.THIN);
	        styleCell.setBorderRight(BorderStyle.THIN);
	        
	        //f.nro_doc, m.fec,m.fec_sunat, f.id_tdc, f.ape_pat, f.ape_mat, f.nom,f.corr, f.cel, m.*
	        
	        for (int i = 0; i < list.size(); i++) {
	        	columna=1;
	        	Cell cell1 = sheet.getRow(filaInicio+i).createCell(columna++);
	        	cell1.setCellValue(i+1);
				cell1.setCellStyle(styleCell);
				
				Cell cell2 = sheet.getRow(filaInicio+i).createCell(columna++);
	        	cell2.setCellValue(list.get(i).getString("serie"));
				cell2.setCellStyle(styleCell);

				Cell cell3 = sheet.getRow(filaInicio+i).createCell(columna++);
	        	cell3.setCellValue(list.get(i).getString("recibo"));
				cell3.setCellStyle(styleCell);

				Cell cell4 = sheet.getRow(filaInicio+i).createCell(columna++);
	        	cell4.setCellValue(list.get(i).getString("fec"));
				cell4.setCellStyle(styleCell);

				cell4 = sheet.getRow(filaInicio+i).createCell(columna++);
				cell4.setCellValue(list.get(i).getString("fec_sunat"));
				cell4.setCellStyle(styleCell);
				
				cell4 = sheet.getRow(filaInicio+i).createCell(columna++);
				cell4.setCellValue(list.get(i).getString("nro_rec_afec"));
				cell4.setCellStyle(styleCell);
				
				cell4 = sheet.getRow(filaInicio+i).createCell(columna++);
				cell4.setCellValue(list.get(i).getString("fec_rec_env"));
				cell4.setCellStyle(styleCell);
				
				cell4 = sheet.getRow(filaInicio+i).createCell(columna++);
				cell4.setCellValue(list.get(i).getString("obs"));
				cell4.setCellStyle(styleCell);

				//buscar nivel
				
				
				cell4 = sheet.getRow(filaInicio+i).createCell(columna++);
				cell4.setCellValue("");
				cell4.setCellStyle(styleCell);

				cell4 = sheet.getRow(filaInicio+i).createCell(columna++);
				cell4.setCellValue(list.get(i).getString("nro_doc"));
				cell4.setCellStyle(styleCell);

				cell4 = sheet.getRow(filaInicio+i).createCell(columna++);
				cell4.setCellValue(list.get(i).getString("cliente"));
				cell4.setCellStyle(styleCell);

				cell4 = sheet.getRow(filaInicio+i).createCell(columna++);
				cell4.setCellValue(list.get(i).getString("monto"));
				cell4.setCellStyle(styleCell);

				cell4 = sheet.getRow(filaInicio+i).createCell(columna++);
				cell4.setCellValue(list.get(i).getString("descuento"));
				cell4.setCellStyle(styleCell);

				cell4 = sheet.getRow(filaInicio+i).createCell(columna++);
				cell4.setCellValue(list.get(i).getString("monto_total"));
				cell4.setCellStyle(styleCell);

				String cod_res  = list.get(i).getString("cod_res");
				
				cell4 = sheet.getRow(filaInicio+i).createCell(columna++);
				if (cod_res==null)
					cell4.setCellValue("PENDIENTE DE ENVIO");
				else
					cell4.setCellValue("ENVIADO A SUNAT");
				cell4.setCellStyle(styleCell);

				cell4 = sheet.getRow(filaInicio+i).createCell(columna++);
				cell4.setCellValue(cod_res);
				cell4.setCellStyle(styleCell);

	        }
	         
		
			inputStream.close();

			FileOutputStream outputStream = new FileOutputStream(nuevoArchivo);
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();
			
			response.setHeader("Content-Disposition","attachment;filename=ReporteFacturaElectronica.xls");

			File initialFile = new File(nuevoArchivo);
		    InputStream is = FileUtils.openInputStream(initialFile);
		    	
			IOUtils.copy(is, response.getOutputStream());
			
		}	
		
		@RequestMapping(value = "/excelDeudores")
		@ResponseBody
		public void descargarExcelDeudores(HttpServletRequest request,HttpServletResponse response,Integer id_gir, Integer id_cic, Integer id_niv, Integer id_gra, Integer id_au, Integer mes, String mat, String ord, Integer id_anio)  throws Exception {
		  		ExcelXlsUtil xls = new ExcelXlsUtil();
		  		Anio anio = anioDAO.get(id_anio);
				String nivel="TODOS";
				String grado="TODOS";
				String aula="TODOS";
				String nom_mes="";
				if(id_niv!=null) {
					if(id_niv==1) {
						nivel="INICIAL";
					} else if(id_niv==2) {
						nivel="PRIMARIA";
					} else if(id_niv==3) {
						nivel="SECUNDARIA";
					} else if(id_niv==4) {
						nivel="PRE-UNIVERSITARIO";
					}
				}
				if(id_gra!=null) {
					Grad grad = gradDAO.get(id_gra);
					grado=grad.getNom();
				}
				if(id_au!=null) {
					Aula aul=aulaDAO.get(id_au);
					aula=aul.getSecc();
				}
				if(mes!=null) {
					if(mes==3) {
						nom_mes="Marzo";
					} else if(mes==4) {
						nom_mes="Abril";
					} else if(mes==5) {
						nom_mes="Mayo";
					} else if(mes==6) {
						nom_mes="Junio";
					} else if(mes==7) {
						nom_mes="Julio";
					} else if(mes==8) {
						nom_mes="Agosto";
					} else if(mes==9) {
						nom_mes="Setiembre";
					} else if(mes==10) {
						nom_mes="Octubre";
					} else if(mes==11) {
						nom_mes="Noviembre";
					} else if(mes==12) {
						nom_mes="Diciembre";
					}
				}
				
				String nom_mat= "MATRICULADOS Y TRASLADADOS";
				if(mat!=null) {
					if(mat.equals("S")) {
						nom_mat="MATRICULADOS";
					} else if(mat.equals("N")) {
						nom_mat="TRASLADADOS";
					}
				}
			List<Map<String,Object>> list = new ArrayList<>();
			if(id_gir.equals(2) || id_gir.equals(3)) {
				 list = pagorealizadoDAO.listaDeudasAcadVac(id_anio,id_gir,id_cic, id_niv, id_gra, id_au, mes, mat, ord);
			} else {
				 list = pagorealizadoDAO.listaDeudas(id_anio, id_niv, id_gra, id_au, mes, mat, ord);
			}
			
			//List<Map<String,Object>> list = pagorealizadoDAO.listaPagados(id_anio, id_niv, id_gra, id_au, mes, mat, ord);
			Date fec=new Date();
			response.setContentType("application/vnd.ms-excel");
			
			String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();
			
			String archivo=null;
			if(id_gir.equals(2) || id_gir.equals(3)) {
				 archivo = xls.generaExcelReporteDeudasAcadVac(rutacARPETA, "plantilla_reporte_deudores_acadvac.xls",nivel, grado, aula,nom_mes, nom_mat, list);
			} else {
				 archivo = xls.generaExcelReporteDeudas(rutacARPETA, "plantilla_reporte_deudores.xls",nivel, grado, aula,nom_mes, nom_mat, list);
			}

			response.setHeader("Content-Disposition","attachment;filename=Reporte_Deudores" + FechaUtil.toString(fec, "dd-MM-yyyy") + ".xls");

			File initialFile = new File(archivo);
		    InputStream is = FileUtils.openInputStream(initialFile);
		    	
			IOUtils.copy(is, response.getOutputStream());
			
		}	
		
		@RequestMapping(value = "/excelPagados")
		@ResponseBody
		public void descargarExcelPagados(HttpServletRequest request,HttpServletResponse response, Integer id_niv, Integer id_gra, Integer id_au, Integer mes, String mat, String ord, Integer id_anio)  throws Exception {
		  		ExcelXlsUtil xls = new ExcelXlsUtil();
		  		Anio anio = anioDAO.get(id_anio);
				String nivel="TODOS";
				String grado="TODOS";
				String aula="TODOS";
				String nom_mes="";
				if(id_niv!=null) {
					if(id_niv==1) {
						nivel="INICIAL";
					} else if(id_niv==2) {
						nivel="PRIMARIA";
					} else if(id_niv==3) {
						nivel="SECUNDARIA";
					}
				}
				if(id_gra!=null) {
					Grad grad = gradDAO.get(id_gra);
					grado=grad.getNom();
				}
				if(id_au!=null) {
					Aula aul=aulaDAO.get(id_au);
					aula=aul.getSecc();
				}
				if(mes!=null) {
					if(mes==3) {
						nom_mes="Marzo";
					} else if(mes==4) {
						nom_mes="Abril";
					} else if(mes==5) {
						nom_mes="Mayo";
					} else if(mes==6) {
						nom_mes="Junio";
					} else if(mes==7) {
						nom_mes="Julio";
					} else if(mes==8) {
						nom_mes="Agosto";
					} else if(mes==9) {
						nom_mes="Setiembre";
					} else if(mes==10) {
						nom_mes="Octubre";
					} else if(mes==11) {
						nom_mes="Noviembre";
					} else if(mes==12) {
						nom_mes="Diciembre";
					}
				}
				
				String nom_mat= "MATRICULADOS Y TRASLADADOS";
				if(mat!=null) {
					if(mat.equals("S")) {
						nom_mat="MATRICULADOS";
					} else if(mat.equals("N")) {
						nom_mat="TRASLADADOS";
					}
				}
			//List<Map<String,Object>> list = pagorealizadoDAO.listaDeudas(id_anio, id_niv, id_gra, id_au, mes, mat, ord);
			List<Map<String,Object>> list = pagorealizadoDAO.listaPagados(id_anio, id_niv, id_gra, id_au, mes, mat, ord);
			Date fec=new Date();
			response.setContentType("application/vnd.ms-excel");
			
			String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();

			String archivo = xls.generaExcelReportePagos(rutacARPETA, "plantilla_reporte_pagados.xls",nivel, grado, aula,nom_mes, nom_mat, list);
			response.setHeader("Content-Disposition","attachment;filename=Reporte_Pagados" + FechaUtil.toString(fec, "dd-MM-yyyy") + ".xls");

			File initialFile = new File(archivo);
		    InputStream is = FileUtils.openInputStream(initialFile);
		    	
			IOUtils.copy(is, response.getOutputStream());
			
		}	
	/*	
		@RequestMapping(value = "/generarLibretaNuevo/{id_anio}/{id_cpu}/{id_mat}")
		//@SuppressWarnings("unchecked") //aquiii
		//public AjaxResponseBody generaLibreta_Nuevo(Integer id_fam, Integer id_anio, Integer id_cpu,List<Matricula> matriculas) throws Exception {
		public void generaLibreta_Nuevo(@PathVariable Integer id_anio,@PathVariable Integer id_cpu,@PathVariable Integer id_mat, HttpServletResponse response) throws Exception {
			AjaxResponseBody result = new AjaxResponseBody();
	        CabeceraNotaReq cabecera = new CabeceraNotaReq();
			try {
				
			//	logger.debug("generaLibreta");
				Anio anio = anioDAO.get(id_anio);
				//String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();
				
				Document document = new Document();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				//PdfCopy copy = new PdfCopy(document, baos);

		        document.open();
		        
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
		       List<Row> areas= dcnAreaDAO.listarAreasCombo(dcnNivel.getId());
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
		        List<Row> listaPeriodos = cronogramaDAO.listarPeriodosX_Nivel(matricula.getId_niv(), id_anio);
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
		        List<AreaNotaReq> areas_nota=notaDAO.promediosAreasMatricula(id_mat, matricula.getId_alu(), matricula.getId_gra(), matricula.getId_au_asi(),id_cpu, id_anio, dcnNivel.getId());
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
				//String libreta_conf = gson.toJson(cabecera);
				//System.out.println(libreta_conf);
				//document.close();
				//return  baos.toByteArray();
				//result.setResult(cabecera);
				
			} catch (Exception e) {
				// TODO: handle exception
				System.out.print((e.getMessage().toString()));
			}
			try {
				String nombrePDF = "libreta_" + 1 +".pdf";
				response.setHeader("Content-Disposition","inline; filename=\"" + nombrePDF +"\"");
				response.setContentType("application/pdf; name=\"" + nombrePDF + "\"");

				response.getOutputStream().write(generaLibretaNuevo(cabecera));
			} catch (Exception e) {
				// TODO: handle exception
			 e.printStackTrace();
			}
			

		}
		
		@SuppressWarnings("unchecked")
		public byte[] generaLibretaNuevo(CabeceraNotaReq cabecera) throws Exception {
			
			String nombreJSON = cabecera.getPeriodo() + "-" + cabecera.getCod_alumno();
			String rutaJson = "C:\\plantillas\\tmp\\" + nombreJSON + ".json";

		    Path path = Paths.get(rutaJson);
		    byte[] strToBytes = cabecera.toString().getBytes();

		    Files.write(path, strToBytes);
		    
		//	logger.debug("generaLibreta");
			//Anio anio = anioDAO.get(id_anio);
			String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();
			  
			Document document = new Document();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfCopy copy = new PdfCopy(document, baos);

	        document.open();
			
	    	InputStream inputStream = null;
			String ruta=null;
			
				ruta=rutacARPETA  + "report.jrxml";
			
			inputStream = new FileInputStream( ruta);
			//Prametros
			 Map<String, Object> parameters = new HashMap<>();
		        parameters.put(JsonQueryExecuterFactory.JSON_DATE_PATTERN, "yyyy-MM-dd");
		        parameters.put(JsonQueryExecuterFactory.JSON_NUMBER_PATTERN, "#,##0.##");
		        parameters.put(JsonQueryExecuterFactory.JSON_LOCALE, Locale.ENGLISH);
		        parameters.put(JRParameter.REPORT_LOCALE, Locale.US);
		        parameters.put(JsonQueryExecuterFactory.JSON_SOURCE,rutaJson);
		       // String initialString = "text";
		      //  InputStream targetStream = IOUtils.toInputStream(cabecera.toString());
		       //InputStream iostream = new ByteArrayInputStream(cabecera.toString().getBytes(StandardCharsets.UTF_8));
		      // InputStream iostream = new java.io.ByteArrayInputStream(cabecera.toString().getBytes("UTF8"));
		        //InputStream iostream = CharSourc.wrap(cabecera).asByteSource(StandardCharsets.UTF_8).openStream();
		       // InputStream targetStream = new ByteArrayInputStream(cabecera.toString().getBytes(("UTF-8")));
		       // CharSource.wrap(initialString).asByteSource(StandardCharsets.UTF_8).openStream();

		       // parameters.put(JsonQueryExecuterFactory.JSON_INPUT_STREAM,iostream);
		        parameters.put("SUBREPORT_DIR", JASPER_FILE_LOCATION);
		        parameters.put("IMAGE_LOCATION",IMAGE_LOCATION );
		        //return parameters;

			// grilla
		        System.out.println( "generating jasper report..." );
		        String compiledFile = JASPER_FILE_LOCATION+"report.jasper"; 
		       // Map<String, Object> parameters = getParameters();
		        JasperPrint jasperPrint = JasperFillManager.fillReport(compiledFile, 
		                parameters);
		        byte[] bytesPDF =  JasperExportManager.exportReportToPdf(jasperPrint);
		        PdfReader reader = new PdfReader(bytesPDF);

				copy.addPage(copy.getImportedPage(reader, 1));
			/*JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			//HashMap<String,Object> parameters = new HashMap<String,Object>();
			//JsonDataSource datasource = new JsonDataSource(new ByteArrayInputStream(cabecera.toString().getBytes("UTF-8")));
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);
			byte[] bytesPDF =  JasperExportManager.exportReportToPdf(jasperPrint);
				
				
				PdfReader reader = new PdfReader(bytesPDF);

				copy.addPage(copy.getImportedPage(reader, 1));*/
				
				
			//}
			
		/*	document.close();
			return  baos.toByteArray();

		}*/
		
		@RequestMapping(value = "/generarLibretaNuevo/{id_anio}/{id_cpu}/{id_gra}/{id_gir}")
		//@SuppressWarnings("unchecked") //aquiii
		//public AjaxResponseBody generaLibreta_Nuevo(Integer id_fam, Integer id_anio, Integer id_cpu,List<Matricula> matriculas) throws Exception {
		public AjaxResponseBody generaLibreta_Nuevo(@PathVariable Integer id_anio,@PathVariable Integer id_cpu,@PathVariable Integer id_gra,@PathVariable Integer id_gir, HttpServletResponse response) throws Exception {
			AjaxResponseBody result = new AjaxResponseBody();
	        CabeceraNotaReq cabecera = new CabeceraNotaReq();
			try {
				
				List<Row> list_matriculas=matriculaDAO.listarAlumnosxGradoAnioSinPF(id_gra, id_anio, id_gir);
				//logger.debug("generaLibreta");
				Anio anio = anioDAO.get(id_anio);
				//String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();
				File file = null;
				FileInputStream fis = null;
				ByteArrayOutputStream bos = null;
				Document document = new Document();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				//PdfCopy copy = new PdfCopy(document, baos);

		        document.open();
				for (Row row_mat : list_matriculas) {
					
			        Integer id_mat=row_mat.getInteger("id_mat");
			        String abrv=row_mat.getString("abrv_classroom")+row_mat.getString("secc");
			        String dni_alumno=row_mat.getString("nro_doc");
			        Matricula matricula = matriculaDAO.get(id_mat);
			        Row alumno = alumnoDAO.datosAlumno(id_mat).get(0);
			        //Row periodo_academico=perUniDAO.datosPeriodoxNivel(id_anio, matricula.getId_niv()).get(0);
			        Row periodo_academico=perUniDAO.datosPeriodo(id_cpu).get(0);
			        String titulo = "";
			        if(id_gir.equals(1)) {
			        	titulo = "INFORME DE LOS PROCESOS DEL EDUCANDO - "+anio.getNom().toUpperCase();
			        } else if(id_gir.equals(2)){
			        	titulo = "ACADEMIA ENCINAS - "+anio.getNom().toUpperCase();
			        }   else if(id_gir.equals(3)){
			        	titulo = "VACACIONES ÚTILES ALBERT EINSTEIN - "+anio.getNom().toUpperCase();
			        }
			        
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
			        System.out.println(cabecera.getAlumno());
			      //  DetalleNotaReq detalle_nota = new DetalleNotaReq();
			        Param param = new Param();
			        param.put("id_niv", matricula.getId_niv());
			        param.put("id_dcn", confAnioAcadDcn.getId_dcn());
			        DcnNivel dcnNivel= dcNivelDAO.getByParams(param);
			        List<AreaCompetenciaReq> areas_competencia= new ArrayList<AreaCompetenciaReq>();
			      //Obtengo la lista de Areas
			       List<Row> areas= dcnAreaDAO.listarAreasComboAnio(dcnNivel.getId(),id_anio, id_gra, id_gir);
			       for (Row row : areas) {
						List<CompetenciaDc> competenciasDc= competenciaDcDAO.listByParams(new Param("id_dcare",row.getInteger("id")),new String[]{"orden asc"});
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
			        List<AreaNotaReq> areas_nota=notaDAO.promediosAreasMatricula(id_mat, matricula.getId_alu(), matricula.getId_gra(), matricula.getId_au_asi(),id_cpu, id_anio, dcnNivel.getId(), id_gir);
			        		// areas_nota_total.add(areas_nota);
			        	//}
			      //  }
			        cabecera.setList_areas_notas(areas_nota);
			        cabecera.setList_periodos(periodos_acad);
			        AulaDetalle aulaDetalle = aulaDetalleDAO.getByParams(new Param("id_au",matricula.getId_au_asi()));
			        Trabajador tutor = new Trabajador();
			        if(aulaDetalle!=null) {
			        	 tutor = trabajadorDAO.get(aulaDetalle.getId_tut());
					     Persona persona = personaDAO.get(tutor.getId_per());
					     cabecera.setNom_tutor(persona.getApe_pat()+" "+persona.getApe_mat()+" "+persona.getNom());
			        } else {
			        	 cabecera.setNom_tutor("");
			        }
			        
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
					//String nombrePDF = "libreta_" + dni_alumno +"_"+alumno.getString("nom").replace(" ", "")+alumno.getString("ape_pat")+"_"+abrv+"_"+cabecera.getPeriodo().replace(" ", "")+".pdf";
					String nombrePDF = null;
					String nombreJson = null;
					if(id_gir.equals(1)) {
						 nombrePDF = anio.getNom()+" - "+abrv+" - "+cabecera.getPeriodo()+" - "+cabecera.getAlumno();
						 nombreJson =abrv.replace(" ", "")+"_"+cabecera.getPeriodo().replace(" ", "")+"_"+cabecera.getAlumno().replace(" ", "");
					} else if(id_gir.equals(2)) {
						 nombrePDF = "ACAD"+anio.getNom()+" - "+abrv+" - "+cabecera.getPeriodo()+" - "+cabecera.getAlumno();
						 nombreJson ="ACAD"+abrv.replace(" ", "")+"_"+cabecera.getPeriodo().replace(" ", "")+"_"+cabecera.getAlumno().replace(" ", "");
					} else if (id_gir.equals(3)) {
						 nombrePDF = "VU"+anio.getNom()+" - "+abrv+" - "+cabecera.getPeriodo()+" - "+cabecera.getAlumno();
						 nombreJson = "VU"+abrv.replace(" ", "")+"_"+cabecera.getPeriodo().replace(" ", "")+"_"+cabecera.getAlumno().replace(" ", "");
					}
					
					
					//response.setHeader("Content-Disposition","inline; filename=\"" + nombrePDF +"\"");
					//response.setContentType("application/pdf; name=\"" + nombrePDF + "\"");
					Grad grado=gradDAO.get(id_gra);
					GiroNegocio giro=giroNegocioDAO.get(id_gir);
					Nivel nivel =nivelDAO.get(grado.getId_nvl());
					String nom_periodo=periodo_academico.getString("nump")+" "+periodo_academico.getString("periodo");
					String pdf=generaLibretaNuevo(cabecera,nombrePDF,nombreJson,anio.getNom(),nivel.getNom(),grado.getNom(),giro.getNom(),nom_periodo);
					//response.getOutputStream().write();
					File initialFile = new File(pdf);
				    InputStream is = FileUtils.openInputStream(initialFile);
				    response.setContentType("application/pdf");
				    //String nombreJSON = cabecera.getPeriodo().replace(" ", "") + "-" + cabecera.getCod_alumno();
					response.addHeader("Content-Disposition", "attachment; filename=" + nombrePDF + ".pdf");

					IOUtils.copy(is, response.getOutputStream());
					//document.close();
					file = new File(pdf);
					fis = new FileInputStream(file);
					bos = new ByteArrayOutputStream();
					int readNum;
					byte[] buf = new byte[1024];
					try {

						for (; (readNum = fis.read(buf)) != -1;) {
							bos.write(buf, 0, readNum);
						}
					} catch (IOException ex) {

					}
					ServletOutputStream out = response.getOutputStream();
					bos.writeTo(out);
					//is.close();
					out.close();
					//document.close();
					//IOUtils.copy(is, response.getOutputStream());
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
			return result;

		}
		
		
		
		 public void agregarCarpeta(String ruta, String carpeta, ZipOutputStream zip) throws Exception {
		        File directorio = new File(carpeta);
		        for (String nombreArchivo : directorio.list()) {
		            if (ruta.equals("")) {
		                agregarArchivo(directorio.getName(), carpeta + "/" + nombreArchivo, zip);
		            } else {
		                agregarArchivo(ruta + "/" + directorio.getName(), carpeta + "/" + nombreArchivo, zip);
		            }
		        }
		    }

		    public void agregarArchivo(String ruta, String directorio, ZipOutputStream zip) throws Exception {
		        File archivo = new File(directorio);
		        if (archivo.isDirectory()) {
		            agregarCarpeta(ruta, directorio, zip);
		        } else {
		            byte[] buffer = new byte[4096];
		            int leido;
		            FileInputStream entrada = new FileInputStream(archivo);
		            zip.putNextEntry(new ZipEntry(ruta + "/" + archivo.getName()));
		            while ((leido = entrada.read(buffer)) > 0) {
		                zip.write(buffer, 0, leido);
		            }
		        }
		    }

		    public void comprimir(String archivo, String archivoZIP) throws Exception {
		        ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(archivoZIP));
		        agregarCarpeta("", archivo, zip);
		        zip.flush();
		        zip.close();
		    }
		    
		    public static HttpServletResponse downLoadFiles(List<File> files,
                    String zipPath,
                    HttpServletRequest request, HttpServletResponse response) throws Exception {
				try {
				
				File file = new File(zipPath);
				if (!file.exists()){
				file.createNewFile();
				}
				response.reset();
				//response.getWriter()
				// Crear flujo de salida de archivo
				FileOutputStream fous = new FileOutputStream(file);
				
				ZipOutputStream zipOut = new ZipOutputStream(fous);
				zipFile(files, zipOut);
				zipOut.close();
				fous.close();
				return downloadZip(file,response);
				}catch (Exception e) {
				e.printStackTrace();
				}	
	
			return response ;
			}
		    
		    private static void zipFile
		    (List files,ZipOutputStream outputStream) {
		        int size = files.size();
		        for(int i = 0; i < size; i++) {
		            File file = (File) files.get(i);
		            zipFile(file, outputStream);
		        }
		    }
		    
		    private static HttpServletResponse downloadZip(File file, HttpServletResponse response) {
		        try {
		            // Descarga el archivo como una secuencia.
		            InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
		            byte[] buffer = new byte[fis.available()];
		            fis.read(buffer);
		            fis.close();
		            // borra la respuesta
		            response.reset();

		            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
		            response.setContentType("application/octet-stream");
		            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(),"UTF-8"));
		            toClient.write(buffer);
		            toClient.flush();
		            toClient.close();
		        } catch (IOException ex) {
		            ex.printStackTrace();
		        }finally{
		            try {
		                File f = new File(file.getPath());
		                f.delete();
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
		        }
		        return response;
		    }
		    
		    private static void zipFile(File inputFile,
                    ZipOutputStream ouputStream) {
				try {
				 if(inputFile.exists()) {
				     if (inputFile.isFile()) {
				         FileInputStream IN = new FileInputStream(inputFile);
				         BufferedInputStream bins = new BufferedInputStream(IN, 512);
				         //org.apache.tools.zip.ZipEntry
				         ZipEntry entry = new ZipEntry(inputFile.getName());
				         ouputStream.putNextEntry(entry);
				         // Datos de salida al archivo comprimido
				         int nNumber;
				         byte[] buffer = new byte[512];
				         while ((nNumber = bins.read(buffer)) != -1) {
				             ouputStream.write(buffer, 0, nNumber);
				         }
				         // Cerrar el objeto de secuencia creado
				         bins.close();
				         IN.close();
				     } else {
				         try {
				             File[] files = inputFile.listFiles();
				             for (int i = 0; i < files.length; i++) {
				                 zipFile(files[i], ouputStream);
				             }
				         } catch (Exception e) {
				             e.printStackTrace();
				         }
				     }
				 }
				} catch (Exception e) {
				 e.printStackTrace();
				}
				}
		    
		    
		   
		    /**
		           * Escriba los archivos que deben descargarse en un zip y descárguelos al cliente
		     * @param response HttpServletResponse
		     * @param request HttpServletRequest
		           * @param zipNamePrefix personaliza el nombre del zip
		           * @param fileMap Map <String, File> String: el nombre del archivo escrito en el zip, el archivo debe escribirse en el archivo zip
		     */
		    public static void downloadZipToClient(
		            String zipNamePrefix,
		            HttpServletResponse response,
		            HttpServletRequest request,
		            Map<String, File> fileMap){

		        // Establecer el nombre del zip, los encabezados de solicitud y los encabezados de respuesta
		        String zipName = getZipName(zipNamePrefix);
		        // Escribe el nombre del paquete comprimido en el encabezado de la respuesta
		        response = setZipNameToResponse(zipName, response, request);
		        // Escriba el archivo en el zip, realice la descarga mientras comprime
		        setFileToZipOnResponse(fileMap, response);
		    }

		    /**
		           * Establecer el nombre al descargar el zip
		     * @param prefix
		     * @return
		     */
		    public static String getZipName(String prefix){
		        long l = System.currentTimeMillis();
		        Date date = new Date(l);
		        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		        String nowDateString = dateFormat.format(date);
		        return prefix + "_"+nowDateString+".zip";
		    }

		    /**
		           * Escriba el zip en la respuesta
		           * @param name El nombre del archivo comprimido
		           * @param respuesta flujo de respuesta
		           * @param solicitud de flujo de solicitud
		     * @return
		     */
		    public static HttpServletResponse setZipNameToResponse(
		            String name,
		            HttpServletResponse response,
		            HttpServletRequest request){
		        response.reset();
		        response.setCharacterEncoding("utf-8");
		        response.setContentType("multipart/form-data");
		        String agent = request.getHeader("USER-AGENT");
		        String msie = "MSIE";
		        String trident = "Trident";

		        try {
		            if (agent.contains(msie)||agent.contains(trident)) {
		                name = java.net.URLEncoder.encode(name, "UTF-8");
		            } else {
		                name = new String(name.getBytes("UTF-8"),"ISO-8859-1");
		            }
		        } catch (UnsupportedEncodingException e) {
		            //logger.error("Código anormal");
		            e.printStackTrace();
		        }
		        response.setHeader("Content-Disposition", "attachment;fileName=\"" + name + "\"");
		        return response;
		    }

		    /**
		           * Establecer método de compresión
		     * @return
		     */
		    private static ZipOutputStream setZipWay(HttpServletResponse response){
		        ZipOutputStream zipos = null;
		        try {
		            zipos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
		            zipos.setMethod(ZipOutputStream.DEFLATED); // Establecer método de compresión
		        } catch (IOException e) {
		            e.printStackTrace();
		           // logger.error("Excepción IO");
		        }
		        return zipos;
		    }

		    /**
		           * Escriba el archivo en el zip, realice la descarga mientras comprime
		     *
		           * @param fileMap Necesita escribir el archivo zip
		           * @param respuesta flujo de respuesta
		     */
		    private static void setFileToZipOnResponse(Map<String, File> fileMap, HttpServletResponse response){
		        ZipOutputStream zipos = setZipWay(response);
		        DataOutputStream os = null;
		        for (Map.Entry<String, File> entry: fileMap.entrySet()) {
		            String fileName = entry.getKey();
		            File file = entry.getValue();
		            try {
		                zipos.putNextEntry(new ZipEntry(fileName));
		                os = new DataOutputStream(zipos);
		                InputStream is = new FileInputStream(file);
		                byte[] b = new byte[100];
		                int length = 0;
		                while((length = is.read(b))!= -1){
		                    os.write(b, 0, length);
		                }
		                is.close();
		                zipos.closeEntry();
		            } catch (IOException e) {
		                e.printStackTrace();
		               // logger.error("IO stream anormal");
		            }
		        }
		        try {
		            os.flush();
		            os.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		           // logger.info("Excepción de transmisión cerrada");
		        }
		    }
		    
		    //probando aqui
		    /*
		     * Zip function Zip all files and folders
		     */
		   // @SuppressWarnings("finally")
		    @RequestMapping (value = "/downloadFiles2/{id_gir}/{id_anio}/{id_gra}/{id_cpu}", method = RequestMethod.GET) // Coincide con la solicitud de descarga en href
		    public void zipFiles(HttpServletResponse response, @PathVariable Integer id_gir, @PathVariable Integer id_anio, @PathVariable Integer id_gra, @PathVariable Integer id_cpu) {
		    	
		        boolean result = false;
		        GiroNegocio giro = giroNegocioDAO.get(id_gir);
		        Anio anio = anioDAO.get(id_anio);
		        Grad grado= gradDAO.get(id_gra);
		        Nivel nivel=nivelDAO.get(grado.getId_nvl());
		        Row periodo_academico=perUniDAO.datosPeriodo(id_cpu).get(0);
		        String nom_periodo=periodo_academico.getString("nump")+" "+periodo_academico.getString("periodo");
		        //try {
		            System.out.println("Program Start zipping the given files");
		            /*
		             * send to the Zip procedure
		             */
		            
		            OutputStream outputStream = null;
			        ZipOutputStream zos = null;
			        try {
			        
			        	//zipFolder(srcFolder, destZipFile);
			            outputStream = response.getOutputStream();

			            response.setContentType("multipart/form-data");
			 
			            response.setHeader("Content-Disposition", "attachment;fileName = " + URLEncoder.encode("Libretas"+anio.getNom()+"_"+giro.getNom().replace(" ", "")+"_"+grado.getNom().replace(" ", "")+"_"+nom_periodo.replace(" ", "")+".zip", "UTF-8"));
			          
			            zos = new ZipOutputStream(outputStream);
			            
			            File dirFile = new File("//home//aeedupeh//public_html//plantillas//tmp//libretas//"+anio.getNom()+"//"+giro.getNom()+"//"+nivel.getNom()+"//"+grado.getNom()+"//"+nom_periodo);
			            
			            String[] fileList = dirFile.list();
			            
			            if (fileList != null) {
			        
			        
			                for (String fp : fileList) {
			        
			                	  //if(fp.equals("prueba.zip")) {
			                		  File file = new File("//home//aeedupeh//public_html//plantillas//tmp//libretas//"+anio.getNom()+"//"+giro.getNom()+"//"+nivel.getNom()+"//"+grado.getNom()+"//"+nom_periodo, fp); 
					                    zipFile(file, zos);   
					                    response.flushBuffer();
			                	 // }
			                   
			                    
			                }
			            }
			        } catch (Exception e) {
			        
			        
			            e.printStackTrace();
			        } finally {
			        
			        
			            try {
			        
			        
			                if (zos != null) {
			        
			        
			                    zos.close();
			                }
			                if (outputStream != null) {
			        
			        
			                    outputStream.close();
			                }
			            } catch (IOException e) {
			        
			        
			                e.printStackTrace();
			            }
			        }
		            result = true;
		            System.out.println("Given files are successfully zipped");
		       /* } catch (Exception e) {
		        	System.out.println(e.getMessage());
		            System.out.println("Some Errors happned during the Zip process");
		        } finally {
		           // return result;
		        }*/
		    }
		    
		   /* public static void MultiFileZipDownload(HttpServletResponse response, String downloadName, String filePath) {
		        
		        
		        OutputStream outputStream = null;
		        ZipOutputStream zos = null;
		        try {
		        
		        
		            outputStream = response.getOutputStream();

		            response.setContentType("multipart/form-data");
		 
		            response.setHeader("Content-Disposition", "attachment;fileName = " + URLEncoder.encode(downloadName, "UTF-8"));
		          
		            zos = new ZipOutputStream(outputStream);
		            
		            File dirFile = new File(filePath);
		            
		            String[] fileList = dirFile.list();
		            
		            if (fileList != null) {
		        
		        
		                for (String fp : fileList) {
		        
		        
		                    File file = new File(filePath, fp); 
		                    zipFile(file, zos);  
		                    response.flushBuffer();
		                }
		            }
		        } catch (Exception e) {
		        
		        
		            e.printStackTrace();
		        } finally {
		        
		        
		            try {
		        
		        
		                if (zos != null) {
		        
		        
		                    zos.close();
		                }
		                if (outputStream != null) {
		        
		        
		                    outputStream.close();
		                }
		            } catch (IOException e) {
		        
		        
		                e.printStackTrace();
		            }
		        }
		    }*/

		    /*
		     * Zip the folders
		     */
		    private void zipFolder(String srcFolder, String destZipFile) throws Exception {
		        ZipOutputStream Zip = null;
		        FileOutputStream fileWriter = null;
		        /*
		         * create the output stream to Zip file result
		         */
		        fileWriter = new FileOutputStream(destZipFile);
		        Zip = new ZipOutputStream(fileWriter);
		        /*
		         * add the folder to the Zip
		         */
		        addFolderToZip("", srcFolder, Zip);
		        /*
		         * close the Zip objects
		         */
		        Zip.flush();
		        Zip.close();
		    }

		    /*
		     * recursively add files to the Zip files
		     */
		    private void addFileToZip(String path, String srcFile, ZipOutputStream Zip, boolean flag) throws Exception {
		        /*
		         * create the file object for inputs
		         */
		        File folder = new File(srcFile);

		        /*
		         * if the folder is empty add empty folder to the Zip file
		         */
		        if (flag == true) {
		            Zip.putNextEntry(new ZipEntry(path + "/" + folder.getName() + "/"));
		        } else { /*
		                 * if the current name is directory, recursively traverse it
		                 * to get the files
		                 */
		            if (folder.isDirectory()) {
		                /*
		                 * if folder is not empty
		                 */
		                addFolderToZip(path, srcFile, Zip);
		            } else {
		                /*
		                 * write the file to the output
		                 */
		                byte[] buf = new byte[1024];
		                int len;
		                FileInputStream in = new FileInputStream(srcFile);
		                Zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
		                while ((len = in.read(buf)) > 0) {
		                    /*
		                     * Write the Result
		                     */
		                    Zip.write(buf, 0, len);
		                }
		            }
		        }
		    }

		    /*
		     * add folder to the Zip file
		     */
		    private void addFolderToZip(String path, String srcFolder, ZipOutputStream Zip) throws Exception {
		        File folder = new File(srcFolder);

		        /*
		         * check the empty folder
		         */
		        if (folder.list().length == 0) {
		            System.out.println(folder.getName());
		            addFileToZip(path, srcFolder, Zip, true);
		        } else {
		            /*
		             * list the files in the folder
		             */
		            for (String fileName : folder.list()) {
		                if (path.equals("")) {
		                    addFileToZip(folder.getName(), srcFolder + "/" + fileName, Zip, false);
		                } else {
		                    addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, Zip, false);
		                }
		            }
		        }
		    }
		    //lo anterior ha funcionado solo la comprensión, veamos lo siguiente
		    
		    
		  /*  @RequestMapping (value = "/downloadFiles2", method = RequestMethod.GET) // Coincide con la solicitud de descarga en href
		    public static ResponseEntity<byte[]> downloadSingleFileToClient(String fileName){
		        // Cuando el archivo no existe, regresa vacío
		    	 String directorioZip = "D://plantillas//tmp//libretas//";
			        // ruta completa donde están los archivos a comprimir
			        File file = new File(directorioZip);
			        fileName ="prueba2";
		        if (!file.exists()) {
		            //logger.warn("el archivo no existe:{}", file);
		            return null;
		        }
		       // logger.info("Obtenga el archivo para descargarlo en el archivo local del usuario: {}",file);
		        HttpHeaders headers = new HttpHeaders();
		        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		        try {
		            headers.setContentDispositionFormData("attachment", new String(fileName.getBytes("utf-8"), "ISO8859-1"));
		        } catch (UnsupportedEncodingException e) {
		          //  logger.error("Excepción de codificación al analizar el nombre del archivo");
		            e.printStackTrace();
		        }
		        ResponseEntity<byte[]> result = null;
		        try {
		          //  logger.info("Empiece a escribir el archivo en el flujo de respuestas");
		            result = new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
		          //  logger.info("¡El archivo se descargó correctamente!");
		        } catch (IOException e) {
		          //  logger.error("No se pudo escribir el archivo en el flujo de respuesta");
		            e.printStackTrace();
		        }
		        return result;
		    }*/

		    
		    
		    
		    @RequestMapping (value = "/downloadFiles/", method = RequestMethod.GET) // Coincide con la solicitud de descarga en href
			@ResponseBody
			//@SystemControllerLog (descripción = "Descargar archivo")
			
			 
			 public void downloadFiles ( HttpServletRequest request,  HttpServletResponse response) {
		    	 // cadena que contiene la ruta donde están los archivos a comprimir
		        String directorioZip = "D://plantillas//tmp//libretas//";
		        // ruta completa donde están los archivos a comprimir
		        File carpetaComprimir = new File(directorioZip);

		        // valida si existe el directorio
		        if (carpetaComprimir.exists()) {
		            // lista los archivos que hay dentro del directorio
		            File[] ficheros = carpetaComprimir.listFiles();


		            // ciclo para recorrer todos los archivos a comprimir
		            for (int i = 0; i < ficheros.length; i++) {
		                System.out.println("Nombre del fichero: " + ficheros[i].getName());
		                String extension="";
		                for (int j = 0; j < ficheros[i].getName().length(); j++) {
		                    //obtiene la extensión del archivo
		                    if (ficheros[i].getName().charAt(j)=='.') {
		                        extension=ficheros[i].getName().substring(j, (int)ficheros[i].getName().length());
		                        //System.out.println(extension);
		                    }
		                }
		                try {
		                    // crea un buffer temporal para ir poniendo los archivos a comprimir
		                    ZipOutputStream zous = new ZipOutputStream(new FileOutputStream(directorioZip + ficheros[i].getName().replace(extension, ".zip")));

		                    //nombre con el que se va guardar el archivo 
		                    ZipEntry entrada = new ZipEntry(ficheros[i].getName());
		                    zous.putNextEntry(entrada);



		                        //obtiene el archivo para comprimir
		                        FileInputStream fis = new FileInputStream(directorioZip+entrada.getName());
		                        int leer;
		                        byte[] buffer = new byte[1024];
		                        while (0 < (leer = fis.read(buffer))) {
		                            zous.write(buffer, 0, leer);
		                        }
		                        fis.close();
		                        zous.closeEntry();
		                    zous.close();                   
		                } catch (FileNotFoundException e) {
		                    e.printStackTrace();
		                } catch (IOException e) {
		                    e.printStackTrace();
		                }               
		            }
		            System.out.println("Directorio de salida: " + directorioZip);
		        } else {
		            System.out.println("No se encontró el directorio..");
		        }
			/*  public ResponseEntity<byte[]> download(HttpServletRequest request,
			             @RequestParam("filename") String filename,
			             Model model)throws Exception {
			        // Descarga la ruta del archivo
			        String path = request.getServletContext().getRealPath("/images/");
			        File file = new File(path + File.separator + filename);
			        HttpHeaders headers = new HttpHeaders();  
			        // Descargue el nombre del archivo mostrado para resolver el problema del nombre chino confuso  
			        String downloadFielName = new String(filename.getBytes("UTF-8"),"iso-8859-1");
			        // Notifique al navegador que abra la imagen con el archivo adjunto (método de descarga)
			        headers.setContentDispositionFormData("attachment", downloadFielName); 
			        // application / octet-stream: datos de flujo binario (la descarga de archivos más común).
			        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),    
			                headers, HttpStatus.CREATED);  */

		     /*  try {
                    // ruta se refiere a la ruta del archivo a descargar.
			       File file = new File("D:\\plantillas\\tmp\\libretas\\");
			                    // Obtener el nombre del archivo.
			       String filename = file.getName();
			                    // Obtener el nombre de la extensión del archivo.
			       String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
			
			                    // Descarga el archivo como una secuencia.
			       InputStream fis = new BufferedInputStream(new FileInputStream("D:\\plantillas\\tmp\\libretas\\"));
			       byte[] buffer = new byte[fis.available()];
			       fis.read(buffer);
			       fis.close();
			                    // Respuesta clara
			       response.reset();
			                    // Establecer el encabezado de la respuesta
			       response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
			       response.addHeader("Content-Length", "" + file.length());
			       OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			       response.setContentType("application/octet-stream");
			       toClient.write(buffer);
			       toClient.flush();
			       toClient.close();
			   } catch (IOException ex) {
			       ex.printStackTrace();
			   }*/
			   //return response;

		    	
		    	//public class Ejecutor {

		    		//public static void main(String[] args) {
		    			// cadena que contiene la ruta donde están los archivos a comprimir
		    		/*	String directorioZip = "D:\\plantillas\\tmp\\libretas\\";
		    			// ruta completa donde están los archivos a comprimir
		    			File carpetaComprimir = new File(directorioZip);

		    			// valida si existe el directorio
		    			if (carpetaComprimir.exists()) {
		    				// lista los archivos que hay dentro del directorio
		    				File[] ficheros = carpetaComprimir.listFiles();
		    				System.out.println("Número de ficheros encontrados: " + ficheros.length);

		    				// ciclo para recorrer todos los archivos a comprimir
		    				for (int i = 0; i < ficheros.length; i++) {
		    					System.out.println("Nombre del fichero: " + ficheros[i].getName());
		    					String extension="";
		    					for (int j = 0; j < ficheros[i].getName().length(); j++) {
		    						//obtiene la extensión del archivo
		    						if (ficheros[i].getName().charAt(j)=='.') {
		    							extension=ficheros[i].getName().substring(j, (int)ficheros[i].getName().length());
		    							//System.out.println(extension);
		    						}
		    					}
		    					try {
		    						// crea un buffer temporal para ir poniendo los archivos a comprimir
		    						ZipOutputStream zous = new ZipOutputStream(new FileOutputStream(directorioZip + ficheros[i].getName().replace(extension, ".zip")));
		    						
		    						//nombre con el que se va guardar el archivo dentro del zip
		    						ZipEntry entrada = new ZipEntry(ficheros[i].getName());
		    						zous.putNextEntry(entrada);
		    						
		    							//System.out.println("Nombre del Archivo: " + entrada.getName());
		    							System.out.println("Comprimiendo.....");
		    							//obtiene el archivo para irlo comprimiendo
		    							FileInputStream fis = new FileInputStream(directorioZip+entrada.getName());
		    							int leer;
		    							byte[] buffer = new byte[1024];
		    							while (0 < (leer = fis.read(buffer))) {
		    								zous.write(buffer, 0, leer);
		    							}
		    							fis.close();
		    							zous.closeEntry();
		    						zous.close();					
		    					} catch (FileNotFoundException e) {
		    						e.printStackTrace();
		    					} catch (IOException e) {
		    						e.printStackTrace();
		    					}				
		    				}
		    				System.out.println("Directorio de salida: " + directorioZip);
		    			} else {
		    				System.out.println("No se encontró el directorio..");
		    			}*/
		    		//}
		    	//}
		    	/*try { //anterior
		    		 List<File> fileList = new ArrayList<File>(); // Colección de archivos
			    	  String zipFilePath = "D://plantillas//tmp//libretas//"; // La ubicación del caché zip, elimine el zip caché después de descargar el método en la clase de herramienta de método
			    	  downLoadFiles(fileList,zipFilePath,request,response); // Método de descarga de llamadas
				} catch (Exception e) {
					// TODO: handle exception
				}
		    	
		    	
		    	try {
		    		//CompresorZIP comp = new CompresorZIP();
				       // JFileChooser jfc = new JFileChooser();
				      //  jfc.showOpenDialog(jfc);
				       // File archivoSeleccionado = jfc.getSelectedFile();
				       // String parent = archivoSeleccionado.getParent();
				        //String nuevoParent = parent.replaceAll("\\\\", "\\\\\\\\");
				        String nuevoParent = "D://plantillas//tmp//libretas//";
				       // JOptionPane.showMessageDialog(null, "CARPETA SELECCIONADA -> " + nuevoParent);
				        String destino = nuevoParent + "prueba.zip";
				       // JOptionPane.showMessageDialog(null, "DESTINO -> " + destino);
				       // JOptionPane.showMessageDialog(null, "Comprimiendo...");
				        comprimir(nuevoParent, destino);
				        //JOptionPane.showMessageDialog(null, "Archivo ZIP creado correctamente !");
				} catch (Exception e) {
					// TODO: handle exception
				}
		    	*/
		    	
		    	
		    	
				
				/*try {
					 // registra la hora en que comenzó la descarga
					long begin_time = new Date().getTime();
			 
					 // crea un enlace URL
					 // Descargue un método de entrada del sitio web hao123, aquí está la dirección de descarga
					URL url = new URL("http://login.ae.edu.pe:8080/documentos/Contrato Colegio AE.pdf");
			 
					 // Obtener conexión
					URLConnection conn = url.openConnection();
			 
					 // Obtenga la ruta completa del archivo
					String fileName = url.getFile();
			 
					 // Obtener el nombre del archivo
					fileName = fileName.substring(fileName.lastIndexOf("/"));
			 
					 System.out.println ("Iniciar descarga >>>");
			 
					 // Obtener el tamaño del archivo
					int fileSize = conn.getContentLength();
			 
					 System.out.println ("Tamaño total del archivo:" + fileSize + "bytes");
			 
					 // Establecer el tamaño del bloque
					int blockSize = 1024 * 1024;
					 // número de bloques de archivos
					int blockNum = fileSize / blockSize;
			 
					if ((fileSize % blockSize) != 0) {
						blockNum += 1;
					}
			 
					 System.out.println ("Número de bloques-> Número de hilos:" + blockNum);
			 
					Thread[] threads = new Thread[blockNum];
					
					for (int i = 0; i < blockNum; i++) {
						 
						 // Variables necesarias para objetos de función anónimos
						final int index = i;
						final int finalBlockNum = blockNum;
						final String finalFileName = fileName;
			 
						 // crea un hilo
						threads[i] = new Thread() {
							public void run() {
								try {
			 
									 // Vuelva a adquirir la conexión
									URLConnection conn = url.openConnection();
									 // Vuelve a buscar la secuencia
									InputStream in = conn.getInputStream();
									 // define puntos de inicio y fin
									int beginPoint = 0, endPoint = 0;
			 
									 System.out.print ("sección" + (index + 1) + "archivo de bloque:");
									beginPoint = index * blockSize;
			 
									 // determina el punto final
									if (index < finalBlockNum - 1) {
										endPoint = beginPoint + blockSize;
									} else {
										endPoint = fileSize;
									}
			 
									 System.out.println ("Bytes iniciales:" + beginPoint + ", Bytes finales:" + endPoint);
			 
									 // Almacene el archivo descargado en una carpeta
									 // Cuando la carpeta no existe, cree una nueva
									File filePath = new File("E:/temp_file/");
									if (!filePath.exists()) {
										filePath.mkdirs();
									}
									
									FileOutputStream fos = new FileOutputStream(new File("E:/temp_file/", finalFileName + "_" + (index + 1)));
			 
									 // omite los bytes beginPoint para leer
									in.skip(beginPoint);
									byte[] buffer = new byte[1024];
									int count;
									 // define el progreso de descarga actual
									int process = beginPoint;
									 // El progreso actual debe ser menor que el número de bytes finales
									while (process < endPoint) {
			 
										count = in.read(buffer);
										 // determina si leer el último bloque
										if (process + count >= endPoint) {
											count = endPoint - process;
											process = endPoint;
										} else {
											 // calcular el progreso actual
											process += count;
										}
										 // guarda la secuencia del archivo
										fos.write(buffer, 0, count);
			 
									}
									fos.close();
									in.close();
			 
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
			 
						};
						threads[i].start();
			 
					}
					
					for (Thread t : threads) {
						t.join();
					}
			 
					 // Si la carpeta no existe, cree una carpeta
					File filePath = new File("C:/download/");
					if (!filePath.exists()) {
						filePath.mkdirs();
					}
					 // define la secuencia de salida del archivo
					FileOutputStream fos = new FileOutputStream("C:/download/" + fileName);
					for (int i = 0; i < blockNum; i++) {
						FileInputStream fis = new FileInputStream("C:/download/" + fileName + "_" + (i + 1));
						byte[] buffer = new byte[1024];
						int count;
						while ((count = fis.read(buffer)) > 0) {
							fos.write(buffer, 0, count);
						}
						fis.close();
					}
					fos.close();
			 
					long end_time = new Date().getTime();
					long seconds = (end_time - begin_time) / 1000;
					long minutes = seconds / 60;
					long second = seconds % 60;
			 
					 System.out.println ("Descarga completada, tiempo empleado:" + minutes + "minutos" + second + "segundos");
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e.getMessage());
				}*/

				 // Solo fusionar archivos cuando todos los hilos están terminados
				

		    	 
		   }

		   /* public static void main(String[] args) throws Exception {
		       
		    }*/
		
		@SuppressWarnings("unchecked")
		public String generaLibretaNuevo(CabeceraNotaReq cabecera,String nombrePDF,String nombreJSON, String anio, String nivel,String grado,String giro, String nom_periodo) throws Exception {

			//logger.debug("generaLibreta");
			//pOR AHORA COMENTADO
			String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();
			//Genero el json
			//String nombreJSON = cabecera.getPeriodo().replace(" ", "") + "-" + cabecera.getCod_alumno();
			//String nombreJSON = nombrePDF;
			//Comentado x ahora
			String rutaJson = "//home//aeedupeh//public_html//plantillas//tmp//" + nombreJSON + ".json";
			//String rutaJson = "D://plantillas//tmp//" + nombreJSON + ".json";
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
			//comentado x ahora
			 ruta=rutacARPETA  + "final_ireport_java/report.jrxml";
			//ruta= "D://plantillas//final_ireport_java//report.jrxml";
			
			//inputStream = new FileInputStream(ruta);
			

		   String JASPER_FILE_LOCATION="//home//aeedupeh//public_html//plantillas//final_ireport_java//";
		   String IMAGE_LOCATION = "//home//aeedupeh//public_html//plantillas//final_ireport_java//";
		   String SIGNATURE_IMG_LOCATION = "//home//aeedupeh//public_html//plantillas//final_ireport_java//";
		  // String JASPER_FILE_LOCATION="D://plantillas//final_ireport_java//";
		  // String IMAGE_LOCATION = "D://plantillas//final_ireport_java//";
		   //String SIGNATURE_IMG_LOCATION = "D://plantillas//final_ireport_java//";
			//Parametros
			Map<String, Object> parameters = new HashMap<>();
	        parameters.put(JsonQueryExecuterFactory.JSON_DATE_PATTERN, "yyyy-MM-dd");
	        parameters.put(JsonQueryExecuterFactory.JSON_NUMBER_PATTERN, "#,##0.##");
	        parameters.put(JsonQueryExecuterFactory.JSON_LOCALE, Locale.ENGLISH);
	        parameters.put(JRParameter.REPORT_LOCALE, Locale.US);
	        parameters.put(JsonQueryExecuterFactory.JSON_SOURCE,rutaJson);
	        parameters.put("SUBREPORT_DIR", JASPER_FILE_LOCATION);
	        parameters.put("IMAGE_LOCATION",IMAGE_LOCATION );
	        parameters.put("SIGNATURE_IMG_LOCATION",SIGNATURE_IMG_LOCATION);
	        
	        System.out.println( "generating jasper report..." );
	        String compiledFile = JASPER_FILE_LOCATION+"report.jasper"; 
	       // Map<String, Object> parameters = getParameters();
	        JasperPrint jasperPrint = JasperFillManager.fillReport(compiledFile, parameters);
	        //String rutacARPETA="D://plantillas//";
	        //Crear carpetas por año y Grado
	        File directorio = new File(rutacARPETA+"/tmp/libretas/"+anio);
	        if (!directorio.exists()) {
	            if (directorio.mkdirs()) {
	                System.out.println("Directorio creado");
	            } else {
	                System.out.println("Error al crear directorio");
	            }
	        }
	        
	       
           	 File directorio_libreta_grado = new File(rutacARPETA+"/tmp/libretas/"+anio+"/"+giro+"/"+nivel+"/"+grado+"/"+nom_periodo);
             if (!directorio_libreta_grado.exists()) {
            	 if (directorio_libreta_grado.mkdirs()) {
 	                System.out.println("Directorio creado");
 	            } else {
 	                System.out.println("Error al crear directorio");
 	            }
             }
	            
	  
	        
	      //  String pdfGenerado =rutacARPETA + "/tmp/libretas/" + nombrePDF +".pdf";
	        String pdfGenerado =rutacARPETA + "/tmp/libretas/"+anio+"/"+giro+"/"+nivel+"/" +grado+"/" +nom_periodo+"/"+ nombrePDF +".pdf";
			JasperExportManager.exportReportToPdfFile(jasperPrint, pdfGenerado);
			
			//logger.debug("despues de JasperExportManager.exportReportToPdfFile");
			
			//document.close();
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
		
		private static Map<String, Object> getParameters() throws FileNotFoundException{
			
			String cabecera="";
			
			
			
	        Map<String, Object> parameters = new HashMap<>();
	        parameters.put(JsonQueryExecuterFactory.JSON_DATE_PATTERN, "yyyy-MM-dd");
	        parameters.put(JsonQueryExecuterFactory.JSON_NUMBER_PATTERN, "#,##0.##");
	        parameters.put(JsonQueryExecuterFactory.JSON_LOCALE, Locale.ENGLISH);
	        parameters.put(JRParameter.REPORT_LOCALE, Locale.US);
	        //parameters.put(JsonQueryExecuterFactory.JSON_SOURCE,JSON_DATA_FILE);
	        parameters.put(JsonQueryExecuterFactory.JSON_INPUT_STREAM, cabecera);
	        parameters.put("SUBREPORT_DIR", JASPER_FILE_LOCATION);
	        parameters.put("IMAGE_LOCATION",IMAGE_LOCATION );
	        return parameters;
	    }

	    /**
	     * @param args the command line arguments
	     */
	    public static void main(String[] args) throws FileNotFoundException, JRException{
	        // TODO code application logic here
	        System.out.println( "generating jasper report..." );
	        String compiledFile = JASPER_FILE_LOCATION+"report.jasper"; 
	        Map<String, Object> parameters = getParameters();
	        JasperPrint jasperPrint = JasperFillManager.fillReport(compiledFile, 
	                parameters);
	        JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);

	    }
	    
	    /**
		 * Exportar a excel la seccion con su codigo de barras
		 * @param response
		 * @param id_au
		 * @throws Exception
		 */
		@RequestMapping(value = "/seccion/{id_au}/{id_gir}/{id_cic}")
		@ResponseBody
		public void getLista(HttpServletResponse response, @PathVariable Integer id_au, @PathVariable Integer id_gir,  @PathVariable Integer id_cic) throws Exception {

			Aula aula = aulaDAO.getFull(id_au, new String[] { Grad.TABLA, Nivel.TABLA });
			Integer id_=aula.getId_per();
			String tipo="";
			String nom_cic="";
			if(id_gir.equals(1)) {
				 tipo = "C";
			} else if(id_gir.equals(2)) {
				 tipo = "A";
				 Ciclo ciclo =cicloDAO.get(id_cic);
				 nom_cic=ciclo.getNom();
			} else if(id_gir.equals(3)) {
				 tipo = "V";
			}
			
			Periodo periodo= periodoDAO.get(aula.getId_per());
			Sucursal sucursal = sucursalDAO.get(periodo.getId_suc());
			String sede ="";
			if(sucursal.getId().equals(2)) {
				sede = "SEDE 1";
			} else if(sucursal.getId().equals(3)) {
				sede = "SEDE 2";
			} if(sucursal.getId().equals(4)) {
				sede = "SEDE 3";
			}

			//String grado = aula.getGrad().getNivel().getNom() + "-" + aula.getGrad().getNom() + "-" + aula.getSecc();
			String grado="";
			if(id_gir.equals(2)) {
				if(nom_cic.length()>=20)
					grado = nom_cic.substring(0, 20) + "-" + aula.getGrad().getNom()+ "-" + aula.getSecc() ;
				else
					 grado = nom_cic+"-" + aula.getGrad().getNom()+ "-" + aula.getSecc() ;
			} else {
				 grado = sede+" - "+aula.getGrad().getNivel().getNom() + "-" + aula.getGrad().getNom()+ "-" + aula.getSecc() ;
			}

			List<Map<String, Object>> listaAlumnos = matriculaDAO.reporteCodigoBarras(id_au);
			for (Map<String, Object> map : listaAlumnos) {
				String nombre = map.get("nom").toString();
				String ape_pat = map.get("ape_pat").toString();
				//String tipo = map.get("tipo").toString();
				nombre = nombre.split(" ")[0];
				//map.put("alumno", map.get("ape_pat") + " " + map.get("ape_mat") + ",");
				if (nombre.length()>10)
					nombre = nombre.substring(0, 10);
				if (ape_pat.length()>10)
					ape_pat = ape_pat.substring(0, 10);
				map.put("nombre", nombre+" "+ape_pat);
				map.put("grado", grado);
				map.put("cod", map.get("cod") + "-" + String.format("%02d", Integer.parseInt(map.get("periodo").toString())));
				
				//WordToPDFUtil util = new WordToPDFUtil();
				//String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();
				//funcionaba mal
				/*Row map2 = new Row();
				String nombre = map.get("nom").toString();
				//nombre = nombre.split(" ")[0];
				map2.put("alumno", map.get("ape_pat") + " " + map.get("ape_mat") + ",");
				if (nombre.length()>27)
					nombre = nombre.substring(0, 27);
				map2.put("nombre", nombre);
				map2.put("grado", grado);
				String cod=map.get("cod") + "-" + String.format("%02d", Integer.parseInt(map.get("periodo").toString()));
				//map2.put("cod", map.get("cod") + "-" + String.format("%02d", Integer.parseInt(map.get("periodo").toString())));
				 /*float[] _margenes = new float[]{0,5,2,10};
			        float _width = 134;
			        float _fontSize = 8;
				 float left = _margenes[0];
			        float right = _margenes[1];
			        float top = _margenes[2];
			        float bottom = _margenes[3];
				 Document document = new Document();
			        Rectangle one = new Rectangle(154,118);
			        document.setPageSize(one);
			        document.setMargins(left, right, top, bottom);*/
			        
			       /* Document document = new Document(PageSize.A4, left, right, top, bottom);
			        
			        ByteArrayOutputStream out = new ByteArrayOutputStream();            

			        PdfWriter writer = PdfWriter.getInstance(document, out);
			        document.open();
			        document.setMargins(left, right, 0, bottom);*/
			       /* Barcode39 barcode39 = new Barcode39();
			    	barcode39.setCode(cod);
			    	barcode39.createImageWithBarcode(writer.getDirectContent(), BaseColor.BLACK, BaseColor.GRAY);*/
			
				   // Agregamos la imagen del codigo QR al documento
				  // documento.add(codigoBarrasQR.getImage());
			    	/*BarcodeEAN codeEAN = new BarcodeEAN(); 
			    	codeEAN.setCodeType(codeEAN.EAN13); 
			    	codeEAN.setCode("cod"); 
			    	Image imageEAN = codeEAN.createImageWithBarcode(cb, null, null);*/

			    //	Fuente: https://www.iteramos.com/pregunta/42339/generador-de-imagenes-de-codigo-de-barras-en-java
			//	BarcodeQRCode codigoBarrasQR = new BarcodeQRCode(cod, 0, 0, null);
				//funcionaba mal
			//	map2.put("codigo",cod);
				
				
				
				

			}
			
			WordToPDFUtil util = new WordToPDFUtil();
			String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();
			Row carnet = new Row();
			carnet.put("listaAlumnos", listaAlumnos);
			String pdf = util.createPdfCarnet(rutacARPETA, carnet, tipo );
			downloadHtml(response, pdf,grado);
			//return pdf;
			//byte[] fileContent = Files.readAllBytes(new File(pdf).toPath());
			//String DIR = rutacARPETA + "carnetColegio/";
			//String html = util.createPdf2(PageSize.A4.rotate(), 1, carnet, DIR+"carnet.html");
			//URL url = new URL("C:/plantillas/carnetColegio/tmp/carnet-2022-03-11-18473421.html");
			/* URL fetchWebsite = new URL("C:\\plantillas\\carnetColegio\\tmp\\carnet-2022-03-11-18473421.html");
		        ReadableByteChannel readableByteChannel = Channels.newChannel(fetchWebsite.openStream());

		        try (FileOutputStream fos = new FileOutputStream("C:\\Users\\Downloads\\carnets.html")) {
		            fos.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
		        }*/
			/*String url = DIR+"tmp/prueba.txt"; //dirección url del recurso a descargar
			String name = "carnets.txt"; //nombre del archivo destino
			//Directorio destino para las descargas
			String folder = "C:\\Downloads\\";

			//Crea el directorio de destino en caso de que no exista
			File dir = new File(folder);

			if (!dir.exists())
			  if (!dir.mkdir())
			    return; // no se pudo crear la carpeta de destino
			
			File file = new File(folder + name);
			
			URLConnection conn = new URL(url).openConnection();
			conn.connect();
			System.out.println("\nempezando descarga: \n");
			System.out.println(">> URL: " + url);
			System.out.println(">> Nombre: " + name);
			System.out.println(">> tamaño: " + conn.getContentLength() + " bytes");
			
			InputStream in = conn.getInputStream();
			OutputStream out = new FileOutputStream(file);
			
			int b = 0;
			while (b != -1) {
			  b = in.read();
			  if (b != -1)
			    out.write(b);
			}
			
			out.close();
			in.close();*/
			
			/*String fileName = url.getFile();
			String destName = "./dowloands" + fileName.substring(fileName.lastIndexOf("/"));
			System.out.println(destName);
		 
			InputStream is = url.openStream();
			OutputStream os = new FileOutputStream(destName);
		 
			byte[] b = new byte[2048];
			int length;
		 
			while ((length = is.read(b)) != -1) {
				os.write(b);
			}
		 
			is.close();
			os.close();*/
			 
			//byte[] fileContent = Files.readAllBytes(new File(pdf).toPath());
			
			/*CodigoBarrasUtil codigoBarras = new CodigoBarrasUtil();

	        float[] _margenes = new float[]{0,5,2,10};
	        float _width = 134;
	        float _fontSize = 8;
	        
			InputStream is = codigoBarras.createPdf(_width, _margenes, _fontSize, listaAlumnos);

			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment; filename=seccion.pdf");

			IOUtils.copy(is, response.getOutputStream());*/
			
			/*String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();
			
			InputStream inputStream = null;
			String ruta = rutacARPETA + "/carnetAE/carnet_AE.jrxml";

			inputStream = new FileInputStream(ruta);

			// grilla
			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(listaAlumnos);
			HashMap<String, Object> parameters = new HashMap<String, Object>();

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);
			byte[] bytesPDF = JasperExportManager.exportReportToPdf(jasperPrint);

			//PdfReader reader = new PdfReader(bytesPDF);

			//copy.addPage(copy.getImportedPage(reader, 1));

			//document.close();
			
			return bytesPDF; //baos.toByteArray();*/

			/* Por ahora vamos a hacer las pruebas

			CodigoBarrasUtil codigoBarras = new CodigoBarrasUtil();

	        float[] _margenes = new float[]{0,5,2,10};
	        float _width = 134;
	        float _fontSize = 8;
	        
			InputStream is = codigoBarras.createPdf(_width, _margenes, _fontSize, listaAlumnos);

			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment; filename=seccion.pdf");

			IOUtils.copy(is, response.getOutputStream());*/
			

		}	
		
	    /**
		 * Exportar a excel la seccion con su codigo de barras
		 * @param response
		 * @param id_au
		 * @throws Exception
		 */
		@RequestMapping(value = "/seccion2/{id_au}/{id_gir}/{id_cic}")
		@ResponseBody
		public void getLista2(HttpServletResponse response, @PathVariable Integer id_au, @PathVariable Integer id_gir,  @PathVariable Integer id_cic) throws Exception {

			Aula aula = aulaDAO.getFull(id_au, new String[] { Grad.TABLA, Nivel.TABLA });
			Integer id_=aula.getId_per();
			String tipo="";
			String nom_cic="";
			if(id_gir.equals(1)) {
				 tipo = "C";
			} else if(id_gir.equals(2)) {
				 tipo = "A";
				 Ciclo ciclo =cicloDAO.get(id_cic);
				 nom_cic=ciclo.getNom();
			} else if(id_gir.equals(3)) {
				 tipo = "V";
			}
			
			Periodo periodo= periodoDAO.get(aula.getId_per());
			Sucursal sucursal = sucursalDAO.get(periodo.getId_suc());
			String sede ="";
			if(sucursal.getId().equals(2)) {
				sede = "SEDE 1";
			} else if(sucursal.getId().equals(3)) {
				sede = "SEDE 2";
			} if(sucursal.getId().equals(4)) {
				sede = "SEDE 3";
			}

			//String grado = aula.getGrad().getNivel().getNom() + "-" + aula.getGrad().getNom() + "-" + aula.getSecc();
			String grado="";
			if(id_gir.equals(2)) {
				if(nom_cic.length()>=20)
					grado = nom_cic.substring(0, 20) + "-" + aula.getGrad().getNom()+ "-" + aula.getSecc() ;
				else
					 grado = nom_cic+"-" + aula.getGrad().getNom()+ "-" + aula.getSecc() ;
			} else {
				 grado = sede+" - "+aula.getGrad().getNivel().getNom() + "-" + aula.getGrad().getNom()+ "-" + aula.getSecc() ;
			}

			List<Map<String, Object>> listaAlumnos = matriculaDAO.reporteCodigoBarras(id_au);
			for (Map<String, Object> map : listaAlumnos) {
				String nombre = map.get("nom").toString();
				String ape_pat = map.get("ape_pat").toString();
				//String tipo = map.get("tipo").toString();
				nombre = nombre.split(" ")[0];
				//map.put("alumno", map.get("ape_pat") + " " + map.get("ape_mat") + ",");
				if (nombre.length()>10)
					nombre = nombre.substring(0, 10);
				if (ape_pat.length()>10)
					ape_pat = ape_pat.substring(0, 10);
				map.put("nombre", nombre+" "+ape_pat);
				map.put("nombres", nombre);
				map.put("alumno", map.get("ape_pat") + " " + map.get("ape_mat") + ",");
				map.put("grado", grado);
				map.put("cod", map.get("cod") + "-" + String.format("%02d", Integer.parseInt(map.get("periodo").toString())));
				
				//WordToPDFUtil util = new WordToPDFUtil();
				//String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();
				//funcionaba mal
				/*Row map2 = new Row();
				String nombre = map.get("nom").toString();
				//nombre = nombre.split(" ")[0];
				map2.put("alumno", map.get("ape_pat") + " " + map.get("ape_mat") + ",");
				if (nombre.length()>27)
					nombre = nombre.substring(0, 27);
				map2.put("nombre", nombre);
				map2.put("grado", grado);
				String cod=map.get("cod") + "-" + String.format("%02d", Integer.parseInt(map.get("periodo").toString()));
				//map2.put("cod", map.get("cod") + "-" + String.format("%02d", Integer.parseInt(map.get("periodo").toString())));
				 /*float[] _margenes = new float[]{0,5,2,10};
			        float _width = 134;
			        float _fontSize = 8;
				 float left = _margenes[0];
			        float right = _margenes[1];
			        float top = _margenes[2];
			        float bottom = _margenes[3];
				 Document document = new Document();
			        Rectangle one = new Rectangle(154,118);
			        document.setPageSize(one);
			        document.setMargins(left, right, top, bottom);*/
			        
			       /* Document document = new Document(PageSize.A4, left, right, top, bottom);
			        
			        ByteArrayOutputStream out = new ByteArrayOutputStream();            

			        PdfWriter writer = PdfWriter.getInstance(document, out);
			        document.open();
			        document.setMargins(left, right, 0, bottom);*/
			       /* Barcode39 barcode39 = new Barcode39();
			    	barcode39.setCode(cod);
			    	barcode39.createImageWithBarcode(writer.getDirectContent(), BaseColor.BLACK, BaseColor.GRAY);*/
			
				   // Agregamos la imagen del codigo QR al documento
				  // documento.add(codigoBarrasQR.getImage());
			    	/*BarcodeEAN codeEAN = new BarcodeEAN(); 
			    	codeEAN.setCodeType(codeEAN.EAN13); 
			    	codeEAN.setCode("cod"); 
			    	Image imageEAN = codeEAN.createImageWithBarcode(cb, null, null);*/

			    //	Fuente: https://www.iteramos.com/pregunta/42339/generador-de-imagenes-de-codigo-de-barras-en-java
			//	BarcodeQRCode codigoBarrasQR = new BarcodeQRCode(cod, 0, 0, null);
				//funcionaba mal
			//	map2.put("codigo",cod);
				
				
				
				

			}
			
			/*WordToPDFUtil util = new WordToPDFUtil();
			String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();
			Row carnet = new Row();
			carnet.put("listaAlumnos", listaAlumnos);
			String pdf = util.createPdfCarnet(rutacARPETA, carnet, tipo );
			downloadHtml(response, pdf,grado);*/
			//return pdf;
			//byte[] fileContent = Files.readAllBytes(new File(pdf).toPath());
			//String DIR = rutacARPETA + "carnetColegio/";
			//String html = util.createPdf2(PageSize.A4.rotate(), 1, carnet, DIR+"carnet.html");
			//URL url = new URL("C:/plantillas/carnetColegio/tmp/carnet-2022-03-11-18473421.html");
			/* URL fetchWebsite = new URL("C:\\plantillas\\carnetColegio\\tmp\\carnet-2022-03-11-18473421.html");
		        ReadableByteChannel readableByteChannel = Channels.newChannel(fetchWebsite.openStream());

		        try (FileOutputStream fos = new FileOutputStream("C:\\Users\\Downloads\\carnets.html")) {
		            fos.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
		        }*/
			/*String url = DIR+"tmp/prueba.txt"; //dirección url del recurso a descargar
			String name = "carnets.txt"; //nombre del archivo destino
			//Directorio destino para las descargas
			String folder = "C:\\Downloads\\";

			//Crea el directorio de destino en caso de que no exista
			File dir = new File(folder);

			if (!dir.exists())
			  if (!dir.mkdir())
			    return; // no se pudo crear la carpeta de destino
			
			File file = new File(folder + name);
			
			URLConnection conn = new URL(url).openConnection();
			conn.connect();
			System.out.println("\nempezando descarga: \n");
			System.out.println(">> URL: " + url);
			System.out.println(">> Nombre: " + name);
			System.out.println(">> tamaño: " + conn.getContentLength() + " bytes");
			
			InputStream in = conn.getInputStream();
			OutputStream out = new FileOutputStream(file);
			
			int b = 0;
			while (b != -1) {
			  b = in.read();
			  if (b != -1)
			    out.write(b);
			}
			
			out.close();
			in.close();*/
			
			/*String fileName = url.getFile();
			String destName = "./dowloands" + fileName.substring(fileName.lastIndexOf("/"));
			System.out.println(destName);
		 
			InputStream is = url.openStream();
			OutputStream os = new FileOutputStream(destName);
		 
			byte[] b = new byte[2048];
			int length;
		 
			while ((length = is.read(b)) != -1) {
				os.write(b);
			}
		 
			is.close();
			os.close();*/
			 
			//byte[] fileContent = Files.readAllBytes(new File(pdf).toPath());
			
			/*CodigoBarrasUtil codigoBarras = new CodigoBarrasUtil();

	        float[] _margenes = new float[]{0,5,2,10};
	        float _width = 134;
	        float _fontSize = 8;
	        
			InputStream is = codigoBarras.createPdf(_width, _margenes, _fontSize, listaAlumnos);

			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment; filename=seccion.pdf");

			IOUtils.copy(is, response.getOutputStream());*/
			
			/*String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();
			
			InputStream inputStream = null;
			String ruta = rutacARPETA + "/carnetAE/carnet_AE.jrxml";

			inputStream = new FileInputStream(ruta);

			// grilla
			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(listaAlumnos);
			HashMap<String, Object> parameters = new HashMap<String, Object>();

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);
			byte[] bytesPDF = JasperExportManager.exportReportToPdf(jasperPrint);

			//PdfReader reader = new PdfReader(bytesPDF);

			//copy.addPage(copy.getImportedPage(reader, 1));

			//document.close();
			
			return bytesPDF; //baos.toByteArray();*/

			// Por ahora vamos a hacer las pruebas

			CodigoBarrasUtil codigoBarras = new CodigoBarrasUtil();

	        float[] _margenes = new float[]{0,5,2,10};
	        float _width = 134;
	        float _fontSize = 9;
	        
			InputStream is = codigoBarras.createPdf(_width, _margenes, _fontSize, listaAlumnos);

			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment; filename=seccion.pdf");

			IOUtils.copy(is, response.getOutputStream());
			

		}	
		
		@RequestMapping(value = "/generarCarnet/{id_alu}")
		@ResponseBody
		public void generarCarnet(HttpServletResponse response, @PathVariable Integer id_alu) throws Exception {
			
			Row matricula = matriculaDAO.getMatricula(id_alu, 6);
			Alumno alumno = alumnoDAO.get(id_alu);
			Aula aula = aulaDAO.getFull(matricula.getInteger("id_au_asi"), new String[] { Grad.TABLA, Nivel.TABLA });
			Periodo periodo= periodoDAO.get(aula.getId_per());
			Sucursal sucursal = sucursalDAO.get(periodo.getId_suc());
			String sede ="";
			if(sucursal.getId().equals(2)) {
				sede = "SEDE 1";
			} else if(sucursal.getId().equals(3)) {
				sede = "SEDE 2";
			} if(sucursal.getId().equals(4)) {
				sede = "SEDE 3";
			}

			//String grado = aula.getGrad().getNivel().getNom() + "-" + aula.getGrad().getNom() + "-" + aula.getSecc();
			String grado = sede+" - "+aula.getGrad().getNivel().getNom() + "-" + aula.getGrad().getNom()+ "-" + aula.getSecc() ;

			List<Map<String, Object>> listaAlumnos = matriculaDAO.alumnoCodigoBarras(matricula.getInteger("id"));
			String cod=alumno.getCod();
			for (Map<String, Object> map : listaAlumnos) {
				String nombre = map.get("nom").toString();
				nombre=nombre.split(" ")[0];
				String ape_pat = map.get("ape_pat").toString();
				//nombre = nombre.split(" ")[0];
				//map.put("alumno", map.get("ape_pat") + " " + map.get("ape_mat") + ",");
				if (nombre.length()>10)
					nombre = nombre.substring(0, 10);
				if (ape_pat.length()>10)
					ape_pat = ape_pat.substring(0, 10);
				map.put("nombre", nombre+" "+ape_pat);
				map.put("grado", grado);
				map.put("cod", map.get("cod") + "-" + String.format("%02d", Integer.parseInt(map.get("periodo").toString())));	
				
			}
			
			WordToPDFUtil util = new WordToPDFUtil();
			String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();
			Row carnet = new Row();
			carnet.put("listaAlumnos", listaAlumnos);
			String pdf = util.createPdfCarnetAlu(rutacARPETA, carnet,cod );
			downloadHtmlCarnetAlu(response, pdf,cod);
			

		}	
		
		
		public void downloadHtml(HttpServletResponse response, String ruta, String aula) throws Exception {
		       // leer en la secuencia
			   //String rutaCarpeta= "c:\\contratos\\";
			   //Para el servidor
			   //String rutaCarpeta= "//home//aeedupeh//public_html//plantillas//tmp//contratos//";
			   			   
		       InputStream inStream = new FileInputStream (ruta); // Ruta de almacenamiento de archivos
		       // Establecer el formato de salida
		       
		       //String name = URLEncoder.encode("Contrato" + contrato + ".docx", "UTF-8");
				//name = URLDecoder.decode(name, "ISO8859_1");
		       
		       //InputStream is = FileUtils.openInputStream(initialFile);
		    	
				
				response.setContentType("text/html;charset=UTF-8");
		       //response.setContentType("application/xhtml+xml");
		       response.setCharacterEncoding("utf-8");
				response.addHeader("Content-Disposition", "attachment; filename=carnet" + aula + ".html");

				IOUtils.copy(inStream, response.getOutputStream());
		       /* response.reset();
		        response.setContentType("application/pdf");
		        response.addHeader("Content-Disposition", "attachment; filename=\"" + contrato + "\"");
		        // Recorre los datos en la secuencia
		        byte[] b = new byte[1024];
		        int len;
		        try {
		            while ((len = inStream.read(b)) > 0)
		                response.getOutputStream().write(b, 0, len);
		            inStream.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }*/
		}
		
		public void downloadHtmlCarnetAlu(HttpServletResponse response, String ruta, String cod) throws Exception {
		       // leer en la secuencia
			   //String rutaCarpeta= "c:\\contratos\\";
			   //Para el servidor
			   //String rutaCarpeta= "//home//aeedupeh//public_html//plantillas//tmp//contratos//";
			   			   
		       InputStream inStream = new FileInputStream (ruta); // Ruta de almacenamiento de archivos
		       // Establecer el formato de salida
		       
		       //String name = URLEncoder.encode("Contrato" + contrato + ".docx", "UTF-8");
				//name = URLDecoder.decode(name, "ISO8859_1");
		       
		       //InputStream is = FileUtils.openInputStream(initialFile);
		    	
				
				response.setContentType("text/html;charset=UTF-8");
		       //response.setContentType("application/xhtml+xml");
		       response.setCharacterEncoding("utf-8");
				response.addHeader("Content-Disposition", "attachment; filename=carnet" + cod + ".html");

				IOUtils.copy(inStream, response.getOutputStream());
		       /* response.reset();
		        response.setContentType("application/pdf");
		        response.addHeader("Content-Disposition", "attachment; filename=\"" + contrato + "\"");
		        // Recorre los datos en la secuencia
		        byte[] b = new byte[1024];
		        int len;
		        try {
		            while ((len = inStream.read(b)) > 0)
		                response.getOutputStream().write(b, 0, len);
		            inStream.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }*/
		}
		
		  public java.io.InputStream  createCodigo(float _ancho,float[] _margenes, float _fontSize,String cod) throws IOException, DocumentException {
		    	
		        float left = _margenes[0];
		        float right = _margenes[1];
		        float top = _margenes[2];
		        float bottom = _margenes[3];
		        
		        Document document = new Document();
		        Rectangle one = new Rectangle(154,118);
		        document.setPageSize(one);
		        document.setMargins(left, right, top, bottom);
		        /*
		        Document document = new Document(PageSize.A4, left, right, top, bottom);
		        */
		        ByteArrayOutputStream out = new ByteArrayOutputStream();            

		        PdfWriter writer = PdfWriter.getInstance(document, out);
		        document.open();
		        document.setMargins(left, right, 0, bottom);
		        
		       
		        
		        
		        
		        
		        document.add( createBarcode(writer, cod ));
		        document.close();
		        
		        return new ByteArrayInputStream(out.toByteArray());

		    }
		  
		  public static PdfPCell createBarcode(PdfWriter writer, String code) throws DocumentException, IOException {
		    	Barcode39 barcode39 = new Barcode39();
		    	barcode39.setCode(code);
		        PdfPCell cell = new PdfPCell(barcode39.createImageWithBarcode(writer.getDirectContent(), BaseColor.BLACK, BaseColor.GRAY), true);
		        cell.setBorder(Rectangle.NO_BORDER);

		        cell.setPadding(2);
		        cell.setPaddingTop(5);
		        return cell;
		    }
		  
		  @RequestMapping(value = "/exportarDirectorioPPFF")
			@ResponseBody
			public void descargarExcel(HttpServletRequest request,HttpServletResponse response,Integer id_au, Integer id_niv, Integer id_grad, Integer id_anio, String usuario, Integer id_gir, Integer id_suc, Integer id_cic)  throws Exception {
			  
				ExcelXlsUtil xls = new ExcelXlsUtil();
				List<Row> list = matriculaDAO.listaDatosComunicacionPadres(id_au, id_niv, id_grad, id_anio, id_gir, id_suc, id_cic);
				
				response.setContentType("application/vnd.ms-excel");
				
				String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();
				//String  rutacARPETA =  "C:/plantillas/";

				Nivel nivel = nivelDAO.get(id_niv);
				String tutor = usuario;
				
				String archivo = xls.generaExcelReporteDatosComunicacion(rutacARPETA, "plantilla_directorio_ppff.xls", nivel.getNom(), tutor, list);
		        DateFormat dateFormat = new java.text.SimpleDateFormat("mm-dd hh-mm-ss");  

				response.setHeader("Content-Disposition","attachment;filename=Reporte_Directorio" + dateFormat.format(new Date())  + ".xls");

				File initialFile = new File(archivo);
			    InputStream is = FileUtils.openInputStream(initialFile);
			    	
				IOUtils.copy(is, response.getOutputStream());
				
			}
		  
			/**
			 * Exportar a excel la seccion con su codigo de barras
			 * @param response
			 * @param id_au
			 * @throws Exception
			 */
			@RequestMapping(value = "/alumno/{id_mat}")
			@ResponseBody
			public void getAlumno(HttpServletResponse response, @PathVariable Integer id_mat) throws Exception {



				List<Map<String, Object>> listaAlumnos = matriculaDAO.alumnoCodigoBarras(id_mat);
				for (Map<String, Object> map : listaAlumnos) {
					
					/*String grado = map.get("nivel") + "-" + map.get("grado") + "-" + map.get("secc");

					String nombre = map.get("nom").toString();
					//nombre = nombre.split(" ")[0];
					map.put("alumno", map.get("ape_pat") + " " + map.get("ape_mat") + ",");
					if (nombre.length()>27)
						nombre = nombre.substring(0, 27);
					map.put("nombre", nombre);
					map.put("nombres", nombre);
					map.put("grado", grado);
					map.put("cod", map.get("cod") + "-" + String.format("%02d", Integer.parseInt(map.get("periodo").toString())));*/
					
					//Aqui
					
					Aula aula = aulaDAO.getFull(Integer.parseInt(map.get("id_au").toString()), new String[] { Grad.TABLA, Nivel.TABLA });
					Integer id_=aula.getId_per();
					String tipo="";
					String nom_cic="";
					Ciclo ciclo = cicloDAO.get(Integer.parseInt(map.get("id_cic").toString()));
					Integer id_cic= ciclo.getId();
					Integer id_gir=Integer.parseInt(map.get("id_gir").toString());
					if(id_gir.equals(1)) {
						 tipo = "C";
					} else if(id_gir.equals(2)) {
						 tipo = "A";
						// Ciclo ciclo =cicloDAO.get(id_cic);
						 nom_cic=ciclo.getNom();
					} else if(id_gir.equals(3)) {
						 tipo = "V";
					}
					
					Periodo periodo= periodoDAO.get(aula.getId_per());
					Sucursal sucursal = sucursalDAO.get(periodo.getId_suc());
					String sede ="";
					if(sucursal.getId().equals(2)) {
						sede = "SEDE 1";
					} else if(sucursal.getId().equals(3)) {
						sede = "SEDE 2";
					} if(sucursal.getId().equals(4)) {
						sede = "SEDE 3";
					}

					//String grado = aula.getGrad().getNivel().getNom() + "-" + aula.getGrad().getNom() + "-" + aula.getSecc();
					String grado="";
					if(id_gir.equals(2)) {
						if(nom_cic.length()>=20)
							grado = nom_cic.substring(0, 20) + "-" + aula.getGrad().getNom()+ "-" + aula.getSecc() ;
						else
							 grado = nom_cic+"-" + aula.getGrad().getNom()+ "-" + aula.getSecc() ;
					} else {
						 grado = sede+" - "+aula.getGrad().getNivel().getNom() + "-" + aula.getGrad().getNom()+ "-" + aula.getSecc() ;
					}

					String nombre = map.get("nom").toString();
					String ape_pat = map.get("ape_pat").toString();
					//String tipo = map.get("tipo").toString();
					nombre = nombre.split(" ")[0];
					//map.put("alumno", map.get("ape_pat") + " " + map.get("ape_mat") + ",");
					if (nombre.length()>10)
						nombre = nombre.substring(0, 10);
					if (ape_pat.length()>10)
						ape_pat = ape_pat.substring(0, 10);
					map.put("nombre", nombre+" "+ape_pat);
					map.put("nombres", nombre);
					map.put("alumno", map.get("ape_pat") + " " + map.get("ape_mat") + ",");
					map.put("grado", grado);
					map.put("cod", map.get("cod") + "-" + String.format("%02d", Integer.parseInt(map.get("periodo").toString())));
				}


				CodigoBarrasUtil codigoBarras = new CodigoBarrasUtil();

		        float[] _margenes = new float[]{0,5,2,10};
		        float _width = 134;
		        float _fontSize = 8;
		        
				InputStream is = codigoBarras.createPdf(_width, _margenes, _fontSize, listaAlumnos);

				response.setContentType("application/pdf");
				response.addHeader("Content-Disposition", "attachment; filename=seccion.pdf");

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

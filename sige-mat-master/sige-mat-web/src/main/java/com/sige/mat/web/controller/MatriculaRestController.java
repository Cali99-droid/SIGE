package com.sige.mat.web.controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sige.common.enums.EnumConceptoPago;
import com.sige.common.enums.EnumCondicionMatricula;
import com.sige.common.enums.EnumTipoMovimiento;
import com.sige.core.dao.cache.CacheManager;
import com.sige.invoice.bean.Impresion;
import com.sige.mat.dao.AcademicoPagoDAO;
import com.sige.mat.dao.AlumnoDAO;
import com.sige.mat.dao.AlumnoDescuentoDAO;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.AulaDAO;
import com.sige.mat.dao.BancoDAO;
import com.sige.mat.dao.CicloDAO;
import com.sige.mat.dao.CicloTurnoDAO;
import com.sige.mat.dao.ConfCuotaDAO;
import com.sige.mat.dao.ConfFechasDAO;
import com.sige.mat.dao.ConfMensualidadDAO;
import com.sige.mat.dao.ConfPagosCicloCuotaDAO;
import com.sige.mat.dao.ConfPagosCicloDAO;
import com.sige.mat.dao.CronogramaDAO;
import com.sige.mat.dao.CronogramaRatificacionDAO;
import com.sige.mat.dao.FamiliarDAO;
import com.sige.mat.dao.GruFamAlumnoDAO;
import com.sige.mat.dao.GruFamDAO;
import com.sige.mat.dao.MatrVacanteDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.MovimientoDAO;
import com.sige.mat.dao.MovimientoDescuentoDAO;
import com.sige.mat.dao.MovimientoDetalleDAO;
import com.sige.mat.dao.NivelDAO;
import com.sige.mat.dao.ParametroDAO;
import com.sige.mat.dao.PeriodoDAO;
import com.sige.mat.dao.PersonaDAO;
import com.sige.mat.dao.RatificacionDAO;
import com.sige.mat.dao.ReglasNegocioDAO;
import com.sige.mat.dao.ReservaDAO;
import com.sige.mat.dao.SeccionSugeridaDAO;
import com.sige.mat.dao.ServicioDAO;
import com.sige.mat.dao.SolicitudDAO;
import com.sige.mat.dao.SucursalDAO;
import com.sige.rest.request.FamiliarReq;
import com.sige.rest.request.PagoMatriculaReq;
import com.sige.rest.request.PagoRequest;
import com.sige.rest.request.RatificacionReq;
import com.sige.spring.service.CampusVirtualService;
import com.sige.spring.service.CondicionService;
import com.sige.spring.service.FacturacionService;
import com.sige.spring.service.MatriculaService;
import com.sige.spring.service.PagosService;
import com.sige.spring.service.SituacionFinalService;
import com.sige.spring.service.VacanteService;
import com.tesla.colegio.model.AcademicoPago;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.AlumnoDescuento;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.Banco;
import com.tesla.colegio.model.Ciclo;
import com.tesla.colegio.model.CicloTurno;
import com.tesla.colegio.model.ConfCuota;
import com.tesla.colegio.model.ConfFechas;
import com.tesla.colegio.model.ConfMensualidad;
import com.tesla.colegio.model.ConfPagosCiclo;
import com.tesla.colegio.model.ConfPagosCicloCuota;
import com.tesla.colegio.model.CronogramaRatificacion;
import com.tesla.colegio.model.Curso;
import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.GruFam;
import com.tesla.colegio.model.GruFamAlumno;
import com.tesla.colegio.model.MatrVacante;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Movimiento;
import com.tesla.colegio.model.MovimientoDescuento;
import com.tesla.colegio.model.MovimientoDetalle;
import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.Parametro;
import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.Persona;
import com.tesla.colegio.model.Ratificacion;
import com.tesla.colegio.model.ReglasNegocio;
import com.tesla.colegio.model.Reserva;
import com.tesla.colegio.model.ReservaCuota;
import com.tesla.colegio.model.SeccionSugerida;
import com.tesla.colegio.model.Servicio;
import com.tesla.colegio.model.Solicitud;
import com.tesla.colegio.model.Sucursal; 
import com.tesla.colegio.model.bean.CondicionBean;
import com.tesla.colegio.model.bean.FamiliarDeudaBean;
import com.tesla.colegio.model.bean.MatriculaPagos;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.common.exceptions.ControllerException;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.ArchivoUtil;
import com.tesla.frmk.util.DocxUtil;
import com.tesla.frmk.util.ExcelXlsUtil;
import com.tesla.frmk.util.FechaUtil;
import com.tesla.frmk.util.JsonUtil;


@RestController
@RequestMapping(value = "/api/matricula")
public class MatriculaRestController {
	final static Logger logger = Logger.getLogger(MatriculaRestController.class);
	@Autowired
	private FamiliarDAO apoderadoDAO;

	@Autowired
	private MatriculaDAO matriculaDAO;
	
	@Autowired
	private AnioDAO anioDAO;
	
	@Autowired
	private CronogramaDAO cronogramaDAO;
	
	@Autowired
	private ConfFechasDAO confFechasDAO;
	
	@Autowired
	private AlumnoDAO alumnoDAO;
	
	@Autowired
	private MatrVacanteDAO matvacanteDAO; 

	@Autowired
	private PeriodoDAO periodoDAO;
	
	@Autowired
	private SolicitudDAO solicitudDAO;
	
	@Autowired
	private ParametroDAO parametroDAO;
	
	@Autowired
	private ReservaDAO reservaDAO;
	
	@Autowired
	private ConfCuotaDAO confCuotaDAO;
	
	@Autowired
	private AulaDAO aulaDAO;
	
	@Autowired
	private SucursalDAO sucursalDAO;
	
	@Autowired
	private ServicioDAO servicioDAO;
	
	@Autowired
	private ConfMensualidadDAO confMensualidadDAO;
	
	@Autowired
	private ConfPagosCicloDAO confPagosCicloDAO;
	
	@Autowired
	private ConfPagosCicloCuotaDAO confPagosCicloCuotaDAO;
	
	@Autowired
	private PersonaDAO personaDAO;

	@Autowired
	private VacanteService vacanteService ;

	@Autowired
	private PagosService pagosService;

	@Autowired
	private MatriculaService matriculaService;


	@Autowired
	private CondicionService condicionService;

	@Autowired
	private SituacionFinalService situacionFinalService;

	@Autowired
	private FacturacionService facturacionService;

	@Autowired
	private MovimientoDAO movimientoDAO;
	
	@Autowired
	private MovimientoDetalleDAO movimientoDetalleDAO;
	
	@Autowired
	private MovimientoDescuentoDAO movimientoDescuentoDAO;
	
	@Autowired
	private NivelDAO nivelDAO;
	
	@Autowired
	private ReglasNegocioDAO reglasNegocioDAO;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private AcademicoPagoDAO academicoPagoDAO;
	
	@Autowired
	private AlumnoDescuentoDAO alumnoDescuentoDAO;
	
	@Autowired
	private BancoDAO bancoDAO;
	
	@Autowired
	private GruFamAlumnoDAO gruFamAlumnoDAO;
	
	@Autowired
	private CampusVirtualService campusVirtualService;
	
	@Autowired
	private SeccionSugeridaDAO seccionSugeridaDAO;
	
	@Autowired
	private CicloDAO cicloDAO;
	
	@Autowired
	private CicloTurnoDAO cicloTurnoDAO;
	
	@Autowired
	private FamiliarDAO familiarDAO;
	
	@Autowired
	private GruFamDAO gruFamDAO;
	
	@Autowired
	private RatificacionDAO ratificacionDAO;
	
	@Autowired
	private CronogramaRatificacionDAO cron_ratifiRatificacionDAO;
	
/**
 * DAtos del apoderado de la matricula
 * @param id_mat
 * @return
 */
	@RequestMapping(value = "/apoderado/{id_mat}")
	public AjaxResponseBody getPagosProgramados(@PathVariable Integer id_mat) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		Param param = new Param();
		param.put("tip", "MEN");
		param.put("id_mat", id_mat);
		param.put("est", "A");
		param.put("canc", "0");
		

		Row apoderado = apoderadoDAO.apoderadMatricula(id_mat);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("apoderado", apoderado);
		
		result.setResult(map);
		
		return result;

	}	
	@RequestMapping(value = "/seccion/{id_au}")
	public AjaxResponseBody getAlumnosxSeccion(@PathVariable Integer id_au) {
		AjaxResponseBody result = new AjaxResponseBody();
		
		List<Map<String, Object>> ReporteList =matriculaDAO.Reporte_Mat(id_au);
		result.setResult(ReporteList);
		
		return result;
	}
	
	@RequestMapping(value = "/MatriculaBuscar2")
	public AjaxResponseBody buscarAlumnos(String apellidosNombres, Integer id_anio, Integer id_suc) {
		AjaxResponseBody result = new AjaxResponseBody();


		Anio anio = anioDAO.get(id_anio);
		int anio_anterior = Integer.parseInt(anio.getNom()) - 1;
		Param param = new Param();
		param.put("nom", anio_anterior);
		Anio anioAnterior = anioDAO.getByParams(param);
		//Map<String,Object> map = new HashMap<String,Object>();
		ReglasNegocio matr = reglasNegocioDAO.getByParams(new Param("cod",Constante.MATR_SEGUN_CRONOGRAMA));
		String matricula_segun_cronograma=matr.getVal();
		Map<String,Object> map = new HashMap<String,Object>();
		//RG1: Verficar si para la matrícula se hará según cronograma o no.
		if(matricula_segun_cronograma.equals("0")){
			List<Row> alumnoList = matriculaDAO.listarNoMatriculados(apellidosNombres.trim(), 4);
			map.put("alumnoList", alumnoList);
			map.put("apellidosNombres", apellidosNombres);
		} else if(matricula_segun_cronograma.equals("1")){
			//ALUMNOS ANTIGUOS - CON CRONOGRAMA
			boolean antiguo_con_cronograma = cronogramaDAO.alumnosAntiguosTienenVigente(id_anio);

			//ALUMNOS ANTIGUOS - SIN CRONOGRAMA
			boolean antiguo_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "AS");

					
			//ALUMNOS NUEVOS SIN CRONOGRAMA
			boolean nuevos_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "NS");
			
			//ALUMNOS NUEVOS CON CRONOGRAMA 
			boolean nuevos_cronograma = confFechasDAO.cronogramaVigente(id_anio, "NC");
			
			if (antiguo_con_cronograma){
				logger.debug("antiguo_con_cronograma");
				
				List<Map<String, Object>> alumnoList = matriculaDAO.listaTodosAlumnos("AC", apellidosNombres.trim(), id_anio, anioAnterior.getId(), id_suc);

				map.put("alumnoList", alumnoList);
				map.put("apellidosNombres", apellidosNombres);
				
			}else if (antiguo_sin_cronograma && nuevos_sin_cronograma){
				logger.debug("antiguo_sin_cronograma Y nuevos_cronograma");
				List<Map<String, Object>> alumnoList = matriculaDAO.listaTodosAlumnos("ASNS", apellidosNombres.trim(), id_anio, anioAnterior.getId(), id_suc);

				map.put("alumnoList", alumnoList);
				map.put("apellidosNombres", apellidosNombres);
				
					
			}else if (antiguo_sin_cronograma && !nuevos_cronograma){

				logger.debug("antiguo_SIN_cronograma");
				
				List<Map<String, Object>> alumnoList = matriculaDAO.listaTodosAlumnos("AS", apellidosNombres.trim(), id_anio, anioAnterior.getId(), id_suc);

				map.put("alumnoList", alumnoList);
				map.put("apellidosNombres", apellidosNombres);

				
			}else{
				if(nuevos_cronograma || nuevos_sin_cronograma){
					logger.debug("nuevos_cronograma");
					List<Map<String, Object>> alumnoList = matriculaDAO.listaTodosAlumnos("NC", apellidosNombres.trim(), id_anio, anioAnterior.getId(), id_suc);

					map.put("alumnoList", alumnoList);
					map.put("apellidosNombres", apellidosNombres);
					
				}
				
			}
		}
		
		result.setResult(map);



		return result;
	}
	
	@RequestMapping( value="/situacionAlumno", method = RequestMethod.GET)
	public AjaxResponseBody situacionAlumno( Integer id_alu, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			Integer id_cli=null;
			Matricula matriculaAnterior = matriculaService.obtenerMatriculaAnterior(id_alu,id_anio);
			if (matriculaAnterior == null)
				id_cli = Constante.CLIENTE_NUEVO;
			else{
				//id_cli = Constante.CLIENTE_ALUMNO;
				Param param= new Param();
				param.put("matr_vac.id_alu", id_alu);
				param.put("per.id_anio", id_anio);
				List<MatrVacante> matricula_vacante= matvacanteDAO.listFullByParams(param,null);

				if(matricula_vacante.size()>0)
					id_cli = Constante.CLIENTE_NUEVO;
				else
					id_cli = Constante.CLIENTE_ALUMNO;				
			}
			
			result.setResult(id_cli);
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping(value = "/grabarRatificacion", method = RequestMethod.POST)
	// @Transactional
	//public AjaxResponseBody grabarRatificacion(Integer id_mat[], String res[], Integer id_anio_rat) {
	public AjaxResponseBody grabarRatificacion(@RequestBody RatificacionReq ratificacionReq) {
		AjaxResponseBody result = new AjaxResponseBody();

		try {
			
			Integer id_mats[]=ratificacionReq.getId_mats_alu();
			String respuestas[]=ratificacionReq.getResp_alu();
			Integer id_anio=ratificacionReq.getId_anio();
			for (int j = 0; j < id_mats.length; j++) {
				Ratificacion ratificacion = new Ratificacion();
				ratificacion.setId_mat(id_mats[j]);
				ratificacion.setId_anio_rat(id_anio);
				ratificacion.setRes(respuestas[j]);
				ratificacion.setEst("A");
				ratificacionDAO.saveOrUpdate(ratificacion);
			}
			/*for (int i = 0; i < id_mat.length; i++) {
				Ratificacion ratificacion = new Ratificacion();
				ratificacion.setId_mat(id_mat[i]);
				ratificacion.setId_anio_rat(id_anio_rat);
				ratificacion.setRes(res[i]);
				ratificacionDAO.saveOrUpdate(ratificacion);
			}*/
				
				

			result.setResult(1);

		} catch (Exception e) {
			result.setException(e);
		}

		return result;

	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/MatriculaPagos2", method = RequestMethod.GET)
	public AjaxResponseBody matriculaPagos(Integer id_mat,Integer id_au, Integer id_alu, Integer id_anio) throws Exception {
		AjaxResponseBody result = new AjaxResponseBody();
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (id_mat == null) {
			/*** ALUMNO NUEVO A MATRICULAR**/

			List<MatriculaPagos> listPagar = matriculaService.obtenerPagosProgramados(id_mat,id_alu, id_au, id_anio,null);
			result.setResult(map);
			BigDecimal total = BigDecimal.ZERO;
			for (MatriculaPagos matriculaPagos : listPagar) {
				total = total.add(matriculaPagos.getMonto());
			}
			
			map.put("pagos", listPagar);
			
			map.put("total", total);
			return result;
			
		} else {
			Matricula matricula = matriculaDAO.get(id_mat);
			Integer id_au_antigua = matricula.getId_au();	// Integer id_au_actual = matricula.getId_au_asi();
			Integer id_au_actual = matricula.getId_au_asi();
			
			/*** revisar si hubo cambio de seccion**/

			if (id_au_antigua.equals(id_au_actual)){
				/*** no hubo cambio de seccion **/
				
				map = facturacionService.pagosMatriculaRealizados(id_mat,null);
				
			}else{
				//CAMBIO DE SECCION O LOCAL
				//Aula aulaNueva = aulaDAO.get(id_au);
				//Aula aulaActual = aulaDAO.get(id_au_actual);
				Aula aulaNueva = aulaDAO.get(id_au_actual);
				Aula aulaAntigua = aulaDAO.get(id_au_antigua);
				
				if (aulaNueva.getId_per().equals(aulaAntigua.getId_per())){
					/*** SOLO cambio de seccion **/
					map =  facturacionService.pagosMatriculaRealizados(id_mat,"CS");
				}else{
					/*** cambio de local**/
					/*Pagos de matricula NUEVO*/
					List<MatriculaPagos> listPagar = matriculaService.obtenerPagosProgramados(id_mat,id_alu, id_au_actual, id_anio,"CL");
					BigDecimal totalPagar = BigDecimal.ZERO;
					for (MatriculaPagos matriculaPagos : listPagar) {
						totalPagar = totalPagar.add(matriculaPagos.getMonto());
					}
					
					/*Pagos realizados anteriormente*/
					map =  facturacionService.pagosMatriculaRealizados(id_mat,"CL");
					List<MatriculaPagos> listPagados =  (List<MatriculaPagos>) map.get("pagos");
					BigDecimal totalPagados = (BigDecimal)map.get("total");
					
					//unir las dos listas
					listPagados.addAll(listPagar);
					
					//diferencia de pagos
					BigDecimal total = totalPagar.subtract(totalPagados);
					
					if (total.compareTo(BigDecimal.ZERO)>0){
						//DEBE PAGAR LA DIFERENCIA
						MatriculaPagos academicoPago = new MatriculaPagos();
						if(matricula.getMat_val().equals("1"))//si es una matricula validada
							academicoPago.setCanc("1");
						else if(matricula.getMat_val().equals("0"))
							academicoPago.setCanc("0");// Por defecto pago pendiente
						academicoPago.setMonto(total);
						academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_CAMBIO_LOCAL);
						listPagados.add(academicoPago);
					}else{
						//EL COLEGIO DEBE DEVOLVER LA DIFERENCIA
						//Buscar el recibo si ya hizo la devolucion
						List<Row> devolucion_cambio_local= movimientoDAO.pagoCambioLocal(id_mat);
						MatriculaPagos academicoPago = new MatriculaPagos();
						Param param  = new Param();
						param.put("id_mat", id_mat);
						param.put("tip", "MAT");
						AcademicoPago aca = academicoPagoDAO.getByParams(param);
						/*if(matricula.getMat_val().equals("1"))//si es una matricula validada
							academicoPago.setCanc("1");
						else if(matricula.getMat_val().equals("0"))
							academicoPago.setCanc("0");// Por defecto pago pendiente*/
						if(aca.getCanc().equals("1"))//si es una matricula validada
							academicoPago.setCanc("1");
						else if(aca.getCanc().equals("0"))
							academicoPago.setCanc("0");// Por defecto pago pendiente
						academicoPago.setMonto(total);
						if(devolucion_cambio_local.size()>0)
							academicoPago.setNro_rec(devolucion_cambio_local.get(0).getString("nro_rec"));
						academicoPago.setTip(com.tesla.colegio.util.Constante.DEVOLUCION_CAMBIO_LOCAL);
						listPagados.add(academicoPago);
					}
					
					map.put("pagos", listPagados);
					map.put("total", total);

				}
				
				
				
			}
			
			result.setResult(map);
			return result;

		}

	}
	
	@Transactional
	@RequestMapping(value = "/ResumenPagoMatriculaWebColegio", method = RequestMethod.POST)
	public AjaxResponseBody ResumenPagoMatriculaWebColegio(Integer id_alu_sel[], Integer id_anio, Integer id_suc[], Integer id_cme[], Integer id_cct[]) throws Exception {
		AjaxResponseBody result = new AjaxResponseBody();
		try {
		List<MatriculaPagos> listPagos =new ArrayList<MatriculaPagos>(); 
		Map<String,Object> map = new HashMap<String,Object>();
		BigDecimal monto_total_ciclo = BigDecimal.ZERO;
		//Verifico los cronogramas
		//ALUMNOS ANTIGUOS - CON CRONOGRAMA
		boolean antiguo_con_cronograma = cronogramaDAO.alumnosAntiguosTienenVigente(id_anio);

		//ALUMNOS ANTIGUOS - SIN CRONOGRAMA
		boolean antiguo_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "AS");

				
		//ALUMNOS NUEVOS SIN CRONOGRAMA
		boolean nuevos_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "NS");
		
		//ALUMNOS NUEVOS CON CRONOGRAMA 
		boolean nuevos_cronograma = confFechasDAO.cronogramaVigente(id_anio, "NC");
				
		for (int i = 0; i < id_alu_sel.length; i++) {
			Param param_regla = new Param();
			param_regla.put("nom", "MATR_AULA_SUG");
			ReglasNegocio reglasNegocio = reglasNegocioDAO.getByParams(param_regla);
			if(antiguo_con_cronograma) {
				//Obtengo su costo de acuerdo a su aula sugerida
				Matricula matricula_anterior = matriculaDAO.getMatriculaAnteriorParaReserva(id_alu_sel[i], id_anio-1);
				//Datos del Alumno
				Alumno alumno = alumnoDAO.get(id_alu_sel[i]);
				Persona persona = personaDAO.get(alumno.getId_per());
				
				
				if(reglasNegocio!=null){
					if(reglasNegocio.getVal().equals("1")) {
						//Para esta matricula, busco su aula sugerida
						Param param = new Param();
						param.put("id_mat", matricula_anterior.getId());
						SeccionSugerida seccionSugerida= seccionSugeridaDAO.getByParams(param);
						Aula aula_sug=aulaDAO.get(seccionSugerida.getId_au_nue());
						Ciclo ciclo = cicloDAO.get(aula_sug.getId_cic());
						//Obtengo el aula Nuevo
						ConfCuota confCuota = confCuotaDAO.getByParams(new Param("id_per", ciclo.getId_per()));
						MatriculaPagos academicoPago2 = new MatriculaPagos();
						academicoPago2.setTip(Constante.PAGO_MATRICULA);
						academicoPago2.setDescripcion("ALUMNO "+persona.getApe_pat()+" "+persona.getApe_mat()+ " "+ persona.getNom());
						academicoPago2.setMonto(confCuota.getMatricula());
						listPagos.add(academicoPago2);
						monto_total_ciclo=monto_total_ciclo.add(confCuota.getMatricula());
					} else {
						//Busco la configuracion de matricula segun local, modalidad, año
						//Buscamos el periodo
						Param param = new Param();
						param.put("id_suc", id_suc[i]);
						Integer id_niv=null;
						if(matricula_anterior.getId_gra().equals(3))
							id_niv= 2;
						else if(matricula_anterior.getId_gra().equals(9))
							id_niv=3;
						else 
							id_niv= matricula_anterior.getId_niv();
						
						param.put("id_niv", id_niv);
						param.put("id_anio", id_anio);
						param.put("id_tpe",1);
						Periodo periodo_nuevo = periodoDAO.getByParams(param);
						//Buscamos el ciclo Turno
						CicloTurno ciclo_turno = cicloTurnoDAO.get(id_cct[i]);
						
						//Obtengo los costos
						
						Param param_cuota = new Param();
						param_cuota.put("id_per", periodo_nuevo.getId());
						param_cuota.put("id_cct", ciclo_turno.getId());
						param_cuota.put("id_cme", id_cme[i]);
						ConfCuota confCuota = confCuotaDAO.getByParams(param_cuota);
						MatriculaPagos academicoPago2 = new MatriculaPagos();
						academicoPago2.setTip(Constante.PAGO_MATRICULA);
						academicoPago2.setDescripcion("ALUMNO "+persona.getApe_pat()+" "+persona.getApe_mat()+ " "+ persona.getNom());
						academicoPago2.setMonto(confCuota.getMatricula());
						listPagos.add(academicoPago2);
						monto_total_ciclo=monto_total_ciclo.add(confCuota.getMatricula());
					}
				} else {
					Param param = new Param();
					param.put("id_mat", matricula_anterior.getId());
					SeccionSugerida seccionSugerida= seccionSugeridaDAO.getByParams(param);
					Aula aula_sug=aulaDAO.get(seccionSugerida.getId_au_nue());
					Ciclo ciclo = cicloDAO.get(aula_sug.getId_cic());
					//Obtengo el aula Nuevo
					ConfCuota confCuota = confCuotaDAO.getByParams(new Param("id_per", ciclo.getId_per()));
					MatriculaPagos academicoPago2 = new MatriculaPagos();
					academicoPago2.setTip(Constante.PAGO_MATRICULA);
					academicoPago2.setDescripcion("ALUMNO "+persona.getApe_pat()+" "+persona.getApe_mat()+ " "+ persona.getNom());
					academicoPago2.setMonto(confCuota.getMatricula());
					listPagos.add(academicoPago2);
					monto_total_ciclo=monto_total_ciclo.add(confCuota.getMatricula());
				}
				
			} if(antiguo_sin_cronograma && !nuevos_sin_cronograma) {
				//Obtengo su costo de acuerdo a su aula sugerida
				Matricula matricula_anterior = matriculaDAO.getMatriculaAnteriorParaReserva(id_alu_sel[i], id_anio-1);
				//Datos del Alumno
				Alumno alumno = alumnoDAO.get(id_alu_sel[i]);
				Persona persona = personaDAO.get(alumno.getId_per());
				
				
				if(reglasNegocio!=null){
					if(reglasNegocio.getVal().equals("1")) {
						//Para esta matricula, busco su aula sugerida
						Param param = new Param();
						param.put("id_mat", matricula_anterior.getId());
						SeccionSugerida seccionSugerida= seccionSugeridaDAO.getByParams(param);
						Aula aula_sug=aulaDAO.get(seccionSugerida.getId_au_nue());
						Ciclo ciclo = cicloDAO.get(aula_sug.getId_cic());
						//Obtengo el aula Nuevo
						ConfCuota confCuota = confCuotaDAO.getByParams(new Param("id_per", ciclo.getId_per()));
						MatriculaPagos academicoPago2 = new MatriculaPagos();
						academicoPago2.setTip(Constante.PAGO_MATRICULA);
						academicoPago2.setDescripcion("ALUMNO "+persona.getApe_pat()+" "+persona.getApe_mat()+ " "+ persona.getNom());
						academicoPago2.setMonto(confCuota.getMatricula());
						listPagos.add(academicoPago2);
						monto_total_ciclo=monto_total_ciclo.add(confCuota.getMatricula());
					} else {
						//Busco la configuracion de matricula segun local, modalidad, año
						//Buscamos el periodo
						Param param = new Param();
						param.put("id_suc", id_suc[i]);
						Integer id_niv=null;
						if(matricula_anterior.getId_gra().equals(3))
							id_niv= 2;
						else if(matricula_anterior.getId_gra().equals(9))
							id_niv=3;
						else 
							id_niv= matricula_anterior.getId_niv();
						
						param.put("id_niv", id_niv);
						param.put("id_anio", id_anio);
						param.put("id_tpe",1);
						Periodo periodo_nuevo = periodoDAO.getByParams(param);
						//Buscamos el ciclo Turno
						CicloTurno ciclo_turno = cicloTurnoDAO.get(id_cct[i]);
						
						//Obtengo los costos
						
						Param param_cuota = new Param();
						param_cuota.put("id_per", periodo_nuevo.getId());
						param_cuota.put("id_cct", ciclo_turno.getId());
						param_cuota.put("id_cme", id_cme[i]);
						ConfCuota confCuota = confCuotaDAO.getByParams(param_cuota);
						MatriculaPagos academicoPago2 = new MatriculaPagos();
						academicoPago2.setTip(Constante.PAGO_MATRICULA);
						academicoPago2.setDescripcion("ALUMNO "+persona.getApe_pat()+" "+persona.getApe_mat()+ " "+ persona.getNom());
						academicoPago2.setMonto(confCuota.getMatricula());
						listPagos.add(academicoPago2);
						monto_total_ciclo=monto_total_ciclo.add(confCuota.getMatricula());
					}
				} else {
					Param param = new Param();
					param.put("id_mat", matricula_anterior.getId());
					SeccionSugerida seccionSugerida= seccionSugeridaDAO.getByParams(param);
					Aula aula_sug=aulaDAO.get(seccionSugerida.getId_au_nue());
					Ciclo ciclo = cicloDAO.get(aula_sug.getId_cic());
					//Obtengo el aula Nuevo
					ConfCuota confCuota = confCuotaDAO.getByParams(new Param("id_per", ciclo.getId_per()));
					MatriculaPagos academicoPago2 = new MatriculaPagos();
					academicoPago2.setTip(Constante.PAGO_MATRICULA);
					academicoPago2.setDescripcion("ALUMNO "+persona.getApe_pat()+" "+persona.getApe_mat()+ " "+ persona.getNom());
					academicoPago2.setMonto(confCuota.getMatricula());
					listPagos.add(academicoPago2);
					monto_total_ciclo=monto_total_ciclo.add(confCuota.getMatricula());
				}
				
			} else if(antiguo_sin_cronograma && nuevos_sin_cronograma) {
				//Busco si tiene matricula anterior
				Matricula matricula_anterior = matriculaDAO.getMatriculaAnteriorParaReserva(id_alu_sel[i], id_anio-1);
				//Datos del Alumno
				Alumno alumno = alumnoDAO.get(id_alu_sel[i]);
				Persona persona = personaDAO.get(alumno.getId_per());
				if(matricula_anterior!=null) { //  Es alumno antiguo
					if(reglasNegocio!=null){
						if(reglasNegocio.getVal().equals("1")) {
							//Buscamos el periodo
							Param param = new Param();
							param.put("id_suc", id_suc[i]);
							Integer id_niv=null;
							if(matricula_anterior.getId_gra().equals(3))
								id_niv= 2;
							else if(matricula_anterior.getId_gra().equals(9))
								id_niv=3;
							else 
								id_niv= matricula_anterior.getId_niv();
							
							param.put("id_niv", id_niv);
							param.put("id_anio", id_anio);
							param.put("id_tpe",1);
							Periodo periodo_nuevo = periodoDAO.getByParams(param);
							//Obtengo los costos
							ConfCuota confCuota = confCuotaDAO.getByParams(new Param("id_per", periodo_nuevo.getId()));
							MatriculaPagos academicoPago2 = new MatriculaPagos();
							academicoPago2.setTip(Constante.PAGO_MATRICULA);
							academicoPago2.setDescripcion("ALUMNO "+persona.getApe_pat()+" "+persona.getApe_mat()+ " "+ persona.getNom());
							academicoPago2.setMonto(confCuota.getMatricula());
							listPagos.add(academicoPago2);
							monto_total_ciclo=monto_total_ciclo.add(confCuota.getMatricula());
						} else {
							//Buscamos el periodo
							Param param = new Param();
							param.put("id_suc", id_suc[i]);
							Integer id_niv=null;
							if(matricula_anterior.getId_gra().equals(3))
								id_niv= 2;
							else if(matricula_anterior.getId_gra().equals(9))
								id_niv=3;
							else 
								id_niv= matricula_anterior.getId_niv();
							
							param.put("id_niv", id_niv);
							param.put("id_anio", id_anio);
							param.put("id_tpe",1);
							Periodo periodo_nuevo = periodoDAO.getByParams(param);
							//Buscamos el ciclo Turno
							CicloTurno ciclo_turno = cicloTurnoDAO.get(id_cct[i]);
							
							//Obtengo los costos
							
							
							Param param_cuota = new Param();
							param_cuota.put("id_per", periodo_nuevo.getId());
							param_cuota.put("id_cct", ciclo_turno.getId());
							param_cuota.put("id_cme", id_cme[i]);
							ConfCuota confCuota = confCuotaDAO.getByParams(param_cuota);
							MatriculaPagos academicoPago2 = new MatriculaPagos();
							academicoPago2.setTip(Constante.PAGO_MATRICULA);
							academicoPago2.setDescripcion("ALUMNO "+persona.getApe_pat()+" "+persona.getApe_mat()+ " "+ persona.getNom());
							academicoPago2.setMonto(confCuota.getMatricula());
							listPagos.add(academicoPago2);
							//Buscamos si ha tenido reserva
							Row reserva = reservaDAO.obtenerReservaAlumno(id_anio, id_alu_sel[i]);
							if(reserva!=null) {
								ReservaCuota reservaCuota = reservaDAO.getMontoReserva(periodo_nuevo.getId_anio(), id_alu_sel[i]);
								BigDecimal montoReserva = reservaCuota.getMonto();
								MatriculaPagos academicoPago4 = new MatriculaPagos();
								academicoPago4.setTip(Constante.PAGO_RESERVA);
								academicoPago4.setDescripcion("ALUMNO "+persona.getApe_pat()+" "+persona.getApe_mat()+ " "+ persona.getNom());
								academicoPago4.setMonto(montoReserva.multiply(new BigDecimal(-1)));
								listPagos.add(academicoPago4);
								monto_total_ciclo=monto_total_ciclo.add(confCuota.getMatricula());
								monto_total_ciclo=monto_total_ciclo.subtract(montoReserva);
							} else {
								monto_total_ciclo=monto_total_ciclo.add(confCuota.getMatricula());
							}
							
							
						}
					} else {
						//Buscamos el periodo
						Param param = new Param();
						param.put("id_suc", id_suc[i]);
						Integer id_niv=null;
						if(matricula_anterior.getId_gra().equals(3))
							id_niv= 2;
						else if(matricula_anterior.getId_gra().equals(9))
							id_niv=3;
						else 
							id_niv= matricula_anterior.getId_niv();
						
						param.put("id_niv", id_niv);
						param.put("id_anio", id_anio);
						param.put("id_tpe",1);
						Periodo periodo_nuevo = periodoDAO.getByParams(param);
						//Obtengo los costos
						ConfCuota confCuota = confCuotaDAO.getByParams(new Param("id_per", periodo_nuevo.getId()));
						MatriculaPagos academicoPago2 = new MatriculaPagos();
						academicoPago2.setTip(Constante.PAGO_MATRICULA);
						academicoPago2.setDescripcion("ALUMNO "+persona.getApe_pat()+" "+persona.getApe_mat()+ " "+ persona.getNom());
						academicoPago2.setMonto(confCuota.getMatricula());
						listPagos.add(academicoPago2);
						monto_total_ciclo=monto_total_ciclo.add(confCuota.getMatricula());
					}
				
					
				} else { //Es nuevo
					//Buscamos la reserva
					//Reserva reserva = reservaDAO.getReservaxAnio(id_anio, id_alu_sel[i]);
					Row reserva = reservaDAO.obtenerReservaAlumno(id_anio, id_alu_sel[i]);
					Periodo periodo_nuevo = periodoDAO.get(reserva.getInteger("id_per"));
					//Obtengo los costos
					ConfCuota confCuota = confCuotaDAO.getByParams(new Param("id_per", periodo_nuevo.getId()));
					MatriculaPagos academicoPago2 = new MatriculaPagos();
					academicoPago2.setTip(Constante.PAGO_MATRICULA);
					academicoPago2.setDescripcion("ALUMNO "+persona.getApe_pat()+" "+persona.getApe_mat()+ " "+ persona.getNom());
					academicoPago2.setMonto(confCuota.getMatricula());
					listPagos.add(academicoPago2);
					monto_total_ciclo=monto_total_ciclo.add(confCuota.getMatricula());
					//Cuota de Ingreso
					MatriculaPagos academicoPago3 = new MatriculaPagos();
					academicoPago3.setTip(Constante.PAGO_CUOTA_INGRESO);
					academicoPago3.setDescripcion("ALUMNO "+persona.getApe_pat()+" "+persona.getApe_mat()+ " "+ persona.getNom());
					academicoPago3.setMonto(confCuota.getCuota());
					listPagos.add(academicoPago3);
					monto_total_ciclo=monto_total_ciclo.add(confCuota.getCuota());
					ReservaCuota reservaCuota = reservaDAO.getMontoReserva(periodo_nuevo.getId_anio(), id_alu_sel[i]);
					BigDecimal montoReserva = reservaCuota.getMonto();
					MatriculaPagos academicoPago4 = new MatriculaPagos();
					academicoPago4.setTip(Constante.PAGO_RESERVA);
					academicoPago4.setDescripcion("ALUMNO "+persona.getApe_pat()+" "+persona.getApe_mat()+ " "+ persona.getNom());
					academicoPago4.setMonto(montoReserva.multiply(new BigDecimal(-1)));
					listPagos.add(academicoPago4);
					monto_total_ciclo=monto_total_ciclo.subtract(montoReserva);
				}
			}
			
			
		}
		
		map.put("listPagos", listPagos);
		map.put("monto_total", monto_total_ciclo);
		result.setResult(map);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	

	@Transactional
	@RequestMapping(value = "/validarDeudas/{id_alu}/{id_anio}", method = RequestMethod.GET)
	public AjaxResponseBody validarDeudasxAlumno(@PathVariable Integer id_alu, @PathVariable Integer id_anio) throws Exception {
		AjaxResponseBody result = new AjaxResponseBody();
		try {
			//Año anterior
			Anio anio = anioDAO.get(id_anio);
			Integer anio_ant= Integer.parseInt(anio.getNom())-1;
			Anio anio_anterior=anioDAO.getByParams(new Param("nom",anio_ant));
			BigDecimal deuda = pagosService.pagosPendientes(id_alu, anio_anterior.getId());
			if (deuda.compareTo(BigDecimal.ZERO)>0) {
				result.setCode("500");
				result.setMsg("¡ATENCIÓN!,\n La familia no debe tener deudas en el año "+anio_anterior.getNom()+" , para realizar la matrícula 2021.\nDeuda de la familia S/ " + deuda);
				return result;
			}
			

		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@Transactional
	@RequestMapping(value = "/validarDeudasPendientes/{id_alu}/{id_anio}", method = RequestMethod.GET)
	public AjaxResponseBody validarDeudasPendientesxAlumno(@PathVariable Integer id_alu, @PathVariable Integer id_anio) throws Exception {
		AjaxResponseBody result = new AjaxResponseBody();
		try {
			//Año anterior
			GruFamAlumno gruFamAlumno = gruFamAlumnoDAO.getByParams(new Param("id_alu",id_alu));
			Integer id_gpf=gruFamAlumno.getId_gpf();
			List<Row> deudas= alumnoDAO.listarDeudasxFamilia(id_gpf);
			if(deudas!=null) {
				Integer tam=deudas.size();
				if (tam>0) {
					result.setCode("500");
					result.setMsg("¡ATENCIÓN!,\n La familia no debe tener deudas pendientes , para realizar la inscripción. " );
					return result;
				}
			}
			
			

		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@Transactional
	@RequestMapping(value = "/validarDeudasxGruFam/{id_usr}/{id_anio}", method = RequestMethod.GET)
	public AjaxResponseBody validarDeudasxGruFam(@PathVariable Integer id_usr, @PathVariable Integer id_anio) throws Exception {
		AjaxResponseBody result = new AjaxResponseBody();
		try {
			//Año anterior
			GruFam gruFam= gruFamDAO.getByParams(new Param("id_usr",id_usr));
			GruFamAlumno gruFamAlumno = gruFamAlumnoDAO.getByParams(new Param("id_gpf",gruFam.getId()));
			Integer id_alu=gruFamAlumno.getId_alu();
			Anio anio = anioDAO.get(id_anio);
			Integer anio_ant= Integer.parseInt(anio.getNom())-1;
			Anio anio_anterior=anioDAO.getByParams(new Param("nom",anio_ant));
			BigDecimal deuda = pagosService.pagosPendientes(id_alu, anio_anterior.getId());
			if (deuda.compareTo(BigDecimal.ZERO)>0) {
				result.setCode("500");
				//result.setMsg("¡ATENCIÓN!,\n La familia no debe tener deudas en el año "+anio_anterior.getNom()+", para realizar la matrícula "+anio.getNom()+".\nDeuda de la familia S/ " + deuda);
				result.setMsg("¡ATENCIÓN!,\n La familia no debe tener deudas en el año "+anio_anterior.getNom()+", para realizar la matrícula "+anio.getNom());
				return result;
			}
			

		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping(value = "/MatriculaEditar2", method = RequestMethod.GET)
	public AjaxResponseBody editar(Integer id_anio, Integer id_alu, Integer id_mat, Integer id_suc) throws Exception {

		AjaxResponseBody result = new AjaxResponseBody();

		try {

			Matricula matricula = null;
			Alumno alumno = null;
			Integer id_cli = null;// tipo de cliente: ALUMNO NUEVO O ALUMNO ANTIGUO
			boolean tieneReserva = false;
			/*********************************************************************************/
			/** REGLAS DE NEGOCIO */
			/*********************************************************************************/		
			//Reglas de Negocio
			//RG1: Verificar si para matricula de nuevos es obligatorio el proceso de admisión
			ReglasNegocio admision_obligatorio_nuevos = reglasNegocioDAO.getByParams(new Param("cod",Constante.ADMI_OBL_NUEVOS));
			//RG2: Verificar si para matricula de nuevos es obligatorio la reserva
			ReglasNegocio reserva_nuevos_obligatorio = reglasNegocioDAO.getByParams(new Param("cod",Constante.RES_MAT_NU_OBL));
			//RG3: Verificar si se trabaja con cronograma de matrícula o no
			ReglasNegocio matr = reglasNegocioDAO.getByParams(new Param("cod",Constante.MATR_SEGUN_CRONOGRAMA));
			String matricula_segun_cronograma=matr.getVal();
			
			/*********************************************************************************/
			/** VERIFICAMOS SI ES UN ALUMNO NUEVO O ANTIGUO */
			/*********************************************************************************/

			Matricula matriculaAnterior = matriculaService.obtenerMatriculaAnterior(id_alu,id_anio);
			if (matriculaAnterior == null)
				id_cli = Constante.CLIENTE_NUEVO;
			else{
				//id_cli = Constante.CLIENTE_ALUMNO;
				/*Considero que no es necesario esta validacion, ya que no puede haber un alumno que reingrese como nuevo si no se ha trasladado
				Param param = new Param();
				param.put("alu.id", id_alu); 
				param.put("per.id_anio", id_anio);
				List<MatrVacante> matrVacanteList = matvacanteDAO.listFullByParams(param, null);

				if(matrVacanteList.size()>0)
					id_cli = Constante.CLIENTE_NUEVO;
				else*/
					id_cli = Constante.CLIENTE_ALUMNO;				
			}
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			boolean solicitud_cambio_local = false;
			
			Solicitud solicitudCambioLocal = null;
			//if(id_mat!=null){
			solicitudCambioLocal = solicitudDAO.detalle(id_alu, id_suc, id_anio);//ver si tiene solicitud a nivel de alumno
				
			if (solicitudCambioLocal!=null){
				solicitud_cambio_local = true;
				map.put("_info","El alumno tiene cambio de local");
			}
			map.put("solicitud_cambio_local",(solicitud_cambio_local) ? "1" : "0" );
			
			/*********************************************************************************/
			/** DATOS DE LA MATRICULA Y DEL ALUMNO */
			/*********************************************************************************/
			if (id_mat != null) {
				matricula = matriculaDAO.get(id_mat);
				id_alu = matricula.getId_alu();
			}
			alumno = alumnoDAO.get(id_alu);
			Persona persona = personaDAO.get(alumno.getId_per());

			map.put("alumno", alumno);
			map.put("persona", persona);
			
			/*********************************************************************************/
			/**
			 * SI ALUMNO YA EST� MATRICULADO ( MOSTRAR DATOS DE LA MATRICULA )
			 */
			/*********************************************************************************/
			Integer id_apod=null;
			if (matricula != null) {

				boolean antiguo_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "AS");
				boolean antiguo_con_cronograma = cronogramaDAO.alumnosAntiguosTienenVigente(id_anio);
				boolean antiguo_cambio_seccion = confFechasDAO.cambioSeccionVigente(id_anio, "AS");
				boolean antiguo_sin_cronograma_cambio_seccion = confFechasDAO.cambioSeccionVigente(id_anio, "AS");
				boolean antiguo_con_crograma_cambio_seccion = confFechasDAO.cambioSeccionVigente(id_anio, "AC");
				map.put("antiguo_sin_cronograma",(antiguo_sin_cronograma) ? "1" : "0" );
				map.put("antiguo_con_cronograma",(antiguo_con_cronograma) ? "1" : "0" );
				map.put("antiguo_sin_cronograma_cs",(antiguo_sin_cronograma_cambio_seccion) ? "1" : "0" );
				map.put("antiguo_con_cronograma_cs",(antiguo_con_crograma_cambio_seccion) ? "1" : "0" );
				if(solicitud_cambio_local){
					Param param = new Param();
					param.put("anio.id", id_anio);
					param.put("suc.id", solicitudCambioLocal.getId_suc_des());
					param.put("tpe.id ", Constante.TIPO_PERIODO_ESCOLAR);
					param.put("srv.id_niv ", matricula.getId_niv());
					
					List<Periodo> peridodos = periodoDAO.listFullByParams(param, null);
					matricula.setId_per( peridodos.get(0).getId() );
					
				}
				//Obtener el periodo del aula anterior
				Matricula matricula_ant= matriculaDAO.getMatriculaAnterior(id_alu, id_anio-1);
				if(matricula_ant!=null) {
					Aula aula_anterior = aulaDAO.getByParams(new Param("id",matricula_ant.getId_au_asi()));
					Periodo per_anterior=  periodoDAO.getByParams(new Param("id",aula_anterior.getId_per()));
					map.put("sucursal_anterior", per_anterior.getId_suc());
					map.put("matricula", matricula);
				} else {
					map.put("sucursal_anterior", null);
					map.put("matricula", matricula);
				}
				

				
				id_mat = matricula.getId();
				int id_per = matricula.getId_per();
				id_apod = matricula.getId_fam();
				Periodo periodo = periodoDAO.getFull(matricula.getId_per(), new String[] { Servicio.TABLA });
				matricula.setPeriodo(periodo);
				

				// Datos de los pagos del tipo Matricula o Cuota de Ingreso
				/*
				 * List<Map<String,Object>> PagoMat_Cuota =
				 * matriculaDAO.listPagos(id_per); model.addObject("Pagos1",
				 * PagoMat_Cuota ); model.addObject("Meses",
				 * Constante.MES);//Descipcion de los meses
				 */

				// Datos de los pagos de Mensualidades
				List<Map<String, Object>> PagoMens = matriculaDAO.listPagosMens(id_per);
				map.put("Pagos2", PagoMens);

				// Obtener Datos de los pagos realizados
				List<Map<String, Object>> pago = matriculaDAO.PagoRealizado(id_mat);
				map.put("pagos", pago);
				if (pago.size() > 0) {
					Map<String, Object> pagorealizado = pago.get(0);
					map.put
					("pago_realizado", pagorealizado);
				}
				List<Map<String, Object>> pagof = matriculaDAO.PagoRealizado(id_mat);
				map.put("pagos", pagof);
				map.put("id_apod", id_apod);
				map.put("id_mat", id_mat);
				map.put("id_per", id_per);

				//cargarListas(model, matricula.getId_per(), matricula.getId_gra(), matricula.getId_alu(), null);

				// Datos de Matricula Documentos
				List<Map<String, Object>> matriculaDocList = alumnoDAO.listAluDoc(id_alu);
				map.put("matriculaDocList", matriculaDocList);
				logger.debug("id_alu:" + id_alu);
				
				//Buscar si realizó el pago
				Param param_pago = new Param();
				param_pago.put("id_mat", matricula.getId());
				param_pago.put("tip", "MAT");
				param_pago.put("canc", "1");
				AcademicoPago academicoPago = academicoPagoDAO.getByParams(param_pago);
				map.put("pago", academicoPago.getCanc());
				Aula aula = aulaDAO.get(matricula.getId_au_asi());
				map.put("modalidad", aula.getId_cme());
				result.setResult(map);
				
				/*if(antiguo_sin_cronograma){
					map.put("aula_sugerida", "0");
				}*/
				result.setResult(map);
				return result;

			} else{
				map.put("id_mat", null);

				
				/*********************************************************************************/
				/** NUEVA MATRICULA - VAMOS A MATRICULAR! */
				/*********************************************************************************/

				matricula = new Matricula();
				matricula.setId_fam(id_apod);

				ConfCuota confCuotaMatricula = null;
				Integer id_per_mat = null;// Periodo que le corresponde la matricula
				Integer id_niv = null;// nivel que le corresponde
				Integer id_gra = null;// grado que le corresponde
				Integer id_au = null;// Aula que le corresponde
				String anio_anterior = null;
				Periodo periodoEscolar = null;
				Aula aulaAnterior = null;

				if (id_cli.equals(Constante.CLIENTE_NUEVO)) {

					/*********************************************************************************/
					/** VALIDACION DE CRONOGRAMA PARA ALUMNOS NUEVOS */
					/*********************************************************************************/
					if(matricula_segun_cronograma.equals("1")){
						boolean nuevo_con_cronograma_cambio_seccion = confFechasDAO.cambioSeccionVigente(id_anio, "NC");
						boolean nuevo_sin_cronograma_cambio_seccion = confFechasDAO.cambioSeccionVigente(id_anio, "NS");
						map.put("nuevo_sin_cronograma_cs",(nuevo_sin_cronograma_cambio_seccion) ? "1" : "0" );
						map.put("nuevo_con_cronograma_cs",(nuevo_con_cronograma_cambio_seccion) ? "1" : "0" );
						
						// VALIDAR QUE ESTA EN FECHA - CRONOGRAMA ALUMNOS NUEVOS
						if (!nuevo_con_cronograma_cambio_seccion || !nuevo_sin_cronograma_cambio_seccion) {
							result.setCode("500");
							//result.setMsg("No se encuentra configurado la fecha inicial para alumnos nuevos,Debe ir a la opcion de configuracion de fechas de matricula");
							result.setMsg("No se encuentra dentro del cronograma de matrículas para nuevos, establecido por la institución");
							return result;
						} /*else {
							Date fecInicial = confFechas.getDel();
							Date fecFinal = confFechas.getAl();

							Calendar cal = Calendar.getInstance();
							cal.add(Calendar.DATE, 1);
							Date fechaSigueinte = cal.getTime();

							Calendar cal2 = Calendar.getInstance();
							cal2.add(Calendar.DATE, -1);
							Date fechaAnterior = cal2.getTime();


							if (fechaSigueinte.after(fecInicial) && (fecFinal == null || fechaAnterior.before(fecFinal))) {
								logger.debug("Esta dentro del rango de fecha para nuevo alumno");
							} else {
								result.setCode("500");
								result.setMsg("La fecha de incio para alumnos nuevos es a partir del " + fecInicial);
								return result;
							}
						}*/			

					}	
					/*********************************************************************************/
					/**
					 * ONTENER NIVEL, GRADO Y SECCION QUE LE CORRESPONDE Y CUOTA PARA SU
					 * MATRICULA
					 */
					/*********************************************************************************/					
						if(admision_obligatorio_nuevos.equals("1")) { // Verficamos el proceso de admisión que haya obtenido vacante
							Param param = new Param();
							param.put("alu.id", id_alu); 
							param.put("matr_vac.res", "A");							
							param.put("per.id_anio", id_anio);
							List<MatrVacante> matrVacanteList = matvacanteDAO.listFullByParams(param, null);
							if(matrVacanteList.size()<=0){
								result.setCode("201");
								result.setMsg("ATENCION,\nEl alumno no tiene condici�n de vacante definido!!");				
								return result;
							} else {
								MatrVacante matrVacante = new MatrVacante();
								matrVacante = matrVacanteList.get(0);
								Periodo periodoVacante = periodoDAO.get(matrVacante.getEvaluacionVac().getId_per()); 
								id_gra = matrVacante.getGrad().getId();
								id_niv = matrVacante.getGrad().getId_nvl();

								param = new Param();
								param.put("pee.id_srv", periodoVacante.getId_srv());
								param.put("pee.id_anio", periodoVacante.getId_anio());
								param.put("pee.est", "A");
								param.put("pee.id_tpe", Constante.TIPO_PERIODO_ESCOLAR);
								periodoEscolar = periodoDAO.listFullByParams(param, null).get(0);
								id_per_mat = periodoEscolar.getId();
							}
						}
						
						if(reserva_nuevos_obligatorio.equals("1")){	//Verficamos que haya reservado
							// Revisar si tiene reserva
							Param param = new Param();
							param = new Param();
							param.put("id_alu", id_alu);
							param.put("id_gra", id_gra);
							List<Reserva> reservas = reservaDAO.listFullByParams(param, null);
							//final Date todayDate = new Date();
							//logger.debug(todayDate);

							if (reservas.size() > 0) {	
								Reserva reserva = reservas.get(0);
								if (new Date().before(reserva.getFec_lim()) || ((new SimpleDateFormat("yyyy-MM-dd").format(new Date())).equals(new SimpleDateFormat("yyyy-MM-dd").format(reserva.getFec_lim())))){
									tieneReserva = true;
									matricula.setId_fam(reserva.getId_fam());
									matricula.setId_au(reserva.getId_au());
								}
							} else {
								result.setCode("201");
								result.setMsg("ATENCION,\nEl alumno no tiene reserva de matrícula!!");				
								return result;
							}	
						} 
				}
				/*************************************************************************************/
				/** ALUMNO ANTIGUO - VAMOS A MATRICULAR! */
				/*************************************************************************************/
				if (!id_cli.equals(Constante.CLIENTE_NUEVO)) {

					/*********************************************************************************/
					/** VALIDACION DE CRONOGRAMA PARA ALUMNOS ANTIGUOS */
					/*********************************************************************************/
					//RG1: Verficar si para la matrícula se hará según cronograma o no
					if(matricula_segun_cronograma.equals("1")){
						boolean antiguo_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "AS");
						boolean antiguo_con_cronograma = cronogramaDAO.alumnosAntiguosTienenVigente(id_anio);
						boolean antiguo_cambio_seccion = confFechasDAO.cambioSeccionVigente(id_anio, "AS");
						boolean antiguo_sin_cronograma_cambio_seccion = confFechasDAO.cambioSeccionVigente(id_anio, "AS");
						boolean antiguo_con_crograma_cambio_seccion = confFechasDAO.cambioSeccionVigente(id_anio, "AC");
						map.put("antiguo_sin_cronograma",(antiguo_sin_cronograma) ? "1" : "0" );
						map.put("antiguo_con_cronograma",(antiguo_con_cronograma) ? "1" : "0" );
						map.put("antiguo_sin_cronograma_cs",(antiguo_sin_cronograma_cambio_seccion) ? "1" : "0" );
						map.put("antiguo_con_cronograma_cs",(antiguo_con_crograma_cambio_seccion) ? "1" : "0" );
						
						//Verificar si existe cronograma vigente
						if(antiguo_sin_cronograma || antiguo_con_cronograma || antiguo_cambio_seccion || antiguo_sin_cronograma_cambio_seccion || antiguo_con_crograma_cambio_seccion) {
							Param param = new Param();
							param.put("id_anio", id_anio);

							boolean estaDentroCronograma = cronogramaDAO.dentroCronograma(id_anio, alumno.getApe_pat());

							if (!estaDentroCronograma) {

								param = new Param();
								param.put("tipo", "E");// E = EXTEMPORANEO
								param.put("id_anio", id_anio);
								ConfFechas confFechas = confFechasDAO.getByParams(param);

								if(confFechas!=null){


									Date fecInicial = confFechas.getDel();
									Date fecFinal = confFechas.getAl();

									Calendar cal = Calendar.getInstance();
									cal.add(Calendar.DATE, 1);
									Date fechaSigueinte = cal.getTime();

									Calendar cal2 = Calendar.getInstance();
									cal2.add(Calendar.DATE, -1);
									Date fechaAnterior = cal2.getTime();

									logger.debug("fechaAnterior:" + fechaAnterior);

									if (fecInicial.after(fechaAnterior)) {
										// TODAVIA NO INICIA EXTEMPORANLEOA
										map.put("cronograma", "A");// existe cronograma
																			// para alumnos
																			// antiguos
										result.setCode("500");
										result.setMsg("El alumno no tiene programado su matricula para el dia de hoy, por favor verificar su cronograma");
										return result;
									}
									if (fechaSigueinte.after(fecInicial) && (fecFinal == null || fechaAnterior.before(fecFinal))) {
										map.put("cronograma", "E");// es cronograma extemporaneo
										logger.debug("Esta dentro del rango de fecha para extemporaneo");
									} else {
										result.setCode("500");
										result.setMsg("La fecha de incio para alumnos nuevos es a partir del " + fecInicial);
										return result;
									}
									
								} else {
									result.setCode("500");
									result.setMsg("El alumno no tiene programado su matricula para el dia de hoy, por favor verificar su cronograma");
									return result;
								}
							} 						
						} else {
							result.setCode("500");
							result.setMsg("No existe cronograma vigente para matrícula de alumnos antiguos, de acuerdo al cronograma de la IE");
							return result;
						}
					
					}

					/*********************************************************************************/
					/** VALIDACION DE DEUDA DE ALUMNOS ANTIGUOS */
					/*********************************************************************************/
					/* Usado solo para el AE, en el proceso de migración
					String validacionDeuda = validarDeuda(alumno.getNro_doc());
					if (validacionDeuda != null) {
						result.setCode("500");
						result.setMsg(validacionDeuda);
						return result;
					}*/
					
					//ReglasNegocio regla_deuda = reglasNegocioDAO.getByParams(new Param("cod",Constante.RESTR_MAT_DEUDA));
					//String restringir_matricula_deuda=regla_deuda.getVal();
					//if(restringir_matricula_deuda.equals("1")){
						BigDecimal deuda = pagosService.pagosPendientes(id_alu, id_anio);
						if (deuda.compareTo(BigDecimal.ZERO)>0) {
							result.setCode("500");
							result.setMsg("�ATENCI�N!,\n Primero debe cancelar las mensualidades vencidas.\nDeuda de la familia S/ " + deuda);
							return result;
						}	
					//}

					/*********************************************************************************/
					/** VALIDACION DE CONDICION DE ALUMNOS ANTIGUOS */
					/*********************************************************************************/
					/* Usado solo para el colegio AE
					String condicionAlumnoArr[] = validarCondicion(alumno.getNro_doc());
					if (condicionAlumnoArr != null) {
						String condicion = condicionAlumnoArr[0];
						String descripcionCondicion = condicionAlumnoArr[1];
						
						if ("1".equals(condicion)){
							//ES UNA ADVERTENCIA
							//result.setCode("500");
							//result.setMsg("EL ALUMNO PUEDE MATRICULARSE, PERO TIENE MATRICULA CONDICIONADA POR: " + descripcionCondicion);
						}else{
							//NO PUEDE MATRICULARSE
							//result.setCode("500");
							//result.setMsg("EL ALUMNNO NO PUEDE MATRICULARSE: " + descripcionCondicion);
							//model.addObject("matricula", matricula);
							return result;
						}
					}*/
					
					//Restringir la matricula o validar las matriculas condicionadas
					//ReglasNegocio condicion = reglasNegocioDAO.getByParams(new Param("cod",Constante.VAL_MATR_COND));
					//String condicion_matricula=condicion.getVal();
					//if(condicion_matricula.equals("1")){
						//Pra el 2021 no valida condicion
						/*List<CondicionBean> condiciones = condicionService.mensajeCondicionalumno(id_alu);
						int nroMatriculasCondiconales = 0;
						String matriculaCondicionada ="";
						if(condiciones!=null)
						for (CondicionBean condicionBean : condiciones) {
							
							if (condicionBean.getTipo().equals("M")){
								nroMatriculasCondiconales ++;
								if (matriculaCondicionada.length()==0)
									matriculaCondicionada = matriculaCondicionada  + condicionBean.getObs();
								else 
									matriculaCondicionada = matriculaCondicionada  + ", " + condicionBean.getObs();
								
							}
							
							if (condicionBean.getTipo().equals("B")){
								result.setCode("201");
								result.setMsg("ATENCION: " + condicionBean.getObs());
								
								return result;
							}
							
							if (condicionBean.getTipo().equals("V")){
								result.setCode("201");
								result.setMsg("ATENCION,\n El alumno pierde vacante:" + condicionBean.getObs());
								
								return result;
							}
							
							
						}
						
						if (nroMatriculasCondiconales >1){
							result.setCode("201");
							result.setMsg("ATENCION,\nEl alumno tiene dos matriculas condicionadas:" + matriculaCondicionada);
							
							return result;
						}

						if (nroMatriculasCondiconales ==1){
							//result.setCode("418");//warning
							map.put("condicionada", "ATENCION,\nEl alumno puede matricularse pero tiene matricula condicionada:" + matriculaCondicionada);
							
//							return result;
						}*/

					//}
					
					/*********************************************************************************/
					/**
					 * ONTENER NIVEL, GRADO Y SECCION QUE LE CORRESPONDE Y CUOTA PARA SU
					 * MATRICULA
					 */
					/*********************************************************************************/		
					// Revisar si tiene reserva
					Param param = new Param();
					param.put("id_alu", id_alu);
					param.put("id_gra", id_gra);
					List<Reserva> reservas = reservaDAO.listFullByParams(param, new String[]{"mat_res.id desc"});
					Reserva reserva = null;
					if (reservas.size() > 0) {
						reserva = reservas.get(0);
						if (new Date().before(reserva.getFec_lim()) || ((new SimpleDateFormat("yyyy-MM-dd").format(new Date())).equals(new SimpleDateFormat("yyyy-MM-dd").format(reserva.getFec_lim())))){
							tieneReserva = true;
							matricula.setId_fam(reserva.getId_fam());
							matricula.setId_au(reserva.getId_au());
							
						}
					}
					
					// ALUMNO ANTIGUO
					Integer id_per_ant = matriculaAnterior.getId_per();
					
					Periodo periodoAnterior = periodoDAO.get(id_per_ant);
					anio_anterior = periodoAnterior.getAnio().getNom();
					map.put("anio_anterior", anio_anterior);
					aulaAnterior = aulaDAO.getFull(matriculaAnterior.getId_au(), new String[] { Grad.TABLA });
					Integer id_sit = matriculaAnterior.getId_sit();

					if (id_sit == null) {
						result.setCode("500");
						//result.setMsg("El alumno no tiene condicion final de matricula:" + periodoAnterior.getAnio().getNom());
						result.setMsg("El alumno no tiene asignado una situacion final academica, coordinar con el administrador para regualizar la informacion");
						return result;
					}

					
					if(solicitud_cambio_local){
						 param = new Param();
						param.put("anio.id", id_anio);
						param.put("suc.id", solicitudCambioLocal.getId_suc_des());
						param.put("tpe.id ", Constante.TIPO_PERIODO_ESCOLAR);
						param.put("srv.id_niv ", matricula.getId_niv());
						
						List<Periodo> peridodos = periodoDAO.listFullByParams(param, null);
						id_per_mat = peridodos.get(0).getId() ;
					}else{
						param = new Param();
						param.put("pee.id_srv", periodoAnterior.getId_srv());
						param.put("pee.id_anio", id_anio);
						param.put("pee.est", "A");
						param.put("pee.id_tpe", com.tesla.colegio.util.Constante.TIPO_PERIODO_ESCOLAR);
						periodoEscolar = periodoDAO.listFullByParams(param, null).get(0);

						id_per_mat = periodoEscolar.getId();
					}

					param = new Param();
					param.put("aula.id_per", id_per_mat);
					// param.put("id_grad", id_gra);
					param.put("aula.id_secc_ant", aulaAnterior.getId());
					List<Aula> aulaNuevaList = aulaDAO.listFullByParams(param, null);//Aula definida para el presente año

					if (aulaNuevaList.size() == 0) {

						if (id_sit.equals(Constante.SITUACION_FINAL_APROBADO)) {
							Integer id_srv = null;
							//id_suc = Integer.parseInt(httpSession.getAttribute("_ID_LOCAL").toString());

							// buscamos el siguiente nivel
							if (aulaAnterior.getGrad().getId().equals(3)) {// 5 años pasa a primaria
								id_niv = 2;
								param = new Param();
								param.put("nom", "PRIMARIA");
								param.put("id_suc", id_suc);
								Servicio servicio = servicioDAO.getByParams(param);
								id_srv = servicio.getId();

							} else if (aulaAnterior.getGrad().getId().equals(9)) {
								id_niv = 3;

								param = new Param();
								param.put("nom", "SECUNDARIA");
								param.put("id_suc", id_suc);
								Servicio servicio = servicioDAO.getByParams(param);
								id_srv = servicio.getId();

							} else{
								id_niv = aulaAnterior.getGrad().getId_nvl();
								String nom_ser = null;
								
								if(id_niv.equals(1)){
									nom_ser="INICIAL";
									
								} else if(id_niv.equals(2)){
									nom_ser="PRIMARIA";
								} else{
									nom_ser="SECUNDARIA";
								}
								param = new Param();
								param.put("nom", nom_ser);
								param.put("id_suc", id_suc);
								Servicio servicio = servicioDAO.getByParams(param);
								id_srv = servicio.getId();
								
							}
							id_gra = aulaAnterior.getId_grad() + 1;

							param = new Param();
							param.put("pee.id_srv", id_srv);
							param.put("pee.id_anio", id_anio);
							param.put("pee.est", "A");
							param.put("pee.id_tpe", com.tesla.colegio.util.Constante.TIPO_PERIODO_ESCOLAR);
							periodoEscolar = periodoDAO.listFullByParams(param, null).get(0);

							id_per_mat = periodoEscolar.getId();

							/*// CREO Q ESTA PARTE YA NO VA

							param = new Param();
							param.put("id_per", id_per);// OK
							param.put("id_grad", id_gra);

							// param.put("secc", aulaAnterior.getSecc());
							param.put("id_secc_ant", aulaAnterior.getId());
							Aula aula = aulaDAO.getByParams(param);

							if (aula == null)
								map.put("aula_sugerida", "0");
							else {
								id_au = aula.getId();
								map.put("aula_sugerida", "1");
							}*/

						}
						if (id_sit.equals(Constante.SITUACION_FINAL_DESAPROBADO)) {
							// buscamos el nivel actual
							id_niv = aulaAnterior.getGrad().getId_nvl(); // es un
																			// catalogo
							id_gra = aulaAnterior.getGrad().getId();
							//id_per= aulaAnterior.getId_per();

							param = new Param();
							param.put("secc", aulaAnterior.getSecc());
							param.put("id_per", id_per_mat);
							param.put("id_grad", id_gra);

							Aula aula = aulaDAO.getByParams(param);

							// if(aula==null)
							map.put("id_gra", "0");
							map.put("aula_sugerida", "0"); // EL REPITENTE
																	// PUEDE
																	// SELECCIONAR
																	// SU AULA
						//ojocargarListas(model, id_per, id_gra, null, null);
							// else
							// model.addObject("aula_sugerida","1");

						}

						/*
						 * param = new Param(); param.put("aula.id_per", id_per);
						 * param.put("id_grad", id_gra); aulaNuevaList =
						 * aulaDAO.listFullByParams(param,null);
						 */

						// model.addObject("_error", "El alumno no tiene una secci�n
						// configurada");
						// return model;
					} else {
						map.put("aula_sugerida", "1");

						Aula aulaNueva = aulaNuevaList.get(0);
						id_au = aulaNueva.getId();
						id_gra = aulaNueva.getId_grad();
						id_niv = aulaNueva.getGrad().getId_nvl();
						/*CODIGO NUEVO*/
						if(reserva !=null){
							matricula.setId_au(reserva.getId_au());
						}
						else					
							matricula.setId_au(id_au);
					}
					
					if (id_sit.equals(Constante.SITUACION_FINAL_APROBADO)) {
						matricula.setId_au(id_au);
						map.put("des_sit", "APROBADO");
						map.put("id_con", EnumCondicionMatricula.PROMOVIDO.getValue());
					}

					if (id_sit.equals(Constante.SITUACION_FINAL_REQUIERE_RECUPERACION)) {
						result.setCode("500");
						result.setMsg("El alumno requiere recuperaci�n pedag�gica");
						//model.addObject("matricula", matricula);
						map.put("des_sit", "REQUIERE RECUPERACION");
						result.setResult(map);
						return result;
					}

					if (id_sit.equals(Constante.SITUACION_FINAL_DESAPROBADO)) {
						//result.setCode("500");
						result.setMsg("El alumno est� desaprobado, repite de a�o");
						map.put("des_sit", "DESAPROBADO");
						map.put("id_con", EnumCondicionMatricula.REPITENTE.getValue());

						matricula.setId_niv(matriculaAnterior.getId_niv());
						matricula.setId_gra(matriculaAnterior.getId_gra());
						
						param = new Param();
						param.put("aula.id_per", id_per_mat);
						param.put("aula.secc", aulaAnterior.getSecc());
						//List<Aula> aulaNuevaList = aulaDAO.listFullByParams(param, null);
						//model.addObject("id_gra", "0");
						map.put("aula_sugerida", "0"); // EL REPITENTE PUEDE SELECCIONAR SU AULA

					}

					if (id_sit.equals(Constante.SITUACION_FINAL_RETIRADO)) {
						result.setCode("500");
						result.setMsg("El alumno es� con situaci�n de retirado, debe volver a postular a una vacante");
						//model.addObject("matricula", matricula);
						
						map.put("des_sit", "RETIRADO");
						result.setResult(map);
						return result;
					}

					if (id_sit.equals(Constante.SITUACION_FINAL_TRASLADADO)) {
						result.setCode("500");
						map.put("des_sit", "TRASLADADO");
						result.setMsg("El alumno es� trasladado, no puede matricularse");
						//model.addObject("matricula", matricula);
						return result;
					}

					if (id_sit.equals(Constante.SITUACION_FINAL_FALLECIDO)) {
						map.put("des_sit", "FALLECIDO");
						result.setCode("500");
						result.setMsg("El alumno est� como fallecido, no puede matricularse");
						//model.addObject("matricula", matricula);
						result.setResult(map);
						return result;
					}

					if (id_sit.equals(Constante.SITUACION_FINAL_ADELANDO_EVALUCACION)) {
						result.setCode("500");
						map.put("des_sit", "ADELANTO EVALUACION");
						result.setMsg("El alumno est� como 'Adelanto de evaluaci�n', no puede matricularse");
						//model.addObject("matricula", matricula);
						result.setResult(map);
						return result;
					}
					
					if (id_sit.equals(Constante.SITUACION_FINAL_POSTERFACION_EVALUACION)) {
						map.put("des_sit", "POSTERGACION EVALUACION");
						result.setResult(map);
						result.setMsg("El alumno est� como 'Postergaci�n de evaluaci�n', no puede matricularse");
						//model.addObject("matricula", matricula);
						result.setResult(map);
						return result;
					}

					//Parametrizar
					//SI EL AULMNO TIENE AULA SUGERIDA
					if (!tieneReserva && matriculaAnterior!=null && matriculaAnterior.getId_au_nue()!=null){
						map.put("aula_sugerida", "1");
						matricula.setId_au(matriculaAnterior.getId_au_nue());	
					}
					
					map.put("aula_sugerida", "0");
					if ( tieneReserva && solicitudCambioLocal==null)
						map.put("aula_sugerida", "1");
					

				}
				Param param = new Param();
				param.put("id_per", id_per_mat);

				ConfMensualidad confMensualidad = confMensualidadDAO.getByParams(param);

				matricula.setFecha(new Date());
				matricula.setId_niv(id_niv);
				matricula.setId_gra(id_gra);

				matricula.setId_per(id_per_mat);
				matricula.setPeriodo(periodoEscolar);
				matricula.setCar_pod("0");
				matricula.setId_alu(id_alu);
				//cargarListas(model, periodoEscolar.getId(), id_gra, id_alu, matricula);

				if (confMensualidad == null) {
					result.setCode("500");
					result.setMsg("No se encuentra configurado el pago de mensualidad");
					//model.addObject("matricula", matricula);
					return result;
				}
				
				Param param1 = new Param();
				param1.put("id_alu", id_alu);
				param1.put("id_gra", id_gra);
			}
		
			
			
			

			
			Integer id_con = obtenerCondicionMatricula(id_alu);
			if (id_con != com.tesla.colegio.util.Constante.CONDICION_MATRICULA_INGRESANTE) {
				map.put("des_sit", "MATRICULA INGRESANTE");
				result.setCode("500");
				result.setResult("Aun no esta desarrollado cuando se matricula a alumno actual");
				//model.addObject("matricula", matricula);
				result.setResult(map);
				return result;
			}
			matricula.setId_con(id_con);

			matricula.setId_cli(id_cli);

			
			map.put("matricula", matricula);
			//Buscar si realizó el pago
			Param param_pago = new Param();
			param_pago.put("id_mat", matricula.getId());
			param_pago.put("tip", "MAT");
			param_pago.put("canc", "1");
			AcademicoPago academicoPago = academicoPagoDAO.getByParams(param_pago);
			map.put("pago", academicoPago);
			Aula aula = aulaDAO.get(matricula.getId_au_asi());
			map.put("modalidad", aula.getId_cme());
			result.setResult(map);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode("500");
			result.setMsg("Error en el sistema, consulte con el administrador del sistema");
			
			//model.addObject("matricula", matricula);
			return result;
		}
	}
	
	//ya no es usado
	/*@RequestMapping(value = "/MatriculaCapacidad2", method = RequestMethod.GET)
	public AjaxResponseBody capacidad(  Integer id_au, Integer id_alu, Integer id_suc) throws Exception {
		AjaxResponseBody result = new AjaxResponseBody();
		Integer id_anio = 1; //TODO: Luego lo parametrizo.tengo sue�o
		Aula aulaActual = aulaDAO.getFull(id_au, new String[]{Grad.TABLA});
		
		Integer id_secc_ant =aulaActual.getId_secc_ant();
		Aula aulaAnterior = null;
		
		if(id_secc_ant==null || id_secc_ant.equals(0)){
			/*
			//vamos al aula anterior
			Integer id_grad_anterior = aulaActual.getGrad().getId_gra_ant();
			Grad gradoAnterior = gradoDAO.get(id_grad_anterior);
			Integer id_nivl_anterior = gradoAnterior.getId_nvl();
			Param param = new Param();
			param.put("id_suc", id_suc);
			param.put("id_niv", id_nivl_anterior);
			Servicio servicio = servicioDAO.getByParams(param);
			
			//periodo anterior
			param = new Param();
			param.put("id_srv", servicio.getId());
			param.put("id_anio", id_anio);
			param.put("id_tpe", Constante.TIPO_PERIODO_ESCOLAR);
			param.put("est", "A");
			
			Periodo periodoAnterior = periodoDAO.getByParams(param);
			
			//aula anterior
			param = new Param();
			param.put("id_per", periodoAnterior.getId());
			param.put("id_grad", id_grad_anterior);
			param.put("secc", aulaActual.getSecc());
			param.put("est", "A");
			
			aulaAnterior = aulaDAO.getByParams(param);
			*/
		/*}
		else
			aulaAnterior = aulaDAO.get(id_secc_ant);
		
		Integer[] reservaArray = reservaDAO.getCapacidad(id_au);
		
		Integer capacidad = reservaArray[0];
		Integer reservados = reservaArray[1];
		Integer total_inscritos = matriculaDAO.getTotalInscritos(id_au);
		Integer inscritos = matriculaDAO.getInscritosNuevos(id_au,id_secc_ant);
		//Integer pasan_anio = matriculaDAO.getPasanAnio(aulaAnterior.getId());//Pasan de a�o y jose no los mandan a otra seccion
		Integer pasan_anio = matriculaDAO.getPasanAnio(id_secc_ant);//Pasan de a�o y jose no los mandan a otra seccion y no
		Integer alumnos_de_otra_seccion_sugeridos =matriculaDAO.getSugeridosSeccion(id_au) ;//alumnos q vienen de otra seccion ocuparan vacantes
		Integer alumnos_se_van_otra_seccion_sugeridos =matriculaDAO.getAlumnosSeVAmOtraSeccion(id_au,id_secc_ant) ;
				
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("capacidad", capacidad);
		map.put("total_inscritos", total_inscritos);
		map.put("inscritos", inscritos);
		map.put("reserva", reservados);
		map.put("pasan_anio", pasan_anio);
		map.put("alumnos_de_otra_seccion_sugeridos", alumnos_de_otra_seccion_sugeridos);
		map.put("alumnos_se_van_otra_seccion_sugeridos", alumnos_se_van_otra_seccion_sugeridos);

		boolean antiguo_con_cronograma = cronogramaDAO.alumnosAntiguosTienenVigente(id_anio);
		
		if (!antiguo_con_cronograma){
			//se liberan vacantes
			pasan_anio = 0;
			map.put("vacantes_disponibles", capacidad -reservados -pasan_anio -alumnos_de_otra_seccion_sugeridos -total_inscritos + alumnos_se_van_otra_seccion_sugeridos);
		}else
			map.put("vacantes_disponibles", capacidad -reservados -pasan_anio -alumnos_de_otra_seccion_sugeridos -inscritos + alumnos_se_van_otra_seccion_sugeridos);
		
		logger.debug(map);
		result.setResult(map);
		return result;
		//response.setContentType("application/json");
		//response.getWriter().write(JsonUtil.toJson(map));

	}*/

	/**
	 * Segunda version del metodo para calcular la capacidad
	 * version mejorada
	 * @param id_au
	 * @param id_alu
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/capacidad", method = RequestMethod.GET)//usado hasta el 2020 x aul
	public AjaxResponseBody capacidadFinal(  Integer id_au, Integer id_alu) throws Exception {
		AjaxResponseBody result = new AjaxResponseBody();
		//id_au=233;
		result.setResult(vacanteService.getNroVacantesMatricula(id_au, id_alu));
				
		return result;
	}
	
	@RequestMapping(value = "/capacidadxGrado", method = RequestMethod.GET)
	public AjaxResponseBody capacidadFinalxGrado( Integer id_grad,Integer id_suc, Integer id_niv, Integer id_anio, Integer id_alu) throws Exception {
		AjaxResponseBody result = new AjaxResponseBody();
		//id_au=233;
		result.setResult(vacanteService.getNroVacantesMatriculaxGrado(id_grad, id_suc, id_niv, id_anio, id_alu));
				
		return result;
	}
	
	@RequestMapping(value = "/capacidadxGradoModalidad", method = RequestMethod.GET)
	public AjaxResponseBody capacidadxGradoModalidad( Integer id_grad,Integer id_suc, Integer id_niv, Integer id_anio, Integer id_alu, Integer id_cme) throws Exception {
		AjaxResponseBody result = new AjaxResponseBody();
		//id_au=233;
		result.setResult(vacanteService.getNroVacantesMatriculaxGradoyModalidad(id_grad, id_suc, id_niv, id_anio, id_alu,id_cme));
				
		return result;
	}
		
	@RequestMapping(value = "/capacidadAcadVac", method = RequestMethod.GET)
	public AjaxResponseBody capacidadFinalAcadVac(Integer id_cic, Integer id_au, Integer id_grad) throws Exception {
		AjaxResponseBody result = new AjaxResponseBody();
		result.setResult(vacanteService.getNroVacantesMatriculaAcadVac(id_cic, id_au, id_grad));
				
		return result;
	}

	
	private String validarDeuda(String dni) {


		List<Parametro> parametros = parametroDAO.list();
		String servicio = null;
		for (Parametro parametro : parametros) {
			if (parametro.getNom().equals("URL_SERVIDOR_EXTERNO"))
				servicio =parametro.getVal(); 
		}
		
		String surl = servicio + "?mod=deudoresDNI&alu_dni=" + dni;

		logger.debug(surl);
		try {
			URL url = new URL(surl);

			InputStream is = url.openConnection().getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			String line = null;
			String deuda = null;
			while ((line = reader.readLine()) != null) {
				logger.debug(line);
				deuda = line;
			}

			if (deuda == null || "0".equals(deuda.trim()))
				return null;
			else
				return "La familia tiene una deuda de " + deuda;

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}

	}
	
	private String[] validarCondicion(String dni) {

		List<Parametro> parametros = parametroDAO.list();
		String servicio = null;
		for (Parametro parametro : parametros) {
			if (parametro.getNom().equals("URL_SERVIDOR_EXTERNO"))
				servicio =parametro.getVal(); 
		}
		
		String surl = servicio + "?mod=condicionAlumno&alu_dni=" + dni;

		logger.debug(surl);
		try {
			URL url = new URL(surl);

			InputStream is = url.openConnection().getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			String line = null;
			String mensaje = null;
			while ((line = reader.readLine()) != null) {
				logger.debug(line);
				mensaje = line;
			}

			if (mensaje == null || "".equals(mensaje.trim()))
				return null;
			else
				return mensaje.split("\\|");

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}

	}
	
	private Integer obtenerCondicionMatricula(Integer id_alu) {
		return com.tesla.colegio.util.Constante.CONDICION_MATRICULA_INGRESANTE;

	}
	
	/**
	 * 
	 * @param model
	 * @param tipo:
	 *            Dos valores: A:Actual correlativo, S:Siguiente correlativo
	 * @param id_fam
	 * @param id_per
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/MatriculaContratoCorrelativo", method = RequestMethod.GET)
	public AjaxResponseBody matriculaContratoCorrelativo(Integer id_fam,Integer id_anio, Integer id_suc, HttpServletResponse response) throws Exception {
		AjaxResponseBody result = new AjaxResponseBody();
		Map<String, Object> mapContrato = new HashMap<String, Object>();

		Map<String, Object> contratoActual = matriculaDAO.getContrato(id_fam, id_anio);

		logger.debug("contratoActual:" + contratoActual);

		if (contratoActual == null) {
			Anio anio = anioDAO.get(id_anio);
			
			//No comprendo xq se consideraria periodo??
			Map<String, Object> map = matriculaDAO.getTotalContratosxLocal(id_anio, id_suc);
			Double total = (Double)map.get("total");
			
			int correlativo = total.intValue() + 1;
			
			if (correlativo==1){
				Sucursal sucursal = sucursalDAO.get(id_suc);
				map.put("cod", sucursal.getCod());
			}
			
			String contrato = anio.getNom() + "-" +  map.get("cod") + "-" + String.format("%05d", correlativo);
			logger.debug("contrato :" + contrato);

			mapContrato.put("contrato", contrato);
		} else {
			mapContrato.put("contrato", contratoActual.get("num_cont").toString());
		}

		logger.debug("mapContrato:" + mapContrato);
		result.setResult(mapContrato);
		return result;

	}
	
	@RequestMapping(value = "/listarSituacionFinal")
	public AjaxResponseBody listarNotasArea(Integer id_au) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(matriculaDAO.listaSituacionFinal(id_au) );
		
		return result;
	}
	


	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( matriculaDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	

	@RequestMapping( method = RequestMethod.GET)
	public AjaxResponseBody porAnio( Integer id_alu, Integer id_anio ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( matriculaDAO.getMatricula(id_alu, id_anio));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping(value = "/actualizarSituacion", method = RequestMethod.POST)
	public AjaxResponseBody actualizarSituacion(Integer id_mat, Integer id_sit) throws Exception {
	
		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			situacionFinalService.actualizarSituacion(id_mat, id_sit);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

		
	}
	
	@RequestMapping(value = "/actualizarRatificacion", method = RequestMethod.POST)
	public AjaxResponseBody actualizarRatificacion(Integer id_alu, Integer id_grad, Integer id_suc,Integer id_rat, Integer res) throws Exception {
	
		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			Ratificacion ratificacion = ratificacionDAO.get(id_rat);
			//Integer id_anio_sig=ratificacion.getId_anio_rat();
			//Anio anio= anioDAO.get(id_anio_sig);
			//Integer nro_vac = vacanteService.getNroVacantesReservaxGrado(id_anio_sig, id_alu, id_grad, id_suc);
			//Integer capacidad = vacanteService.getCapacidadxGrado(Integer.parseInt(anio.getNom()), id_grad, id_suc);
			//if(nro_vac>0) {
				matriculaDAO.actualizarRatificacion(id_rat, res);
				result.setResult(1);
			/*} else {
				result.setCode("500");
				result.setMsg("No se puede actualizar la ratificación porque no hay vacante!!");
				return result;
			}*/
			
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

		
	}
	
	@RequestMapping(value = "/validarContrato/{id_mat}", method = RequestMethod.POST)
	public AjaxResponseBody validarContrato(@PathVariable Integer id_mat) throws Exception {
	
		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			matriculaDAO.validarContrato(id_mat);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

		
	}
	
	@RequestMapping(value = "/matriculasContratoEnviado/{id_gpf}/{id_anio}", method = RequestMethod.GET)
	public AjaxResponseBody matriculasContratoEnviado(@PathVariable Integer id_gpf, @PathVariable Integer id_anio) throws Exception {
	
		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(matriculaDAO.listarMatriculasContratoEnv(id_gpf, id_anio));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

		
	}
	
	@RequestMapping(value = "/matriculasDeclaracionEnviado/{id_gpf}/{id_anio}", method = RequestMethod.GET)
	public AjaxResponseBody matriculasDeclaracionEnviado(@PathVariable Integer id_gpf, @PathVariable Integer id_anio) throws Exception {
	
		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(matriculaDAO.listarMatriculasDeclaEnv(id_gpf, id_anio));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

		
	}
	
	@RequestMapping(value = "/matriculasProtocoloEnviado/{id_gpf}/{id_anio}", method = RequestMethod.GET)
	public AjaxResponseBody matriculasProtocoloEnviado(@PathVariable Integer id_gpf, @PathVariable Integer id_anio) throws Exception {
	
		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(matriculaDAO.listarMatriculasProtocoloEnv(id_gpf, id_anio));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

		
	}
	
	@Transactional
	@RequestMapping(value = "/validarContratoxFamiliar/{id_fam}/{id_anio}", method = RequestMethod.POST)
	public AjaxResponseBody validarContratoxFamiliar(@PathVariable Integer id_fam,@PathVariable Integer id_anio) throws Exception {
	
		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			Param param = new Param();
			param.put("mat.tipo", "C");
			param.put("fam.id", id_fam);
			param.put("pee.id_anio", id_anio);
			param.put("pee.id_tpe", 1);
			List<Matricula> matriculas= matriculaDAO.listFullByParams(param, null);
			for (Matricula matricula : matriculas) {
				matriculaDAO.validarContratoxUsuario(matricula.getId());
				//Actualizar fecha de pago
				Param param2 = new Param();
				param2.put("id_mat", matricula.getId());
				param2.put("tip", "MAT");
				AcademicoPago academicoPago = academicoPagoDAO.getByParams(param2);
				if(academicoPago!=null) {
					//Fecha de vencimiento
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(new Date());
					calendar.add(Calendar.DAY_OF_YEAR, 1);
					//Date fechaActual=new Date();
					String cadenaFecha=FechaUtil.toString(calendar.getTime());

					Date fechaConcreta = new SimpleDateFormat("dd/MM/yyyy").parse(cadenaFecha);
					//academicoPago.setFec_venc(FechaUtil.toDate("20/02/2022"));
					academicoPago.setFec_venc(fechaConcreta);
					academicoPagoDAO.saveOrUpdate(academicoPago);
				}
				//Cuota de Ingreso
				//Actualizar fecha de pago
				Param param3 = new Param();
				param3.put("id_mat", matricula.getId());
				param3.put("tip", "ING");
				AcademicoPago academicoPago_ci = academicoPagoDAO.getByParams(param3);
				if(academicoPago_ci!=null) {
					academicoPago_ci.setFec_venc(FechaUtil.toDate("20/02/2022"));
					academicoPagoDAO.saveOrUpdate(academicoPago_ci);
				}		
			}
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

		
	}
	
	@RequestMapping(value = "/actualizarAulaGrado/{id}/{id_grad}/{id_au}", method = RequestMethod.POST)
	public AjaxResponseBody actualizarSituacion(@PathVariable Integer id, @PathVariable Integer id_grad, @PathVariable Integer id_au) throws Exception {
	
		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			matriculaDAO.actualizarMatriculaGradoAula(id, id_grad, id_au);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

		
	}

	@RequestMapping(value = "/actualizarSeccion", method = RequestMethod.POST)
	public AjaxResponseBody actualizarSeccion( Integer id , Integer id_au) throws Exception {
		
		AjaxResponseBody result = new AjaxResponseBody();

		
		/** FACTURACION **/
		
		//CAMBIO DE SECCION O LOCAL
		Matricula matricula = matriculaDAO.getFull(id,new String[]{Periodo.TABLA});
		Integer id_au_actual = matricula.getId_au_asi();
		
		Aula aulaNueva = aulaDAO.getFull(id_au,new String[]{Periodo.TABLA});
		Aula aulaActual = aulaDAO.get(id_au_actual);
		
		if (!aulaNueva.getId_per().equals(aulaActual.getId_per())){

			/*Pagos de matricula NUEVO*/
			
			//Impresion impresion = facturacionService.cambioLocal(matricula, aulaActual, id_au,aulaNueva.getPeriodo().getId_anio(), aulaNueva.getPeriodo().getId_suc(), aulaNueva.getId_per());
			 facturacionService.cambioLocal(matricula, aulaActual, id_au,aulaNueva.getPeriodo().getId_anio(), aulaNueva.getPeriodo().getId_suc(), aulaNueva.getId_per());
			//result.setResult(impresion);
		}else
			matriculaService.actualizarSeccion( id , id_au);


		 /*
		try {
			
			Matricula matricula = matriculaDAO.getFull(id,new String []{Periodo.TABLA});
			
			Param param = new Param();
			param.put("id_alu", matricula.getId_alu());
			param.put("id_anio", matricula.getPeriodo().getId_anio());
			param.put("est", "A");
			Solicitud solicitud = Solic
			
			matriculaDAO.actualizarSeccion(usuarioOBJ.getId(),id , id_au);
		} catch (Exception e) {
			result.setException(e);
		}
		*/
		
		return result;

		
	}
	
	@RequestMapping(value = "/actualizarGradoxLocal", method = RequestMethod.POST)
	public AjaxResponseBody actualizarGradoxLocal( Integer id , Integer id_per, Integer id_cme, Integer id_cct) throws Exception {
		
		AjaxResponseBody result = new AjaxResponseBody();

		
		/** FACTURACION **/
		
		//CAMBIO DE SECCION O LOCAL
		Matricula matricula = matriculaDAO.getFull(id,new String[]{Periodo.TABLA});
		Integer id_au_actual = matricula.getId_au_asi();
		Aula aulaActual = aulaDAO.get(id_au_actual);
		Periodo periodo_ant=periodoDAO.get(aulaActual.getId_per());
		Integer id_per_ac=aulaActual.getId_per();
		Periodo periodo_nuevo=periodoDAO.get(id_per);
		if (!id_per_ac.equals(id_per)){

			/*Pagos de matricula NUEVO*/
			
			//Impresion impresion = facturacionService.cambioLocal(matricula, aulaActual, id_au,aulaNueva.getPeriodo().getId_anio(), aulaNueva.getPeriodo().getId_suc(), aulaNueva.getId_per());
			// facturacionService.cambioLocal(matricula, aulaActual, id_au,aulaNueva.getPeriodo().getId_anio(), aulaNueva.getPeriodo().getId_suc(), aulaNueva.getId_per());
			 facturacionService.cambioLocalxGrado(matricula, aulaActual, id_per_ac, periodo_ant.getId_anio(), periodo_nuevo.getId_suc(), id_per, id_cme, id_cct);
			 //result.setResult(impresion);
		} //else
			//matriculaService.actualizarSeccion( id , id_au);
		
		return result;

		
	}
	
	@RequestMapping(value = "/pagarCambioLocal", method = RequestMethod.POST)
	public AjaxResponseBody pagarCambioLocal(@RequestBody  PagoMatriculaReq pagoMatriculaReq) throws Exception {
		
		AjaxResponseBody result = new AjaxResponseBody();

		
		/** FACTURACION **/
		
		//CAMBIO DE SECCION O LOCAL
		Matricula matricula = matriculaDAO.getFull(pagoMatriculaReq.getId_mat(),new String[]{Periodo.TABLA});
		Integer id_au_actual = matricula.getId_au_asi();
		
		Aula aulaNueva = aulaDAO.getFull(id_au_actual,new String[]{Periodo.TABLA});
		//Aula aulaActual = aulaDAO.get(id_au_actual);

			/*Pagos de matricula NUEVO*/
			
		Impresion impresion = facturacionService.pagarCambioLocal(matricula, matricula.getId_au(),aulaNueva.getPeriodo().getId_anio(), aulaNueva.getPeriodo().getId_suc(), aulaNueva.getId_per());
			// facturacionService.cambioLocal(matricula, aulaActual, id_au,aulaNueva.getPeriodo().getId_anio(), aulaNueva.getPeriodo().getId_suc(), aulaNueva.getId_per());
			result.setResult(impresion);

		 /*
		try {
			
			Matricula matricula = matriculaDAO.getFull(id,new String []{Periodo.TABLA});
			
			Param param = new Param();
			param.put("id_alu", matricula.getId_alu());
			param.put("id_anio", matricula.getPeriodo().getId_anio());
			param.put("est", "A");
			Solicitud solicitud = Solic
			
			matriculaDAO.actualizarSeccion(usuarioOBJ.getId(),id , id_au);
		} catch (Exception e) {
			result.setException(e);
		}
		*/
		
		return result;

		
	}

	
	@RequestMapping(value = "/matricularPagar", method = RequestMethod.POST)
	public AjaxResponseBody matricularypagar(Matricula matricula, Integer id_suc, Integer id_perfil, HttpSession session) throws Exception{
		AjaxResponseBody result = new AjaxResponseBody();
		Integer id_anio = periodoDAO.getByParams(new Param("id",matricula.getId_per())).getId_anio();
		//Impresion impresion = matriculaService.matricularYPagar(matricula,id_suc, id_anio, id_perfil);
		
		//result.setResult(impresion);
		result.setResult(matriculaService.matricularYPagar(matricula,id_suc, id_anio, id_perfil));
		
		return result;

	}
	
	@RequestMapping(value = "/matricularPagarV2", method = RequestMethod.POST)
	public AjaxResponseBody matricularypagarV2(Matricula matricula,Integer id_fam_mat,Integer id_anio, Integer id_suc, Integer id_perfil, Integer id_bco_pag, HttpSession session) throws Exception{
		AjaxResponseBody result = new AjaxResponseBody();
		//Integer id_anio = periodoDAO.getByParams(new Param("id",matricula.getId_per())).getId_anio();
		//Impresion impresion = matriculaService.matricularYPagar(matricula,id_suc, id_anio, id_perfil);
		
		//result.setResult(impresion);
		matricula.setId_fam(id_fam_mat);
		//Obtener la persona
		Familiar familiar = familiarDAO.get(id_fam_mat);
		matricula.setId_per_res(familiar.getId_per());
		result.setResult(matriculaService.matricularYPagarV2(matricula, id_suc, id_anio, id_perfil, id_bco_pag));
		
		return result;

	}
	
	@Transactional
	@RequestMapping(value = "/matricularColegioWeb", method = RequestMethod.POST)
	public AjaxResponseBody matricularColegioWeb(Matricula matricula,Integer id_anio, Integer id_bco, Integer id_alu_sel[],Integer id_suc[],Integer id_cme[], Integer id_cct[],Integer id_per_res, Integer id_fam_id_par, Integer id_fam_res_pag, Integer id_fam_res_aca, Integer id_perfil, HttpSession session)  throws Exception{
		AjaxResponseBody result = new AjaxResponseBody();
		//Validar las Deudas
		Anio anio = anioDAO.get(id_anio);
		String contrato=null;
		//Fecha de vencimiento
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_YEAR, 2);
		//Date fechaActual=new Date();
		String cadenaFecha=FechaUtil.toString(calendar.getTime());

		Date fechaConcreta = new SimpleDateFormat("dd/MM/yyyy").parse(cadenaFecha);
		String dia = new SimpleDateFormat("dd").format(fechaConcreta);
		String mes = new SimpleDateFormat("MMMM").format(fechaConcreta);
		String anio_nom = new SimpleDateFormat("yyyy").format(fechaConcreta);
		//Verificar el cronograma 
		//ALUMNOS ANTIGUOS - CON CRONOGRAMA
		boolean antiguo_con_cronograma = cronogramaDAO.alumnosAntiguosTienenVigente(id_anio);

		//ALUMNOS ANTIGUOS - SIN CRONOGRAMA
		boolean antiguo_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "AS");

				
		//ALUMNOS NUEVOS SIN CRONOGRAMA
		boolean nuevos_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "NS");
		
		//ALUMNOS NUEVOS CON CRONOGRAMA 
		boolean nuevos_cronograma = confFechasDAO.cronogramaVigente(id_anio, "NC");
		
		ReglasNegocio regla_mat_sug= reglasNegocioDAO.getByParams(new Param("nom","MATR_AULA_SUG"));

		//Verificar si es matricula de nuevo o antiguo
		for (int i = 0; i < id_alu_sel.length; i++) {
			//Valido deudas
			Integer anio_ant= Integer.parseInt(anio.getNom())-1;
			Anio anio_anterior=anioDAO.getByParams(new Param("nom",anio_ant));
			BigDecimal deuda = pagosService.pagosPendientes(id_alu_sel[i], anio_anterior.getId());
			
			//Obtener familia
			GruFamAlumno gruFamAlumno = gruFamAlumnoDAO.getByParams(new Param("id_alu", id_alu_sel[i]));
			Integer id_grupo=gruFamAlumno.getId_gpf();
			//Por ahora comentado 2022
			/*if (deuda.compareTo(BigDecimal.ZERO)>0) {
				result.setCode("500");
				result.setMsg("¡ATENCIÓN!,\n Primero debe cancelar las mensualidades vencidas.\nDeuda de la familia S/ " + deuda);
				return result;
			}*/
			//Valido la capacidad
			
			//Busco su matricula anterior
			List<Row> matricula_ant = matriculaDAO.obtenerMatriculaColegioxAlumnoAnio(id_alu_sel[i], id_anio-1);
			if (antiguo_con_cronograma){
				Periodo periodo_ant=periodoDAO.get(matricula_ant.get(0).getInteger("id_per"));
				//Busco el grado y periodo al q se va a matricular
				SeccionSugerida seccionSugerida = seccionSugeridaDAO.getByParams(new Param("id_mat",matricula_ant.get(0).getInteger("id_mat")));

				if(seccionSugerida!=null) {
					Integer id_suc_nue= seccionSugerida.getId_suc_nue();
					Integer id_anio_nue=seccionSugerida.getId_anio();
					//Buscamos el periodo
					Param param = new Param();
					param.put("id_suc", id_suc_nue);
					Integer id_niv=null;
					if(matricula_ant.get(0).getInteger("id_gra").equals(3))
						id_niv= 2;
					else if(matricula_ant.get(0).getInteger("id_gra").equals(9))
						id_niv=3;
					else 
						id_niv= matricula_ant.get(0).getInteger("id_niv");
					
					param.put("id_niv", id_niv);
					param.put("id_anio", id_anio);
					param.put("id_tpe",1);
					Periodo periodo_nuevo = periodoDAO.getByParams(param);
					Ciclo ciclo_nuevo=cicloDAO.getByParams(new Param("id_per",periodo_nuevo.getId()));
					matricula.setId_per(periodo_nuevo.getId());
					matricula.setId_cic(ciclo_nuevo.getId());
					matricula.setId_gra(matricula_ant.get(0).getInteger("id_gra")+1);
					matricula.setId_niv(id_niv);
					//Buscamos el aula q tiene vacante
					Param param3= new Param();
					param3.put("aula.id_cic",ciclo_nuevo.getId());
					param3.put("aula.id_grad", matricula_ant.get(0).getInteger("id_gra")+1);
					param3.put("pee.id_suc", id_suc[i]);
					param3.put("aula.id_cme",id_cme[i]);
					param3.put("cta.id_cit", id_cct[i]);
					List<Aula> aulas = aulaDAO.listFullByParams(param3, new String[]{"aula.secc"});
					for (Aula aula2 : aulas) {
						//Busco la capacidad del aula
						Integer cap2=aula2.getCap();
						//Busco los matriculados en ese aula
						Param param4 = new Param();
						param4.put("id_au_asi", aula2.getId());
						param4.put("est", "A");
						List<Matricula> matriculas2=matriculaDAO.listByParams(param4, null);
						//Buscamos los trasladados
						Param param5 = new Param();
						param5.put("id_au_asi", aula2.getId());
						param5.put("id_sit", "5");
						List<Matricula> trasladados=matriculaDAO.listByParams(param5, null);
						Integer res2=vacanteService.getReservasxAula(aula2.getId(), id_anio);
						Integer nro_vac2=cap2-matriculas2.size()-res2;
						 nro_vac2=nro_vac2+trasladados.size();
						if(nro_vac2>0) {
							matricula.setId_au(aula2.getId());
							matricula.setId_au_asi(aula2.getId());
							break;
						}
					}
				} else {
					
							//Buscamos el periodo siguiente
							Param param = new Param();
							//param.put("id_suc", periodo_ant.getId_suc());
							param.put("id_suc", id_suc[i]);
							Integer id_niv=null;
							if(matricula_ant.get(0).getInteger("id_gra").equals(3))
								id_niv= 2;
							else if(matricula_ant.get(0).getInteger("id_gra").equals(9))
								id_niv=3;
							else 
								id_niv= matricula_ant.get(0).getInteger("id_niv");
							
							param.put("id_niv", id_niv);
							param.put("id_anio", id_anio);
							param.put("id_tpe",1);
							Periodo periodo_nuevo = periodoDAO.getByParams(param);
							Ciclo ciclo_nuevo=cicloDAO.getByParams(new Param("id_per",periodo_nuevo.getId()));
							matricula.setId_per(periodo_nuevo.getId());
							matricula.setId_cic(ciclo_nuevo.getId());
							matricula.setId_gra(matricula_ant.get(0).getInteger("id_gra")+1);
							matricula.setId_niv(id_niv);
							//Buscamos el aula q tiene vacante
							Param param3= new Param();
							param3.put("aula.id_cic",ciclo_nuevo.getId());
							param3.put("aula.id_grad", matricula_ant.get(0).getInteger("id_gra")+1);
							param3.put("pee.id_suc", id_suc[i]);
							param3.put("aula.id_cme",id_cme[i]);
							param3.put("cta.id_cit", id_cct[i]);
							List<Aula> aulas = aulaDAO.listFullByParams(param3, new String[]{"aula.secc"});
							for (Aula aula2 : aulas) {
								//Busco la capacidad del aula
								Integer cap2=aula2.getCap();
								//Busco los matriculados en ese aula
								Param param4 = new Param();
								param4.put("id_au_asi", aula2.getId());
								param4.put("est", "A");
								List<Matricula> matriculas2=matriculaDAO.listByParams(param4, null);
								//Buscamos los trasladados
								Param param5 = new Param();
								param5.put("id_au_asi", aula2.getId());
								param5.put("id_sit", "5");
								List<Matricula> trasladados=matriculaDAO.listByParams(param5, null);
								Integer res2=vacanteService.getReservasxAula(aula2.getId(), id_anio);
								Integer nro_vac2=cap2-matriculas2.size()-res2;
								 nro_vac2=nro_vac2+trasladados.size();
								if(nro_vac2>0) {
									matricula.setId_au(aula2.getId());
									matricula.setId_au_asi(aula2.getId());
									break;
								}
							}
				
				}
				matricula.setId_cli(4);
				matricula.setId_alu(id_alu_sel[i]);
				//Busco al familiar
				Param param_fam = new Param();
				param_fam.put("id_per", id_per_res);
			    param_fam.put("id_par", id_fam_id_par);
				Familiar familiar = familiarDAO.getByParams(param_fam);
				matricula.setId_fam(familiar.getId());
				matricula.setId_per_res(id_per_res);
				matricula.setId_fam_res_pag(id_fam_res_pag);
				matricula.setId_fam_res_aca(id_fam_res_aca);
				matricula.setFecha(new Date());
				matricula.setTipo("C");
				matricula.setEst("A");
				
				//Vien la logica del contrato


				//Map<String, Object> contratoActual = matriculaDAO.getContrato(familiar.getId(), id_anio);
				//Cambio el 2022 que debe ser por grupo familiar
				Map<String, Object> contratoActual = matriculaDAO.getContratoxGruFam(id_grupo, id_anio);

				logger.debug("contratoActual:" + contratoActual);

				if (contratoActual == null) {
					
					//No comprendo xq se consideraria periodo??
					Map<String, Object> map = matriculaDAO.getTotalContratos(id_anio);
					Double total = (Double)map.get("total");
					
					int correlativo = total.intValue() + 1;
					
					contrato = anio.getNom() + "-" + String.format("%05d", correlativo);

				} else {
					contrato = contratoActual.get("num_cont").toString();
				}
				matricula.setNum_cont(contrato);
				Integer id_mat= matriculaDAO.saveOrUpdate(matricula);
				
				//Inserto los pagos por derecho de matricula y pensiones
				//Matricula
				//Busco la configuracion de la cuota segun modalidad y turno
				Param param_cuota = new Param();
				param_cuota.put("id_per", matricula.getId_per());
				param_cuota.put("id_cct", id_cct[i]);
				param_cuota.put("id_cme", id_cme[i]);
				ConfCuota confCuota = confCuotaDAO.getByParams(param_cuota);
				if(confCuota==null) {
					result.setCode("500");
					result.setMsg("¡ATENCIÓN!,\n No existe una configuración de cronograma de paos para esta modalidad y turno, por favor comunicarse con el Administrador del sistema");
					return result;
				} 
				AcademicoPago academicoPago = new AcademicoPago();
				academicoPago.setCanc("0");// Por defecto pago pendiente
				academicoPago.setMonto(confCuota.getMatricula());
				academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_MATRICULA);
				academicoPago.setId_mat(id_mat);
				academicoPago.setId_bco_pag(id_bco);
				academicoPago.setMontoTotal(confCuota.getMatricula());
				academicoPago.setFec_venc(FechaUtil.toDate(cadenaFecha));
				academicoPago.setEst("A");
				academicoPagoDAO.saveOrUpdate(academicoPago);

				Integer[] meses = new Integer[] { 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };

				Param param = new Param();
				param.put("id_per", matricula.getId_per());
				param.put("id_cct", id_cct[i]);
				param.put("id_cme", id_cme[i]);
				ConfMensualidad confMensualidad = confMensualidadDAO.getByParams(param);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

				String format = formatter.format(new Date());
				int fecActual = Integer.parseInt(format);
				Calendar calendar1 = Calendar.getInstance();
			    calendar1.setTime(new Date());
			    Integer mes_act = calendar1.get(Calendar.MONTH)+1;
				
				for (int j = 0; j < meses.length; j++) {
					Param param1 = new Param();
					param1.put("id_mat", id_mat);
					param1.put("mens", meses[j]);
					AcademicoPago pago_mes = academicoPagoDAO.getByParams(param1);
					if(pago_mes==null){
						Calendar cal = Calendar.getInstance();
						//int anio_actual = cal.get(Calendar.YEAR);
						int anio_matricula= Integer.parseInt(anioDAO.getByParams(new Param("id",id_anio)).getNom());
						String tipo_fec_ven=confMensualidadDAO.getByParams(new Param("id_per", matricula.getId_per())).getTipo_fec_ven();
						int fecVencimiento =0 ;
						Integer dia_mora=0 ;
						String fec_ven=null;
						if(tipo_fec_ven.equals(Constante.TIPO_FEC_VEN_DIA)){
							 fecVencimiento = getFecVencimiento(anio_matricula, meses[j], matricula.getId_per());
							 dia_mora=confMensualidadDAO.getByParams(new Param("id_per", matricula.getId_per())).getDia_mora();
						} else if(tipo_fec_ven.equals(Constante.TIPO_FEC_VEN_FIN)){
							fec_ven=getFecVencimientoFinMes(Integer.parseInt(anio.getNom()), meses[j]);
							String fecha[]  = fec_ven.split("/");
							 fecVencimiento=Integer.parseInt(fecha[2]+fecha[1]+fecha[0]);
							 dia_mora=Integer.parseInt(fec_ven.substring(0, 2));
						}
						//int fecVencimiento = getFecVencimiento(anio_matricula, meses[i], matricula.getId_per());
						//Integer dia_mora=confMensualidadDAO.getByParams(new Param("id_per", matricula.getId_per())).getDia_mora();
						
						AcademicoPago academicoPago2 = new AcademicoPago();
						
						academicoPago2.setEst("A");
						academicoPago2.setId_mat(id_mat);
						academicoPago2.setId_bco_pag(id_bco);
						if (fecActual < fecVencimiento) {
							//int dias_cla=dia_mora-(new Date().getDate());
							BigDecimal monto_mens=new BigDecimal(0);
							//if(dias_cla>0)
							//monto_mens=(confMensualidad.getMonto().divide(new BigDecimal(30),2)).multiply(new BigDecimal(dias_cla));
							//else
							monto_mens=confMensualidad.getMonto();	
							academicoPago2.setMonto(monto_mens);
							academicoPago2.setMontoTotal(monto_mens);
							academicoPago2.setCanc("0");// Por defecto pago pendiente
						} else if(fecActual==fecVencimiento){
							academicoPago2.setCanc("1");// Por defecto pagado
							academicoPago2.setMonto(new BigDecimal(0));
							academicoPago2.setMontoTotal(new BigDecimal(0));
						} else if(fecActual > fecVencimiento){
							int dia_act=(new Date().getDate());
							int dias_cla=dia_mora-dia_act;
							academicoPago2.setMonto(new BigDecimal(0));
							academicoPago2.setCanc("1");//Le pongo cancelado
							academicoPago2.setMontoTotal(new BigDecimal(0));
							Integer mes_sig=meses[i]+1;
							if(mes_act.equals(mes_sig)) {
								AcademicoPago pago_mes_siguiente = new AcademicoPago();
								pago_mes_siguiente.setEst("A");
								pago_mes_siguiente.setId_mat(matricula.getId());
								BigDecimal monto_mens=(confMensualidad.getMonto().divide(new BigDecimal(30),2)).multiply(new BigDecimal(dias_cla));
								pago_mes_siguiente.setMonto(monto_mens);
								pago_mes_siguiente.setMontoTotal(monto_mens);
								pago_mes_siguiente.setCanc("0");// Por defecto pago pendiente
								pago_mes_siguiente.setMens(mes_sig);
								pago_mes_siguiente.setDesc_hermano(new BigDecimal(0));
								pago_mes_siguiente.setDesc_pronto_pago(new BigDecimal(0));
								pago_mes_siguiente.setDesc_pago_adelantado(new BigDecimal(0));
								pago_mes_siguiente.setDesc_personalizado(new BigDecimal(0));
								pago_mes_siguiente.setTip(com.tesla.colegio.util.Constante.PAGO_MENSUAL);
								pago_mes_siguiente.setId_bco_pag(id_bco);
								//Fecha ven
								String fec_ven_mes_act=getFecVencimientoFinMes(Integer.parseInt(anio.getNom()), mes_sig);
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
								pago_mes_siguiente.setFec_venc(sdf.parse(fec_ven_mes_act));
								
								academicoPagoDAO.saveOrUpdate(pago_mes_siguiente);
							}
						} else{
							academicoPago2.setMonto(confMensualidad.getMonto());
							academicoPago2.setMontoTotal(confMensualidad.getMonto());
							academicoPago2.setCanc("1");
						}
						//academicoPago.setMonto(confMensualidad.getMonto());
						academicoPago2.setMens(meses[j]);
						academicoPago2.setTip(com.tesla.colegio.util.Constante.PAGO_MENSUAL);
						if(meses[j].equals(12)) {
							String fec_venc_dic=confMensualidad.getFec_fin_dic();
							if(fec_venc_dic!=null) {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
								academicoPago2.setFec_venc(sdf.parse(fec_venc_dic));
							} else {
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
								academicoPago2.setFec_venc(sdf.parse(fec_ven));
							}	
						} else {
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
							academicoPago2.setFec_venc(sdf.parse(fec_ven));
						}
						academicoPagoDAO.saveOrUpdate(academicoPago2);
					}
			}
			} else if(antiguo_sin_cronograma && !nuevos_sin_cronograma) {
				if(matricula_ant.size()>0) {//Es antiguo
					//Buscamos el periodo
					Param param = new Param();
					param.put("id_suc", id_suc[i]);
					Integer id_niv=null;
					if(matricula_ant.get(0).getInteger("id_gra").equals(3))
						id_niv= 2;
					else if(matricula_ant.get(0).getInteger("id_gra").equals(9))
						id_niv=3;
					else 
						id_niv= matricula_ant.get(0).getInteger("id_niv");
					
					param.put("id_niv", id_niv);
					param.put("id_anio", id_anio);
					param.put("id_tpe",1);
					Periodo periodo_nuevo = periodoDAO.getByParams(param);
					Ciclo ciclo_nuevo = cicloDAO.getByParams(new Param("id_per",periodo_nuevo.getId()));
					matricula.setId_per(periodo_nuevo.getId());
					matricula.setId_cic(ciclo_nuevo.getId());
					matricula.setId_gra(matricula_ant.get(0).getInteger("id_gra")+1);
					matricula.setId_niv(id_niv);
					//Buscamos el aula q tiene vacante
					Param param3= new Param();
					param3.put("aula.id_cic",ciclo_nuevo.getId());
					param3.put("aula.id_grad", matricula_ant.get(0).getInteger("id_gra")+1);
					param3.put("pee.id_suc", id_suc[i]);
					param3.put("aula.id_cme",id_cme[i]);
					param3.put("cta.id_cit", id_cct[i]);
					List<Aula> aulas = aulaDAO.listFullByParams(param3, new String[]{"aula.secc"});
					for (Aula aula2 : aulas) {
						//Busco la capacidad del aula
						Integer cap2=aula2.getCap();
						//Busco los matriculados en ese aula
						Param param4 = new Param();
						param4.put("id_au_asi", aula2.getId());
						param4.put("est", "A");
						List<Matricula> matriculas2=matriculaDAO.listByParams(param4, null);
						//Buscamos los trasladados
						Param param5 = new Param();
						param5.put("id_au_asi", aula2.getId());
						param5.put("id_sit", "5");
						List<Matricula> trasladados=matriculaDAO.listByParams(param5, null);
						Integer res2=vacanteService.getReservasxAula(aula2.getId(), id_anio);
						Integer nro_vac2=cap2-matriculas2.size()-res2;
						 nro_vac2=nro_vac2+trasladados.size();
						if(nro_vac2>0) {
							matricula.setId_au(aula2.getId());
							matricula.setId_au_asi(aula2.getId());
							break;
						}
					}
					
					matricula.setId_cli(4);
					matricula.setId_alu(id_alu_sel[i]);
					//Busco al familiar
					//Busco al familiar
					Param param_fam = new Param();
					param_fam.put("id_per", id_per_res);
				    param_fam.put("id_par", id_fam_id_par);
					Familiar familiar = familiarDAO.getByParams(param_fam);
					matricula.setId_fam(familiar.getId());
					matricula.setId_per_res(id_per_res);
					matricula.setId_fam_res_pag(id_fam_res_pag);
					matricula.setId_fam_res_aca(id_fam_res_aca);
					matricula.setFecha(new Date());
					matricula.setTipo("C");
					matricula.setEst("A");
					//Vien la logica del contrato


					Map<String, Object> contratoActual = matriculaDAO.getContrato(familiar.getId(), id_anio);

					logger.debug("contratoActual:" + contratoActual);

					if (contratoActual == null) {
						
						//No comprendo xq se consideraria periodo??
						Map<String, Object> map = matriculaDAO.getTotalContratos(id_anio);
						Double total = (Double)map.get("total");
						
						int correlativo = total.intValue() + 1;
						
						contrato = anio.getNom() + "-" + String.format("%05d", correlativo);

					} else {
						contrato = contratoActual.get("num_cont").toString();
					}
					matricula.setNum_cont(contrato);
					Integer id_mat= matriculaDAO.saveOrUpdate(matricula);
					
					//Inserto los pagos por derecho de matricula y pensiones
					//Matricula
					//Busco la configuracion de la cuota segun modalidad y turno
					Param param_cuota = new Param();
					param_cuota.put("id_per", matricula.getId_per());
					param_cuota.put("id_cct", id_cct[i]);
					param_cuota.put("id_cme", id_cme[i]);
					ConfCuota confCuota = confCuotaDAO.getByParams(param_cuota);
					AcademicoPago academicoPago = new AcademicoPago();
					academicoPago.setCanc("0");// Por defecto pago pendiente
					academicoPago.setMonto(confCuota.getMatricula());
					academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_MATRICULA);
					academicoPago.setId_mat(id_mat);
					academicoPago.setId_bco_pag(id_bco);
					academicoPago.setMontoTotal(confCuota.getMatricula());
					academicoPago.setFec_venc(FechaUtil.toDate(cadenaFecha));
					academicoPago.setEst("A");
					academicoPagoDAO.saveOrUpdate(academicoPago);

					Integer[] meses = new Integer[] { 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };

					Param param2 = new Param();
					param2.put("id_per", matricula.getId_per());
					param2.put("id_cct", id_cct[i]);
					param2.put("id_cme", id_cme[i]);
					ConfMensualidad confMensualidad = confMensualidadDAO.getByParams(param2);
					SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

					String format = formatter.format(new Date());
					int fecActual = Integer.parseInt(format);
					Calendar calendar1 = Calendar.getInstance();
				    calendar1.setTime(new Date());
				    Integer mes_act = calendar1.get(Calendar.MONTH)+1;
					for (int j = 0; j < meses.length; j++) {
						Param param1 = new Param();
						param1.put("id_mat", id_mat);
						param1.put("mens", meses[j]);
						AcademicoPago pago_mes = academicoPagoDAO.getByParams(param1);
						if(pago_mes==null){
							Calendar cal = Calendar.getInstance();
							//int anio_actual = cal.get(Calendar.YEAR);
							int anio_matricula= Integer.parseInt(anioDAO.getByParams(new Param("id",id_anio)).getNom());
							String tipo_fec_ven=confMensualidadDAO.getByParams(new Param("id_per", matricula.getId_per())).getTipo_fec_ven();
							int fecVencimiento =0 ;
							Integer dia_mora=0 ;
							String fec_ven=null;
							if(tipo_fec_ven.equals(Constante.TIPO_FEC_VEN_DIA)){
								 fecVencimiento = getFecVencimiento(anio_matricula, meses[j], matricula.getId_per());
								 dia_mora=confMensualidadDAO.getByParams(new Param("id_per", matricula.getId_per())).getDia_mora();
							} else if(tipo_fec_ven.equals(Constante.TIPO_FEC_VEN_FIN)){
								fec_ven=getFecVencimientoFinMes(Integer.parseInt(anio.getNom()), meses[j]);
								String fecha[]  = fec_ven.split("/");
								 fecVencimiento=Integer.parseInt(fecha[2]+fecha[1]+fecha[0]);
								 dia_mora=Integer.parseInt(fec_ven.substring(0, 2));
							}
							//int fecVencimiento = getFecVencimiento(anio_matricula, meses[i], matricula.getId_per());
							//Integer dia_mora=confMensualidadDAO.getByParams(new Param("id_per", matricula.getId_per())).getDia_mora();
							
							AcademicoPago academicoPago2 = new AcademicoPago();
							
							academicoPago2.setEst("A");
							academicoPago2.setId_mat(id_mat);
							academicoPago2.setId_bco_pag(id_bco);
							if (fecActual < fecVencimiento) {
								//int dias_cla=dia_mora-(new Date().getDate());
								BigDecimal monto_mens=new BigDecimal(0);
								//if(dias_cla>0)
								//monto_mens=(confMensualidad.getMonto().divide(new BigDecimal(30),2)).multiply(new BigDecimal(dias_cla));
								//else
								monto_mens=confMensualidad.getMonto();	
								academicoPago2.setMonto(monto_mens);
								academicoPago2.setMontoTotal(monto_mens);
								academicoPago2.setCanc("0");// Por defecto pago pendiente
							} else if(fecActual==fecVencimiento){
								academicoPago2.setCanc("1");// Por defecto pagado
								academicoPago2.setMonto(new BigDecimal(0));
								academicoPago2.setMontoTotal(new BigDecimal(0));
							} else if(fecActual > fecVencimiento){
								int dia_act=(new Date().getDate());
								int dias_cla=dia_mora-dia_act;
								academicoPago2.setMonto(new BigDecimal(0));
								academicoPago2.setCanc("1");//Le pongo cancelado
								academicoPago2.setMontoTotal(new BigDecimal(0));
								Integer mes_sig=meses[j]+1;
								if(mes_act.equals(mes_sig)) {
									AcademicoPago pago_mes_siguiente = new AcademicoPago();
									pago_mes_siguiente.setEst("A");
									pago_mes_siguiente.setId_mat(matricula.getId());
									BigDecimal monto_mens=(confMensualidad.getMonto().divide(new BigDecimal(30),2)).multiply(new BigDecimal(dias_cla));
									pago_mes_siguiente.setMonto(monto_mens);
									pago_mes_siguiente.setMontoTotal(monto_mens);
									pago_mes_siguiente.setCanc("0");// Por defecto pago pendiente
									pago_mes_siguiente.setMens(mes_sig);
									pago_mes_siguiente.setDesc_hermano(new BigDecimal(0));
									pago_mes_siguiente.setDesc_pronto_pago(new BigDecimal(0));
									pago_mes_siguiente.setDesc_pago_adelantado(new BigDecimal(0));
									pago_mes_siguiente.setDesc_personalizado(new BigDecimal(0));
									pago_mes_siguiente.setTip(com.tesla.colegio.util.Constante.PAGO_MENSUAL);
									pago_mes_siguiente.setId_bco_pag(id_bco);
									if(meses[j].equals(12)) {
										String fec_venc_dic=confMensualidad.getFec_fin_dic();
										if(fec_venc_dic!=null) {
											SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
											academicoPago2.setFec_venc(sdf.parse(fec_venc_dic));
										} else {
											//Fecha ven
											String fec_ven_mes_act=getFecVencimientoFinMes(Integer.parseInt(anio.getNom()), mes_sig);
											SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
											pago_mes_siguiente.setFec_venc(sdf.parse(fec_ven_mes_act));
										}
										
									} else {
										//Fecha ven
										String fec_ven_mes_act=getFecVencimientoFinMes(Integer.parseInt(anio.getNom()), mes_sig);
										SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
										pago_mes_siguiente.setFec_venc(sdf.parse(fec_ven_mes_act));
									}
									
									
									academicoPagoDAO.saveOrUpdate(pago_mes_siguiente);
								}
								
							} else{
								academicoPago2.setMonto(confMensualidad.getMonto());
								academicoPago2.setMontoTotal(confMensualidad.getMonto());
								academicoPago2.setCanc("1");
							}
							//academicoPago.setMonto(confMensualidad.getMonto());
							academicoPago2.setMens(meses[j]);
							academicoPago2.setTip(com.tesla.colegio.util.Constante.PAGO_MENSUAL);
							if(meses[j].equals(12)) {
								String fec_venc_dic=confMensualidad.getFec_fin_dic();
								if(fec_venc_dic!=null) {
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
									academicoPago2.setFec_venc(sdf.parse(fec_venc_dic));
								} else {
									SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
									academicoPago2.setFec_venc(sdf.parse(fec_ven));
								}								
							} else {
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
								academicoPago2.setFec_venc(sdf.parse(fec_ven));
							}
							academicoPagoDAO.saveOrUpdate(academicoPago2);
						}
				}
				
				} else {
					//Es nuevo y busco su reserva
					Row reserva = reservaDAO.obtenerReservaAlumno(id_anio, id_alu_sel[i]);
					if(reserva==null) {
						result.setCode("500");
						result.setMsg("¡ATENCIÓN!,\n El alumno no tiene la reserva para el presente periodo académico, no puede proceder con la matrícula");
						return result;
					} else {
						Ciclo ciclo_acad=cicloDAO.getByParams(new Param("id_per",reserva.getInteger("id_per")));
						matricula.setId_per(reserva.getInteger("id_per"));
						matricula.setId_cic(ciclo_acad.getId());
						matricula.setId_gra(reserva.getInteger("id_gra"));
						matricula.setId_niv(reserva.getInteger("id_niv"));
						matricula.setId_au(reserva.getInteger("id_au"));
						matricula.setId_au_asi(reserva.getInteger("id_au"));
						matricula.setId_cli(1);
						matricula.setId_alu(id_alu_sel[i]);
						//Busco al familiar
						//Busco al familiar
						Param param_fam = new Param();
						param_fam.put("id_per", id_per_res);
					    param_fam.put("id_par", id_fam_id_par);
						Familiar familiar = familiarDAO.getByParams(param_fam);
						matricula.setId_fam(familiar.getId());
						matricula.setId_per_res(id_per_res);
						matricula.setFecha(new Date());
						matricula.setTipo("C");
						matricula.setEst("A");
						//Vien la logica del contrato


						Map<String, Object> contratoActual = matriculaDAO.getContrato(familiar.getId(), id_anio);

						logger.debug("contratoActual:" + contratoActual);

						if (contratoActual == null) {
							
							//No comprendo xq se consideraria periodo??
							Map<String, Object> map = matriculaDAO.getTotalContratos(id_anio);
							Double total = (Double)map.get("total");
							
							int correlativo = total.intValue() + 1;
							
							contrato = anio.getNom() + "-" + String.format("%05d", correlativo);

						} else {
							contrato = contratoActual.get("num_cont").toString();
						}
						matricula.setNum_cont(contrato);
						Integer id_mat= matriculaDAO.saveOrUpdate(matricula);
						
						//Inserto los pagos por derecho de matricula y pensiones
						//Matricula
						Param param_cuota = new Param();
						param_cuota.put("id_per", matricula.getId_per());
						param_cuota.put("id_cct", id_cct[i]);
						param_cuota.put("id_cme", id_cme[i]);
						ConfCuota confCuota = confCuotaDAO.getByParams(param_cuota);
						//ConfCuota confCuota = confCuotaDAO.getByParams(new Param("id_per", reserva.getInteger("id_per")));
						Periodo periodo_nuevo=periodoDAO.get(reserva.getInteger("id_per"));
						ReservaCuota reservaCuota = reservaDAO.getMontoReserva(periodo_nuevo.getId_anio(), id_alu_sel[i]);
						BigDecimal montoReserva = reservaCuota.getMonto();
						BigDecimal monto_matricula=confCuota.getMatricula().subtract(montoReserva);
						AcademicoPago academicoPago = new AcademicoPago();
						academicoPago.setCanc("0");// Por defecto pago pendiente
						academicoPago.setMonto(monto_matricula);
						academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_MATRICULA);
						academicoPago.setId_mat(id_mat);
						academicoPago.setId_bco_pag(id_bco);
						academicoPago.setMontoTotal(monto_matricula);
						academicoPago.setFec_venc(FechaUtil.toDate(cadenaFecha));
						academicoPago.setEst("A");
						academicoPagoDAO.saveOrUpdate(academicoPago);
						//Insertamos la cuota de Ingreso
						AcademicoPago academicoPago_ci = new AcademicoPago();
						academicoPago_ci.setCanc("0");// Por defecto pago pendiente
						academicoPago_ci.setMonto(confCuota.getCuota());
						academicoPago_ci.setTip(com.tesla.colegio.util.Constante.PAGO_CUOTA_INGRESO);
						academicoPago_ci.setId_mat(id_mat);
						academicoPago_ci.setId_bco_pag(id_bco);
						academicoPago_ci.setMontoTotal(confCuota.getCuota());
						academicoPago_ci.setFec_venc(FechaUtil.toDate(cadenaFecha));
						academicoPago_ci.setEst("A");
						academicoPagoDAO.saveOrUpdate(academicoPago_ci);

						Integer[] meses = new Integer[] { 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };

						Param param = new Param();
						param.put("id_per", matricula.getId_per());
						param.put("id_cct", id_cct[i]);
						param.put("id_cme", id_cme[i]);
						ConfMensualidad confMensualidad = confMensualidadDAO.getByParams(param);
						SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

						String format = formatter.format(new Date());
						Calendar calendar1 = Calendar.getInstance();
					    calendar1.setTime(new Date());
					    Integer mes_act = calendar1.get(Calendar.MONTH)+1;
						int fecActual = Integer.parseInt(format);
						
						for (int j = 0; j < meses.length; j++) {
							Param param1 = new Param();
							param1.put("id_mat", id_mat);
							param1.put("mens", meses[j]);
							AcademicoPago pago_mes = academicoPagoDAO.getByParams(param1);
							if(pago_mes==null){
								Calendar cal = Calendar.getInstance();
								//int anio_actual = cal.get(Calendar.YEAR);
								int anio_matricula= Integer.parseInt(anioDAO.getByParams(new Param("id",id_anio)).getNom());
								String tipo_fec_ven=confMensualidadDAO.getByParams(new Param("id_per", matricula.getId_per())).getTipo_fec_ven();
								int fecVencimiento =0 ;
								Integer dia_mora=0 ;
								String fec_ven=null;
								//Integer mes_matricula=f
								if(tipo_fec_ven.equals(Constante.TIPO_FEC_VEN_DIA)){
									 fecVencimiento = getFecVencimiento(anio_matricula, meses[j], matricula.getId_per());
									 dia_mora=confMensualidadDAO.getByParams(new Param("id_per", matricula.getId_per())).getDia_mora();
								} else if(tipo_fec_ven.equals(Constante.TIPO_FEC_VEN_FIN)){
									fec_ven=getFecVencimientoFinMes(Integer.parseInt(anio.getNom()), meses[j]);
									String fecha[]  = fec_ven.split("/");
									 fecVencimiento=Integer.parseInt(fecha[2]+fecha[1]+fecha[0]);
									 dia_mora=Integer.parseInt(fec_ven.substring(0, 2));
								}
								//int fecVencimiento = getFecVencimiento(anio_matricula, meses[i], matricula.getId_per());
								//Integer dia_mora=confMensualidadDAO.getByParams(new Param("id_per", matricula.getId_per())).getDia_mora();
								
								AcademicoPago academicoPago2 = new AcademicoPago();
								
								academicoPago2.setEst("A");
								academicoPago2.setId_mat(id_mat);
								academicoPago2.setId_bco_pag(id_bco);
								if (fecActual < fecVencimiento) {
									//int dias_cla=dia_mora-(new Date().getDate());
									BigDecimal monto_mens=new BigDecimal(0);
									//if(dias_cla>0)
									//monto_mens=(confMensualidad.getMonto().divide(new BigDecimal(30),2)).multiply(new BigDecimal(dias_cla));
									//else
									monto_mens=confMensualidad.getMonto();	
									academicoPago2.setMonto(monto_mens);
									academicoPago2.setMontoTotal(monto_mens);
									academicoPago2.setCanc("0");// Por defecto pago pendiente
								} else if(fecActual==fecVencimiento){
									academicoPago2.setCanc("1");// Por defecto pagado
									academicoPago2.setMonto(new BigDecimal(0));
									academicoPago2.setMontoTotal(new BigDecimal(0));
								} else if(fecActual > fecVencimiento){
									int dia_act=(new Date().getDate());
									int dias_cla=dia_mora-dia_act;
									academicoPago2.setMonto(new BigDecimal(0));
									academicoPago2.setCanc("1");//Le pongo cancelado
									academicoPago2.setMontoTotal(new BigDecimal(0));
									//Por ahora comentado
									Integer mes_sig=meses[j]+1;
									if(mes_act.equals(mes_sig)) {
										AcademicoPago pago_mes_siguiente = new AcademicoPago();
										pago_mes_siguiente.setEst("A");
										pago_mes_siguiente.setId_mat(matricula.getId());
										BigDecimal monto_mens=(confMensualidad.getMonto().divide(new BigDecimal(30),2)).multiply(new BigDecimal(dias_cla));
										pago_mes_siguiente.setMonto(monto_mens);
										pago_mes_siguiente.setMontoTotal(monto_mens);
										pago_mes_siguiente.setCanc("0");// Por defecto pago pendiente
										pago_mes_siguiente.setMens(mes_sig);
										pago_mes_siguiente.setDesc_hermano(new BigDecimal(0));
										pago_mes_siguiente.setDesc_pronto_pago(new BigDecimal(0));
										pago_mes_siguiente.setDesc_pago_adelantado(new BigDecimal(0));
										pago_mes_siguiente.setDesc_personalizado(new BigDecimal(0));
										pago_mes_siguiente.setTip(com.tesla.colegio.util.Constante.PAGO_MENSUAL);
										pago_mes_siguiente.setId_mat(id_mat);
										pago_mes_siguiente.setId_bco_pag(id_bco);
										if(meses[j].equals(12)) {
											String fec_venc_dic=confMensualidad.getFec_fin_dic();
											if(fec_venc_dic!=null) {
												SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
												academicoPago2.setFec_venc(sdf.parse(fec_venc_dic));
											} else {
												//Fecha ven
												String fec_ven_mes_act=getFecVencimientoFinMes(Integer.parseInt(anio.getNom()), mes_sig);
												SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
												pago_mes_siguiente.setFec_venc(sdf.parse(fec_ven_mes_act));
											}
											
										} else {
											//Fecha ven
											String fec_ven_mes_act=getFecVencimientoFinMes(Integer.parseInt(anio.getNom()), mes_sig);
											SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
											pago_mes_siguiente.setFec_venc(sdf.parse(fec_ven_mes_act));
										}
										academicoPagoDAO.saveOrUpdate(pago_mes_siguiente);
									}
								} else{
									academicoPago2.setMonto(confMensualidad.getMonto());
									academicoPago2.setMontoTotal(confMensualidad.getMonto());
									academicoPago2.setCanc("1");
								}
								//academicoPago.setMonto(confMensualidad.getMonto());
								academicoPago2.setMens(meses[j]);
								academicoPago2.setTip(com.tesla.colegio.util.Constante.PAGO_MENSUAL);
								if(meses[j].equals(12)) {
									String fec_venc_dic=confMensualidad.getFec_fin_dic();
									if(fec_venc_dic!=null) {
										SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
										academicoPago2.setFec_venc(sdf.parse(fec_venc_dic));
									} else {
										SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
										academicoPago2.setFec_venc(sdf.parse(fec_ven));
									}								
								} else {
									SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
									academicoPago2.setFec_venc(sdf.parse(fec_ven));
								}
								academicoPagoDAO.saveOrUpdate(academicoPago2);
							}
					}
					}
				}
			} else if(antiguo_sin_cronograma && nuevos_sin_cronograma) {
				if(matricula_ant.size()>0) {//Es antiguo
					//Buscamos el periodo
					Param param = new Param();
					param.put("id_suc", id_suc[i]);
					Integer id_niv=null;
					if(matricula_ant.get(0).getInteger("id_gra").equals(3))
						id_niv= 2;
					else if(matricula_ant.get(0).getInteger("id_gra").equals(9))
						id_niv=3;
					else 
						id_niv= matricula_ant.get(0).getInteger("id_niv");
					
					param.put("id_niv", id_niv);
					param.put("id_anio", id_anio);
					param.put("id_tpe",1);
					Periodo periodo_nuevo = periodoDAO.getByParams(param);
					Ciclo ciclo_nuevo = cicloDAO.getByParams(new Param("id_per",periodo_nuevo.getId()));
					matricula.setId_per(periodo_nuevo.getId());
					matricula.setId_cic(ciclo_nuevo.getId());
					matricula.setId_gra(matricula_ant.get(0).getInteger("id_gra")+1);
					matricula.setId_niv(id_niv);
					//Buscamos el aula q tiene vacante
					Param param3= new Param();
					param3.put("aula.id_cic",ciclo_nuevo.getId());
					param3.put("aula.id_grad", matricula_ant.get(0).getInteger("id_gra")+1);
					param3.put("pee.id_suc", id_suc[i]);
					param3.put("aula.id_cme",id_cme[i]);
					param3.put("cta.id_cit", id_cct[i]);
					List<Aula> aulas = aulaDAO.listFullByParams(param3, new String[]{"aula.secc"});
					for (Aula aula2 : aulas) {
						//Busco la capacidad del aula
						Integer cap2=aula2.getCap();
						//Busco los matriculados en ese aula
						Param param4 = new Param();
						param4.put("id_au_asi", aula2.getId());
						param4.put("est", "A");
						List<Matricula> matriculas2=matriculaDAO.listByParams(param4, null);
						//Buscamos los trasladados
						Param param5 = new Param();
						param5.put("id_au_asi", aula2.getId());
						param5.put("id_sit", "5");
						List<Matricula> trasladados=matriculaDAO.listByParams(param5, null);
						Integer res2=vacanteService.getReservasxAula(aula2.getId(), id_anio);
						Integer nro_vac2=cap2-matriculas2.size()-res2;
						 nro_vac2=nro_vac2+trasladados.size();
						if(nro_vac2>0) {
							matricula.setId_au(aula2.getId());
							matricula.setId_au_asi(aula2.getId());
							break;
						}
					}
					
					matricula.setId_cli(4);
					matricula.setId_alu(id_alu_sel[i]);
					//Busco al familiar
					//Busco al familiar
					Param param_fam = new Param();
					param_fam.put("id_per", id_per_res);
				    param_fam.put("id_par", id_fam_id_par);
					Familiar familiar = familiarDAO.getByParams(param_fam);
					matricula.setId_fam(familiar.getId());
					matricula.setId_per_res(id_per_res);
					matricula.setId_fam_res_pag(id_fam_res_pag);
					matricula.setId_fam_res_aca(id_fam_res_aca);
					matricula.setFecha(new Date());
					matricula.setTipo("C");
					matricula.setEst("A");
					//Vien la logica del contrato


					Map<String, Object> contratoActual = matriculaDAO.getContrato(familiar.getId(), id_anio);

					logger.debug("contratoActual:" + contratoActual);

					if (contratoActual == null) {
						
						//No comprendo xq se consideraria periodo??
						Map<String, Object> map = matriculaDAO.getTotalContratos(id_anio);
						Double total = (Double)map.get("total");
						
						int correlativo = total.intValue() + 1;
						
						contrato = anio.getNom() + "-" + String.format("%05d", correlativo);

					} else {
						contrato = contratoActual.get("num_cont").toString();
					}
					matricula.setNum_cont(contrato);
					Integer id_mat= matriculaDAO.saveOrUpdate(matricula);
					
					//Inserto los pagos por derecho de matricula y pensiones
					//Matricula
					//Busco la configuracion de la cuota segun modalidad y turno
					Param param_cuota = new Param();
					param_cuota.put("id_per", matricula.getId_per());
					param_cuota.put("id_cct", id_cct[i]);
					param_cuota.put("id_cme", id_cme[i]);
					ConfCuota confCuota = confCuotaDAO.getByParams(param_cuota);
					//Busco si tiene reserva
					Row reserva = reservaDAO.obtenerReservaAlumno(id_anio, id_alu_sel[i]);
					BigDecimal monto_matricula=confCuota.getMatricula();
					if(reserva!=null) {
						ReservaCuota reservaCuota = reservaDAO.getMontoReserva(periodo_nuevo.getId_anio(), id_alu_sel[i]);
						BigDecimal montoReserva = reservaCuota.getMonto();
						 monto_matricula=monto_matricula.subtract(montoReserva);
					}
					
					
					AcademicoPago academicoPago = new AcademicoPago();
					academicoPago.setCanc("0");// Por defecto pago pendiente
					academicoPago.setMonto(monto_matricula);
					academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_MATRICULA);
					academicoPago.setId_mat(id_mat);
					academicoPago.setId_bco_pag(id_bco);
					academicoPago.setMontoTotal(monto_matricula);
					academicoPago.setFec_venc(FechaUtil.toDate(cadenaFecha));
					academicoPago.setEst("A");
					academicoPagoDAO.saveOrUpdate(academicoPago);

					Integer[] meses = new Integer[] { 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };

					Param param2 = new Param();
					param2.put("id_per", matricula.getId_per());
					param2.put("id_cct", id_cct[i]);
					param2.put("id_cme", id_cme[i]);
					ConfMensualidad confMensualidad = confMensualidadDAO.getByParams(param2);
					SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

					String format = formatter.format(new Date());
					int fecActual = Integer.parseInt(format);
					Calendar calendar1 = Calendar.getInstance();
				    calendar1.setTime(new Date());
				    Integer mes_act = calendar1.get(Calendar.MONTH)+1;
					for (int j = 0; j < meses.length; j++) {
						Param param1 = new Param();
						param1.put("id_mat", id_mat);
						param1.put("mens", meses[j]);
						AcademicoPago pago_mes = academicoPagoDAO.getByParams(param1);
						if(pago_mes==null){
							Calendar cal = Calendar.getInstance();
							//int anio_actual = cal.get(Calendar.YEAR);
							int anio_matricula= Integer.parseInt(anioDAO.getByParams(new Param("id",id_anio)).getNom());
							String tipo_fec_ven=confMensualidadDAO.getByParams(new Param("id_per", matricula.getId_per())).getTipo_fec_ven();
							int fecVencimiento =0 ;
							Integer dia_mora=0 ;
							String fec_ven=null;
							if(tipo_fec_ven.equals(Constante.TIPO_FEC_VEN_DIA)){
								 fecVencimiento = getFecVencimiento(anio_matricula, meses[j], matricula.getId_per());
								 dia_mora=confMensualidadDAO.getByParams(new Param("id_per", matricula.getId_per())).getDia_mora();
							} else if(tipo_fec_ven.equals(Constante.TIPO_FEC_VEN_FIN)){
								fec_ven=getFecVencimientoFinMes(Integer.parseInt(anio.getNom()), meses[j]);
								String fecha[]  = fec_ven.split("/");
								 fecVencimiento=Integer.parseInt(fecha[2]+fecha[1]+fecha[0]);
								 dia_mora=Integer.parseInt(fec_ven.substring(0, 2));
							}
							//int fecVencimiento = getFecVencimiento(anio_matricula, meses[i], matricula.getId_per());
							//Integer dia_mora=confMensualidadDAO.getByParams(new Param("id_per", matricula.getId_per())).getDia_mora();
							
							AcademicoPago academicoPago2 = new AcademicoPago();
							
							academicoPago2.setEst("A");
							academicoPago2.setId_mat(id_mat);
							academicoPago2.setId_bco_pag(id_bco);
							if (fecActual < fecVencimiento) {
								//int dias_cla=dia_mora-(new Date().getDate());
								BigDecimal monto_mens=new BigDecimal(0);
								//if(dias_cla>0)
								//monto_mens=(confMensualidad.getMonto().divide(new BigDecimal(30),2)).multiply(new BigDecimal(dias_cla));
								//else
								monto_mens=confMensualidad.getMonto();	
								academicoPago2.setMonto(monto_mens);
								academicoPago2.setMontoTotal(monto_mens);
								academicoPago2.setCanc("0");// Por defecto pago pendiente
							} else if(fecActual==fecVencimiento){
								academicoPago2.setCanc("1");// Por defecto pagado
								academicoPago2.setMonto(new BigDecimal(0));
								academicoPago2.setMontoTotal(new BigDecimal(0));
							} else if(fecActual > fecVencimiento){
								int dia_act=(new Date().getDate());
								int dias_cla=dia_mora-dia_act;
								academicoPago2.setMonto(new BigDecimal(0));
								academicoPago2.setCanc("1");//Le pongo cancelado
								academicoPago2.setMontoTotal(new BigDecimal(0));
								Integer mes_sig=meses[j]+1;
								if(mes_act.equals(mes_sig)) {
									AcademicoPago pago_mes_siguiente = new AcademicoPago();
									pago_mes_siguiente.setEst("A");
									pago_mes_siguiente.setId_mat(id_mat);
									BigDecimal monto_mens=(confMensualidad.getMonto().divide(new BigDecimal(30),2)).multiply(new BigDecimal(dias_cla));
									pago_mes_siguiente.setMonto(monto_mens);
									pago_mes_siguiente.setMontoTotal(monto_mens);
									pago_mes_siguiente.setCanc("0");// Por defecto pago pendiente
									pago_mes_siguiente.setMens(mes_sig);
									pago_mes_siguiente.setDesc_hermano(new BigDecimal(0));
									pago_mes_siguiente.setDesc_pronto_pago(new BigDecimal(0));
									pago_mes_siguiente.setDesc_pago_adelantado(new BigDecimal(0));
									pago_mes_siguiente.setDesc_personalizado(new BigDecimal(0));
									pago_mes_siguiente.setTip(com.tesla.colegio.util.Constante.PAGO_MENSUAL);
									pago_mes_siguiente.setId_bco_pag(id_bco);
									if(meses[j].equals(12)) {
										String fec_venc_dic=confMensualidad.getFec_fin_dic();
										if(fec_venc_dic!=null) {
											SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); 
											academicoPago2.setFec_venc(sdf.parse(fec_venc_dic));
										} else {
											//Fecha ven
											String fec_ven_mes_act=getFecVencimientoFinMes(Integer.parseInt(anio.getNom()), mes_sig);
											SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
											pago_mes_siguiente.setFec_venc(sdf.parse(fec_ven_mes_act));
										}
										
									} else {
										//Fecha ven
										String fec_ven_mes_act=getFecVencimientoFinMes(Integer.parseInt(anio.getNom()), mes_sig);
										SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
										pago_mes_siguiente.setFec_venc(sdf.parse(fec_ven_mes_act));
									}									
									academicoPagoDAO.saveOrUpdate(pago_mes_siguiente);
								}
								
							} else{
								academicoPago2.setMonto(confMensualidad.getMonto());
								academicoPago2.setMontoTotal(confMensualidad.getMonto());
								academicoPago2.setCanc("1");
							}
							//academicoPago.setMonto(confMensualidad.getMonto());
							academicoPago2.setMens(meses[j]);
							academicoPago2.setTip(com.tesla.colegio.util.Constante.PAGO_MENSUAL);
							if(meses[j].equals(12)) {
								String fec_venc_dic=confMensualidad.getFec_fin_dic();
								if(fec_venc_dic!=null) {
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
									academicoPago2.setFec_venc(sdf.parse(fec_venc_dic));
								} else {
									SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
									academicoPago2.setFec_venc(sdf.parse(fec_ven));
								}								
							} else {
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
								academicoPago2.setFec_venc(sdf.parse(fec_ven));
							}
							academicoPagoDAO.saveOrUpdate(academicoPago2);
						}
				}
				
				} else {
					//Es nuevo y busco su reserva
					Row reserva = reservaDAO.obtenerReservaAlumno(id_anio, id_alu_sel[i]);
					if(reserva==null) {
						result.setCode("500");
						result.setMsg("¡ATENCIÓN!,\n El alumno no tiene la reserva para el presente periodo académico, no puede proceder con la matrícula");
						return result;
					} else {
						//sucia
						//Buscamos el periodo
						Param param = new Param();
						param.put("id_suc", id_suc[i]);
						Integer id_niv=null;
						/*if(reserva.getInteger("id_gra").equals(3))
							id_niv= 2;
						else if(reserva.getInteger("id_gra").equals(9))
							id_niv=3;
						else */
							id_niv= reserva.getInteger("id_niv");
						
						param.put("id_niv", id_niv);
						param.put("id_anio", id_anio);
						param.put("id_tpe",1);
						Periodo periodo_nuevo = periodoDAO.getByParams(param);
						Ciclo ciclo_nuevo = cicloDAO.getByParams(new Param("id_per",periodo_nuevo.getId()));
						matricula.setId_per(periodo_nuevo.getId());
						matricula.setId_cic(ciclo_nuevo.getId());
						matricula.setId_gra(reserva.getInteger("id_gra"));
						matricula.setId_niv(id_niv);
						//Buscamos el aula q tiene vacante
						Param param3= new Param();
						param3.put("aula.id_cic",ciclo_nuevo.getId());
						param3.put("aula.id_grad", reserva.getInteger("id_gra"));
						param3.put("pee.id_suc", id_suc[i]);
						param3.put("aula.id_cme",id_cme[i]);
						param3.put("cta.id_cit", id_cct[i]);
						List<Aula> aulas = aulaDAO.listFullByParams(param3, new String[]{"aula.secc"});
						for (Aula aula2 : aulas) {
							//Busco la capacidad del aula
							Integer cap2=aula2.getCap();
							//Busco los matriculados en ese aula
							Param param4 = new Param();
							param4.put("id_au_asi", aula2.getId());
							param4.put("est", "A");
							List<Matricula> matriculas2=matriculaDAO.listByParams(param4, null);
							//Buscamos los trasladados
							Param param5 = new Param();
							param5.put("id_au_asi", aula2.getId());
							param5.put("id_sit", "5");
							List<Matricula> trasladados=matriculaDAO.listByParams(param5, null);
							Integer res2=vacanteService.getReservasxAula(aula2.getId(), id_anio);
							Integer nro_vac2=cap2-matriculas2.size()-res2;
							 nro_vac2=nro_vac2+trasladados.size();
							if(nro_vac2>0) {
								matricula.setId_au(aula2.getId());
								matricula.setId_au_asi(aula2.getId());
								break;
							}
						}
						///aqui terminzs
						/*Ciclo ciclo_acad=cicloDAO.getByParams(new Param("id_per",reserva.getInteger("id_per")));
						matricula.setId_per(reserva.getInteger("id_per"));
						matricula.setId_cic(ciclo_acad.getId());
						matricula.setId_gra(reserva.getInteger("id_gra"));
						matricula.setId_niv(reserva.getInteger("id_niv"));
						matricula.setId_au(reserva.getInteger("id_au"));
						matricula.setId_au_asi(reserva.getInteger("id_au"));*/
						matricula.setId_cli(1);
						matricula.setId_alu(id_alu_sel[i]);
						//Busco al familiar
						Param param_fam = new Param();
						param_fam.put("id_per", id_per_res);
					    param_fam.put("id_par", id_fam_id_par);
						Familiar familiar = familiarDAO.getByParams(param_fam);
						matricula.setId_fam(familiar.getId());
						matricula.setId_per_res(id_per_res);
						matricula.setFecha(new Date());
						matricula.setTipo("C");
						matricula.setEst("A");
						//Vien la logica del contrato


						Map<String, Object> contratoActual = matriculaDAO.getContrato(familiar.getId(), id_anio);

						logger.debug("contratoActual:" + contratoActual);

						if (contratoActual == null) {
							
							//No comprendo xq se consideraria periodo??
							Map<String, Object> map = matriculaDAO.getTotalContratos(id_anio);
							Double total = (Double)map.get("total");
							
							int correlativo = total.intValue() + 1;
							
							contrato = anio.getNom() + "-" + String.format("%05d", correlativo);

						} else {
							contrato = contratoActual.get("num_cont").toString();
						}
						matricula.setNum_cont(contrato);
						Integer id_mat= matriculaDAO.saveOrUpdate(matricula);
						
						//Inserto los pagos por derecho de matricula y pensiones
						//Matricula
						Param param_cuota = new Param();
						param_cuota.put("id_per", matricula.getId_per());
						param_cuota.put("id_cct", id_cct[i]);
						param_cuota.put("id_cme", id_cme[i]);
						ConfCuota confCuota = confCuotaDAO.getByParams(param_cuota);
						//ConfCuota confCuota = confCuotaDAO.getByParams(new Param("id_per", reserva.getInteger("id_per")));
					//	Periodo periodo_nuevo=periodoDAO.get(reserva.getInteger("id_per"));
						ReservaCuota reservaCuota = reservaDAO.getMontoReserva(periodo_nuevo.getId_anio(), id_alu_sel[i]);
						BigDecimal montoReserva = reservaCuota.getMonto();
						BigDecimal monto_matricula=confCuota.getMatricula().subtract(montoReserva);
						AcademicoPago academicoPago = new AcademicoPago();
						academicoPago.setCanc("0");// Por defecto pago pendiente
						academicoPago.setMonto(monto_matricula);
						academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_MATRICULA);
						academicoPago.setId_mat(id_mat);
						academicoPago.setId_bco_pag(id_bco);
						academicoPago.setMontoTotal(monto_matricula);
						academicoPago.setFec_venc(FechaUtil.toDate(cadenaFecha));
						academicoPago.setEst("A");
						academicoPagoDAO.saveOrUpdate(academicoPago);
						//Insertamos la cuota de Ingreso
						AcademicoPago academicoPago_ci = new AcademicoPago();
						academicoPago_ci.setCanc("0");// Por defecto pago pendiente
						academicoPago_ci.setMonto(confCuota.getCuota());
						academicoPago_ci.setTip(com.tesla.colegio.util.Constante.PAGO_CUOTA_INGRESO);
						academicoPago_ci.setId_mat(id_mat);
						academicoPago_ci.setId_bco_pag(id_bco);
						academicoPago_ci.setMontoTotal(confCuota.getCuota());
						academicoPago_ci.setFec_venc(FechaUtil.toDate(cadenaFecha));
						academicoPago_ci.setEst("A");
						academicoPagoDAO.saveOrUpdate(academicoPago_ci);

						Integer[] meses = new Integer[] { 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };

						
						Param param_1 = new Param();
						param_1.put("id_per", matricula.getId_per());
						param_1.put("id_cct", id_cct[i]);
						param_1.put("id_cme", id_cme[i]);
						ConfMensualidad confMensualidad = confMensualidadDAO.getByParams(param_1);

						//ConfMensualidad confMensualidad = confMensualidadDAO.getByParams(param_1);
						SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

						String format = formatter.format(new Date());
						Calendar calendar1 = Calendar.getInstance();
					    calendar1.setTime(new Date());
					    Integer mes_act = calendar1.get(Calendar.MONTH)+1;
						int fecActual = Integer.parseInt(format);
						
						for (int j = 0; j < meses.length; j++) {
							Param param1 = new Param();
							param1.put("id_mat", id_mat);
							param1.put("mens", meses[j]);
							AcademicoPago pago_mes = academicoPagoDAO.getByParams(param1);
							if(pago_mes==null){
								Calendar cal = Calendar.getInstance();
								//int anio_actual = cal.get(Calendar.YEAR);
								int anio_matricula= Integer.parseInt(anioDAO.getByParams(new Param("id",id_anio)).getNom());
								String tipo_fec_ven=confMensualidadDAO.getByParams(new Param("id_per", matricula.getId_per())).getTipo_fec_ven();
								int fecVencimiento =0 ;
								Integer dia_mora=0 ;
								String fec_ven=null;
								//Integer mes_matricula=f
								if(tipo_fec_ven.equals(Constante.TIPO_FEC_VEN_DIA)){
									 fecVencimiento = getFecVencimiento(anio_matricula, meses[j], matricula.getId_per());
									 dia_mora=confMensualidadDAO.getByParams(new Param("id_per", matricula.getId_per())).getDia_mora();
								} else if(tipo_fec_ven.equals(Constante.TIPO_FEC_VEN_FIN)){
									fec_ven=getFecVencimientoFinMes(Integer.parseInt(anio.getNom()), meses[j]);
									String fecha[]  = fec_ven.split("/");
									 fecVencimiento=Integer.parseInt(fecha[2]+fecha[1]+fecha[0]);
									 dia_mora=Integer.parseInt(fec_ven.substring(0, 2));
								}
								//int fecVencimiento = getFecVencimiento(anio_matricula, meses[i], matricula.getId_per());
								//Integer dia_mora=confMensualidadDAO.getByParams(new Param("id_per", matricula.getId_per())).getDia_mora();
								
								AcademicoPago academicoPago2 = new AcademicoPago();
								
								academicoPago2.setEst("A");
								academicoPago2.setId_mat(id_mat);
								academicoPago2.setId_bco_pag(id_bco);
								if (fecActual < fecVencimiento) {
									//int dias_cla=dia_mora-(new Date().getDate());
									BigDecimal monto_mens=new BigDecimal(0);
									//if(dias_cla>0)
									//monto_mens=(confMensualidad.getMonto().divide(new BigDecimal(30),2)).multiply(new BigDecimal(dias_cla));
									//else
									monto_mens=confMensualidad.getMonto();	
									academicoPago2.setMonto(monto_mens);
									academicoPago2.setMontoTotal(monto_mens);
									academicoPago2.setCanc("0");// Por defecto pago pendiente
								} else if(fecActual==fecVencimiento){
									academicoPago2.setCanc("1");// Por defecto pagado
									academicoPago2.setMonto(new BigDecimal(0));
									academicoPago2.setMontoTotal(new BigDecimal(0));
								} else if(fecActual > fecVencimiento){
									int dia_act=(new Date().getDate());
									int dias_cla=dia_mora-dia_act;
									academicoPago2.setMonto(new BigDecimal(0));
									academicoPago2.setCanc("1");//Le pongo cancelado
									academicoPago2.setMontoTotal(new BigDecimal(0));
									//Por ahora comentado
									Integer mes_sig=meses[j]+1;
									if(mes_act.equals(mes_sig)) {
										AcademicoPago pago_mes_siguiente = new AcademicoPago();
										pago_mes_siguiente.setEst("A");
										pago_mes_siguiente.setId_mat(matricula.getId());
										BigDecimal monto_mens=(confMensualidad.getMonto().divide(new BigDecimal(30),2)).multiply(new BigDecimal(dias_cla));
										pago_mes_siguiente.setMonto(monto_mens);
										pago_mes_siguiente.setMontoTotal(monto_mens);
										pago_mes_siguiente.setCanc("0");// Por defecto pago pendiente
										pago_mes_siguiente.setMens(mes_sig);
										pago_mes_siguiente.setDesc_hermano(new BigDecimal(0));
										pago_mes_siguiente.setDesc_pronto_pago(new BigDecimal(0));
										pago_mes_siguiente.setDesc_pago_adelantado(new BigDecimal(0));
										pago_mes_siguiente.setDesc_personalizado(new BigDecimal(0));
										pago_mes_siguiente.setTip(com.tesla.colegio.util.Constante.PAGO_MENSUAL);
										pago_mes_siguiente.setId_mat(id_mat);
										pago_mes_siguiente.setId_bco_pag(id_bco);
										if(meses[j].equals(12)) {
											String fec_venc_dic=confMensualidad.getFec_fin_dic();
											if(fec_venc_dic!=null) {
												SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
												academicoPago2.setFec_venc(sdf.parse(fec_venc_dic));
											} else {
												//Fecha ven
												String fec_ven_mes_act=getFecVencimientoFinMes(Integer.parseInt(anio.getNom()), mes_sig);
												SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
												pago_mes_siguiente.setFec_venc(sdf.parse(fec_ven_mes_act));
											}
											
										} else {
											//Fecha ven
											String fec_ven_mes_act=getFecVencimientoFinMes(Integer.parseInt(anio.getNom()), mes_sig);
											SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
											pago_mes_siguiente.setFec_venc(sdf.parse(fec_ven_mes_act));
										}										
										academicoPagoDAO.saveOrUpdate(pago_mes_siguiente);
									}
								} else{
									academicoPago2.setMonto(confMensualidad.getMonto());
									academicoPago2.setMontoTotal(confMensualidad.getMonto());
									academicoPago2.setCanc("1");
								}
								//academicoPago.setMonto(confMensualidad.getMonto());
								academicoPago2.setMens(meses[j]);
								academicoPago2.setTip(com.tesla.colegio.util.Constante.PAGO_MENSUAL);
								if(meses[j].equals(12)) {
									String fec_venc_dic=confMensualidad.getFec_fin_dic();
									if(fec_venc_dic!=null) {
										SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
										academicoPago2.setFec_venc(sdf.parse(fec_venc_dic));
									} else {
										SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
										academicoPago2.setFec_venc(sdf.parse(fec_ven));
									}								
								} else {
									SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
									academicoPago2.setFec_venc(sdf.parse(fec_ven));
								}
								academicoPagoDAO.saveOrUpdate(academicoPago2);
							}
					}
					}
				}
			}


		}
		
		Banco banco = bancoDAO.get(id_bco);
		//result.setResult(contrato);
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("dia", dia);
		map1.put("mes", mes);
		map1.put("anio", anio);
		map1.put("banco", banco.getNom());
		map1.put("contrato", contrato);
		result.setResult(map1);
		//result.setResult(impresion);
		//result.setResult(matriculaService.matricularYPagar(matricula,id_suc, id_anio, id_perfil));
		
		return result;

	}
	
	private int getFecVencimiento(int anio, int mes, int id_per) {

		int anioSiguiente;
		int mesSiguiente;

		if (mes == 12) {
			anioSiguiente = anio + 1;
			mesSiguiente = 1;
		} else {
			anioSiguiente = anio;
			mesSiguiente = mes + 1;
		}
		
		Integer dia_mora=confMensualidadDAO.getByParams(new Param("id_per", id_per)).getDia_mora();
		String fecVenc = anioSiguiente + String.format("%02d", mesSiguiente) + dia_mora;// TODO
																					// PARAMETRIZAR
																					// FECHA

		return Integer.parseInt(fecVenc);

	}
	
	private String getFecVencimientoFinMes(int anio, int mes) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
		 Calendar calendar = new GregorianCalendar(anio,mes-1,1);
		 Date date = calendar.getTime();
		 Calendar cal_ini = Calendar.getInstance();
		 cal_ini.setTime(date);
		 cal_ini.set(Calendar.DAY_OF_MONTH, 1);
		 cal_ini.set(Calendar.DATE, cal_ini.getActualMaximum(Calendar.DATE));
		 String fecVenc= sdf.format(cal_ini.getTime());

		return fecVenc;

	}
	

	@RequestMapping(value = "/matriculadosxContrato", method = RequestMethod.GET)
	public AjaxResponseBody matriculadosxContrato(String num_cont) throws Exception{
		AjaxResponseBody result = new AjaxResponseBody();


		result.setResult(matriculaDAO.matriculadosxContrato(num_cont));
		
		return result;

	}
	
	
	@RequestMapping(value = "/reporteConsolidado")
	public AjaxResponseBody listarReporte(Integer id_anio, Integer id_suc,Integer id_niv, Integer id_gir, Integer id_gra) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		//ALUMNOS ANTIGUOS - CON CRONOGRAMA
		boolean antiguo_con_cronograma = cronogramaDAO.alumnosAntiguosTienenVigente(id_anio);

		//ALUMNOS ANTIGUOS - SIN CRONOGRAMA
		boolean antiguo_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "AS");
		
		String tipo="";
		if(antiguo_con_cronograma){
			tipo="AC";
		} else {
			tipo="AS";
		} 
		 
		result.setResult(matriculaDAO.reporteConsolidado(id_anio,id_suc,tipo, id_niv, id_gir, id_gra));
		
		return result;
	}
	
	@RequestMapping(value = "/reporteConsolidadoGrado") //aqui
	public AjaxResponseBody listarReporteGrado(Integer id_anio, Integer id_suc, Integer id_niv, Integer id_gir, Integer id_gra) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		//ALUMNOS ANTIGUOS - CON CRONOGRAMA
		boolean antiguo_con_cronograma = cronogramaDAO.alumnosAntiguosTienenVigente(id_anio);

		//ALUMNOS ANTIGUOS - SIN CRONOGRAMA
		boolean antiguo_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "AS");
		
		String tipo="";
		if(antiguo_con_cronograma){
			tipo="AC";
		} else {
			tipo="AS";
		} 
		 
		result.setResult(matriculaDAO.reporteConsolidadoGrado(id_anio,id_suc,tipo, id_niv,id_gir,id_gra));
		
		return result;
	}
	
	@RequestMapping(value = "/reporteConsolidadoGeneralGrado") 
	public AjaxResponseBody listarReporteConsolidadoGeneralGrado(Integer id_anio, Integer id_suc, Integer id_niv, Integer id_gir, Integer id_gra) throws Exception{

		AjaxResponseBody result = new AjaxResponseBody();
		
		//ALUMNOS ANTIGUOS - CON CRONOGRAMA
		/*boolean antiguo_con_cronograma = cronogramaDAO.alumnosAntiguosTienenVigente(id_anio);

		//ALUMNOS ANTIGUOS - SIN CRONOGRAMA
		boolean antiguo_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "AS");
		
		String tipo="";
		if(antiguo_con_cronograma){
			tipo="AC";
		} else {
			tipo="AS";
		} */
		Calendar c1 = Calendar.getInstance();
		Integer anio_acad = c1.get(Calendar.YEAR);
		Integer id_anio_acad = anioDAO.getByParams(new Param("nom",anio_acad)).getId();
		String tipo="";
		//Obtengo el estado del a�o
		Date fec_act=new java.util.Date();
		Date fec_ini=anioDAO.getByParams(new Param("nom",anio_acad)).getFec_ini();
		Date fec_fin=anioDAO.getByParams(new Param("nom",anio_acad)).getFec_fin();
		String est="";
		if(fec_act.before(fec_ini)){
			est="N";//No hay clases
		} else if(fec_act.after(fec_fin)){
			est="F";//Finalizo las clases
		} else if(fec_act.after(fec_ini) && fec_act.before(fec_fin)){
			est="C";//Estan en clases
		}

		Integer anio_eva=Integer.parseInt(anioDAO.getByParams(new Param("id",id_anio)).getNom());
		Integer id_anio_eva=anioDAO.getByParams(new Param("nom",anio_eva)).getId();
		

		//Obtengo datos del cronograma de los antiguos
				boolean antiguo_con_cronograma = cronogramaDAO.alumnosAntiguosTienenVigente(id_anio_eva);
				boolean antiguo_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio_eva, "AS");
				boolean nuevo_con_cronograma = confFechasDAO.cronogramaVigente(id_anio_eva, "NC");
				boolean nuevos_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio_eva, "NS");
		
		if(anio_acad.intValue()==anio_eva.intValue() && est.equals("N") && !antiguo_con_cronograma && !antiguo_sin_cronograma && nuevo_con_cronograma){//actual y estan en cronogramas de nuevos
			//nro_vac=getCapacidadxGrado(anio_acad, id_gra, id_suc)-getAlumnosMatriculadosxGrado(id_anio_eva, id_gra, id_suc)-getMatriculasVacante(id_eva, id_gra, id_anio_eva)-getReservasxGrado(periodoEscolar.getId(), id_gra);
			tipo="C1";
		}else if(anio_acad.intValue()==anio_eva.intValue() && est.equals("N") && antiguo_sin_cronograma && nuevos_sin_cronograma){//actual y estan en sin cronograma de antiguos y nuevo sin cronogrma
			/*Integer cap=getCapacidadxGrado(anio_acad, id_gra, id_suc);
			Integer mat=getAlumnosMatriculadosxGrado(id_anio_eva, id_gra, id_suc);
			Integer mat_vac=getMatriculasVacante(id_eva, id_gra, id_anio_eva);
			Integer res=getReservasxGrado(periodoEscolar.getId(), id_gra);
			if(cap!=null)
			nro_vac=cap-mat-mat_vac-res;
			else nro_vac=0;*/
			tipo="C1";
		}
		else if(anio_acad.intValue()==anio_eva.intValue() && est.equals("N") && !antiguo_con_cronograma){
			//nro_vac=getCapacidadxGrado(anio_acad, id_gra, id_suc)-getAlumnosMatriculadosxGrado(id_anio_ant, id_gra_ant, id_suc)-getAlumnosNoRatificaronxGrado(id_anio_ant, id_anio_acad, id_gra_ant, id_suc)-getDesaprobados(id_anio_ant, id_gra, id_suc)-getMatriculasVacante(id_eva, id_gra, id_anio_eva)-getReservasxGrado(periodoEscolar.getId(), id_gra);
			tipo="C2";
		}else if(anio_acad.intValue()==anio_eva.intValue() && est.equals("N") && antiguo_con_cronograma){
			/*Integer cap=getCapacidadxGrado(anio_acad, id_gra, id_suc);
			Integer mat=getAlumnosMatriculadosxGrado(id_anio_eva, id_gra, id_suc);
			Integer no_rat=getAlumnosNoRatificaronxGrado(id_anio_ant, id_anio_acad, id_gra_ant, id_suc);
			Integer sug=alumnosSugeridosGrado(id_gra_ant, ciclo.getId(), anio_acad);
			Integer mat_vac=getMatriculasVacante(id_eva, id_gra, id_anio_eva);
			Integer res=getReservasxGrado(periodoEscolar.getId(), id_gra);
			nro_vac=cap-mat-no_rat-sug-mat_vac-res;*/
			tipo="C4";
		} else if((anio_acad.intValue()==anio_eva.intValue() && est.equals("C") && antiguo_con_cronograma) || (anio_acad.intValue()==anio_eva.intValue() && est.equals("C")  && nuevos_sin_cronograma)){
			/*Integer cap =getCapacidadxGrado(anio_acad, id_gra, id_suc);
			Integer mat=getAlumnosMatriculadosxGrado(id_anio_eva, id_gra, id_suc);
			Integer mat_vac=getMatriculasVacante(id_eva, id_gra, id_anio_eva);
			Integer res=getReservasxGrado(periodoEscolar.getId(), id_gra);			
			nro_vac=cap-mat-mat_vac-res;*/
			tipo="C1";
		}else if(anio_acad.intValue()==anio_eva.intValue() && est.equals("F") && antiguo_con_cronograma){
				throw new Exception("A�O ACADEMICO CERRADO!!");
		} else if(anio_eva.intValue()>anio_acad.intValue() && est.equals("N") && !antiguo_con_cronograma){
			throw new Exception("SOLO SE PUEDE OTROGAR VACANTES AL A�O SIGUIENTE!!");
		} else if(anio_eva.intValue()>anio_acad.intValue() && est.equals("C")){
			/*Integer cap=getCapacidadxGrado(anio_eva, id_gra, id_suc);
			Integer mat_ant=getAlumnosMatriculadosxGrado(id_anio_acad, id_gra_ant, id_suc);
			Integer no_rat=getAlumnosNoRatificaronxGrado(id_anio_acad, id_anio_eva, id_gra_ant, id_suc);
			Integer mat_vac=getMatriculasVacante(id_eva, id_gra, id_anio_eva);
			Integer res=getReservasxGrado(periodoEscolar.getId(), id_gra);
			//nro_vac=-getAlumnosMatriculadosxGrado(id_anio_acad, id_gra_ant, id_suc)-getMatriculasVacante(id_eva, id_gra, id_anio_eva)-getReservasxGrado(periodoEscolar.getId(), id_gra);
			nro_vac=cap-mat_ant-no_rat-mat_vac-res;
			//nro_vac=getCapacidadxGrado(anio_eva, id_gra, id_suc)-getAlumnosMatriculadosxGrado(id_anio_acad, id_gra_ant, id_suc)-getMatriculasVacante(id_eva, id_gra, id_anio_eva)-getReservasxGrado(periodoEscolar.getId(), id_gra);*/
			tipo="C4";
		} else if(anio_eva.intValue()>anio_acad.intValue() && est.equals("F") && !antiguo_con_cronograma){
			//nro_vac=getCapacidadxGrado(anio_eva, id_gra, id_suc)-getAlumnosMatriculadosxGrado(id_anio_acad, id_gra_ant, id_suc)-getAlumnosNoRatificaronxGrado(id_anio_acad, id_anio_eva, id_gra_ant, id_suc)-getMatriculasVacante(id_eva, id_gra, id_anio_eva)-getDesaprobados(id_anio_acad, id_gra, id_suc)-getReservasxGrado(periodoEscolar.getId(), id_gra);
			tipo="C4";
		} else if(anio_eva.intValue()>anio_acad.intValue() && est.equals("F") && antiguo_con_cronograma){
			//nro_vac=getCapacidadxGrado(anio_eva, id_gra, id_suc)-getAlumnosMatriculadosxGrado(id_anio_acad, id_gra_ant, id_suc)-getMatriculasVacante(id_eva, id_gra, id_anio_eva)-getDesaprobados(id_anio_acad, id_gra, id_suc)-getReservasxGrado(periodoEscolar.getId(), id_gra);
			/*Integer cap=getCapacidadxGrado(anio_eva, id_gra, id_suc);
			Integer mat_ant=getAlumnosMatriculadosxGrado(id_anio_acad, id_gra_ant, id_suc);
			Integer no_rat=getAlumnosNoRatificaronxGrado(id_anio_acad, id_anio_eva, id_gra_ant, id_suc);
			Integer mat_vac=getMatriculasVacante(id_eva, id_gra, id_anio_eva);
			Integer res=getReservasxGrado(periodoEscolar.getId(), id_gra);
			//nro_vac=-getAlumnosMatriculadosxGrado(id_anio_acad, id_gra_ant, id_suc)-getMatriculasVacante(id_eva, id_gra, id_anio_eva)-getReservasxGrado(periodoEscolar.getId(), id_gra);
			nro_vac=cap-mat_ant-no_rat-mat_vac-res;*/
			tipo="C4";
		}
		
		//return nro_vac;
		
		 
		result.setResult(matriculaDAO.reporteConsolidadoGradoGeneral(id_anio,id_suc,tipo, id_niv,id_gir,id_gra));
		
		return result;
	}
	
	@RequestMapping(value = "/datosApoderado/{id_mat}", method = RequestMethod.GET)
	public AjaxResponseBody datosApoderado(@PathVariable Integer id_mat) throws Exception{
		AjaxResponseBody result = new AjaxResponseBody();


		result.setResult(matriculaDAO.getDatosApoderado(id_mat));
		
		return result;

	}

	@RequestMapping(value = "/reporteMatriculaList", method = RequestMethod.GET)
	public AjaxResponseBody reporteMatriculaList( Integer id_anio, Integer id_cic, Integer id_gra, Integer id_au, Integer id_gir, Integer id_niv, String rep_com, String tras) {
		AjaxResponseBody result = new AjaxResponseBody();

		result.setResult(matriculaDAO.Reporte_Mat(id_anio, id_cic, id_gra, id_au,id_gir,id_niv, rep_com, tras));
		return result;
	 
	}
	
	@RequestMapping(value = "/reporteRatificacion", method = RequestMethod.GET)
	public AjaxResponseBody reporteRatificacion( Integer id_anio, Integer id_cic, Integer id_gra, Integer id_au, Integer id_gir, Integer id_niv, String rep_com, String rat, String fec_ini, String fec_fin) {
		AjaxResponseBody result = new AjaxResponseBody();
		try {
			result.setResult(matriculaDAO.Reporte_Ratificacion(id_anio, id_cic, id_gra, id_au, id_gir, id_niv, rat,fec_ini,fec_fin));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return result;
	 
	}
	
	@RequestMapping(value = "/reporteMatriculaListAcademico", method = RequestMethod.GET)
	public AjaxResponseBody reporteMatriculaListAcademico( Integer id_anio, Integer id_cic, Integer id_gra, Integer id_au, Integer id_gir, Integer id_niv, String rep_com, String tras) {
		AjaxResponseBody result = new AjaxResponseBody();

		result.setResult(matriculaDAO.Reporte_MatriculaAcademico(id_anio, id_cic, id_gra, id_au,id_gir,id_niv, rep_com, tras));
		return result;
	 
	}
	
	@RequestMapping(value = "/reporteMatriculaListCambioSec", method = RequestMethod.GET)
	public AjaxResponseBody reporteMatriculaListAcad( Integer id_anio, Integer id_gra, Integer id_au, Integer id_gir, Integer id_niv) {
		AjaxResponseBody result = new AjaxResponseBody();

		result.setResult(matriculaDAO.Reporte_Mat_Acad(id_anio, id_gra, id_au,id_gir,id_niv));
		return result;
	 
	}
	
	@RequestMapping(value = "/reporteMatriculados", method = RequestMethod.GET)
	public AjaxResponseBody reporteMatriculados( Integer id_anio,Integer id_niv, Integer id_gra, Integer id_au	) {
		AjaxResponseBody result = new AjaxResponseBody();

		result.setResult(matriculaDAO.reporteMatriculados(id_anio, id_niv, id_gra, id_au));
		return result;
	 
	}

	@RequestMapping(value = "/aniosList", method = RequestMethod.GET)
	public AjaxResponseBody aniosList( Integer id_alu) {
		AjaxResponseBody result = new AjaxResponseBody();

		result.setResult(matriculaDAO.aniosList(id_alu));
		return result;
	 
	}
	
	@RequestMapping(value = "/reporteNoMatriculados", method = RequestMethod.GET)
	public AjaxResponseBody reporteMatriculaList(HttpServletRequest request, Integer id_anio) {
		AjaxResponseBody result = new AjaxResponseBody();
		Integer anio_act =Integer.parseInt(anioDAO.getByParams(new Param("id",id_anio)).getNom()); 
		Integer anio_ant=anio_act-1;
		Integer id_anio_ant=anioDAO.getByParams(new Param("nom",anio_ant)).getId();
		result.setResult(matriculaDAO.noMatriculadosAnioAnterior(id_anio_ant, id_anio));
		return result;
	 
	}

	/**
	 * Lista los matriculados por contrato
	 * @param nro_contrato
	 * @return
	 */
	@RequestMapping(value = "/listxContrato/{num_cont}", method = RequestMethod.GET)
	public AjaxResponseBody listxContrato(String num_cont) {
		AjaxResponseBody result = new AjaxResponseBody();
		List<Matricula> matriculados = matriculaDAO.listFullByParams(new Param("num_cont",num_cont),new String[]{"mat.id"});
		result.setResult(matriculados);
		return result;
	 
	}
	
	@Transactional	
	@RequestMapping(value = "/GrabarPagoReq", method = RequestMethod.POST)
	public AjaxResponseBody grabarPagoReq(@RequestBody  PagoMatriculaReq pagoMatriculaReq, HttpServletResponse response) throws Exception{
		AjaxResponseBody result = new AjaxResponseBody();
		Impresion impresion = matriculaService.pagarMatricula(pagoMatriculaReq,pagoMatriculaReq.getId_suc());
		result.setResult(impresion);
		return result;
	}
	
	/**
	 * Impresion de Contrato
	 * @param num_cont
	 * @return
	 */
	@RequestMapping(value = "/ImpresionContrato", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosContrato(String num_cont, Integer id_suc, Integer id_anio) {
		AjaxResponseBody result = new AjaxResponseBody();
		//List<Row> cabecera_contrato=matriculaDAO.cabeceraContrato(num_cont);
		List<Row> matriculados = matriculaDAO.matriculadosxContrato(num_cont);
		List<Row> costos = matriculaDAO.listarCostosLocal(id_suc, id_anio);
		return result;
	}
	
	@Transactional
	@RequestMapping(value = "/generarContrato")
	@ResponseBody
	public void descargarContrato(HttpServletRequest request, HttpServletResponse response,Integer id_fam, Integer id_suc, Integer id_anio) throws Exception {
		// AjaxResponseBody result = new AjaxResponseBody();
		// Integer id_anio=2;
		
		//List<Row> cabecera_contrato= matriculaDAO.cabeceraContrato(id_fam, id_anio);
		
		//buscar si contrato ya existe
		/*Param param = new Param();
		param.put("num_cont", cabecera_contrato.get(0).getString("num_cont"));
		List<Matricula> matriculasContrato = matriculaDAO.listByParams(param, new String[]{"id desc"});*/
		List<Row> matriculasLocalApod= matriculaDAO.matriculadosWebxApoderado(id_fam, id_anio);
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
		Row cabecera_contrato=matriculaDAO.cabeceraContrato(id_fam,id_anio).get(0);
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
		List<Row> matriculados = matriculaDAO.matriculadosxContrato(num_cont);
		Integer cant_hijos = matriculados.size();
		map.put("cant_hijos", cant_hijos.toString());
		for (Row row : matriculados) {
			if(row.getString("num_adenda")==null)
			alumnos_concat = alumnos_concat + row.getString("alumno") + " al "+ row.getString("grado")+" "+ row.getString("seccion")+ " de Educaci�n "+row.getString("nivel")+", ";
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
			if(id_suc==2){
				nuevo = DocxUtil.generate(parametro.getVal(), "Contrato_local1.docx", map);
			} else if(id_suc==3){
				nuevo = DocxUtil.generate(parametro.getVal(), "Contrato_local2.docx", map);
			} else if(id_suc==4){
				nuevo = DocxUtil.generate(parametro.getVal(), "Contrato_local3.docx", map);
			}
			fileName = URLEncoder.encode("Contrato.docx", "UTF-8");
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
	
	@RequestMapping(value = "/obtenerFamiliarSugerido", method = RequestMethod.GET)
	public AjaxResponseBody obtenerFamiliarSugerido(Integer id_anio, Integer id_gpf) {
		AjaxResponseBody result = new AjaxResponseBody();
		List<Row> familiares= matriculaDAO.getFamiliarSugeridoMatricula(id_anio, id_gpf);
		result.setResult(familiares);
		return result;
	 
	}
	
	@RequestMapping(value = "/obtenerFamiliarSugeridoxUsuario", method = RequestMethod.GET)
	public AjaxResponseBody obtenerFamiliarSugeridoxUsuario(Integer id_anio, Integer id_usr) {
		AjaxResponseBody result = new AjaxResponseBody();
		List<Row> familiares= matriculaDAO.getFamiliarSugeridoMatriculaxUsuario(id_anio, id_usr);
		result.setResult(familiares);
		return result;
	 
	}
	
	
	@RequestMapping(value = "/obtenerResponsablePagoSugeridoxUsuario", method = RequestMethod.GET)
	public AjaxResponseBody obtenerResponsablePagoSugeridoxUsuario(Integer id_anio, Integer id_usr) {
		AjaxResponseBody result = new AjaxResponseBody();
		List<Row> familiares= matriculaDAO.getResponsablePagoSugeridoMatriculaxUsuario(id_anio, id_usr);
		result.setResult(familiares);
		return result;
	 
	}
	
	@RequestMapping(value = "/obtenerResponsableAcademicoSugeridoxUsuario", method = RequestMethod.GET)
	public AjaxResponseBody obtenerResponsableAcademicoSugeridoxUsuario(Integer id_anio, Integer id_usr) {
		AjaxResponseBody result = new AjaxResponseBody();
		List<Row> familiares= matriculaDAO.getResponsableAcademicoSugeridoMatriculaxUsuario(id_anio, id_usr);
		result.setResult(familiares);
		return result;
	 
	}
	
	@RequestMapping(value = "/listarDatosComunicacionXAula", method = RequestMethod.GET)
	public AjaxResponseBody listarDatosComunicacionXAula(Integer id_au, Integer id_niv, Integer id_grad, Integer id_anio, Integer id_gir, Integer id_suc, Integer id_cic) {
		AjaxResponseBody result = new AjaxResponseBody();
		List<Row> datosFamiliares= matriculaDAO.listaDatosComunicacionPadres(id_au, id_niv, id_grad, id_anio, id_gir, id_suc,id_cic);
		result.setResult(datosFamiliares);
		return result;
	 
	}
	
	
	/*@RequestMapping(value = "/obtenerDatosMatriculaAcadVac")
	public AjaxResponseBody listarNotasArea(Integer id_au) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(matriculaDAO.listaSituacionFinal(id_au) );
		
		return result;
	}*/
	
	
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
        DateFormat dateFormat = new SimpleDateFormat("mm-dd hh-mm-ss");  

		response.setHeader("Content-Disposition","attachment;filename=Reporte_Directorio" + dateFormat.format(new Date())  + ".xls");

		File initialFile = new File(archivo);
	    InputStream is = FileUtils.openInputStream(initialFile);
	    	
		IOUtils.copy(is, response.getOutputStream());
		
	}	
	
	@Transactional
	@RequestMapping(value = "/matricularAcadPag", method = RequestMethod.POST)
	public AjaxResponseBody matricularAcadPag(Matricula matricula, Integer id_grad, String tipo, Integer nro_cuo, Integer id_cct, Integer id_bco_pag[], String monto[], String fec_venc[], String total_costo, String obs) throws Exception{
		AjaxResponseBody result = new AjaxResponseBody();
		//Validar que el alumno no se matricule en el mismo ciclo y grado
		Param param1 = new Param();
		param1.put("id_alu", matricula.getId_alu());
		param1.put("id_cic", matricula.getId_cic());
		param1.put("id_gra", id_grad);
		param1.put("est", "A");
		Matricula matricula_existe=matriculaDAO.getByParams(param1);
		if(matricula_existe!=null) {
			result.setCode("500");
			result.setMsg("El alumno está ya matriculado en el presente ciclo y grado, no puede volverse a matricular");
			//model.addObject("matricula", matricula);w2
			return result;
		}
		matricula.setTipo(tipo);
		matricula.setId_gra(id_grad);
		matricula.setFecha(new Date());
		//Se hace la asignación de aula din{amicamente
		//Hallo la lista de aulas para el ciclo y grado
		/*Param param3= new Param();
		param3.put("aula.id_cic",matricula.getId_cic());
		param3.put("aula.id_grad", id_grad);
		param3.put("cit.id", id_cct);
		List<Aula> aulas = aulaDAO.listFullByParams(param3, new String[]{"aula.secc"});
		for (Aula aula : aulas) {
			//Busco la capacidad del aula
			Integer cap=aula.getCap();
			//Busco los matriculados en ese aula
			Param param4 = new Param();
			param4.put("id_au_asi", aula.getId());
			param4.put("est", "A");
			List<Matricula> matriculas=matriculaDAO.listByParams(param4, null);
			Integer nro_vac=cap-matriculas.size();
			if(nro_vac>0) {
				matricula.setId_au(aula.getId());
				matricula.setId_au_asi(aula.getId());
				continue;
			}
		}
		if(matricula.getId_au_asi()==null) {
			result.setCode("500");
			result.setMsg("No existe aulas disponibles para este grado");
			//model.addObject("matricula", matricula);w2
			return result;
		}
		*/
		Integer id_mat=matriculaDAO.saveOrUpdate(matricula);
		//Inserto ls pagos
		//Hallo el monto del ciclo
		ConfPagosCiclo confPagosCiclo= confPagosCicloDAO.getByParams(new Param("id_cct",id_cct));
		BigDecimal monto_ciclo=confPagosCiclo.getCosto();
		Integer nro_cuotas_posibles_pagar=confPagosCiclo.getNum_cuotas();
		
		Map<String,Object> map = new HashMap<String,Object>();
		BigDecimal monto_total_cuota = BigDecimal.ZERO;
		String fecha_venc=null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_YEAR, 2);
		Date fechaActual=new Date();
		String cadenaFecha=FechaUtil.toString(calendar.getTime());
		
		
		
		//Logica para cuotas q se paga en menos
		//Hallamos lo q va a pagar en la primera cuota
		Param param_ciclo = new Param();
		param_ciclo.put("id_cfpav", confPagosCiclo.getId());
		param_ciclo.put("nro_cuota", 1);
		ConfPagosCicloCuota confPagosCicloCuota1 = confPagosCicloCuotaDAO.getByParams(param_ciclo);
		BigDecimal cuota1=confPagosCicloCuota1.getCosto();
		BigDecimal resta=monto_ciclo.subtract(cuota1);
		Integer nro_cuo_pendientes_dif_1=nro_cuo-1;
		//hallamos las cuotas mensulaes de la resta
		BigDecimal cuota_mens_resta=BigDecimal.ZERO;
		if(nro_cuo_pendientes_dif_1>0)
		cuota_mens_resta=resta.divide(new BigDecimal(nro_cuo_pendientes_dif_1), 2, RoundingMode.CEILING);
		//System.out.println(cadenaFecha); 
		
		//Aca todo el cambio
		List<Row> descuentoConf = confPagosCicloDAO.datosDescuentoCuota(confPagosCiclo.getId_cct());
		//Obtengo su ultima matricula en el año anterior y q no sea trasladado
		Periodo periodo= periodoDAO.getByParams(new Param("id",matricula.getId_per()));
		Anio anio_mat=anioDAO.getByParams(new Param("id",periodo.getId_anio()));
		Integer anio_nom=Integer.parseInt(anio_mat.getNom());
		Integer anio_ant=anio_nom-1;
		Integer id_anio_ant=anioDAO.getByParams(new Param("nom",anio_ant)).getId();
		Matricula matricula_anterior=matriculaDAO.getMatriculaAnterior(matricula.getId_alu(), id_anio_ant);
		Row matricula_vigente_colegio=matriculaDAO.getMatricula(matricula.getId_alu(), anio_mat.getId());
		//Hallo el total de descuentos por el ciclo turno
		BigDecimal total_desc = BigDecimal.ZERO;
		if(descuentoConf!=null) {
			for (Row row : descuentoConf) {
				String acu=row.getString("acu");//aqui
				if(acu.equals("1")) {//Si el descuento es acumulable sigo la logica y considero los descuentos
					if(nro_cuo<=row.getInt("nro_cuota_max")) {
						if(row.get("nom").equals(Constante.DESCUENTO_EINSTINO)) {
							if(matricula_anterior!=null || matricula_vigente_colegio!=null) {
								String venc=row.getString("venc");
								Date fec_ven=row.getDate("fec_venc");
								BigDecimal descuento=(BigDecimal)row.get("monto");
								//if(nro_cuo_total==1) {
									if(venc.equals("1")) {
										if (fechaActual.before(fec_ven)){
											total_desc=total_desc.add(descuento);
											//Insertamos en la tabla de descuentos
											AlumnoDescuento alumnoDescuento = new AlumnoDescuento();
											alumnoDescuento.setId_fdes(row.getInteger("id"));
											alumnoDescuento.setId_mat(id_mat);
											alumnoDescuento.setDescuento(descuento);
											alumnoDescuento.setMotivo(row.getString("nom"));
											alumnoDescuento.setEst("A");
											alumnoDescuentoDAO.saveOrUpdate(alumnoDescuento);
											//monto_total_cuota=monto_ciclo.subtract(descuento);
											//fecha_venc=cadenaFecha;
										} /*else {
											//monto_total_cuota=monto_ciclo;
											//fecha_venc=cadenaFecha;
										}*/
									} else {
										total_desc=total_desc.add(descuento);
										//Insertamos en la tabla de descuentos
										AlumnoDescuento alumnoDescuento = new AlumnoDescuento();
										alumnoDescuento.setId_fdes(row.getInteger("id"));
										alumnoDescuento.setId_mat(id_mat);
										alumnoDescuento.setDescuento(descuento);
										alumnoDescuento.setMotivo(row.getString("nom"));
										alumnoDescuento.setEst("A");
										alumnoDescuentoDAO.saveOrUpdate(alumnoDescuento);
									}
							}
						} else {
							String venc=row.getString("venc");
							Date fec_ven=row.getDate("fec_venc");
							BigDecimal descuento=(BigDecimal)row.get("monto");
							//if(nro_cuo_total==1) {
								if(venc.equals("1")) {
									if (fechaActual.before(fec_ven)){
										total_desc=total_desc.add(descuento);
										//Insertamos en la tabla de descuentos
										AlumnoDescuento alumnoDescuento = new AlumnoDescuento();
										alumnoDescuento.setId_fdes(row.getInteger("id"));
										alumnoDescuento.setId_mat(id_mat);
										alumnoDescuento.setDescuento(descuento);
										alumnoDescuento.setMotivo(row.getString("nom"));
										alumnoDescuento.setEst("A");
										alumnoDescuentoDAO.saveOrUpdate(alumnoDescuento);
										//monto_total_cuota=monto_ciclo.subtract(descuento);
										//fecha_venc=cadenaFecha;
									} /*else {
										//monto_total_cuota=monto_ciclo;
										//fecha_venc=cadenaFecha;
									}*/
								} else {
									total_desc=total_desc.add(descuento);
									//Insertamos en la tabla de descuentos
									AlumnoDescuento alumnoDescuento = new AlumnoDescuento();
									alumnoDescuento.setId_fdes(row.getInteger("id"));
									alumnoDescuento.setId_mat(id_mat);
									alumnoDescuento.setDescuento(descuento);
									alumnoDescuento.setMotivo(row.getString("nom"));
									alumnoDescuento.setEst("A");
									alumnoDescuentoDAO.saveOrUpdate(alumnoDescuento);
								}
						}

					
						
					}
				} else {
					if(nro_cuo==row.getInt("nro_cuota_max")) {
						if(nro_cuo<=row.getInt("nro_cuota_max")) {
							if(row.get("nom").equals(Constante.DESCUENTO_EINSTINO)) {
								if(matricula_anterior!=null || matricula_vigente_colegio!=null) {
									String venc=row.getString("venc");
									Date fec_ven=row.getDate("fec_venc");
									BigDecimal descuento=(BigDecimal)row.get("monto");
									//if(nro_cuo_total==1) {
										if(venc.equals("1")) {
											if (fechaActual.before(fec_ven)){
												total_desc=total_desc.add(descuento);
												//Insertamos en la tabla de descuentos
												AlumnoDescuento alumnoDescuento = new AlumnoDescuento();
												alumnoDescuento.setId_fdes(row.getInteger("id"));
												alumnoDescuento.setId_mat(id_mat);
												alumnoDescuento.setDescuento(descuento);
												alumnoDescuento.setMotivo(row.getString("nom"));
												alumnoDescuento.setEst("A");
												alumnoDescuentoDAO.saveOrUpdate(alumnoDescuento);
												//monto_total_cuota=monto_ciclo.subtract(descuento);
												//fecha_venc=cadenaFecha;
											} /*else {
												//monto_total_cuota=monto_ciclo;
												//fecha_venc=cadenaFecha;
											}*/
										} else {
											total_desc=total_desc.add(descuento);
											//Insertamos en la tabla de descuentos
											AlumnoDescuento alumnoDescuento = new AlumnoDescuento();
											alumnoDescuento.setId_fdes(row.getInteger("id"));
											alumnoDescuento.setId_mat(id_mat);
											alumnoDescuento.setDescuento(descuento);
											alumnoDescuento.setMotivo(row.getString("nom"));
											alumnoDescuento.setEst("A");
											alumnoDescuentoDAO.saveOrUpdate(alumnoDescuento);
										}
								}
							} else {
								String venc=row.getString("venc");
								Date fec_ven=row.getDate("fec_venc");
								BigDecimal descuento=(BigDecimal)row.get("monto");
								//if(nro_cuo_total==1) {
									if(venc.equals("1")) {
										if (fechaActual.before(fec_ven)){
											total_desc=total_desc.add(descuento);
											//Insertamos en la tabla de descuentos
											AlumnoDescuento alumnoDescuento = new AlumnoDescuento();
											alumnoDescuento.setId_fdes(row.getInteger("id"));
											alumnoDescuento.setId_mat(id_mat);
											alumnoDescuento.setDescuento(descuento);
											alumnoDescuento.setMotivo(row.getString("nom"));
											alumnoDescuento.setEst("A");
											alumnoDescuentoDAO.saveOrUpdate(alumnoDescuento);
											//monto_total_cuota=monto_ciclo.subtract(descuento);
											//fecha_venc=cadenaFecha;
										} /*else {
											//monto_total_cuota=monto_ciclo;
											//fecha_venc=cadenaFecha;
										}*/
									} else {
										total_desc=total_desc.add(descuento);
										//Insertamos en la tabla de descuentos
										AlumnoDescuento alumnoDescuento = new AlumnoDescuento();
										alumnoDescuento.setId_fdes(row.getInteger("id"));
										alumnoDescuento.setId_mat(id_mat);
										alumnoDescuento.setDescuento(descuento);
										alumnoDescuento.setMotivo(row.getString("nom"));
										alumnoDescuento.setEst("A");
										alumnoDescuentoDAO.saveOrUpdate(alumnoDescuento);
									}
							}

						
							
						}
					}
				}

				}	
		}
		
		//String fec_venc_cuota1="";
		Date fechaConcreta = new SimpleDateFormat("dd/MM/yyyy").parse(cadenaFecha);

	    String dia = new SimpleDateFormat("dd").format(fechaConcreta);
	    String mes = new SimpleDateFormat("MMMM").format(fechaConcreta);
	    String anio = new SimpleDateFormat("yyyy").format(fechaConcreta);
	  
	    Banco banco =null;
	    //Obtener el nombre del banco
	    if(id_bco_pag!=null) {
	    	 banco = bancoDAO.getByParams(new Param("id",id_bco_pag[0]));
	    }
	    
	    
	    //Si es el pago del tipo OTRO
	    if(nro_cuo.equals(0)) {
	    	//Fecha de vencimiento
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(new Date());
			calendar2.add(Calendar.DAY_OF_YEAR, 2);
			//Date fechaActual=new Date();
			String cadenaFecha2=FechaUtil.toString(calendar2.getTime());

			Date fec_vencimiento = new SimpleDateFormat("yyyy/MM/dd").parse(cadenaFecha);
	    	AcademicoPago academicoPago = new AcademicoPago();
			academicoPago.setId_mat(id_mat);
			academicoPago.setTip("MAT");
			academicoPago.setId_bco_pag(1);
			academicoPago.setNro_cuota(1);
			academicoPago.setTip_pag("O");
			academicoPago.setMonto(new BigDecimal(total_costo));
			academicoPago.setCanc("0");
			academicoPago.setFec_venc(fec_vencimiento);
			academicoPago.setDesc_personalizado(new BigDecimal(0));
			academicoPago.setMontoTotal(new BigDecimal(total_costo));
			academicoPago.setObs(obs);
			academicoPago.setEst("A");
			academicoPagoDAO.saveOrUpdate(academicoPago);
	    } 
		//Inserto los pagos para todas las cuotas
		for (int i = 1; i <= nro_cuo; i++) {
			//Aqui obtengo los valores de las cuotas
			BigDecimal monto_cuota=new BigDecimal(monto[i-1]);
			Date fecha_cuota=FechaUtil.toDate(fec_venc[i-1]);
			if(i==1) {
				BigDecimal monto_final=monto_cuota.add(total_desc);
				AcademicoPago academicoPago = new AcademicoPago();
				academicoPago.setId_mat(id_mat);
				academicoPago.setTip("MAT");
				academicoPago.setId_bco_pag(id_bco_pag[i-1]);
				academicoPago.setNro_cuota(i);
				academicoPago.setMonto(monto_final);
				academicoPago.setCanc("0");
				academicoPago.setFec_venc(fecha_cuota);
				academicoPago.setDesc_personalizado(total_desc);
				academicoPago.setMontoTotal(monto_cuota);
				academicoPago.setEst("A");
				academicoPagoDAO.saveOrUpdate(academicoPago);
				
			} else {
				AcademicoPago academicoPago = new AcademicoPago();
				academicoPago.setId_mat(id_mat);
				academicoPago.setTip("MAT");
				academicoPago.setId_bco_pag(id_bco_pag[i-1]);
				academicoPago.setNro_cuota(i);
				academicoPago.setMonto(monto_cuota);
				academicoPago.setCanc("0");
				academicoPago.setFec_venc(fecha_cuota);
				//academicoPago.setDesc_personalizado();
				academicoPago.setMontoTotal(monto_cuota);
				academicoPago.setEst("A");
				academicoPagoDAO.saveOrUpdate(academicoPago);
			}
		
			
			/*if(i==1) {
				Param param = new Param();
				param.put("id_cfpav", confPagosCiclo.getId());
				param.put("nro_cuota", i);
				ConfPagosCicloCuota confPagosCicloCuota = confPagosCicloCuotaDAO.getByParams(param);
				monto_total_cuota=confPagosCicloCuota.getCosto().subtract(total_desc);
				fecha_venc=cadenaFecha;
				//Inserto en Academico Pago
				AcademicoPago academicoPago = new AcademicoPago();
				academicoPago.setId_mat(id_mat);
				academicoPago.setTip("MAT");
				academicoPago.setId_bco_pag(id_bco_pag[i-1]);
				academicoPago.setNro_cuota(i);
				academicoPago.setMonto(confPagosCicloCuota.getCosto());
				academicoPago.setCanc("0");
				academicoPago.setFec_venc(FechaUtil.toDate(fecha_venc));
				academicoPago.setDesc_personalizado(total_desc);
				academicoPago.setMontoTotal(monto_total_cuota);
				academicoPago.setEst("A");
				academicoPagoDAO.saveOrUpdate(academicoPago);
				
				
			} else {
				Param param = new Param();
				param.put("id_cfpav", confPagosCiclo.getId());
				param.put("nro_cuota", i);
				ConfPagosCicloCuota confPagosCicloCuota = confPagosCicloCuotaDAO.getByParams(param);
				monto_total_cuota=confPagosCicloCuota.getCosto();
				fecha_venc=FechaUtil.toString(confPagosCicloCuota.getFec_venc());
				//Inserto en Academico Pago
				AcademicoPago academicoPago = new AcademicoPago();
				academicoPago.setId_mat(id_mat);
				academicoPago.setTip("MAT");
				academicoPago.setId_bco_pag(id_bco_pag[i-1]);
				academicoPago.setNro_cuota(i);
				academicoPago.setMonto(confPagosCicloCuota.getCosto());
				academicoPago.setCanc("0");
				academicoPago.setFec_venc(FechaUtil.toDate(fecha_venc));
				//academicoPago.setDesc_personalizado(total_desc);
				academicoPago.setMontoTotal(monto_total_cuota);
				academicoPago.setEst("A");
				academicoPagoDAO.saveOrUpdate(academicoPago);
			}	*/	
		}
		
		GruFamAlumno gruFamAlumno = gruFamAlumnoDAO.getByParams(new Param("id_alu",matricula.getId_alu()));
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("dia", dia);
		map1.put("mes", mes);
		map1.put("anio", anio);
		if(banco!=null) {
			map1.put("banco", banco.getNom());
		}else {
			map1.put("banco", 1);
		}
		
		map1.put("id_mat", id_mat);
		map1.put("id_gpf", gruFamAlumno.getId_gpf());
		result.setResult(map1);
		
		//result.setResult(id_mat);
		//result.setResult(impresion);
		//result.setResult(matriculaService.matricularYPagar(matricula,id_suc, id_anio, id_perfil));
		
		return result;
	}
	
	@Transactional
	@RequestMapping(value = "/matricularAcadPagOld", method = RequestMethod.POST)
	public AjaxResponseBody matricularAcadPagOld(Matricula matricula, Integer id_grad, String tipo, Integer nro_cuo, Integer id_cct, Integer id_bco_pag[]) throws Exception{
		AjaxResponseBody result = new AjaxResponseBody();
		//Validar que el alumno no se matricule en el mismo ciclo y grado
		Param param1 = new Param();
		param1.put("id_alu", matricula.getId_alu());
		param1.put("id_cic", matricula.getId_cic());
		param1.put("id_gra", id_grad);
		Matricula matricula_existe=matriculaDAO.getByParams(param1);
		if(matricula_existe!=null) {
			result.setCode("500");
			result.setMsg("El alumno está ya matriculado en el presente ciclo y grado, no puede volverse a matricular");
			//model.addObject("matricula", matricula);w2
			return result;
		}
		matricula.setTipo(tipo);
		matricula.setId_gra(id_grad);
		matricula.setFecha(new Date());
		//Se hace la asignación de aula din{amicamente
		//Hallo la lista de aulas para el ciclo y grado
		/*Param param3= new Param();
		param3.put("aula.id_cic",matricula.getId_cic());
		param3.put("aula.id_grad", id_grad);
		param3.put("cit.id", id_cct);
		List<Aula> aulas = aulaDAO.listFullByParams(param3, new String[]{"aula.secc"});
		for (Aula aula : aulas) {
			//Busco la capacidad del aula
			Integer cap=aula.getCap();
			//Busco los matriculados en ese aula
			Param param4 = new Param();
			param4.put("id_au_asi", aula.getId());
			param4.put("est", "A");
			List<Matricula> matriculas=matriculaDAO.listByParams(param4, null);
			Integer nro_vac=cap-matriculas.size();
			if(nro_vac>0) {
				matricula.setId_au(aula.getId());
				matricula.setId_au_asi(aula.getId());
				continue;
			}
		}
		if(matricula.getId_au_asi()==null) {
			result.setCode("500");
			result.setMsg("No existe aulas disponibles para este grado");
			//model.addObject("matricula", matricula);w2
			return result;
		}
		*/
		Integer id_mat=matriculaDAO.saveOrUpdate(matricula);
		//Inserto ls pagos
		//Hallo el monto del ciclo
		ConfPagosCiclo confPagosCiclo= confPagosCicloDAO.getByParams(new Param("id_cct",id_cct));
		BigDecimal monto_ciclo=confPagosCiclo.getCosto();
		Integer nro_cuotas_posibles_pagar=confPagosCiclo.getNum_cuotas();
		
		Map<String,Object> map = new HashMap<String,Object>();
		BigDecimal monto_total_cuota = BigDecimal.ZERO;
		String fecha_venc=null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_YEAR, 2);
		Date fechaActual=new Date();
		String cadenaFecha=FechaUtil.toString(calendar.getTime());
		
		
		
		//Logica para cuotas q se paga en menos
		//Hallamos lo q va a pagar en la primera cuota
		Param param_ciclo = new Param();
		param_ciclo.put("id_cfpav", confPagosCiclo.getId());
		param_ciclo.put("nro_cuota", 1);
		ConfPagosCicloCuota confPagosCicloCuota1 = confPagosCicloCuotaDAO.getByParams(param_ciclo);
		BigDecimal cuota1=confPagosCicloCuota1.getCosto();
		BigDecimal resta=monto_ciclo.subtract(cuota1);
		Integer nro_cuo_pendientes_dif_1=nro_cuo-1;
		//hallamos las cuotas mensulaes de la resta
		BigDecimal cuota_mens_resta=BigDecimal.ZERO;
		if(nro_cuo_pendientes_dif_1>0)
		cuota_mens_resta=resta.divide(new BigDecimal(nro_cuo_pendientes_dif_1), 2, RoundingMode.CEILING);
		//System.out.println(cadenaFecha); 
		
		//Aca todo el cambio
		List<Row> descuentoConf = confPagosCicloDAO.datosDescuentoCuota(confPagosCiclo.getId_cct());
		//Obtengo su ultima matricula en el año anterior y q no sea trasladado
		Periodo periodo= periodoDAO.getByParams(new Param("id",matricula.getId_per()));
		Anio anio_mat=anioDAO.getByParams(new Param("id",periodo.getId_anio()));
		Integer anio_nom=Integer.parseInt(anio_mat.getNom());
		Integer anio_ant=anio_nom-1;
		Integer id_anio_ant=anioDAO.getByParams(new Param("nom",anio_ant)).getId();
		Matricula matricula_anterior=matriculaDAO.getMatriculaAnterior(matricula.getId_alu(), id_anio_ant);
		Row matricula_vigente_colegio=matriculaDAO.getMatricula(matricula.getId_alu(), anio_mat.getId());
		//Hallo el total de descuentos por el ciclo turno
		BigDecimal total_desc = BigDecimal.ZERO;
		if(descuentoConf!=null) {
			for (Row row : descuentoConf) {
				String acu=row.getString("acu");//aqui
				if(acu.equals("1")) {//Si el descuento es acumulable sigo la logica y considero los descuentos
					if(nro_cuo<=row.getInt("nro_cuota_max")) {
						if(row.get("nom").equals(Constante.DESCUENTO_EINSTINO)) {
							if(matricula_anterior!=null || matricula_vigente_colegio!=null) {
								String venc=row.getString("venc");
								Date fec_ven=row.getDate("fec_venc");
								BigDecimal descuento=(BigDecimal)row.get("monto");
								//if(nro_cuo_total==1) {
									if(venc.equals("1")) {
										if (fechaActual.before(fec_ven)){
											total_desc=total_desc.add(descuento);
											//Insertamos en la tabla de descuentos
											AlumnoDescuento alumnoDescuento = new AlumnoDescuento();
											alumnoDescuento.setId_fdes(row.getInteger("id"));
											alumnoDescuento.setId_mat(id_mat);
											alumnoDescuento.setDescuento(descuento);
											alumnoDescuento.setMotivo(row.getString("nom"));
											alumnoDescuento.setEst("A");
											alumnoDescuentoDAO.saveOrUpdate(alumnoDescuento);
											//monto_total_cuota=monto_ciclo.subtract(descuento);
											//fecha_venc=cadenaFecha;
										} /*else {
											//monto_total_cuota=monto_ciclo;
											//fecha_venc=cadenaFecha;
										}*/
									} else {
										total_desc=total_desc.add(descuento);
										//Insertamos en la tabla de descuentos
										AlumnoDescuento alumnoDescuento = new AlumnoDescuento();
										alumnoDescuento.setId_fdes(row.getInteger("id"));
										alumnoDescuento.setId_mat(id_mat);
										alumnoDescuento.setDescuento(descuento);
										alumnoDescuento.setMotivo(row.getString("nom"));
										alumnoDescuento.setEst("A");
										alumnoDescuentoDAO.saveOrUpdate(alumnoDescuento);
									}
							}
						} else {
							String venc=row.getString("venc");
							Date fec_ven=row.getDate("fec_venc");
							BigDecimal descuento=(BigDecimal)row.get("monto");
							//if(nro_cuo_total==1) {
								if(venc.equals("1")) {
									if (fechaActual.before(fec_ven)){
										total_desc=total_desc.add(descuento);
										//Insertamos en la tabla de descuentos
										AlumnoDescuento alumnoDescuento = new AlumnoDescuento();
										alumnoDescuento.setId_fdes(row.getInteger("id"));
										alumnoDescuento.setId_mat(id_mat);
										alumnoDescuento.setDescuento(descuento);
										alumnoDescuento.setMotivo(row.getString("nom"));
										alumnoDescuento.setEst("A");
										alumnoDescuentoDAO.saveOrUpdate(alumnoDescuento);
										//monto_total_cuota=monto_ciclo.subtract(descuento);
										//fecha_venc=cadenaFecha;
									} /*else {
										//monto_total_cuota=monto_ciclo;
										//fecha_venc=cadenaFecha;
									}*/
								} else {
									total_desc=total_desc.add(descuento);
									//Insertamos en la tabla de descuentos
									AlumnoDescuento alumnoDescuento = new AlumnoDescuento();
									alumnoDescuento.setId_fdes(row.getInteger("id"));
									alumnoDescuento.setId_mat(id_mat);
									alumnoDescuento.setDescuento(descuento);
									alumnoDescuento.setMotivo(row.getString("nom"));
									alumnoDescuento.setEst("A");
									alumnoDescuentoDAO.saveOrUpdate(alumnoDescuento);
								}
						}

					
						
					}
				} else {
					if(nro_cuo==row.getInt("nro_cuota_max")) {
						if(nro_cuo<=row.getInt("nro_cuota_max")) {
							if(row.get("nom").equals(Constante.DESCUENTO_EINSTINO)) {
								if(matricula_anterior!=null || matricula_vigente_colegio!=null) {
									String venc=row.getString("venc");
									Date fec_ven=row.getDate("fec_venc");
									BigDecimal descuento=(BigDecimal)row.get("monto");
									//if(nro_cuo_total==1) {
										if(venc.equals("1")) {
											if (fechaActual.before(fec_ven)){
												total_desc=total_desc.add(descuento);
												//Insertamos en la tabla de descuentos
												AlumnoDescuento alumnoDescuento = new AlumnoDescuento();
												alumnoDescuento.setId_fdes(row.getInteger("id"));
												alumnoDescuento.setId_mat(id_mat);
												alumnoDescuento.setDescuento(descuento);
												alumnoDescuento.setMotivo(row.getString("nom"));
												alumnoDescuento.setEst("A");
												alumnoDescuentoDAO.saveOrUpdate(alumnoDescuento);
												//monto_total_cuota=monto_ciclo.subtract(descuento);
												//fecha_venc=cadenaFecha;
											} /*else {
												//monto_total_cuota=monto_ciclo;
												//fecha_venc=cadenaFecha;
											}*/
										} else {
											total_desc=total_desc.add(descuento);
											//Insertamos en la tabla de descuentos
											AlumnoDescuento alumnoDescuento = new AlumnoDescuento();
											alumnoDescuento.setId_fdes(row.getInteger("id"));
											alumnoDescuento.setId_mat(id_mat);
											alumnoDescuento.setDescuento(descuento);
											alumnoDescuento.setMotivo(row.getString("nom"));
											alumnoDescuento.setEst("A");
											alumnoDescuentoDAO.saveOrUpdate(alumnoDescuento);
										}
								}
							} else {
								String venc=row.getString("venc");
								Date fec_ven=row.getDate("fec_venc");
								BigDecimal descuento=(BigDecimal)row.get("monto");
								//if(nro_cuo_total==1) {
									if(venc.equals("1")) {
										if (fechaActual.before(fec_ven)){
											total_desc=total_desc.add(descuento);
											//Insertamos en la tabla de descuentos
											AlumnoDescuento alumnoDescuento = new AlumnoDescuento();
											alumnoDescuento.setId_fdes(row.getInteger("id"));
											alumnoDescuento.setId_mat(id_mat);
											alumnoDescuento.setDescuento(descuento);
											alumnoDescuento.setMotivo(row.getString("nom"));
											alumnoDescuento.setEst("A");
											alumnoDescuentoDAO.saveOrUpdate(alumnoDescuento);
											//monto_total_cuota=monto_ciclo.subtract(descuento);
											//fecha_venc=cadenaFecha;
										} /*else {
											//monto_total_cuota=monto_ciclo;
											//fecha_venc=cadenaFecha;
										}*/
									} else {
										total_desc=total_desc.add(descuento);
										//Insertamos en la tabla de descuentos
										AlumnoDescuento alumnoDescuento = new AlumnoDescuento();
										alumnoDescuento.setId_fdes(row.getInteger("id"));
										alumnoDescuento.setId_mat(id_mat);
										alumnoDescuento.setDescuento(descuento);
										alumnoDescuento.setMotivo(row.getString("nom"));
										alumnoDescuento.setEst("A");
										alumnoDescuentoDAO.saveOrUpdate(alumnoDescuento);
									}
							}

						
							
						}
					}
				}

				}	
		}
		
		//String fec_venc_cuota1="";
		Date fechaConcreta = new SimpleDateFormat("dd/MM/yyyy").parse(cadenaFecha);

	    String dia = new SimpleDateFormat("dd").format(fechaConcreta);
	    String mes = new SimpleDateFormat("MMMM").format(fechaConcreta);
	    String anio = new SimpleDateFormat("yyyy").format(fechaConcreta);
	    //Obtener el nombre del banco
	    Banco banco = bancoDAO.getByParams(new Param("id",id_bco_pag[0]));
		//Inserto los pagos para todas las cuotas
		for (int i = 1; i <= nro_cuo; i++) {
			if(i==1 && nro_cuo==1) {
				monto_total_cuota=monto_ciclo.subtract(total_desc);
				fecha_venc=cadenaFecha;
				//Inserto en Academico Pago
				AcademicoPago academicoPago = new AcademicoPago();
				academicoPago.setId_mat(id_mat);
				academicoPago.setTip("MAT");
				academicoPago.setId_bco_pag(id_bco_pag[i-1]);
				academicoPago.setNro_cuota(i);
				academicoPago.setMonto(monto_ciclo);
				academicoPago.setCanc("0");
				academicoPago.setFec_venc(FechaUtil.toDate(fecha_venc));
				academicoPago.setDesc_personalizado(total_desc);
				academicoPago.setMontoTotal(monto_total_cuota);
				academicoPago.setEst("A");
				academicoPagoDAO.saveOrUpdate(academicoPago);
			} else if(i==1 && nro_cuo<=nro_cuotas_posibles_pagar && nro_cuo!=1) {
				Param param = new Param();
				param.put("id_cfpav", confPagosCiclo.getId());
				param.put("nro_cuota", i);
				ConfPagosCicloCuota confPagosCicloCuota = confPagosCicloCuotaDAO.getByParams(param);
				monto_total_cuota=confPagosCicloCuota.getCosto().subtract(total_desc);
				fecha_venc=cadenaFecha;
				//Inserto en Academico Pago
				AcademicoPago academicoPago = new AcademicoPago();
				academicoPago.setId_mat(id_mat);
				academicoPago.setTip("MAT");
				academicoPago.setId_bco_pag(id_bco_pag[i-1]);
				academicoPago.setNro_cuota(i);
				academicoPago.setMonto(confPagosCicloCuota.getCosto());
				academicoPago.setCanc("0");
				academicoPago.setFec_venc(FechaUtil.toDate(fecha_venc));
				academicoPago.setDesc_personalizado(total_desc);
				academicoPago.setMontoTotal(monto_total_cuota);
				academicoPago.setEst("A");
				academicoPagoDAO.saveOrUpdate(academicoPago);
			} else if(i>1 && nro_cuo<nro_cuotas_posibles_pagar) {		
				Param param = new Param();
				param.put("id_cfpav", confPagosCiclo.getId());
				param.put("nro_cuota", i);
				ConfPagosCicloCuota confPagosCicloCuota = confPagosCicloCuotaDAO.getByParams(param);
				monto_total_cuota=cuota_mens_resta;
				fecha_venc=FechaUtil.toString(confPagosCicloCuota.getFec_venc());
				//Inserto en Academico Pago
				AcademicoPago academicoPago = new AcademicoPago();
				academicoPago.setId_mat(id_mat);
				academicoPago.setTip("MEN");
				academicoPago.setId_bco_pag(id_bco_pag[i-1]);
				academicoPago.setNro_cuota(i);
				academicoPago.setMonto(cuota_mens_resta);
				academicoPago.setCanc("0");
				academicoPago.setFec_venc(FechaUtil.toDate(fecha_venc));
				//academicoPago.setDesc_personalizado(total_desc);
				academicoPago.setMontoTotal(monto_total_cuota);
				academicoPago.setEst("A");
				academicoPagoDAO.saveOrUpdate(academicoPago);
			} else if(i>1 && nro_cuo==nro_cuotas_posibles_pagar) {		
				Param param = new Param();
				param.put("id_cfpav", confPagosCiclo.getId());
				param.put("nro_cuota", i);
				ConfPagosCicloCuota confPagosCicloCuota = confPagosCicloCuotaDAO.getByParams(param);
				monto_total_cuota=confPagosCicloCuota.getCosto();
				fecha_venc=FechaUtil.toString(confPagosCicloCuota.getFec_venc());
				//Inserto en Academico Pago
				AcademicoPago academicoPago = new AcademicoPago();
				academicoPago.setId_mat(id_mat);
				academicoPago.setTip("MEN");
				academicoPago.setId_bco_pag(id_bco_pag[i-1]);
				academicoPago.setNro_cuota(i);
				academicoPago.setMonto(confPagosCicloCuota.getCosto());
				academicoPago.setCanc("0");
				academicoPago.setFec_venc(FechaUtil.toDate(fecha_venc));
				//academicoPago.setDesc_personalizado(total_desc);
				academicoPago.setMontoTotal(monto_total_cuota);
				academicoPago.setEst("A");
				academicoPagoDAO.saveOrUpdate(academicoPago);
			} 
			/*if(i==1) {
				Param param = new Param();
				param.put("id_cfpav", confPagosCiclo.getId());
				param.put("nro_cuota", i);
				ConfPagosCicloCuota confPagosCicloCuota = confPagosCicloCuotaDAO.getByParams(param);
				monto_total_cuota=confPagosCicloCuota.getCosto().subtract(total_desc);
				fecha_venc=cadenaFecha;
				//Inserto en Academico Pago
				AcademicoPago academicoPago = new AcademicoPago();
				academicoPago.setId_mat(id_mat);
				academicoPago.setTip("MAT");
				academicoPago.setId_bco_pag(id_bco_pag[i-1]);
				academicoPago.setNro_cuota(i);
				academicoPago.setMonto(confPagosCicloCuota.getCosto());
				academicoPago.setCanc("0");
				academicoPago.setFec_venc(FechaUtil.toDate(fecha_venc));
				academicoPago.setDesc_personalizado(total_desc);
				academicoPago.setMontoTotal(monto_total_cuota);
				academicoPago.setEst("A");
				academicoPagoDAO.saveOrUpdate(academicoPago);
				
				
			} else {
				Param param = new Param();
				param.put("id_cfpav", confPagosCiclo.getId());
				param.put("nro_cuota", i);
				ConfPagosCicloCuota confPagosCicloCuota = confPagosCicloCuotaDAO.getByParams(param);
				monto_total_cuota=confPagosCicloCuota.getCosto();
				fecha_venc=FechaUtil.toString(confPagosCicloCuota.getFec_venc());
				//Inserto en Academico Pago
				AcademicoPago academicoPago = new AcademicoPago();
				academicoPago.setId_mat(id_mat);
				academicoPago.setTip("MAT");
				academicoPago.setId_bco_pag(id_bco_pag[i-1]);
				academicoPago.setNro_cuota(i);
				academicoPago.setMonto(confPagosCicloCuota.getCosto());
				academicoPago.setCanc("0");
				academicoPago.setFec_venc(FechaUtil.toDate(fecha_venc));
				//academicoPago.setDesc_personalizado(total_desc);
				academicoPago.setMontoTotal(monto_total_cuota);
				academicoPago.setEst("A");
				academicoPagoDAO.saveOrUpdate(academicoPago);
			}	*/	
		}
		
		GruFamAlumno gruFamAlumno = gruFamAlumnoDAO.getByParams(new Param("id_alu",matricula.getId_alu()));
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("dia", dia);
		map1.put("mes", mes);
		map1.put("anio", anio);
		map1.put("banco", banco.getNom());
		map1.put("id_mat", id_mat);
		map1.put("id_gpf", gruFamAlumno.getId_gpf());
		result.setResult(map1);
		
		//result.setResult(id_mat);
		//result.setResult(impresion);
		//result.setResult(matriculaService.matricularYPagar(matricula,id_suc, id_anio, id_perfil));
		
		return result;
	}
	
	@RequestMapping(value = "/listarMatriculasCicloxAlumno", method = RequestMethod.GET)
	public AjaxResponseBody listarMatriculasCiclxAlumno(Integer id_alu, Integer id_anio) {
		AjaxResponseBody result = new AjaxResponseBody();
		result.setResult(matriculaDAO.listarMatriculaCiclo(id_alu, id_anio));
		return result;
	 
	}
	
	@RequestMapping( value="eliminarMatriculaAcadVac/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//Verificamos q no haya pagado la primera cuota
			Param param = new Param();
			param.put("id_mat", id);
			param.put("nro_cuota", 1);
			AcademicoPago academicoPago= academicoPagoDAO.getByParams(param);
			String canc="";
			if(academicoPago!=null) {
				canc=academicoPago.getCanc();
			}
			
			if(canc.equals("1")) {
				result.setCode("500");
				result.setMsg("El alumno ya realizó el pago de la primera cuota, no se puede eliminar la matrícula");
				//model.addObject("matricula", matricula);w2
				return result;
			} else {
				//`Primero eliminamos las cuotas de pago
				academicoPagoDAO.delete(id);
				//eliminamos los descuentos relacionados
				alumnoDescuentoDAO.delete(id);
				//Eliminamos la matricula
				matriculaDAO.delete(id);
			}
			
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	

	@RequestMapping(value = "/matriculaAcadVacEditar", method = RequestMethod.GET)
	public AjaxResponseBody matriculaAcadVacEditar(Integer id_mat) {
		AjaxResponseBody result = new AjaxResponseBody();
		result.setResult(matriculaDAO.obtenerDatosMatriculaAcadVac(id_mat));
		return result;
	 
	}
	
	@RequestMapping(value = "/obtenerPagosMatricula", method = RequestMethod.GET)
	public AjaxResponseBody obtenerpagosMatricula(Integer id_mat) {
		AjaxResponseBody result = new AjaxResponseBody();
		List<AcademicoPago> listaPagosMatricula = academicoPagoDAO.listByParams(new Param("id_mat",id_mat),new String[]{"nro_cuota"});
		Integer nro_cuo_total=0;
		for (AcademicoPago academicoPago : listaPagosMatricula) {
			Integer nro_cuo_pag=academicoPago.getNro_cuota();
			nro_cuo_total = nro_cuo_total + nro_cuo_pag;
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("nro_cuo_total", nro_cuo_total);
		map.put("listaPagos", listaPagosMatricula);
		result.setResult(map);
		return result;
	 
	}
	
	@RequestMapping( value="/crearUsuariosGoogle/{id_anio}/{id_cic}", method = RequestMethod.GET)
	public AjaxResponseBody crearGruposClasrrom(@PathVariable Integer id_anio, @PathVariable Integer id_cic) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			campusVirtualService.generarUsuarioGooglexCilo(id_anio, id_cic);
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/crearUsuariosGoogleMatriculasValidadas/{id_anio}/{id_cic}", method = RequestMethod.GET)
	public AjaxResponseBody crearUusuariosClasrromMatriculasVal(@PathVariable Integer id_anio, @PathVariable Integer id_cic) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			campusVirtualService.generarUsuarioGooglexCiCloColegioMatriculasValidadas(id_anio, id_cic);
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/crearUsuariosGoogleMatriculasValidadasvu/{id_anio}/{id_cic}", method = RequestMethod.GET)
	public AjaxResponseBody crearUusuariosClasrromMatriculasValVU(@PathVariable Integer id_anio, @PathVariable Integer id_cic) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			campusVirtualService.generarUsuarioGooglexCiCloColegioMatriculasValidadasVU(id_anio, id_cic);
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/crearCursosGoogleClassroom/{id_anio}/{id_gir}/{id_cic}", method = RequestMethod.GET)
	public AjaxResponseBody crearCursosGoogleClassroom(@PathVariable Integer id_anio,@PathVariable Integer id_gir, @PathVariable Integer id_cic) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			campusVirtualService.crearGruposClassroomxGiro(id_anio, id_gir, id_cic);
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/enrolarAlumnosAulas/{id_anio}/{id_cic}", method = RequestMethod.GET)
	public AjaxResponseBody enrolarAlumnosAulas(@PathVariable Integer id_anio, @PathVariable Integer id_cic) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			campusVirtualService.asignacionClasexCiclo(id_anio, id_cic);
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/enrolarAlumnosAulasColegio/{id_anio}/{id_cic}", method = RequestMethod.GET)
	public AjaxResponseBody enrolarAlumnosAulasColegio(@PathVariable Integer id_anio, @PathVariable Integer id_cic) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			campusVirtualService.asignacionClasexCicloColegio(id_anio, id_cic);
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/enrolarAlumnosAulasColegiovu/{id_anio}/{id_cic}", method = RequestMethod.GET)
	public AjaxResponseBody enrolarAlumnosAulasColegioVU(@PathVariable Integer id_anio, @PathVariable Integer id_cic) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			campusVirtualService.asignacionClasexCicloColegioVU(id_anio, id_cic);
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/generarCursoAula/{id_cic}", method = RequestMethod.GET)
	public AjaxResponseBody generarCursoAula(@PathVariable Integer id_cic) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			campusVirtualService.generarCursoAula(id_cic);
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/generarUsuarioGooglexMatricula/{id_alu}/{id_mat}/{id_anio}", method = RequestMethod.POST)
	public AjaxResponseBody generarUsuarioGooglexMatricula(@PathVariable Integer id_alu,@PathVariable Integer id_mat, @PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			campusVirtualService.generarUsuarioGooglexMatricula(id_alu, id_mat, id_anio);;
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/generarUsuarioGooglexMatriculaColegio/{id_alu}/{id_mat}/{id_anio}", method = RequestMethod.POST)
	public AjaxResponseBody generarUsuarioGooglexMatriculaxColegio(@PathVariable Integer id_alu,@PathVariable Integer id_mat, @PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			campusVirtualService.generarUsuarioGooglexMatriculaColegio(id_alu, id_mat, id_anio);
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/desactivarCursos/{id_anio}", method = RequestMethod.POST)
	public AjaxResponseBody desactivarCursos(@PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			campusVirtualService.desactivarCursos(id_anio);
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/desactivarGruposxCiclo/{id_cic}", method = RequestMethod.POST)
	public AjaxResponseBody desactivarGruposxCiclo(@PathVariable Integer id_cic) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			campusVirtualService.desactivarCursosxCiclo(id_cic);;
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/desactivarAulasxCiclo/{id_cic}", method = RequestMethod.POST)
	public AjaxResponseBody desactivarAulasxCiclo(@PathVariable Integer id_cic) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			campusVirtualService.desactivarAulasxCiclo(id_cic);
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
		
	
	@RequestMapping( value="/desactivarCuentasxCiclo/{id_cic}", method = RequestMethod.POST)
	public AjaxResponseBody desactivarCuentasxCiclo(@PathVariable Integer id_cic) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			campusVirtualService.desacticarCuentasxCiclo(id_cic);
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/listaMatriculasValidasNoValidadas", method = RequestMethod.GET)
	public AjaxResponseBody listaMatriculaValidadNoValidas(Integer id_anio,Integer id_gir, Integer id_tip) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(matriculaDAO.listaMatriculasValidasNoValidadas(id_anio, id_gir, id_tip));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	
	@RequestMapping( value="/validarObservacionesMatricula/{id_alu}", method = RequestMethod.GET)
	public AjaxResponseBody validarObservacionesMatricula(@PathVariable Integer id_alu) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
				//Pra el 2021 no valida condicion
				List<CondicionBean> condiciones = condicionService.mensajeCondicionalumno(id_alu);
				int nroMatriculasCondiconales = 0;
				String matriculaCondicionada ="";
				if(condiciones!=null)
				for (CondicionBean condicionBean : condiciones) {
					
					if (condicionBean.getTipo().equals("M")){
						nroMatriculasCondiconales ++;
						if (matriculaCondicionada.length()==0)
							matriculaCondicionada = matriculaCondicionada  + condicionBean.getObs();
						else 
							matriculaCondicionada = matriculaCondicionada  + ", " + condicionBean.getObs();
						
					}
					
					if (condicionBean.getTipo().equals("B")){
						result.setCode("201");
						result.setMsg("ATENCION: " + condicionBean.getObs());
						
						return result;
					}
					
					if (condicionBean.getTipo().equals("V")){
						result.setCode("201");
						result.setMsg("ATENCION,\n El alumno no puede matricularse, pierde vacante - MOTIVO: " + condicionBean.getObs());
						
						return result;
					}
					
					
				}
				
				if (nroMatriculasCondiconales >1){
					//result.setCode("201");
					//result.setMsg("ATENCION,\nEl alumno tiene dos matriculas condicionadas:" + matriculaCondicionada);
					result.setCode("201");
					result.setMsg("ATENCION,\nEl alumno tiene dos matriculas condicionadas:" + matriculaCondicionada);
					
					return result;
				}

				if (nroMatriculasCondiconales ==1){
					//result.setCode("418");//warning
					result.setCode("418");
					result.setMsg("ATENCION,\nEl alumno puede matricularse pero tiene matricula condicionada:" + matriculaCondicionada);
					//map.put("condicionada", "ATENCION,\nEl alumno puede matricularse pero tiene matricula condicionada:" + matriculaCondicionada);
					
//					return result;
				}

		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	
	
	
	
	/**
	 * Procesar el archivo excel de la lectora
	 * @param uploadfile
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/subirContrato")
	public AjaxResponseBody uploadFile(@RequestParam("file") MultipartFile uploadfile,Integer id_mat) {

		AjaxResponseBody result = new AjaxResponseBody();

		if (uploadfile.isEmpty()) {
			result.setCode("ARCHIVO");
			result.setMsg("archivo es vacio");
			return result;
		}

		try {

			
			InputStream is = uploadfile.getInputStream();
			
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
             File destino = new File("C:\\contratos\\");

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
             }


			//resultadosDAO.calcularResultados(id_oli);
			//List<Row> listaCargada= procesaExcel(is,id_oli);
			
			//result.setResult(listaCargada);
			return result;
			
		} catch (Exception e) {
			result.setCode("ARCHIVO");
			result.setMsg("archivo con errores:" + e.getMessage());
			logger.error("metodo:uploadFile",e);
			return result;
		}

	}
	
	@RequestMapping( value="/activarCuentasGooglexUusuario/{usuario}", method = RequestMethod.POST)
	public AjaxResponseBody activarCuentasGooglexUusuario(@PathVariable String usuario) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			campusVirtualService.activarCuentaGoogleUsr(usuario);
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/inactivarMatricula/{id_mat}", method = RequestMethod.POST)
	public AjaxResponseBody inactivarMatricula(@PathVariable Integer id_mat) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			matriculaDAO.inactivarMatricula(id_mat);
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	
	@RequestMapping( value="/validarFechaRatificacion", method = RequestMethod.GET)
	public AjaxResponseBody validarFechaRatificacion() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//Boolean existe = matriculaDAO.validarFechaRatificacion(id_anio);
			List<Row> ratificacion = matriculaDAO.validarFechaRatificacion();
			if(ratificacion.size()>0) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("existe", 1);
				map.put("id_anio_rat", ratificacion.get(0).getInteger("id_anio_rat"));
				Anio anio = anioDAO.get(ratificacion.get(0).getInteger("id_anio_rat"));
				map.put("anio_rat", anio.getNom());
				map.put("id_anio_mat", ratificacion.get(0).getInteger("id_anio"));
				result.setResult(map);
				return result;		
			} else {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("existe", 0);
				map.put("id_anio_rat", null);
				map.put("anio_rat", null);
				map.put("id_anio_mat", null);
				result.setResult(map);
				return result;	
			}			
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/validarFechaMatriculas/{id_anio}", method = RequestMethod.GET)
	public AjaxResponseBody validarFechaMatriculas(@PathVariable Integer id_anio) {
		//ALUMNOS ANTIGUOS - CON CRONOGRAMA
		boolean antiguo_con_cronograma = cronogramaDAO.alumnosAntiguosTienenVigente(id_anio);

		//ALUMNOS ANTIGUOS - SIN CRONOGRAMA
		boolean antiguo_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "AS");

				
		//ALUMNOS NUEVOS SIN CRONOGRAMA
		boolean nuevos_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "NS");
		
		//ALUMNOS NUEVOS CON CRONOGRAMA 
		boolean nuevos_cronograma = confFechasDAO.cronogramaVigente(id_anio, "NC");
		AjaxResponseBody result = new AjaxResponseBody();
		if(antiguo_con_cronograma || antiguo_sin_cronograma || nuevos_sin_cronograma || nuevos_cronograma) {
			result.setResult(1);
		} else {
			result.setResult(0);
		}
		
		return result;
		
	}
	
	
	@RequestMapping( value="/validarRatificacionFamilia/{id_anio}/{id_usr}", method = RequestMethod.GET)
	public AjaxResponseBody validarRatificacionFamilia(@PathVariable Integer id_anio, @PathVariable Integer id_usr) {
		AjaxResponseBody result = new AjaxResponseBody();
		try {
			Integer id_anio_ant= id_anio-1;
			List<Row> lista_matriculas = matriculaDAO.cantidadMatriculas(id_anio_ant, id_usr);
			List<Row> lista_matr_ratificaron = matriculaDAO.cantidadMatriculasRatificadas(id_anio_ant, id_usr, id_anio);
			Integer cant_mat= lista_matriculas.size();
			Integer cant_rat = lista_matr_ratificaron.size();
			if(cant_mat.equals(cant_rat)) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("continua", 1);
				map.put("id_anio_rat", id_anio);
				result.setResult(map);
				return result;	
			} else {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("continua", 0);
				map.put("id_anio_rat", id_anio);
				result.setResult(map);
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
			result.setException(e);
		}
		
		return result;		
	}
	
	@RequestMapping( value="/validarHijosNoRat/{id_anio}/{id_usr}", method = RequestMethod.GET)
	public AjaxResponseBody validarHijosNoRat(@PathVariable Integer id_anio, @PathVariable Integer id_usr) {
		AjaxResponseBody result = new AjaxResponseBody();
		try {
			Integer id_anio_ant= id_anio-1;
			Anio anio =anioDAO.get(id_anio);
			List<Row> lista_matr_no_ratificaron = matriculaDAO.hijosNoRatificados(id_anio_ant, id_usr, id_anio);
			List<Row> lista_matriculas = matriculaDAO.cantidadMatriculas(id_anio_ant, id_usr);
			String msj="";
			if(lista_matr_no_ratificaron.size()>0) {
				//int cont=1;
				for (Row row : lista_matr_no_ratificaron) {
					//if(cont==lista_matr_no_ratificaron.size())
					// msj = msj + row.getString("alumno")+ ". ";	
					//else
						msj = msj + row.getString("alumno")+ ", ";	
					//cont ++;
				}
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("cantidad", lista_matr_no_ratificaron.size());
				map.put("cantidad_hijos", lista_matriculas.size());
				map.put("mensaje", "En el proceso de ratificación, indicó que "+msj+" no continuará(an) estudios en el año "+anio.getNom()+".");
				result.setResult(map);
				return result;	
			} else {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("cantidad", 0);
				map.put("cantidad_hijos", lista_matriculas.size());
				map.put("mensaje", null);
				result.setResult(map);
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
			result.setException(e);
		}
		
		return result;		
	}
	
	
	
	/*@RequestMapping(value = "/eliminarMatriculasNoValidadas", method = RequestMethod.GET)
	public AjaxResponseBody obtenerpagosMatricula(Integer id_mat) {
		AjaxResponseBody result = new AjaxResponseBody();
		List<AcademicoPago> listaPagosMatricula = academicoPagoDAO.listByParams(new Param("id_mat",id_mat),new String[]{"nro_cuota"});
		Integer nro_cuo_total=0;
		for (AcademicoPago academicoPago : listaPagosMatricula) {
			Integer nro_cuo_pag=academicoPago.getNro_cuota();
			nro_cuo_total = nro_cuo_total + nro_cuo_pag;
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("nro_cuo_total", nro_cuo_total);
		map.put("listaPagos", listaPagosMatricula);
		result.setResult(map);
		return result;
	 
	}*/
	
}

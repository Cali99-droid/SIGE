package com.sige.mat.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.util.DocxUtil;
import com.tesla.frmk.util.FechaUtil;
import com.tesla.frmk.util.JsonUtil;
import com.sige.common.enums.EnumConceptoPago;
import com.sige.common.enums.EnumFormaPago;
import com.sige.mat.dao.AlumnoDAO;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.AulaDAO;
import com.sige.mat.dao.ConfCuotaDAO;
import com.sige.mat.dao.ConfFechasDAO;
import com.sige.mat.dao.CronogramaDAO;
import com.sige.mat.dao.EvaluacionDAO;
import com.sige.mat.dao.EvaluacionVacDAO;
import com.sige.mat.dao.GradDAO;
import com.sige.mat.dao.GruFamAlumnoDAO;
import com.sige.mat.dao.GruFamDAO;
import com.sige.mat.dao.GruFamFamiliarDAO;
import com.sige.mat.dao.MatrVacanteDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.MovimientoDAO;
import com.sige.mat.dao.MovimientoDetalleDAO;
import com.sige.mat.dao.ParametroDAO;
import com.sige.mat.dao.PeriodoDAO;
import com.sige.mat.dao.PersonaDAO;
import com.sige.mat.dao.ReservaCuotaDAO;
import com.sige.mat.dao.ReservaDAO;
import com.sige.mat.dao.ServicioDAO;
import com.sige.mat.dao.SolicitudDAO;
import com.sige.spring.service.CondicionService;
import com.sige.spring.service.FacturacionService;
import com.sige.spring.service.PagosService;
import com.sige.spring.service.SolicitudService;
import com.sige.spring.service.VacanteService;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.Cliente;
import com.tesla.colegio.model.CondMatricula;
import com.tesla.colegio.model.ConfCuota;
import com.tesla.colegio.model.EvaluacionVac;
import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.GruFam;
import com.tesla.colegio.model.GruFamAlumno;
import com.tesla.colegio.model.GruFamFamiliar;
import com.tesla.colegio.model.MatrVacante;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Movimiento;
import com.tesla.colegio.model.MovimientoDetalle;
import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.Parametro;
import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.Persona;
import com.tesla.colegio.model.Reserva;
import com.tesla.colegio.model.ReservaCuota;
import com.tesla.colegio.model.Servicio;
import com.tesla.colegio.model.Solicitud;
import com.tesla.colegio.model.Usuario;
import com.tesla.colegio.model.bean.CondicionBean;
import com.tesla.colegio.util.Constante;

@RestController
@RequestMapping(value = "/api/reserva")
public class ReservaRestController {

	@Autowired
	private ReservaDAO reservaDAO;

	@Autowired
	private FacturacionService facturacionService;

	@Autowired
	private MovimientoDAO movimientoDAO;

	@Autowired
	private MovimientoDetalleDAO movimientoDetalleDAO;

	@Autowired
	private MatrVacanteDAO matrVacanteDAO;

	@Autowired
	private ParametroDAO parametroDAO;

	@Autowired
	private MatriculaDAO matriculaDAO;

	@Autowired
	private ServicioDAO servicioDAO;

	@Autowired
	private PeriodoDAO periodoDAO;

	@Autowired
	private EvaluacionVacDAO evaluacionVacDAO;

	@Autowired
	private ConfCuotaDAO confCuotaDAO;

	@Autowired
	private ReservaCuotaDAO reservaCuotaDAO;

	@Autowired
	private AlumnoDAO alumnoDAO;

	@Autowired
	private GradDAO gradDAO;

	@Autowired
	private AulaDAO aulaDAO;

	@Autowired
	private GruFamAlumnoDAO gruFamAlumnoDAO;

	@Autowired
	private GruFamFamiliarDAO gruFamFamiliarDAO;

	@Autowired
	private VacanteService vacanteService;

	@Autowired
	private HttpSession httpSession;

	@Autowired
	private CronogramaDAO cronogramaDAO;

	@Autowired
	private ConfFechasDAO confFechasDAO;

	@Autowired
	private CondicionService condicionService;
	
	@Autowired
	private PagosService pagosService;

	@Autowired
	private SolicitudDAO solicitudDAO;
	
	@Autowired
	private SolicitudService solicitudService;
	
	@Autowired
	private PersonaDAO personaDAO;
	
	@Autowired
	private AnioDAO anioDAO;
	
	@Autowired
	private GruFamDAO gruFamDAO;
	
	
	@RequestMapping(value = "/listar")
	public AjaxResponseBody buscarAlumnosReserva(Integer id_anio, Integer id_suc, String nomApeAlumno) {

		AjaxResponseBody result = new AjaxResponseBody();

		Integer id_anio_ant = id_anio - 1;// TODO MEJORAR EL A�O ANTERIOR, Ver el caso de un alumno q estudia el a�o anterior(antiguo) luego se traslada y postula nuevamente

		// ALUMNOS ANTIGUOS - CON CRONOGRAMA
		boolean antiguo_con_cronograma = cronogramaDAO.alumnosAntiguosTienenVigente(id_anio);

		// ALUMNOS ANTIGUOS - SIN CRONOGRAMA
		boolean antiguo_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "AS");
		
		// ALUMNOS NU7EVOS - CON CRONOGRAMA
		boolean nuevo_con_cronograma = confFechasDAO.cronogramaVigente(id_anio, "NC");
		
		// ALUMNOS NU7EVOS -SIN CRONOGRAMA
		boolean nuevo_sin_cronograma =  confFechasDAO.cronogramaVigente(id_anio, "NS");
				
		if (antiguo_con_cronograma) {
			List<Map<String, Object>> alumnoList = matrVacanteDAO.listAptosParaReserva(id_anio, id_anio_ant,
					nomApeAlumno, id_suc.toString(), "AC");
			result.setResult(alumnoList);
		} else if (antiguo_sin_cronograma && !nuevo_sin_cronograma) {
			List<Map<String, Object>> alumnoList = matrVacanteDAO.listAptosParaReserva(id_anio, id_anio_ant,
					nomApeAlumno, id_suc.toString(), "AS");
			result.setResult(alumnoList);
		}else if (antiguo_sin_cronograma && nuevo_sin_cronograma) {
			List<Map<String, Object>> alumnoList = matrVacanteDAO.listAptosParaReserva(id_anio, id_anio_ant,
					nomApeAlumno, id_suc.toString(), "ASNS");
			result.setResult(alumnoList);
		}else if (nuevo_con_cronograma) {
			List<Map<String, Object>> alumnoList = matrVacanteDAO.listAptosParaReserva(id_anio, id_anio_ant,
					nomApeAlumno, id_suc.toString(), "NC");
			result.setResult(alumnoList);
		}else if (nuevo_sin_cronograma) {
			List<Map<String, Object>> alumnoList = matrVacanteDAO.listAptosParaReserva(id_anio, id_anio_ant,
					nomApeAlumno, id_suc.toString(), "NS");
			result.setResult(alumnoList);
		} else {
			List<Map<String, Object>> alumnoList = matrVacanteDAO.listAptosParaReserva(id_anio, id_anio_ant,
					nomApeAlumno, id_suc.toString(), "NS");
			result.setResult(alumnoList);
		}

		return result;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id) {

		AjaxResponseBody result = new AjaxResponseBody();

		try {
			result.setResult(reservaDAO.get(id));
		} catch (Exception e) {
			result.setException(e);
		}

		return result;

	}

	@RequestMapping(value = "/editar", method = RequestMethod.GET)
	public AjaxResponseBody editar(Integer id_vac, Integer id_alu, Integer id_res, Integer id_suc, Integer id_anio) throws Exception {

		AjaxResponseBody result = new AjaxResponseBody();
		
		BigDecimal deuda = pagosService.pagosPendientes(id_alu, id_anio);
		if (deuda.compareTo(BigDecimal.ZERO)>0) {
			result.setCode("500");
			result.setMsg("¡ATENCIÓN!,\n Primero debe cancelar las mensualidades vencidas.\nDeuda de la familia S/ " + deuda);
			return result;
		}

		Integer id_anio_ant = id_anio - 1;
		Integer condicion_alumno = Constante.CONDICION_MATRICULA_INGRESANTE;

		Matricula alu_antiguo = null;
		MatrVacante alu_nuevo = null;
		if (id_vac != null && id_alu != null) {
			Param param = new Param();
			param.put("id_alu", id_alu);
			param.put("id", id_vac);
			alu_antiguo = matriculaDAO.getByParams(param);
			alu_nuevo = matrVacanteDAO.getByParams(param);
		}

		Reserva reserva = null;

		if (id_res != null) {
			reserva = reservaDAO.getFull(id_res, new String[] { ReservaCuota.TABLA });
		}

		// Datos del grado, nivel, periodo que postulo si es alumno nuevo y
		// grado al que pasa si es antiguo
		Integer id_gra = 0;
		Integer id_niv = 0;
		Periodo periodo = null;
		Servicio servicio = null;
		Periodo periodo_es = null;
		Integer id_au_nue = null;
		// ALUMNO ANTIGUO
		if (alu_antiguo != null) {
			Grad grado = gradDAO.get(alu_antiguo.getId_gra());
			id_gra = gradDAO.getByParams(new Param("id_gra_ant", grado.getId())).getId();
			id_niv = gradDAO.getByParams(new Param("id", id_gra)).getId_nvl();

			Param param = new Param();
			param.put("id_niv", id_niv);
			param.put("id_suc", id_suc);

			servicio = servicioDAO.getByParams(param);
			Param param2 = new Param();
			param2.put("id_srv", servicio.getId());
			param2.put("id_tpe", Constante.CONDICION_ESCOLAR);
			param2.put("id_anio", id_anio);
			periodo_es = periodoDAO.getByParams(param2);

			Matricula mat_ant = matriculaDAO.getMatriculaAnteriorParaReserva(id_alu, id_anio_ant);
			id_au_nue = mat_ant.getId_au_nue();
			// id_au_nue=matriculaDAO.getMatriculaAnterior(id_alu,
			// id_anio).getId_au_nue();

			//////// cargarListas(model, id_gra, id_alu, periodo_es.getId());
		} else if (alu_nuevo != null) {
			Grad grado = gradDAO.get(alu_nuevo.getId_gra());
			id_gra = grado.getId();
			id_niv = grado.getId_nvl();
			Param param = new Param();

			EvaluacionVac evaluacionVac = evaluacionVacDAO.getByParams(new Param("id", alu_nuevo.getId_eva()));

			periodo = periodoDAO.getByParams(new Param("id", evaluacionVac.getId_per()));
			param.put("id_srv", periodo.getId_srv());
			param.put("id_tpe", Constante.CONDICION_ESCOLAR);
			param.put("id_anio", id_anio);
			periodo_es = periodoDAO.getByParams(param);
			//////// cargarListas(model, id_gra, id_alu, periodo_es.getId());
		}

		ConfCuota cuota = confCuotaDAO.getByParams(new Param("id_per", periodo_es.getId()));

		if (cuota == null) {
			reserva = new Reserva();
			reserva.setId_gra(id_gra);
			reserva.setId_niv(id_niv);

			result.setCode("201");
			result.setMsg("Falta configurar el monto de reserva para el periodo");
			return result;
		}

		// Datos del alumno
		Alumno alumno = alumnoDAO.get(id_alu);
		Persona persona= personaDAO.get(alumno.getId_per());
		alumno.setApe_pat(persona.getApe_pat());
		alumno.setApe_mat(persona.getApe_mat());
		alumno.setNom(persona.getNom());
		if (reserva == null) {

			reserva = new Reserva();
			reserva.setId_con(condicion_alumno);
			reserva.setFec(new Date());// Por defecto fecha de reserva
			reserva.setId_gra(id_gra);
			reserva.setId_niv(id_niv);
			reserva.setId_alu(id_alu);
			reserva.setId_per(periodo_es.getId());
			reserva.setFec_lim(periodo_es.getFec_cie_mat());// FECHA SUGERIDA DE
		
		/*	//revisa
			try {
				String sDate1="2020-03-15";  
				Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
				reserva.setFec_lim(date1);// FECHA SUGERIDA DE
				
			} catch (ParseException e) {
				
				reserva.setFec_lim(new Date());// FECHA SUGERIDA DE
				
			} */
			    
															
			reserva.setId_con(Constante.CONDICION_MATRICULA_INGRESANTE);
			reserva.getReservaCuota().setMonto(cuota.getReserva());
			if (condicion_alumno.equals(Constante.CONDICION_MATRICULA_INGRESANTE))
				reserva.setId_cli(Constante.CLIENTE_NUEVO);
			if (id_au_nue != null) {
				reserva.setId_au(id_au_nue);
			}
		}

		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("alumno", alumno);
		map.put("reserva", reserva);
		
	
		
		//ALUMNOS ANTIGUOS - CON CRONOGRAMA
			boolean antiguo_con_cronograma = cronogramaDAO.alumnosAntiguosTienenVigente(id_anio);

		//ALUMNOS ANTIGUOS - SIN CRONOGRAMA
			boolean antiguo_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "AS");

						
		//ALUMNOS NUEVOS SIN CRONOGRAMA
		  boolean nuevos_cronograma = confFechasDAO.cronogramaVigente(id_anio, "NS");
		  
		  if (antiguo_con_cronograma){
			  map.put("cronograma", "AC"); 
		  } else if(antiguo_sin_cronograma){
			  
			  map.put("cronograma", "AS");
			  
			//si tiene solicitud de cambio de local
			Param param = new Param();
			param.put("id_alu", id_alu);
			param.put("id_anio", id_anio);
			param.put("est", "A");
			Solicitud solicitudCambioLocal = solicitudDAO.getByParams(param);
			
			if (solicitudCambioLocal!=null)
				map.put("solicitud_cambio_local", "1"); 

			
		  } else if(nuevos_cronograma){
			  map.put("cronograma", "NC");
		  }
		
		
		result.setResult(map);

		return result;
	}

	@Transactional
	@RequestMapping(value = "/grabar", method = RequestMethod.POST)
	public AjaxResponseBody grabar(Reserva reserva) throws Exception {

		AjaxResponseBody result = new AjaxResponseBody();

		/* No funciona a partir del 2022
		if (reserva.getId() != null) {
			Aula aula = aulaDAO.get(reserva.getId_au());
			reserva.setId_per(aula.getId_per());
		}*/
		
		Integer id_res = reservaDAO.saveOrUpdate(reserva);
		Integer id_fmo = null;
		String nro_rec = null;
		// se graba por primera vez la el movimiento y la cuouta
		if (reserva.getId() == null) {
			// movimiento
			Periodo periodo = periodoDAO.getFull(reserva.getId_per(), new String[] { "ges_servicio" });
			Alumno alumno = alumnoDAO.get(reserva.getId_alu());
			Persona persona = personaDAO.get(alumno.getId_per());
			nro_rec = facturacionService.getNroRecibo(periodo.getId_suc());
			
			//ConfCuota cuota = confCuotaDAO.getByParams(new Param("id_per", reserva.getId_per())); // Por ahora comentado funcionaba hasta el 2022 cuando no habia modalidad ni turno
			ConfCuota cuota = confCuotaDAO.getByParams(new Param("id_per", reserva.getId_per()));
			/*Param param_cuota = new Param();
			param_cuota.put("id_per", reserva.getId_per());
			param_cuota.put("id_cct", ciclo_turno.getId());
			param_cuota.put("id_cme", id_cme[i]);
			ConfCuota confCuota = confCuotaDAO.getByParams(param_cuota);*/
			//BigDecimal monto_reserva=new BigDecimal(50);
			BigDecimal monto_reserva=cuota.getReserva();
		//	Aula aula = aulaDAO.get(reserva.getId_au()); No funciona desde el 2022
			Grad grado = gradDAO.get(reserva.getId_gra());

			Movimiento movimiento = new Movimiento();
			movimiento.setDescuento(BigDecimal.ZERO);
			movimiento.setEst("A");
			movimiento.setFec(new Date());
			movimiento.setId_fam(reserva.getId_fam());
			movimiento.setId_fpa(EnumFormaPago.EFECTIVO.getValue());// efectivo
			movimiento.setId_suc(periodo.getId_suc());
			//movimiento.setMonto(cuota.getReserva());
			//movimiento.setMonto_total(cuota.getReserva());
			movimiento.setMonto(monto_reserva);
			movimiento.setMonto_total(monto_reserva);
			movimiento.setNro_rec(nro_rec);
			movimiento.setObs("RESERVA DE MATRICULA");
			movimiento.setTipo("I");
 
			id_fmo = movimientoDAO.saveOrUpdate(movimiento);
			MovimientoDetalle movimientoDetalle = new MovimientoDetalle();
			movimientoDetalle.setDescuento(BigDecimal.ZERO);
			movimientoDetalle.setId_fco(EnumConceptoPago.RESERVA_MATRICULA.getValue());
			movimientoDetalle.setEst("A");
 			movimientoDetalle.setId_fmo(id_fmo);
			//movimientoDetalle.setMonto(cuota.getReserva());
			//movimientoDetalle.setMonto_total(cuota.getReserva());
			movimientoDetalle.setMonto(monto_reserva);
			movimientoDetalle.setMonto_total(monto_reserva);
			//No grabamos el aula desde el 2022
			/*movimientoDetalle.setObs("RESERVA DE MATRICULA: " + persona.getApe_pat() + " " + persona.getApe_mat() + ", "
					+ persona.getNom() + " " + periodo.getServicio().getNom().substring(0, 4) + " " + grado.getNom()
					+ " " + aula.getSecc() + ", VIGENCIA HASTA EL: " + FechaUtil.toString(reserva.getFec_lim()));*/
			movimientoDetalle.setObs("RESERVA DE MATRICULA: " + persona.getApe_pat() + " " + persona.getApe_mat() + ", "
					+ persona.getNom() + " " + periodo.getServicio().getNom().substring(0, 4) + " " + grado.getNom()
					+ ", VIGENCIA HASTA EL: " + FechaUtil.toString(reserva.getFec_lim()));
			movimientoDetalleDAO.saveOrUpdate(movimientoDetalle);

			// cuota
			ReservaCuota reservaCuota = new ReservaCuota();
			reservaCuota.setEst("A");
 			reservaCuota.setMonto(monto_reserva);
 			//reservaCuota.setMonto(cuota.getReserva());
			reservaCuota.setNro_recibo(nro_rec);
			reservaCuota.setId_res(id_res);
			reservaCuota.setId_fmo(id_fmo);
			reservaCuotaDAO.saveOrUpdate(reservaCuota);

			facturacionService.updateNroRecibo(periodo.getId_suc(), nro_rec);
			
			solicitudService.desactivarSolicitudes(  reserva.getId_alu(), periodo.getId_anio());


		}
		
	
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id_res);
		map.put("id_fmo", id_fmo);//
		map.put("nro_rec", nro_rec);//

		result.setResult(map);
		return result;

	}

	@RequestMapping(value = "/imprimir/{id}")
	@ResponseBody
	public void reservaImprimir(HttpServletResponse response, @PathVariable Integer id) throws Exception {

		Parametro parametro = parametroDAO.getByParams(new Param("nom", "RUTA_PLANTILLA"));

		Reserva reserva = reservaDAO.getFull(id, new String[] { Alumno.TABLA, Aula.TABLA, Grad.TABLA, Nivel.TABLA,
				CondMatricula.TABLA, Cliente.TABLA, Periodo.TABLA, Familiar.TABLA });

		File file = null;
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;

		Map<String, String> map = new HashMap<String, String>();
		map.put("ANIO", reserva.getPeriodo().getAnio().getNom());
		String apoderado = reserva.getFamiliar().getNom() + " " + reserva.getFamiliar().getApe_pat() + " "
				+ reserva.getFamiliar().getApe_mat();
		map.put("APODERADO", apoderado.toUpperCase());
		map.put("NIVEL", reserva.getNivel().getNom());
		String alumno = reserva.getAlumno().getNom() + " " + reserva.getAlumno().getApe_pat() + " "
				+ reserva.getAlumno().getApe_mat();
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

	@RequestMapping(value = "/listarReserva", method = RequestMethod.GET)
	public AjaxResponseBody listarGrados(Integer id_au, String mat, Integer id_niv, Integer id_gra, Integer id_suc, String id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();

		try {
			result.setResult(reservaDAO.listarReserva(id_au, mat,id_niv, id_gra, id_suc, id_anio));
		} catch (Exception e) {
			result.setException(e);
		}

		return result;

	}

	@RequestMapping(value = "/obtenerNroVac", method = RequestMethod.GET)
	public AjaxResponseBody obtenerNroVac(Integer id_au, Integer id_anio, Integer id_alu) {

		AjaxResponseBody result = new AjaxResponseBody();

		try {
			Integer nro_vac = vacanteService.getNroVacantesReserva(id_au, id_anio, id_alu);
			Integer capacidad = vacanteService.getCapacidadxAula(id_au);
			Param param = new Param();
			param.put("nro_vac", nro_vac);
			param.put("capacidad", capacidad);
			result.setResult(param);
		} catch (Exception e) {
			result.setException(e);
		}

		return result;

	}
	
	
	@RequestMapping(value = "/obtenerNroVacxGrado", method = RequestMethod.GET)
	public AjaxResponseBody obtenerNroVacxGrado(Integer id_anio, Integer id_alu, Integer id_suc, Integer id_grad) {

		AjaxResponseBody result = new AjaxResponseBody();

		try {
			Anio anio= anioDAO.get(id_anio);
			Integer nro_vac = vacanteService.getNroVacantesReservaxGrado(id_anio, id_alu, id_grad, id_suc);
			Integer capacidad = vacanteService.getCapacidadxGrado(Integer.parseInt(anio.getNom()), id_grad, id_suc);
			Param param = new Param();
			param.put("nro_vac", nro_vac);
			param.put("capacidad", capacidad);
			result.setResult(param);
		} catch (Exception e) {
			result.setException(e);
		}

		return result;

	}

	@RequestMapping(value = "/validarCondicion", method = RequestMethod.GET)
	public AjaxResponseBody validarCondicion(Integer id_alu) {
		AjaxResponseBody result = new AjaxResponseBody();

		List<CondicionBean> condiciones = condicionService.mensajeCondicionalumno(id_alu);
		int nroMatriculasCondiconales = 0;
		String matriculaCondicionada = "";
		if (condiciones != null)
			for (CondicionBean condicionBean : condiciones) {

				if (condicionBean.getTipo().equals("M")) {
					nroMatriculasCondiconales++;
					if (matriculaCondicionada.length() == 0)
						matriculaCondicionada = matriculaCondicionada + condicionBean.getObs();
					else
						matriculaCondicionada = matriculaCondicionada + ", " + condicionBean.getObs();

				}

				if (condicionBean.getTipo().equals("B")) {
					result.setCode("201");
					result.setMsg(
							"Coordinar con el Jefe de Normas para completar la Matr�cula: " + condicionBean.getObs());

					return result;
				}

				if (condicionBean.getTipo().equals("V")) {
					result.setCode("201");
					result.setMsg("El alumno pierde vacante:" + condicionBean.getObs());

					return result;
				}

			}

		if (nroMatriculasCondiconales > 1) {
			result.setCode("201");
			result.setMsg("El alumno tiene dos matriculas condicionadas:" + matriculaCondicionada);

			return result;
		}

		if (nroMatriculasCondiconales == 1) {
			// result.setCode("418");//warning
			result.setResult("El alumno puede matricularse pero tiene matricula condicionada:" + matriculaCondicionada);

		}

		return result;

	}
	
	@RequestMapping(value = "/validarReservaVigente/{id_usr}", method = RequestMethod.GET)
	public AjaxResponseBody validarReservaVigente(@PathVariable Integer id_usr) {
		AjaxResponseBody result = new AjaxResponseBody();
		//Sacar el grupo familiar
		GruFam gru_fam = gruFamDAO.getByParams(new Param("id_usr",id_usr));
		//Lista de Hijos del grupo familiar
		List<GruFamAlumno> grupo_alu = gruFamAlumnoDAO.listByParams(new Param("id_gpf",gru_fam.getId()),null);
		Boolean tieneReserva=false;
		if(grupo_alu.size()>0) {
			for (GruFamAlumno gruFamAlumno : grupo_alu) {
				Integer id_alu=gruFamAlumno.getId_alu();
				// Revisar si tiene reserva
				Param param = new Param();
				param.put("id_alu", id_alu);
				List<Reserva> reservas = reservaDAO.listFullByParams(param, new String[]{"mat_res.id desc"});
				Boolean tieneReserva_alu=false;
				if (reservas.size() > 0) {
					for (Reserva reserva : reservas) {
						if (new Date().before(reserva.getFec_lim()) || ((new SimpleDateFormat("yyyy-MM-dd").format(new Date())).equals(new SimpleDateFormat("yyyy-MM-dd").format(reserva.getFec_lim())))){
							tieneReserva_alu = true;
							break;	
						}
					}
					if(tieneReserva_alu) {
						tieneReserva=true;
						break;
					}
				}
			}
		}
		
		
		if(tieneReserva) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("continua", 1);
			//map.put("id_anio_rat", id_anio);
			result.setResult(map);
			return result;	
		} else {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("continua", 0);
			//map.put("id_anio_rat", id_anio);
			result.setResult(map);
			return result;	
		}
		
		
	}
}

package com.sige.mat.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder; 
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sige.common.enums.EnumModulo;
import com.sige.common.enums.EnumParametro;
import com.sige.core.dao.cache.CacheManager;
import com.sige.invoice.bean.Impresion;
import com.sige.invoice.bean.ImpresionDcto;
import com.sige.invoice.bean.ImpresionItem;
import com.sige.rest.request.PagoRequest;
import com.sige.spring.service.FacturacionService;
import com.sige.spring.service.ModuloService;
import com.sige.spring.service.PagosService;
import com.tesla.colegio.model.AcademicoPago;
import com.tesla.colegio.model.AcapagBeca;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.Beca;
import com.tesla.colegio.model.Ciclo;
import com.tesla.colegio.model.ConfMensualidad;
import com.tesla.colegio.model.DescHno;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Parametro;
import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.SituacionMat;
import com.tesla.colegio.model.Solicitud;
import com.tesla.colegio.model.TipoDescuento;
import com.tesla.colegio.model.TrasladoDetalle;
import com.tesla.colegio.model.Usuario;
import com.tesla.colegio.model.bean.FamiliarDeudaBean;
import com.sige.mat.dao.AcademicoPagoDAO;
import com.sige.mat.dao.AcapagBecaDAO;
import com.sige.mat.dao.AlumnoDescuentoDAO;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.AulaDAO;
import com.sige.mat.dao.BecaDAO;
import com.sige.mat.dao.CicloDAO;
import com.sige.mat.dao.ConfMensualidadDAO;
import com.sige.mat.dao.DescHnoDAO;
import com.sige.mat.dao.FamiliarDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.MovimientoDAO;
import com.sige.mat.dao.MovimientoDetalleDAO;
import com.sige.mat.dao.ParametroDAO;
import com.sige.mat.dao.PeriodoDAO;
import com.sige.mat.dao.SituacionMatDAO;
import com.sige.mat.dao.SolicitudDAO;
import com.sige.mat.dao.SucursalDAO;
import com.sige.mat.dao.TarifasEmergenciaDAO;
import com.sige.mat.dao.TipoDescuentoDAO;
import com.sige.mat.dao.TrasladoDetalleDAO;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.rest.util.AjaxResponseBody;

import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.DocxUtil;
import com.tesla.frmk.util.ExcelXlsUtil;
import com.tesla.frmk.util.FechaUtil;
import com.tesla.frmk.util.NumberUtil;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@RestController
@RequestMapping(value = "/api/pagos")
public class PagosRestController {
	final static Logger logger = Logger.getLogger(PagosRestController.class);

	@Autowired
	private FacturacionService facturacionService;
	
	@Autowired
	private ModuloService moduloService;

	@Autowired
	private AcademicoPagoDAO academicoPagoDAO;

	@Autowired
	private AlumnoDescuentoDAO alumnoDescuentoDAO;

	@Autowired
	private FamiliarDAO apoderadoDAO;

	@Autowired
	private DescHnoDAO deschnoDAO;

	@Autowired
	private MatriculaDAO matriculaDAO;

	@Autowired
	private SolicitudDAO solicitudDAO;

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private ConfMensualidadDAO confMensualidadDAO;

	@Autowired
	private SituacionMatDAO situacionMatDAO;

	@Autowired
	private AnioDAO anioDAO;

	@Autowired
	private ParametroDAO parametroDAO;

	@Autowired
	private SucursalDAO sucursalDAO;

	@Autowired
	private MovimientoDAO movimientoDAO;

	@Autowired
	private MovimientoDetalleDAO movimientoDetalleDAO;
	
	@Autowired
	private TrasladoDetalleDAO trasladoDetalleDAO;
		
	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private PeriodoDAO periodoDAO;
	
	@Autowired
	private PagosService pagosService;
	
	@Autowired
	private CicloDAO cicloDAO;
	
	@Autowired
	private AcapagBecaDAO acadBecaDAO;
	
	@Autowired
	private BecaDAO becaDAO;
	
	@Autowired
	private TipoDescuentoDAO tipoDescuentoDAO;
	
	@Autowired
	private TarifasEmergenciaDAO tarifasEmergenciaDAO;
	
	@Autowired
	private AulaDAO aulaDAO;
	

	// @JsonView(Views.Public.class)
	@RequestMapping(value = "/{id_mat}")
	public AjaxResponseBody getPagosProgramados(@PathVariable Integer id_mat) {

		AjaxResponseBody result = new AjaxResponseBody();

		Param param = new Param();
		param.put("tip", "MEN");
		param.put("id_mat", id_mat);
		param.put("est", "A");
		param.put("canc", "0");

		List<AcademicoPago> pagos = academicoPagoDAO.listByParams(param, new String[] { "mens" });

		Row apoderado = apoderadoDAO.apoderadMatricula(id_mat);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("apoderado", apoderado);
		map.put("pagos", pagos);

		result.setResult(map);

		return result;

	}

	@RequestMapping(value = "/pagados/{id_mat}")
	public AjaxResponseBody getPagosPagados(@PathVariable Integer id_mat) {

		AjaxResponseBody result = new AjaxResponseBody();

		Matricula matricula = matriculaDAO.get(id_mat);
		String num_cont = matricula.getNum_cont();
		List<Matricula> hermanoList = matriculaDAO.listFullByParams(new Param("num_cont", num_cont),
				new String[] { "mat.id" });

		List<Map<String, Object>> alumnosMap = new ArrayList<Map<String, Object>>();

		for (Matricula matricula2 : hermanoList) {
			
			/*Param param = new Param();
			param.put("tip", "MEN");
			param.put("id_mat", matricula2.getId());
			param.put("est", "A");
			param.put("canc", "1");

			List<AcademicoPago> pagos = academicoPagoDAO.listByParams(param, new String[] { "mens" });
*/
			List<Row> pagos = academicoPagoDAO.obtenerPagosMensualidad(matricula2.getId());
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("alumno", matricula2.getAlumno().getApe_pat() + " " + matricula2.getAlumno().getApe_mat() + ", "
					+ matricula2.getAlumno().getNom());
			map.put("pagos", pagos);
			map.put("id_mat", matricula2.getId());
			alumnosMap.add(map);
		}

		result.setResult(alumnosMap);

		return result;

	}
	
	@RequestMapping(value = "/pagadosxAlumno/{id_alu}")
	public AjaxResponseBody getPagosPagadosxAlumno(@PathVariable Integer id_alu) {

		AjaxResponseBody result = new AjaxResponseBody();

		result.setResult(academicoPagoDAO.obtenerPagosAlumno(id_alu));

		return result;

	}
	
	@RequestMapping(value = "/ncxAlumno/{id_alu}")
	public AjaxResponseBody ncxAlumno(@PathVariable Integer id_alu) {

		AjaxResponseBody result = new AjaxResponseBody();

		result.setResult(academicoPagoDAO.obtenerNCxAlumno(id_alu));

		return result;

	}
	
	@RequestMapping(value = "/listaBecas")
	public AjaxResponseBody listaBecas() {

		AjaxResponseBody result = new AjaxResponseBody();

		result.setResult(becaDAO.listByParams(null, new String[]{ "val" }));

		return result;

	}
	
	@RequestMapping(value = "/listarSeriesBoletas")
	public AjaxResponseBody listarSeriesBoletas() {

		AjaxResponseBody result = new AjaxResponseBody();

		result.setResult(academicoPagoDAO.listarSeriesBoletas());

		return result;

	}
	
	@RequestMapping(value = "/listarSeriesNotasCredito")
	public AjaxResponseBody listarSeriesNotasCredito() {

		AjaxResponseBody result = new AjaxResponseBody();

		result.setResult(academicoPagoDAO.listarSeriesNC());

		return result;

	}

	@RequestMapping(value = "/pagosAtrasados/{id_mat}", method = RequestMethod.GET)
	public AjaxResponseBody listaPagosAtrasados(@PathVariable Integer id_mat) {

		AjaxResponseBody result = new AjaxResponseBody();

		Param param_pago = new Param();
		param_pago.put("id_mat", id_mat);
		param_pago.put("tip", "MEN");
		param_pago.put("canc", "0");
		param_pago.put("est", "A");
		List<AcademicoPago> meses_pagos = academicoPagoDAO.listByParams(param_pago, new String[] { "mens" });
		Matricula matricula = new Matricula();
		matricula.setId(id_mat);
		int anio_actual = academicoPagoDAO.obtenerAnioPeriodo(id_mat).getInteger("anio");

		Calendar cal = Calendar.getInstance();
		// int anio_actual = cal.get(Calendar.YEAR);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

		String format = formatter.format(new Date());
		int fecActual = Integer.parseInt(format);

		List<AcademicoPago> meses_atrasados = new ArrayList<>();
		
		Integer id_per=matriculaDAO.getByParams(new Param("id", id_mat)).getId_per();

		for (AcademicoPago meses_pagar : meses_pagos) {
			int fecVencimiento = getFecVencimiento(anio_actual, meses_pagar.getMens(), id_per);
			if (fecActual > fecVencimiento) {
				meses_atrasados.add(meses_pagar);
			}
		}
		result.setResult(meses_atrasados);

		return result;
	}

	/**
	 * Lista todos los pagos de los hijos del cliente, lo busca a partir del
	 * id_mat de uno de los hermanos
	 * 
	 * @param id_mat
	 * @return
	 */
	@RequestMapping(value = "/pagosPendientes/{id_suc}/{id_mat}", method = RequestMethod.GET)
	public AjaxResponseBody DescuentoxMes(@PathVariable Integer id_suc, @PathVariable Integer id_mat) {

		AjaxResponseBody result = new AjaxResponseBody();

		try {

			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

			String format = formatter.format(new Date());
			int fecActual = Integer.parseInt(format);

			// Obtenemos datos de la matricula del alumno
			Matricula matricula = matriculaDAO.get(id_mat);
			Integer id_per = matricula.getId_per();
			String num_cont = matricula.getNum_cont();

			// Obtenemos el descuento por hermano de acuerdo al periodo que
			// pertenece

			
			BigDecimal desc_hermano = confMensualidadDAO.getByParams(new Param("id_per", id_per)).getDesc_hermano();

			Integer anio_actual = anioDAO.getById_per(id_per);

			// String
			// descuento=deschnoDAO.getByParams(param_des).getMonto().toString();

			// Buscamos si el alumno tiene hermano con el mismo numero de
			// contrato
			List<Matricula> hermanoList = matriculaDAO.listFullByParams(new Param("num_cont", num_cont),
					new String[] { "mat.id" });
			Integer cant_hermanos = 0;
			for (Matricula matriculalist : hermanoList) {
				// if( !(new Integer(5)).equals(matriculalist.getId_sit() ) &&
				// !matriculalist.getId_niv().equals(Constante.NIVEL_INICIAL)
				// )//si el hermano no esta trasladado
				//SituacionMat situacionMat = situacionMatDAO.getByParams(new Param("id_mat", matriculalist.getId()));
				TrasladoDetalle situacionMat=trasladoDetalleDAO.getByParams(new Param("id_mat", matriculalist.getId()));
				Integer id_sit = null;
				if (situacionMat != null)
					id_sit = situacionMat.getId_sit();
				if (id_sit != null) {
					if ((id_sit != null && id_sit != 5) && !matriculalist.getId_niv().equals(Constante.NIVEL_INICIAL))// si
																														// el
																														// hermano
																														// no
																														// esta
																														// trasladado*/
						cant_hermanos = cant_hermanos + 1;
				} else {
					// if(
					// !matriculalist.getId_niv().equals(Constante.NIVEL_INICIAL)
					// )//si el hermano no esta trasladado*/
					cant_hermanos = cant_hermanos + 1;
				}

			}

			// descuentos por pronto pago (descuentos de secretaria)

			// Obtenemos el monto de todos los meses pendientes que deberia
			// pagar el alumno

			List<Map<String, Object>> alumnosMap = new ArrayList<Map<String, Object>>();

			Row apoderado = apoderadoDAO.apoderadMatricula(id_mat);

			for (Matricula matricula2 : hermanoList) {
				if (matricula2.getPeriodo().getId_suc().equals(id_suc)) {
					Param param = new Param();
					param.put("id_mat", matricula2.getId_alu());
					Solicitud solicitud = solicitudDAO.getByParams(param);
					Integer id_periodoCorrecto = matricula2.getId_per();
					if (solicitud != null)
						id_periodoCorrecto = matriculaDAO.periodoCambioLocal(matricula2.getId());

					ConfMensualidad confMensualidad = confMensualidadDAO
							.getByParams(new Param("id_per", id_periodoCorrecto));

					BigDecimal desc_secretaria = confMensualidad.getDescuento();// descuento
																				// por
																				// secretaria
					Integer dias_mora = confMensualidad.getDia_mora();// descuento
																		// por
																		// secretaria

					// Obtener descuento personalizado si es que lo tiene
					Map<String, Object> alumnoDescuento = alumnoDescuentoDAO.getAlumnoPorMatricula(matricula2.getId());
					BigDecimal descuentoPersonalizado = null;
					String id_descuento_personalizado = null;

					if (alumnoDescuento != null) {
						descuentoPersonalizado = new BigDecimal(alumnoDescuento.get("descuento").toString());
						id_descuento_personalizado = alumnoDescuento.get("id").toString();
					}
					Param param_pago = new Param();
					param_pago.put("id_mat", matricula2.getId());
					param_pago.put("tip", "MEN");
					param_pago.put("canc", "0");
					param_pago.put("est", "A");
					List<AcademicoPago> meses_pagos = academicoPagoDAO.listByParams(param_pago,
							new String[] { "mens" });
					boolean hayVencimiento = true;
					int mesesPagoPuntuales = 0;
					for (AcademicoPago meses_pagar : meses_pagos) {
						//Calculamos si tiene descuento 0
						Param param1 = new Param();
						param1.put("mens", meses_pagar.getMens());
						param1.put("id_mat", matricula2.getId());
						AcademicoPago descuento = academicoPagoDAO.getByParams(param1);
						
						if (solicitud != null) // ES UN PARCHE QUE HAY Q
												// CORREGIR
							meses_pagar.setMonto(confMensualidad.getMonto());
						Periodo periodo = periodoDAO.getByParams(new Param("id",id_periodoCorrecto));
						Integer anio_ult_mat=null;
						if(periodo!=null)
							anio_ult_mat=periodo.getId_anio();
						//Obtener el tipo de fecha de vencimiento
						String tipo_fec_venc = confMensualidadDAO.getByParams(new Param("id_per",id_periodoCorrecto)).getTipo_fec_ven();
						int fecVencimiento=0;
						if(tipo_fec_venc!=null){
							if(tipo_fec_venc.equals(Constante.TIPO_FEC_VEN_FIN)){
								
								fecVencimiento = pagosService.getFecVencimientoFin(id_mat, meses_pagar.getMens(),"MEN");
							} else if(tipo_fec_venc.equals(Constante.TIPO_FEC_VEN_DIA)){
								fecVencimiento = getFecVencimiento(anio_actual, meses_pagar.getMens(),id_periodoCorrecto);
							}
						} else{
							fecVencimiento = getFecVencimiento(anio_actual, meses_pagar.getMens(),id_periodoCorrecto);
						}						
						
						
						if(descuento.getDesc_hermano()!=null && descuento.getDesc_pago_adelantado()!=null && descuento.getDesc_personalizado()!=null && descuento.getDesc_pronto_pago()!=null){//si el descuento es diferente de nulo y cero entonces se procede a los c�lculos de descuento, caso contrario no
							if(descuento.getDesc_hermano().compareTo(new BigDecimal(0))!=0 && descuento.getDesc_pago_adelantado().compareTo(new BigDecimal(0))!=0 && descuento.getDesc_personalizado().compareTo(new BigDecimal(0))!=0 && descuento.getDesc_pronto_pago().compareTo(new BigDecimal(0))!=0){
								if (descuentoPersonalizado != null && fecActual <= fecVencimiento) {
									meses_pagar.setDesc_personalizado(descuentoPersonalizado);
									meses_pagar.setDesc_hermano(new BigDecimal(0));
									meses_pagar.setDesc_pronto_pago(new BigDecimal(0));
								} else {
									// TODO FALTA PARAMETRIZAR EL DESCUENTO POR HERMANO
									meses_pagar.setDesc_personalizado(new BigDecimal(0));

									if (fecActual <= fecVencimiento) {
										hayVencimiento = false;
										mesesPagoPuntuales ++;
										if (cant_hermanos > 1 && matricula2.getId_niv() != Constante.NIVEL_INICIAL) {
											meses_pagar.setDesc_hermano(desc_hermano);
										} else
											meses_pagar.setDesc_hermano(new BigDecimal(0));

										meses_pagar.setDesc_pronto_pago(desc_secretaria);

									} else {
										meses_pagar.setDesc_hermano(new BigDecimal(0));
										meses_pagar.setDesc_pronto_pago(new BigDecimal(0));

									}

								}
							}
						} else{
								if (descuentoPersonalizado != null && fecActual <= fecVencimiento) {
									meses_pagar.setDesc_personalizado(descuentoPersonalizado);
									meses_pagar.setDesc_hermano(new BigDecimal(0));
									meses_pagar.setDesc_pronto_pago(new BigDecimal(0));
								} else {
									// TODO FALTA PARAMETRIZAR EL DESCUENTO POR HERMANO
									meses_pagar.setDesc_personalizado(new BigDecimal(0));

									if (fecActual <= fecVencimiento) {
										hayVencimiento = false;
										mesesPagoPuntuales ++;
										if (cant_hermanos > 1 && matricula2.getId_niv() != Constante.NIVEL_INICIAL) {
											meses_pagar.setDesc_hermano(desc_hermano);
										} else
											meses_pagar.setDesc_hermano(new BigDecimal(0));

										meses_pagar.setDesc_pronto_pago(desc_secretaria);

									} else {
										meses_pagar.setDesc_hermano(new BigDecimal(0));
										meses_pagar.setDesc_pronto_pago(new BigDecimal(0));

									}

								}
						}

					}

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id_mat", matricula2.getId());
					map.put("nombres", matricula2.getAlumno().getApe_pat() + " " + matricula2.getAlumno().getApe_mat()
							+ ", " + matricula2.getAlumno().getNom());
					map.put("nivel", matricula2.getNivel().getNom());
					map.put("grado", matricula2.getGrad().getNom());
					map.put("seccion", matricula2.getAula().getSecc());
					map.put("meses_descuento", meses_pagos);
					map.put("descuento_hermano", desc_hermano);
					map.put("descuento_personalizado_id", id_descuento_personalizado);// descuento
																						// administrativo
					//A�o 2020 ya no existe este descuento
					/*if (!hayVencimiento && mesesPagoPuntuales==10)//TOTAL DE MESES DE PAGO
						map.put("descuento_12", 50);// 50% de la pension del mes
													// 12 TODO PARAMETRIZAR POR
													// BD
					else*/
						map.put("descuento_12", 0);
					// map.put("descuento_secretaria",confMensualidad.getDescuento()
					// ); //falta jalar de bd

					alumnosMap.add(map);
				}

			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("apoderado", apoderado);
			map.put("hijos", alumnosMap);
			map.put("nro_rec", facturacionService.getNroRecibo(id_suc));

			result.setResult(map);
		} catch (Exception e) {
			result.setException(e);
		}

		return result;
	}

	/**
	 * Lista todos los pagos de los hijos del cliente, lo busca a partir del
	 * id_mat de uno de los hermanos
	 * 
	 * @param id_mat
	 * @return
	 */
	@RequestMapping(value = "/alumno/pagosPendientes/{id_alu}", method = RequestMethod.GET)
	public AjaxResponseBody pagosPendientesxAlumno(@PathVariable Integer id_alu) {

		AjaxResponseBody result = new AjaxResponseBody();

		try {

			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

			String format = formatter.format(new Date());
			int fecActual = Integer.parseInt(format);

			// Obtenemos datos de la matricula del alumno
			Param param = new Param();
			param.put("id_alu", id_alu);

			List<Matricula> matriculas = matriculaDAO.listByParams(param, new String[] { "id" });

			List<AcademicoPago> _meses_pagos = new ArrayList<AcademicoPago>();

			for (Matricula matricula2 : matriculas) {

				Integer id_per = matricula2.getId_per();
				String num_cont = matricula2.getNum_cont();

				// Obtenemos el descuento por hermano de acuerdo al periodo que
				// pertenece
				Param param_des = new Param();
				param_des.put("id_per", id_per);
				DescHno descHno = deschnoDAO.getByParams(param_des);

				Integer anio_actual = anioDAO.getById_per(id_per);
				BigDecimal desc_hermano = null;
				Param param3 = new Param();
				param3.put("id_per", id_per);
				if(matricula2.getId_cct()!=null)
					param3.put("id_cct", matricula2.getId_cct());
				Aula aula = aulaDAO.get(matricula2.getId_au_asi());
				if(aula.getId_cme()!=null)
					param3.put("id_cme", aula.getId_cme());
				//ConfMensualidad mensualidad = confMensualidadDAO.getByParams(new Param("id_per", id_per));
				ConfMensualidad mensualidad = confMensualidadDAO.getByParams(param3);
				if(mensualidad!=null)
					desc_hermano=mensualidad.getDesc_hermano();
	
				// String
				// descuento=deschnoDAO.getByParams(param_des).getMonto().toString();

				// Buscamos si el alumno tiene hermano con el mismo numero de
				// contrato
				List<Matricula> hermanoList = new ArrayList<Matricula>();
				if (num_cont != null && !"".equals(num_cont))
					hermanoList = matriculaDAO.listFullByParams(new Param("num_cont", num_cont),
							new String[] { "mat.id" });
				else
					continue;

				Integer cant_hermanos = 0;
				for (Matricula matriculalist : hermanoList) {
					// if( !(new Integer(5)).equals(matriculalist.getId_sit() )
					// &&
					// !matriculalist.getId_niv().equals(Constante.NIVEL_INICIAL)
					// )//si el hermano no esta trasladado
					//SituacionMat situacionMat = situacionMatDAO.getByParams(new Param("id_mat", matriculalist.getId()));
					TrasladoDetalle situacionMat=trasladoDetalleDAO.getByParams(new Param("id_mat", matriculalist.getId()));
					Integer id_sit = null;
					if (situacionMat != null)
						id_sit = situacionMat.getId_sit();
					if (id_sit != null) {
						if ((id_sit != null && id_sit != 5)
								&& !matriculalist.getId_niv().equals(Constante.NIVEL_INICIAL))// si
																								// el
																								// hermano
																								// no
																								// esta
																								// trasladado*/
							cant_hermanos = cant_hermanos + 1;
					} else {
						// if(
						// !matriculalist.getId_niv().equals(Constante.NIVEL_INICIAL)
						// )//si el hermano no esta trasladado*/
						cant_hermanos = cant_hermanos + 1;
					}

				}

				// Obtenemos el monto de todos los meses pendientes que deberia
				// pagar el alumno

				param = new Param();
				param.put("id_mat", matricula2.getId_alu());
				Solicitud solicitud = solicitudDAO.getByParams(param);
				Integer id_periodoCorrecto = matricula2.getId_per();
				if (solicitud != null)
					id_periodoCorrecto = matriculaDAO.periodoCambioLocal(matricula2.getId());

				/*ConfMensualidad confMensualidad = confMensualidadDAO
						.getByParams(new Param("id_per", id_periodoCorrecto));*/
				Param param4 = new Param();
				param4.put("id_per", id_per);
				if(matricula2.getId_cct()!=null)
					param4.put("id_cct", matricula2.getId_cct());
				Aula aula_co = aulaDAO.get(matricula2.getId_au_asi());
				if(aula_co.getId_cme()!=null)
					param4.put("id_cme", aula_co.getId_cme());
				//ConfMensualidad mensualidad = confMensualidadDAO.getByParams(new Param("id_per", id_per));
				ConfMensualidad confMensualidad = confMensualidadDAO.getByParams(param4);

				BigDecimal desc_secretaria= new BigDecimal(0);
				Integer dias_mora=0;
				if(confMensualidad!=null) {
				 desc_secretaria = confMensualidad.getDescuento();// descuento
																			// por
																			// secretaria
				dias_mora = confMensualidad.getDia_mora();// descuento
				}													// por
																	// secretaria

				// Obtener descuento personalizado si es que lo tiene
				Map<String, Object> alumnoDescuento = alumnoDescuentoDAO.getAlumnoPorMatricula(matricula2.getId());
				BigDecimal descuentoPersonalizado = null;
				String id_descuento_personalizado = null;

				if (alumnoDescuento != null) {
					descuentoPersonalizado = new BigDecimal(alumnoDescuento.get("descuento").toString());
					id_descuento_personalizado = alumnoDescuento.get("id").toString();
				}
				Param param_pago = new Param();
				param_pago.put("id_mat", matricula2.getId());
				param_pago.put("tip", "MEN");
				param_pago.put("canc", "0");
				param_pago.put("est", "A");
				List<AcademicoPago> meses_pagos = academicoPagoDAO.listByParams(param_pago, new String[] { "mens" });
				boolean existioVencimiento = false;
				for (AcademicoPago meses_pagar : meses_pagos) {

					if (solicitud != null) // ES UN PARCHE QUE HAY Q CORREGIR
						meses_pagar.setMonto(confMensualidad.getMonto());
					int fecVencimiento = getFecVencimiento(anio_actual, meses_pagar.getMens(),id_periodoCorrecto);// TODO
																								// reemplazar
																								// parametrizable

					if (descuentoPersonalizado != null && fecActual <= fecVencimiento) {
						meses_pagar.setDesc_personalizado(descuentoPersonalizado);
						meses_pagar.setDesc_hermano(new BigDecimal(0));
						meses_pagar.setDesc_pronto_pago(new BigDecimal(0));
					} else {
						// TODO FALTA PARAMETRIZAR EL DESCUENTO POR HERMANO
						meses_pagar.setDesc_personalizado(new BigDecimal(0));

						if (fecActual <= fecVencimiento) {
							existioVencimiento = true;

							if (cant_hermanos > 1 && matricula2.getId_niv() != Constante.NIVEL_INICIAL) {
								meses_pagar.setDesc_hermano(desc_hermano);
							} else
								meses_pagar.setDesc_hermano(new BigDecimal(0));

							meses_pagar.setDesc_pronto_pago(desc_secretaria);

						} else {
							meses_pagar.setDesc_hermano(new BigDecimal(0));
							meses_pagar.setDesc_pronto_pago(new BigDecimal(0));

						}

					}

					_meses_pagos.add(meses_pagar);
				}

			}

			result.setResult(_meses_pagos);

		} catch (Exception e) {
			result.setException(e);
		}

		return result;
	}
	
	@RequestMapping(value = "/todospagosPendientesAlumno/{id_alu}/{id_suc}", method = RequestMethod.GET)
	public AjaxResponseBody todospagosPendientesxAlumno(@PathVariable Integer id_alu, @PathVariable Integer id_suc) {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

			String format = formatter.format(new Date());
			int fecActual = Integer.parseInt(format);
			
			// Obtenemos datos de la matricula del alumno
			Param param = new Param();
			param.put("mat.id_alu", id_alu);
			if(id_suc!=null && id_suc!=0)
			param.put("pee.id_suc", id_suc);

			List<Matricula> matriculas = matriculaDAO.listFullByParams(param, new String[] { "mat.id" });

			List<AcademicoPago> _meses_pagos = new ArrayList<AcademicoPago>();

			for (Matricula matricula2 : matriculas) {
				Integer anio_mat= anioDAO.getById_per(matricula2.getId_per());
				//String anio_nom=anioDAO.getByParams(new Param("id",id_anio)).getNom();
				if(matricula2.getTipo()!=null) {
					if(matricula2.getTipo().equals("A") || matricula2.getTipo().equals("V")) {
						Param param_pago = new Param();
						param_pago.put("id_mat", matricula2.getId());
						//param_pago.put("tip", "MEN");
						param_pago.put("canc", "0");
						param_pago.put("est", "A");
						List<AcademicoPago> cuotas_pendientes = academicoPagoDAO.listByParams(param_pago, new String[] { "nro_cuota" });
						boolean existioVencimiento = false;
						for (AcademicoPago meses_pagar : cuotas_pendientes) {
						//	meses_pagar.setAnio(anio_mat.toString());
						//	meses_pagar.setConcepto("CUOTA NRO"+meses_pagar.getNro_cuota());
							//meses_pagar.setNombre_mes(null);
							BigDecimal des_beca= new BigDecimal(0);
							BigDecimal des_hermano= new BigDecimal(0);
							BigDecimal des_pronto_pago= new BigDecimal(0);
							BigDecimal des_personalizado= new BigDecimal(0);
							BigDecimal des_pag_adelantado= new BigDecimal(0);
							Ciclo ciclo = cicloDAO.getByParams(new Param("id",matricula2.getId_cic()));
							AcademicoPago academicoPago2=new AcademicoPago();
							academicoPago2.setAnio(anio_mat.toString());
							academicoPago2.setConcepto("Cuota Nro. "+meses_pagar.getNro_cuota()+" - "+ciclo.getNom());
							academicoPago2.setMonto(meses_pagar.getMonto());
							academicoPago2.setDesc_beca(meses_pagar.getDesc_beca()==null? des_beca : meses_pagar.getDesc_beca());
							academicoPago2.setDesc_hermano(meses_pagar.getDesc_hermano()==null? des_hermano : meses_pagar.getDesc_hermano());
							academicoPago2.setDesc_pronto_pago(meses_pagar.getDesc_pronto_pago()==null? des_pronto_pago : meses_pagar.getDesc_pronto_pago());
							academicoPago2.setDesc_personalizado(meses_pagar.getDesc_personalizado()==null? des_personalizado : meses_pagar.getDesc_personalizado());
							academicoPago2.setDesc_pago_adelantado(meses_pagar.getDesc_pago_adelantado()==null? des_pag_adelantado: meses_pagar.getDesc_pago_adelantado());
							academicoPago2.setMontoTotal(meses_pagar.getMontoTotal());
							academicoPago2.setTip("MEN");
							academicoPago2.setMens(meses_pagar.getNro_cuota());
							academicoPago2.setId(meses_pagar.getId());
							academicoPago2.setMatricula(matricula2);
							_meses_pagos.add(academicoPago2);
							
						}
					} else if(matricula2.getTipo().equals("C")) {
						String mat_validada = matricula2.getCon_val();
						if(mat_validada!=null) {
							if(mat_validada.equals("1")) {
								Param param_pago = new Param();
								param_pago.put("id_mat", matricula2.getId());
								param_pago.put("tip", "MAT");
								param_pago.put("canc", "0");
								param_pago.put("est", "A");
								List<AcademicoPago> cuotas_pendientes = academicoPagoDAO.listByParams(param_pago, new String[] { "nro_cuota" });
								boolean existioVencimiento = false;
								for (AcademicoPago meses_pagar : cuotas_pendientes) {
								//	meses_pagar.setAnio(anio_mat.toString());
								//	meses_pagar.setConcepto("CUOTA NRO"+meses_pagar.getNro_cuota());
									//meses_pagar.setNombre_mes(null);
									BigDecimal des_beca= new BigDecimal(0);
									BigDecimal des_hermano= new BigDecimal(0);
									BigDecimal des_pronto_pago= new BigDecimal(0);
									BigDecimal des_personalizado= new BigDecimal(0);
									BigDecimal des_pag_adelantado= new BigDecimal(0);
									//Ciclo ciclo = cicloDAO.getByParams(new Param("id",matricula2.getId_cic()));
									AcademicoPago academicoPago2=new AcademicoPago();
									academicoPago2.setAnio(anio_mat.toString());
									academicoPago2.setConcepto("MATRICULA");
									academicoPago2.setMonto(meses_pagar.getMonto());
									academicoPago2.setDesc_beca(meses_pagar.getDesc_beca()==null? des_beca : meses_pagar.getDesc_beca());
									academicoPago2.setDesc_hermano(meses_pagar.getDesc_hermano()==null? des_hermano : meses_pagar.getDesc_hermano());
									academicoPago2.setDesc_pronto_pago(meses_pagar.getDesc_pronto_pago()==null? des_pronto_pago : meses_pagar.getDesc_pronto_pago());
									academicoPago2.setDesc_personalizado(meses_pagar.getDesc_personalizado()==null? des_personalizado : meses_pagar.getDesc_personalizado());
									academicoPago2.setDesc_pago_adelantado(meses_pagar.getDesc_pago_adelantado()==null? des_pag_adelantado: meses_pagar.getDesc_pago_adelantado());
									academicoPago2.setMontoTotal(meses_pagar.getMontoTotal());
									academicoPago2.setTip("MAT");
									academicoPago2.setMens(1);
									academicoPago2.setId(meses_pagar.getId());
									academicoPago2.setMatricula(matricula2);
									_meses_pagos.add(academicoPago2);
								}	
							}
						
						
						Param param_pago2 = new Param();
						param_pago2.put("id_mat", matricula2.getId());
						param_pago2.put("tip", "ING");
						param_pago2.put("canc", "0");
						param_pago2.put("est", "A");
						List<AcademicoPago> cuotas_pendientes2 = academicoPagoDAO.listByParams(param_pago2, new String[] { "nro_cuota" });
						for (AcademicoPago meses_pagar : cuotas_pendientes2) {
						//	meses_pagar.setAnio(anio_mat.toString());
						//	meses_pagar.setConcepto("CUOTA NRO"+meses_pagar.getNro_cuota());
							//meses_pagar.setNombre_mes(null);
							BigDecimal des_beca= new BigDecimal(0);
							BigDecimal des_hermano= new BigDecimal(0);
							BigDecimal des_pronto_pago= new BigDecimal(0);
							BigDecimal des_personalizado= new BigDecimal(0);
							BigDecimal des_pag_adelantado= new BigDecimal(0);
							//Ciclo ciclo = cicloDAO.getByParams(new Param("id",matricula2.getId_cic()));
							AcademicoPago academicoPago3=new AcademicoPago();
							academicoPago3.setAnio(anio_mat.toString());
							academicoPago3.setConcepto("CUOTA DE INGRESO");
							academicoPago3.setMonto(meses_pagar.getMonto());
							academicoPago3.setDesc_beca(meses_pagar.getDesc_beca()==null? des_beca : meses_pagar.getDesc_beca());
							academicoPago3.setDesc_hermano(meses_pagar.getDesc_hermano()==null? des_hermano : meses_pagar.getDesc_hermano());
							academicoPago3.setDesc_pronto_pago(meses_pagar.getDesc_pronto_pago()==null? des_pronto_pago : meses_pagar.getDesc_pronto_pago());
							academicoPago3.setDesc_personalizado(meses_pagar.getDesc_personalizado()==null? des_personalizado : meses_pagar.getDesc_personalizado());
							academicoPago3.setDesc_pago_adelantado(meses_pagar.getDesc_pago_adelantado()==null? des_pag_adelantado: meses_pagar.getDesc_pago_adelantado());
							academicoPago3.setMontoTotal(meses_pagar.getMontoTotal());
							academicoPago3.setTip("ING");
							academicoPago3.setMens(1);
							academicoPago3.setId(meses_pagar.getId());
							academicoPago3.setMatricula(matricula2);
							_meses_pagos.add(academicoPago3);
						}	
						}
						/*Param param_pago3 = new Param();
						param_pago3.put("id_mat", matricula2.getId());
						param_pago3.put("tip", "MEN");
						param_pago3.put("canc", "0");
						param_pago3.put("est", "A");
						List<AcademicoPago> cuotas_pendientes3 = academicoPagoDAO.listByParams(param_pago3, new String[] { "mens" });
						for (AcademicoPago meses_pagar : cuotas_pendientes3) {
						//	meses_pagar.setAnio(anio_mat.toString());
						//	meses_pagar.setConcepto("CUOTA NRO"+meses_pagar.getNro_cuota());
							//meses_pagar.setNombre_mes(null);
							BigDecimal des_beca= new BigDecimal(0);
							BigDecimal des_hermano= new BigDecimal(0);
							BigDecimal des_pronto_pago= new BigDecimal(0);
							BigDecimal des_personalizado= new BigDecimal(0);
							BigDecimal des_pag_adelantado= new BigDecimal(0);
							//Ciclo ciclo = cicloDAO.getByParams(new Param("id",matricula2.getId_cic()));
							AcademicoPago academicoPago4=new AcademicoPago();
							academicoPago4.setAnio(anio_mat.toString());
							academicoPago4.setConcepto("Mensualidad");
							academicoPago4.setMonto(meses_pagar.getMonto());
							academicoPago4.setDesc_beca(meses_pagar.getDesc_beca()==null? des_beca : meses_pagar.getDesc_beca());
							academicoPago4.setDesc_hermano(meses_pagar.getDesc_hermano()==null? des_hermano : meses_pagar.getDesc_hermano());
							academicoPago4.setDesc_pronto_pago(meses_pagar.getDesc_pronto_pago()==null? des_pronto_pago : meses_pagar.getDesc_pronto_pago());
							academicoPago4.setDesc_personalizado(meses_pagar.getDesc_personalizado()==null? des_personalizado : meses_pagar.getDesc_personalizado());
							academicoPago4.setDesc_pago_adelantado(meses_pagar.getDesc_pago_adelantado()==null? des_pag_adelantado: meses_pagar.getDesc_pago_adelantado());
							academicoPago4.setMontoTotal(meses_pagar.getMontoTotal());
							academicoPago4.setTip("MEN");
							academicoPago4.setMens(meses_pagar.getMens());
							academicoPago4.setId(meses_pagar.getId());
							academicoPago4.setMatricula(matricula2);
							_meses_pagos.add(academicoPago4);
						}	*/
						/**
						 * Comentado 2021
						 */
						
						Integer id_per = matricula2.getId_per();
						String num_cont = matricula2.getNum_cont();

						// Obtenemos el descuento por hermano de acuerdo al periodo que
						// pertenece
						Param param_des = new Param();
						param_des.put("id_per", id_per);
						DescHno descHno = deschnoDAO.getByParams(param_des);

						Integer anio_actual = anioDAO.getById_per(id_per);
						BigDecimal desc_hermano = null;
						ConfMensualidad mensualidad = confMensualidadDAO.getByParams(new Param("id_per", id_per));
						if(mensualidad!=null)
							desc_hermano=mensualidad.getDesc_hermano();
			
						// String
						// descuento=deschnoDAO.getByParams(param_des).getMonto().toString();

						// Buscamos si el alumno tiene hermano con el mismo numero de
						// contrato
						
						List<Matricula> hermanoList = new ArrayList<Matricula>();
						//Lista de matriculas por apoderado
						Param param_mat = new Param();
						param_mat.put("mat.id_fam", matricula2.getId_fam());
						//Periodo Matricula
						Periodo periodo_mat = periodoDAO.get(matricula2.getId_per());
						param_mat.put("pee.id_anio", periodo_mat.getId_anio());
						param_mat.put("pee.id_tpe", 1);
						 hermanoList = matriculaDAO.listFullByParams(param_mat, null);
						/*if (num_cont != null && !"".equals(num_cont)) {
							hermanoList = matriculaDAO.listFullByParams(new Param("num_cont", num_cont),
									new String[] { "mat.id" });
						} else {
							continue;
						}*/
						
						Integer cant_hermanos = 0;
						for (Matricula matriculalist : hermanoList) {
							//TrasladoDetalle situacionMat=trasladoDetalleDAO.getByParams(new Param("id_mat", matriculalist.getId()));//Por ahora se maneja la situacion en la misma matricula
							
							Integer id_sit = matriculalist.getId_sit();
							/*if (situacionMat != null)
								id_sit = situacionMat.getId_sit();
							if (id_sit != null) {
								if ((id_sit != null && id_sit != 5 && id_sit!=4)
										&& !matriculalist.getId_niv().equals(Constante.NIVEL_INICIAL))
									cant_hermanos = cant_hermanos + 1;
							} else {
								// if(
								// !matriculalist.getId_niv().equals(Constante.NIVEL_INICIAL)
								// )//si el hermano no esta trasladado
								cant_hermanos = cant_hermanos + 1;
							}*/
							

							
							if (id_sit != null) {
								if ((id_sit != null && id_sit != 5 && id_sit!=4))
									cant_hermanos = cant_hermanos + 1;
									//&& !matriculalist.getId_niv().equals(Constante.NIVEL_INICIAL))
									
							} else {
								// if(
								// !matriculalist.getId_niv().equals(Constante.NIVEL_INICIAL)
								// )//si el hermano no esta trasladado
								cant_hermanos = cant_hermanos + 1;
							}

						}

						// Obtenemos el monto de todos los meses pendientes que deberia
						// pagar el alumno

						param = new Param();
						param.put("id_mat", matricula2.getId_alu());
						Solicitud solicitud = solicitudDAO.getByParams(param);
						Integer id_periodoCorrecto = matricula2.getId_per();
						if (solicitud != null)
							id_periodoCorrecto = matriculaDAO.periodoCambioLocal(matricula2.getId());

						ConfMensualidad confMensualidad = confMensualidadDAO
								.getByParams(new Param("id_per", id_periodoCorrecto));

						BigDecimal desc_secretaria = confMensualidad.getDescuento();// descuento
																					// por
																					// secretaria
						Integer dias_mora = confMensualidad.getDia_mora();// descuento
																			// por
																			// secretaria

						// Obtener descuento personalizado si es que lo tiene
						Map<String, Object> alumnoDescuento = alumnoDescuentoDAO.getAlumnoPorMatricula(matricula2.getId());
						BigDecimal descuentoPersonalizado = null;
						String id_descuento_personalizado = null;

						if (alumnoDescuento != null) {
							descuentoPersonalizado = new BigDecimal(alumnoDescuento.get("descuento").toString());
							id_descuento_personalizado = alumnoDescuento.get("id").toString();
						}
						Param param_pago3 = new Param();
						param_pago3.put("id_mat", matricula2.getId());
						param_pago3.put("tip", "MEN");
						param_pago3.put("canc", "0");
						param_pago3.put("est", "A");
						List<AcademicoPago> meses_pagos = academicoPagoDAO.listByParams(param_pago3, new String[] { "mens" });
						boolean existioVencimiento2 = false;
						for (AcademicoPago meses_pagar : meses_pagos) {

							if (solicitud != null) // ES UN PARCHE QUE HAY Q CORREGIR
								meses_pagar.setMonto(confMensualidad.getMonto());
							Row tarifa=null;
							Periodo periodo=periodoDAO.get(id_periodoCorrecto);
							if(!meses_pagar.getMens().equals(0)) {
								tarifa= tarifasEmergenciaDAO.listarTarifasMEs(meses_pagar.getMens(),periodo.getId_anio()); 
							}
							if(tarifa!=null) {
								meses_pagar.setMonto(tarifa.getBigDecimal("monto"));
								meses_pagar.setMontoTotal(tarifa.getBigDecimal("monto_total"));
							}
							//int fecVencimiento = getFecVencimiento(anio_actual, meses_pagar.getMens(),id_periodoCorrecto);// 
							int fecVencimiento=0;
							//Obtener el tipo de fecha de vencimiento
							Integer dia_mora=10;
							String tipo_fec_venc = confMensualidadDAO.getByParams(new Param("id_per",matricula2.getId_per())).getTipo_fec_ven();
							if(tipo_fec_venc!=null){
								if(tipo_fec_venc.equals(Constante.TIPO_FEC_VEN_FIN)){								
									fecVencimiento = pagosService.getFecVencimientoFin(matricula2.getId(), meses_pagar.getMens(),"MEN");
								} else if(tipo_fec_venc.equals(Constante.TIPO_FEC_VEN_DIA)){
									

									fecVencimiento = getFecVencimiento(anio_mat,meses_pagar.getMens(),dia_mora);
								}
							} else{
								if(dia_mora!=null){
									System.out.println("3.2");

									fecVencimiento =  getFecVencimiento(anio_mat,meses_pagar.getMens(),dia_mora);
								} 
							}
																										
							meses_pagar.setDesc_pago_adelantado(new BigDecimal(0));
							meses_pagar.setDesc_beca(new BigDecimal(0));
							if (descuentoPersonalizado != null && fecActual <= fecVencimiento) {
								meses_pagar.setDesc_personalizado(descuentoPersonalizado);
								meses_pagar.setDesc_beca(new BigDecimal(0));
								meses_pagar.setDesc_hermano(new BigDecimal(0));
								meses_pagar.setDesc_pronto_pago(new BigDecimal(0));
								meses_pagar.setDesc_pago_adelantado(new BigDecimal(0));
							} else {
								// TODO FALTA PARAMETRIZAR EL DESCUENTO POR HERMANO
								meses_pagar.setDesc_personalizado(new BigDecimal(0));

								if (fecActual <= fecVencimiento) {
									existioVencimiento2 = true;

									

									meses_pagar.setDesc_pronto_pago(desc_secretaria);
									//Buscamos si existe Beca
									AcapagBeca acapagBeca=acadBecaDAO.getByParams(new Param("id_fac",meses_pagar.getId()));
									if(acapagBeca!=null) {
										meses_pagar.setDesc_beca(acapagBeca.getMonto_afectado());
										meses_pagar.setDesc_hermano(new BigDecimal(0));
									} else {
										if (cant_hermanos > 1 && matricula2.getId_niv() != Constante.NIVEL_INICIAL) {
											meses_pagar.setDesc_hermano(desc_hermano);
										} else
											meses_pagar.setDesc_hermano(new BigDecimal(0));
									}

								} else {
									//Buscamos si existe Beca
									AcapagBeca acapagBeca=acadBecaDAO.getByParams(new Param("id_fac",meses_pagar.getId()));
									if(acapagBeca!=null) {
										meses_pagar.setDesc_beca(acapagBeca.getMonto_afectado());
									}
									meses_pagar.setDesc_hermano(new BigDecimal(0));
									meses_pagar.setDesc_pronto_pago(new BigDecimal(0));
									//meses_pagar.setDesc_beca(new BigDecimal(0));
								}

							}
							meses_pagar.setAnio(anio_mat.toString());
							meses_pagar.setConcepto("Pensión");
							_meses_pagos.add(meses_pagar);
						}
					}
			
				} /*else {
					Integer id_per = matricula2.getId_per();
					String num_cont = matricula2.getNum_cont();

					// Obtenemos el descuento por hermano de acuerdo al periodo que
					// pertenece
					Param param_des = new Param();
					param_des.put("id_per", id_per);
					DescHno descHno = deschnoDAO.getByParams(param_des);

					Integer anio_actual = anioDAO.getById_per(id_per);
					BigDecimal desc_hermano = null;
					ConfMensualidad mensualidad = confMensualidadDAO.getByParams(new Param("id_per", id_per));
					if(mensualidad!=null)
						desc_hermano=mensualidad.getDesc_hermano();
		
					// String
					// descuento=deschnoDAO.getByParams(param_des).getMonto().toString();

					// Buscamos si el alumno tiene hermano con el mismo numero de
					// contrato
					List<Matricula> hermanoList = new ArrayList<Matricula>();
					if (num_cont != null && !"".equals(num_cont))
						hermanoList = matriculaDAO.listFullByParams(new Param("num_cont", num_cont),
								new String[] { "mat.id" });
					else
						continue;

					Integer cant_hermanos = 0;
					for (Matricula matriculalist : hermanoList) {
						// if( !(new Integer(5)).equals(matriculalist.getId_sit() )
						// &&
						// !matriculalist.getId_niv().equals(Constante.NIVEL_INICIAL)
						// )//si el hermano no esta trasladado
						//SituacionMat situacionMat = situacionMatDAO.getByParams(new Param("id_mat", matriculalist.getId()));
						TrasladoDetalle situacionMat=trasladoDetalleDAO.getByParams(new Param("id_mat", matriculalist.getId()));
						Integer id_sit = null;
						if (situacionMat != null)
							id_sit = situacionMat.getId_sit();
						if (id_sit != null) {
							if ((id_sit != null && id_sit != 5)
									&& !matriculalist.getId_niv().equals(Constante.NIVEL_INICIAL))// si
																									// el
																									// hermano
																									// no
																									// esta
																									// trasladado
								cant_hermanos = cant_hermanos + 1;
						} else {
							// if(
							// !matriculalist.getId_niv().equals(Constante.NIVEL_INICIAL)
							// )//si el hermano no esta trasladado
							cant_hermanos = cant_hermanos + 1;
						}

					}

					// Obtenemos el monto de todos los meses pendientes que deberia
					// pagar el alumno

					param = new Param();
					param.put("id_mat", matricula2.getId_alu());
					Solicitud solicitud = solicitudDAO.getByParams(param);
					Integer id_periodoCorrecto = matricula2.getId_per();
					if (solicitud != null)
						id_periodoCorrecto = matriculaDAO.periodoCambioLocal(matricula2.getId());

					ConfMensualidad confMensualidad = confMensualidadDAO
							.getByParams(new Param("id_per", id_periodoCorrecto));
					
					BigDecimal desc_secretaria= new BigDecimal(0);
					Integer dias_mora =10;
					if(confMensualidad!=null) {
						desc_secretaria= confMensualidad.getDescuento();
						dias_mora = confMensualidad.getDia_mora();
					}
					// Obtener descuento personalizado si es que lo tiene
					Map<String, Object> alumnoDescuento = alumnoDescuentoDAO.getAlumnoPorMatricula(matricula2.getId());
					BigDecimal descuentoPersonalizado = null;
					String id_descuento_personalizado = null;

					if (alumnoDescuento != null) {
						descuentoPersonalizado = new BigDecimal(alumnoDescuento.get("descuento").toString());
						id_descuento_personalizado = alumnoDescuento.get("id").toString();
					}
					Param param_pago = new Param();
					param_pago.put("id_mat", matricula2.getId());
					param_pago.put("tip", "MEN");
					param_pago.put("canc", "0");
					param_pago.put("est", "A");
					List<AcademicoPago> meses_pagos = academicoPagoDAO.listByParams(param_pago, new String[] { "mens" });
					boolean existioVencimiento = false;
					for (AcademicoPago meses_pagar : meses_pagos) {

						if (solicitud != null) // ES UN PARCHE QUE HAY Q CORREGIR
							meses_pagar.setMonto(confMensualidad.getMonto());
						//int fecVencimiento = getFecVencimiento(anio_actual, meses_pagar.getMens(),id_periodoCorrecto);
						//CALCULAR EL DESCUENTO SI PAGA PUNTUAL
						int fecVencimiento=0;
						//Obtener el tipo de fecha de vencimiento
						Integer dia_mora=10;
						String tipo_fec_venc = confMensualidadDAO.getByParams(new Param("id_per",matricula2.getId_per())).getTipo_fec_ven();
						if(tipo_fec_venc!=null){
							if(tipo_fec_venc.equals(Constante.TIPO_FEC_VEN_FIN)){								
								fecVencimiento = pagosService.getFecVencimientoFin(matricula2.getId(), meses_pagar.getMens());
							} else if(tipo_fec_venc.equals(Constante.TIPO_FEC_VEN_DIA)){
								

								fecVencimiento = getFecVencimiento(anio_mat,meses_pagar.getMens(),dia_mora);
							}
						} else{
							if(dia_mora!=null){
								System.out.println("3.2");

								fecVencimiento =  getFecVencimiento(anio_mat,meses_pagar.getMens(),dia_mora);
							} 
						}
	
						meses_pagar.setDesc_pago_adelantado(new BigDecimal(0));
						meses_pagar.setDesc_beca(new BigDecimal(0));
						if (descuentoPersonalizado != null && fecActual <= fecVencimiento) {
							meses_pagar.setDesc_personalizado(descuentoPersonalizado);
							meses_pagar.setDesc_hermano(new BigDecimal(0));
							meses_pagar.setDesc_pronto_pago(new BigDecimal(0));
							meses_pagar.setDesc_beca(new BigDecimal(0));
						} else {
							meses_pagar.setDesc_personalizado(new BigDecimal(0));

							if (fecActual <= fecVencimiento) {
								existioVencimiento = true;

								if (cant_hermanos > 1 && matricula2.getId_niv() != Constante.NIVEL_INICIAL) {
									meses_pagar.setDesc_hermano(desc_hermano);
								} else
									meses_pagar.setDesc_hermano(new BigDecimal(0));

								meses_pagar.setDesc_pronto_pago(desc_secretaria);
								//Buscamos si existe Beca
								AcapagBeca acapagBeca=acadBecaDAO.getByParams(new Param("id_fac",meses_pagar.getId()));
								if(acapagBeca!=null) {
									meses_pagar.setDesc_beca(acapagBeca.getMonto_afectado());
								}

							} else {
								meses_pagar.setDesc_hermano(new BigDecimal(0));
								meses_pagar.setDesc_pronto_pago(new BigDecimal(0));

							}

						}
						meses_pagar.setAnio(anio_mat.toString());
						meses_pagar.setConcepto("Pensión");
						_meses_pagos.add(meses_pagar);
					}
				}*/
				
				
			}
			
			result.setResult(_meses_pagos);
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		
		return result;
		
	}	
	
	@RequestMapping(value = "/todospagosPendientesAluNoAfectadoBeca/{id_alu}/{id_suc}", method = RequestMethod.GET)
	public AjaxResponseBody todospagosPendientesxAluNoAfectadoBeca(@PathVariable Integer id_alu, @PathVariable Integer id_suc) {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

			String format = formatter.format(new Date());
			int fecActual = Integer.parseInt(format);
			
			// Obtenemos datos de la matricula del alumno
			Param param = new Param();
			param.put("mat.id_alu", id_alu);
			if(id_suc!=null && id_suc!=0)
			param.put("pee.id_suc", id_suc);

			List<Matricula> matriculas = matriculaDAO.listFullByParams(param, new String[] { "mat.id" });

			List<AcademicoPago> _meses_pagos = new ArrayList<AcademicoPago>();

			for (Matricula matricula2 : matriculas) {
				Integer anio_mat= anioDAO.getById_per(matricula2.getId_per());
				//String anio_nom=anioDAO.getByParams(new Param("id",id_anio)).getNom();
				if(matricula2.getTipo()!=null) {
					if(matricula2.getTipo().equals("A") || matricula2.getTipo().equals("V")) {
						//Param param_pago = new Param();
						//param_pago.put("id_mat", matricula2.getId());
						//param_pago.put("tip", "MEN");
						//param_pago.put("canc", "0");
						//param_pago.put("est", "A");
						//param_pago.put("id_bec", " NULL");
						//List<AcademicoPago> cuotas_pendientes = academicoPagoDAO.listByParams(param_pago, new String[] { "nro_cuota" });
						List<Row> cuotas_pendientes = academicoPagoDAO.listarPagosParaBeca(matricula2.getId(), "nro_cuota");
						boolean existioVencimiento = false;
						for (Row meses_pagar : cuotas_pendientes) {
						//	meses_pagar.setAnio(anio_mat.toString());
						//	meses_pagar.setConcepto("CUOTA NRO"+meses_pagar.getNro_cuota());
							//meses_pagar.setNombre_mes(null);
							BigDecimal des_beca= new BigDecimal(0);
							BigDecimal des_hermano= new BigDecimal(0);
							BigDecimal des_pronto_pago= new BigDecimal(0);
							BigDecimal des_personalizado= new BigDecimal(0);
							BigDecimal des_pag_adelantado= new BigDecimal(0);
							Ciclo ciclo = cicloDAO.getByParams(new Param("id",matricula2.getId_cic()));
							AcademicoPago academicoPago2=new AcademicoPago();
							academicoPago2.setAnio(anio_mat.toString());
							academicoPago2.setConcepto("Cuota Nro. "+meses_pagar.getInteger("nro_cuota")+" - "+ciclo.getNom());
							academicoPago2.setMonto(meses_pagar.getBigDecimal("monto"));
							academicoPago2.setDesc_beca(meses_pagar.getBigDecimal("desc_beca")==null? des_beca : meses_pagar.getBigDecimal("desc_beca"));
							academicoPago2.setDesc_hermano(meses_pagar.getBigDecimal("desc_hermano")==null? des_hermano : meses_pagar.getBigDecimal("desc_hermano"));
							academicoPago2.setDesc_pronto_pago(meses_pagar.getBigDecimal("desc_pronto_pago")==null? des_pronto_pago : meses_pagar.getBigDecimal("desc_pronto_pago"));
							academicoPago2.setDesc_personalizado(meses_pagar.getBigDecimal("desc_personalizado")==null? des_personalizado : meses_pagar.getBigDecimal("desc_personalizado"));
							academicoPago2.setDesc_pago_adelantado(meses_pagar.getBigDecimal("desc_pago_adelantado")==null? des_pag_adelantado: meses_pagar.getBigDecimal("desc_pago_adelantado"));
							academicoPago2.setMontoTotal(meses_pagar.getBigDecimal("monto_total"));
							academicoPago2.setTip("MEN");
							academicoPago2.setMens(meses_pagar.getInteger("nro_cuota"));
							academicoPago2.setId(meses_pagar.getInt("id"));
							academicoPago2.setMatricula(matricula2);
							_meses_pagos.add(academicoPago2);
							
						}
					} else {
						if(id_suc==0) {
							Integer id_per = matricula2.getId_per();
							String num_cont = matricula2.getNum_cont();

							// Obtenemos el descuento por hermano de acuerdo al periodo que
							// pertenece
							Param param_des = new Param();
							param_des.put("id_per", id_per);
							DescHno descHno = deschnoDAO.getByParams(param_des);

							Integer anio_actual = anioDAO.getById_per(id_per);
							BigDecimal desc_hermano = null;
							ConfMensualidad mensualidad = confMensualidadDAO.getByParams(new Param("id_per", id_per));
							if(mensualidad!=null)
								desc_hermano=mensualidad.getDesc_hermano();
				
							// String
							// descuento=deschnoDAO.getByParams(param_des).getMonto().toString();

							// Buscamos si el alumno tiene hermano con el mismo numero de
							// contrato
							List<Matricula> hermanoList = new ArrayList<Matricula>();
							if (num_cont != null && !"".equals(num_cont))
								hermanoList = matriculaDAO.listFullByParams(new Param("num_cont", num_cont),
										new String[] { "mat.id" });
							else
								continue;

							Integer cant_hermanos = 0;
							for (Matricula matriculalist : hermanoList) {
								TrasladoDetalle situacionMat=trasladoDetalleDAO.getByParams(new Param("id_mat", matriculalist.getId()));
								Integer id_sit = null;
								if (situacionMat != null)
									id_sit = situacionMat.getId_sit();
								if (id_sit != null) {
									if ((id_sit != null && id_sit != 5)
											&& !matriculalist.getId_niv().equals(Constante.NIVEL_INICIAL))
										cant_hermanos = cant_hermanos + 1;
								} else {
									// if(
									// !matriculalist.getId_niv().equals(Constante.NIVEL_INICIAL)
									// )//si el hermano no esta trasladado
									cant_hermanos = cant_hermanos + 1;
								}

							}

							// Obtenemos el monto de todos los meses pendientes que deberia
							// pagar el alumno

							param = new Param();
							param.put("id_mat", matricula2.getId_alu());
							Solicitud solicitud = solicitudDAO.getByParams(param);
							Integer id_periodoCorrecto = matricula2.getId_per();
							if (solicitud != null)
								id_periodoCorrecto = matriculaDAO.periodoCambioLocal(matricula2.getId());

							ConfMensualidad confMensualidad = confMensualidadDAO
									.getByParams(new Param("id_per", id_periodoCorrecto));

							BigDecimal desc_secretaria = confMensualidad.getDescuento();// descuento
																						// por
																						// secretaria
							Integer dias_mora = confMensualidad.getDia_mora();// descuento
																				// por
																				// secretaria

							// Obtener descuento personalizado si es que lo tiene
							Map<String, Object> alumnoDescuento = alumnoDescuentoDAO.getAlumnoPorMatricula(matricula2.getId());
							BigDecimal descuentoPersonalizado = null;
							String id_descuento_personalizado = null;

							if (alumnoDescuento != null) {
								descuentoPersonalizado = new BigDecimal(alumnoDescuento.get("descuento").toString());
								id_descuento_personalizado = alumnoDescuento.get("id").toString();
							}
							Param param_pago3 = new Param();
							param_pago3.put("id_mat", matricula2.getId());
							param_pago3.put("tip", "MEN");
							param_pago3.put("canc", "0");
							param_pago3.put("est", "A");
							List<AcademicoPago> meses_pagos = academicoPagoDAO.listByParams(param_pago3, new String[] { "mens" });
							boolean existioVencimiento2 = false;
							for (AcademicoPago meses_pagar : meses_pagos) {
								if(meses_pagar.getId_bec().equals(0)) {
									//Verificamos si existe una tarifa de emergencia
									Periodo periodo = periodoDAO.get(id_periodoCorrecto);
									Row tarifa=null;
									if(!meses_pagar.getMens().equals(0)) {
										tarifa= tarifasEmergenciaDAO.listarTarifasMEs(meses_pagar.getMens(),periodo.getId_anio()); 
									}

									if (solicitud != null) // ES UN PARCHE QUE HAY Q CORREGIR
										meses_pagar.setMonto(confMensualidad.getMonto());
									if(tarifa!=null) {
										meses_pagar.setMonto(tarifa.getBigDecimal("monto"));
										meses_pagar.setMontoTotal(tarifa.getBigDecimal("monto_total"));
									}
										
									//int fecVencimiento = getFecVencimiento(anio_actual, meses_pagar.getMens(),id_periodoCorrecto);// 
									int fecVencimiento=0;
									//Obtener el tipo de fecha de vencimiento
									Integer dia_mora=10;
									String tipo_fec_venc = confMensualidadDAO.getByParams(new Param("id_per",matricula2.getId_per())).getTipo_fec_ven();
									if(tipo_fec_venc!=null){
										if(tipo_fec_venc.equals(Constante.TIPO_FEC_VEN_FIN)){								
											fecVencimiento = pagosService.getFecVencimientoFin(matricula2.getId(), meses_pagar.getMens(),"MEN");
										} else if(tipo_fec_venc.equals(Constante.TIPO_FEC_VEN_DIA)){
											

											fecVencimiento = getFecVencimiento(anio_mat,meses_pagar.getMens(),dia_mora);
										}
									} else{
										if(dia_mora!=null){
											System.out.println("3.2");

											fecVencimiento =  getFecVencimiento(anio_mat,meses_pagar.getMens(),dia_mora);
										} 
									}
																												
									meses_pagar.setDesc_pago_adelantado(new BigDecimal(0));
									meses_pagar.setDesc_beca(new BigDecimal(0));
									if (descuentoPersonalizado != null && fecActual <= fecVencimiento) {
										meses_pagar.setDesc_personalizado(descuentoPersonalizado);
										meses_pagar.setDesc_beca(new BigDecimal(0));
										meses_pagar.setDesc_hermano(new BigDecimal(0));
										meses_pagar.setDesc_pronto_pago(new BigDecimal(0));
										meses_pagar.setDesc_pago_adelantado(new BigDecimal(0));
									} else {
										// TODO FALTA PARAMETRIZAR EL DESCUENTO POR HERMANO
										meses_pagar.setDesc_personalizado(new BigDecimal(0));

										if (fecActual <= fecVencimiento) {
											existioVencimiento2 = true;

											if (cant_hermanos > 1 && matricula2.getId_niv() != Constante.NIVEL_INICIAL) {
												if(tarifa!=null)
													meses_pagar.setDesc_hermano(tarifa.getBigDecimal("des_hermano"));
												else
													meses_pagar.setDesc_hermano(desc_hermano);
											} else
												meses_pagar.setDesc_hermano(new BigDecimal(0));

											meses_pagar.setDesc_pronto_pago(desc_secretaria);
											//Buscamos si existe Beca
											AcapagBeca acapagBeca=acadBecaDAO.getByParams(new Param("id_fac",meses_pagar.getId()));
											if(acapagBeca!=null) {
												meses_pagar.setDesc_beca(acapagBeca.getMonto_afectado());
											}

										} else {
											meses_pagar.setDesc_hermano(new BigDecimal(0));
											meses_pagar.setDesc_pronto_pago(new BigDecimal(0));
											meses_pagar.setDesc_beca(new BigDecimal(0));
										}

									}
									meses_pagar.setAnio(anio_mat.toString());
									meses_pagar.setConcepto("Pensión");
									_meses_pagos.add(meses_pagar);
								}
							}
						}

					}
			
				}
				
			}
			
			result.setResult(_meses_pagos);
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		
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

		return Integer.parseInt(fecVenc);

	}

	@RequestMapping(value = "/pagar", method = RequestMethod.POST)
	// @Transactional
	public AjaxResponseBody pagar(PagoRequest pago, Integer id_suc) {
		AjaxResponseBody result = new AjaxResponseBody();

		try {
			Impresion impresion = facturacionService.pagarMensualidad(pago, id_suc);

			result.setResult(impresion);

		} catch (Exception e) {
			result.setException(e);
		}

		return result;

	}
	
	@Transactional
	@RequestMapping(value = "/pagarDeudasAlumno", method = RequestMethod.POST)
	// @Transactional
	public AjaxResponseBody pagarDeudasAlumno(HttpServletResponse response, PagoRequest pago, Integer id_suc, Integer id_per_pag) {
		AjaxResponseBody result = new AjaxResponseBody();

		try {
			List<Impresion> impresiones = facturacionService.pagarMensualidadGeneral(pago, id_suc, id_per_pag);

			result.setResult(impresiones);
			/*for (Impresion impresion : impresiones) {
				String pdf =generatePDF(impresion); 
				result.setResult(impresion);
				File initialFile = new File(pdf);
			    InputStream is = FileUtils.openInputStream(initialFile);
			    	
				
				response.setContentType("application/pdf");
				response.addHeader("Content-Disposition", "attachment; filename=boleta" + impresion.getCabecera().getNro() + ".pdf");

				IOUtils.copy(is, response.getOutputStream());
			}*/
			
			

		} catch (Exception e) {
			result.setException(e);
		}

		return result;

	}

	@RequestMapping(value = "/banco/{id_suc}")
	public AjaxResponseBody getBancoPagados(@PathVariable Integer id_suc, @RequestParam String fec_ini,
			@RequestParam(value = "fec_fin", required = false) String fec_fin, @RequestParam String nro_serie) {

		AjaxResponseBody result = new AjaxResponseBody();

		try {
			List<Map<String, Object>> pagos = academicoPagoDAO.listaPagosBanco(id_suc, FechaUtil.toDate(fec_ini),
					FechaUtil.toDate(fec_fin), nro_serie);

			result.setResult(pagos);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setException(e);
		}

		return result;

	}

	@RequestMapping(value = "/reportePagosInfocorp", method = RequestMethod.GET)
	public AjaxResponseBody reportePagosInfocorp(Integer id_anio, int id_mes, Integer id_suc) {

		AjaxResponseBody result = new AjaxResponseBody();
		Integer anio = Integer.parseInt(anioDAO.getByParams(new Param("id", id_anio)).getNom());
		Date fec = new Date();
		LocalDate localDate = fec.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int mes = localDate.getMonthValue();
		int dia = localDate.getDayOfMonth();

		Integer mes_fin=null;
		//Obtengo el estado del a�o
		Date fec_act=new java.util.Date();
		Date fec_ini=anioDAO.getByParams(new Param("id",id_anio)).getFec_ini();
		Date fec_fin=anioDAO.getByParams(new Param("id",id_anio)).getFec_fin();
		String est="";
		if(fec_act.before(fec_ini)){
			est="N";//No hay clases
		} else if(fec_act.after(fec_fin)){
			est="F";//Finalizo las clases
		} else if(fec_act.after(fec_ini) && fec_act.before(fec_fin)){
			est="C";//Estan en clases
		}
		if(est.equals("C") || est.equals("N")){
			if (mes == 1) {
				mes_fin = 12;
			} else {
				mes_fin = mes - 1;
			}
		} else if(est.equals("F")){
			mes_fin= 12;
		}		
				
		try {
			result.setResult(academicoPagoDAO.reportePagosInfocorp(anio, id_anio, mes_fin, id_mes, id_suc));
		} catch (Exception e) {
			result.setException(e);
		}

		return result;

	}

	@RequestMapping(value = "/excel")
	@ResponseBody
	public void descargarExcel(HttpServletRequest request, HttpServletResponse response, Integer id_anio,
			Integer id_mes, Integer id_suc) throws Exception {

		ExcelXlsUtil xls = new ExcelXlsUtil();
		Integer anio = Integer.parseInt(anioDAO.getByParams(new Param("id", id_anio)).getNom());
		Date fec = new Date();
		LocalDate localDate = fec.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int mes = localDate.getMonthValue();
		int dia = localDate.getDayOfMonth();

		Integer mes_fin=null;
		//Obtengo el estado del a�o
		Date fec_act=new java.util.Date();
		Date fec_ini=anioDAO.getByParams(new Param("id",id_anio)).getFec_ini();
		Date fec_fin=anioDAO.getByParams(new Param("id",id_anio)).getFec_fin();
		String est="";
		if(fec_act.before(fec_ini)){
			est="N";//No hay clases
		} else if(fec_act.after(fec_fin)){
			est="F";//Finalizo las clases
		} else if(fec_act.after(fec_ini) && fec_act.before(fec_fin)){
			est="C";//Estan en clases
		}
		if(est.equals("C") || est.equals("N")){
			if (mes == 1) {
				mes_fin = 12;
			} else {
				mes_fin = mes - 1;
			}
		} else if(est.equals("F")){
			mes_fin= 12;
		}		
		List<Map<String, Object>> list = academicoPagoDAO.reportePagosInfocorp(anio, id_anio, mes_fin, id_mes, id_suc);

		// List<Map<String,Object>> list =
		// movimientoDAO.consultarReporteCaja(FechaUtil.toDate(fec_ini),FechaUtil.toDate(fec_fin),id_suc);

		response.setContentType("application/vnd.ms-excel");

		String rutacARPETA = cacheManager.getComboBox("mod_parametro", new String[] { "RUTA_PLANTILLA" }).get(0)
				.getAux1();

		String archivo = xls.generaExcelReporteInfocorp(rutacARPETA, "plantilla_reporte_infocorp.xls", list);
		response.setHeader("Content-Disposition",
				"attachment;filename=Reporte_Infocorp" + FechaUtil.toString(new Date(), "dd-MM-yyyy") + ".xls");

		File initialFile = new File(archivo);
		InputStream is = FileUtils.openInputStream(initialFile);

		IOUtils.copy(is, response.getOutputStream());

	}

	@RequestMapping(value = "/carta_cobranza1")
	@ResponseBody
	public void descargarCarta(HttpServletRequest request, HttpServletResponse response, Integer id_anio,
			Integer id_mes, Integer id_suc) throws Exception {
		// AjaxResponseBody result = new AjaxResponseBody();
		// Integer id_anio=2;
		Parametro parametro = parametroDAO.getByParams(new Param("nom", "RUTA_PLANTILLA"));

		Date fec = new Date();
		Integer anio = Integer.parseInt(anioDAO.getByParams(new Param("id", id_anio)).getNom());
		LocalDate localDate = fec.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int mes = localDate.getMonthValue();
		int dia = localDate.getDayOfMonth();

		Integer mes_fin=null;
		//Obtengo el estado del a�o
		Date fec_act=new java.util.Date();
		Date fec_ini=anioDAO.getByParams(new Param("id",id_anio)).getFec_ini();
		Date fec_fin=anioDAO.getByParams(new Param("id",id_anio)).getFec_fin();
		String est="";
		if(fec_act.before(fec_ini)){
			est="N";//No hay clases
		} else if(fec_act.after(fec_fin)){
			est="F";//Finalizo las clases
		} else if(fec_act.after(fec_ini) && fec_act.before(fec_fin)){
			est="C";//Estan en clases
		}
		if(est.equals("C") || est.equals("N")){
			if (mes == 1) {
				mes_fin = 12;
			} else {
				mes_fin = mes - 1;
			}
		} else if(est.equals("F")){
			mes_fin= 12;
		}		
		List<Map<String, Object>> list = academicoPagoDAO.cartaCobranza(anio, id_anio, mes_fin, id_mes, id_suc);

		Map<String, String> map = new HashMap<String, String>();
		Map<String, Object> familiares = (Map<String, Object>) list.get(0).get("familiares");
		XWPFDocument src1Document = new XWPFDocument();
		int cont = 0;
		CTBody src1Body = null;
		File file = null;
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		for (Map.Entry<String, Object> entry : familiares.entrySet()) {
			String id_fam = entry.getKey();

			Map<String, Object> familiar = (Map<String, Object>) familiares.get(id_fam);
			if (familiar.get("familiar") != null) {
				map.put("FAMILIAR", (familiar.get("familiar").toString()));
				map.put("DOCIDENTIDAD", (familiar.get("dni").toString()));
				map.put("monto", (familiar.get("deuda").toString()));
				map.put("numero", (familiar.get("numero").toString()));
			}

			int annio;
			String mesactual = "";
			String MES[] = { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre",
					"Octubre", "Noviembre", "Diciembre" };
			Calendar c1 = Calendar.getInstance();
			annio = c1.get(Calendar.YEAR);
			mesactual = MES[c1.get(Calendar.MONTH)];
			String fecha_actual = c1.get(Calendar.DAY_OF_MONTH) + " de " + mesactual + " del " + c1.get(Calendar.YEAR);

			map.put("date", fecha_actual);
			List<FamiliarDeudaBean> hijos = (ArrayList<FamiliarDeudaBean>) familiar.get("hijo");
			String hijosconcat = "";
			String aulaconcat = "";
			if (hijos != null) {
				for (FamiliarDeudaBean familiarhijos : hijos) {
					hijosconcat = hijosconcat + familiarhijos.getHijos() + ", ";
					aulaconcat = aulaconcat + familiarhijos.getAulas() + ", ";
				}
			}
			map.put("alumnos", hijosconcat);
			map.put("grado", aulaconcat);

			List<FamiliarDeudaBean> meses = (ArrayList<FamiliarDeudaBean>) familiar.get("meses");
			String mesdeuda = "";
			if (meses != null) {
				for (FamiliarDeudaBean mesesdeuda : meses) {
					if (mesesdeuda.getMes_deuda() == 1) {
						mesdeuda = mesdeuda + "enero, ";
					} else if (mesesdeuda.getMes_deuda() == 2) {
						mesdeuda = mesdeuda + "febrero, ";
					} else if (mesesdeuda.getMes_deuda() == 3) {
						mesdeuda = mesdeuda + "marzo, ";
					} else if (mesesdeuda.getMes_deuda() == 4) {
						mesdeuda = mesdeuda + "abril, ";
					} else if (mesesdeuda.getMes_deuda() == 5) {
						mesdeuda = mesdeuda + "mayo, ";
					} else if (mesesdeuda.getMes_deuda() == 6) {
						mesdeuda = mesdeuda + "junio, ";
					} else if (mesesdeuda.getMes_deuda() == 7) {
						mesdeuda = mesdeuda + "julio, ";
					} else if (mesesdeuda.getMes_deuda() == 8) {
						mesdeuda = mesdeuda + "agosto, ";
					} else if (mesesdeuda.getMes_deuda() == 9) {
						mesdeuda = mesdeuda + "setiembre, ";
					} else if (mesesdeuda.getMes_deuda() == 10) {
						mesdeuda = mesdeuda + "octubre, ";
					} else if (mesesdeuda.getMes_deuda() == 11) {
						mesdeuda = mesdeuda + "noviembre, ";
					} else if (mesesdeuda.getMes_deuda() == 12) {
						mesdeuda = mesdeuda + "diciembre, ";
					}
				}
			}
			map.put("meses", mesdeuda);

			InputStream nuevoArchivo = DocxUtil.generateInputStream(parametro.getVal(), "CartaPago.docx", map);// un
																												// adicional
																												// parametro
																												// ,
																												// leiste?
																												// cual]?
			cont = cont + 1;

			if (src1Body == null) {// solo la primera vez solo se pone el
									// primero
				OPCPackage src1Package = OPCPackage.open(nuevoArchivo);
				src1Document = new XWPFDocument(src1Package);
				src1Body = src1Document.getDocument().getBody();// esto debe
																// inizar solo
																// una vez...
																// pero como
																// podria
																// ponerlo fuera
																// del bucle?
				// usa la condicion
			} else {// a p�rtir del segundo se hace un appendBody
				OPCPackage src2Package = OPCPackage.open(nuevoArchivo);
				XWPFDocument src2Document = new XWPFDocument(src2Package);
				CTBody src2Body = src2Document.getDocument().getBody();// esto
																		// debe
																		// inizar
																		// solo
																		// una
																		// vez...
																		// pero
																		// como
																		// podria
																		// ponerlo
																		// fuera
																		// del
																		// bucle?

				DocxUtil.appendBody(src1Body, src2Body);// ves esto?si creo q
														// ahi esta el problema
														// como lo inicializo y
														// q se vaya agregando?
														// usa un contador,

			}

			// al primer documento no se hace append... al segundo si se hace
			// append

		}

		// OutputStream out = new
		// FileOutputStream("D://plantillas//CartaFinal.docx");//prueba nro 2,
		// no se si funcione.. porsiacaso proibal
		OutputStream out = new FileOutputStream("/home/aeedupeh/public_html/plantillas/CartaFinal.docx");
		src1Document.write(out);

		// iniicio * aca se muestra el archivo fisico al naegador
		String nuevo = DocxUtil.generate(parametro.getVal(), "CartaFinal.docx", map);
		String fileName = URLEncoder.encode("CartaFinal.docx", "UTF-8");
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

	/*
	 * @RequestMapping(value = "/carta_cobranza") public AjaxResponseBody
	 * generarCartas( Integer id_anio, Integer id_mes, Integer id_suc) {
	 * 
	 * AjaxResponseBody result = new AjaxResponseBody(); Integer anio, int
	 * id_anio,int mes_fin, int id_mes String anio=anioDAO.getByParams(new
	 * Param("id",id_anio)).getNom();
	 * result.setResult(academicoPagoDAO.cartaCobranza(anio, id_anio, 7, 2) );
	 * 
	 * return result; }
	 */
	/*
	 * @RequestMapping(value = "/generarFacturaElectronica") public
	 * AjaxResponseBody generarFacturaElectronica(String fec ) {
	 * 
	 * AjaxResponseBody result = new AjaxResponseBody(); try { Documento
	 * documento = new ResumenBoleta();
	 * 
	 * SimpleDateFormat formatter2_string = new SimpleDateFormat("yyyy-MM-dd");
	 * DateFormat formatter2; Date date; formatter2 = new
	 * SimpleDateFormat("yyyy-MM-dd"); date = formatter2.parse(fec);
	 * SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd"); String
	 * format = formatter.format(date); int fecha = Integer.parseInt(format); //
	 * CABECERA DEL DOCUMENTO ELECTRONICO DOC doc = new DOC();
	 * doc.setTipoDocumento(Constants.TIPO_COMPROBANTE_RESUMEN_BOLETA);//ok
	 * doc.setMoneda(Constants.MONEDA_NACIONAL_CODIGO);//ok
	 * doc.setTipoCambio(null);
	 * 
	 * Integer correlativo=2;
	 * 
	 * String numeroMask = String.format("%05d", correlativo); //INICIO - VARIA
	 * POR DIA //doc.setNumDocumento("RC-"+fecha+"-"+numeroMask);//VARIA PRIMER
	 * ENVIO CORRELATIVO 00001, crear tabla...
	 * doc.setNumDocumento("RC-"+formatter.format(new
	 * Date())+"-"+numeroMask);//VARIA PRIMER ENVIO CORRELATIVO 00001, crear
	 * tabla... //doc.setNumDocumento("RC-"+ "20181102"+"-"+numeroMask);//VARIA
	 * PRIMER ENVIO CORRELATIVO 00001, crear tabla...
	 * 
	 * doc.set_fechaEmisionDocumentosOrigen(fec);//VARIA SON PAGOS DEL BANCO
	 * doc.set_fechaEmision(formatter2_string.format(new Date()));//VARIA FECHA
	 * DE ENVIO REAL //FIN - VARIA POR DIA
	 * 
	 * documento.setDoc(doc);
	 * 
	 * // DATOS DEL EMISOR EMS ems = new EMS();
	 * ems.setTipoDocumentoIdentidad("6"); ems.setRuc("20531084587");//ok
	 * ems.setRazonSocial("ASOCIACION EDUCATIVA LUZ Y CIENCIA");
	 * documento.setEms(ems);
	 * 
	 * // PAGOS DE SECRETARIA List<Row> boletasEmi =
	 * movimientoDAO.pagosSecretaria(fec);
	 * 
	 * // POR AHORA BOLETAS BANCO //List<Row> boletasEmi =
	 * facturacionService.getBoletas(fec);
	 * 
	 * //List<Row> boletasEmi = movimientoDAO.pagosSecretaria(fec);
	 * 
	 * 
	 * Integer i=0; List<BOL> boletas = new ArrayList<BOL>();
	 * 
	 * BigDecimal b = new BigDecimal("0.00");
	 * 
	 * b.setScale(2, BigDecimal.ROUND_UP);
	 * 
	 * for (Row row : boletasEmi) { i++; BOL bol = new BOL(); BigDecimal
	 * montoTotal = new BigDecimal(row.getString("monto_total"));
	 * bol.setNumeroFila(i);//VARIA correlativo
	 * bol.setNumeroBoleta(row.getString("nro_rec").substring(5, 11));//VARIA
	 * bol.setNumeroSerie(row.getString("nro_rec").substring(0, 4));//VARIA
	 * BANCO bol.setTipoDocumento("03");//CONSTANTE tipo de comprobante BOLETA
	 * bol.setImporteTotal(new BigDecimal(row.getString("monto_total")));//VARIA
	 * TOTAL ya deducido los descuentos
	 * bol.setNroDocumentoIdentidad(row.getString("nro_doc"));
	 * bol.setTipoDocumentoIdentidad(row.getString("tip_doc"));
	 * bol.setSumaTotalOperacionesGravadas(b);//SIEMPRE ES CERO
	 * bol.setSumaTotalOperacionesExoneradas(b);//SIEMPRE ES CERO if
	 * (montoTotal.compareTo(BigDecimal.ZERO)==0){
	 * bol.setSumaTotalOperacionesInafecta(b);//ES LO Q PAGFA REALMENTE ()
	 * bol.setSumaTotalOperacionesGratuitas(new
	 * BigDecimal(row.getString("monto"))); }else{
	 * bol.setSumaTotalOperacionesInafecta(montoTotal);
	 * bol.setSumaTotalOperacionesGratuitas(b); }
	 * bol.setTipoDocumentoIdentidad(row.getString("tip_doc")); if
	 * ("DNI".equals(row.getString("tip_doc")))
	 * bol.setTipoDocumentoIdentidad("1"); if
	 * ("PASAPORTE".equals(row.getString("tip_doc")))
	 * bol.setTipoDocumentoIdentidad("7"); if (row.getString("tip_doc")!=null &&
	 * row.getString("tip_doc").indexOf("EXTRAN")>-1)
	 * bol.setTipoDocumentoIdentidad("4");
	 * 
	 * bol.setSumaTotalIGV(b); boletas.add(bol);
	 * 
	 * 
	 * }
	 * 
	 * EIV eiv = new EIV();
	 * eiv.setTipoDocumento(Constants.TIPO_ARCHIVO_RESUMEN_DIARIO_BOLETA);
	 * eiv.setTipoCreacionDocumento(TipoCreacionDocumento.EMITIDO.getValue());
	 * 
	 * // eiv.setNombreArchivoTXT(documento); documento.setEIV(eiv);
	 * documento.setBols(boletas); //try { // generar documento XML y ZIP
	 * Documento documentoXML = new
	 * ProcesarTxt(documento).procesarDocumentos();//
	 * 
	 * } catch (Exception e) { result.setException(e); }
	 * 
	 * 
	 * 
	 * return result; }
	 */

	@RequestMapping(value = "/datosAlumnoPago/{id_mat}")
	public AjaxResponseBody getLista(@PathVariable Integer id_mat) {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
			result.setResult(academicoPagoDAO.datosAlumnoPago(id_mat).get(0));

		} catch (Exception e) {
			result.setException(e);
		}
		return result;

	}
	
	@RequestMapping(value = "/pagoMensualidad")
	public AjaxResponseBody pagoMensualidad() {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
			result.setResult(moduloService.valorParametro(EnumModulo.MODULO_TESORERIA.getValue(), EnumParametro.PAGO_MENSUALIDADES.getDescripcion()));

		} catch (Exception e) {
			result.setException(e);
		}
		return result;

	}
	
public String generatePDF(Impresion impresion)throws Exception{
		
		//String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();

		String rutacARPETA =  "C:/plantillas/";

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

		logger.debug("antes de llamar al jasper");
		 
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list);

		InputStream resource = new FileInputStream(rutacARPETA + "/boleta.jasper");
		logger.debug("llamo al jasper");
		
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(resource);
		
		logger.debug("despues del loadObject");
		
		resource.close();
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);
		
		logger.debug("despues de JasperFillManager.fillReport");
		
		String pdfGenerado =rutacARPETA + "/tmp/boleta" + (new Date()).toString().replaceAll(" ","").replaceAll(":", "") +".pdf";
		JasperExportManager.exportReportToPdfFile(jasperPrint, pdfGenerado);
		
		logger.debug("despues de JasperExportManager.exportReportToPdfFile");
		
		return pdfGenerado;
	}

	@RequestMapping(value = "/grabarBecas", method = RequestMethod.POST)
	@Transactional
	public AjaxResponseBody grabarBecas(Integer id_facadpag[],Integer id_fac[], Integer id_bec[], Integer id_mbec[], String monto[], String monto_afec[]) {
		AjaxResponseBody result = new AjaxResponseBody();
	
		try {
			for (int i = 0; i < id_fac.length; i++) {
				//Insertamos la beca
				AcapagBeca pagoBeca = new AcapagBeca();
				pagoBeca.setId_fac(id_fac[i]);
				pagoBeca.setId_bec(id_bec[i]);
				pagoBeca.setId_mbec(id_mbec[i]);
				pagoBeca.setMonto_total(new BigDecimal(monto[i]));
				pagoBeca.setMonto_afectado(new BigDecimal(monto_afec[i]));
				pagoBeca.setEst("A");
				acadBecaDAO.saveOrUpdate(pagoBeca);
				 //Actualizamos el Acadmico Pago
				AcademicoPago academicoPago= academicoPagoDAO.get(id_fac[i]);
				academicoPago.setDesc_beca(new BigDecimal(monto_afec[i]));
				academicoPago.setId_bec(id_bec[i]);
				academicoPagoDAO.saveOrUpdate(academicoPago);	
			}
			result.setResult(1);
	
		} catch (Exception e) {
			result.setException(e);
		}
	
		return result;
	
	}
	
	@RequestMapping(value = "/obtenemosMontoBeca/{id_bec}", method = RequestMethod.GET)
	public AjaxResponseBody obtenemosMontoBeca(@PathVariable Integer id_bec) {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
			Beca beca = becaDAO.get(id_bec);
			TipoDescuento tipoDescuento = tipoDescuentoDAO.get(beca.getId_tdes());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("tip_des", tipoDescuento.getCod());
			map.put("valor", beca.getVal());
			
			result.setResult(map);
			
		} catch (Exception e) {
			result.setException(e);
		}
		return result;
	}	
	
	@RequestMapping(value = "/exonerarMensualidad/{id_fac}", method = RequestMethod.POST)
	public AjaxResponseBody exonerarMensualidad(@PathVariable Integer id_fac) {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
			academicoPagoDAO.exonerarMensualidad(id_fac);
			result.setResult(1);
			
		} catch (Exception e) {
			result.setException(e);
		}
		return result;
	}	
	
	@RequestMapping(value = "/listarDescuentosAlumno/{id_anio}/{id_gir}", method = RequestMethod.GET)
	public AjaxResponseBody listarDescuentosAlumno(@PathVariable Integer id_anio, @PathVariable Integer id_gir) {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
			result.setResult(academicoPagoDAO.listarDescuentosAlumnosxAnio(id_anio, id_gir));
			
		} catch (Exception e) {
			result.setException(e);
		}
		return result;
	}	
}

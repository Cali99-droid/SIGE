package com.sige.spring.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.omg.PortableServer.ID_ASSIGNMENT_POLICY_ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sige.common.enums.EnumConceptoPago;
import com.sige.core.dao.cache.CacheManager;
import com.sige.invoice.bean.ComunicacionBajaBean;
import com.sige.invoice.bean.ComunicacionBajaItemBean;
import com.sige.invoice.bean.DocumentoReferencia;
import com.sige.invoice.bean.Impresion;
import com.sige.invoice.bean.ImpresionCabecera;
import com.sige.invoice.bean.ImpresionCliente;
import com.sige.invoice.bean.ImpresionDcto;
import com.sige.invoice.bean.ImpresionItem;
import com.sige.invoice.bean.SunatJson;
import com.sige.invoice.bean.SunatResultJson;
import com.sige.mat.dao.AcademicoPagoDAO;
import com.sige.mat.dao.AlumnoDAO;
import com.sige.mat.dao.AlumnoDescuentoDAO;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.AulaDAO;
import com.sige.mat.dao.BecaDAO;
import com.sige.mat.dao.CicloDAO;
import com.sige.mat.dao.ConceptoDAO;
import com.sige.mat.dao.ConfCuotaDAO;
import com.sige.mat.dao.ConfMensualidadDAO;
import com.sige.mat.dao.ConfReciboDAO;
import com.sige.mat.dao.DescuentoConfDAO;
import com.sige.mat.dao.DescuentoDAO;
import com.sige.mat.dao.EmpresaDAO;
import com.sige.mat.dao.ErrorDAO;
import com.sige.mat.dao.FamiliarDAO;
import com.sige.mat.dao.GruFamAlumnoDAO;
import com.sige.mat.dao.GruFamDAO;
import com.sige.mat.dao.GruFamFamiliarDAO;
import com.sige.mat.dao.InscripcionCampusDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.MovimientoDAO;
import com.sige.mat.dao.MovimientoDescuentoDAO;
import com.sige.mat.dao.MovimientoDetalleDAO;
import com.sige.mat.dao.NotaCreditoDAO;
import com.sige.mat.dao.PeriodoDAO;
import com.sige.mat.dao.PersonaDAO;
import com.sige.mat.dao.ReservaDAO;
import com.sige.mat.dao.SitHistorialDAO;
import com.sige.mat.dao.SucursalDAO;
import com.sige.mat.dao.TarifasEmergenciaDAO;
import com.sige.mat.dao.TrasladoDetalleDAO;
import com.sige.mat.dao.UsuarioCampusDAO;
import com.sige.rest.request.ErrorReq;
import com.sige.rest.request.PagoRequest;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.AcademicoPago;
import com.tesla.colegio.model.AcapagBeca;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.AlumnoDescuento;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.Beca;
import com.tesla.colegio.model.Ciclo;
import com.tesla.colegio.model.ComunicacionBaja;
import com.tesla.colegio.model.Concepto;
import com.tesla.colegio.model.ConfCuota;
import com.tesla.colegio.model.ConfMensualidad;
import com.tesla.colegio.model.ConfRecibo;
import com.tesla.colegio.model.Descuento;
import com.tesla.colegio.model.DescuentoConf;
import com.tesla.colegio.model.Error;
import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.GruFam;
import com.tesla.colegio.model.GruFamAlumno;
import com.tesla.colegio.model.GruFamFamiliar;
import com.tesla.colegio.model.InscripcionCampus;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Movimiento;
import com.tesla.colegio.model.MovimientoDescuento;
import com.tesla.colegio.model.MovimientoDetalle;
import com.tesla.colegio.model.ReservaCuota;
import com.tesla.colegio.model.SitHistorial; 
import com.tesla.colegio.model.Sucursal;
import com.tesla.colegio.model.TrasladoDetalle;
import com.tesla.colegio.model.Usuario;
import com.tesla.colegio.model.UsuarioCampus;
import com.tesla.colegio.model.UsuarioSeg;
import com.tesla.colegio.model.bean.MatriculaPagos;
import com.tesla.colegio.model.NotaCredito;
import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.Persona;
import com.tesla.colegio.model.Reserva;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.common.exceptions.ServiceException;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.CorreoUtil;
import com.tesla.frmk.util.FechaUtil;
import com.tesla.frmk.util.NumberUtil;
import com.tesla.frmk.util.RestUtil;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

@Service
//@PropertySource("classpath:config.properties")
public class FacturacionService {

	final static Logger logger = Logger.getLogger(FacturacionService.class);

	@Autowired
	private Environment env;
	
	@Autowired
	private ConfReciboDAO confReciboDAO;

	@Autowired
	private SucursalDAO sucursalDAO;

	@Autowired
	private AcademicoPagoDAO academicoPagoDAO;

	@Autowired
	private MatriculaDAO matriculaDAO;

	@Autowired
	private MovimientoDAO movimientoDAO;

	@Autowired
	private MovimientoDetalleDAO movimientoDetalleDAO;

	@Autowired
	private MovimientoDescuentoDAO movimientoDescuentoDAO;

	@Autowired
	private TrasladoDetalleDAO trasladoDetalleDAO;

	@Autowired
	private SitHistorialDAO sitHistorialDAO;

	@Autowired
	private ConceptoDAO conceptoDAO;

	@Autowired
	private AulaDAO aulaDAO;
	
	@Autowired
	private AnioDAO anioDAO;

	@Autowired
	private ConfCuotaDAO confCuotaDAO;
	
	@Autowired
	private FamiliarDAO familiarDAO;
	
	@Autowired
	private ReservaDAO reservaDAO;

	@Autowired
	private ConfMensualidadDAO confMensualidadDAO;

	@Autowired
	private TokenSeguridad tokenSeguridad;
	
	@Autowired
	private NotaCreditoDAO notaCreditoDAO;
	
	@Autowired
	private EmpresaDAO empresaDAO;
	
	@Autowired
	private AlumnoDAO alumnoDAO;
	
	@Autowired
	private PeriodoDAO periodoDAO;
	
	@Autowired
	private InscripcionCampusDAO inscripcionCampusDAO;
	
	@Autowired
	private UsuarioCampusDAO usuarioCampusDAO;
	
	@Autowired
	private ErrorDAO errorDAO;
	
	@Autowired
	private PersonaDAO personaDAO;
	
	@Autowired
	private AlumnoDescuentoDAO alumnoDescuentoDAO;
	
	@Autowired
	private DescuentoConfDAO descuentoConfDAO;
	
	@Autowired
	private DescuentoDAO descuentoDAO;
	
	@Autowired
	private TarifasEmergenciaDAO tarifasEmergenciaDAO;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private VacanteService vacanteService;
	
	@Autowired
	private BecaDAO becaDAO;
	
	@Autowired
	private GruFamFamiliarDAO gruFamFamiliarDAO;
	
	@Autowired
	private GruFamAlumnoDAO gruFamAlumnoDAO;
	
	@Autowired
	private GruFamDAO gruFamDAO;
	
	@Autowired
	private CicloDAO cicloDAO;
	
	public void updateNroRecibo(Integer id_suc, String numero) throws ServiceException {
		Param param = new Param();
		param.put("id_suc", id_suc);
		param.put("est", "A");
		// param.put("serie", serie);

		ConfRecibo confRecibo = confReciboDAO.getByParams(param);
		confRecibo.setNumero(Integer.parseInt(numero.split("-")[1]));
		confReciboDAO.saveOrUpdate(confRecibo);
	}
	
	public void updateNroReciboAcadVac(String serie, String numero) throws ServiceException {
		Param param = new Param();
		param.put("serie", serie);
		param.put("est", "A");
		// param.put("serie", serie);

		ConfRecibo confRecibo = confReciboDAO.getByParams(param);
		confRecibo.setId_suc(null);
		confRecibo.setNumero(Integer.parseInt(numero.split("-")[1]));
		confReciboDAO.saveOrUpdate(confRecibo);
	}

	public void updateNroReciboNotaCredito(Integer id_suc, String numero) throws ServiceException {
		Param param = new Param();
		param.put("id_suc", id_suc);
		param.put("est", "A");


		ConfRecibo confRecibo = confReciboDAO.getByParams(param);
		confRecibo.setNumero_nc(Integer.parseInt(numero.split("-")[1]));
		confReciboDAO.saveOrUpdate(confRecibo);
	}

	
	public void updateNroReciboBanco(Integer id_bco, String numero) throws ServiceException {
		Param param = new Param();
		param.put("id_bco", id_bco);
		param.put("est", "A");
		// param.put("serie", serie);

		List<Row> row = confReciboDAO.listConfBanco(param, null);
		row.get(0).put("numero", Integer.parseInt(numero.split("-")[1]));
		confReciboDAO.saveOrUpdateBanco(row.get(0));
	}

	public String getNroRecibo(Integer id_suc) throws ServiceException {

		Param param = new Param();
		param.put("id_suc", id_suc);
		param.put("est", "A");

		List<ConfRecibo> confRecibo = confReciboDAO.listByParams(param, null);

		if (confRecibo.size() == 0)
			throw new ServiceException("No existe configurado un numero de serie para el local");

		if (confRecibo.size() > 1)
			throw new ServiceException("Existe mas de numero de serie vigente para el local");

		String serie = confRecibo.get(0).getSerie();
		Integer numero = confRecibo.get(0).getNumero() + 1;

		if (confRecibo.get(0).getHasta() != null && confRecibo.get(0).getHasta() < numero)
			throw new ServiceException("El numero correlativo no puede ser mayor que al maximo numero configurado");

		String serieMask;
		if (confRecibo.get(0).getTipo().equals("E"))// electronico
			serieMask = serie;
		else
			serieMask = String.format("%04d", Integer.parseInt(serie));

		String numeroMask = String.format("%06d", numero);

		return serieMask + "-" + numeroMask;
	}
	
	public String getNroReciboAcadVac(String serie_rec) throws ServiceException {

		Param param = new Param();
		param.put("serie", serie_rec);
		param.put("est", "A");

		List<ConfRecibo> confRecibo = confReciboDAO.listByParams(param, null);

		if (confRecibo.size() == 0)
			throw new ServiceException("No existe configurado un numero de serie para el local");

		if (confRecibo.size() > 1)
			throw new ServiceException("Existe mas de numero de serie vigente para el local");

		String serie = confRecibo.get(0).getSerie();
		Integer numero = confRecibo.get(0).getNumero() + 1;

		if (confRecibo.get(0).getHasta() != null && confRecibo.get(0).getHasta() < numero)
			throw new ServiceException("El numero correlativo no puede ser mayor que al maximo numero configurado");

		String serieMask;
		if (confRecibo.get(0).getTipo().equals("E"))// electronico
			serieMask = serie;
		else
			serieMask = String.format("%04d", Integer.parseInt(serie));

		String numeroMask = String.format("%06d", numero);

		return serieMask + "-" + numeroMask;
	}

	public String getNroReciboNotaCredito(Integer id_suc) throws ServiceException {

		Param param = new Param();
		param.put("id_suc", id_suc);
		param.put("est", "A");

		
		List<ConfRecibo> confRecibo = confReciboDAO.listByParams(param, null);

		if (confRecibo.size() == 0)
			throw new ServiceException("No existe configurado un numero de serie para el local");

		if (confRecibo.size() > 1)
			throw new ServiceException("Existe mas de numero de serie vigente para el local");

		String serie = confRecibo.get(0).getSerie().replaceAll("B00", "B01");
		Integer numero = confRecibo.get(0).getNumero_nc() + 1;;//@TODO confRecibo.get(0).getNum_cre() + 1;

		String serieMask;
		if (confRecibo.get(0).getTipo().equals("E"))// electronico
			serieMask = serie;
		else
			serieMask = String.format("%04d", Integer.parseInt(serie));

		String numeroMask = String.format("%06d", numero);

		return serieMask + "-" + numeroMask;
	}
	
	public String getNroReciboBanco(Integer id_bco) throws ServiceException {

		Param param = new Param();
		param.put("id_bco", id_bco);
		param.put("est", "A");

		List<Row> confRecibo = confReciboDAO.listConfBanco(param, null);

		if (confRecibo.size() == 0)
			throw new ServiceException("No existe configurado un numero de serie para el banco");

		if (confRecibo.size() > 1)
			throw new ServiceException("Existe mas de numero de serie vigente para el banco");

		String serie = confRecibo.get(0).getString("serie");
		Integer numero = confRecibo.get(0).getInteger("numero") + 1;

		// if (confRecibo.get(0).getHasta()!=null &&
		// confRecibo.get(0).getHasta()<numero)
		// throw new ServiceException("El numero correlativo no puede ser mayor
		// que al maximo numero configurado");

		String serieMask;
		if (confRecibo.get(0).getString("tipo").equals("E"))// electronico
			serieMask = serie;
		else
			serieMask = String.format("%04d", Integer.parseInt(serie));

		String numeroMask = String.format("%06d", numero);

		return serieMask + "-" + numeroMask;
	}

	@Transactional(rollbackFor = ServiceException.class)
	public Impresion pagarMensualidad(PagoRequest pago, Integer id_suc) throws ServiceException {
		String[] arrPAgos = pago.getId().substring(1).split("\\|");
		String[] arrDescuento = pago.getDescuento().substring(1).split("\\|");
		String[] arrDescuentoSecretaria = pago.getDescuento_secretaria().substring(1).split("\\|");
		String[] arrDescuentoPersonalizado = pago.getDescuento_personalizado().substring(1).split("\\|");

		Integer idPago = Integer.parseInt(arrPAgos[0]);
		AcademicoPago academicoPago = academicoPagoDAO.get(idPago);

		
		String nro_rec = getNroRecibo(id_suc);


		UsuarioSeg usuarioSeg = tokenSeguridad.getUsuarioSeg();

		logger.debug("nuevo recibo:" + nro_rec);
		//Datos de la empresa
		Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);

		// INICIO - FACTURA ELECTRONICA

		Impresion impresion = new Impresion();
		ImpresionCabecera impresionCabecera = new ImpresionCabecera();
		impresionCabecera.setNombreBoleta("BOLETA ELECTRÓNICA");
		impresionCabecera.setNro(nro_rec);
		impresionCabecera.setDia(FechaUtil.toString(new Date(), "dd/MM/yyyy"));
		impresionCabecera.setComercio(empresa.getString("giro_negocio").toUpperCase());
		impresionCabecera.setHora(FechaUtil.toString(new Date(), "hh:mm a"));
		impresionCabecera.setUsuario(usuarioSeg.getNombres());
		Sucursal local = sucursalDAO.get(usuarioSeg.getId_suc());
		impresionCabecera.setTelefono(local.getTel());
		impresionCabecera.setLocal(local.getDir());

		impresion.setCabecera(impresionCabecera);
		ImpresionCliente impresionCliente = new ImpresionCliente();
		Row datosCliente = academicoPagoDAO.obtenerDatosCliente(idPago);
		/*
		 * if(datosCliente==null){ result.setCode("500");
		 * result.setMsg("No se pudo obtener los datos del cliente"); }
		 */

		impresionCliente.setDireccion(datosCliente.get("direccion") == null ? "" : datosCliente.getString("direccion"));
		impresionCliente.setNombre(datosCliente.getString("nombres"));
		impresionCliente.setTip_doc(datosCliente.getString("tip_doc"));
		impresionCliente.setNro_doc(datosCliente.getString("nro_doc"));
		impresion.setCliente(impresionCliente);

		// FIN - FACTURA ELECTRONICA

		BigDecimal descuentoTotal = new BigDecimal(0);
		BigDecimal monto = new BigDecimal(0);
		Integer id_fam = null;
		List<MovimientoDetalle> movimientoDetalles = new ArrayList<MovimientoDetalle>();

		for (int i = 0; i < arrPAgos.length; i++) {
			Integer id = Integer.parseInt(arrPAgos[i]);
			academicoPago = academicoPagoDAO.get(id);

			// INICIO - FACTURA ELECTRONICA
			Row datosAlumno = matriculaDAO.getDatosAlumno(academicoPago.getId_mat());
			id_fam = datosAlumno.getInteger("id_fam");
			BigDecimal descuentoItem = BigDecimal.ZERO;
			ImpresionItem impresionItem = new ImpresionItem();
			impresionItem.setNro(i + 1);
			String descripcion;

			if ((new BigDecimal(arrDescuento[i]).compareTo(new BigDecimal(0))) > 0) {
				descripcion = "Descuento por hermano";
				descripcion += "\nMensualidad de " + Constante.MES[academicoPago.getMens() - 1];
				descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
						+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);
				// descripcion = "Descuento por hermano\n";
				impresionItem.getDescuentos()
						.add(new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(arrDescuento[i])));
				descuentoItem = descuentoItem.add(new BigDecimal(arrDescuento[i]));
			}
			if ((new BigDecimal(arrDescuentoSecretaria[i]).compareTo(new BigDecimal(0))) > 0) {
				descripcion = "Descuento pronto pago ";
				descripcion += "\nMensualidad de " + Constante.MES[academicoPago.getMens() - 1];
				descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
						+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);
				impresionItem.getDescuentos()
						.add(new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(arrDescuentoSecretaria[i])));
				descuentoItem = descuentoItem.add(new BigDecimal(arrDescuentoSecretaria[i]));
			}
			if ((new BigDecimal(arrDescuentoPersonalizado[i]).compareTo(new BigDecimal(0))) > 0) {
				descripcion = "Descuento pronto pago ";
				descripcion += "\nMensualidad de " + Constante.MES[academicoPago.getMens() - 1];
				descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
						+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);
				impresionItem.getDescuentos().add(
						new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(arrDescuentoPersonalizado[i])));
				descuentoItem = descuentoItem.add(new BigDecimal(arrDescuentoPersonalizado[i]));
			}

			descripcion = "Mensualidad de " + Constante.MES[academicoPago.getMens() - 1] + "\n"
					+ datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
					+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);

			impresionItem.setDescripcion(descripcion.toUpperCase());
			impresionItem.setPrecioUnit(academicoPago.getMonto());
			impresionItem.setCantidad(1);
			impresionItem.setTipoOperacion("OPE_INA");
			// FIN - FACTURA ELECTRONICA

			academicoPago.setCanc("1");
			academicoPago.setDesc_hermano(new BigDecimal(arrDescuento[i]));
			academicoPago.setDesc_pronto_pago(new BigDecimal(arrDescuentoSecretaria[i]));
			academicoPago.setDesc_personalizado(new BigDecimal(arrDescuentoPersonalizado[i]));
			academicoPago.setDesc_pago_adelantado(BigDecimal.ZERO);
			academicoPago.setNro_rec(nro_rec);
			academicoPago.setFec_pago(new Date());

			MovimientoDetalle movimientoDetalle = new MovimientoDetalle();
			movimientoDetalle.setId_fco(Constante.CONCEPTO_MENSUALIDAD);
			movimientoDetalle.setEst("A");
			movimientoDetalle.setMonto(academicoPago.getMonto());
			movimientoDetalle.setObs(descripcion.replaceAll("\n", " ").toUpperCase());

			movimientoDetalle.getDescuentos()
					.add(new MovimientoDescuento(academicoPago.getDesc_hermano(), "DESCUENTO POR HERMANO"));
			movimientoDetalle.getDescuentos()
					.add(new MovimientoDescuento(academicoPago.getDesc_pronto_pago(), "DESCUENTO POR PRONTO PAGO"));
			movimientoDetalle.getDescuentos()
					.add(new MovimientoDescuento(academicoPago.getDesc_personalizado(), "DESCUENTO POR PRONTO PAGO"));

			if (arrPAgos.length == 10 && i == 9) {

				Calendar cal = Calendar.getInstance();
				int anio_actual = cal.get(Calendar.YEAR);
				int fecVencimiento = getFecVencimiento(anio_actual, 4, Integer.parseInt(datosAlumno.get("id_per").toString()));// TODO
																		// ver el mes 

				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

				String format = formatter.format(new Date());
				int fecActual = Integer.parseInt(format);

				if (fecActual <= fecVencimiento) {
					BigDecimal montoTotalAPagar = academicoPago.getMonto().subtract(academicoPago.getDesc_hermano())
							.subtract(academicoPago.getDesc_pronto_pago())
							.subtract(academicoPago.getDesc_personalizado());
					BigDecimal descuentoAdelantado = montoTotalAPagar.divide(new BigDecimal(2));
					BigDecimal descuentoTotalDic = descuentoAdelantado.add(academicoPago.getDesc_hermano())
							.add(academicoPago.getDesc_pronto_pago()).add(academicoPago.getDesc_personalizado());

					// descuentoTotal = descuentoTotal.add(descuentoTotalDic);

					academicoPago.setDesc_pago_adelantado(descuentoAdelantado);
					academicoPago.setMontoTotal(academicoPago.getMonto().subtract(descuentoTotalDic));

					descuentoItem = descuentoItem.add(descuentoAdelantado);
					movimientoDetalle.getDescuentos()
							.add(new MovimientoDescuento(descuentoAdelantado, "DESCUENTO POR PAGO ADELANTADO"));

					descripcion = "Descuento pago adelantado";
					descripcion += "\nMensualidad de " + Constante.MES[academicoPago.getMens() - 1];
					descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
							+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);
					impresionItem.getDescuentos()
							.add(new ImpresionDcto(descripcion.toUpperCase(), descuentoAdelantado));
					// descuentoItem = descuentoItem.add(new
					// BigDecimal(arrDescuentoPersonalizado[i]));

				}
			}

			academicoPago.setMontoTotal(academicoPago.getMonto().subtract(academicoPago.getDesc_hermano())
					.subtract(academicoPago.getDesc_pronto_pago()).subtract(academicoPago.getDesc_personalizado())
					.subtract(academicoPago.getDesc_pago_adelantado()));

			impresionItem.setPrecio(academicoPago.getMonto());
			impresionItem.setDescuento(descuentoItem);

			impresion.getItems().add(impresionItem);

			movimientoDetalle.setMonto_total(academicoPago.getMonto().subtract(descuentoItem));

			movimientoDetalle.setDescuento(descuentoItem);

			movimientoDetalles.add(movimientoDetalle);

			descuentoTotal = descuentoTotal.add(descuentoItem);
			monto = monto.add(academicoPago.getMonto());

			academicoPagoDAO.saveOrUpdate(academicoPago);

		}

		// INICIO - se graba movimiento final
		Movimiento movimiento = new Movimiento();
		movimiento.setEst("A");
		movimiento.setFec(new Date());
		movimiento.setId_fam(id_fam);
		movimiento.setId_suc(id_suc);
		movimiento.setMonto(monto);
		movimiento.setDescuento(descuentoTotal);
		movimiento.setMonto_total(monto.subtract(descuentoTotal));
		movimiento.setObs("PAGO DE MENSUALIDAD");
		movimiento.setId_fpa(1);// efectivo
		movimiento.setTipo("I");
		movimiento.setNro_rec(nro_rec);

		Integer id_mov = movimientoDAO.saveOrUpdate(movimiento);
		for (MovimientoDetalle movimientoDetalle : movimientoDetalles) {
			movimientoDetalle.setId_fmo(id_mov);
			Integer id_fmd = movimientoDetalleDAO.saveOrUpdate(movimientoDetalle);

			/* Grabar movimiento descuentos */
			for (MovimientoDescuento movimientoDescuento : movimientoDetalle.getDescuentos()) {

				grabarMovimientoDescuento(id_fmd, movimientoDescuento.getDes(), movimientoDescuento.getDescuento());

			}

		}
		// FIN - se graba movimiento final

		updateNroRecibo(id_suc, nro_rec);

		return impresion;
}
	
	@Transactional(rollbackFor = ServiceException.class)
	public List<Impresion> pagarMensualidadGeneral(PagoRequest pago, Integer id_suc, Integer id_per_pag) throws ServiceException {
		List<Impresion> impresiones = new ArrayList<Impresion>();
		String[] arrPAgos = pago.getId().substring(1).split("\\|");
		String[] arrDescuento = pago.getDescuento().substring(1).split("\\|");
		String[] arrDescuentoSecretaria = pago.getDescuento_secretaria().substring(1).split("\\|");
		String[] arrDescuentoPersonalizado = pago.getDescuento_personalizado().substring(1).split("\\|");
		String[] arrDescuentoPagoAdelantado = pago.getDescuento_pago_ade().substring(1).split("\\|");
		String[] arrDescuentoBeca = pago.getDescuento_beca().substring(1).split("\\|");
		String[] tipo_pago = pago.getTipo_pago().substring(1).split("\\|");
		String nro_rec_col="";
		String nro_rec_acad="";
		String nro_rec_vac="";
		UsuarioSeg usuarioSeg = tokenSeguridad.getUsuarioSeg();
		//logger.debug("nuevo recibo:" + nro_rec);
		//Datos de la empresa
		Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);
		Impresion impresion_colegio = new Impresion();
		ImpresionCabecera impresionCabecera_colegio = new ImpresionCabecera();
		ImpresionCliente impresionCliente_colegio = new ImpresionCliente();
		BigDecimal monto_colegio = new BigDecimal(0);
		BigDecimal descuentoTotal_colegio = new BigDecimal(0);
		List<MovimientoDetalle> movimientoDetalles_colegio = new ArrayList<MovimientoDetalle>();
		//Academia
		Impresion impresion_academia = new Impresion();
		ImpresionCabecera impresionCabecera_academia = new ImpresionCabecera();
		ImpresionCliente impresionCliente_academia = new ImpresionCliente();
		BigDecimal monto_academia = new BigDecimal(0);
		BigDecimal descuentoTotal_academia = new BigDecimal(0);
		List<MovimientoDetalle> movimientoDetalles_academia = new ArrayList<MovimientoDetalle>();
		//Vacaciones
		Impresion impresion_vacaciones = new Impresion();
		ImpresionCabecera impresionCabecera_vacaciones = new ImpresionCabecera();
		ImpresionCliente impresionCliente_vacaciones = new ImpresionCliente();
		BigDecimal monto_vacaciones = new BigDecimal(0);
		BigDecimal descuentoTotal_vacaciones = new BigDecimal(0);
		List<MovimientoDetalle> movimientoDetalles_vacaciones = new ArrayList<MovimientoDetalle>();
		//Agrupamos los pagos segun el tipo
		for (int i = 0; i < arrPAgos.length; i++) {
			Integer idPago = Integer.parseInt(arrPAgos[i]);
			AcademicoPago academicoPago = academicoPagoDAO.get(idPago);
			if(tipo_pago[i].equals("C")) {				
				Integer id_fam = null;
				
				//Verificar si ya tiene asignado un número
				if(nro_rec_col.equals("")) {
					nro_rec_col = getNroRecibo(id_suc);
					// INICIO - FACTURA ELECTRONICA
					impresionCabecera_colegio.setNombreBoleta("BOLETA ELECTRÓNICA");
					impresionCabecera_colegio.setNro(nro_rec_col);
					impresionCabecera_colegio.setDia(FechaUtil.toString(new Date(), "dd/MM/yyyy"));
					impresionCabecera_colegio.setComercio(empresa.getString("giro_negocio").toUpperCase());
					impresionCabecera_colegio.setHora(FechaUtil.toString(new Date(), "hh:mm a"));
					impresionCabecera_colegio.setUsuario(usuarioSeg.getNombres());
					Sucursal local = sucursalDAO.get(usuarioSeg.getId_suc());
					impresionCabecera_colegio.setTelefono(local.getTel());
					impresionCabecera_colegio.setLocal(local.getDir());
					impresion_colegio.setCabecera(impresionCabecera_colegio);
					//Row datosCliente = academicoPagoDAO.obtenerDatosCliente(idPago);
					Persona datosCliente = personaDAO.listFullByParams(new Param("per.id",id_per_pag), null).get(0);
					/*
					 * if(datosCliente==null){ result.setCode("500");
					 * result.setMsg("No se pudo obtener los datos del cliente"); }
					 */
					impresionCliente_colegio.setDireccion(datosCliente.getDir() == null ? "" : datosCliente.getDir());
					String ape_pat=datosCliente.getApe_pat() == null ? "" : datosCliente.getApe_pat().toUpperCase();
					String ape_mat=datosCliente.getApe_mat() == null ? "" : datosCliente.getApe_mat().toUpperCase();
					String nom=datosCliente.getNom()== null ? "" : datosCliente.getNom().toUpperCase();
					String direccion=datosCliente.getDir()==null ? "" : datosCliente.getDir().toUpperCase();
					impresionCliente_colegio.setNombre(ape_pat+" "+ape_mat+" "+nom);
					impresionCliente_colegio.setTip_doc(datosCliente.getTipoDocumento().getNom());
					impresionCliente_colegio.setNro_doc(datosCliente.getNro_doc());
					impresionCliente_colegio.setDireccion(direccion);
					impresion_colegio.setCliente(impresionCliente_colegio);

					// FIN - FACTURA ELECTRONICA

				}
				
				Integer id = Integer.parseInt(arrPAgos[i]);
				academicoPago = academicoPagoDAO.get(id);

				// INICIO - FACTURA ELECTRONICA
				Row datosAlumno = matriculaDAO.getDatosAlumno(academicoPago.getId_mat());
				id_fam = datosAlumno.getInteger("id_fam");
				BigDecimal descuentoItem = BigDecimal.ZERO;
				ImpresionItem impresionItem = new ImpresionItem();
				impresionItem.setNro(i + 1);
				String descripcion="";
				BigDecimal monto = new BigDecimal(0);
				if(academicoPago.getTip().equals("MEN")) {
					Row tarifa=null;
					Periodo periodo= periodoDAO.get(datosAlumno.getInteger("id_per"));
					if(!academicoPago.getMens().equals(0)) {
						tarifa= tarifasEmergenciaDAO.listarTarifasMEs(academicoPago.getMens(),periodo.getId_anio());
					}
					
					System.out.println("tarifa->" + tarifa);
					//System.out.println(pagosList.get(i).get("));
					//BigDecimal desc_hermano = new BigDecimal(0);
					
					if(tarifa!=null){
						monto=tarifa.getBigDecimal("monto");
						//desc_hermano=tarifa.getBigDecimal("des_hermano");
						//System.out.println("desc_hermano->" + desc_hermano);
					} else{
						monto=academicoPago.getMonto();
						/*if(!academicoPago.getMens().equals(0))
							desc_hermano = new BigDecimal(10);
						else
							desc_hermano = new BigDecimal(0);*/
					}
					

					if ((new BigDecimal(arrDescuento[i]).compareTo(new BigDecimal(0))) > 0) {
						descripcion = "Descuento por hermano";
						//descripcion += "\nMensualidad de " + Constante.MES[academicoPago.getMens() - 1];
						//descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
						//		+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);
						// descripcion = "Descuento por hermano\n";
						impresionItem.getDescuentos()
								.add(new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(arrDescuento[i])));
						descuentoItem = descuentoItem.add(new BigDecimal(arrDescuento[i]));
					}
					if ((new BigDecimal(arrDescuentoSecretaria[i]).compareTo(new BigDecimal(0))) > 0) {
						descripcion = "Descuento pronto pago ";
						//descripcion += "\nMensualidad de " + Constante.MES[academicoPago.getMens() - 1];
						//descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
						//		+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);
						impresionItem.getDescuentos()
								.add(new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(arrDescuentoSecretaria[i])));
						descuentoItem = descuentoItem.add(new BigDecimal(arrDescuentoSecretaria[i]));
					}
					if ((new BigDecimal(arrDescuentoPersonalizado[i]).compareTo(new BigDecimal(0))) > 0) {
						descripcion = "Descuento personalizado ";
						//descripcion += "\nMensualidad de " + Constante.MES[academicoPago.getMens() - 1];
						//descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
						//		+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);
						impresionItem.getDescuentos().add(
								new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(arrDescuentoPersonalizado[i])));
						descuentoItem = descuentoItem.add(new BigDecimal(arrDescuentoPersonalizado[i]));
					}
					if ((new BigDecimal(arrDescuentoPagoAdelantado[i]).compareTo(new BigDecimal(0))) > 0) {
						descripcion = "Descuento pago adelantado ";
						//descripcion += "\nMensualidad de " + Constante.MES[academicoPago.getMens() - 1];
						//descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
						//		+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);
						impresionItem.getDescuentos().add(
								new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(arrDescuentoPagoAdelantado[i])));
						descuentoItem = descuentoItem.add(new BigDecimal(arrDescuentoPagoAdelantado[i]));
					}
					if ((new BigDecimal(arrDescuentoBeca[i]).compareTo(new BigDecimal(0))) > 0) {
						//Hallamos la descripcion de la beca
						Beca beca = becaDAO.get(academicoPago.getId_bec());
						descripcion = "Descuento por "+beca.getNom();
						//descripcion += "\nMensualidad de " + Constante.MES[academicoPago.getMens() - 1];
						//descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
						//		+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);
						impresionItem.getDescuentos().add(
								new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(arrDescuentoBeca[i])));
						descuentoItem = descuentoItem.add(new BigDecimal(arrDescuentoBeca[i]));
					}					

					descripcion = "Mensualidad de " + Constante.MES[academicoPago.getMens() - 1] + "\n"
							+ datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
							+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);
				} else if(academicoPago.getTip().equals("MAT")) {
					if ((new BigDecimal(arrDescuento[i]).compareTo(new BigDecimal(0))) > 0) {
						descripcion = "Descuento por hermano";
						descripcion += "\nMatrícula de ";
						descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
								 + datosAlumno.get("secc")+" "+ datosAlumno.getString("nivel").substring(0, 3); //+ datosAlumno.get("secc") + " "
						// descripcion = "Descuento por hermano\n";
						impresionItem.getDescuentos()
								.add(new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(arrDescuento[i])));
						descuentoItem = descuentoItem.add(new BigDecimal(arrDescuento[i]));
					}
					if ((new BigDecimal(arrDescuentoSecretaria[i]).compareTo(new BigDecimal(0))) > 0) {
						descripcion = "Descuento pronto pago ";
						descripcion += "\nMatrícula de ";
						descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
								 +  datosAlumno.get("secc")+" "+datosAlumno.getString("nivel").substring(0, 3); //+ datosAlumno.get("secc") + " "
						impresionItem.getDescuentos()
								.add(new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(arrDescuentoSecretaria[i])));
						descuentoItem = descuentoItem.add(new BigDecimal(arrDescuentoSecretaria[i]));
					}
					if ((new BigDecimal(arrDescuentoPersonalizado[i]).compareTo(new BigDecimal(0))) > 0) {
						descripcion = "Descuento personalizado ";
						descripcion += "\nMatrícula de ";
						descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
								 +  datosAlumno.get("secc")+" "+datosAlumno.getString("nivel").substring(0, 3); //+ datosAlumno.get("secc") + " "
						impresionItem.getDescuentos().add(
								new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(arrDescuentoPersonalizado[i])));
						descuentoItem = descuentoItem.add(new BigDecimal(arrDescuentoPersonalizado[i]));
					}
					if ((new BigDecimal(arrDescuentoPagoAdelantado[i]).compareTo(new BigDecimal(0))) > 0) {
						descripcion = "Descuento pago adelantado ";
						descripcion += "\nMatrícula de ";
						descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
								 +  datosAlumno.get("secc")+" "+datosAlumno.getString("nivel").substring(0, 3); //+ datosAlumno.get("secc") + " "
						impresionItem.getDescuentos().add(
								new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(arrDescuentoPagoAdelantado[i])));
						descuentoItem = descuentoItem.add(new BigDecimal(arrDescuentoPagoAdelantado[i]));
					}
					if ((new BigDecimal(arrDescuentoBeca[i]).compareTo(new BigDecimal(0))) > 0) {
						//Hallamos la descripcion de la beca
						Beca beca = becaDAO.get(academicoPago.getId_bec());
						descripcion = "Descuento por "+beca.getNom();
						descripcion += "\nMatrícula de ";
						descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
								 +  datosAlumno.get("secc")+" "+datosAlumno.getString("nivel").substring(0, 3); //+ datosAlumno.get("secc") + " "
						impresionItem.getDescuentos().add(
								new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(arrDescuentoPagoAdelantado[i])));
						descuentoItem = descuentoItem.add(new BigDecimal(arrDescuentoPagoAdelantado[i]));
					}					

					descripcion = "Matrícula de \n"
							+ datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
							 +  datosAlumno.get("secc")+" "+datosAlumno.getString("nivel").substring(0, 3); //+ datosAlumno.get("secc") + " "
					//Valido la matricula
					Matricula matricula = matriculaDAO.getByParams(new Param("id",academicoPago.getId_mat()));
					matriculaDAO.validarMatricula(matricula.getId());
				} else if(academicoPago.getTip().equals("ING")) {
					if ((new BigDecimal(arrDescuento[i]).compareTo(new BigDecimal(0))) > 0) {
						descripcion = "Descuento por hermano";
						descripcion += "\nCuota de Ingreso de ";
						descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
								 +  datosAlumno.get("secc")+" "+datosAlumno.getString("nivel").substring(0, 3); //+ datosAlumno.get("secc") + " "
						// descripcion = "Descuento por hermano\n";
						impresionItem.getDescuentos()
								.add(new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(arrDescuento[i])));
						descuentoItem = descuentoItem.add(new BigDecimal(arrDescuento[i]));
					}
					if ((new BigDecimal(arrDescuentoSecretaria[i]).compareTo(new BigDecimal(0))) > 0) {
						descripcion = "Descuento pronto pago ";
						descripcion += "\nCuota de Ingreso de ";
						descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
								 +  datosAlumno.get("secc")+" "+datosAlumno.getString("nivel").substring(0, 3); //+ datosAlumno.get("secc") + " "
						impresionItem.getDescuentos()
								.add(new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(arrDescuentoSecretaria[i])));
						descuentoItem = descuentoItem.add(new BigDecimal(arrDescuentoSecretaria[i]));
					}
					if ((new BigDecimal(arrDescuentoPersonalizado[i]).compareTo(new BigDecimal(0))) > 0) {
						descripcion = "Descuento personalizado ";
						descripcion += "\nCuota de Ingreso de ";
						descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
								 +  datosAlumno.get("secc")+" "+datosAlumno.getString("nivel").substring(0, 3); //+ datosAlumno.get("secc") + " "
						impresionItem.getDescuentos().add(
								new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(arrDescuentoPersonalizado[i])));
						descuentoItem = descuentoItem.add(new BigDecimal(arrDescuentoPersonalizado[i]));
					}
					if ((new BigDecimal(arrDescuentoPagoAdelantado[i]).compareTo(new BigDecimal(0))) > 0) {
						descripcion = "Descuento pago adelantado ";
						descripcion += "\nCuota de Ingreso de ";
						descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
								 +  datosAlumno.get("secc")+" "+datosAlumno.getString("nivel").substring(0, 3); //+ datosAlumno.get("secc") + " "
						impresionItem.getDescuentos().add(
								new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(arrDescuentoPagoAdelantado[i])));
						descuentoItem = descuentoItem.add(new BigDecimal(arrDescuentoPagoAdelantado[i]));
					}
					if ((new BigDecimal(arrDescuentoBeca[i]).compareTo(new BigDecimal(0))) > 0) {
						//Hallamos la descripcion de la beca
						Beca beca = becaDAO.get(academicoPago.getId_bec());
						descripcion = "Descuento por "+beca.getNom();
						descripcion += "\nCuota de Ingreso de ";
						descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
								 +  datosAlumno.get("secc")+" "+datosAlumno.getString("nivel").substring(0, 3); //+ datosAlumno.get("secc") + " "
						impresionItem.getDescuentos().add(
								new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(arrDescuentoPagoAdelantado[i])));
						descuentoItem = descuentoItem.add(new BigDecimal(arrDescuentoPagoAdelantado[i]));
					}					

					descripcion = "Cuota de Ingreso de \n"
							+ datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
							 +  datosAlumno.get("secc")+" "+datosAlumno.getString("nivel").substring(0, 3); //+ datosAlumno.get("secc") + " "
				}
				
					impresionItem.setDescripcion(descripcion.toUpperCase());
					impresionItem.setPrecioUnit(academicoPago.getMonto());
					impresionItem.setCantidad(1);
					impresionItem.setTipoOperacion("OPE_INA");
					// FIN - FACTURA ELECTRONICA

					academicoPago.setCanc("1");
					academicoPago.setDesc_hermano(new BigDecimal(arrDescuento[i]));
					academicoPago.setDesc_pronto_pago(new BigDecimal(arrDescuentoSecretaria[i]));
					academicoPago.setDesc_personalizado(new BigDecimal(arrDescuentoPersonalizado[i]));
					BigDecimal descuentoTotal = academicoPago.getMonto().subtract(monto);
					
					if(descuentoTotal.compareTo(new BigDecimal(0)) > 0 && academicoPago.getTip().equals("MEN")) {
						academicoPago.setDesc_personalizado(descuentoTotal);
						//descripcion = "Descuento personalizado ";
						/*descripcion += "\nMensualidad de " + Constante.MES[academicoPago.getMens() - 1];
						descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
								+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);*/
					
						//descripcion += "\nMensualidad de " + Constante.MES[academicoPago.getMens() - 1];
						/*descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
								+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);*/
						impresionItem.getDescuentos().add(
								new ImpresionDcto( "Descuento personalizado".toUpperCase(), descuentoTotal));
						descuentoItem = descuentoItem.add(descuentoTotal);
					}
					//academicoPago.setDesc_pago_adelantado(BigDecimal.ZERO);
					academicoPago.setDesc_pago_adelantado(new BigDecimal(arrDescuentoPagoAdelantado[i]));
					academicoPago.setDesc_beca(new BigDecimal(arrDescuentoBeca[i]));
					academicoPago.setNro_rec(nro_rec_col);
					academicoPago.setFec_pago(new Date());

					MovimientoDetalle movimientoDetalle = new MovimientoDetalle();
					if (academicoPago.getTip().equals("MEN")) {
						movimientoDetalle.setId_fco(Constante.CONCEPTO_MENSUALIDAD);
					} else if (academicoPago.getTip().equals("MAT")) {
						movimientoDetalle.setId_fco(Constante.TIPO_CONCEPTO_MATRICULA);
					} else if (academicoPago.getTip().equals("ING")) {
						movimientoDetalle.setId_fco(Constante.TIPO_CONCEPTO_CUOTA_ING);
					}
					
					movimientoDetalle.getDescuentos()
					.add(new MovimientoDescuento(academicoPago.getDesc_hermano(), "DESCUENTO POR HERMANO"));
					movimientoDetalle.getDescuentos()
					.add(new MovimientoDescuento(academicoPago.getDesc_pronto_pago(), "DESCUENTO POR PRONTO PAGO"));
					movimientoDetalle.getDescuentos()
					.add(new MovimientoDescuento(academicoPago.getDesc_personalizado(), "DESCUENTO PERSONALIZADO"));
					if ((academicoPago.getDesc_beca().compareTo(new BigDecimal(0))) > 0) {
						//Hallamos la descripcion de la beca
						Beca beca = becaDAO.get(academicoPago.getId_bec());
						movimientoDetalle.getDescuentos()
						.add(new MovimientoDescuento(academicoPago.getDesc_beca(), "DESCUENTO POR "+beca.getNom()));
					}
					
					movimientoDetalle.setEst("A");
					//movimientoDetalle.setMonto(academicoPago.getMonto());
					movimientoDetalle.setMonto(academicoPago.getMonto());
					movimientoDetalle.setObs(descripcion.replaceAll("\n", " ").toUpperCase());
					
					academicoPago.setMontoTotal(academicoPago.getMonto().subtract(academicoPago.getDesc_hermano())
							.subtract(academicoPago.getDesc_pronto_pago()).subtract(academicoPago.getDesc_personalizado())
							.subtract(academicoPago.getDesc_pago_adelantado()).subtract(academicoPago.getDesc_beca()));

					impresionItem.setPrecio(academicoPago.getMonto());
					impresionItem.setDescuento(descuentoItem);

					impresion_colegio.getItems().add(impresionItem);

					movimientoDetalle.setMonto_total(academicoPago.getMonto().subtract(descuentoItem));

					movimientoDetalle.setDescuento(descuentoItem);

					movimientoDetalles_colegio.add(movimientoDetalle);

					descuentoTotal_colegio = descuentoTotal_colegio.add(descuentoItem);
					monto_colegio = monto_colegio.add(academicoPago.getMonto());

					academicoPagoDAO.saveOrUpdate(academicoPago);
					
						
		} else if(tipo_pago[i].equals("A")) {
			//Verificar si ya tiene asignado un número
			if(nro_rec_acad.equals("")) {
				nro_rec_acad = getNroReciboAcadVac("B005");
				// INICIO - FACTURA ELECTRONICA
				impresionCabecera_academia.setNombreBoleta("BOLETA ELECTRÓNICA");
				impresionCabecera_academia.setNro(nro_rec_acad);
				impresionCabecera_academia.setDia(FechaUtil.toString(new Date(), "dd/MM/yyyy"));
				impresionCabecera_academia.setComercio(empresa.getString("giro_negocio").toUpperCase());
				impresionCabecera_academia.setHora(FechaUtil.toString(new Date(), "hh:mm a"));
				impresionCabecera_academia.setUsuario(usuarioSeg.getNombres());
				Sucursal local = sucursalDAO.get(usuarioSeg.getId_suc());
				impresionCabecera_academia.setTelefono(local.getTel());
				impresionCabecera_academia.setLocal(local.getDir());
				impresion_academia.setCabecera(impresionCabecera_academia);
				//Row datosCliente = academicoPagoDAO.obtenerDatosCliente(idPago);
				Persona datosCliente = personaDAO.listFullByParams(new Param("per.id",id_per_pag), null).get(0);
				/*
				 * if(datosCliente==null){ result.setCode("500");
				 * result.setMsg("No se pudo obtener los datos del cliente"); }
				 */
				impresionCliente_academia.setDireccion(datosCliente.getDir() == null ? "" : datosCliente.getDir());
				String ape_pat=datosCliente.getApe_pat() == null ? "" : datosCliente.getApe_pat().toUpperCase();
				String ape_mat=datosCliente.getApe_mat() == null ? "" : datosCliente.getApe_mat().toUpperCase();
				String nom=datosCliente.getNom()== null ? "" : datosCliente.getNom().toUpperCase();
				String direccion=datosCliente.getDir()==null ? "" : datosCliente.getDir().toUpperCase();
				impresionCliente_academia.setNombre(ape_pat+" "+ape_mat+" "+nom);
				impresionCliente_academia.setTip_doc(datosCliente.getTipoDocumento().getNom());
				impresionCliente_academia.setNro_doc(datosCliente.getNro_doc());
				impresionCliente_academia.setDireccion(direccion);
				impresion_academia.setCliente(impresionCliente_academia);
			}
				// FIN - FACTURA ELECTRONICA
				
				Integer id = Integer.parseInt(arrPAgos[i]);
				academicoPago = academicoPagoDAO.get(id);
				//Se hace la asignacion del aula
				//Hallo la matrícula
				Matricula matricula = matriculaDAO.getByParams(new Param("id",academicoPago.getId_mat()));
				//Hallo la lista de aulas para el ciclo y grado
				Param param3= new Param();
				param3.put("aula.id_cic",matricula.getId_cic());
				param3.put("aula.id_grad", matricula.getId_gra());
				param3.put("cit.id", matricula.getId_cct());
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
						matricula.setMat_val("1");
						break;
					}
				}
				if(matricula.getId_au_asi()==null) {
					throw new ServiceException("No existe aulas disponibles para este grado, por favor comunicar al Administrador del Sistema.");
				}
				//Actualizo el aula
				matriculaDAO.actualizarSeccionxPrimeraVez(matricula.getId(), matricula.getId_au_asi());
				// INICIO - FACTURA ELECTRONICA
				Row datosAlumno = matriculaDAO.getDatosAlumno(academicoPago.getId_mat());
				BigDecimal descuentoItem = BigDecimal.ZERO;
				ImpresionItem impresionItem = new ImpresionItem();
				impresionItem.setNro(i + 1);
				String descripcion="";
				//Obtengo los descuentos insertados en su matricula , solo para la primera cuota
				if(academicoPago.getNro_cuota().equals(1)) {
					if(academicoPago.getTip_pag()==null) {
						List<AlumnoDescuento> descuentos= alumnoDescuentoDAO.listByParams(new Param("id_mat",academicoPago.getId_mat()),new String[] { "id" });
						for (AlumnoDescuento alumnoDescuento : descuentos) {
							//Obtener datos del descuento
							DescuentoConf descuentoConf = descuentoConfDAO.getByParams(new Param("id",alumnoDescuento.getId_fdes()));
							//Obtengo el nombre del descuento
							Descuento descuento= descuentoDAO.get(descuentoConf.getId_des());
							descripcion = "Descuento por "+descuento.getNom();
							impresionItem.getDescuentos()
									.add(new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(descuentoConf.getMonto())));
							descuentoItem = descuentoItem.add(new BigDecimal(descuentoConf.getMonto()));
						}
					} else if(!academicoPago.getTip_pag().equals("O")){
						List<AlumnoDescuento> descuentos= alumnoDescuentoDAO.listByParams(new Param("id_mat",academicoPago.getId_mat()),new String[] { "id" });
						for (AlumnoDescuento alumnoDescuento : descuentos) {
							//Obtener datos del descuento
							DescuentoConf descuentoConf = descuentoConfDAO.getByParams(new Param("id",alumnoDescuento.getId_fdes()));
							//Obtengo el nombre del descuento
							Descuento descuento= descuentoDAO.get(descuentoConf.getId_des());
							descripcion = "Descuento por "+descuento.getNom();
							impresionItem.getDescuentos()
									.add(new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(descuentoConf.getMonto())));
							descuentoItem = descuentoItem.add(new BigDecimal(descuentoConf.getMonto()));
						}
					}
					
				}
				
				if ((new BigDecimal(arrDescuentoBeca[i]).compareTo(new BigDecimal(0))) > 0) {
					//Hallamos la descripcion de la beca
					Beca beca = becaDAO.get(academicoPago.getId_bec());
					descripcion = "Descuento por "+beca.getNom();
					impresionItem.getDescuentos().add(
							new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(arrDescuentoBeca[i])));
					descuentoItem = descuentoItem.add(new BigDecimal(arrDescuentoBeca[i]));
				}		
				descripcion = "Cuota Nro. "+academicoPago.getNro_cuota()+" - "+datosAlumno.getString("ciclo") + "\n"
						+ datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
						+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);
				
				impresionItem.setDescripcion(descripcion.toUpperCase());
				impresionItem.setPrecioUnit(academicoPago.getMonto());
				impresionItem.setCantidad(1);
				impresionItem.setTipoOperacion("OPE_INA");
				// FIN - FACTURA ELECTRONICA

				academicoPago.setCanc("1");
				academicoPago.setDesc_hermano(new BigDecimal(arrDescuento[i]));
				academicoPago.setDesc_pronto_pago(new BigDecimal(arrDescuentoSecretaria[i]));
				academicoPago.setDesc_personalizado(new BigDecimal(arrDescuentoPersonalizado[i]));
				//academicoPago.setDesc_pago_adelantado(BigDecimal.ZERO);
				academicoPago.setDesc_pago_adelantado(new BigDecimal(arrDescuentoPagoAdelantado[i]));
				academicoPago.setDesc_beca(new BigDecimal(arrDescuentoBeca[i]));
				academicoPago.setNro_rec(nro_rec_acad);
				academicoPago.setFec_pago(new Date());

				MovimientoDetalle movimientoDetalle = new MovimientoDetalle();
				movimientoDetalle.setId_fco(Constante.CONCEPTO_PAGO_ACADEMIA);
				//Obtengo los descuentos insertados en su matricula
				if(academicoPago.getNro_cuota().equals(1)) {
					if(academicoPago.getTip_pag()==null) {
						List<AlumnoDescuento> descuentos= alumnoDescuentoDAO.listByParams(new Param("id_mat",academicoPago.getId_mat()),new String[] { "id" });
						for (AlumnoDescuento alumnoDescuento : descuentos) {
							//Obtener datos del descuento
							DescuentoConf descuentoConf = descuentoConfDAO.getByParams(new Param("id",alumnoDescuento.getId_fdes()));
							//Obtengo el nombre del descuento
							Descuento descuento= descuentoDAO.get(descuentoConf.getId_des());
							movimientoDetalle.getDescuentos().add(new MovimientoDescuento(new BigDecimal(descuentoConf.getMonto()), descuento.getNom()));
						}
					} else if(!academicoPago.getTip_pag().equals("O")) {
						List<AlumnoDescuento> descuentos= alumnoDescuentoDAO.listByParams(new Param("id_mat",academicoPago.getId_mat()),new String[] { "id" });
						for (AlumnoDescuento alumnoDescuento : descuentos) {
							//Obtener datos del descuento
							DescuentoConf descuentoConf = descuentoConfDAO.getByParams(new Param("id",alumnoDescuento.getId_fdes()));
							//Obtengo el nombre del descuento
							Descuento descuento= descuentoDAO.get(descuentoConf.getId_des());
							movimientoDetalle.getDescuentos().add(new MovimientoDescuento(new BigDecimal(descuentoConf.getMonto()), descuento.getNom()));
						}
					}
					
				}
				if ((new BigDecimal(arrDescuentoBeca[i]).compareTo(new BigDecimal(0))) > 0) {
					//Hallamos la descripcion de la beca
					Beca beca = becaDAO.get(academicoPago.getId_bec());
					//descripcion = "Descuento por "+beca.getNom();
					//Hallamos el monto de descuento
					movimientoDetalle.getDescuentos().add(new MovimientoDescuento(academicoPago.getDesc_beca(), beca.getNom()));
				}	
				movimientoDetalle.setEst("A");
				movimientoDetalle.setMonto(academicoPago.getMonto());
				movimientoDetalle.setObs(descripcion.replaceAll("\n", " ").toUpperCase());
				
				academicoPago.setMontoTotal(academicoPago.getMonto().subtract(academicoPago.getDesc_hermano())
						.subtract(academicoPago.getDesc_pronto_pago()).subtract(academicoPago.getDesc_personalizado())
						.subtract(academicoPago.getDesc_pago_adelantado()).subtract(academicoPago.getDesc_beca()));

				impresionItem.setPrecio(academicoPago.getMonto());
				impresionItem.setDescuento(descuentoItem);

				impresion_academia.getItems().add(impresionItem);

				movimientoDetalle.setMonto_total(academicoPago.getMonto().subtract(descuentoItem));

				movimientoDetalle.setDescuento(descuentoItem);

				movimientoDetalles_academia.add(movimientoDetalle);

				descuentoTotal_academia = descuentoTotal_academia.add(descuentoItem);
				monto_academia = monto_academia.add(academicoPago.getMonto());

				academicoPagoDAO.saveOrUpdate(academicoPago);

		} else if(tipo_pago[i].equals("V")) {
			//Verificar si ya tiene asignado un número
			if(nro_rec_vac.equals("")) {
				nro_rec_vac = getNroReciboAcadVac("B006");
				// INICIO - FACTURA ELECTRONICA
				impresionCabecera_vacaciones.setNombreBoleta("BOLETA ELECTRÓNICA");
				impresionCabecera_vacaciones.setNro(nro_rec_vac);
				impresionCabecera_vacaciones.setDia(FechaUtil.toString(new Date(), "dd/MM/yyyy"));
				impresionCabecera_vacaciones.setComercio(empresa.getString("giro_negocio").toUpperCase());
				impresionCabecera_vacaciones.setHora(FechaUtil.toString(new Date(), "hh:mm a"));
				impresionCabecera_vacaciones.setUsuario(usuarioSeg.getNombres());
				Sucursal local = sucursalDAO.get(usuarioSeg.getId_suc());
				impresionCabecera_vacaciones.setTelefono(local.getTel());
				impresionCabecera_vacaciones.setLocal(local.getDir());
				impresion_vacaciones.setCabecera(impresionCabecera_vacaciones);
				//Row datosCliente = academicoPagoDAO.obtenerDatosCliente(idPago);
				Persona datosCliente = personaDAO.listFullByParams(new Param("per.id",id_per_pag), null).get(0);
				/*
				 * if(datosCliente==null){ result.setCode("500");
				 * result.setMsg("No se pudo obtener los datos del cliente"); }
				 */
				impresionCliente_vacaciones.setDireccion(datosCliente.getDir() == null ? "" : datosCliente.getDir());
				String ape_pat=datosCliente.getApe_pat() == null ? "" : datosCliente.getApe_pat().toUpperCase();
				String ape_mat=datosCliente.getApe_mat() == null ? "" : datosCliente.getApe_mat().toUpperCase();
				String nom=datosCliente.getNom()== null ? "" : datosCliente.getNom().toUpperCase();
				String direccion=datosCliente.getDir()==null ? "" : datosCliente.getDir().toUpperCase();
				impresionCliente_vacaciones.setNombre(ape_pat+" "+ape_mat+" "+nom);
				impresionCliente_vacaciones.setTip_doc(datosCliente.getTipoDocumento().getNom());
				impresionCliente_vacaciones.setNro_doc(datosCliente.getNro_doc());
				impresionCliente_vacaciones.setDireccion(direccion);
				impresion_vacaciones.setCliente(impresionCliente_vacaciones);
			}
				// FIN - FACTURA ELECTRONICA
				
				Integer id = Integer.parseInt(arrPAgos[i]);
				academicoPago = academicoPagoDAO.get(id);
				
				//Se hace la asignacion del aula
				//Hallo la matrícula
				Matricula matricula = matriculaDAO.getByParams(new Param("id",academicoPago.getId_mat()));
				//Hallo la lista de aulas para el ciclo y grado
				Param param3= new Param();
				param3.put("aula.id_cic",matricula.getId_cic());
				param3.put("aula.id_grad", matricula.getId_gra());
				param3.put("cit.id", matricula.getId_cct());
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
						matricula.setMat_val("1");
						break;
					}
				}
				if(matricula.getId_au_asi()==null) {
					throw new ServiceException("No existe aulas disponibles para este grado, por favor comunicar al Administrador del Sistema.");
				}
				//Actualizo el aula
				matriculaDAO.actualizarSeccionxPrimeraVez(matricula.getId(), matricula.getId_au_asi());

				// INICIO - FACTURA ELECTRONICA
				Row datosAlumno = matriculaDAO.getDatosAlumno(academicoPago.getId_mat());
				BigDecimal descuentoItem = BigDecimal.ZERO;
				ImpresionItem impresionItem = new ImpresionItem();
				impresionItem.setNro(i + 1);
				String descripcion="";
				//Obtengo los descuentos insertados en su matricula , solo para la primera cuota
				if(academicoPago.getNro_cuota().equals(1)) {
					if(academicoPago.getTip_pag()==null) {
						List<AlumnoDescuento> descuentos= alumnoDescuentoDAO.listByParams(new Param("id_mat",academicoPago.getId_mat()),new String[] { "id" });
						for (AlumnoDescuento alumnoDescuento : descuentos) {
							//Obtener datos del descuento
							DescuentoConf descuentoConf = descuentoConfDAO.getByParams(new Param("id",alumnoDescuento.getId_fdes()));
							//Obtengo el nombre del descuento
							Descuento descuento= descuentoDAO.get(descuentoConf.getId_des());
							descripcion = "Descuento por "+descuento.getNom();
							impresionItem.getDescuentos()
									.add(new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(descuentoConf.getMonto())));
							descuentoItem = descuentoItem.add(new BigDecimal(descuentoConf.getMonto()));
						}	
					} else if(!academicoPago.getTip_pag().equals("O")) {
						List<AlumnoDescuento> descuentos= alumnoDescuentoDAO.listByParams(new Param("id_mat",academicoPago.getId_mat()),new String[] { "id" });
						for (AlumnoDescuento alumnoDescuento : descuentos) {
							//Obtener datos del descuento
							DescuentoConf descuentoConf = descuentoConfDAO.getByParams(new Param("id",alumnoDescuento.getId_fdes()));
							//Obtengo el nombre del descuento
							Descuento descuento= descuentoDAO.get(descuentoConf.getId_des());
							descripcion = "Descuento por "+descuento.getNom();
							impresionItem.getDescuentos()
									.add(new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(descuentoConf.getMonto())));
							descuentoItem = descuentoItem.add(new BigDecimal(descuentoConf.getMonto()));
						}	
					}
					
				}
				
				if ((new BigDecimal(arrDescuentoBeca[i]).compareTo(new BigDecimal(0))) > 0) {
					//Hallamos la descripcion de la beca
					Beca beca = becaDAO.get(academicoPago.getId_bec());
					descripcion = "Descuento por "+beca.getNom();
					impresionItem.getDescuentos().add(
							new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(arrDescuentoBeca[i])));
					descuentoItem = descuentoItem.add(new BigDecimal(arrDescuentoBeca[i]));
				}	
				descripcion = "Cuota Nro. "+academicoPago.getNro_cuota()+" - "+datosAlumno.getString("ciclo") + "\n"
						+ datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
						+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);
				
				impresionItem.setDescripcion(descripcion.toUpperCase());
				impresionItem.setPrecioUnit(academicoPago.getMonto());
				impresionItem.setCantidad(1);
				impresionItem.setTipoOperacion("OPE_INA");
				// FIN - FACTURA ELECTRONICA

				academicoPago.setCanc("1");
				academicoPago.setDesc_hermano(new BigDecimal(arrDescuento[i]));
				academicoPago.setDesc_pronto_pago(new BigDecimal(arrDescuentoSecretaria[i]));
				academicoPago.setDesc_personalizado(new BigDecimal(arrDescuentoPersonalizado[i]));
				//academicoPago.setDesc_pago_adelantado(BigDecimal.ZERO);
				academicoPago.setDesc_pago_adelantado(new BigDecimal(arrDescuentoPagoAdelantado[i]));
				academicoPago.setDesc_beca(new BigDecimal(arrDescuentoBeca[i]));
				academicoPago.setNro_rec(nro_rec_vac);
				academicoPago.setFec_pago(new Date());

				MovimientoDetalle movimientoDetalle = new MovimientoDetalle();
				movimientoDetalle.setId_fco(Constante.CONCEPTO_PAGO_VACACIONES);
				//Obtengo los descuentos insertados en su matricula
				if(academicoPago.getNro_cuota().equals(1)) {
					if(academicoPago.getTip_pag()==null) {
						List<AlumnoDescuento> descuentos= alumnoDescuentoDAO.listByParams(new Param("id_mat",academicoPago.getId_mat()),new String[] { "id" });
						for (AlumnoDescuento alumnoDescuento : descuentos) {
							//Obtener datos del descuento
							DescuentoConf descuentoConf = descuentoConfDAO.getByParams(new Param("id",alumnoDescuento.getId_fdes()));
							//Obtengo el nombre del descuento
							Descuento descuento= descuentoDAO.get(descuentoConf.getId_des());
							movimientoDetalle.getDescuentos().add(new MovimientoDescuento(new BigDecimal(descuentoConf.getMonto()), descuento.getNom()));
						}	
					} else if(!academicoPago.getTip_pag().equals("O")) {
						List<AlumnoDescuento> descuentos= alumnoDescuentoDAO.listByParams(new Param("id_mat",academicoPago.getId_mat()),new String[] { "id" });
						for (AlumnoDescuento alumnoDescuento : descuentos) {
							//Obtener datos del descuento
							DescuentoConf descuentoConf = descuentoConfDAO.getByParams(new Param("id",alumnoDescuento.getId_fdes()));
							//Obtengo el nombre del descuento
							Descuento descuento= descuentoDAO.get(descuentoConf.getId_des());
							movimientoDetalle.getDescuentos().add(new MovimientoDescuento(new BigDecimal(descuentoConf.getMonto()), descuento.getNom()));
						}
					}
					
				}
				if ((new BigDecimal(arrDescuentoBeca[i]).compareTo(new BigDecimal(0))) > 0) {
					//Hallamos la descripcion de la beca
					Beca beca = becaDAO.get(academicoPago.getId_bec());
					//descripcion = "Descuento por "+beca.getNom();
					//Hallamos el monto de descuento
					movimientoDetalle.getDescuentos().add(new MovimientoDescuento(academicoPago.getDesc_beca(), beca.getNom()));
				}	
				movimientoDetalle.setEst("A");
				movimientoDetalle.setMonto(academicoPago.getMonto());
				movimientoDetalle.setObs(descripcion.replaceAll("\n", " ").toUpperCase());
				
				academicoPago.setMontoTotal(academicoPago.getMonto().subtract(academicoPago.getDesc_hermano())
						.subtract(academicoPago.getDesc_pronto_pago()).subtract(academicoPago.getDesc_personalizado())
						.subtract(academicoPago.getDesc_pago_adelantado()).subtract(academicoPago.getDesc_beca()));

				impresionItem.setPrecio(academicoPago.getMonto());
				impresionItem.setDescuento(descuentoItem);

				impresion_vacaciones.getItems().add(impresionItem);

				movimientoDetalle.setMonto_total(academicoPago.getMonto().subtract(descuentoItem));

				movimientoDetalle.setDescuento(descuentoItem);

				movimientoDetalles_vacaciones.add(movimientoDetalle);

				descuentoTotal_vacaciones = descuentoTotal_vacaciones.add(descuentoItem);
				monto_vacaciones = monto_vacaciones.add(academicoPago.getMonto());

				academicoPagoDAO.saveOrUpdate(academicoPago);
		}
	}		
		//Grabamos todos los movimientos tipo colegio
		// INICIO - se graba movimiento final
		if(!nro_rec_col.equals("")) {
			Movimiento movimiento_colegio = new Movimiento();
			movimiento_colegio.setEst("A");
			movimiento_colegio.setFec(new Date());
				//movimiento.setId_fam(id_fam);
			movimiento_colegio.setId_per(id_per_pag);
			movimiento_colegio.setId_suc(id_suc);
			movimiento_colegio.setMonto(monto_colegio);
			movimiento_colegio.setDescuento(descuentoTotal_colegio);
			movimiento_colegio.setMonto_total(monto_colegio.subtract(descuentoTotal_colegio));
			movimiento_colegio.setObs("PAGO DE MENSUALIDAD");
			movimiento_colegio.setId_fpa(1);// efectivo
			movimiento_colegio.setTipo("I");
			movimiento_colegio.setNro_rec(nro_rec_col);
			Integer id_mov_col = movimientoDAO.saveOrUpdate(movimiento_colegio);
			for (MovimientoDetalle movimientoDetalle : movimientoDetalles_colegio) {
				movimientoDetalle.setId_fmo(id_mov_col);
				Integer id_fmd = movimientoDetalleDAO.saveOrUpdate(movimientoDetalle);

				/* Grabar movimiento descuentos */
				for (MovimientoDescuento movimientoDescuento : movimientoDetalle.getDescuentos()) {

					grabarMovimientoDescuento(id_fmd, movimientoDescuento.getDes(), movimientoDescuento.getDescuento());

				}

			}
			// FIN - se graba movimiento final
			
			//Actualizamos el número de recibo de colegio
			updateNroRecibo(id_suc, nro_rec_col);
			impresiones.add(impresion_colegio);
		}
		
		if(!nro_rec_acad.equals("")) {
			Movimiento movimiento_academia = new Movimiento();
			movimiento_academia.setEst("A");
			movimiento_academia.setFec(new Date());
				//movimiento.setId_fam(id_fam);
			movimiento_academia.setId_per(id_per_pag);
			movimiento_academia.setId_suc(id_suc);
			movimiento_academia.setMonto(monto_academia);
			movimiento_academia.setDescuento(descuentoTotal_academia);
			movimiento_academia.setMonto_total(monto_academia.subtract(descuentoTotal_academia));
			movimiento_academia.setObs("PAGO DE MENSUALIDAD");
			movimiento_academia.setId_fpa(1);// efectivo
			movimiento_academia.setTipo("I");
			movimiento_academia.setNro_rec(nro_rec_acad);

			Integer id_mov_aca = movimientoDAO.saveOrUpdate(movimiento_academia);
			for (MovimientoDetalle movimientoDetalle : movimientoDetalles_academia) {
				movimientoDetalle.setId_fmo(id_mov_aca);
				Integer id_fmd = movimientoDetalleDAO.saveOrUpdate(movimientoDetalle);

				/* Grabar movimiento descuentos */
				for (MovimientoDescuento movimientoDescuento : movimientoDetalle.getDescuentos()) {

					grabarMovimientoDescuento(id_fmd, movimientoDescuento.getDes(), movimientoDescuento.getDescuento());

				}

			}
			// FIN - se graba movimiento final
			
			//Actualizamos el número de recibo de colegio
			updateNroReciboAcadVac("B005", nro_rec_acad);
			impresiones.add(impresion_academia);
		}
		
		if(!nro_rec_vac.equals("")) {
			Movimiento movimiento_vacaciones = new Movimiento();
			movimiento_vacaciones.setEst("A");
			movimiento_vacaciones.setFec(new Date());
				//movimiento.setId_fam(id_fam);
			movimiento_vacaciones.setId_per(id_per_pag);
			movimiento_vacaciones.setId_suc(id_suc);
			movimiento_vacaciones.setMonto(monto_vacaciones);
			movimiento_vacaciones.setDescuento(descuentoTotal_vacaciones);
			movimiento_vacaciones.setMonto_total(monto_vacaciones.subtract(descuentoTotal_vacaciones));
			movimiento_vacaciones.setObs("PAGO DE MENSUALIDAD");
			movimiento_vacaciones.setId_fpa(1);// efectivo
			movimiento_vacaciones.setTipo("I");
			movimiento_vacaciones.setNro_rec(nro_rec_vac);

			Integer id_mov_vac = movimientoDAO.saveOrUpdate(movimiento_vacaciones);
			for (MovimientoDetalle movimientoDetalle : movimientoDetalles_vacaciones) {
				movimientoDetalle.setId_fmo(id_mov_vac);
				Integer id_fmd = movimientoDetalleDAO.saveOrUpdate(movimientoDetalle);

				/* Grabar movimiento descuentos */
				for (MovimientoDescuento movimientoDescuento : movimientoDetalle.getDescuentos()) {

					grabarMovimientoDescuento(id_fmd, movimientoDescuento.getDes(), movimientoDescuento.getDescuento());

				}

			}
			// FIN - se graba movimiento final
			
			//Actualizamos el número de recibo de colegio
			updateNroReciboAcadVac("B006", nro_rec_vac);
			impresiones.add(impresion_vacaciones);
		}
		
		
		return impresiones;

		//Logica anterior
		/*Integer idPago = Integer.parseInt(arrPAgos[0]);
		AcademicoPago academicoPago = academicoPagoDAO.get(idPago);

		String nro_rec="";
		if(tipo.equals("C")) {
			nro_rec = getNroRecibo(id_suc);
		} else if(tipo.equals("A")) {
			nro_rec = getNroReciboAcadVac("B005");
		} else if(tipo.equals("V")) {
			nro_rec = getNroReciboAcadVac("B006");
		}
		


		UsuarioSeg usuarioSeg = tokenSeguridad.getUsuarioSeg();

		logger.debug("nuevo recibo:" + nro_rec);
		//Datos de la empresa
		Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);

		// INICIO - FACTURA ELECTRONICA

		Impresion impresion = new Impresion();
		ImpresionCabecera impresionCabecera = new ImpresionCabecera();
		impresionCabecera.setNombreBoleta("BOLETA ELECTRÓNICA");
		impresionCabecera.setNro(nro_rec);
		impresionCabecera.setDia(FechaUtil.toString(new Date(), "dd/MM/yyyy"));
		impresionCabecera.setComercio(empresa.getString("giro_negocio").toUpperCase());
		impresionCabecera.setHora(FechaUtil.toString(new Date(), "hh:mm a"));
		impresionCabecera.setUsuario(usuarioSeg.getNombres());
		Sucursal local = sucursalDAO.get(usuarioSeg.getId_suc());
		impresionCabecera.setTelefono(local.getTel());
		impresionCabecera.setLocal(local.getDir());

		impresion.setCabecera(impresionCabecera);
		ImpresionCliente impresionCliente = new ImpresionCliente();
		//Row datosCliente = academicoPagoDAO.obtenerDatosCliente(idPago);
		Persona datosCliente = personaDAO.listFullByParams(new Param("id",id_per), null).get(0);
		/*
		 * if(datosCliente==null){ result.setCode("500");
		 * result.setMsg("No se pudo obtener los datos del cliente"); }
		 */

		/*impresionCliente.setDireccion(datosCliente.getDir() == null ? "" : datosCliente.getDir());
		impresionCliente.setNombre(datosCliente.getApe_pat() == null ? "" : datosCliente.getApe_pat().toUpperCase()+" "+datosCliente.getApe_mat() == null ? "" : datosCliente.getApe_mat().toUpperCase()+" "+datosCliente.getNom()== null ? "" : datosCliente.getNom().toUpperCase());
		impresionCliente.setTip_doc(datosCliente.getTipoDocumento().getNom());
		impresionCliente.setNro_doc(datosCliente.getNro_doc());
		impresion.setCliente(impresionCliente);

		// FIN - FACTURA ELECTRONICA

		BigDecimal descuentoTotal = new BigDecimal(0);
		BigDecimal monto = new BigDecimal(0);
		Integer id_fam = null;
		List<MovimientoDetalle> movimientoDetalles = new ArrayList<MovimientoDetalle>();

		for (int i = 0; i < arrPAgos.length; i++) {
			Integer id = Integer.parseInt(arrPAgos[i]);
			academicoPago = academicoPagoDAO.get(id);

			// INICIO - FACTURA ELECTRONICA
			Row datosAlumno = matriculaDAO.getDatosAlumno(academicoPago.getId_mat());
			id_fam = datosAlumno.getInteger("id_fam");
			BigDecimal descuentoItem = BigDecimal.ZERO;
			ImpresionItem impresionItem = new ImpresionItem();
			impresionItem.setNro(i + 1);
			String descripcion="";
			if(tipo.equals("C")) {
				if ((new BigDecimal(arrDescuento[i]).compareTo(new BigDecimal(0))) > 0) {
					descripcion = "Descuento por hermano";
					descripcion += "\nMensualidad de " + Constante.MES[academicoPago.getMens() - 1];
					descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
							+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);
					// descripcion = "Descuento por hermano\n";
					impresionItem.getDescuentos()
							.add(new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(arrDescuento[i])));
					descuentoItem = descuentoItem.add(new BigDecimal(arrDescuento[i]));
				}
				if ((new BigDecimal(arrDescuentoSecretaria[i]).compareTo(new BigDecimal(0))) > 0) {
					descripcion = "Descuento pronto pago ";
					descripcion += "\nMensualidad de " + Constante.MES[academicoPago.getMens() - 1];
					descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
							+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);
					impresionItem.getDescuentos()
							.add(new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(arrDescuentoSecretaria[i])));
					descuentoItem = descuentoItem.add(new BigDecimal(arrDescuentoSecretaria[i]));
				}
				if ((new BigDecimal(arrDescuentoPersonalizado[i]).compareTo(new BigDecimal(0))) > 0) {
					descripcion = "Descuento pronto pago ";
					descripcion += "\nMensualidad de " + Constante.MES[academicoPago.getMens() - 1];
					descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
							+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);
					impresionItem.getDescuentos().add(
							new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(arrDescuentoPersonalizado[i])));
					descuentoItem = descuentoItem.add(new BigDecimal(arrDescuentoPersonalizado[i]));
				}
				if ((new BigDecimal(arrDescuentoPagoAdelantado[i]).compareTo(new BigDecimal(0))) > 0) {
					descripcion = "Descuento pago adelantado ";
					descripcion += "\nMensualidad de " + Constante.MES[academicoPago.getMens() - 1];
					descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
							+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);
					impresionItem.getDescuentos().add(
							new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(arrDescuentoPagoAdelantado[i])));
					descuentoItem = descuentoItem.add(new BigDecimal(arrDescuentoPagoAdelantado[i]));
				}

				descripcion = "Mensualidad de " + Constante.MES[academicoPago.getMens() - 1] + "\n"
						+ datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
						+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);
			} else if (tipo.equals("A") || tipo.equals("V")) {
				//Obtengo los descuentos insertados en su matricula
				List<AlumnoDescuento> descuentos= alumnoDescuentoDAO.listByParams(new Param("id_mat",academicoPago.getId_mat()),new String[] { "id" });
				for (AlumnoDescuento alumnoDescuento : descuentos) {
					//Obtener datos del descuento
					DescuentoConf descuentoConf = descuentoConfDAO.getByParams(new Param("id",alumnoDescuento.getId_fdes()));
					descripcion = "Descuento por "+descuentoConf.getNom();
					impresionItem.getDescuentos()
							.add(new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(descuentoConf.getMonto())));
					descuentoItem = descuentoItem.add(new BigDecimal(descuentoConf.getMonto()));
				}
				descripcion = "Cuota Nro. "+academicoPago.getNro_cuota()+" - "+datosAlumno.getString("ciclo") + "\n"
						+ datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
						+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);
			}


			impresionItem.setDescripcion(descripcion.toUpperCase());
			impresionItem.setPrecioUnit(academicoPago.getMonto());
			impresionItem.setCantidad(1);
			impresionItem.setTipoOperacion("OPE_INA");
			// FIN - FACTURA ELECTRONICA

			academicoPago.setCanc("1");
			academicoPago.setDesc_hermano(new BigDecimal(arrDescuento[i]));
			academicoPago.setDesc_pronto_pago(new BigDecimal(arrDescuentoSecretaria[i]));
			academicoPago.setDesc_personalizado(new BigDecimal(arrDescuentoPersonalizado[i]));
			//academicoPago.setDesc_pago_adelantado(BigDecimal.ZERO);
			academicoPago.setDesc_pago_adelantado(new BigDecimal(arrDescuentoPagoAdelantado[i]));
			academicoPago.setNro_rec(nro_rec);
			academicoPago.setFec_pago(new Date());

			MovimientoDetalle movimientoDetalle = new MovimientoDetalle();
			
			if(tipo.equals("C")) {
				movimientoDetalle.setId_fco(Constante.CONCEPTO_MENSUALIDAD);
				movimientoDetalle.getDescuentos()
				.add(new MovimientoDescuento(academicoPago.getDesc_hermano(), "DESCUENTO POR HERMANO"));
				movimientoDetalle.getDescuentos()
				.add(new MovimientoDescuento(academicoPago.getDesc_pronto_pago(), "DESCUENTO POR PRONTO PAGO"));
				movimientoDetalle.getDescuentos()
				.add(new MovimientoDescuento(academicoPago.getDesc_personalizado(), "DESCUENTO POR PRONTO PAGO"));
			} else if(tipo.equals("A")) {
				movimientoDetalle.setId_fco(Constante.CONCEPTO_PAGO_ACADEMIA);
				//Obtengo los descuentos insertados en su matricula
				List<AlumnoDescuento> descuentos= alumnoDescuentoDAO.listByParams(new Param("id_mat",academicoPago.getId_mat()),new String[] { "id" });
				for (AlumnoDescuento alumnoDescuento : descuentos) {
					//Obtener datos del descuento
					DescuentoConf descuentoConf = descuentoConfDAO.getByParams(new Param("id",alumnoDescuento.getId_fdes()));
					movimientoDetalle.getDescuentos().add(new MovimientoDescuento(new BigDecimal(descuentoConf.getMonto()), descuentoConf.getNom()));
				}
			} else if(tipo.equals("V")) {
				movimientoDetalle.setId_fco(Constante.CONCEPTO_PAGO_VACACIONES);
				//Obtengo los descuentos insertados en su matricula
				List<AlumnoDescuento> descuentos= alumnoDescuentoDAO.listByParams(new Param("id_mat",academicoPago.getId_mat()),new String[] { "id" });
				for (AlumnoDescuento alumnoDescuento : descuentos) {
					//Obtener datos del descuento
					DescuentoConf descuentoConf = descuentoConfDAO.getByParams(new Param("id",alumnoDescuento.getId_fdes()));
					movimientoDetalle.getDescuentos().add(new MovimientoDescuento(new BigDecimal(descuentoConf.getMonto()), descuentoConf.getNom()));
				}
			}
			movimientoDetalle.setEst("A");
			movimientoDetalle.setMonto(academicoPago.getMonto());
			movimientoDetalle.setObs(descripcion.replaceAll("\n", " ").toUpperCase());

			
			if(tipo.equals("C")) {
				if (arrPAgos.length == 10 && i == 9) {

					Calendar cal = Calendar.getInstance();
					int anio_actual = cal.get(Calendar.YEAR);
					int fecVencimiento = getFecVencimiento(anio_actual, 4, Integer.parseInt(datosAlumno.get("id_per").toString()));// TODO
																			// ver el mes 

					SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

					String format = formatter.format(new Date());
					int fecActual = Integer.parseInt(format);

					if (fecActual <= fecVencimiento) {
						BigDecimal montoTotalAPagar = academicoPago.getMonto().subtract(academicoPago.getDesc_hermano())
								.subtract(academicoPago.getDesc_pronto_pago())
								.subtract(academicoPago.getDesc_personalizado());
						BigDecimal descuentoAdelantado = montoTotalAPagar.divide(new BigDecimal(2));
						BigDecimal descuentoTotalDic = descuentoAdelantado.add(academicoPago.getDesc_hermano())
								.add(academicoPago.getDesc_pronto_pago()).add(academicoPago.getDesc_personalizado());

						// descuentoTotal = descuentoTotal.add(descuentoTotalDic);

						academicoPago.setDesc_pago_adelantado(descuentoAdelantado);
						academicoPago.setMontoTotal(academicoPago.getMonto().subtract(descuentoTotalDic));

						descuentoItem = descuentoItem.add(descuentoAdelantado);
						movimientoDetalle.getDescuentos()
								.add(new MovimientoDescuento(descuentoAdelantado, "DESCUENTO POR PAGO ADELANTADO"));

						descripcion = "Descuento pago adelantado";
						descripcion += "\nMensualidad de " + Constante.MES[academicoPago.getMens() - 1];
						descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
								+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);
						impresionItem.getDescuentos()
								.add(new ImpresionDcto(descripcion.toUpperCase(), descuentoAdelantado));
						// descuentoItem = descuentoItem.add(new
						// BigDecimal(arrDescuentoPersonalizado[i]));

					}
				}
			}

			academicoPago.setMontoTotal(academicoPago.getMonto().subtract(academicoPago.getDesc_hermano())
					.subtract(academicoPago.getDesc_pronto_pago()).subtract(academicoPago.getDesc_personalizado())
					.subtract(academicoPago.getDesc_pago_adelantado()));

			impresionItem.setPrecio(academicoPago.getMonto());
			impresionItem.setDescuento(descuentoItem);

			impresion.getItems().add(impresionItem);

			movimientoDetalle.setMonto_total(academicoPago.getMonto().subtract(descuentoItem));

			movimientoDetalle.setDescuento(descuentoItem);

			movimientoDetalles.add(movimientoDetalle);

			descuentoTotal = descuentoTotal.add(descuentoItem);
			monto = monto.add(academicoPago.getMonto());

			academicoPagoDAO.saveOrUpdate(academicoPago);

		}

		// INICIO - se graba movimiento final
		Movimiento movimiento = new Movimiento();
		movimiento.setEst("A");
		movimiento.setFec(new Date());
		//movimiento.setId_fam(id_fam);
		movimiento.setId_per(id_per);
		movimiento.setId_suc(id_suc);
		movimiento.setMonto(monto);
		movimiento.setDescuento(descuentoTotal);
		movimiento.setMonto_total(monto.subtract(descuentoTotal));
		if(tipo.equals("C"))
			movimiento.setObs("PAGO DE MENSUALIDAD");
		else
			movimiento.setObs("PAGO DE CUOTA");
		movimiento.setId_fpa(1);// efectivo
		movimiento.setTipo("I");
		movimiento.setNro_rec(nro_rec);

		Integer id_mov = movimientoDAO.saveOrUpdate(movimiento);
		for (MovimientoDetalle movimientoDetalle : movimientoDetalles) {
			movimientoDetalle.setId_fmo(id_mov);
			Integer id_fmd = movimientoDetalleDAO.saveOrUpdate(movimientoDetalle);

			/* Grabar movimiento descuentos */
		/*	for (MovimientoDescuento movimientoDescuento : movimientoDetalle.getDescuentos()) {

				grabarMovimientoDescuento(id_fmd, movimientoDescuento.getDes(), movimientoDescuento.getDescuento());

			}

		}
		// FIN - se graba movimiento final

		updateNroRecibo(id_suc, nro_rec);*/

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
		String fecVenc = anioSiguiente + String.format("%02d", mesSiguiente) + dia_mora;

		return Integer.parseInt(fecVenc);

	}

	public List<Row> getBoletas(String fecha) throws ServiceException {

		if (fecha == null)
			return academicoPagoDAO.pagosBancoTodos();
		else
			return academicoPagoDAO.pagosBanco(fecha);

	}
	/*
	 * private List<Row> getBoletas(Date fec_in, Date fec_fin, Integer id_suc,
	 * Integer id_anio) throws ServiceException{ Param param = new Param();
	 * param.put("fec_ini", fec_ini); param.put("fec_fin", fec_fin);
	 * param.put("id_suc", id_suc); param.put("e", id_suc);
	 * 
	 * return null; }
	 */

	public void cargarMovimientosBanco(Date fec ) throws ServiceException {

		List<Row> boletas = new ArrayList<>();
		if (fec != null) {
			String fecha_plana = FechaUtil.toStringMYQL(fec);
			boletas = getBoletas(fecha_plana);
		} else
			boletas = getBoletas(null);

		for (Row row : boletas) {

			Movimiento movimiento = new Movimiento();

			BigDecimal dcto_hermano = row.getBigDecimal("desc_hermano") == null ? BigDecimal.ZERO : row.getBigDecimal("desc_hermano");
			BigDecimal dcto_pronto_pago = row.getBigDecimal("desc_pronto_pago") == null ? BigDecimal.ZERO : row.getBigDecimal("desc_pronto_pago");
			BigDecimal dcto_pago_adelantado = row.getBigDecimal("desc_pago_adelantado") == null ? BigDecimal.ZERO : row.getBigDecimal("desc_pago_adelantado");
			BigDecimal dcto_personalizado = row.getBigDecimal("desc_personalizado") == null ? BigDecimal.ZERO: row.getBigDecimal("desc_personalizado");
			BigDecimal dcto_beca = row.getBigDecimal("desc_beca") == null ? BigDecimal.ZERO: row.getBigDecimal("desc_beca");
			BigDecimal descuento = dcto_hermano.add(dcto_pronto_pago).add(dcto_pago_adelantado).add(dcto_personalizado).add(dcto_beca);

			movimiento.setTipo("I");
			movimiento.setFec(row.getDate("fec_pago"));
			movimiento.setId_suc(row.getInteger("id_suc"));
			movimiento.setId_mat(row.getInteger("id_mat"));
			movimiento.setId_fam(row.getInteger("id_fam"));
			movimiento.setId_fpa(2);// deposito en banco
			movimiento.setMonto(row.getBigDecimal("monto"));
			movimiento.setDescuento(descuento);
			movimiento.setMonto_total(row.getBigDecimal("monto_total"));
			movimiento.setNro_rec(row.getString("nro_rec"));
			if(row.getString("tip").equals("MAT"))
				movimiento.setObs("MATRICULA ");
			else if(row.getString("tip").equals("ING"))
				movimiento.setObs("CUOTA DE INGRESO");
			else
				movimiento.setObs("PAGO DE MENSUALIDAD " + row.getString("banco"));
			movimiento.setEst("A");
			movimiento.setUsr_ins(1);
			//movimiento.setUsuario(usuario);

			//System.out.println(movimiento);
			
			int id_fmo = movimientoDAO.saveOrUpdate(movimiento);

			MovimientoDetalle movimientoDetalle = new MovimientoDetalle();
			movimientoDetalle.setId_fco(3);// MENSUALIDAD COLEGIO
			movimientoDetalle.setDescuento(descuento);
			movimientoDetalle.setId_fmo(id_fmo);
			movimientoDetalle.setMonto(row.getBigDecimal("monto"));
			movimientoDetalle.setMonto_total(row.getBigDecimal("monto_total"));
			// MENSUALIDAD DE NOVIEMBRE HUERTA JHADDE MAJHALL-3 Aï¿½OS-B INI
			if(row.getString("tip").equals("MAT")) {
				movimientoDetalle.setObs("MATRÍCULA " + row.getString("alu_ape_pat") + " " + row.getString("alu_ape_mat") + " "
						+ row.getString("alu_nom") + " -" + row.getString("grad_nom") + "-" + row.getString("secc") + " "
						+ row.getString("niv").substring(0, 3));
			} else if(row.getString("tip").equals("ING")) {
				movimientoDetalle.setObs("CUOTA DE INGRESO " + row.getString("alu_ape_pat") + " " + row.getString("alu_ape_mat") + " "
						+ row.getString("alu_nom") + " -" + row.getString("grad_nom") + "-" + row.getString("secc") + " "
						+ row.getString("niv").substring(0, 3));
			} else {
				movimientoDetalle.setObs("MENSUALIDAD DE " + Constante.MES[row.getInteger("mens") - 1]
						+ " " + row.getString("alu_ape_pat") + " " + row.getString("alu_ape_mat") + " "
						+ row.getString("alu_nom") + " -" + row.getString("grad_nom") + "-" + row.getString("secc") + " "
						+ row.getString("niv").substring(0, 3));
			}
			movimientoDetalle.setEst("A");
			movimientoDetalle.setUsr_ins(1);
			//movimientoDetalle.setUsuario(usuario);
			movimientoDetalle.setFec_ins(new Date());

			int id_fmd = movimientoDetalleDAO.saveOrUpdate(movimientoDetalle);

			grabarMovimientoDescuento(id_fmd, "DESCUENTO POR PAGO ADELANTADO", dcto_pago_adelantado);
			grabarMovimientoDescuento(id_fmd, "DESCUENTO POR PERSONALIZADO", dcto_personalizado);
			grabarMovimientoDescuento(id_fmd, "DESCUENTO POR PAGO ADELANTADO", dcto_pago_adelantado);
			grabarMovimientoDescuento(id_fmd, "DESCUENTO POR PRONTO PAGO", dcto_pronto_pago);
			grabarMovimientoDescuento(id_fmd, "DESCUENTO POR BECA O SEMIBECA", dcto_beca);
		}

	}

	private void grabarMovimientoDescuento(Integer id_fmd, String descripcion, BigDecimal descuento) {

		if (descuento != null && descuento.compareTo(BigDecimal.ZERO) != 0) {
			MovimientoDescuento movimientoDescuento = new MovimientoDescuento();
			movimientoDescuento.setDes(descripcion);
			movimientoDescuento.setDescuento(descuento);
			movimientoDescuento.setEst("A");
			//movimientoDescuento.setUsuario(usuario);
			movimientoDescuento.setId_fmd(id_fmd);
			movimientoDescuentoDAO.saveOrUpdate(movimientoDescuento);
		}

	}

	private String list2JSON(List<Impresion> impresiones) {
		String json = "[";

		for (Impresion impresion : impresiones) {
			json = json + " {\"cabecera\":" + impresion.getCabecera().toString();
			json = json + ",\"cliente\":" + impresion.getCliente().toString();
			if(impresion.getDocumentoReferencia()!=null)
				json = json + ",\"documentoReferencia\":" + impresion.getDocumentoReferencia().toString();
			json = json + ",\"items\":[";
			for (ImpresionItem item : impresion.getItems()) {
				json = json + item.toString() + ",";
			}
			json = json.substring(0, json.length() - 1);
			json = json + "]},";

		}
		json = json.substring(0, json.length() - 1);

		json = json + "]";

		return json;
	}

	public SunatResultJson enviarResumenRestSunat(String fechaEnvioSunat, List<Impresion> impresiones) throws Exception{

		String output = null;
		String rpta = "";
		String ambiente = env.getProperty("ambiente");
		String fac_api = env.getProperty("fac-api");
		
		try {

			if (fechaEnvioSunat == null)
				fechaEnvioSunat = FechaUtil.toString(new Date());

			String[] arrFecha = fechaEnvioSunat.split("/");

			String urlRest = fac_api + "/boleta/generar/2";
			logger.debug("urlRest->"+urlRest);
			//if (ambiente.equals("D"))
			//	urlRest = urlRest.replace("fac-web-api", "fac-web-api-desa");
				//urlRest = fac_api + "-desa/boleta/generar/2";
			
			logger.debug("Sending To Server ....:" + urlRest);

			URL url = new URL(urlRest);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setConnectTimeout(20000);

			String json = list2JSON(impresiones);

			logger.debug("JSON:" + json);

			java.io.OutputStream os = conn.getOutputStream();//esta linea dice el log ( pero debio puntar en el log la linea 1842)
			os.write(json.getBytes("UTF-8"));
			os.flush();

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : H 	 	TTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			logger.debug("Output from Server .... \n");

			while ((output = br.readLine()) != null) {
				if (output != null)
					rpta += output;
			}

			logger.debug("respuesta:" + rpta);
			conn.disconnect();

		} catch (MalformedURLException e) {

			logger.error("error de url",e );
			throw new ServiceException("Error de la url del servicio sunat");
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("io expception",e );

			throw new ServiceException("Error de escritura");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("excepcion",e );

			throw new ServiceException("Error de la sunat");

		}
		Type listType = new TypeToken<SunatJson>() {
		}.getType();
		SunatJson rptaFactura1 = new Gson().fromJson(rpta, listType);

		return rptaFactura1.getResult();

	}

	@Transactional
	public SunatResultJson enviarNotaCreditoRestSunat( List<Impresion> impresiones) throws Exception{

		String output = null;
		String rpta = "";
		String ambiente = env.getProperty("ambiente");
		String fac_api = env.getProperty("fac-api");
		
		try {

			String fechaEnvioSunat = FechaUtil.toString(new Date());


			//String[] arrFecha = fechaEnvioSunat.split("/");

			String urlRest = fac_api + "/nc/generar/2";/*1: GENERAR XML Y 2 ES ENVIAR A SUNAT*/
			//String urlRest = fac_api + "/boleta/nc/1";/*1: GENERAR XML Y 2 ES ENVIAR A SUNAT*/
			
			//if (ambiente.equals("D"))
			//	urlRest = urlRest.replace("fac-web-api", "fac-web-api-desa");
				//urlRest = fac_api + "-desa/boleta/generar/2";
			
			logger.debug("Sending To Server ....:" + urlRest);

			URL url = new URL(urlRest);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setConnectTimeout(20000);

			String json = list2JSON(impresiones);

			//System.out.println(json);
			
			logger.debug("JSON:" + json);

			java.io.OutputStream os = conn.getOutputStream();
			os.write(json.getBytes("UTF-8"));
			os.flush();

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : H 	 	TTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			logger.debug("Output from Server .... \n");

			while ((output = br.readLine()) != null) {
				if (output != null)
					rpta += output;
			}

			logger.debug("respuesta:" + rpta);
			conn.disconnect();

		} catch (MalformedURLException e) {

			logger.error("error de url",e );
			throw new ServiceException("Error de la url del servicio sunat");
		} catch (IOException e) {

			logger.error("io expception",e );

			throw new ServiceException("Error de escritura");

		} catch (Exception e) {

			logger.error("excepcion",e );

			throw new ServiceException("Error de la sunat");

		}
		Type listType = new TypeToken<SunatJson>() {
		}.getType();
		SunatJson rptaFactura1 = new Gson().fromJson(rpta, listType);

		return rptaFactura1.getResult();

	}

	
	@Transactional
	public SunatResultJson enviarComunicacionBajaRestSunat( List<Impresion> impresiones) throws Exception{

		String output = null;
		String rpta = "";
		String ambiente = env.getProperty("ambiente");
		String fac_api = env.getProperty("fac-api");
		
		try {

			String fechaEnvioSunat = FechaUtil.toString(new Date());


			String[] arrFecha = fechaEnvioSunat.split("/");

			String urlRest = fac_api + "/comunicacionBaja/generar/2";/*1: GENERAR XML Y 2 ES ENVIAR A SUNAT*/
			
			//if (ambiente.equals("D"))
			//	urlRest = urlRest.replace("fac-web-api", "fac-web-api-desa");
				//urlRest = fac_api + "-desa/boleta/generar/2";
			
			logger.debug("Sending To Server ....:" + urlRest);

			URL url = new URL(urlRest);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setConnectTimeout(20000);

			String json = list2JSON(impresiones);

			//System.out.println(json);
			
			logger.debug("JSON:" + json);

			java.io.OutputStream os = conn.getOutputStream();
			os.write(json.getBytes("UTF-8"));
			os.flush();

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : H 	 	TTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			logger.debug("Output from Server .... \n");

			while ((output = br.readLine()) != null) {
				if (output != null)
					rpta += output;
			}

			logger.debug("respuesta:" + rpta);
			conn.disconnect();

		} catch (MalformedURLException e) {

			logger.error("error de url",e );
			throw new ServiceException("Error de la url del servicio sunat");
		} catch (IOException e) {

			logger.error("io expception",e );

			throw new ServiceException("Error de escritura");

		} catch (Exception e) {

			logger.error("excepcion",e );

			throw new ServiceException("Error de la sunat");

		}
		Type listType = new TypeToken<SunatJson>() {
		}.getType();
		SunatJson rptaFactura1 = new Gson().fromJson(rpta, listType);

		return rptaFactura1.getResult();

	}

	
	
	public AjaxResponseBody getRespuestaTicket(String id_res, String ticket ) throws Exception{

		String output = null;
		String rpta = "";
		String ambiente = env.getProperty("ambiente");
		String fac_api = env.getProperty("fac-api");
		
		try {
 
			String urlRest = fac_api + "/boleta/" + id_res + "/" + ticket;
			
			if (ambiente.equals("D"))
				urlRest = urlRest.replace("fac-web-api", "fac-web-api-desa");
			
			logger.debug("Sending To Server ....:" + urlRest);

			URL url = new URL(urlRest);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
		    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setConnectTimeout(5000);

			//String json = list2JSON(impresiones);

			//logger.debug("JSON:" + json);

			java.io.OutputStream os = conn.getOutputStream();
			//os.write(json.getBytes("UTF-8"));
			os.flush();

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : H 	 	TTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			logger.debug("Output from Server .... \n");

			while ((output = br.readLine()) != null) {
				if (output != null)
					rpta += output;
			}

			logger.debug("respuesta:" + rpta);
			conn.disconnect();

		} catch (MalformedURLException e) {

			logger.error("error de url",e );
			throw new ServiceException("Error de la url del servicio sunat");
		} catch (IOException e) {

			logger.error("io expception",e );

			throw new ServiceException("Error de escritura");

		} catch (Exception e) {

			logger.error("excepcion",e );

			throw new ServiceException("Error de la sunat");

		}
		Type listType = new TypeToken<AjaxResponseBody>() {
		}.getType();
		AjaxResponseBody rptaFactura1 = new Gson().fromJson(rpta, listType);

		return rptaFactura1;

	}

	
	/**
	 * 
	 * @param academicoPagos
	 * @param id_suc
	 * @return
	 */
	@Transactional
	public Impresion grabarTraslado(List<AcademicoPago> academicoPagos, TrasladoDetalle trasladoDetalle,
			SitHistorial sitHistorial, Integer[] id_fco, Integer id_suc ) throws ServiceException {

		Impresion impresion = new Impresion();
		try {
			String nro_rec = getNroRecibo(id_suc);

			UsuarioSeg usuarioSeg = tokenSeguridad.getUsuarioSeg();
			
			// Grabo el Traslado
			// Se necesita saber si el alumno ya tiene situacion final y existe en
			// el historial
			Integer id_mat = trasladoDetalle.getId_mat();
			Map<String, Object> matricula=matriculaDAO.getMatriculaDatosPrincipales(id_mat);
			Aula aula = aulaDAO.get(Integer.parseInt(matricula.get("id_au").toString()));
			Alumno alumno = alumnoDAO.get(Integer.parseInt(matricula.get("id_alu").toString()));
			int anio_tra=Integer.parseInt(matricula.get("anio").toString());
			LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		     int anio_actual  = localDate.getYear();
		     Integer mes_tras=localDate.getMonthValue();
			// param.put("id_sit", trasladoDetalle.getId_sit());
			Integer id_sit_ant = matriculaDAO.getByParams(new Param("id", id_mat)).getId_sit();
			Integer id_sit_act = trasladoDetalle.getId_sit();

			// Verificamos si existe la ultima situacion de matricula en el
			// historial
			if (id_sit_ant != null && !id_sit_ant.equals(0)) {
				if (!sitHistorialDAO.existsHistorial(id_mat, id_sit_ant)) {
					sitHistorial.setId_mat(id_mat);
					sitHistorial.setId_sit(id_sit_ant);
					sitHistorialDAO.saveOrUpdate(sitHistorial);
				}
			}

			// Inserto la situacion actual de Trasladado
			sitHistorial.setId_mat(id_mat);
			sitHistorial.setId_sit(id_sit_act);
			sitHistorialDAO.saveOrUpdate(sitHistorial);

			// Inserto el traslado detalle
			trasladoDetalleDAO.saveOrUpdate(trasladoDetalle);

			// Actualizo la situacion de la matricula
			matriculaDAO.actualizarSituacion(id_mat, id_sit_act);
			
			//Datos de la empresa
			Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);
			
			//Verrificamos si tiene inscripcion al campus virtual
			Param param = new Param();
			param.put("id_alu", matricula.get("id_alu"));
			param.put("id_fam", matricula.get("id_fam"));
			param.put("id_anio", matricula.get("id_anio"));
			
			//InscripcionCampus inscripcionCampus = inscripcionCampusDAO.getByParams(param);
			if(matricula.get("usuario")!=null){
				String json="{"
						+"\"email\": \""+matricula.get("usuario")+"\","
						+"\"suspendido\": "+true+","
						+"\"motivoSuspension\": \"Traslado\"}";
				
				RestUtil restUtil = new RestUtil();
				
				Object respuesta=restUtil.requestPOST("http://login.ae.edu.pe:8085/ae-google-api/user/desactive", "POST", json);
				
				if(respuesta.toString().equals("OK")){
					String json2="{"
							+"\"idClassroom\": \""+aula.getId_classroom()+"\","
							+"\"idUsuario\": \""+alumno.getId_classRoom()+"\"}";
					
					RestUtil restUtil2 = new RestUtil();
					
					Object respuesta2=restUtil2.requestPOST("http://login.ae.edu.pe:8085/ae-google-api/course/unrole", "POST", json2);
					if(respuesta2.toString().equals("OK")){
						
					} else {

					}
					//Desactivamos en el script de sige_usuarios
					//usuarioCampusDAO.desactivarSigeUsuarios(matricula.get("usuario").toString());(2021 comentado)
					
					//Desactivamos del moodle por el momento
					/*Object respuesta_moodle=restUtil.requestPOST("http://ae.edu.pe:8081/sige-moodle-api/deleteByEmail/"+matricula.get("usuario"), "DELETE", json);
					
					if(respuesta_moodle.toString().equals("OK")){
						
					}*/
				} else {
					Gson gson = new Gson();
				//	ErrorReq errorReq = gson.fromJson(respuesta.toString(), ErrorReq.class);							
					Error error = new Error();
					//error.setSql_code(errorReq.getCode());
					error.setSql_code("400");
					error.setError("Error en desactivar el usuario: ");
					//error.setId_cvi(id_ins);
					//error.setError("Error en Creación de Usuario: "+alumno.getUsuario()+" "+respuesta.toString());
					error.setEst("A");
					errorDAO.saveOrUpdate(error);
				}
			}
			
			// INICIO - FACTURA ELECTRONICA

			ImpresionCabecera impresionCabecera = new ImpresionCabecera();
			impresionCabecera.setNombreBoleta("BOLETA ELECTRÓNICA");
			impresionCabecera.setNro(nro_rec);
			impresionCabecera.setDia(FechaUtil.toString(new Date(), "dd/MM/yyyy"));
			impresionCabecera.setComercio(empresa.getString("giro_negocio").toUpperCase());
			impresionCabecera.setHora(FechaUtil.toString(new Date(), "hh:mm a"));
			impresionCabecera.setUsuario(usuarioSeg.getNombres());
			Sucursal local = sucursalDAO.get(usuarioSeg.getId_suc());
			impresionCabecera.setTelefono(local.getTel());
			impresionCabecera.setLocal(local.getDir());
			impresion.setCabecera(impresionCabecera);
			ImpresionCliente impresionCliente = new ImpresionCliente();
			
			Row familiar = matriculaDAO.getDatosApoderado(id_mat);
			 impresionCliente.setDireccion(familiar.get("direccion")==null?"": familiar.getString("direccion"));
			 impresionCliente.setNombre(familiar.getString("nombres"));
			 impresionCliente.setTip_doc(familiar.getString("tip_doc"));
			 impresionCliente.setNro_doc(familiar.getString("nro_doc"));

			 
			impresion.setCliente(impresionCliente);
			List<MovimientoDetalle> movimientoDetalles = new ArrayList<MovimientoDetalle>();
			Integer i = 0;
			BigDecimal monto = new BigDecimal(0);
			Integer id_fam = null;
			BigDecimal descuentoTotal = new BigDecimal(0);
			if (academicoPagos.size() > 0) {
				Integer id_pago = academicoPagos.get(0).getId();

				for (AcademicoPago academicoPago : academicoPagos) {
					// INICIO - FACTURA ELECTRONICA
					Row datosAlumno = matriculaDAO.getDatosAlumno(academicoPago.getId_mat());
					id_fam = datosAlumno.getInteger("id_fam");
					BigDecimal descuentoItem = BigDecimal.ZERO;
					ImpresionItem impresionItem = new ImpresionItem();
					impresionItem.setNro(i + 1);
					String descripcion;

					if (academicoPago.getDesc_hermano() != null
							&& academicoPago.getDesc_hermano().compareTo(new BigDecimal(0)) > 0) {
						descripcion = "Descuento por hermano";
						descripcion += "\nMensualidad de " + Constante.MES[academicoPago.getMens() - 1];
						descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
								+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);
						// descripcion = "Descuento por hermano\n";
						impresionItem.getDescuentos()
								.add(new ImpresionDcto(descripcion.toUpperCase(), academicoPago.getDesc_hermano()));
						descuentoItem = descuentoItem.add(academicoPago.getDesc_hermano());
					}
					if (academicoPago.getDesc_pronto_pago() != null
							&& academicoPago.getDesc_pronto_pago().compareTo(new BigDecimal(0)) > 0) {
						descripcion = "Descuento pronto pago ";
						descripcion += "\nMensualidad de " + Constante.MES[academicoPago.getMens() - 1];
						descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
								+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);
						impresionItem.getDescuentos()
								.add(new ImpresionDcto(descripcion.toUpperCase(), academicoPago.getDesc_pronto_pago()));
						descuentoItem = descuentoItem.add(academicoPago.getDesc_pronto_pago());
					}
					if (academicoPago.getDesc_personalizado() != null
							&& academicoPago.getDesc_personalizado().compareTo(new BigDecimal(0)) > 0) {
						descripcion = "Descuento pronto pago ";
						descripcion += "\nMensualidad de " + Constante.MES[academicoPago.getMens() - 1];
						descripcion += "\n" + datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
								+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);
						impresionItem.getDescuentos()
								.add(new ImpresionDcto(descripcion.toUpperCase(), academicoPago.getDesc_personalizado()));
						descuentoItem = descuentoItem.add(academicoPago.getDesc_personalizado());
					}

					descripcion = "Mensualidad de " + Constante.MES[academicoPago.getMens() - 1] + "\n"
							+ datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
							+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);

					impresionItem.setDescripcion(descripcion.toUpperCase());
					impresionItem.setPrecioUnit(academicoPago.getMonto());
					impresionItem.setCantidad(1);
					impresionItem.setTipoOperacion("OPE_INA");
					// FIN - FACTURA ELECTRONICA

					MovimientoDetalle movimientoDetalle = new MovimientoDetalle();
					movimientoDetalle.setId_fco(Constante.CONCEPTO_MENSUALIDAD);
					movimientoDetalle.setEst("A");
					movimientoDetalle.setMonto(academicoPago.getMonto());
					movimientoDetalle.setObs(descripcion.replaceAll("\n", " ").toUpperCase());
	 
					movimientoDetalle.getDescuentos()
							.add(new MovimientoDescuento(academicoPago.getDesc_hermano(), "DESCUENTO POR HERMANO"));
					movimientoDetalle.getDescuentos()
							.add(new MovimientoDescuento(academicoPago.getDesc_pronto_pago(), "DESCUENTO POR PRONTO PAGO"));
					movimientoDetalle.getDescuentos().add(
							new MovimientoDescuento(academicoPago.getDesc_personalizado(), "DESCUENTO POR PRONTO PAGO"));

					if (academicoPago.getDesc_pago_adelantado() == null)
						academicoPago.setDesc_pago_adelantado(BigDecimal.ZERO);
					if (academicoPago.getDesc_personalizado() == null)
						academicoPago.setDesc_personalizado(BigDecimal.ZERO);
					if (academicoPago.getDesc_pronto_pago() == null)
						academicoPago.setDesc_pronto_pago(BigDecimal.ZERO);
					if (academicoPago.getDesc_hermano() == null)
						academicoPago.setDesc_hermano(BigDecimal.ZERO);

					academicoPago.setMontoTotal(academicoPago.getMonto().subtract(academicoPago.getDesc_hermano())
							.subtract(academicoPago.getDesc_pronto_pago()).subtract(academicoPago.getDesc_personalizado())
							.subtract(academicoPago.getDesc_pago_adelantado()));
					
					academicoPago.setNro_rec(nro_rec);
					academicoPago.setCanc("1");
					impresionItem.setPrecio(academicoPago.getMonto());
					impresionItem.setDescuento(descuentoItem);

					impresion.getItems().add(impresionItem);

					movimientoDetalle.setMonto_total(academicoPago.getMonto().subtract(descuentoItem));

					movimientoDetalle.setDescuento(descuentoItem);

					movimientoDetalles.add(movimientoDetalle);

					descuentoTotal = descuentoTotal.add(descuentoItem);
					monto = monto.add(academicoPago.getMonto());

					academicoPagoDAO.saveOrUpdate(academicoPago);
					i++;
				}
				
				
			}
				
			if(mes_tras!=null && anio_actual==anio_tra){
				for(int h=mes_tras+1; h<=12;h++){
					academicoPagoDAO.updatePago(id_mat, h);
				}
			}	
			// FIN - FACTURA ELECTRONICA

			// Grabar los movimientos por Documentos
			BigDecimal monto_doc = new BigDecimal(0);

			for (int j = 0; j < id_fco.length; j++) {
				MovimientoDetalle movimientoDetalle = new MovimientoDetalle();
				movimientoDetalle.setId_fco(id_fco[j]);	
				movimientoDetalle.setEst("A");
				Concepto concepto_pago = conceptoDAO.getByParams(new Param("id", id_fco[j]));
				movimientoDetalle.setMonto(concepto_pago.getMonto());
				movimientoDetalle.setMonto_total(concepto_pago.getMonto());
				movimientoDetalle.setObs(concepto_pago.getDes().replaceAll("\n", " ").toUpperCase()+" ALUMNO:"+matricula.get("alumno").toString());
	 			movimientoDetalle.setDescuento(BigDecimal.ZERO);
				monto_doc = monto_doc.add(concepto_pago.getMonto());
				movimientoDetalles.add(movimientoDetalle);

				// impresion

				ImpresionItem impresionItem = new ImpresionItem();
				impresionItem.setNro(i + 1);
				impresionItem.setDescripcion(movimientoDetalle.getObs());
				impresionItem.setPrecioUnit(movimientoDetalle.getMonto());
				impresionItem.setCantidad(1);
				impresionItem.setDescuento(BigDecimal.ZERO);
				impresionItem.setTipoOperacion("OPE_INA");
				impresionItem.setPrecio(movimientoDetalle.getMonto());
				impresion.getItems().add(impresionItem);

				i = i + 1;
			}

			BigDecimal monto_total = monto.add(monto_doc);
			// INICIO - se graba movimiento final de mensualidades
			Movimiento movimiento = new Movimiento();
			movimiento.setEst("A");
			movimiento.setFec(new Date());
			movimiento.setId_fam(id_fam);
			movimiento.setId_mat(id_mat);
			movimiento.setId_suc(id_suc);
			movimiento.setMonto(monto_total);
			movimiento.setDescuento(descuentoTotal);
			movimiento.setMonto_total(monto_total.subtract(descuentoTotal));
			movimiento.setObs("PAGO DE TRASLADO");
			movimiento.setId_fpa(1);// efectivo
			movimiento.setTipo("I");
			movimiento.setNro_rec(nro_rec);

			Integer id_mov = movimientoDAO.saveOrUpdate(movimiento);
			for (MovimientoDetalle movimientoDetalle : movimientoDetalles) {
				movimientoDetalle.setId_fmo(id_mov);
				Integer id_fmd = movimientoDetalleDAO.saveOrUpdate(movimientoDetalle);

				/* Grabar movimiento descuentos */
				for (MovimientoDescuento movimientoDescuento : movimientoDetalle.getDescuentos()) {

					grabarMovimientoDescuento(id_fmd, movimientoDescuento.getDes(), movimientoDescuento.getDescuento());

				}

			}
			// FIN - se graba movimiento final

			updateNroRecibo(id_suc, nro_rec);

		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return impresion;
	}

	@Transactional
	public void cambioLocal(Matricula matricula, Aula aulaActual, Integer id_au, Integer id_anio,  Integer id_suc,Integer id_per_nuevo) throws ServiceException {

		//Impresion impresionCabcera = new Impresion();
		
		List<MatriculaPagos> listPagar = obtenerPagosProgramados(matricula.getId_alu(), id_au,id_anio);

		UsuarioSeg usuarioSeg = tokenSeguridad.getUsuarioSeg();
		
		BigDecimal totalPagar = BigDecimal.ZERO;
		for (MatriculaPagos matriculaPagos : listPagar) {
			totalPagar = totalPagar.add(matriculaPagos.getMonto());
		}

		//matriculaDAO.actualizarSeccion(usuario.getUsrId(), matricula.getId(), id_au);
		
		Param param = new Param();
		param = new Param();
		param.put("id_per", id_per_nuevo);
		param.put("id_au_asi", id_au);
		param.put("id_mat", matricula.getId());
		param.put("id_usr", tokenSeguridad.getId());
		param.put("obs", "Cambio de local desde:" + matricula.getPeriodo().getId_suc());
		matriculaDAO.actualizarLocalYSeccion(param);
	
		/****
		 * inicio cambio de pagos de mensualidad
		 */
		
		//actualizar meses
		
		param = new Param();
		param.put("id_per",id_per_nuevo);

		ConfMensualidad confMensualidad = confMensualidadDAO.getByParams(param);

		BigDecimal montoMensualidadNuevo = confMensualidad.getMonto();
		BigDecimal montoMensualidadTotal= null;
			
		param = new Param();
		param.put("id_mat", matricula.getId());
		param.put("canc", "0");
		param.put("tip", "MEN");
		List<AcademicoPago> pagos = academicoPagoDAO.listByParams(param, new String[]{"id"});
		BigDecimal totalPagarx = BigDecimal.ZERO;
		for (AcademicoPago academicoPago : pagos) {
			
			//BigDecimal descuentosOriginales = academicoPago.getMontoTotal().subtract(academicoPago.getMonto());
			BigDecimal descuentosOriginales = BigDecimal.ZERO;
			
			if (academicoPago.getDesc_hermano()!=null)
				descuentosOriginales = descuentosOriginales.add(academicoPago.getDesc_hermano());
			if (academicoPago.getDesc_pago_adelantado()!=null)
				descuentosOriginales = descuentosOriginales.add(academicoPago.getDesc_pago_adelantado());
			if (academicoPago.getDesc_pronto_pago()!=null)
				descuentosOriginales = descuentosOriginales.add(academicoPago.getDesc_pronto_pago());
			if (academicoPago.getDesc_personalizado()!=null)
				descuentosOriginales = descuentosOriginales.add(academicoPago.getDesc_personalizado());
			
			if (academicoPago.getMontoTotal()==null){
				academicoPago.setMontoTotal(academicoPago.getMonto().subtract(descuentosOriginales));
			}
			
			totalPagarx = totalPagarx.add(academicoPago.getMontoTotal());
			
			montoMensualidadTotal= montoMensualidadNuevo.subtract(descuentosOriginales);
			
			param =new Param();
			academicoPago.setMonto(montoMensualidadNuevo);
			academicoPago.setMontoTotal(montoMensualidadTotal);
 			
			academicoPagoDAO.saveOrUpdate(academicoPago);
			
			//Inhabilitamos la matricula
			//matriculaDAO.inhabilitarMatricula(matricula.getId()); comentado 2021 
		}
		
		/***
		 * fin cambios de monto de pagos de mensualidad
		 */
		
		
		
		/*
		Param param = new Param();
		param.put("id_alu", matricula.getId_alu());
		param.put("id_anio", matricula.getPeriodo().getId_anio());
		param.put("est", "A");
		*/
		/* Pagos realizados anteriormente */
	/*	Map<String, Object> map = pagosMatriculaRealizados(matricula.getId());
		List<MatriculaPagos> listPagados = (List<MatriculaPagos>) map.get("pagos");
		BigDecimal totalPagados = (BigDecimal) map.get("total");

		// diferencia de pagos
		BigDecimal total = totalPagar.subtract(totalPagados);
		
		//Datos de la empresa
		Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);

		if (total.compareTo(BigDecimal.ZERO) > 0) {
			
			// DEBE PAGAR LA DIFERENCIA
			MatriculaPagos matriculaPago = new MatriculaPagos();
			matriculaPago.setCanc("0");// Por defecto pago pendiente
			matriculaPago.setMonto(total);
			matriculaPago.setTip(com.tesla.colegio.util.Constante.PAGO_CAMBIO_LOCAL);
			listPagados.add(matriculaPago);

			String nro_rec = getNroRecibo(id_suc);

			Row datosAlumno = matriculaDAO.getDatosAlumno(matricula.getId());
			
			Movimiento movimiento = new Movimiento();
			movimiento.setEst("A");
 			movimiento.setFec(new Date());
			movimiento.setId_fam(matricula.getId_fam());
			movimiento.setId_suc(id_suc);
			movimiento.setMonto(total);
			movimiento.setDescuento(BigDecimal.ZERO);
			movimiento.setMonto_total(total);
			movimiento.setObs("DIFERENCIA DE MATRICULA POR CAMBIO DE LOCAL "  
					+ " " + datosAlumno.getString("alumno") + " -" + datosAlumno.getString("grado") + "-" + datosAlumno.getString("secc") + " "
					+ datosAlumno.getString("nivel").substring(0, 3));
			movimiento.setId_fpa(1);// efectivo
			movimiento.setTipo("I");
			movimiento.setNro_rec(nro_rec);

			int id_fmo = movimientoDAO.saveOrUpdate(movimiento);

			MovimientoDetalle movimientoDetalle = new MovimientoDetalle();
			movimientoDetalle.setId_fco(EnumConceptoPago.CAMBIO_DE_LOCAL.getValue());// CAMBIO DE LOCAL
			movimientoDetalle.setDescuento(BigDecimal.ZERO);
			movimientoDetalle.setId_fmo(id_fmo);
			movimientoDetalle.setMonto(total);
			movimientoDetalle.setMonto_total(total);
			// MENSUALIDAD DE NOVIEMBRE HUERTA JHADDE MAJHALL-3 Aï¿½OS-B INI
			movimientoDetalle.setObs("DIFERENCIA DE MATRICULA POR CAMBIO DE LOCAL "  
					+ " " + datosAlumno.getString("alumno") + " -" + datosAlumno.getString("grado") + "-" + datosAlumno.getString("secc") + " "
					+ datosAlumno.getString("nivel").substring(0, 3));
			movimientoDetalle.setEst("A");
 			movimientoDetalle.setFec_ins(new Date());

			int id_fmd = movimientoDetalleDAO.saveOrUpdate(movimientoDetalle);
			
			/* Grabar movimiento descuentos */
	/*	
			grabarMovimientoDescuento(id_fmd,"MATRICULA ANTERIOR "  
					+ " " + datosAlumno.getString("alumno") + " -" + datosAlumno.getString("grado") + "-" + aulaActual.getSecc() + " "
					+ datosAlumno.getString("nivel").substring(0, 3), 
					totalPagados, usuario);
*/
		
		/*	Impresion impresion = new Impresion();
			ImpresionCabecera impresionCabecera = new ImpresionCabecera();
			impresionCabecera.setNro(nro_rec);
			impresionCabecera.setDia(FechaUtil.toString(new Date(), "dd/MM/yyyy"));
			impresionCabecera.setComercio(empresa.getString("giro_negocio").toUpperCase());
			impresionCabecera.setHora(FechaUtil.toString(new Date(), "hh:mm a"));
			impresionCabecera.setUsuario( usuarioSeg.getNombres());
			Sucursal local = sucursalDAO.get(id_suc);
			impresionCabecera.setTelefono(local.getTel());
			impresionCabecera.setLocal(local.getDir());

			impresion.setCabecera(impresionCabecera);

			ImpresionCliente impresionCliente = new ImpresionCliente();
			Familiar familiar = familiarDAO.get(matricula.getId_fam());

			impresionCliente.setDireccion(familiar.getDir() == null ? "" : familiar.getDir());
			impresionCliente.setNombre(familiar.getApe_pat() + " " + familiar.getApe_mat() + ", " + familiar.getNom());
			if (familiar.getId_tdc().intValue() == Constante.TIPO_DOCUMENTO_DNI.intValue())
				impresionCliente.setTip_doc("DNI");
			else if (familiar.getId_tdc().intValue() == Constante.TIPO_DOCUMENTO_PASAPORTE.intValue())
				impresionCliente.setTip_doc("PASAPORTE");
			else
				impresionCliente.setTip_doc("CARNE EXTRANJERIA");
			
			impresionCliente.setNro_doc(familiar.getNro_doc());
			
			impresion.setCliente(impresionCliente);
			
			ImpresionItem impresionItem = new ImpresionItem();
			impresionItem.setNro(1);
			 
						
			impresionItem.setDescripcion("DIFERENCA POR CAMBIO DE LOCAL "  
					+ " " + datosAlumno.getString("alumno")  +  " -" + datosAlumno.getString("grado") + "-" + datosAlumno.getString("secc") + " " + datosAlumno.getString("nivel").substring(0, 3));
			
			impresionItem.setPrecioUnit(total);
			impresionItem.setCantidad(1);
			impresionItem.setDescuento(BigDecimal.ZERO);
			impresionItem.setPrecio(total);
			impresionItem.setPrecioUnit(total);
			impresionItem.setTipoOperacion("OPE_INA");
			impresion.getItems().add(impresionItem);
			
			updateNroRecibo(id_suc, nro_rec);
			
			return impresion;
			
		} else {
			// EL COLEGIO DEBE DEVOLVER LA DIFERENCIA
			/*
			MatriculaPagos academicoPago = new MatriculaPagos();
			academicoPago.setCanc("0");// Por defecto pago pendiente
			academicoPago.setMonto(total);
			academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_CAMBIO_LOCAL);
			listPagados.add(academicoPago);
			*/
		/*	total = total.multiply(new BigDecimal(-1));
			
			// DEBE PAGAR LA DIFERENCIA
 
				String nro_rec = getNroReciboNotaCredito(id_suc);

				Row datosAlumno = matriculaDAO.getDatosAlumno(matricula.getId());
				
				Movimiento movimiento = new Movimiento();
				movimiento.setEst("A");
				movimiento.setFec(new Date());
				movimiento.setId_fam(matricula.getId_fam());
				movimiento.setId_suc(id_suc);
				movimiento.setMonto(total);
				movimiento.setDescuento(BigDecimal.ZERO);
				movimiento.setMonto_total(total);
				movimiento.setObs("DIFERENCIA DE MATRICULA POR CAMBIO DE LOCAL "  
						+ " " + datosAlumno.getString("alumno") + " -" + datosAlumno.getString("grado") + "-" + datosAlumno.getString("secc") + " "
						+ datosAlumno.getString("nivel").substring(0, 3));
				movimiento.setId_fpa(1);// efectivo
				movimiento.setTipo("S");//salida de efectivo
				movimiento.setNro_rec(nro_rec);

				int id_fmo = movimientoDAO.saveOrUpdate(movimiento);

				MovimientoDetalle movimientoDetalle = new MovimientoDetalle();
				movimientoDetalle.setId_fco(EnumConceptoPago.CAMBIO_DE_LOCAL.getValue());// CAMBIO DE LOCAL
				movimientoDetalle.setDescuento(BigDecimal.ZERO);
				movimientoDetalle.setId_fmo(id_fmo);
				movimientoDetalle.setMonto(total);
				movimientoDetalle.setMonto_total(total);
				// MENSUALIDAD DE NOVIEMBRE HUERTA JHADDE MAJHALL-3 Aï¿½OS-B INI
				movimientoDetalle.setObs("DIFERENCIA DE MATRICULA POR CAMBIO DE LOCAL "  
						+ " " + datosAlumno.getString("alumno") + " -" + datosAlumno.getString("grado") + "-" + datosAlumno.getString("secc") + " "
						+ datosAlumno.getString("nivel").substring(0, 3));
				movimientoDetalle.setEst("A");
				movimientoDetalle.setFec_ins(new Date());

				int id_fmd = movimientoDetalleDAO.saveOrUpdate(movimientoDetalle);
		 
			
				Impresion impresion = new Impresion();
				ImpresionCabecera impresionCabecera = new ImpresionCabecera();
				impresionCabecera.setNro(nro_rec);
				impresionCabecera.setDia(FechaUtil.toString(new Date(), "dd/MM/yyyy"));
				impresionCabecera.setComercio(empresa.getString("giro_negocio").toUpperCase());
				impresionCabecera.setHora(FechaUtil.toString(new Date(), "hh:mm a"));
				impresionCabecera.setUsuario( usuarioSeg.getNombres());
				Sucursal local = sucursalDAO.get(id_suc);
				impresionCabecera.setTelefono(local.getTel());
				impresionCabecera.setLocal(local.getDir());

				impresion.setCabecera(impresionCabecera);

				ImpresionCliente impresionCliente = new ImpresionCliente();
				Familiar familiar = familiarDAO.get(matricula.getId_fam());

				impresionCliente.setDireccion(familiar.getDir() == null ? "" : familiar.getDir());
				impresionCliente.setNombre(familiar.getApe_pat() + " " + familiar.getApe_mat() + ", " + familiar.getNom());
				if (familiar.getId_tdc().intValue() == Constante.TIPO_DOCUMENTO_DNI.intValue())
					impresionCliente.setTip_doc("DNI");
				else if (familiar.getId_tdc().intValue() == Constante.TIPO_DOCUMENTO_PASAPORTE.intValue())
					impresionCliente.setTip_doc("PASAPORTE");
				else
					impresionCliente.setTip_doc("CARNE EXTRANJERIA");
				
				impresionCliente.setNro_doc(familiar.getNro_doc());
				
				impresion.setCliente(impresionCliente);
				
				ImpresionItem impresionItem = new ImpresionItem();
				impresionItem.setNro(1);
				 
							
				impresionItem.setDescripcion("DIFERENCA POR CAMBIO DE LOCAL "  
						+ " " + datosAlumno.getString("alumno")  +  " -" + datosAlumno.getString("grado") + "-" + datosAlumno.getString("secc") + " "
						+ datosAlumno.getString("nivel").substring(0, 3));
				
				impresionItem.setPrecioUnit(total);
				impresionItem.setCantidad(1);
				impresionItem.setDescuento(BigDecimal.ZERO);
				impresionItem.setPrecio(total);
				impresionItem.setPrecioUnit(total);
				impresionItem.setTipoOperacion("OPE_INA");
				impresion.getItems().add(impresionItem);
				
				updateNroReciboNotaCredito(id_suc, nro_rec);
				
				return impresion;
			
			
		}*/
		
	}
	
	@Transactional
	public void cambioLocalxGrado(Matricula matricula, Aula aulaActual, Integer id_per_ant, Integer id_anio,  Integer id_suc,Integer id_per_nuevo, Integer id_cme, Integer id_cct) throws ServiceException {

		//Impresion impresionCabcera = new Impresion();
		//AjaxResponseBody result = new AjaxResponseBody();
		//List<MatriculaPagos> listPagar = obtenerPagosProgramadosxPeriodo(matricula.getId_alu(), id_per_ant,id_anio);

		UsuarioSeg usuarioSeg = tokenSeguridad.getUsuarioSeg();
		
		//BigDecimal totalPagar = BigDecimal.ZERO;
	/*	for (MatriculaPagos matriculaPagos : listPagar) {
			totalPagar = totalPagar.add(matriculaPagos.getMonto());
		}*/

		//matriculaDAO.actualizarSeccion(usuario.getUsrId(), matricula.getId(), id_au);
		//Buscar un aula donde haya vacante
		/*Integer id_au_nue=null;
		Param param3= new Param();
		param3.put("pee.id",id_per_nuevo);
		param3.put("aula.id_grad", matricula.getId_gra());
		List<Aula> aulas = aulaDAO.listFullByParams(param3, new String[]{"aula.secc"});
		for (Aula aula : aulas) {
			//Busco la capacidad del aula
			Integer cap=aula.getCap();
			//Busco los matriculados en ese aula
			Param param4 = new Param();
			param4.put("id_au_asi", aula.getId());
			param4.put("est", "A");
			param4.put("mat_val", "1");
			List<Matricula> matriculas=matriculaDAO.listByParams(param4, null);
			Integer reservas=vacanteService.getReservasxAula(aula.getId(),id_anio);
			Integer nro_vac=cap-matriculas.size()-reservas;
			if(nro_vac>0) {
				id_au_nue=aula.getId();
				break;
			}
		}*/
		Ciclo ciclo_nuevo=cicloDAO.getByParams(new Param("id_per",id_per_nuevo));
		
		//Buscamos el aula q tiene vacante
		Param param3= new Param();
		param3.put("aula.id_cic",ciclo_nuevo.getId());
		param3.put("aula.id_grad", matricula.getId_gra());
		param3.put("pee.id_suc", id_suc);
		param3.put("aula.id_cme",id_cme);
		param3.put("cta.id_cit", id_cct);
		List<Aula> aulas = aulaDAO.listFullByParams(param3, new String[]{"aula.secc"});
		Integer id_au_nue=null;
		for (Aula aula2 : aulas) {
			//Busco la capacidad del aula
			Integer cap2=aula2.getCap();
			//Busco los matriculados en ese aula
			Param param4 = new Param();
			param4.put("id_au_asi", aula2.getId());
			param4.put("est", "A");
			List<Matricula> matriculas2=matriculaDAO.listByParams(param4, null);
			Integer res2=vacanteService.getReservasxAula(aula2.getId(), id_anio);
			//Buscamos los trasladados
			Param param5 = new Param();
			param5.put("id_au_asi", aula2.getId());
			param5.put("id_sit", "5");
			List<Matricula> trasladados=matriculaDAO.listByParams(param5, null);
			Integer nro_vac2=cap2-matriculas2.size()-res2+trasladados.size();
			if(nro_vac2>0) {
				id_au_nue=aula2.getId();
				break;
			}
		}
		Param param = new Param();
		param = new Param();
		param.put("id_per", id_per_nuevo);
		param.put("id_au_asi", id_au_nue);
		param.put("id_cic", ciclo_nuevo.getId());
		param.put("id_cct", id_cct);
		param.put("id_mat", matricula.getId());
		param.put("id_usr", tokenSeguridad.getId());
		param.put("obs", "Cambio de local desde:" + matricula.getPeriodo().getId_suc());
		matriculaDAO.actualizarLocalYSeccion(param);
	
		/****
		 * inicio cambio de pagos de mensualidad
		 */
		
		//actualizar meses
		
		/*param = new Param();
		param.put("id_per",id_per_nuevo);

		ConfMensualidad confMensualidad = confMensualidadDAO.getByParams(param);*/
		Param param2 = new Param();
		param2.put("id_per", id_per_nuevo);
		param2.put("id_cct", id_cct);
		param2.put("id_cme", id_cme);
		ConfMensualidad confMensualidad = confMensualidadDAO.getByParams(param2);

		BigDecimal montoMensualidadNuevo = confMensualidad.getMonto();
		BigDecimal montoMensualidadTotal= null;
			
		param = new Param();
		param.put("id_mat", matricula.getId());
		param.put("canc", "0");
		param.put("tip", "MEN");
		List<AcademicoPago> pagos = academicoPagoDAO.listByParams(param, new String[]{"id"});
		BigDecimal totalPagarx = BigDecimal.ZERO;
		for (AcademicoPago academicoPago : pagos) {
			
			//BigDecimal descuentosOriginales = academicoPago.getMontoTotal().subtract(academicoPago.getMonto());
			BigDecimal descuentosOriginales = BigDecimal.ZERO;
			
			if (academicoPago.getDesc_hermano()!=null)
				descuentosOriginales = descuentosOriginales.add(academicoPago.getDesc_hermano());
			if (academicoPago.getDesc_pago_adelantado()!=null)
				descuentosOriginales = descuentosOriginales.add(academicoPago.getDesc_pago_adelantado());
			if (academicoPago.getDesc_pronto_pago()!=null)
				descuentosOriginales = descuentosOriginales.add(academicoPago.getDesc_pronto_pago());
			if (academicoPago.getDesc_personalizado()!=null)
				descuentosOriginales = descuentosOriginales.add(academicoPago.getDesc_personalizado());
			
			if (academicoPago.getMontoTotal()==null){
				academicoPago.setMontoTotal(academicoPago.getMonto().subtract(descuentosOriginales));
			}
			
			totalPagarx = totalPagarx.add(academicoPago.getMontoTotal());
			
			montoMensualidadTotal= montoMensualidadNuevo.subtract(descuentosOriginales);
			
			param =new Param();
			academicoPago.setMonto(montoMensualidadNuevo);
			academicoPago.setMontoTotal(montoMensualidadTotal);
 			
			academicoPagoDAO.saveOrUpdate(academicoPago);
			
			//Inhabilitamos la matricula, Pra cambio de local no
			//matriculaDAO.inhabilitarMatricula(matricula.getId());
		}
		
		/***
		 * fin cambios de monto de pagos de mensualidad
		 */
		
	}
	
	@Transactional
	public Impresion pagarCambioLocal(Matricula matricula, Integer id_au, Integer id_anio,  Integer id_suc,Integer id_per_nuevo) throws ServiceException {

		Impresion impresionCabcera = new Impresion();
		
		List<MatriculaPagos> listPagar = obtenerPagosProgramados(matricula.getId_alu(), matricula.getId_au_asi(),id_anio);

		UsuarioSeg usuarioSeg = tokenSeguridad.getUsuarioSeg();
		
		BigDecimal totalPagar = BigDecimal.ZERO;
		for (MatriculaPagos matriculaPagos : listPagar) {
			totalPagar = totalPagar.add(matriculaPagos.getMonto());
		}

		/* Pagos realizados anteriormente */
		Map<String, Object> map = pagosMatriculaRealizados(matricula.getId(),null);
		List<MatriculaPagos> listPagados = (List<MatriculaPagos>) map.get("pagos");
		BigDecimal totalPagados = (BigDecimal) map.get("total");
		
		//Obtener el id de movimiento de la matricula y/o cuota de ingreso
		

		// diferencia de pagos
		BigDecimal total = totalPagar.subtract(totalPagados);
		
		//Datos de la empresa
		Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);
		
		//Actualizamos el estado de la matricula, lo habilitamos para que sea valida
		matriculaDAO.updateEstadoMatricula(matricula.getId());

		if (total.compareTo(BigDecimal.ZERO) > 0) {
			
			// DEBE PAGAR LA DIFERENCIA
			MatriculaPagos matriculaPago = new MatriculaPagos();
			matriculaPago.setCanc("0");// Por defecto pago pendiente
			matriculaPago.setMonto(total);
			matriculaPago.setTip(com.tesla.colegio.util.Constante.PAGO_CAMBIO_LOCAL);
			listPagados.add(matriculaPago);

			String nro_rec = getNroRecibo(id_suc);

			Row datosAlumno = matriculaDAO.getDatosAlumno(matricula.getId());
			
			Movimiento movimiento = new Movimiento();
			movimiento.setEst("A");
 			movimiento.setFec(new Date());
			movimiento.setId_fam(matricula.getId_fam());
			movimiento.setId_suc(id_suc);
			movimiento.setMonto(total);
			movimiento.setId_mat(matricula.getId());
			movimiento.setDescuento(BigDecimal.ZERO);
			movimiento.setMonto_total(total);
			movimiento.setObs("DIFERENCIA DE MATRICULA POR CAMBIO DE LOCAL "  
					+ " " + datosAlumno.getString("alumno") + " -" + datosAlumno.getString("grado") + "-" + datosAlumno.getString("secc") + " "
					+ datosAlumno.getString("nivel").substring(0, 3));
			movimiento.setId_fpa(1);// efectivo
			movimiento.setTipo("I");
			movimiento.setNro_rec(nro_rec);

			int id_fmo = movimientoDAO.saveOrUpdate(movimiento);

			MovimientoDetalle movimientoDetalle = new MovimientoDetalle();
			movimientoDetalle.setId_fco(EnumConceptoPago.CAMBIO_DE_LOCAL.getValue());// CAMBIO DE LOCAL
			movimientoDetalle.setDescuento(BigDecimal.ZERO);
			movimientoDetalle.setId_fmo(id_fmo);
			movimientoDetalle.setMonto(total);
			movimientoDetalle.setMonto_total(total);
			// MENSUALIDAD DE NOVIEMBRE HUERTA JHADDE MAJHALL-3 Aï¿½OS-B INI
			movimientoDetalle.setObs("DIFERENCIA DE MATRICULA POR CAMBIO DE LOCAL "  
					+ " " + datosAlumno.getString("alumno") + " -" + datosAlumno.getString("grado") + "-" + datosAlumno.getString("secc") + " "
					+ datosAlumno.getString("nivel").substring(0, 3));
			movimientoDetalle.setEst("A");
 			movimientoDetalle.setFec_ins(new Date());

			int id_fmd = movimientoDetalleDAO.saveOrUpdate(movimientoDetalle);
			
			/* Grabar movimiento descuentos */
	/*	
			grabarMovimientoDescuento(id_fmd,"MATRICULA ANTERIOR "  
					+ " " + datosAlumno.getString("alumno") + " -" + datosAlumno.getString("grado") + "-" + aulaActual.getSecc() + " "
					+ datosAlumno.getString("nivel").substring(0, 3), 
					totalPagados, usuario);
*/
		
			Impresion impresion = new Impresion();
			ImpresionCabecera impresionCabecera = new ImpresionCabecera();
			impresionCabecera.setNombreBoleta("BOLETA ELECTRÓNICA");
			impresionCabecera.setNro(nro_rec);
			impresionCabecera.setDia(FechaUtil.toString(new Date(), "dd/MM/yyyy"));
			impresionCabecera.setComercio(empresa.getString("giro_negocio").toUpperCase());
			impresionCabecera.setHora(FechaUtil.toString(new Date(), "hh:mm a"));
			impresionCabecera.setUsuario( usuarioSeg.getNombres());
			Sucursal local = sucursalDAO.get(id_suc);
			impresionCabecera.setTelefono(local.getTel());
			impresionCabecera.setLocal(local.getDir());

			impresion.setCabecera(impresionCabecera);

			ImpresionCliente impresionCliente = new ImpresionCliente();
			Familiar familiar = familiarDAO.get(matricula.getId_fam());

			impresionCliente.setDireccion(familiar.getDir() == null ? "" : familiar.getDir());
			impresionCliente.setNombre(familiar.getApe_pat() + " " + familiar.getApe_mat() + ", " + familiar.getNom());
			if (familiar.getId_tdc().intValue() == Constante.TIPO_DOCUMENTO_DNI.intValue())
				impresionCliente.setTip_doc("DNI");
			else if (familiar.getId_tdc().intValue() == Constante.TIPO_DOCUMENTO_PASAPORTE.intValue())
				impresionCliente.setTip_doc("PASAPORTE");
			else
				impresionCliente.setTip_doc("CARNE EXTRANJERIA");
			
			impresionCliente.setNro_doc(familiar.getNro_doc());
			
			impresion.setCliente(impresionCliente);
			
			ImpresionItem impresionItem = new ImpresionItem();
			impresionItem.setNro(1);
			 
						
			impresionItem.setDescripcion("DIFERENCA POR CAMBIO DE LOCAL "  
					+ " " + datosAlumno.getString("alumno")  +  " -" + datosAlumno.getString("grado") + "-" + datosAlumno.getString("secc") + " " + datosAlumno.getString("nivel").substring(0, 3));
			
			impresionItem.setPrecioUnit(total);
			impresionItem.setCantidad(1);
			impresionItem.setDescuento(BigDecimal.ZERO);
			impresionItem.setPrecio(total);
			impresionItem.setPrecioUnit(total);
			impresionItem.setTipoOperacion("OPE_INA");
			impresion.getItems().add(impresionItem);
			
			updateNroRecibo(id_suc, nro_rec);
			
			return impresion;
			
		} else {
			// EL COLEGIO DEBE DEVOLVER LA DIFERENCIA
			/*
			MatriculaPagos academicoPago = new MatriculaPagos();
			academicoPago.setCanc("0");// Por defecto pago pendiente
			academicoPago.setMonto(total);
			academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_CAMBIO_LOCAL);
			listPagados.add(academicoPago);
			*/
			total = total.multiply(new BigDecimal(-1));
			
			// DEBE PAGAR LA DIFERENCIA
				//Para generar la nota de credito, se debe afectar al recibo afecta y a ese local se emite la nota de credito
				Integer id_suc_con=matricula.getId_suc_con(); //sucursal del contrato donde se hizo la primera matricula
				//String nro_rec = getNroReciboNotaCredito(id_suc);
				String nro_rec = getNroReciboNotaCredito(id_suc_con);

				Row datosAlumno = matriculaDAO.getDatosAlumno(matricula.getId());
				
				Movimiento movimiento = new Movimiento();
				movimiento.setEst("A");
				movimiento.setFec(new Date());
				movimiento.setId_fam(matricula.getId_fam());
				movimiento.setId_mat(matricula.getId());
				//movimiento.setId_suc(id_suc);
				movimiento.setId_suc(id_suc_con);
				movimiento.setMonto(total);
				movimiento.setDescuento(BigDecimal.ZERO);
				movimiento.setMonto_total(total);
				movimiento.setObs("DEVOLUCIÓN DE DINERO POR CAMBIO DE LOCAL "  
						+ " " + datosAlumno.getString("alumno") + " -" + datosAlumno.getString("grado") + "-" + datosAlumno.getString("secc") + " "
						+ datosAlumno.getString("nivel").substring(0, 3));
				movimiento.setId_fpa(1);// efectivo
				movimiento.setTipo("S");//salida de efectivo
				movimiento.setNro_rec(nro_rec);

				int id_fmo = movimientoDAO.saveOrUpdate(movimiento);

				MovimientoDetalle movimientoDetalle = new MovimientoDetalle();
				movimientoDetalle.setId_fco(EnumConceptoPago.CAMBIO_DE_LOCAL.getValue());// CAMBIO DE LOCAL
				movimientoDetalle.setDescuento(BigDecimal.ZERO);
				movimientoDetalle.setId_fmo(id_fmo);
				movimientoDetalle.setMonto(total);
				movimientoDetalle.setMonto_total(total);
				// MENSUALIDAD DE NOVIEMBRE HUERTA JHADDE MAJHALL-3 Aï¿½OS-B INI
				movimientoDetalle.setObs("DEVOLUCIÓN DE DINERO POR CAMBIO DE LOCAL "  
						+ " " + datosAlumno.getString("alumno") + " -" + datosAlumno.getString("grado") + "-" + datosAlumno.getString("secc") + " "
						+ datosAlumno.getString("nivel").substring(0, 3));
				movimientoDetalle.setEst("A");
				movimientoDetalle.setFec_ins(new Date());

				int id_fmd = movimientoDetalleDAO.saveOrUpdate(movimientoDetalle);
				
				//Obtenemos el id_mov con el numero de recibo
				Integer id_mov=movimientoDAO.getByParams(new Param("nro_rec",map.get("nro_rec").toString())).getId();
				//Insertamos en la Tabla Notas de Credito
				NotaCredito notaCredito = new NotaCredito();
				notaCredito.setId_fmo(id_mov);
				notaCredito.setId_fmo_nc(id_fmo);
				notaCredito.setMotivo("DEVOLUCION POR CAMBIO DE LOCAL");
				notaCredito.setMonto(total);
				notaCredito.setEst("A");
				notaCredito.setFec_emi(new Date());
				notaCreditoDAO.saveOrUpdate(notaCredito);
			
				Impresion impresion = new Impresion();
				ImpresionCabecera impresionCabecera = new ImpresionCabecera();
				impresionCabecera.setNombreBoleta("NOTA DE CRÉDITO");
				impresionCabecera.setNro(nro_rec);
				impresionCabecera.setDia(FechaUtil.toString(new Date(), "dd/MM/yyyy"));
				impresionCabecera.setComercio(empresa.getString("giro_negocio").toUpperCase());
				impresionCabecera.setHora(FechaUtil.toString(new Date(), "hh:mm a"));
				impresionCabecera.setUsuario( usuarioSeg.getNombres());
				Sucursal local = sucursalDAO.get(id_suc);
				impresionCabecera.setTelefono(local.getTel());
				impresionCabecera.setLocal(local.getDir());

				impresion.setCabecera(impresionCabecera);

				ImpresionCliente impresionCliente = new ImpresionCliente();
				Familiar familiar = familiarDAO.get(matricula.getId_fam());

				impresionCliente.setDireccion(familiar.getDir() == null ? "" : familiar.getDir());
				impresionCliente.setNombre(familiar.getApe_pat() + " " + familiar.getApe_mat() + ", " + familiar.getNom());
				if (familiar.getId_tdc().intValue() == Constante.TIPO_DOCUMENTO_DNI.intValue())
					impresionCliente.setTip_doc("DNI");
				else if (familiar.getId_tdc().intValue() == Constante.TIPO_DOCUMENTO_PASAPORTE.intValue())
					impresionCliente.setTip_doc("PASAPORTE");
				else
					impresionCliente.setTip_doc("CARNE EXTRANJERIA");
				
				impresionCliente.setNro_doc(familiar.getNro_doc());
				
				impresion.setCliente(impresionCliente);
				
				ImpresionItem impresionItem = new ImpresionItem();
				impresionItem.setNro(1);
				 
							
				impresionItem.setDescripcion("DEVOLUCIÓN DE DINERO POR CAMBIO DE LOCAL "  
						+ " " + datosAlumno.getString("alumno")  +  " -" + datosAlumno.getString("grado") + "-" + datosAlumno.getString("secc") + " "
						+ datosAlumno.getString("nivel").substring(0, 3));
				
				impresionItem.setPrecioUnit(total);
				impresionItem.setCantidad(1);
				impresionItem.setDescuento(BigDecimal.ZERO);
				impresionItem.setPrecio(total);
				impresionItem.setPrecioUnit(total);
				impresionItem.setTipoOperacion("OPE_INA");
				//Movimiento movimiento = movimientoDAO.get(notaCreditoForm.getId_fmo());
				DocumentoReferencia documentoReferencia = new DocumentoReferencia();
				documentoReferencia.setNro_rec(map.get("nro_rec").toString());
				documentoReferencia.setTipoDocumento("03");//BOLETA ELECTRONICA
				impresion.setDocumentoReferencia(documentoReferencia);
				impresion.getItems().add(impresionItem);
				
				// El envio seria luego
				/*//luego de tener la impresion recien enviaria a SUNAT
				SunatResultJson resultSunat = facturacionService.enviarNotaCreditoRestSunat(impresiones);
				String code = resultSunat.getCode();
				
				if (code.equals("0")) {
					
					//PASO CORRECTAMENTE
				
					//for (Impresion impresion : impresiones) {
						
						//System.out.println(notaCreditoForm);
						 
						notaCreditoDAO.actualizarCodRes(notaCreditoForm.getId(), resultSunat.getRespuesta_sunat(), Integer.parseInt(resultSunat.getId_eiv()), resultSunat.getArchivo(), impresiones.get(0).getCabecera().getNro());
						
						//movimientoDAO.actualizarNCCodRes(notaCreditoForm, impresion.getCabecera().getNro(), resultSunat.getArchivo(), resultSunat.getId_eiv(), resultSunat.getRespuesta_sunat(),FechaUtil.toDate(impresion.getCabecera().getDia(), "dd/MM/yyyy") );	
					//}
					
					
					//comunicacionBajaDAO.actualizarCodRes(comunicacionBajaDTO.getId(),resultSunat.getTicket(),Integer.parseInt(resultSunat.getId_eiv()), resultSunat.getRespuesta_sunat());

				}else{
					//ERROR EN EL ENVIO A SUNAT
					result.setCode(code);
					result.setMsg(resultSunat.getRespuesta_sunat());
				}*/
				//updateNroReciboNotaCredito(id_suc, nro_rec); 			
				updateNroReciboNotaCredito(id_suc_con, nro_rec); 
				return impresion;
			
			
		}
		
	}
	
	private List<MatriculaPagos> obtenerPagosProgramados( Integer id_alu, Integer id_au, Integer id_anio) {
		// obtener periodo de estudio

		List<MatriculaPagos> list = new ArrayList<MatriculaPagos>();
		
		Aula aula = aulaDAO.get(id_au);

		
		// pago que le corresponde para la matricula
		ConfCuota confCuota = confCuotaDAO.getByParams(new Param("id_per", aula.getId_per()));
		MatriculaPagos academicoPago = new MatriculaPagos();
		academicoPago.setCanc("0");// Por defecto pago pendiente
		academicoPago.setMonto(confCuota.getMatricula());
		academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_MATRICULA);

		ReservaCuota reservaCuota = reservaDAO.getMontoReserva(id_anio, id_alu);

		if (reservaCuota != null) {

			BigDecimal montoReserva = reservaCuota.getMonto();
			academicoPago.setMontoReserva(montoReserva);
		}
		list.add(academicoPago);
		Matricula mat_anterior = obtenerMatriculaAnterior(id_alu, id_anio);
		
		//if (obtenerMatriculaAnterior(id_alu).equals(com.tesla.colegio.util.Constante.CLIENTE_NUEVO)) {
		if (mat_anterior==null) {//alumno nuevo
			academicoPago = new MatriculaPagos();
			academicoPago.setCanc("0");// Por defecto pago pendiente
			academicoPago.setMonto(confCuota.getCuota());
			academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_CUOTA_INGRESO);
			list.add(academicoPago);
		}

		return list;
	}
	
	private List<MatriculaPagos> obtenerPagosProgramadosxPeriodo( Integer id_alu, Integer id_per, Integer id_anio) {
		// obtener periodo de estudio

		List<MatriculaPagos> list = new ArrayList<MatriculaPagos>();
		
		// pago que le corresponde para la matricula
		ConfCuota confCuota = confCuotaDAO.getByParams(new Param("id_per", id_per));
		MatriculaPagos academicoPago = new MatriculaPagos();
		academicoPago.setCanc("0");// Por defecto pago pendiente
		academicoPago.setMonto(confCuota.getMatricula());
		academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_MATRICULA);

		ReservaCuota reservaCuota = reservaDAO.getMontoReserva(id_anio, id_alu);

		if (reservaCuota != null) {

			BigDecimal montoReserva = reservaCuota.getMonto();
			academicoPago.setMontoReserva(montoReserva);
		}
		list.add(academicoPago);
		Matricula mat_anterior = obtenerMatriculaAnterior(id_alu, id_anio);
		
		//if (obtenerMatriculaAnterior(id_alu).equals(com.tesla.colegio.util.Constante.CLIENTE_NUEVO)) {
		if (mat_anterior==null) {//alumno nuevo
			academicoPago = new MatriculaPagos();
			academicoPago.setCanc("0");// Por defecto pago pendiente
			academicoPago.setMonto(confCuota.getCuota());
			academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_CUOTA_INGRESO);
			list.add(academicoPago);
		}

		return list;
	}
	
	public Map<String,Object>pagosMatriculaRealizados(Integer id_mat, String tip_cambio){
		
		Map<String, Object> map = new HashMap<String, Object>();
		MatriculaPagos pagos = new MatriculaPagos();
		
		Matricula matricula = matriculaDAO.getByParams(new Param("id",id_mat));
		
		Periodo periodo = periodoDAO.getByParams(new Param("id",matricula.getId_per()));

		Param param = new Param();
		param.put("id_mat", id_mat);
		param.put("tip", com.tesla.colegio.util.Constante.PAGO_MATRICULA);
		List<AcademicoPago> pagosMatricula = academicoPagoDAO.listByParams(param, new String[] { "id asc" });
		//Obtener Pago Matricula Real
		ConfCuota cuota_matricula = confCuotaDAO.getByParams(new Param("id_per",periodo.getId()));
		//pagos.setMonto(cuota_matricula.getMatricula());//ponemos lo real antes(pagosMatricula.get(0).getMonto())
		pagos.setMonto(pagosMatricula.get(0).getMonto());//ponemos lo real antes(pagosMatricula.get(0).getMonto())
		pagos.setNro_rec(pagosMatricula.get(0).getNro_rec());
		pagos.setTip(com.tesla.colegio.util.Constante.PAGO_MATRICULA);
		
		List<MatriculaPagos> listPagar =  new ArrayList<MatriculaPagos>();
		BigDecimal total = BigDecimal.ZERO;
	
		total = total.add(pagosMatricula.get(0).getMonto());
		listPagar.add(pagos);

		param = new Param();
		param.put("id_mat", id_mat);
		param.put("tip", com.tesla.colegio.util.Constante.PAGO_CUOTA_INGRESO);
		List<AcademicoPago> pagosCuotaIngreso = academicoPagoDAO.listByParams(param, new String[] { "id asc" });
		if (pagosCuotaIngreso.size() > 0){
			pagos = new MatriculaPagos();
			pagos.setMonto(pagosCuotaIngreso.get(0).getMonto());
			pagos.setNro_rec(pagosCuotaIngreso.get(0).getNro_rec());
			pagos.setTip(com.tesla.colegio.util.Constante.PAGO_CUOTA_INGRESO);
			total = total.add(pagos.getMonto());

			listPagar.add(pagos);			
		}
		
		ReservaCuota reservaCuota = reservaDAO.getMontoReserva(periodo.getId_anio(), matricula.getId_alu());

		if (reservaCuota != null) {
			/** PAGO POR RESERVA **/
			BigDecimal montoReserva = reservaCuota.getMonto();
			pagos = new MatriculaPagos();
			pagos.setMonto(montoReserva.multiply(new BigDecimal(-1)));
			pagos.setNro_rec(reservaCuota.getNro_recibo());
			pagos.setTip(com.tesla.colegio.util.Constante.PAGO_RESERVA);
			pagos.setNro_rec(reservaCuota.getNro_recibo());
			if(tip_cambio!=null){
				if(tip_cambio.equals("CL")){
					total = total.subtract(pagos.getMonto());
				}
			}
			//total = total.subtract(pagos.getMonto());//aqui antes comentado
			listPagar.add(pagos);		
			
		}
		
		
		map.put("pagos", listPagar);
		map.put("nro_rec", pagosMatricula.get(0).getNro_rec());
		if(tip_cambio!=null){
			if(tip_cambio.equals("CS") && matricula.getMat_val().equals("1")){//Si solo fue cambio de seccion le total es cero
				map.put("total", 0);
			} else if(tip_cambio.equals("CS") && matricula.getMat_val().equals("0")){
				map.put("total", total);
			} else if(tip_cambio.equals("CL")){
				map.put("total", total);
			}
		} else{
			map.put("total", total);
		}

		return map;
	}
	

	private Matricula obtenerMatriculaAnterior(Integer id_alu, Integer id_anio) {
		
		Anio anio = anioDAO.get(id_anio);
		int anio_anterior = Integer.parseInt(anio.getNom()) - 1;

		Param param = new Param();
		param.put("nom", anio_anterior);
		Anio anioAnterior = anioDAO.getByParams(param);

		Matricula matricula = matriculaDAO.getMatriculaAnterior(id_alu, anioAnterior.getId());

		return matricula;
		/*
		 * if(matricula == null) return
		 * com.tesla.colegio.util.Constante.CLIENTE_NUEVO; else return
		 * com.tesla.colegio.util.Constante.CLIENTE_ALUMNO;//TODO FALTA
		 * REENTRANTE
		 */
	}

	
	public Impresion getImpresion (String nro_rec, Integer id_anio, Integer id_alu){
		Movimiento movimiento = movimientoDAO.getFullByNroRec(nro_rec,new String[]{Familiar.TABLA,Alumno.TABLA, Usuario.TABLA, Sucursal.TABLA, "col_persona_a", "col_persona_f"});

		logger.debug("nuevo recibo:" + nro_rec);
		//Datos de la Empresa
		Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);
		
		Impresion impresion = new Impresion();
		ImpresionCabecera impresionCabecera = new ImpresionCabecera();
		impresionCabecera.setNombreBoleta("BOLETA ELECTRÓNICA");
		impresionCabecera.setNro(nro_rec);
		impresionCabecera.setDia(FechaUtil.toString(movimiento.getFec(), "dd/MM/yyyy"));
		impresionCabecera.setComercio(empresa.getString("giro_negocio").toUpperCase());
		impresionCabecera.setHora(FechaUtil.toString(movimiento.getFec_ins(), "hh:mm a"));
		impresionCabecera.setUsuario((movimiento.getUsuario().getTrabajador().getNom() + " " + movimiento.getUsuario().getTrabajador().getApe_pat()).toUpperCase());
		impresionCabecera.setTelefono(movimiento.getSucursal().getTel());
		impresionCabecera.setLocal(movimiento.getSucursal().getDir());
		impresion.setCabecera(impresionCabecera);
		
		
		ImpresionCliente impresionCliente = new ImpresionCliente(); 
		//Direccion de la familia
		GruFamAlumno gru_faFamAlumno= gruFamAlumnoDAO.getByParams(new Param("id_alu",id_alu));
		GruFam gruFam= gruFamDAO.get(gru_faFamAlumno.getId_gpf());
		//impresionCliente.setDireccion( movimiento.getFamiliar().getDir()==null?"": movimiento.getFamiliar().getDir());
		impresionCliente.setDireccion( gruFam.getDireccion()==null?"": gruFam.getDireccion());
		impresionCliente.setNombre(movimiento.getPersona_fam().getApe_pat() + " " + movimiento.getPersona_fam().getApe_mat() + ", " + movimiento.getPersona_fam().getNom());
		impresionCliente.setTip_doc(movimiento.getPersona_fam().getTipoDocumento().getNom());
		impresionCliente.setNro_doc(movimiento.getPersona_fam().getNro_doc());
		impresion.setCliente(impresionCliente);	
			
		//FIN - FACTURA ELECTRONICA
		
		List<MovimientoDetalle> items = movimientoDetalleDAO.listFullByParams(new Param("id_fmo", movimiento.getId()), new String[]{"fmd.id"});
		int i=0;
		for (MovimientoDetalle movimientoDetalle : items) {
			ImpresionItem impresionItem = new ImpresionItem();
			impresionItem.setNro(i+1);
			if(movimientoDetalle.getId_fco().equals(Constante.TIPO_CONCEPTO_RESERVA)) {
				Row reserva = reservaDAO.obtenerReservaAlumno(id_anio, id_alu);
				Alumno alumno = alumnoDAO.get(reserva.getInt("id_alu"));
				Persona persona = personaDAO.get(alumno.getId_per());
				impresionItem.setDescripcion( "Alumno:" + persona.getApe_pat() + " " + persona.getApe_mat() + ", " + persona.getNom()  + "\n " + movimientoDetalle.getConcepto().getNom().toUpperCase()  );
			} else {
				impresionItem.setDescripcion(movimientoDetalle.getObs());
			}
				
			//impresionItem.setDescripcion( "Alumno:" + movimiento.getMatricula().getAlumno().getPersona().getApe_pat() + " " + movimiento.getMatricula().getAlumno().getPersona().getApe_mat() + ", " + movimiento.getMatricula().getAlumno().getPersona().getNom()  + "\n " + movimientoDetalle.getConcepto().getNom().toUpperCase()  );
			//impresionItem.setDescripcion( "Alumno:" +  + " " + movimiento.getPersona_alu().getApe_mat() + ", " + movimiento.getPersona_alu().getNom()  + "\n " + movimientoDetalle.getConcepto().getNom().toUpperCase()  );
			//impresionItem.setDescripcion(movimientoDetalle.getObs());//se comento xq mostraba seccion para reserva
			impresionItem.setDescuento(movimientoDetalle.getDescuento());//
			impresionItem.setPrecioUnit(movimientoDetalle.getMonto());
			impresionItem.setCantidad(1);
			impresionItem.setPrecio(movimientoDetalle.getMonto());
			impresionItem.setTipoOperacion("OPE_INA");
			
			Param param = new Param();
			param.put("id_fmd", movimientoDetalle.getId());
			param.put("est", "A");
			
			List<MovimientoDescuento> descuentois = movimientoDescuentoDAO.listByParams(param, null);
			
			for (MovimientoDescuento movimientoDescuento: descuentois) {
				impresionItem.getDescuentos().add(new ImpresionDcto(movimientoDescuento.getDes(), movimientoDescuento.getDescuento() ));	
			}
				
			impresion.getItems().add(impresionItem);
		} 
		
		
		return impresion;
	}
	
	public Impresion getImpresion (Integer id_fmo, Integer id_alu){
		Movimiento movimiento1 = movimientoDAO.get(id_fmo);
		String nro_rec = movimiento1.getNro_rec();
		
	//	Movimiento movimiento = movimientoDAO.getFullByNroRec(movimiento1.getNro_rec(),new String[]{Familiar.TABLA,Alumno.TABLA, Usuario.TABLA, Sucursal.TABLA});
		//Movimiento movimiento = movimientoDAO.getFullByNroRec(nro_rec,new String[]{Familiar.TABLA, Usuario.TABLA, Sucursal.TABLA});
		Movimiento movimiento = movimientoDAO.getFullByNroRec(nro_rec,new String[]{Familiar.TABLA,Alumno.TABLA, Usuario.TABLA, Sucursal.TABLA, "col_persona_a", "col_persona_f"});
		
		//Datos de la empresa
		Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);

		//logger.debug("nuevo recibo:" + nro_rec);
		
		//INICIO - FACTURA ELECTRONICA
		//Ver si es Nota de Credito
		List<AcademicoPago> academicoPagos = new ArrayList<>();
		NotaCredito nota_credito=notaCreditoDAO.getByParams(new Param("id_fmo_nc",id_fmo));
		DocumentoReferencia documentoReferencia = new DocumentoReferencia();
		if(nota_credito!=null) {
			//AcademicoPago pago_afec= academicoPagoDAO.getByParams(new Param("id",nota_credito.getId_fmo()));
			Movimiento pago_afec=movimientoDAO.getByParams(new Param("id",nota_credito.getId_fmo()));
			academicoPagos = academicoPagoDAO.listFullByParams(new Param("fac_acad.nro_rec",pago_afec.getNro_rec()), null);
		
			documentoReferencia.setNro_rec(pago_afec.getNro_rec());
		
		} else {
			academicoPagos = academicoPagoDAO.listFullByParams(new Param("fac_acad.nro_rec",nro_rec), null);
		}
		
		AcademicoPago academicoPago = null;
		if(academicoPagos.size()>0) {
			//academicoPago = academicoPagoDAO.listFullByParams(new Param("fac_acad.nro_rec",nro_rec), null).get(0);
			academicoPago = academicoPagos.get(0);
		}
		
		Impresion impresion = new Impresion();
		ImpresionCabecera impresionCabecera = new ImpresionCabecera();
		if(movimiento.getTipo().equals("S"))
			impresionCabecera.setNombreBoleta("NOTA DE CRÉDITO");
		else
			impresionCabecera.setNombreBoleta("BOLETA ELECTRÓNICA");
		impresionCabecera.setNro(nro_rec);
		impresionCabecera.setDia(FechaUtil.toString(movimiento.getFec(), "dd/MM/yyyy"));
		impresionCabecera.setComercio(empresa.getString("giro_negocio").toUpperCase());
		impresionCabecera.setHora(FechaUtil.toString(movimiento.getFec_ins(), "hh:mm a"));
		
		//Grupo Familiar
		GruFamAlumno gru_faFamAlumno= gruFamAlumnoDAO.getByParams(new Param("id_alu",id_alu));
		GruFam gruFam= gruFamDAO.get(gru_faFamAlumno.getId_gpf());
		//impresionCliente.setDireccion( movimiento.getFamiliar().getDir()==null?"": movimiento.getFamiliar().getDir());
		
		if (movimiento.getId_fpa().intValue()==1)
			impresionCabecera.setUsuario((movimiento.getUsuario().getTrabajador().getNom() + " " + movimiento.getUsuario().getTrabajador().getApe_pat()).toUpperCase());
		else{
			Param param = new Param();
			param.put("nro_rec", nro_rec);
			param.put("est", "A");
			//AcademicoPago academico = academicoPagoDAO.getByParams(param);
			impresionCabecera.setUsuario("BANCO " + academicoPago.getBanco());
		} 
		impresionCabecera.setTelefono(movimiento.getSucursal().getTel());
		impresionCabecera.setLocal(movimiento.getSucursal().getDir());
		
		impresion.setCabecera(impresionCabecera);
		impresion.setDocumentoReferencia(documentoReferencia);
		
		ImpresionCliente impresionCliente = new ImpresionCliente(); 
		Matricula matricula = null;
		if(academicoPago!=null) {
			matricula = matriculaDAO.get(academicoPago.getId_mat());
		}
		
		List<Persona> datosCliente = null;
		if(matricula!=null) {
			if(matricula.getTipo()!=null) {
				if(matricula.getTipo().equals("C")) {
					if(matricula.getId_fam_res_pag()!=null && !matricula.getId_fam_res_pag().equals(0)) {
						Familiar familiar = familiarDAO.get(matricula.getId_fam_res_pag());
						datosCliente = personaDAO.listFullByParams(new Param("per.id",familiar.getId_per()), null);
						
					} else {
						datosCliente = personaDAO.listFullByParams(new Param("per.id",matricula.getId_per_res()), null);
					}
				} else {
					if(matricula.getId_fam()!=null && !matricula.getId_fam().equals(0)) {
						Familiar familiar = familiarDAO.get(matricula.getId_fam());
						datosCliente = personaDAO.listFullByParams(new Param("per.id",familiar.getId_per()), null);
						
					} else {
						datosCliente = personaDAO.listFullByParams(new Param("per.id",matricula.getId_per_res()), null);
					}
				}
			} else {
				if(matricula.getId_fam()!=null && !matricula.getId_fam().equals(0)) {
					Familiar familiar = familiarDAO.get(matricula.getId_fam());
					datosCliente = personaDAO.listFullByParams(new Param("per.id",familiar.getId_per()), null);
					
				} else {
					datosCliente = personaDAO.listFullByParams(new Param("per.id",matricula.getId_per_res()), null);
				}
			}
		
				
		}
		
		Persona cliente = null;
		if(datosCliente!=null) {
			if(datosCliente.size()>0) {
				cliente=datosCliente.get(0);
			}
		}
		
		/*if(matricula.getId_fam()!=null && matricula.getTipo()==null) {
			Familiar familiar = familiarDAO.get(matricula.getId_fam());
			Persona datosCliente1 = personaDAO.listFullByParams(new Param("per.id",familiar.getId_per()), null).get(0);
			impresionCliente.setDireccion( datosCliente1.getDir()==null?"": datosCliente1.getDir());
			impresionCliente.setNombre(datosCliente1.getNom() + " " + datosCliente1.getApe_pat() + " " + datosCliente1.getApe_mat());
			impresionCliente.setTip_doc(datosCliente1.getTipoDocumento().getNom());
			impresionCliente.setNro_doc(datosCliente1.getNro_doc());
		} else {
			impresionCliente.setDireccion( cliente.getDir()==null?"": cliente.getDir());
			impresionCliente.setNombre(cliente.getNom() + " " + cliente.getApe_pat() + " " + cliente.getApe_pat());
			impresionCliente.setTip_doc(cliente.getTipoDocumento().getNom());
			impresionCliente.setNro_doc(cliente.getNro_doc());
		}*/
			
		//
		impresionCliente.setDireccion( gruFam.getDireccion()==null?"": gruFam.getDireccion());
		if(cliente!=null) {
			impresionCliente.setNombre(cliente.getNom() + " " + cliente.getApe_pat() + " " + cliente.getApe_mat());
			impresionCliente.setTip_doc(cliente.getTipoDocumento().getNom());
			impresionCliente.setNro_doc(cliente.getNro_doc());
		} else {
			impresionCliente.setNombre(movimiento.getPersona_fam().getApe_pat() + " " + movimiento.getPersona_fam().getApe_mat() + ", " + movimiento.getPersona_fam().getNom());
			impresionCliente.setTip_doc(movimiento.getPersona_fam().getTipoDocumento().getNom());
			impresionCliente.setNro_doc(movimiento.getPersona_fam().getNro_doc());
		}
		
		impresion.setCliente(impresionCliente);	
		
		impresion.setCliente(impresionCliente);	
			
		//FIN - FACTURA ELECTRONICA
		
		List<MovimientoDetalle> items = movimientoDetalleDAO.listFullByParams(new Param("id_fmo", movimiento.getId()), new String[]{"fmd.id"});
		int i=0;
		for (MovimientoDetalle movimientoDetalle : items) {
			ImpresionItem impresionItem = new ImpresionItem();
			impresionItem.setNro(i+1);

			//impresionItem.setDescripcion( "Alumno:" + movimiento.getMatricula().getAlumno().getApe_pat() + " " + movimiento.getMatricula().getAlumno().getApe_mat() + ", " + movimiento.getMatricula().getAlumno().getNom()  + "\n " + movimientoDetalle.getConcepto().getNom().toUpperCase()  );
			impresionItem.setDescripcion(movimientoDetalle.getObs());
			impresionItem.setDescuento(movimientoDetalle.getDescuento());//
			impresionItem.setPrecioUnit(movimientoDetalle.getMonto());
			impresionItem.setCantidad(1);
			impresionItem.setPrecio(movimientoDetalle.getMonto());
			impresionItem.setTipoOperacion("OPE_INA");
			
			Param param = new Param();
			param.put("id_fmd", movimientoDetalle.getId());
			param.put("est", "A");
			
			List<MovimientoDescuento> descuentois = movimientoDescuentoDAO.listByParams(param, null);
			
			for (MovimientoDescuento movimientoDescuento: descuentois) {
				impresionItem.getDescuentos().add(new ImpresionDcto(movimientoDescuento.getDes(), movimientoDescuento.getDescuento() ));	
			}
				
			impresion.getItems().add(impresionItem);
		} 
		
		
		return impresion;
	}
	/*
	public List<Date> fechasPendientes(){
		String sql ="";
	}*/

	@Transactional
	public List<Impresion> generarFacturaElectronica(String nro_serie, String tip_com, boolean flag_correo ){

		List<Impresion> impresiones = new ArrayList<Impresion>();

		logger.debug("generar factura electronica por job!!");
		logger.debug("flag_correo:" + flag_correo);

		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		//Datos de la empresa
		Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);

		try {
			Date fec_ini = formatter.parse(formatter.format(today));
			 
			
			List<Row> list = movimientoDAO.pagosFacturaElectronica( fec_ini, fec_ini,null,null,tip_com, nro_serie, "2");//PENDIENTES DE ENVIO SUNAT
			
			logger.debug("nro_serie:" + nro_serie);
			logger.debug("total:" + list.size());
			logger.debug("list:" + list);

			List<Integer> id_fmos = new ArrayList<Integer>();
			for (Row row : list) {
				id_fmos.add(row.getInteger("id"));
				Impresion impresion = new Impresion();
				ImpresionCabecera cabecera = new ImpresionCabecera();
				cabecera.setNombreBoleta("BOLETA ELECTRÓNICA");
				cabecera.setNro(row.getString("nro_rec"));
				cabecera.setDia(FechaUtil.toString(fec_ini, "dd/MM/YYYY"));
				cabecera.setComercio(empresa.getString("empresa").toUpperCase());
				cabecera.setHora(FechaUtil.toString(new Date(), "hh:mm a"));
				impresion.setCabecera(cabecera);
				ImpresionCliente cliente = new ImpresionCliente();
				cliente.setDireccion("");
				cliente.setNombre(row.getString("ape_pat") + " " + row.getString("ape_mat") + ", " + row.getString("nom"));
				cliente.setCorreo(row.getString("corr"));
				cliente.setNro_doc(row.getString("nro_doc"));
				Integer tipdocSIGE = row.getInteger("id_tdc");//FALTA PARAMETRIZAR
				if (tipdocSIGE==1)//dni
					cliente.setTip_doc("1");
				if (tipdocSIGE==2)//pasaporte
					cliente.setTip_doc("7");
				if (tipdocSIGE==3)//carnet de extrangeria
					cliente.setTip_doc("4");

				impresion.setCliente(cliente);
				//OBTENER DETALLE1
				List<MovimientoDetalle> movimientoDetalles = movimientoDetalleDAO.getListByIDfmo(row.getInteger("id"), new String[]{"fac_movimiento","fac_concepto"});
				int i =0;
				for (MovimientoDetalle movimientoDetalle : movimientoDetalles) {
					
					ImpresionItem item = new ImpresionItem();
					 
					item.setCantidad(1);
					item.setDescripcion(movimientoDetalle.getObs());
					item.setDescuento(movimientoDetalle.getDescuento());
					item.setNro(++i);
					item.setPrecio(movimientoDetalle.getMonto_total());
					item.setPrecioUnit(movimientoDetalle.getMonto_total());
					item.setTipoOperacion("OPE_INA");
					impresion.getItems().add(item);
				}
			
				impresiones.add(impresion);
				
			}
			
			if(list.size()>0){
				
				logger.debug("impresiones" + impresiones);
				
				SunatResultJson respuesta = enviarResumenRestSunat( FechaUtil.toString(new Date()), impresiones);
				
				String archivo =  respuesta.getArchivo();//PARECE QUE NO SE USA
				String respuesta_sunat =  respuesta.getRespuesta_sunat();
				String id_eiv =  respuesta.getId_eiv();
				
				movimientoDAO.actualizarCodRes(id_fmos, archivo, id_eiv, respuesta_sunat,fec_ini);
			}

			
			logger.debug("antes de preguntar si envia correo");
			if (flag_correo){
			
				logger.debug("enviando correo");
				
				String texto= "<h1>FACTURACION ELECTRONICA</h1>";
				texto += "fecha:" + FechaUtil.toString(fec_ini, "dd/MM/YYYY");
				
				if(list.size()==0){
					texto += "<br>NO EXISTEN BOLETAS/FACTURAS PARA ENVIAR A SUNAT ";
					texto += "<br>NRO DE SERIES: " + nro_serie;
				}else{
					texto += "<br>DOCUMENTOS ENVIADOS A SUNAT:";
					texto += "<TABLE WIDTH='100%' border='0'>";
					texto += "<TR>";
					texto += "<TD>nro</TD><TD>Nro Doc</TD><TD>Cliente</TD><TD>Monto Total</TD>";
					texto += "</TR>";
					int i=1;
					for (Row row: list) {
						String nro_doc = row.getString("nro_doc");
						String cliente = row.getString("cliente");
						String monto_total = row.getString("monto_total");
					
						texto += "<TR>";
						texto += "<TD>" + (i++) + "</TD>" ;
						texto += "<TD>" +nro_doc + "</TD>" ;
						texto += "<TD>" +cliente + "</TD>" ;
						texto += "<TD>" +monto_total + "</TD>" ;
						texto += "</TR>";
					}
					texto += "</TABLE>";
				}	
				texto += "<br><br>Atentamente";
				texto += "<br><b>La DirecciÃ³n</b>";

					CorreoUtil correoUtil = new CorreoUtil();

					//correoUtil.enviar("ENVIO DE FACTURA ELECTRONICA", "AREA DE TESORERIA", new String[]{"michael.valle77@gmail.com","elebola@gmail.com"}, texto);

				
			}

		} catch (Exception e) {
			logger.error("error envio de faCtura", e);

			
			String texto= "<h1>FACTURACION ELECTRONICA</h1>";
			texto += "fecha:" + FechaUtil.toString(new Date(), "dd/MM/YYYY");

			texto += "<br>ERROR AL ENVIAR LA FACTURA ELECTRONICA:";

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String sStackTrace = sw.toString(); // stack trace as a string
			texto += sStackTrace;
			texto += "<br><br>Atentamente";
			texto += "<br><b>La Direcciï¿½n</b>";

			try {
				CorreoUtil correoUtil = new CorreoUtil();

				//correoUtil.enviar("ENVIO DE FACTURA ELECTRONICA", "AREA DE TESORERIA", new String[]{"michael.valle77@gmail.com","elebola@gmail.com"}, texto);
			} catch (Exception e1) {

				logger.error("error envio de correo", e);

			}

		}
		
		return impresiones;

	}
	
	public SunatResultJson enviarComunicacionBajaSunat( ComunicacionBajaBean comunicacionBajaBean) throws Exception{

		String output = null;
		String rpta = "";
		String ambiente = env.getProperty("ambiente");
		String fac_api = env.getProperty("fac-api");
		
		try {

			String	fechaEnvioSunat = FechaUtil.toString(new Date());

			String[] arrFecha = fechaEnvioSunat.split("/");

			String urlRest = fac_api + "/comunicacionBaja/generar/2";
			
			if (ambiente.equals("D"))
				urlRest = urlRest.replace("fac-web-api", "fac-web-api-desa");
				//urlRest = fac_api + "-desa/boleta/generar/2";
			
			logger.debug("Sending To Server ....:" + urlRest);

			URL url = new URL(urlRest);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setConnectTimeout(20000);

			String json = comunicacionJSON(comunicacionBajaBean);
			System.err.println(json);
			logger.debug(json);

			java.io.OutputStream os = conn.getOutputStream();
			os.write(json.getBytes("UTF-8"));
			os.flush();

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			logger.debug("Output from Server .... \n");

			while ((output = br.readLine()) != null) {
				if (output != null)
					rpta += output;
			}

			logger.debug("respuesta:" + rpta);
			conn.disconnect();

		} catch (MalformedURLException e) {

			logger.error("error de url",e );
			throw new ServiceException("Error de la url del servicio sunat");
		} catch (IOException e) {

			logger.error("io expception",e );

			throw new ServiceException("Error de escritura");

		} catch (Exception e) {

			logger.error("excepcion",e );

			throw new ServiceException("Error de la sunat");

		}
		Type listType = new TypeToken<SunatJson>() {
		}.getType();
		SunatJson rptaFactura1 = new Gson().fromJson(rpta, listType);

		return rptaFactura1.getResult();

	}

	 
	@Transactional
	public List<Impresion>  obtenerNotaCredito(NotaCredito notaCreditoForm){
		
		
		List<Impresion> impresiones = new ArrayList<Impresion>();
		Date fec_sunat = new Date();
		
		List<Row> list = movimientoDAO.pagosFacturaElectronica2(notaCreditoForm.getId_fmo(), "1");//ACEPTADO EN SUNAT
		
		List<Integer> id_fmos = new ArrayList<Integer>();
		
		//Datos de la empresa
		Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);
		
		for (Row row : list) {
			id_fmos.add(row.getInteger("id"));
			Impresion impresion = new Impresion();
			ImpresionCabecera cabecera = new ImpresionCabecera();
			cabecera.setNombreBoleta("NOTA DE CRÉDITO");
			//cabecera.setNro(row.getString("nro_rec"));
			cabecera.setDia(FechaUtil.toString(fec_sunat,"dd/MM/yyyy"));//FECHA DE LA NOTA DE CREDITO
			cabecera.setComercio(empresa.getString("empresa").toUpperCase());
			cabecera.setHora(FechaUtil.toString(new Date(), "hh:mm a"));
			cabecera.setComercio(empresa.getString("giro_negocio").toUpperCase());
			//if (movimiento.getId_fpa().intValue()==1)
			//cabecera.setUsuario(row.getString("tra_ape_pat") + " " + row.getString("tra_ape_mat")+ " "+ row.getString("tra_nom"));
			cabecera.setUsuario("SONIA HUAMAN ROSALES");
			cabecera.setTelefono(row.getString("suc_tel"));
			cabecera.setLocal(row.getString("suc_nom"));
			impresion.setCabecera(cabecera);
			ImpresionCliente cliente = new ImpresionCliente();
			cliente.setDireccion(row.getString("dir"));
			cliente.setNombre(row.getString("ape_pat") + " " + row.getString("ape_mat") + ", " + row.getString("nom"));
			cliente.setCorreo(row.getString("corr"));
			//OBTENER NUEVO NRO DE RECIBO DE LA NOTA DE CREDITO
			String arr[] = row.getString("nro_rec").split("-");//nro de serie del documento a afectar
			Param param = new Param();
			param.put("serie", arr[0]);
			Row configRecibo = confReciboDAO.getConfPorSerie(arr[0]);
			int correlativo = configRecibo.getInt("numero_nc") +1;
			String nro_serie_nc = configRecibo.getString("serie_nc") + "-" + String.format("%06d", correlativo);
			cabecera.setNro(nro_serie_nc);

			cliente.setNro_doc(row.getString("nro_doc"));
			Integer tipdocSIGE = row.getInteger("id_tdc");//FALTA PARAMETRIZAR
			if(tipdocSIGE!=null){
				if (tipdocSIGE==1)//dni
					cliente.setTip_doc("1");
				if (tipdocSIGE==2)//pasaporte
					cliente.setTip_doc("7");
				if (tipdocSIGE==3)//carnet de extrangeria
					cliente.setTip_doc("4");
			}else
				cliente.setTip_doc("");
			
			impresion.setCliente(cliente);
			 		
			ImpresionItem item = new ImpresionItem();
			 
			item.setCantidad(1);
			item.setDescripcion("DEVOLUCIÓN - "+notaCreditoForm.getMotivo());
			item.setDescuento(new BigDecimal("0.00"));
			item.setNro(1);
			item.setPrecio(notaCreditoForm.getMonto().setScale(2));
			item.setPrecioUnit(notaCreditoForm.getMonto().setScale(2));
			item.setTipoOperacion("OPE_INA");
			impresion.getItems().add(item);
			
			//DOCUMENTO AFECTADO
			//Movimiento movimiento = movimientoDAO.get(notaCreditoForm.getId_fmo());
			DocumentoReferencia documentoReferencia = new DocumentoReferencia();
			documentoReferencia.setNro_rec(row.getString("nro_rec"));
			documentoReferencia.setTipoDocumento("03");//BOLETA ELECTRONICA
			
			impresion.setDocumentoReferencia(documentoReferencia);
			impresiones.add(impresion);
			
		}
		return impresiones;
	}
	

	@Transactional
	public List<Impresion>  obtenerComunicacionBaja(ComunicacionBaja comunicacionBajaForm) throws ServiceException{
		
		
		List<Impresion> impresiones = new ArrayList<Impresion>();
		Date fec_sunat = new Date();
		
		List<Row> list = movimientoDAO.pagosByNCElectronica(comunicacionBajaForm.getId_fmo(), "1");//ACEPTADO EN SUNAT
		
		if(list.isEmpty()){
			throw new ServiceException("El documento original no se encuentra marcado en el SIGE como enviado a SUNAT, no puede darse de baja.");
		}
		
		List<Integer> id_fmos = new ArrayList<Integer>();
		//Datos de la empresa
		Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);
		
		for (Row row : list) {
			id_fmos.add(row.getInteger("id"));
			Impresion impresion = new Impresion();
			ImpresionCabecera cabecera = new ImpresionCabecera();
			cabecera.setNombreBoleta("BOLETA ELECTRÓNICA");
			//cabecera.setNro(row.getString("nro_rec"));
			cabecera.setDia(FechaUtil.toString(fec_sunat,"dd/MM/yyyy"));//FECHA DE LA NOTA DE CREDITO
			cabecera.setComercio(empresa.getString("empresa").toUpperCase());
			cabecera.setHora(FechaUtil.toString(new Date(), "hh:mm a"));
			if(row.getString("tipo").equals("I"))//BOLETA
				cabecera.setTipo_envio("03");
			if(row.getString("tipo").equals("N")){//NOTA DE CREDITO
				cabecera.setTipo_envio("07");
				DocumentoReferencia documentoReferencia = new DocumentoReferencia();
				
				Param param = new Param();
				param.put("id_fmo_nc", comunicacionBajaForm.getId_fmo());
				
				com.tesla.colegio.model.NotaCredito notaCredito = notaCreditoDAO.getByParams(param);
				
				Movimiento movimientoREf = movimientoDAO.get(notaCredito.getId_fmo()) ;
				
				documentoReferencia.setNro_rec(movimientoREf.getNro_rec());
				documentoReferencia.setTipoDocumento("03");//BOLETA ELECTRONICA
				impresion.setDocumentoReferencia(documentoReferencia);
			}
			
			ImpresionCliente cliente = new ImpresionCliente();
			cliente.setDireccion(row.getString("dir"));
			cliente.setNombre(row.getString("ape_pat") + " " + row.getString("ape_mat") + ", " + row.getString("nom"));
			cliente.setCorreo(row.getString("corr"));
			//OBTENER NUEVO NRO DE RECIBO DE LA NOTA DE CREDITO
			String nro_serie_nc= row.getString("nro_rec");//nro de serie del documento a afectar
			 
			cabecera.setNro(nro_serie_nc);
			
			impresion.setCabecera(cabecera);
			
			cliente.setNro_doc(row.getString("nro_doc"));
			Integer tipdocSIGE = row.getInteger("id_tdc");//FALTA PARAMETRIZAR
			if(tipdocSIGE!=null){
				if (tipdocSIGE==1)//dni
					cliente.setTip_doc("1");
				if (tipdocSIGE==2)//pasaporte
					cliente.setTip_doc("7");
				if (tipdocSIGE==3)//carnet de extrangeria
					cliente.setTip_doc("4");
			}else
				cliente.setTip_doc("");
			
			impresion.setCliente(cliente);
			 		
			ImpresionItem item = new ImpresionItem();
			 
			item.setCantidad(1);
			item.setDescripcion(comunicacionBajaForm.getMotivo());
			item.setDescuento(new BigDecimal("0.00"));
			item.setNro(1);
			item.setPrecio(row.getBigDecimal("monto_total"));
			item.setPrecioUnit(row.getBigDecimal("monto_total"));
			item.setTipoOperacion("OPE_INA");
			impresion.getItems().add(item);
			

			 
			impresiones.add(impresion);
			
		}
		return impresiones;
	}
	

	
	private String comunicacionJSON(ComunicacionBajaBean comunicacionBajaBean) {
		String json = "{";
		
		json = json + " \"fechaCreacion\":\"" + comunicacionBajaBean.getFechaCreacion() + "\"";
		//json = json + ",\"fechaEnvio\":\"" + comunicacionBajaBean.getFechaEnvio() + "\"";
		json = json + ", \"razonSocial\":\"" + comunicacionBajaBean.getRazonSocial() + "\"";
		json = json + ", \"ruc\":\"" + comunicacionBajaBean.getRuc() + "\"";
		
		json = json + ",\"items\":[";
		for (ComunicacionBajaItemBean item : comunicacionBajaBean.getItems()) {
			json = json + item.toString() + ",";
		}
		json = json.substring(0, json.length() - 1);
		json = json + "]}";

	
		return json;
	}
}

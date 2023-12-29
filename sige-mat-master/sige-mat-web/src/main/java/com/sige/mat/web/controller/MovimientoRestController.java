package com.sige.mat.web.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sige.core.dao.cache.CacheManager;
import com.sige.invoice.bean.Impresion;
import com.sige.invoice.bean.ImpresionCabecera;
import com.sige.invoice.bean.ImpresionCliente;
import com.sige.invoice.bean.ImpresionDcto;
import com.sige.invoice.bean.ImpresionItem;
import com.sige.mat.dao.AcademicoPagoDAO;
import com.sige.mat.dao.ConceptoDAO;
import com.sige.mat.dao.EmpresaDAO;
import com.sige.mat.dao.GruFamAlumnoDAO;
import com.sige.mat.dao.GruFamDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.MovimientoDAO;
import com.sige.mat.dao.MovimientoDetalleDAO;
import com.sige.mat.dao.SucursalDAO;
import com.sige.rest.request.MovimientoDetalleReq;
import com.sige.rest.request.MovimientoReq;
import com.sige.spring.service.FacturacionService;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.AcademicoPago;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.Concepto;
import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.GruFam;
import com.tesla.colegio.model.GruFamAlumno;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Movimiento;
import com.tesla.colegio.model.MovimientoDetalle;
import com.tesla.colegio.model.Sucursal;
import com.tesla.colegio.model.Usuario;
import com.tesla.colegio.model.UsuarioSeg;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.FechaUtil;
import com.tesla.frmk.util.NumberUtil;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@RestController
@RequestMapping(value = "/api/movimiento")
public class MovimientoRestController {

	final static Logger logger = Logger.getLogger(MovimientoRestController.class);

	@Autowired
	private MovimientoDAO movimientoDAO;

	@Autowired
	private AcademicoPagoDAO academicoPagoDAO;

	@Autowired
	private MatriculaDAO matriculaDAO;
	
	@Autowired
	private MovimientoDetalleDAO movimientoDetalleDAO;
 
	@Autowired
	private TokenSeguridad tokenSeguridad;

	
	@Autowired
	private FacturacionService facturacionService;

	@Autowired
	private SucursalDAO sucursalDAO;

	@Autowired
	private ConceptoDAO conceptoDAO;
	
	@Autowired
	private EmpresaDAO empresaDAO;
	
	@Autowired
	private GruFamDAO gruFamDAO;
	
	@Autowired
	private GruFamAlumnoDAO gruFamAlumnoDAO;
	
	@Autowired
	private CacheManager cacheManager;
	
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Integer id_alu, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		//result.setResult(movimientoDAO.consultarReporteCajaxAlumno( param, new String[]{"fmo.id"}) );
		result.setResult(movimientoDAO.consultarReporteCajaxAlumno(id_anio, id_alu));
		
		return result;

	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Movimiento movimiento) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
			if(movimiento.getId()==null && movimiento.getTipo().equals("I") ){
				String nro_rec = facturacionService.getNroRecibo(movimiento.getId_suc());
				movimiento.setNro_rec(nro_rec);
				facturacionService.updateNroRecibo(movimiento.getId_suc(), nro_rec);
			}

			//por el momento no existe descuentos
			movimiento.setDescuento(new BigDecimal(0));
			movimiento.setMonto_total(movimiento.getMonto());

			result.setResult( movimientoDAO.saveOrUpdate(movimiento) );
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}

	@RequestMapping( value="/ingreso", method = RequestMethod.POST)
	@Transactional
	public AjaxResponseBody grabarIngreso(@RequestBody MovimientoReq movimientoReq) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		UsuarioSeg usuarioSeg = tokenSeguridad.getUsuarioSeg();
 
		try {
			Integer id_mat = movimientoReq.getId_mat();
			
			Row alumno = matriculaDAO.getDatosAlumno(id_mat); 
			
			if (movimientoReq.getDetalle().length==0){
				result.setCode("500");
				result.setMsg("El pago no tiene movimientos");
				return  result;
			}
			
			//Datos de la empresa
			Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);
			
			//if (id_mat !=null){

				BigDecimal montoTotal = new BigDecimal(0);
				for (MovimientoDetalleReq detalle: movimientoReq.getDetalle()) {
					montoTotal = montoTotal.add(detalle.getMonto());
				}
				
				String nro_rec = facturacionService.getNroRecibo(movimientoReq.getId_suc());
				
				Matricula datosAlumno = null;
				if(id_mat!=null)
					datosAlumno = matriculaDAO.get(id_mat);
				
				//ES UN PAGO DE UN ALUMNO MATRICULADO
				Movimiento movimiento = new Movimiento();
				movimiento.setDescuento(new BigDecimal(0));
				movimiento.setEst("A");
				movimiento.setFec(FechaUtil.toDate(movimientoReq.getFec()));
				movimiento.setId_mat(id_mat);
				if (datosAlumno!=null)
					movimiento.setId_fam(datosAlumno.getId_fam());
				movimiento.setId_suc(movimientoReq.getId_suc());
				movimiento.setMonto(montoTotal);
				movimiento.setMonto_total(montoTotal);
				movimiento.setNro_rec(nro_rec);
				movimiento.setTipo(movimientoReq.getTipo());
				movimiento.setId_fpa(1);//EFECTIVO 
 				
				//se graba observacion en la cabecera para aquellos q no son alumnos
				String observacion = "";
				for (MovimientoDetalleReq detalle: movimientoReq.getDetalle()) {
					observacion = observacion + detalle.getObs() + ",";
				}
				observacion = observacion.substring(0, observacion.length()-1);
				
				movimiento.setObs(observacion);
				
				Integer id_fmo = movimientoDAO.saveOrUpdate(movimiento);
				
				for (MovimientoDetalleReq detalle: movimientoReq.getDetalle()) {

					String obs = "";
					if (detalle.getId_fco()!=null)
						obs = conceptoDAO.get(detalle.getId_fco()).getNom();
					if (detalle.getObs()!=null )
						obs  = obs + " " + detalle.getObs();
					MovimientoDetalle movimientoDetalle = new MovimientoDetalle();
					movimientoDetalle.setDescuento(new BigDecimal(0));
					movimientoDetalle.setEst("A");
					movimientoDetalle.setId_fco(detalle.getId_fco());
					movimientoDetalle.setId_fmo(id_fmo);
					movimientoDetalle.setMonto(detalle.getMonto());
					movimientoDetalle.setMonto_total(detalle.getMonto());
					if (alumno==null)
						movimientoDetalle.setObs(obs);
					else
						movimientoDetalle.setObs(obs + " " + alumno.getString("alumno"));
					movimientoDetalleDAO.saveOrUpdate(movimientoDetalle);
				}
				
				facturacionService.updateNroRecibo(movimientoReq.getId_suc(), nro_rec);
				
			//factura electronica
				
				
				Impresion impresion = new Impresion();
				ImpresionCabecera impresionCabecera = new ImpresionCabecera();
				impresionCabecera.setNombreBoleta("BOLETA ELECTR�NICA");
				impresionCabecera.setNro(nro_rec);
				impresionCabecera.setDia(FechaUtil.toString(new Date(), "dd/MM/yyyy"));
				impresionCabecera.setComercio(empresa.getString("giro_negocio"));
				impresionCabecera.setHora(FechaUtil.toString(new Date(), "hh:mm a"));
				impresionCabecera.setUsuario(usuarioSeg.getNombres());
				Sucursal local = sucursalDAO.get(usuarioSeg.getId_suc());
				impresionCabecera.setTelefono(local.getTel());
				impresionCabecera.setLocal(local.getDir());
				
				impresion.setCabecera(impresionCabecera);
				ImpresionCliente impresionCliente = new ImpresionCliente();
				Row datosCliente =null;
				if (id_mat!=null)
					datosCliente = matriculaDAO.getDatosApoderado(id_mat);
				
				if(datosCliente==null){
					//result.setCode("500");
					//result.setMsg("No se pudo obtener los datos del cliente");
					impresionCliente.setDireccion("");
					impresionCliente.setNombre("");
					impresionCliente.setTip_doc("1");//dni es 1? creo q si no? 
					impresionCliente.setNro_doc(null);
					impresion.setCliente(impresionCliente);	

				}else{
					impresionCliente.setDireccion( datosCliente.get("direccion")==null?"": datosCliente.getString("direccion"));
					impresionCliente.setNombre(datosCliente.getString("nombres"));
					impresionCliente.setTip_doc(datosCliente.getString("tip_doc"));
					impresionCliente.setNro_doc(datosCliente.getString("nro_doc"));
					impresion.setCliente(impresionCliente);	
					
				}
				
				
				int i=0;
				for (MovimientoDetalleReq detalle: movimientoReq.getDetalle()) {
					ImpresionItem impresionItem = new ImpresionItem();
					impresionItem.setNro(i+1);	
					
					Concepto concepto = conceptoDAO.get(detalle.getId_fco());
					String obs=detalle.getObs();
					if (alumno!=null && detalle.getId_fco()==16)
						impresionItem.setDescripcion( "Alumno:" + alumno.getString("alumno") +"\n "+"Nivel:"+alumno.getString("nivel")+"- Grado:"+alumno.getString("grado")+"- Secc:"+alumno.getString("secc")+"\n"+ concepto.getDes().toUpperCase()+" DE: "+obs);
						//impresionItem.setDescripcion( "Alumno:" + alumno.getString("ape_pat") + " " + alumno.getString("ape_mat") + ", " + alumno.getString("nom")  + "\n " + concepto.getDes().toUpperCase()  );	
					else if(alumno!=null && detalle.getId_fco()==24)
						impresionItem.setDescripcion( "Alumno:" + alumno.getString("alumno") +"\n "+"Nivel:"+alumno.getString("nivel")+"- Grado:"+alumno.getString("grado")+"- Secc:"+alumno.getString("secc")+"\n"+ concepto.getDes().toUpperCase()+" DE: "+obs);
					else if(alumno!=null)
							impresionItem.setDescripcion( "Alumno:" + alumno.getString("alumno") +"\n "+"Nivel:"+alumno.getString("nivel")+"- Grado:"+alumno.getString("grado")+"- Secc:"+alumno.getString("secc")+"\n"+ concepto.getDes().toUpperCase());	
					else
						impresionItem.setDescripcion(concepto.getDes());
					impresionItem.setDescuento(new BigDecimal(0));
					impresionItem.setPrecioUnit(concepto.getMonto());
					impresionItem.setCantidad(1);
					impresionItem.setPrecio(concepto.getMonto());
					impresionItem.setTipoOperacion("OPE_INA");
					impresion.getItems().add(impresionItem);
				}

					
				
			
			result.setResult( impresion );
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			movimientoDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			Movimiento movimiento = movimientoDAO.get(id);
			movimiento.setMovimientoDetalleReqs(movimientoDetalleDAO.listReq(id));
			
			result.setResult( movimiento );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/nro_rec/{id_suc}", method = RequestMethod.GET)
	public AjaxResponseBody getNroRec(@PathVariable Integer id_suc ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( facturacionService.getNroRecibo(id_suc));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}		


	@RequestMapping(value = "/imprimir/mensualidad/{nro_rec}/{id_alu}", method = RequestMethod.POST)
	@Transactional
	public AjaxResponseBody imprimirmensualidad( @PathVariable String nro_rec, @PathVariable Integer id_alu){
		AjaxResponseBody result = new AjaxResponseBody();
		
		try{
			
			//Movimiento movimiento = movimientoDAO.getFullByNroRec(nro_rec,new String[]{Familiar.TABLA, Usuario.TABLA, Sucursal.TABLA});
			Movimiento movimiento = movimientoDAO.getFullByNroRec(nro_rec,new String[]{Familiar.TABLA,Alumno.TABLA, Usuario.TABLA, Sucursal.TABLA, "col_persona_a", "col_persona_f"});
			
			//String nro_rec = movimiento.getNro_rec();

			logger.debug("nuevo recibo:" + nro_rec);
			//Datos de la empresa
			Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);
			
			//Grupo Familiar
			GruFamAlumno gru_faFamAlumno= gruFamAlumnoDAO.getByParams(new Param("id_alu",id_alu));
			GruFam gruFam= gruFamDAO.get(gru_faFamAlumno.getId_gpf());
			
			//INICIO - FACTURA ELECTRONICA
			
			Impresion impresion = new Impresion();
			ImpresionCabecera impresionCabecera = new ImpresionCabecera();
			impresionCabecera.setNombreBoleta("BOLETA ELECTR�NICA");
			impresionCabecera.setNro(nro_rec);
			impresionCabecera.setDia(FechaUtil.toString(movimiento.getFec(), "dd/MM/yyyy"));
			impresionCabecera.setComercio(empresa.getString("giro_negocio"));
			impresionCabecera.setHora(FechaUtil.toString(movimiento.getFec_ins(), "hh:mm a"));
			impresionCabecera.setUsuario((movimiento.getUsuario().getTrabajador().getNom() + " " + movimiento.getUsuario().getTrabajador().getApe_pat()).toUpperCase());
			impresionCabecera.setTelefono(movimiento.getSucursal().getTel());
			impresionCabecera.setLocal(movimiento.getSucursal().getDir());
			impresion.setCabecera(impresionCabecera);
			
			
			ImpresionCliente impresionCliente = new ImpresionCliente(); 
			//impresionCliente.setDireccion( movimiento.getFamiliar().getDir()==null?"": movimiento.getFamiliar().getDir());
			impresionCliente.setDireccion( gruFam.getDireccion()==null?"": gruFam.getDireccion());
			//impresionCliente.setNombre(movimiento.getFamiliar().getNom() + " " + movimiento.getFamiliar().getApe_pat() + " " + movimiento.getFamiliar().getApe_pat());
			impresionCliente.setNombre(movimiento.getPersona_fam().getApe_pat() + " " + movimiento.getPersona_fam().getApe_mat() + ", " + movimiento.getPersona_fam().getNom());
			impresionCliente.setTip_doc(movimiento.getPersona_fam().getTipoDocumento().getNom());
			impresionCliente.setNro_doc(movimiento.getPersona_fam().getNro_doc());
			impresion.setCliente(impresionCliente);	
				
			//FIN - FACTURA ELECTRONICA
			
			//List<MovimientoDetalle> items = movimientoDetalleDAO.listByParams(new Param("id_fmo", movimiento.getId()), new String[]{"id"});
			
			//PAGOS DE MENSUALIDAD --> DEBE SER REEMPLAZADO POR PAGO 
			
			Param param = new Param();
			param.put("nro_rec", nro_rec);
			List<AcademicoPago> mensalidades = academicoPagoDAO.listByParams(param, new String[]{"mens"});
			
			int i=0;
			for (AcademicoPago academicoPago : mensalidades) {
				i++;
				//INICIO - FACTURA ELECTRONICA
				Row datosAlumno = matriculaDAO.getDatosAlumno(academicoPago.getId_mat());
				BigDecimal descuentoHermano = academicoPago.getDesc_hermano();
				BigDecimal descuentoProntoPago= academicoPago.getDesc_pronto_pago();
				BigDecimal descuentoPersonalizado= academicoPago.getDesc_personalizado();
				BigDecimal descuentoPagoAdelantado= academicoPago.getDesc_pago_adelantado();
				
				BigDecimal descuentoItem = descuentoHermano.add(descuentoProntoPago).add(descuentoPersonalizado).add(descuentoPagoAdelantado);
				ImpresionItem impresionItem = new ImpresionItem();
				impresionItem.setNro(i+1);
				String descripcion;
				
				if (descuentoHermano.compareTo(new BigDecimal(0))>0){
					descripcion = "Descuento por hermano";
					impresionItem.getDescuentos().add(new ImpresionDcto(descripcion.toUpperCase(), descuentoHermano ));
				}
				if (descuentoProntoPago.compareTo(new BigDecimal(0))>0){
					descripcion = "Descuento pronto pago";
					impresionItem.getDescuentos().add(new ImpresionDcto(descripcion.toUpperCase(),descuentoProntoPago ));
				}
				if (descuentoPersonalizado.compareTo(new BigDecimal(0))>0){
					descripcion = "Descuento pronto pago ";
					impresionItem.getDescuentos().add(new ImpresionDcto(descripcion.toUpperCase(),descuentoPersonalizado));
				}
				if (descuentoPagoAdelantado.compareTo(new BigDecimal(0))>0){
					descripcion = "Descuento pago adelantado";
					impresionItem.getDescuentos().add(new ImpresionDcto(descripcion.toUpperCase(),descuentoPagoAdelantado));
				}
				
				descripcion = "Mensualidad de " + Constante.MES[academicoPago.getMens()-1]  + "\n" + datosAlumno.getString("alumno")+"-" + datosAlumno.getString("grado") + "-" + datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0,3);
				
				impresionItem.setDescripcion(descripcion.toUpperCase() );
				impresionItem.setDescuento(descuentoItem);
				impresionItem.setPrecioUnit(academicoPago.getMonto());
				impresionItem.setCantidad(1);
				impresionItem.setPrecio(academicoPago.getMonto());
				impresionItem.setTipoOperacion("OPE_INA");
				impresion.getItems().add(impresionItem);
				//FIN - FACTURA ELECTRONICA
				


				
			}
		

			
			result.setResult(impresion);
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		
		return result;

	}

	
	/**
	 * Genera json para la impresora
	 * @param nro_rec
	 * @return
	 */
	@RequestMapping(value = "/imprimir/caja/{nro_rec}/{id_anio}/{id_alu}", method = RequestMethod.POST)
	@Transactional
	public AjaxResponseBody imprimirCaja( @PathVariable String nro_rec, @PathVariable Integer id_anio, @PathVariable Integer id_alu){
		AjaxResponseBody result = new AjaxResponseBody();
		
		
		try{
			
			Impresion impresion = facturacionService.getImpresion(nro_rec, id_anio,id_alu);
			result.setResult(impresion);
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		
		return result;

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
			Impresion impresion = facturacionService.getImpresion(id_fmo, id_alu);
			String pdf =generatePDF(impresion); 
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

	private Impresion getImpresion(Integer id_fmo){
		Movimiento movimiento = movimientoDAO.getFullById(id_fmo,new String[]{Familiar.TABLA,Alumno.TABLA, Usuario.TABLA, Sucursal.TABLA});
		
		//String nro_rec = movimiento.getNro_rec();

		logger.debug("id movimiento:" + id_fmo);
		
		//Datos de la Empresa
		Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);
		
		//INICIO - FACTURA ELECTRONICA
		
		Impresion impresion = new Impresion();
		ImpresionCabecera impresionCabecera = new ImpresionCabecera();
		impresionCabecera.setNombreBoleta("BOLETA ELECTR�NICA");
		impresionCabecera.setNro(movimiento.getNro_rec());
		impresionCabecera.setDia(FechaUtil.toString(movimiento.getFec(), "dd/MM/yyyy"));
		impresionCabecera.setComercio(empresa.getString("giro_negocio"));
		impresionCabecera.setHora(FechaUtil.toString(movimiento.getFec_ins(), "hh:mm a"));
		impresionCabecera.setUsuario((movimiento.getUsuario().getTrabajador().getNom() + " " + movimiento.getUsuario().getTrabajador().getApe_pat()).toUpperCase());
		impresionCabecera.setTelefono(movimiento.getSucursal().getTel());
		impresionCabecera.setLocal(movimiento.getSucursal().getDir());
		impresion.setCabecera(impresionCabecera);
		
		
		ImpresionCliente impresionCliente = new ImpresionCliente(); 
		impresionCliente.setDireccion( movimiento.getFamiliar().getDir()==null?"": movimiento.getFamiliar().getDir());
		impresionCliente.setNombre(movimiento.getFamiliar().getApe_pat() + " " + movimiento.getFamiliar().getApe_mat() + ", " + movimiento.getFamiliar().getNom());
		impresionCliente.setTip_doc(movimiento.getFamiliar().getTipoDocumento().getNom());
		impresionCliente.setNro_doc(movimiento.getFamiliar().getNro_doc());
		impresion.setCliente(impresionCliente);	
			
		//FIN - FACTURA ELECTRONICA
		
		//List<MovimientoDetalle> items = movimientoDetalleDAO.listFullByParams(new Param("id_fmo", movimiento.getId()), new String[]{"fmd.id"});
		List<MovimientoDetalle> items = movimientoDetalleDAO.getListByIDfmo( movimiento.getId(), new String[]{"fac_movimiento","fac_concepto"});

		int i=0;
		for (MovimientoDetalle movimientoDetalle : items) {
			/*PAGO DEL ITEM*/
			ImpresionItem impresionItem = new ImpresionItem();
			impresionItem.setNro(i+1);

			impresionItem.setDescripcion( movimientoDetalle.getObs() );
			impresionItem.setDescuento(movimientoDetalle.getDescuento());//
			impresionItem.setPrecioUnit(movimientoDetalle.getMonto_total());
			impresionItem.setCantidad(1);
			impresionItem.setPrecio(movimientoDetalle.getMonto_total());
			impresionItem.setTipoOperacion("OPE_INA");
			impresion.getItems().add(impresionItem);
			
			
		} 
			
		
		return impresion;
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

}

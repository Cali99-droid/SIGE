package com.sige.spring.service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sige.common.enums.EnumConceptoPago;
import com.sige.common.enums.EnumTipoMovimiento;
import com.sige.core.dao.cache.CacheManager;
import com.sige.invoice.bean.Impresion;
import com.sige.invoice.bean.ImpresionDcto;
import com.sige.invoice.bean.ImpresionItem;
import com.sige.mat.dao.AcademicoPagoDAO;
import com.sige.mat.dao.AlumnoDAO;
import com.sige.mat.dao.AlumnoDescuentoDAO;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.AulaDAO;
import com.sige.mat.dao.ConfCuotaDAO;
import com.sige.mat.dao.ConfMensualidadDAO;
import com.sige.mat.dao.CorrelativoDAO;
import com.sige.mat.dao.DescHnoDAO;
import com.sige.mat.dao.FamiliarDAO;
import com.sige.mat.dao.GruFamAlumnoDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.MovimientoDAO;
import com.sige.mat.dao.MovimientoDescuentoDAO;
import com.sige.mat.dao.MovimientoDetalleDAO;
import com.sige.mat.dao.PeriodoDAO;
import com.sige.mat.dao.ReservaDAO;
import com.sige.mat.dao.SituacionMatDAO;
import com.sige.mat.dao.SolicitudDAO;
import com.sige.mat.dao.SucursalDAO;
import com.sige.rest.request.PagoMatriculaReq;
import com.tesla.colegio.model.AcademicoPago;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.ConfCuota;
import com.tesla.colegio.model.ConfMensualidad;
import com.tesla.colegio.model.Correlativo;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.GruFamAlumno;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Movimiento;
import com.tesla.colegio.model.MovimientoDescuento;
import com.tesla.colegio.model.MovimientoDetalle;
import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.ReservaCuota;
import com.tesla.colegio.model.Solicitud;
import com.tesla.colegio.model.Sucursal;
import com.tesla.colegio.model.bean.MatriculaPagos;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.NumberUtil;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
public class ImpresionService {

	@Autowired
	private CacheManager cacheManager;
public String generatePDF(Impresion impresion)throws Exception{
		
		String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();

		//String rutacARPETA =  "C:/plantillas/";

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



}

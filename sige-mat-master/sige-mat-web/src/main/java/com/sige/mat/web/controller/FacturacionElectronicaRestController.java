		package com.sige.mat.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sige.core.dao.cache.CacheManager;
import com.sige.invoice.bean.Impresion;
import com.sige.invoice.bean.ImpresionCabecera;
import com.sige.invoice.bean.ImpresionCliente;
import com.sige.invoice.bean.ImpresionItem;
import com.sige.invoice.bean.SunatResultJson;
import com.sige.mat.dao.MovimientoDAO;
import com.sige.mat.dao.MovimientoDetalleDAO;
import com.sige.spring.service.FacturacionService;
import com.tesla.colegio.model.MovimientoDetalle;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.ArchivoUtil;
import com.tesla.frmk.util.FechaUtil;


@RestController
@RequestMapping(value = "/api/facturaElectronica")
public class FacturacionElectronicaRestController {
	final static Logger logger = Logger.getLogger(FacturacionElectronicaRestController.class);

	@Autowired
	private CacheManager  cacheManager ;
	
	@Autowired
	private FacturacionService facturacionService;

	@Autowired
	private MovimientoDAO movimientoDAO;

	@Autowired
	private MovimientoDetalleDAO movimientoDetalleDAO;

	
	@RequestMapping(value = "/consulta")
	public AjaxResponseBody generarFacturaElectronica( String fec_ini, String fec_fin,String fec_ini_env, String fec_fin_env, String tip_com, String nro_serie, String enviadoSunat) {
		 
		AjaxResponseBody result = new AjaxResponseBody();
		try {
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
			List<Row> list = movimientoDAO.pagosFacturaElectronica(fec_ini_ins , fec_fin_ins, fec_ini_envio, fec_fin_envio, tip_com, nro_serie, enviadoSunat);
			result.setResult(list);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;
	}
	
	@RequestMapping(value = "/pendientesResumenDiario")
	public AjaxResponseBody pendientesResumenDiario( String fec_ini, String fec_fin, String nro_serie ) {
		 
		AjaxResponseBody result = new AjaxResponseBody();
		try {
			
			//List<Row> list = movimientoDAO.pagosFacturaElectronica(FechaUtil.toDate(fec_ini) , FechaUtil.toDate(fec_ini), nro_serie, "0");
			List<Row> list = movimientoDAO.pagosFacturaElectronica2(fec_ini , FechaUtil.toDate(fec_ini), nro_serie, "0");
			result.setResult(list);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;
	}


	
	@RequestMapping(value = "/cargarMovimientosBanco")
	public AjaxResponseBody cargarMovimientosBanco(String fec, Object usuario ) {
		 
		AjaxResponseBody result = new AjaxResponseBody();
		try {
			
			facturacionService.cargarMovimientosBanco(FechaUtil.toDate(fec));
				
			
			
		} catch (Exception e) {
			result.setException(e);
		}
		

		
		return result;
	}
	
	/**se ejecuta por unica vez para regulizar los movimientos de los pagos de bancos*/
	/*
	@RequestMapping(value = "/cargarMovimientosBancoPendienes", method = RequestMethod.POST)
	public AjaxResponseBody cargarMovimientosBancoPendienes( ) {
		 
		AjaxResponseBody result = new AjaxResponseBody();
		try {
			Usuario usuario =new Usuario();
			usuario.setId(1);
			facturacionService.cargarMovimientosBanco(null,usuario);
				
			
			
		} catch (Exception e) {
			result.setException(e);
		}
		

		
		return result;
	}*/
		
	

	
	@RequestMapping(value = "/envioResumenDiarioJOB", method = RequestMethod.POST)
	public AjaxResponseBody envioResumenDiarioJOB(String tip_com,String nro_serie,boolean flag_correo) {
	
		AjaxResponseBody result = new AjaxResponseBody();
		try {

			result.setResult(facturacionService.generarFacturaElectronica(tip_com,nro_serie,flag_correo));		
			
			
		} catch (Exception e) {
			logger.error("errorJOB",e);
			result.setException(e);
		}
		

		
		return result;
	}
	
		
	@RequestMapping(value = "/envioResumenDiario")
	public AjaxResponseBody generarFacturaElectronica(String fec_ini, String fec_envio_sunat, String nro_serie ) {
		 
		AjaxResponseBody result = new AjaxResponseBody();
		try {
			

			List<Impresion> impresiones = new ArrayList<Impresion>();
			Date fec_sunat = new Date();
			if (fec_envio_sunat !=null)
				FechaUtil.toDate(fec_envio_sunat);
			
			//List<Row> list = movimientoDAO.pagosFacturaElectronica(FechaUtil.toDate(fec_ini) , FechaUtil.toDate(fec_ini), nro_serie, "2");//PENDIENTES DE ENVIO SUNAT
			List<Row> list = movimientoDAO.pagosFacturaElectronica3(fec_ini , FechaUtil.toDate(fec_ini), nro_serie, "2");//PENDIENTES DE ENVIO SUNAT
			List<Integer> id_fmos = new ArrayList<Integer>();
			for (Row row : list) {
				id_fmos.add(row.getInteger("id"));
				Impresion impresion = new Impresion();
				ImpresionCabecera cabecera = new ImpresionCabecera();
				cabecera.setNombreBoleta("BOLETA ELECTR�NICA");
				cabecera.setNro(row.getString("nro_rec"));
				cabecera.setDia(fec_ini);
				cabecera.setComercio("ASOCIACION EDUCATIVA LUZ Y CIENCIA");
				cabecera.setHora(FechaUtil.toString(new Date(), "hh:mm a"));
				impresion.setCabecera(cabecera);
				ImpresionCliente cliente = new ImpresionCliente();
				cliente.setDireccion("");
				cliente.setNombre(row.getString("ape_pat") + " " + row.getString("ape_mat") + ", " + row.getString("nom"));
				cliente.setCorreo(row.getString("corr"));
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
			
		   logger.info(impresiones);
			SunatResultJson respuesta = facturacionService.enviarResumenRestSunat(fec_envio_sunat, impresiones);
			//Actualizar el estado de enviado asi no se haya enviado a la SUNAT
			movimientoDAO.actualizarEstadoMovimiento(id_fmos);
			logger.info(respuesta);
			//nOSA FALTA SABER QUE CODIGO DE RESPUESTA ES:
			/*- ERROR:?
			- RESPUESTA PENDIENTE:?
			- RESPUESTA OK :0
			- ERROR DE ENVIADO :99
			*/
			if (respuesta!=null && "0".equals(respuesta.getCode().trim()) ){
				
				String archivo =  respuesta.getArchivo();//EL ARCHIVO VIENE CON XML CONFIRMATORIO
				String respuesta_sunat =  respuesta.getRespuesta_sunat();//ticket, este puede ser consultado mas adelante.
				String id_eiv =  respuesta.getId_eiv();
				
				movimientoDAO.actualizarCodRes(id_fmos, archivo, id_eiv, respuesta_sunat,fec_sunat);
			}else {
				String respuesta_sunat =  respuesta.getRespuesta_sunat();//ticket, este puede ser consultado mas adelante.
				String id_eiv =  respuesta.getId_eiv();

				movimientoDAO.actualizarCodRes(id_fmos, null, id_eiv, respuesta_sunat,fec_sunat);
			}
			
			if("99".equals(respuesta.getCode()))
				throw new Exception("Facturas ya fueron emitiadas anteriormente");
			
			result.setResult(respuesta);
			
		} catch (Exception e) {
			result.setCode("500");
			result.setMsg("Error al enviar la factura electrónica:" + e.getMessage());
			result.setException(e);
		}
		

		
		return result;
	}
	
	@RequestMapping(value = "/respuestaTicket")
	public AjaxResponseBody respuestaTicket(String id_res,String ticket,String fec_ini, String id_eiv, String fec_envio_sunat, String tip_com, String nro_serie)throws Exception{
		 
		AjaxResponseBody result = facturacionService.getRespuestaTicket(id_res, ticket);
		String code = result.getResult().toString();
		
		logger.debug("code:" + code);
		
		if (code.equals("0")){
			
			List<Row> list = movimientoDAO.pagosFacturaElectronica(FechaUtil.toDate(fec_ini) , FechaUtil.toDate(fec_ini),null,null,tip_com, nro_serie, "2");//PENDIENTES DE ENVIO SUNAT
			
			List<Integer> id_fmos = new ArrayList<Integer>();
			for (Row row : list) {
				id_fmos.add(row.getInteger("id"));
			}	
			movimientoDAO.actualizarCodRes(id_fmos, ticket, id_eiv, code,new Date());

		}
		return result;

		 
	}
	
	
	
	@RequestMapping(value = "/reporte/xls")
	@ResponseBody
	public void descargarExcel(HttpServletRequest request,HttpServletResponse response,String fec_ini, String fec_fin, String tip_com, String nro_serie, String enviadoSunat)  throws Exception {


		List<Row> list = movimientoDAO.pagosFacturaElectronica(FechaUtil.toDate(fec_ini) , FechaUtil.toDate(fec_fin),null,null, tip_com, nro_serie, enviadoSunat);

	
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
		Cell cell = sheet.getRow(3).getCell(2);
		cell.setCellValue(fec_ini);

		//fec_fin
		cell = sheet.getRow(3).getCell(5);
		cell.setCellValue(fec_fin);

		//fec_ini
		cell = sheet.getRow(3).getCell(9);
		cell.setCellValue(nro_serie);

		//fec_ini
		cell = sheet.getRow(3).getCell(13);
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
        	cell2.setCellValue(list.get(i).getString("nro_rec"));
			cell2.setCellStyle(styleCell);

			Cell cell3 = sheet.getRow(filaInicio+i).createCell(columna++);
        	cell3.setCellValue(list.get(i).getString("fec"));
			cell3.setCellStyle(styleCell);

			cell3 = sheet.getRow(filaInicio+i).createCell(columna++);
        	cell3.setCellValue(list.get(i).getString("fec_sunat"));
			cell3.setCellStyle(styleCell);
			
			cell3 = sheet.getRow(filaInicio+i).createCell(columna++);
        	cell3.setCellValue(list.get(i).getString("obs"));
			cell3.setCellStyle(styleCell);

			//buscar nivel
			
			
			cell3 = sheet.getRow(filaInicio+i).createCell(columna++);
        	cell3.setCellValue("");
			cell3.setCellStyle(styleCell);

			cell3 = sheet.getRow(filaInicio+i).createCell(columna++);
        	cell3.setCellValue(list.get(i).getString("nro_doc"));
			cell3.setCellStyle(styleCell);

			cell3 = sheet.getRow(filaInicio+i).createCell(columna++);
        	cell3.setCellValue(list.get(i).getString("cliente"));
			cell3.setCellStyle(styleCell);

			cell3 = sheet.getRow(filaInicio+i).createCell(columna++);
        	cell3.setCellValue(list.get(i).getString("monto"));
			cell3.setCellStyle(styleCell);

			cell3 = sheet.getRow(filaInicio+i).createCell(columna++);
        	cell3.setCellValue(list.get(i).getString("descuento"));
			cell3.setCellStyle(styleCell);

			cell3 = sheet.getRow(filaInicio+i).createCell(columna++);
        	cell3.setCellValue(list.get(i).getString("monto_total"));
			cell3.setCellStyle(styleCell);

			String cod_res  = list.get(i).getString("cod_res");
			
			cell3 = sheet.getRow(filaInicio+i).createCell(columna++);
			if (cod_res==null)
				cell3.setCellValue("PENDIENTE DE ENVIO");
			else
				cell3.setCellValue("ENVIADO A SUNAT");
			cell3.setCellStyle(styleCell);

			cell3 = sheet.getRow(filaInicio+i).createCell(columna++);
        	cell3.setCellValue(cod_res);
			cell3.setCellStyle(styleCell);

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
}

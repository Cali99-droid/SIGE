package com.tesla.frmk.util;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.sige.invoice.bean.Impresion;
import com.sige.invoice.bean.ImpresionItem;
import com.tesla.colegio.model.bean.PromedioBean;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class IReportUtil {

	public static void  generatePDF(Impresion impresion)throws Exception{
		
		//InputStream inputStream = new FileInputStream("D:\\proyectos\\colegio\\boleta.jrxml");
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		
		
		// boleta
		parameters.put("total_venta", "440.00");
		parameters.put("boleta_nro", "B0001-00001");
		parameters.put("boleta_fecha_emision", "10/10/2018");
		parameters.put("boleta_hora", "12:00 am");
		parameters.put("boleta_comercio", "XXXXXXX");
		parameters.put("boleta_local", "XXXXX");
		parameters.put("boleta_usuario", "XXXXXXXXXXX");
		parameters.put("boleta_telefono", "(043) xxxxxx");
		parameters.put("boleta_codigo", "hola amigo");

		//cliente
		parameters.put("cliente_nombre", "fulano de tal");
		parameters.put("cliente_direccion", "av los precursosre smp");
		parameters.put("cliente_nro_doc", "21323333");
		parameters.put("cliente_tip_doc", "DNI");
		
		//LISTA DE ITEMS
		List<ImpresionItem> list = new ArrayList<ImpresionItem>();
		
		BigDecimal operacionGravada = new BigDecimal(0);
		BigDecimal operacionGratuita = new BigDecimal(0);
		BigDecimal operacionInafecta = new BigDecimal(0);
		BigDecimal descuento = new BigDecimal(0);
		BigDecimal igv = new BigDecimal(0);
		BigDecimal total = new BigDecimal(0);
		
		ImpresionItem itemBean = new ImpresionItem();
		itemBean.setNro(1);
		itemBean.setDescripcion("MENSUALIDAD DE SETIEMBRE-JUANA PEREZ");
		itemBean.setCantidad(1);
		itemBean.setDescuento(new BigDecimal(0));
		itemBean.setPrecioUnit(new BigDecimal(200));
		itemBean.setPrecio(itemBean.getPrecioUnit().multiply(new BigDecimal(itemBean.getCantidad())));
		itemBean.setTipoOperacion("OPE_INA");//OPERACION INAFECTA
		list.add(itemBean);
		
		if (itemBean.getTipoOperacion().equals("OPE_INA")){
			operacionInafecta = operacionInafecta.add(itemBean.getPrecio());
		}else if (itemBean.getTipoOperacion().equals("OPE_GRAT"))
			operacionGratuita = operacionGratuita.add(itemBean.getPrecio());
		else if (itemBean.getTipoOperacion().equals("OPE_GRAV")){
			operacionGravada = operacionGravada.add(itemBean.getPrecio());
			//igv = igv.add(augend)
		}
		total = total.add(itemBean.getPrecio());
		
		itemBean = new ImpresionItem();
		itemBean.setNro(2);
		itemBean.setDescripcion("MENSUALIDAD DE SETIEMBRE-MIGUEL PEREZ");
		itemBean.setCantidad(1);
		itemBean.setDescuento(new BigDecimal(0));
		itemBean.setPrecioUnit(new BigDecimal(210));
		itemBean.setPrecio(itemBean.getPrecioUnit().multiply(new BigDecimal(itemBean.getCantidad())));
		itemBean.setTipoOperacion("OPE_INA");//OPERACION INAFECTA
		list.add(itemBean);

		if (itemBean.getTipoOperacion().equals("OPE_INA")){
			operacionInafecta = operacionInafecta.add(itemBean.getPrecio());
		}else if (itemBean.getTipoOperacion().equals("OPE_GRAT"))
			operacionGratuita = operacionGratuita.add(itemBean.getPrecio());
		else if (itemBean.getTipoOperacion().equals("OPE_GRAV")){
			operacionGravada = operacionGravada.add(itemBean.getPrecio());
			//igv = igv.add(augend)
		}
		total = total.add(itemBean.getPrecio());
		
		//resumen
		parameters.put("resumen_gravadas", NumberUtil.toString(operacionGravada) );
		parameters.put("resumen_gratuitas", NumberUtil.toString(operacionGratuita) );
		parameters.put("resumen_inafectas", NumberUtil.toString(operacionInafecta));
		parameters.put("resumen_descuento", NumberUtil.toString(descuento));
		parameters.put("resumen_igv", NumberUtil.toString(igv));
		parameters.put("resumen_monto_texto", NumberUtil.toTexto(total).toUpperCase() + " SOLES");
		parameters.put("resumen_total_venta", NumberUtil.toString(total));
		
			
		InputStream resource = new FileInputStream("C:\\Users\\cym\\Documents\\personal\\pdf\\boleta.jasper");
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(resource);
		resource.close();
		
		
		
		
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list);

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);
		JasperExportManager.exportReportToPdfFile(jasperPrint, "C:/impresora/boleta" + (new Date()).toString().replaceAll(" ","").replaceAll(":", "") +".pdf");
		
	}
	



		
	public static void main3(String[] args) throws Exception {

		InputStream inputStream = new FileInputStream("D:/proyectos/colegio/boleta.jrxml");
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		
		//DATOS DE LA EMPRESA
		
		// titulo
		parameters.put("total_venta", "440.00");
		parameters.put("codigo_barras", "hola amigo");
		
		// grilla
		JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
		JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

		List<ImpresionItem> list = new ArrayList<ImpresionItem>();
		/*ItemBean itemBean = new ItemBean();
		itemBean.setNro("1");
		itemBean.setDescripcion("MENSUALIDAD DE SETIEMBRE-JUANA PEREZ");
		itemBean.setCantidad("1.00");
		itemBean.setDescuento("0.00");
		itemBean.setPrecioUnit("200.00");
		itemBean.setPrecio("200.00");
		list.add(itemBean);

		itemBean = new ItemBean();
		itemBean.setNro("2");
		itemBean.setDescripcion("MENSUALIDAD DE SETIEMBRE-MIGUEL PEREZ");
		itemBean.setCantidad("1.00");
		itemBean.setDescuento("0.00");
		itemBean.setPrecioUnit("200.00");
		itemBean.setPrecio("200.00");
		list.add(itemBean);
*/
		
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list);

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);
		JasperExportManager.exportReportToPdfFile(jasperPrint, "D:/boleta2.pdf");

	}
	

	public static void main2(String[] args) throws Exception {

		InputStream inputStream = new FileInputStream("D:/proyectos/colegio/jasper/report1.jrxml");
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		// titulo
		parameters.put("P_TITULO", "LIBRETA DE NOTAS 2018");
		parameters.put("P_ALUMNO", "LUIS ALBERTO SANCHEZ CARRION DE LA FUENTE");
		parameters.put("P_NIVEL", "PRIMARIA");
		parameters.put("P_GRADO", "TERCERO");
		parameters.put("P_SECCION", "A");

		// grilla
		JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
		JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

		List<PromedioBean> list = new ArrayList<PromedioBean>();
		PromedioBean promedioBean = new PromedioBean();
		promedioBean.setCurso("asdasd asd ads");
		promedioBean.setPeriodo1("11");
		promedioBean.setPeriodo2("11");
		promedioBean.setPeriodo3("14");
		promedioBean.setPeriodo4("16");
		list.add(promedioBean);

		promedioBean = new PromedioBean();
		promedioBean.setCurso("juan asd ads");
		promedioBean.setPeriodo1("15");
		promedioBean.setPeriodo2("13");
		promedioBean.setPeriodo3("18");
		promedioBean.setPeriodo4("20");
		list.add(promedioBean);

		promedioBean = new PromedioBean();
		promedioBean.setCurso("michgael asd ads");
		promedioBean.setPeriodo1("10");
		promedioBean.setPeriodo2("15");
		promedioBean.setPeriodo3("12");
		promedioBean.setPeriodo4("11");
		list.add(promedioBean);

		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list);

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);
		JasperExportManager.exportReportToPdfFile(jasperPrint, "D:/pdf3.pdf");

	}
}
package com.tesla.frmk.util;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.sige.mat.dao.AcademicoPagoDAO;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.spring.service.FacturacionService;
import com.tesla.colegio.model.AcademicoPago;
import com.tesla.colegio.model.Anio;
import com.tesla.frmk.sql.Param;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

public class ExcelXlsUtil {
	final static Logger logger = Logger.getLogger(ExcelXlsUtil.class);
	@Autowired
	private AcademicoPagoDAO academicoPagoDAO;

	@Autowired
	private MatriculaDAO matriculaDAO;
	
	@Autowired
	private AnioDAO anioDAO;

	@Autowired
	private FacturacionService facturacionService;

	public static void main(String[] args) throws IOException {
		//ExcelXlsUtil excel = new ExcelXlsUtil();
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		
		Map<String,Object> map  = new HashMap<String,Object>();
		map.put("nro_rec", "asda");
		map.put("des", "pago de x");
		map.put("monto", new BigDecimal(10.5));
		
		list.add(map) ;
		
		 map  = new HashMap<String,Object>();
		map.put("nro_rec", "101010");
		map.put("des", "pago de Y");
		map.put("monto", new BigDecimal(50.5));
		
		list.add(map) ;
		
		
	//	String nuevoExcel = excel.generaExcelReporteCaja("D:\\", "plantilla_reporte_caja.xls", "22/02/2018","SHUAMAN","PRINCIPAL",list);

		//logger.debug(nuevoExcel);

	}

	public String generaExcelReporteCaja(String carpeta, String archivo, Date fec_fin,String usuario, String local,List<Map<String,Object>> list) {

		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd_hhmmss");

		String nuevoArchivo = null;
		int filaInicial = 7;
		int columnaInicial = 1;
		try {
			nuevoArchivo = dt1.format(new Date());

			nuevoArchivo = carpeta + archivo.replace("plantilla_reporte_caja", "Reporte_Caja" + nuevoArchivo);

			ArchivoUtil.copyFileUsingStream(new File(carpeta + archivo), new File(nuevoArchivo));

			FileInputStream inputStream = new FileInputStream(new File(nuevoArchivo));
			Workbook workbook = WorkbookFactory.create(inputStream);

			Sheet sheet = workbook.getSheetAt(0);

			// TITULO
			sheet.getRow(1).getCell(1).setCellValue("INGRESO DIARIO DEL " + FechaUtil.toString(fec_fin));
			// USUARIO
			sheet.getRow(3).getCell(4).setCellValue(usuario);
			// LOCAL
			sheet.getRow(4).getCell(4).setCellValue(local);

			sheet.shiftRows(8, sheet.getLastRowNum(), 1);
			
			int fila =1;
			Double total = new Double(0);
			DataFormat format = workbook.createDataFormat();
			Font font = workbook.createFont();
		      font.setColor(HSSFColor.BLUE.index);

			for (Map<String,Object> map : list) {

				Row row = sheet.createRow(filaInicial++);

				Double monto = Double.parseDouble( map.get("monto_total").toString());
				String tipoDes = "";
			
				if ( map.get("tipo").toString().equals("I")){
					total = total + monto;
					tipoDes = "INGRESO";
				}else{
					total = total + monto;
					tipoDes = "SALIDA";
				}
				row.createCell(columnaInicial + 0).setCellValue(fila++);
				row.createCell(columnaInicial + 1).setCellValue(map.get("nro_rec").toString());
				row.createCell(columnaInicial + 2).setCellValue(map.get("fecha").toString());
				row.createCell(columnaInicial + 3).setCellValue(map.get("concepto").toString());
				row.createCell(columnaInicial + 4).setCellValue(map.get("obs").toString());
				row.createCell(columnaInicial + 5).setCellValue((map.get("nivel")==null)? "":map.get("nivel").toString());
				row.createCell(columnaInicial + 6).setCellValue((map.get("grado")==null)? "":map.get("grado").toString());
				row.createCell(columnaInicial + 7).setCellValue((map.get("secc")==null)? "":map.get("secc").toString());
				//row.createCell(columnaInicial + 7).setCellValue(tipoDes);
				CellStyle style = workbook.createCellStyle();
				style.setDataFormat(format.getFormat("#,##0.00"));
		        style.setFont(font);

				Cell cell = row.createCell(columnaInicial + 8);
				cell.setCellValue(monto);
				cell.setCellStyle(style);

			     
			}
			
			Row row = sheet.createRow(++filaInicial);

			
			row.createCell(columnaInicial + 6).setCellValue("TOTAL");
			Cell cell = row.createCell(columnaInicial + 7); 
			cell.setCellValue(total);
		    
			CellStyle style = workbook.createCellStyle();
			style.setDataFormat(format.getFormat("#,##0.00"));
			cell.setCellStyle(style);
			
			inputStream.close();

			FileOutputStream outputStream = new FileOutputStream(nuevoArchivo);
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return nuevoArchivo;
	}
	

	public String generaExcelReporteBanco(String carpeta, String archivo, Date fecha,String usuario, String local,List<Map<String,Object>> list) {

		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd_hhmmss");

		String nuevoArchivo = null;
		int filaInicial = 7;
		int columnaInicial = 1;
		try {
			nuevoArchivo = dt1.format(new Date());

			nuevoArchivo = carpeta  + "tmp/"+ archivo.replace("plantilla_reporte_banco", "Reporte_Banco" + nuevoArchivo);

			ArchivoUtil.copyFileUsingStream(new File(carpeta + archivo), new File(nuevoArchivo));

			FileInputStream inputStream = new FileInputStream(new File(nuevoArchivo));
			Workbook workbook = WorkbookFactory.create(inputStream);

			Sheet sheet = workbook.getSheetAt(0);

			// TITULO
			sheet.getRow(1).getCell(1).setCellValue("PAGOS EN BANCO DIARIO DEL " + FechaUtil.toString(fecha));
			// USUARIO
			sheet.getRow(3).getCell(3).setCellValue(usuario);
			// LOCAL
			sheet.getRow(4).getCell(3).setCellValue(local);

			sheet.shiftRows(8, sheet.getLastRowNum(), 1);
			
			int fila =1;
			Double total = new Double(0);
			DataFormat format = workbook.createDataFormat();
			Font font = workbook.createFont();
		      font.setColor(HSSFColor.BLUE.index);

			for (Map<String,Object> map : list) {

				Row row = sheet.createRow(filaInicial++);

				Double monto = Double.parseDouble( map.get("monto_total").toString());
				String tipoDes = "";
			
				if ( map.get("tipo").toString().equals("I")){
					total = total + monto;
					tipoDes = "INGRESO";
				}else{
					total = total - monto;
					tipoDes = "SALIDA";
				}
				row.createCell(columnaInicial + 0).setCellValue(fila++);
				row.createCell(columnaInicial + 1).setCellValue(map.get("nro_rec").toString());
				row.createCell(columnaInicial + 2).setCellValue(map.get("concepto").toString());
				row.createCell(columnaInicial + 3).setCellValue(map.get("alumno").toString());
				row.createCell(columnaInicial + 4).setCellValue((map.get("nivel")==null)? "":map.get("nivel").toString());
				row.createCell(columnaInicial + 5).setCellValue((map.get("grado")==null)? "":map.get("grado").toString());
				row.createCell(columnaInicial + 6).setCellValue((map.get("secc")==null)? "":map.get("secc").toString());
				//row.createCell(columnaInicial + 7).setCellValue(tipoDes);
				CellStyle style = workbook.createCellStyle();
				style.setDataFormat(format.getFormat("#,##0.00"));
		        style.setFont(font);

				Cell cell = row.createCell(columnaInicial + 7);
				cell.setCellValue(monto);
				cell.setCellStyle(style);

			     
			}
			
			Row row = sheet.createRow(++filaInicial);

			
			row.createCell(columnaInicial + 6).setCellValue("TOTAL");
			Cell cell = row.createCell(columnaInicial + 7); 
			cell.setCellValue(total);
		    
			CellStyle style = workbook.createCellStyle();
			style.setDataFormat(format.getFormat("#,##0.00"));
			cell.setCellStyle(style);
			
			inputStream.close();

			FileOutputStream outputStream = new FileOutputStream(nuevoArchivo);
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return nuevoArchivo;
	}	
	public String generaExcelReportePagos(String carpeta, String archivo,String nivel, String grado, String aula, String sucursal,List<Map<String,Object>> list) {

		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd_hhmmss");
		SimpleDateFormat dt2 = new SimpleDateFormat("yyyy");
		String anio=dt2.format(new Date());
		String nuevoArchivo = null;
		int filaInicial = 5;
		int columnaInicial = 1;
		try {
			nuevoArchivo = dt1.format(new Date());

			nuevoArchivo = carpeta + archivo.replace("plantilla_reporte_pensiones", "Reporte_Pagos" + nuevoArchivo);

			ArchivoUtil.copyFileUsingStream(new File(carpeta + archivo), new File(nuevoArchivo));

			FileInputStream inputStream = new FileInputStream(new File(nuevoArchivo));
			Workbook workbook = WorkbookFactory.create(inputStream);

			Sheet sheet = workbook.getSheetAt(0);
			
			// TITULO
			sheet.getRow(0).getCell(0).setCellValue("CONTROL DE PAGOS DE PENSIONES - " + anio);
			// LOCAL
			sheet.getRow(2).getCell(0).setCellValue("LOCAL: "+sucursal);
			// NIVEL
			sheet.getRow(2).getCell(4).setCellValue("NIVEL: "+nivel);
			// GRADO
			sheet.getRow(2).getCell(8).setCellValue("GRADO: "+grado);
			// GRADO
			sheet.getRow(2).getCell(11).setCellValue("SECCIï¿½N: "+aula);
			
		    //Para el salto de linea en la misma celda
			CellStyle cs = workbook.createCellStyle();
		    cs.setWrapText(true);
		    
		    //Font font = workbook.createFont();
	        //font.setColor(HSSFColor.BLUE.index);
	     

			for (Map<String,Object> map : list) {
			sheet.getRow(filaInicial).getCell(columnaInicial + 0).setCellStyle(cs);
			
			sheet.getRow(filaInicial).getCell(columnaInicial + 0).setCellValue(map.get("alumno").toString());
			sheet.getRow(filaInicial).getCell(columnaInicial + 1).setCellValue(map.get("mat_nro_rec").toString()+"\n"+map.get("mat_monto").toString());
			sheet.getRow(filaInicial).getCell(columnaInicial + 2).setCellValue(map.get("ing_nro_rec").toString()+"\n"+map.get("ing_monto").toString());
			//logger.debug(map.get("marzo_monto"));
			//if(!map.get("marzo_monto").toString().equals(" ") && map.get("marzo_banco").toString().equals(" ")){
			sheet.getRow(filaInicial).getCell(columnaInicial + 3).setCellValue(map.get("marzo_nro_rec").toString()+"\n"+map.get("marzo_monto").toString());
			//} //else if(map.get("marzo_monto")!=" " && map.get("marzo_banco").toString()!=" "){
				///cs.setFont(font);
				//sheet.getRow(filaInicial).getCell(columnaInicial + 3).setCellValue(map.get("marzo_nro_rec").toString()+"\n S/."+map.get("marzo_monto").toString());
			//}			
			sheet.getRow(filaInicial).getCell(columnaInicial + 4).setCellValue(map.get("abril_nro_rec").toString()+"\n"+map.get("abril_monto").toString());
			sheet.getRow(filaInicial).getCell(columnaInicial + 5).setCellValue(map.get("mayo_nro_rec").toString()+"\n"+map.get("mayo_monto").toString());
			sheet.getRow(filaInicial).getCell(columnaInicial + 6).setCellValue(map.get("junio_nro_rec").toString()+"\n"+map.get("junio_monto").toString());
			sheet.getRow(filaInicial).getCell(columnaInicial + 7).setCellValue(map.get("julio_nro_rec").toString()+"\n"+map.get("julio_monto").toString());
			sheet.getRow(filaInicial).getCell(columnaInicial + 8).setCellValue(map.get("agosto_nro_rec").toString()+"\n"+map.get("agosto_monto").toString());
			sheet.getRow(filaInicial).getCell(columnaInicial + 9).setCellValue(map.get("setiembre_nro_rec").toString()+"\n"+map.get("setiembre_monto").toString());
			sheet.getRow(filaInicial).getCell(columnaInicial + 10).setCellValue(map.get("octubre_nro_rec").toString()+"\n"+map.get("octubre_monto").toString());
			sheet.getRow(filaInicial).getCell(columnaInicial + 11).setCellValue(map.get("noviembre_nro_rec").toString()+"\n"+map.get("noviembre_monto").toString());
			sheet.getRow(filaInicial++).getCell(columnaInicial + 12).setCellValue(map.get("diciembre_nro_rec").toString()+"\n"+map.get("diciembre_monto").toString());
			     
			}
			
			
		    
			inputStream.close();

			FileOutputStream outputStream = new FileOutputStream(nuevoArchivo);
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return nuevoArchivo;
	}
	
	public String generaExcelReporteDeudas(String carpeta, String archivo,String nivel, String grado, String aula, String mes, String situacion,List<Map<String,Object>> list) {

		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd_hhmmss");
		SimpleDateFormat dt2 = new SimpleDateFormat("yyyy");
		String anio=dt2.format(new Date());
		String nuevoArchivo = null;
		int filaInicial = 7;
		int columnaInicial = 0;
		try {
			nuevoArchivo = dt1.format(new Date());

			nuevoArchivo = carpeta + archivo.replace("plantilla_reporte_deudores", "Reporte_Deudores" + nuevoArchivo);

			ArchivoUtil.copyFileUsingStream(new File(carpeta + archivo), new File(nuevoArchivo));

			FileInputStream inputStream = new FileInputStream(new File(nuevoArchivo));
			Workbook workbook = WorkbookFactory.create(inputStream);

			Sheet sheet = workbook.getSheetAt(0);
			
			// TITULO
			sheet.getRow(0).getCell(1).setCellValue("REPORTE DE DEUDAS AÑO - " + anio);
			// NIVEL, GRADO, AULA
			sheet.getRow(2).getCell(1).setCellValue("NIVEL: "+nivel+"  GRADO: "+grado+" SECCIÓN: "+aula);
			// MES  SITUACION
			sheet.getRow(4).getCell(1).setCellValue("HASTA EL MES DE : "+mes+" SITUACIÓN: "+situacion);
			Date fec=new Date();
			
			sheet.getRow(0).getCell(19).setCellValue("Fecha: " + FechaUtil.toString(fec, "dd-MM-yyyy"));
			sheet.getRow(2).getCell(19).setCellValue("Hora: " + FechaUtil.toString(fec, "HH:mm:ss"));
		    //Para el salto de linea en la misma celda
			CellStyle cs = workbook.createCellStyle();
		    cs.setWrapText(true);
		    
		    DataFormat format = workbook.createDataFormat();
			Font font = workbook.createFont();
		      font.setColor(HSSFColor.BLACK.index);
		      font.setBold(true);
		    //Font font = workbook.createFont();
	        //font.setColor(HSSFColor.BLUE.index);
	     
			Integer i=1;
			Double total_marzo = new Double(0);
			Double total_abril = new Double(0);
			Double total_mayo = new Double(0);
			Double total_junio = new Double(0);
			Double total_julio = new Double(0);
			Double total_agosto = new Double(0);
			Double total_setiembre = new Double(0);
			Double total_octubre = new Double(0);
			Double total_noviembre = new Double(0);
			Double total_diciembre = new Double(0);
			Double total_anual = new Double(0);
			for (Map<String,Object> map : list) {
				if(map.get("alumno")!=null) {
					System.out.println(map.get("alumno"));
					//sheet.getRow(filaInicial).getCell(columnaInicial + 0).setCellStyle(cs);
					Row row = sheet.createRow(filaInicial++);
					row.createCell(columnaInicial + 0).setCellValue(i);
					row.createCell(columnaInicial + 1).setCellValue(map.get("alumno").toString());
					row.createCell(columnaInicial + 2).setCellValue(map.get("familiar").toString());
					row.createCell(columnaInicial + 3).setCellValue((map.get("celular")==null)? "":map.get("celular").toString());
					row.createCell(columnaInicial + 4).setCellValue((map.get("direccion")==null)? "":map.get("direccion").toString());
					row.createCell(columnaInicial + 5).setCellValue((map.get("correo")==null)? "":map.get("correo").toString());
					row.createCell(columnaInicial + 6).setCellValue(map.get("aula").toString());
					row.createCell(columnaInicial + 7).setCellValue("Pensión");
					row.createCell(columnaInicial + 10).setCellValue((map.get("marzo_monto")==null)? "":map.get("marzo_monto").toString());
					row.createCell(columnaInicial + 11).setCellValue((map.get("abril_monto")==null)? "":map.get("abril_monto").toString());
					row.createCell(columnaInicial + 12).setCellValue((map.get("mayo_monto")==null)? "":map.get("mayo_monto").toString());
					row.createCell(columnaInicial + 13).setCellValue((map.get("junio_monto")==null)? "":map.get("junio_monto").toString());
					row.createCell(columnaInicial + 14).setCellValue((map.get("julio_monto")==null)? "":map.get("julio_monto").toString());
					row.createCell(columnaInicial + 15).setCellValue((map.get("agosto_monto")==null)? "":map.get("agosto_monto").toString());
					row.createCell(columnaInicial + 16).setCellValue((map.get("setiembre_monto")==null)? "":map.get("setiembre_monto").toString());
					row.createCell(columnaInicial + 17).setCellValue((map.get("octubre_monto")==null)? "":map.get("octubre_monto").toString());
					row.createCell(columnaInicial + 18).setCellValue((map.get("noviembre_monto")==null)? "":map.get("noviembre_monto").toString());
					row.createCell(columnaInicial + 19).setCellValue((map.get("diciembre_monto")==null)? "":map.get("diciembre_monto").toString());
					//logger.debug(map.get("marzo_monto"));
					//if(!map.get("marzo_monto").toString().equals(" ") && map.get("marzo_banco").toString().equals(" ")){
					Double total = new Double(0);
					Double monto_marzo = Double.parseDouble((map.get("marzo_monto")==null || map.get("marzo_monto")=="")? "0.00":map.get("marzo_monto").toString());
					Double monto_abril = Double.parseDouble((map.get("abril_monto")==null || map.get("abril_monto")=="")? "0.00":map.get("abril_monto").toString());
					Double monto_mayo = Double.parseDouble((map.get("mayo_monto")==null || map.get("mayo_monto")=="")? "0.00":map.get("mayo_monto").toString());
					Double monto_junio = Double.parseDouble((map.get("junio_monto")==null || map.get("junio_monto")=="")? "0.00":map.get("junio_monto").toString());
					Double monto_julio = Double.parseDouble((map.get("julio_monto")==null || map.get("julio_monto")=="")? "0.00":map.get("julio_monto").toString());
					Double monto_agosto = Double.parseDouble((map.get("agosto_monto")==null || map.get("agosto_monto")=="")? "0.00":map.get("agosto_monto").toString());
					Double monto_setiembre = Double.parseDouble((map.get("setiembre_monto")==null || map.get("setiembre_monto")=="")? "0.00":map.get("setiembre_monto").toString());
					Double monto_octubre = Double.parseDouble((map.get("octubre_monto")==null || map.get("octubre_monto")=="")? "0.00":map.get("octubre_monto").toString());
					Double monto_noviembre = Double.parseDouble((map.get("noviembre_monto")==null || map.get("noviembre_monto")=="")? "0.00":map.get("noviembre_monto").toString());
					Double monto_diciembre = Double.parseDouble((map.get("diciembre_monto")==null || map.get("diciembre_monto")=="")? "0.00":map.get("diciembre_monto").toString());
					CellStyle style = workbook.createCellStyle();
					style.setDataFormat(format.getFormat("#,##0.00"));
			        style.setFont(font);
						total = total+monto_marzo+monto_abril+monto_mayo+monto_junio+monto_julio+monto_agosto+monto_setiembre+monto_octubre+monto_noviembre+monto_diciembre;
						Cell cell = row.createCell(columnaInicial + 20);
						cell.setCellStyle(style);
						cell.setCellValue(total);
						
						//style.setFont(font);
						row.createCell(columnaInicial + 21).setCellValue((map.get("situacion")==null)? "":map.get("situacion").toString());
						total_marzo = total_marzo + monto_marzo;
						total_abril = total_abril + monto_abril;
						total_mayo = total_mayo + monto_mayo;
						total_junio = total_junio + monto_junio;
						total_julio = total_julio + monto_julio;
						total_agosto = total_agosto + monto_agosto;
						total_setiembre = total_setiembre + monto_setiembre;
						total_octubre = total_octubre + monto_octubre;
						total_noviembre = total_noviembre + monto_noviembre;
						total_diciembre = total_diciembre + monto_diciembre;
						total_anual = total_anual + total;
						i++;
					//filaInicial++;
				}
			
			}	
			Row row = sheet.createRow(i+6);
			CellStyle style = workbook.createCellStyle();
	        style.setFont(font);
			Cell cell = row.createCell(columnaInicial + 2);
			cell.setCellStyle(style);
			cell.setCellValue("TOTAL DE ALUMNOS: "+(i-1));
			CellStyle style2 = workbook.createCellStyle();
			style2.setDataFormat(format.getFormat("#,##0.00"));
			style2.setFont(font);
			Cell cell2 = row.createCell(columnaInicial + 10);
			cell2.setCellStyle(style2);
			cell2.setCellValue(total_marzo);
			Cell cell3 = row.createCell(columnaInicial + 11);
			cell3.setCellStyle(style2);
			cell3.setCellValue(total_abril);
			Cell cell4 = row.createCell(columnaInicial + 12);
			cell4.setCellStyle(style2);
			cell4.setCellValue(total_mayo);
			Cell cell5 = row.createCell(columnaInicial + 13);
			cell5.setCellStyle(style2);
			cell5.setCellValue(total_junio);
			Cell cell6 = row.createCell(columnaInicial + 14);
			cell6.setCellStyle(style2);
			cell6.setCellValue(total_julio);
			Cell cell7 = row.createCell(columnaInicial + 15);
			cell7.setCellStyle(style2);
			cell7.setCellValue(total_agosto);
			Cell cell8 = row.createCell(columnaInicial + 16);
			cell8.setCellStyle(style2);
			cell8.setCellValue(total_setiembre);
			Cell cell9 = row.createCell(columnaInicial + 17);
			cell9.setCellStyle(style2);
			cell9.setCellValue(total_octubre);
			Cell cell10 = row.createCell(columnaInicial + 18);
			cell10.setCellStyle(style2);
			cell10.setCellValue(total_noviembre);
			Cell cell11 = row.createCell(columnaInicial + 19);
			cell11.setCellStyle(style2);
			cell11.setCellValue(total_diciembre);
			Cell cell12 = row.createCell(columnaInicial + 20);
			cell12.setCellStyle(style2);
			cell12.setCellValue(total_anual);
			inputStream.close();

			FileOutputStream outputStream = new FileOutputStream(nuevoArchivo);
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return nuevoArchivo;
	}
	
	public String generaExcelReporteDeudasAcadVac(String carpeta, String archivo,String nivel, String grado, String aula, String mes, String situacion,List<Map<String,Object>> list) {

		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd_hhmmss");
		SimpleDateFormat dt2 = new SimpleDateFormat("yyyy");
		String anio=dt2.format(new Date());
		String nuevoArchivo = null;
		int filaInicial = 7;
		int columnaInicial = 0;
		try {
			nuevoArchivo = dt1.format(new Date());

			nuevoArchivo = carpeta + archivo.replace("plantilla_reporte_deudores", "Reporte_Deudores" + nuevoArchivo);

			ArchivoUtil.copyFileUsingStream(new File(carpeta + archivo), new File(nuevoArchivo));

			FileInputStream inputStream = new FileInputStream(new File(nuevoArchivo));
			Workbook workbook = WorkbookFactory.create(inputStream);

			Sheet sheet = workbook.getSheetAt(0);
			
			// TITULO
			sheet.getRow(0).getCell(1).setCellValue("REPORTE DE DEUDAS AÑO - " + anio);
			// NIVEL, GRADO, AULA
			sheet.getRow(2).getCell(1).setCellValue("NIVEL: "+nivel+"  GRADO: "+grado+" SECCIÓN: "+aula);
			// MES  SITUACION
			//sheet.getRow(4).getCell(1).setCellValue("HASTA EL MES DE : "+mes+" SITUACIÓN: "+situacion);
			Date fec=new Date();
			
			sheet.getRow(0).getCell(19).setCellValue("Fecha: " + FechaUtil.toString(fec, "dd-MM-yyyy"));
			sheet.getRow(2).getCell(19).setCellValue("Hora: " + FechaUtil.toString(fec, "HH:mm:ss"));
		    //Para el salto de linea en la misma celda
			CellStyle cs = workbook.createCellStyle();
		    cs.setWrapText(true);
		    
		    DataFormat format = workbook.createDataFormat();
			Font font = workbook.createFont();
		      font.setColor(HSSFColor.BLACK.index);
		      font.setBold(true);
		    //Font font = workbook.createFont();
	        //font.setColor(HSSFColor.BLUE.index);
	     
			Integer i=1;
			Double total_cuota1 = new Double(0);
			Double total_cuota2 = new Double(0);
			Double total_cuota3 = new Double(0);
			Double total_cuota4 = new Double(0);
			Double total_cuota5 = new Double(0);
			Double total_cuota6 = new Double(0);
			Double total_cuota7 = new Double(0);
			Double total_cuota8 = new Double(0);
			Double total_cuota9 = new Double(0);
			Double total_cuota10 = new Double(0);
			Double total_anual = new Double(0);
			for (Map<String,Object> map : list) {
				if(map.get("alumno")!=null) {
					System.out.println(map.get("alumno"));
					//sheet.getRow(filaInicial).getCell(columnaInicial + 0).setCellStyle(cs);
					Row row = sheet.createRow(filaInicial++);
					row.createCell(columnaInicial + 0).setCellValue(i);
					row.createCell(columnaInicial + 1).setCellValue(map.get("alumno").toString());
					row.createCell(columnaInicial + 2).setCellValue(map.get("responsable").toString());
					row.createCell(columnaInicial + 3).setCellValue((map.get("celular")==null)? "":map.get("celular").toString());
					row.createCell(columnaInicial + 4).setCellValue((map.get("direccion")==null)? "":map.get("direccion").toString());
					row.createCell(columnaInicial + 5).setCellValue((map.get("correo")==null)? "":map.get("correo").toString());
					row.createCell(columnaInicial + 6).setCellValue(map.get("ciclo").toString());
					row.createCell(columnaInicial + 7).setCellValue(map.get("aula").toString());
					row.createCell(columnaInicial + 8).setCellValue("Pensión");
					row.createCell(columnaInicial + 9).setCellValue((map.get("cuota1")==null)? "":map.get("cuota1").toString());
					row.createCell(columnaInicial + 10).setCellValue((map.get("cuota2")==null)? "":map.get("cuota2").toString());
					row.createCell(columnaInicial + 11).setCellValue((map.get("cuota3")==null)? "":map.get("cuota3").toString());
					row.createCell(columnaInicial + 12).setCellValue((map.get("cuota4")==null)? "":map.get("cuota4").toString());
					row.createCell(columnaInicial + 13).setCellValue((map.get("cuota5")==null)? "":map.get("cuota5").toString());
					row.createCell(columnaInicial + 14).setCellValue((map.get("cuota6")==null)? "":map.get("cuota6").toString());
					row.createCell(columnaInicial + 15).setCellValue((map.get("cuota7")==null)? "":map.get("cuota7").toString());
					row.createCell(columnaInicial + 16).setCellValue((map.get("cuota8")==null)? "":map.get("cuota8").toString());
					row.createCell(columnaInicial + 17).setCellValue((map.get("cuota9")==null)? "":map.get("cuota9").toString());
					row.createCell(columnaInicial + 18).setCellValue((map.get("cuota10")==null)? "":map.get("cuota10").toString());
					//logger.debug(map.get("marzo_monto"));
					//if(!map.get("marzo_monto").toString().equals(" ") && map.get("marzo_banco").toString().equals(" ")){
					Double total = new Double(0);
					Double monto_cuota1 = Double.parseDouble((map.get("cuota1")==null || map.get("cuota1")=="")? "0.00":map.get("cuota1").toString());
					Double monto_cuota2 = Double.parseDouble((map.get("cuota2")==null || map.get("cuota2")=="")? "0.00":map.get("cuota2").toString());
					Double monto_cuota3 = Double.parseDouble((map.get("cuota3")==null || map.get("cuota3")=="")? "0.00":map.get("cuota3").toString());
					Double monto_cuota4 = Double.parseDouble((map.get("cuota4")==null || map.get("cuota4")=="")? "0.00":map.get("cuota4").toString());
					Double monto_cuota5 = Double.parseDouble((map.get("cuota5")==null || map.get("cuota5")=="")? "0.00":map.get("cuota5").toString());
					Double monto_cuota6 = Double.parseDouble((map.get("cuota6")==null || map.get("cuota6")=="")? "0.00":map.get("cuota6").toString());
					Double monto_cuota7 = Double.parseDouble((map.get("cuota7")==null || map.get("cuota7")=="")? "0.00":map.get("cuota7").toString());
					Double monto_cuota8 = Double.parseDouble((map.get("cuota8")==null || map.get("cuota8")=="")? "0.00":map.get("cuota8").toString());
					Double monto_cuota9 = Double.parseDouble((map.get("cuota9")==null || map.get("cuota9")=="")? "0.00":map.get("cuota9").toString());
					Double monto_cuota10 = Double.parseDouble((map.get("cuota10")==null || map.get("cuota10")=="")? "0.00":map.get("cuota10").toString());
					CellStyle style = workbook.createCellStyle();
					style.setDataFormat(format.getFormat("#,##0.00"));
			        style.setFont(font);
						total = total+monto_cuota1+monto_cuota2+monto_cuota3+monto_cuota4+monto_cuota5+monto_cuota6+monto_cuota7+monto_cuota8+monto_cuota9+monto_cuota10;
						Cell cell = row.createCell(columnaInicial + 19);
						cell.setCellStyle(style);
						cell.setCellValue(total);
						
						//style.setFont(font);
						row.createCell(columnaInicial + 20).setCellValue((map.get("situacion")==null)? "":map.get("situacion").toString());
						total_cuota1 = total_cuota1 + monto_cuota1;
						total_cuota2 = total_cuota2 + monto_cuota2;
						total_cuota3 = total_cuota3 + monto_cuota3;
						total_cuota4 = total_cuota4 + monto_cuota4;
						total_cuota5 = total_cuota5 + monto_cuota5;
						total_cuota6 = total_cuota6 + monto_cuota6;
						total_cuota7 = total_cuota7 + monto_cuota7;
						total_cuota8 = total_cuota8 + monto_cuota8;
						total_cuota9 = total_cuota9 + monto_cuota9;
						total_cuota10 = total_cuota10 + monto_cuota10;
						total_anual = total_anual + total;
						i++;
					//filaInicial++;
				}
			
			}	
			Row row = sheet.createRow(i+6);
			CellStyle style = workbook.createCellStyle();
	        style.setFont(font);
			Cell cell = row.createCell(columnaInicial + 2);
			cell.setCellStyle(style);
			cell.setCellValue("TOTAL DE ALUMNOS: "+(i-1));
			CellStyle style2 = workbook.createCellStyle();
			style2.setDataFormat(format.getFormat("#,##0.00"));
			style2.setFont(font);
			Cell cell2 = row.createCell(columnaInicial + 9);
			cell2.setCellStyle(style2);
			cell2.setCellValue(total_cuota1);
			Cell cell3 = row.createCell(columnaInicial + 10);
			cell3.setCellStyle(style2);
			cell3.setCellValue(total_cuota2);
			Cell cell4 = row.createCell(columnaInicial + 11);
			cell4.setCellStyle(style2);
			cell4.setCellValue(total_cuota3);
			Cell cell5 = row.createCell(columnaInicial + 12);
			cell5.setCellStyle(style2);
			cell5.setCellValue(total_cuota4);
			Cell cell6 = row.createCell(columnaInicial + 13);
			cell6.setCellStyle(style2);
			cell6.setCellValue(total_cuota5);
			Cell cell7 = row.createCell(columnaInicial + 14);
			cell7.setCellStyle(style2);
			cell7.setCellValue(total_cuota6);
			Cell cell8 = row.createCell(columnaInicial + 15);
			cell8.setCellStyle(style2);
			cell8.setCellValue(total_cuota7);
			Cell cell9 = row.createCell(columnaInicial + 16);
			cell9.setCellStyle(style2);
			cell9.setCellValue(total_cuota8);
			Cell cell10 = row.createCell(columnaInicial + 17);
			cell10.setCellStyle(style2);
			cell10.setCellValue(total_cuota9);
			Cell cell11 = row.createCell(columnaInicial + 18);
			cell11.setCellStyle(style2);
			cell11.setCellValue(total_cuota10);
			Cell cell12 = row.createCell(columnaInicial + 19);
			cell12.setCellStyle(style2);
			cell12.setCellValue(total_anual);
			inputStream.close();

			FileOutputStream outputStream = new FileOutputStream(nuevoArchivo);
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return nuevoArchivo;
	}
	
	public String generaExcelReportePagos(String carpeta, String archivo,String nivel, String grado, String aula, String mes, String situacion,List<Map<String,Object>> list) {

		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd_hhmmss");
		SimpleDateFormat dt2 = new SimpleDateFormat("yyyy");
		String anio=dt2.format(new Date());
		String nuevoArchivo = null;
		int filaInicial = 7;
		int columnaInicial = 0;
		try {
			nuevoArchivo = dt1.format(new Date());

			nuevoArchivo = carpeta + archivo.replace("plantilla_reporte_pagados", "Reporte_Pagados" + nuevoArchivo);

			ArchivoUtil.copyFileUsingStream(new File(carpeta + archivo), new File(nuevoArchivo));

			FileInputStream inputStream = new FileInputStream(new File(nuevoArchivo));
			Workbook workbook = WorkbookFactory.create(inputStream);

			Sheet sheet = workbook.getSheetAt(0);
			
			// TITULO
			sheet.getRow(0).getCell(1).setCellValue("REPORTE DE PAGOS AÑO - " + anio);
			// NIVEL, GRADO, AULA
			sheet.getRow(2).getCell(1).setCellValue("NIVEL: "+nivel+"  GRADO: "+grado+" SECCIÓN: "+aula);
			// MES  SITUACION
			sheet.getRow(4).getCell(1).setCellValue("HASTA EL MES DE : "+mes+" SITUACIÓN: "+situacion);
			Date fec=new Date();
			
			sheet.getRow(0).getCell(19).setCellValue("Fecha: " + FechaUtil.toString(fec, "dd-MM-yyyy"));
			sheet.getRow(2).getCell(19).setCellValue("Hora: " + FechaUtil.toString(fec, "HH:mm:ss"));
		    //Para el salto de linea en la misma celda
			CellStyle cs = workbook.createCellStyle();
		    cs.setWrapText(true);
		    
		    DataFormat format = workbook.createDataFormat();
			Font font = workbook.createFont();
		      font.setColor(HSSFColor.BLACK.index);
		      font.setBold(true);
		    //Font font = workbook.createFont();
	        //font.setColor(HSSFColor.BLUE.index);
	     
			Integer i=1;
			Double total_marzo = new Double(0);
			Double total_abril = new Double(0);
			Double total_mayo = new Double(0);
			Double total_junio = new Double(0);
			Double total_julio = new Double(0);
			Double total_agosto = new Double(0);
			Double total_setiembre = new Double(0);
			Double total_octubre = new Double(0);
			Double total_noviembre = new Double(0);
			Double total_diciembre = new Double(0);
			Double total_anual = new Double(0);
			for (Map<String,Object> map : list) {
				if(map.get("alumno")!=null) {
					System.out.println(map.get("alumno"));
					//sheet.getRow(filaInicial).getCell(columnaInicial + 0).setCellStyle(cs);
					Row row = sheet.createRow(filaInicial++);
					row.createCell(columnaInicial + 0).setCellValue(i);
					row.createCell(columnaInicial + 1).setCellValue(map.get("alumno").toString());
					row.createCell(columnaInicial + 2).setCellValue(map.get("familiar").toString());
					row.createCell(columnaInicial + 3).setCellValue((map.get("celular")==null)? "":map.get("celular").toString());
					row.createCell(columnaInicial + 4).setCellValue((map.get("direccion")==null)? "":map.get("direccion").toString());
					row.createCell(columnaInicial + 5).setCellValue(map.get("aula").toString());
					row.createCell(columnaInicial + 6).setCellValue("Pensión");
					row.createCell(columnaInicial + 9).setCellValue((map.get("marzo_monto")==null)? "":map.get("marzo_monto").toString().concat(" - ").concat((map.get("marzo_recibo")==null)? "":map.get("marzo_recibo").toString()));
					row.createCell(columnaInicial + 10).setCellValue((map.get("abril_monto")==null)? "":map.get("abril_monto").toString().concat(" - ").concat((map.get("marzo_recibo")==null)? "":map.get("abril_recibo").toString()));
					row.createCell(columnaInicial + 11).setCellValue((map.get("mayo_monto")==null)? "":map.get("mayo_monto").toString().concat(" - ").concat((map.get("marzo_recibo")==null)? "":map.get("mayo_recibo").toString()));
					row.createCell(columnaInicial + 12).setCellValue((map.get("junio_monto")==null)? "":map.get("junio_monto").toString().concat(" - ").concat((map.get("marzo_recibo")==null)? "":map.get("junio_recibo").toString()));
					row.createCell(columnaInicial + 13).setCellValue((map.get("julio_monto")==null)? "":map.get("julio_monto").toString().concat(" - ").concat((map.get("marzo_recibo")==null)? "":map.get("julio_recibo").toString()));
					row.createCell(columnaInicial + 14).setCellValue((map.get("agosto_monto")==null)? "":map.get("agosto_monto").toString().concat((map.get("marzo_recibo")==null)? "":map.get("agosto_recibo").toString()));
					row.createCell(columnaInicial + 15).setCellValue((map.get("setiembre_monto")==null)? "":map.get("setiembre_monto").toString().concat(" - ").concat((map.get("marzo_recibo")==null)? "":map.get("setiembre_recibo").toString()));
					row.createCell(columnaInicial + 16).setCellValue((map.get("octubre_monto")==null)? "":map.get("octubre_monto").toString().concat(" - ").concat((map.get("marzo_recibo")==null)? "":map.get("octubre_recibo").toString()));
					row.createCell(columnaInicial + 17).setCellValue((map.get("noviembre_monto")==null)? "":map.get("noviembre_monto").toString().concat(" - ").concat((map.get("marzo_recibo")==null)? "":map.get("noviembre_recibo").toString()));
					row.createCell(columnaInicial + 18).setCellValue((map.get("diciembre_monto")==null)? "":map.get("diciembre_monto").toString().concat(" - ").concat((map.get("marzo_recibo")==null)? "":map.get("diciembre_recibo").toString()));
					//logger.debug(map.get("marzo_monto"));
					//if(!map.get("marzo_monto").toString().equals(" ") && map.get("marzo_banco").toString().equals(" ")){
					Double total = new Double(0);
					Double monto_marzo = Double.parseDouble((map.get("marzo_monto")==null || map.get("marzo_monto")=="")? "0.00":map.get("marzo_monto").toString());
					Double monto_abril = Double.parseDouble((map.get("abril_monto")==null || map.get("abril_monto")=="")? "0.00":map.get("abril_monto").toString());
					Double monto_mayo = Double.parseDouble((map.get("mayo_monto")==null || map.get("mayo_monto")=="")? "0.00":map.get("mayo_monto").toString());
					Double monto_junio = Double.parseDouble((map.get("junio_monto")==null || map.get("junio_monto")=="")? "0.00":map.get("junio_monto").toString());
					Double monto_julio = Double.parseDouble((map.get("julio_monto")==null || map.get("julio_monto")=="")? "0.00":map.get("julio_monto").toString());
					Double monto_agosto = Double.parseDouble((map.get("agosto_monto")==null || map.get("agosto_monto")=="")? "0.00":map.get("agosto_monto").toString());
					Double monto_setiembre = Double.parseDouble((map.get("setiembre_monto")==null || map.get("setiembre_monto")=="")? "0.00":map.get("setiembre_monto").toString());
					Double monto_octubre = Double.parseDouble((map.get("octubre_monto")==null || map.get("octubre_monto")=="")? "0.00":map.get("octubre_monto").toString());
					Double monto_noviembre = Double.parseDouble((map.get("noviembre_monto")==null || map.get("noviembre_monto")=="")? "0.00":map.get("noviembre_monto").toString());
					Double monto_diciembre = Double.parseDouble((map.get("diciembre_monto")==null || map.get("diciembre_monto")=="")? "0.00":map.get("diciembre_monto").toString());
					CellStyle style = workbook.createCellStyle();
					style.setDataFormat(format.getFormat("#,##0.00"));
			        style.setFont(font);
						total = total+monto_marzo+monto_abril+monto_mayo+monto_junio+monto_julio+monto_agosto+monto_setiembre+monto_octubre+monto_noviembre+monto_diciembre;
						Cell cell = row.createCell(columnaInicial + 19);
						cell.setCellStyle(style);
						cell.setCellValue(total);
						
						//style.setFont(font);
						row.createCell(columnaInicial + 20).setCellValue((map.get("situacion")==null)? "":map.get("situacion").toString());
						total_marzo = total_marzo + monto_marzo;
						total_abril = total_abril + monto_abril;
						total_mayo = total_mayo + monto_mayo;
						total_junio = total_junio + monto_junio;
						total_julio = total_julio + monto_julio;
						total_agosto = total_agosto + monto_agosto;
						total_setiembre = total_setiembre + monto_setiembre;
						total_octubre = total_octubre + monto_octubre;
						total_noviembre = total_noviembre + monto_noviembre;
						total_diciembre = total_diciembre + monto_diciembre;
						total_anual = total_anual + total;
						i++;
					//filaInicial++;
				}
			
			}	
			Row row = sheet.createRow(i+6);
			CellStyle style = workbook.createCellStyle();
	        style.setFont(font);
			Cell cell = row.createCell(columnaInicial + 2);
			cell.setCellStyle(style);
			cell.setCellValue("TOTAL DE ALUMNOS: "+(i-1));
			CellStyle style2 = workbook.createCellStyle();
			style2.setDataFormat(format.getFormat("#,##0.00"));
			style2.setFont(font);
			Cell cell2 = row.createCell(columnaInicial + 9);
			cell2.setCellStyle(style2);
			cell2.setCellValue(total_marzo);
			Cell cell3 = row.createCell(columnaInicial + 10);
			cell3.setCellStyle(style2);
			cell3.setCellValue(total_abril);
			Cell cell4 = row.createCell(columnaInicial + 11);
			cell4.setCellStyle(style2);
			cell4.setCellValue(total_mayo);
			Cell cell5 = row.createCell(columnaInicial + 12);
			cell5.setCellStyle(style2);
			cell5.setCellValue(total_junio);
			Cell cell6 = row.createCell(columnaInicial + 13);
			cell6.setCellStyle(style2);
			cell6.setCellValue(total_julio);
			Cell cell7 = row.createCell(columnaInicial + 14);
			cell7.setCellStyle(style2);
			cell7.setCellValue(total_agosto);
			Cell cell8 = row.createCell(columnaInicial + 15);
			cell8.setCellStyle(style2);
			cell8.setCellValue(total_setiembre);
			Cell cell9 = row.createCell(columnaInicial + 16);
			cell9.setCellStyle(style2);
			cell9.setCellValue(total_octubre);
			Cell cell10 = row.createCell(columnaInicial + 17);
			cell10.setCellStyle(style2);
			cell10.setCellValue(total_noviembre);
			Cell cell11 = row.createCell(columnaInicial + 18);
			cell11.setCellStyle(style2);
			cell11.setCellValue(total_diciembre);
			Cell cell12 = row.createCell(columnaInicial + 19);
			cell12.setCellStyle(style2);
			cell12.setCellValue(total_anual);
			inputStream.close();

			FileOutputStream outputStream = new FileOutputStream(nuevoArchivo);
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return nuevoArchivo;
	}
	
	public String generaExcelReporteInfocorp(String carpeta, String archivo,List<Map<String, Object>> list) {

		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd_hhmmss");

		String nuevoArchivo = null;
		int filaInicial = 5;
		int columnaInicial = 1;
		try {
			nuevoArchivo = dt1.format(new Date());

			nuevoArchivo = carpeta + archivo.replace("plantilla_reporte_infocorp", "Reporte_Infocorp" + nuevoArchivo);

			ArchivoUtil.copyFileUsingStream(new File(carpeta + archivo), new File(nuevoArchivo));

			FileInputStream inputStream = new FileInputStream(new File(nuevoArchivo));
			Workbook workbook = WorkbookFactory.create(inputStream);

			Sheet sheet = workbook.getSheetAt(0);


			sheet.shiftRows(8, sheet.getLastRowNum(), 1);
			
			int fila =1;
			Double total = new Double(0);
			DataFormat format = workbook.createDataFormat();
			Font font = workbook.createFont();
		      font.setColor(HSSFColor.BLUE.index);

		  	for (Map<String,Object> map : list) {

				Row row = sheet.createRow(filaInicial++);
				row.createCell(columnaInicial + 0).setCellValue(fila++);
				row.createCell(columnaInicial + 1).setCellValue(map.get("fec_con").toString());
				row.createCell(columnaInicial + 2).setCellValue(map.get("codigo").toString());
				row.createCell(columnaInicial + 3).setCellValue(map.get("id").toString());
				row.createCell(columnaInicial + 4).setCellValue(map.get("id_tdc").toString());
				row.createCell(columnaInicial + 5).setCellValue(map.get("numdoc").toString());
				row.createCell(columnaInicial + 6).setCellValue(map.get("tip_per").toString());
				row.createCell(columnaInicial + 7).setCellValue(map.get("tip_deudor").toString());
				row.createCell(columnaInicial + 8).setCellValue(map.get("familiar").toString());
				row.createCell(columnaInicial + 9).setCellValue((map.get("direccion")==null)?"":map.get("direccion").toString());
				row.createCell(columnaInicial + 11).setCellValue((map.get("dis_nom")==null)?"":map.get("dis_nom").toString());
				row.createCell(columnaInicial + 13).setCellValue((map.get("dep_nom")==null)?"":map.get("dep_nom").toString());
				row.createCell(columnaInicial + 14).setCellValue(map.get("fecha_ven").toString());
				row.createCell(columnaInicial + 15).setCellValue(map.get("doc_cre").toString());
				row.createCell(columnaInicial + 16).setCellValue(map.get("tip_mo").toString());
				row.createCell(columnaInicial + 17).setCellValue(map.get("monto").toString());
				//row.createCell(columnaInicial + 7).setCellValue(tipoDes);
				Double monto = new Double(map.get("monto").toString());
				total = total + monto;
				CellStyle style = workbook.createCellStyle();
				style.setDataFormat(format.getFormat("#,##0.00"));
		        style.setFont(font);
			     
			}
			
			Row row = sheet.createRow(++filaInicial);

			
		//row.createCell(columnaInicial + 6).setCellValue("TOTAL");
			Cell cell = row.createCell(columnaInicial + 17); 
			cell.setCellValue(total);
		    
			CellStyle style = workbook.createCellStyle();
			style.setDataFormat(format.getFormat("#,##0.00"));
			cell.setCellStyle(style);
			
			//inputStream.close();

			FileOutputStream outputStream = new FileOutputStream(nuevoArchivo);
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return nuevoArchivo;
	}
	
	public void generaRegistrosAuxiliares(Workbook workbook ,Integer indice, Integer id_anio,List<Map<String,Object>> list) {

		int filaInicial = 11;
		int columnaInicial = 0;
		try {
			

			Sheet sheet = workbook.getSheetAt(indice);
			// GRADO
			sheet.getRow(4).getCell(1).setCellValue("Grado: " + list.get(0).get("nivel")+ "-" +list.get(0).get("grado"));

			// SECCION
			sheet.getRow(5).getCell(1).setCellValue("Seccion: "+ list.get(0).get("secc"));
			
			
			
			int fila =1;
			for (Map<String,Object> map : list) {

				//Row row = sheet.createRow(filaInicial++);

				sheet.getRow(filaInicial).getCell(columnaInicial + 0).setCellValue(fila++);
				sheet.getRow(filaInicial++).getCell(columnaInicial + 1).setCellValue(map.get("alumno").toString());
			     
			}
			
			//for (int i = 1; i <= totalRecords; i++) {
              //  workbook.setSheetName(i, "S" + i);
          //  }
			//XSSFWorkbook workboo = new XSSFWorkbook(file);
            
		    
			

		} catch (Exception e) {
			e.printStackTrace();

		}


	}
	
	public void generaReporteAsistencia(Workbook workbook ,Integer indice, Integer id_anio,List<Map<String, Object>> list, List<Map<String, Object>> list_dias_mes, String usuario, Integer id_mes, String anio) {

		int filaInicial = 5;
		int columnaInicial = 0;
		try {
			
			Sheet sheet = workbook.getSheetAt(indice);
			// MES
			String mes=null;
			if (id_mes.equals(1)) {
				mes="ENERO";
			} else if (id_mes.equals(2)) {
				mes="FEBRERO";
			} else if (id_mes.equals(3)) {
				mes="MARZO";
			} else if (id_mes.equals(4)) {
				mes="ABRIL";
			} else if (id_mes.equals(5)) {
				mes="MAYO";
			} else if (id_mes.equals(6)) {
				mes="JUNIO";
			} else if (id_mes.equals(7)) {
				mes="JULIO";
			} else if (id_mes.equals(8)) {
				mes="AGOSTO";
			} else if (id_mes.equals(9)) {
				mes="SETIEMBRE";
			} else if (id_mes.equals(10)) {
				mes="OCTUBRE";
			} else if (id_mes.equals(11)) {
				mes="NOVIEMBRE";
			} else if (id_mes.equals(12)) {
				mes="DICIEMBRE";
			}
			
			sheet.getRow(0).getCell(0).setCellValue("ASISTENCIA DEL MES DE "+mes+" "+anio); 
			sheet.getRow(2).getCell(0).setCellValue("AULA: " + list.get(0).get("nivel")+ "-" +list.get(0).get("grado")+"-"+list.get(0).get("secc"));
			sheet.getRow(2).getCell(9).setCellValue("Asistente(a): " + usuario);
						
			//estilo de sabado y domingo
			CellStyle styleSD = workbook.createCellStyle();
			styleSD.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			styleSD.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			styleSD.setBorderBottom(BorderStyle.THIN);
			styleSD.setBorderLeft(BorderStyle.THIN);
			styleSD.setBorderTop(BorderStyle.THIN);
			styleSD.setBorderRight(BorderStyle.THIN);
			
			//estilo de LETRAS ROJO POR FALTA
			CellStyle styleRED = workbook.createCellStyle();
			Font font = workbook.createFont();
		    font.setBold(true);
		    font.setFontName("calibri");

	        font.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
	        styleRED.setFont(font);
	        styleRED.setBorderBottom(BorderStyle.THIN);
	        styleRED.setBorderLeft(BorderStyle.THIN);
	        styleRED.setBorderTop(BorderStyle.THIN);
	        styleRED.setBorderRight(BorderStyle.THIN);
	        styleRED.setAlignment(HorizontalAlignment.CENTER);
			
			int fila =1;
			for (Map<String,Object> map : list) {
				columnaInicial = 0;
				
				//Row row = sheet.createRow(filaInicial++);

				sheet.getRow(filaInicial).getCell(columnaInicial++).setCellValue(fila++);
				sheet.getRow(filaInicial).getCell(columnaInicial++).setCellValue(map.get("alumno").toString());
			     
				//iteramos cada listade dias
				for (Map<String, Object> mapDia: list_dias_mes) {
					String dia = mapDia.get("dia").toString();
					Date date = FechaUtil.toDate(dia, "dd/MM/yyyy");
					String asistencia = "";
					Object diaAsistenciaObj = map.get("dias_asistencia");
					
					if(diaAsistenciaObj!=null){
						com.tesla.frmk.sql.Row rowAsistencia = (com.tesla.frmk.sql.Row)diaAsistenciaObj;
						if (rowAsistencia.getString(dia)!=null)
							asistencia = rowAsistencia.getString(dia);
					}
					
					Cell cell = sheet.getRow(filaInicial).getCell(columnaInicial++);
					cell.setCellValue(asistencia);
					
					if (FechaUtil.isSabOrDom(date)){
					
					    cell.setCellStyle(styleSD);
					}
					
					if ("F".equals(asistencia)){
						
					    cell.setCellStyle(styleRED);
					}
					
				}
				
				filaInicial++;
			}
			
			int filadias=4;
			int colum = 2;
			for ( Map<String,Object> map : list_dias_mes) {
				sheet.getRow(filadias).getCell(colum++).setCellValue(map.get("dia").toString());
			}
			
			//for (int i = 1; i <= totalRecords; i++) {
              //  workbook.setSheetName(i, "S" + i);
          //  }
			//XSSFWorkbook workboo = new XSSFWorkbook(file);
            
		    
			

		} catch (Exception e) {
			e.printStackTrace();

		}


	}

	public static void main1(String[] args) {
		String excelFilePath = "D:/Libro1.xls";

		try {

			int filaInicio = 3;


			FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
			Workbook workbook = WorkbookFactory.create(inputStream);

			Sheet sheet = workbook.getSheetAt(0);

			Object[][] bookData = { { "The Passionate Programmer", "Chad Fowler", 16 },
					{ "Software Craftmanship", "Pete McBreen", 26 },
					{ "The Art of Agile Development", "James Shore", 32 },
					{ "Continuous Delivery", "Jez Humble", 41 }, };

			for (Object[] aBook : bookData) {

				Row fila = sheet.getRow(filaInicio++);

				int inicio = 0;
				for (Object field : aBook) {
					Cell cell2Update = fila.getCell(inicio++);

					logger.debug("field:" + field);
					cell2Update.setCellValue(field.toString());
				}

			}
			/*
			 * int rowCount = 1;//sheet.getLastRowNum();
			 * 
			 * for (Object[] aBook : bookData) { Row row =
			 * sheet.createRow(++rowCount);
			 * 
			 * int columnCount = 0;
			 * 
			 * Cell cell = row.createCell(columnCount);
			 * cell.setCellValue(rowCount);
			 * 
			 * for (Object field : aBook) { cell =
			 * row.createCell(++columnCount); if (field instanceof String) {
			 * cell.setCellValue((String) field); } else if (field instanceof
			 * Integer) { cell.setCellValue((Integer) field); } }
			 * 
			 * }
			 */

			inputStream.close();

			FileOutputStream outputStream = new FileOutputStream(excelFilePath);
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public String generaExcelReporteTema(String carpeta, String archivo, String nivel, String curso, List<com.tesla.frmk.sql.Row> list) {

		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd_hhmmss");

		String nuevoArchivo = null;
		int filaInicial = 7;
		int columnaInicial = 1;
		try {
			nuevoArchivo = dt1.format(new Date());

			nuevoArchivo = carpeta + "tmp/" + archivo.replace("plantilla_reporte_tema", "Reporte_Tema" + nuevoArchivo);

			ArchivoUtil.copyFileUsingStream(new File(carpeta + archivo), new File(nuevoArchivo));

			FileInputStream inputStream = new FileInputStream(new File(nuevoArchivo));
			Workbook workbook = WorkbookFactory.create(inputStream);

			Sheet sheet = workbook.getSheetAt(0);

			// TITULO
			//sheet.getRow(1).getCell(1).setCellValue("REPORTE DE TEMAS");
			// USUARIO
			sheet.getRow(3).getCell(2).setCellValue(nivel.toUpperCase());
			sheet.getRow(4).getCell(2).setCellValue(curso.toUpperCase());

			sheet.shiftRows(8, sheet.getLastRowNum(), 1);
			
			int fila =1;
			Double total = new Double(0);
			DataFormat format = workbook.createDataFormat();
			Font font = workbook.createFont();
		      font.setColor(HSSFColor.BLUE.index);

		    String tema = "";
		    /*
		    CellStyle style = workbook.createCellStyle();
		    style.setBorderLeft(BorderStyle.THIN);
		    style.setBorderBottom(BorderStyle.NONE);
		    style.setBorderBottom(BorderStyle.NONE);
		    //style.setBorderBottom(BorderStyle.THIN);
		    
		    CellStyle style2 = workbook.createCellStyle();
		    style2.setBorderLeft(BorderStyle.THIN);
		    style2.setBorderRight(BorderStyle.THIN);
		    style2.setBorderBottom(BorderStyle.THIN);
		    style2.setBorderTop(BorderStyle.THIN);
		    style2.setBottomBorderColor(IndexedColors.WHITE.getIndex());
		    */
			for (com.tesla.frmk.sql.Row map : list) {

				Row row = sheet.createRow(filaInicial++);

				if (tema.equals(map.get("tema").toString())){
					row.createCell(columnaInicial + 1).setCellValue("");
					//row.getCell(columnaInicial+1).setCellStyle(style2);
				}else{
					row.createCell(columnaInicial + 0).setCellValue(fila++);
					row.createCell(columnaInicial + 1).setCellValue(map.get("tema").toString());
					//row.getCell(columnaInicial+0).setCellStyle(style);
					//row.getCell(columnaInicial+1).setCellStyle(style);

				}
				
				tema = map.get("tema").toString();
				
				row.createCell(columnaInicial + 2).setCellValue(map.get("subtema").toString());
 

			     
			}
			
			Row row = sheet.createRow(++filaInicial);

			 
			
			inputStream.close();

			FileOutputStream outputStream = new FileOutputStream(nuevoArchivo);
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return nuevoArchivo;
	}
	
	public String generaExcelReporteDatosComunicacion(String carpeta, String archivo, String nivel, String tutor, List<com.tesla.frmk.sql.Row> list) {

		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd_hhmmss");

		String nuevoArchivo = null;
		int filaInicial = 7;
		int columnaInicial = 1;
		try {
			nuevoArchivo = dt1.format(new Date());

			nuevoArchivo = carpeta +"tmp/"+ archivo.replace("plantilla_directorio_ppff", "Directorio_Padres_Familia" + nuevoArchivo);
			//System.out.println(carpeta + archivo);
			//System.out.println(nuevoArchivo);
			
			ArchivoUtil.copyFileUsingStream(new File(carpeta + archivo), new File(nuevoArchivo));

			FileInputStream inputStream = new FileInputStream(new File(nuevoArchivo));
			Workbook workbook = WorkbookFactory.create(inputStream);

			Sheet sheet = workbook.getSheetAt(0);

			// TITULO
			//sheet.getRow(1).getCell(1).setCellValue("REPORTE DE TEMAS");
			// USUARIO
			sheet.getRow(3).getCell(2).setCellValue(nivel.toUpperCase());
			sheet.getRow(4).getCell(2).setCellValue(tutor.toUpperCase());

			sheet.shiftRows(8, sheet.getLastRowNum(), 1);
			
			int fila =1;
			Double total = new Double(0);
			DataFormat format = workbook.createDataFormat();
			Font font = workbook.createFont();
		      //font.setColor(HSSFColor.BLUE.index);
		      font.setUnderline(HSSFFont.U_DOUBLE);
		    String alumno = "";
		    String grado = "";
		    String seccion = "";
		    
		    CellStyle style = workbook.createCellStyle();
		    
		  /*  style.setBorderLeft(BorderStyle.THIN);
		    style.setBorderBottom(BorderStyle.NONE);
		    style.setBorderBottom(BorderStyle.NONE);
		    //style.setBorderBottom(BorderStyle.THIN);
		    
		    CellStyle style2 = workbook.createCellStyle();
		    style2.setBorderLeft(BorderStyle.THIN);
		    style2.setBorderRight(BorderStyle.THIN);
		    style2.setBorderBottom(BorderStyle.THIN);
		    style2.setBorderTop(BorderStyle.THIN);
		    style2.setBottomBorderColor(IndexedColors.WHITE.getIndex());
		    */
			for (com.tesla.frmk.sql.Row map : list) {

				Row row = sheet.createRow(filaInicial++);

				if (alumno.equals(map.get("alumno").toString())){
					row.createCell(columnaInicial + 1).setCellValue("");
					row.createCell(columnaInicial + 2).setCellValue("");
					row.createCell(columnaInicial + 3).setCellValue("");
					//row.getCell(columnaInicial+1).setCellStyle(style2);
				}else{
					row.createCell(columnaInicial + 0).setCellValue(fila++);
					row.createCell(columnaInicial + 1).setCellValue(map.get("alumno").toString());
					row.createCell(columnaInicial + 2).setCellValue(map.get("grado").toString());
					row.createCell(columnaInicial + 3).setCellValue(map.get("seccion").toString());
					//row.getCell(columnaInicial+0).setCellStyle(style);
					//row.getCell(columnaInicial+1).setCellStyle(style);

				}
				
				/*if (grado.equals(map.get("grado").toString())){
					row.createCell(columnaInicial + 2).setCellValue("");
					//row.getCell(columnaInicial+1).setCellStyle(style2);
				}else{
					row.createCell(columnaInicial + 0).setCellValue(fila++);
					row.createCell(columnaInicial + 2).setCellValue(map.get("grado").toString());
					//row.getCell(columnaInicial+0).setCellStyle(style);
					//row.getCell(columnaInicial+1).setCellStyle(style);

				}
				
				if (seccion.equals(map.get("seccion").toString())){
					row.createCell(columnaInicial + 3).setCellValue("");
					//row.getCell(columnaInicial+1).setCellStyle(style2);
				}else{
					row.createCell(columnaInicial + 0).setCellValue(fila++);
					row.createCell(columnaInicial + 3).setCellValue(map.get("seccion").toString());
					//row.getCell(columnaInicial+0).setCellStyle(style);
					//row.getCell(columnaInicial+1).setCellStyle(style);

				}*/
				
				alumno = map.get("alumno").toString();
				//grado = map.get("grado").toString();
				//seccion = map.get("seccion").toString();
				
				//row.createCell(columnaInicial + 2).setCellValue(map.get("grado").toString());
				//row.createCell(columnaInicial + 3).setCellValue(map.get("seccion").toString());
				row.createCell(columnaInicial + 4).setCellValue((map.get("parentesco")==null)?"":map.get("parentesco").toString());
				if(map.get("id_fam").toString().equals(map.get("id_apod").toString())){
					style.setFont(font);
					
					row.createCell(columnaInicial + 5).setCellValue(map.get("familiar").toString());
					row.getCell(columnaInicial+5).setCellStyle(style);
				} else{
					row.createCell(columnaInicial + 5).setCellValue(map.get("familiar").toString());
				}
				row.createCell(columnaInicial + 6).setCellValue((map.get("fam_cel")==null)?"":map.get("fam_cel").toString());
				row.createCell(columnaInicial + 7).setCellValue((map.get("fam_dir")==null)?"":map.get("fam_dir").toString());
				row.createCell(columnaInicial + 8).setCellValue((map.get("fam_corr")==null)?"":map.get("fam_corr").toString());
				if(map.get("viv_alu")!=null)
					row.createCell(columnaInicial + 9).setCellValue((map.get("viv_alu").toString().equals("1"))? "Sí":"No");			     
			}
			
			Row row = sheet.createRow(++filaInicial);

			inputStream.close();

			FileOutputStream outputStream = new FileOutputStream(nuevoArchivo);
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return nuevoArchivo;
	}
	
	public String generaExcelFormatoUsuarioAlu(String carpeta, String archivo, List<com.tesla.frmk.sql.Row> list) {

		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd_hhmmss");

		String nuevoArchivo = null;
		int filaInicial = 1;
		int columnaInicial = 0;
		try {
			nuevoArchivo = dt1.format(new Date());

			nuevoArchivo = carpeta +"tmp/"+ archivo.replace("formato_generacion_usr_alu", "Formato_Usuarios_Alumnos" + nuevoArchivo);
			//System.out.println(carpeta + archivo);
			//System.out.println(nuevoArchivo);
			
			ArchivoUtil.copyFileUsingStream(new File(carpeta + archivo), new File(nuevoArchivo));

			FileInputStream inputStream = new FileInputStream(new File(nuevoArchivo));
			Workbook workbook = WorkbookFactory.create(inputStream);

			Sheet sheet = workbook.getSheetAt(0);

			for (com.tesla.frmk.sql.Row map : list) {
				
				Row row = sheet.createRow(filaInicial++);
				row.createCell(columnaInicial).setCellValue(map.get("usuario").toString()+","+map.get("nom").toString()+","+map.get("ape_pat").toString()+","+map.get("alumno").toString()+","+map.getString("nivel")+","+map.getString("grado")+", , , , , , , , ");		     
			}
			
			Row row = sheet.createRow(++filaInicial);

			inputStream.close();

			FileOutputStream outputStream = new FileOutputStream(nuevoArchivo);
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return nuevoArchivo;
	}
}

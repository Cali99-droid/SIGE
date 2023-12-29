package com.tesla.frmk.util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode39;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tesla.frmk.sql.Row;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
 
/**
 * https://memorynotfound.com/add-barcode-qrcode-pdf-itext/
 * @author MV
 *
 */
public class CodigoBarrasUtil{
    public static final String DEST = "C:/Users/Deister/Documents/michael/personal/impresora/barcode39";
 
    public static void main(String[] args) throws IOException,
            DocumentException {
    	
    	DateFormat formatter = new SimpleDateFormat("dd-HH-mm-ss");
        String today = formatter.format(new Date());
        

        
        float[] _margenes = new float[]{0,5,2,10};
        float _width = 134;
        float _fontSize = 8;
        
        List<Map<String, Object>> alumnos = new ArrayList<Map<String,Object>>();
        
        Map<String, Object> alumno = new HashMap<String, Object>();
        alumno.put("local", "PRINCIPAL");
        alumno.put("alumno", "VALLE LEON, MICHAEL");
        alumno.put("nombre", "MICHAEL PAUL");
        alumno.put("grado", "SECUNDARIA - PRIMERO - A");
        alumno.put("cod", "00000001-02");
        alumnos.add(alumno);
        alumno = new HashMap<String, Object>();
        alumno.put("local", "PRINCIPAL");
        alumno.put("alumno", "PINEDA SIFUENTES,");
        alumno.put("nombre","JOSE ISIDRO");
        alumno.put("grado", "SECUNDARIA - PRIMERO - A");
        alumno.put("cod", "00000001-03" );
        
        alumnos.add(alumno);
        /*
        alumno = new HashMap<String, Object>();
        alumno.put("alumno", " VALLE LEON, SAMUEL");
        alumno.put("grado", " 2018-SECUNDARIA-PRIMERO-A");
        alumno.put("cod", "00000001-04");

        alumnos.add(alumno);
        */
        InputStream is = new CodigoBarrasUtil().createPdf(_width,_margenes,_fontSize,alumnos);
        OutputStream outputStream = new FileOutputStream(DEST + today + ".pdf");

        IOUtils.copy(is, outputStream);
        outputStream.close();
        
    }
 
    public java.io.InputStream  createPdf(float _ancho,float[] _margenes, float _fontSize,List<Map<String,Object>> list) throws IOException, DocumentException {
    	
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
        
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(_ancho);
        table.setLockedWidth(true);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
       
        for (Map<String, Object> map: list) {
        
        	float  lineSpacing;
            lineSpacing = 10f; //5f
            
            PdfPCell cell = new PdfPCell(new Phrase(lineSpacing,map.get("local").toString(),FontFactory.getFont(FontFactory.COURIER, 8)));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setPadding(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase(lineSpacing,map.get("alumno").toString(),FontFactory.getFont(FontFactory.COURIER, _fontSize)));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setPadding(2);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase(lineSpacing,map.get("nombres").toString(),FontFactory.getFont(FontFactory.COURIER, _fontSize)));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setPadding(2);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase(lineSpacing,map.get("grado").toString(),FontFactory.getFont(FontFactory.COURIER, _fontSize)));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setPadding(2);
            table.addCell(cell);
            
            table.addCell(createBarcode(writer, map.get("cod").toString() ));
        }
        
        
        document.add(table);
        document.close();
        
        return new ByteArrayInputStream(out.toByteArray());

    }
    
 public java.io.InputStream  createPdfCarnet(float _ancho,float[] _margenes, float _fontSize,List<Map<String,Object>> list) throws IOException, DocumentException {
    	
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
        
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(_ancho);
        table.setLockedWidth(true);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
       
        for (Map<String, Object> map: list) {
        
        	float  lineSpacing;
            lineSpacing = 10f; //5f
            PdfPCell cell = new PdfPCell(new Phrase(lineSpacing,map.get("local").toString(),FontFactory.getFont(FontFactory.COURIER, 12)));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setPadding(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase(lineSpacing,map.get("alumno").toString(),FontFactory.getFont(FontFactory.COURIER, _fontSize)));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setPadding(2);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase(lineSpacing,map.get("nombre").toString(),FontFactory.getFont(FontFactory.COURIER, _fontSize)));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setPadding(2);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase(lineSpacing,map.get("grado").toString(),FontFactory.getFont(FontFactory.COURIER, _fontSize)));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setPadding(2);
            table.addCell(cell);
            
            table.addCell(createBarcode(writer, map.get("cod").toString() ));
        }
        
        
        document.add(table);
        document.close();
        
        return new ByteArrayInputStream(out.toByteArray());

    }
 
 
 public java.io.InputStream  createPdfApoderado(float _ancho,float[] _margenes, float _fontSize,List<Row> list) throws IOException, DocumentException {
    	
        float left = _margenes[0];
        float right = _margenes[1];
        float top = _margenes[2];
        float bottom = _margenes[3];
        
        Document document = new Document();
        Rectangle one = new Rectangle(144,108);
        document.setPageSize(one);
        document.setMargins(left, right, top, bottom);
        /*
        Document document = new Document(PageSize.A4, left, right, top, bottom);
        */
        ByteArrayOutputStream out = new ByteArrayOutputStream();            

        PdfWriter writer = PdfWriter.getInstance(document, out);
        document.open();
        document.setMargins(left, right, 0, bottom);
        
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(_ancho);
        table.setLockedWidth(true);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
       
        for (Map<String, Object> map: list) {
        
        	float  lineSpacing;
            lineSpacing = 10f; //5f
            
            
            PdfPCell cell = new PdfPCell(new Phrase(lineSpacing,map.get("familiar").toString() ,FontFactory.getFont(FontFactory.COURIER, 9)));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setPadding(1);
            cell.setPaddingTop(5);
            table.addCell(cell);
            
             cell = new PdfPCell(new Phrase(lineSpacing, map.get("nombre").toString(),FontFactory.getFont(FontFactory.COURIER, 9)));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setPadding(1);
            //cell.setPaddingTop(1);
            table.addCell(cell);
            
            
            Font SUBFONT = new Font(Font.getFamily("TIMES_ROMAN"), 9,    Font.BOLD|Font.UNDERLINE);

            cell = new PdfPCell(new Phrase("DATOS DE ACCESO", SUBFONT));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setPadding(1);
            cell.setPaddingTop(2);
            cell.setPaddingBottom(2);

            table.addCell(cell);
            
            
            cell = new PdfPCell(new Phrase("Web: www.login.ae.edu.pe",FontFactory.getFont(FontFactory.COURIER, 9)));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setPadding(1);
            cell.setPaddingTop(2);
            cell.setPaddingBottom(2);

            table.addCell(cell);
            
            
            cell = new PdfPCell(new Phrase(lineSpacing,"Usuario:" + map.get("nro_doc").toString(),FontFactory.getFont(FontFactory.COURIER, 9)));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setPadding(1);//espacio
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase(lineSpacing,"Clave  :" + map.get("pass").toString(),FontFactory.getFont(FontFactory.COURIER, 9)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPadding(1);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setPaddingTop(1);
            cell.setPaddingBottom(2);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(lineSpacing,"Seleccionar:Familiar" ,FontFactory.getFont(FontFactory.COURIER, 8)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //cell.setPaddingTop(3);
            cell.setPadding(1);
            cell.setBorder(Rectangle.NO_BORDER);
            
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(lineSpacing,map.get("grado").toString(),FontFactory.getFont(FontFactory.COURIER, 8)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            //cell.setPaddingTop(3);
            cell.setPadding(1);
            cell.setBorder(Rectangle.NO_BORDER);
            
            table.addCell(cell);

            
        }
        
        
        document.add(table);
        document.close();
        
        return new ByteArrayInputStream(out.toByteArray());

    }
 
    public static PdfPCell createBarcode(PdfWriter writer, String code) throws DocumentException, IOException {
    	Barcode39 barcode39 = new Barcode39();
    	barcode39.setCode(code);
        PdfPCell cell = new PdfPCell(barcode39.createImageWithBarcode(writer.getDirectContent(), BaseColor.BLACK, BaseColor.GRAY), true);
        cell.setBorder(Rectangle.NO_BORDER);

        cell.setPadding(3);
        cell.setPaddingTop(6);
        return cell;
    }
    
}
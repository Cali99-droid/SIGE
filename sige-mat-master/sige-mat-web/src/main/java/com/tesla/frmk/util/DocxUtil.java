package com.tesla.frmk.util;



import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.XmlOptions;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.slf4j.LoggerFactory;

import com.sige.core.dao.SQLUtilImpl;

public class DocxUtil {
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(DocxUtil.class);
	/**
	 * 
	 * @param plantilla
	 * @param map Parametros para reemplazar
	 * @return
	 */
	public static String generate(String carpeta,String plantilla,Map<String,String> map) throws Exception{
		logger.debug(carpeta + plantilla);
		XWPFDocument doc = new XWPFDocument(OPCPackage.open(carpeta + plantilla));
		for (XWPFParagraph p : doc.getParagraphs()) {
		    List<XWPFRun> runs = p.getRuns();
		    if (runs != null) {
		        for (XWPFRun r : runs) {
		            String text = r.getText(0);
		            
		            for (Map.Entry<String, String> pair : map.entrySet()) {
		            	logger.debug(pair.getKey() + "-" + pair.getValue());
		                //i += pair.getKey() + pair.getValue();
		                if (text != null && text.contains(pair.getKey())) {
			                text = text.replace(pair.getKey(), pair.getValue());
			                r.setText(text, 0);
			            }
		            }
		            
		            /*
		            
		            if (text != null && text.contains("ANIO")) {
		                text = text.replace("ANIO", "2017");
		                r.setText(text, 0);
		            }
		            if (text != null && text.contains("APODERADO")) {
		                text = text.replace("APODERADO", "MICHAEL VALLE LEON");
		                r.setText(text, 0);
		            }*/
		        }
		    }
		}

		SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String fecha = dt.format(new Date());
        
		plantilla = plantilla.replace(".docx",fecha +".docx");
		String nuevoArchivo = carpeta + "tmp/" + plantilla;
		doc.write(new FileOutputStream(nuevoArchivo));
		
		return nuevoArchivo;
	}
	
	/**
	 * 
	 * @param plantilla
	 * @param map Parametros para reemplazar
	 * @return
	 */
	public static InputStream generateInputStream(String carpeta,String plantilla,Map<String,String> map) throws Exception{
		logger.debug(carpeta + plantilla);
		XWPFDocument doc = new XWPFDocument(OPCPackage.open(carpeta + plantilla));
		for (XWPFParagraph p : doc.getParagraphs()) {
		    List<XWPFRun> runs = p.getRuns();
		    if (runs != null) {
		        for (XWPFRun r : runs) {
		            String text = r.getText(0);
		            
		            for (Map.Entry<String, String> pair : map.entrySet()) {
		            	logger.debug(pair.getKey() + "-" + pair.getValue());
		                //i += pair.getKey() + pair.getValue();
		                if (text != null && text.contains(pair.getKey())) {
			                text = text.replace(pair.getKey(), pair.getValue());
			                r.setText(text, 0);
			            }
		            }
		            
		            /*
		            
		            if (text != null && text.contains("ANIO")) {
		                text = text.replace("ANIO", "2017");
		                r.setText(text, 0);
		            }
		            if (text != null && text.contains("APODERADO")) {
		                text = text.replace("APODERADO", "MICHAEL VALLE LEON");
		                r.setText(text, 0);
		            }*/
		        }
		    }
		}

		SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String fecha = dt.format(new Date());
        
		plantilla = plantilla.replace(".docx",fecha +".docx");
		String nuevoArchivo = carpeta + "tmp/" + plantilla;
		doc.write(new FileOutputStream(nuevoArchivo));
		return new FileInputStream(nuevoArchivo);
		
		//return nuevoArchivo;
	}
	
	public static void main1(String arg[]) throws Exception{
		XWPFDocument doc = new XWPFDocument(OPCPackage.open("D://proyectos//colegio//Plantilla.docx"));
		for (XWPFParagraph p : doc.getParagraphs()) {
		    List<XWPFRun> runs = p.getRuns();
		    if (runs != null) {
		        for (XWPFRun r : runs) {
		            String text = r.getText(0);
		            if (text != null && text.contains("ANIO")) {
		                text = text.replace("ANIO", "2017");
		                r.setText(text, 0);
		            }
		            if (text != null && text.contains("APODERADO")) {
		                text = text.replace("APODERADO", "MICHAEL VALLE LEON");
		                r.setText(text, 0);
		            }
		        }
		    }
		}
		//doc.write(new FileOutputStream("D://proyectos//colegio//Plantilla3.docx"));
	}
	
	public static void appendBody(CTBody src, CTBody append) throws Exception {
	    XmlOptions optionsOuter = new XmlOptions();
	    optionsOuter.setSaveOuter();
	    String appendString = append.xmlText(optionsOuter);
	    String srcString = src.xmlText();
	    String prefix = srcString.substring(0,srcString.indexOf(">")+1);
	    String mainPart = srcString.substring(srcString.indexOf(">")+1,srcString.lastIndexOf("<"));
	    String sufix = srcString.substring( srcString.lastIndexOf("<") );
	    String addPart = appendString.substring(appendString.indexOf(">") + 1, appendString.lastIndexOf("<"));
	    CTBody makeBody = CTBody.Factory.parse(prefix+mainPart+addPart+sufix);
	    src.set(makeBody);
	}
	
	//aca hacemos nuestra prueba 
	public static void main(String arg[]) throws Exception{
		InputStream src1 = new FileInputStream("D://plantillas//CartaPago18-09-2018-10-59-10.docx");
		InputStream src2 = new FileInputStream("D://plantillas//CartaPago18-09-2018-10-59-08.docx");
		InputStream src3 = new FileInputStream("D://plantillas//CartaPago17-09-2018-04-42-30.docx");
		
		OPCPackage src1Package = OPCPackage.open(src1);
		XWPFDocument src1Document = new XWPFDocument(src1Package);        
	    CTBody src1Body = src1Document.getDocument().getBody();
	    
	    
	    OPCPackage src2Package = OPCPackage.open(src2);
	    XWPFDocument src2Document = new XWPFDocument(src2Package);
	    CTBody src2Body = src2Document.getDocument().getBody();

	  //iteracion 1
	    appendBody(src1Body, src2Body);//uno 1 y 2
	    
	    
	    OPCPackage src3Package = OPCPackage.open(src3);
	    XWPFDocument src3Document = new XWPFDocument(src3Package);
	    CTBody src3Body = src3Document.getDocument().getBody();
	    
	    //iteracion 2
	    appendBody(src1Body, src3Body);//uno 2 y 3
	    
	    //solo dos appendBody, por q son 3 input.. 
	    //como loves? mmm masomenos lo q tengo q pasar es el input entoncs? aqui primero seria el bucle no?
	    
	    OutputStream out = new FileOutputStream("D://plantillas//CartaFINAL6.docx");//prueba nro 2
	    src1Document.write(out);
		
	}

	
}

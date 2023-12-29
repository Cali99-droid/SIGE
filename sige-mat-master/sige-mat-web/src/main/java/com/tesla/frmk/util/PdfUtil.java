package com.tesla.frmk.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.tesla.colegio.model.bean.ContratoBean;
import com.tesla.colegio.model.bean.ContratoHijoBean;
import com.tesla.colegio.util.Constante;

public class PdfUtil {

	public static void main(String args[]) throws Exception {

		String SRC = "E:/pruebas/Contrato_001.pdf";
		String DEST = "E:/pruebas/Contrato_001-1.pdf";

		new File(DEST).delete();

		ContratoBean contrato = new ContratoBean();
		contrato.setDistrito("HUARMEY");
		contrato.setDomicilio("AVA SALVAJETRRY NRO MAZ 3 LOTE X POR LA ESQUINA");
		contrato.setNombre("JUAN MANUEL CRISTOMO DE LA CRUZ SANTA ESTEVAN");
		contrato.setNro_doc("10857529");
		contrato.setNumero("2019-001-000001");
		contrato.setProvincia("ANCASH");

		contrato.setPoder_nombre("MIGUEL SANCHEZ CARRION");
		contrato.setPoder_nro_doc("292929292");

		
		ContratoHijoBean hijo = new ContratoHijoBean();
		hijo.setNivel("PRIMARIA -4TO A");
		hijo.setMensualidad("S/ 250.0");
		hijo.setNombre("LOPEZ CASTRO, JUAN");
		
		contrato.getHijos().add(hijo);

		ContratoHijoBean hijo2 = new ContratoHijoBean();
		hijo2.setNivel("SECUNDARIA -5TO A");
		hijo2.setMensualidad("S/ 280.0");
		hijo2.setNombre("LOPEZ CASTRO, PEDRO");
		contrato.getHijos().add(hijo2);
		
		ContratoHijoBean hijo3 = new ContratoHijoBean();
		hijo3.setNivel("SECUNDARIA -6TO A");
		hijo3.setMensualidad("S/ 280.0");
		hijo3.setNombre("LOPEZ CASTRO, MONICA");
		contrato.getHijos().add(hijo3);
		
		new PdfUtil().agregarTexto2Pdf(SRC, DEST, contrato);

	}

	public void agregarTexto2Pdf(String src, String dest, ContratoBean contrato) throws IOException, DocumentException {
		PdfReader pdfReader = new PdfReader(src);

		PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(dest));

		PdfContentByte canvas = pdfStamper.getOverContent(1);

		ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(contrato.getNumero(), new Font(Font.FontFamily.TIMES_ROMAN,12, Font.BOLD)), 451, 809, 0);

		Font smallItalic = new Font(Font.FontFamily.TIMES_ROMAN,8, Font.NORMAL);
		Phrase phraseNombre = new Phrase(contrato.getNombre(), smallItalic);
		ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phraseNombre, 105, 744, 0);

		Phrase phraseNro_doc= new Phrase(contrato.getNro_doc(), smallItalic);
		ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phraseNro_doc, 500, 744, 0);

		Phrase phraseDom = new Phrase(contrato.getDomicilio(), smallItalic);
		ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phraseDom, 100, 744-10, 0);
		
		Phrase phraseDis = new Phrase(contrato.getDistrito(), smallItalic);
		ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phraseDis, 345, 744-10, 0);
		
		
		Phrase phraseProv = new Phrase(contrato.getProvincia(), smallItalic);
		ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phraseProv, 462, 744-10, 0);
		
		
		Phrase phrasePoderNombre = new Phrase(contrato.getPoder_nombre(), smallItalic);
		ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrasePoderNombre, 325, 744-20, 0);
		
		Phrase phrasePoderNroDoc = new Phrase(contrato.getPoder_nro_doc(), smallItalic);
		ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrasePoderNroDoc, 45, 744-29, 0);
		
	
		PdfContentByte canvas2 = pdfStamper.getOverContent(2);
		int salto = 0;
		
		for (ContratoHijoBean hijo : contrato.getHijos()) {
			
			int y = 403 - salto;
 
			
			Font small = new Font(Font.FontFamily.TIMES_ROMAN,9, Font.NORMAL);
			Phrase phrase = new Phrase(hijo.getNombre() , small);
			ColumnText.showTextAligned(canvas2, Element.ALIGN_LEFT, phrase , 88, y, 0);
		
	 
			Phrase phraseNivel = new Phrase(hijo.getNivel() , small);
			ColumnText.showTextAligned(canvas2, Element.ALIGN_LEFT, phraseNivel , 318, y, 0);
		
 
			Phrase phraseMes = new Phrase(hijo.getMensualidad() , small);
			ColumnText.showTextAligned(canvas2, Element.ALIGN_LEFT, phraseMes , 462, y, 0);
		
			
			salto = salto + 20;			
		}
		
		Calendar cal = Calendar.getInstance();
		int dia = cal.get(Calendar.DAY_OF_MONTH);
		int mes = cal.get(Calendar.MONTH);
		
		Phrase phraseDia = new Phrase(String.valueOf(dia) , smallItalic);
		ColumnText.showTextAligned(canvas2, Element.ALIGN_LEFT, phraseDia, 193,153, 0);
		
		Phrase phraseMes = new Phrase(Constante.MES[mes] , smallItalic);
		ColumnText.showTextAligned(canvas2, Element.ALIGN_LEFT, phraseMes, 270, 153, 0);
		
		Font nromal = new Font(Font.FontFamily.HELVETICA,7, Font.BOLD);
		
		Phrase phraseFirma = new Phrase(contrato.getNombre(), nromal);
		ColumnText.showTextAligned(canvas2, Element.ALIGN_CENTER, phraseFirma,420, 98, 0);
		
		
		pdfStamper.close();

		pdfReader.close();

	}

}

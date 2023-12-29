package com.tesla.frmk.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.tesla.frmk.sql.Row;

public class WordToPDFUtil {
	/**
	 * ayuda
	 * https://stackoverflow.com/questions/47895935/converting-html-to-pdf-using-itext
	 */

	private static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd-HmsS");

	private String DIR = "E:/desarrollo/pdf/";

	public static void main(String args[]) throws Exception {

		String _DIR = "C:/plantillas/progr_anual/";
		Document document = new Document(PageSize.A4.rotate(), 100f, 100f, 35f, 30f);
		
		String outPutfile = _DIR + "tmp/" + FORMAT.format(new Date()) + ".pdf";

		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outPutfile));
		document.open();

		CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
		HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
		htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());

		CssFile cssFile = XMLWorkerHelper.getCSS(new FileInputStream(_DIR + "style.css"));
		cssResolver.addCss(cssFile);

		htmlContext.setImageProvider(new AbstractImageProvider() {
			public String getImageRootPath() {
				return _DIR ;
			}
		});

		PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
		HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
		CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);

		XMLWorker worker = new XMLWorker(css, true);
		XMLParser p = new XMLParser(worker);
		p.parse(new FileInputStream(_DIR + "error.html"));

		document.close();

		//new WordToPDFUtil().createPdf(id_usr, programacionAnual);
		// new WordToPDFUtil().replaceContent();
	}

	public String  createPdf(String rutacARPETA,Integer id_usr, Row programacionAnual) throws IOException, DocumentException, Exception {

		DIR = rutacARPETA + "progr_anual/";
		String pdf_1_Informacion_general = createPdf(id_usr, programacionAnual, "1_informacion_general.html");
		String pdf_2_calendarizacion = createPdf(id_usr, programacionAnual, "2_calendarizacion.html");
		String pdf_3_organizacion_unidades = createPdf(id_usr, programacionAnual, "3_organizacion_unidades.html");
		//String pdf_4_unidades_capacidades = createPdf(id_usr, programacionAnual, "4_unidades_capacidades.html");
		
		String[] pdfs = new String[]{pdf_1_Informacion_general,pdf_2_calendarizacion,pdf_3_organizacion_unidades};
		
		String pdfFinal = merge(id_usr, pdfs);
		
		return pdfFinal;
	}
	
	public String  createPdfCarnet(String rutacARPETA, Row carnet, String tipo) throws IOException, DocumentException, Exception {
		if(tipo.equals("A"))
			DIR = rutacARPETA + "carnetAcademia/";
		else if (tipo.equals("C") || tipo.equals("V"))
			DIR = rutacARPETA + "carnetColegio/";
		String pdf_1_Informacion_general = createPdf2(carnet, "carnet.html");
		/*String pdf_2_calendarizacion = createPdf(id_usr, programacionAnual, "2_calendarizacion.html");
		String pdf_3_organizacion_unidades = createPdf(id_usr, programacionAnual, "3_organizacion_unidades.html");*/
		//String pdf_4_unidades_capacidades = createPdf(id_usr, programacionAnual, "4_unidades_capacidades.html");
		
		//String[] pdfs = new String[]{pdf_1_Informacion_general};
		
		//String pdfFinal = merge(id_usr, pdfs);
		
		return pdf_1_Informacion_general;
	}
	
	public String  createPdfCarnetAlu(String rutacARPETA, Row carnet, String cod) throws IOException, DocumentException, Exception {

		DIR = rutacARPETA + "carnetColegio/";
		String pdf_1_Informacion_general = createPdf2Alu(carnet, "carnet.html",cod);
		/*String pdf_2_calendarizacion = createPdf(id_usr, programacionAnual, "2_calendarizacion.html");
		String pdf_3_organizacion_unidades = createPdf(id_usr, programacionAnual, "3_organizacion_unidades.html");*/
		//String pdf_4_unidades_capacidades = createPdf(id_usr, programacionAnual, "4_unidades_capacidades.html");
		
		//String[] pdfs = new String[]{pdf_1_Informacion_general};
		
		//String pdfFinal = merge(id_usr, pdfs);
		
		return pdf_1_Informacion_general;
	}

	/**
	 * Crear el pdf para Curso unidad
	 * @param rutacARPETA
	 * @param id_usr
	 * @param cursoUnidadRow
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 * @throws Exception
	 */
	public String  createPdfCursoUnidad(String rutacARPETA,Integer id_usr, Row cursoUnidadRow) throws IOException, DocumentException, Exception {

		DIR = rutacARPETA + "curso_unidad/";
		
		String pdf_1_unidad_aprendizaje = createPdf(PageSize.A4,id_usr, cursoUnidadRow, "1_unidad_aprendizaje.html");
		String pdf_2_sesiones= createPdf(PageSize.A4,id_usr, cursoUnidadRow, "2_sesiones.html");
		
		String[] pdfs = new String[]{pdf_1_unidad_aprendizaje,pdf_2_sesiones};
		
		String pdfFinal = merge(PageSize.A4, id_usr, pdfs);
		
		return pdfFinal;
	}
	
	public String createPdf(Integer id_usr, Row programacionAnual, String plantilla)
			throws IOException, DocumentException, Exception {
		// Document document = new Document();
		 
		return  createPdf(PageSize.A4.rotate(), id_usr, programacionAnual, plantilla);
	}

	public String createPdf(Rectangle rectangle, Integer id_usr, Row programacionAnual, String plantilla)
			throws IOException, DocumentException, Exception {
		// Document document = new Document();

		String htmlTMP = replaceContent(plantilla, programacionAnual);

		Document document = new Document(rectangle, 50f, 50f, 35f, 30f);
		
		String outPutfile = DIR + "tmp/" + id_usr + FORMAT.format(new Date()) + ".pdf";

		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outPutfile));
		document.open();

		CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
		HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
		htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());

		CssFile cssFile = XMLWorkerHelper.getCSS(new FileInputStream(DIR + "style.css"));
		cssResolver.addCss(cssFile);

		htmlContext.setImageProvider(new AbstractImageProvider() {
			public String getImageRootPath() {
				return DIR ;
			}
		});

		PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
		HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
		CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);

		XMLWorker worker = new XMLWorker(css, true);
		XMLParser p = new XMLParser(worker);
		p.parse(new FileInputStream(htmlTMP));

		document.close();

		return outPutfile;
	}
	
	public String createPdf2( Row carnet, String plantilla)
			throws IOException, DocumentException, Exception {
		// Document document = new Document();
		 
		return  createPdf2(PageSize.A4.rotate(), carnet, plantilla);
	}
	
	public String createPdf2Alu( Row carnet, String plantilla, String cod)
			throws IOException, DocumentException, Exception {
		// Document document = new Document();
		 
		return  createPdf2Alu(PageSize.A4.rotate(), carnet, plantilla,cod);
	}

	public String createPdf2(Rectangle rectangle, Row carnet, String plantilla)
			throws IOException, DocumentException, Exception {
		 Document document = new Document();
		// String outPutfile = DIR + "tmp/" + id_usr + FORMAT.format(new Date()) + ".pdf";

		String htmlTMP = replaceContent2(plantilla, carnet);
		/*String url = htmlTMP;
		File outs = new File(outPutfile);

		URL u = new URL(url);
		URLConnection uc = u.openConnection();
		BufferedInputStream is = new BufferedInputStream(uc.getInputStream());
		BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(outs));

		byte[] b = new byte[8 * 1024];
		int read = 0;
		while ((read = is.read(b)) > -1) {
			bout.write(b, 0, read);
		}
		bout.flush();
		bout.close();
		is.close();
*/
	
		
		/*  Document document = new Document(PageSize.A4); 
	      PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outPutfile)); 
	      document.open(); 
	      document.addAuthor("Persona creadora"); 
	      document.addCreator("Software generador"); 
	      document.addCreationDate(); 
	      document.addTitle("Titulo del documento"); 
	      HTMLWorker htmlWorker = new HTMLWorker(document); 
	      String str = htmlTMP;
	      htmlWorker.parse(new StringReader(str)); 
	      document.close(); */
		//descarga();
		//return htmlTMP;

		/*PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outPutfile));
		document.open();

		CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
		HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
		htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());

		CssFile cssFile = XMLWorkerHelper.getCSS(new FileInputStream(DIR + "style.css"));
		CssFile cssFile2 = XMLWorkerHelper.getCSS(new FileInputStream(DIR + "normalize.css"));
		cssResolver.addCss(cssFile);
		cssResolver.addCss(cssFile2);


		htmlContext.setImageProvider(new AbstractImageProvider() {
			public String getImageRootPath() {
				return DIR ;
			}
		});

		PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
		HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
		CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);

		XMLWorker worker = new XMLWorker(css, true);
		XMLParser p = new XMLParser(worker);
		p.parse(new FileInputStream(htmlTMP));

		document.close();*/

		return htmlTMP;
		
	}
	
	public String createPdf2Alu(Rectangle rectangle, Row carnet, String plantilla, String cod)
			throws IOException, DocumentException, Exception {
		 Document document = new Document();
		// String outPutfile = DIR + "tmp/" + id_usr + FORMAT.format(new Date()) + ".pdf";

		String htmlTMP = replaceContentAlu(plantilla, carnet, cod);
		

		return htmlTMP;
		
	}
	
	
	public void descarga()
			throws IOException, DocumentException, Exception {
		// Document document = new Document();

		
		//URL url = new URL(
			//	"C://plantillas//carnetColegio//tmp//carnet-2022-03-11-18473421");

		URL url = new URL("C://plantillas//carnetColegio//tmp//carnet-2022-03-11-18473421.html");
		String fileName = url.getFile();
		String destName = "./figures" + fileName.substring(fileName.lastIndexOf("/"));
		System.out.println(destName);
	 
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destName);
	 
		byte[] b = new byte[2048];
		int length;
	 
		while ((length = is.read(b)) != -1) {
			os.write(b);
		}
	 
		is.close();
		os.close();
		
		
		//String outPutfile = DIR + "tmp/" + id_usr + FORMAT.format(new Date()) + ".pdf";

		/*PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outPutfile));
		document.open();

		CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
		HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
		htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());

		CssFile cssFile = XMLWorkerHelper.getCSS(new FileInputStream(DIR + "style.css"));
		CssFile cssFile2 = XMLWorkerHelper.getCSS(new FileInputStream(DIR + "normalize.css"));
		cssResolver.addCss(cssFile);
		cssResolver.addCss(cssFile2);


		htmlContext.setImageProvider(new AbstractImageProvider() {
			public String getImageRootPath() {
				return DIR ;
			}
		});

		PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
		HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
		CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);

		XMLWorker worker = new XMLWorker(css, true);
		XMLParser p = new XMLParser(worker);
		p.parse(new FileInputStream(htmlTMP));

		document.close();

		return outPutfile;
		//return htmlTMP;*/
	}


	
	public String replaceContent(String plantilla, Row row) throws Exception {
		VelocityEngine velocity = new VelocityEngine();

		try {
			// Modificado el 2014.

			Properties p = new Properties();
			p.setProperty("resource.loader", "file");
			p.setProperty("file.resource.loader.class",
					"org.apache.velocity.runtime.resource.loader.FileResourceLoader");
			p.setProperty("file.resource.loader.path", DIR );
			p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogChute");

			p.setProperty("file.resource.loader.cache", "false");
			p.setProperty("file.resource.loader.modificationCheckInterval", "0");

			velocity.init(p);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("No se puede leer la plantilla del documento dinamico");
		}

		Template template;

		VelocityContext context = new VelocityContext();

		context.put("programacionAnual", row);

		StringWriter writer = new StringWriter();

		template = velocity.getTemplate(plantilla);

		template.merge(context, writer);

		String htmlTmp = DIR + "tmp/" + plantilla.replace(".html", "-") + FORMAT.format(new Date()) + ".html";
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(htmlTmp, true)));
		out.println(writer.toString());
		out.close();

		return htmlTmp;
	}
	
	public String replaceContent2(String plantilla, Row row) throws Exception {
		VelocityEngine velocity = new VelocityEngine();

		try {
			// Modificado el 2014.

			Properties p = new Properties();
			p.setProperty("resource.loader", "file");
			p.setProperty("file.resource.loader.class",
					"org.apache.velocity.runtime.resource.loader.FileResourceLoader");
			p.setProperty("file.resource.loader.path", DIR );
			p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogChute");

			p.setProperty("file.resource.loader.cache", "false");
			p.setProperty("file.resource.loader.modificationCheckInterval", "0");

			velocity.init(p);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("No se puede leer la plantilla del documento dinamico");
		}

		Template template;

		VelocityContext context = new VelocityContext();

		context.put("listaAlumnos", row);

		StringWriter writer = new StringWriter();

		template = velocity.getTemplate(plantilla);

		template.merge(context, writer);

		//String htmlTmp = DIR + "tmp/" + plantilla.replace(".html", "-") + FORMAT.format(new Date()) + ".html";
		String htmlTmp = DIR +"/tmp/" + plantilla.replace(".html", "-")+FORMAT.format(new Date())+".html";
		//String htmlTmp = "C:/plantillas/carnetColegio/tmp/" + plantilla.replace(".html", "-")+FORMAT.format(new Date())+".html";
		//String htmlTmp = "C:/plantillas/carnetColegio/tmp/" + plantilla.replace(".html", "-")+FORMAT.format(new Date())+".html";
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(htmlTmp, true)));
		out.println(writer.toString());
		out.close();
		

		return htmlTmp;
	}
	
	public String replaceContentAlu(String plantilla, Row row, String cod) throws Exception {
		VelocityEngine velocity = new VelocityEngine();

		try {
			// Modificado el 2014.

			Properties p = new Properties();
			p.setProperty("resource.loader", "file");
			p.setProperty("file.resource.loader.class",
					"org.apache.velocity.runtime.resource.loader.FileResourceLoader");
			p.setProperty("file.resource.loader.path", DIR );
			p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogChute");

			p.setProperty("file.resource.loader.cache", "false");
			p.setProperty("file.resource.loader.modificationCheckInterval", "0");

			velocity.init(p);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("No se puede leer la plantilla del documento dinamico");
		}

		Template template;

		VelocityContext context = new VelocityContext();

		context.put("listaAlumnos", row);

		StringWriter writer = new StringWriter();

		template = velocity.getTemplate(plantilla);

		template.merge(context, writer);

		//String htmlTmp = DIR + "tmp/" + plantilla.replace(".html", "-") + FORMAT.format(new Date()) + ".html";
		String htmlTmp = DIR+"tmp/" + plantilla.replace(".html", "-")+cod+FORMAT.format(new Date())+".html";
		//localString htmlTmp = "C:/plantillas/carnetColegio/tmp/" + plantilla.replace(".html", "-")+cod+FORMAT.format(new Date())+".html";
		//String htmlTmp = "C:/plantillas/carnetColegio/tmp/" + plantilla.replace(".html", "-")+FORMAT.format(new Date())+".html";
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(htmlTmp, true)));
		out.println(writer.toString());
		out.close();
		

		return htmlTmp;
	}



	public  String merge(Integer id_usr,String[] args) {

			return merge(PageSize.A4.rotate(), id_usr,args);
	}

	
	public  String merge(Rectangle rectangle, Integer id_usr,String[] args) {

		List<InputStream> list = new ArrayList<InputStream>();
		try {
			// Source pdfs
			for (String file : args) {
				list.add(new FileInputStream(new File(file)));
			}
			String outPutfile = DIR + "tmp/"+ id_usr + FORMAT.format(new Date()) + ".pdf";


			// Resulting pdf
			OutputStream out = new FileOutputStream(new File(outPutfile));

			doMerge(rectangle, list, out);
			
			return outPutfile;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (DocumentException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void doMerge(List<InputStream> list, OutputStream outputStream)
			throws DocumentException, IOException {

		 doMerge( PageSize.A4.rotate(), list, outputStream);
	}
	
	public static void doMerge( Rectangle rectangle, List<InputStream> list, OutputStream outputStream)
			throws DocumentException, IOException {
		//Document document = new Document(PageSize.A4.rotate(), 50f, 35f, 50f, 30f);
		Document document = new Document(rectangle, 50f, 35f, 50f, 30f);
		PdfWriter writer = PdfWriter.getInstance(document, outputStream);
		document.open();
		PdfContentByte cb = writer.getDirectContent();

		
		for (InputStream in : list) {
			PdfReader reader = new PdfReader(in);
			
			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
			    float pageHeight = reader.getPageSizeWithRotation(i).getHeight();
				document.newPage();
				PdfImportedPage page = writer.getImportedPage(reader, i);
				
				if(rectangle.equals(PageSize.A4))
					cb.addTemplate(page, 0, 0);
				else
					cb.addTemplate(page, 0, -1f, 1f, 0, 0, pageHeight);
				 

			}
		}

		outputStream.flush();
		document.close();
		outputStream.close();
	}

	
}

package com.tesla.frmk.util;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import com.tesla.colegio.model.bean.Adjunto;

import javax.mail.internet.MimeMessage;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
 

public class CorreoUtil {

	final static Logger logger = Logger.getLogger(CorreoUtil.class);

	 @Autowired
	 JavaMailSender mailSender;
	 

	 public void enviar(String asunto, String remitente, String destino, String contenido,String nombreAdjunto, byte[] adjunto, String correo, String colegio, String usuario, String pass)throws Exception{
		  
		    //System.out.println("asunto:" + asunto);

		 
	        JavaMailSender sender = getJavaMailSender(usuario, pass);

		    logger.debug("creo sender");

		    MimeMessage message = sender.createMimeMessage();
		    //message.setContent(testo, "text/html");

		    logger.debug("creo mensaje");
		    
		    MimeMessageHelper helper = new MimeMessageHelper(message, true);
		    
		    logger.debug("creo helper");
		    
		    helper.setTo(destino);
		    helper.setFrom(correo,colegio);
		    helper.setSubject(asunto);
		    
		    //System.out.println("destino:" + destino);
		    //System.out.println("contenido:" + contenido);
		    helper.setText(contenido,true);
		   
		    if(adjunto!=null)
		    	helper.addAttachment(nombreAdjunto, new ByteArrayResource(adjunto));
		    
	        sender.send(message);
	 }
	 
	 public void enviar(String asunto, String remitente, String destino, String contenido,List<Adjunto> adjuntos, String correo, String colegio, String usuario, String pass)throws Exception{
		  
		 //System.out.println("asunto:" + destino);

		 
	        JavaMailSender sender = getJavaMailSender(usuario, pass);

		    logger.debug("creo sender");

		    MimeMessage message = sender.createMimeMessage();
		    //message.setContent(testo, "text/html");

		    logger.debug("creo mensaje");
		    
		    MimeMessageHelper helper = new MimeMessageHelper(message, true);
		    
		    logger.debug("creo helper");
		    
		    helper.setTo(destino);
		    helper.setFrom(correo,colegio);
		    helper.setSubject(asunto);
		    
		    //System.out.println("destino:" + destino);
		    //System.out.println("contenido:" + contenido);
		    helper.setText(contenido,true);
		    if(adjuntos!=null){
				   for (Adjunto adjunto : adjuntos) {
					   
					   helper.addAttachment(adjunto.getNombre(), new ByteArrayResource(adjunto.getArchivo()));
				   }
				    
		    }	
			
	        sender.send(message);
	 }
	 
	 public void enviar(String asunto, String remitente, String destino, String contenido, String correo, String colegio, String usuario, String pass)throws Exception{
		 
		 enviar(asunto, remitente, destino, contenido,null,null, correo, colegio, usuario, pass);
		  
	 }

	 public void enviar(String asunto, String remitente, String destino[], String contenido, String correo, String colegio, String usuario, String pass)throws Exception{
		 /*
		 	SimpleMailMessage message = new SimpleMailMessage();
	        message.setSubject(asunto);
	        message.setText("<u>" + contenido + "</u>");
	        message.setTo(destino);
	        message.setFrom(remitente);
	        
	        JavaMailSender sender = getJavaMailSender();
	        */
		 //System.out.println("asunto:" + destino);

		 
	        JavaMailSender sender = getJavaMailSender(usuario, pass);

		    logger.debug("creo sender");

		    MimeMessage message = sender.createMimeMessage();
		    //message.setContent(testo, "text/html");

		    logger.debug("creo mensaje");
		    
		    MimeMessageHelper helper = new MimeMessageHelper(message, true);
		    
		    logger.debug("creo helper");
		    
		    helper.setTo(destino);
		    helper.setFrom(correo,colegio);
		    helper.setSubject(asunto);
		    
		    //System.out.println("destino:" + destino);
		    //System.out.println("contenido:" + contenido);
		    helper.setText(contenido,true);
		    
	        sender.send(message);
	 }
	 
	 /*public void enviar2(String asunto, String remitente, String destino, String contenido, String correo, String colegio)throws Exception{
		  
		    logger.debug("asunto:" + destino);

		 
	        JavaMailSender sender = getJavaMailSender();

		    logger.debug("creo sender");

		    MimeMessage message = sender.createMimeMessage();
		    //message.setContent(testo, "text/html");

		    logger.debug("creo mensaje");
		    
		    MimeMessageHelper helper = new MimeMessageHelper(message, true);
		    
		    logger.debug("creo helper");
		    
		    helper.setTo(destino);
		    helper.setFrom(correo,colegio);
		    helper.setSubject(asunto);
		    
		    logger.debug("destino:" + destino);
		    logger.debug("contenido:" + contenido);
		    helper.setText(contenido,true);
		   
		   //for (Adjunto adjunto : adjuntos) {
			   
			   helper.addAttachment("myDocument.pdf", new ClassPathResource("C:/plantillas/Comunicado.pdf"));
			   //message.addAttachment("myDocument.pdf", new ClassPathResource("doc/myDocument.pdf"));
		  // }
		    	
		    	
			
	        sender.send(message);
	 }*/
	 public JavaMailSender getJavaMailSender(String usuario, String pass)
	    {
	        JavaMailSenderImpl sender = new JavaMailSenderImpl();
	        /*
	        sender.setProtocol("smtp");
	        sender.setHost("smtp.gmail.com");
	        sender.setPort(587);
	        sender.setUsername("michael.valle77@gmail.com");
	        sender.setPassword("X");
*/
	        sender.setProtocol("smtp");
	       // sender.setHost("mail.ae.edu.pe");
	       // sender.setHost("smtp.gmail.com");
	        sender.setHost("smtp.gmail.com");
	        sender.setPort(465);
	       // sender.setUsername("donotreply@ae.edu.pe");
	       // sender.setPassword("&}oC4mD+fDAM");
	       // sender.setUsername("lina.leon@ae.edu.pe");
	      //  sender.setUsername("informes@ae.edu.pe");
	      //  sender.setUsername("consultas@ae.edu.pe");
	      //  sender.setUsername("noreply2@ae.edu.pe");
	        sender.setUsername(usuario);
	        //sender.setPassword("&}oC4mD+fDAM");
	        //sender.setPassword("Estefaniasunny");
	       // sender.setPassword("u6mW&fqhc;QG");
	       // sender.setPassword("2iQzp#AtLGk&");
	        sender.setPassword(pass);
	        Properties mailProps = new Properties();
	        mailProps.put("mail.smtp.auth", "true");
	        mailProps.put("mail.smtp.ssl.enable", "true"); 
	        mailProps.put("mail.smtp.socketFactory.class", "com.blogspot.lifeinide.AlwaysTrustSSLContextFactory");

	        //mailProps.put("mail.smtps.debug", "true");
	        mailProps.put("mail.smtp.socketFactory.port", 465);


	        sender.setJavaMailProperties(mailProps);

	        return sender;
	    }
	

}


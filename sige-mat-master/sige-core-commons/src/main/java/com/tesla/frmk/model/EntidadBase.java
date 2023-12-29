package com.tesla.frmk.model;

import java.util.Date;

 import org.springframework.stereotype.Component;
 
@Component
public class EntidadBase {

	//@Autowired
	//private TokenSeguridad tokenStrategy;
	 
	
	private String est;
	private Integer usr_ins = 0;
	private Integer usr_act = 0;//ahora todos los metodos deberiam setear el usuario.. para q no se caiga.. por defecto le pondre 0
	private Date fec_ins;
	private Date fec_act;
	private Object usuario;

	public void setUsuario(Object usuario) {
		this.usuario = usuario;
	}

 
	public int getUsrId(){
		/*
		if(tokenStrategy!=null)
			return tokenStrategy.getId();
		else return 0;
		*/
		return 1;
		
	}
	
	public String getEst(){
		return est;
	}
	
	public String getEstDesc(){
		if ("A".equals(this.est))
			return "Activo";
		else
			return "Inactivo";
	}
	
	public void setEst(String est) {
		this.est = est;
	}

	public boolean isActive(){
		return this.est==null || "A".equals(this.est);
	}

	public Integer getUsr_ins() {
		/*
		if(tokenStrategy!=null)
			return tokenStrategy.getId();
		else return usr_ins;
		*/
		return 1;
	}
	public void setUsr_ins(Integer usr_ins) {
		
		this.usr_ins = usr_ins;
	}
	
	public Integer getUsr_act() {
		/*
		if(tokenStrategy!=null)
			return tokenStrategy.getId();
		else
			return usr_act;
		*/
		return 1;
	}
	public void setUsr_act(Integer usr_act) {
		this.usr_act = usr_act;
	}
	public Date getFec_ins() {
		return fec_ins;
	}
	public void setFec_ins(Date fec_ins) {
		this.fec_ins = fec_ins;
	}
	public Date getFec_act() {
		return fec_act;
	}
	public void setFec_act(Date fec_act) {
		this.fec_act = fec_act;
	}
	


}

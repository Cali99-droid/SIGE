package com.sige.rest.request;

import java.io.Serializable;
import java.util.Date;
import java.util.List; 

public class CapacidadReq implements Serializable{
 
	private static final long serialVersionUID = -4470656952360885399L;
	private String id;
	private String text;
	private EstadoConfDesReq state;
	private List<DesemepenioReq> nodes;
	private String color;
	private String backColor;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public List<DesemepenioReq> getNodes() {
		return nodes;
	}
	public void setNodes(List<DesemepenioReq> nodes) {
		this.nodes = nodes;
	}
	public EstadoConfDesReq getState() {
		return state;
	}
	public void setState(EstadoConfDesReq state) {
		this.state = state;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getBackColor() {
		return backColor;
	}
	public void setBackColor(String backColor) {
		this.backColor = backColor;
	}
		
	
}

package com.sige.rest.request;

import java.io.Serializable;
import java.util.List;


public class UsuarioGrupoReq implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5605920645861114418L;
	private String idClassroom;
	private List<UsuarioClassroomEnrollReq> listUsuarios;

	public String getIdClassroom() {
		return idClassroom;
	}

	public void setIdClassroom(String idClassroom) {
		this.idClassroom = idClassroom;
	}

	public List<UsuarioClassroomEnrollReq> getListUsuarios() {
		return listUsuarios;
	}

	public void setListUsuarios(List<UsuarioClassroomEnrollReq> listUsuarios) {
		this.listUsuarios = listUsuarios;
	}

	@Override
	public String toString() {
		return "[{idClassroom:" + idClassroom + ", listUsuarios:[" + listUsuarios + "]}]";
	}

	
	


}

package com.sige.rest.request;

import java.io.Serializable;
import java.util.Date; 

public class EstadoConfDesReq implements Serializable{
 
	private static final long serialVersionUID = -4470656952360885399L;
	private boolean checked;
	private boolean expanded;
	private boolean disabled;
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public boolean isExpanded() {
		return expanded;
	}
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

		
}

package com.tesla.frmk.common.exceptions;

import com.sige.common.enums.EnumTipoException;

public class ControllerException extends Exception {
	private static final long serialVersionUID = -1504973846338622692L;
	private String errorMessage;
	private EnumTipoException tipoException=EnumTipoException.ERROR;

	public ControllerException() {
        super();
    }

	/**
	 * Constructor	
	 * 
	 * @param errorCode, Codigo de error especificado en ErrorCodeConstante
	 */
    public ControllerException(String errorMessage) {
    	super(errorMessage);
    	
    	this.errorMessage = errorMessage;
    }
    
    public ControllerException(String errorMessage,EnumTipoException tipoException) {
    	super(errorMessage);
    	
    	this.errorMessage = errorMessage;
    	this.tipoException = tipoException;
    }
    
    /**
     * Constructor
     * 
     * @param cause
     */
    public ControllerException(Throwable cause) {
        super(cause);
    }
 
    /**
     * Constructor
     * 
     * @param message
     * @param cause
     */
    public ControllerException(String message, Throwable cause) {
        super(message, cause);
        
        this.errorMessage = message;
    }


    public String getMessage() {
        return errorMessage;
    }

	public EnumTipoException getTipoException() {
		return tipoException;
	}

	public void setTipoException(EnumTipoException tipoException) {
		this.tipoException = tipoException;
	}

    
}


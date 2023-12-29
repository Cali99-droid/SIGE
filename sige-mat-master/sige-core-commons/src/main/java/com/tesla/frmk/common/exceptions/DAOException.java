package com.tesla.frmk.common.exceptions;

public class DAOException extends Exception {
	private static final long serialVersionUID = -1504973846338622692L;
	private String errorMessage;

	public DAOException() {
        super();
    }

	/**
	 * Constructor	
	 * 
	 * @param errorCode, Codigo de error especificado en ErrorCodeConstante
	 */
    public DAOException(String errorMessage) {
    	super(errorMessage);
    	
    	this.errorMessage = errorMessage;
    }

    
    /**
     * Constructor
     * 
     * @param cause
     */
    public DAOException(Throwable cause) {
        super(cause);
    }
 
    /**
     * Constructor
     * 
     * @param message
     * @param cause
     */
    public DAOException(String message, Throwable cause) {
        super(message, cause);
        
        this.errorMessage = message;
    }


    public String getMessage() {
        return errorMessage;
    }

    
}


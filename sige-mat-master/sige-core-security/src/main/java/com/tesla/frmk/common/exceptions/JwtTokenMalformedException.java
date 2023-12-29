package com.tesla.frmk.common.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * @author mvalle
 */
public class JwtTokenMalformedException extends AuthenticationException {


    public JwtTokenMalformedException(String msg) {
        super(msg);
    }
    
}

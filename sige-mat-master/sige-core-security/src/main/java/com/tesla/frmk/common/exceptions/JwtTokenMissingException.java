package com.tesla.frmk.common.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * @author mvalle
 */
public class JwtTokenMissingException extends AuthenticationException {

	public JwtTokenMissingException(String msg) {
        super(msg);
    }

}

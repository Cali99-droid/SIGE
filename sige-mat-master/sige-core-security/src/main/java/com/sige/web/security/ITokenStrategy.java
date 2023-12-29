package com.sige.web.security;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.UserDetails;

import com.tesla.colegio.model.UsuarioSeg;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

public abstract class ITokenStrategy {
	
	public static final String secretKey = "mySecretKey";
	public static final String headerKey = "Authorization";
	
	private final String HEADER = "Authorization";
	private final String PREFIX = "Bearer ";
	private final String SECRET = "mySecretKey";
	
	//public int getId()
	
	public boolean validateToken(final Claims claims, UserDetails userDetails) {
		String username = claims.getSubject();
		return username.equals(userDetails.getUsername());
	}
	
	public String getTokenFromRequest(HttpServletRequest request) {
		return request.getHeader(headerKey);
	}
/*
	public Claims parseToken(final String tokenStr) 
			throws ExpiredJwtException, MalformedJwtException, SignatureException {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(tokenStr).getBody();
	}*/
	
	public Claims parseToken(String tokenStr) {
		String jwtToken = tokenStr.replace(PREFIX, "");
		return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
	}

	
	public Date getExpirationDate(final Claims claims) {
		Date expiration = claims.getExpiration();
		return expiration;
	}
	
	public boolean tokenExpired(final Claims claims) {
	    final Date expiration = this.getExpirationDate(claims);
	    return expiration.before(new Date(System.currentTimeMillis()));
	}
	
	public String getUsername(HttpServletRequest request) {
		String username = null;
		try {
			final Claims claims = parseToken(getTokenFromRequest(request));
			username = claims.getSubject();
		} catch (Exception e) {
			
		}
		return username;
	}

	
	//public abstract String createToken(final UserDetails user, final Integer id_per);
	
	/* protected abstract methods */
	protected abstract String generateToken(Map<String, Object> claims);
    protected abstract Date generateExpirationDate();
}

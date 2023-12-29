package com.sige.web.security;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.tesla.colegio.model.UsuarioSeg;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.util.FechaUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenSeguridad extends ITokenStrategy {
	private final String HEADER = "Authorization";
	private final String PREFIX = "Bearer ";
	private final String SECRET = "mySecretKey";
	
	public static final String claimUseranme = "sub";
	public static final String claimCreated = "created";
	public static final String claimAdmin = "admin";
	public static final String id_per = "id_per";
	public static final String id  = "id";
	public static final String id_tra  = "id_tra";
	public static final String id_suc  = "id_suc";
	public static final String ini = "ini";
	public static final String nombres  = "nombres";
	public static final String roles  = "roles";

	@Autowired
	private HttpServletRequest  request;
	
	@Override
	public boolean validateToken(final Claims claims, UserDetails userDetails) {
		String username = claims.get("id_per") + "-" + claims.getSubject();
		return username.equals(userDetails.getUsername()) && !tokenExpired(claims);
	}
	
	public int getId(){
		String token = request.getHeader("Authorization");
		final Claims claims = parseToken(token);

		return Integer.parseInt(claims.get("id").toString());
	}

	@SuppressWarnings("unchecked")
	public UsuarioSeg getUsuarioSeg(){
		String token = request.getHeader("Authorization");
		final Claims claims = parseToken(token);

		UsuarioSeg usuarioSeg = new UsuarioSeg();
		usuarioSeg.setId_per( Integer.parseInt(claims.get(id_per).toString()));
		usuarioSeg.setId( Integer.parseInt(claims.get(id).toString()));
		usuarioSeg.setLogin(claims.get(claimUseranme).toString());
		usuarioSeg.setNombres(claims.get(nombres).toString());
		if(claims.get(id_tra)!=null)
		usuarioSeg.setId_tra(Integer.parseInt(claims.get(id_tra).toString()));
		if(claims.get(id_suc)!=null)
		usuarioSeg.setId_suc(Integer.parseInt(claims.get(id_suc).toString()));
		if(claims.get(ini)!=null)
			usuarioSeg.setIni(claims.get(ini).toString());

		Object roles_o = claims.get(roles);
		
		if (roles_o!=null){
			List<Integer> o2 = (ArrayList<Integer>) roles_o;
			usuarioSeg.setRoles(o2.toArray(new Integer[o2.size()]));
		}
			
		
		return usuarioSeg;
	}

	
	 
	
	public String createToken(final UsuarioSeg usuarioSeg) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(claimUseranme, usuarioSeg.getLogin());
		//claims.put(claimAdmin, user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_1")));
		claims.put(claimCreated, new Date(System.currentTimeMillis()));
		claims.put(id_per, usuarioSeg.getId_per());
		claims.put(id_suc, usuarioSeg.getId_suc());
		claims.put(id, usuarioSeg.getId());
		claims.put(nombres, usuarioSeg.getNombres());
		claims.put(roles, usuarioSeg.getRoles());
		claims.put(id_tra, usuarioSeg.getId_tra());
		claims.put(ini, usuarioSeg.getIni());

		return generateToken(claims);
	}
	
	
    protected String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
    
    protected Date generateExpirationDate() {
        //return new Date(System.currentTimeMillis() + Constante.MINUTOS_EXPIRACION *60* 1000);
        return new Date(System.currentTimeMillis() + Constante.MINUTOS_EXPIRACION *60* 1000* 60);

    }
    
    public Date getExpirationDateFromToken(String token) {
    	Claims claims =Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token.replace(PREFIX, "")).getBody();
    	return claims.getExpiration();
    	
    }
	public Claims validateToken(HttpServletRequest request) {
		String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
		return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
	}
    
	public String getJWTToken(UsuarioSeg usuarioSeg) {
		String secretKey = "mySecretKey";
		
		String claimUseranme = "sub";
		String claimCreated = "created";
		String claimAdmin = "admin";
		String id_per = "id_per";
		String id  = "id";
		String id_tra  = "id_tra";
		String id_suc  = "id_suc";
		String ini = "ini";
		String nombres  = "nombres";
		String roles  = "roles";
		
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");
		
		Map<String, Object> claims = new HashMap<>();
		claims.put(claimUseranme, usuarioSeg.getLogin());
		//claims.put(claimAdmin, user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_1")));
		claims.put(claimCreated, new Date(System.currentTimeMillis()));
		claims.put(id_per, usuarioSeg.getId_per());
		claims.put(id_suc, usuarioSeg.getId_suc());
		claims.put(id, usuarioSeg.getId());
		claims.put(nombres, usuarioSeg.getNombres());
		claims.put(roles, usuarioSeg.getRoles());
		claims.put(id_tra, usuarioSeg.getId_tra());
		claims.put(ini, usuarioSeg.getIni());
		claims.put("authorities",
				grantedAuthorities.stream()
						.map(GrantedAuthority::getAuthority)
						.collect(Collectors.toList()));
		Date fechaExpiracion = new Date(System.currentTimeMillis() + 60000*60);	//1 HORA
		claims.put("fecha_Expiracion", FechaUtil.toString(fechaExpiracion));

		String token = Jwts
				.builder()
				.setId("softtekJWT")
				.setSubject(usuarioSeg.getLogin())
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setClaims(claims)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(fechaExpiracion)
				.signWith(SignatureAlgorithm.HS512,secretKey.getBytes()).compact();

		return "Bearer " + token;
	}

}

package com.sige.spring.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sige.mat.dao.OpcionDAO;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.Opcion;
import com.tesla.colegio.model.UsuarioSeg;
import com.tesla.frmk.rest.util.AjaxResponseBody;

@Service
public class SeguridadService {
	

	@Autowired
	private TokenSeguridad tokenStrategy;


	//mvalle 2020-06-29 
	//@Autowired
	private AuthenticationManager authenticationManager;

	private OpcionDAO opcionDAO;

	/**
	 * Generar token
	 * @param usuarioSeg
	 * @return
	 * @throws AuthenticationException
	 */
	public String authenticateAndSignToken(UsuarioSeg usuarioSeg) throws AuthenticationException {
		
		String perfil_usuario = usuarioSeg.getId_per() + "-" + usuarioSeg.getLogin();
		//System.out.println(usuarioSeg);
		final Authentication authentication = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(perfil_usuario, usuarioSeg.getPassword()));
		
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_1" ));
		
		//final UserDetails user = userDetailsService.loadUserByUsername(username);
		return tokenStrategy.createToken(usuarioSeg);
	}
	

     public List<Opcion> obtenerOpcionesXUsuario(Integer id_usr) throws Exception{
             List<Opcion> lstEstructuraOrganizacional = opcionDAO.getListOpcionesXusuario(id_usr);

            //Padres Cabeceras
            List<Opcion> lstPadresCabeceras = (lstEstructuraOrganizacional
                    .stream()
                    .filter(n -> n.getId_opc() == null)
                    .collect(Collectors.toList())
                    .stream()
                    .sorted(Comparator.comparing(Opcion::getId))
                    .collect(Collectors.toList()));

            //Todos los hijos
            List<Opcion> lstHijosTodos = (lstEstructuraOrganizacional.stream()
                    .filter(n -> n.getId_opc() != null)
                    .collect(Collectors.toList())
                    .stream()
                    .sorted(Comparator.comparing(Opcion::getId))
                    .collect(Collectors.toList()));

            //Se setea hijos e hijos de los hijos a los padres
            lstHijosTodos.stream().forEach(hijos -> recursiveMethod(lstPadresCabeceras, lstHijosTodos));
            return lstPadresCabeceras;
        
    }

    
    private void recursiveMethod(List<Opcion> padres, List<Opcion> hijosAll) {
        padres.stream().forEach(fath -> {
            hijosAll.stream().forEach(todos -> {
                if (todos.getId_opc().intValue() == fath.getId().intValue()) {
                    List<Opcion> hijo = (fath.getOpcions() != null) ? fath.getOpcions() : new ArrayList<>();
                    if (!hijo.contains(todos)) {
                        hijo.add(todos);
                        Collections.sort(hijo, Comparator.comparing(Opcion::getId));
                        fath.setOpcion(hijo);
                    }
                }
            });
            if (fath.getOpcions() != null && fath.getOpcions().size() > 0) {
                recursiveMethod(fath.getOpcions(), hijosAll);
            }
        });
    }
	
//pendiente
 	private List<Opcion> getMenu(Integer id_rol) {

		AjaxResponseBody result = new AjaxResponseBody();

		UsuarioSeg usuario = tokenStrategy.getUsuarioSeg();

		List<Opcion> opciones = null;// opcionDAO.getListMenuDashBoard(usuario.getId_suc());// @TODO FALTA ROLES
		
		// 1er nivel
		List<Opcion> padres = new ArrayList<Opcion>();

		for (Opcion opcion : opciones) {
			if (opcion.getOpcion() != null && opcion.getOpcion().getId_opc() == null) {

				boolean padrenuevo = true;
				for (Opcion padre : padres) {
					if (opcion.getId_opc() == padre.getId())
						padrenuevo = false;
				}
				if (padrenuevo) {
					opcion.getOpcion().setNiv(1);
					padres.add(opcion.getOpcion());
					// opcion.setNiv(1);
				}
			}

			if (opcion.getOpcion() != null && opcion.getOpcion().getId_opc() != null) {
				boolean padrenuevo = true;
				for (Opcion padre : padres) {
					if (opcion.getOpcion().getId_opc() == padre.getId())
						padrenuevo = false;
				}
				if (padrenuevo) {
					opcion.getOpcion().getOpcion().setNiv(1);
					padres.add(opcion.getOpcion().getOpcion());
				}

			}
		}

		// 2do nivel
		for (Opcion padre : padres) {
			for (Opcion opcion : opciones) {
				if (padre.getId() == opcion.getId_opc()) {

					boolean padreTieneHijo = false;
					for (Opcion hijo : padre.getOpcions()) {
						if (hijo.getId() == opcion.getId())
							padreTieneHijo = true;
					}

					if (!padreTieneHijo) {
						Opcion opcion1 = new Opcion();
						opcion1.setId(opcion.getId());
						opcion1.setIcon(opcion.getIcon());
						opcion1.setUrl(opcion.getUrl());
						opcion1.setNom(opcion.getNom());
						opcion1.setTitulo(opcion.getTitulo());
						opcion1.setSubtitulo(opcion.getSubtitulo());
						opcion1.setNiv(2);
						padre.getOpcions().add(opcion1);
					}

				}

				if (opcion.getOpcion() != null && padre.getId() == opcion.getOpcion().getId_opc()) {

					boolean padreTieneHijo = false;
					for (Opcion hijo : padre.getOpcions()) {
						if (hijo.getId() == opcion.getId_opc())
							padreTieneHijo = true;
					}

					if (!padreTieneHijo) {
						Opcion opcion1 = new Opcion();
						opcion1.setId(opcion.getOpcion().getId());
						opcion1.setIcon(opcion.getOpcion().getIcon());
						opcion1.setUrl(opcion.getOpcion().getUrl());
						opcion1.setNom(opcion.getOpcion().getNom());
						opcion1.setTitulo(opcion.getOpcion().getTitulo());
						opcion1.setSubtitulo(opcion.getOpcion().getSubtitulo());
						opcion1.setNiv(2);
						padre.getOpcions().add(opcion1);
					}

				}

			}
		}

		// 3er nivel
		for (Opcion opcion : opciones) {
			if (opcion.getOpcion() != null && opcion.getOpcion().getId_opc() != null) {
				// 3er nivel
				// obtener padre e hijo
				Integer id_padre = opcion.getOpcion().getId_opc();
				Integer id_hijo = opcion.getOpcion().getId();

				// obtener padre
				for (Opcion padre : padres) {
					if (padre.getId() == id_padre) {
						// obtener hijo
						for (Opcion hijo : padre.getOpcions()) {
							if (hijo.getId() == id_hijo) {
								Opcion opcion1 = new Opcion();
								opcion1.setId(opcion.getId());
								opcion1.setIcon(opcion.getIcon());
								opcion1.setUrl(opcion.getUrl());
								opcion1.setNom(opcion.getNom());
								opcion1.setTitulo(opcion.getTitulo());
								opcion1.setSubtitulo(opcion.getSubtitulo());
								opcion1.setNiv(3);
								hijo.getOpcions().add(opcion1);
							}
						}

					}
				}
			}
		}

		// llenar los hijos a los padres
		/*
		 * for (Opcion padre : padres) { for (Opcion opcion : opciones ) { //hijo sin
		 * nieto if (opcion.getId()!=padre.getId() && opcion.getOpcion()!=null &&
		 * opcion.getOpcion().getId_opc()==null && opcion.getId_opc() == padre.getId()){
		 * opcion.setNiv(2);
		 * 
		 * Opcion opcion1 = new Opcion(); opcion1.setId(opcion.getId());
		 * opcion1.setIcon(opcion.getIcon()); opcion1.setUrl(opcion.getUrl());
		 * opcion1.setNom(opcion.getNom()); opcion1.setTitulo(opcion.getTitulo());
		 * opcion1.setSubtitulo(opcion.getSubtitulo()); opcion1.setNiv(2);
		 * padre.getOpcions().add(opcion1); }
		 * 
		 * //nieto if (opcion.getId()!=padre.getId() && opcion.getOpcion()!=null &&
		 * opcion.getOpcion().getId_opc()!=null && opcion.getOpcion().getId_opc() ==
		 * padre.getId()){
		 * 
		 * Opcion opcionHijo = opcion.getOpcion(); boolean padreTieneHijo =false; for
		 * (Opcion hijo : padre.getOpcions()) { if (hijo.getId()==
		 * opcionHijo.getId_opc()) padreTieneHijo = true; }
		 * 
		 * if (!padreTieneHijo){ Opcion opcion1 = new Opcion();
		 * opcion1.setId(opcionHijo.getId()); opcion1.setIcon(opcionHijo.getIcon());
		 * opcion1.setUrl(opcionHijo.getUrl()); opcion1.setNom(opcionHijo.getNom());
		 * opcion1.setTitulo(opcionHijo.getTitulo());
		 * opcion1.setSubtitulo(opcionHijo.getSubtitulo()); opcion1.setNiv(2);
		 * 
		 * //agregar nieto Opcion opcion2 = new Opcion(); opcion2.setId(opcion.getId());
		 * opcion2.setIcon(opcion.getIcon()); opcion2.setUrl(opcion.getUrl());
		 * opcion2.setNom(opcion.getNom()); opcion2.setTitulo(opcion.getTitulo());
		 * opcion2.setSubtitulo(opcion.getSubtitulo()); opcion2.setNiv(3);
		 * 
		 * opcion1.getOpcions().add(opcion2);
		 * 
		 * padre.getOpcions().add(opcion1);//hijo }
		 * 
		 * //obtener hijo ( padre del nieto)
		 * 
		 * for (Opcion hijo : padre.getOpcions()) { if (hijo.getId() ==
		 * opcion.getOpcion().getId_opc()) hijo.getOpcions().add(opcion); }
		 * 
		 * } } }
		 */

		// llenar los nietos a los hijos
		/*
		 * for (Opcion padre : padres) {
		 * 
		 * for (Opcion hijo : padre.getOpcions()) {
		 * 
		 * 
		 * for (Opcion opcion : opciones ) { if (//opcion.getId()!=hijo.getId() &&
		 * opcion.getOpcion()!=null && opcion.getOpcion().getId_opc()!=null &&
		 * opcion.getId_opc() == hijo.getId()){ opcion.setNiv(3);
		 * 
		 * Opcion opcion1 = new Opcion(); opcion1.setId(opcion.getId());
		 * opcion1.setIcon(opcion.getIcon()); opcion1.setUrl(opcion.getUrl());
		 * opcion1.setNom(opcion.getNom()); opcion1.setTitulo(opcion.getTitulo());
		 * opcion1.setSubtitulo(opcion.getSubtitulo()); opcion1.setNiv(3);
		 * hijo.getOpcions().add(opcion1); } }
		 * 
		 * }
		 * 
		 * 
		 * 
		 * }
		 */

		/*
		 * for (Opcion opcion : opciones ) { //hijo en 2do nivel if
		 * (opcion.getOpcion()!=null && opcion.getOpcion().getId_opc()==null){
		 * opcion.setNiv(1); boolean padrenuevo =false; for (Opcion padre : padres){ if
		 * (opcion.getId_opc()==padre.getId()) padrenuevo = true; } if(padrenuevo)
		 * padres.add(opcion.getOpcion()); }
		 * 
		 * 
		 * if (opcion.getOpcion()!=null && opcion.getOpcion().getId_opc()!=null){
		 * opcion.setNiv(1); boolean padrenuevo =false; for (Opcion padre : padres){ if
		 * (opcion.getId_opc()==padre.getId()) padrenuevo = true; } if(padrenuevo)
		 * padres.add(opcion.getOpcion()); }
		 * 
		 * }
		 */

		// 2do nivel
		/*
		 * for (Opcion opcion : opciones ) {
		 * 
		 * Opcion opcionPadre = null; //2do nivel for (Opcion padre : padres ) { if
		 * (opcion.getId_opc()==padre.getId()) opcionPadre = padre; } if
		 * (opcionPadre!=null){ opcion.setNiv(2);
		 * 
		 * opcionPadre.getOpcions().add(opcion); }
		 * 
		 * }
		 * 
		 * //3er nivel for (Opcion opcion : opciones ) { if (opcion.getNiv()==null){
		 * 
		 * for (Opcion opcion1 : padres ) { for (Opcion opcion2 : opcion1.getOpcions() )
		 * { if (opcion2.getId() == opcion.getId_opc()){ opcion.setNiv(3);
		 * opcion2.getOpcions().add(opcion); } } } } }
		 */

		result.setResult(padres);

		return padres;
	}

 	
}

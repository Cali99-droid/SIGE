package com.sige.mat.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sige.common.enums.EnumPerfil;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.LogLoginDAO;
import com.sige.mat.dao.RolDAO;
import com.sige.mat.dao.UsuarioDAO;
import com.sige.mat.dao.UsuarioNivelDAO;
import com.sige.mat.dao.UsuarioTokenDAO;
import com.tesla.colegio.model.Trabajador;
import com.tesla.colegio.model.Usuario;
import com.tesla.colegio.model.UsuarioNivel;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;

@RestController
@RequestMapping(value = "/api/usuario")
public class UsuarioRestController {
	
	@Autowired
	private UsuarioDAO usuarioDAO;

	@Autowired
	private LogLoginDAO logLoginDAO;

	@Autowired
	private UsuarioNivelDAO usuarioNivelDAO;
	
	@Autowired
	private RolDAO rolDAO;
	
	@Autowired
	private UsuarioTokenDAO usuarioTokenDAO;

	@Autowired
	private CacheManager cacheManager;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Usuario usuario) {

		AjaxResponseBody result = new AjaxResponseBody();
		 Param param = new Param();
		 param.put("tra.est", "A");
		result.setResult(usuarioDAO.listFullByParams( param, new String[]{"tra.ape_pat asc, tra.ape_mat asc"}) );
		
		return result;
	}
	
	@RequestMapping(value = "/listarUsuariosDocentes")
	public AjaxResponseBody listarUusariosDocentes() {

		AjaxResponseBody result = new AjaxResponseBody();
		result.setResult(usuarioDAO.listarUsuariosDocentes());
		
		return result;
	}
	
	@RequestMapping(value = "/listarRol")
	public AjaxResponseBody listarRol() {

		AjaxResponseBody result = new AjaxResponseBody();
		result.setResult(rolDAO.listarRoles());
		
		return result;
	}
	
	@RequestMapping(value = "/listarLocales/{id_usr}")
	public AjaxResponseBody listarLocales(@PathVariable Integer id_usr) {

		AjaxResponseBody result = new AjaxResponseBody();
		result.setResult(usuarioDAO.listarLocales(id_usr));
		
		return result;
	}
	
	@RequestMapping(value = "/buscar")
	public AjaxResponseBody buscar(String nom,String est) {

		AjaxResponseBody result = new AjaxResponseBody();
		Param param = new Param();
		param.put("concat(tra.ape_pat,' ',tra.ape_mat,' ', tra.nom)", "%" + nom + "%");
		if(!"".equals(est))
			param.put("usr.est", est);
		
		result.setResult(usuarioDAO.listFullByParams( param, new String[]{"tra.ape_pat asc, tra.ape_mat asc"}) );
		
		return result;
	}

	@RequestMapping(value = "/listarTrabajador")
	public AjaxResponseBody listarTrabajador(Integer id_pue) { /*Id del puesto*/

		AjaxResponseBody result = new AjaxResponseBody();
		 
		if (id_pue==null)
			id_pue=0;
		
		result.setResult(usuarioDAO.listaTrabajador(id_pue) );
		
		return result;
	}
	
	@RequestMapping( value="/listarNiveles", method = RequestMethod.GET)
	public AjaxResponseBody listarNiveles( Integer id_usr) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( usuarioNivelDAO.listarNiveles(id_usr));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}

	@Transactional
	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Usuario usuario, Integer id_usr_niv[],Integer id_niveles[]) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			Integer id_usr=usuarioDAO.saveOrUpdate(usuario);
			
			for (int i = 0; i < id_niveles.length; i++) {
				UsuarioNivel usuarioNivel = new UsuarioNivel();
				if(id_usr_niv[i]!=null) {
					usuarioNivel.setId(id_usr_niv[i]);
				}
				usuarioNivel.setId_niv(id_niveles[i]);
				usuarioNivel.setId_usr(id_usr);
				usuarioNivel.setEst("A");
				usuarioNivelDAO.saveOrUpdate(usuarioNivel);
			}
			result.setResult(1);
			//cacheManager.update(Usuario.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//Primero elimamos los niveles asignados
			usuarioNivelDAO.delete(id);
			usuarioTokenDAO.deleteUsuario(id);
			usuarioDAO.delete(id);
			//cacheManager.update(Usuario.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			Usuario usuario = usuarioDAO.getFull(id, new String[]{Trabajador.TABLA});
			
			List<Row> usuarioNiveles = usuarioNivelDAO.listaxUsuario(id);
			Row usuario_datos = new Row();
			usuario_datos.put("niveles", usuarioNiveles);
			usuario_datos.put("usuario",usuario);			
			result.setResult(usuario_datos);
			return result;
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/datos", method = RequestMethod.GET)
	public AjaxResponseBody datos(Usuario usuario) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(usuarioDAO.listaDatosUsuario());
		
		return result;

	}	
	
	@RequestMapping( value="/actualizarEstado", method = RequestMethod.POST)
	public AjaxResponseBody actualizarEstado(Integer id, String est) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			usuarioDAO.actualizarEstado(id, est);
			
			if(est.equals("A")){
				//limpiar los bloqueos
				logLoginDAO.resetearIntentosFallidos(id, EnumPerfil.PERFIL_TRABAJADOR.getValue());
			}
			
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/datosTrabajador/{id_usr}", method = RequestMethod.GET)
	public AjaxResponseBody datosTrabajador(@PathVariable Integer id_usr) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(usuarioDAO.listaDatosTrabajador(id_usr).get(0));
		
		return result;

	}	

}

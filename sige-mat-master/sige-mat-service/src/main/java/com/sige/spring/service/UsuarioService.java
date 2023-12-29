package com.sige.spring.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sige.mat.dao.UsuarioDAO;
import com.sige.mat.dao.UsuarioNivelDAO;
import com.tesla.colegio.model.Usuario;
import com.tesla.colegio.model.UsuarioNivel;
import com.tesla.frmk.sql.Param;


@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioDAO usuarioDAO;
	
	@Autowired
	private UsuarioNivelDAO usuarioNivelDAO;
	
	@Transactional
	public Integer saveOrUpdate(Usuario usuario){
		
		Integer id_usr = usuarioDAO.saveOrUpdate(usuario);
		
		if(usuario.getId()==null){
			//insertar los niveles los niveles nuevos
			Param param= new Param();
			param.put("id_usr", id_usr);
			
			
			List<UsuarioNivel> usuarioNivelesActuales = usuarioNivelDAO.listByParams(param,null);
			
			Integer[] id_niveles_actuales = new Integer[usuarioNivelesActuales.size()];
			int i=0;
			for (UsuarioNivel usuarioNivel : usuarioNivelesActuales) {
				id_niveles_actuales[i] = usuarioNivel.getId_niv();
				i++;
			}
			
			//insertar si no existe
			for (Integer id_niv: usuario.getId_niveles()) {
				
				if (!Arrays.stream(id_niveles_actuales).anyMatch(id_niv::equals)){
					UsuarioNivel usuarioNivel = new UsuarioNivel();
					usuarioNivel.setEst("A");
					usuarioNivel.setId_usr(id_usr);
					usuarioNivel.setId_niv(id_niv);
					usuarioNivelDAO.saveOrUpdate(usuarioNivel);
				}
				
			}
			
			//desactivar si estaba en BD pero ya no est� en el arreglo nuevo
			for (UsuarioNivel usuarioNivelActual : usuarioNivelesActuales) {
				Integer id_niv_act =usuarioNivelActual.getId_niv();
				
				//desactivar si no existe
				if (!Arrays.stream( usuario.getId_niveles()).anyMatch(id_niv_act::equals)){
					param = new Param();
					param.put("usr_id", id_usr);
					param.put("niv_id", id_niv_act);
					
					UsuarioNivel usuarioNivel = usuarioNivelDAO.getByParams(param);
					usuarioNivel.setEst("I");
					usuarioNivelDAO.saveOrUpdate(usuarioNivel);
				}

			}
		}else{
			//actualizar los niveles
			for (Integer id_niv : usuario.getId_niveles()) {
				UsuarioNivel usuarioNivel = new UsuarioNivel();
				usuarioNivel.setEst("A");
				usuarioNivel.setId_usr(id_usr);
				usuarioNivel.setId_niv(id_niv);
				usuarioNivelDAO.saveOrUpdate(usuarioNivel);
				
			}
		}
		
		return id_usr;
	}
	
}

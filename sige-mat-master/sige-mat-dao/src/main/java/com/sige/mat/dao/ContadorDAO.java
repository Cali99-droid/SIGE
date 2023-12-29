package com.sige.mat.dao;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.ContadorDAOImpl;
import com.sige.web.security.TokenSeguridad;

/**
 * Define m�todos DAO operations para la entidad contador.
 * @author MV
 *
 */
@Repository
public class ContadorDAO extends ContadorDAOImpl{
	
	@Autowired
	private TokenSeguridad tokenSeguridad;
	
	@Autowired
	private SQLUtil sqlUtil;
	
	public void actualizarContador(Integer nro, Date fec){
		String sql = "update msj_contador set nro=?, fec=?, fec_act=curdate() , usr_act=? "; 
		sqlUtil.update(sql, new Object[]{nro, fec, tokenSeguridad.getId()});
	}
	
	public void actualizarContadorSinTK(Integer nro, Date fec){
		String sql = "update msj_contador set nro=?, fec=?, fec_act=curdate() "; 
		sqlUtil.update(sql, new Object[]{nro, fec});
	}
	
	public void actualizarUsuarioContador(String usuario){
		String sql = "update msj_contador set usr=?, fec_act=curdate() , usr_act=? "; 
		sqlUtil.update(sql, new Object[]{usuario, tokenSeguridad.getId()});
	}
	
	public void actualizarFechaContador(Date fec){
		String sql = "update msj_contador set fec=?, nro=0, fec_act=curdate() , usr_act=? "; 
		sqlUtil.update(sql, new Object[]{fec, tokenSeguridad.getId()});
	}
	
	public void actualizarFechaContadorSinTK(Date fec){
		String sql = "update msj_contador set fec=?, nro=0, fec_act=curdate() "; 
		sqlUtil.update(sql, new Object[]{fec});
	}
	
	public void actualizarUsuarioContadorSinTK(String usuario){
		String sql = "update msj_contador set usr=?, fec_act=curdate()  "; 
		sqlUtil.update(sql, new Object[]{usuario});
	}
	
}

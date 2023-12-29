package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.UsuarioCampusDAOImpl;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad usuario_campus.
 * @author MV
 *
 */
@Repository
public class UsuarioCampusDAO extends UsuarioCampusDAOImpl{
	
	@Autowired
	private SQLUtil sqlUtil;
	
	public void actualizarPswAlumno(String usuario, String psw){
		String sql = "update cvi_usuario_campus set psw=? WHERE usr=?";
		sqlUtil.update(sql,new Object[]{ psw,usuario});		
	}
	
	public void desactivarSigeUsuarios(String usuario){
		String sql = "UPDATE `cvi_sige_usuarios` SET est='I' WHERE Correo=?";
		sqlUtil.update(sql,new Object[]{usuario});		
	}
	
	public Row obtenerContraseniaUsuario(String usuario){
		String sql = "SELECT psw FROM `cvi_usuario_campus` WHERE usr=? ";
		List<Row> contrasenia = sqlUtil.query(sql,new Object[]{usuario});
		if (contrasenia.size()==0)
			return  null;
		else
			return contrasenia.get(0);	
	}
	
}

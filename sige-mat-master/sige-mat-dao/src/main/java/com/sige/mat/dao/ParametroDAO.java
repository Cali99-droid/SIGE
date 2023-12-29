package com.sige.mat.dao;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.ParametroDAOImpl;
import com.tesla.frmk.sql.Row;


/**
 * Implementaci&oacute;n de la interface ParametroDAO.
 * @author MV
 *
 */
@Repository
public class ParametroDAO extends ParametroDAOImpl{
	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> parametrosxModulo(Integer id_mod, String nom) {//aqui
		
		String sql = "SELECT * FROM mod_parametro where id_mod=? and nom=?";
		return sqlUtil.query(sql, new Object[] {id_mod,nom});

	}
	
	public int upValorParametro(Integer id,String val) {

		// update
		String sql = "UPDATE mod_parametro SET val=? WHERE id=? ";

		return jdbcTemplate.update(sql,val,id);

	}
}

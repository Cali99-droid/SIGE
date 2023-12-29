package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.GiroNegocioDAOImpl;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad giro_negocio.
 * @author MV
 *
 */
@Repository
public class GiroNegocioDAO extends GiroNegocioDAOImpl{
	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> listarGiroNegocio() {
		String sql = "select id, nom value from ges_giro_negocio";
		return sqlUtil.query(sql);

	}
}

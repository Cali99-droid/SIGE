package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import  com.sige.mat.dao.impl.DisenioCurricularDAOImpl;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad disenio_curricular.
 * @author MV
 *
 */
@Repository
public class DisenioCurricularDAO extends DisenioCurricularDAOImpl{
	
	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> listarDisenioCurricular() {
		String sql = "select id, nom value from col_disenio_curricular cdc ;";
		return sqlUtil.query(sql);

	}
}

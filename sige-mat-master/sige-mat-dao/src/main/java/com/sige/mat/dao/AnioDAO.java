package com.sige.mat.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.AnioDAOImpl;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad anio.
 * @author MV
 *
 */
@Repository
public class AnioDAO extends AnioDAOImpl{
	final static Logger logger = Logger.getLogger(EvaluacionDAO.class);
	
	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> listaAnios() {
		
		String sql = "SELECT id, nom as value FROM `col_anio` ORDER BY nom DESC";
		logger.info(sql);
		return sqlUtil.query(sql);

	}
	
	public Integer getById_per(Integer id_per){
		
		String sql = "select a.nom from col_anio a inner join per_periodo p on p.id_anio= a.id where p.id=" + id_per;
		
		return sqlUtil.queryForObject(sql, Integer.class);
		
	}
	
}

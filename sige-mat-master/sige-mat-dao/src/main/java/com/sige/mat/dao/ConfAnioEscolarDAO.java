package com.sige.mat.dao;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.ConfAnioEscolarDAOImpl;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad conf_anio_escolar.
 * @author MV
 *
 */
@Repository
public class ConfAnioEscolarDAO extends ConfAnioEscolarDAOImpl{
	final static Logger logger = Logger.getLogger(EvaluacionDAO.class);
	
	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> getConfSemanas(Integer id_anio) {
		String sql = "SELECT id, nro_sem FROM col_conf_anio_escolar ccfae where id_anio="+id_anio;
		logger.info(sql);
		return sqlUtil.query(sql);
	}
}

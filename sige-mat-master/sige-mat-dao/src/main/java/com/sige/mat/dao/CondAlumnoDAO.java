package com.sige.mat.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.CondAlumnoDAOImpl;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad cond_alumno.
 * @author MV
 *
 */
@Repository
public class CondAlumnoDAO extends CondAlumnoDAOImpl{
	final static Logger logger = Logger.getLogger(CondAlumnoDAO.class);
	
	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> listaCondicionTipo() {
		
		String sql = "SELECT con.id , con.nom value FROM cat_cond_alumno con INNER JOIN cat_tip_cond ctc ON con.id_ctc=ctc.id WHERE ctc.id=1";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {});

	}
	
	public List<Row> listaCondicionTipoCond() {
		
		String sql = "SELECT con.id , con.nom value FROM cat_cond_alumno con INNER JOIN cat_tip_cond ctc ON con.id_ctc=ctc.id WHERE ctc.id=2";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {});

	}
}

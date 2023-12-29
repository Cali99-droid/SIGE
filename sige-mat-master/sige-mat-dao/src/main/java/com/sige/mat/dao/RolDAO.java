package com.sige.mat.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.RolDAOImpl;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad rol.
 * @author MV
 *
 */
@Repository
public class RolDAO extends RolDAOImpl{
	
	final static Logger logger = Logger.getLogger(TrabajadorDAO.class);

    @Autowired
    private SQLUtil sqlUtil;
	
	public List<Row> listarRoles() {

		String sql ="SELECT id, nom value FROM seg_rol";
		logger.info(sql);
		return sqlUtil.query(sql);

	}
}

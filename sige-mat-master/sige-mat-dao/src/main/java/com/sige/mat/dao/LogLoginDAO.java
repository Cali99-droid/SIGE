package com.sige.mat.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.sige.mat.dao.impl.LogLoginDAOImpl;

/**
 * Define m�todos DAO operations para la entidad log_login.
 * 
 * @author MV
 *
 */
@Repository
public class LogLoginDAO extends LogLoginDAOImpl {
	final static Logger logger = Logger.getLogger(LogLoginDAO.class);

	public Integer cantidadAccesosFallidos(Integer id_usr, Integer id_per) {
		String sql = "select count(*) from seg_log_login where id_usr=? and id_per=? and est='A' and exito='0' and DATE(fec_ins)=CURDATE()";

		return jdbcTemplate.queryForObject(sql, new Object[] { id_usr, id_per }, Integer.class);

	}
	
	public void resetearIntentosFallidos(Integer id_usr, Integer id_per) {
		
		String sql = "update seg_log_login set est='I' ,fec_act= curdate() where id_usr=? and id_per=? and est='A' and exito='0' and DATE(fec_ins)=CURDATE()";

		jdbcTemplate.update(sql, new Object[] { id_usr, id_per });

	}

}

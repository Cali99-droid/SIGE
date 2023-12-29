package com.sige.mat.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.sige.mat.dao.impl.MatriculaDocDAOImpl;
import com.tesla.colegio.model.MatriculaDoc;


/**
 * Define m�todos DAO operations para la entidad matricula_doc.
 * @author MV
 *
 */
@Repository
public class MatriculaDocDAO extends MatriculaDocDAOImpl{
	final static Logger logger = Logger.getLogger(MatriculaDocDAO.class);

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(MatriculaDoc matricula_doc) {
		if (matricula_doc.getId_ado() == null) {
			// update
			String sql = "UPDATE mat_matricula_doc "
						+ "SET id_ado=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id_alu=?";
			
			logger.info(sql);

			jdbcTemplate.update(sql, 
						matricula_doc.getId_ado(),
						matricula_doc.getId_alu(),
						matricula_doc.getEst(),
						matricula_doc.getUsr_act(),
						new java.util.Date()); 
			return matricula_doc.getId_ado();

		} else {
			// insert
			String sql = "insert into mat_matricula_doc ("
						+ "id_ado, "
						+ "id_alu, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				logger.info(sql);

				jdbcTemplate.update(sql, 
				matricula_doc.getId_ado(),
				matricula_doc.getId_alu(),
				matricula_doc.getEst(),
				matricula_doc.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}
}

package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.tesla.colegio.model.ExaConfEscrito;



import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ExaConfEscritoDAO.
 * @author MV
 *
 */
public class ExaConfEscritoDAOImpl{
	final static Logger logger = Logger.getLogger(ExaConfEscritoDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	//@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(ExaConfEscrito exa_conf_escrito) {
		if (exa_conf_escrito.getId() != null) {
			// update
			String sql = "UPDATE eva_exa_conf_escrito "
						+ "SET id_eva_ex=?, "
						+ "num_pre=?, "
						+ "pje_pre_cor=?, "
						+ "pje_pre_inc=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						exa_conf_escrito.getId_eva_ex(),
						exa_conf_escrito.getNum_pre(),
						exa_conf_escrito.getPje_pre_cor(),
						exa_conf_escrito.getPje_pre_inc(),
						exa_conf_escrito.getEst(),
						exa_conf_escrito.getUsr_act(),
						new java.util.Date(),
						exa_conf_escrito.getId()); 

		} else {
			// insert
			String sql = "insert into eva_exa_conf_escrito ("
						+ "id_eva_ex, "
						+ "num_pre, "
						+ "pje_pre_cor, "
						+ "pje_pre_inc, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				exa_conf_escrito.getId_eva_ex(),
				exa_conf_escrito.getNum_pre(),
				exa_conf_escrito.getPje_pre_cor(),
				exa_conf_escrito.getPje_pre_inc(),
				exa_conf_escrito.getEst(),
				exa_conf_escrito.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from eva_exa_conf_escrito where id_eva_ex=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<ExaConfEscrito> list() {
		String sql = "select * from eva_exa_conf_escrito";
		
		//logger.info(sql);
		
		List<ExaConfEscrito> listExaConfEscrito = jdbcTemplate.query(sql, new RowMapper<ExaConfEscrito>() {

			
			public ExaConfEscrito mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listExaConfEscrito;
	}

	
	public ExaConfEscrito get(int id) {
		String sql = "select * from eva_exa_conf_escrito WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ExaConfEscrito>() {

			
			public ExaConfEscrito extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public ExaConfEscrito getFull(int id, String tablas[]) {
		String sql = "select ex_esc.id ex_esc_id, ex_esc.id_eva_ex ex_esc_id_eva_ex , ex_esc.num_pre ex_esc_num_pre , ex_esc.ptje_apro ex_esc_ptje_apro , ex_esc.pje_pre_cor ex_esc_pje_pre_cor , ex_esc.pje_pre_inc ex_esc_pje_pre_inc  ,ex_esc.est ex_esc_est ";
	
		sql = sql + " from eva_exa_conf_escrito ex_esc "; 
		sql = sql + " where ex_esc.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<ExaConfEscrito>() {
		
			
			public ExaConfEscrito extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ExaConfEscrito exaconfescrito= rsToEntity(rs,"ex_esc_");
							return exaconfescrito;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public ExaConfEscrito getByParams(Param param) {

		String sql = "select * from eva_exa_conf_escrito " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ExaConfEscrito>() {
			
			public ExaConfEscrito extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<ExaConfEscrito> listByParams(Param param, String[] order) {

		String sql = "select * from eva_exa_conf_escrito " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<ExaConfEscrito>() {

			
			public ExaConfEscrito mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<ExaConfEscrito> listFullByParams(ExaConfEscrito exaconfescrito, String[] order) {
	
		return listFullByParams(Param.toParam("ex_esc",exaconfescrito), order);
	
	}	
	
	
	public List<ExaConfEscrito> listFullByParams(Param param, String[] order) {

		String sql = "select ex_esc.id ex_esc_id, ex_esc.id_eva_ex ex_esc_id_eva_ex , ex_esc.num_pre ex_esc_num_pre , ex_esc.ptje_apro ex_esc_ptje_apro , ex_esc.pje_pre_cor ex_esc_pje_pre_cor , ex_esc.pje_pre_inc ex_esc_pje_pre_inc  ,ex_esc.est ex_esc_est ";
		sql = sql + " from eva_exa_conf_escrito ex_esc";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<ExaConfEscrito>() {

			
			public ExaConfEscrito mapRow(ResultSet rs, int rowNum) throws SQLException {
				ExaConfEscrito exaconfescrito= rsToEntity(rs,"ex_esc_");
				return exaconfescrito;
			}

		});

	}	




	// funciones privadas utilitarias para ExaConfEscrito

	private ExaConfEscrito rsToEntity(ResultSet rs,String alias) throws SQLException {
		ExaConfEscrito exa_conf_escrito = new ExaConfEscrito();

		exa_conf_escrito.setId(rs.getInt( alias + "id"));
		exa_conf_escrito.setId_eva_ex(rs.getInt( alias + "id_eva_ex"));
		exa_conf_escrito.setNum_pre(rs.getInt( alias + "num_pre"));
		exa_conf_escrito.setPje_pre_cor(rs.getBigDecimal( alias + "pje_pre_cor"));
		exa_conf_escrito.setPje_pre_inc(rs.getBigDecimal( alias + "pje_pre_inc"));
		exa_conf_escrito.setEst(rs.getString( alias + "est"));
								
		return exa_conf_escrito;

	}
	
}

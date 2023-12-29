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
import com.tesla.colegio.model.ExaConfMarcacion;



import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ExaConfMarcacionDAO.
 * @author MV
 *
 */
public class ExaConfMarcacionDAOImpl{
	final static Logger logger = Logger.getLogger(ExaConfMarcacionDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	//@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(ExaConfMarcacion exa_conf_marcacion) {
		if (exa_conf_marcacion.getId() != null) {
			// update
			String sql = "UPDATE eva_exa_conf_marcacion "
						+ "SET id_eva_ex=?, "
						+ "num_pre=?, "
						+ "pje_pre_cor=?, "
						+ "pje_pre_inc=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						exa_conf_marcacion.getId_eva_ex(),
						exa_conf_marcacion.getNum_pre(),
						exa_conf_marcacion.getPje_pre_cor(),
						exa_conf_marcacion.getPje_pre_inc(),
						exa_conf_marcacion.getEst(),
						exa_conf_marcacion.getUsr_act(),
						new java.util.Date(),
						exa_conf_marcacion.getId()); 

		} else {
			// insert
			String sql = "insert into eva_exa_conf_marcacion ("
						+ "id_eva_ex, "
						+ "num_pre, "
						+ "pje_pre_cor, "
						+ "pje_pre_inc, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				exa_conf_marcacion.getId_eva_ex(),
				exa_conf_marcacion.getNum_pre(),
				exa_conf_marcacion.getPje_pre_cor(),
				exa_conf_marcacion.getPje_pre_inc(),
				exa_conf_marcacion.getEst(),
				exa_conf_marcacion.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from eva_exa_conf_marcacion where id_eva_ex=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<ExaConfMarcacion> list() {
		String sql = "select * from eva_exa_conf_marcacion";
		
		//logger.info(sql);
		
		List<ExaConfMarcacion> listExaConfMarcacion = jdbcTemplate.query(sql, new RowMapper<ExaConfMarcacion>() {

			
			public ExaConfMarcacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listExaConfMarcacion;
	}

	
	public ExaConfMarcacion get(int id) {
		String sql = "select * from eva_exa_conf_marcacion WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ExaConfMarcacion>() {

			
			public ExaConfMarcacion extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public ExaConfMarcacion getFull(int id, String tablas[]) {
		String sql = "select ex_mar.id ex_mar_id, ex_mar.id_eva_ex ex_mar_id_eva_ex , ex_mar.num_pre ex_mar_num_pre  , ex_mar.pje_pre_cor ex_mar_pje_pre_cor , ex_mar.pje_pre_inc ex_mar_pje_pre_inc  ,ex_mar.est ex_mar_est ";
	
		sql = sql + " from eva_exa_conf_marcacion ex_mar "; 
		sql = sql + " where ex_mar.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<ExaConfMarcacion>() {
		
			
			public ExaConfMarcacion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ExaConfMarcacion exaconfmarcacion= rsToEntity(rs,"ex_mar_");
							return exaconfmarcacion;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public ExaConfMarcacion getByParams(Param param) {

		String sql = "select * from eva_exa_conf_marcacion " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ExaConfMarcacion>() {
			
			public ExaConfMarcacion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<ExaConfMarcacion> listByParams(Param param, String[] order) {

		String sql = "select * from eva_exa_conf_marcacion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<ExaConfMarcacion>() {

			
			public ExaConfMarcacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<ExaConfMarcacion> listFullByParams(ExaConfMarcacion exaconfmarcacion, String[] order) {
	
		return listFullByParams(Param.toParam("ex_mar",exaconfmarcacion), order);
	
	}	
	
	
	public List<ExaConfMarcacion> listFullByParams(Param param, String[] order) {

		String sql = "select ex_mar.id ex_mar_id, ex_mar.id_eva_ex ex_mar_id_eva_ex , ex_mar.num_pre ex_mar_num_pre , ex_mar.pje_pre_cor ex_mar_pje_pre_cor , ex_mar.pje_pre_inc ex_mar_pje_pre_inc  ,ex_mar.est ex_mar_est ";
		sql = sql + " from eva_exa_conf_marcacion ex_mar";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<ExaConfMarcacion>() {

			
			public ExaConfMarcacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				ExaConfMarcacion exaconfmarcacion= rsToEntity(rs,"ex_mar_");
				return exaconfmarcacion;
			}

		});

	}	




	// funciones privadas utilitarias para ExaConfMarcacion

	private ExaConfMarcacion rsToEntity(ResultSet rs,String alias) throws SQLException {
		ExaConfMarcacion exa_conf_marcacion = new ExaConfMarcacion();

		exa_conf_marcacion.setId(rs.getInt( alias + "id"));
		exa_conf_marcacion.setId_eva_ex(rs.getInt( alias + "id_eva_ex"));
		exa_conf_marcacion.setNum_pre(rs.getInt( alias + "num_pre"));
		exa_conf_marcacion.setPje_pre_cor(rs.getBigDecimal( alias + "pje_pre_cor"));
		exa_conf_marcacion.setPje_pre_inc(rs.getBigDecimal( alias + "pje_pre_inc"));
		exa_conf_marcacion.setEst(rs.getString( alias + "est"));
								
		return exa_conf_marcacion;

	}
	
}

package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Arrays;

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
import com.tesla.colegio.model.CriterioAlternativa;

import com.tesla.colegio.model.CriterioPregunta;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CriterioAlternativaDAO.
 * @author MV
 *
 */
public class CriterioAlternativaDAOImpl{
	final static Logger logger = Logger.getLogger(CriterioAlternativaDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(CriterioAlternativa citerio_alternativa) {
		if (citerio_alternativa.getId() != null) {
			// update
			String sql = "UPDATE eva_criterio_alternativa "
						+ "SET id_pre=?, "
						+ "alt=?, "
						+ "punt=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						citerio_alternativa.getId_pre(),
						citerio_alternativa.getAlt(),
						citerio_alternativa.getPunt(),
						citerio_alternativa.getEst(),
						citerio_alternativa.getUsr_act(),
						new java.util.Date(),
						citerio_alternativa.getId()); 

		} else {
			// insert
			String sql = "insert into eva_criterio_alternativa ("
						+ "id_pre, "
						+ "alt, "
						+ "punt, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				citerio_alternativa.getId_pre(),
				citerio_alternativa.getAlt(),
				citerio_alternativa.getPunt(),
				citerio_alternativa.getEst(),
				citerio_alternativa.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from eva_criterio_alternativa where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<CriterioAlternativa> list() {
		String sql = "select * from eva_criterio_alternativa";
		
		//logger.info(sql);
		
		List<CriterioAlternativa> listCriterioAlternativa = jdbcTemplate.query(sql, new RowMapper<CriterioAlternativa>() {

			public CriterioAlternativa mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCriterioAlternativa;
	}

	public CriterioAlternativa get(int id) {
		String sql = "select * from eva_criterio_alternativa WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CriterioAlternativa>() {

			public CriterioAlternativa extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CriterioAlternativa getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select ex_cri_alt.id ex_cri_alt_id, ex_cri_alt.id_pre ex_cri_alt_id_pre , ex_cri_alt.alt ex_cri_alt_alt , ex_cri_alt.punt ex_cri_alt_punt  ,ex_cri_alt.est ex_cri_alt_est ";
		if (aTablas.contains("eva_citerio_pregunta"))
			sql = sql + ", ex_cri_pre.id ex_cri_pre_id  , ex_cri_pre.id_per ex_cri_pre_id_per , ex_cri_pre.pre ex_cri_pre_pre  ";
	
		sql = sql + " from eva_criterio_alternativa ex_cri_alt "; 
		if (aTablas.contains("eva_citerio_pregunta"))
			sql = sql + " left join eva_citerio_pregunta ex_cri_pre on ex_cri_pre.id = ex_cri_alt.id_pre ";
		sql = sql + " where ex_cri_alt.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<CriterioAlternativa>() {

			public CriterioAlternativa extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CriterioAlternativa CriterioAlternativa= rsToEntity(rs,"ex_cri_alt_");
					if (aTablas.contains("eva_citerio_pregunta")){
						CriterioPregunta CriterioPregunta = new CriterioPregunta();  
							CriterioPregunta.setId(rs.getInt("ex_cri_pre_id")) ;  
							CriterioPregunta.setId_per(rs.getInt("ex_cri_pre_id_per")) ;  
							CriterioPregunta.setPre(rs.getString("ex_cri_pre_pre")) ;  
							CriterioAlternativa.setCriterioPregunta(CriterioPregunta);
					}
							return CriterioAlternativa;
				}
				
				return null;
			}
			
		});


	}		
	
	public CriterioAlternativa getByParams(Param param) {

		String sql = "select * from eva_criterio_alternativa " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CriterioAlternativa>() {

			public CriterioAlternativa extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CriterioAlternativa> listByParams(Param param, String[] order) {

		String sql = "select * from eva_criterio_alternativa " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<CriterioAlternativa>() {

			public CriterioAlternativa mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	public List<CriterioAlternativa> listFullByParams(CriterioAlternativa CriterioAlternativa, String[] order) {
	
		return listFullByParams(Param.toParam("ex_cri_alt",CriterioAlternativa), order);
	
	}	
	
	public List<CriterioAlternativa> listFullByParams(Param param, String[] order) {

		String sql = "select ex_cri_alt.id ex_cri_alt_id, ex_cri_alt.id_pre ex_cri_alt_id_pre , ex_cri_alt.alt ex_cri_alt_alt , ex_cri_alt.punt ex_cri_alt_punt  ,ex_cri_alt.est ex_cri_alt_est ";
		sql = sql + ", ex_cri_pre.id ex_cri_pre_id  , ex_cri_pre.id_per ex_cri_pre_id_per , ex_cri_pre.pre ex_cri_pre_pre  ";
		sql = sql + " from eva_criterio_alternativa ex_cri_alt";
		sql = sql + " left join eva_citerio_pregunta ex_cri_pre on ex_cri_pre.id = ex_cri_alt.id_pre ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<CriterioAlternativa>() {

			public CriterioAlternativa mapRow(ResultSet rs, int rowNum) throws SQLException {
				CriterioAlternativa CriterioAlternativa= rsToEntity(rs,"ex_cri_alt_");
				CriterioPregunta CriterioPregunta = new CriterioPregunta();  
				CriterioPregunta.setId(rs.getInt("ex_cri_pre_id")) ;  
				CriterioPregunta.setId_per(rs.getInt("ex_cri_pre_id_per")) ;  
				CriterioPregunta.setPre(rs.getString("ex_cri_pre_pre")) ;  
				CriterioAlternativa.setCriterioPregunta(CriterioPregunta);
				return CriterioAlternativa;
			}

		});

	}	




	// funciones privadas utilitarias para CriterioAlternativa

	private CriterioAlternativa rsToEntity(ResultSet rs,String alias) throws SQLException {
		CriterioAlternativa citerio_alternativa = new CriterioAlternativa();

		citerio_alternativa.setId(rs.getInt( alias + "id"));
		citerio_alternativa.setId_pre(rs.getInt( alias + "id_pre"));
		citerio_alternativa.setAlt(rs.getString( alias + "alt"));
		citerio_alternativa.setPunt(rs.getInt( alias + "punt"));
		citerio_alternativa.setEst(rs.getString( alias + "est"));
								
		return citerio_alternativa;

	}
	
}

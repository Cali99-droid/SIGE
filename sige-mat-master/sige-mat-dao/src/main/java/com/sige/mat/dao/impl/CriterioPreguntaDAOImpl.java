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
import com.tesla.colegio.model.CriterioPregunta;

import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.CriterioAlternativa;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CriterioPreguntaDAO.
 * @author MV
 *
 */
public class CriterioPreguntaDAOImpl{
	final static Logger logger = Logger.getLogger(CriterioPreguntaDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(CriterioPregunta citerio_pregunta) {
		if (citerio_pregunta.getId() != null) {
			// update
			String sql = "UPDATE eva_criterio_pregunta "
						+ "SET id_per=?, "
						+ "pre=?, "
						+ "ord=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						citerio_pregunta.getId_per(),
						citerio_pregunta.getPre(),
						citerio_pregunta.getOrd(),
						citerio_pregunta.getEst(),
						citerio_pregunta.getUsr_act(),
						new java.util.Date(),
						citerio_pregunta.getId());
			return citerio_pregunta.getId(); //para q siempre devuelva el id de la pregunta

		} else {
			// insert
			String sql = "insert into eva_criterio_pregunta ("
						+ "id_per, "
						+ "pre, "
						+ "ord, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?,?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				citerio_pregunta.getId_per(),
				citerio_pregunta.getPre(),
				citerio_pregunta.getOrd(),
				citerio_pregunta.getEst(),
				citerio_pregunta.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from eva_criterio_pregunta where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<CriterioPregunta> list() {
		String sql = "select * from eva_criterio_pregunta";
		
		//logger.info(sql);
		
		List<CriterioPregunta> listCriterioPregunta = jdbcTemplate.query(sql, new RowMapper<CriterioPregunta>() {

			public CriterioPregunta mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCriterioPregunta;
	}

	public CriterioPregunta get(int id) {
		String sql = "select * from eva_criterio_pregunta WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CriterioPregunta>() {

			public CriterioPregunta extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CriterioPregunta getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select ex_cri_pre.id ex_cri_pre_id, ex_cri_pre.id_per ex_cri_pre_id_per , ex_cri_pre.pre ex_cri_pre_pre  ,ex_cri_pre.est ex_cri_pre_est ";
		if (aTablas.contains("per_periodo"))
			sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ";
	
		sql = sql + " from eva_criterio_pregunta ex_cri_pre "; 
		if (aTablas.contains("per_periodo"))
			sql = sql + " left join per_periodo pee on pee.id = ex_cri_pre.id_per ";
		sql = sql + " where ex_cri_pre.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<CriterioPregunta>() {
		
			public CriterioPregunta extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CriterioPregunta CriterioPregunta= rsToEntity(rs,"ex_cri_pre_");
					if (aTablas.contains("per_periodo")){
						Periodo periodo = new Periodo();  
							periodo.setId(rs.getInt("pee_id")) ;  
							periodo.setId_anio(rs.getInt("pee_id_anio")) ;  
							periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
							periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
							periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
							periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
							periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
							CriterioPregunta.setPeriodo(periodo);
					}
							return CriterioPregunta;
				}
				
				return null;
			}
			
		});


	}		
	
	public CriterioPregunta getByParams(Param param) {

		String sql = "select * from eva_criterio_pregunta " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CriterioPregunta>() {

			public CriterioPregunta extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CriterioPregunta> listByParams(Param param, String[] order) {

		String sql = "select * from eva_criterio_pregunta " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<CriterioPregunta>() {

			public CriterioPregunta mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CriterioPregunta> listFullByParams(CriterioPregunta CriterioPregunta, String[] order) {
	
		return listFullByParams(Param.toParam("ex_cri_pre",CriterioPregunta), order);
	
	}	
	
	public List<CriterioPregunta> listFullByParams(Param param, String[] order) {

		String sql = "select ex_cri_pre.id ex_cri_pre_id, ex_cri_pre.id_per ex_cri_pre_id_per , ex_cri_pre.pre ex_cri_pre_pre  ,ex_cri_pre.est ex_cri_pre_est ";
		sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ";
		sql = sql + " from eva_criterio_pregunta ex_cri_pre";
		sql = sql + " left join per_periodo pee on pee.id = ex_cri_pre.id_per ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<CriterioPregunta>() {

			public CriterioPregunta mapRow(ResultSet rs, int rowNum) throws SQLException {
				CriterioPregunta CriterioPregunta= rsToEntity(rs,"ex_cri_pre_");
				Periodo periodo = new Periodo();  
				periodo.setId(rs.getInt("pee_id")) ;  
				periodo.setId_anio(rs.getInt("pee_id_anio")) ;  
				periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
				periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
				periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
				periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
				periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
				CriterioPregunta.setPeriodo(periodo);
				return CriterioPregunta;
			}

		});

	}	


	public List<CriterioAlternativa> getListCriterioAlternativa(Param param, String[] order) {
		String sql = "select * from eva_citerio_alternativa " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<CriterioAlternativa>() {

			public CriterioAlternativa mapRow(ResultSet rs, int rowNum) throws SQLException {
				CriterioAlternativa citerio_alternativa = new CriterioAlternativa();

				citerio_alternativa.setId(rs.getInt("id"));
				citerio_alternativa.setId_pre(rs.getInt("id_pre"));
				citerio_alternativa.setAlt(rs.getString("alt"));
				citerio_alternativa.setPunt(rs.getInt("punt"));
				citerio_alternativa.setEst(rs.getString("est"));
												
				return citerio_alternativa;
			}

		});	
	}


	// funciones privadas utilitarias para CriterioPregunta

	private CriterioPregunta rsToEntity(ResultSet rs,String alias) throws SQLException {
		CriterioPregunta citerio_pregunta = new CriterioPregunta();

		citerio_pregunta.setId(rs.getInt( alias + "id"));
		citerio_pregunta.setId_per(rs.getInt( alias + "id_per"));
		citerio_pregunta.setPre(rs.getString( alias + "pre"));
		citerio_pregunta.setOrd(rs.getInt( alias + "ord"));
		citerio_pregunta.setEst(rs.getString( alias + "est"));
								
		return citerio_pregunta;

	}
	
}

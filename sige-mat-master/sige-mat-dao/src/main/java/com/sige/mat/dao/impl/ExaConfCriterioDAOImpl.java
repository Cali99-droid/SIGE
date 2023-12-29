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
import com.tesla.colegio.model.ExaConfCriterio;

import com.tesla.colegio.model.EvaluacionVacExamen;
import com.tesla.colegio.model.InsExaCri;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ExaConfCriterioDAO.
 * @author MV
 *
 */
public class ExaConfCriterioDAOImpl{
	final static Logger logger = Logger.getLogger(ExaConfCriterioDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	//@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(ExaConfCriterio exa_conf_criterio) {
		if (exa_conf_criterio.getId() != null) {
			// update
			String sql = "UPDATE eva_exa_conf_criterio "
						+ "SET id_eva_ex=?, "
						+ "dur=?, "
						+ "fec_ini_psi=?, "
						+ "fec_fin_psi=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			 jdbcTemplate.update(sql, 
						exa_conf_criterio.getId_eva_ex(),
						exa_conf_criterio.getDur(),
						exa_conf_criterio.getFec_ini_psi(),
						exa_conf_criterio.getFec_fin_psi(),
						exa_conf_criterio.getEst(),
						exa_conf_criterio.getUsr_act(),
						new java.util.Date(),
						exa_conf_criterio.getId());
			 return exa_conf_criterio.getId();

		} else {
			// insert
			String sql = "insert into eva_exa_conf_criterio ("
						+ "id_eva_ex, "
						+ "dur, "
						+ "fec_ini_psi, "
						+ "fec_fin_psi, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				exa_conf_criterio.getId_eva_ex(),
				exa_conf_criterio.getDur(),
				exa_conf_criterio.getFec_ini_psi(),
				exa_conf_criterio.getFec_fin_psi(),
				exa_conf_criterio.getEst(),
				exa_conf_criterio.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from eva_exa_conf_criterio where id_eva_ex=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<ExaConfCriterio> list() {
		String sql = "select * from eva_exa_conf_criterio";
		
		//logger.info(sql);
		
		List<ExaConfCriterio> listExaConfCriterio = jdbcTemplate.query(sql, new RowMapper<ExaConfCriterio>() {

			
			public ExaConfCriterio mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listExaConfCriterio;
	}

	
	public ExaConfCriterio get(int id) {
		String sql = "select * from eva_exa_conf_criterio WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ExaConfCriterio>() {

			
			public ExaConfCriterio extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public ExaConfCriterio getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select ex_cri.id ex_cri_id, ex_cri.id_eva_ex ex_cri_id_eva_ex , ex_cri.dur ex_cri_dur , ex_cri.fec_ini_psi ex_cri_fec_ini_psi , ex_cri.fec_fin_psi ex_cri_fec_fin_psi  ,ex_cri.est ex_cri_est ";
		if (aTablas.contains("eva_evaluacion_vac_examen"))
			sql = sql + ", evaex.id evaex_id  , evaex.id_eva evaex_id_eva , evaex.id_eae evaex_id_eae , evaex.id_tae evaex_id_tae , evaex.fec_exa evaex_fec_exa , evaex.fec_not evaex_fec_not  ";
	
		sql = sql + " from eva_exa_conf_criterio ex_cri "; 
		if (aTablas.contains("eva_evaluacion_vac_examen"))
			sql = sql + " left join eva_evaluacion_vac_examen evaex on evaex.id = ex_cri.id_eva_ex ";
		sql = sql + " where ex_cri.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<ExaConfCriterio>() {
		
			
			public ExaConfCriterio extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ExaConfCriterio exaconfcriterio= rsToEntity(rs,"ex_cri_");
					if (aTablas.contains("eva_evaluacion_vac_examen")){
						EvaluacionVacExamen evaluacionvacexamen = new EvaluacionVacExamen();  
							evaluacionvacexamen.setId(rs.getInt("evaex_id")) ;  
							evaluacionvacexamen.setId_eva(rs.getInt("evaex_id_eva")) ;  
							evaluacionvacexamen.setId_eae(rs.getInt("evaex_id_eae")) ;  
							evaluacionvacexamen.setId_tae(rs.getInt("evaex_id_tae")) ;  
							evaluacionvacexamen.setFec_exa(rs.getDate("evaex_fec_exa")) ;  
							evaluacionvacexamen.setFec_not(rs.getDate("evaex_fec_not")) ;  
							exaconfcriterio.setEvaluacionVacExamen(evaluacionvacexamen);
					}
							return exaconfcriterio;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public ExaConfCriterio getByParams(Param param) {

		String sql = "select * from eva_exa_conf_criterio " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ExaConfCriterio>() {
			
			public ExaConfCriterio extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<ExaConfCriterio> listByParams(Param param, String[] order) {

		String sql = "select * from eva_exa_conf_criterio " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<ExaConfCriterio>() {

			
			public ExaConfCriterio mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<ExaConfCriterio> listFullByParams(ExaConfCriterio exaconfcriterio, String[] order) {
	
		return listFullByParams(Param.toParam("ex_cri",exaconfcriterio), order);
	
	}	
	
	
	public List<ExaConfCriterio> listFullByParams(Param param, String[] order) {

		String sql = "select ex_cri.id ex_cri_id, ex_cri.id_eva_ex ex_cri_id_eva_ex , ex_cri.dur ex_cri_dur , ex_cri.fec_ini_psi ex_cri_fec_ini_psi , ex_cri.fec_fin_psi ex_cri_fec_fin_psi  ,ex_cri.est ex_cri_est ";
		sql = sql + ", evaex.id evaex_id  , evaex.id_eva evaex_id_eva , evaex.id_eae evaex_id_eae , evaex.id_tae evaex_id_tae , evaex.fec_exa evaex_fec_exa , evaex.fec_not evaex_fec_not  ";
		sql = sql + " from eva_exa_conf_criterio ex_cri";
		sql = sql + " left join eva_evaluacion_vac_examen evaex on evaex.id = ex_cri.id_eva_ex ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<ExaConfCriterio>() {

			
			public ExaConfCriterio mapRow(ResultSet rs, int rowNum) throws SQLException {
				ExaConfCriterio exaconfcriterio= rsToEntity(rs,"ex_cri_");
				EvaluacionVacExamen evaluacionvacexamen = new EvaluacionVacExamen();  
				evaluacionvacexamen.setId(rs.getInt("evaex_id")) ;  
				evaluacionvacexamen.setId_eva(rs.getInt("evaex_id_eva")) ;  
				evaluacionvacexamen.setId_eae(rs.getInt("evaex_id_eae")) ;  
				evaluacionvacexamen.setId_tae(rs.getInt("evaex_id_tae")) ;  
				evaluacionvacexamen.setFec_exa(rs.getDate("evaex_fec_exa")) ;  
				evaluacionvacexamen.setFec_not(rs.getDate("evaex_fec_not")) ;  
				exaconfcriterio.setEvaluacionVacExamen(evaluacionvacexamen);
				return exaconfcriterio;
			}

		});

	}	


	public List<InsExaCri> getListInsExaCri(Param param, String[] order) {
		String sql = "select * from eva_ins_exa_cri " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<InsExaCri>() {

			
			public InsExaCri mapRow(ResultSet rs, int rowNum) throws SQLException {
				InsExaCri ins_exa_cri = new InsExaCri();

				ins_exa_cri.setId(rs.getInt("id"));
				ins_exa_cri.setId_excri(rs.getInt("id_excri"));
				ins_exa_cri.setId_ins(rs.getInt("id_ins"));
				ins_exa_cri.setEst(rs.getString("est"));
												
				return ins_exa_cri;
			}

		});	
	}


	// funciones privadas utilitarias para ExaConfCriterio

	private ExaConfCriterio rsToEntity(ResultSet rs,String alias) throws SQLException {
		ExaConfCriterio exa_conf_criterio = new ExaConfCriterio();

		exa_conf_criterio.setId(rs.getInt( alias + "id"));
		exa_conf_criterio.setId_eva_ex(rs.getInt( alias + "id_eva_ex"));
		exa_conf_criterio.setDur(rs.getInt( alias + "dur"));
		exa_conf_criterio.setFec_ini_psi(rs.getDate( alias + "fec_ini_psi"));
		exa_conf_criterio.setFec_fin_psi(rs.getDate( alias + "fec_fin_psi"));
		exa_conf_criterio.setEst(rs.getString( alias + "est"));
								
		return exa_conf_criterio;

	}
	
}

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
import com.tesla.colegio.model.EvaluacionVacExamen;

import com.tesla.colegio.model.EvaluacionVac;
import com.tesla.colegio.model.AreaEva;
import com.tesla.colegio.model.TipEva;
import com.tesla.colegio.model.ExaConfMarcacion;
import com.tesla.colegio.model.ExaConfEscrito;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface EvaluacionVacExamenDAO.
 * @author MV
 *
 */
public class EvaluacionVacExamenDAOImpl{
	final static Logger logger = Logger.getLogger(EvaluacionVacExamenDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	//@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(EvaluacionVacExamen evaluacion_vac_examen) {
		if (evaluacion_vac_examen.getId() != null) {
			// update
			String sql = "UPDATE eva_evaluacion_vac_examen "
						+ "SET id_eva=?, "
						+ "id_eae=?, "
						+ "id_tae=?, "
						+ "fec_exa=?, "
						+ "fec_not=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			 jdbcTemplate.update(sql, 
						evaluacion_vac_examen.getId_eva(),
						evaluacion_vac_examen.getId_eae(),
						evaluacion_vac_examen.getId_tae(),
						evaluacion_vac_examen.getFec_exa(),
						evaluacion_vac_examen.getFec_not(),
						evaluacion_vac_examen.getEst(),
						evaluacion_vac_examen.getUsr_act(),
						new java.util.Date(),
						evaluacion_vac_examen.getId());
			 
			 return evaluacion_vac_examen.getId();

		} else {
			// insert
			String sql = "insert into eva_evaluacion_vac_examen ("
						+ "id_eva, "
						+ "id_eae, "
						+ "id_tae, "
						+ "fec_exa, "
						+ "fec_not, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				evaluacion_vac_examen.getId_eva(),
				evaluacion_vac_examen.getId_eae(),
				evaluacion_vac_examen.getId_tae(),
				evaluacion_vac_examen.getFec_exa(),
				evaluacion_vac_examen.getFec_not(),
				evaluacion_vac_examen.getEst(),
				evaluacion_vac_examen.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from eva_evaluacion_vac_examen where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<EvaluacionVacExamen> list() {
		String sql = "select * from eva_evaluacion_vac_examen";
		
		//logger.info(sql);
		
		List<EvaluacionVacExamen> listEvaluacionVacExamen = jdbcTemplate.query(sql, new RowMapper<EvaluacionVacExamen>() {

			
			public EvaluacionVacExamen mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listEvaluacionVacExamen;
	}

	
	public EvaluacionVacExamen get(int id) {
		String sql = "select * from eva_evaluacion_vac_examen WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<EvaluacionVacExamen>() {

			
			public EvaluacionVacExamen extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public EvaluacionVacExamen getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select evaex.id evaex_id, evaex.id_eva evaex_id_eva , evaex.id_eae evaex_id_eae , evaex.id_tae evaex_id_tae , evaex.fec_exa evaex_fec_exa , evaex.fec_not evaex_fec_not  ,evaex.est evaex_est ";
		if (aTablas.contains("eva_evaluacion_vac"))
			sql = sql + ", eva_vac.id eva_vac_id  , eva_vac.id_per eva_vac_id_per , eva_vac.des eva_vac_des , eva_vac.precio eva_vac_precio  ";
		if (aTablas.contains("eva_area_eva"))
			sql = sql + ", eae.id eae_id  , eae.nom eae_nom  ";
		if (aTablas.contains("eva_tip_eva"))
			sql = sql + ", tae.id tae_id  , tae.nom tae_nom , tae.tabla tae_tabla  ";
	
		sql = sql + " from eva_evaluacion_vac_examen evaex "; 
		if (aTablas.contains("eva_evaluacion_vac"))
			sql = sql + " left join eva_evaluacion_vac eva_vac on eva_vac.id = evaex.id_eva ";
		if (aTablas.contains("eva_area_eva"))
			sql = sql + " left join eva_area_eva eae on eae.id = evaex.id_eae ";
		if (aTablas.contains("eva_tip_eva"))
			sql = sql + " left join eva_tip_eva tae on tae.id = evaex.id_tae ";
		sql = sql + " where evaex.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<EvaluacionVacExamen>() {
		
			
			public EvaluacionVacExamen extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					EvaluacionVacExamen evaluacionvacexamen= rsToEntity(rs,"evaex_");
					if (aTablas.contains("eva_evaluacion_vac")){
						EvaluacionVac evaluacionvac = new EvaluacionVac();  
							evaluacionvac.setId(rs.getInt("eva_vac_id")) ;  
							evaluacionvac.setId_per(rs.getInt("eva_vac_id_per")) ;  
							evaluacionvac.setDes(rs.getString("eva_vac_des")) ;  
							evaluacionvac.setPrecio(rs.getBigDecimal("eva_vac_precio")) ;  
							evaluacionvacexamen.setEvaluacionVac(evaluacionvac);
					}
					if (aTablas.contains("eva_area_eva")){
						AreaEva areaeva = new AreaEva();  
							areaeva.setId(rs.getInt("eae_id")) ;  
							areaeva.setNom(rs.getString("eae_nom")) ;  
							evaluacionvacexamen.setAreaEva(areaeva);
					}
					if (aTablas.contains("eva_tip_eva")){
						TipEva tipeva = new TipEva();  
							tipeva.setId(rs.getInt("tae_id")) ;  
							tipeva.setNom(rs.getString("tae_nom")) ;  
							tipeva.setTabla(rs.getString("tae_tabla")) ;  
							evaluacionvacexamen.setTipEva(tipeva);
					}
							return evaluacionvacexamen;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public EvaluacionVacExamen getByParams(Param param) {

		String sql = "select * from eva_evaluacion_vac_examen " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<EvaluacionVacExamen>() {
			
			public EvaluacionVacExamen extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<EvaluacionVacExamen> listByParams(Param param, String[] order) {

		String sql = "select * from eva_evaluacion_vac_examen " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<EvaluacionVacExamen>() {

			
			public EvaluacionVacExamen mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<EvaluacionVacExamen> listFullByParams(EvaluacionVacExamen evaluacionvacexamen, String[] order) {
	
		return listFullByParams(Param.toParam("evaex",evaluacionvacexamen), order);
	
	}	
	
	
	public List<EvaluacionVacExamen> listFullByParams(Param param, String[] order) {

		String sql = "select evaex.id evaex_id, evaex.id_eva evaex_id_eva , evaex.id_eae evaex_id_eae , evaex.id_tae evaex_id_tae , evaex.fec_exa evaex_fec_exa , evaex.fec_not evaex_fec_not  ,evaex.est evaex_est ";
		sql = sql + ", eva_vac.id eva_vac_id  , eva_vac.id_per eva_vac_id_per , eva_vac.des eva_vac_des , eva_vac.precio eva_vac_precio  ";
		sql = sql + ", eae.id eae_id  , eae.nom eae_nom  ";
		sql = sql + ", tae.id tae_id  , tae.nom tae_nom , tae.tabla tae_tabla  ";
		sql = sql + " from eva_evaluacion_vac_examen evaex";
		sql = sql + " left join eva_evaluacion_vac eva_vac on eva_vac.id = evaex.id_eva ";
		sql = sql + " left join eva_area_eva eae on eae.id = evaex.id_eae ";
		sql = sql + " left join eva_tip_eva tae on tae.id = evaex.id_tae ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<EvaluacionVacExamen>() {

			
			public EvaluacionVacExamen mapRow(ResultSet rs, int rowNum) throws SQLException {
				EvaluacionVacExamen evaluacionvacexamen= rsToEntity(rs,"evaex_");
				EvaluacionVac evaluacionvac = new EvaluacionVac();  
				evaluacionvac.setId(rs.getInt("eva_vac_id")) ;  
				evaluacionvac.setId_per(rs.getInt("eva_vac_id_per")) ;  
				evaluacionvac.setDes(rs.getString("eva_vac_des")) ;  
				evaluacionvac.setPrecio(rs.getBigDecimal("eva_vac_precio")) ;  
				evaluacionvacexamen.setEvaluacionVac(evaluacionvac);
				AreaEva areaeva = new AreaEva();  
				areaeva.setId(rs.getInt("eae_id")) ;  
				areaeva.setNom(rs.getString("eae_nom")) ;  
				evaluacionvacexamen.setAreaEva(areaeva);
				TipEva tipeva = new TipEva();  
				tipeva.setId(rs.getInt("tae_id")) ;  
				tipeva.setNom(rs.getString("tae_nom")) ;  
				tipeva.setTabla(rs.getString("tae_tabla")) ;  
				evaluacionvacexamen.setTipEva(tipeva);
				return evaluacionvacexamen;
			}

		});

	}	


	public List<ExaConfMarcacion> getListExaConfMarcacion(Param param, String[] order) {
		String sql = "select * from eva_exa_conf_marcacion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<ExaConfMarcacion>() {

			
			public ExaConfMarcacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				ExaConfMarcacion exa_conf_marcacion = new ExaConfMarcacion();

				exa_conf_marcacion.setId(rs.getInt("id"));
				exa_conf_marcacion.setId_eva_ex(rs.getInt("id_eva_ex"));
				exa_conf_marcacion.setNum_pre(rs.getInt("num_pre"));
				exa_conf_marcacion.setPje_pre_cor(rs.getBigDecimal("pje_pre_cor"));
				exa_conf_marcacion.setPje_pre_inc(rs.getBigDecimal("pje_pre_inc"));
				exa_conf_marcacion.setEst(rs.getString("est"));
												
				return exa_conf_marcacion;
			}

		});	
	}
	public List<ExaConfEscrito> getListExaConfEscrito(Param param, String[] order) {
		String sql = "select * from eva_exa_conf_escrito " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<ExaConfEscrito>() {

			
			public ExaConfEscrito mapRow(ResultSet rs, int rowNum) throws SQLException {
				ExaConfEscrito exa_conf_escrito = new ExaConfEscrito();

				exa_conf_escrito.setId(rs.getInt("id"));
				exa_conf_escrito.setId_eva_ex(rs.getInt("id_eva_ex"));
				exa_conf_escrito.setNum_pre(rs.getInt("num_pre"));
				exa_conf_escrito.setPje_pre_cor(rs.getBigDecimal("pje_pre_cor"));
				exa_conf_escrito.setPje_pre_inc(rs.getBigDecimal("pje_pre_inc"));
				exa_conf_escrito.setEst(rs.getString("est"));
												
				return exa_conf_escrito;
			}

		});	
	}


	// funciones privadas utilitarias para EvaluacionVacExamen

	private EvaluacionVacExamen rsToEntity(ResultSet rs,String alias) throws SQLException {
		EvaluacionVacExamen evaluacion_vac_examen = new EvaluacionVacExamen();

		evaluacion_vac_examen.setId(rs.getInt( alias + "id"));
		evaluacion_vac_examen.setId_eva(rs.getInt( alias + "id_eva"));
		evaluacion_vac_examen.setId_eae(rs.getInt( alias + "id_eae"));
		evaluacion_vac_examen.setId_tae(rs.getInt( alias + "id_tae"));
		evaluacion_vac_examen.setFec_exa(rs.getDate( alias + "fec_exa"));
		evaluacion_vac_examen.setFec_not(rs.getDate( alias + "fec_not"));
		evaluacion_vac_examen.setEst(rs.getString( alias + "est"));
								
		return evaluacion_vac_examen;

	}
	
}

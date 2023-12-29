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
import com.tesla.colegio.model.Examen;

import com.tesla.colegio.model.EvaluacionVac;
import com.tesla.colegio.model.AreaEva;
import com.tesla.colegio.model.TipEva;
import com.tesla.colegio.model.NotaCriterio;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ExamenDAO.
 * @author MV
 *
 */
public class ExamenDAOImpl{
	final static Logger logger = Logger.getLogger(ExamenDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Examen examen) {
		if (examen.getId() != null) {
			// update
			String sql = "UPDATE eva_examen "
						+ "SET id_eva=?, "
						+ "id_eae=?, "
						+ "id_tae=?, "
						+ "fec_exa=?, "
						+ "fec_not=?, "
						+ "precio=?, "
						+ "pje_pre_cor=?, "
						+ "pje_pre_inc=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						examen.getId_eva(),
						examen.getId_eae(),
						examen.getId_tae(),
						examen.getFec_exa(),
						examen.getFec_not(),
						examen.getPrecio(),
						examen.getPje_pre_cor(),
						examen.getPje_pre_inc(),
						examen.getEst(),
						examen.getUsr_act(),
						new java.util.Date(),
						examen.getId()); 

		} else {
			// insert
			String sql = "insert into eva_examen ("
						+ "id_eva, "
						+ "id_eae, "
						+ "id_tae, "
						+ "fec_exa, "
						+ "fec_not, "
						+ "precio, "
						+ "pje_pre_cor, "
						+ "pje_pre_inc, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				examen.getId_eva(),
				examen.getId_eae(),
				examen.getId_tae(),
				examen.getFec_exa(),
				examen.getFec_not(),
				examen.getPrecio(),
				examen.getPje_pre_cor(),
				examen.getPje_pre_inc(),
				examen.getEst(),
				examen.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from eva_examen where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<Examen> list() {
		String sql = "select * from eva_examen";
		
		//logger.info(sql);
		
		List<Examen> listExamen = jdbcTemplate.query(sql, new RowMapper<Examen>() {

			
			public Examen mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listExamen;
	}

	
	public Examen get(int id) {
		String sql = "select * from eva_examen WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Examen>() {

			
			public Examen extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public Examen getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select ex_vac.id ex_vac_id, ex_vac.id_eva ex_vac_id_eva , ex_vac.id_eae ex_vac_id_eae , ex_vac.id_tae ex_vac_id_tae , ex_vac.fec_exa ex_vac_fec_exa , ex_vac.fec_not ex_vac_fec_not , ex_vac.precio ex_vac_precio , ex_vac.pje_pre_cor ex_vac_pje_pre_cor , ex_vac.pje_pre_inc ex_vac_pje_pre_inc  ,ex_vac.est ex_vac_est ";
		if (aTablas.contains("eva_evaluacion_vac"))
			sql = sql + ", eva_vac.id eva_vac_id  , eva_vac.id_per eva_vac_id_per , eva_vac.des eva_vac_des  ";
		if (aTablas.contains("eva_area_eva"))
			sql = sql + ", eae.id eae_id  , eae.nom eae_nom  ";
		if (aTablas.contains("eva_tip_eva"))
			sql = sql + ", tae.id tae_id  , tae.nom tae_nom  ";
	
		sql = sql + " from eva_examen ex_vac "; 
		if (aTablas.contains("eva_evaluacion_vac"))
			sql = sql + " left join eva_evaluacion_vac eva_vac on eva_vac.id = ex_vac.id_eva ";
		if (aTablas.contains("eva_area_eva"))
			sql = sql + " left join eva_area_eva eae on eae.id = ex_vac.id_eae ";
		if (aTablas.contains("eva_tip_eva"))
			sql = sql + " left join eva_tip_eva tae on tae.id = ex_vac.id_tae ";
		sql = sql + " where ex_vac.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Examen>() {
		
			
			public Examen extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Examen examen= rsToEntity(rs,"ex_vac_");
					if (aTablas.contains("eva_evaluacion_vac")){
						EvaluacionVac evaluacionvac = new EvaluacionVac();  
							evaluacionvac.setId(rs.getInt("eva_vac_id")) ;  
							evaluacionvac.setId_per(rs.getInt("eva_vac_id_per")) ;  
							evaluacionvac.setDes(rs.getString("eva_vac_des")) ;  
							examen.setEvaluacionVac(evaluacionvac);
					}
					if (aTablas.contains("eva_area_eva")){
						AreaEva areaeva = new AreaEva();  
							areaeva.setId(rs.getInt("eae_id")) ;  
							areaeva.setNom(rs.getString("eae_nom")) ;  
							examen.setAreaEva(areaeva);
					}
					if (aTablas.contains("eva_tip_eva")){
						TipEva tipeva = new TipEva();  
							tipeva.setId(rs.getInt("tae_id")) ;  
							tipeva.setNom(rs.getString("tae_nom")) ;  
							examen.setTipEva(tipeva);
					}
							return examen;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public Examen getByParams(Param param) {

		String sql = "select * from eva_examen " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Examen>() {
			
			public Examen extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<Examen> listByParams(Param param, String[] order) {

		String sql = "select * from eva_examen " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Examen>() {

			
			public Examen mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<Examen> listFullByParams(Examen examen, String[] order) {
	
		return listFullByParams(Param.toParam("ex_vac",examen), order);
	
	}	
	
	
	public List<Examen> listFullByParams(Param param, String[] order) {

		String sql = "select ex_vac.id ex_vac_id, ex_vac.id_eva ex_vac_id_eva , ex_vac.id_eae ex_vac_id_eae , ex_vac.id_tae ex_vac_id_tae , ex_vac.fec_exa ex_vac_fec_exa , ex_vac.fec_not ex_vac_fec_not , ex_vac.precio ex_vac_precio , ex_vac.pje_pre_cor ex_vac_pje_pre_cor , ex_vac.pje_pre_inc ex_vac_pje_pre_inc  ,ex_vac.est ex_vac_est ";
		sql = sql + ", eva_vac.id eva_vac_id  , eva_vac.id_per eva_vac_id_per , eva_vac.des eva_vac_des  ";
		sql = sql + ", eae.id eae_id  , eae.nom eae_nom  ";
		sql = sql + ", tae.id tae_id  , tae.nom tae_nom  ";
		sql = sql + " from eva_examen ex_vac";
		sql = sql + " left join eva_evaluacion_vac eva_vac on eva_vac.id = ex_vac.id_eva ";
		sql = sql + " left join eva_area_eva eae on eae.id = ex_vac.id_eae ";
		sql = sql + " left join eva_tip_eva tae on tae.id = ex_vac.id_tae ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Examen>() {

			
			public Examen mapRow(ResultSet rs, int rowNum) throws SQLException {
				Examen examen= rsToEntity(rs,"ex_vac_");
				EvaluacionVac evaluacionvac = new EvaluacionVac();  
				evaluacionvac.setId(rs.getInt("eva_vac_id")) ;  
				evaluacionvac.setId_per(rs.getInt("eva_vac_id_per")) ;  
				evaluacionvac.setDes(rs.getString("eva_vac_des")) ;  
				examen.setEvaluacionVac(evaluacionvac);
				AreaEva areaeva = new AreaEva();  
				areaeva.setId(rs.getInt("eae_id")) ;  
				areaeva.setNom(rs.getString("eae_nom")) ;  
				examen.setAreaEva(areaeva);
				TipEva tipeva = new TipEva();  
				tipeva.setId(rs.getInt("tae_id")) ;  
				tipeva.setNom(rs.getString("tae_nom")) ;  
				examen.setTipEva(tipeva);
				return examen;
			}

		});

	}	


	public List<NotaCriterio> getListNotaCriterio(Param param, String[] order) {
		String sql = "select * from eva_nota_criterio " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<NotaCriterio>() {

			
			public NotaCriterio mapRow(ResultSet rs, int rowNum) throws SQLException {
				NotaCriterio nota_criterio = new NotaCriterio();

				nota_criterio.setId(rs.getInt("id"));
				nota_criterio.setId_alu(rs.getInt("id_alu"));
				nota_criterio.setId_exa(rs.getInt("id_exa"));
				nota_criterio.setNum(rs.getInt("num"));
				nota_criterio.setInstr_tecn1(rs.getString("instr_tecn1"));
				nota_criterio.setInstr_tecn2(rs.getString("instr_tecn2"));
				nota_criterio.setInstr_tecn3(rs.getString("instr_tecn3"));
				nota_criterio.setInstr_tecn4(rs.getString("instr_tecn4"));
				nota_criterio.setResultado(rs.getString("resultado"));
				nota_criterio.setApto(rs.getString("apto"));
				nota_criterio.setConcl_recom1(rs.getString("concl_recom1"));
				nota_criterio.setConcl_recom2(rs.getString("concl_recom2"));
				nota_criterio.setConcl_recom3(rs.getString("concl_recom3"));
				nota_criterio.setConcl_recom4(rs.getString("concl_recom4"));
				nota_criterio.setConcl_recom5(rs.getString("concl_recom5"));
				nota_criterio.setConcl_recom6(rs.getString("concl_recom6"));
				nota_criterio.setEst(rs.getString("est"));
												
				return nota_criterio;
			}

		});	
	}


	// funciones privadas utilitarias para Examen

	private Examen rsToEntity(ResultSet rs,String alias) throws SQLException {
		Examen examen = new Examen();

		examen.setId(rs.getInt( alias + "id"));
		examen.setId_eva(rs.getInt( alias + "id_eva"));
		examen.setId_eae(rs.getInt( alias + "id_eae"));
		examen.setId_tae(rs.getInt( alias + "id_tae"));
		examen.setFec_exa(rs.getDate( alias + "fec_exa"));
		examen.setFec_not(rs.getDate( alias + "fec_not"));
		examen.setPrecio(rs.getBigDecimal( alias + "precio"));
		examen.setPje_pre_cor(rs.getBigDecimal( alias + "pje_pre_cor"));
		examen.setPje_pre_inc(rs.getBigDecimal( alias + "pje_pre_inc"));
		examen.setEst(rs.getString( alias + "est"));
								
		return examen;

	}
	
}

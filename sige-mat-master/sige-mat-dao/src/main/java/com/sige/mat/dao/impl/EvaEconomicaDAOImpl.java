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
import com.tesla.colegio.model.EvaEconomica;

import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.EvaResultado;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface EvaEconomicaDAO.
 * @author MV
 *
 */
public class EvaEconomicaDAOImpl{
	final static Logger logger = Logger.getLogger(EvaEconomicaDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(EvaEconomica eva_economica) {
		if (eva_economica.getId() != null) {
			// update
			String sql = "UPDATE eco_eva_economica "
						+ "SET id_fam=?, "
						+ "ptj=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						eva_economica.getId_fam(),
						eva_economica.getPtj(),
						eva_economica.getEst(),
						eva_economica.getUsr_act(),
						new java.util.Date(),
						eva_economica.getId()); 
			return eva_economica.getId();

		} else {
			// insert
			String sql = "insert into eco_eva_economica ("
						+ "id_fam, "
						+ "ptj, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				eva_economica.getId_fam(),
				eva_economica.getPtj(),
				eva_economica.getEst(),
				eva_economica.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from eco_eva_economica where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<EvaEconomica> list() {
		String sql = "select * from eco_eva_economica";
		
		//logger.info(sql);
		
		List<EvaEconomica> listEvaEconomica = jdbcTemplate.query(sql, new RowMapper<EvaEconomica>() {

			@Override
			public EvaEconomica mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listEvaEconomica;
	}

	public EvaEconomica get(int id) {
		String sql = "select * from eco_eva_economica WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<EvaEconomica>() {

			@Override
			public EvaEconomica extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public EvaEconomica getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select eve.id eve_id, eve.id_fam eve_id_fam , eve.ptj eve_ptj  ,eve.est eve_est ";
		if (aTablas.contains("alu_familiar"))
			sql = sql + ", fam.id fam_id  , fam.nom fam_nom  ";
	
		sql = sql + " from eco_eva_economica eve "; 
		if (aTablas.contains("alu_familiar"))
			sql = sql + " left join alu_familiar fam on fam.id = eve.id_fam ";
		sql = sql + " where eve.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<EvaEconomica>() {
		
			@Override
			public EvaEconomica extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					EvaEconomica evaeconomica= rsToEntity(rs,"eve_");
					if (aTablas.contains("alu_familiar")){
						Familiar familiar = new Familiar();  
							familiar.setId(rs.getInt("fam_id")) ;  
							familiar.setNom(rs.getString("fam_nom")) ;  
							evaeconomica.setFamiliar(familiar);
					}
							return evaeconomica;
				}
				
				return null;
			}
			
		});


	}		
	
	public EvaEconomica getByParams(Param param) {

		String sql = "select * from eco_eva_economica " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<EvaEconomica>() {
			@Override
			public EvaEconomica extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<EvaEconomica> listByParams(Param param, String[] order) {

		String sql = "select * from eco_eva_economica " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<EvaEconomica>() {

			@Override
			public EvaEconomica mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<EvaEconomica> listFullByParams(EvaEconomica evaeconomica, String[] order) {
	
		return listFullByParams(Param.toParam("eve",evaeconomica), order);
	
	}	
	
	public List<EvaEconomica> listFullByParams(Param param, String[] order) {

		String sql = "select eve.id eve_id, eve.id_fam eve_id_fam , eve.ptj eve_ptj  ,eve.est eve_est ";
		sql = sql + ", fam.id fam_id  , fam.nom fam_nom  ";
		sql = sql + " from eco_eva_economica eve";
		sql = sql + " left join alu_familiar fam on fam.id = eve.id_fam ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<EvaEconomica>() {

			@Override
			public EvaEconomica mapRow(ResultSet rs, int rowNum) throws SQLException {
				EvaEconomica evaeconomica= rsToEntity(rs,"eve_");
				Familiar familiar = new Familiar();  
				familiar.setId(rs.getInt("fam_id")) ;  
				familiar.setNom(rs.getString("fam_nom")) ;  
				evaeconomica.setFamiliar(familiar);
				return evaeconomica;
			}

		});

	}	


	public List<EvaResultado> getListEvaResultado(Param param, String[] order) {
		String sql = "select * from eco_eva_resultado " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<EvaResultado>() {

			@Override
			public EvaResultado mapRow(ResultSet rs, int rowNum) throws SQLException {
				EvaResultado eva_resultado = new EvaResultado();

				eva_resultado.setId(rs.getInt("id"));
				eva_resultado.setId_eve(rs.getInt("id_eve"));
				eva_resultado.setId_ecc(rs.getInt("id_ecc"));
				eva_resultado.setVal_resp(rs.getString("val_resp"));
				eva_resultado.setEst(rs.getString("est"));
												
				return eva_resultado;
			}

		});	
	}


	// funciones privadas utilitarias para EvaEconomica

	private EvaEconomica rsToEntity(ResultSet rs,String alias) throws SQLException {
		EvaEconomica eva_economica = new EvaEconomica();

		eva_economica.setId(rs.getInt( alias + "id"));
		eva_economica.setId_fam(rs.getInt( alias + "id_fam"));
		eva_economica.setPtj(rs.getBigDecimal( alias + "ptj"));
		eva_economica.setEst(rs.getString( alias + "est"));
								
		return eva_economica;

	}
	
}

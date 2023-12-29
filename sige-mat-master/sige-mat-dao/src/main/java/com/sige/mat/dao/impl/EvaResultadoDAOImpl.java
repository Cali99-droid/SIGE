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
import com.tesla.colegio.model.EvaResultado;

import com.tesla.colegio.model.EvaEconomica;
import com.tesla.colegio.model.Configuracion;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface EvaResultadoDAO.
 * @author MV
 *
 */
public class EvaResultadoDAOImpl{
	final static Logger logger = Logger.getLogger(EvaResultadoDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(EvaResultado eva_resultado) {
		if (eva_resultado.getId() != null) {
			// update
			String sql = "UPDATE eco_eva_resultado "
						+ "SET id_eve=?, "
						+ "id_ecc=?, "
						+ "val_resp=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						eva_resultado.getId_eve(),
						eva_resultado.getId_ecc(),
						eva_resultado.getVal_resp(),
						eva_resultado.getEst(),
						eva_resultado.getUsr_act(),
						new java.util.Date(),
						eva_resultado.getId()); 
			return eva_resultado.getId();

		} else {
			// insert
			String sql = "insert into eco_eva_resultado ("
						+ "id_eve, "
						+ "id_ecc, "
						+ "val_resp, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				eva_resultado.getId_eve(),
				eva_resultado.getId_ecc(),
				eva_resultado.getVal_resp(),
				eva_resultado.getEst(),
				eva_resultado.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from eco_eva_resultado where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<EvaResultado> list() {
		String sql = "select * from eco_eva_resultado";
		
		//logger.info(sql);
		
		List<EvaResultado> listEvaResultado = jdbcTemplate.query(sql, new RowMapper<EvaResultado>() {

			@Override
			public EvaResultado mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listEvaResultado;
	}

	public EvaResultado get(int id) {
		String sql = "select * from eco_eva_resultado WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<EvaResultado>() {

			@Override
			public EvaResultado extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public EvaResultado getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select ere.id ere_id, ere.id_eve ere_id_eve , ere.id_ecc ere_id_ecc , ere.val_resp ere_val_resp  ,ere.est ere_est ";
		if (aTablas.contains("eco_eva_economica"))
			sql = sql + ", eve.id eve_id  , eve.id_fam eve_id_fam , eve.ptj eve_ptj  ";
		if (aTablas.contains("eco_configuracion"))
			sql = sql + ", ecc.id ecc_id  , ecc.condicion ecc_condicion , ecc.tip_resp ecc_tip_resp , ecc.id_ect ecc_id_ect , ecc.ptj ecc_ptj  ";
	
		sql = sql + " from eco_eva_resultado ere "; 
		if (aTablas.contains("eco_eva_economica"))
			sql = sql + " left join eco_eva_economica eve on eve.id = ere.id_eve ";
		if (aTablas.contains("eco_configuracion"))
			sql = sql + " left join eco_configuracion ecc on ecc.id = ere.id_ecc ";
		sql = sql + " where ere.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<EvaResultado>() {
		
			@Override
			public EvaResultado extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					EvaResultado evaresultado= rsToEntity(rs,"ere_");
					if (aTablas.contains("eco_eva_economica")){
						EvaEconomica evaeconomica = new EvaEconomica();  
							evaeconomica.setId(rs.getInt("eve_id")) ;  
							evaeconomica.setId_fam(rs.getInt("eve_id_fam")) ;  
							evaeconomica.setPtj(rs.getBigDecimal("eve_ptj")) ;  
							evaresultado.setEvaEconomica(evaeconomica);
					}
					if (aTablas.contains("eco_configuracion")){
						Configuracion configuracion = new Configuracion();  
							configuracion.setId(rs.getInt("ecc_id")) ;  
							configuracion.setCondicion(rs.getString("ecc_condicion")) ;  
							configuracion.setTip_resp(rs.getString("ecc_tip_resp")) ;  
							configuracion.setId_ect(rs.getInt("ecc_id_ect")) ;  
							configuracion.setPtj(rs.getBigDecimal("ecc_ptj")) ;  
							evaresultado.setConfiguracion(configuracion);
					}
							return evaresultado;
				}
				
				return null;
			}
			
		});


	}		
	
	public EvaResultado getByParams(Param param) {

		String sql = "select * from eco_eva_resultado " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<EvaResultado>() {
			@Override
			public EvaResultado extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<EvaResultado> listByParams(Param param, String[] order) {

		String sql = "select * from eco_eva_resultado " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<EvaResultado>() {

			@Override
			public EvaResultado mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<EvaResultado> listFullByParams(EvaResultado evaresultado, String[] order) {
	
		return listFullByParams(Param.toParam("ere",evaresultado), order);
	
	}	
	
	public List<EvaResultado> listFullByParams(Param param, String[] order) {

		String sql = "select ere.id ere_id, ere.id_eve ere_id_eve , ere.id_ecc ere_id_ecc , ere.val_resp ere_val_resp  ,ere.est ere_est ";
		sql = sql + ", eve.id eve_id  , eve.id_fam eve_id_fam , eve.ptj eve_ptj  ";
		sql = sql + ", ecc.id ecc_id  , ecc.condicion ecc_condicion , ecc.tip_resp ecc_tip_resp , ecc.id_ect ecc_id_ect , ecc.ptj ecc_ptj  ";
		sql = sql + " from eco_eva_resultado ere";
		sql = sql + " left join eco_eva_economica eve on eve.id = ere.id_eve ";
		sql = sql + " left join eco_configuracion ecc on ecc.id = ere.id_ecc ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<EvaResultado>() {

			@Override
			public EvaResultado mapRow(ResultSet rs, int rowNum) throws SQLException {
				EvaResultado evaresultado= rsToEntity(rs,"ere_");
				EvaEconomica evaeconomica = new EvaEconomica();  
				evaeconomica.setId(rs.getInt("eve_id")) ;  
				evaeconomica.setId_fam(rs.getInt("eve_id_fam")) ;  
				evaeconomica.setPtj(rs.getBigDecimal("eve_ptj")) ;  
				evaresultado.setEvaEconomica(evaeconomica);
				Configuracion configuracion = new Configuracion();  
				configuracion.setId(rs.getInt("ecc_id")) ;  
				configuracion.setCondicion(rs.getString("ecc_condicion")) ;  
				configuracion.setTip_resp(rs.getString("ecc_tip_resp")) ;  
				configuracion.setId_ect(rs.getInt("ecc_id_ect")) ;  
				configuracion.setPtj(rs.getBigDecimal("ecc_ptj")) ;  
				evaresultado.setConfiguracion(configuracion);
				return evaresultado;
			}

		});

	}	




	// funciones privadas utilitarias para EvaResultado

	private EvaResultado rsToEntity(ResultSet rs,String alias) throws SQLException {
		EvaResultado eva_resultado = new EvaResultado();

		eva_resultado.setId(rs.getInt( alias + "id"));
		eva_resultado.setId_eve(rs.getInt( alias + "id_eve"));
		eva_resultado.setId_ecc(rs.getInt( alias + "id_ecc"));
		eva_resultado.setVal_resp(rs.getString( alias + "val_resp"));
		eva_resultado.setEst(rs.getString( alias + "est"));
								
		return eva_resultado;

	}
	
}

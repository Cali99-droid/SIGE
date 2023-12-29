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
import com.tesla.colegio.model.ConceptoPago;

import com.tesla.colegio.model.PagoProgramacion;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ConceptoPagoDAO.
 * @author MV
 *
 */
public class ConceptoPagoDAOImpl{
	final static Logger logger = Logger.getLogger(ConceptoPagoDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(ConceptoPago concepto_pago) {
		if (concepto_pago.getId() != null) {
			// update
			String sql = "UPDATE cat_concepto_pago "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						concepto_pago.getNom(),
						concepto_pago.getEst(),
						concepto_pago.getUsr_act(),
						new java.util.Date(),
						concepto_pago.getId()); 

		} else {
			// insert
			String sql = "insert into cat_concepto_pago ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				concepto_pago.getNom(),
				concepto_pago.getEst(),
				concepto_pago.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from cat_concepto_pago where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<ConceptoPago> list() {
		String sql = "select * from cat_concepto_pago";
		
		//logger.info(sql);
		
		List<ConceptoPago> listConceptoPago = jdbcTemplate.query(sql, new RowMapper<ConceptoPago>() {

			
			public ConceptoPago mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listConceptoPago;
	}

	
	public ConceptoPago get(int id) {
		String sql = "select * from cat_concepto_pago WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ConceptoPago>() {

			
			public ConceptoPago extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public ConceptoPago getFull(int id, String tablas[]) {
		String sql = "select cpa.id cpa_id, cpa.nom cpa_nom  ,cpa.est cpa_est ";
	
		sql = sql + " from cat_concepto_pago cpa "; 
		sql = sql + " where cpa.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<ConceptoPago>() {
		
			
			public ConceptoPago extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ConceptoPago conceptopago= rsToEntity(rs,"cpa_");
							return conceptopago;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public ConceptoPago getByParams(Param param) {

		String sql = "select * from cat_concepto_pago " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ConceptoPago>() {
			
			public ConceptoPago extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<ConceptoPago> listByParams(Param param, String[] order) {

		String sql = "select * from cat_concepto_pago " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<ConceptoPago>() {

			
			public ConceptoPago mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<ConceptoPago> listFullByParams(ConceptoPago conceptopago, String[] order) {
	
		return listFullByParams(Param.toParam("cpa",conceptopago), order);
	
	}	
	
	
	public List<ConceptoPago> listFullByParams(Param param, String[] order) {

		String sql = "select cpa.id cpa_id, cpa.nom cpa_nom  ,cpa.est cpa_est ";
		sql = sql + " from cat_concepto_pago cpa";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<ConceptoPago>() {

			
			public ConceptoPago mapRow(ResultSet rs, int rowNum) throws SQLException {
				ConceptoPago conceptopago= rsToEntity(rs,"cpa_");
				return conceptopago;
			}

		});

	}	


	public List<PagoProgramacion> getListPagoProgramacion(Param param, String[] order) {
		String sql = "select * from pag_pago_programacion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<PagoProgramacion>() {

			
			public PagoProgramacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				PagoProgramacion pago_programacion = new PagoProgramacion();

				pago_programacion.setId(rs.getInt("id"));
				pago_programacion.setId_cpa(rs.getInt("id_cpa"));
				pago_programacion.setId_per(rs.getInt("id_per"));
				pago_programacion.setMonto(rs.getBigDecimal("monto"));
				pago_programacion.setMes(rs.getString("mes"));
				pago_programacion.setFec(rs.getDate("fec"));
				pago_programacion.setEst(rs.getString("est"));
												
				return pago_programacion;
			}

		});	
	}


	// funciones privadas utilitarias para ConceptoPago

	private ConceptoPago rsToEntity(ResultSet rs,String alias) throws SQLException {
		ConceptoPago concepto_pago = new ConceptoPago();

		concepto_pago.setId(rs.getInt( alias + "id"));
		concepto_pago.setNom(rs.getString( alias + "nom"));
		concepto_pago.setEst(rs.getString( alias + "est"));
								
		return concepto_pago;

	}
	
}

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
import com.tesla.colegio.model.TipoPago;



import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface TipoPagoDAO.
 * @author MV
 *
 */
public class TipoPagoDAOImpl{
	final static Logger logger = Logger.getLogger(TipoPagoDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(TipoPago tipo_pago) {
		if (tipo_pago.getId() > 0) {
			// update
			String sql = "UPDATE asi_tipo_pago "
						+ "SET nom=?, "
						+ "des=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						tipo_pago.getNom(),
						tipo_pago.getDes(),
						tipo_pago.getEst(),
						tipo_pago.getUsr_act(),
						new java.util.Date(),
						tipo_pago.getId()); 

		} else {
			// insert
			String sql = "insert into asi_tipo_pago ("
						+ "nom, "
						+ "des, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				tipo_pago.getNom(),
				tipo_pago.getDes(),
				tipo_pago.getEst(),
				tipo_pago.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from asi_tipo_pago where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<TipoPago> list() {
		String sql = "select * from asi_tipo_pago";
		
		//logger.info(sql);
		
		List<TipoPago> listTipoPago = jdbcTemplate.query(sql, new RowMapper<TipoPago>() {

			
			public TipoPago mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listTipoPago;
	}

	
	public TipoPago get(int id) {
		String sql = "select * from asi_tipo_pago WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoPago>() {

			
			public TipoPago extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public TipoPago getFull(int id, String tablas[]) {
		String sql = "select tpa.id tpa_id, tpa.nom tpa_nom , tpa.des tpa_des  ,tpa.est tpa_est ";
	
		sql = sql + " from asi_tipo_pago tpa "; 
		sql = sql + " where tpa.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoPago>() {
		
			
			public TipoPago extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					TipoPago tipoPago= rsToEntity(rs,"tpa_");
							return tipoPago;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public TipoPago getByParams(Param param) {

		String sql = "select * from asi_tipo_pago " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoPago>() {
			
			public TipoPago extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<TipoPago> listByParams(Param param, String[] order) {

		String sql = "select * from asi_tipo_pago " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<TipoPago>() {

			
			public TipoPago mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<TipoPago> listFullByParams(TipoPago tipoPago, String[] order) {
	
		return listFullByParams(Param.toParam("tpa",tipoPago), order);
	
	}	
	
	
	public List<TipoPago> listFullByParams(Param param, String[] order) {

		String sql = "select tpa.id tpa_id, tpa.nom tpa_nom , tpa.des tpa_des  ,tpa.est tpa_est ";
		sql = sql + " from asi_tipo_pago tpa";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<TipoPago>() {

			
			public TipoPago mapRow(ResultSet rs, int rowNum) throws SQLException {
				TipoPago tipoPago= rsToEntity(rs,"tpa_");
				return tipoPago;
			}

		});

	}	




	// funciones privadas utilitarias para TipoPago

	private TipoPago rsToEntity(ResultSet rs,String alias) throws SQLException {
		TipoPago tipo_pago = new TipoPago();

		tipo_pago.setId(rs.getInt( alias + "id"));
		tipo_pago.setNom(rs.getString( alias + "nom"));
		tipo_pago.setDes(rs.getString( alias + "des"));
		tipo_pago.setEst(rs.getString( alias + "est"));
								
		return tipo_pago;

	}
	
}

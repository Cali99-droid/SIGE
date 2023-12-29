package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.tesla.colegio.model.TipFrecPago;


import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface TipFrecPagoDAO.
 * @author MV
 *
 */
public class TipFrecPagoDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@Autowired
	private TokenSeguridad tokenSeguridad;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(TipFrecPago tip_frec_pago) {
		if (tip_frec_pago.getId() != null) {
			// update
			String sql = "UPDATE cat_tip_frec_pago "
						+ "SET nom=?, "
						+ "des=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						tip_frec_pago.getNom(),
						tip_frec_pago.getDes(),
						tip_frec_pago.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						tip_frec_pago.getId()); 
			return tip_frec_pago.getId();

		} else {
			// insert
			String sql = "insert into cat_tip_frec_pago ("
						+ "nom, "
						+ "des, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				tip_frec_pago.getNom(),
				tip_frec_pago.getDes(),
				tip_frec_pago.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_tip_frec_pago where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<TipFrecPago> list() {
		String sql = "select * from cat_tip_frec_pago";
		
		System.out.println(sql);
		
		List<TipFrecPago> listTipFrecPago = jdbcTemplate.query(sql, new RowMapper<TipFrecPago>() {

			@Override
			public TipFrecPago mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listTipFrecPago;
	}

	public TipFrecPago get(int id) {
		String sql = "select * from cat_tip_frec_pago WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TipFrecPago>() {

			@Override
			public TipFrecPago extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public TipFrecPago getFull(int id, String tablas[]) {
		String sql = "select fpag.id fpag_id, fpag.nom fpag_nom , fpag.des fpag_des  ,fpag.est fpag_est ";
	
		sql = sql + " from cat_tip_frec_pago fpag "; 
		sql = sql + " where fpag.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<TipFrecPago>() {
		
			@Override
			public TipFrecPago extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					TipFrecPago tipfrecpago= rsToEntity(rs,"fpag_");
							return tipfrecpago;
				}
				
				return null;
			}
			
		});


	}		
	
	public TipFrecPago getByParams(Param param) {

		String sql = "select * from cat_tip_frec_pago " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TipFrecPago>() {
			@Override
			public TipFrecPago extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<TipFrecPago> listByParams(Param param, String[] order) {

		String sql = "select * from cat_tip_frec_pago " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<TipFrecPago>() {

			@Override
			public TipFrecPago mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<TipFrecPago> listFullByParams(TipFrecPago tipfrecpago, String[] order) {
	
		return listFullByParams(Param.toParam("fpag",tipfrecpago), order);
	
	}	
	
	public List<TipFrecPago> listFullByParams(Param param, String[] order) {

		String sql = "select fpag.id fpag_id, fpag.nom fpag_nom , fpag.des fpag_des  ,fpag.est fpag_est ";
		sql = sql + " from cat_tip_frec_pago fpag";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<TipFrecPago>() {

			@Override
			public TipFrecPago mapRow(ResultSet rs, int rowNum) throws SQLException {
				TipFrecPago tipfrecpago= rsToEntity(rs,"fpag_");
				return tipfrecpago;
			}

		});

	}	




	// funciones privadas utilitarias para TipFrecPago

	private TipFrecPago rsToEntity(ResultSet rs,String alias) throws SQLException {
		TipFrecPago tip_frec_pago = new TipFrecPago();

		tip_frec_pago.setId(rs.getInt( alias + "id"));
		tip_frec_pago.setNom(rs.getString( alias + "nom"));
		tip_frec_pago.setDes(rs.getString( alias + "des"));
		tip_frec_pago.setEst(rs.getString( alias + "est"));
								
		return tip_frec_pago;

	}
	
}

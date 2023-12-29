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
import com.tesla.colegio.model.Concepto;

import com.tesla.colegio.model.Movimiento;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ConceptoDAO.
 * @author MV
 *
 */
public class ConceptoDAOImpl{
	final static Logger logger = Logger.getLogger(ConceptoDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Concepto concepto) {
		if (concepto.getId() != null) {
			// update
			String sql = "UPDATE fac_concepto "
						+ "SET nom=?, "
						+ "des=?, "
						+ "tip=?, "
						+ "flag_edit=?, "
						+ "monto=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						concepto.getNom(),
						concepto.getDes(),
						concepto.getTip(),
						concepto.getFlag_edit(),
						concepto.getMonto(),
						concepto.getEst(),
						concepto.getUsr_act(),
						new java.util.Date(),
						concepto.getId()); 
			return concepto.getId();

		} else {
			// insert
			String sql = "insert into fac_concepto ("
						+ "nom, "
						+ "des, tip,"
						+ "monto, "
						+ "flag_edit, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?,?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				concepto.getNom(),
				concepto.getDes(),
				concepto.getTip(),
				concepto.getMonto(),
				concepto.getFlag_edit(),
				concepto.getEst(),
				concepto.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from fac_concepto where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Concepto> list() {
		String sql = "select * from fac_concepto";
		
		//logger.info(sql);
		
		List<Concepto> listConcepto = jdbcTemplate.query(sql, new RowMapper<Concepto>() {

			@Override
			public Concepto mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listConcepto;
	}

	public Concepto get(int id) {
		String sql = "select * from fac_concepto WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Concepto>() {

			@Override
			public Concepto extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Concepto getFull(int id, String tablas[]) {
		String sql = "select fco.id fco_id, fco.nom fco_nom , fco.des fco_des , fco.tip fco_tip, fco.flag_edit fco_flag_edit, fco.monto fco_monto  ,fco.est fco_est ";
	
		sql = sql + " from fac_concepto fco "; 
		sql = sql + " where fco.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Concepto>() {
		
			@Override
			public Concepto extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Concepto concepto= rsToEntity(rs,"fco_");
							return concepto;
				}
				
				return null;
			}
			
		});


	}		
	
	public Concepto getByParams(Param param) {

		String sql = "select * from fac_concepto " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Concepto>() {
			@Override
			public Concepto extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Concepto> listByParams(Param param, String[] order) {

		String sql = "select * from fac_concepto " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Concepto>() {

			@Override
			public Concepto mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Concepto> listFullByParams(Concepto concepto, String[] order) {
	
		return listFullByParams(Param.toParam("fco",concepto), order);
	
	}	
	
	public List<Concepto> listFullByParams(Param param, String[] order) {

		String sql = "select fco.id fco_id, fco.nom fco_nom , fco.des fco_des , fco.tip fco_tip , fco.flag_edit fco_flag_edit, fco.monto fco_monto  ,fco.est fco_est ";
		sql = sql + " from fac_concepto fco";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Concepto>() {

			@Override
			public Concepto mapRow(ResultSet rs, int rowNum) throws SQLException {
				Concepto concepto= rsToEntity(rs,"fco_");
				return concepto;
			}

		});

	}	


	public List<Movimiento> getListMovimiento(Param param, String[] order) {
		String sql = "select * from fac_movimiento " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Movimiento>() {

			@Override
			public Movimiento mapRow(ResultSet rs, int rowNum) throws SQLException {
				Movimiento movimiento = new Movimiento();

				movimiento.setId(rs.getInt("id"));
				movimiento.setTipo(rs.getString("tipo"));
				
				movimiento.setFec(rs.getDate("fec"));
				movimiento.setId_suc(rs.getInt("id_suc"));
				//movimiento.setId_fba(rs.getInt("id_fba"));
				//movimiento.setId_fco(rs.getInt("id_fco"));
				movimiento.setId_mat(rs.getInt("id_mat"));
				movimiento.setMonto(rs.getBigDecimal("monto"));
				movimiento.setDescuento(rs.getBigDecimal("descuento"));
				movimiento.setMonto_total(rs.getBigDecimal("monto_total"));
				movimiento.setNro_rec(rs.getString("nro_rec"));
				movimiento.setEst(rs.getString("est"));
												
				return movimiento;
			}

		});	
	}


	// funciones privadas utilitarias para Concepto

	private Concepto rsToEntity(ResultSet rs,String alias) throws SQLException {
		Concepto concepto = new Concepto();

		concepto.setId(rs.getInt( alias + "id"));
		concepto.setNom(rs.getString( alias + "nom"));
		concepto.setDes(rs.getString( alias + "des"));
		concepto.setTip(rs.getString( alias + "tip"));
		concepto.setFlag_edit(rs.getString( alias + "flag_edit"));
		concepto.setMonto(rs.getBigDecimal( alias + "monto"));
		concepto.setEst(rs.getString( alias + "est"));
								
		return concepto;

	}
	
}

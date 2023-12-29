package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.tesla.colegio.model.MovimientoDescuento;

import com.tesla.colegio.model.MovimientoDetalle;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface MovimientoDescuentoDAO.
 * @author MV
 *
 */
public class MovimientoDescuentoDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(MovimientoDescuento movimiento_descuento) {
		if (movimiento_descuento.getId() != null) {
			// update
			String sql = "UPDATE fac_movimiento_descuento "
						+ "SET id_fmd=?, "
						+ "descuento=?, "
						+ "des=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						movimiento_descuento.getId_fmd(),
						movimiento_descuento.getDescuento(),
						movimiento_descuento.getDes(),
						movimiento_descuento.getEst(),
						movimiento_descuento.getUsr_act(),
						new java.util.Date(),
						movimiento_descuento.getId()); 
			return movimiento_descuento.getId();

		} else {
			// insert
			String sql = "insert into fac_movimiento_descuento ("
						+ "id_fmd, "
						+ "descuento, "
						+ "des, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				movimiento_descuento.getId_fmd(),
				movimiento_descuento.getDescuento(),
				movimiento_descuento.getDes(),
				movimiento_descuento.getEst(),
				movimiento_descuento.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from fac_movimiento_descuento where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<MovimientoDescuento> list() {
		String sql = "select * from fac_movimiento_descuento";
		
		
		
		List<MovimientoDescuento> listMovimientoDescuento = jdbcTemplate.query(sql, new RowMapper<MovimientoDescuento>() {

			@Override
			public MovimientoDescuento mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listMovimientoDescuento;
	}

	public MovimientoDescuento get(int id) {
		String sql = "select * from fac_movimiento_descuento WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<MovimientoDescuento>() {

			@Override
			public MovimientoDescuento extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public MovimientoDescuento getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select fdd.id fdd_id, fdd.id_fmd fdd_id_fmd , fdd.descuento fdd_descuento , fdd.des fdd_des  ,fdd.est fdd_est ";
		if (aTablas.contains("fac_movimiento_detalle"))
			sql = sql + ", fmd.id fmd_id  , fmd.id_fmo fmd_id_fmo , fmd.id_fco fmd_id_fco , fmd.monto fmd_monto , fmd.descuento fmd_descuento , fmd.monto_total fmd_monto_total , fmd.obs fmd_obs  ";
	
		sql = sql + " from fac_movimiento_descuento fdd "; 
		if (aTablas.contains("fac_movimiento_detalle"))
			sql = sql + " left join fac_movimiento_detalle fmd on fmd.id = fdd.id_fmd ";
		sql = sql + " where fdd.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<MovimientoDescuento>() {
		
			@Override
			public MovimientoDescuento extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					MovimientoDescuento movimientodescuento= rsToEntity(rs,"fdd_");
					if (aTablas.contains("fac_movimiento_detalle")){
						MovimientoDetalle movimientodetalle = new MovimientoDetalle();  
							movimientodetalle.setId(rs.getInt("fmd_id")) ;  
							movimientodetalle.setId_fmo(rs.getInt("fmd_id_fmo")) ;  
							movimientodetalle.setId_fco(rs.getInt("fmd_id_fco")) ;  
							movimientodetalle.setMonto(rs.getBigDecimal("fmd_monto")) ;  
							movimientodetalle.setDescuento(rs.getBigDecimal("fmd_descuento")) ;  
							movimientodetalle.setMonto_total(rs.getBigDecimal("fmd_monto_total")) ;  
							movimientodetalle.setObs(rs.getString("fmd_obs")) ;  
							movimientodescuento.setMovimientoDetalle(movimientodetalle);
					}
							return movimientodescuento;
				}
				
				return null;
			}
			
		});


	}		
	
	public MovimientoDescuento getByParams(Param param) {

		String sql = "select * from fac_movimiento_descuento " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<MovimientoDescuento>() {
			@Override
			public MovimientoDescuento extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<MovimientoDescuento> listByParams(Param param, String[] order) {

		String sql = "select * from fac_movimiento_descuento " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<MovimientoDescuento>() {

			@Override
			public MovimientoDescuento mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<MovimientoDescuento> listFullByParams(MovimientoDescuento movimientodescuento, String[] order) {
	
		return listFullByParams(Param.toParam("fdd",movimientodescuento), order);
	
	}	
	
	public List<MovimientoDescuento> listFullByParams(Param param, String[] order) {

		String sql = "select fdd.id fdd_id, fdd.id_fmd fdd_id_fmd , fdd.descuento fdd_descuento , fdd.des fdd_des  ,fdd.est fdd_est ";
		sql = sql + ", fmd.id fmd_id  , fmd.id_fmo fmd_id_fmo , fmd.id_fco fmd_id_fco , fmd.monto fmd_monto , fmd.descuento fmd_descuento , fmd.monto_total fmd_monto_total , fmd.obs fmd_obs  ";
		sql = sql + " from fac_movimiento_descuento fdd";
		sql = sql + " left join fac_movimiento_detalle fmd on fmd.id = fdd.id_fmd ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<MovimientoDescuento>() {

			@Override
			public MovimientoDescuento mapRow(ResultSet rs, int rowNum) throws SQLException {
				MovimientoDescuento movimientodescuento= rsToEntity(rs,"fdd_");
				MovimientoDetalle movimientodetalle = new MovimientoDetalle();  
				movimientodetalle.setId(rs.getInt("fmd_id")) ;  
				movimientodetalle.setId_fmo(rs.getInt("fmd_id_fmo")) ;  
				movimientodetalle.setId_fco(rs.getInt("fmd_id_fco")) ;  
				movimientodetalle.setMonto(rs.getBigDecimal("fmd_monto")) ;  
				movimientodetalle.setDescuento(rs.getBigDecimal("fmd_descuento")) ;  
				movimientodetalle.setMonto_total(rs.getBigDecimal("fmd_monto_total")) ;  
				movimientodetalle.setObs(rs.getString("fmd_obs")) ;  
				movimientodescuento.setMovimientoDetalle(movimientodetalle);
				return movimientodescuento;
			}

		});

	}	




	// funciones privadas utilitarias para MovimientoDescuento

	private MovimientoDescuento rsToEntity(ResultSet rs,String alias) throws SQLException {
		MovimientoDescuento movimiento_descuento = new MovimientoDescuento();

		movimiento_descuento.setId(rs.getInt( alias + "id"));
		movimiento_descuento.setId_fmd(rs.getInt( alias + "id_fmd"));
		movimiento_descuento.setDescuento(rs.getBigDecimal( alias + "descuento"));
		movimiento_descuento.setDes(rs.getString( alias + "des"));
		movimiento_descuento.setEst(rs.getString( alias + "est"));
								
		return movimiento_descuento;

	}
	
}

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
import com.tesla.colegio.model.MovimientoDetalle;

import com.tesla.colegio.model.Movimiento;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.Concepto;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface MovimientoDetalleDAO.
 * @author MV
 *
 */
public class MovimientoDetalleDAOImpl{
	final static Logger logger = Logger.getLogger(MovimientoDetalleDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	@Autowired
	private TokenSeguridad tokenSeguridad;
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	//@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(MovimientoDetalle movimiento_detalle) {
		if (movimiento_detalle.getId() != null) {
			// update
			String sql = "UPDATE fac_movimiento_detalle "
						+ "SET id_fmo=?, "
						+ "id_fco=?, "
						+ "monto=?, "
						+ "descuento=?, "
						+ "monto_total=?, "
						+ "obs=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						movimiento_detalle.getId_fmo(),
						movimiento_detalle.getId_fco(),
						movimiento_detalle.getMonto(),
						movimiento_detalle.getDescuento(),
						movimiento_detalle.getMonto_total(),
						movimiento_detalle.getObs(),
						movimiento_detalle.getEst(),
						movimiento_detalle.getUsr_act(),
						new java.util.Date(),
						tokenSeguridad.getId()); 
			return movimiento_detalle.getId();

		} else {
			// insert
			String sql = "insert into fac_movimiento_detalle ("
						+ "id_fmo, "
						+ "id_fco, "
						+ "monto, "
						+ "descuento, "
						+ "monto_total, "
						+ "obs, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				movimiento_detalle.getId_fmo(),
				movimiento_detalle.getId_fco(),
				movimiento_detalle.getMonto(),
				movimiento_detalle.getDescuento(),
				movimiento_detalle.getMonto_total(),
				movimiento_detalle.getObs(),
				movimiento_detalle.getEst(),
				//tokenSeguridad.getId(),
				movimiento_detalle.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from fac_movimiento_detalle where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<MovimientoDetalle> list() {
		String sql = "select * from fac_movimiento_detalle";
		
		//logger.info(sql);
		
		List<MovimientoDetalle> listMovimientoDetalle = jdbcTemplate.query(sql, new RowMapper<MovimientoDetalle>() {

			@Override
			public MovimientoDetalle mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listMovimientoDetalle;
	}

	public MovimientoDetalle get(int id) {
		String sql = "select * from fac_movimiento_detalle WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<MovimientoDetalle>() {

			@Override
			public MovimientoDetalle extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public MovimientoDetalle getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select fmd.id fmd_id, fmd.id_fmo fmd_id_fmo , fmd.id_fco fmd_id_fco , fmd.monto fmd_monto , fmd.descuento fmd_descuento , fmd.monto_total fmd_monto_total , fmd.obs fmd_obs  ,fmd.est fmd_est ";
		if (aTablas.contains("fac_movimiento"))
			sql = sql + ", fmo.id fmo_id  , fmo.tipo fmo_tipo , fmo.fec fmo_fec , fmo.id_suc fmo_id_suc , fmo.id_mat fmo_id_mat , fmo.monto fmo_monto , fmo.descuento fmo_descuento , fmo.monto_total fmo_monto_total , fmo.nro_rec fmo_nro_rec , fmo.obs fmo_obs  ";
		if (aTablas.contains("fac_concepto"))
			sql = sql + ", fco.id fco_id  , fco.nom fco_nom , fco.des fco_des , fco.monto fco_monto  ";
	
		sql = sql + " from fac_movimiento_detalle fmd "; 
		if (aTablas.contains("fac_movimiento"))
			sql = sql + " left join fac_movimiento fmo on fmo.id = fmd.id_fmo ";
		if (aTablas.contains("fac_concepto"))
			sql = sql + " left join fac_concepto fco on fco.id = fmd.id_fco ";
		sql = sql + " where fmd.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<MovimientoDetalle>() {
		
			@Override
			public MovimientoDetalle extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					MovimientoDetalle movimientodetalle= rsToEntity(rs,"fmd_");
					if (aTablas.contains("fac_movimiento")){
						Movimiento movimiento = new Movimiento();  
							movimiento.setId(rs.getInt("fmo_id")) ;  
							movimiento.setTipo(rs.getString("fmo_tipo")) ;  
							movimiento.setFec(rs.getDate("fmo_fec")) ;  
							movimiento.setId_suc(rs.getInt("fmo_id_suc")) ;  
							movimiento.setId_mat(rs.getInt("fmo_id_mat")) ;  
							movimiento.setMonto(rs.getBigDecimal("fmo_monto")) ;  
							movimiento.setDescuento(rs.getBigDecimal("fmo_descuento")) ;  
							movimiento.setMonto_total(rs.getBigDecimal("fmo_monto_total")) ;  
							movimiento.setNro_rec(rs.getString("fmo_nro_rec")) ;  
							movimiento.setObs(rs.getString("fmo_obs")) ;  
							movimientodetalle.setMovimiento(movimiento);
					}
					if (aTablas.contains("fac_concepto")){
						Concepto concepto = new Concepto();  
							concepto.setId(rs.getInt("fco_id")) ;  
							concepto.setNom(rs.getString("fco_nom")) ;  
							concepto.setDes(rs.getString("fco_des")) ;  
							concepto.setMonto(rs.getBigDecimal("fco_monto")) ;  
							movimientodetalle.setConcepto(concepto);
					}
							return movimientodetalle;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public List<MovimientoDetalle> getListByIDfmo(int id_fmo, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select fmd.id fmd_id, fmd.id_fmo fmd_id_fmo , fmd.id_fco fmd_id_fco , fmd.monto fmd_monto , fmd.descuento fmd_descuento , fmd.monto_total fmd_monto_total , fmd.obs fmd_obs  ,fmd.est fmd_est ";
		if (aTablas.contains("fac_movimiento"))
			sql = sql + ", fmo.id fmo_id  , fmo.tipo fmo_tipo , fmo.fec fmo_fec , fmo.id_suc fmo_id_suc , fmo.id_mat fmo_id_mat , fmo.monto fmo_monto , fmo.descuento fmo_descuento , fmo.monto_total fmo_monto_total , fmo.nro_rec fmo_nro_rec , fmo.obs fmo_obs  ";
		if (aTablas.contains("fac_concepto"))
			sql = sql + ", fco.id fco_id  , fco.nom fco_nom , fco.des fco_des , fco.monto fco_monto  ";
	
		sql = sql + " from fac_movimiento_detalle fmd "; 
		if (aTablas.contains("fac_movimiento"))
			sql = sql + " left join fac_movimiento fmo on fmo.id = fmd.id_fmo ";
		if (aTablas.contains("fac_concepto"))
			sql = sql + " left join fac_concepto fco on fco.id = fmd.id_fco ";
		sql = sql + " where fmd.id_fmo= " + id_fmo; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<MovimientoDetalle>() {

			@Override
			public MovimientoDetalle mapRow(ResultSet rs, int rowNum) throws SQLException {
				MovimientoDetalle movimientodetalle= rsToEntity(rs,"fmd_");
				Movimiento movimiento = new Movimiento();  
				movimiento.setId(rs.getInt("fmo_id")) ;  
				movimiento.setTipo(rs.getString("fmo_tipo")) ;  
				movimiento.setFec(rs.getDate("fmo_fec")) ;  
				movimiento.setId_suc(rs.getInt("fmo_id_suc")) ;  
				movimiento.setId_mat(rs.getInt("fmo_id_mat")) ;  
				movimiento.setMonto(rs.getBigDecimal("fmo_monto")) ;  
				movimiento.setDescuento(rs.getBigDecimal("fmo_descuento")) ;  
				movimiento.setMonto_total(rs.getBigDecimal("fmo_monto_total")) ;  
				movimiento.setNro_rec(rs.getString("fmo_nro_rec")) ;  
				movimiento.setObs(rs.getString("fmo_obs")) ;  
				movimientodetalle.setMovimiento(movimiento);
				Concepto concepto = new Concepto();  
				concepto.setId(rs.getInt("fco_id")) ;  
				concepto.setNom(rs.getString("fco_nom")) ;  
				concepto.setDes(rs.getString("fco_des")) ;  
				concepto.setMonto(rs.getBigDecimal("fco_monto")) ;  
				movimientodetalle.setConcepto(concepto);
				return movimientodetalle;
			}

		});


	}	
	
	public MovimientoDetalle getByParams(Param param) {

		String sql = "select * from fac_movimiento_detalle " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<MovimientoDetalle>() {
			@Override
			public MovimientoDetalle extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<MovimientoDetalle> listByParams(Param param, String[] order) {

		String sql = "select * from fac_movimiento_detalle " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<MovimientoDetalle>() {

			@Override
			public MovimientoDetalle mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<MovimientoDetalle> listFullByParams(MovimientoDetalle movimientodetalle, String[] order) {
	
		return listFullByParams(Param.toParam("fmd",movimientodetalle), order);
	
	}	
	
	public List<MovimientoDetalle> listFullByParams(Param param, String[] order) {

		String sql = "select fmd.id fmd_id, fmd.id_fmo fmd_id_fmo , fmd.id_fco fmd_id_fco , fmd.monto fmd_monto , fmd.descuento fmd_descuento , fmd.monto_total fmd_monto_total , fmd.obs fmd_obs  ,fmd.est fmd_est ";
		sql = sql + ", fmo.id fmo_id  , fmo.tipo fmo_tipo , fmo.fec fmo_fec , fmo.id_suc fmo_id_suc , fmo.id_mat fmo_id_mat , fmo.monto fmo_monto , fmo.descuento fmo_descuento , fmo.monto_total fmo_monto_total , fmo.nro_rec fmo_nro_rec , fmo.obs fmo_obs  ";
		sql = sql + ", fco.id fco_id  , fco.nom fco_nom , fco.des fco_des , fco.monto fco_monto  ";
		sql = sql + " from fac_movimiento_detalle fmd";
		sql = sql + " left join fac_movimiento fmo on fmo.id = fmd.id_fmo ";
		sql = sql + " left join fac_concepto fco on fco.id = fmd.id_fco ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<MovimientoDetalle>() {

			@Override
			public MovimientoDetalle mapRow(ResultSet rs, int rowNum) throws SQLException {
				MovimientoDetalle movimientodetalle= rsToEntity(rs,"fmd_");
				Movimiento movimiento = new Movimiento();  
				movimiento.setId(rs.getInt("fmo_id")) ;  
				movimiento.setTipo(rs.getString("fmo_tipo")) ;  
				movimiento.setFec(rs.getDate("fmo_fec")) ;  
				movimiento.setId_suc(rs.getInt("fmo_id_suc")) ;  
				movimiento.setId_mat(rs.getInt("fmo_id_mat")) ;  
				movimiento.setMonto(rs.getBigDecimal("fmo_monto")) ;  
				movimiento.setDescuento(rs.getBigDecimal("fmo_descuento")) ;  
				movimiento.setMonto_total(rs.getBigDecimal("fmo_monto_total")) ;  
				movimiento.setNro_rec(rs.getString("fmo_nro_rec")) ;  
				movimiento.setObs(rs.getString("fmo_obs")) ;  
				movimientodetalle.setMovimiento(movimiento);
				Concepto concepto = new Concepto();  
				concepto.setId(rs.getInt("fco_id")) ;  
				concepto.setNom(rs.getString("fco_nom")) ;  
				concepto.setDes(rs.getString("fco_des")) ;  
				concepto.setMonto(rs.getBigDecimal("fco_monto")) ;  
				movimientodetalle.setConcepto(concepto);
				return movimientodetalle;
			}

		});

	}	




	// funciones privadas utilitarias para MovimientoDetalle

	private MovimientoDetalle rsToEntity(ResultSet rs,String alias) throws SQLException {
		MovimientoDetalle movimiento_detalle = new MovimientoDetalle();

		movimiento_detalle.setId(rs.getInt( alias + "id"));
		movimiento_detalle.setId_fmo(rs.getInt( alias + "id_fmo"));
		movimiento_detalle.setId_fco(rs.getInt( alias + "id_fco"));
		movimiento_detalle.setMonto(rs.getBigDecimal( alias + "monto"));
		movimiento_detalle.setDescuento(rs.getBigDecimal( alias + "descuento"));
		movimiento_detalle.setMonto_total(rs.getBigDecimal( alias + "monto_total"));
		movimiento_detalle.setObs(rs.getString( alias + "obs"));
		movimiento_detalle.setEst(rs.getString( alias + "est"));
								
		return movimiento_detalle;

	}
	
}

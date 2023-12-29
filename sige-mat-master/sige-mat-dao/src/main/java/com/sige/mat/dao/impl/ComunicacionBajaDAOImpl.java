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

import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.ComunicacionBaja;

import com.tesla.colegio.model.Movimiento;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;

/**
 * Implementaci�n de la interface ComunicacionBajaDAO.
 * @author MV
 *
 */
public class ComunicacionBajaDAOImpl{
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
	public int saveOrUpdate(ComunicacionBaja comunicacion_baja) {
		if (comunicacion_baja.getId() != null) {
			// update
			String sql = "UPDATE fac_comunicacion_baja "
						+ "SET "
						+ "fec_emi=?, "
						+ "id_fmo=?, "
						+ "motivo=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						comunicacion_baja.getFec_emi(),
						comunicacion_baja.getId_fmo(),
						comunicacion_baja.getMotivo(),
						comunicacion_baja.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						comunicacion_baja.getId()); 
			return comunicacion_baja.getId();

		} else {
			// insert
			String sql = "insert into fac_comunicacion_baja ("
						+ "fec_emi, "
						+ "id_fmo, "
						+ "motivo, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				comunicacion_baja.getFec_emi(),
				comunicacion_baja.getId_fmo(),
				comunicacion_baja.getMotivo(),
				comunicacion_baja.getEst(),
				comunicacion_baja.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from fac_comunicacion_baja where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<ComunicacionBaja> list() {
		String sql = "select * from fac_comunicacion_baja";
		
		//System.out.println(sql);
		
		List<ComunicacionBaja> listComunicacionBaja = jdbcTemplate.query(sql, new RowMapper<ComunicacionBaja>() {

			@Override
			public ComunicacionBaja mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listComunicacionBaja;
	}

	public ComunicacionBaja get(int id) {
		String sql = "select * from fac_comunicacion_baja WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ComunicacionBaja>() {

			@Override
			public ComunicacionBaja extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public ComunicacionBaja getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select fcb.id fcb_id, fcb.fec_emi fcb_fec_emi , fcb.id_fmo fcb_id_fmo , fcb.motivo fcb_motivo  ,fcb.est fcb_est ";
		if (aTablas.contains("fac_movimiento"))
			sql = sql + ", fmo.id fmo_id  , fmo.tipo fmo_tipo , fmo.fec fmo_fec , fmo.id_suc fmo_id_suc , fmo.id_mat fmo_id_mat , fmo.monto fmo_monto , fmo.descuento fmo_descuento , fmo.monto_total fmo_monto_total , fmo.nro_rec fmo_nro_rec , fmo.obs fmo_obs , fmo.cod_res fmo_cod_res  ";
	
		sql = sql + " from fac_comunicacion_baja fcb "; 
		if (aTablas.contains("fac_movimiento"))
			sql = sql + " left join fac_movimiento fmo on fmo.id = fcb.id_fmo ";
		sql = sql + " where fcb.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<ComunicacionBaja>() {
		
			@Override
			public ComunicacionBaja extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ComunicacionBaja comunicacionbaja= rsToEntity(rs,"fcb_");
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
							movimiento.setCod_res(rs.getString("fmo_cod_res")) ;  
							comunicacionbaja.setMovimiento(movimiento);
					}
							return comunicacionbaja;
				}
				
				return null;
			}
			
		});


	}		
	
	public ComunicacionBaja getByParams(Param param) {

		String sql = "select * from fac_comunicacion_baja " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ComunicacionBaja>() {
			@Override
			public ComunicacionBaja extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<ComunicacionBaja> listByParams(Param param, String[] order) {

		String sql = "select * from fac_comunicacion_baja " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<ComunicacionBaja>() {

			@Override
			public ComunicacionBaja mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<ComunicacionBaja> listFullByParams(ComunicacionBaja comunicacionbaja, String[] order) {
	
		return listFullByParams(Param.toParam("fcb",comunicacionbaja), order);
	
	}	
	
	public List<ComunicacionBaja> listFullByParams(Param param, String[] order) {

		String sql = "select fcb.id fcb_id, fcb.fec_emi fcb_fec_emi , fcb.id_fmo fcb_id_fmo , fcb.motivo fcb_motivo  ,fcb.est fcb_est ";
		sql = sql + ", fmo.id fmo_id  , fmo.tipo fmo_tipo , fmo.fec fmo_fec , fmo.id_suc fmo_id_suc , fmo.id_mat fmo_id_mat , fmo.monto fmo_monto , fmo.descuento fmo_descuento , fmo.monto_total fmo_monto_total , fmo.nro_rec fmo_nro_rec , fmo.obs fmo_obs , fmo.cod_res fmo_cod_res  ";
		sql = sql + " from fac_comunicacion_baja fcb";
		sql = sql + " left join fac_movimiento fmo on fmo.id = fcb.id_fmo ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<ComunicacionBaja>() {

			@Override
			public ComunicacionBaja mapRow(ResultSet rs, int rowNum) throws SQLException {
				ComunicacionBaja comunicacionbaja= rsToEntity(rs,"fcb_");
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
				movimiento.setCod_res(rs.getString("fmo_cod_res")) ;  
				comunicacionbaja.setMovimiento(movimiento);
				return comunicacionbaja;
			}

		});

	}	




	// funciones privadas utilitarias para ComunicacionBaja

	private ComunicacionBaja rsToEntity(ResultSet rs,String alias) throws SQLException {
		ComunicacionBaja comunicacion_baja = new ComunicacionBaja();

		comunicacion_baja.setId(rs.getInt( alias + "id"));
		comunicacion_baja.setFec_emi(rs.getDate( alias + "fec_emi"));
		comunicacion_baja.setId_fmo(rs.getInt( alias + "id_fmo"));
		comunicacion_baja.setMotivo(rs.getString( alias + "motivo"));
		comunicacion_baja.setEst(rs.getString( alias + "est"));
								
		return comunicacion_baja;

	}
	
}

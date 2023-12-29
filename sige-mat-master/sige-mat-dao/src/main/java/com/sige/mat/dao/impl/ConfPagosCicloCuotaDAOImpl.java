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
import com.tesla.colegio.model.ConfPagosCicloCuota;

import com.tesla.colegio.model.ConfPagosCiclo;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ConfPagosCicloCuotaDAO.
 * @author MV
 *
 */
public class ConfPagosCicloCuotaDAOImpl{
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
	public int saveOrUpdate(ConfPagosCicloCuota conf_pagos_ciclo_cuota) {
		if (conf_pagos_ciclo_cuota.getId() != null) {
			// update
			String sql = "UPDATE fac_conf_pagos_ciclo_cuota "
						+ "SET id_cfpav=?, "
						+ "nro_cuota=?, "
						+ "costo=?, "
						+ "fec_venc=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						conf_pagos_ciclo_cuota.getId_cfpav(),
						conf_pagos_ciclo_cuota.getNro_cuota(),
						conf_pagos_ciclo_cuota.getCosto(),
						conf_pagos_ciclo_cuota.getFec_venc(),
						conf_pagos_ciclo_cuota.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						conf_pagos_ciclo_cuota.getId()); 
			return conf_pagos_ciclo_cuota.getId();

		} else {
			// insert
			String sql = "insert into fac_conf_pagos_ciclo_cuota ("
						+ "id_cfpav, "
						+ "nro_cuota, "
						+ "costo, "
						+ "fec_venc, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				conf_pagos_ciclo_cuota.getId_cfpav(),
				conf_pagos_ciclo_cuota.getNro_cuota(),
				conf_pagos_ciclo_cuota.getCosto(),
				conf_pagos_ciclo_cuota.getFec_venc(),
				conf_pagos_ciclo_cuota.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from fac_conf_pagos_ciclo_cuota where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<ConfPagosCicloCuota> list() {
		String sql = "select * from fac_conf_pagos_ciclo_cuota";
		
		System.out.println(sql);
		
		List<ConfPagosCicloCuota> listConfPagosCicloCuota = jdbcTemplate.query(sql, new RowMapper<ConfPagosCicloCuota>() {

			@Override
			public ConfPagosCicloCuota mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listConfPagosCicloCuota;
	}

	public ConfPagosCicloCuota get(int id) {
		String sql = "select * from fac_conf_pagos_ciclo_cuota WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfPagosCicloCuota>() {

			@Override
			public ConfPagosCicloCuota extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public ConfPagosCicloCuota getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cfpcuo.id cfpcuo_id, cfpcuo.id_cfpav cfpcuo_id_cfpav , cfpcuo.nro_cuota cfpcuo_nro_cuota , cfpcuo.costo cfpcuo_costo , cfpcuo.fec_venc cfpcuo_fec_venc  ,cfpcuo.est cfpcuo_est ";
		if (aTablas.contains("fac_conf_pagos_ciclo"))
			sql = sql + ", cfpav.id cfpav_id  , cfpav.id_cic cfpav_id_cic , cfpav.costo cfpav_costo , cfpav.num_cuotas cfpav_num_cuotas  ";
	
		sql = sql + " from fac_conf_pagos_ciclo_cuota cfpcuo "; 
		if (aTablas.contains("fac_conf_pagos_ciclo"))
			sql = sql + " left join fac_conf_pagos_ciclo cfpav on cfpav.id = cfpcuo.id_cfpav ";
		sql = sql + " where cfpcuo.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfPagosCicloCuota>() {
		
			@Override
			public ConfPagosCicloCuota extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ConfPagosCicloCuota confpagosciclocuota= rsToEntity(rs,"cfpcuo_");
					if (aTablas.contains("fac_conf_pagos_ciclo")){
						ConfPagosCiclo confpagosciclo = new ConfPagosCiclo();  
							confpagosciclo.setId(rs.getInt("cfpav_id")) ;  
							//confpagosciclo.setId_cic(rs.getInt("cfpav_id_cic")) ;  
							//confpagosciclo.setCosto(rs.getString("cfpav_costo")) ;  
							confpagosciclo.setNum_cuotas(rs.getInt("cfpav_num_cuotas")) ;  
							confpagosciclocuota.setConfPagosCiclo(confpagosciclo);
					}
							return confpagosciclocuota;
				}
				
				return null;
			}
			
		});


	}		
	
	public ConfPagosCicloCuota getByParams(Param param) {

		String sql = "select * from fac_conf_pagos_ciclo_cuota " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfPagosCicloCuota>() {
			@Override
			public ConfPagosCicloCuota extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<ConfPagosCicloCuota> listByParams(Param param, String[] order) {

		String sql = "select * from fac_conf_pagos_ciclo_cuota " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<ConfPagosCicloCuota>() {

			@Override
			public ConfPagosCicloCuota mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<ConfPagosCicloCuota> listFullByParams(ConfPagosCicloCuota confpagosciclocuota, String[] order) {
	
		return listFullByParams(Param.toParam("cfpcuo",confpagosciclocuota), order);
	
	}	
	
	public List<ConfPagosCicloCuota> listFullByParams(Param param, String[] order) {

		String sql = "select cfpcuo.id cfpcuo_id, cfpcuo.id_cfpav cfpcuo_id_cfpav , cfpcuo.nro_cuota cfpcuo_nro_cuota , cfpcuo.costo cfpcuo_costo , cfpcuo.fec_venc cfpcuo_fec_venc  ,cfpcuo.est cfpcuo_est ";
		sql = sql + ", cfpav.id cfpav_id  , cfpav.id_cic cfpav_id_cic , cfpav.costo cfpav_costo , cfpav.num_cuotas cfpav_num_cuotas  ";
		sql = sql + " from fac_conf_pagos_ciclo_cuota cfpcuo";
		sql = sql + " left join fac_conf_pagos_ciclo cfpav on cfpav.id = cfpcuo.id_cfpav ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<ConfPagosCicloCuota>() {

			@Override
			public ConfPagosCicloCuota mapRow(ResultSet rs, int rowNum) throws SQLException {
				ConfPagosCicloCuota confpagosciclocuota= rsToEntity(rs,"cfpcuo_");
				ConfPagosCiclo confpagosciclo = new ConfPagosCiclo();  
				confpagosciclo.setId(rs.getInt("cfpav_id")) ;  
				//confpagosciclo.setId_cic(rs.getInt("cfpav_id_cic")) ;  
				//confpagosciclo.setCosto(rs.getString("cfpav_costo")) ;  
				confpagosciclo.setNum_cuotas(rs.getInt("cfpav_num_cuotas")) ;  
				confpagosciclocuota.setConfPagosCiclo(confpagosciclo);
				return confpagosciclocuota;
			}

		});

	}	




	// funciones privadas utilitarias para ConfPagosCicloCuota

	private ConfPagosCicloCuota rsToEntity(ResultSet rs,String alias) throws SQLException {
		ConfPagosCicloCuota conf_pagos_ciclo_cuota = new ConfPagosCicloCuota();

		conf_pagos_ciclo_cuota.setId(rs.getInt( alias + "id"));
		conf_pagos_ciclo_cuota.setId_cfpav(rs.getInt( alias + "id_cfpav"));
		conf_pagos_ciclo_cuota.setNro_cuota(rs.getInt( alias + "nro_cuota"));
		conf_pagos_ciclo_cuota.setCosto(rs.getBigDecimal( alias + "costo"));
		conf_pagos_ciclo_cuota.setFec_venc(rs.getDate( alias + "fec_venc"));
		conf_pagos_ciclo_cuota.setEst(rs.getString( alias + "est"));
								
		return conf_pagos_ciclo_cuota;

	}
	
}

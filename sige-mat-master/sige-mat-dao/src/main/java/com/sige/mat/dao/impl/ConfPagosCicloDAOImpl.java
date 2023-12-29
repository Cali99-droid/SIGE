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
import com.tesla.colegio.model.ConfPagosCiclo;

import com.tesla.colegio.model.Ciclo;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ConfPagosCicloDAO.
 * @author MV
 *
 */
public class ConfPagosCicloDAOImpl{
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
	public int saveOrUpdate(ConfPagosCiclo conf_pagos_ciclo) {
		if (conf_pagos_ciclo.getId() != null) {
			// update
			String sql = "UPDATE fac_conf_pagos_ciclo "
						+ "SET id_cct=?, "
						+ "costo=?, "
						+ "num_cuotas=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						conf_pagos_ciclo.getId_cct(),
						conf_pagos_ciclo.getCosto(),
						conf_pagos_ciclo.getNum_cuotas(),
						conf_pagos_ciclo.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						conf_pagos_ciclo.getId()); 
			return conf_pagos_ciclo.getId();

		} else {
			// insert
			String sql = "insert into fac_conf_pagos_ciclo ("
						+ "id_cct, "
						+ "costo, "
						+ "num_cuotas, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				conf_pagos_ciclo.getId_cct(),
				conf_pagos_ciclo.getCosto(),
				conf_pagos_ciclo.getNum_cuotas(),
				conf_pagos_ciclo.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from fac_conf_pagos_ciclo where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<ConfPagosCiclo> list() {
		String sql = "select * from fac_conf_pagos_ciclo";
		
		System.out.println(sql);
		
		List<ConfPagosCiclo> listConfPagosCiclo = jdbcTemplate.query(sql, new RowMapper<ConfPagosCiclo>() {

			@Override
			public ConfPagosCiclo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listConfPagosCiclo;
	}

	public ConfPagosCiclo get(int id_cct) {
		String sql = "select * from fac_conf_pagos_ciclo WHERE id_cct=" + id_cct;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfPagosCiclo>() {

			@Override
			public ConfPagosCiclo extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public ConfPagosCiclo getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cfpav.id cfpav_id, cfpav.id_cic cfpav_id_cic , cfpav.costo cfpav_costo , cfpav.num_cuotas cfpav_num_cuotas  ,cfpav.est cfpav_est ";
		if (aTablas.contains("col_ciclo"))
			sql = sql + ", cic.id cic_id  , cic.id_per cic_id_per , cic.nom cic_nom , cic.fec_ini cic_fec_ini , cic.fec_fin cic_fec_fin  ";
	
		sql = sql + " from fac_conf_pagos_ciclo cfpav "; 
		if (aTablas.contains("col_ciclo"))
			sql = sql + " left join col_ciclo cic on cic.id = cfpav.id_cic ";
		sql = sql + " where cfpav.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfPagosCiclo>() {
		
			@Override
			public ConfPagosCiclo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ConfPagosCiclo confpagosciclo= rsToEntity(rs,"cfpav_");
					if (aTablas.contains("col_ciclo")){
						Ciclo ciclo = new Ciclo();  
							ciclo.setId(rs.getInt("cic_id")) ;  
							ciclo.setId_per(rs.getInt("cic_id_per")) ;  
							ciclo.setNom(rs.getString("cic_nom")) ;  
							ciclo.setFec_ini(rs.getDate("cic_fec_ini")) ;  
							ciclo.setFec_fin(rs.getDate("cic_fec_fin")) ;  
							confpagosciclo.setCiclo(ciclo);
					}
							return confpagosciclo;
				}
				
				return null;
			}
			
		});


	}		
	
	public ConfPagosCiclo getByParams(Param param) {

		String sql = "select * from fac_conf_pagos_ciclo " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfPagosCiclo>() {
			@Override
			public ConfPagosCiclo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<ConfPagosCiclo> listByParams(Param param, String[] order) {

		String sql = "select * from fac_conf_pagos_ciclo " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<ConfPagosCiclo>() {

			@Override
			public ConfPagosCiclo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<ConfPagosCiclo> listFullByParams(ConfPagosCiclo confpagosciclo, String[] order) {
	
		return listFullByParams(Param.toParam("cfpav",confpagosciclo), order);
	
	}	
	
	public List<ConfPagosCiclo> listFullByParams(Param param, String[] order) {

		String sql = "select cfpav.id cfpav_id, cfpav.id_cct cfpav_id_cct , cfpav.costo cfpav_costo , cfpav.num_cuotas cfpav_num_cuotas  ,cfpav.est cfpav_est ";
		sql = sql + ", cic.id cic_id  , cic.id_per cic_id_per , cic.nom cic_nom , cic.fec_ini cic_fec_ini , cic.fec_fin cic_fec_fin  ";
		sql = sql + " from fac_conf_pagos_ciclo cfpav";
		sql = sql + " left join col_ciclo cic on cic.id = cfpav.id_cic ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<ConfPagosCiclo>() {

			@Override
			public ConfPagosCiclo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ConfPagosCiclo confpagosciclo= rsToEntity(rs,"cfpav_");
				Ciclo ciclo = new Ciclo();  
				ciclo.setId(rs.getInt("cic_id")) ;  
				ciclo.setId_per(rs.getInt("cic_id_per")) ;  
				ciclo.setNom(rs.getString("cic_nom")) ;  
				ciclo.setFec_ini(rs.getDate("cic_fec_ini")) ;  
				ciclo.setFec_fin(rs.getDate("cic_fec_fin")) ;  
				confpagosciclo.setCiclo(ciclo);
				return confpagosciclo;
			}

		});

	}	




	// funciones privadas utilitarias para ConfPagosCiclo

	private ConfPagosCiclo rsToEntity(ResultSet rs,String alias) throws SQLException {
		ConfPagosCiclo conf_pagos_ciclo = new ConfPagosCiclo();

		conf_pagos_ciclo.setId(rs.getInt( alias + "id"));
		conf_pagos_ciclo.setId_cct(rs.getInt( alias + "id_cct"));
		conf_pagos_ciclo.setCosto(rs.getBigDecimal( alias + "costo"));
		conf_pagos_ciclo.setNum_cuotas(rs.getInt( alias + "num_cuotas"));
		conf_pagos_ciclo.setEst(rs.getString( alias + "est"));
								
		return conf_pagos_ciclo;

	}
	
}

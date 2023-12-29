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
import com.tesla.colegio.model.PerUniDet;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.PerUni;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface PerUniDetDAO.
 * @author MV
 *
 */
public class PerUniDetDAOImpl{
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
	public int saveOrUpdate(PerUniDet per_uni_det) {
		if (per_uni_det.getId() != null) {
			// update
			String sql = "UPDATE col_per_uni_det "
						+ "SET "
						+ "nro_sem=?, "
						+ "usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						per_uni_det.getNro_sem(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						per_uni_det.getId()); 
			return per_uni_det.getId();

		} else {
			// insert
			String sql = "insert into col_per_uni_det ("
						+ "id_cpu, "
						+ "ord, "
						+ "nro_sem, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				per_uni_det.getId_cpu(),
				per_uni_det.getOrd(),
				per_uni_det.getNro_sem(),
				per_uni_det.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id_cpu) {
		String sql = "delete from col_per_uni_det where id_cpu=?";
		jdbcTemplate.update(sql, id_cpu);
	}

	public List<PerUniDet> list() {
		String sql = "select * from col_per_uni_det";
		
		
		
		List<PerUniDet> listPerUniDet = jdbcTemplate.query(sql, new RowMapper<PerUniDet>() {

			@Override
			public PerUniDet mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listPerUniDet;
	}

	public PerUniDet get(int id) {
		String sql = "select * from col_per_uni_det WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PerUniDet>() {

			@Override
			public PerUniDet extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public PerUniDet getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cpud.id cpud_id, cpud.id_cpu cpud_id_cpu , cpud.ord cpud_ord , cpud.nro_sem cpud_nro_sem  ,cpud.est cpud_est ";
		if (aTablas.contains("col_per_uni"))
			sql = sql + ", cpu.id cpu_id  , cpu.id_cpa cpu_id_cpa , cpu.id_anio cpu_id_anio , cpu.nump cpu_nump , cpu.numu_ini cpu_numu_ini , cpu.numu_fin cpu_numu_fin , cpu.fec_ini cpu_fec_ini , cpu.fec_fin cpu_fec_fin , cpu.fec_ini_ing cpu_fec_ini_ing , cpu.fec_fin_ing cpu_fec_fin_ing  ";
	
		sql = sql + " from col_per_uni_det cpud "; 
		if (aTablas.contains("col_per_uni"))
			sql = sql + " left join col_per_uni cpu on cpu.id = cpud.id_cpu ";
		sql = sql + " where cpud.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<PerUniDet>() {
		
			@Override
			public PerUniDet extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					PerUniDet perunidet= rsToEntity(rs,"cpud_");
					if (aTablas.contains("col_per_uni")){
						PerUni peruni = new PerUni();  
							peruni.setId(rs.getInt("cpu_id")) ;  
							peruni.setId_cpa(rs.getInt("cpu_id_cpa")) ;  
							peruni.setId_anio(rs.getInt("cpu_id_anio")) ;  
							peruni.setNump(rs.getInt("cpu_nump")) ;  
							peruni.setNumu_ini(rs.getInt("cpu_numu_ini")) ;  
							peruni.setNumu_fin(rs.getInt("cpu_numu_fin")) ;  
							peruni.setFec_ini(rs.getDate("cpu_fec_ini")) ;  
							peruni.setFec_fin(rs.getDate("cpu_fec_fin")) ;  
							peruni.setFec_ini_ing(rs.getDate("cpu_fec_ini_ing")) ;  
							peruni.setFec_fin_ing(rs.getDate("cpu_fec_fin_ing")) ;  
							perunidet.setPerUni(peruni);
					}
							return perunidet;
				}
				
				return null;
			}
			
		});


	}		
	
	public PerUniDet getByParams(Param param) {

		String sql = "select * from col_per_uni_det " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PerUniDet>() {
			@Override
			public PerUniDet extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<PerUniDet> listByParams(Param param, String[] order) {

		String sql = "select * from col_per_uni_det " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<PerUniDet>() {

			@Override
			public PerUniDet mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<PerUniDet> listFullByParams(PerUniDet perunidet, String[] order) {
	
		return listFullByParams(Param.toParam("cpud",perunidet), order);
	
	}	
	
	public List<PerUniDet> listFullByParams(Param param, String[] order) {

		String sql = "select cpud.id cpud_id, cpud.id_cpu cpud_id_cpu , cpud.ord cpud_ord , cpud.nro_sem cpud_nro_sem  ,cpud.est cpud_est ";
		sql = sql + ", cpu.id cpu_id  , cpu.id_cpa cpu_id_cpa , cpu.id_anio cpu_id_anio , cpu.nump cpu_nump , cpu.numu_ini cpu_numu_ini , cpu.numu_fin cpu_numu_fin , cpu.fec_ini cpu_fec_ini , cpu.fec_fin cpu_fec_fin , cpu.fec_ini_ing cpu_fec_ini_ing , cpu.fec_fin_ing cpu_fec_fin_ing  ";
		sql = sql + " from col_per_uni_det cpud";
		sql = sql + " left join col_per_uni cpu on cpu.id = cpud.id_cpu ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<PerUniDet>() {

			@Override
			public PerUniDet mapRow(ResultSet rs, int rowNum) throws SQLException {
				PerUniDet perunidet= rsToEntity(rs,"cpud_");
				PerUni peruni = new PerUni();  
				peruni.setId(rs.getInt("cpu_id")) ;  
				peruni.setId_cpa(rs.getInt("cpu_id_cpa")) ;  
				peruni.setId_anio(rs.getInt("cpu_id_anio")) ;  
				peruni.setNump(rs.getInt("cpu_nump")) ;  
				peruni.setNumu_ini(rs.getInt("cpu_numu_ini")) ;  
				peruni.setNumu_fin(rs.getInt("cpu_numu_fin")) ;  
				peruni.setFec_ini(rs.getDate("cpu_fec_ini")) ;  
				peruni.setFec_fin(rs.getDate("cpu_fec_fin")) ;  
				peruni.setFec_ini_ing(rs.getDate("cpu_fec_ini_ing")) ;  
				peruni.setFec_fin_ing(rs.getDate("cpu_fec_fin_ing")) ;  
				perunidet.setPerUni(peruni);
				return perunidet;
			}

		});

	}	




	// funciones privadas utilitarias para PerUniDet

	private PerUniDet rsToEntity(ResultSet rs,String alias) throws SQLException {
		PerUniDet per_uni_det = new PerUniDet();

		per_uni_det.setId(rs.getInt( alias + "id"));
		per_uni_det.setId_cpu(rs.getInt( alias + "id_cpu"));
		per_uni_det.setOrd(rs.getInt( alias + "ord"));
		per_uni_det.setNro_sem(rs.getInt( alias + "nro_sem"));
		per_uni_det.setEst(rs.getString( alias + "est"));
								
		return per_uni_det;

	}
	
}

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
import com.tesla.colegio.model.ConfSemanas;

import com.tesla.colegio.model.ConfAnioEscolar;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ConfSemanasDAO.
 * @author MV
 *
 */
public class ConfSemanasDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(ConfSemanas conf_semanas) {
		if (conf_semanas.getId() != null) {
			// update
			String sql = "UPDATE col_conf_semanas "
						+ "SET id_cnf_anio=?, "
						+ "nro_sem=?, "
						+ "fec_ini=?, "
						+ "fec_fin=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						conf_semanas.getId_cnf_anio(),
						conf_semanas.getNro_sem(),
						conf_semanas.getFec_ini(),
						conf_semanas.getFec_fin(),
						conf_semanas.getEst(),
						conf_semanas.getUsr_act(),
						new java.util.Date(),
						conf_semanas.getId()); 
			return conf_semanas.getId();

		} else {
			// insert
			String sql = "insert into col_conf_semanas ("
						+ "id_cnf_anio, "
						+ "nro_sem, "
						+ "fec_ini, "
						+ "fec_fin, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				conf_semanas.getId_cnf_anio(),
				conf_semanas.getNro_sem(),
				conf_semanas.getFec_ini(),
				conf_semanas.getFec_fin(),
				conf_semanas.getEst(),
				conf_semanas.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_conf_semanas where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<ConfSemanas> list() {
		String sql = "select * from col_conf_semanas";
		
		
		
		List<ConfSemanas> listConfSemanas = jdbcTemplate.query(sql, new RowMapper<ConfSemanas>() {

			@Override
			public ConfSemanas mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listConfSemanas;
	}

	public ConfSemanas get(int id) {
		String sql = "select * from col_conf_semanas WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfSemanas>() {

			@Override
			public ConfSemanas extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public ConfSemanas getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cnf_sem.id cnf_sem_id, cnf_sem.id_cnf_anio cnf_sem_id_cnf_anio , cnf_sem.nro_sem cnf_sem_nro_sem , cnf_sem.fec_ini cnf_sem_fec_ini , cnf_sem.fec_fin cnf_sem_fec_fin  ,cnf_sem.est cnf_sem_est ";
		if (aTablas.contains("col_conf_anio_escolar"))
			sql = sql + ", cnf_anio.id cnf_anio_id  , cnf_anio.id_anio cnf_anio_id_anio , cnf_anio.nro_sem cnf_anio_nro_sem  ";
	
		sql = sql + " from col_conf_semanas cnf_sem "; 
		if (aTablas.contains("col_conf_anio_escolar"))
			sql = sql + " left join col_conf_anio_escolar cnf_anio on cnf_anio.id = cnf_sem.id_cnf_anio ";
		sql = sql + " where cnf_sem.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfSemanas>() {
		
			@Override
			public ConfSemanas extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ConfSemanas confsemanas= rsToEntity(rs,"cnf_sem_");
					if (aTablas.contains("col_conf_anio_escolar")){
						ConfAnioEscolar confanioescolar = new ConfAnioEscolar();  
							confanioescolar.setId(rs.getInt("cnf_anio_id")) ;  
							confanioescolar.setId_anio(rs.getInt("cnf_anio_id_anio")) ;  
							confanioescolar.setNro_sem(rs.getInt("cnf_anio_nro_sem")) ;  
							confsemanas.setConfAnioEscolar(confanioescolar);
					}
							return confsemanas;
				}
				
				return null;
			}
			
		});


	}		
	
	public ConfSemanas getByParams(Param param) {

		String sql = "select * from col_conf_semanas " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfSemanas>() {
			@Override
			public ConfSemanas extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<ConfSemanas> listByParams(Param param, String[] order) {

		String sql = "select * from col_conf_semanas " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<ConfSemanas>() {

			@Override
			public ConfSemanas mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	/*public List<ConfSemanas> listFullByParams(ConfSemanas confsemanas, String[] order) {
	
		return listFullByParams(Param.toParam("cnf_sem",confsemanas), order);
	
	}*/	
	
	public List<ConfSemanas> listFullByParams(Param param, String[] order) {

		String sql = "select cnf_sem.id cnf_sem_id, cnf_sem.id_cnf_anio cnf_sem_id_cnf_anio , cnf_sem.nro_sem cnf_sem_nro_sem , cnf_sem.fec_ini cnf_sem_fec_ini , cnf_sem.fec_fin cnf_sem_fec_fin  ,cnf_sem.est cnf_sem_est ";
		sql = sql + ", cnf_anio.id cnf_anio_id  , cnf_anio.id_anio cnf_anio_id_anio , cnf_anio.nro_sem cnf_anio_nro_sem  ";
		sql = sql + " from col_conf_semanas cnf_sem";
		sql = sql + " left join col_conf_anio_escolar cnf_anio on cnf_anio.id = cnf_sem.id_cnf_anio ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<ConfSemanas>() {

			@Override
			public ConfSemanas mapRow(ResultSet rs, int rowNum) throws SQLException {
				ConfSemanas confsemanas= rsToEntity(rs,"cnf_sem_");
				ConfAnioEscolar confanioescolar = new ConfAnioEscolar();  
				confanioescolar.setId(rs.getInt("cnf_anio_id")) ;  
				confanioescolar.setId_anio(rs.getInt("cnf_anio_id_anio")) ;  
				confanioescolar.setNro_sem(rs.getInt("cnf_anio_nro_sem")) ;  
				confsemanas.setConfAnioEscolar(confanioescolar);
				return confsemanas;
			}

		});

	}	




	// funciones privadas utilitarias para ConfSemanas

	private ConfSemanas rsToEntity(ResultSet rs,String alias) throws SQLException {
		ConfSemanas conf_semanas = new ConfSemanas();

		conf_semanas.setId(rs.getInt( alias + "id"));
		conf_semanas.setId_cnf_anio(rs.getInt( alias + "id_cnf_anio"));
		conf_semanas.setNro_sem(rs.getInt( alias + "nro_sem"));
		conf_semanas.setFec_ini(rs.getDate( alias + "fec_ini"));
		conf_semanas.setFec_fin(rs.getDate( alias + "fec_fin"));
		conf_semanas.setEst(rs.getString( alias + "est"));
								
		return conf_semanas;

	}
	
}

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
import com.tesla.colegio.model.ConfAnioEscolar;

import com.tesla.colegio.model.Anio;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ConfAnioEscolarDAO.
 * @author MV
 *
 */
public class ConfAnioEscolarDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(ConfAnioEscolar conf_anio_escolar) {
		if (conf_anio_escolar.getId() != null) {
			// update
			String sql = "UPDATE col_conf_anio_escolar "
						+ "SET id_anio=?, "
						+ "nro_sem=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						conf_anio_escolar.getId_anio(),
						conf_anio_escolar.getNro_sem(),
						conf_anio_escolar.getEst(),
						conf_anio_escolar.getUsr_act(),
						new java.util.Date(),
						conf_anio_escolar.getId()); 
			return conf_anio_escolar.getId();

		} else {
			// insert
			String sql = "insert into col_conf_anio_escolar ("
						+ "id_anio, "
						+ "nro_sem, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				conf_anio_escolar.getId_anio(),
				conf_anio_escolar.getNro_sem(),
				conf_anio_escolar.getEst(),
				conf_anio_escolar.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_conf_anio_escolar where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<ConfAnioEscolar> list() {
		String sql = "select * from col_conf_anio_escolar";
		
		
		
		List<ConfAnioEscolar> listConfAnioEscolar = jdbcTemplate.query(sql, new RowMapper<ConfAnioEscolar>() {

			@Override
			public ConfAnioEscolar mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listConfAnioEscolar;
	}

	public ConfAnioEscolar get(int id) {
		String sql = "select * from col_conf_anio_escolar WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfAnioEscolar>() {

			@Override
			public ConfAnioEscolar extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public ConfAnioEscolar getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cnf_anio.id cnf_anio_id, cnf_anio.id_anio cnf_anio_id_anio , cnf_anio.nro_sem cnf_anio_nro_sem  ,cnf_anio.est cnf_anio_est ";
		if (aTablas.contains("col_anio"))
			sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
	
		sql = sql + " from col_conf_anio_escolar cnf_anio "; 
		if (aTablas.contains("col_anio"))
			sql = sql + " left join col_anio anio on anio.id = cnf_anio.id_anio ";
		sql = sql + " where cnf_anio.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfAnioEscolar>() {
		
			@Override
			public ConfAnioEscolar extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ConfAnioEscolar confanioescolar= rsToEntity(rs,"cnf_anio_");
					if (aTablas.contains("col_anio")){
						Anio anio = new Anio();  
							anio.setId(rs.getInt("anio_id")) ;  
							anio.setNom(rs.getString("anio_nom")) ;  
							confanioescolar.setAnio(anio);
					}
							return confanioescolar;
				}
				
				return null;
			}
			
		});


	}		
	
	public ConfAnioEscolar getByParams(Param param) {

		String sql = "select * from col_conf_anio_escolar " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfAnioEscolar>() {
			@Override
			public ConfAnioEscolar extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<ConfAnioEscolar> listByParams(Param param, String[] order) {

		String sql = "select * from col_conf_anio_escolar " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<ConfAnioEscolar>() {

			@Override
			public ConfAnioEscolar mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<ConfAnioEscolar> listFullByParams(ConfAnioEscolar confanioescolar, String[] order) {
	
		return listFullByParams(Param.toParam("cnf_anio",confanioescolar), order);
	
	}	
	
	public List<ConfAnioEscolar> listFullByParams(Param param, String[] order) {

		String sql = "select cnf_anio.id cnf_anio_id, cnf_anio.id_anio cnf_anio_id_anio , cnf_anio.nro_sem cnf_anio_nro_sem  ,cnf_anio.est cnf_anio_est ";
		sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		sql = sql + " from col_conf_anio_escolar cnf_anio";
		sql = sql + " left join col_anio anio on anio.id = cnf_anio.id_anio ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<ConfAnioEscolar>() {

			@Override
			public ConfAnioEscolar mapRow(ResultSet rs, int rowNum) throws SQLException {
				ConfAnioEscolar confanioescolar= rsToEntity(rs,"cnf_anio_");
				Anio anio = new Anio();  
				anio.setId(rs.getInt("anio_id")) ;  
				anio.setNom(rs.getString("anio_nom")) ;  
				confanioescolar.setAnio(anio);
				return confanioescolar;
			}

		});

	}	




	// funciones privadas utilitarias para ConfAnioEscolar

	private ConfAnioEscolar rsToEntity(ResultSet rs,String alias) throws SQLException {
		ConfAnioEscolar conf_anio_escolar = new ConfAnioEscolar();

		conf_anio_escolar.setId(rs.getInt( alias + "id"));
		conf_anio_escolar.setId_anio(rs.getInt( alias + "id_anio"));
		conf_anio_escolar.setNro_sem(rs.getInt( alias + "nro_sem"));
		conf_anio_escolar.setEst(rs.getString( alias + "est"));
								
		return conf_anio_escolar;

	}
	
}

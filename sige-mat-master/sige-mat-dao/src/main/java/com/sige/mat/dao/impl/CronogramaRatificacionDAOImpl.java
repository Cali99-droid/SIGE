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
import com.tesla.colegio.model.CronogramaRatificacion;

import com.tesla.colegio.model.Anio;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CronogramaRatificacionDAO.
 * @author MV
 *
 */
public class CronogramaRatificacionDAOImpl{
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
	public int saveOrUpdate(CronogramaRatificacion cronograma_ratificacion) {
		if (cronograma_ratificacion.getId() != null) {
			// update
			String sql = "UPDATE mat_cronograma_ratificacion "
						+ "SET id_anio=?, "
						+ "id_anio_rat=?, "
						+ "fec_ini=?, "
						+ "fec_fin=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						cronograma_ratificacion.getId_anio(),
						cronograma_ratificacion.getId_anio_rat(),
						cronograma_ratificacion.getFec_ini(),
						cronograma_ratificacion.getFec_fin(),
						cronograma_ratificacion.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						cronograma_ratificacion.getId()); 
			return cronograma_ratificacion.getId();

		} else {
			// insert
			String sql = "insert into mat_cronograma_ratificacion ("
						+ "id_anio, "
					    + "id_anio_rat, "
						+ "fec_ini, "
						+ "fec_fin, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				cronograma_ratificacion.getId_anio(),
				cronograma_ratificacion.getId_anio_rat(),
				cronograma_ratificacion.getFec_ini(),
				cronograma_ratificacion.getFec_fin(),
				cronograma_ratificacion.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from mat_cronograma_ratificacion where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<CronogramaRatificacion> list() {
		String sql = "select * from mat_cronograma_ratificacion";
		
		System.out.println(sql);
		
		List<CronogramaRatificacion> listCronogramaRatificacion = jdbcTemplate.query(sql, new RowMapper<CronogramaRatificacion>() {

			@Override
			public CronogramaRatificacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCronogramaRatificacion;
	}

	public CronogramaRatificacion get(int id) {
		String sql = "select * from mat_cronograma_ratificacion WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CronogramaRatificacion>() {

			@Override
			public CronogramaRatificacion extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CronogramaRatificacion getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select rat.id rat_id, rat.id_anio rat_id_anio , rat.id_anio_rat,  rat.fec_ini rat_fec_ini , rat.fec_fin rat_fec_fin  ,rat.est rat_est ";
		if (aTablas.contains("col_anio"))
			sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
	
		sql = sql + " from mat_cronograma_ratificacion rat "; 
		if (aTablas.contains("col_anio"))
			sql = sql + " left join col_anio anio on anio.id = rat.id_anio ";
		sql = sql + " where rat.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<CronogramaRatificacion>() {
		
			@Override
			public CronogramaRatificacion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CronogramaRatificacion cronogramaratificacion= rsToEntity(rs,"rat_");
					if (aTablas.contains("col_anio")){
						Anio anio = new Anio();  
							anio.setId(rs.getInt("anio_id")) ;  
							anio.setNom(rs.getString("anio_nom")) ;  
							cronogramaratificacion.setAnio(anio);
					}
							return cronogramaratificacion;
				}
				
				return null;
			}
			
		});


	}		
	
	public CronogramaRatificacion getByParams(Param param) {

		String sql = "select * from mat_cronograma_ratificacion " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CronogramaRatificacion>() {
			@Override
			public CronogramaRatificacion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CronogramaRatificacion> listByParams(Param param, String[] order) {

		String sql = "select * from mat_cronograma_ratificacion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<CronogramaRatificacion>() {

			@Override
			public CronogramaRatificacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CronogramaRatificacion> listFullByParams(CronogramaRatificacion cronogramaratificacion, String[] order) {
	
		return listFullByParams(Param.toParam("rat",cronogramaratificacion), order);
	
	}	
	
	public List<CronogramaRatificacion> listFullByParams(Param param, String[] order) {

		String sql = "select rat.id rat_id, rat.id_anio rat_id_anio , rat.id_anio_rat rat_id_anio_rat, rat.fec_ini rat_fec_ini , rat.fec_fin rat_fec_fin  ,rat.est rat_est ";
		sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		sql = sql + " from mat_cronograma_ratificacion rat";
		sql = sql + " left join col_anio anio on anio.id = rat.id_anio ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<CronogramaRatificacion>() {

			@Override
			public CronogramaRatificacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				CronogramaRatificacion cronogramaratificacion= rsToEntity(rs,"rat_");
				Anio anio = new Anio();  
				anio.setId(rs.getInt("anio_id")) ;  
				anio.setNom(rs.getString("anio_nom")) ;  
				cronogramaratificacion.setAnio(anio);
				return cronogramaratificacion;
			}

		});

	}	




	// funciones privadas utilitarias para CronogramaRatificacion

	private CronogramaRatificacion rsToEntity(ResultSet rs,String alias) throws SQLException {
		CronogramaRatificacion cronograma_ratificacion = new CronogramaRatificacion();

		cronograma_ratificacion.setId(rs.getInt( alias + "id"));
		cronograma_ratificacion.setId_anio(rs.getInt( alias + "id_anio"));
		cronograma_ratificacion.setId_anio_rat(rs.getInt( alias + "id_anio_rat"));
		cronograma_ratificacion.setFec_ini(rs.getDate( alias + "fec_ini"));
		cronograma_ratificacion.setFec_fin(rs.getDate( alias + "fec_fin"));
		cronograma_ratificacion.setEst(rs.getString( alias + "est"));
								
		return cronograma_ratificacion;

	}
	
}

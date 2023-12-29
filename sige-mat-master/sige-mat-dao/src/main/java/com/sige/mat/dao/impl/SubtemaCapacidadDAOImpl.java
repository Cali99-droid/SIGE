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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.tesla.colegio.model.SubtemaCapacidad;

import com.tesla.colegio.model.CursoSubtema;
import com.tesla.colegio.model.Capacidad;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface SubtemaCapacidadDAO.
 * @author MV
 *
 */
public class SubtemaCapacidadDAOImpl{
	final static Logger logger = Logger.getLogger(SubtemaCapacidadDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(SubtemaCapacidad subtema_capacidad) {
		if (subtema_capacidad.getId() != null) {
			// update
			String sql = "UPDATE col_subtema_capacidad "
						+ "SET id_ccs=?, "
						+ "id_cap=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						subtema_capacidad.getId_ccs(),
						subtema_capacidad.getId_cap(),
						subtema_capacidad.getEst(),
						subtema_capacidad.getUsr_act(),
						new java.util.Date(),
						subtema_capacidad.getId()); 
			return subtema_capacidad.getId();

		} else {
			// insert
			String sql = "insert into col_subtema_capacidad ("
						+ "id_ccs, "
						+ "id_cap, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				subtema_capacidad.getId_ccs(),
				subtema_capacidad.getId_cap(),
				subtema_capacidad.getEst(),
				subtema_capacidad.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_subtema_capacidad where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<SubtemaCapacidad> list() {
		String sql = "select * from col_subtema_capacidad";
		
		//logger.info(sql);
		
		List<SubtemaCapacidad> listSubtemaCapacidad = jdbcTemplate.query(sql, new RowMapper<SubtemaCapacidad>() {

			@Override
			public SubtemaCapacidad mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listSubtemaCapacidad;
	}

	public SubtemaCapacidad get(int id) {
		String sql = "select * from col_subtema_capacidad WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SubtemaCapacidad>() {

			@Override
			public SubtemaCapacidad extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public SubtemaCapacidad getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cuc.id cuc_id, cuc.id_ccs cuc_id_ccs , cuc.id_cap cuc_id_cap  ,cuc.est cuc_est ";
		if (aTablas.contains("col_curso_subtema"))
			sql = sql + ", ccs.id ccs_id  , ccs.id_anio ccs_id_anio , ccs.id_niv ccs_id_niv , ccs.id_gra ccs_id_gra , ccs.id_cur ccs_id_cur , ccs.id_sub ccs_id_sub , ccs.dur ccs_dur  ";
		if (aTablas.contains("col_capacidad"))
			sql = sql + ", cap.id cap_id  , cap.id_com cap_id_com , cap.nom cap_nom  ";
	
		sql = sql + " from col_subtema_capacidad cuc "; 
		if (aTablas.contains("col_curso_subtema"))
			sql = sql + " left join col_curso_subtema ccs on ccs.id = cuc.id_ccs ";
		if (aTablas.contains("col_capacidad"))
			sql = sql + " left join col_capacidad cap on cap.id = cuc.id_cap ";
		sql = sql + " where cuc.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<SubtemaCapacidad>() {
		
			@Override
			public SubtemaCapacidad extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					SubtemaCapacidad subtemacapacidad= rsToEntity(rs,"cuc_");
					if (aTablas.contains("col_curso_subtema")){
						CursoSubtema cursosubtema = new CursoSubtema();  
							cursosubtema.setId(rs.getInt("ccs_id")) ;  
							cursosubtema.setId_anio(rs.getInt("ccs_id_anio")) ;  
							cursosubtema.setId_niv(rs.getInt("ccs_id_niv")) ;  
							cursosubtema.setId_gra(rs.getInt("ccs_id_gra")) ;  
							cursosubtema.setId_cur(rs.getInt("ccs_id_cur")) ;  
							cursosubtema.setId_sub(rs.getInt("ccs_id_sub")) ;  
							cursosubtema.setDur(rs.getBigDecimal("ccs_dur")) ;  
							subtemacapacidad.setCursoSubtema(cursosubtema);
					}
					if (aTablas.contains("col_capacidad")){
						Capacidad capacidad = new Capacidad();  
							capacidad.setId(rs.getInt("cap_id")) ;  
							capacidad.setId_com(rs.getInt("cap_id_com")) ;  
							capacidad.setNom(rs.getString("cap_nom")) ;  
							subtemacapacidad.setCapacidad(capacidad);
					}
							return subtemacapacidad;
				}
				
				return null;
			}
			
		});


	}		
	
	public SubtemaCapacidad getByParams(Param param) {

		String sql = "select * from col_subtema_capacidad " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SubtemaCapacidad>() {
			@Override
			public SubtemaCapacidad extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<SubtemaCapacidad> listByParams(Param param, String[] order) {

		String sql = "select * from col_subtema_capacidad " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<SubtemaCapacidad>() {

			@Override
			public SubtemaCapacidad mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<SubtemaCapacidad> listFullByParams(SubtemaCapacidad subtemacapacidad, String[] order) {
	
		return listFullByParams(Param.toParam("cuc",subtemacapacidad), order);
	
	}	
	
	public List<SubtemaCapacidad> listFullByParams(Param param, String[] order) {

		String sql = "select cuc.id cuc_id, cuc.id_ccs cuc_id_ccs , cuc.id_cap cuc_id_cap  ,cuc.est cuc_est ";
		sql = sql + ", ccs.id ccs_id  , ccs.id_anio ccs_id_anio , ccs.id_niv ccs_id_niv , ccs.id_gra ccs_id_gra , ccs.id_cur ccs_id_cur , ccs.id_sub ccs_id_sub , ccs.dur ccs_dur  ";
		sql = sql + ", cap.id cap_id  , cap.id_com cap_id_com , cap.nom cap_nom  ";
		sql = sql + " from col_subtema_capacidad cuc";
		sql = sql + " left join col_curso_subtema ccs on ccs.id = cuc.id_ccs ";
		sql = sql + " left join col_capacidad cap on cap.id = cuc.id_cap ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<SubtemaCapacidad>() {

			@Override
			public SubtemaCapacidad mapRow(ResultSet rs, int rowNum) throws SQLException {
				SubtemaCapacidad subtemacapacidad= rsToEntity(rs,"cuc_");
				CursoSubtema cursosubtema = new CursoSubtema();  
				cursosubtema.setId(rs.getInt("ccs_id")) ;  
				cursosubtema.setId_anio(rs.getInt("ccs_id_anio")) ;  
				cursosubtema.setId_niv(rs.getInt("ccs_id_niv")) ;  
				cursosubtema.setId_gra(rs.getInt("ccs_id_gra")) ;  
				cursosubtema.setId_cur(rs.getInt("ccs_id_cur")) ;  
				cursosubtema.setId_sub(rs.getInt("ccs_id_sub")) ;  
				cursosubtema.setDur(rs.getBigDecimal("ccs_dur")) ;  
				subtemacapacidad.setCursoSubtema(cursosubtema);
				Capacidad capacidad = new Capacidad();  
				capacidad.setId(rs.getInt("cap_id")) ;  
				capacidad.setId_com(rs.getInt("cap_id_com")) ;  
				capacidad.setNom(rs.getString("cap_nom")) ;  
				subtemacapacidad.setCapacidad(capacidad);
				return subtemacapacidad;
			}

		});

	}	




	// funciones privadas utilitarias para SubtemaCapacidad

	private SubtemaCapacidad rsToEntity(ResultSet rs,String alias) throws SQLException {
		SubtemaCapacidad subtema_capacidad = new SubtemaCapacidad();

		subtema_capacidad.setId(rs.getInt( alias + "id"));
		subtema_capacidad.setId_ccs(rs.getInt( alias + "id_ccs"));
		subtema_capacidad.setId_cap(rs.getInt( alias + "id_cap"));
		subtema_capacidad.setEst(rs.getString( alias + "est"));
								
		return subtema_capacidad;

	}
	
}

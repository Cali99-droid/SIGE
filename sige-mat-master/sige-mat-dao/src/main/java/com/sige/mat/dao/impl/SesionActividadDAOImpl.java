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
import com.tesla.colegio.model.SesionActividad;

import com.tesla.colegio.model.UnidadSesion;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface SesionActividadDAO.
 * @author MV
 *
 */
public class SesionActividadDAOImpl{
	final static Logger logger = Logger.getLogger(SesionActividadDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(SesionActividad sesion_actividad) {
		if (sesion_actividad.getId() != null) {
			// update
			String sql = "UPDATE col_sesion_actividad "
						+ "SET id_ses=?, "
						+ "nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						sesion_actividad.getId_ses(),
						sesion_actividad.getNom(),
						sesion_actividad.getEst(),
						sesion_actividad.getUsr_act(),
						new java.util.Date(),
						sesion_actividad.getId()); 
			return sesion_actividad.getId();

		} else {
			// insert
			String sql = "insert into col_sesion_actividad ("
						+ "id_ses, "
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				sesion_actividad.getId_ses(),
				sesion_actividad.getNom(),
				sesion_actividad.getEst(),
				sesion_actividad.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_sesion_actividad where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<SesionActividad> list() {
		String sql = "select * from col_sesion_actividad";
		
		//logger.info(sql);
		
		List<SesionActividad> listSesionActividad = jdbcTemplate.query(sql, new RowMapper<SesionActividad>() {

			@Override
			public SesionActividad mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listSesionActividad;
	}

	public SesionActividad get(int id) {
		String sql = "select * from col_sesion_actividad WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SesionActividad>() {

			@Override
			public SesionActividad extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public SesionActividad getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select csa.id csa_id, csa.id_ses csa_id_ses , csa.nom csa_nom  ,csa.est csa_est ";
		if (aTablas.contains("col_unidad_sesion"))
			sql = sql + ", ses.id ses_id  , ses.id_uni ses_id_uni , ses.tit ses_tit , ses.dur ses_dur , ses.tipo ses_tipo  ";
	
		sql = sql + " from col_sesion_actividad csa "; 
		if (aTablas.contains("col_unidad_sesion"))
			sql = sql + " left join col_unidad_sesion ses on ses.id = csa.id_ses ";
		sql = sql + " where csa.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<SesionActividad>() {
		
			@Override
			public SesionActividad extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					SesionActividad sesionactividad= rsToEntity(rs,"csa_");
					if (aTablas.contains("col_unidad_sesion")){
						UnidadSesion unidadsesion = new UnidadSesion();  
							unidadsesion.setId(rs.getInt("ses_id")) ;  
							unidadsesion.setId_uni(rs.getInt("ses_id_uni")) ;  
							//unidadsesion.setTit(rs.getString("ses_tit")) ;  
							//unidadsesion.setDur(rs.getBigDecimal("ses_dur")) ;  
							//unidadsesion.setTipo(rs.getString("ses_tipo")) ;  
							sesionactividad.setUnidadSesion(unidadsesion);
					}
							return sesionactividad;
				}
				
				return null;
			}
			
		});


	}		
	
	public SesionActividad getByParams(Param param) {

		String sql = "select * from col_sesion_actividad " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SesionActividad>() {
			@Override
			public SesionActividad extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<SesionActividad> listByParams(Param param, String[] order) {

		String sql = "select * from col_sesion_actividad " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<SesionActividad>() {

			@Override
			public SesionActividad mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<SesionActividad> listFullByParams(SesionActividad sesionactividad, String[] order) {
	
		return listFullByParams(Param.toParam("csa",sesionactividad), order);
	
	}	
	
	public List<SesionActividad> listFullByParams(Param param, String[] order) {

		String sql = "select csa.id csa_id, csa.id_ses csa_id_ses , csa.nom csa_nom  ,csa.est csa_est ";
		sql = sql + ", ses.id ses_id  , ses.id_uni ses_id_uni , ses.tit ses_tit , ses.dur ses_dur , ses.tipo ses_tipo  ";
		sql = sql + " from col_sesion_actividad csa";
		sql = sql + " left join col_unidad_sesion ses on ses.id = csa.id_ses ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<SesionActividad>() {

			@Override
			public SesionActividad mapRow(ResultSet rs, int rowNum) throws SQLException {
				SesionActividad sesionactividad= rsToEntity(rs,"csa_");
				UnidadSesion unidadsesion = new UnidadSesion();  
				unidadsesion.setId(rs.getInt("ses_id")) ;  
				unidadsesion.setId_uni(rs.getInt("ses_id_uni")) ;  
				//unidadsesion.setTit(rs.getString("ses_tit")) ;  
				//unidadsesion.setDur(rs.getBigDecimal("ses_dur")) ;  
				//unidadsesion.setTipo(rs.getString("ses_tipo")) ;  
				sesionactividad.setUnidadSesion(unidadsesion);
				return sesionactividad;
			}

		});

	}	




	// funciones privadas utilitarias para SesionActividad

	private SesionActividad rsToEntity(ResultSet rs,String alias) throws SQLException {
		SesionActividad sesion_actividad = new SesionActividad();

		sesion_actividad.setId(rs.getInt( alias + "id"));
		sesion_actividad.setId_ses(rs.getInt( alias + "id_ses"));
		sesion_actividad.setNom(rs.getString( alias + "nom"));
		sesion_actividad.setEst(rs.getString( alias + "est"));
								
		return sesion_actividad;

	}
	
}

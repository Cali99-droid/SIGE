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
import com.tesla.colegio.model.ConfFechas;

import com.tesla.colegio.model.Anio;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ConfFechasDAO.
 * @author MV
 *
 */
public class ConfFechasDAOImpl{
	final static Logger logger = Logger.getLogger(ConfFechasDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(ConfFechas conf_fechas) {
		if (conf_fechas.getId() != null) {
			// update
			String sql = "UPDATE mat_conf_fechas "
						+ "SET id_anio=?, "
						+ "tipo=?, "
						+ "del=?, "
						+ "al=?, "
						+ "del_cs=?, "
						+ "al_cs=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						conf_fechas.getId_anio(),
						conf_fechas.getTipo(),
						conf_fechas.getDel(),
						conf_fechas.getAl(),
						conf_fechas.getDel_cs(),
						conf_fechas.getAl_cs(),
						conf_fechas.getEst(),
						conf_fechas.getUsr_act(),
						new java.util.Date(),
						conf_fechas.getId()); 
			return conf_fechas.getId();

		} else {
			// insert
			String sql = "insert into mat_conf_fechas ("
						+ "id_anio, "
						+ "tipo, "
						+ "del, "
						+ "al, "
						+ "del_cs, "
						+ "al_cs, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				conf_fechas.getId_anio(),
				conf_fechas.getTipo(),
				conf_fechas.getDel(),
				conf_fechas.getAl(),
				conf_fechas.getDel_cs(),
				conf_fechas.getAl_cs(),
				conf_fechas.getEst(),
				conf_fechas.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from mat_conf_fechas where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<ConfFechas> list() {
		String sql = "select * from mat_conf_fechas";
		
		//logger.info(sql);
		
		List<ConfFechas> listConfFechas = jdbcTemplate.query(sql, new RowMapper<ConfFechas>() {

			@Override
			public ConfFechas mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listConfFechas;
	}

	public ConfFechas get(int id) {
		String sql = "select * from mat_conf_fechas WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfFechas>() {

			@Override
			public ConfFechas extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public ConfFechas getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select mat_cro.id mat_cro_id, mat_cro.id_anio mat_cro_id_anio , mat_cro.tipo mat_cro_tipo , mat_cro.del mat_cro_del , mat_cro.al mat_cro_al  ,mat_cro.est mat_cro_est ";
		if (aTablas.contains("col_anio"))
			sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
	
		sql = sql + " from mat_conf_fechas mat_cro "; 
		if (aTablas.contains("col_anio"))
			sql = sql + " left join col_anio anio on anio.id = mat_cro.id_anio ";
		sql = sql + " where mat_cro.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfFechas>() {
		
			@Override
			public ConfFechas extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ConfFechas conffechas= rsToEntity(rs,"mat_cro_");
					if (aTablas.contains("col_anio")){
						Anio anio = new Anio();  
							anio.setId(rs.getInt("anio_id")) ;  
							anio.setNom(rs.getString("anio_nom")) ;  
							conffechas.setAnio(anio);
					}
							return conffechas;
				}
				
				return null;
			}
			
		});


	}		
	
	public ConfFechas getByParams(Param param) {

		String sql = "select * from mat_conf_fechas " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfFechas>() {
			@Override
			public ConfFechas extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<ConfFechas> listByParams(Param param, String[] order) {

		String sql = "select * from mat_conf_fechas " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<ConfFechas>() {

			@Override
			public ConfFechas mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<ConfFechas> listFullByParams(ConfFechas conffechas, String[] order) {
	
		return listFullByParams(Param.toParam("mat_cro",conffechas), order);
	
	}	
	
	public List<ConfFechas> listFullByParams(Param param, String[] order) {

		String sql = "select mat_cro.id mat_cro_id, mat_cro.id_anio mat_cro_id_anio , mat_cro.tipo mat_cro_tipo , mat_cro.del mat_cro_del , mat_cro.al mat_cro_al ,mat_cro.del_cs mat_cro_del_cs , mat_cro.al_cs mat_cro_al_cs, mat_cro.est mat_cro_est ";
		sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		sql = sql + " from mat_conf_fechas mat_cro";
		sql = sql + " left join col_anio anio on anio.id = mat_cro.id_anio ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<ConfFechas>() {

			@Override
			public ConfFechas mapRow(ResultSet rs, int rowNum) throws SQLException {
				ConfFechas conffechas= rsToEntity(rs,"mat_cro_");
				Anio anio = new Anio();  
				anio.setId(rs.getInt("anio_id")) ;  
				anio.setNom(rs.getString("anio_nom")) ;  
				conffechas.setAnio(anio);
				return conffechas;
			}

		});

	}	




	// funciones privadas utilitarias para ConfFechas

	private ConfFechas rsToEntity(ResultSet rs,String alias) throws SQLException {
		ConfFechas conf_fechas = new ConfFechas();

		conf_fechas.setId(rs.getInt( alias + "id"));
		conf_fechas.setId_anio(rs.getInt( alias + "id_anio"));
		conf_fechas.setTipo(rs.getString( alias + "tipo"));
		conf_fechas.setDel(rs.getTimestamp( alias + "del"));
		conf_fechas.setAl(rs.getTimestamp( alias + "al"));
		conf_fechas.setEst(rs.getString( alias + "est"));
		conf_fechas.setDel_cs(rs.getTimestamp( alias + "del_cs"));
		conf_fechas.setAl_cs(rs.getTimestamp( alias + "al_cs"));
								
		return conf_fechas;

	}
	
}

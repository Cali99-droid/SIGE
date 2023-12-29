package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
import com.tesla.colegio.model.ConfTipo;

import com.tesla.colegio.model.Configuracion;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ConfTipoDAO.
 * @author MV
 *
 */
public class ConfTipoDAOImpl{
	final static Logger logger = Logger.getLogger(ConfTipoDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(ConfTipo conf_tipo) {
		if (conf_tipo.getId() != null) {
			// update
			String sql = "UPDATE eco_conf_tipo "
						+ "SET tip=?, "
						+ "nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						conf_tipo.getTip(),
						conf_tipo.getNom(),
						conf_tipo.getEst(),
						conf_tipo.getUsr_act(),
						new java.util.Date(),
						conf_tipo.getId()); 
			return conf_tipo.getId();

		} else {
			// insert
			String sql = "insert into eco_conf_tipo ("
						+ "tip, "
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				conf_tipo.getTip(),
				conf_tipo.getNom(),
				conf_tipo.getEst(),
				conf_tipo.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from eco_conf_tipo where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<ConfTipo> list() {
		String sql = "select * from eco_conf_tipo";
		
		//logger.info(sql);
		
		List<ConfTipo> listConfTipo = jdbcTemplate.query(sql, new RowMapper<ConfTipo>() {

			@Override
			public ConfTipo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listConfTipo;
	}

	public ConfTipo get(int id) {
		String sql = "select * from eco_conf_tipo WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfTipo>() {

			@Override
			public ConfTipo extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public ConfTipo getFull(int id, String tablas[]) {
		String sql = "select ect.id ect_id, ect.tip ect_tip , ect.nom ect_nom  ,ect.est ect_est ";
	
		sql = sql + " from eco_conf_tipo ect "; 
		sql = sql + " where ect.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfTipo>() {
		
			@Override
			public ConfTipo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ConfTipo conftipo= rsToEntity(rs,"ect_");
							return conftipo;
				}
				
				return null;
			}
			
		});


	}		
	
	public ConfTipo getByParams(Param param) {

		String sql = "select * from eco_conf_tipo " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfTipo>() {
			@Override
			public ConfTipo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<ConfTipo> listByParams(Param param, String[] order) {

		String sql = "select * from eco_conf_tipo " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<ConfTipo>() {

			@Override
			public ConfTipo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<ConfTipo> listFullByParams(ConfTipo conftipo, String[] order) {
	
		return listFullByParams(Param.toParam("ect",conftipo), order);
	
	}	
	
	public List<ConfTipo> listFullByParams(Param param, String[] order) {

		String sql = "select ect.id ect_id, ect.tip ect_tip , ect.nom ect_nom  ,ect.est ect_est ";
		sql = sql + " from eco_conf_tipo ect";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<ConfTipo>() {

			@Override
			public ConfTipo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ConfTipo conftipo= rsToEntity(rs,"ect_");
				return conftipo;
			}

		});

	}	


	public List<Configuracion> getListConfiguracion(Param param, String[] order) {
		String sql = "select * from eco_configuracion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Configuracion>() {

			@Override
			public Configuracion mapRow(ResultSet rs, int rowNum) throws SQLException {
				Configuracion configuracion = new Configuracion();

				configuracion.setId(rs.getInt("id"));
				configuracion.setCondicion(rs.getString("condicion"));
				configuracion.setTip_resp(rs.getString("tip_resp"));
				configuracion.setId_ect(rs.getInt("id_ect"));
				configuracion.setPtj(rs.getBigDecimal("ptj"));
				configuracion.setEst(rs.getString("est"));
												
				return configuracion;
			}

		});	
	}


	// funciones privadas utilitarias para ConfTipo

	private ConfTipo rsToEntity(ResultSet rs,String alias) throws SQLException {
		ConfTipo conf_tipo = new ConfTipo();

		conf_tipo.setId(rs.getInt( alias + "id"));
		conf_tipo.setTip(rs.getString( alias + "tip"));
		conf_tipo.setNom(rs.getString( alias + "nom"));
		conf_tipo.setEst(rs.getString( alias + "est"));
								
		return conf_tipo;

	}
	
}

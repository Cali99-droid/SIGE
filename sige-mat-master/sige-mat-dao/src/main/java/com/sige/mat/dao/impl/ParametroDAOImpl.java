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
import com.tesla.colegio.model.Parametro;

import com.tesla.colegio.model.Modulo;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci&oacute;n de la interface ParametroDAO.
 * @author MV
 *
 */
public class ParametroDAOImpl{
	final static Logger logger = Logger.getLogger(ParametroDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public ParametroDAOImpl() {
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Parametro parametro) {
		if (parametro.getId() !=null ) {
			// update
			String sql = "UPDATE mod_parametro "
						+ "SET id_mod=?, "
						+ "nom=?, "
						+ "des=?, "
						+ "val=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						parametro.getId_mod(),
						parametro.getNom(),
						parametro.getDes(),
						parametro.getVal(),
						parametro.getEst(),
						parametro.getUsr_act(),
						new java.util.Date(),
						parametro.getId()); 

		} else {
			// insert
			String sql = "insert into mod_parametro ("
						+ "id_mod, "
						+ "nom, "
						+ "des, "
						+ "val, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				parametro.getId_mod(),
				parametro.getNom(),
				parametro.getDes(),
				parametro.getVal(),
				parametro.getEst(),
				parametro.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from mod_parametro where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Parametro> list() {
		String sql = "select * from mod_parametro";
		
		//logger.info(sql);
		
		List<Parametro> listParametro = jdbcTemplate.query(sql, new RowMapper<Parametro>() {

			
			public Parametro mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listParametro;
	}

	public Parametro get(int id) {
		String sql = "select * from mod_parametro WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Parametro>() {

			
			public Parametro extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Parametro getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select par.id par_id, par.id_mod par_id_mod , par.nom par_nom , par.des par_des , par.val par_val  ,par.est par_est ";
		if (aTablas.contains("mod_modulo"))
			sql = sql + ", mod.id mod_id  , mod.cod mod_cod , mod.nom mod_nom , mod.des mod_des  ";
	
		sql = sql + " from mod_parametro par "; 
		if (aTablas.contains("mod_modulo"))
			sql = sql + " left join mod_modulo mod on mod.id = par.id_mod ";
		sql = sql + " where par.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Parametro>() {
		
			
			public Parametro extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Parametro parametro= rsToEntity(rs,"par_");
					if (aTablas.contains("mod_modulo")){
						Modulo modulo = new Modulo();  
							modulo.setId(rs.getInt("mod_id")) ;  
							modulo.setCod(rs.getString("mod_cod")) ;  
							modulo.setNom(rs.getString("mod_nom")) ;  
							modulo.setDes(rs.getString("mod_des")) ;  
							parametro.setModulo(modulo);
					}
							return parametro;
				}
				
				return null;
			}
			
		});


	}		
	
	public Parametro getByParams(Param param) {

		String sql = "select * from mod_parametro " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Parametro>() {
			
			public Parametro extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Parametro> listByParams(Param param, String[] order) {

		String sql = "select * from mod_parametro " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Parametro>() {

			
			public Parametro mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Parametro> listFullByParams(Parametro parametro, String[] order) {
	
		return listFullByParams(Param.toParam("par",parametro), order);
	
	}	
	
	public List<Parametro> listFullByParams(Param param, String[] order) {

		String sql = "select par.id par_id, par.id_mod par_id_mod , par.nom par_nom , par.des par_des , par.val par_val  ,par.est par_est ";
		sql = sql + ", mod1.id mod_id  , mod1.cod mod_cod , mod1.nom mod_nom , mod1.des mod_des  ";
		sql = sql + " from mod_parametro par";
		sql = sql + " left join mod_modulo mod1 on mod1.id = par.id_mod ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Parametro>() {

			
			public Parametro mapRow(ResultSet rs, int rowNum) throws SQLException {
				Parametro parametro= rsToEntity(rs,"par_");
				Modulo modulo = new Modulo();  
				modulo.setId(rs.getInt("mod_id")) ;  
				modulo.setCod(rs.getString("mod_cod")) ;  
				modulo.setNom(rs.getString("mod_nom")) ;  
				modulo.setDes(rs.getString("mod_des")) ;  
				parametro.setModulo(modulo);
				return parametro;
			}

		});

	}	




	// funciones privadas utilitarias para Parametro

	private Parametro rsToEntity(ResultSet rs,String alias) throws SQLException {
		Parametro parametro = new Parametro();

		parametro.setId(rs.getInt( alias + "id"));
		parametro.setId_mod(rs.getInt( alias + "id_mod"));
		parametro.setNom(rs.getString( alias + "nom"));
		parametro.setDes(rs.getString( alias + "des"));
		parametro.setVal(rs.getString( alias + "val"));
		parametro.setEst(rs.getString( alias + "est"));
		parametro.setTipo_html(rs.getString(alias + "tipo_html"));
								
		return parametro;

	}
	
}

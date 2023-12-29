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
import com.tesla.colegio.model.Provincia;

import com.tesla.colegio.model.Departamento;
import com.tesla.colegio.model.Distrito;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ProvinciaDAO.
 * @author MV
 *
 */
public class ProvinciaDAOImpl{
	final static Logger logger = Logger.getLogger(ProvinciaDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Provincia provincia) {
		if (provincia.getId() > 0) {
			// update
			String sql = "UPDATE cat_provincia "
						+ "SET nom=?, "
						+ "id_dep=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						provincia.getNom(),
						provincia.getId_dep(),
						provincia.getEst(),
						provincia.getUsr_act(),
						new java.util.Date(),
						provincia.getId()); 

		} else {
			// insert
			String sql = "insert into cat_provincia ("
						+ "nom, "
						+ "id_dep, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				provincia.getNom(),
				provincia.getId_dep(),
				provincia.getEst(),
				provincia.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from cat_provincia where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<Provincia> list() {
		String sql = "select * from cat_provincia";
		
		//logger.info(sql);
		
		List<Provincia> listProvincia = jdbcTemplate.query(sql, new RowMapper<Provincia>() {

			
			public Provincia mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listProvincia;
	}

	
	public Provincia get(int id) {
		String sql = "select * from cat_provincia WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Provincia>() {

			
			public Provincia extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public Provincia getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select pro.id pro_id, pro.nom pro_nom , pro.id_dep pro_id_dep  ,pro.est pro_est ";
		if (aTablas.contains("cat_departamento"))
			sql = sql + ", dep.id dep_id  , dep.nom dep_nom  ";
	
		sql = sql + " from cat_provincia pro "; 
		if (aTablas.contains("cat_departamento"))
			sql = sql + " left join cat_departamento dep on dep.id = pro.id_dep ";
		sql = sql + " where pro.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Provincia>() {
		
			
			public Provincia extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Provincia provincia= rsToEntity(rs,"pro_");
					if (aTablas.contains("cat_departamento")){
						Departamento departamento = new Departamento();  
							departamento.setId(rs.getInt("dep_id")) ;  
							departamento.setNom(rs.getString("dep_nom")) ;  
							provincia.setDepartamento(departamento);
					}
							return provincia;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public Provincia getByParams(Param param) {

		String sql = "select * from cat_provincia " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Provincia>() {
			
			public Provincia extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<Provincia> listByParams(Param param, String[] order) {

		String sql = "select * from cat_provincia " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Provincia>() {

			
			public Provincia mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<Provincia> listFullByParams(Provincia provincia, String[] order) {
	
		return listFullByParams(Param.toParam("pro",provincia), order);
	
	}	
	
	
	public List<Provincia> listFullByParams(Param param, String[] order) {

		String sql = "select pro.id pro_id, pro.nom pro_nom , pro.id_dep pro_id_dep  ,pro.est pro_est ";
		sql = sql + ", dep.id dep_id  , dep.nom dep_nom  ";
		sql = sql + " from cat_provincia pro";
		sql = sql + " left join cat_departamento dep on dep.id = pro.id_dep ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Provincia>() {

			
			public Provincia mapRow(ResultSet rs, int rowNum) throws SQLException {
				Provincia provincia= rsToEntity(rs,"pro_");
				Departamento departamento = new Departamento();  
				departamento.setId(rs.getInt("dep_id")) ;  
				departamento.setNom(rs.getString("dep_nom")) ;  
				provincia.setDepartamento(departamento);
				return provincia;
			}

		});

	}	


	public List<Distrito> getListDistrito(Param param, String[] order) {
		String sql = "select * from cat_distrito " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Distrito>() {

			
			public Distrito mapRow(ResultSet rs, int rowNum) throws SQLException {
				Distrito distrito = new Distrito();

				distrito.setId(rs.getInt("id"));
				distrito.setNom(rs.getString("nom"));
				distrito.setId_pro(rs.getInt("id_pro"));
				distrito.setEst(rs.getString("est"));
												
				return distrito;
			}

		});	
	}


	// funciones privadas utilitarias para Provincia

	private Provincia rsToEntity(ResultSet rs,String alias) throws SQLException {
		Provincia provincia = new Provincia();

		provincia.setId(rs.getInt( alias + "id"));
		provincia.setNom(rs.getString( alias + "nom"));
		provincia.setId_dep(rs.getInt( alias + "id_dep"));
		provincia.setEst(rs.getString( alias + "est"));
								
		return provincia;

	}
	
}

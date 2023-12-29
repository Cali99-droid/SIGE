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
import com.tesla.colegio.model.Departamento;

import com.tesla.colegio.model.Provincia;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface DepartamentoDAO.
 * @author MV
 *
 */
public class DepartamentoDAOImpl{
	final static Logger logger = Logger.getLogger(DepartamentoDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Departamento departamento) {
		if (departamento.getId() > 0) {
			// update
			String sql = "UPDATE cat_departamento "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						departamento.getNom(),
						departamento.getEst(),
						departamento.getUsr_act(),
						new java.util.Date(),
						departamento.getId()); 

		} else {
			// insert
			String sql = "insert into cat_departamento ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				departamento.getNom(),
				departamento.getEst(),
				departamento.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from cat_departamento where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<Departamento> list() {
		String sql = "select * from cat_departamento";
		
		//logger.info(sql);
		
		List<Departamento> listDepartamento = jdbcTemplate.query(sql, new RowMapper<Departamento>() {

			
			public Departamento mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listDepartamento;
	}

	
	public Departamento get(int id) {
		String sql = "select * from cat_departamento WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Departamento>() {

			
			public Departamento extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public Departamento getFull(int id, String tablas[]) {
		String sql = "select dep.id dep_id, dep.nom dep_nom  ,dep.est dep_est ";
	
		sql = sql + " from cat_departamento dep "; 
		sql = sql + " where dep.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Departamento>() {
		
			
			public Departamento extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Departamento departamento= rsToEntity(rs,"dep_");
							return departamento;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public Departamento getByParams(Param param) {

		String sql = "select * from cat_departamento " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Departamento>() {
			
			public Departamento extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<Departamento> listByParams(Param param, String[] order) {

		String sql = "select * from cat_departamento " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Departamento>() {

			
			public Departamento mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<Departamento> listFullByParams(Departamento departamento, String[] order) {
	
		return listFullByParams(Param.toParam("dep",departamento), order);
	
	}	
	
	
	public List<Departamento> listFullByParams(Param param, String[] order) {

		String sql = "select dep.id dep_id, dep.nom dep_nom  ,dep.est dep_est ";
		sql = sql + " from cat_departamento dep";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Departamento>() {

			
			public Departamento mapRow(ResultSet rs, int rowNum) throws SQLException {
				Departamento departamento= rsToEntity(rs,"dep_");
				return departamento;
			}

		});

	}	


	public List<Provincia> getListProvincia(Param param, String[] order) {
		String sql = "select * from cat_provincia " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Provincia>() {

			
			public Provincia mapRow(ResultSet rs, int rowNum) throws SQLException {
				Provincia provincia = new Provincia();

				provincia.setId(rs.getInt("id"));
				provincia.setNom(rs.getString("nom"));
				provincia.setId_dep(rs.getInt("id_dep"));
				provincia.setEst(rs.getString("est"));
												
				return provincia;
			}

		});	
	}


	// funciones privadas utilitarias para Departamento

	private Departamento rsToEntity(ResultSet rs,String alias) throws SQLException {
		Departamento departamento = new Departamento();

		departamento.setId(rs.getInt( alias + "id"));
		departamento.setNom(rs.getString( alias + "nom"));
		departamento.setEst(rs.getString( alias + "est"));
								
		return departamento;

	}
	
}

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
import com.tesla.colegio.model.Curso;

import com.tesla.colegio.model.CursoAnio;
import com.tesla.colegio.model.Tema;
import com.tesla.colegio.model.CursoSubtema;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CursoDAO.
 * @author MV
 *
 */
public class CursoDAOImpl{
	final static Logger logger = Logger.getLogger(CursoDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Curso curso) {
		if (curso.getId() != null) {
			// update
			String sql = "UPDATE cat_curso "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						curso.getNom(),
						curso.getEst(),
						curso.getUsr_act(),
						new java.util.Date(),
						curso.getId()); 
			return curso.getId();

		} else {
			// insert
			String sql = "insert into cat_curso ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				curso.getNom(),
				curso.getEst(),
				curso.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_curso where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Curso> list() {
		String sql = "select * from cat_curso";
		
		//logger.info(sql);
		
		List<Curso> listCurso = jdbcTemplate.query(sql, new RowMapper<Curso>() {

			@Override
			public Curso mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCurso;
	}

	public Curso get(int id) {
		String sql = "select * from cat_curso WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Curso>() {

			@Override
			public Curso extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Curso getFull(int id, String tablas[]) {
		String sql = "select cur.id cur_id, cur.nom cur_nom  ,cur.est cur_est ";
	
		sql = sql + " from cat_curso cur "; 
		sql = sql + " where cur.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Curso>() {
		
			@Override
			public Curso extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Curso curso= rsToEntity(rs,"cur_");
							return curso;
				}
				
				return null;
			}
			
		});


	}		
	
	public Curso getByParams(Param param) {

		String sql = "select * from cat_curso " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Curso>() {
			@Override
			public Curso extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Curso> listByParams(Param param, String[] order) {

		String sql = "select * from cat_curso " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Curso>() {

			@Override
			public Curso mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Curso> listFullByParams(Curso curso, String[] order) {
	
		return listFullByParams(Param.toParam("cur",curso), order);
	
	}	
	
	public List<Curso> listFullByParams(Param param, String[] order) {

		String sql = "select cur.id cur_id, cur.nom cur_nom  ,cur.est cur_est ";
		sql = sql + " from cat_curso cur";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Curso>() {

			@Override
			public Curso mapRow(ResultSet rs, int rowNum) throws SQLException {
				Curso curso= rsToEntity(rs,"cur_");
				return curso;
			}

		});

	}	


	public List<CursoAnio> getListCursoAnio(Param param, String[] order) {
		String sql = "select * from col_curso_anio " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<CursoAnio>() {

			@Override
			public CursoAnio mapRow(ResultSet rs, int rowNum) throws SQLException {
				CursoAnio curso_anio = new CursoAnio();

				curso_anio.setId(rs.getInt("id"));
				curso_anio.setId_per(rs.getInt("id_per"));
				curso_anio.setId_gra(rs.getInt("id_gra"));
				curso_anio.setId_caa(rs.getInt("id_caa"));
				curso_anio.setId_cur(rs.getInt("id_cur"));
				curso_anio.setPeso(rs.getInt("peso"));
				curso_anio.setOrden(rs.getInt("orden"));
				curso_anio.setFlg_prom(rs.getString("flg_prom"));
				curso_anio.setEst(rs.getString("est"));
												
				return curso_anio;
			}

		});	
	}
	public List<Tema> getListTema(Param param, String[] order) {
		String sql = "select * from col_tema " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Tema>() {

			@Override
			public Tema mapRow(ResultSet rs, int rowNum) throws SQLException {
				Tema tema = new Tema();

				tema.setId(rs.getInt("id"));
				tema.setId_niv(rs.getInt("id_niv"));
				tema.setId_cur(rs.getInt("id_cur"));
				tema.setNom(rs.getString("nom"));
				tema.setOrd(rs.getInt("pri"));
				tema.setEst(rs.getString("est"));
												
				return tema;
			}

		});	
	}
	public List<CursoSubtema> getListCursoSubtema(Param param, String[] order) {
		String sql = "select * from col_curso_subtema " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<CursoSubtema>() {

			@Override
			public CursoSubtema mapRow(ResultSet rs, int rowNum) throws SQLException {
				CursoSubtema curso_subtema = new CursoSubtema();

				curso_subtema.setId(rs.getInt("id"));
				curso_subtema.setId_anio(rs.getInt("id_anio"));
				curso_subtema.setId_cur(rs.getInt("id_cur"));
				curso_subtema.setId_sub(rs.getInt("id_sub"));
				curso_subtema.setId_gra(rs.getInt("id_gra"));
				curso_subtema.setDur(rs.getBigDecimal("dur"));
				curso_subtema.setEst(rs.getString("est"));
												
				return curso_subtema;
			}

		});	
	}


	// funciones privadas utilitarias para Curso

	private Curso rsToEntity(ResultSet rs,String alias) throws SQLException {
		Curso curso = new Curso();

		curso.setId(rs.getInt( alias + "id"));
		curso.setNom(rs.getString( alias + "nom"));
		curso.setEst(rs.getString( alias + "est"));
								
		return curso;

	}
	
}

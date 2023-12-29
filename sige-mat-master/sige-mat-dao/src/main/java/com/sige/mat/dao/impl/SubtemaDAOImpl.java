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
import com.tesla.colegio.model.Subtema;

import com.tesla.colegio.model.Tema;
import com.tesla.colegio.model.CursoSubtema;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface SubtemaDAO.
 * @author MV
 *
 */
public class SubtemaDAOImpl{
	final static Logger logger = Logger.getLogger(SubtemaDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Subtema subtema) {
		if (subtema.getId() != null) {
			// update
			String sql = "UPDATE col_subtema "
						+ "SET id_tem=?, "
						+ "nom=?, "
						+ "ord=?, "
						+ "obs=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						subtema.getId_tem(),
						subtema.getNom(),
						subtema.getOrd(),
						subtema.getObs(),
						subtema.getEst(),
						subtema.getUsr_act(),
						new java.util.Date(),
						subtema.getId()); 
			return subtema.getId();

		} else {
			// insert
			String sql = "insert into col_subtema ("
						+ "id_tem, "
						+ "nom, "
						+ "ord, "
						+ "obs, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				subtema.getId_tem(),
				subtema.getNom(),
				subtema.getOrd(),
				subtema.getObs(),
				subtema.getEst(),
				subtema.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_subtema where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Subtema> list() {
		String sql = "select * from col_subtema";
		
		//logger.info(sql);
		
		List<Subtema> listSubtema = jdbcTemplate.query(sql, new RowMapper<Subtema>() {

			@Override
			public Subtema mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listSubtema;
	}

	public Subtema get(int id) {
		String sql = "select * from col_subtema WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Subtema>() {

			@Override
			public Subtema extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Subtema getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select sub.id sub_id, sub.id_tem sub_id_tem , sub.nom sub_nom , sub.ord sub_ord , sub.obs sub_obs  ,sub.est sub_est ";
		if (aTablas.contains("col_tema"))
			sql = sql + ", tem.id tem_id  , tem.id_niv tem_id_niv , tem.id_cur tem_id_cur , tem.nom tem_nom , tem.ord tem_ord  ";
	
		sql = sql + " from col_subtema sub "; 
		if (aTablas.contains("col_tema"))
			sql = sql + " left join col_tema tem on tem.id = sub.id_tem ";
		sql = sql + " where sub.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Subtema>() {
		
			@Override
			public Subtema extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Subtema subtema= rsToEntity(rs,"sub_");
					if (aTablas.contains("col_tema")){
						Tema tema = new Tema();  
							tema.setId(rs.getInt("tem_id")) ;  
							tema.setId_niv(rs.getInt("tem_id_niv")) ;  
							tema.setId_cur(rs.getInt("tem_id_cur")) ;  
							tema.setNom(rs.getString("tem_nom")) ;  
							tema.setOrd(rs.getInt("tem_ord")) ;  
							subtema.setTema(tema);
					}
							return subtema;
				}
				
				return null;
			}
			
		});


	}		
	
	public Subtema getByParams(Param param) {

		String sql = "select * from col_subtema " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Subtema>() {
			@Override
			public Subtema extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Subtema> listByParams(Param param, String[] order) {

		String sql = "select * from col_subtema " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Subtema>() {

			@Override
			public Subtema mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Subtema> listFullByParams(Subtema subtema, String[] order) {
	
		return listFullByParams(Param.toParam("sub",subtema), order);
	
	}	
	
	public List<Subtema> listFullByParams(Param param, String[] order) {

		String sql = "select sub.id sub_id, sub.id_tem sub_id_tem , sub.nom sub_nom , sub.ord sub_ord , sub.obs sub_obs  ,sub.est sub_est ";
		sql = sql + ", tem.id tem_id  , tem.id_niv tem_id_niv , tem.id_cur tem_id_cur , tem.nom tem_nom , tem.ord tem_ord  ";
		sql = sql + " from col_subtema sub";
		sql = sql + " left join col_tema tem on tem.id = sub.id_tem ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Subtema>() {

			@Override
			public Subtema mapRow(ResultSet rs, int rowNum) throws SQLException {
				Subtema subtema= rsToEntity(rs,"sub_");
				Tema tema = new Tema();  
				tema.setId(rs.getInt("tem_id")) ;  
				tema.setId_niv(rs.getInt("tem_id_niv")) ;  
				tema.setId_cur(rs.getInt("tem_id_cur")) ;  
				tema.setNom(rs.getString("tem_nom")) ;  
				tema.setOrd(rs.getInt("tem_ord")) ;  
				subtema.setTema(tema);
				return subtema;
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
				//curso_subtema.setDur(rs.getBigDecimal("dur"));
				curso_subtema.setEst(rs.getString("est"));
												
				return curso_subtema;
			}

		});	
	}


	// funciones privadas utilitarias para Subtema

	private Subtema rsToEntity(ResultSet rs,String alias) throws SQLException {
		Subtema subtema = new Subtema();

		subtema.setId(rs.getInt( alias + "id"));
		subtema.setId_tem(rs.getInt( alias + "id_tem"));
		subtema.setNom(rs.getString( alias + "nom"));
		subtema.setOrd(rs.getInt( alias + "ord"));
		subtema.setObs(rs.getString( alias + "obs"));
		subtema.setEst(rs.getString( alias + "est"));
								
		return subtema;

	}
	
}

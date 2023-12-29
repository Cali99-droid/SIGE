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
import com.tesla.colegio.model.Competencia;

import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.Curso;
import com.tesla.colegio.model.Capacidad;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CompetenciaDAO.
 * @author MV
 *
 */
public class CompetenciaDAOImpl{
	final static Logger logger = Logger.getLogger(CompetenciaDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Competencia competencia) {
		if (competencia.getId() != null) {
			// update
			String sql = "UPDATE col_competencia "
						+ "SET id_niv=?, "
						+ "id_cur=?, "
						+ "nom=?, "
						+ "peso=?, "
						+ "ord=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						competencia.getId_niv(),
						competencia.getId_cur(),
						competencia.getNom(),
						competencia.getPeso(),
						competencia.getOrd(),
						competencia.getEst(),
						competencia.getUsr_act(),
						new java.util.Date(),
						competencia.getId()); 
			return competencia.getId();

		} else {
			// insert
			String sql = "insert into col_competencia ("
						+ "id_niv, "
						+ "id_cur, "
						+ "nom, "
						+ "peso, "
						+ "ord, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				competencia.getId_niv(),
				competencia.getId_cur(),
				competencia.getNom(),
				competencia.getPeso(),
				competencia.getOrd(),
				competencia.getEst(),
				competencia.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_competencia where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Competencia> list() {
		String sql = "select * from col_competencia";
		
		//logger.info(sql);
		
		List<Competencia> listCompetencia = jdbcTemplate.query(sql, new RowMapper<Competencia>() {

			@Override
			public Competencia mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCompetencia;
	}

	public Competencia get(int id) {
		String sql = "select * from col_competencia WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Competencia>() {

			@Override
			public Competencia extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Competencia getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select com.id com_id, com.id_niv com_id_niv , com.id_cur com_id_cur , com.nom com_nom , com.peso com_peso , com.ord com_ord  ,com.est com_est ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		if (aTablas.contains("cat_curso"))
			sql = sql + ", cur.id cur_id  , cur.nom cur_nom  ";
	
		sql = sql + " from col_competencia com "; 
		if (aTablas.contains("cat_nivel"))
			sql = sql + " left join cat_nivel niv on niv.id = com.id_niv ";
		if (aTablas.contains("cat_curso"))
			sql = sql + " left join cat_curso cur on cur.id = com.id_cur ";
		sql = sql + " where com.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Competencia>() {
		
			@Override
			public Competencia extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Competencia competencia= rsToEntity(rs,"com_");
					if (aTablas.contains("cat_nivel")){
						Nivel nivel = new Nivel();  
							nivel.setId(rs.getInt("niv_id")) ;  
							nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
							nivel.setNom(rs.getString("niv_nom")) ;  
							competencia.setNivel(nivel);
					}
					if (aTablas.contains("cat_curso")){
						Curso curso = new Curso();  
							curso.setId(rs.getInt("cur_id")) ;  
							curso.setNom(rs.getString("cur_nom")) ;  
							competencia.setCurso(curso);
					}
							return competencia;
				}
				
				return null;
			}
			
		});


	}		
	
	public Competencia getByParams(Param param) {

		String sql = "select * from col_competencia " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Competencia>() {
			@Override
			public Competencia extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Competencia> listByParams(Param param, String[] order) {

		String sql = "select * from col_competencia " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Competencia>() {

			@Override
			public Competencia mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Competencia> listFullByParams(Competencia competencia, String[] order) {
	
		return listFullByParams(Param.toParam("com",competencia), order);
	
	}	
	
	public List<Competencia> listFullByParams(Param param, String[] order) {

		String sql = "select com.id com_id, com.id_niv com_id_niv , com.id_cur com_id_cur , com.nom com_nom , com.peso com_peso , com.ord com_ord  ,com.est com_est ";
		sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		sql = sql + ", cur.id cur_id  , cur.nom cur_nom  ";
		sql = sql + " from col_competencia com";
		sql = sql + " left join cat_nivel niv on niv.id = com.id_niv ";
		sql = sql + " left join cat_curso cur on cur.id = com.id_cur ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Competencia>() {

			@Override
			public Competencia mapRow(ResultSet rs, int rowNum) throws SQLException {
				Competencia competencia= rsToEntity(rs,"com_");
				Nivel nivel = new Nivel();  
				nivel.setId(rs.getInt("niv_id")) ;  
				nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
				nivel.setNom(rs.getString("niv_nom")) ;  
				competencia.setNivel(nivel);
				Curso curso = new Curso();  
				curso.setId(rs.getInt("cur_id")) ;  
				curso.setNom(rs.getString("cur_nom")) ;  
				competencia.setCurso(curso);
				return competencia;
			}

		});

	}	


	public List<Capacidad> getListCapacidad(Param param, String[] order) {
		String sql = "select * from col_capacidad " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Capacidad>() {

			@Override
			public Capacidad mapRow(ResultSet rs, int rowNum) throws SQLException {
				Capacidad capacidad = new Capacidad();

				capacidad.setId(rs.getInt("id"));
				capacidad.setId_com(rs.getInt("id_com"));
				capacidad.setNom(rs.getString("nom"));
				capacidad.setEst(rs.getString("est"));
												
				return capacidad;
			}

		});	
	}


	// funciones privadas utilitarias para Competencia

	private Competencia rsToEntity(ResultSet rs,String alias) throws SQLException {
		Competencia competencia = new Competencia();

		competencia.setId(rs.getInt( alias + "id"));
		competencia.setId_niv(rs.getInt( alias + "id_niv"));
		competencia.setId_cur(rs.getInt( alias + "id_cur"));
		competencia.setNom(rs.getString( alias + "nom"));
		competencia.setPeso(rs.getString( alias + "peso"));
		competencia.setOrd(rs.getInt( alias + "ord"));
		competencia.setEst(rs.getString( alias + "est"));
								
		return competencia;

	}
	
}

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
import com.tesla.colegio.model.Tema;

import com.tesla.colegio.model.Nivel;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.Curso;
import com.tesla.colegio.model.Subtema;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface TemaDAO.
 * @author MV
 *
 */
public class TemaDAOImpl{
	final static Logger logger = Logger.getLogger(TemaDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;

	@Autowired
	private TokenSeguridad tokenSeguridad;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Tema tema) {
		if (tema.getId() != null) {
			// update
			String sql = "UPDATE col_tema "
						+ "SET id_niv=?, "
						+ "id_cur=?, "
						+ "id_anio=?, "
						+ "nom=?, "
						+ "ord=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						tema.getId_niv(),
						tema.getId_cur(),
						tema.getId_anio(),
						tema.getNom(),
						tema.getOrd(),
						tema.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						tema.getId()); 
			return tema.getId();

		} else {
			// insert
			String sql = "insert into col_tema ("
						+ "id_niv, "
						+ "id_cur, "
						+ "id_anio, "
						+ "nom, "
						+ "ord, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				tema.getId_niv(),
				tema.getId_cur(),
				tema.getId_anio(),
				tema.getNom(),
				tema.getOrd(),
				tema.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_tema where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Tema> list() {
		String sql = "select * from col_tema";
		
		//logger.info(sql);
		
		List<Tema> listTema = jdbcTemplate.query(sql, new RowMapper<Tema>() {

			@Override
			public Tema mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listTema;
	}

	public Tema get(int id) {
		String sql = "select * from col_tema WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Tema>() {

			@Override
			public Tema extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Tema getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select tem.id tem_id, tem.id_niv tem_id_niv , tem.id_cur tem_id_cur , tem.nom tem_nom , tem.ord tem_ord  ,tem.est tem_est ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		if (aTablas.contains("cat_curso"))
			sql = sql + ", cur.id cur_id  , cur.nom cur_nom  ";
	
		sql = sql + " from col_tema tem "; 
		if (aTablas.contains("cat_nivel"))
			sql = sql + " left join cat_nivel niv on niv.id = tem.id_niv ";
		if (aTablas.contains("cat_curso"))
			sql = sql + " left join cat_curso cur on cur.id = tem.id_cur ";
		sql = sql + " where tem.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Tema>() {
		
			@Override
			public Tema extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Tema tema= rsToEntity(rs,"tem_");
					if (aTablas.contains("cat_nivel")){
						Nivel nivel = new Nivel();  
							nivel.setId(rs.getInt("niv_id")) ;  
							nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
							nivel.setNom(rs.getString("niv_nom")) ;  
							tema.setNivel(nivel);
					}
					if (aTablas.contains("cat_curso")){
						Curso curso = new Curso();  
							curso.setId(rs.getInt("cur_id")) ;  
							curso.setNom(rs.getString("cur_nom")) ;  
							tema.setCurso(curso);
					}
							return tema;
				}
				
				return null;
			}
			
		});


	}		
	
	public Tema getByParams(Param param) {

		String sql = "select * from col_tema " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Tema>() {
			@Override
			public Tema extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Tema> listByParams(Param param, String[] order) {

		String sql = "select * from col_tema " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Tema>() {

			@Override
			public Tema mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
/*	public List<Tema> listFullByParams(Tema tema, String[] order) {
	
		return listFullByParams(Param.toParam("tem",tema), order);
	
	}	*/
	
	public List<Tema> listFullByParams(Param param, String[] order) {

		String sql = "select tem.id tem_id, tem.id_niv tem_id_niv , tem.id_cur tem_id_cur ,tem.id_anio tem_id_anio, tem.nom tem_nom , tem.ord tem_ord  ,tem.est tem_est ";
		sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		sql = sql + ", cur.id cur_id  , cur.nom cur_nom  ";
		sql = sql + " from col_tema tem";
		sql = sql + " left join cat_nivel niv on niv.id = tem.id_niv ";
		sql = sql + " left join cat_curso cur on cur.id = tem.id_cur ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Tema>() {

			@Override
			public Tema mapRow(ResultSet rs, int rowNum) throws SQLException {
				Tema tema= rsToEntity(rs,"tem_");
				Nivel nivel = new Nivel();  
				nivel.setId(rs.getInt("niv_id")) ;  
				nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
				nivel.setNom(rs.getString("niv_nom")) ;  
				tema.setNivel(nivel);
				Curso curso = new Curso();  
				curso.setId(rs.getInt("cur_id")) ;  
				curso.setNom(rs.getString("cur_nom")) ;  
				tema.setCurso(curso);
				return tema;
			}

		});

	}	


	public List<Subtema> getListSubtema(Param param, String[] order) {
		String sql = "select * from col_subtema " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Subtema>() {

			@Override
			public Subtema mapRow(ResultSet rs, int rowNum) throws SQLException {
				Subtema subtema = new Subtema();

				subtema.setId(rs.getInt("id"));
				subtema.setId_tem(rs.getInt("id_tem"));
				subtema.setNom(rs.getString("nom"));
				subtema.setOrd(rs.getInt("ord"));
				subtema.setObs(rs.getString("obs"));
				subtema.setEst(rs.getString("est"));
												
				return subtema;
			}

		});	
	}


	// funciones privadas utilitarias para Tema

	private Tema rsToEntity(ResultSet rs,String alias) throws SQLException {
		Tema tema = new Tema();

		tema.setId(rs.getInt( alias + "id"));
		tema.setId_niv(rs.getInt( alias + "id_niv"));
		tema.setId_cur(rs.getInt( alias + "id_cur"));
		tema.setId_anio(rs.getInt(alias + "id_anio"));
		tema.setNom(rs.getString( alias + "nom"));
		tema.setOrd(rs.getInt( alias + "ord"));
		tema.setEst(rs.getString( alias + "est"));
								
		return tema;

	}
	
}

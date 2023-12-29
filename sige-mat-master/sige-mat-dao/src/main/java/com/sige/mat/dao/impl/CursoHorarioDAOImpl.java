package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.tesla.colegio.model.CursoHorario;

import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.CursoAula;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CursoHorarioDAO.
 * @author MV
 *
 */
public class CursoHorarioDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(CursoHorario curso_horario) {
		if (curso_horario.getId() != null) {
			// update
			String sql = "UPDATE col_curso_horario "
						+ "SET id_anio=?, "
						+ "id_cchp=?, "
						+ "id_cca=?, "
						+ "dia=?, "
						+ "hora_ini=?, "
						+ "hora_fin=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						curso_horario.getId_anio(),
						curso_horario.getId_cchp(),
						curso_horario.getId_cca(),
						curso_horario.getDia(),
						curso_horario.getHora_ini(),
						curso_horario.getHora_fin(),
						curso_horario.getEst(),
						curso_horario.getUsr_act(),
						new java.util.Date(),
						curso_horario.getId()); 
			return curso_horario.getId();

		} else {
			// insert
			String sql = "insert into col_curso_horario ("
						+ "id_anio, "
						+ "id_cchp, "
						+ "id_cca, "
						+ "dia, "
						+ "hora_ini, "
						+ "hora_fin, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				jdbcTemplate.update(sql, 
				curso_horario.getId_anio(),
				curso_horario.getId_cchp(),
				curso_horario.getId_cca(),
				curso_horario.getDia(),
				curso_horario.getHora_ini(),
				curso_horario.getHora_fin(),
				curso_horario.getEst(),
				curso_horario.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_curso_horario where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<CursoHorario> list() {
		String sql = "select * from col_curso_horario";
		
		
		
		List<CursoHorario> listCursoHorario = jdbcTemplate.query(sql, new RowMapper<CursoHorario>() {

			@Override
			public CursoHorario mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCursoHorario;
	}

	public CursoHorario get(int id) {
		String sql = "select * from col_curso_horario WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoHorario>() {

			@Override
			public CursoHorario extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CursoHorario getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cch.id cch_id,  cch.id_cchp cch_id_cchp, cch.id_anio cch_id_anio , cch.id_cca cch_id_cca , cch.dia cch_dia , cch.hora_ini cch_hora_ini , cch.hora_fin cch_hora_fin  ,cch.est cch_est ";
		if (aTablas.contains("col_anio"))
			sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		if (aTablas.contains("col_curso_aula"))
			sql = sql + ", cca.id cca_id  , cca.id_cua cca_id_cua , cca.id_au cca_id_au , cca.id_tra cca_id_tra , cca.nro_ses cca_nro_ses  ";
	
		sql = sql + " from col_curso_horario cch "; 
		if (aTablas.contains("col_anio"))
			sql = sql + " left join col_anio anio on anio.id = cch.id_anio ";
		if (aTablas.contains("col_curso_aula"))
			sql = sql + " left join col_curso_aula cca on cca.id = cch.id_cca ";
		sql = sql + " where cch.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoHorario>() {
		
			@Override
			public CursoHorario extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CursoHorario cursohorario= rsToEntity(rs,"cch_");
					if (aTablas.contains("col_anio")){
						Anio anio = new Anio();  
							anio.setId(rs.getInt("anio_id")) ;  
							anio.setNom(rs.getString("anio_nom")) ;  
							cursohorario.setAnio(anio);
					}
					if (aTablas.contains("col_curso_aula")){
						CursoAula cursoaula = new CursoAula();  
							cursoaula.setId(rs.getInt("cca_id")) ;  
							cursoaula.setId_cua(rs.getInt("cca_id_cua")) ;  
							cursoaula.setId_au(rs.getInt("cca_id_au")) ;  
							cursoaula.setId_tra(rs.getInt("cca_id_tra")) ;  
							//cursoaula.setNro_ses(rs.getInt("cca_nro_ses")) ;  
							cursohorario.setCursoAula(cursoaula);
					}
							return cursohorario;
				}
				
				return null;
			}
			
		});


	}		
	
	public CursoHorario getByParams(Param param) {

		String sql = "select * from col_curso_horario " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoHorario>() {
			@Override
			public CursoHorario extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CursoHorario> listByParams(Param param, String[] order) {

		String sql = "select * from col_curso_horario " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<CursoHorario>() {

			@Override
			public CursoHorario mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CursoHorario> listFullByParams(CursoHorario cursohorario, String[] order) {
	
		return listFullByParams(Param.toParam("cch",cursohorario), order);
	
	}	
	
	public List<CursoHorario> listFullByParams(Param param, String[] order) {

		String sql = "select cch.id cch_id, cch.id_cchp cch_id_cchp, cch.id_anio cch_id_anio , cch.id_cca cch_id_cca , cch.dia cch_dia , cch.hora_ini cch_hora_ini , cch.hora_fin cch_hora_fin  ,cch.est cch_est ";
		sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		sql = sql + ", cca.id cca_id  , cca.id_cua cca_id_cua , cca.id_au cca_id_au , cca.id_tra cca_id_tra , cca.nro_ses cca_nro_ses  ";
		sql = sql + " from col_curso_horario cch";
		sql = sql + " left join col_anio anio on anio.id = cch.id_anio ";
		sql = sql + " left join col_curso_aula cca on cca.id = cch.id_cca ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<CursoHorario>() {

			@Override
			public CursoHorario mapRow(ResultSet rs, int rowNum) throws SQLException {
				CursoHorario cursohorario= rsToEntity(rs,"cch_");
				Anio anio = new Anio();  
				anio.setId(rs.getInt("anio_id")) ;  
				anio.setNom(rs.getString("anio_nom")) ;  
				cursohorario.setAnio(anio);
				CursoAula cursoaula = new CursoAula();  
				cursoaula.setId(rs.getInt("cca_id")) ;  
				cursoaula.setId_cua(rs.getInt("cca_id_cua")) ;  
				cursoaula.setId_au(rs.getInt("cca_id_au")) ;  
				cursoaula.setId_tra(rs.getInt("cca_id_tra")) ;  
				//cursoaula.setNro_ses(rs.getInt("cca_nro_ses")) ;  
				cursohorario.setCursoAula(cursoaula);
				return cursohorario;
			}

		});

	}	




	// funciones privadas utilitarias para CursoHorario

	private CursoHorario rsToEntity(ResultSet rs,String alias) throws SQLException {
		CursoHorario curso_horario = new CursoHorario();

		curso_horario.setId(rs.getInt( alias + "id"));
		curso_horario.setId_anio(rs.getInt( alias + "id_anio"));
		curso_horario.setId_cca(rs.getInt( alias + "id_cca"));
		curso_horario.setId_cchp(rs.getInt( alias + "id_cchp"));
		curso_horario.setDia(rs.getInt( alias + "dia"));
		curso_horario.setHora_ini(rs.getString( alias + "hora_ini"));
		curso_horario.setHora_fin(rs.getString( alias + "hora_fin"));
		curso_horario.setEst(rs.getString( alias + "est"));
								
		return curso_horario;

	}
	
}

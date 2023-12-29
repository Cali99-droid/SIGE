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
import com.tesla.colegio.model.CursoHorarioSes;

import com.tesla.colegio.model.CursoHorario;
import com.tesla.colegio.model.UnidadSesion;
import com.tesla.colegio.model.ConfSemanas;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CursoHorarioSesDAO.
 * @author MV
 *
 */
public class CursoHorarioSesDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(CursoHorarioSes curso_horario_ses) {
		if (curso_horario_ses.getId() != null) {
			// update
			String sql = "UPDATE col_curso_horario_ses "
						+ "SET id_cch=?, "
						+ "id_uns=?, "
						+ "id_ccs=?, "
						+ "fec=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						curso_horario_ses.getId_cch(),
						curso_horario_ses.getId_uns(),
						curso_horario_ses.getId_ccs(),
						curso_horario_ses.getFec(),
						curso_horario_ses.getEst(),
						curso_horario_ses.getUsr_act(),
						new java.util.Date(),
						curso_horario_ses.getId()); 
			return curso_horario_ses.getId();

		} else {
			// insert
			String sql = "insert into col_curso_horario_ses ("
						+ "id_cch, "
						+ "id_uns, "
						+ "id_ccs, "
						+ "fec, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				curso_horario_ses.getId_cch(),
				curso_horario_ses.getId_uns(),
				curso_horario_ses.getId_ccs(),
				curso_horario_ses.getFec(),
				curso_horario_ses.getEst(),
				curso_horario_ses.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_curso_horario_ses where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<CursoHorarioSes> list() {
		String sql = "select * from col_curso_horario_ses";
		
		
		
		List<CursoHorarioSes> listCursoHorarioSes = jdbcTemplate.query(sql, new RowMapper<CursoHorarioSes>() {

			@Override
			public CursoHorarioSes mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCursoHorarioSes;
	}

	public CursoHorarioSes get(int id) {
		String sql = "select * from col_curso_horario_ses WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoHorarioSes>() {

			@Override
			public CursoHorarioSes extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CursoHorarioSes getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cchs.id cchs_id, cchs.id_cch cchs_id_cch , cchs.id_uns cchs_id_uns , cchs.id_ccs cchs_id_ccs , cchs.fec cchs_fec  ,cchs.est cchs_est ";
		if (aTablas.contains("col_curso_horario"))
			sql = sql + ", cch.id cch_id  , cch.id_anio cch_id_anio , cch.id_cca cch_id_cca , cch.dia cch_dia , cch.hora_ini cch_hora_ini , cch.hora_fin cch_hora_fin  ";
		if (aTablas.contains("col_unidad_sesion"))
			sql = sql + ", uns.id uns_id  , uns.id_uni uns_id_uni , uns.nro uns_nro , uns.nom uns_nom  ";
		if (aTablas.contains("col_conf_semanas"))
			sql = sql + ", cnf_sem.id cnf_sem_id  , cnf_sem.id_cnf_anio cnf_sem_id_cnf_anio , cnf_sem.nro_sem cnf_sem_nro_sem , cnf_sem.fec_ini cnf_sem_fec_ini , cnf_sem.fec_fin cnf_sem_fec_fin  ";
	
		sql = sql + " from col_curso_horario_ses cchs "; 
		if (aTablas.contains("col_curso_horario"))
			sql = sql + " left join col_curso_horario cch on cch.id = cchs.id_cch ";
		if (aTablas.contains("col_unidad_sesion"))
			sql = sql + " left join col_unidad_sesion uns on uns.id = cchs.id_uns ";
		if (aTablas.contains("col_conf_semanas"))
			sql = sql + " left join col_conf_semanas cnf_sem on cnf_sem.id = cchs.id_ccs ";
		sql = sql + " where cchs.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoHorarioSes>() {
		
			@Override
			public CursoHorarioSes extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CursoHorarioSes cursohorarioses= rsToEntity(rs,"cchs_");
					if (aTablas.contains("col_curso_horario")){
						CursoHorario cursohorario = new CursoHorario();  
							cursohorario.setId(rs.getInt("cch_id")) ;  
							cursohorario.setId_anio(rs.getInt("cch_id_anio")) ;  
							cursohorario.setId_cca(rs.getInt("cch_id_cca")) ;  
							cursohorario.setDia(rs.getInt("cch_dia")) ;  
							cursohorario.setHora_ini(rs.getString("cch_hora_ini")) ;  
							cursohorario.setHora_fin(rs.getString("cch_hora_fin")) ;  
							cursohorarioses.setCursoHorario(cursohorario);
					}
					if (aTablas.contains("col_unidad_sesion")){
						UnidadSesion unidadsesion = new UnidadSesion();  
							unidadsesion.setId(rs.getInt("uns_id")) ;  
							unidadsesion.setId_uni(rs.getInt("uns_id_uni")) ;  
							//unidadsesion.setNro(rs.getString("uns_nro")) ;  
							unidadsesion.setNom(rs.getString("uns_nom")) ;  
							cursohorarioses.setUnidadSesion(unidadsesion);
					}
					if (aTablas.contains("col_conf_semanas")){
						ConfSemanas confsemanas = new ConfSemanas();  
							confsemanas.setId(rs.getInt("cnf_sem_id")) ;  
							confsemanas.setId_cnf_anio(rs.getInt("cnf_sem_id_cnf_anio")) ;  
							confsemanas.setNro_sem(rs.getInt("cnf_sem_nro_sem")) ;  
							confsemanas.setFec_ini(rs.getDate("cnf_sem_fec_ini")) ;  
							confsemanas.setFec_fin(rs.getDate("cnf_sem_fec_fin")) ;  
							cursohorarioses.setConfSemanas(confsemanas);
					}
							return cursohorarioses;
				}
				
				return null;
			}
			
		});


	}		
	
	public CursoHorarioSes getByParams(Param param) {

		String sql = "select * from col_curso_horario_ses " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoHorarioSes>() {
			@Override
			public CursoHorarioSes extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CursoHorarioSes> listByParams(Param param, String[] order) {

		String sql = "select * from col_curso_horario_ses " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<CursoHorarioSes>() {

			@Override
			public CursoHorarioSes mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CursoHorarioSes> listFullByParams(CursoHorarioSes cursohorarioses, String[] order) {
	
		return listFullByParams(Param.toParam("cchs",cursohorarioses), order);
	
	}	
	
	public List<CursoHorarioSes> listFullByParams(Param param, String[] order) {

		String sql = "select cchs.id cchs_id, cchs.id_cch cchs_id_cch , cchs.id_uns cchs_id_uns , cchs.id_ccs cchs_id_ccs , cchs.fec cchs_fec  ,cchs.est cchs_est ";
		sql = sql + ", cch.id cch_id  , cch.id_anio cch_id_anio , cch.id_cca cch_id_cca , cch.dia cch_dia , cch.hora_ini cch_hora_ini , cch.hora_fin cch_hora_fin  ";
		sql = sql + ", uns.id uns_id  , uns.id_uni uns_id_uni , uns.nro uns_nro , uns.nom uns_nom  ";
		sql = sql + ", cnf_sem.id cnf_sem_id  , cnf_sem.id_cnf_anio cnf_sem_id_cnf_anio , cnf_sem.nro_sem cnf_sem_nro_sem , cnf_sem.fec_ini cnf_sem_fec_ini , cnf_sem.fec_fin cnf_sem_fec_fin  ";
		sql = sql + " from col_curso_horario_ses cchs";
		sql = sql + " left join col_curso_horario cch on cch.id = cchs.id_cch ";
		sql = sql + " left join col_unidad_sesion uns on uns.id = cchs.id_uns ";
		sql = sql + " left join col_conf_semanas cnf_sem on cnf_sem.id = cchs.id_ccs ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<CursoHorarioSes>() {

			@Override
			public CursoHorarioSes mapRow(ResultSet rs, int rowNum) throws SQLException {
				CursoHorarioSes cursohorarioses= rsToEntity(rs,"cchs_");
				CursoHorario cursohorario = new CursoHorario();  
				cursohorario.setId(rs.getInt("cch_id")) ;  
				cursohorario.setId_anio(rs.getInt("cch_id_anio")) ;  
				cursohorario.setId_cca(rs.getInt("cch_id_cca")) ;  
				cursohorario.setDia(rs.getInt("cch_dia")) ;  
				cursohorario.setHora_ini(rs.getString("cch_hora_ini")) ;  
				cursohorario.setHora_fin(rs.getString("cch_hora_fin")) ;  
				cursohorarioses.setCursoHorario(cursohorario);
				UnidadSesion unidadsesion = new UnidadSesion();  
				unidadsesion.setId(rs.getInt("uns_id")) ;  
				unidadsesion.setId_uni(rs.getInt("uns_id_uni")) ;  
				//unidadsesion.setNro(rs.getString("uns_nro")) ;  
				unidadsesion.setNom(rs.getString("uns_nom")) ;  
				cursohorarioses.setUnidadSesion(unidadsesion);
				ConfSemanas confsemanas = new ConfSemanas();  
				confsemanas.setId(rs.getInt("cnf_sem_id")) ;  
				confsemanas.setId_cnf_anio(rs.getInt("cnf_sem_id_cnf_anio")) ;  
				confsemanas.setNro_sem(rs.getInt("cnf_sem_nro_sem")) ;  
				confsemanas.setFec_ini(rs.getDate("cnf_sem_fec_ini")) ;  
				confsemanas.setFec_fin(rs.getDate("cnf_sem_fec_fin")) ;  
				cursohorarioses.setConfSemanas(confsemanas);
				return cursohorarioses;
			}

		});

	}	




	// funciones privadas utilitarias para CursoHorarioSes

	private CursoHorarioSes rsToEntity(ResultSet rs,String alias) throws SQLException {
		CursoHorarioSes curso_horario_ses = new CursoHorarioSes();

		curso_horario_ses.setId(rs.getInt( alias + "id"));
		curso_horario_ses.setId_cch(rs.getInt( alias + "id_cch"));
		curso_horario_ses.setId_uns(rs.getInt( alias + "id_uns"));
		curso_horario_ses.setId_ccs(rs.getInt( alias + "id_ccs"));
		curso_horario_ses.setFec(rs.getDate( alias + "fec"));
		curso_horario_ses.setEst(rs.getString( alias + "est"));
								
		return curso_horario_ses;

	}
	
}

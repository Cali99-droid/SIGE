package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.tesla.colegio.model.Anio;

import com.tesla.colegio.model.Cronograma;
import com.tesla.colegio.model.ConfFechas;
import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.GradoHorario;
import com.tesla.colegio.model.GradoHorario;
import com.tesla.colegio.model.AreaAnio;
import com.tesla.colegio.model.CursoHorario;
import com.tesla.colegio.model.Indicador;
import com.tesla.colegio.model.CursoSubtema;
import com.tesla.colegio.model.PerUni;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface AnioDAO.
 * @author MV
 *
 */
public class AnioDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Anio anio) {
		if (anio.getId() != null) {
			// update
			String sql = "UPDATE col_anio "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			

			jdbcTemplate.update(sql, 
						anio.getNom(),
						anio.getEst(),
						anio.getUsr_act(),
						new java.util.Date(),
						anio.getId()); 
			return anio.getId();

		} else {
			// insert
			String sql = "insert into col_anio ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				anio.getNom(),
				anio.getEst(),
				anio.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_anio where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<Anio> list() {
		String sql = "select * from col_anio";
		
		
		
		List<Anio> listAnio = jdbcTemplate.query(sql, new RowMapper<Anio>() {

			@Override
			public Anio mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listAnio;
	}

	public Anio get(int id) {
		String sql = "select * from col_anio WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Anio>() {

			@Override
			public Anio extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Anio getFull(int id, String tablas[]) {
		String sql = "select anio.id anio_id, anio.nom anio_nom  ,anio.est anio_est ";
	
		sql = sql + " from col_anio anio "; 
		sql = sql + " where anio.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<Anio>() {
		
			@Override
			public Anio extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Anio anio= rsToEntity(rs,"anio_");
							return anio;
				}
				
				return null;
			}
			
		});


	}		
	
	public Anio getByParams(Param param) {

		String sql = "select * from col_anio " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Anio>() {
			@Override
			public Anio extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Anio> listByParams(Param param, String[] order) {

		String sql = "select * from col_anio " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<Anio>() {

			@Override
			public Anio mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Anio> listFullByParams(Anio anio, String[] order) {
	
		return listFullByParams(Param.toParam("anio",anio), order);
	
	}	
	
	public List<Anio> listFullByParams(Param param, String[] order) {

		String sql = "select anio.id anio_id, anio.nom anio_nom  ,anio.est anio_est, anio.fec_ini  anio_fec_ini, anio.fec_fin anio_fec_fin";
		sql = sql + " from col_anio anio";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<Anio>() {

			@Override
			public Anio mapRow(ResultSet rs, int rowNum) throws SQLException {
				Anio anio= rsToEntity(rs,"anio_");
				return anio;
			}

		});

	}	


	public List<Cronograma> getListCronograma(Param param, String[] order) {
		String sql = "select * from mat_cronograma " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		
		
		return jdbcTemplate.query(sql, new RowMapper<Cronograma>() {

			@Override
			public Cronograma mapRow(ResultSet rs, int rowNum) throws SQLException {
				Cronograma cronograma = new Cronograma();

				cronograma.setId(rs.getInt("id"));
				cronograma.setId_anio(rs.getInt("id_anio"));
				//cronograma.setId_niv(rs.getInt("id_niv"));
				cronograma.setFec_mat(rs.getDate("fec_mat"));
				cronograma.setDel(rs.getString("del"));
				cronograma.setAl(rs.getString("al"));
				cronograma.setEst(rs.getString("est"));
												
				return cronograma;
			}

		});	
	}
	public List<ConfFechas> getListConfFechas(Param param, String[] order) {
		String sql = "select * from mat_conf_fechas " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		
		
		return jdbcTemplate.query(sql, new RowMapper<ConfFechas>() {

			@Override
			public ConfFechas mapRow(ResultSet rs, int rowNum) throws SQLException {
				ConfFechas conf_fechas = new ConfFechas();

				conf_fechas.setId(rs.getInt("id"));
				conf_fechas.setId_anio(rs.getInt("id_anio"));
				conf_fechas.setTipo(rs.getString("tipo"));
				conf_fechas.setDel(rs.getDate("del"));
				conf_fechas.setAl(rs.getDate("al"));
				conf_fechas.setEst(rs.getString("est"));
												
				return conf_fechas;
			}

		});	
	}
	public List<Periodo> getListPeriodo(Param param, String[] order) {
		String sql = "select * from per_periodo " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		
		
		return jdbcTemplate.query(sql, new RowMapper<Periodo>() {

			@Override
			public Periodo mapRow(ResultSet rs, int rowNum) throws SQLException {
				Periodo periodo = new Periodo();

				periodo.setId(rs.getInt("id"));
				periodo.setId_anio(rs.getInt("id_anio"));
				periodo.setId_suc(rs.getInt("id_suc"));
				periodo.setId_niv(rs.getInt("id_niv"));
				periodo.setId_srv(rs.getInt("id_srv"));
				periodo.setId_tpe(rs.getInt("id_tpe"));
				periodo.setFec_ini(rs.getDate("fec_ini"));
				periodo.setFec_fin(rs.getDate("fec_fin"));
				periodo.setFec_cie_mat(rs.getDate("fec_cie_mat"));
				periodo.setEst(rs.getString("est"));
												
				return periodo;
			}

		});	
	}
	public List<GradoHorario> getListGradoHorario(Param param, String[] order) {
		String sql = "select * from asi_grado_horario " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		
		
		return jdbcTemplate.query(sql, new RowMapper<GradoHorario>() {

			@Override
			public GradoHorario mapRow(ResultSet rs, int rowNum) throws SQLException {
				GradoHorario grado_horario = new GradoHorario();

				grado_horario.setId(rs.getInt("id"));
				grado_horario.setId_anio(rs.getInt("id_anio"));
				grado_horario.setId_au(rs.getInt("id_au"));
				grado_horario.setHora_ini(rs.getString("hora_ini"));
				grado_horario.setHora_fin(rs.getString("hora_fin"));
				grado_horario.setHora_ini_aux(rs.getString("hora_ini_aux"));
				grado_horario.setHora_fin_aux(rs.getString("hora_fin_aux"));
				grado_horario.setEst(rs.getString("est"));
												
				return grado_horario;
			}

		});	
	}
	
	public List<AreaAnio> getListAreaAnio(Param param, String[] order) {
		String sql = "select * from col_area_anio " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		
		
		return jdbcTemplate.query(sql, new RowMapper<AreaAnio>() {

			@Override
			public AreaAnio mapRow(ResultSet rs, int rowNum) throws SQLException {
				AreaAnio area_anio = new AreaAnio();

				area_anio.setId(rs.getInt("id"));
				area_anio.setId_anio(rs.getInt("id_anio"));
				area_anio.setId_niv(rs.getInt("id_niv"));
				area_anio.setId_area(rs.getInt("id_area"));
				area_anio.setOrd(rs.getInt("ord"));
				area_anio.setEst(rs.getString("est"));
												
				return area_anio;
			}

		});	
	}
	public List<CursoHorario> getListCursoHorario(Param param, String[] order) {
		String sql = "select * from col_curso_horario " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		
		
		return jdbcTemplate.query(sql, new RowMapper<CursoHorario>() {

			@Override
			public CursoHorario mapRow(ResultSet rs, int rowNum) throws SQLException {
				CursoHorario curso_horario = new CursoHorario();

				curso_horario.setId(rs.getInt("id"));
				curso_horario.setId_anio(rs.getInt("id_anio"));
				curso_horario.setId_cca(rs.getInt("id_cca"));
				curso_horario.setDia(rs.getInt("dia"));
				curso_horario.setEst(rs.getString("est"));
												
				return curso_horario;
			}

		});	
	}
	public List<Indicador> getListIndicador(Param param, String[] order) {
		String sql = "select * from col_indicador " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		
		
		return jdbcTemplate.query(sql, new RowMapper<Indicador>() {

			@Override
			public Indicador mapRow(ResultSet rs, int rowNum) throws SQLException {
				Indicador indicador = new Indicador();

				indicador.setId(rs.getInt("id"));
				indicador.setNom(rs.getString("nom"));
				indicador.setEst(rs.getString("est"));
												
				return indicador;
			}

		});	
	}
	public List<CursoSubtema> getListCursoSubtema(Param param, String[] order) {
		String sql = "select * from col_curso_subtema " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		
		
		return jdbcTemplate.query(sql, new RowMapper<CursoSubtema>() {

			@Override
			public CursoSubtema mapRow(ResultSet rs, int rowNum) throws SQLException {
				CursoSubtema curso_subtema = new CursoSubtema();

				curso_subtema.setId(rs.getInt("id"));
				curso_subtema.setId_anio(rs.getInt("id_anio"));
				curso_subtema.setId_niv(rs.getInt("id_niv"));
				curso_subtema.setId_gra(rs.getInt("id_gra"));
				curso_subtema.setId_cur(rs.getInt("id_cur"));
				curso_subtema.setId_sub(rs.getInt("id_sub"));
				curso_subtema.setDur(rs.getBigDecimal("dur"));
				curso_subtema.setEst(rs.getString("est"));
												
				return curso_subtema;
			}

		});	
	}
	public List<PerUni> getListPerUni(Param param, String[] order) {
		String sql = "select * from col_per_uni " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		
		
		return jdbcTemplate.query(sql, new RowMapper<PerUni>() {

			@Override
			public PerUni mapRow(ResultSet rs, int rowNum) throws SQLException {
				PerUni per_uni = new PerUni();

				per_uni.setId(rs.getInt("id"));
				per_uni.setId_cpa(rs.getInt("id_cpa"));
				//per_uni.setId_anio(rs.getString("id_anio"));
				per_uni.setNump(rs.getInt("nump"));
				per_uni.setNumu_ini(rs.getInt("numu_ini"));
				per_uni.setNumu_fin(rs.getInt("numu_fin"));
				per_uni.setFec_ini(rs.getDate("fec_ini"));
				per_uni.setFec_fin(rs.getDate("fec_fin"));
				per_uni.setEst(rs.getString("est"));
												
				return per_uni;
			}

		});	
	}


	// funciones privadas utilitarias para Anio

	private Anio rsToEntity(ResultSet rs,String alias) throws SQLException {
		Anio anio = new Anio();

		anio.setId(rs.getInt( alias + "id"));
		anio.setNom(rs.getString( alias + "nom"));
		anio.setEst(rs.getString( alias + "est"));
		anio.setFec_ini(rs.getDate(alias + "fec_ini"));	
		anio.setFec_fin(rs.getDate(alias + "fec_fin"));	
		return anio;

	}
	
}

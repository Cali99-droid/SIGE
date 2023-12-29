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
import com.tesla.colegio.model.CursoHorarioPad;

import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.CursoHorario;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CursoHorarioPadDAO.
 * @author MV
 *
 */
public class CursoHorarioPadDAOImpl{
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
	public int saveOrUpdate(CursoHorarioPad curso_horario_pad) {
		if (curso_horario_pad.getId() != null) {
			// update
			String sql = "UPDATE col_curso_horario_pad "
						+ "SET id_anio=?, "
						+ "id_au=?, "
						+ "fec_ini_vig=?, "
						+ "fec_fin_vig=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						curso_horario_pad.getId_anio(),
						curso_horario_pad.getId_au(),
						curso_horario_pad.getFec_ini_vig(),
						curso_horario_pad.getFec_fin_vig(),
						curso_horario_pad.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						curso_horario_pad.getId()); 
			return curso_horario_pad.getId();

		} else {
			// insert
			String sql = "insert into col_curso_horario_pad ("
						+ "id_anio, "
						+ "id_au, "
						+ "fec_ini_vig, "
						+ "fec_fin_vig, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				curso_horario_pad.getId_anio(),
				curso_horario_pad.getId_au(),
				curso_horario_pad.getFec_ini_vig(),
				curso_horario_pad.getFec_fin_vig(),
				curso_horario_pad.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_curso_horario_pad where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<CursoHorarioPad> list() {
		String sql = "select * from col_curso_horario_pad";
		
		//System.out.println(sql);
		
		List<CursoHorarioPad> listCursoHorarioPad = jdbcTemplate.query(sql, new RowMapper<CursoHorarioPad>() {

			@Override
			public CursoHorarioPad mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCursoHorarioPad;
	}

	public CursoHorarioPad get(int id) {
		String sql = "select * from col_curso_horario_pad WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoHorarioPad>() {

			@Override
			public CursoHorarioPad extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CursoHorarioPad getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cchp.id cchp_id, cchp.id_anio cchp_id_anio , cchp.id_au cchp_id_au , cchp.fec_ini_vig cchp_fec_ini_vig , cchp.fec_fin_vig cchp_fec_fin_vig  ,cchp.est cchp_est ";
		if (aTablas.contains("col_anio"))
			sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		if (aTablas.contains("col_aula"))
			sql = sql + ", au.id au_id  , au.id_per au_id_per , au.id_grad au_id_grad , au.id_secc_ant au_id_secc_ant , au.id_tur au_id_tur , au.secc au_secc , au.cap au_cap  ";
	
		sql = sql + " from col_curso_horario_pad cchp "; 
		if (aTablas.contains("col_anio"))
			sql = sql + " left join col_anio anio on anio.id = cchp.id_anio ";
		if (aTablas.contains("col_aula"))
			sql = sql + " left join col_aula au on au.id = cchp.id_au ";
		sql = sql + " where cchp.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoHorarioPad>() {
		
			@Override
			public CursoHorarioPad extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CursoHorarioPad cursohorariopad= rsToEntity(rs,"cchp_");
					if (aTablas.contains("col_anio")){
						Anio anio = new Anio();  
							anio.setId(rs.getInt("anio_id")) ;  
							anio.setNom(rs.getString("anio_nom")) ;  
							cursohorariopad.setAnio(anio);
					}
					if (aTablas.contains("col_aula")){
						Aula aula = new Aula();  
							aula.setId(rs.getInt("au_id")) ;  
							aula.setId_per(rs.getInt("au_id_per")) ;  
							aula.setId_grad(rs.getInt("au_id_grad")) ;  
							aula.setId_secc_ant(rs.getInt("au_id_secc_ant")) ;  
							aula.setId_tur(rs.getInt("au_id_tur")) ;  
							aula.setSecc(rs.getString("au_secc")) ;  
							aula.setCap(rs.getInt("au_cap")) ;  
							cursohorariopad.setAula(aula);
					}
							return cursohorariopad;
				}
				
				return null;
			}
			
		});


	}		
	
	public CursoHorarioPad getByParams(Param param) {

		String sql = "select * from col_curso_horario_pad " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoHorarioPad>() {
			@Override
			public CursoHorarioPad extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CursoHorarioPad> listByParams(Param param, String[] order) {

		String sql = "select * from col_curso_horario_pad " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<CursoHorarioPad>() {

			@Override
			public CursoHorarioPad mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CursoHorarioPad> listFullByParams(CursoHorarioPad cursohorariopad, String[] order) {
	
		return listFullByParams(Param.toParam("cchp",cursohorariopad), order);
	
	}	
	
	public List<CursoHorarioPad> listFullByParams(Param param, String[] order) {

		String sql = "select cchp.id cchp_id, cchp.id_anio cchp_id_anio , cchp.id_au cchp_id_au , cchp.fec_ini_vig cchp_fec_ini_vig , cchp.fec_fin_vig cchp_fec_fin_vig  ,cchp.est cchp_est ";
		sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		sql = sql + ", au.id au_id  , au.id_per au_id_per , au.id_grad au_id_grad , au.id_secc_ant au_id_secc_ant , au.id_tur au_id_tur , au.secc au_secc , au.cap au_cap  ";
		sql = sql + " from col_curso_horario_pad cchp";
		sql = sql + " left join col_anio anio on anio.id = cchp.id_anio ";
		sql = sql + " left join col_aula au on au.id = cchp.id_au ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<CursoHorarioPad>() {

			@Override
			public CursoHorarioPad mapRow(ResultSet rs, int rowNum) throws SQLException {
				CursoHorarioPad cursohorariopad= rsToEntity(rs,"cchp_");
				Anio anio = new Anio();  
				anio.setId(rs.getInt("anio_id")) ;  
				anio.setNom(rs.getString("anio_nom")) ;  
				cursohorariopad.setAnio(anio);
				Aula aula = new Aula();  
				aula.setId(rs.getInt("au_id")) ;  
				aula.setId_per(rs.getInt("au_id_per")) ;  
				aula.setId_grad(rs.getInt("au_id_grad")) ;  
				aula.setId_secc_ant(rs.getInt("au_id_secc_ant")) ;  
				aula.setId_tur(rs.getInt("au_id_tur")) ;  
				aula.setSecc(rs.getString("au_secc")) ;  
				aula.setCap(rs.getInt("au_cap")) ;  
				cursohorariopad.setAula(aula);
				return cursohorariopad;
			}

		});

	}	


	public List<CursoHorario> getListCursoHorario(Param param, String[] order) {
		String sql = "select * from col_curso_horario " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<CursoHorario>() {

			@Override
			public CursoHorario mapRow(ResultSet rs, int rowNum) throws SQLException {
				CursoHorario curso_horario = new CursoHorario();

				curso_horario.setId(rs.getInt("id"));
				curso_horario.setId_anio(rs.getInt("id_anio"));
				curso_horario.setId_cca(rs.getInt("id_cca"));
				//curso_horario.setId_cchp(rs.getInt("id_cchp"));
				curso_horario.setDia(rs.getInt("dia"));
				curso_horario.setHora_ini(rs.getString("hora_ini"));
				curso_horario.setHora_fin(rs.getString("hora_fin"));
				curso_horario.setEst(rs.getString("est"));
												
				return curso_horario;
			}

		});	
	}


	// funciones privadas utilitarias para CursoHorarioPad

	private CursoHorarioPad rsToEntity(ResultSet rs,String alias) throws SQLException {
		CursoHorarioPad curso_horario_pad = new CursoHorarioPad();

		curso_horario_pad.setId(rs.getInt( alias + "id"));
		curso_horario_pad.setId_anio(rs.getInt( alias + "id_anio"));
		curso_horario_pad.setId_au(rs.getInt( alias + "id_au"));
		curso_horario_pad.setFec_ini_vig(rs.getDate( alias + "fec_ini_vig"));
		curso_horario_pad.setFec_fin_vig(rs.getDate( alias + "fec_fin_vig"));
		curso_horario_pad.setEst(rs.getString( alias + "est"));
								
		return curso_horario_pad;

	}
	
}

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
import com.tesla.colegio.model.CursoSesion;

import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.AreaAnio;
import com.tesla.colegio.model.Curso;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CursoSesionDAO.
 * @author MV
 *
 */
public class CursoSesionDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(CursoSesion curso_sesion) {
		if (curso_sesion.getId() != null) {
			// update
			String sql = "UPDATE col_curso_sesion "
						+ "SET id_niv=?, "
						+ "id_gra=?, "
						+ "id_caa=?, "
						+ "id_cur=?, "
						+ "nro_ses=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						curso_sesion.getId_niv(),
						curso_sesion.getId_gra(),
						curso_sesion.getId_caa(),
						curso_sesion.getId_cur(),
						curso_sesion.getNro_ses(),
						curso_sesion.getEst(),
						curso_sesion.getUsr_act(),
						new java.util.Date(),
						curso_sesion.getId()); 
			return curso_sesion.getId();

		} else {
			// insert
			String sql = "insert into col_curso_sesion ("
						+ "id_niv, "
						+ "id_gra, "
						+ "id_caa, "
						+ "id_cur, "
						+ "nro_ses, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				curso_sesion.getId_niv(),
				curso_sesion.getId_gra(),
				curso_sesion.getId_caa(),
				curso_sesion.getId_cur(),
				curso_sesion.getNro_ses(),
				curso_sesion.getEst(),
				curso_sesion.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_curso_sesion where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<CursoSesion> list() {
		String sql = "select * from col_curso_sesion";
		
		
		
		List<CursoSesion> listCursoSesion = jdbcTemplate.query(sql, new RowMapper<CursoSesion>() {

			@Override
			public CursoSesion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCursoSesion;
	}

	public CursoSesion get(int id) {
		String sql = "select * from col_curso_sesion WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoSesion>() {

			@Override
			public CursoSesion extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CursoSesion getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select ccs.id ccs_id, ccs.id_niv ccs_id_niv , ccs.id_gra ccs_id_gra , ccs.id_caa ccs_id_caa , ccs.id_cur ccs_id_cur , ccs.nro_ses ccs_nro_ses  ,ccs.est ccs_est ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		if (aTablas.contains("cat_grad"))
			sql = sql + ", gra.id gra_id  , gra.id_nvl gra_id_nvl , gra.id_gra_ant gra_id_gra_ant , gra.nom gra_nom , gra.tipo gra_tipo  ";
		if (aTablas.contains("col_area_anio"))
			sql = sql + ", caa.id caa_id  , caa.id_anio caa_id_anio , caa.id_niv caa_id_niv , caa.id_area caa_id_area , caa.ord caa_ord  ";
		if (aTablas.contains("cat_curso"))
			sql = sql + ", cur.id cur_id  , cur.nom cur_nom  ";
	
		sql = sql + " from col_curso_sesion ccs "; 
		if (aTablas.contains("cat_nivel"))
			sql = sql + " left join cat_nivel niv on niv.id = ccs.id_niv ";
		if (aTablas.contains("cat_grad"))
			sql = sql + " left join cat_grad gra on gra.id = ccs.id_gra ";
		if (aTablas.contains("col_area_anio"))
			sql = sql + " left join col_area_anio caa on caa.id = ccs.id_caa ";
		if (aTablas.contains("cat_curso"))
			sql = sql + " left join cat_curso cur on cur.id = ccs.id_cur ";
		sql = sql + " where ccs.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoSesion>() {
		
			@Override
			public CursoSesion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CursoSesion cursosesion= rsToEntity(rs,"ccs_");
					if (aTablas.contains("cat_nivel")){
						Nivel nivel = new Nivel();  
							nivel.setId(rs.getInt("niv_id")) ;  
							nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
							nivel.setNom(rs.getString("niv_nom")) ;  
							cursosesion.setNivel(nivel);
					}
					if (aTablas.contains("cat_grad")){
						Grad grad = new Grad();  
							grad.setId(rs.getInt("gra_id")) ;  
							grad.setId_nvl(rs.getInt("gra_id_nvl")) ;  
							grad.setId_gra_ant(rs.getInt("gra_id_gra_ant")) ;  
							grad.setNom(rs.getString("gra_nom")) ;  
							grad.setTipo(rs.getString("gra_tipo")) ;  
							cursosesion.setGrad(grad);
					}
					if (aTablas.contains("col_area_anio")){
						AreaAnio areaanio = new AreaAnio();  
							areaanio.setId(rs.getInt("caa_id")) ;  
							areaanio.setId_anio(rs.getInt("caa_id_anio")) ;  
							areaanio.setId_niv(rs.getInt("caa_id_niv")) ;  
							areaanio.setId_area(rs.getInt("caa_id_area")) ;  
							areaanio.setOrd(rs.getInt("caa_ord")) ;  
							cursosesion.setAreaAnio(areaanio);
					}
					if (aTablas.contains("cat_curso")){
						Curso curso = new Curso();  
							curso.setId(rs.getInt("cur_id")) ;  
							curso.setNom(rs.getString("cur_nom")) ;  
							cursosesion.setCurso(curso);
					}
							return cursosesion;
				}
				
				return null;
			}
			
		});


	}		
	
	public CursoSesion getByParams(Param param) {

		String sql = "select * from col_curso_sesion " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoSesion>() {
			@Override
			public CursoSesion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CursoSesion> listByParams(Param param, String[] order) {

		String sql = "select * from col_curso_sesion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<CursoSesion>() {

			@Override
			public CursoSesion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CursoSesion> listFullByParams(CursoSesion cursosesion, String[] order) {
	
		return listFullByParams(Param.toParam("ccs",cursosesion), order);
	
	}	
	
	public List<CursoSesion> listFullByParams(Param param, String[] order) {

		String sql = "select ccs.id ccs_id, ccs.id_niv ccs_id_niv , ccs.id_gra ccs_id_gra , ccs.id_caa ccs_id_caa , ccs.id_cur ccs_id_cur , ccs.nro_ses ccs_nro_ses  ,ccs.est ccs_est ";
		sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		sql = sql + ", gra.id gra_id  , gra.id_nvl gra_id_nvl , gra.id_gra_ant gra_id_gra_ant , gra.nom gra_nom , gra.tipo gra_tipo  ";
		sql = sql + ", caa.id caa_id  , caa.id_anio caa_id_anio , caa.id_niv caa_id_niv , caa.id_area caa_id_area , caa.ord caa_ord  ";
		sql = sql + ", cur.id cur_id  , cur.nom cur_nom  ";
		sql = sql + " from col_curso_sesion ccs";
		sql = sql + " left join cat_nivel niv on niv.id = ccs.id_niv ";
		sql = sql + " left join cat_grad gra on gra.id = ccs.id_gra ";
		sql = sql + " left join col_area_anio caa on caa.id = ccs.id_caa ";
		sql = sql + " left join cat_curso cur on cur.id = ccs.id_cur ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<CursoSesion>() {

			@Override
			public CursoSesion mapRow(ResultSet rs, int rowNum) throws SQLException {
				CursoSesion cursosesion= rsToEntity(rs,"ccs_");
				Nivel nivel = new Nivel();  
				nivel.setId(rs.getInt("niv_id")) ;  
				nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
				nivel.setNom(rs.getString("niv_nom")) ;  
				cursosesion.setNivel(nivel);
				Grad grad = new Grad();  
				grad.setId(rs.getInt("gra_id")) ;  
				grad.setId_nvl(rs.getInt("gra_id_nvl")) ;  
				grad.setId_gra_ant(rs.getInt("gra_id_gra_ant")) ;  
				grad.setNom(rs.getString("gra_nom")) ;  
				grad.setTipo(rs.getString("gra_tipo")) ;  
				cursosesion.setGrad(grad);
				AreaAnio areaanio = new AreaAnio();  
				areaanio.setId(rs.getInt("caa_id")) ;  
				areaanio.setId_anio(rs.getInt("caa_id_anio")) ;  
				areaanio.setId_niv(rs.getInt("caa_id_niv")) ;  
				areaanio.setId_area(rs.getInt("caa_id_area")) ;  
				areaanio.setOrd(rs.getInt("caa_ord")) ;  
				cursosesion.setAreaAnio(areaanio);
				Curso curso = new Curso();  
				curso.setId(rs.getInt("cur_id")) ;  
				curso.setNom(rs.getString("cur_nom")) ;  
				cursosesion.setCurso(curso);
				return cursosesion;
			}

		});

	}	




	// funciones privadas utilitarias para CursoSesion

	private CursoSesion rsToEntity(ResultSet rs,String alias) throws SQLException {
		CursoSesion curso_sesion = new CursoSesion();

		curso_sesion.setId(rs.getInt( alias + "id"));
		curso_sesion.setId_niv(rs.getInt( alias + "id_niv"));
		curso_sesion.setId_gra(rs.getInt( alias + "id_gra"));
		curso_sesion.setId_caa(rs.getInt( alias + "id_caa"));
		curso_sesion.setId_cur(rs.getInt( alias + "id_cur"));
		curso_sesion.setNro_ses(rs.getInt( alias + "nro_ses"));
		curso_sesion.setEst(rs.getString( alias + "est"));
								
		return curso_sesion;

	}
	
}

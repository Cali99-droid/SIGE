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
import com.tesla.colegio.model.CursoAulaVirtual;

import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.PeriodoCurso;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CursoAulaVirtualDAO.
 * @author MV
 *
 */
public class CursoAulaVirtualDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(CursoAulaVirtual curso_aula_virtual) {
		if (curso_aula_virtual.getId() != null) {
			// update
			String sql = "UPDATE cvi_curso_aula_virtual "
						+ "SET id_anio=?, "
						+ "id_gra=?, "
						+ "nom=?, "
						+ "abrev=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						curso_aula_virtual.getId_anio(),
						curso_aula_virtual.getId_gra(),
						curso_aula_virtual.getNom(),
						curso_aula_virtual.getAbrev(),
						curso_aula_virtual.getEst(),
						curso_aula_virtual.getUsr_act(),
						new java.util.Date(),
						curso_aula_virtual.getId()); 
			return curso_aula_virtual.getId();

		} else {
			// insert
			String sql = "insert into cvi_curso_aula_virtual ("
						+ "id_anio, "
						+ "id_gra, "
						+ "nom, "
						+ "abrev, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				curso_aula_virtual.getId_anio(),
				curso_aula_virtual.getId_gra(),
				curso_aula_virtual.getNom(),
				curso_aula_virtual.getAbrev(),
				curso_aula_virtual.getEst(),
				curso_aula_virtual.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cvi_curso_aula_virtual where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<CursoAulaVirtual> list() {
		String sql = "select * from cvi_curso_aula_virtual";
		
		//System.out.println(sql);
		
		List<CursoAulaVirtual> listCursoAulaVirtual = jdbcTemplate.query(sql, new RowMapper<CursoAulaVirtual>() {

			@Override
			public CursoAulaVirtual mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCursoAulaVirtual;
	}

	public CursoAulaVirtual get(int id) {
		String sql = "select * from cvi_curso_aula_virtual WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoAulaVirtual>() {

			@Override
			public CursoAulaVirtual extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CursoAulaVirtual getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cau.id cau_id, cau.id_anio cau_id_anio , cau.id_gra cau_id_gra , cau.nom cau_nom , cau.abrev cau_abrev  ,cau.est cau_est ";
		if (aTablas.contains("col_anio"))
			sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		if (aTablas.contains("cat_grad"))
			sql = sql + ", gra.id gra_id  , gra.id_nvl gra_id_nvl , gra.id_gra_ant gra_id_gra_ant , gra.nom gra_nom , gra.tipo gra_tipo  ";
	
		sql = sql + " from cvi_curso_aula_virtual cau "; 
		if (aTablas.contains("col_anio"))
			sql = sql + " left join col_anio anio on anio.id = cau.id_anio ";
		if (aTablas.contains("cat_grad"))
			sql = sql + " left join cat_grad gra on gra.id = cau.id_gra ";
		sql = sql + " where cau.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoAulaVirtual>() {
		
			@Override
			public CursoAulaVirtual extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CursoAulaVirtual cursoaulavirtual= rsToEntity(rs,"cau_");
					if (aTablas.contains("col_anio")){
						Anio anio = new Anio();  
							anio.setId(rs.getInt("anio_id")) ;  
							anio.setNom(rs.getString("anio_nom")) ;  
							cursoaulavirtual.setAnio(anio);
					}
					if (aTablas.contains("cat_grad")){
						Grad grad = new Grad();  
							grad.setId(rs.getInt("gra_id")) ;  
							grad.setId_nvl(rs.getInt("gra_id_nvl")) ;  
							grad.setId_gra_ant(rs.getInt("gra_id_gra_ant")) ;  
							grad.setNom(rs.getString("gra_nom")) ;  
							grad.setTipo(rs.getString("gra_tipo")) ;  
							cursoaulavirtual.setGrad(grad);
					}
							return cursoaulavirtual;
				}
				
				return null;
			}
			
		});


	}		
	
	public CursoAulaVirtual getByParams(Param param) {

		String sql = "select * from cvi_curso_aula_virtual " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoAulaVirtual>() {
			@Override
			public CursoAulaVirtual extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CursoAulaVirtual> listByParams(Param param, String[] order) {

		String sql = "select * from cvi_curso_aula_virtual " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<CursoAulaVirtual>() {

			@Override
			public CursoAulaVirtual mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CursoAulaVirtual> listFullByParams(CursoAulaVirtual cursoaulavirtual, String[] order) {
	
		return listFullByParams(Param.toParam("cau",cursoaulavirtual), order);
	
	}	
	
	public List<CursoAulaVirtual> listFullByParams(Param param, String[] order) {

		String sql = "select cau.id cau_id, cau.id_anio cau_id_anio , cau.id_gra cau_id_gra , cau.nom cau_nom , cau.abrev cau_abrev  ,cau.est cau_est ";
		sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		sql = sql + ", gra.id gra_id  , gra.id_nvl gra_id_nvl , gra.id_gra_ant gra_id_gra_ant , gra.nom gra_nom , gra.tipo gra_tipo  ";
		sql = sql + " from cvi_curso_aula_virtual cau";
		sql = sql + " left join col_anio anio on anio.id = cau.id_anio ";
		sql = sql + " left join cat_grad gra on gra.id = cau.id_gra ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<CursoAulaVirtual>() {

			@Override
			public CursoAulaVirtual mapRow(ResultSet rs, int rowNum) throws SQLException {
				CursoAulaVirtual cursoaulavirtual= rsToEntity(rs,"cau_");
				Anio anio = new Anio();  
				anio.setId(rs.getInt("anio_id")) ;  
				anio.setNom(rs.getString("anio_nom")) ;  
				cursoaulavirtual.setAnio(anio);
				Grad grad = new Grad();  
				grad.setId(rs.getInt("gra_id")) ;  
				grad.setId_nvl(rs.getInt("gra_id_nvl")) ;  
				grad.setId_gra_ant(rs.getInt("gra_id_gra_ant")) ;  
				grad.setNom(rs.getString("gra_nom")) ;  
				grad.setTipo(rs.getString("gra_tipo")) ;  
				cursoaulavirtual.setGrad(grad);
				return cursoaulavirtual;
			}

		});

	}	


	public List<PeriodoCurso> getListPeriodoCurso(Param param, String[] order) {
		String sql = "select * from cvi_periodo_curso " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<PeriodoCurso>() {

			@Override
			public PeriodoCurso mapRow(ResultSet rs, int rowNum) throws SQLException {
				PeriodoCurso periodo_curso = new PeriodoCurso();

				periodo_curso.setId(rs.getInt("id"));
				periodo_curso.setId_cpv(rs.getInt("id_cpv"));
				periodo_curso.setId_cau(rs.getInt("id_cau"));
				periodo_curso.setActivo(rs.getInt("activo"));
				periodo_curso.setEst(rs.getString("est"));
												
				return periodo_curso;
			}

		});	
	}


	// funciones privadas utilitarias para CursoAulaVirtual

	private CursoAulaVirtual rsToEntity(ResultSet rs,String alias) throws SQLException {
		CursoAulaVirtual curso_aula_virtual = new CursoAulaVirtual();

		curso_aula_virtual.setId(rs.getInt( alias + "id"));
		curso_aula_virtual.setId_anio(rs.getInt( alias + "id_anio"));
		curso_aula_virtual.setId_gra(rs.getInt( alias + "id_gra"));
		curso_aula_virtual.setNom(rs.getString( alias + "nom"));
		curso_aula_virtual.setAbrev(rs.getString( alias + "abrev"));
		curso_aula_virtual.setEst(rs.getString( alias + "est"));
								
		return curso_aula_virtual;

	}
	
}

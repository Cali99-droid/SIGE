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
import com.tesla.colegio.model.CursoSubtema;

import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.Curso;
import com.tesla.colegio.model.Subtema;
import com.tesla.colegio.model.Tema;
import com.tesla.colegio.model.UnidadTema;
import com.tesla.colegio.model.SesionTema;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CursoSubtemaDAO.
 * @author MV
 *
 */
public class CursoSubtemaDAOImpl{
	final static Logger logger = Logger.getLogger(CursoSubtemaDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(CursoSubtema curso_subtema) {
		if (curso_subtema.getId() != null) {
			// update
			String sql = "UPDATE col_curso_subtema "
						+ "SET id_anio=?, "
						+ "id_niv=?, "
						+ "id_gra=?, "
						+ "id_cur=?, "
						+ "id_sub=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						curso_subtema.getId_anio(),
						curso_subtema.getId_niv(),
						curso_subtema.getId_gra(),
						curso_subtema.getId_cur(),
						curso_subtema.getId_sub(),
						curso_subtema.getEst(),
						curso_subtema.getUsr_act(),
						new java.util.Date(),
						curso_subtema.getId()); 
			return curso_subtema.getId();

		} else {
			// insert
			String sql = "insert into col_curso_subtema ("
						+ "id_anio, "
						+ "id_niv, "
						+ "id_gra, "
						+ "id_cur, "
						+ "id_sub, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?,?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				curso_subtema.getId_anio(),
				curso_subtema.getId_niv(),
				curso_subtema.getId_gra(),
				curso_subtema.getId_cur(),
				curso_subtema.getId_sub(),
				curso_subtema.getEst(),
				curso_subtema.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_curso_subtema where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<CursoSubtema> list() {
		String sql = "select * from col_curso_subtema";
		
		//logger.info(sql);
		
		List<CursoSubtema> listCursoSubtema = jdbcTemplate.query(sql, new RowMapper<CursoSubtema>() {

			@Override
			public CursoSubtema mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCursoSubtema;
	}

	public CursoSubtema get(int id) {
		String sql = "select * from col_curso_subtema WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoSubtema>() {

			@Override
			public CursoSubtema extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CursoSubtema getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select ccs.id ccs_id, ccs.id_anio ccs_id_anio , ccs.id_niv ccs_id_niv , ccs.id_gra ccs_id_gra , ccs.id_cur ccs_id_cur , ccs.id_sub ccs_id_sub , ccs.dur ccs_dur  ,ccs.est ccs_est ";
		if (aTablas.contains("col_anio"))
			sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		if (aTablas.contains("cat_grad"))
			sql = sql + ", gra.id gra_id  , gra.id_nvl gra_id_nvl , gra.nom gra_nom  ";
		if (aTablas.contains("cat_curso"))
			sql = sql + ", cur.id cur_id  , cur.nom cur_nom  ";
		if (aTablas.contains("col_subtema"))
			sql = sql + ", sub.id sub_id  , sub.id_tem sub_id_tem , sub.nom sub_nom , sub.ord sub_ord , sub.obs sub_obs  ";
	
		sql = sql + " from col_curso_subtema ccs "; 
		if (aTablas.contains("col_anio"))
			sql = sql + " left join col_anio anio on anio.id = ccs.id_anio ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + " left join cat_nivel niv on niv.id = ccs.id_niv ";
		if (aTablas.contains("cat_grad"))
			sql = sql + " left join cat_grad gra on gra.id = ccs.id_gra ";
		if (aTablas.contains("cat_curso"))
			sql = sql + " left join cat_curso cur on cur.id = ccs.id_cur ";
		if (aTablas.contains("col_subtema"))
			sql = sql + " left join col_subtema sub on sub.id = ccs.id_sub ";
		sql = sql + " where ccs.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoSubtema>() {
		
			@Override
			public CursoSubtema extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CursoSubtema cursosubtema= rsToEntity(rs,"ccs_");
					if (aTablas.contains("col_anio")){
						Anio anio = new Anio();  
							anio.setId(rs.getInt("anio_id")) ;  
							anio.setNom(rs.getString("anio_nom")) ;  
							cursosubtema.setAnio(anio);
					}
					if (aTablas.contains("cat_nivel")){
						Nivel nivel = new Nivel();  
							nivel.setId(rs.getInt("niv_id")) ;  
							nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
							nivel.setNom(rs.getString("niv_nom")) ;  
							cursosubtema.setNivel(nivel);
					}
					if (aTablas.contains("cat_grad")){
						Grad grad = new Grad();  
							grad.setId(rs.getInt("gra_id")) ;  
							grad.setId_nvl(rs.getInt("gra_id_nvl")) ;  
							grad.setNom(rs.getString("gra_nom")) ;  
							cursosubtema.setGrad(grad);
					}
					if (aTablas.contains("cat_curso")){
						Curso curso = new Curso();  
							curso.setId(rs.getInt("cur_id")) ;  
							curso.setNom(rs.getString("cur_nom")) ;  
							cursosubtema.setCurso(curso);
					}
					if (aTablas.contains("col_subtema")){
						Subtema subtema = new Subtema();  
							subtema.setId(rs.getInt("sub_id")) ;  
							subtema.setId_tem(rs.getInt("sub_id_tem")) ;  
							subtema.setNom(rs.getString("sub_nom")) ;  
							subtema.setOrd(rs.getInt("sub_ord")) ;  
							subtema.setObs(rs.getString("sub_obs")) ;  
							cursosubtema.setSubtema(subtema);
					}
							return cursosubtema;
				}
				
				return null;
			}
			
		});


	}		
	
	public CursoSubtema getByParams(Param param) {

		String sql = "select * from col_curso_subtema " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoSubtema>() {
			@Override
			public CursoSubtema extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CursoSubtema> listByParams(Param param, String[] order) {

		String sql = "select * from col_curso_subtema " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<CursoSubtema>() {

			@Override
			public CursoSubtema mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CursoSubtema> listFullByParams(CursoSubtema cursosubtema, String[] order) {
	
		return listFullByParams(Param.toParam("ccs",cursosubtema), order);
	
	}	
	
	public List<CursoSubtema> listFullByParams(Param param, String[] order) {

		String sql = "select ccs.id ccs_id, ccs.id_anio ccs_id_anio , ccs.id_niv ccs_id_niv , ccs.id_gra ccs_id_gra , ccs.id_cur ccs_id_cur , ccs.id_sub ccs_id_sub , ccs.est ccs_est ";
		sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		sql = sql + ", gra.id gra_id  , gra.id_nvl gra_id_nvl , gra.nom gra_nom  ";
		sql = sql + ", cur.id cur_id  , cur.nom cur_nom  ";
		sql = sql + ", sub.id sub_id  , sub.id_tem sub_id_tem , sub.nom sub_nom , sub.ord sub_ord , sub.obs sub_obs  ";
		sql = sql + ", tem.nom tem_nom ";
		sql = sql + " from col_curso_subtema ccs";
		sql = sql + " left join col_anio anio on anio.id = ccs.id_anio ";
		sql = sql + " left join cat_nivel niv on niv.id = ccs.id_niv ";
		sql = sql + " left join cat_grad gra on gra.id = ccs.id_gra ";
		sql = sql + " left join cat_curso cur on cur.id = ccs.id_cur ";
		sql = sql + " left join col_subtema sub on sub.id = ccs.id_sub ";
		sql = sql + " left join col_tema tem on sub.id_tem=tem.id ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<CursoSubtema>() {

			@Override
			public CursoSubtema mapRow(ResultSet rs, int rowNum) throws SQLException {
				CursoSubtema cursosubtema= rsToEntity(rs,"ccs_");
				Anio anio = new Anio();  
				anio.setId(rs.getInt("anio_id")) ;  
				anio.setNom(rs.getString("anio_nom")) ;  
				cursosubtema.setAnio(anio);
				Nivel nivel = new Nivel();  
				nivel.setId(rs.getInt("niv_id")) ;  
				nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
				nivel.setNom(rs.getString("niv_nom")) ;  
				cursosubtema.setNivel(nivel);
				Grad grad = new Grad();  
				grad.setId(rs.getInt("gra_id")) ;  
				grad.setId_nvl(rs.getInt("gra_id_nvl")) ;  
				grad.setNom(rs.getString("gra_nom")) ;  
				cursosubtema.setGrad(grad);
				Curso curso = new Curso();  
				curso.setId(rs.getInt("cur_id")) ;  
				curso.setNom(rs.getString("cur_nom")) ;  
				cursosubtema.setCurso(curso);
				Subtema subtema = new Subtema();  
				subtema.setId(rs.getInt("sub_id")) ;  
				subtema.setId_tem(rs.getInt("sub_id_tem")) ;  
				subtema.setNom(rs.getString("sub_nom")) ;  
				subtema.setOrd(rs.getInt("sub_ord")) ;  
				subtema.setObs(rs.getString("sub_obs")) ;  
				cursosubtema.setSubtema(subtema);
				Tema tema = new Tema();
				tema.setNom(rs.getString("tem_nom"));
				cursosubtema.setTema(tema);
				return cursosubtema;
			}

		});

	}	


	public List<UnidadTema> getListUnidadTema(Param param, String[] order) {
		String sql = "select * from col_unidad_tema " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<UnidadTema>() {

			@Override
			public UnidadTema mapRow(ResultSet rs, int rowNum) throws SQLException {
				UnidadTema unidad_tema = new UnidadTema();

				unidad_tema.setId(rs.getInt("id"));
				unidad_tema.setId_uni(rs.getInt("id_uni"));
				unidad_tema.setId_ccs(rs.getInt("id_ccs"));
				unidad_tema.setEst(rs.getString("est"));
												
				return unidad_tema;
			}

		});	
	}
	public List<SesionTema> getListSesionTema(Param param, String[] order) {
		String sql = "select * from col_sesion_tema " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<SesionTema>() {

			@Override
			public SesionTema mapRow(ResultSet rs, int rowNum) throws SQLException {
				SesionTema sesion_tema = new SesionTema();

				sesion_tema.setId(rs.getInt("id"));
				sesion_tema.setId_ses(rs.getInt("id_ses"));
				sesion_tema.setId_ccs(rs.getInt("id_ccs"));
				sesion_tema.setTipo(rs.getString("tipo"));
				sesion_tema.setEst(rs.getString("est"));
												
				return sesion_tema;
			}

		});	
	}


	// funciones privadas utilitarias para CursoSubtema

	private CursoSubtema rsToEntity(ResultSet rs,String alias) throws SQLException {
		CursoSubtema curso_subtema = new CursoSubtema();

		curso_subtema.setId(rs.getInt( alias + "id"));
		curso_subtema.setId_anio(rs.getInt( alias + "id_anio"));
		curso_subtema.setId_niv(rs.getInt( alias + "id_niv"));
		curso_subtema.setId_gra(rs.getInt( alias + "id_gra"));
		curso_subtema.setId_cur(rs.getInt( alias + "id_cur"));
		curso_subtema.setId_sub(rs.getInt( alias + "id_sub"));
		//curso_subtema.setDur(rs.getBigDecimal( alias + "dur"));
		curso_subtema.setEst(rs.getString( alias + "est"));
								
		return curso_subtema;

	}
	
}

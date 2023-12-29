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
import com.tesla.colegio.model.SesionTema;

import com.tesla.colegio.model.UnidadSesion;
import com.tesla.colegio.model.CursoSubtema;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface SesionTemaDAO.
 * @author MV
 *
 */
public class SesionTemaDAOImpl{
	final static Logger logger = Logger.getLogger(SesionTemaDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(SesionTema sesion_tema) {
		if (sesion_tema.getId() != null) {
			// update
			String sql = "UPDATE col_sesion_tema "
						+ "SET id_ses=?, "
						+ "id_ccs=?, "
						+ "tipo=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						sesion_tema.getId_ses(),
						sesion_tema.getId_ccs(),
						sesion_tema.getTipo(),
						sesion_tema.getEst(),
						sesion_tema.getUsr_act(),
						new java.util.Date(),
						sesion_tema.getId()); 
			return sesion_tema.getId();

		} else {
			// insert
			String sql = "insert into col_sesion_tema ("
						+ "id_ses, "
						+ "id_ccs, "
						+ "tipo, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				sesion_tema.getId_ses(),
				sesion_tema.getId_ccs(),
				sesion_tema.getTipo(),
				sesion_tema.getEst(),
				sesion_tema.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_sesion_tema where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<SesionTema> list() {
		String sql = "select * from col_sesion_tema";
		
		//logger.info(sql);
		
		List<SesionTema> listSesionTema = jdbcTemplate.query(sql, new RowMapper<SesionTema>() {

			@Override
			public SesionTema mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listSesionTema;
	}

	public SesionTema get(int id) {
		String sql = "select * from col_sesion_tema WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SesionTema>() {

			@Override
			public SesionTema extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public SesionTema getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cst.id cst_id, cst.id_ses cst_id_ses , cst.id_ccs cst_id_ccs , cst.tipo cst_tipo  ,cst.est cst_est ";
		if (aTablas.contains("col_unidad_sesion"))
			sql = sql + ", ses.id ses_id  , ses.id_uni ses_id_uni , ses.tit ses_tit , ses.dur ses_dur , ses.tipo ses_tipo  ";
		if (aTablas.contains("col_curso_subtema"))
			sql = sql + ", ccs.id ccs_id  , ccs.id_anio ccs_id_anio , ccs.id_cur ccs_id_cur , ccs.id_sub ccs_id_sub , ccs.id_gra ccs_id_gra , ccs.dur ccs_dur  ";
	
		sql = sql + " from col_sesion_tema cst "; 
		if (aTablas.contains("col_unidad_sesion"))
			sql = sql + " left join col_unidad_sesion ses on ses.id = cst.id_ses ";
		if (aTablas.contains("col_curso_subtema"))
			sql = sql + " left join col_curso_subtema ccs on ccs.id = cst.id_ccs ";
		sql = sql + " where cst.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<SesionTema>() {
		
			@Override
			public SesionTema extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					SesionTema sesiontema= rsToEntity(rs,"cst_");
					if (aTablas.contains("col_unidad_sesion")){
						UnidadSesion unidadsesion = new UnidadSesion();  
							unidadsesion.setId(rs.getInt("ses_id")) ;  
							unidadsesion.setId_uni(rs.getInt("ses_id_uni")) ;  
							//unidadsesion.setTit(rs.getString("ses_tit")) ;  
							//unidadsesion.setDur(rs.getBigDecimal("ses_dur")) ;  
							//unidadsesion.setTipo(rs.getString("ses_tipo")) ;  
							sesiontema.setUnidadSesion(unidadsesion);
					}
					if (aTablas.contains("col_curso_subtema")){
						CursoSubtema cursosubtema = new CursoSubtema();  
							cursosubtema.setId(rs.getInt("ccs_id")) ;  
							cursosubtema.setId_anio(rs.getInt("ccs_id_anio")) ;  
							cursosubtema.setId_cur(rs.getInt("ccs_id_cur")) ;  
							cursosubtema.setId_sub(rs.getInt("ccs_id_sub")) ;  
							cursosubtema.setId_gra(rs.getInt("ccs_id_gra")) ;  
							cursosubtema.setDur(rs.getBigDecimal("ccs_dur")) ;  
							sesiontema.setCursoSubtema(cursosubtema);
					}
							return sesiontema;
				}
				
				return null;
			}
			
		});


	}		
	
	public SesionTema getByParams(Param param) {

		String sql = "select * from col_sesion_tema " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SesionTema>() {
			@Override
			public SesionTema extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<SesionTema> listByParams(Param param, String[] order) {

		String sql = "select * from col_sesion_tema " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<SesionTema>() {

			@Override
			public SesionTema mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<SesionTema> listFullByParams(SesionTema sesiontema, String[] order) {
	
		return listFullByParams(Param.toParam("cst",sesiontema), order);
	
	}	
	
	public List<SesionTema> listFullByParams(Param param, String[] order) {

		String sql = "select cst.id cst_id, cst.id_ses cst_id_ses , cst.id_ccs cst_id_ccs , cst.tipo cst_tipo  ,cst.est cst_est ";
		sql = sql + ", ses.id ses_id  , ses.id_uni ses_id_uni , ses.tit ses_tit , ses.dur ses_dur , ses.tipo ses_tipo  ";
		sql = sql + ", ccs.id ccs_id  , ccs.id_anio ccs_id_anio , ccs.id_cur ccs_id_cur , ccs.id_sub ccs_id_sub , ccs.id_gra ccs_id_gra , ccs.dur ccs_dur  ";
		sql = sql + " from col_sesion_tema cst";
		sql = sql + " left join col_unidad_sesion ses on ses.id = cst.id_ses ";
		sql = sql + " left join col_curso_subtema ccs on ccs.id = cst.id_ccs ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<SesionTema>() {

			@Override
			public SesionTema mapRow(ResultSet rs, int rowNum) throws SQLException {
				SesionTema sesiontema= rsToEntity(rs,"cst_");
				UnidadSesion unidadsesion = new UnidadSesion();  
				unidadsesion.setId(rs.getInt("ses_id")) ;  
				unidadsesion.setId_uni(rs.getInt("ses_id_uni")) ;  
				//unidadsesion.setTit(rs.getString("ses_tit")) ;  
				//unidadsesion.setDur(rs.getBigDecimal("ses_dur")) ;  
				//unidadsesion.setTipo(rs.getString("ses_tipo")) ;  
				sesiontema.setUnidadSesion(unidadsesion);
				CursoSubtema cursosubtema = new CursoSubtema();  
				cursosubtema.setId(rs.getInt("ccs_id")) ;  
				cursosubtema.setId_anio(rs.getInt("ccs_id_anio")) ;  
				cursosubtema.setId_cur(rs.getInt("ccs_id_cur")) ;  
				cursosubtema.setId_sub(rs.getInt("ccs_id_sub")) ;  
				cursosubtema.setId_gra(rs.getInt("ccs_id_gra")) ;  
				cursosubtema.setDur(rs.getBigDecimal("ccs_dur")) ;  
				sesiontema.setCursoSubtema(cursosubtema);
				return sesiontema;
			}

		});

	}	




	// funciones privadas utilitarias para SesionTema

	private SesionTema rsToEntity(ResultSet rs,String alias) throws SQLException {
		SesionTema sesion_tema = new SesionTema();

		sesion_tema.setId(rs.getInt( alias + "id"));
		sesion_tema.setId_ses(rs.getInt( alias + "id_ses"));
		sesion_tema.setId_ccs(rs.getInt( alias + "id_ccs"));
		sesion_tema.setTipo(rs.getString( alias + "tipo"));
		sesion_tema.setEst(rs.getString( alias + "est"));
								
		return sesion_tema;

	}
	
}

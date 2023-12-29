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
import com.tesla.colegio.model.UnidadTema;

import com.tesla.colegio.model.CursoUnidad;
import com.tesla.colegio.model.CursoSubtema;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface UnidadTemaDAO.
 * @author MV
 *
 */
public class UnidadTemaDAOImpl{
	final static Logger logger = Logger.getLogger(UnidadTemaDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(UnidadTema unidad_tema) {
		if (unidad_tema.getId() != null) {
			// update
			String sql = "UPDATE col_unidad_tema "
						+ "SET id_uni=?, "
						+ "id_ccs=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						unidad_tema.getId_uni(),
						unidad_tema.getId_ccs(),
						unidad_tema.getEst(),
						unidad_tema.getUsr_act(),
						new java.util.Date(),
						unidad_tema.getId()); 
			return unidad_tema.getId();

		} else {
			// insert
			String sql = "insert into col_unidad_tema ("
						+ "id_uni, "
						+ "id_ccs, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				unidad_tema.getId_uni(),
				unidad_tema.getId_ccs(),
				unidad_tema.getEst(),
				unidad_tema.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_unidad_tema where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<UnidadTema> list() {
		String sql = "select * from col_unidad_tema";
		
		//logger.info(sql);
		
		List<UnidadTema> listUnidadTema = jdbcTemplate.query(sql, new RowMapper<UnidadTema>() {

			@Override
			public UnidadTema mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listUnidadTema;
	}

	public UnidadTema get(int id) {
		String sql = "select * from col_unidad_tema WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<UnidadTema>() {

			@Override
			public UnidadTema extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public UnidadTema getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cut.id cut_id, cut.id_uni cut_id_uni , cut.id_ccs cut_id_ccs  ,cut.est cut_est ";
		if (aTablas.contains("col_curso_unidad"))
			sql = sql + ", uni.id uni_id  , uni.id_cua uni_id_cua , uni.num uni_num , uni.nom uni_nom , uni.des uni_des , uni.nro_ses uni_nro_ses , uni.nro_sem uni_nro_sem , uni.producto uni_producto  ";
		if (aTablas.contains("col_curso_subtema"))
			sql = sql + ", ccs.id ccs_id  , ccs.id_anio ccs_id_anio , ccs.id_cur ccs_id_cur , ccs.id_sub ccs_id_sub , ccs.id_gra ccs_id_gra , ccs.dur ccs_dur  ";
	
		sql = sql + " from col_unidad_tema cut "; 
		if (aTablas.contains("col_curso_unidad"))
			sql = sql + " left join col_curso_unidad uni on uni.id = cut.id_uni ";
		if (aTablas.contains("col_curso_subtema"))
			sql = sql + " left join col_curso_subtema ccs on ccs.id = cut.id_ccs ";
		sql = sql + " where cut.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<UnidadTema>() {
		
			@Override
			public UnidadTema extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					UnidadTema unidadtema= rsToEntity(rs,"cut_");
					if (aTablas.contains("col_curso_unidad")){
						CursoUnidad cursounidad = new CursoUnidad();  
							cursounidad.setId(rs.getInt("uni_id")) ;  
							cursounidad.setNum(rs.getInt("uni_num")) ;  
							cursounidad.setNom(rs.getString("uni_nom")) ;  
							cursounidad.setDes(rs.getString("uni_des")) ;  
							//cursounidad.setNro_sem(rs.getInt("uni_nro_sem")) ;  
							cursounidad.setProducto(rs.getString("uni_producto")) ;  
							unidadtema.setCursoUnidad(cursounidad);
					}
					if (aTablas.contains("col_curso_subtema")){
						CursoSubtema cursosubtema = new CursoSubtema();  
							cursosubtema.setId(rs.getInt("ccs_id")) ;  
							cursosubtema.setId_anio(rs.getInt("ccs_id_anio")) ;  
							cursosubtema.setId_cur(rs.getInt("ccs_id_cur")) ;  
							cursosubtema.setId_sub(rs.getInt("ccs_id_sub")) ;  
							cursosubtema.setId_gra(rs.getInt("ccs_id_gra")) ;  
							cursosubtema.setDur(rs.getBigDecimal("ccs_dur")) ;  
							unidadtema.setCursoSubtema(cursosubtema);
					}
							return unidadtema;
				}
				
				return null;
			}
			
		});


	}		
	
	public UnidadTema getByParams(Param param) {

		String sql = "select * from col_unidad_tema " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<UnidadTema>() {
			@Override
			public UnidadTema extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<UnidadTema> listByParams(Param param, String[] order) {

		String sql = "select * from col_unidad_tema " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<UnidadTema>() {

			@Override
			public UnidadTema mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<UnidadTema> listFullByParams(UnidadTema unidadtema, String[] order) {
	
		return listFullByParams(Param.toParam("cut",unidadtema), order);
	
	}	
	
	public List<UnidadTema> listFullByParams(Param param, String[] order) {

		String sql = "select cut.id cut_id, cut.id_uni cut_id_uni , cut.id_ccs cut_id_ccs  ,cut.est cut_est ";
		sql = sql + ", uni.id uni_id  , uni.id_cua uni_id_cua , uni.num uni_num , uni.nom uni_nom , uni.des uni_des , uni.nro_ses uni_nro_ses , uni.nro_sem uni_nro_sem , uni.producto uni_producto  ";
		sql = sql + ", ccs.id ccs_id  , ccs.id_anio ccs_id_anio , ccs.id_cur ccs_id_cur , ccs.id_sub ccs_id_sub , ccs.id_gra ccs_id_gra , ccs.dur ccs_dur  ";
		sql = sql + " from col_unidad_tema cut";
		sql = sql + " left join col_curso_unidad uni on uni.id = cut.id_uni ";
		sql = sql + " left join col_curso_subtema ccs on ccs.id = cut.id_ccs ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<UnidadTema>() {

			@Override
			public UnidadTema mapRow(ResultSet rs, int rowNum) throws SQLException {
				UnidadTema unidadtema= rsToEntity(rs,"cut_");
				CursoUnidad cursounidad = new CursoUnidad();  
				cursounidad.setId(rs.getInt("uni_id")) ;  
				cursounidad.setNum(rs.getInt("uni_num")) ;  
				cursounidad.setNom(rs.getString("uni_nom")) ;  
				cursounidad.setDes(rs.getString("uni_des")) ;  
				//cursounidad.setNro_sem(rs.getInt("uni_nro_sem")) ;  
				cursounidad.setProducto(rs.getString("uni_producto")) ;  
				unidadtema.setCursoUnidad(cursounidad);
				CursoSubtema cursosubtema = new CursoSubtema();  
				cursosubtema.setId(rs.getInt("ccs_id")) ;  
				cursosubtema.setId_anio(rs.getInt("ccs_id_anio")) ;  
				cursosubtema.setId_cur(rs.getInt("ccs_id_cur")) ;  
				cursosubtema.setId_sub(rs.getInt("ccs_id_sub")) ;  
				cursosubtema.setId_gra(rs.getInt("ccs_id_gra")) ;  
				cursosubtema.setDur(rs.getBigDecimal("ccs_dur")) ;  
				unidadtema.setCursoSubtema(cursosubtema);
				return unidadtema;
			}

		});

	}	




	// funciones privadas utilitarias para UnidadTema

	private UnidadTema rsToEntity(ResultSet rs,String alias) throws SQLException {
		UnidadTema unidad_tema = new UnidadTema();

		unidad_tema.setId(rs.getInt( alias + "id"));
		unidad_tema.setId_uni(rs.getInt( alias + "id_uni"));
		unidad_tema.setId_ccs(rs.getInt( alias + "id_ccs"));
		unidad_tema.setEst(rs.getString( alias + "est"));
								
		return unidad_tema;

	}
	
}

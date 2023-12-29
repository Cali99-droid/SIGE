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
import com.tesla.colegio.model.UnidadSesion;

import com.tesla.colegio.model.CursoUnidad;
import com.tesla.colegio.model.SesionTipo;
import com.tesla.colegio.model.SesionIndicador;
import com.tesla.colegio.model.SesionActividad;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface UnidadSesionDAO.
 * @author MV
 *
 */
public class UnidadSesionDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(UnidadSesion unidad_sesion) {
		if (unidad_sesion.getId() != null) {
			// update
			String sql = "UPDATE col_unidad_sesion "
						+ "SET "
						+ "nom=?, "
						+ "usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						unidad_sesion.getNom(),
						unidad_sesion.getUsr_act(),
						new java.util.Date(),
						unidad_sesion.getId()); 
			return unidad_sesion.getId();

		} else {
			// insert
			String sql = "insert into col_unidad_sesion ("
						+ "id_uni, "
						+ "nro, "
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				unidad_sesion.getId_uni(),
				unidad_sesion.getNro(),
				unidad_sesion.getNom(),
				unidad_sesion.getEst(),
				unidad_sesion.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_unidad_sesion where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<UnidadSesion> list() {
		String sql = "select * from col_unidad_sesion";
		
		
		
		List<UnidadSesion> listUnidadSesion = jdbcTemplate.query(sql, new RowMapper<UnidadSesion>() {

			@Override
			public UnidadSesion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listUnidadSesion;
	}

	public UnidadSesion get(int id) {
		String sql = "select * from col_unidad_sesion WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<UnidadSesion>() {

			@Override
			public UnidadSesion extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public UnidadSesion getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select uns.id uns_id, uns.id_uni uns_id_uni , uns.nro uns_nro , uns.nom uns_nom  ,uns.est uns_est ";
		if (aTablas.contains("col_curso_unidad"))
			sql = sql + ", uni.id uni_id  , uni.id_niv uni_id_niv , uni.id_gra uni_id_gra , uni.id_cur uni_id_cur , uni.id_cpu uni_id_cpu , uni.num uni_num , uni.nom uni_nom , uni.des uni_des , uni.nro_sem uni_nro_sem , uni.producto uni_producto  ";
	
		sql = sql + " from col_unidad_sesion uns "; 
		if (aTablas.contains("col_curso_unidad"))
			sql = sql + " left join col_curso_unidad uni on uni.id = uns.id_uni ";
		sql = sql + " where uns.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<UnidadSesion>() {
		
			@Override
			public UnidadSesion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					UnidadSesion unidadsesion= rsToEntity(rs,"uns_");
					if (aTablas.contains("col_curso_unidad")){
						CursoUnidad cursounidad = new CursoUnidad();  
							cursounidad.setId(rs.getInt("uni_id")) ;  
							cursounidad.setId_niv(rs.getInt("uni_id_niv")) ;  
							cursounidad.setId_gra(rs.getInt("uni_id_gra")) ;  
							cursounidad.setId_cur(rs.getInt("uni_id_cur")) ;  
							cursounidad.setId_cpu(rs.getInt("uni_id_cpu")) ;  
							cursounidad.setNum(rs.getInt("uni_num")) ;  
							cursounidad.setNom(rs.getString("uni_nom")) ;  
							cursounidad.setDes(rs.getString("uni_des")) ;  
						//	cursounidad.setNro_sem(rs.getInt("uni_nro_sem")) ;  
							cursounidad.setProducto(rs.getString("uni_producto")) ;  
							unidadsesion.setCursoUnidad(cursounidad);
					}
							return unidadsesion;
				}
				
				return null;
			}
			
		});


	}		
	
	public UnidadSesion getByParams(Param param) {

		String sql = "select * from col_unidad_sesion " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<UnidadSesion>() {
			@Override
			public UnidadSesion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<UnidadSesion> listByParams(Param param, String[] order) {

		String sql = "select * from col_unidad_sesion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<UnidadSesion>() {

			@Override
			public UnidadSesion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<UnidadSesion> listFullByParams(UnidadSesion unidadsesion, String[] order) {
	
		return listFullByParams(Param.toParam("uns",unidadsesion), order);
	
	}	
	
	public List<UnidadSesion> listFullByParams(Param param, String[] order) {

		String sql = "select uns.id uns_id, uns.id_uni uns_id_uni , uns.nro uns_nro , uns.nom uns_nom  ,uns.est uns_est, ses.id id_ses ";
		sql = sql + ", uni.id uni_id  , uni.id_niv uni_id_niv , uni.id_gra uni_id_gra , uni.id_cur uni_id_cur , uni.id_cpu uni_id_cpu , uni.num uni_num , uni.nom uni_nom , uni.des uni_des , uni.producto uni_producto  ";
		sql = sql + " from col_unidad_sesion uns";
		sql = sql + " left join col_curso_unidad uni on uni.id = uns.id_uni ";
		sql = sql + " left join col_sesion_tipo ses ON ses.id_uns=uns.id ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<UnidadSesion>() {

			@Override
			public UnidadSesion mapRow(ResultSet rs, int rowNum) throws SQLException {
				UnidadSesion unidadsesion= rsToEntity(rs,"uns_");
				CursoUnidad cursounidad = new CursoUnidad();  
				cursounidad.setId(rs.getInt("uni_id")) ;  
				cursounidad.setId_niv(rs.getInt("uni_id_niv")) ;  
				cursounidad.setId_gra(rs.getInt("uni_id_gra")) ;  
				cursounidad.setId_cur(rs.getInt("uni_id_cur")) ;  
				cursounidad.setId_cpu(rs.getInt("uni_id_cpu")) ;  
				cursounidad.setNum(rs.getInt("uni_num")) ;  
				cursounidad.setNom(rs.getString("uni_nom")) ;  
				cursounidad.setDes(rs.getString("uni_des")) ;  
			//	cursounidad.setNro_sem(rs.getInt("uni_nro_sem")) ;  
				cursounidad.setProducto(rs.getString("uni_producto")) ;  
				unidadsesion.setCursoUnidad(cursounidad);
				SesionTipo sesionTipo = new SesionTipo();
				sesionTipo.setId(rs.getInt("id_ses"));
				unidadsesion.setSesionTipo(sesionTipo);
				return unidadsesion;
			}

		});

	}	


	public List<SesionTipo> getListSesionTipo(Param param, String[] order) {
		String sql = "select * from col_sesion_tipo " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		
		
		return jdbcTemplate.query(sql, new RowMapper<SesionTipo>() {

			@Override
			public SesionTipo mapRow(ResultSet rs, int rowNum) throws SQLException {
				SesionTipo sesion_tipo = new SesionTipo();

				sesion_tipo.setId(rs.getInt("id"));
				sesion_tipo.setId_uns(rs.getInt("id_uns"));
				sesion_tipo.setId_cts(rs.getInt("id_cts"));
				sesion_tipo.setEst(rs.getString("est"));
												
				return sesion_tipo;
			}

		});	
	}
	public List<SesionIndicador> getListSesionIndicador(Param param, String[] order) {
		String sql = "select * from col_sesion_indicador " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		
		
		return jdbcTemplate.query(sql, new RowMapper<SesionIndicador>() {

			@Override
			public SesionIndicador mapRow(ResultSet rs, int rowNum) throws SQLException {
				SesionIndicador sesion_indicador = new SesionIndicador();

				sesion_indicador.setId(rs.getInt("id"));
				sesion_indicador.setId_ses(rs.getInt("id_ses"));
				sesion_indicador.setId_ind(rs.getInt("id_ind"));
				sesion_indicador.setEst(rs.getString("est"));
												
				return sesion_indicador;
			}

		});	
	}
	public List<SesionActividad> getListSesionActividad(Param param, String[] order) {
		String sql = "select * from col_sesion_actividad " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		
		
		return jdbcTemplate.query(sql, new RowMapper<SesionActividad>() {

			@Override
			public SesionActividad mapRow(ResultSet rs, int rowNum) throws SQLException {
				SesionActividad sesion_actividad = new SesionActividad();

				sesion_actividad.setId(rs.getInt("id"));
				sesion_actividad.setId_ses(rs.getInt("id_ses"));
				sesion_actividad.setNom(rs.getString("nom"));
				sesion_actividad.setEst(rs.getString("est"));
												
				return sesion_actividad;
			}

		});	
	}


	// funciones privadas utilitarias para UnidadSesion

	private UnidadSesion rsToEntity(ResultSet rs,String alias) throws SQLException {
		UnidadSesion unidad_sesion = new UnidadSesion();

		unidad_sesion.setId(rs.getInt( alias + "id"));
		unidad_sesion.setId_uni(rs.getInt( alias + "id_uni"));
		unidad_sesion.setNro(rs.getInt( alias + "nro"));
		unidad_sesion.setNom(rs.getString( alias + "nom"));
		unidad_sesion.setEst(rs.getString( alias + "est"));
								
		return unidad_sesion;

	}
	
}

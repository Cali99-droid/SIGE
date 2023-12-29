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
import com.tesla.colegio.model.TipoSesion;

import com.tesla.colegio.model.SesionTipo;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface TipoSesionDAO.
 * @author MV
 *
 */
public class TipoSesionDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(TipoSesion tipo_sesion) {
		if (tipo_sesion.getId() != null) {
			// update
			String sql = "UPDATE cat_tipo_sesion "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						tipo_sesion.getNom(),
						tipo_sesion.getEst(),
						tipo_sesion.getUsr_act(),
						new java.util.Date(),
						tipo_sesion.getId()); 
			return tipo_sesion.getId();

		} else {
			// insert
			String sql = "insert into cat_tipo_sesion ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				tipo_sesion.getNom(),
				tipo_sesion.getEst(),
				tipo_sesion.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_tipo_sesion where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<TipoSesion> list() {
		String sql = "select * from cat_tipo_sesion";
		
		
		
		List<TipoSesion> listTipoSesion = jdbcTemplate.query(sql, new RowMapper<TipoSesion>() {

			@Override
			public TipoSesion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listTipoSesion;
	}

	public TipoSesion get(int id) {
		String sql = "select * from cat_tipo_sesion WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoSesion>() {

			@Override
			public TipoSesion extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public TipoSesion getFull(int id, String tablas[]) {
		String sql = "select cts.id cts_id, cts.nom cts_nom  ,cts.est cts_est ";
	
		sql = sql + " from cat_tipo_sesion cts "; 
		sql = sql + " where cts.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoSesion>() {
		
			@Override
			public TipoSesion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					TipoSesion tiposesion= rsToEntity(rs,"cts_");
							return tiposesion;
				}
				
				return null;
			}
			
		});


	}		
	
	public TipoSesion getByParams(Param param) {

		String sql = "select * from cat_tipo_sesion " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoSesion>() {
			@Override
			public TipoSesion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<TipoSesion> listByParams(Param param, String[] order) {

		String sql = "select * from cat_tipo_sesion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<TipoSesion>() {

			@Override
			public TipoSesion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<TipoSesion> listFullByParams(TipoSesion tiposesion, String[] order) {
	
		return listFullByParams(Param.toParam("cts",tiposesion), order);
	
	}	
	
	public List<TipoSesion> listFullByParams(Param param, String[] order) {

		String sql = "select cts.id cts_id, cts.nom cts_nom  ,cts.est cts_est ";
		sql = sql + " from cat_tipo_sesion cts";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<TipoSesion>() {

			@Override
			public TipoSesion mapRow(ResultSet rs, int rowNum) throws SQLException {
				TipoSesion tiposesion= rsToEntity(rs,"cts_");
				return tiposesion;
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


	// funciones privadas utilitarias para TipoSesion

	private TipoSesion rsToEntity(ResultSet rs,String alias) throws SQLException {
		TipoSesion tipo_sesion = new TipoSesion();

		tipo_sesion.setId(rs.getInt( alias + "id"));
		tipo_sesion.setNom(rs.getString( alias + "nom"));
		tipo_sesion.setEst(rs.getString( alias + "est"));
								
		return tipo_sesion;

	}
	
}

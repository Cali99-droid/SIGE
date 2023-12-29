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
import com.tesla.colegio.model.SesionTipo;

import com.tesla.colegio.model.TipoSesion;
import com.tesla.colegio.model.SesionDesempenio;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface SesionTipoDAO.
 * @author MV
 *
 */
public class SesionTipoDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(SesionTipo sesion_tipo) {
		if (sesion_tipo.getId() != null) {
			// update
			String sql = "UPDATE col_sesion_tipo "
						+ "SET id_uns=?, "
						+ "id_cts=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						sesion_tipo.getId_uns(),
						sesion_tipo.getId_cts(),
						sesion_tipo.getEst(),
						sesion_tipo.getUsr_act(),
						new java.util.Date(),
						sesion_tipo.getId()); 
			return sesion_tipo.getId();

		} else {
			// insert
			String sql = "insert into col_sesion_tipo ("
						+ "id_uns, "
						+ "id_cts, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				sesion_tipo.getId_uns(),
				sesion_tipo.getId_cts(),
				sesion_tipo.getEst(),
				sesion_tipo.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_sesion_tipo where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<SesionTipo> list() {
		String sql = "select * from col_sesion_tipo";
		
		
		
		List<SesionTipo> listSesionTipo = jdbcTemplate.query(sql, new RowMapper<SesionTipo>() {

			@Override
			public SesionTipo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listSesionTipo;
	}

	public SesionTipo get(int id) {
		String sql = "select * from col_sesion_tipo WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SesionTipo>() {

			@Override
			public SesionTipo extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public SesionTipo getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select ses.id ses_id, ses.id_uns ses_id_uns , ses.id_cts ses_id_cts  ,ses.est ses_est ";
		if (aTablas.contains("cat_tipo_sesion"))
			sql = sql + ", cts.id cts_id  , cts.nom cts_nom  ";
	
		sql = sql + " from col_sesion_tipo ses "; 
		if (aTablas.contains("cat_tipo_sesion"))
			sql = sql + " left join cat_tipo_sesion cts on cts.id = ses.id_cts ";
		sql = sql + " where ses.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<SesionTipo>() {
		
			@Override
			public SesionTipo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					SesionTipo sesiontipo= rsToEntity(rs,"ses_");
					if (aTablas.contains("cat_tipo_sesion")){
						TipoSesion tiposesion = new TipoSesion();  
							tiposesion.setId(rs.getInt("cts_id")) ;  
							tiposesion.setNom(rs.getString("cts_nom")) ;  
							sesiontipo.setTipoSesion(tiposesion);
					}
							return sesiontipo;
				}
				
				return null;
			}
			
		});


	}		
	
	public SesionTipo getByParams(Param param) {

		String sql = "select * from col_sesion_tipo " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SesionTipo>() {
			@Override
			public SesionTipo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<SesionTipo> listByParams(Param param, String[] order) {

		String sql = "select * from col_sesion_tipo " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<SesionTipo>() {

			@Override
			public SesionTipo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<SesionTipo> listFullByParams(SesionTipo sesiontipo, String[] order) {
	
		return listFullByParams(Param.toParam("ses",sesiontipo), order);
	
	}	
	
	public List<SesionTipo> listFullByParams(Param param, String[] order) {

		String sql = "select ses.id ses_id, ses.id_uns ses_id_uns , ses.id_cts ses_id_cts  ,ses.est ses_est ";
		sql = sql + ", cts.id cts_id  , cts.nom cts_nom  ";
		sql = sql + " from col_sesion_tipo ses";
		sql = sql + " left join cat_tipo_sesion cts on cts.id = ses.id_cts ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<SesionTipo>() {

			@Override
			public SesionTipo mapRow(ResultSet rs, int rowNum) throws SQLException {
				SesionTipo sesiontipo= rsToEntity(rs,"ses_");
				TipoSesion tiposesion = new TipoSesion();  
				tiposesion.setId(rs.getInt("cts_id")) ;  
				tiposesion.setNom(rs.getString("cts_nom")) ;  
				sesiontipo.setTipoSesion(tiposesion);
				return sesiontipo;
			}

		});

	}	


	public List<SesionDesempenio> getListSesionDesempenio(Param param, String[] order) {
		String sql = "select * from col_sesion_desempenio " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		
		
		return jdbcTemplate.query(sql, new RowMapper<SesionDesempenio>() {

			@Override
			public SesionDesempenio mapRow(ResultSet rs, int rowNum) throws SQLException {
				SesionDesempenio sesion_desempenio = new SesionDesempenio();

				sesion_desempenio.setId(rs.getInt("id"));
				sesion_desempenio.setId_cde(rs.getInt("id_cde"));
				sesion_desempenio.setId_ses(rs.getInt("id_ses"));
				sesion_desempenio.setEst(rs.getString("est"));
												
				return sesion_desempenio;
			}

		});	
	}


	// funciones privadas utilitarias para SesionTipo

	private SesionTipo rsToEntity(ResultSet rs,String alias) throws SQLException {
		SesionTipo sesion_tipo = new SesionTipo();

		sesion_tipo.setId(rs.getInt( alias + "id"));
		sesion_tipo.setId_uns(rs.getInt( alias + "id_uns"));
		sesion_tipo.setId_cts(rs.getInt( alias + "id_cts"));
		sesion_tipo.setEst(rs.getString( alias + "est"));
								
		return sesion_tipo;

	}
	
}

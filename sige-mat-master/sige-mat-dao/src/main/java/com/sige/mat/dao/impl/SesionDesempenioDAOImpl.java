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
import com.tesla.colegio.model.SesionDesempenio;

import com.tesla.colegio.model.Desempenio;
import com.tesla.colegio.model.SesionTipo;
import com.tesla.colegio.model.Indicador;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface SesionDesempenioDAO.
 * @author MV
 *
 */
public class SesionDesempenioDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(SesionDesempenio sesion_desempenio) {
		if (sesion_desempenio.getId() != null) {
			// update
			String sql = "UPDATE col_sesion_desempenio "
						+ "SET id_cde=?, "
						+ "id_ses=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						sesion_desempenio.getId_cde(),
						sesion_desempenio.getId_ses(),
						sesion_desempenio.getEst(),
						sesion_desempenio.getUsr_act(),
						new java.util.Date(),
						sesion_desempenio.getId()); 
			return sesion_desempenio.getId();

		} else {
			// insert
			String sql = "insert into col_sesion_desempenio ("
						+ "id_cde, "
						+ "id_ses, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				sesion_desempenio.getId_cde(),
				sesion_desempenio.getId_ses(),
				sesion_desempenio.getEst(),
				sesion_desempenio.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_sesion_desempenio where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<SesionDesempenio> list() {
		String sql = "select * from col_sesion_desempenio";
		
		
		
		List<SesionDesempenio> listSesionDesempenio = jdbcTemplate.query(sql, new RowMapper<SesionDesempenio>() {

			@Override
			public SesionDesempenio mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listSesionDesempenio;
	}

	public SesionDesempenio get(int id) {
		String sql = "select * from col_sesion_desempenio WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SesionDesempenio>() {

			@Override
			public SesionDesempenio extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public SesionDesempenio getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select csd.id csd_id, csd.id_cde csd_id_cde , csd.id_ses csd_id_ses  ,csd.est csd_est ";
		if (aTablas.contains("col_desempenio"))
			sql = sql + ", cde.id cde_id  , cde.nom cde_nom , cde.id_cgc cde_id_cgc  ";
		if (aTablas.contains("col_sesion_tipo"))
			sql = sql + ", ses.id ses_id  , ses.id_uns ses_id_uns , ses.id_cts ses_id_cts  ";
	
		sql = sql + " from col_sesion_desempenio csd "; 
		if (aTablas.contains("col_desempenio"))
			sql = sql + " left join col_desempenio cde on cde.id = csd.id_cde ";
		if (aTablas.contains("col_sesion_tipo"))
			sql = sql + " left join col_sesion_tipo ses on ses.id = csd.id_ses ";
		sql = sql + " where csd.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<SesionDesempenio>() {
		
			@Override
			public SesionDesempenio extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					SesionDesempenio sesiondesempenio= rsToEntity(rs,"csd_");
					if (aTablas.contains("col_desempenio")){
						Desempenio desempenio = new Desempenio();  
							desempenio.setId(rs.getInt("cde_id")) ;  
							desempenio.setNom(rs.getString("cde_nom")) ;  
							desempenio.setId_cgc(rs.getInt("cde_id_cgc")) ;  
							sesiondesempenio.setDesempenio(desempenio);
					}
					if (aTablas.contains("col_sesion_tipo")){
						SesionTipo sesiontipo = new SesionTipo();  
							sesiontipo.setId(rs.getInt("ses_id")) ;  
							sesiontipo.setId_uns(rs.getInt("ses_id_uns")) ;  
							sesiontipo.setId_cts(rs.getInt("ses_id_cts")) ;  
							sesiondesempenio.setSesionTipo(sesiontipo);
					}
							return sesiondesempenio;
				}
				
				return null;
			}
			
		});


	}		
	
	public SesionDesempenio getByParams(Param param) {

		String sql = "select * from col_sesion_desempenio " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SesionDesempenio>() {
			@Override
			public SesionDesempenio extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<SesionDesempenio> listByParams(Param param, String[] order) {

		String sql = "select * from col_sesion_desempenio " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<SesionDesempenio>() {

			@Override
			public SesionDesempenio mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<SesionDesempenio> listFullByParams(SesionDesempenio sesiondesempenio, String[] order) {
	
		return listFullByParams(Param.toParam("csd",sesiondesempenio), order);
	
	}	
	
	public List<SesionDesempenio> listFullByParams(Param param, String[] order) {

		String sql = "select csd.id csd_id, csd.id_cde csd_id_cde , csd.id_ses csd_id_ses  ,csd.est csd_est ";
		sql = sql + ", cde.id cde_id  , cde.nom cde_nom , cde.id_cgc cde_id_cgc  ";
		sql = sql + ", ses.id ses_id  , ses.id_uns ses_id_uns , ses.id_cts ses_id_cts  ";
		sql = sql + " from col_sesion_desempenio csd";
		sql = sql + " left join col_desempenio cde on cde.id = csd.id_cde ";
		sql = sql + " left join col_sesion_tipo ses on ses.id = csd.id_ses ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<SesionDesempenio>() {

			@Override
			public SesionDesempenio mapRow(ResultSet rs, int rowNum) throws SQLException {
				SesionDesempenio sesiondesempenio= rsToEntity(rs,"csd_");
				Desempenio desempenio = new Desempenio();  
				desempenio.setId(rs.getInt("cde_id")) ;  
				desempenio.setNom(rs.getString("cde_nom")) ;  
				desempenio.setId_cgc(rs.getInt("cde_id_cgc")) ;  
				sesiondesempenio.setDesempenio(desempenio);
				SesionTipo sesiontipo = new SesionTipo();  
				sesiontipo.setId(rs.getInt("ses_id")) ;  
				sesiontipo.setId_uns(rs.getInt("ses_id_uns")) ;  
				sesiontipo.setId_cts(rs.getInt("ses_id_cts")) ;  
				sesiondesempenio.setSesionTipo(sesiontipo);
				return sesiondesempenio;
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
				indicador.setId_csd(rs.getInt("id_csd"));
				indicador.setEst(rs.getString("est"));
												
				return indicador;
			}

		});	
	}


	// funciones privadas utilitarias para SesionDesempenio

	private SesionDesempenio rsToEntity(ResultSet rs,String alias) throws SQLException {
		SesionDesempenio sesion_desempenio = new SesionDesempenio();

		sesion_desempenio.setId(rs.getInt( alias + "id"));
		sesion_desempenio.setId_cde(rs.getInt( alias + "id_cde"));
		sesion_desempenio.setId_ses(rs.getInt( alias + "id_ses"));
		sesion_desempenio.setEst(rs.getString( alias + "est"));
								
		return sesion_desempenio;

	}
	
}

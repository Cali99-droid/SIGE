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
import com.tesla.colegio.model.SesionIndicador;

import com.tesla.colegio.model.UnidadSesion;
import com.tesla.colegio.model.Indicador;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface SesionIndicadorDAO.
 * @author MV
 *
 */
public class SesionIndicadorDAOImpl{
	final static Logger logger = Logger.getLogger(SesionIndicadorDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(SesionIndicador sesion_indicador) {
		if (sesion_indicador.getId() != null) {
			// update
			String sql = "UPDATE col_sesion_indicador "
						+ "SET id_ses=?, "
						+ "id_ind=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						sesion_indicador.getId_ses(),
						sesion_indicador.getId_ind(),
						sesion_indicador.getEst(),
						sesion_indicador.getUsr_act(),
						new java.util.Date(),
						sesion_indicador.getId()); 
			return sesion_indicador.getId();

		} else {
			// insert
			String sql = "insert into col_sesion_indicador ("
						+ "id_ses, "
						+ "id_ind, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				sesion_indicador.getId_ses(),
				sesion_indicador.getId_ind(),
				sesion_indicador.getEst(),
				sesion_indicador.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_sesion_indicador where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<SesionIndicador> list() {
		String sql = "select * from col_sesion_indicador";
		
		//logger.info(sql);
		
		List<SesionIndicador> listSesionIndicador = jdbcTemplate.query(sql, new RowMapper<SesionIndicador>() {

			@Override
			public SesionIndicador mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listSesionIndicador;
	}

	public SesionIndicador get(int id) {
		String sql = "select * from col_sesion_indicador WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SesionIndicador>() {

			@Override
			public SesionIndicador extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public SesionIndicador getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select csi.id csi_id, csi.id_ses csi_id_ses , csi.id_ind csi_id_ind  ,csi.est csi_est ";
		if (aTablas.contains("col_unidad_sesion"))
			sql = sql + ", ses.id ses_id  , ses.id_uni ses_id_uni , ses.tit ses_tit , ses.dur ses_dur , ses.tipo ses_tipo  ";
		if (aTablas.contains("col_indicador"))
			sql = sql + ", ind.id ind_id  , ind.id_cap ind_id_cap , ind.nom ind_nom  ";
	
		sql = sql + " from col_sesion_indicador csi "; 
		if (aTablas.contains("col_unidad_sesion"))
			sql = sql + " left join col_unidad_sesion ses on ses.id = csi.id_ses ";
		if (aTablas.contains("col_indicador"))
			sql = sql + " left join col_indicador ind on ind.id = csi.id_ind ";
		sql = sql + " where csi.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<SesionIndicador>() {
		
			@Override
			public SesionIndicador extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					SesionIndicador sesionindicador= rsToEntity(rs,"csi_");
					if (aTablas.contains("col_unidad_sesion")){
						UnidadSesion unidadsesion = new UnidadSesion();  
							unidadsesion.setId(rs.getInt("ses_id")) ;  
							unidadsesion.setId_uni(rs.getInt("ses_id_uni")) ;  
							//unidadsesion.setTit(rs.getString("ses_tit")) ;  
							//unidadsesion.setDur(rs.getBigDecimal("ses_dur")) ;  
							//unidadsesion.setTipo(rs.getString("ses_tipo")) ;  
							sesionindicador.setUnidadSesion(unidadsesion);
					}
					if (aTablas.contains("col_indicador")){
						Indicador indicador = new Indicador();  
							indicador.setId(rs.getInt("ind_id")) ;  
							indicador.setNom(rs.getString("ind_nom")) ;  
							sesionindicador.setIndicador(indicador);
					}
							return sesionindicador;
				}
				
				return null;
			}
			
		});


	}		
	
	public SesionIndicador getByParams(Param param) {

		String sql = "select * from col_sesion_indicador " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SesionIndicador>() {
			@Override
			public SesionIndicador extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<SesionIndicador> listByParams(Param param, String[] order) {

		String sql = "select * from col_sesion_indicador " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<SesionIndicador>() {

			@Override
			public SesionIndicador mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<SesionIndicador> listFullByParams(SesionIndicador sesionindicador, String[] order) {
	
		return listFullByParams(Param.toParam("csi",sesionindicador), order);
	
	}	
	
	public List<SesionIndicador> listFullByParams(Param param, String[] order) {

		String sql = "select csi.id csi_id, csi.id_ses csi_id_ses , csi.id_ind csi_id_ind  ,csi.est csi_est ";
		sql = sql + ", ses.id ses_id  , ses.id_uni ses_id_uni , ses.tit ses_tit , ses.dur ses_dur , ses.tipo ses_tipo  ";
		sql = sql + ", ind.id ind_id  , ind.id_cap ind_id_cap , ind.nom ind_nom  ";
		sql = sql + " from col_sesion_indicador csi";
		sql = sql + " left join col_unidad_sesion ses on ses.id = csi.id_ses ";
		sql = sql + " left join col_indicador ind on ind.id = csi.id_ind ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<SesionIndicador>() {

			@Override
			public SesionIndicador mapRow(ResultSet rs, int rowNum) throws SQLException {
				SesionIndicador sesionindicador= rsToEntity(rs,"csi_");
				UnidadSesion unidadsesion = new UnidadSesion();  
				unidadsesion.setId(rs.getInt("ses_id")) ;  
				unidadsesion.setId_uni(rs.getInt("ses_id_uni")) ;  
				//unidadsesion.setTit(rs.getString("ses_tit")) ;  
				//unidadsesion.setDur(rs.getBigDecimal("ses_dur")) ;  
				//unidadsesion.setTipo(rs.getString("ses_tipo")) ;  
				sesionindicador.setUnidadSesion(unidadsesion);
				Indicador indicador = new Indicador();  
				indicador.setId(rs.getInt("ind_id")) ;   
				indicador.setNom(rs.getString("ind_nom")) ;  
				sesionindicador.setIndicador(indicador);
				return sesionindicador;
			}

		});

	}	




	// funciones privadas utilitarias para SesionIndicador

	private SesionIndicador rsToEntity(ResultSet rs,String alias) throws SQLException {
		SesionIndicador sesion_indicador = new SesionIndicador();

		sesion_indicador.setId(rs.getInt( alias + "id"));
		sesion_indicador.setId_ses(rs.getInt( alias + "id_ses"));
		sesion_indicador.setId_ind(rs.getInt( alias + "id_ind"));
		sesion_indicador.setEst(rs.getString( alias + "est"));
								
		return sesion_indicador;

	}
	
}

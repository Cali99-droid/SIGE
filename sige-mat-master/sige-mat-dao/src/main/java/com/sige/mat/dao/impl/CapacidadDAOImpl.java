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
import com.tesla.colegio.model.Capacidad;

import com.tesla.colegio.model.Competencia;
import com.tesla.colegio.model.Indicador;
import com.tesla.colegio.model.SubtemaCapacidad;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CapacidadDAO.
 * @author MV
 *
 */
public class CapacidadDAOImpl{
	
	final static Logger logger = Logger.getLogger(CapacidadDAOImpl.class);
	
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Capacidad capacidad) {
		if (capacidad.getId() != null) {
			// update
			String sql = "UPDATE col_capacidad "
						+ "SET id_com=?, "
						+ "nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						capacidad.getId_com(),
						capacidad.getNom(),
						capacidad.getEst(),
						capacidad.getUsr_act(),
						new java.util.Date(),
						capacidad.getId()); 
			return capacidad.getId();

		} else {
			// insert
			String sql = "insert into col_capacidad ("
						+ "id_com, "
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				capacidad.getId_com(),
				capacidad.getNom(),
				capacidad.getEst(),
				capacidad.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_capacidad where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Capacidad> list() {
		String sql = "select * from col_capacidad";
		
		//logger.info(sql);
		
		List<Capacidad> listCapacidad = jdbcTemplate.query(sql, new RowMapper<Capacidad>() {

			@Override
			public Capacidad mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCapacidad;
	}

	public Capacidad get(int id) {
		String sql = "select * from col_capacidad WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Capacidad>() {

			@Override
			public Capacidad extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Capacidad getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cap.id cap_id, cap.id_com cap_id_com , cap.nom cap_nom  ,cap.est cap_est ";
		if (aTablas.contains("col_competencia"))
			sql = sql + ", com.id com_id  , com.id_niv com_id_niv , com.id_cur com_id_cur , com.nom com_nom , com.peso com_peso , com.ord com_ord  ";
	
		sql = sql + " from col_capacidad cap "; 
		if (aTablas.contains("col_competencia"))
			sql = sql + " left join col_competencia com on com.id = cap.id_com ";
		sql = sql + " where cap.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Capacidad>() {
		
			@Override
			public Capacidad extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Capacidad capacidad= rsToEntity(rs,"cap_");
					if (aTablas.contains("col_competencia")){
						Competencia competencia = new Competencia();  
							competencia.setId(rs.getInt("com_id")) ;  
							competencia.setId_niv(rs.getInt("com_id_niv")) ;  
							competencia.setId_cur(rs.getInt("com_id_cur")) ;  
							competencia.setNom(rs.getString("com_nom")) ;  
							competencia.setPeso(rs.getString("com_peso")) ;  
							competencia.setOrd(rs.getInt("com_ord")) ;  
							capacidad.setCompetencia(competencia);
					}
							return capacidad;
				}
				
				return null;
			}
			
		});


	}		
	
	public Capacidad getByParams(Param param) {

		String sql = "select * from col_capacidad " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Capacidad>() {
			@Override
			public Capacidad extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Capacidad> listByParams(Param param, String[] order) {

		String sql = "select * from col_capacidad " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Capacidad>() {

			@Override
			public Capacidad mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	/*public List<Capacidad> listFullByParams(Capacidad capacidad, String[] order) {
	
		return listFullByParams(Param.toParam("cap",capacidad), order);
	
	}	*/
	
	public List<Capacidad> listFullByParams(Param param, String[] order) {

		String sql = "select cap.id cap_id, cap.id_com cap_id_com , cap.nom cap_nom  ,cap.est cap_est ";
		sql = sql + ", com.id com_id  , com.id_niv com_id_niv , com.id_cur com_id_cur , com.nom com_nom , com.peso com_peso , com.ord com_ord  ";
		sql = sql + " from col_capacidad cap";
		sql = sql + " left join col_competencia com on com.id = cap.id_com ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Capacidad>() {

			@Override
			public Capacidad mapRow(ResultSet rs, int rowNum) throws SQLException {
				Capacidad capacidad= rsToEntity(rs,"cap_");
				Competencia competencia = new Competencia();  
				competencia.setId(rs.getInt("com_id")) ;  
				competencia.setId_niv(rs.getInt("com_id_niv")) ;  
				competencia.setId_cur(rs.getInt("com_id_cur")) ;  
				competencia.setNom(rs.getString("com_nom")) ;  
				competencia.setPeso(rs.getString("com_peso")) ;  
				competencia.setOrd(rs.getInt("com_ord")) ;  
				capacidad.setCompetencia(competencia);
				return capacidad;
			}

		});

	}	


	public List<Indicador> getListIndicador(Param param, String[] order) {
		String sql = "select * from col_indicador " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Indicador>() {

			@Override
			public Indicador mapRow(ResultSet rs, int rowNum) throws SQLException {
				Indicador indicador = new Indicador();

				indicador.setId(rs.getInt("id"));
				indicador.setNom(rs.getString("nom"));
				indicador.setEst(rs.getString("est"));
												
				return indicador;
			}

		});	
	}
	public List<SubtemaCapacidad> getListSubtemaCapacidad(Param param, String[] order) {
		String sql = "select * from col_subtema_capacidad " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<SubtemaCapacidad>() {

			@Override
			public SubtemaCapacidad mapRow(ResultSet rs, int rowNum) throws SQLException {
				SubtemaCapacidad subtema_capacidad = new SubtemaCapacidad();

				subtema_capacidad.setId(rs.getInt("id"));
				subtema_capacidad.setId_ccs(rs.getInt("id_ccs"));
				subtema_capacidad.setId_cap(rs.getInt("id_cap"));
				subtema_capacidad.setEst(rs.getString("est"));
												
				return subtema_capacidad;
			}

		});	
	}


	// funciones privadas utilitarias para Capacidad

	private Capacidad rsToEntity(ResultSet rs,String alias) throws SQLException {
		Capacidad capacidad = new Capacidad();

		capacidad.setId(rs.getInt( alias + "id"));
		capacidad.setId_com(rs.getInt( alias + "id_com"));
		capacidad.setNom(rs.getString( alias + "nom"));
		capacidad.setEst(rs.getString( alias + "est"));
								
		return capacidad;

	}
	
}

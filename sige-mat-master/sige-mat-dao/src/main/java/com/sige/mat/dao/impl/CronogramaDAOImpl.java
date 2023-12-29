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
import com.tesla.colegio.model.Cronograma;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.Anio;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CronogramaDAO.
 * @author MV
 *
 */
public class CronogramaDAOImpl{
	final static Logger logger = Logger.getLogger(CronogramaDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@Autowired
	private TokenSeguridad tokenSeguridad;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Cronograma cronograma) {
		if (cronograma.getId() != null) {
			// update
			String sql = "UPDATE mat_cronograma "
						+ "SET id_anio=?, "
						+ "fec_mat=?, "
						+ "del=?, "
						+ "al=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						cronograma.getId_anio(),
						cronograma.getFec_mat(),
						cronograma.getDel(),
						cronograma.getAl(),
						cronograma.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						cronograma.getId()
						); 
			return cronograma.getId();

		} else {
			// insert
			String sql = "insert into mat_cronograma ("
						+ "id_anio, "
						+ "fec_mat, "
						+ "tipo, "
						+ "del, "
						+ "al, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?,?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				cronograma.getId_anio(),
				cronograma.getFec_mat(),
				cronograma.getTipo(),
				cronograma.getDel(),
				cronograma.getAl(),
				cronograma.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from mat_cronograma where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Cronograma> list() {
		String sql = "select * from mat_cronograma";
		
		//logger.info(sql);
		
		List<Cronograma> listCronograma = jdbcTemplate.query(sql, new RowMapper<Cronograma>() {

			@Override
			public Cronograma mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCronograma;
	}

	public Cronograma get(int id) {
		String sql = "select * from mat_cronograma WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Cronograma>() {

			@Override
			public Cronograma extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Cronograma getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select mat_cro.id mat_cro_id, mat_cro.id_anio mat_cro_id_anio , mat_cro.fec_mat mat_cro_fec_mat ,  mat_cro.tipo mat_cro_tipo , mat_cro.del mat_cro_del , mat_cro.al mat_cro_al  ,mat_cro.est mat_cro_est ";
		if (aTablas.contains("col_anio"))
			sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
	
		sql = sql + " from mat_cronograma mat_cro "; 
		if (aTablas.contains("col_anio"))
			sql = sql + " left join col_anio anio on anio.id = mat_cro.id_anio ";
		sql = sql + " where mat_cro.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Cronograma>() {
		
			@Override
			public Cronograma extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Cronograma cronograma= rsToEntity(rs,"mat_cro_");
					if (aTablas.contains("col_anio")){
						Anio anio = new Anio();  
							anio.setId(rs.getInt("anio_id")) ;  
							anio.setNom(rs.getString("anio_nom")) ;  
							cronograma.setAnio(anio);
					}


							return cronograma;
				}
				
				return null;
			}
			
		});


	}		
	
	public Cronograma getByParams(Param param) {

		String sql = "select * from mat_cronograma " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Cronograma>() {
			@Override
			public Cronograma extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Cronograma> listByParams(Param param, String[] order) {

		String sql = "select * from mat_cronograma " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Cronograma>() {

			@Override
			public Cronograma mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Cronograma> listFullByParams(Cronograma cronograma, String[] order) {
	
		return listFullByParams(Param.toParam("mat_cro",cronograma), order);
	
	}	
	
	public List<Cronograma> listFullByParams(Param param, String[] order) {

		String sql = "select mat_cro.id mat_cro_id, mat_cro.id_anio mat_cro_id_anio , mat_cro.fec_mat mat_cro_fec_mat ,  mat_cro.tipo mat_cro_tipo , mat_cro.del mat_cro_del , mat_cro.al mat_cro_al  ,mat_cro.est mat_cro_est ";
		sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		sql = sql + " from mat_cronograma mat_cro";
		sql = sql + " left join col_anio anio on anio.id = mat_cro.id_anio ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Cronograma>() {

			@Override
			public Cronograma mapRow(ResultSet rs, int rowNum) throws SQLException {
				Cronograma cronograma= rsToEntity(rs,"mat_cro_");
				Anio anio = new Anio();  
				anio.setId(rs.getInt("anio_id")) ;  
				anio.setNom(rs.getString("anio_nom")) ;  
				cronograma.setAnio(anio);
				return cronograma;
			}

		});

	}	




	// funciones privadas utilitarias para Cronograma

	private Cronograma rsToEntity(ResultSet rs,String alias) throws SQLException {
		Cronograma cronograma = new Cronograma();

		cronograma.setId(rs.getInt( alias + "id"));
		cronograma.setId_anio(rs.getInt( alias + "id_anio"));
		cronograma.setFec_mat(rs.getDate( alias + "fec_mat"));
		cronograma.setTipo(rs.getString( alias + "tipo"));
		cronograma.setDel(rs.getString( alias + "del"));
		cronograma.setAl(rs.getString( alias + "al"));
		cronograma.setEst(rs.getString( alias + "est"));
								
		return cronograma;

	}
	
}

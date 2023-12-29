package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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

import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.LecturaBarras;



import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface LecturaBarrasDAO.
 * @author MV
 *
 */
public class LecturaBarrasDAOImpl{
	final static Logger logger = Logger.getLogger(LecturaBarrasDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(LecturaBarras lectura_barras) {
		if (lectura_barras.getId() != null) {
			// update
			String sql = "UPDATE asi_lectura_barras "
						+ "SET "
						+ "fecha_ori=?, "
						+ "fecha=?, "
						+ "fec_act=? "
						+ "WHERE id=?";
			
			////logger.info(sql);

			jdbcTemplate.update(sql, 

						lectura_barras.getFecha_ori(),
						lectura_barras.getFecha(),
						new java.util.Date(),
						lectura_barras.getId()); 
			return lectura_barras.getId();

		} else {
			// insert
			String sql = "insert into asi_lectura_barras ("
						+ "codigo, "
						+ "id_per, "
						+ "fecha_ori, "
						+ "fecha, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				////logger.info(sql);

				jdbcTemplate.update(sql, 
				lectura_barras.getCodigo(),
				lectura_barras.getId_per(),
				lectura_barras.getFecha_ori(),
				lectura_barras.getFecha(),
				lectura_barras.getEst(),
				lectura_barras.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}
	
	public int save(LecturaBarras lectura_barras) {
			String sql = "insert into asi_lectura_barras ("
						+ "codigo, "
						+ "asistencia, "
						+ "id_per, "
						+ "fecha_ori, "
						+ "fecha, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

				////logger.info(sql);

				jdbcTemplate.update(sql, 
				lectura_barras.getCodigo(),
				lectura_barras.getAsistencia(),
				lectura_barras.getId_per(),
				lectura_barras.getFecha_ori(),
				lectura_barras.getFecha(),
				lectura_barras.getEst(),
				lectura_barras.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		
	}
	
	public int update(LecturaBarras lecturaBarras) {
		// update
		/*String sql = "UPDATE asi_lectura_barras "
					+ "SET "
					+ "fecha_ori=?, "
					+ "asistencia=?, "
					+ "usr_act=?,fec_act=? "
				+ "WHERE id=?";
		
		//logger.info(sql);

		return jdbcTemplate.update(sql, 
				lecturaBarras.getFecha_ori(),
				lecturaBarras.getAsistencia(),
				lecturaBarras.getUsr_act(),
				new java.util.Date(),
				lecturaBarras.getId()); */
		
		String sql = "UPDATE asi_lectura_barras "
				+ "SET "
				+ "asistencia=?, "
				+ "usr_act=?,fec_act=?, observacion=? "
			+ "WHERE id=?";
	
	//logger.info(sql);

	return jdbcTemplate.update(sql, 
			lecturaBarras.getAsistencia(),
			lecturaBarras.getUsr_act(),
			new java.util.Date(),
			lecturaBarras.getObservacion(),
			lecturaBarras.getId()); 


	} 

	public void delete(int id) {
		String sql = "delete from asi_lectura_barras where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<LecturaBarras> list() {
		String sql = "select * from asi_lectura_barras";
		
		//logger.info(sql);
		
		List<LecturaBarras> listLecturaBarras = jdbcTemplate.query(sql, new RowMapper<LecturaBarras>() {

			@Override
			public LecturaBarras mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listLecturaBarras;
	}

	public LecturaBarras get(int id) {
		String sql = "select * from asi_lectura_barras WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<LecturaBarras>() {

			@Override
			public LecturaBarras extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public LecturaBarras getFull(int id, String tablas[]) {
		String sql = "select alb.id alb_id, alb.codigo alb_codigo , alb.id_per alb_id_per , alb.fecha_ori alb_fecha_ori , alb.fecha alb_fecha  ,alb.est alb_est ";
	
		sql = sql + " from asi_lectura_barras alb "; 
		sql = sql + " where alb.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<LecturaBarras>() {
		
			@Override
			public LecturaBarras extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					LecturaBarras lecturabarras= rsToEntity(rs,"alb_");
							return lecturabarras;
				}
				
				return null;
			}
			
		});


	}		
	
	public LecturaBarras getByParams(Param param) {

		String sql = "select * from asi_lectura_barras " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<LecturaBarras>() {
			@Override
			public LecturaBarras extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<LecturaBarras> listByParams(Param param, String[] order) {

		String sql = "select * from asi_lectura_barras " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<LecturaBarras>() {

			@Override
			public LecturaBarras mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<LecturaBarras> listFullByParams(LecturaBarras lecturabarras, String[] order) {
	
		return listFullByParams(Param.toParam("alb",lecturabarras), order);
	
	}	
	
	public List<LecturaBarras> listFullByParams(Param param, String[] order) {

		String sql = "select alb.id alb_id, alb.codigo alb_codigo , alb.id_per alb_id_per , alb.fecha_ori alb_fecha_ori , alb.fecha alb_fecha  ,alb.est alb_est ";
		sql = sql + " from asi_lectura_barras alb";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<LecturaBarras>() {

			@Override
			public LecturaBarras mapRow(ResultSet rs, int rowNum) throws SQLException {
				LecturaBarras lecturabarras= rsToEntity(rs,"alb_");
				return lecturabarras;
			}

		});

	}	




	// funciones privadas utilitarias para LecturaBarras

	private LecturaBarras rsToEntity(ResultSet rs,String alias) throws SQLException {
		LecturaBarras lectura_barras = new LecturaBarras();

		lectura_barras.setId(rs.getInt( alias + "id"));
		lectura_barras.setCodigo(rs.getString( alias + "codigo"));
		lectura_barras.setId_per(rs.getInt( alias + "id_per"));
		lectura_barras.setFecha_ori(rs.getString( alias + "fecha_ori"));
		lectura_barras.setFecha(rs.getDate( alias + "fecha"));
		lectura_barras.setObservacion(rs.getString(alias+ "observacion"));
		lectura_barras.setEst(rs.getString( alias + "est"));
								
		return lectura_barras;

	}
	
}

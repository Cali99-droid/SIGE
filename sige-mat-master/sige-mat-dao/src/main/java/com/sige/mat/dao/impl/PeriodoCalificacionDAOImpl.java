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
import com.tesla.colegio.model.PerUni;
import com.tesla.colegio.model.PeriodoAca;
import com.tesla.colegio.model.PeriodoCalificacion;
import com.tesla.colegio.model.PerAcaNivel;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.CursoUnidad;
import com.tesla.colegio.model.Nivel;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface PerUniDAO.
 * @author MV
 *
 */
public class PeriodoCalificacionDAOImpl{
	final static Logger logger = Logger.getLogger(PeriodoCalificacionDAOImpl.class);
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
	public int saveOrUpdate(PeriodoCalificacion periodoCalificacion) {
		if (periodoCalificacion.getId() != null) {
			// update
			String sql = "UPDATE col_periodo_calificacion "
						+ "SET id_gra=?, "
						+ "id_cpu=?, "
						+ "id_anio=?, "
						+ "id_tca=?, "
						+ "letra=?, "
						+ "nota_ini=?, "
						+ "nota_fin=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
					periodoCalificacion.getId_gra(),
					periodoCalificacion.getId_cpu(),
					periodoCalificacion.getId_anio(),
					periodoCalificacion.getId_tca(),
					periodoCalificacion.getLetra(),
					periodoCalificacion.getNota_ini(),
					periodoCalificacion.getNota_fin(),
					periodoCalificacion.getEst(),
					tokenSeguridad.getId(),
					new java.util.Date(),
					periodoCalificacion.getId()); 
			return periodoCalificacion.getId();

		} else {
			// insert
			String sql = "insert into col_periodo_calificacion ("
						+ "id_gra, "
						+ "id_cpu, "
						+ "id_anio, "
						+ "id_tca, "
						+ "letra, "
						+ "nota_ini, "
						+ "nota_fin, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ? , ? , ? , ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
						periodoCalificacion.getId_gra(),
						periodoCalificacion.getId_cpu(),
						periodoCalificacion.getId_anio(),
						periodoCalificacion.getId_tca(),
						periodoCalificacion.getLetra(),
						periodoCalificacion.getNota_ini(),
						periodoCalificacion.getNota_fin(),
						periodoCalificacion.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_periodo_calificacion where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<PeriodoCalificacion> list() {
		String sql = "select * from col_periodo_calificacion";
		
		//logger.info(sql);
		
		List<PeriodoCalificacion> listPeriodoCalificacion = jdbcTemplate.query(sql, new RowMapper<PeriodoCalificacion>() {

			@Override
			public PeriodoCalificacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listPeriodoCalificacion;
	}

	public PeriodoCalificacion get(int id) {
		String sql = "select * from col_periodo_calificacion WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PeriodoCalificacion>() {

			@Override
			public PeriodoCalificacion extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}
	
	public PeriodoCalificacion getByParams(Param param) {

		String sql = "select * from col_periodo_calificacion " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PeriodoCalificacion>() {
			@Override
			public PeriodoCalificacion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<PeriodoCalificacion> listByParams(Param param, String[] order) {

		String sql = "select * from col_periodo_calificacion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<PeriodoCalificacion>() {

			@Override
			public PeriodoCalificacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	
	

	// funciones privadas utilitarias para PerUni

	private PeriodoCalificacion rsToEntity(ResultSet rs,String alias) throws SQLException {
		PeriodoCalificacion periodoCalificacion = new PeriodoCalificacion();

		periodoCalificacion.setId(rs.getInt( alias + "id"));
		periodoCalificacion.setId_gra(rs.getInt( alias + "id_gra"));
		periodoCalificacion.setId_cpu(rs.getInt( alias + "id_cpu"));
		periodoCalificacion.setId_anio(rs.getInt( alias + "id_anio"));
		periodoCalificacion.setId_tca(rs.getInt( alias + "id_tca"));
		periodoCalificacion.setLetra(rs.getString( alias + "letra"));
		periodoCalificacion.setNota_ini(rs.getInt( alias + "nota_ini"));
		periodoCalificacion.setNota_fin(rs.getInt( alias + "nota_fin"));
		periodoCalificacion.setEst(rs.getString( alias + "est"));
		return periodoCalificacion;

	}
	
}

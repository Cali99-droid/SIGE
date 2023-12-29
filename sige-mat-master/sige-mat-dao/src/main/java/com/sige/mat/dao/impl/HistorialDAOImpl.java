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
import com.tesla.colegio.model.Historial;

import com.tesla.colegio.model.EstadoMensajeria;
import com.tesla.colegio.model.Receptor;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface HistorialDAO.
 * @author MV
 *
 */
public class HistorialDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Historial historial) {
		if (historial.getId() != null) {
			// update
			String sql = "UPDATE msj_historial "
						+ "SET id_est=?, "
						+ "id_rec=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						historial.getId_est(),
						historial.getId_rec(),
						historial.getEst(),
						historial.getUsr_act(),
						new java.util.Date(),
						historial.getId()); 
			return historial.getId();

		} else {
			// insert
			String sql = "insert into msj_historial ("
						+ "id_est, "
						+ "id_rec, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				historial.getId_est(),
				historial.getId_rec(),
				historial.getEst(),
				historial.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from msj_historial where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Historial> list() {
		String sql = "select * from msj_historial";
		
		//System.out.println(sql);
		
		List<Historial> listHistorial = jdbcTemplate.query(sql, new RowMapper<Historial>() {

			@Override
			public Historial mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listHistorial;
	}

	public Historial get(int id) {
		String sql = "select * from msj_historial WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Historial>() {

			@Override
			public Historial extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Historial getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select hist.id hist_id, hist.id_est hist_id_est , hist.id_rec hist_id_rec  ,hist.est hist_est ";
		if (aTablas.contains("msj_estado_mensajeria"))
			sql = sql + ", est.id est_id  , est.des est_des  ";
		if (aTablas.contains("msj_receptor"))
			sql = sql + ", rec.id rec_id  , rec.id_usr rec_id_usr , rec.id_per rec_id_per , rec.id_est rec_id_est  ";
	
		sql = sql + " from msj_historial hist "; 
		if (aTablas.contains("msj_estado_mensajeria"))
			sql = sql + " left join msj_estado_mensajeria est on est.id = hist.id_est ";
		if (aTablas.contains("msj_receptor"))
			sql = sql + " left join msj_receptor rec on rec.id = hist.id_rec ";
		sql = sql + " where hist.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Historial>() {
		
			@Override
			public Historial extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Historial historial= rsToEntity(rs,"hist_");
					if (aTablas.contains("msj_estado_mensajeria")){
						EstadoMensajeria estadomensajeria = new EstadoMensajeria();  
							estadomensajeria.setId(rs.getInt("est_id")) ;  
							estadomensajeria.setDes(rs.getString("est_des")) ;  
							historial.setEstadoMensajeria(estadomensajeria);
					}
					if (aTablas.contains("msj_receptor")){
						Receptor receptor = new Receptor();  
							receptor.setId(rs.getInt("rec_id")) ;  
							receptor.setId_usr(rs.getInt("rec_id_usr")) ;  
							receptor.setId_per(rs.getInt("rec_id_per")) ;  
							receptor.setId_est(rs.getInt("rec_id_est")) ;  
							historial.setReceptor(receptor);
					}
							return historial;
				}
				
				return null;
			}
			
		});


	}		
	
	public Historial getByParams(Param param) {

		String sql = "select * from msj_historial " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Historial>() {
			@Override
			public Historial extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Historial> listByParams(Param param, String[] order) {

		String sql = "select * from msj_historial " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Historial>() {

			@Override
			public Historial mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Historial> listFullByParams(Historial historial, String[] order) {
	
		return listFullByParams(Param.toParam("hist",historial), order);
	
	}	
	
	public List<Historial> listFullByParams(Param param, String[] order) {

		String sql = "select hist.id hist_id, hist.id_est hist_id_est , hist.id_rec hist_id_rec  ,hist.est hist_est ";
		sql = sql + ", est.id est_id  , est.des est_des  ";
		sql = sql + ", rec.id rec_id  , rec.id_usr rec_id_usr , rec.id_per rec_id_per , rec.id_est rec_id_est  ";
		sql = sql + " from msj_historial hist";
		sql = sql + " left join msj_estado_mensajeria est on est.id = hist.id_est ";
		sql = sql + " left join msj_receptor rec on rec.id = hist.id_rec ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Historial>() {

			@Override
			public Historial mapRow(ResultSet rs, int rowNum) throws SQLException {
				Historial historial= rsToEntity(rs,"hist_");
				EstadoMensajeria estadomensajeria = new EstadoMensajeria();  
				estadomensajeria.setId(rs.getInt("est_id")) ;  
				estadomensajeria.setDes(rs.getString("est_des")) ;  
				historial.setEstadoMensajeria(estadomensajeria);
				Receptor receptor = new Receptor();  
				receptor.setId(rs.getInt("rec_id")) ;  
				receptor.setId_usr(rs.getInt("rec_id_usr")) ;  
				receptor.setId_per(rs.getInt("rec_id_per")) ;  
				receptor.setId_est(rs.getInt("rec_id_est")) ;  
				historial.setReceptor(receptor);
				return historial;
			}

		});

	}	




	// funciones privadas utilitarias para Historial

	private Historial rsToEntity(ResultSet rs,String alias) throws SQLException {
		Historial historial = new Historial();

		historial.setId(rs.getInt( alias + "id"));
		historial.setId_est(rs.getInt( alias + "id_est"));
		historial.setId_rec(rs.getInt( alias + "id_rec"));
		historial.setEst(rs.getString( alias + "est"));
								
		return historial;

	}
	
}

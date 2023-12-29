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
import com.tesla.colegio.model.EstadoMensajeria;

import com.tesla.colegio.model.Receptor;
import com.tesla.colegio.model.Historial;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface EstadoMensajeriaDAO.
 * @author MV
 *
 */
public class EstadoMensajeriaDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(EstadoMensajeria estado_mensajeria) {
		if (estado_mensajeria.getId() != null) {
			// update
			String sql = "UPDATE msj_estado_mensajeria "
						+ "SET des=?, "
						+ "cod=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						estado_mensajeria.getDes(),
						estado_mensajeria.getCod(),
						estado_mensajeria.getEst(),
						estado_mensajeria.getUsr_act(),
						new java.util.Date(),
						estado_mensajeria.getId()); 
			return estado_mensajeria.getId();

		} else {
			// insert
			String sql = "insert into msj_estado_mensajeria ("
						+ "des, "
						+ "cod, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				estado_mensajeria.getDes(),
				estado_mensajeria.getCod(),
				estado_mensajeria.getEst(),
				estado_mensajeria.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from msj_estado_mensajeria where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<EstadoMensajeria> list() {
		String sql = "select * from msj_estado_mensajeria";
		
		//System.out.println(sql);
		
		List<EstadoMensajeria> listEstadoMensajeria = jdbcTemplate.query(sql, new RowMapper<EstadoMensajeria>() {

			@Override
			public EstadoMensajeria mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listEstadoMensajeria;
	}

	public EstadoMensajeria get(int id) {
		String sql = "select * from msj_estado_mensajeria WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<EstadoMensajeria>() {

			@Override
			public EstadoMensajeria extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public EstadoMensajeria getFull(int id, String tablas[]) {
		String sql = "select est.id est_id, est.des est_des , est.cod est_cod  ,est.est est_est ";
	
		sql = sql + " from msj_estado_mensajeria est "; 
		sql = sql + " where est.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<EstadoMensajeria>() {
		
			@Override
			public EstadoMensajeria extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					EstadoMensajeria estadomensajeria= rsToEntity(rs,"est_");
							return estadomensajeria;
				}
				
				return null;
			}
			
		});


	}		
	
	public EstadoMensajeria getByParams(Param param) {

		String sql = "select * from msj_estado_mensajeria " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<EstadoMensajeria>() {
			@Override
			public EstadoMensajeria extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<EstadoMensajeria> listByParams(Param param, String[] order) {

		String sql = "select * from msj_estado_mensajeria " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<EstadoMensajeria>() {

			@Override
			public EstadoMensajeria mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<EstadoMensajeria> listFullByParams(EstadoMensajeria estadomensajeria, String[] order) {
	
		return listFullByParams(Param.toParam("est",estadomensajeria), order);
	
	}	
	
	public List<EstadoMensajeria> listFullByParams(Param param, String[] order) {

		String sql = "select est.id est_id, est.des est_des , est.cod est_cod  ,est.est est_est ";
		sql = sql + " from msj_estado_mensajeria est";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<EstadoMensajeria>() {

			@Override
			public EstadoMensajeria mapRow(ResultSet rs, int rowNum) throws SQLException {
				EstadoMensajeria estadomensajeria= rsToEntity(rs,"est_");
				return estadomensajeria;
			}

		});

	}	


	public List<Receptor> getListReceptor(Param param, String[] order) {
		String sql = "select * from msj_receptor " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Receptor>() {

			@Override
			public Receptor mapRow(ResultSet rs, int rowNum) throws SQLException {
				Receptor receptor = new Receptor();

				receptor.setId(rs.getInt("id"));
				receptor.setId_usr(rs.getInt("id_usr"));
				receptor.setId_per(rs.getInt("id_per"));
				receptor.setId_msj(rs.getInt("id_msj"));
				receptor.setId_est(rs.getInt("id_est"));
				receptor.setEst(rs.getString("est"));
												
				return receptor;
			}

		});	
	}
	public List<Historial> getListHistorial(Param param, String[] order) {
		String sql = "select * from msj_historial " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Historial>() {

			@Override
			public Historial mapRow(ResultSet rs, int rowNum) throws SQLException {
				Historial historial = new Historial();

				historial.setId(rs.getInt("id"));
				historial.setId_est(rs.getInt("id_est"));
				historial.setId_rec(rs.getInt("id_rec"));
				historial.setEst(rs.getString("est"));
												
				return historial;
			}

		});	
	}


	// funciones privadas utilitarias para EstadoMensajeria

	private EstadoMensajeria rsToEntity(ResultSet rs,String alias) throws SQLException {
		EstadoMensajeria estado_mensajeria = new EstadoMensajeria();

		estado_mensajeria.setId(rs.getInt( alias + "id"));
		estado_mensajeria.setDes(rs.getString( alias + "des"));
		estado_mensajeria.setCod(rs.getString( alias + "cod"));
		estado_mensajeria.setEst(rs.getString( alias + "est"));
								
		return estado_mensajeria;

	}
	
}

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
import com.tesla.colegio.model.Adjunto;

import com.tesla.colegio.model.Mensaje;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface AdjuntoDAO.
 * @author MV
 *
 */
public class AdjuntoDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Adjunto adjunto) {
		if (adjunto.getId() != null) {
			// update
			String sql = "UPDATE msj_adjunto "
						+ "SET archivo=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						adjunto.getArchivo(),
						adjunto.getEst(),
						adjunto.getUsr_act(),
						new java.util.Date(),
						adjunto.getId()); 
			return adjunto.getId();

		} else {
			// insert
			String sql = "insert into msj_adjunto ("
						+ "archivo, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				adjunto.getArchivo(),
				adjunto.getEst(),
				adjunto.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from msj_adjunto where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Adjunto> list() {
		String sql = "select * from msj_adjunto";
		
		//System.out.println(sql);
		
		List<Adjunto> listAdjunto = jdbcTemplate.query(sql, new RowMapper<Adjunto>() {

			@Override
			public Adjunto mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listAdjunto;
	}

	public Adjunto get(int id) {
		String sql = "select * from msj_adjunto WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Adjunto>() {

			@Override
			public Adjunto extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Adjunto getFull(int id, String tablas[]) {
		String sql = "select adj.id adj_id, adj.archivo adj_archivo  ,adj.est adj_est ";
	
		sql = sql + " from msj_adjunto adj "; 
		sql = sql + " where adj.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Adjunto>() {
		
			@Override
			public Adjunto extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Adjunto adjunto= rsToEntity(rs,"adj_");
							return adjunto;
				}
				
				return null;
			}
			
		});


	}		
	
	public Adjunto getByParams(Param param) {

		String sql = "select * from msj_adjunto " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Adjunto>() {
			@Override
			public Adjunto extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Adjunto> listByParams(Param param, String[] order) {

		String sql = "select * from msj_adjunto " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Adjunto>() {

			@Override
			public Adjunto mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Adjunto> listFullByParams(Adjunto adjunto, String[] order) {
	
		return listFullByParams(Param.toParam("adj",adjunto), order);
	
	}	
	
	public List<Adjunto> listFullByParams(Param param, String[] order) {

		String sql = "select adj.id adj_id, adj.archivo adj_archivo  ,adj.est adj_est ";
		sql = sql + " from msj_adjunto adj";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Adjunto>() {

			@Override
			public Adjunto mapRow(ResultSet rs, int rowNum) throws SQLException {
				Adjunto adjunto= rsToEntity(rs,"adj_");
				return adjunto;
			}

		});

	}	


	public List<Mensaje> getListMensaje(Param param, String[] order) {
		String sql = "select * from msj_mensaje " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Mensaje>() {

			@Override
			public Mensaje mapRow(ResultSet rs, int rowNum) throws SQLException {
				Mensaje mensaje = new Mensaje();

				mensaje.setId(rs.getInt("id"));
				mensaje.setId_emi(rs.getInt("id_emi"));
				mensaje.setId_adj(rs.getInt("id_adj"));
				mensaje.setDes(rs.getString("des"));
				mensaje.setFec_envio(rs.getDate("fec_envio"));
				mensaje.setEst(rs.getString("est"));
												
				return mensaje;
			}

		});	
	}


	// funciones privadas utilitarias para Adjunto

	private Adjunto rsToEntity(ResultSet rs,String alias) throws SQLException {
		Adjunto adjunto = new Adjunto();

		adjunto.setId(rs.getInt( alias + "id"));
		adjunto.setArchivo(rs.getString( alias + "archivo"));
		adjunto.setEst(rs.getString( alias + "est"));
								
		return adjunto;

	}
	
}

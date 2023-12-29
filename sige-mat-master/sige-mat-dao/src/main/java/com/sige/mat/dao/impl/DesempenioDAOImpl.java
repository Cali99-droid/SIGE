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
import com.tesla.colegio.model.Desempenio;

import com.tesla.colegio.model.Indicador;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface DesempenioDAO.
 * @author MV
 *
 */
public class DesempenioDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Desempenio desempenio) {
		if (desempenio.getId() != null) {
			// update
			String sql = "UPDATE col_desempenio "
						+ "SET nom=?, "
						+ "id_cgc=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						desempenio.getNom(),
						desempenio.getId_cgc(),
						desempenio.getEst(),
						desempenio.getUsr_act(),
						new java.util.Date(),
						desempenio.getId()); 
			return desempenio.getId();

		} else {
			// insert
			String sql = "insert into col_desempenio ("
						+ "nom, "
						+ "id_cgc, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				desempenio.getNom(),
				desempenio.getId_cgc(),
				desempenio.getEst(),
				desempenio.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_desempenio where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}
	
	public void deletexGrupoCapacidad(int id_cgc) {
		String sql = "delete from col_desempenio where id_cgc=?";
		
		
		
		jdbcTemplate.update(sql, id_cgc);
	}


	public List<Desempenio> list() {
		String sql = "select * from col_desempenio";
		
		
		
		List<Desempenio> listDesempenio = jdbcTemplate.query(sql, new RowMapper<Desempenio>() {

			@Override
			public Desempenio mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listDesempenio;
	}

	public Desempenio get(int id) {
		String sql = "select * from col_desempenio WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Desempenio>() {

			@Override
			public Desempenio extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	
	public Desempenio getByParams(Param param) {

		String sql = "select * from col_desempenio " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Desempenio>() {
			@Override
			public Desempenio extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Desempenio> listByParams(Param param, String[] order) {

		String sql = "select * from col_desempenio " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<Desempenio>() {

			@Override
			public Desempenio mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
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
				//indicador.setId_des(rs.getInt("id_des"));
				indicador.setEst(rs.getString("est"));
												
				return indicador;
			}

		});	
	}


	// funciones privadas utilitarias para Desempenio

	private Desempenio rsToEntity(ResultSet rs,String alias) throws SQLException {
		Desempenio desempenio = new Desempenio();

		desempenio.setId(rs.getInt( alias + "id"));
		desempenio.setNom(rs.getString( alias + "nom"));
		desempenio.setId_cgc(rs.getInt( alias + "id_cgc"));
		desempenio.setEst(rs.getString( alias + "est"));
								
		return desempenio;

	}
	
}

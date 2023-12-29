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
import com.tesla.colegio.model.Contador;


import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ContadorDAO.
 * @author MV
 *
 */
public class ContadorDAOImpl{
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
	public int saveOrUpdate(Contador contador) {
		if (contador.getId() != null) {
			// update
			String sql = "UPDATE msj_contador "
						+ "SET nro=?, "
						+ "fec=?, "
						+ "usr=?, "
						+ "psw=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						contador.getNro(),
						contador.getFec(),
						contador.getUsr(),
						contador.getPsw(),
						contador.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						contador.getId()); 
			return contador.getId();

		} else {
			// insert
			String sql = "insert into msj_contador ("
						+ "nro, "
						+ "fec, "
						+ "usr, "
						+ "psw, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				contador.getNro(),
				contador.getFec(),
				contador.getUsr(),
				contador.getPsw(),
				contador.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from msj_contador where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Contador> list() {
		String sql = "select * from msj_contador";
		
		System.out.println(sql);
		
		List<Contador> listContador = jdbcTemplate.query(sql, new RowMapper<Contador>() {

			@Override
			public Contador mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listContador;
	}

	public Contador get(int id) {
		String sql = "select * from msj_contador WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Contador>() {

			@Override
			public Contador extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Contador getFull(int id, String tablas[]) {
		String sql = "select mscon.id mscon_id, mscon.nro mscon_nro , mscon.fec mscon_fec , mscon.usr mscon_usr , mscon.psw mscon_psw  ,mscon.est mscon_est ";
	
		sql = sql + " from msj_contador mscon "; 
		sql = sql + " where mscon.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Contador>() {
		
			@Override
			public Contador extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Contador contador= rsToEntity(rs,"mscon_");
							return contador;
				}
				
				return null;
			}
			
		});


	}		
	
	public Contador getByParams(Param param) {

		String sql = "select * from msj_contador " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Contador>() {
			@Override
			public Contador extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Contador> listByParams(Param param, String[] order) {

		String sql = "select * from msj_contador " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Contador>() {

			@Override
			public Contador mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Contador> listFullByParams(Contador contador, String[] order) {
	
		return listFullByParams(Param.toParam("mscon",contador), order);
	
	}	
	
	public List<Contador> listFullByParams(Param param, String[] order) {

		String sql = "select mscon.id mscon_id, mscon.nro mscon_nro , mscon.fec mscon_fec , mscon.usr mscon_usr , mscon.psw mscon_psw  ,mscon.est mscon_est ";
		sql = sql + " from msj_contador mscon";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Contador>() {

			@Override
			public Contador mapRow(ResultSet rs, int rowNum) throws SQLException {
				Contador contador= rsToEntity(rs,"mscon_");
				return contador;
			}

		});

	}	




	// funciones privadas utilitarias para Contador

	private Contador rsToEntity(ResultSet rs,String alias) throws SQLException {
		Contador contador = new Contador();

		contador.setId(rs.getInt( alias + "id"));
		contador.setNro(rs.getInt( alias + "nro"));
		contador.setFec(rs.getDate( alias + "fec"));
		contador.setUsr(rs.getString( alias + "usr"));
		contador.setPsw(rs.getString( alias + "psw"));
		contador.setEst(rs.getString( alias + "est"));
								
		return contador;

	}
	
}

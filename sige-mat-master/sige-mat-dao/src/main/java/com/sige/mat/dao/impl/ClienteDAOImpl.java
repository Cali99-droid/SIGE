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
import com.tesla.colegio.model.Cliente;

import com.tesla.colegio.model.Matricula;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ClienteDAO.
 * @author MV
 *
 */
public class ClienteDAOImpl{
	final static Logger logger = Logger.getLogger(ClienteDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Cliente cliente) {
		if (cliente.getId()!=null) {
			// update
			String sql = "UPDATE cat_cliente "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						cliente.getNom(),
						cliente.getEst(),
						cliente.getUsr_act(),
						new java.util.Date(),
						cliente.getId()); 

		} else {
			// insert
			String sql = "insert into cat_cliente ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				cliente.getNom(),
				cliente.getEst(),
				cliente.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from cat_cliente where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<Cliente> list() {
		String sql = "select * from cat_cliente";
		
		//logger.info(sql);
		
		List<Cliente> listCliente = jdbcTemplate.query(sql, new RowMapper<Cliente>() {

			
			public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCliente;
	}

	
	public Cliente get(int id) {
		String sql = "select * from cat_cliente WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Cliente>() {

			
			public Cliente extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public Cliente getFull(int id, String tablas[]) {
		String sql = "select cli.id cli_id, cli.nom cli_nom  ,cli.est cli_est ";
	
		sql = sql + " from cat_cliente cli "; 
		sql = sql + " where cli.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Cliente>() {
		
			
			public Cliente extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Cliente cliente= rsToEntity(rs,"cli_");
							return cliente;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public Cliente getByParams(Param param) {

		String sql = "select * from cat_cliente " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Cliente>() {
			
			public Cliente extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<Cliente> listByParams(Param param, String[] order) {

		String sql = "select * from cat_cliente " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Cliente>() {

			
			public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<Cliente> listFullByParams(Cliente cliente, String[] order) {
	
		return listFullByParams(Param.toParam("cli",cliente), order);
	
	}	
	
	
	public List<Cliente> listFullByParams(Param param, String[] order) {

		String sql = "select cli.id cli_id, cli.nom cli_nom  ,cli.est cli_est ";
		sql = sql + " from cat_cliente cli";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Cliente>() {

			
			public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
				Cliente cliente= rsToEntity(rs,"cli_");
				return cliente;
			}

		});

	}	


	public List<Matricula> getListMatricula(Param param, String[] order) {
		String sql = "select * from mat_matricula " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Matricula>() {

			
			public Matricula mapRow(ResultSet rs, int rowNum) throws SQLException {
				Matricula matricula = new Matricula();

				matricula.setId(rs.getInt("id"));
				matricula.setId_alu(rs.getInt("id_alu"));
				matricula.setId_fam(rs.getInt("id_fam"));
				matricula.setId_enc(rs.getInt("id_enc"));
				matricula.setId_con(rs.getInt("id_con"));
				matricula.setId_cli(rs.getInt("id_cli"));
				matricula.setId_per(rs.getInt("id_per"));
				matricula.setId_au(rs.getInt("id_au"));
				matricula.setId_gra(rs.getInt("id_gra"));
				matricula.setId_niv(rs.getInt("id_niv"));
				matricula.setFecha(rs.getDate("fecha"));
				matricula.setCar_pod(rs.getString("car_pod"));
				matricula.setEst(rs.getString("est"));
												
				return matricula;
			}

		});	
	}


	// funciones privadas utilitarias para Cliente

	private Cliente rsToEntity(ResultSet rs,String alias) throws SQLException {
		Cliente cliente = new Cliente();

		cliente.setId(rs.getInt( alias + "id"));
		cliente.setNom(rs.getString( alias + "nom"));
		cliente.setEst(rs.getString( alias + "est"));
								
		return cliente;

	}
	
}

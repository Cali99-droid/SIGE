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
import com.tesla.colegio.model.ResDiarioBoleta;



import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ResDiarioBoletaDAO.
 * @author MV
 *
 */
public class ResDiarioBoletaDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(ResDiarioBoleta res_diario_boleta) {
		if (res_diario_boleta.getId() != null) {
			// update
			String sql = "UPDATE fac_res_diario_boleta "
						+ "SET numero=?, "
						+ "fec_emi=?, "
						+ "cant_reg=?, "
						+ "estado=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						res_diario_boleta.getNumero(),
						res_diario_boleta.getFec_emi(),
						res_diario_boleta.getCant_reg(),
						res_diario_boleta.getEstado(),
						res_diario_boleta.getEst(),
						res_diario_boleta.getUsr_act(),
						new java.util.Date(),
						res_diario_boleta.getId()); 
			return res_diario_boleta.getId();

		} else {
			// insert
			String sql = "insert into fac_res_diario_boleta ("
						+ "numero, "
						+ "fec_emi, "
						+ "cant_reg, "
						+ "estado, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				res_diario_boleta.getNumero(),
				res_diario_boleta.getFec_emi(),
				res_diario_boleta.getCant_reg(),
				res_diario_boleta.getEstado(),
				res_diario_boleta.getEst(),
				res_diario_boleta.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from fac_res_diario_boleta where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<ResDiarioBoleta> list() {
		String sql = "select * from fac_res_diario_boleta";
		
		
		
		List<ResDiarioBoleta> listResDiarioBoleta = jdbcTemplate.query(sql, new RowMapper<ResDiarioBoleta>() {

			@Override
			public ResDiarioBoleta mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listResDiarioBoleta;
	}

	public ResDiarioBoleta get(int id) {
		String sql = "select * from fac_res_diario_boleta WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ResDiarioBoleta>() {

			@Override
			public ResDiarioBoleta extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public ResDiarioBoleta getFull(int id, String tablas[]) {
		String sql = "select frda.id frda_id, frda.numero frda_numero , frda.fec_emi frda_fec_emi , frda.cant_reg frda_cant_reg , frda.estado frda_estado  ,frda.est frda_est ";
	
		sql = sql + " from fac_res_diario_boleta frda "; 
		sql = sql + " where frda.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<ResDiarioBoleta>() {
		
			@Override
			public ResDiarioBoleta extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ResDiarioBoleta resdiarioboleta= rsToEntity(rs,"frda_");
							return resdiarioboleta;
				}
				
				return null;
			}
			
		});


	}		
	
	public ResDiarioBoleta getByParams(Param param) {

		String sql = "select * from fac_res_diario_boleta " + SQLFrmkUtil.getWhere(param) + " order by id desc";
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ResDiarioBoleta>() {
			@Override
			public ResDiarioBoleta extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<ResDiarioBoleta> listByParams(Param param, String[] order) {

		String sql = "select * from fac_res_diario_boleta " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<ResDiarioBoleta>() {

			@Override
			public ResDiarioBoleta mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<ResDiarioBoleta> listFullByParams(ResDiarioBoleta resdiarioboleta, String[] order) {
	
		return listFullByParams(Param.toParam("frda",resdiarioboleta), order);
	
	}	
	
	public List<ResDiarioBoleta> listFullByParams(Param param, String[] order) {

		String sql = "select frda.id frda_id, frda.numero frda_numero , frda.fec_emi frda_fec_emi , frda.cant_reg frda_cant_reg , frda.estado frda_estado  ,frda.est frda_est ";
		sql = sql + " from fac_res_diario_boleta frda";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<ResDiarioBoleta>() {

			@Override
			public ResDiarioBoleta mapRow(ResultSet rs, int rowNum) throws SQLException {
				ResDiarioBoleta resdiarioboleta= rsToEntity(rs,"frda_");
				return resdiarioboleta;
			}

		});

	}	




	// funciones privadas utilitarias para ResDiarioBoleta

	private ResDiarioBoleta rsToEntity(ResultSet rs,String alias) throws SQLException {
		ResDiarioBoleta res_diario_boleta = new ResDiarioBoleta();

		res_diario_boleta.setId(rs.getInt( alias + "id"));
		res_diario_boleta.setNumero(rs.getString( alias + "numero"));
		res_diario_boleta.setFec_emi(rs.getDate( alias + "fec_emi"));
		res_diario_boleta.setCant_reg(rs.getString( alias + "cant_reg"));
		res_diario_boleta.setEstado(rs.getString( alias + "estado"));
		res_diario_boleta.setEst(rs.getString( alias + "est"));
								
		return res_diario_boleta;

	}
	
}

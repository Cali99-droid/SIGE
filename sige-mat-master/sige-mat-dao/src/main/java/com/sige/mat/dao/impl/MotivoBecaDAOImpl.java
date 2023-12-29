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
import com.tesla.colegio.model.MotivoBeca;

import com.tesla.colegio.model.Beca;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface MotivoBecaDAO.
 * @author MV
 *
 */
public class MotivoBecaDAOImpl{
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
	public int saveOrUpdate(MotivoBeca motivo_beca) {
		if (motivo_beca.getId() != null) {
			// update
			String sql = "UPDATE col_motivo_beca "
						+ "SET nom=?, "
						+ "id_bec=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						motivo_beca.getNom(),
						motivo_beca.getId_bec(),
						motivo_beca.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						motivo_beca.getId()); 
			return motivo_beca.getId();

		} else {
			// insert
			String sql = "insert into col_motivo_beca ("
						+ "nom, "
						+ "id_bec, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				motivo_beca.getNom(),
				motivo_beca.getId_bec(),
				motivo_beca.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_motivo_beca where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<MotivoBeca> list() {
		String sql = "select * from col_motivo_beca";
		
		System.out.println(sql);
		
		List<MotivoBeca> listMotivoBeca = jdbcTemplate.query(sql, new RowMapper<MotivoBeca>() {

			@Override
			public MotivoBeca mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listMotivoBeca;
	}

	public MotivoBeca get(int id) {
		String sql = "select * from col_motivo_beca WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<MotivoBeca>() {

			@Override
			public MotivoBeca extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public MotivoBeca getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select mbec.id mbec_id, mbec.nom mbec_nom , mbec.id_bec mbec_id_bec  ,mbec.est mbec_est ";
		if (aTablas.contains("col_beca"))
			sql = sql + ", bec.id bec_id  , bec.nom bec_nom , bec.val bec_val , bec.abrv bec_abrv , bec.id_tdes bec_id_tdes  ";
	
		sql = sql + " from col_motivo_beca mbec "; 
		if (aTablas.contains("col_beca"))
			sql = sql + " left join col_beca bec on bec.id = mbec.id_bec ";
		sql = sql + " where mbec.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<MotivoBeca>() {
		
			@Override
			public MotivoBeca extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					MotivoBeca motivobeca= rsToEntity(rs,"mbec_");
					if (aTablas.contains("col_beca")){
						Beca beca = new Beca();  
							beca.setId(rs.getInt("bec_id")) ;  
							beca.setNom(rs.getString("bec_nom")) ;  
							beca.setVal(rs.getString("bec_val")) ;  
							beca.setAbrv(rs.getString("bec_abrv")) ;  
							beca.setId_tdes(rs.getInt("bec_id_tdes")) ;  
							motivobeca.setBeca(beca);
					}
							return motivobeca;
				}
				
				return null;
			}
			
		});


	}		
	
	public MotivoBeca getByParams(Param param) {

		String sql = "select * from col_motivo_beca " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<MotivoBeca>() {
			@Override
			public MotivoBeca extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<MotivoBeca> listByParams(Param param, String[] order) {

		String sql = "select * from col_motivo_beca " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<MotivoBeca>() {

			@Override
			public MotivoBeca mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<MotivoBeca> listFullByParams(MotivoBeca motivobeca, String[] order) {
	
		return listFullByParams(Param.toParam("mbec",motivobeca), order);
	
	}	
	
	public List<MotivoBeca> listFullByParams(Param param, String[] order) {

		String sql = "select mbec.id mbec_id, mbec.nom mbec_nom , mbec.id_bec mbec_id_bec  ,mbec.est mbec_est ";
		sql = sql + ", bec.id bec_id  , bec.nom bec_nom , bec.val bec_val , bec.abrv bec_abrv , bec.id_tdes bec_id_tdes  ";
		sql = sql + " from col_motivo_beca mbec";
		sql = sql + " left join col_beca bec on bec.id = mbec.id_bec ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<MotivoBeca>() {

			@Override
			public MotivoBeca mapRow(ResultSet rs, int rowNum) throws SQLException {
				MotivoBeca motivobeca= rsToEntity(rs,"mbec_");
				Beca beca = new Beca();  
				beca.setId(rs.getInt("bec_id")) ;  
				beca.setNom(rs.getString("bec_nom")) ;  
				beca.setVal(rs.getString("bec_val")) ;  
				beca.setAbrv(rs.getString("bec_abrv")) ;  
				beca.setId_tdes(rs.getInt("bec_id_tdes")) ;  
				motivobeca.setBeca(beca);
				return motivobeca;
			}

		});

	}	




	// funciones privadas utilitarias para MotivoBeca

	private MotivoBeca rsToEntity(ResultSet rs,String alias) throws SQLException {
		MotivoBeca motivo_beca = new MotivoBeca();

		motivo_beca.setId(rs.getInt( alias + "id"));
		motivo_beca.setNom(rs.getString( alias + "nom"));
		motivo_beca.setId_bec(rs.getInt( alias + "id_bec"));
		motivo_beca.setEst(rs.getString( alias + "est"));
								
		return motivo_beca;

	}
	
}

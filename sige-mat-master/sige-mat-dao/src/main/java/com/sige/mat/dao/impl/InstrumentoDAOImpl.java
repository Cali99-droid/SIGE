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
import com.tesla.colegio.model.Instrumento;

import com.tesla.colegio.model.InsExaCri;
import com.tesla.colegio.model.InsExaCri;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface InstrumentoDAO.
 * @author MV
 *
 */
public class InstrumentoDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Instrumento instrumento) {
		if (instrumento.getId() != null) {
			// update
			String sql = "UPDATE eva_instrumento "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						instrumento.getNom(),
						instrumento.getEst(),
						instrumento.getUsr_act(),
						new java.util.Date(),
						instrumento.getId()); 
			return instrumento.getId();

		} else {
			// insert
			String sql = "insert into eva_instrumento ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				instrumento.getNom(),
				instrumento.getEst(),
				instrumento.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from eva_instrumento where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<Instrumento> list() {
		String sql = "select * from eva_instrumento";
		
		
		
		List<Instrumento> listInstrumento = jdbcTemplate.query(sql, new RowMapper<Instrumento>() {

			@Override
			public Instrumento mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listInstrumento;
	}

	public Instrumento get(int id) {
		String sql = "select * from eva_instrumento WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Instrumento>() {

			@Override
			public Instrumento extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Instrumento getFull(int id, String tablas[]) {
		String sql = "select ins.id ins_id, ins.nom ins_nom  ,ins.est ins_est ";
	
		sql = sql + " from eva_instrumento ins "; 
		sql = sql + " where ins.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<Instrumento>() {
		
			@Override
			public Instrumento extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Instrumento instrumento= rsToEntity(rs,"ins_");
							return instrumento;
				}
				
				return null;
			}
			
		});


	}		
	
	public Instrumento getByParams(Param param) {

		String sql = "select * from eva_instrumento " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Instrumento>() {
			@Override
			public Instrumento extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Instrumento> listByParams(Param param, String[] order) {

		String sql = "select * from eva_instrumento " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<Instrumento>() {

			@Override
			public Instrumento mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Instrumento> listFullByParams(Instrumento instrumento, String[] order) {
	
		return listFullByParams(Param.toParam("ins",instrumento), order);
	
	}	
	
	public List<Instrumento> listFullByParams(Param param, String[] order) {

		String sql = "select ins.id ins_id, ins.nom ins_nom  ,ins.est ins_est ";
		sql = sql + " from eva_instrumento ins";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<Instrumento>() {

			@Override
			public Instrumento mapRow(ResultSet rs, int rowNum) throws SQLException {
				Instrumento instrumento= rsToEntity(rs,"ins_");
				return instrumento;
			}

		});

	}	


	public List<InsExaCri> getListInsExaCri(Param param, String[] order) {
		String sql = "select * from eva_ins_exa_cri " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		
		
		return jdbcTemplate.query(sql, new RowMapper<InsExaCri>() {

			@Override
			public InsExaCri mapRow(ResultSet rs, int rowNum) throws SQLException {
				InsExaCri ins_exa_cri = new InsExaCri();

				ins_exa_cri.setId(rs.getInt("id"));
				ins_exa_cri.setId_excri(rs.getInt("id_excri"));
				ins_exa_cri.setId_ins(rs.getInt("id_ins"));
				ins_exa_cri.setEst(rs.getString("est"));
												
				return ins_exa_cri;
			}

		});	
	}

	// funciones privadas utilitarias para Instrumento

	private Instrumento rsToEntity(ResultSet rs,String alias) throws SQLException {
		Instrumento instrumento = new Instrumento();

		instrumento.setId(rs.getInt( alias + "id"));
		instrumento.setNom(rs.getString( alias + "nom"));
		instrumento.setEst(rs.getString( alias + "est"));
								
		return instrumento;

	}
	
}

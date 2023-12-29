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
import com.tesla.colegio.model.CompetenciaTrans;

import com.tesla.colegio.model.DcnCompTrans;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CompetenciaTransDAO.
 * @author MV
 *
 */
public class CompetenciaTransDAOImpl{
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
	public int saveOrUpdate(CompetenciaTrans competencia_trans) {
		if (competencia_trans.getId() != null) {
			// update
			String sql = "UPDATE cat_competencia_trans "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						competencia_trans.getNom(),
						competencia_trans.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						competencia_trans.getId()); 
			return competencia_trans.getId();

		} else {
			// insert
			String sql = "insert into cat_competencia_trans ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				competencia_trans.getNom(),
				competencia_trans.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_competencia_trans where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<CompetenciaTrans> list() {
		String sql = "select * from cat_competencia_trans";
		
		System.out.println(sql);
		
		List<CompetenciaTrans> listCompetenciaTrans = jdbcTemplate.query(sql, new RowMapper<CompetenciaTrans>() {

			@Override
			public CompetenciaTrans mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCompetenciaTrans;
	}

	public CompetenciaTrans get(int id) {
		String sql = "select * from cat_competencia_trans WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CompetenciaTrans>() {

			@Override
			public CompetenciaTrans extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CompetenciaTrans getFull(int id, String tablas[]) {
		String sql = "select ctran.id ctran_id, ctran.nom ctran_nom  ,ctran.est ctran_est ";
	
		sql = sql + " from cat_competencia_trans ctran "; 
		sql = sql + " where ctran.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<CompetenciaTrans>() {
		
			@Override
			public CompetenciaTrans extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CompetenciaTrans competenciatrans= rsToEntity(rs,"ctran_");
							return competenciatrans;
				}
				
				return null;
			}
			
		});


	}		
	
	public CompetenciaTrans getByParams(Param param) {

		String sql = "select * from cat_competencia_trans " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CompetenciaTrans>() {
			@Override
			public CompetenciaTrans extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CompetenciaTrans> listByParams(Param param, String[] order) {

		String sql = "select * from cat_competencia_trans " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<CompetenciaTrans>() {

			@Override
			public CompetenciaTrans mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CompetenciaTrans> listFullByParams(CompetenciaTrans competenciatrans, String[] order) {
	
		return listFullByParams(Param.toParam("ctran",competenciatrans), order);
	
	}	
	
	public List<CompetenciaTrans> listFullByParams(Param param, String[] order) {

		String sql = "select ctran.id ctran_id, ctran.nom ctran_nom  ,ctran.est ctran_est ";
		sql = sql + " from cat_competencia_trans ctran";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<CompetenciaTrans>() {

			@Override
			public CompetenciaTrans mapRow(ResultSet rs, int rowNum) throws SQLException {
				CompetenciaTrans competenciatrans= rsToEntity(rs,"ctran_");
				return competenciatrans;
			}

		});

	}	


	public List<DcnCompTrans> getListDcnCompTrans(Param param, String[] order) {
		String sql = "select * from aca_dcn_comp_trans " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<DcnCompTrans>() {

			@Override
			public DcnCompTrans mapRow(ResultSet rs, int rowNum) throws SQLException {
				DcnCompTrans dcn_comp_trans = new DcnCompTrans();

				dcn_comp_trans.setId(rs.getInt("id"));
				dcn_comp_trans.setId_dcare(rs.getInt("id_dcare"));
				dcn_comp_trans.setId_ctran(rs.getInt("id_ctran"));
				dcn_comp_trans.setEst(rs.getString("est"));
												
				return dcn_comp_trans;
			}

		});	
	}


	// funciones privadas utilitarias para CompetenciaTrans

	private CompetenciaTrans rsToEntity(ResultSet rs,String alias) throws SQLException {
		CompetenciaTrans competencia_trans = new CompetenciaTrans();

		competencia_trans.setId(rs.getInt( alias + "id"));
		competencia_trans.setNom(rs.getString( alias + "nom"));
		competencia_trans.setEst(rs.getString( alias + "est"));
								
		return competencia_trans;

	}
	
}

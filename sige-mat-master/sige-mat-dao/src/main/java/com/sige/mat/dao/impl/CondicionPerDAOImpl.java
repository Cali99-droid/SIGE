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
import com.tesla.colegio.model.CondicionPer;

import com.tesla.colegio.model.Persona;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CondicionPerDAO.
 * @author MV
 *
 */
public class CondicionPerDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(CondicionPer condicion_per) {
		if (condicion_per.getId() != null) {
			// update
			String sql = "UPDATE cat_condicion_per "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						condicion_per.getNom(),
						condicion_per.getEst(),
						condicion_per.getUsr_act(),
						new java.util.Date(),
						condicion_per.getId()); 
			return condicion_per.getId();

		} else {
			// insert
			String sql = "insert into cat_condicion_per ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				condicion_per.getNom(),
				condicion_per.getEst(),
				condicion_per.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_condicion_per where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<CondicionPer> list() {
		String sql = "select * from cat_condicion_per";
		
		//System.out.println(sql);
		
		List<CondicionPer> listCondicionPer = jdbcTemplate.query(sql, new RowMapper<CondicionPer>() {

			@Override
			public CondicionPer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCondicionPer;
	}

	public CondicionPer get(int id) {
		String sql = "select * from cat_condicion_per WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CondicionPer>() {

			@Override
			public CondicionPer extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CondicionPer getFull(int id, String tablas[]) {
		String sql = "select cond.id cond_id, cond.nom cond_nom  ,cond.est cond_est ";
	
		sql = sql + " from cat_condicion_per cond "; 
		sql = sql + " where cond.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<CondicionPer>() {
		
			@Override
			public CondicionPer extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CondicionPer condicionper= rsToEntity(rs,"cond_");
							return condicionper;
				}
				
				return null;
			}
			
		});


	}		
	
	public CondicionPer getByParams(Param param) {

		String sql = "select * from cat_condicion_per " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CondicionPer>() {
			@Override
			public CondicionPer extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CondicionPer> listByParams(Param param, String[] order) {

		String sql = "select * from cat_condicion_per " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<CondicionPer>() {

			@Override
			public CondicionPer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CondicionPer> listFullByParams(CondicionPer condicionper, String[] order) {
	
		return listFullByParams(Param.toParam("cond",condicionper), order);
	
	}	
	
	public List<CondicionPer> listFullByParams(Param param, String[] order) {

		String sql = "select cond.id cond_id, cond.nom cond_nom  ,cond.est cond_est ";
		sql = sql + " from cat_condicion_per cond";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<CondicionPer>() {

			@Override
			public CondicionPer mapRow(ResultSet rs, int rowNum) throws SQLException {
				CondicionPer condicionper= rsToEntity(rs,"cond_");
				return condicionper;
			}

		});

	}	


	public List<Persona> getListPersona(Param param, String[] order) {
		String sql = "select * from col_persona " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Persona>() {

			@Override
			public Persona mapRow(ResultSet rs, int rowNum) throws SQLException {
				Persona persona = new Persona();

				persona.setId(rs.getInt("id"));
				persona.setId_tdc(rs.getString("id_tdc"));
				persona.setId_tap(rs.getString("id_tap"));
				persona.setId_gen(rs.getString("id_gen"));
				persona.setId_eci(rs.getInt("id_eci"));
				persona.setId_rel(rs.getInt("id_rel"));
				persona.setId_dist_viv(rs.getInt("id_dist_viv"));
				persona.setNro_doc(rs.getString("nro_doc"));
				persona.setFec_emi(rs.getDate("fec_emi"));
				persona.setUbigeo(rs.getString("ubigeo"));
				persona.setNom(rs.getString("nom"));
				persona.setApe_pat(rs.getString("ape_pat"));
				persona.setApe_mat(rs.getString("ape_mat"));
				persona.setFoto(rs.getString("foto"));
				persona.setHue(rs.getString("hue"));
				persona.setFec_nac(rs.getDate("fec_nac"));
				persona.setFec_def(rs.getDate("fec_def"));
				persona.setId_pais_nac(rs.getInt("id_pais_nac"));
				persona.setId_dist_nac(rs.getInt("id_dist_nac"));
				persona.setId_nac(rs.getInt("id_nac"));
				persona.setTlf(rs.getString("tlf"));
				persona.setCorr(rs.getString("corr"));
				persona.setCel(rs.getString("cel"));
				persona.setViv(rs.getString("viv"));
				persona.setDir(rs.getString("dir"));
				persona.setTrab(rs.getString("trab"));
				persona.setId_cond(rs.getInt("id_cond"));
				persona.setEst(rs.getString("est"));
												
				return persona;
			}

		});	
	}


	// funciones privadas utilitarias para CondicionPer

	private CondicionPer rsToEntity(ResultSet rs,String alias) throws SQLException {
		CondicionPer condicion_per = new CondicionPer();

		condicion_per.setId(rs.getInt( alias + "id"));
		condicion_per.setNom(rs.getString( alias + "nom"));
		condicion_per.setEst(rs.getString( alias + "est"));
								
		return condicion_per;

	}
	
}

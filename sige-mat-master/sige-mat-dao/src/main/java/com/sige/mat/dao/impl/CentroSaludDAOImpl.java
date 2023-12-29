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
import com.tesla.colegio.model.CentroSalud;

import com.tesla.colegio.model.GruFam;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CentroSaludDAO.
 * @author MV
 *
 */
public class CentroSaludDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(CentroSalud centro_salud) {
		if (centro_salud.getId() != null) {
			// update
			String sql = "UPDATE cat_centro_salud "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						centro_salud.getNom(),
						centro_salud.getEst(),
						centro_salud.getUsr_act(),
						new java.util.Date(),
						centro_salud.getId()); 
			return centro_salud.getId();

		} else {
			// insert
			String sql = "insert into cat_centro_salud ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				centro_salud.getNom(),
				centro_salud.getEst(),
				centro_salud.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_centro_salud where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<CentroSalud> list() {
		String sql = "select * from cat_centro_salud";
		
		//System.out.println(sql);
		
		List<CentroSalud> listCentroSalud = jdbcTemplate.query(sql, new RowMapper<CentroSalud>() {

			@Override
			public CentroSalud mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCentroSalud;
	}

	public CentroSalud get(int id) {
		String sql = "select * from cat_centro_salud WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CentroSalud>() {

			@Override
			public CentroSalud extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CentroSalud getFull(int id, String tablas[]) {
		String sql = "select csal.id csal_id, csal.nom csal_nom  ,csal.est csal_est ";
	
		sql = sql + " from cat_centro_salud csal "; 
		sql = sql + " where csal.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<CentroSalud>() {
		
			@Override
			public CentroSalud extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CentroSalud centrosalud= rsToEntity(rs,"csal_");
							return centrosalud;
				}
				
				return null;
			}
			
		});


	}		
	
	public CentroSalud getByParams(Param param) {

		String sql = "select * from cat_centro_salud " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CentroSalud>() {
			@Override
			public CentroSalud extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CentroSalud> listByParams(Param param, String[] order) {

		String sql = "select * from cat_centro_salud " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<CentroSalud>() {

			@Override
			public CentroSalud mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CentroSalud> listFullByParams(CentroSalud centrosalud, String[] order) {
	
		return listFullByParams(Param.toParam("csal",centrosalud), order);
	
	}	
	
	public List<CentroSalud> listFullByParams(Param param, String[] order) {

		String sql = "select csal.id csal_id, csal.nom csal_nom  ,csal.est csal_est ";
		sql = sql + " from cat_centro_salud csal";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<CentroSalud>() {

			@Override
			public CentroSalud mapRow(ResultSet rs, int rowNum) throws SQLException {
				CentroSalud centrosalud= rsToEntity(rs,"csal_");
				return centrosalud;
			}

		});

	}	


	public List<GruFam> getListGruFam(Param param, String[] order) {
		String sql = "select * from alu_gru_fam " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<GruFam>() {

			@Override
			public GruFam mapRow(ResultSet rs, int rowNum) throws SQLException {
				GruFam gru_fam = new GruFam();

				gru_fam.setId(rs.getInt("id"));
				gru_fam.setNom(rs.getString("nom"));
				gru_fam.setCod(rs.getString("cod"));
				gru_fam.setDes(rs.getString("des"));
				gru_fam.setId_dist(rs.getInt("id_dist"));
				gru_fam.setDireccion(rs.getString("direccion"));
				gru_fam.setReferencia(rs.getString("referencia"));
				gru_fam.setId_seg(rs.getInt("id_seg"));
				gru_fam.setId_csal(rs.getInt("id_csal"));
				gru_fam.setEst(rs.getString("est"));
												
				return gru_fam;
			}

		});	
	}


	// funciones privadas utilitarias para CentroSalud

	private CentroSalud rsToEntity(ResultSet rs,String alias) throws SQLException {
		CentroSalud centro_salud = new CentroSalud();

		centro_salud.setId(rs.getInt( alias + "id"));
		centro_salud.setNom(rs.getString( alias + "nom"));
		centro_salud.setEst(rs.getString( alias + "est"));
								
		return centro_salud;

	}
	
}

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
import com.tesla.colegio.model.Nacionalidad;

import com.tesla.colegio.model.Persona;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface NacionalidadDAO.
 * @author MV
 *
 */
public class NacionalidadDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Nacionalidad nacionalidad) {
		if (nacionalidad.getId() != null) {
			// update
			String sql = "UPDATE cat_nacionalidad "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						nacionalidad.getNom(),
						nacionalidad.getEst(),
						nacionalidad.getUsr_act(),
						new java.util.Date(),
						nacionalidad.getId()); 
			return nacionalidad.getId();

		} else {
			// insert
			String sql = "insert into cat_nacionalidad ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				nacionalidad.getNom(),
				nacionalidad.getEst(),
				nacionalidad.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_nacionalidad where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Nacionalidad> list() {
		String sql = "select * from cat_nacionalidad";
		
		//System.out.println(sql);
		
		List<Nacionalidad> listNacionalidad = jdbcTemplate.query(sql, new RowMapper<Nacionalidad>() {

			@Override
			public Nacionalidad mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listNacionalidad;
	}

	public Nacionalidad get(int id) {
		String sql = "select * from cat_nacionalidad WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Nacionalidad>() {

			@Override
			public Nacionalidad extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Nacionalidad getFull(int id, String tablas[]) {
		String sql = "select nac.id nac_id, nac.nom nac_nom  ,nac.est nac_est ";
	
		sql = sql + " from cat_nacionalidad nac "; 
		sql = sql + " where nac.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Nacionalidad>() {
		
			@Override
			public Nacionalidad extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Nacionalidad nacionalidad= rsToEntity(rs,"nac_");
							return nacionalidad;
				}
				
				return null;
			}
			
		});


	}		
	
	public Nacionalidad getByParams(Param param) {

		String sql = "select * from cat_nacionalidad " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Nacionalidad>() {
			@Override
			public Nacionalidad extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Nacionalidad> listByParams(Param param, String[] order) {

		String sql = "select * from cat_nacionalidad " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Nacionalidad>() {

			@Override
			public Nacionalidad mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Nacionalidad> listFullByParams(Nacionalidad nacionalidad, String[] order) {
	
		return listFullByParams(Param.toParam("nac",nacionalidad), order);
	
	}	
	
	public List<Nacionalidad> listFullByParams(Param param, String[] order) {

		String sql = "select nac.id nac_id, nac.nom nac_nom  ,nac.est nac_est ";
		sql = sql + " from cat_nacionalidad nac";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Nacionalidad>() {

			@Override
			public Nacionalidad mapRow(ResultSet rs, int rowNum) throws SQLException {
				Nacionalidad nacionalidad= rsToEntity(rs,"nac_");
				return nacionalidad;
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


	// funciones privadas utilitarias para Nacionalidad

	private Nacionalidad rsToEntity(ResultSet rs,String alias) throws SQLException {
		Nacionalidad nacionalidad = new Nacionalidad();

		nacionalidad.setId(rs.getInt( alias + "id"));
		nacionalidad.setNom(rs.getString( alias + "nom"));
		nacionalidad.setEst(rs.getString( alias + "est"));
								
		return nacionalidad;

	}
	
}

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
import com.tesla.colegio.model.EncuestaAlt;

import com.tesla.colegio.model.EncuestaPreg;
import com.tesla.colegio.model.EncuestaAlumnoDet;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface EncuestaAltDAO.
 * @author MV
 *
 */
public class EncuestaAltDAOImpl{
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
	public int saveOrUpdate(EncuestaAlt encuesta_alt) {
		if (encuesta_alt.getId() != null) {
			// update
			String sql = "UPDATE col_encuesta_alt "
						+ "SET id_enc_pre=?, "
						+ "alt=?, "
						+ "ptje=?, "
						+ "ord=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						encuesta_alt.getId_enc_pre(),
						encuesta_alt.getAlt(),
						encuesta_alt.getPtje(),
						encuesta_alt.getOrd(),
						encuesta_alt.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						encuesta_alt.getId()); 
			return encuesta_alt.getId();

		} else {
			// insert
			String sql = "insert into col_encuesta_alt ("
						+ "id_enc_pre, "
						+ "alt, "
						+ "ptje, "
						+ "ord, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				encuesta_alt.getId_enc_pre(),
				encuesta_alt.getAlt(),
				encuesta_alt.getPtje(),
				encuesta_alt.getOrd(),
				encuesta_alt.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_encuesta_alt where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<EncuestaAlt> list() {
		String sql = "select * from col_encuesta_alt";
		
		System.out.println(sql);
		
		List<EncuestaAlt> listEncuestaAlt = jdbcTemplate.query(sql, new RowMapper<EncuestaAlt>() {

			@Override
			public EncuestaAlt mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listEncuestaAlt;
	}

	public EncuestaAlt get(int id) {
		String sql = "select * from col_encuesta_alt WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<EncuestaAlt>() {

			@Override
			public EncuestaAlt extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public EncuestaAlt getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select enc_alt.id enc_alt_id, enc_alt.id_enc_pre enc_alt_id_enc_pre , enc_alt.alt enc_alt_alt , enc_alt.ptje enc_alt_ptje , enc_alt.ord enc_alt_ord  ,enc_alt.est enc_alt_est ";
		if (aTablas.contains("col_encuesta_preg"))
			sql = sql + ", enc_pre.id enc_pre_id  , enc_pre.id_enc enc_pre_id_enc , enc_pre.id_cte enc_pre_id_cte , enc_pre.pre enc_pre_pre , enc_pre.ord enc_pre_ord , enc_pre.dep enc_pre_dep  ";
	
		sql = sql + " from col_encuesta_alt enc_alt "; 
		if (aTablas.contains("col_encuesta_preg"))
			sql = sql + " left join col_encuesta_preg enc_pre on enc_pre.id = enc_alt.id_enc_pre ";
		sql = sql + " where enc_alt.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<EncuestaAlt>() {
		
			@Override
			public EncuestaAlt extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					EncuestaAlt encuestaalt= rsToEntity(rs,"enc_alt_");
					if (aTablas.contains("col_encuesta_preg")){
						EncuestaPreg encuestapreg = new EncuestaPreg();  
							encuestapreg.setId(rs.getInt("enc_pre_id")) ;  
							encuestapreg.setId_enc(rs.getInt("enc_pre_id_enc")) ;  
							encuestapreg.setId_ctp(rs.getInt("enc_pre_id_ctp")) ;  
							encuestapreg.setPre(rs.getString("enc_pre_pre")) ;  
							encuestapreg.setOrd(rs.getInt("enc_pre_ord")) ;  
							encuestapreg.setDep(rs.getString("enc_pre_dep")) ;  
							encuestaalt.setEncuestaPreg(encuestapreg);
					}
							return encuestaalt;
				}
				
				return null;
			}
			
		});


	}		
	
	public EncuestaAlt getByParams(Param param) {

		String sql = "select * from col_encuesta_alt " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<EncuestaAlt>() {
			@Override
			public EncuestaAlt extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<EncuestaAlt> listByParams(Param param, String[] order) {

		String sql = "select * from col_encuesta_alt " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<EncuestaAlt>() {

			@Override
			public EncuestaAlt mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<EncuestaAlt> listFullByParams(EncuestaAlt encuestaalt, String[] order) {
	
		return listFullByParams(Param.toParam("enc_alt",encuestaalt), order);
	
	}	
	
	public List<EncuestaAlt> listFullByParams(Param param, String[] order) {

		String sql = "select enc_alt.id enc_alt_id, enc_alt.id_enc_pre enc_alt_id_enc_pre , enc_alt.alt enc_alt_alt , enc_alt.ptje enc_alt_ptje , enc_alt.ord enc_alt_ord  ,enc_alt.est enc_alt_est ";
		sql = sql + ", enc_pre.id enc_pre_id  , enc_pre.id_enc enc_pre_id_enc , enc_pre.id_cte enc_pre_id_cte , enc_pre.pre enc_pre_pre , enc_pre.ord enc_pre_ord , enc_pre.dep enc_pre_dep  ";
		sql = sql + " from col_encuesta_alt enc_alt";
		sql = sql + " left join col_encuesta_preg enc_pre on enc_pre.id = enc_alt.id_enc_pre ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<EncuestaAlt>() {

			@Override
			public EncuestaAlt mapRow(ResultSet rs, int rowNum) throws SQLException {
				EncuestaAlt encuestaalt= rsToEntity(rs,"enc_alt_");
				EncuestaPreg encuestapreg = new EncuestaPreg();  
				encuestapreg.setId(rs.getInt("enc_pre_id")) ;  
				encuestapreg.setId_enc(rs.getInt("enc_pre_id_enc")) ;  
				encuestapreg.setId_ctp(rs.getInt("enc_pre_id_ctp")) ;  
				encuestapreg.setPre(rs.getString("enc_pre_pre")) ;  
				encuestapreg.setOrd(rs.getInt("enc_pre_ord")) ;  
				encuestapreg.setDep(rs.getString("enc_pre_dep")) ;  
				encuestaalt.setEncuestaPreg(encuestapreg);
				return encuestaalt;
			}

		});

	}	


	public List<EncuestaAlumnoDet> getListEncuestaAlumnoDet(Param param, String[] order) {
		String sql = "select * from col_encuesta_alumno_det " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<EncuestaAlumnoDet>() {

			@Override
			public EncuestaAlumnoDet mapRow(ResultSet rs, int rowNum) throws SQLException {
				EncuestaAlumnoDet encuesta_alumno_det = new EncuestaAlumnoDet();

				encuesta_alumno_det.setId(rs.getInt("id"));
				encuesta_alumno_det.setId_enc_alu(rs.getInt("id_enc_alu"));
				encuesta_alumno_det.setId_enc_pre(rs.getInt("id_enc_pre"));
				encuesta_alumno_det.setId_enc_alt(rs.getInt("id_enc_alt"));
				encuesta_alumno_det.setRes(rs.getString("res"));
				encuesta_alumno_det.setEst(rs.getString("est"));
												
				return encuesta_alumno_det;
			}

		});	
	}


	// funciones privadas utilitarias para EncuestaAlt

	private EncuestaAlt rsToEntity(ResultSet rs,String alias) throws SQLException {
		EncuestaAlt encuesta_alt = new EncuestaAlt();

		encuesta_alt.setId(rs.getInt( alias + "id"));
		encuesta_alt.setId_enc_pre(rs.getInt( alias + "id_enc_pre"));
		encuesta_alt.setAlt(rs.getString( alias + "alt"));
		encuesta_alt.setPtje(rs.getInt( alias + "ptje"));
		encuesta_alt.setOrd(rs.getInt( alias + "ord"));
		encuesta_alt.setEst(rs.getString( alias + "est"));
								
		return encuesta_alt;

	}
	
}

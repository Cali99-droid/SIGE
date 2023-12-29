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
import com.tesla.colegio.model.PregDependencia;

import com.tesla.colegio.model.EncuestaPreg;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface PregDependenciaDAO.
 * @author MV
 *
 */
public class PregDependenciaDAOImpl{
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
	public int saveOrUpdate(PregDependencia preg_dependencia) {
		if (preg_dependencia.getId() != null) {
			// update
			String sql = "UPDATE col_preg_dependencia "
						+ "SET id_enc_pre=?, "
						+ "id_pre_dep=?, id_alt=? "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						preg_dependencia.getId_enc_pre(),
						preg_dependencia.getId_pre_dep(),
						preg_dependencia.getId_alt(),
						preg_dependencia.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						preg_dependencia.getId()); 
			return preg_dependencia.getId();

		} else {
			// insert
			String sql = "insert into col_preg_dependencia ("
						+ "id_enc_pre, "
						+ "id_pre_dep, id_alt, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				preg_dependencia.getId_enc_pre(),
				preg_dependencia.getId_pre_dep(),
				preg_dependencia.getId_alt(),
				preg_dependencia.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_preg_dependencia where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<PregDependencia> list() {
		String sql = "select * from col_preg_dependencia";
		
		System.out.println(sql);
		
		List<PregDependencia> listPregDependencia = jdbcTemplate.query(sql, new RowMapper<PregDependencia>() {

			@Override
			public PregDependencia mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listPregDependencia;
	}

	public PregDependencia get(int id) {
		String sql = "select * from col_preg_dependencia WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PregDependencia>() {

			@Override
			public PregDependencia extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public PregDependencia getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select enc_pre_dep.id enc_pre_dep_id, enc_pre_dep.id_enc_pre enc_pre_dep_id_enc_pre , enc_pre_dep.id_pre_dep enc_pre_dep_id_pre_dep  ,enc_pre_dep.est enc_pre_dep_est ";
		if (aTablas.contains("col_encuesta_preg"))
			sql = sql + ", enc_pre.id enc_pre_id  , enc_pre.id_enc enc_pre_id_enc , enc_pre.id_cte enc_pre_id_cte , enc_pre.pre enc_pre_pre , enc_pre.ord enc_pre_ord , enc_pre.dep enc_pre_dep  ";
		if (aTablas.contains("col_encuesta_preg"))
			sql = sql + ", enc_pre.id enc_pre_id  , enc_pre.id_enc enc_pre_id_enc , enc_pre.id_cte enc_pre_id_cte , enc_pre.pre enc_pre_pre , enc_pre.ord enc_pre_ord , enc_pre.dep enc_pre_dep  ";
	
		sql = sql + " from col_preg_dependencia enc_pre_dep "; 
		if (aTablas.contains("col_encuesta_preg"))
			sql = sql + " left join col_encuesta_preg enc_pre on enc_pre.id = enc_pre_dep.id_enc_pre ";
		if (aTablas.contains("col_encuesta_preg"))
			sql = sql + " left join col_encuesta_preg enc_pre on enc_pre.id = enc_pre_dep.id_pre_dep ";
		sql = sql + " where enc_pre_dep.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<PregDependencia>() {
		
			@Override
			public PregDependencia extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					PregDependencia pregdependencia= rsToEntity(rs,"enc_pre_dep_");
					if (aTablas.contains("col_encuesta_preg")){
						EncuestaPreg encuestapreg = new EncuestaPreg();  
							encuestapreg.setId(rs.getInt("enc_pre_id")) ;  
							encuestapreg.setId_enc(rs.getInt("enc_pre_id_enc")) ;  
							encuestapreg.setId_ctp(rs.getInt("enc_pre_id_ctp")) ;  
							encuestapreg.setPre(rs.getString("enc_pre_pre")) ;  
							encuestapreg.setOrd(rs.getInt("enc_pre_ord")) ;  
							encuestapreg.setDep(rs.getString("enc_pre_dep")) ;  
							pregdependencia.setEncuestaPreg(encuestapreg);
					}
					if (aTablas.contains("col_encuesta_preg")){
						EncuestaPreg encuestapreg = new EncuestaPreg();  
							encuestapreg.setId(rs.getInt("enc_pre_id")) ;  
							encuestapreg.setId_enc(rs.getInt("enc_pre_id_enc")) ;  
							encuestapreg.setId_ctp(rs.getInt("enc_pre_id_ctp")) ;  
							encuestapreg.setPre(rs.getString("enc_pre_pre")) ;  
							encuestapreg.setOrd(rs.getInt("enc_pre_ord")) ;  
							encuestapreg.setDep(rs.getString("enc_pre_dep")) ;  
							pregdependencia.setEncuestaPreg(encuestapreg);
					}
							return pregdependencia;
				}
				
				return null;
			}
			
		});


	}		
	
	public PregDependencia getByParams(Param param) {

		String sql = "select * from col_preg_dependencia " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PregDependencia>() {
			@Override
			public PregDependencia extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<PregDependencia> listByParams(Param param, String[] order) {

		String sql = "select * from col_preg_dependencia " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<PregDependencia>() {

			@Override
			public PregDependencia mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<PregDependencia> listFullByParams(PregDependencia pregdependencia, String[] order) {
	
		return listFullByParams(Param.toParam("enc_pre_dep",pregdependencia), order);
	
	}	
	
	public List<PregDependencia> listFullByParams(Param param, String[] order) {

		String sql = "select enc_pre_dep.id enc_pre_dep_id, enc_pre_dep.id_enc_pre enc_pre_dep_id_enc_pre , enc_pre_dep.id_pre_dep enc_pre_dep_id_pre_dep  ,enc_pre_dep.est enc_pre_dep_est ";
		sql = sql + ", enc_pre.id enc_pre_id  , enc_pre.id_enc enc_pre_id_enc , enc_pre.id_cte enc_pre_id_cte , enc_pre.pre enc_pre_pre , enc_pre.ord enc_pre_ord , enc_pre.dep enc_pre_dep  ";
		sql = sql + ", enc_pre.id enc_pre_id  , enc_pre.id_enc enc_pre_id_enc , enc_pre.id_cte enc_pre_id_cte , enc_pre.pre enc_pre_pre , enc_pre.ord enc_pre_ord , enc_pre.dep enc_pre_dep  ";
		sql = sql + " from col_preg_dependencia enc_pre_dep";
		sql = sql + " left join col_encuesta_preg enc_pre on enc_pre.id = enc_pre_dep.id_enc_pre ";
		sql = sql + " left join col_encuesta_preg enc_pre on enc_pre.id = enc_pre_dep.id_pre_dep ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<PregDependencia>() {

			@Override
			public PregDependencia mapRow(ResultSet rs, int rowNum) throws SQLException {
				PregDependencia pregdependencia= rsToEntity(rs,"enc_pre_dep_");
				EncuestaPreg encuestapreg = new EncuestaPreg();  
				encuestapreg.setId(rs.getInt("enc_pre_id")) ;  
				encuestapreg.setId_enc(rs.getInt("enc_pre_id_enc")) ;  
				encuestapreg.setId_ctp(rs.getInt("enc_pre_id_ctp")) ;  
				encuestapreg.setPre(rs.getString("enc_pre_pre")) ;  
				encuestapreg.setOrd(rs.getInt("enc_pre_ord")) ;  
				encuestapreg.setDep(rs.getString("enc_pre_dep")) ;  
				pregdependencia.setEncuestaPreg(encuestapreg);
				return pregdependencia;
			}

		});

	}	




	// funciones privadas utilitarias para PregDependencia

	private PregDependencia rsToEntity(ResultSet rs,String alias) throws SQLException {
		PregDependencia preg_dependencia = new PregDependencia();

		preg_dependencia.setId(rs.getInt( alias + "id"));
		preg_dependencia.setId_enc_pre(rs.getInt( alias + "id_enc_pre"));
		preg_dependencia.setId_pre_dep(rs.getInt( alias + "id_pre_dep"));
		preg_dependencia.setId_alt(rs.getInt( alias + "id_alt"));
		preg_dependencia.setEst(rs.getString( alias + "est"));
								
		return preg_dependencia;

	}
	
}

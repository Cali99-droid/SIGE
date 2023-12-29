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
import com.tesla.colegio.model.EncuestaPreg;

import com.tesla.colegio.model.Encuesta;
import com.tesla.colegio.model.TipoPre;
import com.tesla.colegio.model.EncuestaAlt;
import com.tesla.colegio.model.PregDependencia;
import com.tesla.colegio.model.PregDependencia;
import com.tesla.colegio.model.EncuestaAlumnoDet;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface EncuestaPregDAO.
 * @author MV
 *
 */
public class EncuestaPregDAOImpl{
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
	public int saveOrUpdate(EncuestaPreg encuesta_preg) {
		if (encuesta_preg.getId() != null) {
			// update
			String sql = "UPDATE col_encuesta_preg "
						+ "SET id_enc=?, "
						+ "id_ctp=?, "
						+ "pre=?, "
						+ "ord=?, "
						+ "dep=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						encuesta_preg.getId_enc(),
						encuesta_preg.getId_ctp(),
						encuesta_preg.getPre(),
						encuesta_preg.getOrd(),
						encuesta_preg.getDep(),
						encuesta_preg.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						encuesta_preg.getId()); 
			return encuesta_preg.getId();

		} else {
			// insert
			String sql = "insert into col_encuesta_preg ("
						+ "id_enc, "
						+ "id_ctp, "
						+ "pre, "
						+ "ord, "
						+ "dep, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				encuesta_preg.getId_enc(),
				encuesta_preg.getId_ctp(),
				encuesta_preg.getPre(),
				encuesta_preg.getOrd(),
				encuesta_preg.getDep(),
				encuesta_preg.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_encuesta_preg where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<EncuestaPreg> list() {
		String sql = "select * from col_encuesta_preg";
		
		System.out.println(sql);
		
		List<EncuestaPreg> listEncuestaPreg = jdbcTemplate.query(sql, new RowMapper<EncuestaPreg>() {

			@Override
			public EncuestaPreg mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listEncuestaPreg;
	}

	public EncuestaPreg get(int id) {
		String sql = "select * from col_encuesta_preg WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<EncuestaPreg>() {

			@Override
			public EncuestaPreg extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public EncuestaPreg getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select enc_pre.id enc_pre_id, enc_pre.id_enc enc_pre_id_enc , enc_pre.id_cte enc_pre_id_cte , enc_pre.pre enc_pre_pre , enc_pre.ord enc_pre_ord , enc_pre.dep enc_pre_dep  ,enc_pre.est enc_pre_est ";
		if (aTablas.contains("col_encuesta"))
			sql = sql + ", enc.id enc_id  , enc.id_anio enc_id_anio , enc.id_gir enc_id_gir , enc.nom enc_nom , enc.msj_ini enc_msj_ini , enc.msj_fin enc_msj_fin , enc.fec_ini enc_fec_ini , enc.fec_fin enc_fec_fin  ";
		if (aTablas.contains("cat_tipo_enc"))
			sql = sql + ", cte.id cte_id  , cte.nom cte_nom , cte.cod cte_cod  ";
	
		sql = sql + " from col_encuesta_preg enc_pre "; 
		if (aTablas.contains("col_encuesta"))
			sql = sql + " left join col_encuesta enc on enc.id = enc_pre.id_enc ";
		if (aTablas.contains("cat_tipo_enc"))
			sql = sql + " left join cat_tipo_enc cte on cte.id = enc_pre.id_cte ";
		sql = sql + " where enc_pre.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<EncuestaPreg>() {
		
			@Override
			public EncuestaPreg extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					EncuestaPreg encuestapreg= rsToEntity(rs,"enc_pre_");
					if (aTablas.contains("col_encuesta")){
						Encuesta encuesta = new Encuesta();  
							encuesta.setId(rs.getInt("enc_id")) ;  
							encuesta.setId_anio(rs.getInt("enc_id_anio")) ;  
							encuesta.setId_gir(rs.getInt("enc_id_gir")) ;  
							encuesta.setNom(rs.getString("enc_nom")) ;  
							encuesta.setMsj_ini(rs.getString("enc_msj_ini")) ;  
							encuesta.setMsj_fin(rs.getString("enc_msj_fin")) ;  
							encuesta.setFec_ini(rs.getDate("enc_fec_ini")) ;  
							encuesta.setFec_fin(rs.getDate("enc_fec_fin")) ;  
							encuestapreg.setEncuesta(encuesta);
					}
					if (aTablas.contains("cat_tipo_enc")){
						TipoPre tipoenc = new TipoPre();  
							tipoenc.setId(rs.getInt("cte_id")) ;  
							tipoenc.setNom(rs.getString("cte_nom")) ;  
							tipoenc.setCod(rs.getString("cte_cod")) ;  
							encuestapreg.setTipoEnc(tipoenc);
					}
							return encuestapreg;
				}
				
				return null;
			}
			
		});


	}		
	
	public EncuestaPreg getByParams(Param param) {

		String sql = "select * from col_encuesta_preg " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<EncuestaPreg>() {
			@Override
			public EncuestaPreg extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<EncuestaPreg> listByParams(Param param, String[] order) {

		String sql = "select * from col_encuesta_preg " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<EncuestaPreg>() {

			@Override
			public EncuestaPreg mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<EncuestaPreg> listFullByParams(EncuestaPreg encuestapreg, String[] order) {
	
		return listFullByParams(Param.toParam("enc_pre",encuestapreg), order);
	
	}	
	
	public List<EncuestaPreg> listFullByParams(Param param, String[] order) {

		String sql = "select enc_pre.id enc_pre_id, enc_pre.id_enc enc_pre_id_enc , enc_pre.id_cte enc_pre_id_cte , enc_pre.pre enc_pre_pre , enc_pre.ord enc_pre_ord , enc_pre.dep enc_pre_dep  ,enc_pre.est enc_pre_est ";
		sql = sql + ", enc.id enc_id  , enc.id_anio enc_id_anio , enc.id_gir enc_id_gir , enc.nom enc_nom , enc.msj_ini enc_msj_ini , enc.msj_fin enc_msj_fin , enc.fec_ini enc_fec_ini , enc.fec_fin enc_fec_fin  ";
		sql = sql + ", cte.id cte_id  , cte.nom cte_nom , cte.cod cte_cod  ";
		sql = sql + " from col_encuesta_preg enc_pre";
		sql = sql + " left join col_encuesta enc on enc.id = enc_pre.id_enc ";
		sql = sql + " left join cat_tipo_enc cte on cte.id = enc_pre.id_cte ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<EncuestaPreg>() {

			@Override
			public EncuestaPreg mapRow(ResultSet rs, int rowNum) throws SQLException {
				EncuestaPreg encuestapreg= rsToEntity(rs,"enc_pre_");
				Encuesta encuesta = new Encuesta();  
				encuesta.setId(rs.getInt("enc_id")) ;  
				encuesta.setId_anio(rs.getInt("enc_id_anio")) ;  
				encuesta.setId_gir(rs.getInt("enc_id_gir")) ;  
				encuesta.setNom(rs.getString("enc_nom")) ;  
				encuesta.setMsj_ini(rs.getString("enc_msj_ini")) ;  
				encuesta.setMsj_fin(rs.getString("enc_msj_fin")) ;  
				encuesta.setFec_ini(rs.getDate("enc_fec_ini")) ;  
				encuesta.setFec_fin(rs.getDate("enc_fec_fin")) ;  
				encuestapreg.setEncuesta(encuesta);
				TipoPre tipoenc = new TipoPre();  
				tipoenc.setId(rs.getInt("cte_id")) ;  
				tipoenc.setNom(rs.getString("cte_nom")) ;  
				tipoenc.setCod(rs.getString("cte_cod")) ;  
				encuestapreg.setTipoEnc(tipoenc);
				return encuestapreg;
			}

		});

	}	


	public List<EncuestaAlt> getListEncuestaAlt(Param param, String[] order) {
		String sql = "select * from col_encuesta_alt " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<EncuestaAlt>() {

			@Override
			public EncuestaAlt mapRow(ResultSet rs, int rowNum) throws SQLException {
				EncuestaAlt encuesta_alt = new EncuestaAlt();

				encuesta_alt.setId(rs.getInt("id"));
				encuesta_alt.setId_enc_pre(rs.getInt("id_enc_pre"));
				encuesta_alt.setAlt(rs.getString("alt"));
				encuesta_alt.setPtje(rs.getInt("ptje"));
				encuesta_alt.setOrd(rs.getInt("ord"));
				encuesta_alt.setEst(rs.getString("est"));
												
				return encuesta_alt;
			}

		});	
	}
	public List<PregDependencia> getListPregDependencia(Param param, String[] order) {
		String sql = "select * from col_preg_dependencia " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<PregDependencia>() {

			@Override
			public PregDependencia mapRow(ResultSet rs, int rowNum) throws SQLException {
				PregDependencia preg_dependencia = new PregDependencia();

				preg_dependencia.setId(rs.getInt("id"));
				preg_dependencia.setId(rs.getInt("id"));
				preg_dependencia.setId_enc_pre(rs.getInt("id_enc_pre"));
				preg_dependencia.setId_pre_dep(rs.getInt("id_pre_dep"));
				preg_dependencia.setEst(rs.getString("est"));
												
				return preg_dependencia;
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


	// funciones privadas utilitarias para EncuestaPreg

	private EncuestaPreg rsToEntity(ResultSet rs,String alias) throws SQLException {
		EncuestaPreg encuesta_preg = new EncuestaPreg();

		encuesta_preg.setId(rs.getInt( alias + "id"));
		encuesta_preg.setId_enc(rs.getInt( alias + "id_enc"));
		encuesta_preg.setId_ctp(rs.getInt( alias + "id_ctp"));
		encuesta_preg.setPre(rs.getString( alias + "pre"));
		encuesta_preg.setOrd(rs.getInt( alias + "ord"));
		encuesta_preg.setDep(rs.getString( alias + "dep"));
		encuesta_preg.setEst(rs.getString( alias + "est"));
								
		return encuesta_preg;

	}
	
}

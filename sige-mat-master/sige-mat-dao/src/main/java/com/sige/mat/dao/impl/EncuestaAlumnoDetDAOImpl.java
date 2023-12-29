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
import com.tesla.colegio.model.EncuestaAlumnoDet;

import com.tesla.colegio.model.EncuestaAlumno;
import com.tesla.colegio.model.EncuestaPreg;
import com.tesla.colegio.model.EncuestaAlt;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface EncuestaAlumnoDetDAO.
 * @author MV
 *
 */
public class EncuestaAlumnoDetDAOImpl{
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
	public int saveOrUpdate(EncuestaAlumnoDet encuesta_alumno_det) {
		if (encuesta_alumno_det.getId() != null) {
			// update
			String sql = "UPDATE col_encuesta_alumno_det "
						+ "SET id_enc_alu=?, "
						+ "id_enc_pre=?, "
						+ "id_enc_alt=?, "
						+ "res=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						encuesta_alumno_det.getId_enc_alu(),
						encuesta_alumno_det.getId_enc_pre(),
						encuesta_alumno_det.getId_enc_alt(),
						encuesta_alumno_det.getRes(),
						encuesta_alumno_det.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						encuesta_alumno_det.getId()); 
			return encuesta_alumno_det.getId();

		} else {
			// insert
			String sql = "insert into col_encuesta_alumno_det ("
						+ "id_enc_alu, "
						+ "id_enc_pre, "
						+ "id_enc_alt, "
						+ "res, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				encuesta_alumno_det.getId_enc_alu(),
				encuesta_alumno_det.getId_enc_pre(),
				encuesta_alumno_det.getId_enc_alt(),
				encuesta_alumno_det.getRes(),
				encuesta_alumno_det.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_encuesta_alumno_det where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<EncuestaAlumnoDet> list() {
		String sql = "select * from col_encuesta_alumno_det";
		
		System.out.println(sql);
		
		List<EncuestaAlumnoDet> listEncuestaAlumnoDet = jdbcTemplate.query(sql, new RowMapper<EncuestaAlumnoDet>() {

			@Override
			public EncuestaAlumnoDet mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listEncuestaAlumnoDet;
	}

	public EncuestaAlumnoDet get(int id) {
		String sql = "select * from col_encuesta_alumno_det WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<EncuestaAlumnoDet>() {

			@Override
			public EncuestaAlumnoDet extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public EncuestaAlumnoDet getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select enc_alu_det.id enc_alu_det_id, enc_alu_det.id_enc_alu enc_alu_det_id_enc_alu , enc_alu_det.id_enc_pre enc_alu_det_id_enc_pre , enc_alu_det.id_enc_alt enc_alu_det_id_enc_alt , enc_alu_det.res enc_alu_det_res  ,enc_alu_det.est enc_alu_det_est ";
		if (aTablas.contains("col_encuesta_alumno"))
			sql = sql + ", enc_alu.id enc_alu_id  , enc_alu.id_enc enc_alu_id_enc , enc_alu.id_mat enc_alu_id_mat , enc_alu.num enc_alu_num , enc_alu.ptje enc_alu_ptje , enc_alu.res enc_alu_res  ";
		if (aTablas.contains("col_encuesta_preg"))
			sql = sql + ", enc_pre.id enc_pre_id  , enc_pre.id_enc enc_pre_id_enc , enc_pre.id_cte enc_pre_id_cte , enc_pre.pre enc_pre_pre , enc_pre.ord enc_pre_ord , enc_pre.dep enc_pre_dep  ";
		if (aTablas.contains("col_encuesta_alt"))
			sql = sql + ", enc_alt.id enc_alt_id  , enc_alt.id_enc_pre enc_alt_id_enc_pre , enc_alt.alt enc_alt_alt , enc_alt.ptje enc_alt_ptje , enc_alt.ord enc_alt_ord  ";
	
		sql = sql + " from col_encuesta_alumno_det enc_alu_det "; 
		if (aTablas.contains("col_encuesta_alumno"))
			sql = sql + " left join col_encuesta_alumno enc_alu on enc_alu.id = enc_alu_det.id_enc_alu ";
		if (aTablas.contains("col_encuesta_preg"))
			sql = sql + " left join col_encuesta_preg enc_pre on enc_pre.id = enc_alu_det.id_enc_pre ";
		if (aTablas.contains("col_encuesta_alt"))
			sql = sql + " left join col_encuesta_alt enc_alt on enc_alt.id = enc_alu_det.id_enc_alt ";
		sql = sql + " where enc_alu_det.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<EncuestaAlumnoDet>() {
		
			@Override
			public EncuestaAlumnoDet extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					EncuestaAlumnoDet encuestaalumnodet= rsToEntity(rs,"enc_alu_det_");
					if (aTablas.contains("col_encuesta_alumno")){
						EncuestaAlumno encuestaalumno = new EncuestaAlumno();  
							encuestaalumno.setId(rs.getInt("enc_alu_id")) ;  
							encuestaalumno.setId_enc(rs.getInt("enc_alu_id_enc")) ;  
							encuestaalumno.setId_mat(rs.getInt("enc_alu_id_mat")) ;  
							encuestaalumno.setNum(rs.getString("enc_alu_num")) ;  
							encuestaalumno.setPtje(rs.getInt("enc_alu_ptje")) ;  
							encuestaalumno.setRes(rs.getString("enc_alu_res")) ;  
							encuestaalumnodet.setEncuestaAlumno(encuestaalumno);
					}
					if (aTablas.contains("col_encuesta_preg")){
						EncuestaPreg encuestapreg = new EncuestaPreg();  
							encuestapreg.setId(rs.getInt("enc_pre_id")) ;  
							encuestapreg.setId_enc(rs.getInt("enc_pre_id_enc")) ;  
							encuestapreg.setId_ctp(rs.getInt("enc_pre_id_ctp")) ;  
							encuestapreg.setPre(rs.getString("enc_pre_pre")) ;  
							encuestapreg.setOrd(rs.getInt("enc_pre_ord")) ;  
							encuestapreg.setDep(rs.getString("enc_pre_dep")) ;  
							encuestaalumnodet.setEncuestaPreg(encuestapreg);
					}
					if (aTablas.contains("col_encuesta_alt")){
						EncuestaAlt encuestaalt = new EncuestaAlt();  
							encuestaalt.setId(rs.getInt("enc_alt_id")) ;  
							encuestaalt.setId_enc_pre(rs.getInt("enc_alt_id_enc_pre")) ;  
							encuestaalt.setAlt(rs.getString("enc_alt_alt")) ;  
							encuestaalt.setPtje(rs.getInt("enc_alt_ptje")) ;  
							encuestaalt.setOrd(rs.getInt("enc_alt_ord")) ;  
							encuestaalumnodet.setEncuestaAlt(encuestaalt);
					}
							return encuestaalumnodet;
				}
				
				return null;
			}
			
		});


	}		
	
	public EncuestaAlumnoDet getByParams(Param param) {

		String sql = "select * from col_encuesta_alumno_det " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<EncuestaAlumnoDet>() {
			@Override
			public EncuestaAlumnoDet extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<EncuestaAlumnoDet> listByParams(Param param, String[] order) {

		String sql = "select * from col_encuesta_alumno_det " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<EncuestaAlumnoDet>() {

			@Override
			public EncuestaAlumnoDet mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<EncuestaAlumnoDet> listFullByParams(EncuestaAlumnoDet encuestaalumnodet, String[] order) {
	
		return listFullByParams(Param.toParam("enc_alu_det",encuestaalumnodet), order);
	
	}	
	
	public List<EncuestaAlumnoDet> listFullByParams(Param param, String[] order) {

		String sql = "select enc_alu_det.id enc_alu_det_id, enc_alu_det.id_enc_alu enc_alu_det_id_enc_alu , enc_alu_det.id_enc_pre enc_alu_det_id_enc_pre , enc_alu_det.id_enc_alt enc_alu_det_id_enc_alt , enc_alu_det.res enc_alu_det_res  ,enc_alu_det.est enc_alu_det_est ";
		sql = sql + ", enc_alu.id enc_alu_id  , enc_alu.id_enc enc_alu_id_enc , enc_alu.id_mat enc_alu_id_mat , enc_alu.num enc_alu_num , enc_alu.ptje enc_alu_ptje , enc_alu.res enc_alu_res  ";
		sql = sql + ", enc_pre.id enc_pre_id  , enc_pre.id_enc enc_pre_id_enc , enc_pre.id_cte enc_pre_id_cte , enc_pre.pre enc_pre_pre , enc_pre.ord enc_pre_ord , enc_pre.dep enc_pre_dep  ";
		sql = sql + ", enc_alt.id enc_alt_id  , enc_alt.id_enc_pre enc_alt_id_enc_pre , enc_alt.alt enc_alt_alt , enc_alt.ptje enc_alt_ptje , enc_alt.ord enc_alt_ord  ";
		sql = sql + " from col_encuesta_alumno_det enc_alu_det";
		sql = sql + " left join col_encuesta_alumno enc_alu on enc_alu.id = enc_alu_det.id_enc_alu ";
		sql = sql + " left join col_encuesta_preg enc_pre on enc_pre.id = enc_alu_det.id_enc_pre ";
		sql = sql + " left join col_encuesta_alt enc_alt on enc_alt.id = enc_alu_det.id_enc_alt ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<EncuestaAlumnoDet>() {

			@Override
			public EncuestaAlumnoDet mapRow(ResultSet rs, int rowNum) throws SQLException {
				EncuestaAlumnoDet encuestaalumnodet= rsToEntity(rs,"enc_alu_det_");
				EncuestaAlumno encuestaalumno = new EncuestaAlumno();  
				encuestaalumno.setId(rs.getInt("enc_alu_id")) ;  
				encuestaalumno.setId_enc(rs.getInt("enc_alu_id_enc")) ;  
				encuestaalumno.setId_mat(rs.getInt("enc_alu_id_mat")) ;  
				encuestaalumno.setNum(rs.getString("enc_alu_num")) ;  
				encuestaalumno.setPtje(rs.getInt("enc_alu_ptje")) ;  
				encuestaalumno.setRes(rs.getString("enc_alu_res")) ;  
				encuestaalumnodet.setEncuestaAlumno(encuestaalumno);
				EncuestaPreg encuestapreg = new EncuestaPreg();  
				encuestapreg.setId(rs.getInt("enc_pre_id")) ;  
				encuestapreg.setId_enc(rs.getInt("enc_pre_id_enc")) ;  
				encuestapreg.setId_ctp(rs.getInt("enc_pre_id_ctp")) ;  
				encuestapreg.setPre(rs.getString("enc_pre_pre")) ;  
				encuestapreg.setOrd(rs.getInt("enc_pre_ord")) ;  
				encuestapreg.setDep(rs.getString("enc_pre_dep")) ;  
				encuestaalumnodet.setEncuestaPreg(encuestapreg);
				EncuestaAlt encuestaalt = new EncuestaAlt();  
				encuestaalt.setId(rs.getInt("enc_alt_id")) ;  
				encuestaalt.setId_enc_pre(rs.getInt("enc_alt_id_enc_pre")) ;  
				encuestaalt.setAlt(rs.getString("enc_alt_alt")) ;  
				encuestaalt.setPtje(rs.getInt("enc_alt_ptje")) ;  
				encuestaalt.setOrd(rs.getInt("enc_alt_ord")) ;  
				encuestaalumnodet.setEncuestaAlt(encuestaalt);
				return encuestaalumnodet;
			}

		});

	}	




	// funciones privadas utilitarias para EncuestaAlumnoDet

	private EncuestaAlumnoDet rsToEntity(ResultSet rs,String alias) throws SQLException {
		EncuestaAlumnoDet encuesta_alumno_det = new EncuestaAlumnoDet();

		encuesta_alumno_det.setId(rs.getInt( alias + "id"));
		encuesta_alumno_det.setId_enc_alu(rs.getInt( alias + "id_enc_alu"));
		encuesta_alumno_det.setId_enc_pre(rs.getInt( alias + "id_enc_pre"));
		encuesta_alumno_det.setId_enc_alt(rs.getInt( alias + "id_enc_alt"));
		encuesta_alumno_det.setRes(rs.getString( alias + "res"));
		encuesta_alumno_det.setEst(rs.getString( alias + "est"));
								
		return encuesta_alumno_det;

	}
	
}

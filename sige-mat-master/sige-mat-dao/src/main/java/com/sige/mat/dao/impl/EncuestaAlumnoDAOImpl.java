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
import com.tesla.colegio.model.EncuestaAlumno;

import com.tesla.colegio.model.Encuesta;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.EncuestaAlumnoDet;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface EncuestaAlumnoDAO.
 * @author MV
 *
 */
public class EncuestaAlumnoDAOImpl{
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
	public int saveOrUpdate(EncuestaAlumno encuesta_alumno) {
		if (encuesta_alumno.getId() != null) {
			// update
			String sql = "UPDATE col_encuesta_alumno "
						+ "SET id_enc=?, "
						+ "id_mat=?, "
						+ "num=?, "
						+ "ptje=?, "
						+ "res=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						encuesta_alumno.getId_enc(),
						encuesta_alumno.getId_mat(),
						encuesta_alumno.getNum(),
						encuesta_alumno.getPtje(),
						encuesta_alumno.getRes(),
						encuesta_alumno.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						encuesta_alumno.getId()); 
			return encuesta_alumno.getId();

		} else {
			// insert
			String sql = "insert into col_encuesta_alumno ("
						+ "id_enc, "
						+ "id_mat, "
						+ "num, "
						+ "ptje, "
						+ "res, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				encuesta_alumno.getId_enc(),
				encuesta_alumno.getId_mat(),
				encuesta_alumno.getNum(),
				encuesta_alumno.getPtje(),
				encuesta_alumno.getRes(),
				encuesta_alumno.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_encuesta_alumno where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<EncuestaAlumno> list() {
		String sql = "select * from col_encuesta_alumno";
		
		System.out.println(sql);
		
		List<EncuestaAlumno> listEncuestaAlumno = jdbcTemplate.query(sql, new RowMapper<EncuestaAlumno>() {

			@Override
			public EncuestaAlumno mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listEncuestaAlumno;
	}

	public EncuestaAlumno get(int id) {
		String sql = "select * from col_encuesta_alumno WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<EncuestaAlumno>() {

			@Override
			public EncuestaAlumno extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public EncuestaAlumno getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select enc_alu.id enc_alu_id, enc_alu.id_enc enc_alu_id_enc , enc_alu.id_mat enc_alu_id_mat , enc_alu.num enc_alu_num , enc_alu.ptje enc_alu_ptje , enc_alu.res enc_alu_res  ,enc_alu.est enc_alu_est ";
		if (aTablas.contains("col_encuesta"))
			sql = sql + ", enc.id enc_id  , enc.id_anio enc_id_anio , enc.id_gir enc_id_gir , enc.nom enc_nom , enc.msj_ini enc_msj_ini , enc.msj_fin enc_msj_fin , enc.fec_ini enc_fec_ini , enc.fec_fin enc_fec_fin  ";
		if (aTablas.contains("mat_matricula"))
			sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_cic mat_id_cic , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.tipo mat_tipo , mat.tip_mat mat_tip_mat , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
	
		sql = sql + " from col_encuesta_alumno enc_alu "; 
		if (aTablas.contains("col_encuesta"))
			sql = sql + " left join col_encuesta enc on enc.id = enc_alu.id_enc ";
		if (aTablas.contains("mat_matricula"))
			sql = sql + " left join mat_matricula mat on mat.id = enc_alu.id_mat ";
		sql = sql + " where enc_alu.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<EncuestaAlumno>() {
		
			@Override
			public EncuestaAlumno extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					EncuestaAlumno encuestaalumno= rsToEntity(rs,"enc_alu_");
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
							encuestaalumno.setEncuesta(encuesta);
					}
					if (aTablas.contains("mat_matricula")){
						Matricula matricula = new Matricula();  
							matricula.setId(rs.getInt("mat_id")) ;  
							matricula.setId_alu(rs.getInt("mat_id_alu")) ;  
							matricula.setId_fam(rs.getInt("mat_id_fam")) ;  
							matricula.setId_enc(rs.getInt("mat_id_enc")) ;  
							matricula.setId_con(rs.getInt("mat_id_con")) ;  
							matricula.setId_cli(rs.getInt("mat_id_cli")) ;  
							matricula.setId_per(rs.getInt("mat_id_per")) ;  
							matricula.setId_cic(rs.getInt("mat_id_cic")) ;  
							matricula.setId_au(rs.getInt("mat_id_au")) ;  
							matricula.setId_gra(rs.getInt("mat_id_gra")) ;  
							matricula.setId_niv(rs.getInt("mat_id_niv")) ;  
							matricula.setFecha(rs.getDate("mat_fecha")) ;  
							matricula.setCar_pod(rs.getString("mat_car_pod")) ;  
							matricula.setTipo(rs.getString("mat_tipo")) ;  
							//matricula.setTip_mat(rs.getString("mat_tip_mat")) ;  
							matricula.setNum_cont(rs.getString("mat_num_cont")) ;  
							matricula.setObs(rs.getString("mat_obs")) ;  
							encuestaalumno.setMatricula(matricula);
					}
							return encuestaalumno;
				}
				
				return null;
			}
			
		});


	}		
	
	public EncuestaAlumno getByParams(Param param) {

		String sql = "select * from col_encuesta_alumno " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<EncuestaAlumno>() {
			@Override
			public EncuestaAlumno extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<EncuestaAlumno> listByParams(Param param, String[] order) {

		String sql = "select * from col_encuesta_alumno " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<EncuestaAlumno>() {

			@Override
			public EncuestaAlumno mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<EncuestaAlumno> listFullByParams(EncuestaAlumno encuestaalumno, String[] order) {
	
		return listFullByParams(Param.toParam("enc_alu",encuestaalumno), order);
	
	}	
	
	public List<EncuestaAlumno> listFullByParams(Param param, String[] order) {

		String sql = "select enc_alu.id enc_alu_id, enc_alu.id_enc enc_alu_id_enc , enc_alu.id_mat enc_alu_id_mat , enc_alu.num enc_alu_num , enc_alu.ptje enc_alu_ptje , enc_alu.res enc_alu_res  ,enc_alu.est enc_alu_est ";
		sql = sql + ", enc.id enc_id  , enc.id_anio enc_id_anio , enc.id_gir enc_id_gir , enc.nom enc_nom , enc.msj_ini enc_msj_ini , enc.msj_fin enc_msj_fin , enc.fec_ini enc_fec_ini , enc.fec_fin enc_fec_fin  ";
		sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_cic mat_id_cic , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.tipo mat_tipo , mat.tip_mat mat_tip_mat , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		sql = sql + " from col_encuesta_alumno enc_alu";
		sql = sql + " left join col_encuesta enc on enc.id = enc_alu.id_enc ";
		sql = sql + " left join mat_matricula mat on mat.id = enc_alu.id_mat ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<EncuestaAlumno>() {

			@Override
			public EncuestaAlumno mapRow(ResultSet rs, int rowNum) throws SQLException {
				EncuestaAlumno encuestaalumno= rsToEntity(rs,"enc_alu_");
				Encuesta encuesta = new Encuesta();  
				encuesta.setId(rs.getInt("enc_id")) ;  
				encuesta.setId_anio(rs.getInt("enc_id_anio")) ;  
				encuesta.setId_gir(rs.getInt("enc_id_gir")) ;  
				encuesta.setNom(rs.getString("enc_nom")) ;  
				encuesta.setMsj_ini(rs.getString("enc_msj_ini")) ;  
				encuesta.setMsj_fin(rs.getString("enc_msj_fin")) ;  
				encuesta.setFec_ini(rs.getDate("enc_fec_ini")) ;  
				encuesta.setFec_fin(rs.getDate("enc_fec_fin")) ;  
				encuestaalumno.setEncuesta(encuesta);
				Matricula matricula = new Matricula();  
				matricula.setId(rs.getInt("mat_id")) ;  
				matricula.setId_alu(rs.getInt("mat_id_alu")) ;  
				matricula.setId_fam(rs.getInt("mat_id_fam")) ;  
				matricula.setId_enc(rs.getInt("mat_id_enc")) ;  
				matricula.setId_con(rs.getInt("mat_id_con")) ;  
				matricula.setId_cli(rs.getInt("mat_id_cli")) ;  
				matricula.setId_per(rs.getInt("mat_id_per")) ;  
				matricula.setId_cic(rs.getInt("mat_id_cic")) ;  
				matricula.setId_au(rs.getInt("mat_id_au")) ;  
				matricula.setId_gra(rs.getInt("mat_id_gra")) ;  
				matricula.setId_niv(rs.getInt("mat_id_niv")) ;  
				matricula.setFecha(rs.getDate("mat_fecha")) ;  
				matricula.setCar_pod(rs.getString("mat_car_pod")) ;  
				matricula.setTipo(rs.getString("mat_tipo")) ;  
				//matricula.setTip_mat(rs.getString("mat_tip_mat")) ;  
				matricula.setNum_cont(rs.getString("mat_num_cont")) ;  
				matricula.setObs(rs.getString("mat_obs")) ;  
				encuestaalumno.setMatricula(matricula);
				return encuestaalumno;
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


	// funciones privadas utilitarias para EncuestaAlumno

	private EncuestaAlumno rsToEntity(ResultSet rs,String alias) throws SQLException {
		EncuestaAlumno encuesta_alumno = new EncuestaAlumno();

		encuesta_alumno.setId(rs.getInt( alias + "id"));
		encuesta_alumno.setId_enc(rs.getInt( alias + "id_enc"));
		encuesta_alumno.setId_mat(rs.getInt( alias + "id_mat"));
		encuesta_alumno.setNum(rs.getString( alias + "num"));
		encuesta_alumno.setPtje(rs.getInt( alias + "ptje"));
		encuesta_alumno.setRes(rs.getString( alias + "res"));
		encuesta_alumno.setEst(rs.getString( alias + "est"));
								
		return encuesta_alumno;

	}
	
}

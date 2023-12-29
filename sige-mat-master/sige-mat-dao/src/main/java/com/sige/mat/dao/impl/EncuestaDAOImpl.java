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
import com.tesla.colegio.model.Encuesta;

import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.GiroNegocio;
import com.tesla.colegio.model.EncuestaPreg;
import com.tesla.colegio.model.EncuestaAlumno;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface EncuestaDAO.
 * @author MV
 *
 */
public class EncuestaDAOImpl{
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
	public int saveOrUpdate(Encuesta encuesta) {
		if (encuesta.getId() != null) {
			// update
			String sql = "UPDATE col_encuesta "
						+ "SET id_anio=?, "
						+ "id_gir=?, "
						+ "nom=?, "
						+ "msj_ini=?, "
						+ "msj_fin=?, "
						+ "fec_ini=?, "
						+ "fec_fin=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						encuesta.getId_anio(),
						encuesta.getId_gir(),
						encuesta.getNom(),
						encuesta.getMsj_ini(),
						encuesta.getMsj_fin(),
						encuesta.getFec_ini(),
						encuesta.getFec_fin(),
						encuesta.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						encuesta.getId()); 
			return encuesta.getId();

		} else {
			// insert
			String sql = "insert into col_encuesta ("
						+ "id_anio, "
						+ "id_gir, "
						+ "nom, "
						+ "msj_ini, "
						+ "msj_fin, "
						+ "fec_ini, "
						+ "fec_fin, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				encuesta.getId_anio(),
				encuesta.getId_gir(),
				encuesta.getNom(),
				encuesta.getMsj_ini(),
				encuesta.getMsj_fin(),
				encuesta.getFec_ini(),
				encuesta.getFec_fin(),
				encuesta.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_encuesta where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Encuesta> list() {
		String sql = "select * from col_encuesta";
		
		System.out.println(sql);
		
		List<Encuesta> listEncuesta = jdbcTemplate.query(sql, new RowMapper<Encuesta>() {

			@Override
			public Encuesta mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listEncuesta;
	}

	public Encuesta get(int id) {
		String sql = "select * from col_encuesta WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Encuesta>() {

			@Override
			public Encuesta extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Encuesta getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select enc.id enc_id, enc.id_anio enc_id_anio , enc.id_gir enc_id_gir , enc.nom enc_nom , enc.msj_ini enc_msj_ini , enc.msj_fin enc_msj_fin , enc.fec_ini enc_fec_ini , enc.fec_fin enc_fec_fin  ,enc.est enc_est ";
		if (aTablas.contains("col_anio"))
			sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		if (aTablas.contains("ges_giro_negocio"))
			sql = sql + ", gir.id gir_id  , gir.id_emp gir_id_emp , gir.nom gir_nom , gir.des gir_des  ";
	
		sql = sql + " from col_encuesta enc "; 
		if (aTablas.contains("col_anio"))
			sql = sql + " left join col_anio anio on anio.id = enc.id_anio ";
		if (aTablas.contains("ges_giro_negocio"))
			sql = sql + " left join ges_giro_negocio gir on gir.id = enc.id_gir ";
		sql = sql + " where enc.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Encuesta>() {
		
			@Override
			public Encuesta extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Encuesta encuesta= rsToEntity(rs,"enc_");
					if (aTablas.contains("col_anio")){
						Anio anio = new Anio();  
							anio.setId(rs.getInt("anio_id")) ;  
							anio.setNom(rs.getString("anio_nom")) ;  
							encuesta.setAnio(anio);
					}
					if (aTablas.contains("ges_giro_negocio")){
						GiroNegocio gironegocio = new GiroNegocio();  
							gironegocio.setId(rs.getInt("gir_id")) ;  
							gironegocio.setId_emp(rs.getInt("gir_id_emp")) ;  
							gironegocio.setNom(rs.getString("gir_nom")) ;  
							gironegocio.setDes(rs.getString("gir_des")) ;  
							encuesta.setGiroNegocio(gironegocio);
					}
							return encuesta;
				}
				
				return null;
			}
			
		});


	}		
	
	public Encuesta getByParams(Param param) {

		String sql = "select * from col_encuesta " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Encuesta>() {
			@Override
			public Encuesta extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Encuesta> listByParams(Param param, String[] order) {

		String sql = "select * from col_encuesta " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Encuesta>() {

			@Override
			public Encuesta mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Encuesta> listFullByParams(Encuesta encuesta, String[] order) {
	
		return listFullByParams(Param.toParam("enc",encuesta), order);
	
	}	
	
	public List<Encuesta> listFullByParams(Param param, String[] order) {

		String sql = "select enc.id enc_id, enc.id_anio enc_id_anio , enc.id_gir enc_id_gir , enc.nom enc_nom , enc.msj_ini enc_msj_ini , enc.msj_fin enc_msj_fin , enc.fec_ini enc_fec_ini , enc.fec_fin enc_fec_fin  ,enc.est enc_est ";
		sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		sql = sql + ", gir.id gir_id  , gir.id_emp gir_id_emp , gir.nom gir_nom , gir.des gir_des  ";
		sql = sql + " from col_encuesta enc";
		sql = sql + " left join col_anio anio on anio.id = enc.id_anio ";
		sql = sql + " left join ges_giro_negocio gir on gir.id = enc.id_gir ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Encuesta>() {

			@Override
			public Encuesta mapRow(ResultSet rs, int rowNum) throws SQLException {
				Encuesta encuesta= rsToEntity(rs,"enc_");
				Anio anio = new Anio();  
				anio.setId(rs.getInt("anio_id")) ;  
				anio.setNom(rs.getString("anio_nom")) ;  
				encuesta.setAnio(anio);
				GiroNegocio gironegocio = new GiroNegocio();  
				gironegocio.setId(rs.getInt("gir_id")) ;  
				gironegocio.setId_emp(rs.getInt("gir_id_emp")) ;  
				gironegocio.setNom(rs.getString("gir_nom")) ;  
				gironegocio.setDes(rs.getString("gir_des")) ;  
				encuesta.setGiroNegocio(gironegocio);
				return encuesta;
			}

		});

	}	


	public List<EncuestaPreg> getListEncuestaPreg(Param param, String[] order) {
		String sql = "select * from col_encuesta_preg " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<EncuestaPreg>() {

			@Override
			public EncuestaPreg mapRow(ResultSet rs, int rowNum) throws SQLException {
				EncuestaPreg encuesta_preg = new EncuestaPreg();

				encuesta_preg.setId(rs.getInt("id"));
				encuesta_preg.setId_enc(rs.getInt("id_enc"));
				encuesta_preg.setId_ctp(rs.getInt("id_ctp"));
				encuesta_preg.setPre(rs.getString("pre"));
				encuesta_preg.setOrd(rs.getInt("ord"));
				encuesta_preg.setDep(rs.getString("dep"));
				encuesta_preg.setEst(rs.getString("est"));
												
				return encuesta_preg;
			}

		});	
	}
	public List<EncuestaAlumno> getListEncuestaAlumno(Param param, String[] order) {
		String sql = "select * from col_encuesta_alumno " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<EncuestaAlumno>() {

			@Override
			public EncuestaAlumno mapRow(ResultSet rs, int rowNum) throws SQLException {
				EncuestaAlumno encuesta_alumno = new EncuestaAlumno();

				encuesta_alumno.setId(rs.getInt("id"));
				encuesta_alumno.setId_enc(rs.getInt("id_enc"));
				encuesta_alumno.setId_mat(rs.getInt("id_mat"));
				encuesta_alumno.setNum(rs.getString("num"));
				encuesta_alumno.setPtje(rs.getInt("ptje"));
				encuesta_alumno.setRes(rs.getString("res"));
				encuesta_alumno.setEst(rs.getString("est"));
												
				return encuesta_alumno;
			}

		});	
	}


	// funciones privadas utilitarias para Encuesta

	private Encuesta rsToEntity(ResultSet rs,String alias) throws SQLException {
		Encuesta encuesta = new Encuesta();

		encuesta.setId(rs.getInt( alias + "id"));
		encuesta.setId_anio(rs.getInt( alias + "id_anio"));
		encuesta.setId_gir(rs.getInt( alias + "id_gir"));
		encuesta.setNom(rs.getString( alias + "nom"));
		encuesta.setMsj_ini(rs.getString( alias + "msj_ini"));
		encuesta.setMsj_fin(rs.getString( alias + "msj_fin"));
		encuesta.setFec_ini(rs.getDate( alias + "fec_ini"));
		encuesta.setFec_fin(rs.getDate( alias + "fec_fin"));
		encuesta.setEst(rs.getString( alias + "est"));
								
		return encuesta;

	}
	
}

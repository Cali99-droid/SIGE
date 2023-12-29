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
import com.tesla.colegio.model.InscripcionCampus;

import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.UsuarioCampus;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface InscripcionCampusDAO.
 * @author MV
 *
 */
public class InscripcionCampusDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(InscripcionCampus inscripcion_campus) {
		if (inscripcion_campus.getId() != null) {
			// update
			String sql = "UPDATE cvi_inscripcion_campus "
						+ "SET id_alu=?, "
						+ "id_fam=?, "
						+ "id_anio=?, "
						+ "tc_acept=?, "
						+ "tc_not_acept_mot=?, "
						+ "id_error=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						inscripcion_campus.getId_alu(),
						inscripcion_campus.getId_fam(),
						inscripcion_campus.getId_anio(),
						inscripcion_campus.getTc_acept(),
						inscripcion_campus.getTc_not_acept_mot(),
						inscripcion_campus.getId_error(),
						inscripcion_campus.getEst(),
						inscripcion_campus.getUsr_act(),
						new java.util.Date(),
						inscripcion_campus.getId()); 
			return inscripcion_campus.getId();

		} else {
			// insert
			String sql = "insert into cvi_inscripcion_campus ("
						+ "id_alu, "
						+ "id_fam, "
						+ "id_anio, "
						+ "tc_acept, "
						+ "tc_not_acept_mot, "
						+ "id_error, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				inscripcion_campus.getId_alu(),
				inscripcion_campus.getId_fam(),
				inscripcion_campus.getId_anio(),
				inscripcion_campus.getTc_acept(),
				inscripcion_campus.getTc_not_acept_mot(),
				inscripcion_campus.getId_error(),
				inscripcion_campus.getEst(),
				inscripcion_campus.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cvi_inscripcion_campus where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<InscripcionCampus> list() {
		String sql = "select * from cvi_inscripcion_campus";
		
		//System.out.println(sql);
		
		List<InscripcionCampus> listInscripcionCampus = jdbcTemplate.query(sql, new RowMapper<InscripcionCampus>() {

			@Override
			public InscripcionCampus mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listInscripcionCampus;
	}

	public InscripcionCampus get(int id) {
		String sql = "select * from cvi_inscripcion_campus WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<InscripcionCampus>() {

			@Override
			public InscripcionCampus extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public InscripcionCampus getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cvic.id cvic_id, cvic.id_alu cvic_id_alu , cvic.id_fam cvic_id_fam , cvic.id_anio cvic_id_anio , cvic.tc_acept cvic_tc_acept , cvic.tc_not_acept_mot cvic_tc_not_acept_mot , cvic.id_error cvic_id_error  ,cvic.est cvic_est ";
		if (aTablas.contains("alu_alumno"))
			sql = sql + ", alu.id alu_id  , alu.id_tdc alu_id_tdc , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen , alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";
		if (aTablas.contains("alu_familiar"))
			sql = sql + ", fam.id fam_id  , fam.nom fam_nom  ";
		if (aTablas.contains("col_anio"))
			sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
	
		sql = sql + " from cvi_inscripcion_campus cvic "; 
		if (aTablas.contains("alu_alumno"))
			sql = sql + " left join alu_alumno alu on alu.id = cvic.id_alu ";
		if (aTablas.contains("alu_familiar"))
			sql = sql + " left join alu_familiar fam on fam.id = cvic.id_fam ";
		if (aTablas.contains("col_anio"))
			sql = sql + " left join col_anio anio on anio.id = cvic.id_anio ";
		sql = sql + " where cvic.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<InscripcionCampus>() {
		
			@Override
			public InscripcionCampus extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					InscripcionCampus inscripcioncampus= rsToEntity(rs,"cvic_");
					if (aTablas.contains("alu_alumno")){
						Alumno alumno = new Alumno();  
							alumno.setId(rs.getInt("alu_id")) ;  
							alumno.setId_tdc(rs.getInt("alu_id_tdc")) ;  
							alumno.setId_idio1(rs.getInt("alu_id_idio1")) ;  
							alumno.setId_idio2(rs.getInt("alu_id_idio2")) ;  
							alumno.setId_eci(rs.getInt("alu_id_eci")) ;  
							alumno.setId_tap(rs.getString("alu_id_tap")) ;  
							alumno.setId_gen(rs.getString("alu_id_gen")) ;  
							alumno.setCod(rs.getString("alu_cod")) ;  
							alumno.setNro_doc(rs.getString("alu_nro_doc")) ;  
							alumno.setNom(rs.getString("alu_nom")) ;  
							alumno.setApe_pat(rs.getString("alu_ape_pat")) ;  
							alumno.setApe_mat(rs.getString("alu_ape_mat")) ;  
							//alumno.setFec_nac(rs.getString("alu_fec_nac")) ;  
							alumno.setNum_hij(rs.getInt("alu_num_hij")) ;  
							alumno.setDireccion(rs.getString("alu_direccion")) ;  
							alumno.setTelf(rs.getString("alu_telf")) ;  
							alumno.setCelular(rs.getString("alu_celular")) ;  
							alumno.setPass_educando(rs.getString("alu_pass_educando")) ;  
							//alumno.setFoto(rs.getString("alu_foto")) ;  
							inscripcioncampus.setAlumno(alumno);
					}
					if (aTablas.contains("alu_familiar")){
						Familiar familiar = new Familiar();  
							familiar.setId(rs.getInt("fam_id")) ;  
							familiar.setNom(rs.getString("fam_nom")) ;  
							inscripcioncampus.setFamiliar(familiar);
					}
					if (aTablas.contains("col_anio")){
						Anio anio = new Anio();  
							anio.setId(rs.getInt("anio_id")) ;  
							anio.setNom(rs.getString("anio_nom")) ;  
							inscripcioncampus.setAnio(anio);
					}
							return inscripcioncampus;
				}
				
				return null;
			}
			
		});


	}		
	
	public InscripcionCampus getByParams(Param param) {

		String sql = "select * from cvi_inscripcion_campus " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<InscripcionCampus>() {
			@Override
			public InscripcionCampus extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<InscripcionCampus> listByParams(Param param, String[] order) {

		String sql = "select * from cvi_inscripcion_campus " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<InscripcionCampus>() {

			@Override
			public InscripcionCampus mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<InscripcionCampus> listFullByParams(InscripcionCampus inscripcioncampus, String[] order) {
	
		return listFullByParams(Param.toParam("cvic",inscripcioncampus), order);
	
	}	
	
	public List<InscripcionCampus> listFullByParams(Param param, String[] order) {

		String sql = "select cvic.id cvic_id, cvic.id_alu cvic_id_alu , cvic.id_fam cvic_id_fam , cvic.id_anio cvic_id_anio , cvic.tc_acept cvic_tc_acept , cvic.tc_not_acept_mot cvic_tc_not_acept_mot , cvic.id_error cvic_id_error  ,cvic.est cvic_est ";
		sql = sql + ", alu.id alu_id  , alu.id_tdc alu_id_tdc , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen , alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";
		sql = sql + ", fam.id fam_id  , fam.nom fam_nom  ";
		sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		sql = sql + " from cvi_inscripcion_campus cvic";
		sql = sql + " left join alu_alumno alu on alu.id = cvic.id_alu ";
		sql = sql + " left join alu_familiar fam on fam.id = cvic.id_fam ";
		sql = sql + " left join col_anio anio on anio.id = cvic.id_anio ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<InscripcionCampus>() {

			@Override
			public InscripcionCampus mapRow(ResultSet rs, int rowNum) throws SQLException {
				InscripcionCampus inscripcioncampus= rsToEntity(rs,"cvic_");
				Alumno alumno = new Alumno();  
				alumno.setId(rs.getInt("alu_id")) ;  
				alumno.setId_tdc(rs.getInt("alu_id_tdc")) ;  
				alumno.setId_idio1(rs.getInt("alu_id_idio1")) ;  
				alumno.setId_idio2(rs.getInt("alu_id_idio2")) ;  
				alumno.setId_eci(rs.getInt("alu_id_eci")) ;  
				alumno.setId_tap(rs.getString("alu_id_tap")) ;  
				alumno.setId_gen(rs.getString("alu_id_gen")) ;  
				alumno.setCod(rs.getString("alu_cod")) ;  
				alumno.setNro_doc(rs.getString("alu_nro_doc")) ;  
				alumno.setNom(rs.getString("alu_nom")) ;  
				alumno.setApe_pat(rs.getString("alu_ape_pat")) ;  
				alumno.setApe_mat(rs.getString("alu_ape_mat")) ;  
				//alumno.setFec_nac(rs.getString("alu_fec_nac")) ;  
				alumno.setNum_hij(rs.getInt("alu_num_hij")) ;  
				alumno.setDireccion(rs.getString("alu_direccion")) ;  
				alumno.setTelf(rs.getString("alu_telf")) ;  
				alumno.setCelular(rs.getString("alu_celular")) ;  
				alumno.setPass_educando(rs.getString("alu_pass_educando")) ;  
				//alumno.setFoto(rs.getString("alu_foto")) ;  
				inscripcioncampus.setAlumno(alumno);
				Familiar familiar = new Familiar();  
				familiar.setId(rs.getInt("fam_id")) ;  
				familiar.setNom(rs.getString("fam_nom")) ;  
				inscripcioncampus.setFamiliar(familiar);
				Anio anio = new Anio();  
				anio.setId(rs.getInt("anio_id")) ;  
				anio.setNom(rs.getString("anio_nom")) ;  
				inscripcioncampus.setAnio(anio);
				return inscripcioncampus;
			}

		});

	}	


	public List<UsuarioCampus> getListUsuarioCampus(Param param, String[] order) {
		String sql = "select * from cvi_usuario_campus " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<UsuarioCampus>() {

			@Override
			public UsuarioCampus mapRow(ResultSet rs, int rowNum) throws SQLException {
				UsuarioCampus usuario_campus = new UsuarioCampus();

				usuario_campus.setId(rs.getInt("id"));
				usuario_campus.setId_cvic(rs.getInt("id_cvic"));
				usuario_campus.setUsr(rs.getString("usr"));
				usuario_campus.setPsw(rs.getString("psw"));
				usuario_campus.setId_error(rs.getInt("id_error"));
				usuario_campus.setEst(rs.getString("est"));
												
				return usuario_campus;
			}

		});	
	}


	// funciones privadas utilitarias para InscripcionCampus

	private InscripcionCampus rsToEntity(ResultSet rs,String alias) throws SQLException {
		InscripcionCampus inscripcion_campus = new InscripcionCampus();

		inscripcion_campus.setId(rs.getInt( alias + "id"));
		inscripcion_campus.setId_alu(rs.getInt( alias + "id_alu"));
		inscripcion_campus.setId_fam(rs.getInt( alias + "id_fam"));
		inscripcion_campus.setId_anio(rs.getInt( alias + "id_anio"));
		inscripcion_campus.setTc_acept(rs.getString( alias + "tc_acept"));
		inscripcion_campus.setTc_not_acept_mot(rs.getString( alias + "tc_not_acept_mot"));
		inscripcion_campus.setId_error(rs.getInt( alias + "id_error"));
		inscripcion_campus.setEst(rs.getString( alias + "est"));
								
		return inscripcion_campus;

	}
	
}

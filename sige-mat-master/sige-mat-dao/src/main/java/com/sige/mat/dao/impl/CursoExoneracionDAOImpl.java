package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.tesla.colegio.model.CursoExoneracion;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.Curso;
import com.tesla.colegio.model.Matricula;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CursoExoneracionDAO.
 * @author MV
 *
 */
public class CursoExoneracionDAOImpl{
	final static Logger logger = Logger.getLogger(CursoExoneracionDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(CursoExoneracion curso_exoneracion) {
		if (curso_exoneracion.getId() != null) {
			// update
			String sql = "UPDATE not_curso_exoneracion "
						+ "SET id_cur=?, "
						+ "id_mat=?, "
						+ "fecha=?, "
						+ "motivo=?, "
						+ "resol=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						curso_exoneracion.getId_cur(),
						curso_exoneracion.getId_mat(),
						curso_exoneracion.getFecha(),
						curso_exoneracion.getMotivo(),
						curso_exoneracion.getResol(),
						curso_exoneracion.getEst(),
						curso_exoneracion.getUsr_act(),
						new java.util.Date(),
						curso_exoneracion.getId()); 
			return curso_exoneracion.getId();

		} else {
			// insert
			String sql = "insert into not_curso_exoneracion ("
						+ "id_cur, "
						+ "id_mat, "
						+ "fecha, "
						+ "motivo, "
						+ "resol, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				curso_exoneracion.getId_cur(),
				curso_exoneracion.getId_mat(),
				curso_exoneracion.getFecha(),
				curso_exoneracion.getMotivo(),
				curso_exoneracion.getResol(),
				curso_exoneracion.getEst(),
				curso_exoneracion.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from not_curso_exoneracion where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<CursoExoneracion> list() {
		String sql = "select * from not_curso_exoneracion";
		
		//logger.info(sql);
		
		List<CursoExoneracion> listCursoExoneracion = jdbcTemplate.query(sql, new RowMapper<CursoExoneracion>() {

			@Override
			public CursoExoneracion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCursoExoneracion;
	}

	public CursoExoneracion get(int id) {
		String sql = "select * from not_curso_exoneracion WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoExoneracion>() {

			@Override
			public CursoExoneracion extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CursoExoneracion getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select nce.id nce_id, nce.id_cur nce_id_cur , nce.id_mat nce_id_mat , nce.fecha nce_fecha , nce.motivo nce_motivo , nce.resol nce_resol  ,nce.est nce_est ";
	
		if (aTablas.contains("cat_curso"))
			sql = sql + ", cur.id cur_id  , cur.nom cur_nom  ";
		
		if (aTablas.contains("alu_alumno"))
			sql = sql + ", alu.id alu_id  , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat  ";
		
		if (aTablas.contains("mat_matricula"))
			sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		
		sql = sql + " from not_curso_exoneracion nce "; 
		
		if (aTablas.contains("cat_curso"))
			sql = sql + " left join cat_curso cur on cur.id = nce.id_cur ";
		
		if (aTablas.contains("mat_matricula"))
			sql = sql + " left join mat_matricula mat on mat.id = nce.id_mat ";
		
		if (aTablas.contains("alu_alumno"))
			sql = sql + " left join alu_alumno alu on alu.id = mat.id_alu ";
		
		sql = sql + " where nce.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoExoneracion>() {
		
			@Override
			public CursoExoneracion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CursoExoneracion cursoexoneracion= rsToEntity(rs,"nce_");
					if (aTablas.contains("cat_curso")){
						Curso curso = new Curso();  
							curso.setId(rs.getInt("cur_id")) ;  
							curso.setNom(rs.getString("cur_nom")) ;  
							cursoexoneracion.setCurso(curso);
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
							matricula.setId_au(rs.getInt("mat_id_au")) ;  
							matricula.setId_gra(rs.getInt("mat_id_gra")) ;  
							matricula.setId_niv(rs.getInt("mat_id_niv")) ;  
							matricula.setFecha(rs.getDate("mat_fecha")) ;  
							matricula.setCar_pod(rs.getString("mat_car_pod")) ;  
							matricula.setNum_cont(rs.getString("mat_num_cont")) ;  
							matricula.setObs(rs.getString("mat_obs")) ;  
							
							if (aTablas.contains("alu_alumno")){
								Alumno alumno= new Alumno();
								alumno.setApe_pat(rs.getString("alu_ape_pat"));
								alumno.setApe_mat(rs.getString("alu_ape_mat"));
								alumno.setNom(rs.getString("alu_nom"));
								alumno.setId(rs.getInt("alu_id"));

								matricula.setAlumno(alumno);
							}
							cursoexoneracion.setMatricula(matricula);
					}
							return cursoexoneracion;
				}
				
				return null;
			}
			
		});


	}		
	
	public CursoExoneracion getByParams(Param param) {

		String sql = "select * from not_curso_exoneracion " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoExoneracion>() {
			@Override
			public CursoExoneracion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CursoExoneracion> listByParams(Param param, String[] order) {

		String sql = "select * from not_curso_exoneracion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<CursoExoneracion>() {

			@Override
			public CursoExoneracion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CursoExoneracion> listFullByParams(CursoExoneracion cursoexoneracion, String[] order) {
	
		return listFullByParams(Param.toParam("nce",cursoexoneracion), order);
	
	}	
	
	public List<CursoExoneracion> listFullByParams(Param param, String[] order) {

		String sql = "select nce.id nce_id, nce.id_cur nce_id_cur , nce.id_mat nce_id_mat , nce.fecha nce_fecha , nce.motivo nce_motivo , nce.resol nce_resol  ,nce.est nce_est ";
		sql = sql + ", cur.id cur_id  , cur.nom cur_nom  ";
		sql = sql + ", alu.id alu_id  , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat, alu.nro_doc  alu_nro_doc ";
		sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		sql = sql + " from not_curso_exoneracion nce";
		sql = sql + " left join cat_curso cur on cur.id = nce.id_cur ";
		sql = sql + " left join mat_matricula mat on mat.id = nce.id_mat ";
		sql = sql + " left join alu_alumno alu on alu.id = mat.id_alu ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<CursoExoneracion>() {

			@Override
			public CursoExoneracion mapRow(ResultSet rs, int rowNum) throws SQLException {
				CursoExoneracion cursoexoneracion= rsToEntity(rs,"nce_");
				Curso curso = new Curso();  
				curso.setId(rs.getInt("cur_id")) ;  
				curso.setNom(rs.getString("cur_nom")) ;  
				cursoexoneracion.setCurso(curso);
				Matricula matricula = new Matricula();  
				matricula.setId(rs.getInt("mat_id")) ;  
				matricula.setId_alu(rs.getInt("mat_id_alu")) ;  
				matricula.setId_fam(rs.getInt("mat_id_fam")) ;  
				matricula.setId_enc(rs.getInt("mat_id_enc")) ;  
				matricula.setId_con(rs.getInt("mat_id_con")) ;  
				matricula.setId_cli(rs.getInt("mat_id_cli")) ;  
				matricula.setId_per(rs.getInt("mat_id_per")) ;  
				matricula.setId_au(rs.getInt("mat_id_au")) ;  
				matricula.setId_gra(rs.getInt("mat_id_gra")) ;  
				matricula.setId_niv(rs.getInt("mat_id_niv")) ;  
				matricula.setFecha(rs.getDate("mat_fecha")) ;  
				matricula.setCar_pod(rs.getString("mat_car_pod")) ;  
				matricula.setNum_cont(rs.getString("mat_num_cont")) ;  
				matricula.setObs(rs.getString("mat_obs")) ;  
				
				Alumno alumno= new Alumno();
				alumno.setApe_pat(rs.getString("alu_ape_pat"));
				alumno.setApe_mat(rs.getString("alu_ape_mat"));
				alumno.setNom(rs.getString("alu_nom"));
				alumno.setNro_doc(rs.getString("alu_nro_doc"));
				alumno.setId(rs.getInt("alu_id"));

				matricula.setAlumno(alumno);

				cursoexoneracion.setMatricula(matricula);
				return cursoexoneracion;
			}

		});

	}	




	// funciones privadas utilitarias para CursoExoneracion

	private CursoExoneracion rsToEntity(ResultSet rs,String alias) throws SQLException {
		CursoExoneracion curso_exoneracion = new CursoExoneracion();

		curso_exoneracion.setId(rs.getInt( alias + "id"));
		curso_exoneracion.setId_cur(rs.getInt( alias + "id_cur"));
		curso_exoneracion.setId_mat(rs.getInt( alias + "id_mat"));
		curso_exoneracion.setFecha(rs.getDate( alias + "fecha"));
		curso_exoneracion.setMotivo(rs.getString( alias + "motivo"));
		curso_exoneracion.setResol(rs.getString( alias + "resol"));
		curso_exoneracion.setEst(rs.getString( alias + "est"));
								
		return curso_exoneracion;

	}
	
}

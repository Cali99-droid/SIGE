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

import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.AulaEspecial;

import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.Matricula;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface AulaEspecialDAO.
 * @author MV
 *
 */
public class AulaEspecialDAOImpl{
	
	final static Logger logger = Logger.getLogger(AulaEspecialDAOImpl.class);
	
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(AulaEspecial aula_especial) {
		if (aula_especial.getId() != null) {
			// update
			String sql = "UPDATE col_aula_especial "
						+ "SET id_gra=?, "
						+ "id_mat=?, "
						+ "id_au=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						aula_especial.getId_gra(),
						aula_especial.getId_mat(),
						aula_especial.getId_au(),
						aula_especial.getEst(),
						aula_especial.getUsr_act(),
						new java.util.Date(),
						aula_especial.getId()); 
			return aula_especial.getId();

		} else {
			// insert
			String sql = "insert into col_aula_especial ("
						+ "id_gra, "
						+ "id_mat, "
						+ "id_au, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				aula_especial.getId_gra(),
				aula_especial.getId_mat(),
				aula_especial.getId_au(),
				aula_especial.getEst(),
				aula_especial.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_aula_especial where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<AulaEspecial> list() {
		String sql = "select * from col_aula_especial";
		
		////logger.info(sql);
		
		List<AulaEspecial> listAulaEspecial = jdbcTemplate.query(sql, new RowMapper<AulaEspecial>() {

			@Override
			public AulaEspecial mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listAulaEspecial;
	}

	public AulaEspecial get(int id) {
		String sql = "select * from col_aula_especial WHERE id=" + id;
		
		////logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<AulaEspecial>() {

			@Override
			public AulaEspecial extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public AulaEspecial getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cae.id cae_id, cae.id_gra cae_id_gra , cae.id_mat cae_id_mat , cae.id_au cae_id_au  ,cae.est cae_est ";
		if (aTablas.contains("cat_grad"))
			sql = sql + ", gra.id gra_id  , gra.id_nvl gra_id_nvl , gra.id_gra_ant gra_id_gra_ant , gra.nom gra_nom , gra.tipo gra_tipo  ";
		if (aTablas.contains("mat_matricula"))
			sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
	
		sql = sql + " from col_aula_especial cae "; 
		if (aTablas.contains("cat_grad"))
			sql = sql + " left join cat_grad gra on gra.id = cae.id_gra ";
		if (aTablas.contains("mat_matricula"))
			sql = sql + " left join mat_matricula mat on mat.id = cae.id_mat ";
		sql = sql + " where cae.id= " + id; 
				
		////logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<AulaEspecial>() {
		
			@Override
			public AulaEspecial extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					AulaEspecial aulaespecial= rsToEntity(rs,"cae_");
					if (aTablas.contains("cat_grad")){
						Grad grad = new Grad();  
							grad.setId(rs.getInt("gra_id")) ;  
							grad.setId_nvl(rs.getInt("gra_id_nvl")) ;  
							grad.setId_gra_ant(rs.getInt("gra_id_gra_ant")) ;  
							grad.setNom(rs.getString("gra_nom")) ;  
							grad.setTipo(rs.getString("gra_tipo")) ;  
							aulaespecial.setGrad(grad);
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
							aulaespecial.setMatricula(matricula);
					}
							return aulaespecial;
				}
				
				return null;
			}
			
		});


	}		
	
	public AulaEspecial getByParams(Param param) {

		String sql = "select * from col_aula_especial " + SQLFrmkUtil.getWhere(param);
		
		////logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<AulaEspecial>() {
			@Override
			public AulaEspecial extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<AulaEspecial> listByParams(Param param, String[] order) {

		String sql = "select * from col_aula_especial " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		////logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<AulaEspecial>() {

			@Override
			public AulaEspecial mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<AulaEspecial> listFullByParams(AulaEspecial aulaespecial, String[] order) {
	
		return listFullByParams(Param.toParam("cae",aulaespecial), order);
	
	}	
	
	public List<AulaEspecial> listFullByParams(Param param, String[] order) {

		String sql = "select cae.id cae_id, cae.id_gra cae_id_gra , cae.id_mat cae_id_mat  , cae.id_au cae_id_au  ,cae.est cae_est ";
		sql = sql + ", gra.id gra_id  , gra.id_nvl gra_id_nvl , gra.id_gra_ant gra_id_gra_ant , gra.nom gra_nom , gra.tipo gra_tipo  ";
		sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		sql = sql + ", alu.id alu_id, alu.nom alu_nom, alu.ape_pat alu_ape_pat, alu.ape_mat alu_ape_mat ";
		sql = sql + ", au.id au_id  , au.secc au_secc ";
		sql = sql + ", gra_mat.id gra_mat_id  , gra_mat.id_nvl gra_mat_id_nvl , gra_mat.nom gra_mat_nom ";
		sql = sql + " from col_aula_especial cae";
		sql = sql + " left join cat_grad gra on gra.id = cae.id_gra ";
		sql = sql + " left join mat_matricula mat on mat.id = cae.id_mat ";
		sql = sql + " left join per_periodo per on per.id = mat.id_per ";
		sql = sql + " left join alu_alumno alu on alu.id = mat.id_alu ";
		sql = sql + " left join col_aula au on au.id = mat.id_au ";
		sql = sql + " left join cat_grad gra_mat on gra_mat.id = au.id_grad ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<AulaEspecial>() {

			@Override
			public AulaEspecial mapRow(ResultSet rs, int rowNum) throws SQLException {
				AulaEspecial aulaespecial= rsToEntity(rs,"cae_");
				
				Grad grad = new Grad();  
				grad.setId(rs.getInt("gra_id")) ;  
				grad.setId_nvl(rs.getInt("gra_id_nvl")) ;  
				grad.setId_gra_ant(rs.getInt("gra_id_gra_ant")) ;  
				grad.setNom(rs.getString("gra_nom")) ;  
				grad.setTipo(rs.getString("gra_tipo")) ;  
				aulaespecial.setGrad(grad);
				
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
				alumno.setId(rs.getInt("alu_id")) ;  
				alumno.setNom(rs.getString("alu_nom")) ;  
				alumno.setApe_mat(rs.getString("alu_ape_mat")) ;  
				alumno.setApe_pat(rs.getString("alu_ape_pat")) ;  
				matricula.setAlumno(alumno); 
				
				Grad gradoMat= new Grad();  
				gradoMat.setId(rs.getInt("gra_mat_id")) ;  
				gradoMat.setNom(rs.getString("gra_mat_nom")) ;  
				matricula.setGrad(gradoMat); 
				

				Aula aula = new Aula();  
				aula.setId(rs.getInt("au_id")) ;  
				aula.setSecc(rs.getString("au_secc")) ;  
				matricula.setAula(aula); 
				
				aulaespecial.setMatricula(matricula);
				return aulaespecial;
			}

		});

	}	




	// funciones privadas utilitarias para AulaEspecial

	private AulaEspecial rsToEntity(ResultSet rs,String alias) throws SQLException {
		AulaEspecial aula_especial = new AulaEspecial();

		aula_especial.setId(rs.getInt( alias + "id"));
		aula_especial.setId_gra(rs.getInt( alias + "id_gra"));
		aula_especial.setId_mat(rs.getInt( alias + "id_mat"));
		aula_especial.setId_au(rs.getInt( alias + "id_au"));
		aula_especial.setEst(rs.getString( alias + "est"));
								
		return aula_especial;

	}
	
}

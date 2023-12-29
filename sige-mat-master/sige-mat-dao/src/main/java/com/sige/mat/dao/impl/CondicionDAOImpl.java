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
import com.tesla.colegio.model.Condicion;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.CondAlumno;
import com.tesla.colegio.model.Matricula;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CondicionDAO.
 * @author MV
 *
 */
public class CondicionDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Condicion condicion) {
		if (condicion.getId() != null) {
			// update
			String sql = "UPDATE mat_condicion "
						+ "SET id_cond=?, "
						+ "id_mat=?, "
						+ "des=?, "
						+ "mat_blo=?, "
						+ "obs_blo=?, "
						+ "tip_blo=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						condicion.getId_cond(),
						condicion.getId_mat(),
						condicion.getDes(),
						condicion.getMat_blo(),
						condicion.getObs_blo(),
						condicion.getTip_blo(),
						condicion.getEst(),
						condicion.getUsr_act(),
						new java.util.Date(),
						condicion.getId()); 
			return condicion.getId();

		} else {
			// insert
			String sql = "insert into mat_condicion ("
						+ "id_cond, "
						+ "id_mat, "
						+ "des, "
						+ "mat_blo, "
						+ "obs_blo, "
						+ "tip_blo, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				condicion.getId_cond(),
				condicion.getId_mat(),
				condicion.getDes(),
				condicion.getMat_blo(),
				condicion.getObs_blo(),
				condicion.getTip_blo(),
				condicion.getEst(),
				condicion.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from mat_condicion where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<Condicion> list() {
		String sql = "select * from mat_condicion";
		
		
		
		List<Condicion> listCondicion = jdbcTemplate.query(sql, new RowMapper<Condicion>() {

			@Override
			public Condicion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCondicion;
	}

	public Condicion get(int id) {
		String sql = "select * from mat_condicion WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Condicion>() {

			@Override
			public Condicion extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Condicion getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select mc.id mc_id, mc.id_cond mc_id_cond , mc.id_mat mc_id_mat  ,mc.est mc_est ";
		if (aTablas.contains("cat_cond_alumno"))
			sql = sql + ", cond.id cond_id  , cond.id_ctc cond_id_ctc , cond.nom cond_nom , cond.des cond_des  ";
		if (aTablas.contains("mat_matricula"))
			sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
	
		sql = sql + " from mat_condicion mc "; 
		if (aTablas.contains("cat_cond_alumno"))
			sql = sql + " left join cat_cond_alumno cond on cond.id = mc.id_cond ";
		if (aTablas.contains("mat_matricula"))
			sql = sql + " left join mat_matricula mat on mat.id = mc.id_mat ";
		sql = sql + " where mc.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<Condicion>() {
		
			@Override
			public Condicion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Condicion condicion= rsToEntity(rs,"mc_");
					if (aTablas.contains("cat_cond_alumno")){
						CondAlumno condalumno = new CondAlumno();  
							condalumno.setId(rs.getInt("cond_id")) ;  
							condalumno.setId_ctc(rs.getInt("cond_id_ctc")) ;  
							condalumno.setNom(rs.getString("cond_nom")) ;  
							condalumno.setDes(rs.getString("cond_des")) ;  
							condicion.setCondAlumno(condalumno);
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
							condicion.setMatricula(matricula);
					}
							return condicion;
				}
				
				return null;
			}
			
		});


	}		
	
	public Condicion getByParams(Param param) {

		String sql = "select * from mat_condicion " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Condicion>() {
			@Override
			public Condicion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Condicion> listByParams(Param param, String[] order) {

		String sql = "select * from mat_condicion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<Condicion>() {

			@Override
			public Condicion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	/*
	public List<Condicion> listFullByParams(Condicion condicion, String[] order) {
	
		return listFullByParams(Param.toParam("mc",condicion), order);
	
	}	*/
	
	public List<Condicion> listFullByParams(Param param, String[] order) {

		String sql = "select mc.id mc_id, mc.id_cond mc_id_cond , mc.id_mat mc_id_mat  ,mc.est mc_est, mc.des mc_des, mc.mat_blo mc_mat_blo, mc.obs_blo mc_obs_blo, mc.tip_blo mc_tip_blo ";
		sql = sql + ", cond.id cond_id  , cond.id_ctc cond_id_ctc , cond.nom cond_nom , cond.des cond_des  ";
		sql = sql + ", tip.id";
		sql = sql + ", alu.nom alu_nom, alu.ape_pat alu_ape_pat, alu.ape_mat alu_ape_mat, alu.id alu_id"; 
		sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		sql = sql + " from mat_condicion mc";
		sql = sql + " left join cat_cond_alumno cond on cond.id = mc.id_cond ";
		sql = sql + " left join cat_tip_cond tip on cond.id_ctc=tip.id ";
		sql = sql + " left join mat_matricula mat on mat.id = mc.id_mat ";
		sql = sql + " left join alu_alumno alu on mat.id_alu=alu.id ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<Condicion>() {

			@Override
			public Condicion mapRow(ResultSet rs, int rowNum) throws SQLException {
				Condicion condicion= rsToEntity(rs,"mc_");
				CondAlumno condalumno = new CondAlumno();  
				condalumno.setId(rs.getInt("cond_id")) ;  
				condalumno.setId_ctc(rs.getInt("cond_id_ctc")) ;  
				condalumno.setNom(rs.getString("cond_nom")) ;  
				condalumno.setDes(rs.getString("cond_des")) ;  
				condicion.setCondAlumno(condalumno);
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
				condicion.setMatricula(matricula);
				Alumno alumno = new Alumno();
				alumno.setId(rs.getInt("alu_id"));
				alumno.setApe_pat(rs.getString("alu_ape_pat"));
				alumno.setApe_mat(rs.getString("alu_ape_mat"));
				alumno.setNom(rs.getString("alu_nom"));
				condicion.setAlumno(alumno);
				return condicion;
			}

		});

	}	




	// funciones privadas utilitarias para Condicion

	private Condicion rsToEntity(ResultSet rs,String alias) throws SQLException {
		Condicion condicion = new Condicion();

		condicion.setId(rs.getInt( alias + "id"));
		condicion.setId_cond(rs.getInt( alias + "id_cond"));
		condicion.setId_mat(rs.getInt( alias + "id_mat"));
		condicion.setDes(rs.getString(alias+"des"));
		condicion.setMat_blo(rs.getString(alias + "mat_blo"));
		condicion.setObs_blo(rs.getString(alias + "obs_blo"));
		condicion.setTip_blo(rs.getString(alias + "tip_blo"));
		condicion.setEst(rs.getString( alias + "est"));
								
		return condicion;

	}
	
}

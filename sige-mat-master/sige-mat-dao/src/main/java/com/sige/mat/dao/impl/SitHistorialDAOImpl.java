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
import com.tesla.colegio.model.SitHistorial;

import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.ColSituacion;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface SitHistorialDAO.
 * @author MV
 *
 */
public class SitHistorialDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(SitHistorial sit_historial) {
		if (sit_historial.getId() != null) {
			// update
			String sql = "UPDATE mat_sit_historial "
						+ "SET id_mat=?, "
						+ "id_sit=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						sit_historial.getId_mat(),
						sit_historial.getId_sit(),
						sit_historial.getEst(),
						sit_historial.getUsr_act(),
						new java.util.Date(),
						sit_historial.getId()); 
			return sit_historial.getId();

		} else {
			// insert
			String sql = "insert into mat_sit_historial ("
						+ "id_mat, "
						+ "id_sit, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				sit_historial.getId_mat(),
				sit_historial.getId_sit(),
				sit_historial.getEst(),
				sit_historial.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from mat_sit_historial where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<SitHistorial> list() {
		String sql = "select * from mat_sit_historial";
		
		
		
		List<SitHistorial> listSitHistorial = jdbcTemplate.query(sql, new RowMapper<SitHistorial>() {

			@Override
			public SitHistorial mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listSitHistorial;
	}

	public SitHistorial get(int id) {
		String sql = "select * from mat_sit_historial WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SitHistorial>() {

			@Override
			public SitHistorial extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public SitHistorial getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select msh.id msh_id, msh.id_mat msh_id_mat , msh.id_sit msh_id_sit  ,msh.est msh_est ";
		if (aTablas.contains("mat_matricula"))
			sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		if (aTablas.contains("cat_col_situacion"))
			sql = sql + ", cma.id cma_id  , cma.cod cma_cod , cma.nom cma_nom , cma.des cma_des  ";
	
		sql = sql + " from mat_sit_historial msh "; 
		if (aTablas.contains("mat_matricula"))
			sql = sql + " left join mat_matricula mat on mat.id = msh.id_mat ";
		if (aTablas.contains("cat_col_situacion"))
			sql = sql + " left join cat_col_situacion cma on cma.id = msh.id_sit ";
		sql = sql + " where msh.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<SitHistorial>() {
		
			@Override
			public SitHistorial extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					SitHistorial sithistorial= rsToEntity(rs,"msh_");
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
							sithistorial.setMatricula(matricula);
					}
					if (aTablas.contains("cat_col_situacion")){
						ColSituacion colsituacion = new ColSituacion();  
							colsituacion.setId(rs.getInt("cma_id")) ;  
							colsituacion.setCod(rs.getString("cma_cod")) ;  
							colsituacion.setNom(rs.getString("cma_nom")) ;  
							colsituacion.setDes(rs.getString("cma_des")) ;  
							sithistorial.setColSituacion(colsituacion);
					}
							return sithistorial;
				}
				
				return null;
			}
			
		});


	}		
	
	public SitHistorial getByParams(Param param) {

		String sql = "select * from mat_sit_historial " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SitHistorial>() {
			@Override
			public SitHistorial extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<SitHistorial> listByParams(Param param, String[] order) {

		String sql = "select * from mat_sit_historial " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<SitHistorial>() {

			@Override
			public SitHistorial mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<SitHistorial> listFullByParams(SitHistorial sithistorial, String[] order) {
	
		return listFullByParams(Param.toParam("msh",sithistorial), order);
	
	}	
	
	public List<SitHistorial> listFullByParams(Param param, String[] order) {

		String sql = "select msh.id msh_id, msh.id_mat msh_id_mat , msh.id_sit msh_id_sit  ,msh.est msh_est ";
		sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		sql = sql + ", cma.id cma_id  , cma.cod cma_cod , cma.nom cma_nom , cma.des cma_des  ";
		sql = sql + " from mat_sit_historial msh";
		sql = sql + " left join mat_matricula mat on mat.id = msh.id_mat ";
		sql = sql + " left join cat_col_situacion cma on cma.id = msh.id_sit ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<SitHistorial>() {

			@Override
			public SitHistorial mapRow(ResultSet rs, int rowNum) throws SQLException {
				SitHistorial sithistorial= rsToEntity(rs,"msh_");
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
				sithistorial.setMatricula(matricula);
				ColSituacion colsituacion = new ColSituacion();  
				colsituacion.setId(rs.getInt("cma_id")) ;  
				colsituacion.setCod(rs.getString("cma_cod")) ;  
				colsituacion.setNom(rs.getString("cma_nom")) ;  
				colsituacion.setDes(rs.getString("cma_des")) ;  
				sithistorial.setColSituacion(colsituacion);
				return sithistorial;
			}

		});

	}	




	// funciones privadas utilitarias para SitHistorial

	private SitHistorial rsToEntity(ResultSet rs,String alias) throws SQLException {
		SitHistorial sit_historial = new SitHistorial();

		sit_historial.setId(rs.getInt( alias + "id"));
		sit_historial.setId_mat(rs.getInt( alias + "id_mat"));
		sit_historial.setId_sit(rs.getInt( alias + "id_sit"));
		sit_historial.setEst(rs.getString( alias + "est"));
								
		return sit_historial;

	}
	
}

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
import com.tesla.colegio.model.Ratificacion;

import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Anio;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface RatificacionDAO.
 * @author MV
 *
 */
public class RatificacionDAOImpl{
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
	public int saveOrUpdate(Ratificacion ratificacion) {
		if (ratificacion.getId() != null) {
			// update
			String sql = "UPDATE mat_ratificacion "
						+ "SET id_mat=?, "
						+ "id_anio_rat=?, "
						+ "res=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						ratificacion.getId_mat(),
						ratificacion.getId_anio_rat(),
						ratificacion.getRes(),
						ratificacion.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						ratificacion.getId()); 
			return ratificacion.getId();

		} else {
			// insert
			String sql = "insert into mat_ratificacion ("
						+ "id_mat, "
						+ "id_anio_rat, "
						+ "res, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				ratificacion.getId_mat(),
				ratificacion.getId_anio_rat(),
				ratificacion.getRes(),
				ratificacion.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from mat_ratificacion where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Ratificacion> list() {
		String sql = "select * from mat_ratificacion";
		
		System.out.println(sql);
		
		List<Ratificacion> listRatificacion = jdbcTemplate.query(sql, new RowMapper<Ratificacion>() {

			@Override
			public Ratificacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listRatificacion;
	}

	public Ratificacion get(int id) {
		String sql = "select * from mat_ratificacion WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Ratificacion>() {

			@Override
			public Ratificacion extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Ratificacion getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select rat.id rat_id, rat.id_mat rat_id_mat , rat.id_anio_rat rat_id_anio_rat , rat.res rat_res  ,rat.est rat_est ";
		if (aTablas.contains("mat_matricula"))
			sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_cic mat_id_cic , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.tipo mat_tipo , mat.tip_mat mat_tip_mat , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		if (aTablas.contains("col_anio"))
			sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
	
		sql = sql + " from mat_ratificacion rat "; 
		if (aTablas.contains("mat_matricula"))
			sql = sql + " left join mat_matricula mat on mat.id = rat.id_mat ";
		if (aTablas.contains("col_anio"))
			sql = sql + " left join col_anio anio on anio.id = rat.id_anio_rat ";
		sql = sql + " where rat.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Ratificacion>() {
		
			@Override
			public Ratificacion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Ratificacion ratificacion= rsToEntity(rs,"rat_");
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
							ratificacion.setMatricula(matricula);
					}
					if (aTablas.contains("col_anio")){
						Anio anio = new Anio();  
							anio.setId(rs.getInt("anio_id")) ;  
							anio.setNom(rs.getString("anio_nom")) ;  
							ratificacion.setAnio(anio);
					}
							return ratificacion;
				}
				
				return null;
			}
			
		});


	}		
	
	public Ratificacion getByParams(Param param) {

		String sql = "select * from mat_ratificacion " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Ratificacion>() {
			@Override
			public Ratificacion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Ratificacion> listByParams(Param param, String[] order) {

		String sql = "select * from mat_ratificacion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Ratificacion>() {

			@Override
			public Ratificacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Ratificacion> listFullByParams(Ratificacion ratificacion, String[] order) {
	
		return listFullByParams(Param.toParam("rat",ratificacion), order);
	
	}	
	
	public List<Ratificacion> listFullByParams(Param param, String[] order) {

		String sql = "select rat.id rat_id, rat.id_mat rat_id_mat , rat.id_anio_rat rat_id_anio_rat , rat.res rat_res  ,rat.est rat_est ";
		sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_cic mat_id_cic , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.tipo mat_tipo , mat.tip_mat mat_tip_mat , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		sql = sql + " from mat_ratificacion rat";
		sql = sql + " left join mat_matricula mat on mat.id = rat.id_mat ";
		sql = sql + " left join col_anio anio on anio.id = rat.id_anio_rat ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Ratificacion>() {

			@Override
			public Ratificacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				Ratificacion ratificacion= rsToEntity(rs,"rat_");
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
				ratificacion.setMatricula(matricula);
				Anio anio = new Anio();  
				anio.setId(rs.getInt("anio_id")) ;  
				anio.setNom(rs.getString("anio_nom")) ;  
				ratificacion.setAnio(anio);
				return ratificacion;
			}

		});

	}	




	// funciones privadas utilitarias para Ratificacion

	private Ratificacion rsToEntity(ResultSet rs,String alias) throws SQLException {
		Ratificacion ratificacion = new Ratificacion();

		ratificacion.setId(rs.getInt( alias + "id"));
		ratificacion.setId_mat(rs.getInt( alias + "id_mat"));
		ratificacion.setId_anio_rat(rs.getInt( alias + "id_anio_rat"));
		ratificacion.setRes(rs.getString( alias + "res"));
		ratificacion.setEst(rs.getString( alias + "est"));
								
		return ratificacion;

	}
	
}
